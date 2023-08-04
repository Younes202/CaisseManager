
package appli.controller.domaine.dashboard.action;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.criterion.Order;

import appli.model.domaine.dashboard.service.IDashBoardService;
import appli.model.domaine.personnel.service.IEmployeService;
import appli.model.domaine.personnel.service.IPosteService;
import appli.model.domaine.personnel.service.ITypeFraisService;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.common.util.DateUtil;
import framework.model.common.util.DateUtil.TIME_ENUM;
import framework.model.common.util.StringUtil;

/**
 * @author 
 *
 */
@WorkController(nameSpace="dash", jspRootPath="/domaine/dashboard_erp/") 
public class DashRHAction extends ActionBase {
	
	@Inject
	private IDashBoardService dashBoardService;
	@Inject
	private IEmployeService employeservice;
	@Inject
	private IPosteService posteservice;
	@Inject
	private ITypeFraisService typeFraisService;
	
	/**
	 * 
	 * @param httpUtil
	 */
	public void work_init(ActionUtil httpUtil)  {
		//repartition_frais_type_employe(httpUtil);
		httpUtil.setDynamicUrl("/domaine/dashboard_erp/rh/dashboard_rh.jsp");
	}
	
	public void repartition_frais_type_employe(ActionUtil httpUtil){
		httpUtil.setRequestAttribute( "listEmploye", employeservice.findAll(Order.asc("nom")));
		httpUtil.setRequestAttribute( "listTypeFrais", typeFraisService.findAll(Order.asc("libelle")));
		
		Date dtDebut = null, dtFin = null;
		
		// Date début
		String dateDebutParam = httpUtil.getParameter("empl_fr_debut");
		if(StringUtil.isEmpty(dateDebutParam)) {
			Calendar cal = DateUtil.getCalendar(DateUtil.getCurrentDate());
			int mt = cal.get(Calendar.MONTH)+1;
			String mois = (""+mt).length() == 1 ? "0"+mt : ""+mt;
			dtDebut = DateUtil.stringToDate("01"+"/"+mois+"/"+cal.get(Calendar.YEAR));
		} else {
			dtDebut = DateUtil.stringToDate(dateDebutParam);
		}
		
		// Date fin
		String dateFinParam = httpUtil.getParameter("empl_fr_fin");
		if(StringUtil.isEmpty(dateFinParam)) {
			Calendar cal = DateUtil.getCalendar(dtDebut);
			int mt = cal.get(Calendar.MONTH)+1;
			String mois = (""+mt).length() == 1 ? "0"+mt : ""+mt;
			dtFin = DateUtil.stringToDate(cal.getActualMaximum(Calendar.DAY_OF_MONTH)+"/"+mois+"/"+cal.get(Calendar.YEAR));
		} else {
			dtFin = DateUtil.stringToDate(dateFinParam);
		}
		
		dtDebut = DateUtil.getStartOfDay(dtDebut);
		dtFin = DateUtil.getEndOfDay(dtFin);
		
		Long employe = httpUtil.getLongParameter("employe");
		Long typeFrais = httpUtil.getLongParameter("typeFraiSlct");
		
		httpUtil.setRequestAttribute("employe", employe);
		httpUtil.setRequestAttribute("typeFrais", typeFrais);
		httpUtil.setRequestAttribute("curr_fr_dtDebut", dtDebut);
		httpUtil.setRequestAttribute("curr_fr_dtFin", dtFin);
		
		List<Object[]> dataTypeFrais = dashBoardService.getRepartitionFraisParType(dtDebut, dtFin, typeFrais,employe);
		List<Object[]> dataTypeEmploye = dashBoardService.getRepartitionFraisParEmploye(dtDebut, dtFin, typeFrais,employe);
						
		httpUtil.setRequestAttribute("dataTypeFrais", dataTypeFrais);
		httpUtil.setRequestAttribute("dataTypeEmploye", dataTypeEmploye);
		
		httpUtil.setDynamicUrl("/domaine/dashboard_erp/rh/dash_travail2.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void init_data_employe(ActionUtil httpUtil){
		httpUtil.setRequestAttribute( "listEmploye", employeservice.findAll());
		httpUtil.setRequestAttribute( "listPoste", posteservice.findAll());
		
		Date dtDebut = null, dtFin = null;
		
		// Date début
		String dateDebutParam = httpUtil.getParameter("empl_dt_debut");
		if(StringUtil.isEmpty(dateDebutParam)) {
			Calendar cal = DateUtil.getCalendar(DateUtil.getCurrentDate());
			int mt = cal.get(Calendar.MONTH)+1;
			String mois = (""+mt).length() == 1 ? "0"+mt : ""+mt;
			dtDebut = DateUtil.stringToDate("01"+"/"+mois+"/"+cal.get(Calendar.YEAR));
		} else {
			dtDebut = DateUtil.stringToDate(dateDebutParam);
		}
		
		// Date fin
		String dateFinParam = httpUtil.getParameter("empl_dt_fin");
		if(StringUtil.isEmpty(dateFinParam)) {
			Calendar cal = DateUtil.getCalendar(dtDebut);
			int mt = cal.get(Calendar.MONTH)+1;
			String mois = (""+mt).length() == 1 ? "0"+mt : ""+mt;
			dtFin = DateUtil.stringToDate(cal.getActualMaximum(Calendar.DAY_OF_MONTH)+"/"+mois+"/"+cal.get(Calendar.YEAR));
		} else {
			dtFin = DateUtil.stringToDate(dateFinParam);
		}
		
		dtDebut = DateUtil.getStartOfDay(dtDebut);
		dtFin = DateUtil.getEndOfDay(dtFin);
		
		Long employe = httpUtil.getLongParameter("employe");
		Long poste = httpUtil.getLongParameter("poste");
		
		httpUtil.setRequestAttribute("employe", employe);
		httpUtil.setRequestAttribute("poste", poste);
		httpUtil.setRequestAttribute("curr_empl_dtDebut", dtDebut);
		httpUtil.setRequestAttribute("curr_empl_dtFin", dtFin);
		
		List<Object[]> data = dashBoardService.getTempsTravailEmploye(dtDebut, dtFin, employe,poste);
						
		httpUtil.setRequestAttribute("dataEmploye", data);
		
		httpUtil.setDynamicUrl("/domaine/dashboard_erp/rh/dash_travail.jsp");
		
	}
	
	public void init_dashboard(ActionUtil httpUtil){
		// Alerte pointage
		
		// Evolution descipline
		
		// 
	}
	
	public void init_abscence(ActionUtil httpUtil) {
		Date dateDebut = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateDebut_abs"));
		Date dateFin = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateFin_abs"));
		
		if(dateDebut == null) {
			Calendar cal = DateUtil.getCalendar(new Date());
			String dateString = (cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR);
			
			dateDebut = DateUtil.stringToDate("01/"+dateString);
			dateDebut = DateUtil.getStartOfDay(dateDebut);
					
			dateFin = DateUtil.stringToDate(DateUtil.getMaxMonthDate(dateDebut)+"/"+dateString);
			dateDebut = DateUtil.getEndOfDay(dateDebut);
		}
		
		if(httpUtil.getRequest().getParameter("prev") != null) {
			dateDebut = DateUtil.addSubstractDate(dateDebut, TIME_ENUM.MONTH, -1);
			Calendar cal = DateUtil.getCalendar(dateDebut);
			String dateString = (cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR);
			dateFin = DateUtil.stringToDate(DateUtil.getMaxMonthDate(dateDebut)+"/"+dateString);
		} else if(httpUtil.getRequest().getParameter("next") != null) {
			dateDebut = DateUtil.addSubstractDate(dateDebut, TIME_ENUM.MONTH, 1);
			Calendar cal = DateUtil.getCalendar(dateDebut);
			String dateString = (cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR);
			dateFin = DateUtil.stringToDate(DateUtil.getMaxMonthDate(dateDebut)+"/"+dateString);
		}
		
		httpUtil.setRequestAttribute("dateDebut_abs", dateDebut);
		httpUtil.setRequestAttribute("dateFin_abs", dateFin);
		
		List<Object[]> listAbscence = dashBoardService.getCongeNonPayeByDate(dateDebut, dateFin);
		httpUtil.setRequestAttribute("list_abscence", listAbscence);
		
		httpUtil.setDynamicUrl("/domaine/dashboard_erp/rh/dash_abscence.jsp");
	}
	
	public void init_pointage(ActionUtil httpUtil) {
		Date dateDebut = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateDebut_pnt"));
		Date dateFin = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateFin_pnt"));
		
		if(dateDebut == null) {
			Calendar cal = DateUtil.getCalendar(new Date());
			String dateString = (cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR);
			dateDebut =DateUtil.stringToDate("01/"+dateString);
			dateFin = DateUtil.stringToDate(DateUtil.getMaxMonthDate(dateDebut)+"/"+dateString);
		}
		
		if(httpUtil.getRequest().getParameter("prev") != null) {
			dateDebut = DateUtil.addSubstractDate(dateDebut, TIME_ENUM.MONTH, -1);
			Calendar cal = DateUtil.getCalendar(dateDebut);
			String dateString = (cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR);
			dateFin = DateUtil.stringToDate(DateUtil.getMaxMonthDate(dateDebut)+"/"+dateString);
		} else if(httpUtil.getRequest().getParameter("next") != null) {
			dateDebut = DateUtil.addSubstractDate(dateDebut, TIME_ENUM.MONTH, 1);
			Calendar cal = DateUtil.getCalendar(dateDebut);
			String dateString = (cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR);
			dateFin = DateUtil.stringToDate(DateUtil.getMaxMonthDate(dateDebut)+"/"+dateString);
		}
		
		httpUtil.setRequestAttribute("dateDebut_pnt", dateDebut);
		httpUtil.setRequestAttribute("dateFin_pnt", dateFin);
		
		List<Object[]> listPointage = dashBoardService.getPointageRH(dateDebut, dateFin);
		httpUtil.setRequestAttribute("list_pointage", listPointage);
		
		httpUtil.setDynamicUrl("/domaine/dashboard_erp/rh/dash_pointage.jsp");
	}
}
