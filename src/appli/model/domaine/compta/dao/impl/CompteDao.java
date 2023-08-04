package appli.model.domaine.compta.dao.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.compta.bean.CompteBean;
import appli.model.domaine.compta.dao.ICompteDao;
import appli.model.domaine.stock.persistant.FamillePersistant;
import framework.controller.Context;
import framework.controller.bean.NodeBean;
import framework.model.beanContext.ComptePersistant;
import framework.model.common.annotation.validator.WorkModelMethodValidator;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.tree.ElementBase;
import framework.model.common.util.tree.TreeService;
import framework.model.util.GenericJpaDao;


@Named
public class CompteDao extends GenericJpaDao<ComptePersistant, Long> implements ICompteDao{
	@Override
	@Transactional
	@WorkModelMethodValidator
	public void createCompte(CompteBean compte) {
		Long parentId = compte.getElement_id();
		if(parentId == null){
			parentId = compte.getParent_id();
		}
		ComptePersistant parentCompte = findById(parentId);
		
		if(compte.getClasse() == null){
			compte.setClasse(parentCompte.getClasse());
		}
		if(compte.getRubrique() == null){
			compte.setRubrique(parentCompte.getRubrique());
		}
		int right = parentCompte.getB_right();
		// Update
		updateSuccessifElements(2, right, "+");

		ComptePersistant compteP = new ComptePersistant();
		
		ReflectUtil.copyProperties(compteP, compte);
		compteP.setLibelle(compte.getLibelle());
		compteP.setB_left(right);
		compteP.setB_right(right + 1);
		compteP.setLevel(parentCompte.getLevel() + 1);
		compteP.setDate_maj(new Date());
		compteP.setSignature(Context.getUserLogin());
		
		// Ajouter le nouvel élément � l'arbre
		compteP = getEntityManager().merge(compteP);
		
		// Copier les propriété de l'entité
		ReflectUtil.copyProperties(compte, compteP);
	}

	@Override
	@Transactional
	@WorkModelMethodValidator
	public void deleteCompte(Long id) {
		ComptePersistant comptePersistant = findById(id);
		int b_right = comptePersistant.getB_right();
		int b_left = comptePersistant.getB_left();

		// Update elements-----------------------------------------------------
		int decal = 2;

		if (b_right - b_left != 1) {
			decal = b_right - b_left + 1;
		}

		// Delete elements from DB
		Query deleteQuery = getQuery(
				"delete from ComptePersistant compte where compte.b_left>=:left and compte.b_right<=:right")
				.setParameter("left", b_left).setParameter("right", b_right);

		deleteQuery.executeUpdate();
		getEntityManager().flush();

		// Update
		updateSuccessifElements(decal, comptePersistant.getB_right(), "-");
	}

	/**
	 * Mise à jour des bornes à partir des éléments impactés
	 * 
	 * @param decal
	 * @param right
	 */
	@Transactional
	private void updateSuccessifElements(int decal, int right, String sens) {
		// Update right
		Query updateRightQuery = getQuery(
				"update ComptePersistant set b_right=b_right" + sens
						+ ":decal " + "where b_right>=:right")
				.setParameter("decal", decal).setParameter("right", right);
		updateRightQuery.executeUpdate();
		// Update left
		Query updateLeftQuery = getQuery(
				"update ComptePersistant set b_left=b_left" + sens + ":decal "
						+ "where b_left>=:right")
				.setParameter("decal", decal).setParameter("right", right);
		updateLeftQuery.executeUpdate();
	}

	@Override
	@Transactional
	@WorkModelMethodValidator
	public void updateCompte(CompteBean compteBean) {
		ComptePersistant compteDb = findById(compteBean.getId());
		
		
		 // Vérifier si le parent à changé --> déplacement
		ComptePersistant compteParentDB = getCompteParent(compteDb);
		if (!compteBean.getParent_id().equals(compteParentDB.getId())) {
			ComptePersistant compteTarget = findById(compteBean.getParent_id());
			mouveElement(compteDb, compteTarget);
		}
		 
		compteDb = findById(compteBean.getId()); 
		//
		compteDb.setCode(compteBean.getCode());
		compteDb.setLibelle(compteBean.getLibelle());
			
		getEntityManager().merge(compteDb);
	}
	
	@Transactional
	@Override
	public void changerOrdre(Map<String, Object> mapOrder) {
		EntityManager entityManager = getEntityManager();
		List<CompteBean> listCompte = getQuery("from ComptePersistant").getResultList();
		for (ComptePersistant mcP : listCompte) {
			Object idxOrder = mapOrder.get(mcP.getId().toString());
			if(idxOrder != null) {
				mcP.setIdx_order(Integer.valueOf(""+idxOrder));
			}
		}
		
		NodeBean nb = new NodeBean();
		nb.setBleft("b_left");
		nb.setBright("b_right");
		nb.setId("id");
		nb.setLabel("libelle");
		nb.setLevel("level");
		nb.setSort("idx_order");
		
		TreeService ts = new TreeService(listCompte, nb);
		ts.sortTreeByOrderIdx();
		
		List<ElementBase> listElements = ts.getListElement();
		
		for (ElementBase elementBase : listElements) {
			ComptePersistant mnuP = findById((Long)elementBase.getE_id());
			if(!mnuP.getB_left().equals(elementBase.getE_left()) || !mnuP.getB_right().equals(elementBase.getE_right())
					|| !mnuP.getLevel().equals(elementBase.getE_level())) {
				mnuP.setB_left(elementBase.getE_left());
				mnuP.setB_right(elementBase.getE_right());
				mnuP.setLevel(elementBase.getE_level());
				//
				entityManager.merge(mnuP);
			}
		}
	}

	/**
	 * @param compteP
	 * @param compteTarget
	 */
	private void mouveElement(ComptePersistant compteP, ComptePersistant compteTargetP) {
		EntityManager entityManager = getEntityManager();
		List<FamillePersistant> listFamille = getQuery("from "+compteP.getClass().getSimpleName()).getResultList();
		NodeBean nb = new NodeBean();
		nb.setBleft("b_left");
		nb.setBright("b_right");
		nb.setLevel("level");
		nb.setId("id");
		nb.setLabel("libelle");
		
		TreeService ts = new TreeService(listFamille, nb);
		ts.moveElement(compteP.getId(), compteTargetP.getId());
		
		List<ElementBase> listElements = ts.getListElement();
		
		for (ElementBase elementBase : listElements) {
			ComptePersistant mnuP = findById((Long)elementBase.getE_id());
			if(!mnuP.getB_left().equals(elementBase.getE_left()) || !mnuP.getB_right().equals(elementBase.getE_right())
					|| !mnuP.getLevel().equals(elementBase.getE_level())) {
				mnuP.setB_left(elementBase.getE_left());
				mnuP.setB_right(elementBase.getE_right());
				mnuP.setLevel(elementBase.getE_level());
				//
				entityManager.merge(mnuP);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ComptePersistant> getPlanComptabe(Long exerciceId, boolean ignoreRoot) {
		String request = "from ComptePersistant compte where opc_exercice.id:=exeId ";
		
		if(ignoreRoot){
			request = request + "and code!='ROOT' ";
		}
		request = request + " order by compte.code";
		
		return getQuery(request)
				.setParameter("exeId", exerciceId)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ComptePersistant> getPlanComptabe(boolean ignoreRoot) {
		String request = "from ComptePersistant compte where 1=1 ";
		
		if(ignoreRoot){
			request = request + "and code!='ROOT' ";
		}
		request = request + " order by compte.code";
		
		return getQuery(request).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ComptePersistant> getListCompteByCode(String code) {
		String request = null;
		if(code == null){
			request = "from ComptePersistant compte order by compte.b_left";
		} else if(code.indexOf("%") == -1){
			throw new RuntimeException("Il manque le % dans le code du compte !");
		} else{
			request = "from ComptePersistant compte where compte.code like:code order by compte.b_left";
		}
		
		Query query = getQuery(request);
		
		if(code != null){
			query.setParameter("code", code);
		}
		return query.getResultList();
	}

	@Override
	public String getMaxCodeCompte(ComptePersistant parentCompte, int taille) {
		String requete = "select max (compte.code) from ComptePersistant compte "
				+ "where compte.code like :code "
				+ " and length(trim(compte.code))=:taille ";
		
		Query query = getQuery(requete)
				.setParameter("code", parentCompte.getCode()+"%")
				.setParameter("taille", taille);
		// Si pas d'enfant alors le code lui même est le max trouvé
		String maxCode = (String) getSingleResult(query);

		return maxCode == null ? parentCompte.getCode() : maxCode;
	}

	@Override
	public BigDecimal getMontantInitialisation(String ... compte_code){
		BigDecimal result = BigDecimalUtil.ZERO;
		
		for(String code : compte_code){
			String request = "select SUM(case when cp.sens='D' then -cp.montant_init "
				+ "	else cp.montant_init end) "
				+ " from ComptePersistant cp where cp.code like :compte_code";
			Query query = getQuery(request)
					.setParameter("compte_code", code+"%");
			
			BigDecimal solde = (BigDecimal)getSingleResult(query);
			solde = (solde == null) ? BigDecimalUtil.ZERO : solde;
			
			result = BigDecimalUtil.add(result, solde);
		}
		
		return result;
	}
	
	@Override
	public BigDecimal getMontantInitialisation(char sens, String ... compte_code){
		BigDecimal result = BigDecimalUtil.ZERO;
		for(String codeCompte : compte_code){
			String request = "select SUM(cp.montant_init) "
				+ " from ComptePersistant cp where cp.code like :compte_code"
				+ " and cp.sens='"+sens+"'";
			Query query = getQuery(request)
					.setParameter("compte_code", codeCompte);
			
			BigDecimal solde = (BigDecimal)getSingleResult(query);
			solde = (solde == null) ? BigDecimalUtil.ZERO : solde;
			
			if(sens == 'D'/* && solde.compareTo(BigDecimalUtil.ZERO) < 0*/){
				result = BigDecimalUtil.add(result, solde);
			} else if(sens == 'C'/* && solde.compareTo(BigDecimalUtil.ZERO) > 0*/){
				result = BigDecimalUtil.add(result, solde);
			}
		}
		
		return result;
	}
	
	//------------------------------------------------ Gestion de l'arbre ------------------------------------------

	@SuppressWarnings("unchecked")
	@Override
	public List<ComptePersistant> getListCompteChargeRecuperable(boolean isRecuperable) {
		List<ComptePersistant> listCompte = null;
		if(isRecuperable){
			listCompte = getQuery("from ComptePersistant compte where compte.taux_recuperation is not null "
					+ "and compte.taux_recuperation!=0 "
					+ "and compte.code like :code "
					+ "order by compte.code")
				.setParameter("code", "6%")
				.getResultList();
		} else{
			listCompte = getQuery("from ComptePersistant compte where (compte.taux_recuperation is null or compte.taux_recuperation=0) "
				+ "and compte.code like :code "
				+ "order by compte.code")
				.setParameter("code", "6%")
				.getResultList();
		}
		
		return listCompte;	
	}
	
	@Override
	public ComptePersistant getCompteByCode(String code){
		Query query = getQuery("from ComptePersistant compte where compte.code=:code")
				.setParameter("code", code);
		return (ComptePersistant) getSingleResult(query);
	}
	
	 @Override
    public List<ComptePersistant> getListeCompteEnfants(Long parentId) {
       ComptePersistant parentFamille = findById(parentId);
        List<ComptePersistant> listCompte = null;
        
        String req = "from ComptePersistant "
                + "where b_left>:bLeft and b_right<:bRight "
                + "and level=:level";
        req = req + " order by b_left";
        
        // Familles filles
        if(parentFamille.getB_right()-parentFamille.getB_left() > 1){
            listCompte = getQuery(req)
                .setParameter("bLeft", parentFamille.getB_left())
                .setParameter("bRight", parentFamille.getB_right())
                .setParameter("level", parentFamille.getLevel()+1)
                .getResultList();
        }
        
        return listCompte;
    }
    @Override
    public ComptePersistant getCompteParent(Long compteId) {
        return getCompteParent(findById(compteId));
    }
    
    @SuppressWarnings("unchecked")
	@Override
	public ComptePersistant getCompteParent(ComptePersistant compte) {
		// On cherche le parent dans l'ancien exercice
		List<ComptePersistant> listComptes = getQuery(
				"from ComptePersistant cpt where "
						+ "cpt.b_left<:left and cpt.b_right>:right "
						+ "order by cpt.b_left desc ")
				.setParameter("left", compte.getB_left())
				.setParameter("right", compte.getB_right())
				.getResultList();

		for (ComptePersistant comptePersistant : listComptes) {
			return comptePersistant;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ComptePersistant getParentCompte(ComptePersistant oldCompte){
		// On cherche le parent dans l'ancien exercice
		List<ComptePersistant> listCompte = getQuery("from ComptePersistant cpt "
				+ " where "
				+ "cpt.b_left<:left and cpt.b_right>:right "
				+ "order by cpt.b_left desc ")
				.setParameter("left", oldCompte.getB_left())
				.setParameter("right", oldCompte.getB_right())
				.getResultList();
		
		ComptePersistant compteParent = null;
				
		for (ComptePersistant comptePersistant : listCompte) {
			Query query = getQuery("from ComptePersistant cpt "
					+ " where "
					+ "cpt.code=:code ")
					.setParameter("code", comptePersistant.getCode());
			
			compteParent = (ComptePersistant)getSingleResult(query);
			if(compteParent != null){
				break;
			}

		}
		return compteParent;
	}
}
