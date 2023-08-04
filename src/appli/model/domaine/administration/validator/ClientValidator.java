package appli.model.domaine.administration.validator;

import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.personnel.bean.ClientBean;
import appli.model.domaine.personnel.dao.IClientDao;
import framework.model.common.service.MessageService;

@Named
public class ClientValidator {
	@Inject
	private IClientDao clientDao;
	
	/* (non-Javadoc)
	 * @see model.service.commun.validator.ActionValidator#validate(java.lang.Object, java.lang.String)
	 */
	public void updateCreateValidator(ClientBean clientBean) {
		// Le numéro de sécurité doit être unique
		if(clientDao.isNotUnique(clientBean, "numero")){
			MessageService.addFieldMessageKey("client.numero", "msg.valeur.exist");
		}
		// Téléphone unique
		if(clientDao.isNotUnique(clientBean, "telephone")){
			MessageService.addFieldMessageKey("client.telephone", "msg.valeur.exist");
		}
		if(clientDao.isNotUnique(clientBean, "cin")){
			MessageService.addFieldMessageKey("client.cin", "msg.valeur.exist");
		}
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void update(ClientBean clientBean) {
		updateCreateValidator(clientBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void create(ClientBean clientBean){
		updateCreateValidator(clientBean);
	}
	
	public void delete(Long id){
		if(clientDao.getQuery("from CaisseMouvementPersistant where opc_client.id=:clieId").setParameter("clieId", id).getResultList().size() > 0){
			MessageService.addBannerMessage("Ce client est utilisé pour une vente.");			
		} else if(clientDao.getQuery("from CarteFideliteClientPersistant where opc_client.id=:clieId").setParameter("clieId", id).getResultList().size() > 0){
			MessageService.addBannerMessage("Ce client est utilisé dans une carte de fidélité.");
		}
	}
}
