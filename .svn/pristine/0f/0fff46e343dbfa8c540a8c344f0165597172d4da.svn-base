package appli.controller.domaine.caisse.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import appli.controller.domaine.caisse.ContextAppliCaisse;
import appli.controller.domaine.personnel.bean.EmployeBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM;
import appli.model.domaine.caisse.service.ICaisseWebService;
import appli.model.domaine.stock.service.IFamilleService;
import appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.common.service.MessageService;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.StringUtil;

@WorkController(nameSpace = "caisse-web", bean = EmployeBean.class, jspRootPath = "/domaine/caisse_restau/presentoire/")
public class PresentoireAction extends ActionBase {
	@Inject
	private ICaisseWebService caisseWebService;
	@Inject
	private IFamilleService familleService;

	public void work_init(ActionUtil httpUtil) {
		
	}
	
	/**
	 * @param httpUtil
	 * @return
	 */
	private Integer getPaggerVal(ActionUtil httpUtil) {
//		Cookie[] cookies = httpUtil.getRequest().getCookies();
//		String valPaggerSt = "4";
//		if (cookies != null) {
//		 for (Cookie cookie : cookies) {
//		   if (cookie.getName().equals("pagger_pr")) {
//			   valPaggerSt = cookie.getValue();
//		    }
//		  }
//		}
		String valPaggerSt = StringUtil.isEmpty(httpUtil.getParameter("pagger_slct")) ? "4" : httpUtil.getParameter("pagger_slct");
		return Integer.valueOf(valPaggerSt);
	 }
	
	 /**
     * @param currentJourneeId 
     */
    public void loadCommande(ActionUtil httpUtil){
        String tp = httpUtil.getParameter("tp");
        if(tp != null) {
        	httpUtil.setUserAttribute("tp", tp);
        	httpUtil.removeUserAttribute("sidx");
        } else {
        	tp = (String)httpUtil.getUserAttribute("tp");
        }
        if(StringUtil.isEmpty(tp)) {
        	tp = "val";
        }
        
        // Renvoyer les checkboxes
        String[] checks = httpUtil.getRequest().getParameterValues("mvm-check");
        httpUtil.setRequestAttribute("checks", checks);
        
        boolean isMobile = ContextAppli.APPLI_ENV.cais_mob.toString().equals(httpUtil.getUserAttribute("CURRENT_ENV"));
		boolean isMobileFrom = isMobile;
        boolean isServeurProfil = ContextAppli.getUserBean().isInProfile("SERVEUR");
        
        int nbrCol = isMobileFrom ? 5 : getPaggerVal(httpUtil); 
        
        String sidx = httpUtil.getParameter("sidx");
        if(sidx != null) {
        	httpUtil.setUserAttribute("sidx", sidx);
        } else {
        	sidx = (String)httpUtil.getUserAttribute("sidx");
        }
        
        Integer startIdx = (sidx!=null?Integer.valueOf(sidx):0);
        
        List<CaisseMouvementPersistant> listCaisseMouvement = null;
        Long journeeId = ContextAppliCaisse.getJourneeBean().getId();
        
        List<ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM> statutArr = new ArrayList<>();
        
        if(tp.equals("val")) {
        	statutArr.add(STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE);
        	statutArr.add(STATUT_CAISSE_MOUVEMENT_ENUM.PREP);
        } else if(tp.equals("pre")) {
        	statutArr.add(STATUT_CAISSE_MOUVEMENT_ENUM.PRETE);
        } else {
        	statutArr.add(STATUT_CAISSE_MOUVEMENT_ENUM.LIVRE);
        }
        
        STATUT_CAISSE_MOUVEMENT_ENUM[] statutArray = statutArr.toArray(new STATUT_CAISSE_MOUVEMENT_ENUM[statutArr.size()]);
        Long serveurId = isServeurProfil ? ContextAppli.getUserBean().getId() : null;
        //
        int nbrTotal = caisseWebService.findAllCuisine(serveurId, null, journeeId, null, null, null, statutArray).size();
        listCaisseMouvement = caisseWebService.findAllCuisine(serveurId, null, journeeId, null, (startIdx*nbrCol), nbrCol, statutArray);
        
        // Alerte sonore --------------------------------------------------------
        if(!isMobileFrom){
	        if(tp.equals("val")) {
		        Integer DELAIS_ALERT_MINUTE = (Integer) httpUtil.getUserAttribute("DELAIS_ALERT_MINUTE");
				if(DELAIS_ALERT_MINUTE != null) {
			        Date now = new Date();
			        boolean isTempsDepasse = false;
			         
			        for (CaisseMouvementPersistant mvm : listCaisseMouvement) {
			            Date dateCmd = mvm.getDate_creation();
			            if(dateCmd == null){
			                continue;    
			            }
			            
			            int minutes = DateUtil.getDiffMinuts(dateCmd, now);
			            if (minutes > DELAIS_ALERT_MINUTE) {
			                isTempsDepasse = true;
			                break;
			            }
			        }
			        // alertes font et son
			        if (isTempsDepasse){
			        	 httpUtil.setRequestAttribute("alert_sonore", true);
			        }
		        }
	        }
        }
        //-----------------------------------------------------------------------
        
        httpUtil.setRequestAttribute("listCaisseMouvement", listCaisseMouvement);
        httpUtil.setRequestAttribute("nbrTotal", nbrTotal);
        httpUtil.setRequestAttribute("nbrCols", nbrCol);
        //
        if(ContextAppli.APPLI_ENV.cais.toString().equals(httpUtil.getUserAttribute("CURRENT_ENV"))
        		|| isMobile){
        	if(httpUtil.getParameter("is_ref") != null){
        		httpUtil.setDynamicUrl("/domaine/caisse_restau/"+(isMobile?"mobile":"cuisine")+"/cuisine-caisse-main-tracker.jsp");
        	} else{
        		httpUtil.setDynamicUrl("/domaine/caisse_restau/"+(isMobile?"mobile":"cuisine")+"/cuisine-caisse-tracker.jsp");
        	}
        } else{
        	httpUtil.setDynamicUrl("/domaine/caisse_restau/presentoire/presentoire-detail-include.jsp");        	
        }
    }
    
    /**-
     * @param httpUtil
     */
    public void changerStatut(ActionUtil httpUtil) {
    	STATUT_CAISSE_MOUVEMENT_ENUM statut = STATUT_CAISSE_MOUVEMENT_ENUM.PRETE;
    	
    	String mnu = httpUtil.getParameter("mnu");
    	String[] checks = httpUtil.getRequest().getParameterValues("mvm-check");
    	if((checks == null || checks.length == 0) && StringUtil.isEmpty(mnu)) {
    		MessageService.addGrowlMessage("Commandes manquantes", "<b>Veuillez sélectionner au moins une commande.</b>");
    		return;
    	}
    	
    	//
    	Date dtMvm = ContextAppliCaisse.getJourneeBean().getDate_journee();
    	String cmd = httpUtil.getParameter("cmd");
    	if(StringUtil.isEmpty(cmd)){
    		Long[] ids = null;
        	if(checks == null){
        		ids = new Long[1];
        		ids[0] = Long.valueOf(mnu);
        	} else{
    	    	ids = new Long[checks.length];
    	    	for (int i=0; i<checks.length; i++) {
    	    		ids[i] = Long.valueOf(checks[i]);
    			}
        	}
        	
    		caisseWebService.changeStatut(dtMvm, null, ids, statut.toString());
    	} else{
    		caisseWebService.changeStatutElement(null, Long.valueOf(mnu), statut.toString());
    	}
    	// Recharger les commandes
    	loadCommande(httpUtil);
    }
    
    /**
     * @param httpUtil
     */
    public void zoomCommande(ActionUtil httpUtil) {
    	Long mvmId = Long.valueOf(EncryptionUtil.decrypt(httpUtil.getParameter("mvm")));
    	CaisseMouvementPersistant mvm = (CaisseMouvementPersistant) familleService.findById(CaisseMouvementPersistant.class, mvmId);
    	Map<Integer, List<CaisseMouvementArticlePersistant>> mapHorsMenu = new HashMap<>();

    	Map<Integer, Map<String, List<CaisseMouvementArticlePersistant>>> mapClientMvm = new HashMap<>();
    	int nbrClient = (mvm.getMax_idx_client() == null ? 1 : mvm.getMax_idx_client());
    	Integer idxMenu = 0;
    	//
    	for(int i=1; i<=nbrClient; i++){// Les clients
    		Map<String, List<CaisseMouvementArticlePersistant>> mapMenuCmd = mapClientMvm.get(i);
    		if(mapMenuCmd == null) {
    			mapMenuCmd = new HashMap<>();
    			mapClientMvm.put(i, mapMenuCmd);
    		}
    		
    		for(CaisseMouvementArticlePersistant caisseMvmP : mvm.getList_article()){
    			if(caisseMvmP.getIdx_client() != i){
    				continue;
    			}
    			
    			if(BooleanUtil.isTrue(caisseMvmP.getIs_annule()) || (caisseMvmP.getIdx_client()!=null && caisseMvmP.getIdx_client()!=i)){
		           continue;
    			}
    			
    			if("MENU".equals(caisseMvmP.getType_ligne()) && caisseMvmP.getOpc_menu() == null) {
    				idxMenu++;
    				List<CaisseMouvementArticlePersistant> listMvm = new ArrayList<CaisseMouvementArticlePersistant>();
    				mapMenuCmd.put(""+idxMenu, listMvm);
    				listMvm.add(caisseMvmP);
    			} else if(caisseMvmP.getMenu_idx() == null) {
    				List<CaisseMouvementArticlePersistant> listHorsMenu = mapHorsMenu.get(i);
    				if(listHorsMenu == null) {
    					listHorsMenu = new ArrayList<>();
    					mapHorsMenu.put(i, listHorsMenu);
    				}
    				listHorsMenu.add(caisseMvmP);
    			} else {
	    			List<CaisseMouvementArticlePersistant> listMvm = mapMenuCmd.get(""+idxMenu);
	    			listMvm.add(caisseMvmP);
    			}
    		}
    	}
		
    	httpUtil.setRequestAttribute("currMvm", mvm);
    	httpUtil.setRequestAttribute("mapClientMvm", mapClientMvm);
    	httpUtil.setRequestAttribute("mapHorsMenu", mapHorsMenu);
    	httpUtil.setRequestAttribute("tp", httpUtil.getParameter("tp"));
    	//
    	httpUtil.setDynamicUrl("/domaine/caisse_restau/presentoire/zoom-cmd-presentoire.jsp");
    }
}
