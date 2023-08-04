package appli.model.domaine.compta.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.compta.bean.EcritureBean;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_ECRITURE;
import appli.model.domaine.administration.persistant.EcriturePersistant;
import appli.model.domaine.compta.dao.IEcritureDao;
import appli.model.domaine.compta.dao.IExerciceDao;
import appli.model.domaine.compta.service.ICompteService;
import appli.model.domaine.compta.service.IEcritureService;
import appli.model.domaine.compta.validator.EcritureValidator;
import framework.controller.ContextGloabalAppli;
import framework.model.beanContext.ExercicePersistant;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.common.annotation.validator.WorkModelMethodValidator;
import framework.model.common.util.BigDecimalUtil;
import framework.model.service.GenericJpaService;

@WorkModelClassValidator(validator=EcritureValidator.class)
@Named
public class EcritureService extends GenericJpaService<EcritureBean, Long> implements IEcritureService{
	@Inject
	IExerciceDao exerciceDao;
	@Inject
	IEcritureDao ecritureDao;
	@Inject
	ICompteService compteService;

	@WorkModelMethodValidator
	@Override
	@Transactional
	public void updateEcriture(List<EcriturePersistant> listEcritures, Long dbElementId) {
		EcritureBean ectBeanDb = findById(dbElementId);
		// Purge des anciennes écritures
		ecritureDao.getQuery("delete from EcriturePersistant where elementId=:elementId and source=:source and groupe=:group")
			.setParameter("elementId", ectBeanDb.getElementId())
			.setParameter("source", TYPE_ECRITURE.ECRITURE.toString())
			.setParameter("group", ectBeanDb.getGroupe())
			.executeUpdate();
		
		// Création
		createEcriture(listEcritures);
	}
	
	@WorkModelMethodValidator
	@Override
	@Transactional
	public void createEcriture(List<EcriturePersistant> listEcritures) {
		for (EcriturePersistant ecritureBean : listEcritures) {
			getEntityManager().merge(ecritureBean);
		}
	}

	@Override
	public BigDecimal getTotalByExcerciceAndCompte(ExercicePersistant exercicePersistant, String ... compteCodes) {
		return getTotalByExcerciceAndCompte(exercicePersistant, '0', compteCodes);
	}
	
	public BigDecimal getTotalByExcerciceAndCompte(ExercicePersistant exercicePersistant, char sens, boolean excludeCloture, String ... compteCodes) {
		if(exercicePersistant == null){
			return null;
		}

		//
		BigDecimal montant = BigDecimalUtil.ZERO;
		for(String compte : compteCodes){
			 BigDecimal solde = getTotalByExcerciceAndCompte(exercicePersistant, compte, excludeCloture);
			 solde = (solde == null ? BigDecimalUtil.ZERO : solde);
			 
			 if(sens=='0'){
				 montant = BigDecimalUtil.add(montant, solde);
			 } else if(sens == 'D' && solde.compareTo(BigDecimalUtil.ZERO) < 0){
				 montant = BigDecimalUtil.add(montant, solde);
			 } else if(sens == 'C' && solde.compareTo(BigDecimalUtil.ZERO) > 0){
				 montant = BigDecimalUtil.add(montant, solde);
			 }
		}
		
		return BigDecimalUtil.round(montant);
	}
	
	/* (non-Javadoc)
	 * @see org.metier.domaine.cmp.service.IEcritureService#getTotalByExcerciceAndCompte(org.metier.domaine.reg.persistant.ExercicePersistant, java.lang.String[])
	 */
	@Override
	public BigDecimal getTotalByExcerciceAndCompte(ExercicePersistant exercicePersistant, char sens, String ... compteCodes) {
		return getTotalByExcerciceAndCompte(exercicePersistant, sens, false, compteCodes);
	}
	
	/**
	 * @param exerciceId
	 * @param compteCode
	 * @return
	 */
	private BigDecimal getTotalByExcerciceAndCompte(ExercicePersistant exercicePersistant, String compteCode, boolean excludeCloture) {
		String query = "select SUM("
				+ "	case "
				+ "  when e.sens='D' then -e.montant "
				+ "	 else e.montant "
				+ "end"
				+ ") "
				+ "from EcriturePersistant e where "
				+ "and e.opc_compte.code like :compte_code "
				+ "and e.date >=:date_debut "
				+ "and e.date <=:date_fin ";
		
		if(excludeCloture){
			query = query + "and source!='"+TYPE_ECRITURE.CLOTURE.toString()+"' ";
		}
		
		return (BigDecimal) ecritureDao.getQuery(query)
												   .setParameter("compte_code", compteCode)
   												   .setParameter("date_debut", exercicePersistant.getDate_debut())
												   .setParameter("date_fin", exercicePersistant.getDate_fin())
												   .getSingleResult();
	}

	@Override
	public BigDecimal getSoldeComptable(String compteCode, Date dateDebut, Date dateFin, String sens) {
		String query = "select SUM(e.montant) from EcriturePersistant e "
				+ "where e.opc_compte.code like :compte_code "
				+ "and e.sens=:sens "
				+ "and e.date >=:date_debut "
				+ "and e.date <=:date_fin";
		
		return (BigDecimal) ecritureDao.getQuery(query)
												   .setParameter("compte_code", compteCode)
												   .setParameter("sens", sens)
												   .setParameter("date_debut", dateDebut)
												   .setParameter("date_fin", dateFin)
												   .getSingleResult();
	}

	@Override
	public BigDecimal getSoldeComptableFinExercice(String compteCode, ExercicePersistant exercice) {
		return ecritureDao.getSoldeComptableFinExercice(compteCode, exercice);
	}
	
	@Override
	public BigDecimal getSoldeComptable(String compteCode, Date dateDebut, Date dateFin) {
		return ecritureDao.getSoldeComptable(compteCode, dateDebut, dateFin);
	}

	@Override
	public List<EcriturePersistant> getListEcritureByOrigineAndType(Long origineId, TYPE_ECRITURE source) {
		return ecritureDao.getListEcritureByOrigineAndType(origineId, source);
	}

	@Override
	public List<EcriturePersistant> getListEcritureByExerciceAndCompte(ExercicePersistant exercicePersistant, String compteCode){
		return ecritureDao.getListEcritureByExerciceAndCompte(exercicePersistant, compteCode);
	}
	
	@Override
	@Transactional
	public void deleteByOrigineAndType(Long origineId, TYPE_ECRITURE source){
		List<EcriturePersistant> listEcriture = getListEcritureByOrigineAndType(origineId, source);
		for (EcriturePersistant ecriturePersistant : listEcriture) {
			delete(ecriturePersistant.getId());	
		}
	}
	
	@Override
	public List<EcriturePersistant> getListEcritureByExerciceAndGroupeAndType(ExercicePersistant exercicePersistant, Integer groupe, TYPE_ECRITURE source) {
		return ecritureDao.getListEcritureByExerciceAndGroupeAndType(exercicePersistant, groupe, source);
	}
	
	@Override
	public Integer getNextGroupParOrigine(TYPE_ECRITURE source, Long origineId){
		return ecritureDao.getNextGroupParOrigine(source, origineId);
	}

	@Override
	public List<EcriturePersistant> getListEcritureByOrigineAndTypeAndGroup(
			Long origineId, TYPE_ECRITURE type, Integer groupe) {
		return ecritureDao.getListEcritureByOrigineAndTypeAndGroup(origineId, type, groupe);
	}

	@Override
	public List<EcriturePersistant> getListEcritureByDates(Date dateDebut, Date dateFin) {
		return ecritureDao.getListEcritureByDates(dateDebut, dateFin);
	}
	
	//--------------------------------- Surcharge des méthode--------------------------------
	
	@WorkModelMethodValidator
	@Override
	@Transactional
	public EcritureBean update(EcritureBean e) {
		// On supprime puis on re-crée
		ExercicePersistant currentExercice = ContextGloabalAppli.getExerciceBean();
		List<EcriturePersistant> listEcriture = getListEcritureByExerciceAndGroupeAndType(currentExercice, e.getGroupe(), TYPE_ECRITURE.ECRITURE);
		for (EcriturePersistant ecriturePersistant : listEcriture) {
			delete(ecriturePersistant.getId());
		}

		create(e);
		
		return e;
	}

	@Override 
	public Map<String, BigDecimal> getSoldeComptableDetail(String compteCode, Date dateDebut, Date dateFin) {
		return ecritureDao.getSoldeComptableDetail(compteCode, dateDebut, dateFin);
	}

	@WorkModelMethodValidator
	@Override
	@Transactional
	public void delete_from_ecriture(Long ectId) {
		EcriturePersistant ecritureP = ecritureDao.findById(ectId);
		List<EcriturePersistant> listEct = getListEcritureByOrigineAndTypeAndGroup(ecritureP.getOpc_etablissement().getOpc_societe().getId(), TYPE_ECRITURE.ECRITURE, ecritureP.getGroupe());
		
		for (EcriturePersistant ecriturePersistant : listEct) {
			super.delete(ecriturePersistant.getId());
		}
	}
	
	
	
	
	
	
	
	@Override
	public BigDecimal getSoldeCompte(Long compteId){
		// Solde cloture
		List<EcriturePersistant> listSolde = getQuery("from EcriturePersistant "
				+ "where source='CLOTURE_ETAT' "
				+ "and opc_compte.id=:banqueId "
				+ "order by date_mouvement desc")
			.setParameter("banqueId", compteId)
			.getResultList();
		
		Long lastSoldeReportId = (listSolde.size()>0) ? listSolde.get(0).getId() : -1;
		
		String request = "select SUM(case when cp.sens='D' then -cp.montant "
				+ "	else cp.montant end) "
				+ " from EcriturePersistant cp where cp.opc_compte.id=:banque_id "
				+ " and id>=:lastSoldeReportId";
			
			return  (BigDecimal) getQuery(request)
					.setParameter("banque_id", compteId)
					.setParameter("lastSoldeReportId", lastSoldeReportId)
					.getSingleResult();
	}
	
	@Override
	public BigDecimal getSoldeCompte(Long compteId, Date dateFin) {
		// Solde cloture
		List<EcriturePersistant> listSolde = getQuery("from EcriturePersistant "
				+ "where source='CLOTURE_ETAT' and date_mouvement<=:maxDate "
				+ "and opc_compte.id=:banqueId "
				+ "order by date_mouvement desc")
			.setParameter("maxDate", dateFin)
			.setParameter("banqueId", compteId)
			.getResultList();
		
		Long lastSoldeReportId = (listSolde.size()>0) ? listSolde.get(0).getId() : -1;
		
		String request = "select SUM(case when cp.sens='D' then -cp.montant "
				+ "	else cp.montant end) "
				+ " from EcriturePersistant cp where cp.opc_compte.id=:banque_id "
				+ "and cp.date_mouvement<=:date_mouvement and cp.id>=:lastSoldeReportId";
		
		return  (BigDecimal) getQuery(request)
				.setParameter("banque_id", compteId)
				.setParameter("date_mouvement", dateFin)
				.setParameter("lastSoldeReportId", lastSoldeReportId)
				.getSingleResult();
	}

	@Override
	public BigDecimal getSoldeReportCompte(Long compteId, Date dateDebut) {
		// Solde cloture
		List<EcriturePersistant> listSolde = getQuery("from EcriturePersistant "
				+ "where source='CLOTURE_ETAT' and date_mouvement<=:maxDate "
				+ "and opc_compte.id=:banqueId "
				+ "order by date_mouvement desc")
			.setParameter("maxDate", dateDebut)
			.setParameter("banqueId", compteId)
			.getResultList();
		
		EcriturePersistant lastSoldeReportId = (listSolde.size()>0) ? listSolde.get(0) : null;
		
		return lastSoldeReportId==null ? BigDecimalUtil.ZERO : lastSoldeReportId.getMontant();
	}

	/**
	 * Utilisé depuis le tableux dans "/domaine/administration/compteEcriture_list.jsp"
	 * @return
	 */
	@Override
	public BigDecimal getTotalBanqueCaisse(Long banqueId, Date dateEcriture){
		BigDecimal result =  (BigDecimal) getSingleResult(
				getQuery("select SUM(case when sens='D' then -montant "
				+ "	else montant end) from EcriturePersistant where opc_compte.id=:banqueId and DATE(date_mouvement)>=:dtMvm and DATE(date_mouvement)<=:dtMvm"
				).setParameter("banqueId", banqueId)
				.setParameter("dtMvm", dateEcriture)
				);
		return result;
	}
}
