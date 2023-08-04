package appli.model.domaine.administration.validator;
import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.administration.bean.SocieteBean;
import appli.model.domaine.administration.dao.ISocieteDao;
import framework.model.common.service.MessageService;


@Named
public class SocieteValidator {
	@Inject
	private ISocieteDao societeDao;
	
	/* (non-Javadoc)
	 * @see model.service.commun.validator.ActionValidator#validate(java.lang.Object, java.lang.String)
	 */
	public void updateCreateValidator(SocieteBean societeBean) {
		// Le nom doit être unique
		// la Raison sociale doit etre  unique
		if(societeDao.isNotUnique(societeBean, "raison_sociale")){
			MessageService.addFieldMessageKey("societe.raison_sociale", "msg.valeur.exist");
		}
		// Le numéro du registre de commerce  doit etre  unique
		if(societeDao.isNotUnique(societeBean, "numero_rcs")){
			MessageService.addFieldMessageKey("societe.numero_rcs", "msg.valeur.exist");
		}
		// l'ice doit etre unique
		if(societeDao.isNotUnique(societeBean, "raison_sociale")){
			MessageService.addFieldMessageKey("societe.raison_sociale", "msg.valeur.exist");
		}
		// Le numéro du tva doit etre unique
		if(societeDao.isNotUnique(societeBean, "numero_tva")){
			MessageService.addFieldMessageKey("societe.numero_tva", "msg.valeur.exist");
		}
		// l'dentifiant fiscal etre  unique
		if(societeDao.isNotUnique(societeBean, "identifiant_fiscal")){
			MessageService.addFieldMessageKey("societe.identifiant_fiscal", "msg.valeur.exist");
		}
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void update(SocieteBean societeBean) {
		updateCreateValidator(societeBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void create(SocieteBean societeBean){
		updateCreateValidator(societeBean);
	}
	
}
