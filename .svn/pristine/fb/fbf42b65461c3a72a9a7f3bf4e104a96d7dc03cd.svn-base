package appli.model.domaine.administration.validator;
import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.administration.bean.EtablissementBean;
import appli.model.domaine.administration.dao.IEtablissementDao;
import framework.model.common.service.MessageService;


@Named
public class EtablissementValidator {
	@Inject
	private IEtablissementDao etablissementDao;
	
	/* (non-Javadoc)
	 * @see model.service.commun.validator.ActionValidator#validate(java.lang.Object, java.lang.String)
	 */
	public void updateCreateValidator(EtablissementBean etablissementBean) {
		// Le nom doit être unique
		if(etablissementDao.isNotUnique(etablissementBean, "nom")){
			MessageService.addFieldMessageKey("etablissement.nom", "msg.valeur.exist");
		}
		// la Raison sociale doit etre  unique
		if(etablissementDao.isNotUnique(etablissementBean, "raison_sociale")){
			MessageService.addFieldMessageKey("etablissement.raison_sociale", "msg.valeur.exist");
		}
		// Le numéro du registre de commerce  doit etre  unique
//		if(etablissementDao.isNotUnique(etablissementBean, "numero_rcs")){
//			MessageService.addFieldMessageKey("etablissement.numero_rcs", "msg.valeur.exist");
//		}
		// l'ice doit etre unique
		if(etablissementDao.isNotUnique(etablissementBean, "raison_sociale")){
			MessageService.addFieldMessageKey("etablissement.raison_sociale", "msg.valeur.exist");
		}
		// Le numéro du tva doit etre unique
//		if(etablissementDao.isNotUnique(etablissementBean, "numero_tva")){
//			MessageService.addFieldMessageKey("etablissement.numero_tva", "msg.valeur.exist");
//		}
		// l'dentifiant fiscal etre  unique
//		if(etablissementDao.isNotUnique(etablissementBean, "identifiant_fiscal")){
//			MessageService.addFieldMessageKey("etablissement.identifiant_fiscal", "msg.valeur.exist");
//		}
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void update(EtablissementBean etablissementBean) {
		updateCreateValidator(etablissementBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void create(EtablissementBean etablissementBean){
		updateCreateValidator(etablissementBean);
	}
	
}
