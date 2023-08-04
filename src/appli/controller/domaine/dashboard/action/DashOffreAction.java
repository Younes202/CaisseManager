
package appli.controller.domaine.dashboard.action;




import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import appli.model.domaine.administration.service.IUserService;
import appli.model.domaine.dashboard.service.IDashBoardService;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.common.util.DateUtil;
import framework.model.common.util.StringUtil;

/**
 * @author 
 *
 */
@WorkController(nameSpace="dash", jspRootPath="/domaine/dashboard_erp/") 
public class DashOffreAction extends ActionBase {
	
	@Inject
	private IUserService userService;
	@Inject
	private IDashBoardService dashBoardService;
	
	/**
	 * 
	 * @param httpUtil
	 */
	public void work_init(ActionUtil httpUtil)  {
		httpUtil.setDynamicUrl("/domaine/dashboard_erp/offre/dashboard_offre.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void init_dashboard(ActionUtil httpUtil){
		// Points et offres
		
		// Portefeuille virtuel
	}
	
	public void init_data_employe(ActionUtil httpUtil){
		httpUtil.setRequestAttribute( "listUsers", userService.findAllUser(false));
		
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
		
		Long user = httpUtil.getLongParameter("user");
		
		httpUtil.setRequestAttribute("user", user);
		httpUtil.setRequestAttribute("curr_empl_dtDebut", dtDebut);
		httpUtil.setRequestAttribute("curr_empl_dtFin", dtFin);
		
		List<Object[]> data = dashBoardService.getRepVenteCaisseParCaissier(dtDebut, dtFin, user);
						
		httpUtil.setRequestAttribute("dataCMP", data);
		
		httpUtil.setDynamicUrl("/domaine/dashboard_erp/offre/dashboard_offre_detail.jsp");
		
	}
}
