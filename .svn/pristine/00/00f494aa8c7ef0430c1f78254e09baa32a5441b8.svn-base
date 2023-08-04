package appli.model.domaine.compta.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.administration.bean.CompteBancaireBean;
import appli.controller.domaine.administration.bean.EtatFinanceBean;
import appli.model.domaine.administration.persistant.EcriturePersistant;
import appli.model.domaine.administration.service.ICompteBancaireService;
import appli.model.domaine.administration.service.IEtatFinanceService;
import appli.model.domaine.compta.dao.IExerciceDao;
import appli.model.domaine.compta.service.IExerciceService;
import appli.model.domaine.compta.validator.ExerciceValidator;
import framework.controller.ContextGloabalAppli.STATUT_EXERCICE;
import framework.model.beanContext.CompteBancairePersistant;
import framework.model.beanContext.ExerciceBean;
import framework.model.beanContext.ExercicePersistant;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.common.annotation.validator.WorkModelMethodValidator;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.DateUtil.TIME_ENUM;
import framework.model.service.GenericJpaService;

@Named
@WorkModelClassValidator(validator =  ExerciceValidator.class)
public class ExerciceService extends GenericJpaService<ExerciceBean, Long> implements IExerciceService{
	@Inject
	private IExerciceDao exerciceDao;
	@Inject
	private ICompteBancaireService compteBancaireService;
	@Inject
	private IEtatFinanceService etatFinanceService;
//	@Inject
//	private ICompteDao private ;

	@Override
	public boolean is_exercice_date_ouvert(Date date){
		ExercicePersistant exercice = exerciceDao.getExerciceByDate(date, date);
		
		if(exercice == null){
			return true;
		}
		if(exercice.getStatut_cloture().equals(STATUT_EXERCICE.OUVERT.getStatut())){
			return true;
		}
		
		return false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ExercicePersistant getActifExercice() {
		List<ExercicePersistant> listExercice = getQuery("from ExercicePersistant where etat=:etat order by date_fin desc")
			.setParameter("etat", "O")
			.getResultList();
		
		return (listExercice.size() > 0 ? listExercice.get(0) : null);
	}
	
	@Override
	@Transactional
	public void create(ExerciceBean exerciceBean) {
		// Si c'est le prmier exercicei à créer on met à jour alors le plan comptable avec la dépensdance vers cet exercice
		List<ExercicePersistant> listExercice = exerciceDao.findAll();
		if(listExercice.size() == 0){
			// Si c'est premier alors d'office ouvert
			exerciceBean.setStatut_cloture(STATUT_EXERCICE.OUVERT.getStatut());
		} 
		// Controlle des dates car on ne peut pas les modifier par la suite
		else{
			Date dateDebut = getMaxDateFinPlus1();
			Calendar calendar = Calendar.getInstance();
			// Ajouter un an pour avoir la date de fin
			calendar.setTime(dateDebut);
			calendar.add(Calendar.YEAR, 1);
			Date dateFin = calendar.getTime();
			exerciceBean.setDate_debut(dateDebut);
			exerciceBean.setDate_fin(dateFin);
		}
		
		super.create(exerciceBean);
	}
	
	/* (non-Javadoc)
	 * @see org.metier.domaine.reg.service.IExerciceService#getMaxDateFin()
	 */
	@Override
	public Date getMaxDateFinPlus1() {
		Date maxDateFin = exerciceDao.getMaxDateFin();

		return ((maxDateFin != null) ? DateUtil.getNextDayDate(maxDateFin) : maxDateFin);
	}

	
	@Transactional
	@WorkModelMethodValidator
	@Override
	public void ouvrirExercice(Long exerciceId){
		ExercicePersistant exerciceP = exerciceDao.findById(exerciceId);
		
		// Cloture
		exerciceP.setEtat("O");
		//
		getEntityManager().merge(exerciceP);
	}
	
	@Transactional
	@WorkModelMethodValidator
	@Override
	public void cloreExercice(Long exerciceId) {
		ExercicePersistant exerciceP = exerciceDao.findById(exerciceId);
		exerciceP.setEtat("C");
		//
		Date dateCloture = DateUtil.addSubstractDate(exerciceP.getDate_fin(), TIME_ENUM.DAY, 1);
		List<CompteBancaireBean> listBanque = compteBancaireService.findAll();
		
		// Report de solde
		for (CompteBancairePersistant compteBancairePersistant : listBanque) {
			BigDecimal soldeCompte = compteBancaireService.getSoldeCompte(compteBancairePersistant.getId());
			soldeCompte = (soldeCompte == null) ? BigDecimalUtil.ZERO : soldeCompte;
			
			EcriturePersistant cbP = new EcriturePersistant();
			cbP.setDate_mouvement(dateCloture);
			cbP.setElementId(exerciceId);
			cbP.setLibelle("Report de solde ouverture exercice");
			cbP.setOpc_banque(compteBancairePersistant);
			cbP.setSens(soldeCompte.compareTo(BigDecimalUtil.ZERO)>=0 ? "C":"D");
			cbP.setSource("CLOTURE");
			cbP.setMontant(soldeCompte.abs());
			//
			getEntityManager().merge(cbP);
		}
		//
		getEntityManager().merge(exerciceP);
	}
	
	@Transactional
	@WorkModelMethodValidator
	@Override
	public void annulerCloture(Long exerciceId) {
		ExercicePersistant exerciceP = exerciceDao.findById(exerciceId);
		exerciceP.setEtat("O");
		
		// Suppression écriture clôture
		getQuery("delete from EcriturePersistant where element_id=:elementId and source=:source")
			.setParameter("elementId",exerciceId)
			.setParameter("source", "CLOTURE")
			.executeUpdate();
		
		//
		getEntityManager().merge(exerciceP);
	}
	
	@Override
	public ExercicePersistant getChiffresExercice(Long exeId) {
		ExercicePersistant exeP = exerciceDao.findById(exeId);
		
		if("C".equals(exeP.getEtat())) {
			return exeP;
		} else {
			EtatFinanceBean etatFinanceBean = etatFinanceService.getEtatFinanceBean(exeP.getDate_debut(), exeP.getDate_fin());
			ExercicePersistant exePp = new ExercicePersistant();
			exePp.setLibelle(exeP.getLibelle());
			exePp.setMtt_vente_caisse(etatFinanceBean.getMtt_vente_caisse());
			exePp.setMtt_vente_hors_caisse(etatFinanceBean.getMtt_vente_hors_caisse());
			exePp.setMtt_recette_divers(etatFinanceBean.getMtt_recette_divers());
			exePp.setMtt_depense_divers(etatFinanceBean.getMtt_depense_divers());
			exePp.setMtt_achat(etatFinanceBean.getMtt_achat());
			exePp.setMtt_salaire(etatFinanceBean.getMtt_salaire());
			
			return exePp;
		}
	}
	
	@Override
	@WorkModelMethodValidator
	@Transactional
	public void delete(Long exerciceId) {
		try {
			// Suppression des fournisseurs
			String request = "delete from FournisseurPersistant";
			exerciceDao.getQuery(request).executeUpdate();
			
			// Suppression du plan comptable
			request = "delete from ComptePersistant where exercice_id=:exerciceId";
			Query query = exerciceDao.getQuery(request).setParameter("exerciceId", exerciceId);
			query.executeUpdate();
		
			exerciceDao.getEntityManager().flush();
			
			// Suppression de l'exercice
			super.delete(exerciceId);

		} catch(Exception e) {
			framework.model.common.service.MessageService.addBannerMessage("Cet exercice en peut pas être supprimé car il est liée à des comptes.");
		}
	}
}
