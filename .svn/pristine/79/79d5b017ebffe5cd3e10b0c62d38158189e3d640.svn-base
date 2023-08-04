package appli.model.domaine.administration.validator;

import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.administration.bean.TypeEnumBean;
import appli.model.domaine.administration.dao.ITypeEnumDao;
import framework.model.common.service.MessageService;

@Named
public class TypeEnumValidator {
	@Inject
	private ITypeEnumDao typeEnumDao;
	
	/* (non-Javadoc)
	 * @see framework.model.service.commun.validator.ActionValidator#validate(java.lang.Object, java.lang.String)
	 */
	public void updateCreateValidator(TypeEnumBean typeEnumBean) {
		// Tester l'unicité du code
		if(typeEnumDao.isNotUnique(typeEnumBean, "code")){
			MessageService.addFieldMessageKey("typeEnum.code", "msg.valeur.exist");
		}
		// Tester l'unicité du libellé
		if(typeEnumDao.isNotUnique(typeEnumBean, "libelle")){
			MessageService.addFieldMessageKey("typeEnum.libelle", "msg.valeur.exist");
		}
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void update(TypeEnumBean typeEnumBean) {
		updateCreateValidator(typeEnumBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void create(TypeEnumBean typeEnumBean){
		updateCreateValidator(typeEnumBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void delete(Long id){
		// On ne peut pas supprimer une écriture
		MessageService.addBannerMessage("Un type énuméré ne peut pas être supprimé");
	}
	
}
