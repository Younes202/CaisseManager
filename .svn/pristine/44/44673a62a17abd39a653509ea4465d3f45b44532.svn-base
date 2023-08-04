package appli.controller.domaine.caisse_restau.action;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import appli.controller.domaine.caisse.action.CaisseBaseAction;
import appli.controller.domaine.caisse.bean.CaisseBean;
import appli.model.domaine.caisse.service.ICaisseService;
import appli.model.domaine.caisse.service.IJourneeService;
import appli.model.domaine.caisse.service.IMenuCompositionService;
import appli.model.domaine.stock.service.IArticleService;
import appli.model.domaine.vente.persistant.JourneePersistant;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.common.util.DateUtil;
import framework.model.common.util.DateUtil.TIME_ENUM;

@WorkController(nameSpace="caisse", bean=CaisseBean.class, jspRootPath="/domaine/caisse/back-office")
public class CaisseAction extends CaisseBaseAction {
	@Inject
	private ICaisseService caisseService;	
	@Inject
	private IMenuCompositionService menuCompositionService;
	@Inject
	private IJourneeService JourneeService;
	@Inject
	private IArticleService articleService;
	
	/**
	 * @param httpUtil
	 */
	public void tempsGlobalParEmploye(ActionUtil httpUtil){
		httpUtil.setRequestAttribute("listeMenu", menuCompositionService.getListeMenu(true, false));
		httpUtil.setRequestAttribute("listArticle", articleService.getListArticleNonStock(false));
		
		Date dateRef = null;
		JourneePersistant lastJrn = JourneeService.getLastJournee();
		if(lastJrn != null){
			dateRef = lastJrn.getDate_journee();
		} else{
			dateRef = new Date();
		}
		
		Date dateDebut = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateDebut"));
		Date dateFin = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateFin"));
		//
		boolean isFilter = httpUtil.getRequest().getParameter("is_fltr") != null;
		if(!isFilter) {
			dateDebut = DateUtil.stringToDate(httpUtil.getParameter("dateDebut"));
			dateFin = DateUtil.stringToDate(httpUtil.getParameter("dateFin"));
		}
		
		if(dateDebut == null) {
			dateDebut = dateRef;
			dateFin = dateRef;
			httpUtil.getDate("dateDebut").setValue(dateDebut);
			httpUtil.getDate("dateFin").setValue(dateDebut);
		}
		
		if(httpUtil.getParameter("prev") != null) {
			dateDebut = DateUtil.addSubstractDate(dateDebut, TIME_ENUM.DAY, -1);
			dateFin = DateUtil.addSubstractDate(dateFin, TIME_ENUM.DAY, -1);
		} else if(httpUtil
				.getParameter("next") != null) {
			dateDebut = DateUtil.addSubstractDate(dateDebut, TIME_ENUM.DAY, 1);
			dateFin = DateUtil.addSubstractDate(dateFin, TIME_ENUM.DAY, 1);
		}
		
		dateDebut = DateUtil.getStartOfDay(dateDebut);
		dateFin = DateUtil.getEndOfDay(dateFin);
		
		httpUtil.setRequestAttribute("dateDebut", dateDebut);
		httpUtil.setRequestAttribute("dateFin", dateFin);
		
		String[] articles = httpUtil.getRequest().getParameterValues("articles");
		String[] menus = httpUtil.getRequest().getParameterValues("menus");
		
		List<Object> listTempsGlobalParEmploye = caisseService.tempsGlobalParEmploye(dateDebut, dateFin, articles, menus);
		httpUtil.setRequestAttribute("listTempsGlobalParEmploye", listTempsGlobalParEmploye);
		
		httpUtil.setMenuAttribute("tpSuivi", "stat");
		
		httpUtil.setDynamicUrl("/domaine/caisse//back-office/mouvements_statutCmd_list.jsp");
	}
	
	public void telechargerApk(ActionUtil httpUtil){
		httpUtil.doDownload(new File("../../../conf/CaisseManager.apk"), true);
	}
}
