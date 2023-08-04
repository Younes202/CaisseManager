package appli.controller.domaine.caisse_restau.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import appli.controller.domaine.administration.ParametrageRightsConstantes;
import appli.controller.domaine.caisse.ContextAppliCaisse;
import appli.controller.domaine.caisse.bean.CaisseBean;
import appli.controller.domaine.personnel.bean.EmployeBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM;
import appli.model.domaine.administration.persistant.ParametragePersistant;
import appli.model.domaine.administration.service.IParametrageService;
import appli.model.domaine.caisse.service.ICaisseService;
import appli.model.domaine.caisse.service.ICaisseWebService;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import appli.model.domaine.vente.persistant.CaissePersistant;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.common.constante.ActionConstante;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.service.MessageService;
import framework.model.common.util.DateUtil;
import framework.model.common.util.StringUtil;

@WorkController(nameSpace = "caisse-web", bean = EmployeBean.class, jspRootPath = "/domaine/caisse-web/")
public class CuisinePilotageAction extends ActionBase {
	@Inject
	private ICaisseService caisseService;
	@Inject
	private ICaisseWebService caisseWebService;
	@Inject
	private IParametrageService parametrageService;
	
	public void work_init(ActionUtil httpUtil) {
		
	}
	
//	@Override
//	public void work_edit(ActionUtil httpUtil) {
//
//		//
//		httpUtil.setDynamicUrl("/domaine/caisse_restau/caisse_conf_pilotage.jsp");
//	}
	
	/**
	 * @param httpUtil
	 * @return
	 */
	private Integer getPaggerVal(ActionUtil httpUtil) {
		String valPaggerSt = StringUtil.isEmpty(httpUtil.getParameter("pagger_slct")) ? "3" : httpUtil.getParameter("pagger_slct");
		
		return Integer.valueOf(valPaggerSt);
	 }
	
	/**
	 * @param httpUtil
	 */
	public void loadCommande(ActionUtil httpUtil){
		if( httpUtil.getParameter("tpCmd") != null) {
			httpUtil.setMenuAttribute("tpCmd",  httpUtil.getParameter("tpCmd"));
		}
		String tpCmd = (String) httpUtil.getMenuAttribute("tpCmd");
		
		if(httpUtil.getParameter("fltr") != null) {
			if("A".equals(httpUtil.getParameter("cai"))){
				httpUtil.removeMenuAttribute("fltr");	
			} else {
				httpUtil.setMenuAttribute("fltr", httpUtil.getLongParameter("cai"));
			}
		}
		
		boolean isFilterCaisse = (httpUtil.getMenuAttribute("fltr") != null);
		
		String valCollapse = StringUtil.isEmpty(httpUtil.getParameter("collapse_cu")) ? "no" : httpUtil.getParameter("collapse_cu");
		String valCollapseMnu = StringUtil.isEmpty(httpUtil.getParameter("collapse_cuMnu")) ? "no" : httpUtil.getParameter("collapse_cuMnu");
		
		httpUtil.setRequestAttribute("valCollapse", valCollapse);
		httpUtil.setRequestAttribute("valCollapseMnu", valCollapseMnu);
		
		List<CaissePersistant> listCaisse = null;
		if(isFilterCaisse) {
			listCaisse = new ArrayList<>();
			listCaisse.add(caisseService.findById(CaissePersistant.class, (Long)httpUtil.getMenuAttribute("fltr")));
		} else {
			listCaisse = caisseService.getListCaisseActive(ContextAppli.TYPE_CAISSE_ENUM.CUISINE.toString(), true);
		}
		httpUtil.setRequestAttribute("listCaisse", listCaisse);
		
		Long journeeId = ContextAppliCaisse.getJourneeBean().getId();
		 
		String tp = httpUtil.getParameter("tp");
        if(tp != null) {
        	String cais = httpUtil.getParameter("cais");
        	
        	httpUtil.setUserAttribute(cais+"_tp", tp);
        	httpUtil.removeUserAttribute(cais+"_isp");
        	httpUtil.removeUserAttribute(cais+"_sidx");
        } 
        String sidx = httpUtil.getParameter("sidx");
        if(sidx != null) {
        	String cais = httpUtil.getParameter("cais");
        	httpUtil.setUserAttribute(cais+"_sidx", sidx);
        }
        
		for (CaissePersistant caisseBean : listCaisse) {
	        Long currCaisseId = caisseBean.getId();
	        
	        tp = (String)httpUtil.getUserAttribute(currCaisseId+"_tp");
	        if(tp == null) {
	        	httpUtil.setUserAttribute(currCaisseId+"_tp", "val");
	        	tp = "val";
	        }
	        
	        // Renvoyer les checkboxes
	        String[] checks = httpUtil.getRequest().getParameterValues(currCaisseId+"_mvm-check");
	        httpUtil.setRequestAttribute(currCaisseId+"_checks", checks); 
	        
	        int nbrCol = getPaggerVal(httpUtil);
	        
	        sidx = (String)httpUtil.getUserAttribute(currCaisseId+"_sidx");
	        
	        Integer startIdx = (sidx!=null?Integer.valueOf(sidx):0);
	        List<ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM> statutArr = new ArrayList<>();
	        ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM statut = null;
	        
	        if("val".equals(tp)) {
	        	statutArr.add(STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE);
	        	statut = STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE;
	        } else if("enc".equals(tp)) {
	        	statutArr.add(STATUT_CAISSE_MOUVEMENT_ENUM.PREP);
	        	statut = STATUT_CAISSE_MOUVEMENT_ENUM.PREP;
	        } else {
	        	statutArr.add(STATUT_CAISSE_MOUVEMENT_ENUM.PRETE);
	        	statut = STATUT_CAISSE_MOUVEMENT_ENUM.PRETE;
	        }
	        STATUT_CAISSE_MOUVEMENT_ENUM[] statutArray = statutArr.toArray(new STATUT_CAISSE_MOUVEMENT_ENUM[statutArr.size()]);
	        //
	        int nbrTotal = caisseWebService.findAllCuisine(null, currCaisseId, journeeId, tpCmd, null, null, statutArray).size();
	    	List<CaisseMouvementPersistant> listCaisseMouvement = caisseWebService.findAllCuisine(null, currCaisseId, journeeId, tpCmd, (startIdx*nbrCol), nbrCol, statutArray);
	    
	    	 // Compteurs -------------------------------
	    	int nbrValidees = 0;
	    	if("val".equals(tp)) {
	    		nbrValidees = nbrTotal;
	    	} else{
	    		nbrValidees = caisseWebService.findAllCuisine(null, currCaisseId, journeeId, tpCmd, null, null, new STATUT_CAISSE_MOUVEMENT_ENUM[]{STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE} ).size();
	    	}
	    	httpUtil.setRequestAttribute(currCaisseId+"_NBR_VALIDEE", nbrValidees);
	    	// -----------------------------------------------------------------------------
	    	
	        List<Long> listDetail = new ArrayList<>();
	        Map<Long, List<Long>> mapDetail = new HashMap<>();
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
	        httpUtil.setRequestAttribute(currCaisseId+"_mapDetailDest", mapDetail);
	            
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
	        //
	        if(httpUtil.getUserAttribute(caisseBean.getId()+"_NBR_TOTAL_OLD") != null && ((Integer)httpUtil.getUserAttribute(caisseBean.getId()+"_NBR_TOTAL_OLD")<nbrValidees)){
	        	httpUtil.setRequestAttribute("is_new_added", true);
	        }
	        httpUtil.setUserAttribute(caisseBean.getId()+"_NBR_TOTAL_OLD", nbrValidees);
	        
	        httpUtil.setRequestAttribute(currCaisseId+"_listCaisseMouvement", listCaisseMouvement);
	        httpUtil.setRequestAttribute(currCaisseId+"_nbrTotal", nbrTotal);
	        httpUtil.setRequestAttribute(currCaisseId+"_nbrCols", nbrCol);
		}
		httpUtil.setDynamicUrl("/domaine/caisse_restau/cuisine/pilotage-cuisinee-detail-include.jsp");
	}
	
	/**-
     * @param httpUtil
     */
    public void changerStatut(ActionUtil httpUtil) {
		Long currentCaisseId = Long.valueOf(httpUtil.getParameter("cais"));
		String tp = (String)httpUtil.getUserAttribute(currentCaisseId+"_tp");

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
    	 
		CaissePersistant caisseP = (CaissePersistant)caisseService.findById(CaissePersistant.class, currentCaisseId);
		
		String mnu = httpUtil.getParameter("mnu");
		String cmd = httpUtil.getParameter("cmd");
		Long[] ids = null;
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
	    	caisseWebService.changeStatut(dtMvm, caisseP, ids, statut.toString()/*, true*/);
		} else{
			caisseWebService.changeStatutElement(caisseP, Long.valueOf(mnu), statut.toString());
		}
    	
    	// Recharger les commandes
    	loadCommande(httpUtil);
    }
	
	/**
	 * @param httpUtil
	 */
	public void initTransfererCmdCuisine(ActionUtil httpUtil){
		Long currentCaisseId = Long.valueOf(httpUtil.getParameter("cais")); 
		List<CaissePersistant> listCaisse = new ArrayList<>();
		
		List<CaisseBean> listCaisseCuisineActive = caisseService.getListCaisseCuisineActive();
		for(CaissePersistant caisse : listCaisseCuisineActive){
			if(currentCaisseId != caisse.getId()){
				listCaisse.add(caisse);
			}
		}
		
		httpUtil.setRequestAttribute("caisseId", currentCaisseId);
		
		httpUtil.setMenuAttribute("mvmId", httpUtil.getParameter("mnu"));
		httpUtil.setMenuAttribute("detailId", httpUtil.getParameter("det"));
		httpUtil.setRequestAttribute("listCaisse", listCaisse);
		
		httpUtil.setDynamicUrl("/domaine/caisse_restau/normal/transfert_cmd_cuisine_popup.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void transfererCmdCuisine(ActionUtil httpUtil){
		Long currentCaisseId = Long.valueOf(httpUtil.getParameter("cais"));
		String tp = (String)httpUtil.getUserAttribute(currentCaisseId+"_tp");
    	STATUT_CAISSE_MOUVEMENT_ENUM statut = null;
		if(tp.equals("val")) {
        	statut = STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE;
        } else if(tp.equals("enc")) {
        	statut = STATUT_CAISSE_MOUVEMENT_ENUM.PREP;
        } else if(tp.equals("pre")){
        	statut = STATUT_CAISSE_MOUVEMENT_ENUM.PRETE;
        }
		
		Long mvmId = StringUtil.isNotEmpty(httpUtil.getMenuAttribute("mvmId")) ? Long.valueOf(""+httpUtil.getMenuAttribute("mvmId")) : null;
		Long detailId = StringUtil.isNotEmpty(httpUtil.getMenuAttribute("detailId")) ? Long.valueOf(""+httpUtil.getMenuAttribute("detailId")) : null;
		
		Long caisseIdTarget = httpUtil.getLongParameter("caisse_target");
		//
		caisseService.transfererCmdCuisine(statut, mvmId, detailId, currentCaisseId, caisseIdTarget);
		//
		
		loadCommande(httpUtil);
	}
	
	public void init_print_fiche(ActionUtil httpUtil){
		// Liste des imprimantes
		httpUtil.setRequestAttribute("list_imprimante", ParametrageRightsConstantes.getListPrinters());
        //
        String[][] orientationBalance = {{"L", "Paysage"}, {"LR", "Paysage inversé"}, {"P", "Portrait"}};
        httpUtil.setRequestAttribute("orientationBalance", orientationBalance);
        
        String[] imprimantesParams = {"CAISSE_CUISINE_ETQ"};
        for (String param : imprimantesParams) {
        	List<ParametragePersistant> params = parametrageService.getParameterByGroupe(param);
			httpUtil.setRequestAttribute("param_"+param, params);			
		}
		
        httpUtil.setMenuAttribute("is_cuis", true);
        
        httpUtil.setDynamicUrl("/domaine/stock/print/etiquette_conf.jsp");
	}
	public void update_params_imprimante(ActionUtil httpUtil) {
		Map<String, Object> params = httpUtil.getValuesByStartName("param_");
		parametrageService.updateParams(params);
		
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "", "Les paramétres de l'imprimante sont mise à jour.");
		//
		httpUtil.setDynamicUrl("admin.parametrage.work_edit");
	}
	
	/**
	 * @param httpUtil
	 */
	public void work_post(ActionUtil httpUtil) {
		if(httpUtil.getAction().equals(ActionConstante.INIT_UPDATE)){
			work_edit(httpUtil);
		}
	}
}
