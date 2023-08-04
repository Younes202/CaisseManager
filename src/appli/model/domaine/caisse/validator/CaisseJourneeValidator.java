package appli.model.domaine.caisse.validator;

import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.caisse.bean.CaisseJourneeBean;
import appli.model.domaine.caisse.dao.ICaisseJourneeDao;
import appli.model.domaine.vente.persistant.CaisseJourneePersistant;

@Named
public class CaisseJourneeValidator {
	@Inject
	private ICaisseJourneeDao caisseJourneeDao;
	
	/*
	 * 
	 */
	public void updateCreateValidator(CaisseJourneeBean caisseJourneeBean) {
		
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void update(CaisseJourneeBean caisseJourneeBean) {
		updateCreateValidator(caisseJourneeBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void create(CaisseJourneeBean caisseJourneeBean){
		updateCreateValidator(caisseJourneeBean);
	}

	
	/**
	 * @param id
	 */
	public void delete(Long id){
		CaisseJourneePersistant caisseJourneePersistant = caisseJourneeDao.findById(id);
	}
	
}
