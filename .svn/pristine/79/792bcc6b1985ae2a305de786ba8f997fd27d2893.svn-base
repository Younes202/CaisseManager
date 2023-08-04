package appli.controller.domaine.caisse.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.Query;

import appli.controller.domaine.caisse.ContextAppliCaisse;
import appli.controller.domaine.personnel.bean.EmployeBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.caisse.persistant.ArticleStockCaisseInfoPersistant;
import appli.model.domaine.caisse.service.ICaisseService;
import appli.model.domaine.caisse.service.ICaisseWebService;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.stock.service.IFamilleService;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.StringUtil;

@WorkController(nameSpace = "caisse-web", bean = EmployeBean.class, jspRootPath = "/domaine/caisse/")
public class CaisseInventaireAction extends ActionBase {
	@Inject
	private IFamilleService familleService;
	@Inject
	private ICaisseWebService caisseWebService;
	@Inject
	private ICaisseService caisseService;
	
	public void work_init(ActionUtil httpUtil) {
		
	}
	
	public void init_inventaire(ActionUtil httpUtil) {
		boolean isRestau = ContextAppli.IS_RESTAU_ENV();
		String[] famInvFilter = null;
		EtablissementPersistant etsP = familleService.findById(EtablissementPersistant.class, ContextAppli.getEtablissementBean().getId());
		String[] famInvParams = StringUtil.getArrayFromStringDelim(etsP.getFam_caisse_inv(), ";");
		//
		boolean isSub = httpUtil.getParameter("isSub") != null;
		if(isSub) {
			String[] famIdsAr = httpUtil.getRequest().getParameterValues("famInvFilter");
			if(famIdsAr != null && famIdsAr.length > 0) {
				famInvFilter = StringUtil.getArrayFromStringDelim(famIdsAr[0], ",");
			}
			httpUtil.setRequestAttribute("famInvFilter", famInvFilter);
			
			//
			famInvParams = StringUtil.getArrayFromStringDelim(httpUtil.getRequest().getParameterValues("famInvParams")[0], ",");
		} else {
			famInvFilter = famInvParams;
		}
		
		if(isSub && (famInvFilter == null || famInvFilter.length == 0)) {
			famInvFilter = famInvParams;
		}
		
		httpUtil.setRequestAttribute("famInvParams", famInvParams);
		
		String artInvFilter = httpUtil.getParameter("artInvFilter");		
		String tp = (isRestau ? "CU" : "ST");
		
		CaisseMouvementPersistant CURRENT_COMMANDE = (CaisseMouvementPersistant) httpUtil.getUserAttribute("CURRENT_COMMANDE");
		Map<Long, BigDecimal> mapQteCmdTmp = caisseWebService.getEtatStockCmdTmp(CURRENT_COMMANDE, ContextAppliCaisse.getJourneeBean().getId());
		
		//---------------------------- Si click root --------------------
		//boolean isRootFam = StringUtil.isEmpty(famInvFilter);// && familleService.getFamilleRoot(tp).getId().equals(famInvFilter[0]);
		Set<Long> listFamIds = new HashSet<>();
		List<FamillePersistant> familleAll = new ArrayList<>();
		//
		
		if(StringUtil.isNotEmpty(famInvFilter)) {
			for(String famIdStr : famInvFilter) {
				if(StringUtil.isEmpty(famIdStr)) {
					continue;
				}
				familleAll.addAll(familleService.getFamilleEnfants(tp, Long.valueOf(famIdStr), true));
				for (FamillePersistant familleP : familleAll) {
					listFamIds.add(familleP.getId());
				}
				listFamIds.add(Long.valueOf(famIdStr));
			}
		} else {
			listFamIds.add(Long.valueOf(-99));
		}
		
		//
		String requestInfo = "from ArticleStockCaisseInfoPersistant art "
				+ "where (art.opc_article.is_disable is null or art.opc_article.is_disable=0) ";
		
		String request = "from ArticlePersistant art "
				+ "where (is_disable is null or is_disable=0) "
				+(isRestau ? "and (is_stock is null or is_stock=0) ":"")
				+ "and code != 'GEN' ";
	
		if(isRestau) {
			request += "and art.opc_famille_cuisine.id in (:familles) ";
			requestInfo += "and art.opc_article.opc_famille_cuisine.id in (:familles) ";
		} else {
			request += "and art.opc_famille_stock.id in (:familles) ";
			requestInfo += "and art.opc_article.opc_famille_stock.id in (:familles) ";
		}
		
		if(StringUtil.isNotEmpty(artInvFilter)){
			request += "and (art.code like:artId or art.libelle like:artId) ";
			requestInfo += "and (art.opc_article.code like:artId or art.opc_article.libelle like:artId) ";
		}
		request = request + "order by "+(isRestau ? "opc_famille_cuisine.b_left" : "opc_famille_stock.b_left")+", art.code, art.libelle";
		
		Query query = familleService.getQuery(request);
		Query queryInfo = familleService.getQuery(requestInfo);
		
		query.setParameter("familles", listFamIds);
		queryInfo.setParameter("familles", listFamIds);
		
		if(StringUtil.isNotEmpty(artInvFilter)){
			query.setParameter("artId", "%"+artInvFilter+"%");
			queryInfo.setParameter("artId", "%"+artInvFilter+"%");
		}
		List<ArticlePersistant> listArticle = query.getResultList();
		
		Map<Long, ArticleStockCaisseInfoPersistant> mapInfoTmp = new HashMap<>();
		List<ArticleStockCaisseInfoPersistant> listArtInfo = familleService.findAll(ArticleStockCaisseInfoPersistant.class);
		for (ArticleStockCaisseInfoPersistant articleStockCaisseInfoP : listArtInfo) {
			ArticleStockCaisseInfoPersistant cloneBean = (ArticleStockCaisseInfoPersistant) ReflectUtil.cloneBean(articleStockCaisseInfoP);
			mapInfoTmp.put(articleStockCaisseInfoP.getOpc_article().getId(), cloneBean);
		}
		// Prendre en compte les articles en attente
		Map<Long, ArticleStockCaisseInfoPersistant> mapInfo = new LinkedHashMap<>();
		for (ArticlePersistant articleP : listArticle) {
			ArticleStockCaisseInfoPersistant info = mapInfoTmp.get(articleP.getId());
			if(info == null) {
				info = new ArticleStockCaisseInfoPersistant();
				info.setOpc_article(articleP);
				info.setArticle_lib(articleP.getLibelle());
			}
			mapInfo.put(articleP.getId(), info);
			
			BigDecimal qteFinal = BigDecimalUtil.substract(info.getQte_reel(), mapQteCmdTmp.get(articleP.getId()));
			info.setQte_reel(qteFinal);
		}
		
		List listFamille = familleService.getListeFamille(tp, true, false);
		httpUtil.setRequestAttribute("listeFamille", listFamille);
		httpUtil.setRequestAttribute("mapInfo", mapInfo);
		
		httpUtil.setRequestAttribute("artInvFilter", artInvFilter);
		
		httpUtil.setDynamicUrl("/domaine/caisse/caisse_inventaire.jsp");
	}
	
	/**
	 * Sauvegarde temporaires
	 * @param httpUtil
	 */
	@SuppressWarnings("unchecked")
	public void saveSaisieInventaire(ActionUtil httpUtil){
		String[] famInvFilter = httpUtil.getRequest().getParameterValues("famInvParams");
		Set<Long> listFamIds = new HashSet<>();
		List<FamillePersistant> familleAll = new ArrayList<>();
		boolean isRestau = ContextAppli.IS_RESTAU_ENV();
		String tp = (isRestau ? "CU" : "ST");
		
		//
		for(String famIdStr : famInvFilter) {
			if(StringUtil.isEmpty(famIdStr)) {
				continue;
			}
			familleAll.addAll(familleService.getFamilleEnfants(tp, Long.valueOf(famIdStr), true));
			for (FamillePersistant familleP : familleAll) {
				listFamIds.add(familleP.getId());
			}
			listFamIds.add(Long.valueOf(famIdStr));
		}
		
		caisseService.deleteCaisseInfoInv(listFamIds, isRestau);
		
		CaisseMouvementPersistant CURRENT_COMMANDE = (CaisseMouvementPersistant) httpUtil.getUserAttribute("CURRENT_COMMANDE");
		List<ArticleStockCaisseInfoPersistant> listInventaireDetIHM = (List<ArticleStockCaisseInfoPersistant>) httpUtil.buildListBeanFromMap(
				"opc_article.id",
				ArticleStockCaisseInfoPersistant.class, 
				"eaiid", "opc_article.id", "article_lib", "qte_reel", "opc_emplacement.id");
		// Update
		caisseService.updateCaisseInfoInv(famInvFilter, listInventaireDetIHM, CURRENT_COMMANDE);
		
		httpUtil.writeResponse("MSG_CUSTOM:L'inventaire est mise à jour");
	}
}
