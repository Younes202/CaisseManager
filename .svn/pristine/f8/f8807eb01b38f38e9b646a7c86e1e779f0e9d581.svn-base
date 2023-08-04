package appli.model.domaine.administration.validator;

import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.administration.bean.GedBean;
import appli.model.domaine.administration.dao.IGedDao;

@Named
public class GedValidator {
	@Inject
	private IGedDao gedDao;
	
	/*
	 * 
	 */
	public void updateCreateValidator(GedBean gedBean) {

	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void updateGed(GedBean gedBean) {
		updateCreateValidator(gedBean);
		
		// 
//		Long elementId = gedBean.getElement_id();
//		GedPersistant gedPrentDB = gedDao.getGedParent(gedBean);
//		// Vérifier si le parent à changé
//		if(!elementId.equals(gedPrentDB.getId()) && gedBean.getB_right()-gedBean.getB_right() != 1){
//			MessageService.addBannerMessage("Il n'est pas possible de déplacer une ged");
//		}
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void createGed(GedBean gedBean){
		updateCreateValidator(gedBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void deleteGed(Long id){

	}
}
