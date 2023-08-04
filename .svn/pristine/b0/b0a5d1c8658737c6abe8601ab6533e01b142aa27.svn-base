package appli.model.domaine.administration.validator;

import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.administration.bean.LieuxBean;
import appli.model.domaine.administration.dao.ILieuxDao;
import framework.model.common.service.MessageService;

@Named
public class LieuxValidator {
	@Inject
	private ILieuxDao lieuxDao;
	
	/* (non-Javadoc)
	 * @see model.service.commun.validator.ActionValidator#validate(java.lang.Object, java.lang.String)
	 */
	public void updateCreateValidator(LieuxBean lieuxBean) {
		// Le libellé doit être unique
		if(lieuxDao.isNotUnique(lieuxBean, "libelle")){
			MessageService.addFieldMessage("lieux.libelle", "Cette valeur existe déjà");
		}
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void update(LieuxBean lieuxBean) {
		updateCreateValidator(lieuxBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void create(LieuxBean lieuxBean){
		updateCreateValidator(lieuxBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void delete(Long id){
	}
	
}
