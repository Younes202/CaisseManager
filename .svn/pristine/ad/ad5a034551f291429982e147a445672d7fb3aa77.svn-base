package appli.controller.domaine.caisse.action.mobile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import appli.controller.domaine.caisse.ContextAppliCaisse;
import appli.controller.domaine.personnel.bean.EmployeBean;
import appli.model.domaine.caisse.service.ICaisseService;
import appli.model.domaine.caisse.service.ICaisseWebService;
import appli.model.domaine.caisse.service.IMenuCompositionService;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import appli.model.domaine.vente.persistant.MenuCompositionPersistant;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.common.util.ServiceUtil;

@WorkController(nameSpace = "caisse-mobile", bean = EmployeBean.class, jspRootPath = "/domaine/caisse/mobile-client/")
public class FamilleNavigateAction extends ActionBase {
	public void work_init(ActionUtil httpUtil) {
		
	}
	
	/**
	 * @param httpUtil
	 */
	public void loadDatNav(ActionUtil httpUtil) {
		ICaisseService caisseService = (ICaisseService)ServiceUtil.getBusinessBean(ICaisseService.class);
		ICaisseWebService caisseWebService = (ICaisseWebService)ServiceUtil.getBusinessBean(ICaisseWebService.class);
		IMenuCompositionService menuCompoService = (IMenuCompositionService)ServiceUtil.getBusinessBean(IMenuCompositionService.class);
		
		httpUtil.setRequestAttribute("listArticleFavoris", caisseService.getFavorisCaisse(null));
		// Familles
		List<FamillePersistant> listFamille = caisseWebService.getListFamilleCaissePagination(Long.valueOf(-1), null);
		httpUtil.setRequestAttribute("listFamille", listFamille);
		
		// Menus
		List<MenuCompositionPersistant> listMenu = menuCompoService.getListeMenuCaissePagination(Long.valueOf(-1), null);
		httpUtil.setRequestAttribute("listMenu", listMenu);
		
		CaisseMouvementPersistant CURRENT_COMMANDE = (CaisseMouvementPersistant) httpUtil.getUserAttribute("CURRENT_COMMANDE");
		if (CURRENT_COMMANDE == null) {
			CaisseMouvementPersistant newCmd = new CaisseMouvementPersistant();
	        newCmd.setRef_commande(""+System.currentTimeMillis());
	        newCmd.setDate_vente(new Date());
	        newCmd.setOpc_caisse_journee(ContextAppliCaisse.getJourneeCaisseBean());
	        //
	        newCmd.setList_article(new ArrayList<>());
	        newCmd.setList_offre(new ArrayList<>());
	        
			newCmd.setMax_idx_client(1);
			httpUtil.setUserAttribute("CURRENT_IDX_CLIENT", 1);// Index client
			
			httpUtil.setUserAttribute("CURRENT_COMMANDE", newCmd	);
		}
		
		httpUtil.setDynamicUrl("/domaine/caisse/mobile-client/home.jsp");	
	}
}
