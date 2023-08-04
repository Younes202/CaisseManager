package appli.model.domaine.stock.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import appli.controller.domaine.stock.bean.ChargeDiversBean;
import appli.model.domaine.stock.persistant.ChargeDiversPersistant;
import appli.model.domaine.stock.persistant.LabelsGroupPersistant;
import framework.model.service.IGenericJpaService;

public interface IChargeDiversService extends IGenericJpaService<ChargeDiversBean, Long> {

	String generate_numBl(String type);
	void update_automate(ChargeDiversPersistant cdp,Date dateDebut, Date dateFin, Integer frequence);
	public void controleAutomate(Long id);
	public List<ChargeDiversPersistant> getAllTheAutomatedCharge();
	
	public void deleteAutomatisation(ChargeDiversPersistant cdp);
	List<ChargeDiversPersistant> getListCDGroupe(Long charge_id);
	void valideRegroupementBL(ChargeDiversBean mvmBean);
	Map<Long, List<ChargeDiversPersistant>> getMapChargeGroupe(List<ChargeDiversPersistant> listDataAll);
	List<ChargeDiversPersistant> getChargeNonGroupe(Long fournisseurId, List<Long> ignoreIds, Date dateDebut,
			Date dateFin);
	Date getMaxDate(String sens);
	void mergeLabels(List<LabelsGroupPersistant> listLib, String source);
	List<LabelsGroupPersistant> getLibelleParametres(String source);
}
