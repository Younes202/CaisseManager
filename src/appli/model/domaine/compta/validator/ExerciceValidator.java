package appli.model.domaine.compta.validator;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.criterion.Order;

import appli.controller.domaine.administration.bean.UserBean;
import appli.model.domaine.administration.persistant.EcriturePersistant;
import appli.model.domaine.compta.dao.IEcritureDao;
import appli.model.domaine.compta.dao.IExerciceDao;
import framework.controller.ContextGloabalAppli.STATUT_EXERCICE;
import framework.model.beanContext.ExerciceBean;
import framework.model.beanContext.ExercicePersistant;
import framework.model.common.service.MessageService;
import framework.model.common.util.DateUtil;
import framework.model.common.util.DateUtil.TIME_ENUM;

@Named
public class ExerciceValidator {
	@Inject
	private IExerciceDao exerciceDao;
	@Inject
	private IEcritureDao ecritureDao;
	
	/* (non-Javadoc)
	 * @see framework.model.service.commun.validator.ActionValidator#validate(java.lang.Object, java.lang.String)
	 */
	public void updateCreateValidator(ExerciceBean exerciceBean) {
		// Tester l'unicité du libellé
		if(exerciceDao.isNotUnique(exerciceBean, "libelle")){
			MessageService.addFieldMessage("exercice.libelle", "Ce libellé existe déjà");
		}
		// La date de fin doit être postérieure à la date de début
		Date dateDebutForm = exerciceBean.getDate_debut();
		Date dateFinForm = exerciceBean.getDate_fin();
		
		if(dateFinForm.before(dateDebutForm)){
			MessageService.addFieldMessage("exercice.date_fin", "La date de fin doit être postérieure à la date de début");
		}
		// Vérifier si les exercices ne se chevauchent pas
		List<ExercicePersistant> lsitExercice = exerciceDao.findAll(Order.asc("date_debut"));
		for (ExercicePersistant exercicePersistant : lsitExercice) {
			if(!exercicePersistant.getId().equals(exerciceBean.getId())){ 
				Date dateDebutBase = exercicePersistant.getDate_debut();
				Date dateFinBase = exercicePersistant.getDate_fin();
				
				if(
						dateDebutForm.equals(dateDebutBase)
						|| 
						dateFinForm.equals(dateFinBase)
						||
						dateFinForm.equals(dateDebutBase)
						||
						(dateDebutForm.before(dateDebutBase) && dateFinForm.after(dateFinBase))
						||
						(dateDebutForm.after(dateDebutBase) && dateFinForm.before(dateFinBase))
				) {
					MessageService.addFieldMessage("exercice.date_debut", "Les exercice doivent se suivre : "+exercicePersistant.getLibelle());
				}
			}
		}
		// Un exercice ne doit pas excéder à 18 mois
		Calendar startCalendar = new GregorianCalendar();
		startCalendar.setTime(exerciceBean.getDate_debut());
		Calendar endCalendar = new GregorianCalendar();
		endCalendar.setTime(exerciceBean.getDate_fin());

		int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
		int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
		
		if(diffMonth > 18){
			MessageService.addBannerMessage("La durée de l'exercice ne doit pas excéder à 18 mois");
		}
	}

	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void update(ExerciceBean exerciceBean) {
		updateCreateValidator(exerciceBean);
		// Une fois les appels de fond générés plus possible de toucher les dates de l'exercice
		ExercicePersistant exerciceDB = exerciceDao.findById(exerciceBean.getId());
		
		// Une fois les appels de fond générés plus possible de toucher les dates de l'exercice
//		ExercicePersistant exerciceDB = exerciceDao.findById(exerciceBean.getId());
//		List<EcriturePersistant> listEcriture = ecritureDao.getListEcritureByDates(exerciceDB.getDate_debut(), exerciceDB.getDate_fin());
//		
//		if(listEcriture.size() > 0){
		if(exerciceDB.getDate_debut().compareTo(exerciceBean.getDate_debut()) != 0){
			MessageService.addFieldMessage("exercice.date_debut", "La date de début ne peut être changée");
			return;
		}
		if(exerciceDB.getDate_fin().compareTo(exerciceBean.getDate_fin()) != 0){
			MessageService.addFieldMessage("exercice.date_fin", "La date de fin ne peut être changée");
			return;
		}
		if(!exerciceDB.isExerciceOuvert()){
			MessageService.addBannerMessage("On ne peut modifier le statut de l'exercice");
			return;
		}
//		}
	}

	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void delete(Long exerciceId){
		// L'exercice ne doit être clôturé
		ExercicePersistant exercicePersistant = exerciceDao.findById(exerciceId);
		if(!exercicePersistant.isExerciceOuvert()){
			MessageService.addBannerMessage("L'exercice doit être ouvert pour être supprimé.");
			return;
		}
		// Ecriture
		List<EcriturePersistant> listEcriture = ecritureDao.getListEcritureByDates(exercicePersistant.getDate_debut(), exercicePersistant.getDate_fin());
		boolean isEcritureNonOuverture = false;
		//
		if(listEcriture != null) {
			for (EcriturePersistant ecriturePersistant : listEcriture) {
				if(!ecriturePersistant.getSource().equals("CLOTURE")){
					isEcritureNonOuverture = true;
					break;
				}
			}
		}
		//
		if(isEcritureNonOuverture){
			MessageService.addBannerMessage("Cet exercice ne peut pas être supprimé car des écritures y sont rattachées.");
			return;
		}
	}

	/**
	 * @param ids
	 */
	public void delete_group(Long[] ids) {
		for(Long id : ids){
			delete(id);
		}
	}
	
	/**
	 * @param exerciceId
	 */
	public void cloturerExercice(Long exerciceId, UserBean userBean){
		ExercicePersistant exercicePersistant = exerciceDao.findById(exerciceId);
		
		if(STATUT_EXERCICE.CLOTURE.getStatut().equals(exercicePersistant.getStatut_cloture())){
			MessageService.addBannerMessage("Cet exercice a déjà été clôturé.");
			return;
		} else if(STATUT_EXERCICE.EN_COURS_CLOTURE.getStatut().equals(exercicePersistant.getStatut_cloture())){
			MessageService.addBannerMessage("L'opération de clôture est en cours, veuillez attendre la fin de cette opération.");
			return;
		}
		
		// On peut clôturer l'exercice que si l'ancien est cloture definitif
		ExercicePersistant exercicePrecedent = exerciceDao.getExercicePrecedent(exercicePersistant);
		if(exercicePrecedent != null && !exercicePrecedent.getStatut_cloture().equals(STATUT_EXERCICE.CLOTURE_DEFINITIF.getStatut())){
			MessageService.addBannerMessage("Pour clore cet exercice, vous devez clore definitivement l'exercice précédent.");
			return;
		}
	}
	
	/**
	 * @param oldExerciceId
	 * @param userBean
	 */
	public void ouvrirExercice(Long exerciceId, boolean isNewOuverture) {
		ExercicePersistant exercicePersistant = exerciceDao.findById(exerciceId);
		String statut_cloture = exercicePersistant.getStatut_cloture();
		if(!STATUT_EXERCICE.CLOTURE.getStatut().equals(statut_cloture) && !STATUT_EXERCICE.CLOTURE_DEFINITIF.getStatut().equals(statut_cloture)
				&& !STATUT_EXERCICE.OUVERT.getStatut().equals(statut_cloture)){
			MessageService.addBannerMessage("Vous ne pouvez pas ouvrir cet exercice.");
			return;
		} else if(STATUT_EXERCICE.EN_COURS_OUVERTURE.getStatut().equals(statut_cloture)){
			MessageService.addBannerMessage("L'opération d'ouverture est en cours, veuillez attendre la fin de cette opération.");
			return;
		}
		// On ne peut pas ouvrir plus de deux exercice futures par rapport à la date du jour
		Date dateJourFin = DateUtil.stringToDate("31/12/"+DateUtil.getCalendar(new Date()).get(Calendar.YEAR));
		Date dateFinCurrExercice = DateUtil.addSubstractDate(exerciceDao.getMaxDateFin(), TIME_ENUM.YEAR, 1);
		
		if(DateUtil.getDiffYear(dateFinCurrExercice, dateJourFin) > 2){
			MessageService.addBannerMessage("Vous ne pouvez ouvrir plus de deux exercices futures.");
			return;
		}
	}
	
	/**
	 * @param exerciceId
	 */
	public void annulerCloture(Long exerciceId) {
		// On peut clôturer et décloturer que le dernier exercice
		ExercicePersistant exercicePersistant = exerciceDao.findById(exerciceId);
		if(exercicePersistant != null && !exercicePersistant.getStatut_cloture().equals(STATUT_EXERCICE.CLOTURE.getStatut())){
			MessageService.addBannerMessage("Cet exercice doit être d'abord clôturé.");
			return;
		}
	}
	
	/**
	 * @param exerciceId
	 */
	public void cloturerDefinitif(Long exerciceId) {
		// On peut clôturer et décloturer que le dernier exercice
		ExercicePersistant exercicePersistant = exerciceDao.findById(exerciceId);
		if(exercicePersistant != null && !exercicePersistant.getStatut_cloture().equals(STATUT_EXERCICE.CLOTURE.getStatut())){
			MessageService.addBannerMessage("Cet exercice doit être d'abord clôturé.");
			return;
		}
	}
}
