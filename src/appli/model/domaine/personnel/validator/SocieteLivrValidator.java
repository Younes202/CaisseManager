package appli.model.domaine.personnel.validator;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.personnel.bean.SocieteLivrBean;
import appli.model.domaine.personnel.dao.ISocieteLivrDao;
import framework.model.common.service.MessageService;

@Named
public class SocieteLivrValidator {
	@Inject
	private ISocieteLivrDao societeLivrDao;
	
	/* (non-Javadoc)
	 * @see model.service.commun.validator.ActionValidator#validate(java.lang.Object, java.lang.String)
	 */
	public void updateCreateValidator(SocieteLivrBean societeLivrBean) {
		// Le numéro de sécurité doit être unique
		if(societeLivrDao.isNotUnique(societeLivrBean, "numero")){
			MessageService.addFieldMessageKey("societeLivr.numero", "msg.valeur.exist");
		}

	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void update(SocieteLivrBean societeLivrBean) {
		updateCreateValidator(societeLivrBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void create(SocieteLivrBean societeLivrBean){
		updateCreateValidator(societeLivrBean);
	}
		
	public void delete(Long socLivrId){
		// ClientPortefeuilleMvmPersistant
		List listData = societeLivrDao.getQuery("from ClientPortefeuilleMvmPersistant where opc_societeLivr.id=:socLivrId")
			.setParameter("socLivrId", socLivrId)
			.getResultList();
		if(listData.size() > 0){
			MessageService.addBannerMessage("Cette société est associée à des mouvements portefeuille virtuel.");
			return;
		}
		// CaisseMouvementPersistant
		listData = societeLivrDao.getQuery("from CaisseMouvementPersistant where opc_societe_livr.id=:socLivrId")
				.setParameter("socLivrId", socLivrId)
				.getResultList();
		if(listData.size() > 0){
			MessageService.addBannerMessage("Cette société est associée à des mouvements caisse.");
			return;
		}
	}
	
	/**
	 * @param ids
	 */
	public void delete_group(Long[] ids){
		for (Long artId : ids) {
			delete(artId);
		}
	}
}
