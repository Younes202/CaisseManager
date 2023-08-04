package appli.controller.domaine.caisse.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import appli.controller.domaine.caisse.ContextAppliCaisse;
import appli.controller.domaine.personnel.bean.EmployeBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.caisse.persistant.ArticleBalancePersistant;
import appli.model.domaine.caisse.service.IArticle2Service;
import appli.model.domaine.caisse.service.ICaisseService;
import appli.model.domaine.caisse.service.ICaisseWebService;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.stock.persistant.FamilleStockPersistant;
import appli.model.domaine.stock.service.IArticleService;
import appli.model.domaine.stock.service.IFamilleService;
import appli.model.domaine.util_srv.printCom.ticket.PrintCodeBarreBalanceUtil;
import appli.model.domaine.vente.persistant.JourneePersistant;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.ContextGloabalAppli;
import framework.controller.ControllerUtil;
import framework.controller.annotation.WorkController;
import framework.controller.bean.PagerBean;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.StringUtil;

@WorkController(nameSpace = "caisse-web", bean = EmployeBean.class, jspRootPath = "/domaine/caisse/balance/")
public class BalanceAction extends ActionBase {
	@Inject
	private ICaisseWebService caisseWebService;	
	@Inject
	private IFamilleService familleService;
	@Inject
	private IArticleService articleService;
	@Inject
	private ICaisseService caisseService;
	@Inject
	private IArticle2Service articleService2;
	
	public void work_init(ActionUtil httpUtil) {
		
	}
	
	/**
	 * @param httpUtil
	 */
	@SuppressWarnings("unchecked")
	private void manageDataSession(ActionUtil httpUtil) {
		List<String> HISTORIQUE_NAV = (List<String>) httpUtil.getUserAttribute("HISTORIQUE_NAV");
		if (HISTORIQUE_NAV == null) {
			HISTORIQUE_NAV = new ArrayList<>();
			httpUtil.setUserAttribute("HISTORIQUE_NAV", HISTORIQUE_NAV);
		}
		HISTORIQUE_NAV.clear();
	}
	
	/**
	 * Ajouter l'article à la commande
	 * @param articleId
	 */
	public void addArticleFamilleCmd(ActionUtil httpUtil) {
		ArticlePersistant articleP = null;
		String codeBarre = httpUtil.getParameter("cb");
		if(StringUtil.isNotEmpty(codeBarre)) {
			
			String codeBarre2 = codeBarre.trim().replaceAll("", "");
			articleP = articleService.getArticleByCodeBarre(codeBarre2, ContextAppli.IS_RESTAU_ENV());
			//
			if(articleP == null) {
				MessageService.addGrowlMessage("Article non reconnu", "Ce code barre n'est associé à aucun produit.");
				return;
			}
			
			List<FamillePersistant> listParent = familleService.getFamilleParent("ST", articleP.getOpc_famille_stock().getId());
			for (FamillePersistant famillePersistant : listParent) {
				((List<String>) httpUtil.getUserAttribute("HISTORIQUE_NAV")).add("FAM_" + famillePersistant.getId());
			}
		} else {
			Long articleId = httpUtil.getWorkIdLong();
			articleP = articleService.findById(articleId);
		}
		httpUtil.setRequestAttribute("article", articleP);
		
		httpUtil.setDynamicUrl("/domaine/caisse/balance/poids-form.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void addArticlePoids(ActionUtil httpUtil){
		boolean isCodeBarre =  "C".equals(ContextGloabalAppli.getGlobalConfig("BALANCE_MODE"));
		BigDecimal poids = BigDecimalUtil.get(httpUtil.getParameter("art.poids"));
		Long articleId = Long.valueOf(httpUtil.getParameter("art"));
		
		if(poids == null || BigDecimalUtil.isZero(poids)){
			MessageService.addBannerMessage("Veuillez saisir un poids positif.");
			return;
		}
		
		String balanceKey = caisseService.addArticleBalance(articleId, poids, isCodeBarre);
		//
		if(isCodeBarre){
			ArticlePersistant articleP = articleService.findById(articleId);
			
			if(StringUtil.isEmpty(articleP.getCode_barre())){
				MessageService.addBannerMessage("Veuillez ajouter un code barre dans la fiche de cet article.");
				return;
			} else if(articleP.getCode_barre().trim().length() > 6){
				MessageService.addBannerMessage("Le code barre des articles balance ne doit pas dépasser 6 caractères.");
				return;
			}
			
			PrintCodeBarreBalanceUtil pu = new PrintCodeBarreBalanceUtil(articleP, poids);
			printData(httpUtil, pu.getPrintPosBean());
			//
			init_home(httpUtil);
		} else{
			httpUtil.setRequestAttribute("balance_key", balanceKey);
			httpUtil.setDynamicUrl("/domaine/caisse/balance/poids-key-result.jsp");
		}
	}
	
	/**
	 * @param httpUtil
	 */
	public void upButtonEvent(ActionUtil httpUtil) {
		String upMnu = httpUtil.getParameter("up");
		List<String> listNav = (List<String>)httpUtil.getUserAttribute("HISTORIQUE_NAV");
		//
		
		if(listNav != null && StringUtil.isEmpty(upMnu) && listNav.size() > 0) {
			upMnu = listNav.get(listNav.size() - 1);
		}
		
		// 
		if(StringUtil.isNotEmpty(upMnu)){
			manageUpButton(httpUtil, upMnu);
			
			int idxMnu = listNav.indexOf(upMnu);
			int size = listNav.size();
			
			String[] toRemove = new String[size];
			int idx = 0;
			for(int i=idxMnu; i<size; i++) {
				toRemove[idx] = listNav.get(i);
				
			}
			for(String st : toRemove) {
				listNav.remove(st);
			}
		}
		httpUtil.setDynamicUrl("/domaine/caisse/balance/detail-choix.jsp");
	}
	
	/**
	 * @param name
	 */
	@SuppressWarnings({ "rawtypes" })
	private void manageUpButton(ActionUtil httpUtil, String name) {
		Long elementId = Long.valueOf(name.substring(4));// Retirer le prefix FAM_
		FamillePersistant parentFamille = familleService.getFamilleParent(elementId);
		if (parentFamille == null || parentFamille.getLevel() == 0) {
			return;
		}
		List<FamillePersistant> listSousFamille = familleService.getFamilleEnfants("ST", parentFamille.getId(), true);
		// Alimenter le panel des sous menus
		populateSousFamillePanel(httpUtil, parentFamille.getId(), listSousFamille);
	}
	
	/**
	 * @param listSousFamille
	 */
	@SuppressWarnings("unchecked")
	private void populateSousFamillePanel(ActionUtil httpUtil, Long familleId, List<FamillePersistant> listSousFamille) {
		FamilleStockPersistant parentfamille = (FamilleStockPersistant) familleService.getGenriqueDao().findById(familleId);

		httpUtil.setRequestAttribute("listFamille", listSousFamille);
		httpUtil.setRequestAttribute("listArticle", articleService.getListArticleNonStockActif(parentfamille.getId()));

		httpUtil.setDynamicUrl("/domaine/caisse/balance/detail-choix.jsp");
	}
	
	public void init_home(ActionUtil httpUtil) {
		// Familles
		PagerBean pagerBean = ControllerUtil.managePager(httpUtil.getRequest(), "FAM");
		
		List<FamilleStockPersistant> listFamille = caisseWebService.getListFamilleActive(pagerBean);
		httpUtil.setRequestAttribute("listFamille", listFamille);
		
		if(httpUtil.getParameter("sens") == null){
			httpUtil.setDynamicUrl("/domaine/caisse/balance/balance-right-bloc.jsp");
		} else{
			httpUtil.setDynamicUrl("/domaine/caisse/balance/pager-famille-include.jsp");
		}
	}
	public void init_fav(ActionUtil httpUtil) {
		PagerBean pagerBean = ControllerUtil.managePager(httpUtil.getRequest(), "FAV");
		httpUtil.setRequestAttribute("listArticleFavoris", caisseService.getFavorisCaisse(pagerBean));
		
		httpUtil.setDynamicUrl("/domaine/caisse/balance/pager-favoris-include.jsp");
	}
	
	@SuppressWarnings({ "unchecked" })
	public void familleEvent(ActionUtil httpUtil) {
		Long famId = httpUtil.getWorkIdLong();
		if(famId == null) {// Cas pagination
			famId = Long.valueOf(EncryptionUtil.decrypt(httpUtil.getParameter("fam")));
		}
        // Données de la session
     	manageDataSession(httpUtil);
     	
     	PagerBean pagerBean = ControllerUtil.managePager(httpUtil.getRequest(), "ART");
		List<FamillePersistant> listSousFamille = familleService.getFamilleEnfants("ST", famId, true);
		List<ArticlePersistant> listArticle = articleService.getListFamilleArticle(famId, pagerBean);
		
		((List<String>) httpUtil.getUserAttribute("HISTORIQUE_NAV")).add("FAM_" + famId);

		httpUtil.setRequestAttribute("listFamille", listSousFamille);
		httpUtil.setRequestAttribute("listArticle", listArticle);

		httpUtil.setDynamicUrl("/domaine/caisse/balance/detail-choix.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void initHistorique(ActionUtil httpUtil) {
		JourneePersistant journeeP = ContextAppliCaisse.getJourneeBean();
		httpUtil.setFormReadOnly(false);
		
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_mouvement");
		
		Map<String, Object> formCriterion = cplxTable.getFormBean().getFormCriterion();
		formCriterion.put("journeeId", journeeP.getId());
		Long caisseId = ContextAppliCaisse.getCaisseBean().getId();
		//
		formCriterion.put("caisseId", caisseId);
		
		String req = "from ArticleBalancePersistant articleBalance "
				+ "where articleBalance.opc_journee.id='[journeeId]' "
				+ "and articleBalance.opc_caisse.id='[caisseId]' ";
		req = req + "order by articleBalance.id desc";
		
		List<ArticleBalancePersistant> listArticleBalance = (List<ArticleBalancePersistant>) familleService.findByCriteria(cplxTable, req);
		httpUtil.setRequestAttribute("listArticleBalance", listArticleBalance);
		
		httpUtil.setDynamicUrl("/domaine/caisse/balance/historique-list.jsp");
	}
	public void loadArtCodeBarre(ActionUtil httpUtil) {
		httpUtil.setMenuAttribute("IS_FROM_ART", true);
		
		String codeBarre = httpUtil.getParameter("art.code_barre");
		String codeLibelle = httpUtil.getParameter("art.code");
		
		if(StringUtil.isEmpty(codeBarre) && StringUtil.isEmpty(codeLibelle)) {
			MessageService.addGrowlMessage("Erreur saisie", "Veuillez saisir un critère de recherche.");
			return;
		}
		//
		List<ArticlePersistant> listArticleBarre = articleService2.getArticlesByCodeBarre(codeBarre, codeLibelle, null);
		httpUtil.setRequestAttribute("listArticle", listArticleBarre);
		httpUtil.setRequestAttribute("nbrTotal", listArticleBarre.size());
		
		httpUtil.setDynamicUrl("/domaine/caisse/balance/detail-choix.jsp");
	}
}
