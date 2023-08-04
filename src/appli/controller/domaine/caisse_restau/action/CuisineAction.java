package appli.controller.domaine.caisse_restau.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import appli.controller.domaine.caisse.ContextAppliCaisse;
import appli.controller.domaine.personnel.bean.EmployeBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM;
import appli.model.domaine.caisse.service.ICaisseService;
import appli.model.domaine.caisse.service.ICaisseWebService;
import appli.model.domaine.stock.service.IFamilleService;
import appli.model.domaine.stock.service.IMouvementService;
import appli.model.domaine.util_srv.printCom.ticket.PrintEtiquetteCuisineUtil;
import appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import appli.model.domaine.vente.persistant.CaissePersistant;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.ContextGloabalAppli;
import framework.controller.annotation.WorkController;
import framework.model.common.service.MessageService;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.StringUtil;

@WorkController(nameSpace = "caisse-web", bean = EmployeBean.class, jspRootPath = "/domaine/caisse_restau/")
public class CuisineAction extends ActionBase {
	@Inject
	private ICaisseWebService caisseWebService;
	@Inject
	private IFamilleService familleService;
	@Inject
	private ICaisseService caisseService;
	@Inject
	private IMouvementService mouvementService;
	
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
//		   if (cookie.getName().equals("pagger_cu")) {
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
		String valCollapse = StringUtil.isEmpty(httpUtil.getParameter("collapse_cuis")) ? "no" : httpUtil.getParameter("collapse_cuis");
		httpUtil.setRequestAttribute("valCollapse", valCollapse);
		
        String tp = httpUtil.getParameter("tp");
        if(tp != null) {
        	httpUtil.setUserAttribute("tp", tp);
        	httpUtil.removeUserAttribute("isp");
        	httpUtil.removeUserAttribute("sidx");
        } else {
        	tp = (String)httpUtil.getUserAttribute("tp");
        }
        
        String statutsAutorisse = (String)httpUtil.getUserAttribute("ecran_statut");
    	boolean isAutoCmdPrep = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("ECRAN_CMD_AUTO"));// Si passage à prête directement
    	boolean isValideAutorise = (StringUtil.isEmpty(statutsAutorisse) || statutsAutorisse.indexOf(";V;") != -1);
        STATUT_CAISSE_MOUVEMENT_ENUM[] statuts = null;
		if(isAutoCmdPrep || !isValideAutorise){
			statuts = new STATUT_CAISSE_MOUVEMENT_ENUM[]{STATUT_CAISSE_MOUVEMENT_ENUM.PREP};
		} else{
			statuts = new STATUT_CAISSE_MOUVEMENT_ENUM[]{STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE};
		}
		
		if(tp == null){
			tp = statuts[0].equals(STATUT_CAISSE_MOUVEMENT_ENUM.PREP) ? "enc" : "val";
			httpUtil.setUserAttribute("tp", tp);
		}
		
        // Renvoyer les checkboxes
        String[] checks = httpUtil.getRequest().getParameterValues("mvm-check");
        httpUtil.setRequestAttribute("checks", checks); 
        
        int nbrCol = getPaggerVal(httpUtil);
        
        String sidx = httpUtil.getParameter("sidx");
        if(sidx != null) {
        	httpUtil.setUserAttribute("sidx", sidx);
        } else {
        	sidx = (String)httpUtil.getUserAttribute("sidx");
        }
        
        Integer startIdx = (sidx!=null?Integer.valueOf(sidx):0);
        Long journeeId = ContextAppliCaisse.getJourneeBean().getId();
        List<ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM> statutArr = new ArrayList<>();
        ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM statut = null;
        
        if("val".equals(tp)) {
        	statutArr.add(STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE);
        	statut = STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE;
        } else if("enc".equals(tp)) {
        	statutArr.add(STATUT_CAISSE_MOUVEMENT_ENUM.PREP);
        	statut = STATUT_CAISSE_MOUVEMENT_ENUM.PREP;
        } else if("pre".equals(tp)) {
        	statutArr.add(STATUT_CAISSE_MOUVEMENT_ENUM.PRETE);
        	statut = STATUT_CAISSE_MOUVEMENT_ENUM.PRETE;
        }
        
        //
        CaissePersistant caissePersistant = ContextAppliCaisse.getCaisseBean();
        int nbrTotal = 0;
    	STATUT_CAISSE_MOUVEMENT_ENUM[] statutArray = statutArr.toArray(new STATUT_CAISSE_MOUVEMENT_ENUM[statutArr.size()]);
		nbrTotal = caisseWebService.findAllCuisine(null, caissePersistant.getId(), journeeId, null, null, null, statutArray).size();
    	List<CaisseMouvementPersistant> listCaisseMouvement = caisseWebService.findAllCuisine(null, caissePersistant.getId(), journeeId, null, (startIdx*nbrCol), nbrCol, statutArray);
    
    	// Compteurs
    	int nbrValidees = 0;
    	if("val".equals(tp)) {
    		nbrValidees = nbrTotal;
    	} else{
    		nbrValidees = caisseWebService.findAllCuisine(null, caissePersistant.getId(), journeeId, null, null, null, statuts ).size();
    	}
    	httpUtil.setRequestAttribute("NBR_VALIDEE", nbrValidees);
    	
        List<Long> listDetail = new ArrayList<>();
        Map<Long, List<Long>> mapDetail = new HashMap<>();
        Long currCaisseId = caissePersistant.getId();
        //
        for (CaisseMouvementPersistant caisseMvmP : listCaisseMouvement) {
            String[] elementsArray = StringUtil.getArrayFromStringDelim(caisseMvmP.getCaisse_cuisine(), ";");
    		if(elementsArray != null){
    			for(String element : elementsArray){
    				String[] menus = StringUtil.getArrayFromStringDelim(element, ":");
    				Long caisseId = Long.valueOf(menus[0]);
    				if(currCaisseId == caisseId){
    					Long elementId = Long.valueOf(menus[1]);
    					listDetail.add(elementId);
    				}
    			}
    		}
    		mapDetail.put(caisseMvmP.getId(), listDetail);
		}
        httpUtil.setRequestAttribute("mapDetailDest", mapDetail);
            
        // Alerte sonore --------------------------------------------------------
        if(statut.equals(STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE) || statut.equals(STATUT_CAISSE_MOUVEMENT_ENUM.PREP)) {
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
        //-----------------------------------------------------------------------
        
        httpUtil.setRequestAttribute("listCaisseMouvement", listCaisseMouvement);
        httpUtil.setRequestAttribute("nbrTotal", nbrTotal);
        httpUtil.setRequestAttribute("nbrCols", nbrCol);
        //
        if(httpUtil.getUserAttribute("NBR_TOTAL_OLD") != null && ((Integer)httpUtil.getUserAttribute("NBR_TOTAL_OLD")<nbrValidees)){
        	httpUtil.setRequestAttribute("is_new_added", true);
        }
        httpUtil.setUserAttribute("NBR_TOTAL_OLD", nbrValidees);
        
        //
        httpUtil.setDynamicUrl("/domaine/caisse_restau/cuisine/cuisine-detail-include.jsp");
    }
    
    /**
     * @param httpUtil
     */
    public void zoomCommande(ActionUtil httpUtil) {
    	Long mvmId = Long.valueOf(EncryptionUtil.decrypt(httpUtil.getParameter("mvm")));
    	CaisseMouvementPersistant mvm = (CaisseMouvementPersistant) familleService.findById(CaisseMouvementPersistant.class, mvmId);
    	Map<Integer, List<CaisseMouvementArticlePersistant>> mapHorsMenu = new HashMap<>();

    	List<Long> listDetail = new ArrayList<>();
        Map<Long, List<Long>> mapDetail = new HashMap<>();
        
        Long currCaisseId = ContextAppliCaisse.getCaisseBean().getId();
        if(StringUtil.isNotEmpty(httpUtil.getParameter("cai"))){// Cas si on vien du pilotage
        	currCaisseId = Long.valueOf(httpUtil.getParameter("cai"));
        	httpUtil.setRequestAttribute("tpStatut", httpUtil.getUserAttribute(currCaisseId+"_tp"));
        }
        
    	Map<Integer, Map<String, List<CaisseMouvementArticlePersistant>>> mapClientMvm = new HashMap<>();
    	int nbrClient = (mvm.getMax_idx_client() == null ? 1 : mvm.getMax_idx_client());
    	Integer idxMenu = 0;
    	//
    	for(int i=1; i<=nbrClient; i++){// Les clients
    		Map<String, List<CaisseMouvementArticlePersistant>> mapMenuCmd = mapClientMvm.get(i);
    		if(mapMenuCmd == null) {
    			mapMenuCmd = new HashMap();
    			mapClientMvm.put(i, mapMenuCmd);
    		}
    		
    		for(CaisseMouvementArticlePersistant caisseMvmP : mvm.getList_article()){
    			if(caisseMvmP == null || caisseMvmP.getIdx_client() == null || caisseMvmP.getIdx_client() != i){
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
	    			if(listMvm != null){
	    				listMvm.add(caisseMvmP);
	    			}
    			}
    		}
    	}

		//--------------------------------------------------------------------
        String[] elementsArray = StringUtil.getArrayFromStringDelim(mvm.getCaisse_cuisine(), ";");
		if(elementsArray != null){
			for(String element : elementsArray){
				String[] menus = StringUtil.getArrayFromStringDelim(element, ":");
				Long caisseId = Long.valueOf(menus[0]);
				if(currCaisseId == caisseId){
					Long elementId = Long.valueOf(menus[1]);
					listDetail.add(elementId);
				}
			}
		}
		mapDetail.put(mvm.getId(), listDetail);
		//-------------------------------------------------------------------
		
    	httpUtil.setRequestAttribute("mapDetailDest", mapDetail);
    	httpUtil.setRequestAttribute("currMvm", mvm);
    	httpUtil.setRequestAttribute("mapClientMvm", mapClientMvm);
    	httpUtil.setRequestAttribute("mapHorsMenu", mapHorsMenu);
    	httpUtil.setRequestAttribute("tp", httpUtil.getParameter("tp"));
    	//
    	httpUtil.setDynamicUrl("/domaine/caisse_restau/cuisine/zoom-cmd-cuisine.jsp");
    }
    
	public void print_etiquette_menu(ActionUtil httpUtil){
		Long mvmId = httpUtil.getLongParameter("cmd");
		Long elmntId = httpUtil.getLongParameter("mnu");
		//
		CaisseMouvementPersistant cmP = (CaisseMouvementPersistant) caisseService.findById(CaisseMouvementPersistant.class, mvmId);
		List<CaisseMouvementArticlePersistant> listDet = cmP.getList_article();
		
		List<CaisseMouvementArticlePersistant> listToPrint = new ArrayList<>();
		CaisseMouvementArticlePersistant det = null;
    	for (CaisseMouvementArticlePersistant cmaP : listDet){ 
			if(cmaP.getId().equals(elmntId)){
				det = cmaP;
				break;
			}
		}
    	if(det != null){
    		// Si menu prendre le détail du menu
    		if(ContextAppli.TYPE_LIGNE_COMMANDE.MENU.toString().equals(det.getType_ligne())){
    			for (CaisseMouvementArticlePersistant cmaP : listDet){
    				if(BooleanUtil.isTrue(cmaP.getIs_annule())){
    					continue;
    				}
    				if(cmaP.getMenu_idx() != null && cmaP.getMenu_idx().equals(det.getMenu_idx()) && (cmaP.getOpc_article()!=null || BooleanUtil.isTrue(cmaP.getIs_menu()))){
    					listToPrint.add(cmaP);
    				}
    			}
    		} else{
    			listToPrint.add(det);
    		}
    	}
		
    	CaisseMouvementPersistant caisseMvmP = new CaisseMouvementPersistant();
		caisseMvmP.setList_article(listToPrint);
		caisseMvmP.setRef_commande(cmP.getRef_commande());
		
		PrintEtiquetteCuisineUtil pu = new PrintEtiquetteCuisineUtil(ContextAppliCaisse.getCaisseBean().getId(), caisseMvmP);
		printData(httpUtil, pu.getPrintPosBean());
		
		//
		loadCommande(httpUtil);
	}
	
    /**-
     * @param httpUtil
     */
    public void changerStatut(ActionUtil httpUtil) {
    	String tp = (String)httpUtil.getUserAttribute("tp");
    	if(tp.equals("pre")){
    		loadCommande(httpUtil);
    		return;
    	}
    	
    	STATUT_CAISSE_MOUVEMENT_ENUM statut;
		if(tp.equals("val")) {
        	statut = STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE;
        } else if(tp.equals("enc")) {
        	statut = STATUT_CAISSE_MOUVEMENT_ENUM.PREP;
        } else {
        	loadCommande(httpUtil);
    		return;
        }
    	
		CaissePersistant caisseP = ContextAppliCaisse.getCaisseBean();
		String mnu = httpUtil.getParameter("mnu");
		String cmd = httpUtil.getParameter("cmd");
		Long[] ids = null;
		Set<Long> destockedIds = null;
		//
		if(StringUtil.isEmpty(cmd)){
	    	String[] checks = httpUtil.getRequest().getParameterValues("mvm-check");
	    	if((checks == null || checks.length == 0) && StringUtil.isEmpty(mnu)) {
	    		MessageService.addGrowlMessage("Commandes manquantes", "<b>Veuillez sélectionner au moins une commande.</b>");
	    		return;
	    	}
	    	if(checks == null){
	    		ids = new Long[1];
	    		ids[0] = Long.valueOf(mnu);
	    	} else{
	    		ids = new Long[checks.length];
	    		for (int i=0; i<checks.length; i++) {
		    		ids[i] = Long.valueOf(checks[i]);
				}
	    	}
	    	Date dtMvm = ContextAppliCaisse.getJourneeBean().getDate_journee();
	    	//
	    	destockedIds = caisseWebService.changeStatut(dtMvm, caisseP, ids, statut.toString()/*, true*/);
		} else{
			CaisseMouvementArticlePersistant cmpElement = (CaisseMouvementArticlePersistant) mouvementService.findById(CaisseMouvementArticlePersistant.class, Long.valueOf(mnu));
			ids = new Long[]{cmpElement.getOpc_mouvement_caisse().getId()};
			destockedIds = caisseWebService.changeStatutElement(caisseP, Long.valueOf(mnu), statut.toString());
		}
        
        if(destockedIds != null) {
        	Set<Long> idsFinal = destockedIds;
        	Set<CaisseMouvementPersistant> mvmsFinal = new HashSet<>();
        	for(Long mvmId : idsFinal){
        		mvmsFinal.add((CaisseMouvementPersistant) caisseService.findById(CaisseMouvementPersistant.class, mvmId));
        	}
        }
        
    	// Recharger les commandes
    	loadCommande(httpUtil);
    }
   
}
