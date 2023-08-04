package appli.controller.domaine.caisse.action;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import appli.controller.domaine.caisse.ContextAppliCaisse;
import appli.controller.domaine.personnel.bean.EmployeBean;
import appli.model.domaine.caisse.service.ICaisseWebService;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;

@WorkController(nameSpace = "caisse-web", bean = EmployeBean.class, jspRootPath = "/domaine/caisse/normal/")
public class AfficheurClientAction extends ActionBase {
	@Inject
	private ICaisseWebService caisseWebService;

	public void work_init(ActionUtil httpUtil) {
		
	}
	
	 /**
     * @param currentJourneeId 
     */
    public void loadCommande(ActionUtil httpUtil){
        Long journeeId = ContextAppliCaisse.getJourneeBean().getId();        
        //
        List<CaisseMouvementPersistant> listCaisseMouvement = caisseWebService.findAllMvmAfficheur(journeeId);
        
        List<String> lastIds = (List<String>)httpUtil.getUserAttribute("lastIds");
        List<String> newIds = new ArrayList<>();
        if(lastIds == null){
        	lastIds = new ArrayList<>();
        	httpUtil.setUserAttribute("lastIds", lastIds);
        }
        for (CaisseMouvementPersistant caisseMvmP : listCaisseMouvement) {
        	if(!lastIds.contains(caisseMvmP.getRef_commande())){
        		newIds.add(caisseMvmP.getRef_commande());
        	}
		}
        
        lastIds.clear();
        for (CaisseMouvementPersistant caisseMvmP : listCaisseMouvement) {
        	lastIds.add(caisseMvmP.getRef_commande());
        }
        httpUtil.setRequestAttribute("newIds", newIds);
        httpUtil.setRequestAttribute("listCaisseMouvement", listCaisseMouvement);
        httpUtil.setDynamicUrl("/domaine/caisse_restau/afficheur/afficheurClient-detail-include.jsp");
    }
}
