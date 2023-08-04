package appli.model.domaine.stock.dao.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.stock.bean.FamilleBean;
import appli.model.domaine.personnel.persistant.FamilleEmployePersistant;
import appli.model.domaine.stock.dao.IFamilleDao;
import appli.model.domaine.stock.persistant.FamilleConsommationPersistant;
import appli.model.domaine.stock.persistant.FamilleCuisinePersistant;
import appli.model.domaine.stock.persistant.FamilleFournisseurPersistant;
import appli.model.domaine.stock.persistant.FamilleMenuPersistant;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.stock.persistant.FamilleStockPersistant;
import appli.model.domaine.stock.validator.FamilleValidator;
import framework.controller.Context;
import framework.controller.bean.NodeBean;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.common.annotation.validator.WorkModelMethodValidator;
import framework.model.common.service.MessageService;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.tree.ElementBase;
import framework.model.common.util.tree.TreeService;
import framework.model.util.GenericJpaDao;

@Named
@WorkModelClassValidator(validator = FamilleValidator.class)
public class FamilleDao extends GenericJpaDao<FamillePersistant, Long> implements IFamilleDao {
	
	@Override
	public FamillePersistant getFamilleRoot(String type) {
		String entity = null;
		if( type.equals("CU")){
			entity = FamilleCuisinePersistant.class.getSimpleName();
		} else if( type.equals("FO")){
			entity = FamilleFournisseurPersistant.class.getSimpleName();
		} else if( type.equals("ST")){
			entity = FamilleStockPersistant.class.getSimpleName();
		} else if( type.equals("CO")){
			entity = FamilleConsommationPersistant.class.getSimpleName();
		} else if( type.equals("MNU")){
			entity = FamilleMenuPersistant.class.getSimpleName();
		} else if( type.equals("EM")){
			entity = FamilleEmployePersistant.class.getSimpleName();
		}
		
		return (FamillePersistant) getQuery("from "+entity+" where code=:code")
			.setParameter("code", "ROOT")
			.getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public FamillePersistant getFamilleParent(FamillePersistant famille){
		List<FamillePersistant> listFamille = getQuery("from FamillePersistant"
                + " where b_left<:bLeft and b_right>:bRight and code!='ROOT' "
                + " and type=:type"
                + " order by b_left desc")
                .setParameter("bLeft", famille.getB_left())
                .setParameter("bRight", famille.getB_right())
                .setParameter("type", famille.getType())
                .getResultList();
		
		FamillePersistant familleP = (listFamille.size() > 0) ? listFamille.get(0) : null;
		
		return (familleP == null) ? getFamilleRoot(famille.getType()) : familleP;
	}

	@Override
	@Transactional
	@WorkModelMethodValidator
	public void createFamille(FamilleBean famille) {
		FamillePersistant parentCompte = findById(famille.getElement_id());

		int right = parentCompte.getB_right();
		// Update
		updateSuccessifElements(2, right, "+", famille.getType());

		FamillePersistant familleP = null;
		switch (famille.getType()) {
			case "ST": {familleP = new FamilleStockPersistant();}; break;
			case "FO": {familleP = new FamilleFournisseurPersistant();}; break;
			case "CU": {familleP = new FamilleCuisinePersistant();}; break;
			case "CO": {familleP = new FamilleConsommationPersistant();}; break;
			case "MNU": {familleP = new FamilleMenuPersistant();}; break;
			case "CM" : {
				try {
					familleP = (FamillePersistant) Class.forName("appli.model.domaine.copro.persistant.FamilleCoproCompositionPersistant").newInstance();
				} catch (Exception e) {
					e.printStackTrace();
				}			
			}; break;
			case "EM": {familleP = new FamilleEmployePersistant();}; break;
			case "SP": {
				try {
					familleP = (FamillePersistant) Class.forName("appli.model.domaine.agenda.persistant.FamilleAgendaPersistant").newInstance();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}; break;
			case "TA": {
				try {
					familleP = (FamillePersistant) Class.forName("appli.model.domaine.pointage.persistant.FamilleTachePersistant").newInstance();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}; break;
		}
		
		// Insert new sheet
		ReflectUtil.copyProperties(familleP, famille);
		familleP.setLibelle(famille.getLibelle());
		
		if(famille.getType().equals("SP")) {
			ReflectUtil.setProperty(familleP, "durree", ReflectUtil.getObjectPropertieValue(famille, "durree"));
			ReflectUtil.setProperty(familleP, "consigne_avant", ReflectUtil.getObjectPropertieValue(famille, "consigne_avant"));
			ReflectUtil.setProperty(familleP, "consigne_apres", ReflectUtil.getObjectPropertieValue(famille, "consigne_apres"));
			ReflectUtil.setProperty(familleP, "mtt_tarif", ReflectUtil.getObjectPropertieValue(famille, "mtt_tarif"));
		} else if(famille.getType().equals("CU")) {
			familleP.setCaisse_target(famille.getCaisse_target());			
		}
		
		familleP.setB_left(right);
		familleP.setB_right(right + 1);
		familleP.setLevel(parentCompte.getLevel() + 1);
		familleP.setDate_maj(new Date());
		familleP.setSignature(Context.getUserLogin());

		// Ajouter le nouvel élément � l'arbre
		familleP = getEntityManager().merge(familleP);
		
		// Copier les propriété de l'entité
		ReflectUtil.copyProperties(famille, familleP);
	}
	
	@Override
	@Transactional
	@WorkModelMethodValidator
	public void updateFamille(FamilleBean familleBean) {
		FamillePersistant familleDb = findById(familleBean.getId());
		
		// Vérifier si le parent à changé --> déplacement--------------------------------
		FamillePersistant familleParentDB = getFamilleParent(familleDb);
		
		if (!familleBean.getParent_id().equals(familleParentDB.getId())) {
			FamillePersistant familleTarget = findById(familleBean.getParent_id());
			
			// Ajouter contrôle si target n'est pas un enfant
			Integer targetLeft = (Integer) ReflectUtil.getObjectPropertieValue(familleTarget, "b_left");
			Integer targetRight = (Integer) ReflectUtil.getObjectPropertieValue(familleTarget, "b_right");
			Integer origineLeft = (Integer) ReflectUtil.getObjectPropertieValue(familleDb, "b_left");
			Integer origineRight = (Integer) ReflectUtil.getObjectPropertieValue(familleDb, "b_right");
			if(targetLeft > origineLeft  && targetRight < origineRight) {
				MessageService.addBannerMessage("Impossible de déplacer un répertoire vers son enfant.");
				return;
			}
						
			mouveElement(familleDb, familleTarget);
		}
		//------------------------------------------------------------------------------
		familleDb = findById(familleBean.getId());
		
		if(familleDb.getType().equals("SP")) {
			ReflectUtil.setProperty(familleDb, "durree", ReflectUtil.getObjectPropertieValue(familleBean, "durree"));
			ReflectUtil.setProperty(familleDb, "consigne_avant", ReflectUtil.getObjectPropertieValue(familleBean, "consigne_avant"));
			ReflectUtil.setProperty(familleDb, "consigne_apres", ReflectUtil.getObjectPropertieValue(familleBean, "consigne_apres"));
			ReflectUtil.setProperty(familleDb, "mtt_tarif", ReflectUtil.getObjectPropertieValue(familleBean, "mtt_tarif"));
		} else if(familleDb.getType().equals("CU")) {
			familleDb.setCaisse_target(familleBean.getCaisse_target());
			familleDb.setIs_noncaisse(familleBean.getIs_noncaisse());
		}
		
		//
		familleDb.setNbrPersonne(familleBean.getNbrPersonne());
		familleDb.setCode(familleBean.getCode());
		familleDb.setLibelle(familleBean.getLibelle());
		familleDb.setDate_maj(new Date());
		
		getEntityManager().merge(familleDb);
	}

	@Override
	@Transactional
	@WorkModelMethodValidator
	public void deleteFamille(Long id) {
		FamillePersistant famillePersistant = findById(id);
		String code = (String) ReflectUtil.getObjectPropertieValue(famillePersistant, "code");
		
		if("ROOT".equals(code)) {
			MessageService.addBannerMessage("La racine de l'arborescence ne peut pas être supprimée.");
			return;
		}
		
		int b_right = famillePersistant.getB_right();
		int b_left = famillePersistant.getB_left();

		// Update elements-----------------------------------------------------
		int decal = 2;

		if (b_right - b_left != 1) {
			decal = b_right - b_left + 1;
		}

		// Delete elements from DB
		Query deleteQuery = getQuery(
				"delete from FamillePersistant compte where compte.b_left>=:left and compte.b_right<=:right"
				+ " and type=:type")
				.setParameter("left", b_left).setParameter("right", b_right)
				.setParameter("type", famillePersistant.getType());

		deleteQuery.executeUpdate();
		getEntityManager().flush();

		// Update
		updateSuccessifElements(decal, famillePersistant.getB_right(), "-", famillePersistant.getType());
	}

	/**
	 * Mise à jour des bornes à partir des éléments impactés
	 * 
	 * @param decal
	 * @param right
	 */
	@Transactional
	private void updateSuccessifElements(int decal, int right, String sens, String type) {
		// Update right
		Query updateRightQuery = getQuery(
				"update FamillePersistant set b_right=b_right" + sens
						+ ":decal " + "where b_right>=:right and type=:type")
				.setParameter("decal", decal).setParameter("right", right)
				.setParameter("type", type);
		updateRightQuery.executeUpdate();
		// Update left
		Query updateLeftQuery = getQuery(
				"update FamillePersistant set b_left=b_left" + sens + ":decal "
						+ "where b_left>=:right and type=:type")
				.setParameter("decal", decal).setParameter("right", right)
				.setParameter("type", type);
		updateLeftQuery.executeUpdate();
	}

	/**
	 * @param familleP
	 * @param familleTarget
	 */
	@Transactional
	private void mouveElement(FamillePersistant familleP, FamillePersistant familleTargetP) {
		EntityManager entityManager = getEntityManager();
		List<FamillePersistant> listFamille = getQuery("from "+familleP.getClass().getSimpleName()+" where type=:type order by b_left")
				.setParameter("type", familleP.getType())
				.getResultList();
		NodeBean nb = new NodeBean();
		nb.setBleft("b_left");
		nb.setBright("b_right");
		nb.setId("id");
		nb.setLabel("libelle");
		nb.setLevel("level");
		
 		TreeService ts = new TreeService(listFamille, nb);
		ts.moveElement(familleP.getId(), familleTargetP.getId());
		
		List<ElementBase> listElements = ts.getListElement();
		
		for (ElementBase elementBase : listElements) {
			FamillePersistant famP = findById((Long)elementBase.getE_id());
			if(!famP.getB_left().equals(elementBase.getE_left()) || !famP.getB_right().equals(elementBase.getE_right())
					|| !famP.getLevel().equals(elementBase.getE_level())) {
				famP.setB_left(elementBase.getE_left());
				famP.setB_right(elementBase.getE_right());
				famP.setLevel(elementBase.getE_level());
				//
				entityManager.merge(famP);
			}
		}
	}
	
	@Transactional
	@Override
	public void changerOrdre(Map<String, Object> mapOrder, String type) {
		EntityManager entityManager = getEntityManager();
		List<FamillePersistant> listFamille = getQuery("from FamillePersistant where type=:type order by b_left")
				.setParameter("type", type)
				.getResultList();
		for (FamillePersistant famP : listFamille) {
			Object idxOrder = mapOrder.get(famP.getId().toString());
			if(idxOrder != null) {
				famP.setIdx_order(Integer.valueOf(""+idxOrder));
			}
		}
		
		NodeBean nb = new NodeBean();
		nb.setBleft("b_left");
		nb.setBright("b_right");
		nb.setId("id");
		nb.setLabel("libelle");
		nb.setLevel("level");
		nb.setSort("idx_order");
		
		TreeService ts = new TreeService(listFamille, nb);
		ts.sortTreeByOrderIdx();
		
		List<ElementBase> listElements = ts.getListElement();
		
		for (ElementBase elementBase : listElements) {
			FamillePersistant famP = findById((Long)elementBase.getE_id());
			if(!famP.getB_left().equals(elementBase.getE_left()) || !famP.getB_right().equals(elementBase.getE_right())
					|| !famP.getLevel().equals(elementBase.getE_level())) {
				famP.setB_left(elementBase.getE_left());
				famP.setB_right(elementBase.getE_right());
				famP.setLevel(elementBase.getE_level());
				//
				entityManager.merge(famP);
			}
		}
	}

	/**
	 * 
	 */
	@Override
	public String getNextCode(Long elementId, String type) {
		FamillePersistant parentFam = findById(elementId);
		boolean isPrentRoot = parentFam.getCode().equals("ROOT");
		int parentLength = (isPrentRoot ? 0 : parentFam.getCode().length());
		
		Query query = getNativeQuery("select  "
				+ "max("
				+ "	case when LENGTH(code) > "+parentLength+" then CAST(SUBSTR(code, "+(parentLength+1)+") AS UNSIGNED) "
				+ " else 0 end "
				+ ") from famille "
				+ "where type=:type and b_left>:left and b_right<:right and level=:level") 
						.setParameter("type", type)
						.setParameter("left", parentFam.getB_left())
						.setParameter("right", parentFam.getB_right())
						.setParameter("level", parentFam.getLevel()+1);
				
		BigDecimal max_num = (BigDecimal)query.getSingleResult();
		
		String nextCode = "1";
		if(max_num != null){
			nextCode = ""+(max_num.intValue() + 1);
		}
		
		return (isPrentRoot ? ""+nextCode : parentFam.getCode()+nextCode);
	}
}
