package appli.model.domaine.fidelite.validator;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

import appli.controller.domaine.fidelite.bean.CarteFideliteClientBean;
import appli.model.domaine.fidelite.dao.ICarteFideliteClientDao;
import appli.model.domaine.fidelite.persistant.CarteFideliteClientPersistant;
import framework.controller.ContextGloabalAppli;
import framework.model.common.service.MessageService;
import framework.model.common.util.StringUtil;

@Named
public class CarteFideliteClientValidator {
	@Inject
	private ICarteFideliteClientDao carteFideliteClientDao;
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	
	public void updateCreateValidator(CarteFideliteClientBean carteFideliteClientBean){
		boolean isPoints =  StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("POINTS"));
		if(!isPoints){
			MessageService.addBannerMessage("Le module des cartes de fidelité n'est pas activé dans le paramétrage.");
			return;
		}
		
		// Code barre unique
		if(carteFideliteClientDao.isNotUnique(carteFideliteClientBean, "code_barre")){
			MessageService.addFieldMessage("carteFideliteClient.code_barre", "Cette valeur exste déjà");
		}
		// Carte / client unique
		String req = "from CarteFideliteClientPersistant WHERE opc_carte_fidelite.id=:carteId and opc_client.id=:clientId";
		if(carteFideliteClientBean.getId() != null){
			req = req + " and id!=:currId";
		}
		Query query = carteFideliteClientDao.getQuery(req)
			.setParameter("carteId", carteFideliteClientBean.getOpc_carte_fidelite().getId())
			.setParameter("clientId", carteFideliteClientBean.getOpc_client().getId());
		if(carteFideliteClientBean.getId() != null){
			query.setParameter("currId", carteFideliteClientBean.getId());
		}
	
		if(query.getResultList().size() > 0){
			MessageService.addBannerMessage("Cette carte est déjà attribué à ce client.");
		}
		// Controle dates
		if(carteFideliteClientBean.getDate_debut() != null && carteFideliteClientBean.getDate_fin() != null && carteFideliteClientBean.getDate_debut().after(carteFideliteClientBean.getDate_fin())){
			MessageService.addBannerMessage("La date de début doit être antérieure ou égale la date de début.");
		}
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	
	public void update(CarteFideliteClientBean carteFideliteClientBean){
		updateCreateValidator(carteFideliteClientBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	
	public void create (CarteFideliteClientBean carteFideliteClientBean){
		updateCreateValidator(carteFideliteClientBean);
	}
	
	public void delete(Long id){
		// Contrôle mouvement
		CarteFideliteClientPersistant cfP = carteFideliteClientDao.findById(id);
		if(cfP.getList_point_gagne().size()>0){
			MessageService.addBannerMessage("Cette carte est déjà utilisée. Veuillez la désactiver au lieu de la supprimer.");
		} else if(cfP.getList_point_conso().size()>0){
			MessageService.addBannerMessage("Cette carte est déjà utilisée. Veuillez la désactiver au lieu de la supprimer.");
		}
	}
}
