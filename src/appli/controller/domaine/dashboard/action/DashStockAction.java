
package appli.controller.domaine.dashboard.action;




import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import appli.model.domaine.dashboard.service.IDashBoardService;
import appli.model.domaine.stock.persistant.ArticleStockInfoPersistant;
import appli.model.domaine.stock.persistant.InventaireDetailPersistant;
import appli.model.domaine.stock.persistant.LotArticlePersistant;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.common.util.DateUtil;
import framework.model.common.util.DateUtil.TIME_ENUM;

/**
 * @author 
 *
 */
@WorkController(nameSpace="dash", jspRootPath="/domaine/dashboard_erp/") 
public class DashStockAction extends ActionBase {
	
	@Inject
	private IDashBoardService dashBoardService;
	
	/**
	 * 
	 * @param httpUtil
	 */
	public void work_init(ActionUtil httpUtil)  {
		httpUtil.setDynamicUrl("/domaine/dashboard_erp/stock/dashboard_stock.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void init_article_stock(ActionUtil httpUtil){
		// Mouvements sans numéro de facture
		
		// Article en alerte
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_article_alert");
		List<ArticleStockInfoPersistant> listData = (List<ArticleStockInfoPersistant>) dashBoardService.findByCriteriaByQueryId(cplxTable, "article_alert_find");
		
		httpUtil.setRequestAttribute("list_article", listData);
		
		// Articles périmés
		Date date15jours = DateUtil.addSubstractDate(new Date(), TIME_ENUM.DAY, 15);
		cplxTable = getTableBean(httpUtil, "list_article_peremption");
		Map<String, Object> formCriterion = cplxTable.getFormBean().getFormCriterion();
		formCriterion.put("dateRef", date15jours);
		List<LotArticlePersistant> listDataDate = (List<LotArticlePersistant>) dashBoardService.findByCriteriaByQueryId(cplxTable, "article_peremption_find");
		httpUtil.setRequestAttribute("list_article_peremption", listDataDate);
		cplxTable.setDataExport(listDataDate);
		cplxTable.setDataSize(listDataDate.size());
		httpUtil.setRequestAttribute("nbrArtTotalPeremption", cplxTable.getDataSize());
		
		// Evolution des pertes d'articles sur l'année en cours
		httpUtil.setDynamicUrl("/domaine/dashboard_erp/stock/dashboard_articles_include.jsp");
	}
	
	public void init_inventaire(ActionUtil httpUtil){
		Date dateDebut = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateDebut"));
		Date dateFin = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateFin"));
		//
		if(httpUtil.getRequest().getParameter("is_fltr") == null) {
			dateDebut = DateUtil.stringToDate(httpUtil.getParameter("dateDebut"));
			dateFin = DateUtil.stringToDate(httpUtil.getParameter("dateFin"));
		}
		
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
		
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_inv_dash");
		cplxTable.getFormBean().getFormCriterion().put("dtRefDebut", dateDebut);
		cplxTable.getFormBean().getFormCriterion().put("dtRefFin", dateFin);
		
		String req = "from InventaireDetailPersistant inventaireDet "
		+ "where opc_inventaire.date_realisation>='[dtRefDebut]' "
		+ "and opc_inventaire.date_realisation<='[dtRefFin]' "
		+ "and qte_ecart != 0 " +
		" order by opc_article.libelle";
		
		List<InventaireDetailPersistant> listData = (List<InventaireDetailPersistant>) dashBoardService.findByCriteria(cplxTable, req);
		//
		httpUtil.setRequestAttribute("dateDebut", dateDebut);
		httpUtil.setRequestAttribute("dateFin", dateFin);
		httpUtil.setRequestAttribute("list_inv_dash", listData);
		httpUtil.setDynamicUrl("/domaine/dashboard_erp/stock/dashboard_inventaire_include.jsp");
	}
}
