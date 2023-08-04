package appli.model.domaine.personnel.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import appli.controller.domaine.personnel.bean.paie.SalariePaieBean;
import appli.model.domaine.personnel.persistant.paie.SalairePersistant;
import framework.model.service.IGenericJpaService;

public interface ISalariePaieService extends IGenericJpaService<SalariePaieBean, Long> {

	Map<String, SalairePersistant> getMapPaie(Integer annee);

	SalairePersistant getPaieByEmployeYearMonth(Long employe_id, Integer annee, Integer mois);

	List<SalairePersistant> getPaieByYearMonth(Integer annee, Integer mois);

	Map<Integer, Boolean> getMapEtatMois(Integer anneeId);

	void cloreMois(Integer annee, Integer idxMois);

	boolean isLastMois(String mois, Integer annee);

//	List<Integer> getListAnnee();

	BigDecimal getSalaireByEmployeAndDate(Long employe_id, Date date_debut, Date date_fin);

	String[][] getHistoLibelle(String tp);

	/**
	 * @param httpUtil
	 * @param currentDate
	 * @return
	 */
	void calculSalaireFromPointage(Date currentDate);
}
