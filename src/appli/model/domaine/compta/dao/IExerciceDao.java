package appli.model.domaine.compta.dao;

import java.util.Date;

import framework.model.beanContext.ExercicePersistant;
import framework.model.util.IGenericJpaDao;

public interface IExerciceDao extends IGenericJpaDao<ExercicePersistant, Long>{

	/**
	 * @return
	 */
	Date getMaxDateFin();

	/**
	 * @return
	 */
//	ExercicePersistant getActiveExercice();
	
//	ExercicePersistant getActiveExercice(Long coproId);

	/**
	 * @param dateDebut
	 * @return
	 */
	ExercicePersistant getExerciceByDate(Date dateDebut, Date dateFin);

	ExercicePersistant getExerciceSuivant(ExercicePersistant e);
	ExercicePersistant getExercicePrecedent(ExercicePersistant e);

	ExercicePersistant getLastExercice();
}
