package appli.model.domaine.compta.service.impl;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.compta.dao.IExerciceDao;
import appli.model.domaine.compta.service.IExerciceClotureService;
import appli.model.domaine.compta.service.IExerciceService;
import appli.model.domaine.compta.validator.ExerciceValidator;
import framework.controller.ContextGloabalAppli.STATUT_EXERCICE;
import framework.model.beanContext.ExerciceBean;
import framework.model.beanContext.ExercicePersistant;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.common.util.DateUtil;
import framework.model.common.util.DateUtil.TIME_ENUM;
import framework.model.service.GenericJpaService;

@WorkModelClassValidator(validator=ExerciceValidator.class)
@Named
public class ExerciceClotureService extends GenericJpaService<ExerciceBean, Long> implements IExerciceClotureService{
	@Inject
	IExerciceDao exerciceDao;
	@Inject
	IExerciceService exerciceService;
	
	@Override
	public void cloturerExercice(Long exerciceId) {
		boolean errorEnCloture = false;
		
		//
		ExercicePersistant exercicePersistant = (ExercicePersistant) exerciceService.findById(ExercicePersistant.class, exerciceId);
		// Maj à jour flag
		exercicePersistant.setStatut_cloture(STATUT_EXERCICE.EN_COURS_CLOTURE.getStatut());
		// Maj ancien exercice
		exercicePersistant.setStatut_cloture(STATUT_EXERCICE.OUVERT.getStatut());
		
		exerciceService.getEntityManager().merge(exercicePersistant);
		
		if(!errorEnCloture){
			genererDocumentsCloture(exercicePersistant.getId());
			// Ouverture
			ouvrirExercice(exercicePersistant.getId(), false); 
		}
	}

	@Override
	public void cloturerDefinitif(Long exerciceId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void annulerCloture(Long exerciceId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ouvrirExercice(Long currExerciceId, boolean isNewOuverture) {
		ExercicePersistant newExercicePersistant = null;
		ExercicePersistant currExercicePersistant = (ExercicePersistant) exerciceService.findById(ExercicePersistant.class, currExerciceId);
		
		EntityManager em = exerciceService.getEntityManager();
		if(!isNewOuverture){
			newExercicePersistant = exerciceDao.getExerciceSuivant(currExercicePersistant);
			// Pour attacher à la session
			if(newExercicePersistant != null){
				Object findById;
				newExercicePersistant = (ExercicePersistant) findById(ExercicePersistant.class, newExercicePersistant.getId());
			}
			// Maj ancien exercice
			currExercicePersistant.setStatut_cloture(STATUT_EXERCICE.OUVERT.getStatut());
			//
			em.merge(currExercicePersistant);
		}
		if(newExercicePersistant == null){ 
			// Date de début initialisée à la plus grande date des anciens exercice
			// Ajouter un an pour avoir la date de fin
			Date dateDebut = exerciceService.getMaxDateFinPlus1();
			Date dateFin = DateUtil.addSubstractDate(dateDebut, TIME_ENUM.YEAR, 1);
			dateFin = DateUtil.addSubstractDate(dateFin, TIME_ENUM.DAY, -1);

			newExercicePersistant = new ExercicePersistant();
			newExercicePersistant.setDate_debut(dateDebut);
			newExercicePersistant.setDate_fin(dateFin);
			newExercicePersistant.setStatut_cloture(STATUT_EXERCICE.OUVERT.getStatut());
			newExercicePersistant.setLibelle(DateUtil.dateToString(dateDebut) + " au "+DateUtil.dateToString(dateFin));
			newExercicePersistant.setCommentaire("Exercice crée suite à la fermeture de l'exercice "+currExercicePersistant.getLibelle());
			//
			newExercicePersistant = em.merge(newExercicePersistant);
		} else{
			newExercicePersistant.setStatut_cloture(STATUT_EXERCICE.OUVERT.getStatut());
			// Maj ancien exercice
			newExercicePersistant.setStatut_cloture(STATUT_EXERCICE.OUVERT.getStatut());
			
			// Supprimer les écritures d'ouverture
			Query query = getQuery("delete from EcriturePersistant e where e.type=:type and e.origine_id=:origine_id")
					.setParameter("type", ContextAppli.TYPE_ECRITURE.OUVERTURE.toString())
					.setParameter("origine_id", newExercicePersistant.getId());
			query.executeUpdate();
		}
	}
	
	/**
	 * @param exerciceId
	 */
	private void genererDocumentsCloture(Long exerciceId){
		
	}
}