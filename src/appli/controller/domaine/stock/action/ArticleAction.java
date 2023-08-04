package appli.controller.domaine.stock.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.hibernate.criterion.Order;

import appli.controller.domaine.administration.ParametrageRightsConstantes;
import appli.controller.domaine.administration.bean.ValTypeEnumBean;
import appli.controller.domaine.stock.bean.ArticleBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.SOFT_ENVS;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_MOUVEMENT_ENUM;
import appli.model.domaine.administration.persistant.ParametragePersistant;
import appli.model.domaine.administration.service.IParametrageService;
import appli.model.domaine.administration.service.IValTypeEnumService;
import appli.model.domaine.personnel.persistant.ClientPersistant;
import appli.model.domaine.stock.persistant.ArticleDetailPersistant;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.stock.persistant.FournisseurPersistant;
import appli.model.domaine.stock.persistant.MouvementArticlePersistant;
import appli.model.domaine.stock.service.IArticleService;
import appli.model.domaine.stock.service.IFamilleService;
import appli.model.domaine.stock.service.IFournisseurService;
import appli.model.domaine.stock.service.IMouvementService;
import appli.model.domaine.util_srv.printCom.ticket.PrintCodeBarreUtil;
import appli.model.domaine.util_srv.printCom.ticket.PrintEtiquetteUtil;
import appli.model.domaine.vente.persistant.CaissePersistant;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.ControllerUtil;
import framework.controller.annotation.WorkController;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.DateUtil.TIME_ENUM;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.ModelConstante;
import framework.model.util.audit.ReplicationGenerationEventListener;
import framework.model.util.printGen.PrintPosBean;


@WorkController(nameSpace="stock", bean=ArticleBean.class, jspRootPath="/domaine/stock/")
public class ArticleAction extends ActionBase {
	@Inject
	private IFamilleService familleService;
	@Inject
	private IValTypeEnumService valEnumService;
	@Inject
	private IArticleService articleService;
	@Inject
	private IParametrageService parametrageService;
	@Inject
	private IFournisseurService fournisseurService;
	@Inject
	private IMouvementService mouvementService;
	
	public void work_init(ActionUtil httpUtil) {
		// Pour savoir si on est dans le composant ou dans l'article
		if(httpUtil.getParameter("tp") != null){
			httpUtil.setMenuAttribute("tp", httpUtil.getParameter("tp"));
		}
		
		boolean isRestau = SOFT_ENVS.restau.toString().equals(StrimUtil.getGlobalConfigPropertie("context.soft"));
		
		String[][] listeDestination = new String[][]{{"C", "Cuisine"}, {"P", "Présentoire"}};
		httpUtil.setRequestAttribute("listeDestination", listeDestination);
		
		String[][] dataTypeCmd = new String[][]{{"P", "Sur place"}, {"EL", "A emporter et livraison"}};
		httpUtil.setRequestAttribute("dataTypeCmd", dataTypeCmd);
		
		String[][] typeMouvement = new String[][] { { "a", "Achat" }, {"t", "Transfert"}, {"p", "Perte"}, {"i", "Inventaire"}, {"c", "Consommation"}, {"v", "Vente"} };
		httpUtil.setRequestAttribute("typeMouvement", typeMouvement);

		List listFamille = familleService.getListeFamille((isRestau?"CU":"ST"), true, false);
		httpUtil.setRequestAttribute("listeFaimlle", listFamille);
		
		if(httpUtil.isEditionPage()){
			
			// Familles de la balance
			String familleBalance = "|";
			List<CaissePersistant> listCaisse = articleService.getListCaisseActive(ContextAppli.TYPE_CAISSE_ENUM.BALANCE.toString());
			for (CaissePersistant caisseP : listCaisse) {
				if(StringUtil.isEmpty(caisseP.getFamille_balance())){
					continue;
				}
				String[] famBalances = StringUtil.getArrayFromStringDelim(caisseP.getFamille_balance());
				if(famBalances != null) {
					for (String fam : famBalances) {
						if(StringUtil.isNotEmpty(fam)){
							familleBalance += EncryptionUtil.encrypt(fam)+"|";
						}
					}
				}
			}
			httpUtil.setRequestAttribute("familleBalance", familleBalance);
			
			List<ValTypeEnumBean> listValeurs = valEnumService.getListValeursByType(ModelConstante.ENUM_UNITE);
			httpUtil.setRequestAttribute("listeUnite", listValeurs);
			
			List<ValTypeEnumBean> listTva = valEnumService.getListValeursByType(ModelConstante.ENUM_TVA);
			httpUtil.setRequestAttribute("listeTva", listTva);
			
			Long articleId = httpUtil.getWorkIdLong();
			if(articleId != null){
				httpUtil.setMenuAttribute("articleId", articleId);
			} else{
				articleId = (Long)httpUtil.getMenuAttribute("articleId");
			}
			httpUtil.setRequestAttribute("listEtablissement", familleService.findAllNoFilter(EtablissementPersistant.class, Order.asc("nom")));
		} else{
			httpUtil.setRequestAttribute("listArticle", articleService.getListArticleNonStock(false));
		}
		
		if(ContextAppli.getAbonementBean().isOptPlusEtsCentrale() 
				&& StringUtil.isTrue(StrimUtil.getGlobalConfigPropertie("CTRL_CENTRALE"))) {
			httpUtil.setRequestAttribute("isEditable", false);
		} else {
			httpUtil.setRequestAttribute("isEditable", true);
		}
	}
	
	@Override
	public void work_find(ActionUtil httpUtil) {
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_article");
		boolean isRestauEnv = SOFT_ENVS.restau.toString().equals(StrimUtil.getGlobalConfigPropertie("context.soft"));
		
		// Ajouter les sous familles à la requette ------------------------
		Set<String> listFam = new HashSet<>();
		Map<String, Object> mapData = cplxTable.getFilterCriteria();
		if(mapData != null){
			for(String key : mapData.keySet()){
				if(!key.equals(isRestauEnv ? "article.opc_famille_cuisine.id":"article.opc_famille_stock.id")){
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
						
						List<FamillePersistant> familleEnfant = familleService.getFamilleEnfants(isRestauEnv ? "CU":"ST", famId, false);
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
			mapData.put(isRestauEnv ? "article.opc_famille_cuisine.id":"article.opc_famille_stock.id", listFam.toArray(new String[listFam.size()]));
		}
		//-------------------------------------------------------------
		String codeBarre = httpUtil.getParameter("cb");
		String req = "from ArticlePersistant article" +
				" where article.code != 'GEN' and (article.is_stock is null or article.is_stock=0) "
		
//		if("on".equals(httpUtil.getParameter("actifs-activator"))){
//			req = req + " and (is_disable is null or is_disable=0) ";
//		}
		
		+ getFilterStateRequest(httpUtil, "is_disable");
		
		if(StringUtil.isNotEmpty(codeBarre)){
			req = req + " and article.code_barre='"+codeBarre+"'";
		}
		
		req = req + " order by "+(isRestauEnv ? "article.opc_famille_cuisine.b_left":"article.opc_famille_stock.b_left")+", article.ordre, article.code, article.libelle";
		
		List<ArticlePersistant> listData = (List<ArticlePersistant>) articleService.findByCriteria(cplxTable, req);

		for (ArticlePersistant articlePersistant : listData) {
			List<FamillePersistant> familleStr = null;
			if(isRestauEnv){
				if(articlePersistant.getOpc_famille_cuisine() != null){
					familleStr = familleService.getFamilleParent("CU", articlePersistant.getOpc_famille_cuisine().getId());
				}
			} else{
				if(articlePersistant.getOpc_famille_stock() != null){
					familleStr = familleService.getFamilleParent("ST", articlePersistant.getOpc_famille_stock().getId());
				}
			}
			articlePersistant.setFamilleStr(familleStr);
		}

		httpUtil.setRequestAttribute("list_article", listData);
		
		httpUtil.setDynamicUrl("/domaine/stock/article_list.jsp");
	}
	
	@Override
	public void work_merge(ActionUtil httpUtil) {
		ArticleBean articleBean = (ArticleBean) httpUtil.getViewBean();
		articleBean.setId(httpUtil.getWorkIdLong());
		
		if(httpUtil.getWorkIdLong() != null){
			ArticleBean articleDB = articleService.findById(httpUtil.getWorkIdLong());
			articleBean.setList_empl_seuil(articleDB.getList_empl_seuil());
			articleBean.setList_generic(articleDB.getList_generic());
		}
		articleBean.setIs_stock(false);

		setDataList(httpUtil);
		articleBean.setDate_maj(new Date());
		
		Map<String, Object> prixVenteEts = null;
		if(ReplicationGenerationEventListener._IS_CLOUD_SYNCHRO_INSTANCE){
			prixVenteEts = httpUtil.getValuesByStartNameNatif("prix_vente_ets_");
			String prix_vente_ets = "";
			if(prixVenteEts != null){
				for(String key : prixVenteEts.keySet()){
					Long etsId = Long.valueOf(key);
					BigDecimal prixVente = BigDecimalUtil.get(""+prixVenteEts.get(key));
					prix_vente_ets = prix_vente_ets+etsId+":"+prixVente+";";
				}
			}
			articleBean.setPrix_vente_ets(prix_vente_ets);
		}
		
		super.work_merge(httpUtil);
		
		if(ReplicationGenerationEventListener._IS_CLOUD_SYNCHRO_INSTANCE){
			if(prixVenteEts != null){
				for(String key : prixVenteEts.keySet()){
					Long etsId = Long.valueOf(key);
					BigDecimal prixVente = BigDecimalUtil.get(""+prixVenteEts.get(key));
					
					articleBean.setOpc_etablissement(articleService.findById(EtablissementPersistant.class, etsId));
					articleBean.setPrix_vente(prixVente);
					
					articleService.merge(articleBean);
				}
			}
		}
		
		managePieceJointe(httpUtil, articleBean.getId(), "article", 300, 300);
	}

	@Override
	public void work_delete(ActionUtil httpUtil) {
		Long workId = httpUtil.getWorkIdLong();
		
		super.work_delete(httpUtil);
		manageDeleteImage(workId, "article");
	}
	
	public void init_print_fiche(ActionUtil httpUtil){
		// Liste des imprimantes
        httpUtil.setRequestAttribute("list_imprimante", ParametrageRightsConstantes.getListPrinters());
        //
        String[][] orientationBalance = {{"L", "Paysage"}, {"LR", "Paysage inversé"}, {"P", "Portrait"}};
        httpUtil.setRequestAttribute("orientationBalance", orientationBalance);
       
        String[] imprimantesParams = {"ETIQUETTE_PRIX", "ETIQUETTE_BARRE", "ETIQUETTE_BALANCE"};
        for (String param : imprimantesParams) {
        	List<ParametragePersistant> params = parametrageService.getParameterByGroupe(param);
			httpUtil.setRequestAttribute("param_"+param, params);			
		}
        
        httpUtil.setDynamicUrl("/domaine/stock/print/etiquette_conf.jsp");
	}
	
	public void update_params_imprimante(ActionUtil httpUtil) {
		String tab = httpUtil.getParameter("tab");
		if(tab == null){
			tab = "ETIQUETTE_PRIX";
		}
		Map<String, Object> params = httpUtil.getValuesByStartName("param_");
		parametrageService.updateParams(params);
		
//		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "", "Les paramétres de l'imprimante sont mise à jour.");
		//
		httpUtil.writeResponse("MSG_CUSTOM:Paramètres mises à jour.");
	}
	
	/**
	 * @param httpUtil
	 */
	@SuppressWarnings({ "unchecked" })
	private void setDataList(ActionUtil httpUtil) {
		ArticleBean articleBean = (ArticleBean) httpUtil.getViewBean();
		articleBean.setId(httpUtil.getWorkIdLong());
		
		List<ArticleDetailPersistant> listArticleDet = (List<ArticleDetailPersistant>) httpUtil.buildListBeanFromMap("opc_article_composant.id",
														ArticleDetailPersistant.class, "eaiid", "opc_article_composant.id", "quantite", "mode_destock");

		List<ArticleDetailPersistant> listArticle = new ArrayList<>();
		if (articleBean.getId() != null) {
			ArticleBean artBean = articleService.findById(articleBean.getId());
			listArticle = artBean.getList_article();
			listArticle.clear();
		}
		listArticle.addAll(listArticleDet);
		articleBean.setList_article(listArticle);
	}
	
	/**
	 * @param httpUtil
	 */
	public String prix_ttc(BigDecimal prix_ht, String tva) {
		BigDecimal prix_ttc = BigDecimalUtil.ZERO;
		if(StringUtil.isNotEmpty(prix_ht) && StringUtil.isNotEmpty(tva)){
			prix_ttc = BigDecimalUtil.add(prix_ht, BigDecimalUtil.multiply(prix_ht, BigDecimalUtil.divide(BigDecimalUtil.get(tva), BigDecimalUtil.get(100))));
		}
		return BigDecimalUtil.formatNumberZero(prix_ttc, null).replaceAll(",", ".");
	}
	
	/**
	 * @param httpUtil
	 */
	public void generer_code(ActionUtil httpUtil) {
		boolean isRestauEnv = SOFT_ENVS.restau.toString().equals(StrimUtil.getGlobalConfigPropertie("context.soft"));
		Long famId = null;
		if(isRestauEnv){
			famId = httpUtil.getLongParameter("article.opc_famille_cuisine.id");	
		} else{
			famId = httpUtil.getLongParameter("article.opc_famille_stock.id");
		}
		
		if(famId == null) {
			httpUtil.writeResponse("");
			return;
		}
		String code = articleService.generateCode(famId, "ART");
		httpUtil.writeResponse(code);

	}
	
	/**
	 * @param httpUtil
	 */
	public void print_fiche_article(ActionUtil httpUtil){
		Long[] checkedArticles = httpUtil.getCheckedElementsLong("list_article");
		
		if(checkedArticles == null || checkedArticles.length == 0){
			MessageService.addGrowlMessage("", "Veuillez sélectionner au moins un article.");
			return;
		}
		
		List<ArticlePersistant> listArt = articleService.getListArticleChecked(checkedArticles);
		//
		for (ArticlePersistant artP : listArt) {
			if(artP.getOpc_unite_vente_enum() != null){
				artP.getOpc_unite_vente_enum().getId();
			}
		}
		
		httpUtil.setRequestAttribute("type", "ETQ");
		
		PrintEtiquetteUtil pu = new PrintEtiquetteUtil(listArt);
		
		boolean isAsync = printData(httpUtil, pu.getPrintPosBean());
		if(!isAsync) {
			httpUtil.writeResponse("MSG_CUSTOM:Impression effectuée");
		} else {
			forwardToPriterJsp(httpUtil);
		}
	}
	
	/**
	 * @param httpUtil
	 */
	public void print_barre_article(ActionUtil httpUtil){
		Long[] checkedArticles = httpUtil.getCheckedElementsLong("list_article");
		
		if(checkedArticles == null || checkedArticles.length == 0){
			MessageService.addGrowlMessage("", "Veuillez sélectionner au moins un article.");
			return;
		}
		List<PrintPosBean> listPrintPosBean = new ArrayList<>();
		String retour = "";
		for (int i = 0; i < checkedArticles.length; i++) {
			ArticlePersistant articleP = articleService.findById(checkedArticles[i]);
			
			if(StringUtil.isEmpty(articleP.getCode_barre())){ 
				continue;
			}
			
			PrintCodeBarreUtil printCodeBarreUtil = new PrintCodeBarreUtil(articleP.getCode_barre(), articleP);
			retour += StringUtil.getValueOrEmpty(printCodeBarreUtil.generateAndPrintCodeBarreEan13());
			
			listPrintPosBean.add(printCodeBarreUtil.getPrintPosBean());	
		}
		
		boolean isAsync = printData(httpUtil, listPrintPosBean);
		if(isAsync) {
			forwardToPriterJsp(httpUtil);
			return;
		}
		
		if(StringUtil.isNotEmpty(retour)){
    		MessageService.addBannerMessage(retour);
    		return;
    	}
		
		//
		httpUtil.setRequestAttribute("type", "CB");
		httpUtil.writeResponse("MSG_CUSTOM:Impression effectuée");
	}
	
	public void getArticlesByCodeBarre(ActionUtil httpUtil) {
		String value = httpUtil.getParameter("cb");
		ArticlePersistant articleP = articleService.getArticleByCodeBarre(value, ContextAppli.IS_RESTAU_ENV());

		if(articleP != null){
			String json = ControllerUtil.getJSonDataAnnotStartegy(articleP);
			httpUtil.writeResponse(json);
		} else{
			httpUtil.writeResponse("ERREUR : Cet article n'existe pas.");
		}
	}
	
	/**
	 * @param httpUtil
	 */
	public void desactiver(ActionUtil httpUtil) {
		articleService.activerDesactiverElement(httpUtil.getWorkIdLong());
		work_find(httpUtil);
	}
	
	public void work_post(ActionUtil httpUtil){
		manageDataForm(httpUtil, "ARTICLE");
		
		// --------------------------- DYN form --------------------------
		httpUtil.setRequestAttribute("listDataValueForm", articleService.loadDataForm(null, "ARTICLE"));
		// ---------------------------------------------------------------
	}
	
	public void init_histo_prix(ActionUtil httpUtil) {
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_prix_fournisseur");
		//
		Long fournisseurId = httpUtil.getLongParameter("fourn");
		if(fournisseurId != null){
			httpUtil.setMenuAttribute("fournisseurId", fournisseurId);
		}
		fournisseurId = (Long) httpUtil.getMenuAttribute("fournisseurId");
		//
		Long clientId = (Long)httpUtil.getLongParameter("cli");
		if(clientId != null){
			httpUtil.setMenuAttribute("clientId", clientId);	
		}
		clientId = (Long) httpUtil.getMenuAttribute("clientId");
		//
		Long articleId = (Long)httpUtil.getLongParameter("art");
		if(articleId != null){
			httpUtil.setMenuAttribute("articleId", articleId);
		}
		articleId = (Long) httpUtil.getMenuAttribute("articleId");
		
		if(articleId == null){
			MessageService.addBannerMessage("Veuillez sélectionner un article");
			return;
		}
		
		Map<String, Object> formCriterion = cplxTable.getFormBean().getFormCriterion();
		formCriterion.put("fournisseurId", fournisseurId);
		formCriterion.put("clientId", clientId);
		formCriterion.put("articleId", articleId);
		
		if(clientId != null){
			formCriterion.put("typeMvmnt", TYPE_MOUVEMENT_ENUM.v.toString());			
		} else{
			formCriterion.put("typeMvmnt", TYPE_MOUVEMENT_ENUM.a.toString());
		}
		
		List<MouvementArticlePersistant> listData = (List<MouvementArticlePersistant>) articleService.findByCriteriaByQueryId(cplxTable, "histo_prix_mvm_find");
		
		httpUtil.setRequestAttribute("list_prixFournisseur", listData);
		
		 List<FournisseurPersistant> listFournisseur = fournisseurService.getListFournisseur(true, false);
		 httpUtil.setRequestAttribute("listeFournisseur", listFournisseur);
		 List<ClientPersistant> listClient = fournisseurService.findAll(ClientPersistant.class, Order.asc("nom"));
		 httpUtil.setRequestAttribute("listClient", listClient);
		 
		httpUtil.setDynamicUrl("/domaine/stock/historique_prix_fournisseur.jsp");
	}
	
	public void majStockAll(ActionUtil httpUtil){
		Date dateRef = DateUtil.addSubstractDate(new Date(), TIME_ENUM.YEAR, -3);
		new Thread(() -> {
			mouvementService.majQteStockArticle(dateRef);
		}).start();
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "", "Le recalcul est lancé. Cette tâche prendra quelques instants");
		
		httpUtil.setDynamicUrl("admin.job.work_find");

	}
	
	public void fusionnerArticleDoublon(ActionUtil httpUtil){
		int nbr = articleService.fusionnerArticleDoublon();
		httpUtil.writeResponse("MSG_CUSTOM:<h3>Le traitement de fusion des composant est terminé [<b>"+nbr +"</b> fusions].</h3>");
	}
	
	public void purgerDoublonStockInfo(ActionUtil httpUtil) {
		articleService.purgerDoublonStockInfo();
		httpUtil.writeResponse("MSG_CUSTOM:<h3>La purge des doublons est effectuée.</h3>");
	}
	
//	
//	/**
//	 * @param httpUtil
//	 */
//	public void work_post(ActionUtil httpUtil) {
//		if(httpUtil.isCrudOperationOK()){
//			ArticleBean articleBean = (ArticleBean)httpUtil.getViewBean();
//			if(articleBean != null){
//				UploadFileUtil.saveFilesToDir(httpUtil.getRequest(), "article_CU", EncryptionUtil.encrypt(articleBean.getId().toString()));
//			}
//		}
//	}
}
