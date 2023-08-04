package appli.controller.domaine.stock.action;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.hibernate.criterion.Order;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import appli.controller.domaine.administration.bean.ValTypeEnumBean;
import appli.controller.domaine.stock.bean.ArticleBean;
import appli.controller.domaine.stock.bean.MarqueBean;
import appli.controller.domaine.stock.bean.MouvementBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.SOFT_ENVS;
import appli.model.domaine.administration.persistant.TypeEnumPersistant;
import appli.model.domaine.administration.persistant.ValTypeEnumPersistant;
import appli.model.domaine.administration.service.IValTypeEnumService;
import appli.model.domaine.compta.persistant.PaiementPersistant;
import appli.model.domaine.stock.persistant.ArticleDetailPersistant;
import appli.model.domaine.stock.persistant.ArticleGeneriquePersistant;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.persistant.ArticleStockInfoPersistant;
import appli.model.domaine.stock.persistant.EmplacementPersistant;
import appli.model.domaine.stock.persistant.EmplacementSeuilPersistant;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.stock.persistant.FamilleStockPersistant;
import appli.model.domaine.stock.persistant.FournisseurPersistant;
import appli.model.domaine.stock.persistant.InventairePersistant;
import appli.model.domaine.stock.persistant.MouvementArticlePersistant;
import appli.model.domaine.stock.persistant.MouvementPersistant;
import appli.model.domaine.stock.service.IArticleService;
import appli.model.domaine.stock.service.IFamilleService;
import appli.model.domaine.stock.service.IFournisseurService;
import appli.model.domaine.stock.service.IInventaireService;
import appli.model.domaine.stock.service.IMarqueService;
import appli.model.domaine.stock.service.IMouvementService;
import appli.model.domaine.vente.persistant.CaissePersistant;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.ControllerUtil;
import framework.controller.UploadFileUtil;
import framework.controller.annotation.WorkController;
import framework.controller.bean.action.IViewBean;
import framework.model.common.constante.ActionConstante;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.exception.ActionValidationException;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.ControllerBeanUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;
import framework.model.service.GenericJpaService;
import framework.model.util.ModelConstante;


@WorkController(nameSpace="stock", bean=ArticleBean.class, jspRootPath="/domaine/stock/")
public class ComposantAction extends ActionBase {
	@Inject
	private IFamilleService familleService;
	@Inject
	private IValTypeEnumService valEnumService;
	@Inject
	private IArticleService articleService;
	@Inject
	private IMouvementService mouvementService;
	@Inject
	private IMarqueService marqueService;
	@Inject
	private IFournisseurService fournisseurService;
	@Inject
	private IInventaireService inventaireService;
	
	public void work_init(ActionUtil httpUtil) {
		httpUtil.setMenuAttribute("IS_SUB_ADD","stock.composant.work_init_create");
		
		if(StringUtil.isTrue(httpUtil.getParameter("is_compo_inc"))){
			// Construction du bean à partir du mouvements pour le stocker
			Map params = (Map)httpUtil.getRequest().getAttribute(ProjectConstante.WORK_PARAMS);
			IViewBean mapToBean = (IViewBean)ControllerBeanUtil.mapToBean(MouvementBean.class, params);
			httpUtil.setViewBean(mapToBean);
			MouvementBean mouvementBean = (MouvementBean) httpUtil.getViewBean();
					
			MouvementBean mvmData = new MouvementAction().setDataList(httpUtil);
			
			mouvementBean.setList_article(mvmData.getList_article());
			mouvementBean.setMapOldArticleInfo(mvmData.getMapOldArticleInfo());
			
			if(mouvementBean.getList_article() != null){
				for(MouvementArticlePersistant art : mouvementBean.getList_article()){
					art.setOpc_article(articleService.findById(ArticlePersistant.class, art.getOpc_article().getId()));
				}
			}
			
			mouvementBean.setList_paiement((List<PaiementPersistant>) httpUtil.getMenuAttribute("PAIEMENT_DATA"));
			
			httpUtil.setMenuAttribute("mouvementTmpBean", mouvementBean);
		}
		
		getResourceBean().setService(articleService);
		
		// Pour savoir si on est dans le composant ou dans l'article
		if(httpUtil.getParameter("tp") != null){
			httpUtil.setMenuAttribute("tp", httpUtil.getParameter("tp"));
		}
		
		String[][] typeMouvement = new String[][] { { "a", "Achat" }, {"t", "Transfert"}, {"p", "Perte"}, {"i", "Inventaire"}, {"c", "Consommation"}, {"v", "Vente"} };
		httpUtil.setRequestAttribute("typeMouvement", typeMouvement);

		String[][] listeDestination = new String[][]{{"C", "Cuisine"}, {"P", "Présentoire"}};
		httpUtil.setRequestAttribute("listeDestination", listeDestination);
		
		List listFamille = familleService.getListeFamille("ST", true, false);
		httpUtil.setRequestAttribute("listeFaimlle", listFamille);
		
		String[][] listNature = new String[][]{{"N", "Net"}, {"B", "Brut"}};
		httpUtil.setRequestAttribute("listNature", listNature);
		
		if(!StringUtil.isTrue(httpUtil.getParameter("is_compo_inc"))){
			ArticleBean composantBean = (ArticleBean)httpUtil.getViewBean();
			if(composantBean != null && httpUtil.isCrudOperation()) {
	    		String fournisseurs = StringUtil.getStringDelimFromStringArray(composantBean.getFournisseursArray(), "|");
	    		composantBean.setFournisseurs(fournisseurs);
	    	}
		}
		
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
			httpUtil.setRequestAttribute("listeUniteEnumId", valEnumService.getOneByField(TypeEnumPersistant.class, "code", ModelConstante.ENUM_UNITE).getId());

			
			List<ValTypeEnumBean> listTva = valEnumService.getListValeursByType(ModelConstante.ENUM_TVA);
			httpUtil.setRequestAttribute("listeTva", listTva);
			httpUtil.setRequestAttribute("typeTVAEnumId", valEnumService.getOneByField(TypeEnumPersistant.class, "code", ModelConstante.ENUM_TVA).getId());
			
			List<MarqueBean> listMarque = marqueService.findAll(Order.asc("libelle"));
			httpUtil.setRequestAttribute("listeMarque", listMarque);
			List<FournisseurPersistant> listFournisseur = fournisseurService.getListFournisseur(true, false);
			httpUtil.setRequestAttribute("listeFournisseur", listFournisseur);
			 
			if(SOFT_ENVS.pharma.toString().equals(StrimUtil.getGlobalConfigPropertie("context.soft"))) {//-------------------------------------------------------
				List<ValTypeEnumBean> listDosage = valEnumService.getListValeursByType("UNITE_DOSAGE");
				httpUtil.setRequestAttribute("listeDosage", listDosage);
				
				List<ValTypeEnumBean> listeForme = valEnumService.getListValeursByType("FORME");
				httpUtil.setRequestAttribute("listeForme", listeForme);
				httpUtil.setMenuAttribute("IS_SUB_ADD", true);
				httpUtil.setRequestAttribute("listeFormeEnumId", valEnumService.getOneByField(TypeEnumPersistant.class, "code", "FORME").getId());


				
				String[][] typeArticle = new String[][] { { "P", "PRINCEPS" }, {"G", "GENERIQUE"} };
				httpUtil.setRequestAttribute("typeArticle", typeArticle);
				
				List<ArticlePersistant> listArticle = articleService.getListPharmaPrinceps();
				httpUtil.setRequestAttribute("listArticle", listArticle);
			}//--------------------------------------------------------------------
				
				
			if(!httpUtil.isCreateAction() && !ActionConstante.FIND.equals(httpUtil.getAction())){
				Long articleId = httpUtil.getWorkIdLong();
				if(articleId != null){
					httpUtil.setMenuAttribute("articleId", articleId);
				} else{
					articleId = (Long)httpUtil.getMenuAttribute("articleId");
				}
				// Gérer le retour sur cet onglet
				articleId = (Long)httpUtil.getMenuAttribute("articleId");
				if(articleId != null && !httpUtil.isCrudOperation()){
					loadBean(httpUtil, articleId);
				}
			} else{
				httpUtil.removeMenuAttribute("articleId");
			}
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
						
						List<FamillePersistant> familleEnfant = familleService.getFamilleEnfants("ST", famId, true);
						if(familleEnfant != null){
							for (FamillePersistant famillePersistant : familleEnfant) {
								listFam.add(famillePersistant.getId().toString());
							}
						}
					}
				}
			}
		}
		String codeBarre = httpUtil.getParameter("cb");
		
		//
		if(listFam.size() > 0){
			mapData.put("article.opc_famille_stock.id", listFam.toArray(new String[listFam.size()]));
		}
		//-------------------------------------------------------------
		boolean isRestau = SOFT_ENVS.restau.toString().equals(StrimUtil.getGlobalConfigPropertie("context.soft"));
		String tp = (String) httpUtil.getMenuAttribute("tp");
		String article_stock_find = "from ArticlePersistant article" +
				" where article.code != 'GEN' and "+(isRestau ? "article.opc_famille_stock is not null and":"")+ " (article.is_fiche is null or article.is_fiche=0) "
				+ getFilterStateRequest(httpUtil, "is_disable", "list_article");
		
		String fiche_composant_find = "from ArticlePersistant article" +
				" where article.code != 'GEN' and "+(isRestau ? "article.opc_famille_stock is not null and":"")+" article.is_fiche is not null and article.is_fiche=1"
				+ getFilterStateRequest(httpUtil, "is_disable", "list_article");
	
		if(StringUtil.isNotEmpty(codeBarre)){
			article_stock_find = article_stock_find + " and article.code_barre='"+codeBarre+"'";
			fiche_composant_find = fiche_composant_find + " and article.code_barre='"+codeBarre+"'";
		}
		
		if("on".equals(httpUtil.getParameter("actifs-activator"))){
			article_stock_find = article_stock_find + " and (article.is_disable is null or article.is_disable=0) ";
			fiche_composant_find = fiche_composant_find + " and (article.is_disable is null or article.is_disable=0) ";
		}
		article_stock_find = article_stock_find + " order by article.opc_famille_stock.b_left, article.libelle, article.code";
		fiche_composant_find = fiche_composant_find + " order by article.opc_famille_stock.b_left, article.libelle, article.code";
		
		String reqId = "FC".equals(tp) ? fiche_composant_find : article_stock_find;
		
		// Car apeé depuis dashboard notif pharma
		IFamilleService familleSrv = ServiceUtil.getBusinessBean(IFamilleService.class);
		IArticleService articleSrv = ServiceUtil.getBusinessBean(IArticleService.class);
		
		List<ArticlePersistant> listData = (List<ArticlePersistant>) articleSrv.findByCriteria(cplxTable, reqId);

		for (ArticlePersistant articlePersistant : listData) {
			List<FamillePersistant> familleStr = familleSrv.getFamilleParent("ST", articlePersistant.getOpc_famille_stock().getId());
			articlePersistant.setFamilleStr(familleStr);
		}
		
		httpUtil.setRequestAttribute("list_article", listData);
		
		httpUtil.setDynamicUrl("/domaine/stock/composant_list.jsp");			
	}
	
	@Override
	public void work_merge(ActionUtil httpUtil) {
		ArticleBean articleBean = (ArticleBean) httpUtil.getViewBean();
		articleBean.setId(httpUtil.getWorkIdLong());
		
		boolean isNewArticle = (articleBean.getId() == null);
		
		if(httpUtil.getWorkIdLong() != null){
			ArticleBean articleDB = articleService.findById(httpUtil.getWorkIdLong());
			articleBean.setList_article(articleDB.getList_article());
			articleBean.setList_empl_seuil(articleDB.getList_empl_seuil());
			articleBean.setList_generic(articleDB.getList_generic());
		}
		
		if(ContextAppli.IS_RESTAU_ENV()){
			articleBean.setIs_stock(true);
		}
		
		String tp = (String) httpUtil.getMenuAttribute("tp");
		if("FC".equals(tp)){
			articleBean.setIs_fiche(true);
		}
		
		setDataList(httpUtil);

		// Recalcul TTC
		BigDecimal mttTva = null;
		if(articleBean.getOpc_tva_enum() != null){
			ValTypeEnumPersistant valP = articleService.findById(ValTypeEnumPersistant.class, articleBean.getOpc_tva_enum().getId());
			mttTva = (BigDecimalUtil.divide(BigDecimalUtil.multiply(articleBean.getPrix_achat_ht(), BigDecimalUtil.get(valP.getCode())), BigDecimalUtil.get(100)));
		}
		BigDecimal mttAchatTTc = BigDecimalUtil.add(articleBean.getPrix_achat_ht(), mttTva);
		articleBean.setPrix_achat_ttc(mttAchatTTc);
		
		// Calcul TTC
		if(articleBean.getList_article() != null){
			for(ArticleDetailPersistant artP : articleBean.getList_article()){
				ArticleBean opc_article_composant = articleService.findById(artP.getOpc_article_composant().getId());
				articleBean.setPrix_achat_ttc(
						BigDecimalUtil.add(
								articleBean.getPrix_achat_ttc(), 
								BigDecimalUtil.multiply(opc_article_composant.getPrix_achat_ttc(), artP.getQuantite())
				));
				articleBean.setPrix_achat_ht(
						BigDecimalUtil.add(
								articleBean.getPrix_achat_ht(),
								BigDecimalUtil.multiply(opc_article_composant.getPrix_achat_ht(), artP.getQuantite())
				));		
				articleBean.setPrix_achat_moyen_ht(
						BigDecimalUtil.add(
								articleBean.getPrix_achat_moyen_ht(),
								BigDecimalUtil.multiply(opc_article_composant.getPrix_achat_moyen_ht(), artP.getQuantite())
				));
				articleBean.setPrix_achat_moyen_ttc(
						BigDecimalUtil.add(
								articleBean.getPrix_achat_moyen_ttc(),
								BigDecimalUtil.multiply(opc_article_composant.getPrix_achat_moyen_ttc(), artP.getQuantite())
				));
				articleBean.setMtt_tva(
						BigDecimalUtil.add(
								articleBean.getMtt_tva(),
								BigDecimalUtil.multiply(opc_article_composant.getMtt_tva(), artP.getQuantite())
				));		
			}
		}
		
		if(SOFT_ENVS.pharma.toString().equals(StrimUtil.getGlobalConfigPropertie("context.soft"))) {
			List<ArticleGeneriquePersistant> listGen = (isNewArticle ? new ArrayList<>() : articleService.findById(articleBean.getId()).getList_generic());
			listGen.clear();
			
			if(articleBean.getType_art().equals("G")) {
				String[] generic_ids = httpUtil.getRequest().getParameterValues("generic_ids");
				
				if(generic_ids != null && generic_ids.length > 0) {
					for(String artId : generic_ids) {
						ArticleGeneriquePersistant genP = new ArticleGeneriquePersistant();
						ArticlePersistant opcArt = articleService.findById(ArticlePersistant.class, Long.valueOf(artId));
						genP.setOpc_article(opcArt);
						genP.setCode_barre(opcArt.getCode_barre());
						genP.setOpc_article_gen(articleBean);
						genP.setCode_barre_gen(articleBean.getCode_barre());
						
						listGen.add(genP);
					}
				}
			}
			articleBean.setList_generic(listGen);
		}//------------------------------------------------------------------------------------------------
		
		super.work_merge(httpUtil);
		
		// Ajuster le prix des composants composés
		mouvementService.majPrixFichComposant(articleBean.getId());
		
		boolean isFromCaisse = httpUtil.getParameter("is_cai") != null;
		
		if(isFromCaisse){
			httpUtil.writeResponse("MSG_CUSTOM:Article ajouté");
			return;
		}
		
		managePieceJointe(httpUtil, articleBean.getId(), "composant", 300, 300);
		
		if(httpUtil.getMenuAttribute("mouvementTmpBean") != null){
			httpUtil.setDynamicUrl("stock.mouvement.work_init_create");
		}
	}
	
	/**
	 * @param httpUtil
	 */
	public void dupliquer_fiche_article(ActionUtil httpUtil){
		Long[] listComposantsIds = httpUtil.getCheckedElementsLong("list_article");
		Long familleCuisineId = httpUtil.getLongParameter("famille_cuisine");
		String dest = httpUtil.getParameter("destination");
		BigDecimal prixVente = BigDecimalUtil.get(httpUtil.getParameter("prixV"));
		
		if(familleCuisineId != null) {
			httpUtil.setMenuAttribute("famille_cuisine", familleCuisineId);
			httpUtil.setMenuAttribute("destination", dest);
			httpUtil.setMenuAttribute("prixVente", prixVente);
		}
		familleCuisineId = (Long) httpUtil.getMenuAttribute("famille_cuisine");
		dest = (String) httpUtil.getMenuAttribute("destination");
		prixVente = (BigDecimal) httpUtil.getMenuAttribute("prixVente");
		
		//
		if(listComposantsIds == null || listComposantsIds.length == 0){
			MessageService.addBannerMessage("Vous devez cocher au moins un composant avant de valider.");
			return;
		} else{
			boolean isRepondu = MessageService.addDialogConfirmMessage("dupplic-art", "stock.composant.dupliquer_fiche_article", "Vous aller créer des fiches articles pour chaque composant coché."
					+ "<br>Si l'article existe déjà, ses informations seront <b>regénérées</b>.<br>"
					+ "<br>Souhaitez-vous continuer ?");
			if(isRepondu){
				articleService.dupliquerEnFicheArticle(listComposantsIds, familleCuisineId, dest, prixVente);
			}
			work_find(httpUtil);
		}
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "Mise à jour", "L'article est généré avec succès.");
	}
	
	/**
	 * @param httpUtil
	 */
	@SuppressWarnings({ "unchecked" })
	private void setDataList(ActionUtil httpUtil) {
		ArticleBean articleBean = (ArticleBean) httpUtil.getViewBean();
		articleBean.setId(httpUtil.getWorkIdLong());
		
		List<ArticleDetailPersistant> listArticleDet = (List<ArticleDetailPersistant>) httpUtil.buildListBeanFromMap("eaiid", "opc_article_composant.id",
														ArticleDetailPersistant.class, "eaiid", "opc_article_composant.id", "quantite");

		List<ArticleDetailPersistant> listArticle = new ArrayList<>();
		if (articleBean.getId() != null) {
			ArticleBean artBean = articleService.findById(articleBean.getId());
			listArticle = artBean.getList_article();
			listArticle.clear();
		}
		listArticle.addAll(listArticleDet);
		articleBean.setList_article(listArticle);
	}
	
	@Override
	public void work_edit(ActionUtil httpUtil) {
		Long articleId = httpUtil.getLongParameter("art");
		
		if(articleId != null || "A".equals(httpUtil.getMenuAttribute("tp")) || httpUtil.getParameter("isCmp") != null){
			if(articleId != null){
				httpUtil.setViewBean(articleService.findById(articleId));
			}
			httpUtil.setRequestAttribute("isApercu", true);
			httpUtil.setDynamicUrl("/domaine/stock/composant_edit.jsp");
		} else{
			super.work_edit(httpUtil);
		}
	}
	
	/**
	 * @param httpUtil
	 */
	public void find_mouvement(ActionUtil httpUtil){
		Long articleId = (Long)httpUtil.getMenuAttribute("articleId");
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_mouvement");
		// Ajouter les critères
		Map<String, Object> formCriterion = cplxTable.getFormBean().getFormCriterion();
		formCriterion.put("article_id", articleId);
		//
		List<MouvementArticlePersistant> listMouvement = (List<MouvementArticlePersistant>) articleService.findByCriteriaByQueryId(cplxTable, "mouvement_detail_find");
		httpUtil.setRequestAttribute("list_mouvement", listMouvement);

		httpUtil.setDynamicUrl("/domaine/stock/mouvement_article_list.jsp");		
	}
	
	/**
	 * @param httpUtil
	 */
	public void find_fournisseur(ActionUtil httpUtil){
		Long articleId = (Long)httpUtil.getMenuAttribute("articleId");
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_fournisseur");
		Map<String, Object> formCriterion = cplxTable.getFormBean().getFormCriterion();
		formCriterion.put("articleId", articleId);

		List<MouvementArticlePersistant> listFournisseur = (List<MouvementArticlePersistant>) articleService.findByCriteriaByQueryId(cplxTable, "fournisseur_article_find");
		Map<String, MouvementArticlePersistant> mapPrix = new LinkedHashMap<>();
		//
		for (MouvementArticlePersistant mouvementArticleP : listFournisseur) {
			String fournCode = mouvementArticleP.getOpc_mouvement().getOpc_fournisseur().getCode();
			
			if(mapPrix.get(fournCode+"_"+mouvementArticleP.getPrix_ttc()) == null){
				mapPrix.put(fournCode+"_"+mouvementArticleP.getPrix_ttc(), mouvementArticleP);
			}
		}
		List<MouvementArticlePersistant> listData = new ArrayList<>();
		for(String key : mapPrix.keySet()){
			listData.add(mapPrix.get(key));
		}
		cplxTable.setDataSize(listData.size());
		cplxTable.setDataExport(listData);
		
		httpUtil.setRequestAttribute("list_fournisseur", listData);
		
		httpUtil.setDynamicUrl("/domaine/stock/fournisseur_article_list.jsp");
	}

	/**
	 * @param httpUtil
	 */
	public void editTrArticle(ActionUtil httpUtil){
		Long articleId = httpUtil.getLongParameter("art");
		ArticlePersistant articleP = articleService.findById(ArticlePersistant.class, articleId);
		httpUtil.setRequestAttribute("articleP", articleP);
		
		List<ArticleStockInfoPersistant> listArtView = articleService.getListArticleView(articleId);
		httpUtil.setRequestAttribute("listArtView", listArtView);
		
		httpUtil.setDynamicUrl("/domaine/stock/article_tr_consult.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void init_conf_stock(ActionUtil httpUtil){
		List<EmplacementPersistant> listEmplacement = (List<EmplacementPersistant>) articleService.findAll(EmplacementPersistant.class, Order.asc("titre"));
		httpUtil.setRequestAttribute("listEmplacement", listEmplacement);
		 
		Long composantId = httpUtil.getWorkIdLong();
		ArticleBean compP = articleService.findById(composantId);
		Map<Long, BigDecimal> mapData = new HashMap();
		
		for(EmplacementSeuilPersistant emplSeuilP : compP.getList_empl_seuil()){
			mapData.put(emplSeuilP.getOpc_emplacement().getId(), emplSeuilP.getQte_seuil());
		}
		httpUtil.setRequestAttribute("mapData", mapData);
		httpUtil.setRequestAttribute("composantId", composantId);
		
		httpUtil.setDynamicUrl("/domaine/stock/emplacement_stock_alert_conf.jsp");
	}
	public void save_conf_stock(ActionUtil httpUtil){
		List<EmplacementSeuilPersistant> listEmplSeuil = (List<EmplacementSeuilPersistant>) httpUtil.buildListBeanFromMap("qte_seuil", EmplacementSeuilPersistant.class, "eaiid",
															"opc_emplacement.id", "opc_composant.id", "qte_seuil");
		
		Long composantId = httpUtil.getWorkIdLong();
		ArticleBean compP = articleService.findById(composantId);

		//
		for (Iterator<EmplacementSeuilPersistant> iterator = compP.getList_empl_seuil().iterator(); iterator.hasNext();) {
			EmplacementSeuilPersistant emplSeuilP = iterator.next();

			boolean isExistInIhm = false;
			for(EmplacementSeuilPersistant emplSeuilIHM : listEmplSeuil){
				if(emplSeuilIHM.getOpc_composant().getId().equals(emplSeuilP.getOpc_composant().getId()) 
						&& emplSeuilIHM.getOpc_emplacement().getId().equals(emplSeuilP.getOpc_emplacement().getId())){
					emplSeuilP.setQte_seuil(emplSeuilIHM.getQte_seuil());
					
					isExistInIhm = true;
					break;
				}
			}
			if(!isExistInIhm){
				iterator.remove();
			}
		}
		// AJouter les nouveau
		for(EmplacementSeuilPersistant emplSeuilIHM : listEmplSeuil){
			boolean isExistInDB = false;
			for(EmplacementSeuilPersistant semplSeuilDb : compP.getList_empl_seuil()){
				if(semplSeuilDb.getOpc_composant().getId().equals(emplSeuilIHM.getOpc_composant().getId()) 
						&& semplSeuilDb.getOpc_emplacement().getId().equals(emplSeuilIHM.getOpc_emplacement().getId())){
					isExistInDB = true;
					break;
				}
			}
			if(!isExistInDB){
				compP.getList_empl_seuil().add(emplSeuilIHM);
			}
		}
		//
		articleService.update(compP);
		
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "", "Les seuils d'alert ont été mise à jour.");
		
		work_find(httpUtil);
	}
	
	public void getArticlesByCodeBarre(ActionUtil httpUtil) {
		String value = httpUtil.getParameter("cb");
		ArticlePersistant articleP = articleService.getArticleByCodeBarre(value, false);

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
	public void init_dupliquer_fiche_article(ActionUtil httpUtil){
		String checked = httpUtil.getParameter("list_article_" + ProjectConstante.CHECK_SAVE_STR);
		httpUtil.setRequestAttribute("listChecked", checked);
		
		String[][] listeDestination = new String[][]{{"C", "Cuisine"}, {"P", "Présentoire"}};
		httpUtil.setRequestAttribute("listeDestination", listeDestination);
		
		List listFamille = familleService.getListeFamille("CU", true, false);
		httpUtil.setRequestAttribute("listeFamilleCuisine", listFamille);

		String[][] listNature = new String[][]{{"N", "Net"}, {"B", "Brut"}};
		httpUtil.setRequestAttribute("listNature", listNature);
		
		httpUtil.setDynamicUrl("/domaine/stock/composant_article_duplic.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void generer_code(ActionUtil httpUtil) {
		String tp = (String)httpUtil.getMenuAttribute("tp");
		boolean isComposant = "FC".equals(tp);
		
		Long famId = httpUtil.getLongParameter("article.opc_famille_stock.id");
		if(famId == null) {
			httpUtil.writeResponse("");
			return;
		}
		String code = articleService.generateCode(famId, "COMP");
		httpUtil.writeResponse(code+(isComposant?"_FC":"_C"));
	}
	
	/**
	 * @param httpUtil
	 */
	public void desactiver(ActionUtil httpUtil) {
		articleService.activerDesactiverElement(httpUtil.getWorkIdLong());
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "", "L'article est désactivé.");
		work_find(httpUtil);
	}
	
	public void generer_codeBarre(ActionUtil httpUtil){
		Long famId = httpUtil.getLongParameter("article.opc_famille_stock.id");
		if(famId == null) {
			httpUtil.writeResponse("");
			return;
		}
		String code = articleService.generateCodeBarre(famId);
		httpUtil.writeResponse(code);
	}
	
	public void generer_balanceKey(ActionUtil httpUtil){
		Long famId = httpUtil.getLongParameter("article.opc_famille_stock.id");
		if(famId == null) {
			httpUtil.writeResponse("");
			return;
		}
		String code = articleService.generateCleBalance(famId);
		httpUtil.writeResponse(code);
	}
	
	public void loadArticleFastSaisie(ActionUtil httpUtil){
		httpUtil.setDynamicUrl("/domaine/stock/composant_fast_update.jsp");
		
		Long famId = httpUtil.getLongParameter("famille_art");
		if(famId == null){
			return;
		}
		httpUtil.setRequestAttribute("list_article", articleService.getListComposantsActifs(famId));
		
		FamilleStockPersistant opcFamille = familleService.findById(FamilleStockPersistant.class, famId);
		if(opcFamille != null){
			boolean idFamilleBalance = false;
			List<CaissePersistant> listCaisse = articleService.getListCaisseActive(ContextAppli.TYPE_CAISSE_ENUM.BALANCE.toString());
			for (CaissePersistant caisseP : listCaisse) {
				if(StringUtil.isEmpty(caisseP.getFamille_balance())){
					continue;
				}
				if(caisseP.getFamille_balance().indexOf("|"+famId+"|") != -1){
					idFamilleBalance = true;
					break;
				}
			}
			if(idFamilleBalance){
				httpUtil.setRequestAttribute("isBalance", idFamilleBalance);
			}
		}
		
		if(httpUtil.getParameter("isRef") != null){
			httpUtil.setRequestAttribute("fam", famId);
			return;
		}
		
		if(httpUtil.getParameter("isSub") != null){
			
			if(opcFamille == null){
				MessageService.addGrowlMessage("", "La famille est obligatoire.");
				return;
			}
			
			List<ArticlePersistant> listArticle = (List<ArticlePersistant>) httpUtil.buildListBeanFromMap("libelle", ArticlePersistant.class, "eaiid", "libelle", "code_barre", "prix_achat_ttc", "prix_vente");
			for (ArticlePersistant articleP : listArticle) {
				articleP.setOpc_famille_stock(opcFamille);
			}
			articleService.mergeFastUpdate(listArticle);
		}
		
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "", "Les article ont été mises à jour.");
		work_find(httpUtil);
	}
	
	public void sync_balance(ActionUtil httpUtil){
		List<CaissePersistant> listBalance = articleService.getListCaisseActive(ContextAppli.TYPE_CAISSE_ENUM.BALANCE.toString());
		httpUtil.setRequestAttribute("listBalance", listBalance);
		
		httpUtil.setDynamicUrl("/domaine/stock/composant_bal_sync.jsp");
		
		if(httpUtil.getParameter("isSub") == null){
			return;
		}
		
		CaissePersistant balanceP = articleService.findById(CaissePersistant.class, httpUtil.getLongParameter("cai"));
		articleService.synchroniserArticleBalance(balanceP);
		
		httpUtil.writeResponse("MSG_CUSTOM:Synchronisation effectuéé");
	}
	
	public void telechargerModel(ActionUtil httpUtil){
		String path = this.getClass().getResource("").getPath().substring(1);
		httpUtil.doDownload(new File(path+"importArticleModel.xls"), false);
	}
	public void familleComposants(ActionUtil httpUtil){
		if(httpUtil.getParameter("isSub") == null){
			httpUtil.setMenuAttribute("updateFamilleIds", httpUtil.getCheckedElementsLong("list_article"));
			
			httpUtil.setDynamicUrl("/domaine/stock/article_famille_change.jsp");
			return;
		}
		Long[] artIds = (Long[]) httpUtil.getMenuAttribute("updateFamilleIds");
		if(artIds == null || artIds.length == 0){
			MessageService.addBannerMessage("Veuillez sélectionner au moins un article.");
			return;
		} else if(httpUtil.getLongParameter("famille_set") == null){
			MessageService.addBannerMessage("Veuillez sélectionner une famille.");
			return;
		}
		
		articleService.changerFamille(httpUtil.getLongParameter("famille_set"), artIds);
		
		work_find(httpUtil);
	}
	public void exporterComposants(ActionUtil httpUtil){
		if(httpUtil.getParameter("isSub") == null){
			httpUtil.setDynamicUrl("/domaine/stock/article_export_edit.jsp");
			return;
		}
		String[] familles = httpUtil.getRequest().getParameterValues("familles_array");
		if(familles == null || familles.length == 0){
			MessageService.addBannerMessage("Veuillez sélectionner une famille.");
			return;
		}
		
		httpUtil.doDownload(articleService.exporteComposants(familles), true);
	}
	
	public void controle_marge(ActionUtil httpUtil){
		Map<String, BigDecimal[]> mapArticle = articleService.controleMarge();
		httpUtil.setRequestAttribute("mapArticle", mapArticle);
		
		httpUtil.setDynamicUrl("/domaine/stock/controle_marge.jsp");
	}
	
	public void importerComposants(ActionUtil httpUtil){
		if(httpUtil.getParameter("isSub") == null){
			List<EmplacementPersistant> listEmplacement = (List<EmplacementPersistant>) articleService.findAll(EmplacementPersistant.class, Order.asc("titre"));
			httpUtil.setRequestAttribute("listEmplacement", listEmplacement);
			
			httpUtil.setDynamicUrl("/domaine/stock/article_import_edit.jsp");
			return;
		}
		Map<String, byte[]> images = new HashMap<>();
		Map<String, Object> mapPhotos = httpUtil.getValuesByStartName("photo");
		if(mapPhotos != null) {
			for (String key : mapPhotos.keySet()) {
				if(key.indexOf("_") == -1) {
					continue;
				}
				String idx = key.substring(0, key.indexOf("_"));
				
				byte[] imageByte = null;
				String imageName = httpUtil.getParameter("photo"+idx+"_name");
				if(StringUtil.isNotEmpty(imageName)) {
					imageByte = UploadFileUtil.getFileBytes(httpUtil.getRequest(), EncryptionUtil.encrypt("photo"+idx));
					images.put(imageName, imageByte);
				}
			}
		}
		
		if(images == null || images.size() == 0){
			MessageService.addBannerMessage("Vous devez joindre le fichier Excel rempli avant de lancer l'importation");
			return;
		}
		
		GenericJpaService.mergeDataFile(ContextAppli.getEtablissementBean().getId(), "article_import", images, null, null, null);
		
		try{
			boolean isInventaire = httpUtil.getParameter("genereINV") != null;
			boolean isDisComposant = httpUtil.getParameter("disArt") != null;
			boolean  genBarre = httpUtil.getParameter("genereCB") != null;
			Date dateInv = DateUtil.stringToDate(httpUtil.getRequest().getParameter("date_inv"));
			Long emplacementInv = httpUtil.getLongParameter("emplacement_inv");
			
			if(!isInventaire || dateInv == null || emplacementInv == null){
				dateInv = null;
				emplacementInv = null;
			} else{
				if(DateUtil.getDiffYear(dateInv, new Date()) > 2){// Pas plus de 2 ans
					MessageService.addBannerMessage("La date de l'inventaire ne doit pas être antérieure à la date d'aujourd'hui de plus de 2 ans.");
					return;
				}
				Date maxDate = inventaireService.getMaxDateInventaireValide(emplacementInv);
				
				if(maxDate != null && dateInv.before(maxDate)){
					MessageService.addBannerMessage("La date de réalisation doit être postérieure ou égale à la date du dernier inventaire validé ("+DateUtil.dateToString(maxDate)+").");
				}
				
				// Controler inventaire non validé
				if(inventaireService.getInventaireNonValide(emplacementInv).size()>0){
					MessageService.addBannerMessage("Un inventaire non validé est lié à cet emplacement. Veuillez le valider avant de continuer.");
					return;
				}
			}
			//
			InventairePersistant inventaireP = articleService.importerComposants(images.keySet().iterator().next(), 
					genBarre, isDisComposant,
					dateInv, emplacementInv);
			
			if(inventaireP != null){
				MouvementPersistant mvmInventaire = inventaireService.validerInventaire(inventaireP.getId());
				
				Long destId = mvmInventaire.getOpc_destination()!=null?mvmInventaire.getOpc_destination().getId() : null;
				Long emplId = mvmInventaire.getOpc_emplacement()!=null?mvmInventaire.getOpc_emplacement().getId():null;
				
				// --------------------------------------------------------------------------------------------------------------
				 List<MouvementArticlePersistant> listArticle = mvmInventaire.getList_article();
			   	 ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor)ServiceUtil.getBusinessBean("taskExecutor");
			        taskExecutor.submit(new Callable() {
		           public Object call() throws Exception {
		               //  Création mouvement
		        	   try{
		        		   mouvementService.majQteStockArticle(emplId, destId, listArticle);
		        	   } catch(Exception e){
							e.printStackTrace();
						}
		               return null;
		           }
		       });
			}
		} catch(ActionValidationException e){ 
		
		} catch(Exception e){
			e.printStackTrace();
			MessageService.addDialogMessage("Une erreur s'est produit lors de l'import. Veuillez vérifier votre fichier.");
		}
		//
		if(!MessageService.isError()){
			MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "Importation des données", "Les données ont été importées avec succès");
			// Purger le répertoir en premier
			String fullPath = ContextAppli.getEtablissementBean().getId()+"/article_import";
			framework.model.util.FileUtil.clearDir(fullPath); 
		}
		
		work_find(httpUtil);
	}

	public void fusionner_article(ActionUtil httpUtil){
		if(httpUtil.getParameter("isSub") == null) {
			httpUtil.setRequestAttribute("listArticle", articleService.getListArticleStock(false));
			httpUtil.setDynamicUrl("/domaine/stock/article_fusion.jsp");
			return;
		}
		Long source = httpUtil.getLongParameter("article_src");
		Long dest = httpUtil.getLongParameter("article_dest");
		//
		articleService.fusionnerArticle(source, dest);
		work_find(httpUtil);
	}
	
	public void work_post(ActionUtil httpUtil){
		if(!StringUtil.isTrue(httpUtil.getParameter("is_compo_inc"))){
			ArticleBean composantBean = (ArticleBean)httpUtil.getViewBean();
			if(composantBean != null && StringUtil.isNotEmpty(composantBean.getFournisseurs())) {
	        	String[] imprArray = StringUtil.getArrayFromStringDelim(composantBean.getFournisseurs(), "|");
	        	composantBean.setFournisseursArray(imprArray);
	        	httpUtil.setRequestAttribute("fournArray", imprArray);
	    	}
		}
		manageDataForm(httpUtil, "COMPOSANT");
		
		// --------------------------- DYN form --------------------------
		httpUtil.setRequestAttribute("listDataValueForm", articleService.loadDataForm(null, "COMPOSANT"));
		// ---------------------------------------------------------------
	}
}
