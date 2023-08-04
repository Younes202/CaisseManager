package appli.model.domaine.compta.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import appli.controller.domaine.compta.bean.EcritureBean;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_ECRITURE;
import appli.model.domaine.administration.persistant.EcriturePersistant;
import framework.model.beanContext.ExercicePersistant;
import framework.model.service.IGenericJpaService;

public interface IEcritureService extends IGenericJpaService<EcritureBean, Long> {

	/**
	 * @param exercicePersistant
	 * @param compteCode
	 * @param ecartExecice : 0 = exercice en cours    +1 = Exercice suivant    -1 = exercice précédent
	 * @return
	 */
	BigDecimal getTotalByExcerciceAndCompte(ExercicePersistant exercicePersistant, String ... compteCodes);
	BigDecimal getTotalByExcerciceAndCompte(ExercicePersistant previousExercice, char sens, boolean exludeCloture, String ... compteCodes);
	BigDecimal getTotalByExcerciceAndCompte(ExercicePersistant exercicePersistant, char sens, String... compteCodes);
	
	void deleteByOrigineAndType(Long origineId, TYPE_ECRITURE type);
	/**
	 * Récuperer l'identifiant suivant du groupe pour le même origine
	 * @param origineId
	 * @return
	 */
	void createEcriture(List<EcriturePersistant> listEcritures);
	BigDecimal getSoldeComptable(String compteCode, Date dateDebut, Date dateFin);
	BigDecimal getSoldeComptable(String compteCode, Date dateDebut, Date dateFin, String sens);
	Map<String, BigDecimal> getSoldeComptableDetail(String compteCode, Date dateDebut, Date dateFin);
	List<EcriturePersistant> getListEcritureByDates(Date dateDebut, Date dateFin);
	void delete_from_ecriture(Long ectId);
	BigDecimal getSoldeComptableFinExercice(String compteCode, ExercicePersistant exercice);
	void updateEcriture(List<EcriturePersistant> listEcriture, Long dbElementId);
	BigDecimal getTotalBanqueCaisse(Long banqueId, Date dateEcriture);
	BigDecimal getSoldeReportCompte(Long compteId, Date dateDebut);
	BigDecimal getSoldeCompte(Long compteId, Date dateFin);
	BigDecimal getSoldeCompte(Long compteId);
	List<EcriturePersistant> getListEcritureByOrigineAndType(Long origineId, TYPE_ECRITURE source);
	List<EcriturePersistant> getListEcritureByExerciceAndCompte(ExercicePersistant exercicePersistant,
			String compteCode);
	List<EcriturePersistant> getListEcritureByOrigineAndTypeAndGroup(Long origineId, TYPE_ECRITURE type,
			Integer groupe);
	Integer getNextGroupParOrigine(TYPE_ECRITURE source, Long origineId);
	List<EcriturePersistant> getListEcritureByExerciceAndGroupeAndType(ExercicePersistant exercicePersistant,
			Integer groupe, TYPE_ECRITURE source);
}
