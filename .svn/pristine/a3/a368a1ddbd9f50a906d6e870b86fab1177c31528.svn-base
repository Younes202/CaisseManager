package appli.controller.domaine.caisse_restau.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import appli.controller.domaine.administration.ParametrageRightsConstantes;
import appli.controller.domaine.caisse.ContextAppliCaisse;
import appli.controller.domaine.caisse.action.CaisseWebBaseAction;
import appli.controller.domaine.caisse.bean.CaisseBean;
import appli.controller.domaine.personnel.bean.ClientBean;
import appli.model.domaine.administration.persistant.ParametragePersistant;
import appli.model.domaine.administration.service.IParametrageService;
import appli.model.domaine.caisse.service.ICaisseWebService;
import appli.model.domaine.caisse.service.IMenuCompositionService;
import appli.model.domaine.fidelite.persistant.CarteFideliteClientPersistant;
import appli.model.domaine.fidelite.service.ICarteFideliteClientService;
import appli.model.domaine.administration.service.IClientService;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import appli.model.domaine.vente.persistant.MenuCompositionPersistant;
import framework.controller.ActionUtil;
import framework.controller.ContextGloabalAppli;
import framework.controller.ControllerUtil;
import framework.controller.annotation.WorkController;
import framework.controller.bean.PagerBean;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StringUtil;

@WorkController(nameSpace = "caisse-web", bean = CaisseBean.class, jspRootPath = "/domaine/caisse_restau/client-place/")
public class CaisseWebClientAction extends CaisseWebBaseAction {
	@Inject
	private IMenuCompositionService menuCompService;
	@Inject
	private ICaisseWebService caisseWebService;
	@Inject
	private ICarteFideliteClientService carteFideliteService;
	@Inject
	private IClientService clientService;
	@Inject
	private IParametrageService paramsService;
	
	public void work_init(ActionUtil httpUtil) {
		httpUtil.setFormReadOnly();
	}

	public void editCaisseMenu(ActionUtil httpUtil){
		// Familles
		PagerBean pagerBean = ControllerUtil.managePager(httpUtil.getRequest(), "FAM");
		List<FamillePersistant> listFamille = caisseWebService.getListFamilleCaissePagination(ContextAppliCaisse.getCaisseBean().getId(), pagerBean);
		httpUtil.setRequestAttribute("listFamille", listFamille);
		
		// Menus
		pagerBean = ControllerUtil.managePager(httpUtil.getRequest(), "MNU");
		List<MenuCompositionPersistant> listMenu = menuCompService.getListeMenuCaissePagination(ContextAppliCaisse.getCaisseBean().getId(), pagerBean);
		httpUtil.setRequestAttribute("listMenu", listMenu);
					
		initNewCommande(httpUtil);
		httpUtil.setDynamicUrl("/domaine/caisse_restau/client-place/caisse-main.jsp");
	}
	
	public void initAuhentification(ActionUtil httpUtil) {
		httpUtil.setDynamicUrl("/domaine/caisse_restau/client-place/commande-caisse-client/main-paiement.jsp");
	}
	public void backAuhentification(ActionUtil httpUtil) {
		httpUtil.setDynamicUrl("/domaine/caisse_restau/client-place/commande-caisse-client/authentification.jsp");
	}
	 public void initPaiement(ActionUtil httpUtil){
		httpUtil.setDynamicUrl("/domaine/caisse_restau/client-place/commande-caisse-client/paiement-loading.jsp");
	 }
	 
	 public void loginCmd(ActionUtil httpUtil) throws Exception{
		HttpServletRequest request = httpUtil.getRequest();
		String mail = (String)ControllerUtil.getParam(request, "login");
		String pw = (String)ControllerUtil.getParam(request, "password");
		
		String badge = (String)ControllerUtil.getParam(request, "tkn");
		CarteFideliteClientPersistant carteBean = null;
		
		ClientBean clientBean = null;
		if(StringUtil.isNotEmpty(badge)){
			badge = badge.trim().replaceAll("", "");
			
			carteBean = carteFideliteService.getClientCarteByCodeBarre(badge.trim()); 
			if(carteBean == null){
				MessageService.addGrowlMessage("Badge non enregistré", "Ce badge n'a pas été encore enregistré.");
				return;
			}
			clientBean = (ClientBean) ServiceUtil.persistantToBean(ClientBean.class, carteBean.getOpc_client());
			
			// Si le compte est désactivé
			if(BooleanUtil.isTrue(clientBean.getIs_disable())){
				MessageService.addGrowlMessage("Compte désactivé", "Cet utilisateur est désactivé");
				return;
			}
		} else {
			mail = (mail != null) ? mail.trim() : null; 
			pw = (pw != null) ? pw.trim() : null;
			//
			clientBean = clientService.getClientByLoginAndPw(mail, EncryptionUtil.encrypt(pw)); 
			if(clientBean == null){
				MessageService.addFieldMessage("login", "Le login ou le mot de passse est erroné");
				return;
			}
			// Si le compte est désactivé
			if(BooleanUtil.isTrue(clientBean.getIs_disable())){
				MessageService.addFieldMessage("login", "L'utilisateur est désactivé");
				return;
			}
			
			carteBean = carteFideliteService.getCarteClientActive(clientBean.getId());
		}
		
		ParametrageRightsConstantes.loadAllMapGlobParams(false);
		//
//		Map<String, String> mapParams = new HashMap<>();
//		List<ParametragePersistant> listParams = paramsService.findAll();
//		for (ParametragePersistant parametrageP : listParams) {
//			mapParams.put(parametrageP.getCode(), parametrageP.getValeur());
//		}
//		MessageService.getGlobalMap().put("GLOBAL_CONFIG",  mapParams); 
		
		// Paramétrage
		MessageService.getGlobalMap().put("GLOBAL_ETABLISSEMENT", paramsService.findAll(EtablissementPersistant.class).get(0));
		
		//Maj date derniere connexion
		clientBean.setDate_connexion(new Date());
		clientService.update(clientBean);
		
		CaisseMouvementPersistant CURRENT_COMMANDE = (CaisseMouvementPersistant) httpUtil.getUserAttribute("CURRENT_COMMANDE");
		CURRENT_COMMANDE.setOpc_client(clientBean);
		
		// Soldes
		boolean isPoints =  StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("POINTS"));
		if(isPoints && carteBean != null){
//			httpUtil.setRequestAttribute("pointFidelite", carteBean.getTotal_point());
			httpUtil.setRequestAttribute("soldeFidelite", carteBean.getMtt_total());
			//
			if(CURRENT_COMMANDE.getMtt_commande_net() != null && !BigDecimalUtil.isZero(carteBean.getMtt_total()) 
						 && CURRENT_COMMANDE.getMtt_commande_net().compareTo(carteBean.getMtt_total()) <= 0 // Solde carte supérieur ou égal montant commande
						 && CURRENT_COMMANDE.getMtt_commande_net().compareTo(carteBean.getOpc_carte_fidelite().getMtt_seuil()) >= 0 // Si on a atteint le seuil
						 && CURRENT_COMMANDE.getMtt_commande_net().compareTo(carteBean.getOpc_carte_fidelite().getMtt_plafond()) <= 0){// Total en dessous du plafond
				  httpUtil.setRequestAttribute("IS_POINT", true);
			 }
		}
		
		boolean isPortefeuille =  StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("PORTEFEUILLE"));
		if(isPortefeuille){
			if(BooleanUtil.isTrue(clientBean.getIs_portefeuille())){
				httpUtil.setRequestAttribute("soldePortefeuille", clientBean.getSolde_portefeuille());
				//
				if(CURRENT_COMMANDE.getMtt_commande_net() != null && !BigDecimalUtil.isZero(clientBean.getSolde_portefeuille()) 
							&& CURRENT_COMMANDE.getMtt_commande_net().compareTo(clientBean.getSolde_portefeuille()) <= 0){// Montant commande inféreur ou égal solde portefeuille
					httpUtil.setRequestAttribute("IS_PORTEFEUILLE", true);
				} 
			}
		}
		 
		httpUtil.setDynamicUrl("/domaine/caisse_restau/client-place/commande-caisse-client/paiement-loading.jsp");
	 }
}
