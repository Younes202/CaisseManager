package appli.model.domaine.compta.service;

import java.util.Date;

import framework.model.beanContext.ExerciceBean;
import framework.model.beanContext.ExercicePersistant;
import framework.model.service.IGenericJpaService;

public interface IExerciceService extends IGenericJpaService<ExerciceBean, Long> {

	ExercicePersistant getActifExercice();

	Date getMaxDateFinPlus1();

	void ouvrirExercice(Long exerciceId);

	void cloreExercice(Long exerciceId);

	void annulerCloture(Long exerciceId);

	ExercicePersistant getChiffresExercice(Long exeId);

	boolean is_exercice_date_ouvert(Date date);
}
