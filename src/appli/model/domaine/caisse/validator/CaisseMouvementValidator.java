package appli.model.domaine.caisse.validator;

import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.caisse.bean.CaisseMouvementBean;
import appli.model.domaine.caisse.dao.ICaisseMouvementDao;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;

@Named
public class CaisseMouvementValidator {
	@Inject
	private ICaisseMouvementDao caisseMouvementDao;
	
	/*
	 * 
	 */
	public void updateCreateValidator(CaisseMouvementBean caisseMouvementBean) {
		
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void update(CaisseMouvementBean caisseMouvementBean) {
		updateCreateValidator(caisseMouvementBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void create(CaisseMouvementBean caisseMouvementBean){
		updateCreateValidator(caisseMouvementBean);
	}
	
	/**
	 * @param id
	 */
	public void delete(Long id){
		CaisseMouvementPersistant caisseMouvementPersistant = caisseMouvementDao.findById(id);
	}
	
}
