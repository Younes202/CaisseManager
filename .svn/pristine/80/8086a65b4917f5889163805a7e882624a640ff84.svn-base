package appli.model.domaine.fidelite.validator;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.fidelite.bean.CarteFideliteBean;
import appli.model.domaine.fidelite.dao.ICarteFideliteDao;
import appli.model.domaine.fidelite.persistant.CarteFideliteClientPersistant;
import appli.model.domaine.fidelite.persistant.CarteFidelitePersistant;
import framework.controller.ContextGloabalAppli;
import framework.model.common.service.MessageService;
import framework.model.common.util.StringUtil;

@Named
public class CarteFideliteValidator {
	
	@Inject
	private ICarteFideliteDao carteFideliteDao;
	
	/*
	 * 
	 */
	public void updateCreateValidator(CarteFideliteBean carteFideliteBean){
		boolean isPoints =  StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("POINTS"));
		if(!isPoints){
			MessageService.addBannerMessage("Le module des cartes de fidelité n'est pas activé dans le paramétrage.");
			return;
		}
		
		if(carteFideliteDao.isNotUnique(carteFideliteBean, "libelle")){
			MessageService.addFieldMessage("carteFidelite.libelle", "Cette valeur exste déjà");
		}
		
	}
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	
	public void update(CarteFideliteBean carteFideliteBean){
		updateCreateValidator(carteFideliteBean);
	}
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void create(CarteFideliteBean carteFideliteBean){
		updateCreateValidator(carteFideliteBean);
	}
	
	public void delete(Long id){
		// Contrôle mouvement
		CarteFidelitePersistant cfP = carteFideliteDao.findById(id);
		List<CarteFideliteClientPersistant> listCarte = cfP.getList_cartes_client();
		if(listCarte.size()>0){
			MessageService.addBannerMessage("Cette carte est liées à des cartes clients.");
		}
	}

}
