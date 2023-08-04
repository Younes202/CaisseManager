package appli.model.domaine.administration.validator;

import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.administration.bean.CompteBancaireBean;
import appli.model.domaine.administration.dao.ICompteBancaireDao;

@Named
public class CompteBancaireValidator {
	@Inject
	private ICompteBancaireDao compteBancaireDao;
	
	/* (non-Javadoc)
	 * @see model.service.commun.validator.ActionValidator#validate(java.lang.Object, java.lang.String)
	 */
	public void updateCreateValidator(CompteBancaireBean compteBancaireBean) {

	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void update(CompteBancaireBean compteBancaireBean) {
		updateCreateValidator(compteBancaireBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void create(CompteBancaireBean compteBancaireBean){
		updateCreateValidator(compteBancaireBean);
	}
}
