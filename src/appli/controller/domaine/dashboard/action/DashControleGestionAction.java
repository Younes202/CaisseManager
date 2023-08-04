
package appli.controller.domaine.dashboard.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.hibernate.criterion.Order;

import appli.model.domaine.administration.persistant.EcriturePersistant;
import appli.model.domaine.dashboard.service.IDashBoardService;
import appli.model.domaine.stock.persistant.ArticleStockInfoPersistant;
import appli.model.domaine.stock.persistant.LotArticlePersistant;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.beanContext.CompteBancairePersistant;
import framework.model.common.util.DateUtil;
import framework.model.common.util.DateUtil.TIME_ENUM;
import framework.model.common.util.StringUtil;

/**
 * @author 
 *
 */
@WorkController(nameSpace="dash", jspRootPath="/domaine/dashboard_erp/") 
public class DashControleGestionAction extends ActionBase {
	
	@Inject
	private IDashBoardService dashBoardService;
	
	/**
	 * 
	 * @param httpUtil
	 */
	public void work_init(ActionUtil httpUtil)  {
		httpUtil.setRequestAttribute("is_controleGestion", true);
		httpUtil.setDynamicUrl("/domaine/dashboard_erp/dashboard_journee.jsp");
	}
	
	@Override
	public void work_edit(ActionUtil httpUtil) {
		init_dashboard(httpUtil);
	}
	
	/**
	 * @param httpUtil
	 */
	public void init_dashboard(ActionUtil httpUtil){
		// Date situation
		Calendar cal = DateUtil.getCalendar(new Date());
		String dtDebutSt = (cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR);
		httpUtil.setRequestAttribute("curr_sitDebut", (dtDebutSt.length()==6 ? "0"+dtDebutSt:dtDebutSt)); 
		
		// Date ecart
		int mt = cal.get(Calendar.MONTH)+1;
		String mois = (""+mt).length() == 1 ? "0"+mt : ""+mt;
		Date dtDebut = DateUtil.stringToDate("01"+"/"+mois+"/"+cal.get(Calendar.YEAR));
		Date dtFin = DateUtil.stringToDate(cal.getActualMaximum(Calendar.DAY_OF_MONTH)+"/"+mois+"/"+cal.get(Calendar.YEAR));
		
		httpUtil.setRequestAttribute("curr_dtDebut", dtDebut);
		httpUtil.setRequestAttribute("curr_dtFin", dtFin);
		
		httpUtil.setDynamicUrl("/domaine/dashboard_erp/dashboard_journee.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void init_situation_chiffre(ActionUtil httpUtil){
		String dtDebut = httpUtil.getParameter("situation_dt_debut");
		Date dateDebut = null;
		Date dateFin = null;
		
		if(StringUtil.isEmpty(dtDebut)){
			Calendar cal = DateUtil.getCalendar(new Date());
			dtDebut = (cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR);
		}
		
		dateDebut = DateUtil.stringToDate("01/"+dtDebut);
		dateFin = DateUtil.stringToDate(DateUtil.getMaxMonthDate(dateDebut)+"/"+dtDebut);
		
		httpUtil.setRequestAttribute("curr_sitDebut", (dtDebut.length()==6 ? "0"+dtDebut:dtDebut)); 
		
		Map<String, Object> infos = dashBoardService.getDataInfos(dateFin);
		if(infos == null) {
			infos = new HashMap();
		}
		httpUtil.setRequestAttribute("dataSituation", infos);
		
		httpUtil.setDynamicUrl("/domaine/dashboard_erp/dashboard_situation_include.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void init_banque(ActionUtil httpUtil){
		List<CompteBancairePersistant> listBanque = (List<CompteBancairePersistant>) dashBoardService.findAll(CompteBancairePersistant.class, Order.asc("libelle"));
		httpUtil.setRequestAttribute("list_banque", listBanque);
		
		Long banqueId = null;
		
		if(StringUtil.isEmpty(httpUtil.getParameter("curr_banque"))) {
			if(!listBanque.isEmpty()) {
				banqueId = listBanque.get(0).getId();
			}
		} else {
			banqueId = httpUtil.getLongParameter("curr_banque");
		}
		
		httpUtil.setRequestAttribute("curr_banque", banqueId);
		
		List<EcriturePersistant> banqueEvolution = dashBoardService.getBanqueEvolution(banqueId);
		httpUtil.setRequestAttribute("compteEvol", banqueEvolution);
		
		httpUtil.setDynamicUrl("/domaine/dashboard_erp/dashboard_banque.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void init_perte_employe(ActionUtil httpUtil){
		loadDataEcart(httpUtil);
		//
		httpUtil.setDynamicUrl("/domaine/dashboard_erp/employe_ecart_livrason_table.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	private void loadDataEcart(ActionUtil httpUtil) {
		Date dtDebut = null, dtFin = null;
		
		// Date début
		if(StringUtil.isEmpty(httpUtil.getParameter("ecart_dt_debut"))) {
			Calendar cal = DateUtil.getCalendar(new Date());
			int mt = cal.get(Calendar.MONTH)+1;
			String mois = (""+mt).length() == 1 ? "0"+mt : ""+mt;
			dtDebut = DateUtil.stringToDate("01"+"/"+mois+"/"+cal.get(Calendar.YEAR));
		} else {
			dtDebut = DateUtil.stringToDate(httpUtil.getParameter("ecart_dt_debut"));
		}
		
		// Date fin
		if(StringUtil.isEmpty(httpUtil.getParameter("ecart_dt_fin"))) {
			Calendar cal = DateUtil.getCalendar(new Date());
			int mt = cal.get(Calendar.MONTH)+1;
			String mois = (""+mt).length() == 1 ? "0"+mt : ""+mt;
			dtFin = DateUtil.stringToDate(cal.getActualMaximum(Calendar.DAY_OF_MONTH)+"/"+mois+"/"+cal.get(Calendar.YEAR));
		} else {
			dtFin = DateUtil.stringToDate(httpUtil.getParameter("ecart_dt_fin"));
		}
		
		httpUtil.setRequestAttribute("curr_dtDebut", dtDebut);
		httpUtil.setRequestAttribute("curr_dtFin", dtFin);
		
		List<Object[]> ecartEvolution = dashBoardService.getEcartEmploye(dtDebut, dtFin);
		ecartEvolution = (ecartEvolution==null) ? new ArrayList<Object[]>() : ecartEvolution;
		httpUtil.setRequestAttribute("ecartEvol", ecartEvolution);
		
		httpUtil.setRequestAttribute("tab_livraison", httpUtil.getParameter("tab_livraison"));
		
		List<Object[]> livraisonEmploye = dashBoardService.getLivraisonParutilisateur(dtDebut, dtFin);
		httpUtil.setRequestAttribute("livraisonEvol", livraisonEmploye);
	}
	
	/**
	 * @param httpUtil
	 */
	public void find_articlAlert(ActionUtil httpUtil){
		find_article_alert(httpUtil);
		find_article_peremption(httpUtil);
		//
		httpUtil.setDynamicUrl("/domaine/dashboard_erp/article_alert_list.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void find_article_alert(ActionUtil httpUtil){
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_article_alert");
		List<ArticleStockInfoPersistant> listData = (List<ArticleStockInfoPersistant>) dashBoardService.findByCriteriaByQueryId(cplxTable, "article_alert_find");
		httpUtil.setRequestAttribute("list_article", listData);
		
		httpUtil.setRequestAttribute("nbrArtTotal", listData.size());
		
		httpUtil.setDynamicUrl("/domaine/dashboard_erp/article_alert_list.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void find_article_peremption(ActionUtil httpUtil){
		Date date15jours = DateUtil.addSubstractDate(new Date(), TIME_ENUM.DAY, 15);
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_article_peremption");
		Map<String, Object> formCriterion = cplxTable.getFormBean().getFormCriterion();
		formCriterion.put("dateRef", date15jours);
		List<LotArticlePersistant> listData = (List<LotArticlePersistant>) dashBoardService.findByCriteriaByQueryId(cplxTable, "article_peremption_find");
		httpUtil.setRequestAttribute("list_article_peremption", listData);
		
		cplxTable.setDataExport(listData);
		cplxTable.setDataSize(listData.size());
		
		httpUtil.setRequestAttribute("nbrArtTotalPeremption", cplxTable.getDataSize());
		
		httpUtil.setDynamicUrl("/domaine/dashboard_erp/article_peremption_list.jsp");
	}
	
	public void deleteLot(ActionUtil httpUtil) {
//		Long workId = httpUtil.getWorkIdLong();
//		lotArtService.deleteLot(workId);
		
		httpUtil.setDynamicUrl("/domaine/dashboard_erp/article_peremption_list.jsp");
	}
}
