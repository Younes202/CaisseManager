package appli.controller.domaine.stock.action;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import appli.controller.domaine.stock.bean.ArticleBean;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.stock.service.IArticleService;
import appli.model.domaine.stock.service.IEtatStockService;
import appli.model.domaine.stock.service.IFamilleService;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.common.util.DateUtil;
import framework.model.common.util.DateUtil.TIME_ENUM;
import framework.model.common.util.StringUtil;


@WorkController(nameSpace="stock", bean=ArticleBean.class, jspRootPath="/domaine/stock/")
public class EtatStockAction extends ActionBase {
	@Inject
	private IFamilleService familleService;
	@Inject
	private IArticleService articleService;
	@Inject
	private IEtatStockService etatStockService; 
	
	/**
	 * @param httpUtil
	 */
	public void work_init(ActionUtil httpUtil) {
		List<ArticlePersistant> listArticle = articleService.getListArticleStock(true);
		httpUtil.setRequestAttribute("listArticle", listArticle);
	}
	
	@Override
	public void work_find(ActionUtil httpUtil) {
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_article");
		
		httpUtil.setRequestAttribute("listAllarticle", articleService.getListArticleStock(true));
		
		String[] articleArray = httpUtil.getRequest().getParameterValues("article_fltr");
		httpUtil.setRequestAttribute("selectedArticles", articleArray);

		String dtDebut = httpUtil.getParameter("dateDebut");
		
		if(dtDebut != null){
			httpUtil.setMenuAttribute("dateDebut", dtDebut);
		} else{
			if(httpUtil.getMenuAttribute("dateDebut") instanceof Date) {
				dtDebut = DateUtil.dateToString((Date) httpUtil.getMenuAttribute("dateDebut"));
			} else {
				dtDebut = (String) httpUtil.getMenuAttribute("dateDebut");
			}
		}
		
		// Min et Max dates disponibles
		Date[] minMaxDate = etatStockService.getMinMaxDate();
		
		if(StringUtil.isNotEmpty(httpUtil.getRequestAttribute("dateDebut"))) {
			dtDebut = (String) httpUtil.getRequestAttribute("dateDebut");
		}
		Date dateDebut = null;
		if(StringUtil.isEmpty(dtDebut)){
			Calendar cal = DateUtil.getCalendar(new Date());
			dtDebut = (cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR);
		}
		
		dateDebut = DateUtil.stringToDate("01/"+dtDebut);
		
		if(minMaxDate[1] != null) {
			Calendar calendarDtFin = DateUtil.getCalendar(minMaxDate[1]);
			dtDebut = (calendarDtFin.get(Calendar.MONTH)+1)+"/"+calendarDtFin.get(Calendar.YEAR);
			dateDebut = DateUtil.stringToDate("01/"+dtDebut);
		}
		if(minMaxDate[0] != null){
			httpUtil.setRequestAttribute("minDate", DateUtil.getDiffMonth(new Date(), minMaxDate[0]));
		}
		if(minMaxDate[1] != null){
			httpUtil.setRequestAttribute("maxDate", DateUtil.getDiffMonth(new Date(), minMaxDate[1]));
		}
		httpUtil.setRequestAttribute("dateDebut", (dtDebut.length()==6 ? "0"+dtDebut:dtDebut));
		
		if(articleArray == null || articleArray.length == 0){
			if(httpUtil.getRequest().getParameterValues("article_fltr") == null){
				httpUtil.setDynamicUrl("/domaine/stock/etat_stock_list.jsp");
			} else{
				httpUtil.setDynamicUrl("/domaine/stock/etat_stock_list_include.jsp");
			}
			return;
		}
		
		// Ajouter les sous familles à la requette ------------------------
		Set<String> listFam = new HashSet<>();
		Map<String, Object> mapData = cplxTable.getFilterCriteria();
		if(mapData != null){
			for(String key : mapData.keySet()){
				if(!key.equals("article.opc_famille_stock.id")){
					continue;
				}
				
				if(StringUtil.isNotEmpty(mapData.get(key))){
					String[] data = (String[]) mapData.get(key);
					for (String famIdSt : data) {
						if(StringUtil.isEmpty(famIdSt)){
							continue;
						}
						Long famId = Long.valueOf(famIdSt);
						
						listFam.add(famIdSt);
						
						List<FamillePersistant> familleEnfant = familleService.getFamilleEnfants("CU", famId, true);
						if(familleEnfant != null){
							for (FamillePersistant famillePersistant : familleEnfant) {
								listFam.add(famillePersistant.getId().toString());
							}
						}
					}
				}
			}
		}
		//
		if(listFam.size() > 0){
			mapData.put("article.famille_cuisine_id", listFam.toArray(new String[listFam.size()]));
		}
		//-------------------------------------------------------------
		String query = "from ArticlePersistant article where article.is_stock = true";
		
		if(articleArray != null && articleArray.length > 0){
			String ids = StringUtil.getStringDelimFromStringArray(articleArray, ",");
			if(StringUtil.isNotEmpty(ids)){
				query = query + " and article.id in ("+ids+") ";
			}
		}
		query = query + " order by article.opc_famille_stock.b_left, article.code, article.libelle";
		
		List<ArticlePersistant> listArticle = (List<ArticlePersistant>) articleService.findByCriteria(cplxTable, query);
		
		for (ArticlePersistant articlePersistant : listArticle) {
			List<FamillePersistant> familleStr = familleService.getFamilleParent("ST", articlePersistant.getOpc_famille_stock().getId());
			articlePersistant.setFamilleStr(familleStr);
		}

		
		Map<String, Object> mapDataArt = etatStockService.getEtatDetail(dateDebut, listArticle);
		httpUtil.setRequestAttribute("mapRecapArt", mapDataArt.get("map_total"));
		mapDataArt.remove("map_total");
		
		httpUtil.setRequestAttribute("map_article", mapDataArt);
		httpUtil.setRequestAttribute("list_article", listArticle);
		
		Date dateProchaine = DateUtil.addSubstractDate(dateDebut, TIME_ENUM.MONTH, +1);
		String dateFin = DateUtil.dateToString(dateProchaine);
		String[] split = dateFin.split("/");
		String dateF = split[1]+"/"+split[2];
		httpUtil.setRequestAttribute("dateFin", dateF);
		
		if(httpUtil.getRequest().getParameterValues("article_fltr") == null){
			httpUtil.setDynamicUrl("/domaine/stock/etat_stock_list.jsp");
		} else{
			httpUtil.setDynamicUrl("/domaine/stock/etat_stock_list_include.jsp");
		}
	}
	
}
