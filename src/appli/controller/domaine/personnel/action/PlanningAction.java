package appli.controller.domaine.personnel.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.hibernate.criterion.Order;

import appli.controller.domaine.personnel.bean.PlanningBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.administration.persistant.AgencementPersistant;
import appli.model.domaine.administration.service.IAgencementService;
import appli.model.domaine.administration.service.IClientService;
import appli.model.domaine.personnel.persistant.TypePlanningPersistant;
import appli.model.domaine.personnel.service.IEmployeService;
import appli.model.domaine.personnel.service.IPlanningService;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.common.constante.ActionConstante;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.DateUtil.TIME_ENUM;
import framework.model.common.util.StringUtil;

@WorkController(nameSpace = "pers", bean = PlanningBean.class, jspRootPath = "/domaine/personnel/")
public class PlanningAction extends ActionBase {
	@Inject
	private IPlanningService planningService;
	@Inject
	private IEmployeService employeService;
	@Inject
	private IClientService clientService;
	@Inject
	private IAgencementService agencementService;
	
	public void work_init(ActionUtil httpUtil) {
		PlanningBean planningBean = (PlanningBean) httpUtil.getViewBean();
		String action = httpUtil.getAction();
		if(planningBean != null && (action.equals(ActionConstante.MERGE) || action.equals(ActionConstante.CREATE) || action.equals(ActionConstante.UPDATE))){
    		String employes = ";";
    		if(planningBean.getEmployes_array() != null){
    			for (String employeId :  planningBean.getEmployes_array()) {
    				if(StringUtil.isNotEmpty(employeId)){
    					employes = employes + employeId + ";";
    				}
    			}
    		}
    		
    		if(ContextAppli.getUserBean().getOpc_employe() != null && employes.indexOf(ContextAppli.getUserBean().getOpc_employe().getId()+";") == -1) {
    			employes = employes + ContextAppli.getUserBean().getOpc_employe().getId() + ";";
    		}
    		
    		planningBean.setEmployes_str(employes);
				
			String clients = ";";
			if(planningBean.getClients_array() != null){
				for (String clientId :  planningBean.getClients_array()) {
					if(StringUtil.isNotEmpty(clientId)){
						clients = clients + clientId + ";";
					}
				}
			}
			planningBean.setClients_str(clients);
		
			
			// ---------------------------------------------------
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
			
		Map<String, List<String>> mapLieu = new HashMap<>();
		List<AgencementPersistant> listAgencement = agencementService.findAll(AgencementPersistant.class);
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
		
		//----------------------------------------------
		
		String[][] listRappel = {{"M-5", "5 minutes avant"}, {"M-15", "15 minutes avant"}, {"M-30", "30 minutes avant"},
				{"H-1", "1 heure avant"}, {"H-2", "2 heures avant"}, {"H-12", "12 heures avant"},
				{"J-1", "1 jour avant"}, {"J-2", "2 jours avant"}, 
				{"S-1", "1 semaine avant"}};
		httpUtil.setRequestAttribute("listRappel", listRappel);
		
		String[][] listRepetition = {{"QUOTID", "Quotidienne"}, {"HEBDO", "Hebdomadaire"}, {"MENSU", "Mensuelle"}, {"ANNU", "Annuelle"}};
		httpUtil.setRequestAttribute("listRepetition", listRepetition);
		httpUtil.setRequestAttribute("listTypePlanning", planningService.findAll(TypePlanningPersistant.class, Order.asc("libelle")));
		httpUtil.setRequestAttribute("listEmploye", employeService.getListEmployeActifs());
		httpUtil.setRequestAttribute("listClient", clientService.getClientsActifs());
		httpUtil.setRequestAttribute("mapLieu", mapLieu);
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
	   	
		httpUtil.setDynamicUrl("/domaine/personnel/planning_lieu.jsp");
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
		
		super.work_init_create(httpUtil);
		
		if(httpUtil.getParameter("src") != null){
			httpUtil.setMenuAttribute("src", httpUtil.getParameter("src"));
		} else{
			httpUtil.removeMenuAttribute("src");
		}
	}

	@Override
	public void work_merge(ActionUtil httpUtil) {
		PlanningBean planningBean = (PlanningBean) httpUtil.getViewBean();
		planningBean.setId(httpUtil.getWorkIdLong());

		if(planningBean.getDate_fin() == null){
			planningBean.setDate_fin(DateUtil.getEndOfDay(new Date()));
		}
		
		if( !BooleanUtil.isTrue(planningBean.getIs_all_day()) ) {
			//attribuer les heures aux dates de la tache
			String heureDebut = httpUtil.getParameter("heure_debut");;
			String heureFin = httpUtil.getParameter("heure_fin");
			
			int heureDebut_H = 8;
			int heureDebut_M = 0;
			if(StringUtil.isNotEmpty(heureDebut)) {
				heureDebut_H = Integer.valueOf(heureDebut.substring(0, 2));
				heureDebut_M = Integer.valueOf(heureDebut.substring(3));
			}
			
			int heureFin_H = 20;
			int heureFin_M = 0;
			if(StringUtil.isNotEmpty(heureFin)) {
				heureFin_H = Integer.valueOf(heureFin.substring(0, 2));
				heureFin_M = Integer.valueOf(heureFin.substring(3));
			}
			
			Calendar calDebut = DateUtil.getCalendar(planningBean.getDate_debut());
			Calendar calFin = DateUtil.getCalendar(planningBean.getDate_fin());
			calDebut.set(calDebut.get(Calendar.YEAR), calDebut.get(Calendar.MONTH), calDebut.get(Calendar.DAY_OF_MONTH), heureDebut_H, heureDebut_M);
			calFin.set(calFin.get(Calendar.YEAR), calFin.get(Calendar.MONTH), calFin.get(Calendar.DAY_OF_MONTH), heureFin_H, heureFin_M);
			
			planningBean.setDate_debut(calDebut.getTime());
			planningBean.setDate_fin(calFin.getTime());
		} else {
			planningBean.setDate_debut(DateUtil.getStartOfDay(planningBean.getDate_debut()));
			planningBean.setDate_fin(DateUtil.getEndOfDay(planningBean.getDate_debut()));
		}
		
		super.work_merge(httpUtil);
//		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "", "L'élément a été créé avec succès.");
		
		planningService.addMail(planningBean);
		
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
		
		//afficher les champs correspondant au bean
		if(BooleanUtil.isTrue(planningBean.getIs_all_day())) {
			httpUtil.setRequestAttribute("isAllDayEvent", true);
		}
		if(StringUtil.isNotEmpty(planningBean.getRepetition())){
			httpUtil.setRequestAttribute("isRepetition", true);
		}
		 
		String heureDebut_H = (DateUtil.dateTimeToString(planningBean.getDate_debut())).substring(11, 13);
		String heureDebut_M = (DateUtil.dateTimeToString(planningBean.getDate_debut())).substring(14, 16);
		String heureFin_H = (DateUtil.dateTimeToString(planningBean.getDate_fin())).substring(11, 13);
		String heureFin_M = (DateUtil.dateTimeToString(planningBean.getDate_fin())).substring(14, 16);
		
		httpUtil.setRequestAttribute("heure_debut", heureDebut_H+""+heureDebut_M);
		httpUtil.setRequestAttribute("heure_fin", heureFin_H+""+heureFin_M);
		
		super.work_edit(httpUtil);
		
		if(httpUtil.getParameter("src") != null){
			httpUtil.setMenuAttribute("src", httpUtil.getParameter("src"));
		} else{
			httpUtil.removeMenuAttribute("src");
		}
	}
	
	@Override
	public void work_init_update(ActionUtil httpUtil) {
		Long workId = httpUtil.getWorkIdLong();
		PlanningBean planningBean = planningService.findById(workId);
		
		String heureDebut_H = (DateUtil.dateTimeToString(planningBean.getDate_debut())).substring(11, 13);
		String heureDebut_M = (DateUtil.dateTimeToString(planningBean.getDate_debut())).substring(14, 16);
		String heureFin_H = (DateUtil.dateTimeToString(planningBean.getDate_fin())).substring(11, 13);
		String heureFin_M = (DateUtil.dateTimeToString(planningBean.getDate_fin())).substring(14, 16);
		
		httpUtil.setRequestAttribute("heure_debut", heureDebut_H+""+heureDebut_M);
		httpUtil.setRequestAttribute("heure_fin", heureFin_H+""+heureFin_M);
		
		//afficher les champs correspondant au bean
		if(BooleanUtil.isTrue(planningBean.getIs_all_day())) {
			httpUtil.setRequestAttribute("isAllDayEvent", true);
		}
		if(StringUtil.isNotEmpty(planningBean.getRepetition())){
			httpUtil.setRequestAttribute("isRepetition", true);
		}
		//
		super.work_init_update(httpUtil);
	}
	
	public void calcul_heure_fin(ActionUtil httpUtil) {
		String heureDebut = httpUtil.getParameter("heure_debut");
		Long typeCal = httpUtil.getLongParameter("planning.opc_type_planning.id");
		
		if(StringUtil.isNotEmpty(heureDebut) && typeCal != null) {
			TypePlanningPersistant tpPlanning = planningService.findById(TypePlanningPersistant.class, typeCal);
			if(tpPlanning.getDuree() != null) {
				String[] fragHour = StringUtil.getArrayFromStringDelim(heureDebut, ":");
				
				String heureFin = "";
				if(StringUtil.isNotEmpty(httpUtil.getParameter("planning.date_debut"))) {
					Date dateDebut = DateUtil.stringToDate(httpUtil.getParameter("planning.date_debut"));
					dateDebut = DateUtil.setDetailDate(dateDebut, TIME_ENUM.HOUR, Integer.valueOf(fragHour[0]));
					dateDebut = DateUtil.setDetailDate(dateDebut, TIME_ENUM.MINUTE, Integer.valueOf(fragHour[1]));
					
					Date dateFin = DateUtil.addSubstractDate(dateDebut, TIME_ENUM.HOUR, tpPlanning.getDuree());
					heureFin = DateUtil.dateToString(dateFin, "HH:mm");
				} else {
					Date dateDebut = new Date();
					dateDebut = DateUtil.setDetailDate(dateDebut, TIME_ENUM.HOUR, Integer.valueOf(fragHour[0]));
					dateDebut = DateUtil.setDetailDate(dateDebut, TIME_ENUM.MINUTE, Integer.valueOf(fragHour[1]));
					
					Date dateFin = DateUtil.addSubstractDate(new Date(), TIME_ENUM.HOUR, tpPlanning.getDuree());
					heureFin = DateUtil.dateToString(dateFin, "HH:mm");
				}
				
				httpUtil.writeResponse(heureFin);
				return;
			}
		}
		httpUtil.writeResponse("");
	}
	
	@Override
	public void work_delete(ActionUtil httpUtil) {
		super.work_delete(httpUtil);
		
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
			String[] employeArray = StringUtil.getArrayFromStringDelim(planningBean.getEmployes_str(), ";");
			planningBean.setEmployes_array(employeArray);
			//
			String[] clientArray = StringUtil.getArrayFromStringDelim(planningBean.getClients_str(), ";");
			planningBean.setClients_array(clientArray);
			//
			String[] lieuArray = StringUtil.getArrayFromStringDelim(planningBean.getLieu_str(), ";");
			planningBean.setLieu_array(lieuArray);
		}
	}
}
