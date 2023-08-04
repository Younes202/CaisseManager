package appli.model.domaine.administration.validator;

import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.administration.bean.MessageBean;
import appli.model.domaine.administration.dao.IMessageDao;
import framework.model.common.service.MessageService;

@Named
public class MessageValidator {
	@Inject
	private IMessageDao messageDao;
	
	/* (non-Javadoc)
	 * @see framework.model.service.commun.validator.ActionValidator#validate(java.lang.Object, java.lang.String)
	 */
	public void updateCreateValidator(MessageBean messageBean) {

	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void update(MessageBean messageBean) {
		updateCreateValidator(messageBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void create(MessageBean messageBean){
		updateCreateValidator(messageBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void delete(Long id){
		// On ne peut pas supprimer une écriture
		MessageService.addBannerMessage("");
	}
	
}
