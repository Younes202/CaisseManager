package appli.model.domaine.caisse_restau.validator;

import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.caisse_restau.bean.TokenBean;
import appli.model.domaine.caisse_restau.dao.ITokenDao;
import appli.model.domaine.caisse_restau.persistant.TokenPersistant;

@Named
public class TokenValidator {
	@Inject
	private ITokenDao tokenDao;
	
	/* (non-Javadoc)
	 * @see framework.model.service.commun.validator.ActionValidator#validate(java.lang.Object, java.lang.String)
	 */
	public void updateCreateValidator(TokenBean tokenBean) {
		
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void update(TokenBean tokenBean) {
		updateCreateValidator(tokenBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void create(TokenBean tokenBean){
		updateCreateValidator(tokenBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void delete(Long id){
		TokenPersistant tokenPersistant = tokenDao.findById(id);
	}
	
}
