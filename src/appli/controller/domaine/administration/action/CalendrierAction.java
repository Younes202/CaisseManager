package appli.controller.domaine.administration.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.inject.Inject;

import org.hibernate.criterion.Order;

import appli.controller.domaine.fidelite.bean.CarteFideliteClientBean;
import appli.controller.domaine.personnel.bean.ClientBean;
import appli.controller.domaine.personnel.bean.PlanningBean;
import appli.controller.domaine.util_erp.ContextAppli.SOFT_ENVS;
import appli.model.domaine.administration.persistant.AgencementPersistant;
import appli.model.domaine.administration.service.IAgencementService;
import appli.model.domaine.administration.service.IClientService;
import appli.model.domaine.fidelite.persistant.CarteFideliteClientPersistant;
import appli.model.domaine.fidelite.persistant.CarteFidelitePersistant;
import appli.model.domaine.fidelite.service.ICarteFideliteClientService;
import appli.model.domaine.fidelite.service.ICarteFideliteService;
import appli.model.domaine.personnel.persistant.PlanningPersistant;
import appli.model.domaine.personnel.persistant.TypePlanningPersistant;
import appli.model.domaine.personnel.service.IPlanningService;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.beanContext.DataValuesPersistant;
import framework.model.beanContext.VillePersistant;
import framework.model.common.constante.ActionConstante;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.service.MessageService;
import framework.model.common.util.ControllerBeanUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.DateUtil.TIME_ENUM;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;

@WorkController(nameSpace = "caisse-web", bean = PlanningBean.class, jspRootPath = "/domaine/caisse/normal/")
public class CalendrierAction extends ActionBase {
	@Inject
	private IPlanningService planningService;
	@Inject
	private IClientService clientService;
	@Inject
	private IAgencementService agencementService;
	@Inject
	private ICarteFideliteService carteFideliteService;
	@Inject
	private ICarteFideliteClientService carteClientService;
	
	public void work_init(ActionUtil httpUtil) {
		PlanningBean planningBean = (PlanningBean) httpUtil.getViewBean();
		String action = httpUtil.getAction();
		boolean isRestau = SOFT_ENVS.restau.toString().equals(StrimUtil.getGlobalConfigPropertie("context.soft"));
		//
		if(planningBean != null &&
				(action.equals(ActionConstante.MERGE) || action.equals(ActionConstante.CREATE) || action.equals(ActionConstante.UPDATE))){
    		String clients = ";";
    		if(planningBean.getClients_array() != null){
    			for (String clientId :  planningBean.getClients_array()) {
    				if(StringUtil.isNotEmpty(clientId)){
    					clients = clients + clientId + ";";
    				}
    			}
    		}
    		planningBean.setClients_str(clients);
    		
    		//
    		if(isRestau) {
	    		String lieux = ";";
	    		String[] lieuxArray = httpUtil.getRequest().getParameterValues("planning.lieu_array[]");
	    		if(lieuxArray != null){
	    			for (String lieuId :  lieuxArray) {
	    				if(StringUtil.isNotEmpty(lieuId)){
	    					lieux = lieux + lieuId + ";";
	    				}
	    			}
	    		}
	    		planningBean.setLieu_str(lieux);
	    	}
		}
		httpUtil.setRequestAttribute("listTypePlanning", planningService.findAll(TypePlanningPersistant.class, Order.asc("libelle")));
		httpUtil.setRequestAttribute("listClient", clientService.getClientsActifs());
		
		MessageService.getGlobalMap().put("NO_ETS", true);
		httpUtil.setRequestAttribute("listVille", clientService.getListData(VillePersistant.class, "opc_region.libelle, libelle"));
		MessageService.getGlobalMap().remove("NO_ETS");
		
		httpUtil.setRequestAttribute("list_mode_paiement", new String[][]{{"CARTE", "CARTE"}, {"CHEQUE", "CHEQUE"}, {"CHEQUE. DEJ", "CHEQUE. DEJ"}, {"ESPECES", "ESPECES"}});
		httpUtil.setRequestAttribute("listDataValueForm", clientService.loadDataForm(null, "CLIENT"));
		
		if(isRestau) {
			httpUtil.setRequestAttribute("liste_carte", clientService.findAll(CarteFidelitePersistant.class, Order.asc("libelle")));
			
			Map<String, List<String>> mapLieu = new HashMap<>();
			List<AgencementPersistant> listAgencement = agencementService.findAgencementByCalndrier();
			for (AgencementPersistant agencementBean : listAgencement) {
				String[] dets = StringUtil.getArrayFromStringDelim(agencementBean.getTable_coords(), ";");
				
				List<String> listElements = mapLieu.get(agencementBean.getEmplacement());
				if(listElements == null){
					listElements = new ArrayList<>();
					mapLieu.put(agencementBean.getEmplacement(), listElements);
				}
				
				for(String det : dets){
					String[] data = StringUtil.getArrayFromStringDelim(det, ":");
					listElements.add(data[0]);
				}
			}
			
			for(String key : mapLieu.keySet()){
				List<String> listElements = mapLieu.get(key);
				Collections.sort(listElements);
			}
			
			httpUtil.setRequestAttribute("mapLieu", mapLieu);
			
			// Map résa lieu
			Map<String,String> mapLieuDt = new HashMap<>();
			List<PlanningPersistant> listResa = planningService.getResaPostDate();
			for (PlanningPersistant planningB : listResa) {
				String[] lieuArr = StringUtil.getArrayFromStringDelim(planningB.getLieu_str(), ";");
				for (String lieu : lieuArr) {
					mapLieuDt.put(lieu, StringUtil.getValueOrEmpty(mapLieuDt.get(lieu)));
								//+ " ["+DateUtil.dateToString(planningB.getDate_debut(), "EEE dd/MM")
								//+ "-"+DateUtil.dateToString(planningB.getDate_fin(), "EEE dd/MM")+"]");						
				}
			}
			httpUtil.setRequestAttribute("mapLieuDt", mapLieuDt);
		}
	}
	public void generatTime(ActionUtil httpUtil) {
		String heure_debut = httpUtil.getParameter("heure_debut");
		String[] timeValues = httpUtil.getRequest().getParameterValues("planning_opc_type_planning");

		if(StringUtil.isEmpty(heure_debut)) {
			heure_debut = "10:00";
		}

		String formattedResult ="";
		int totalTime = 0 ;
		String formattedTime = "";
        if (timeValues != null && timeValues.length > 0) {
            for (String timeValue : timeValues) {
        		TypePlanningPersistant mvmP = (TypePlanningPersistant)planningService.findById(TypePlanningPersistant.class, Long.parseLong(timeValue));
        		if(mvmP.getDuree()!= null) {
        		totalTime += mvmP.getDuree();
        		}
        	    formattedTime = calculateTimeByMinute(totalTime);

            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        try {
            Date formattedTimeDate = sdf.parse(formattedTime);
            Date realTimeDate = sdf.parse(heure_debut);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(realTimeDate);
            calendar.add(Calendar.HOUR_OF_DAY, formattedTimeDate.getHours());
            calendar.add(Calendar.MINUTE, formattedTimeDate.getMinutes());

            Date result = calendar.getTime();
            formattedResult = sdf.format(result);

            //System.out.println("Result: " + formattedResult);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        
		httpUtil.writeResponse(formattedResult);

	}
	
	public static String calculateTimeByMinute(int minutes) {
	    int hours = minutes / 60;
	    int remainingMinutes = minutes % 60;
	    
	    return String.format("%02d:%02d", hours, remainingMinutes);
	}
		
	
	
	
	public void vue_lieu(ActionUtil httpUtil) {
		RequestTableBean cplxTable = getTableBean(httpUtil, "planning_find");
		boolean isFilterAct = StringUtil.isTrue(httpUtil.getRequest().getParameter("is_filter_act"));
		//----------------------------- Date -------------------------
		Map<String, Object> formCriterion = cplxTable.getFormBean().getFormCriterion();
		Date dateDebut = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateDebut"));
		Date dateFin = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateFin"));
		boolean isDbFin = true;
		if(dateDebut == null) {
			Date dateLast = new Date();
			
			dateDebut = (httpUtil.getMenuAttribute("dateDebut")==null ? dateLast : (Date)httpUtil.getMenuAttribute("dateDebut"));
			dateFin = (httpUtil.getMenuAttribute("dateFin")==null ? dateLast : (Date)httpUtil.getMenuAttribute("dateFin"));
		} else if(httpUtil.getRequest().getParameter("prev") != null) {
			dateDebut = DateUtil.addSubstractDate(dateDebut, TIME_ENUM.MONTH, -1);
			dateFin = DateUtil.addSubstractDate(dateFin, TIME_ENUM.MONTH, -1);
		} else if(httpUtil.getRequest().getParameter("next") != null) {
			dateDebut = DateUtil.addSubstractDate(dateDebut, TIME_ENUM.MONTH, 1);
			dateFin = DateUtil.addSubstractDate(dateFin, TIME_ENUM.MONTH, 1);
		} else{
			isDbFin = false;
		}
		if(isDbFin){
			Calendar cal = DateUtil.getCalendar(dateDebut);
			String dateString = (cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR);
			dateDebut = DateUtil.stringToDate("01/"+dateString);
			dateFin = DateUtil.stringToDate(DateUtil.getMaxMonthDate(dateDebut)+"/"+dateString);
		}
		// Postionner l'heure
		dateDebut = DateUtil.getStartOfDay(dateDebut);
		dateFin = DateUtil.getEndOfDay(dateFin);
		
		httpUtil.setRequestAttribute("dateDebut", dateDebut);
		httpUtil.setRequestAttribute("dateFin", dateFin);
		httpUtil.setMenuAttribute("dateDebut", dateDebut);
		httpUtil.setMenuAttribute("dateFin", dateFin);
		
		if(!isFilterAct){
			formCriterion.put("dateDebut", dateDebut);
			formCriterion.put("dateFin", dateFin);
		} else{
			formCriterion.remove("dateDebut");
			formCriterion.remove("dateFin");
		}
		//-----------------------------------------------------------
		List<?> listData = planningService.findByCriteriaByQueryId(cplxTable, "planning_find", false);
	   	httpUtil.setRequestAttribute("list_planning", listData);
	   	
		httpUtil.setDynamicUrl("/domaine/caisse/normal/calendrier_lieu.jsp");
	}
	
	@Override
	public void work_find(ActionUtil httpUtil) {
		RequestTableBean cplxTable = getTableBean(httpUtil, "planning_find");
		List<?> listData = planningService.findByCriteriaByQueryId(cplxTable, "planning_find", false);
	   	httpUtil.setRequestAttribute("list_planning", listData);
		
	   	httpUtil.setDynamicUrl("/domaine/caisse/normal/calendrier.jsp");
	}
	
	@Override
	public void work_init_create(ActionUtil httpUtil) {
		String dt = httpUtil.getParameter("dt");
		if(StringUtil.isNotEmpty(dt)) {
			PlanningBean planningB = new PlanningBean();
			planningB.setDate_debut(DateUtil.stringToDate(dt, "yyyy-MM-dd"));
			httpUtil.setViewBean(planningB);
		}
		
		if(httpUtil.getParameter("lieu") != null){
			httpUtil.setRequestAttribute("currLieu", httpUtil.getParameter("lieu"));
		}
		
		if(httpUtil.getParameter("src") != null){
			httpUtil.setMenuAttribute("src", httpUtil.getParameter("src"));
		} else{
			httpUtil.removeMenuAttribute("src");
		}
		
		httpUtil.setDynamicUrl("/domaine/caisse/normal/calendrier-edit.jsp");
	}

	@Override
	public void work_merge(ActionUtil httpUtil) {
		PlanningBean planningBean = (PlanningBean) httpUtil.getViewBean();
		planningBean.setId(httpUtil.getWorkIdLong());
		String[] list_plans = httpUtil.getRequest().getParameterValues("planning_opc_type_planning");
		
		String str = ";";
		String str2 = "";
		String firstValue = list_plans[0];
		
		TypePlanningPersistant mvmP = (TypePlanningPersistant)planningService.findById(TypePlanningPersistant.class, Long.parseLong(firstValue));
		planningBean.setOpc_type_planning(mvmP);
		for (String value : list_plans) {
			TypePlanningPersistant mvmP2 = (TypePlanningPersistant)planningService.findById(TypePlanningPersistant.class, Long.parseLong(value));

			str2+="["+mvmP2.getLibelle()+"::"+mvmP2.getDuree()+"min]--";
			str += value + ";";
		}
		planningBean.setAllTypePlanning_str(str2.substring(0,str2.lastIndexOf(']')+1));
		planningBean.setTypePlanning_str(str);
		httpUtil.setRequestAttribute("isFromCalende", true);
		

		if(planningBean.getDate_fin() == null){
			planningBean.setDate_fin(DateUtil.getEndOfDay(new Date()));
		}
		
		//attribuer les heures aux dates de la tache
		String heureDebut = httpUtil.getParameter("heure_debut");;
		String heureFin = httpUtil.getParameter("heure_fin");
		//
		if(StringUtil.isEmpty(heureDebut)) {
			heureDebut = "00:00";
		}
		if(StringUtil.isEmpty(heureFin)) {
			heureFin = "23:59";
		}
		
		int heureDebut_H = Integer.valueOf(heureDebut.substring(0, 2));
		int heureDebut_M = Integer.valueOf(heureDebut.substring(3));
		int heureFin_H = Integer.valueOf(heureFin.substring(0, 2));
		int heureFin_M = Integer.valueOf(heureFin.substring(3));
		
		Calendar calDebut = DateUtil.getCalendar(planningBean.getDate_debut());
		Calendar calFin = DateUtil.getCalendar(planningBean.getDate_fin());
		calDebut.set(calDebut.get(Calendar.YEAR), calDebut.get(Calendar.MONTH), calDebut.get(Calendar.DAY_OF_MONTH), heureDebut_H, heureDebut_M);
		calFin.set(calFin.get(Calendar.YEAR), calFin.get(Calendar.MONTH), calFin.get(Calendar.DAY_OF_MONTH), heureFin_H, heureFin_M);
		
		String[] lieuArray = StringUtil.getArrayFromStringDelim(planningBean.getLieu_str(), ";");

		List<String> listreserv = planningService.checkTableReserved( calDebut.getTime(), calFin.getTime());
			for(String dt : lieuArray) {
				if(listreserv.contains(dt) && dt!=null){
					MessageService.addBannerMessage("Cette Table "+dt+" est deja reservé !!!!");
					return;
				}
			}
		
		planningBean.setDate_debut(calDebut.getTime());
		planningBean.setDate_fin(calFin.getTime());
		 
		if(planningBean.getDate_debut() != null && planningBean.getDate_fin() != null) {
			if(planningBean.getDate_debut().after(planningBean.getDate_fin())) {
				MessageService.addBannerMessage("La date de fin doit être postérieure à la date de début.");
				return;
			}
		}
		
		// Client ---------------------------------------------------------
		if((planningBean.getClients_array()==null || planningBean.getClients_array().length==0) && StringUtil.isNotEmpty(httpUtil.getParameter("client.nom"))){
			Map params = (Map)httpUtil.getRequest().getAttribute(ProjectConstante.WORK_PARAMS);
			ClientBean clientBeanIhm = ControllerBeanUtil.mapToBean(ClientBean.class, params);
			Long carte_id = httpUtil.getLongParameter("carte_id");
			clientBeanIhm.setNumero(clientService.generateNum());
			boolean isCarte = StringUtil.isNotEmpty(httpUtil.getRequest().getParameter("carte_id"));
			
			List<DataValuesPersistant> listDataValues = (List<DataValuesPersistant>) httpUtil.buildListBeanFromMap("data_value", DataValuesPersistant.class, 
					"eaiid", "data_value");
			
			clientBeanIhm.setId(null);
			clientService.create(clientBeanIhm);
			Long clientId = clientBeanIhm.getId();
			
			planningService.deleteDataForm(clientId, "CLIENT");
			planningService.mergeDataForm(listDataValues, clientId, "CLIENT");
			
			CarteFidelitePersistant carteP = null;
			if(isCarte){
				carteP = planningService.findById(CarteFidelitePersistant.class, carte_id);
			}
			
			if(isCarte){
				CarteFideliteClientPersistant cbfP = carteClientService.getCarteClientActive(clientId);
				if(cbfP == null){
					CarteFidelitePersistant cfP = (carteP==null ? carteFideliteService.getCarteOrCarteParDefaut(null) : carteP);
					
					cbfP = new CarteFideliteClientBean();
					String codeBarre = clientBeanIhm.getId()+"_"+new Random(1000).nextInt();
					cbfP.setCode_barre(codeBarre);
					cbfP.setDate_debut(new Date());
					cbfP.setOpc_carte_fidelite(cfP);
					cbfP.setOpc_client(clientBeanIhm);
					//
					cbfP = carteClientService.merge((CarteFideliteClientBean) cbfP);
				}
			}
			
			planningBean.setClients_str(";"+clientId+";");
		}
		//-------------------------------------------------------------------
		
//		planningService.addMail(planningBean);
		
		planningService.merge(planningBean);
		
		if("lieu".equals(httpUtil.getMenuAttribute("src"))){
			vue_lieu(httpUtil);
		} else{
			work_find(httpUtil);	
		}
	}
	
	@Override
	public void work_edit(ActionUtil httpUtil) {
		Long planning_id = Long.valueOf(httpUtil.getParameter("id"));
		PlanningBean planningBean = planningService.findById(planning_id);
		
		String heureDebut_H = (DateUtil.dateTimeToString(planningBean.getDate_debut())).substring(11, 13);
		String heureDebut_M = (DateUtil.dateTimeToString(planningBean.getDate_debut())).substring(14, 16);
		String heureFin_H = (DateUtil.dateTimeToString(planningBean.getDate_fin())).substring(11, 13);
		String heureFin_M = (DateUtil.dateTimeToString(planningBean.getDate_fin())).substring(14, 16);
		
		httpUtil.setRequestAttribute("heure_debut", heureDebut_H+""+heureDebut_M);
		httpUtil.setRequestAttribute("heure_fin", heureFin_H+""+heureFin_M);
		httpUtil.setRequestAttribute("all_planning", planningBean.getAllTypePlanning_str());
		httpUtil.setRequestAttribute("tp","edit");
		
		httpUtil.setViewBean(planningBean);
		
		if(httpUtil.getParameter("src") != null){
			httpUtil.setMenuAttribute("src", httpUtil.getParameter("src"));
		} else{
			httpUtil.removeMenuAttribute("src");
		}
		
		httpUtil.setDynamicUrl("/domaine/caisse/normal/calendrier-edit.jsp");
	}
	
	@Override
	public void work_init_update(ActionUtil httpUtil) {
		Long planning_id = httpUtil.getWorkIdLong();
		PlanningBean planningBean = planningService.findById(planning_id);
		
		String heureDebut_H = (DateUtil.dateTimeToString(planningBean.getDate_debut())).substring(11, 13);
		String heureDebut_M = (DateUtil.dateTimeToString(planningBean.getDate_debut())).substring(14, 16);
		String heureFin_H = (DateUtil.dateTimeToString(planningBean.getDate_fin())).substring(11, 13);
		String heureFin_M = (DateUtil.dateTimeToString(planningBean.getDate_fin())).substring(14, 16);
		
		httpUtil.setRequestAttribute("heure_debut", heureDebut_H+""+heureDebut_M);
		httpUtil.setRequestAttribute("heure_fin", heureFin_H+""+heureFin_M);
		
		httpUtil.setViewBean(planningBean);
		//
		httpUtil.setDynamicUrl("/domaine/caisse/normal/calendrier-edit.jsp");
	}
	
	@Override
	public void work_delete(ActionUtil httpUtil) {
		planningService.delete(httpUtil.getWorkIdLong());
		
		if("lieu".equals(httpUtil.getMenuAttribute("src"))){
			vue_lieu(httpUtil);
		} else{
			work_find(httpUtil);	
		}
	}
	
	/**
	 * @param httpUtil
	 */
	public void work_post(ActionUtil httpUtil) {
		PlanningBean planningBean = (PlanningBean) httpUtil.getViewBean();
		String action = httpUtil.getAction();
		if(action.equals(ActionConstante.EDIT) || action.equals(ActionConstante.INIT_UPDATE)) {
			String[] clientArray = StringUtil.getArrayFromStringDelim(planningBean.getClients_str(), ";");
			planningBean.setClients_array(clientArray);
			//
			String[] lieuArray = StringUtil.getArrayFromStringDelim(planningBean.getLieu_str(), ";");
			planningBean.setLieu_array(lieuArray);
		}
	}
}
