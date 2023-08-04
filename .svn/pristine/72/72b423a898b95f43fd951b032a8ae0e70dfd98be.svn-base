package appli.model.domaine.stock.dao;

import java.util.Date;
import java.util.List;

import appli.controller.domaine.stock.bean.ChargeDiversBean;
import appli.model.domaine.stock.persistant.ChargeDiversPersistant;
import framework.model.util.IGenericJpaDao;

public interface IChargeDiversDao extends IGenericJpaDao<ChargeDiversPersistant, Long>{
	
	Integer max_num(String date);
	public ChargeDiversPersistant createCharge(ChargeDiversPersistant charge);
	void update_automate(ChargeDiversPersistant cdp,Date dateDebut, Date dateFin, Integer frequence);
	public void controleAutomate(Long id);
	public List<ChargeDiversPersistant> getAllTheAutomatedCharge();
	
	public void deleteAutomatisation(ChargeDiversPersistant cdp);
	 
}
