package appli.model.domaine.compta.dao.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import appli.controller.domaine.compta.bean.EcritureBean;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_ECRITURE;
import appli.model.domaine.administration.persistant.EcriturePersistant;
import appli.model.domaine.compta.dao.ICompteDao;
import appli.model.domaine.compta.dao.IEcritureDao;
import appli.model.domaine.compta.dao.IExerciceDao;
import framework.model.beanContext.ExercicePersistant;
import framework.model.util.GenericJpaDao;


@Named
public class EcritureDao extends GenericJpaDao<EcriturePersistant, Long> implements IEcritureDao{
	@Inject
	ICompteDao compteDao;
	@Inject
	IExerciceDao exerciceDao;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<EcriturePersistant> getListEcritureByOrigineAndType(Long origineId, TYPE_ECRITURE type) {
		String request = "from EcriturePersistant ecriture where ecriture.elementId=:elementId and "
				+ "ecriture.source=:type "
				+ "order by ecriture.date_mouvement, ecriture.id";
		
		return getQuery(request).setParameter("elementId", origineId)
						.setParameter("type", type.toString())
						.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<EcriturePersistant> getListEcritureByOrigineAndTypeAndGroup(Long origineId, TYPE_ECRITURE type, Integer groupe) {
		String request = "from EcriturePersistant ecriture where "
				+ "ecriture.elementId=:elementId "
				+ "and ecriture.source=:type and ecriture.groupe=:groupe "
				+ "order by ecriture.date_mouvement, ecriture.id";
		
		return getQuery(request).setParameter("elementId", origineId)
						.setParameter("type", type.toString())
						.setParameter("groupe", groupe)
						.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EcriturePersistant> getListEcritureByExerciceAndGroupeAndType(ExercicePersistant exercicePersistant, Integer groupe, TYPE_ECRITURE type) {
		String request = "from EcriturePersistant ecriture where "
				+ "ecriture.groupe=:groupe "
				+ "and ecriture.source=:type "
				+ "and ecriture.date_mouvement >=:date_debut "
				+ "and ecriture.date_mouvement <=:date_fin "
				+ "order by ecriture.date_mouvement, ecriture.id";
		
		return getQuery(request).setParameter("groupe", groupe)
					.setParameter("type", type.toString())
				    .setParameter("date_debut", exercicePersistant.getDate_debut())
				   	.setParameter("date_fin", exercicePersistant.getDate_fin())
						.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EcriturePersistant> getListEcritureByExerciceAndCompte(ExercicePersistant exercicePersistant, String compteCode) {
		String requete = "from EcriturePersistant e "
				+ "where e.date >=:date_debut "
				+ "and e.date <=:date_fin "
				+ "and e.opc_compte.code=:compteCode "
				+ "order by e.date, e.id";
		Query query = getQuery(requete).setParameter("compteCode", compteCode)
						    .setParameter("date_debut", exercicePersistant.getDate_debut())
						   	.setParameter("date_fin", exercicePersistant.getDate_fin());
		
		return query.getResultList();
	}
	
	@Override
	public Integer getNextGroupParOrigine(TYPE_ECRITURE type, Long origineId, EntityManager entityManager) {
		String requete = "select max (groupe) from EcriturePersistant e "
				+ "where e.source=:type ";

	   if(origineId == null){
			requete = requete + " and e.elementId is null";
		} else{
			requete = requete + " and e.elementId=:origineId";
		}
		
		Query query = getQuery(entityManager, requete).setParameter("type", type.toString());
		
		if(origineId != null){
			query.setParameter("origineId", origineId);
		}
		Integer max = (Integer)getSingleResult(query);
		
		return (max==null) ? 1 : (max+1);
	}
	
	@Override
	public Integer getNextGroupParOrigine(TYPE_ECRITURE type, Long origineId) {
		return getNextGroupParOrigine(type, origineId, getEntityManager());
	}
	
	@Override
	public void create(EcriturePersistant ecritureBean) {
		super.create(ecritureBean);
	}

	@Override
	public BigDecimal getSoldeComptable(String compteCode, Date dateDebut, Date dateFin, String sens) {
		String request = null;
		
		if(sens == null){
			request = "select SUM("
				+ "	case "
				+ "  when e.sens='D' then -e.montant "
				+ "	 else e.montant "
				+ "end"
				+ ") ";
		} else{
			request = "select SUM(montant) ";
		}
		request = request + " from EcriturePersistant e where "
			    + "e.opc_compte.code like :compte_code "
				+ "and e.date >=:dateDebut "
				+ "and e.date <=:dateFin ";
		
		if(sens != null){
			request = request + " and e.sens='"+sens+"'"; 	
		}

		Query query = getQuery(request);

		return (BigDecimal) getSingleResult(
				query.setParameter("dateDebut", dateDebut)
			    .setParameter("dateFin", dateFin)
				.setParameter("compte_code", compteCode));
	}
	
	@Override
	public BigDecimal getSoldeComptable(String compteCode, Date dateDebut, Date dateFin) {
		String request = "select SUM("
				+ "	case "
				+ "  when e.sens='D' then -e.montant "
				+ "	 else e.montant "
				+ "end"
				+ ") "
				+ "from EcriturePersistant e where "
			    + "e.opc_compte.code like :compte_code "
				+ "and e.date >=:dateDebut "
				+ "and e.date <=:dateFin ";
		Query query = getQuery(request);

		return (BigDecimal) getSingleResult(
				query.setParameter("dateDebut", dateDebut)
			    .setParameter("dateFin", dateFin)
				.setParameter("compte_code", compteCode));
	}
		
	@Override
	public BigDecimal getSoldeComptableFinExercice(String compteCode, ExercicePersistant exercice) {
		String request = "select SUM("
				+ "	case "
				+ "  when e.sens='D' then -e.montant "
				+ "	 else e.montant "
				+ "end"
				+ ") "
				+ "from EcriturePersistant e where "
			    + "e.opc_compte.code like :compte_code "
				+ "and e.date >=:dateDebut "
				+ "and e.date <=:dateFin "
				+ "and source!='"+TYPE_ECRITURE.CLOTURE.toString()+"' "
				+ "and (e.categorie is null or e.categorie!='"+EcritureBean.CATEGORIE_ECRITURE.CCAV.toString()+"') "
				+ "and (e.categorie is null or e.categorie!='"+EcritureBean.CATEGORIE_ECRITURE.PCAV.toString()+"')";
		
		Query query = getQuery(request);

		return (BigDecimal) getSingleResult(
				query.setParameter("dateDebut", exercice.getDate_debut())
			    .setParameter("dateFin", exercice.getDate_fin())
				.setParameter("compte_code", compteCode));
	}

	@Override
	public Map<String, BigDecimal> getSoldeComptableDetail(String compteCode, Date dateDebut, Date dateFin){
		Map<String, BigDecimal> mapCompteSolde = new HashMap<String, BigDecimal>();
		
		String request = "select e.opc_compte.code, SUM("
				+ "	case "
				+ "  when e.sens='D' then -e.montant "
				+ "	 else e.montant "
				+ "end"
				+ ") "
				+ "from EcriturePersistant e where "
				+ "and e.date >=:dateDebut "
				+ "and e.date <=:dateFin ";
		
		if(compteCode != null){
			request = request + "and e.opc_compte.code like :compte_code ";
		}
		request = request + "group by e.opc_compte.code";
		
		Query query = getQuery(request);
		if(compteCode != null){
			query.setParameter("compte_code", compteCode+"%");
		}
		@SuppressWarnings("unchecked")
		List<Object[]> result  = 
				query.setParameter("dateDebut", dateDebut)
			    .setParameter("dateFin", dateFin)
				.getResultList();
		
		for (Object[] data : result) {
			mapCompteSolde.put(((String)data[0]), (BigDecimal)data[1]);
		}
		
		return mapCompteSolde;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EcriturePersistant> getListEcritureByExerciceAndCompteAndType(ExercicePersistant exercicePersistant, String compteCode, TYPE_ECRITURE type) {
		String requete = "from EcriturePersistant e "
				+ "where e.date_mouvement >=:date_debut "
				+ "and e.date_mouvement <=:date_fin "
				+ "and e.opc_compte.code=:compteCode "
				+ "and e.source=:type "
				+ "order by e.date, e.id";
		
		Query query = getQuery(requete).setParameter("compteCode", compteCode)
						    .setParameter("date_debut", exercicePersistant.getDate_debut())
						   	.setParameter("date_fin", exercicePersistant.getDate_fin())
						   	.setParameter("type", type.toString());
		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EcriturePersistant> getListEcritureByDates(Date dateDebut, Date dateFin) {
		String request = "from EcriturePersistant e where "
				+ "e.date_mouvement >=:date_debut "
				+ "and e.date_mouvement <=:date_fin "
				+ "order by e.date_mouvement, e.id";
		
		return getQuery(request).setParameter("date_debut", dateDebut)
			   			.setParameter("date_fin", dateFin)
						.getResultList();	
		}
	}
