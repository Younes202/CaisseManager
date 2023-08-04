package appli.model.domaine.compta.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import appli.controller.domaine.util_erp.ContextAppli.TYPE_ECRITURE;
import appli.model.domaine.administration.persistant.EcriturePersistant;
import framework.model.beanContext.ExercicePersistant;
import framework.model.util.IGenericJpaDao;

public interface IEcritureDao extends IGenericJpaDao<EcriturePersistant, Long>{

	List<EcriturePersistant> getListEcritureByOrigineAndType(Long origineId, TYPE_ECRITURE type);
	List<EcriturePersistant> getListEcritureByExerciceAndGroupeAndType(ExercicePersistant exercicePersistant, Integer groupe, TYPE_ECRITURE type);
	List<EcriturePersistant> getListEcritureByExerciceAndCompte(ExercicePersistant exercicePersistant, String compteCode);
	List<EcriturePersistant> getListEcritureByExerciceAndCompteAndType(ExercicePersistant exercicePersistant, String compteCode, TYPE_ECRITURE type);
	List<EcriturePersistant> getListEcritureByOrigineAndTypeAndGroup(Long origineId, TYPE_ECRITURE type, Integer groupe);
	Integer getNextGroupParOrigine(TYPE_ECRITURE type, Long origineId);
	
	Map<String, BigDecimal> getSoldeComptableDetail(String compteCode, Date dateDebut, Date dateFin);
	BigDecimal getSoldeComptable(String compteCode, Date dateDebut, Date dateFin);
	List<EcriturePersistant> getListEcritureByDates(Date dateDebut, Date dateFin);
	Integer getNextGroupParOrigine(TYPE_ECRITURE type, Long origineId, EntityManager entityManager);
	BigDecimal getSoldeComptable(String compteCode, Date dateDebut,	Date dateFin, String sens);
	BigDecimal getSoldeComptableFinExercice(String compteCode, ExercicePersistant exercice);
}
