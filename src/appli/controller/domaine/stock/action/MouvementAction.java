package appli.controller.domaine.stock.action;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.hibernate.criterion.Order;

import appli.controller.domaine.administration.bean.ValTypeEnumBean;
import appli.controller.domaine.stock.bean.ArticleBean;
import appli.controller.domaine.stock.bean.DemandeTransfertBean;
import appli.controller.domaine.stock.bean.EmplacementBean;
import appli.controller.domaine.stock.bean.FournisseurChequeBean;
import appli.controller.domaine.stock.bean.MouvementBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_MOUVEMENT_ENUM;
import appli.model.domaine.administration.persistant.TypeEnumPersistant;
import appli.model.domaine.administration.persistant.ValTypeEnumPersistant;
import appli.model.domaine.administration.service.ICompteBancaireService;
import appli.model.domaine.administration.service.IEtatFinanceService;
import appli.model.domaine.administration.service.IValTypeEnumService;
import appli.model.domaine.compta.persistant.PaiementPersistant;
import appli.model.domaine.personnel.persistant.ClientPersistant;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.persistant.ArticleStockInfoPersistant;
import appli.model.domaine.stock.persistant.ChargeDiversPersistant;
import appli.model.domaine.stock.persistant.EmplacementPersistant;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.stock.persistant.FournisseurPersistant;
import appli.model.domaine.stock.persistant.MouvementArticlePersistant;
import appli.model.domaine.stock.persistant.MouvementPersistant;
import appli.model.domaine.stock.persistant.PreparationArticlePersistant;
import appli.model.domaine.stock.persistant.PreparationPersistant;
import appli.model.domaine.stock.persistant.TravauxPersistant;
import appli.model.domaine.stock.service.IArticleService;
import appli.model.domaine.stock.service.ICentraleSynchroService;
import appli.model.domaine.stock.service.IEmplacementService;
import appli.model.domaine.stock.service.IFamilleService;
import appli.model.domaine.stock.service.IFournisseurChequeService;
import appli.model.domaine.stock.service.IFournisseurService;
import appli.model.domaine.stock.service.IInventaireService;
import appli.model.domaine.stock.service.ILotArticleService;
import appli.model.domaine.stock.service.IMouvementService;
import appli.model.domaine.stock.service.IPreparationService;
import appli.model.domaine.stock.service.impl.FicheCommandePDF;
import appli.model.domaine.stock.service.pdf.TransfertPdf;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.ContextGloabalAppli;
import framework.controller.ControllerUtil;
import framework.controller.FrontFilter;
import framework.controller.annotation.WorkController;
import framework.controller.annotation.WorkForward;
import framework.controller.bean.action.IViewBean;
import framework.model.common.constante.ActionConstante;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.ControllerBeanUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.DateUtil.TIME_ENUM;
import framework.model.common.util.NumericUtil;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.ModelConstante;
import framework.model.util.PdfBean;

@WorkController(nameSpace = "stock", bean = MouvementBean.class, jspRootPath = "/domaine/stock/")
public class MouvementAction extends ActionBase {
	@Inject
	private IValTypeEnumService valEnumService;
	@Inject
	private IEmplacementService emplacementService;
	@Inject
	private IFournisseurService fournisseurService;
	@Inject
	private IFournisseurChequeService fournisseurChequeService;
	@Inject
	private IMouvementService mouvementService;
	@Inject
	private IArticleService articleService;
	@Inject
	private IPreparationService preparationService;
	@Inject
	private ILotArticleService lotArticleService;
	@Inject
	private IFamilleService familleService;
	@Inject 
	private ICompteBancaireService compteBancaireService;
	@Inject 
	private IEtatFinanceService etatFinancierService;
	@Inject 
	private IInventaireService inventaireService;
	@Inject 
	private ICentraleSynchroService centraleService;
//	@Inject
//	private ILotArticleService lotArtService;
	
	/**
	 * @param httpUtil
	 */
	public void work_init(ActionUtil httpUtil) {
		
		httpUtil.setMenuAttribute("IS_SUB_ADD","stock.mouvement.work_init_create");
		// Type mouvement 
		String type = httpUtil.getParameter("tp");
		if (type != null) {
			httpUtil.setMenuAttribute("type_mvmnt", httpUtil.getParameter("tp"));
		} else {
			type = (String) httpUtil.getMenuAttribute("type_mvmnt");
		}
		httpUtil.setRequestAttribute("type_mouvment", type);
				
		// Cas retour création composant
		if(httpUtil.getMenuAttribute("mouvementTmpBean") != null){
			httpUtil.setViewBean((IViewBean) httpUtil.getMenuAttribute("mouvementTmpBean"));
			
			Map<String, Object> params = (Map)httpUtil.getRequestAttribute(ProjectConstante.WORK_PARAMS);
			params.put(ProjectConstante.WORK_FORM_ACTION, "stock.mouvement.work_init_create");
			httpUtil.removeMenuAttribute("mouvementTmpBean");
		}
		
		if(httpUtil.isEditionPage()){
			 List listFamilleConsommation = familleService.getListeFamille("CO", true, false);
			 httpUtil.setRequestAttribute("familleConsommation", listFamilleConsommation);
			 
			 List<ValTypeEnumBean> listeFinancement = valEnumService.getListValeursByType(ModelConstante.ENUM_FINANCEMENT);
			 httpUtil.setRequestAttribute("listeFinancement", listeFinancement);
			 
			 List<ValTypeEnumBean> listVal = valEnumService.getListValeursByType(ModelConstante.ENUM_TVA);
			 httpUtil.setRequestAttribute("valTVA", listVal);

			 String[][] typeTransfertArray = new String[][] { { "A", "Articles" }, { "B", "Bon de livraison" }, 
					{ "F", "Facture" }, { "P", "Préparation" }, { "R", "Reçu" } };

			 if(ContextAppli.TYPE_MOUVEMENT_ENUM.av.toString().equals(httpUtil.getMenuAttribute("type_mvmnt"))) {
				 typeTransfertArray = new String[][] { { "A", "Articles" }, { "B", "Bon de livraison" }, 
						{ "F", "Facture" }, { "R", "Reçu" } };
			 }
			
			typeTransfertArray = new String[][] { { "A", "Articles" }, { "B", "Bon de livraison" }, 
				{ "F", "Facture" }, { "R", "Reçu" } };
				
			httpUtil.setRequestAttribute("listType", typeTransfertArray);
			httpUtil.setRequestAttribute("listeBanque", compteBancaireService.findAll());
		} else {
			httpUtil.setRequestAttribute("immoData", new String[][] { { "I", "Immobilisation" }, { "NI", "Sans immobilisation" }});
			httpUtil.setRequestAttribute("travauxData", new String[][] { { "T", "Pour travaux" }, { "NT", "Non travaux" }});
		}
		
		if(ActionConstante.INIT_CREATE.equals(httpUtil.getAction())) {
			httpUtil.removeMenuAttribute("PAIEMENT_DATA");
		}
		
		 List<EmplacementPersistant> listEmplacement = emplacementService.getEmplacementsInternes();
		 // Centrale
		 if(ContextAppli.getAbonementBean().isOptPlusEtsCentrale()
				 && (ContextAppli.TYPE_MOUVEMENT_ENUM.t.toString().equals(type))) {
			 listEmplacement.addAll(emplacementService.getEmplacementsExternes());
		 }
		 httpUtil.setRequestAttribute("listeDestination", listEmplacement);
		 
		 List<EmplacementPersistant> listOrigine = emplacementService.getEmplacementsInternes();
		 httpUtil.setRequestAttribute("listeOrigine", listOrigine);
		 
		 List<FournisseurPersistant> listFournisseur = fournisseurService.getListFournisseur(true, false);
		 httpUtil.setRequestAttribute("listeFournisseur", listFournisseur);
		 
		 List<ClientPersistant> listClient = fournisseurService.findAll(ClientPersistant.class, Order.asc("nom"));
		 httpUtil.setRequestAttribute("listClient", listClient);
		 
		 List<ValTypeEnumBean> typePerte = valEnumService.getListValeursByType(ModelConstante.ENUM_TYPE_PERTE);
		 httpUtil.setRequestAttribute("typePerte", typePerte); 
		 httpUtil.setRequestAttribute("typePerteEnumId", valEnumService.getOneByField(TypeEnumPersistant.class, "code", ModelConstante.ENUM_TYPE_PERTE).getId());
	
		Long mvmId = httpUtil.getWorkIdLong();
		if(mvmId != null){
			httpUtil.setMenuAttribute("mvmId", mvmId);
		} else{
			mvmId = (Long)httpUtil.getMenuAttribute("mvmId");
		}

		String action = httpUtil.getAction();
		MouvementBean mouvementBean = (MouvementBean) httpUtil.getViewBean();
		
		if(action.equals(ActionConstante.MERGE) || action.equals(ActionConstante.CREATE) || action.equals(ActionConstante.UPDATE)) {
			String type_transfert = mouvementBean.getType_transfert();
    		if(type_transfert != null && !type_transfert.equals("A")){
    			String mvm_ids = "";
    			for (Long articleId :  mouvementBean.getMouvementIds()) {
					mvm_ids = mvm_ids + articleId + ";";
    			}
        		mouvementBean.setTransfert_mvm_ids(mvm_ids);
    		}
    		
    		if(httpUtil.getMenuAttribute("IS_TRV_MNU") != null) {
    			mouvementBean.setOpc_travaux((TravauxPersistant) httpUtil.getMenuAttribute("IS_TRV_MNU"));
    		}
		} 
		
		// Construction des données dépendantes
		if (httpUtil.getParameter("reload") != null) {
			Map params = (Map)httpUtil.getRequestAttribute(ProjectConstante.WORK_PARAMS);
			mouvementBean = (MouvementBean) ControllerBeanUtil.mapToBean(MouvementBean.class, params); 
			//
			loadDataList(httpUtil, mouvementBean);
			httpUtil.setViewBean(mouvementBean);
		}
	}
	
	/**
	 * @param httpUtil
	 * @param mouvementBean
	 */
	private void loadDataList(ActionUtil httpUtil, MouvementBean mouvementBean) {
		if(mouvementBean == null) {
			return;
		}
		List<?> listData = null;
		String libelle = "";
		if(mouvementBean.getType_mvmnt().equals(ContextAppli.TYPE_MOUVEMENT_ENUM.t.toString()) 
				|| mouvementBean.getType_mvmnt().equals(ContextAppli.TYPE_MOUVEMENT_ENUM.av.toString())) {
			
			String typeTransfert = mouvementBean.getType_transfert();
			if (typeTransfert.equals("P")) {
				libelle = "Préparation";
				listData = preparationService.findAll(Order.asc("code"), Order.asc("libelle"));
			} else {
				if (typeTransfert.equals("B")) {
					libelle = "Bon de livraison";
				} else if (typeTransfert.equals("F")) {
					libelle = "Facture";
				} else if (typeTransfert.equals("R")) {
					libelle = "Reçu";
				}
				listData = mouvementService.getMouvementByTypeRef(typeTransfert);
			}
			
			httpUtil.setRequestAttribute("listData", listData);
			httpUtil.setRequestAttribute("libelleSource", libelle); 
		}
	}

	/**
	 * Recharger les articles quand on est dans le transfert et qu'on change le BL, facture, ... [Partiel ajax]
	 * @param httpUtil
	 */
	public void refreshTranfertArticles(ActionUtil httpUtil) {
		Map params = (Map)httpUtil.getRequestAttribute(ProjectConstante.WORK_PARAMS);
		MouvementBean mouvementBean = (MouvementBean) ControllerBeanUtil.mapToBean(MouvementBean.class, params); 
		String[] mvmIdsTransfert = httpUtil.getRequest().getParameterValues("mouvement.mouvementIds");
		if(mvmIdsTransfert == null) {
			httpUtil.setDynamicUrl("/domaine/stock/mouvement_detail_edit.jsp");
			return;
		}
		List<Long> mvmIdsTransfertLong = new ArrayList<>(); 
		for (int i=0; i<mvmIdsTransfert.length; i++) {
			if(StringUtil.isEmpty(mvmIdsTransfert[i])){
				continue;
			}
			mvmIdsTransfertLong.add(Long.valueOf(mvmIdsTransfert[i]));
		}
		Long[] mvmIdsTransfertArray = mvmIdsTransfertLong.toArray(new Long[mvmIdsTransfertLong.size()]);
		
		mouvementBean.setId((Long)httpUtil.getMenuAttribute("mvmId")); 
		mouvementBean.setMouvementIds(mvmIdsTransfertArray);
		
		List<MouvementArticlePersistant> listMvmArticle = loadTranfertArticles(mouvementBean);
		listMvmArticle = grouperArticles(listMvmArticle);// Grouper les quantités et les montans
		
		mouvementBean.setList_article(listMvmArticle);
		httpUtil.setViewBean(mouvementBean);
		
		httpUtil.setRequestAttribute("listArticle", articleService.getListArticleStock(true));
		httpUtil.setRequestAttribute("isM", true);
		
		httpUtil.setDynamicUrl("/domaine/stock/mouvement_detail_edit.jsp");
	}
	
	@Override
	public void work_find(ActionUtil httpUtil) {
		
		 // Retour aux travaux si on vient de ce module
		 if(httpUtil.getMenuAttribute("IS_TRV_MNU") != null){
			 httpUtil.setRequestAttribute("bck",true);
		   	 httpUtil.setDynamicUrl("stock.travaux.work_init_update");
		   	 return;
		 }
		
		httpUtil.setMenuAttribute("IS_GRP_VIEW", false);
		httpUtil.setRequestAttribute("listEmplacement", mouvementService.findAll(EmplacementPersistant.class, Order.asc("titre")));
		
		//boolean isExport = StringUtil.isTrue(ControllerUtil.getParam(httpUtil.getRequest(), ActionConstante.EXPORT));
		
		String type = (String) httpUtil.getMenuAttribute("type_mvmnt");
		Date dateRef = mouvementService.getMaxDate(type);
		// Ajouter le paramétre dans la requête
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_mouvement");
		Map<String, Object> formCriterion = cplxTable.getFormBean().getFormCriterion();
		formCriterion.put("type", type);
		boolean isFilterAct = StringUtil.isTrue(httpUtil.getRequest().getParameter("is_filter_act"));
		Date dateDebut = null;
		Date dateFin = null;
		String immoFilter = StringUtil.isNotEmpty(httpUtil.getRequest().getParameter("immoFilter")) ? 
				httpUtil.getRequest().getParameter("immoFilter") : null;
		String stockFilter = StringUtil.isNotEmpty(httpUtil.getRequest().getParameter("stockFilter")) ? 
				httpUtil.getRequest().getParameter("stockFilter") : null;
		String travauxFilter = StringUtil.isNotEmpty(httpUtil.getRequest().getParameter("travauxFilter")) ? 
				httpUtil.getRequest().getParameter("travauxFilter") : null;
		//----------------------------- Date -------------------------
		dateDebut = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateDebut"));
		dateFin = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateFin"));
		//
		if(httpUtil.getRequest().getParameter("is_fltr") == null) {
			dateDebut = DateUtil.stringToDate(httpUtil.getParameter("dateDebut"));
			dateFin = DateUtil.stringToDate(httpUtil.getParameter("dateFin"));
		}
		
		if(dateDebut == null) {
			dateDebut = (httpUtil.getMenuAttribute("dateDebut")==null ? dateRef : (Date)httpUtil.getMenuAttribute("dateDebut"));
			dateFin = (httpUtil.getMenuAttribute("dateFin")==null ? dateRef : (Date)httpUtil.getMenuAttribute("dateFin"));
			immoFilter = (httpUtil.getMenuAttribute("immoFilter")==null ? immoFilter : (String)httpUtil.getMenuAttribute("immoFilter"));
			stockFilter = (httpUtil.getMenuAttribute("stockFilter")==null ? stockFilter : (String)httpUtil.getMenuAttribute("stockFilter"));
			travauxFilter = (httpUtil.getMenuAttribute("travauxFilter")==null ? travauxFilter : (String)httpUtil.getMenuAttribute("travauxFilter"));
			httpUtil.getDate("dateDebut").setValue(dateDebut);
			httpUtil.getDate("dateFin").setValue(dateDebut);
		}
		
		if(httpUtil.getRequest().getParameter("prev") != null) {
			dateDebut = DateUtil.addSubstractDate(dateDebut, TIME_ENUM.DAY, -1);
			dateFin = DateUtil.addSubstractDate(dateFin, TIME_ENUM.DAY, -1);
		} else if(httpUtil.getRequest().getParameter("next") != null) {
			dateDebut = DateUtil.addSubstractDate(dateDebut, TIME_ENUM.DAY, 1);
			dateFin = DateUtil.addSubstractDate(dateFin, TIME_ENUM.DAY, 1);
		}
		
		dateDebut = DateUtil.getStartOfDay(dateDebut);
		dateFin = DateUtil.getEndOfDay(dateFin);
		
		httpUtil.setRequestAttribute("dateDebut", dateDebut);
		httpUtil.setRequestAttribute("dateFin", dateFin);
		httpUtil.setMenuAttribute("dateDebut", dateDebut);
		httpUtil.setMenuAttribute("dateFin", dateFin);
		httpUtil.setMenuAttribute("immoFilter", immoFilter);
		httpUtil.setMenuAttribute("stockFilter", stockFilter);
		httpUtil.setMenuAttribute("travauxFilter", travauxFilter);

		//
		if(!isFilterAct){
			formCriterion.put("dateDebut", dateDebut);
			formCriterion.put("dateFin", dateFin);
		} else{
			formCriterion.remove("dateDebut");
			formCriterion.remove("dateFin");
		}
		
		String reqMvm = "from MouvementPersistant mouvement where type_mvmnt='[type]'"
				+ " and (mouvement.is_groupant is null or mouvement.is_groupant=0) "
				+ "and mouvement.date_mouvement>='[dateDebut]' "
				+ "and mouvement.date_mouvement<='[dateFin]' "
				+ "and mouvement.opc_travaux.id='[travauxId]' ";
		if(immoFilter != null) {
			if(immoFilter.equals("I")) {// Avec immobilisation
				reqMvm += "and mouvement.nbr_annee_amo>=0 ";
			} else if(immoFilter.equals("NI")) {// Sans immo
				reqMvm += "and (mouvement.nbr_annee_amo is null or mouvement.nbr_annee_amo=0) ";
			}
		}
		if(travauxFilter != null) {
			if(travauxFilter.equals("T")) {// Avec travaux
				reqMvm += "and mouvement.opc_travaux != null ";
			} else if(travauxFilter.equals("NT")) {// Sans travaux
				reqMvm += "and mouvement.opc_travaux is null ";
			}
		}
		if(stockFilter != null) {
			reqMvm += " and (mouvement.opc_emplacement="+stockFilter+" or mouvement.opc_destination="+stockFilter+") ";
		}
		
		reqMvm += " order by mouvement.date_mouvement desc";
				
		List<MouvementPersistant> listData = (List<MouvementPersistant>) mouvementService.findByCriteria(cplxTable, reqMvm);
		mouvementService.refreshEntities(listData);
		//
		Map<Long, String> mapDateGroupement = new HashMap<>();
		for(MouvementPersistant mouvementPersistant : listData) {
			if(mouvementPersistant.getMouvement_group_id() != null){
	   			MouvementBean mvmGroupant = mouvementService.findById(mouvementPersistant.getMouvement_group_id());
				mapDateGroupement.put(mvmGroupant.getId(), DateUtil.dateToString(mvmGroupant.getDate_mouvement()));
	   		}
		}
		// Calcul du total montants sans pagination
	   	BigDecimal totalHtAll = null, totalTtcAll = null, totalRemiseAll = null, totalTva = null;
	   	
	   	List<MouvementPersistant> listDataAll = (List<MouvementPersistant>) mouvementService.findByCriteria(cplxTable, reqMvm, false);
		//
	   	
	   	if(listDataAll.size() > 0) {
		   	for (MouvementPersistant mvmStockViewP : listDataAll) {
	   			totalHtAll = BigDecimalUtil.add(totalHtAll, mvmStockViewP.getMontant_ht());
		   		totalTtcAll = BigDecimalUtil.add(totalTtcAll, mvmStockViewP.getMontant_ttc());
		   		totalRemiseAll = BigDecimalUtil.add(totalRemiseAll, mvmStockViewP.getMontant_ttc_rem());
//		   		totalApresRemiseAll = BigDecimalUtil.add(totalApresRemiseAll, BigDecimalUtil.substract(mvmStockViewP.getMontant_ttc(), mvmStockViewP.getMontant_ttc_rem()));
		   		totalTva = BigDecimalUtil.add(totalTva, mvmStockViewP.getMontant_tva());
		   	}
		}
	   	httpUtil.setRequestAttribute("totalHt", totalHtAll);
	   	httpUtil.setRequestAttribute("totalTtc", totalTtcAll);
	   	httpUtil.setRequestAttribute("totalRemise", totalRemiseAll);
//	   	httpUtil.setRequestAttribute("totalApresRemise", totalApresRemiseAll);
	   	httpUtil.setRequestAttribute("totalTva", totalTva);
	   	
		httpUtil.setRequestAttribute("list_mouvement", listData);
		httpUtil.setRequestAttribute("mapDateGroupement", mapDateGroupement);
	   	//
		httpUtil.setDynamicUrl("/domaine/stock/mouvement_list.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void find_mvm_groupe(ActionUtil httpUtil){
		httpUtil.setMenuAttribute("IS_GRP_VIEW", true);
		httpUtil.setMenuAttribute("tpR", "MVM");
		
		httpUtil.setRequestAttribute("listEmplacement", mouvementService.findAll(EmplacementPersistant.class, Order.asc("titre")));
		
		String type = (String) httpUtil.getMenuAttribute("type_mvmnt");
		// Ajouter le paramétre dans la requête
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_mouvement");
		Map<String, Object> formCriterion = cplxTable.getFormBean().getFormCriterion();
		formCriterion.put("type", type);
		boolean isFilterAct = StringUtil.isTrue(httpUtil.getRequest().getParameter("is_filter_act"));
		Date dateDebut = null;
		Date dateFin = null;
		//----------------------------- Date -------------------------
		dateDebut = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateDebut"));
		dateFin = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateFin"));
		//
		if(httpUtil.getRequest().getParameter("is_fltr") == null) {
			dateDebut = DateUtil.stringToDate(httpUtil.getParameter("dateDebut"));
			dateFin = DateUtil.stringToDate(httpUtil.getParameter("dateFin"));
		}
		
		if(dateDebut == null) {
			dateDebut = (httpUtil.getMenuAttribute("dateDebut")==null ? new Date() : (Date)httpUtil.getMenuAttribute("dateDebut"));
			dateFin = (httpUtil.getMenuAttribute("dateFin")==null ? new Date() : (Date)httpUtil.getMenuAttribute("dateFin"));
			httpUtil.getDate("dateDebut").setValue(dateDebut);
			httpUtil.getDate("dateFin").setValue(dateDebut);
		}
		
		if(httpUtil.getRequest().getParameter("prev") != null) {
			dateDebut = DateUtil.addSubstractDate(dateDebut, TIME_ENUM.DAY, -1);
			dateFin = DateUtil.addSubstractDate(dateFin, TIME_ENUM.DAY, -1);
		} else if(httpUtil.getRequest().getParameter("next") != null) {
			dateDebut = DateUtil.addSubstractDate(dateDebut, TIME_ENUM.DAY, 1);
			dateFin = DateUtil.addSubstractDate(dateFin, TIME_ENUM.DAY, 1);
		}
		
		dateDebut = DateUtil.getStartOfDay(dateDebut);
		dateFin = DateUtil.getEndOfDay(dateFin);
		
		httpUtil.setRequestAttribute("dateDebut", dateDebut);
		httpUtil.setRequestAttribute("dateFin", dateFin);
		httpUtil.setMenuAttribute("dateDebut", dateDebut);
		httpUtil.setMenuAttribute("dateFin", dateFin);
		//
		if(!isFilterAct){
			formCriterion.put("dateDebut", dateDebut);
			formCriterion.put("dateFin", dateFin);
		} else{
			formCriterion.remove("dateDebut");
			formCriterion.remove("dateFin");
		}
		//-----------------------------------------------------------
		// Calcul du total montants sans pagination
   		BigDecimal totalHt = null;
		BigDecimal totalTtc = null;
		BigDecimal totalRemise = null;
		BigDecimal totalApresRemise = null;
	   	List<MouvementPersistant> listDataAll = (List<MouvementPersistant>) mouvementService.findByCriteriaByQueryId(cplxTable, "mouvement_groupe_find", false);
	   	Map<Long, List<MouvementPersistant>> mapTotaux = mouvementService.getMapMvmGroupe(listDataAll);
		Map<Long, List<MouvementPersistant>> mapAvoirTotaux = mouvementService.getMapMvmAvoirGroupe(listDataAll);
		//
	   	for (MouvementPersistant mvmStockViewP : listDataAll) {
	   		List<MouvementPersistant> listEnfants = mapTotaux.get(mvmStockViewP.getId());
	   		List<MouvementPersistant> listAvoirEnfants = mapAvoirTotaux.get(mvmStockViewP.getId());
	   		//
	   		if(listEnfants != null){
	   			for (MouvementPersistant mvmEnfant : listEnfants) {
	   				totalHt = BigDecimalUtil.add(totalHt, mvmEnfant.getMontant_ht());
	   				totalTtc = BigDecimalUtil.add(totalTtc, mvmEnfant.getMontant_ttc());
	   				
	   				totalRemise = BigDecimalUtil.add(totalRemise, mvmEnfant.getMontant_ttc_rem());
	   				totalApresRemise = BigDecimalUtil.add(totalApresRemise, BigDecimalUtil.substract( mvmEnfant.getMontant_ttc(), mvmEnfant.getMontant_ttc_rem()));
				}
	   		}
	   		// Liste des avoirs
	   		if(listAvoirEnfants != null){
	   			for (MouvementPersistant mvmEnfant : listAvoirEnfants) {
	   				totalHt = BigDecimalUtil.substract(totalHt, mvmEnfant.getMontant_ht());
	   				totalTtc = BigDecimalUtil.substract(totalTtc, mvmEnfant.getMontant_ttc());
	   				
	   				totalRemise = BigDecimalUtil.substract(totalRemise, mvmEnfant.getMontant_ttc_rem());
	   				totalApresRemise = BigDecimalUtil.substract(totalApresRemise, BigDecimalUtil.substract( mvmEnfant.getMontant_ttc(), mvmEnfant.getMontant_ttc_rem()));
				}
	   		}
	   	}
		
	   	httpUtil.setRequestAttribute("totalHt", totalHt);
	   	httpUtil.setRequestAttribute("totalTtc", totalTtc);
	   	httpUtil.setRequestAttribute("totalRemise", totalRemise);
	   	httpUtil.setRequestAttribute("totalApresRemise", totalApresRemise);
	   	
		List<MouvementPersistant> listDataGroupe = (List<MouvementPersistant>) mouvementService.findByCriteriaByQueryId(cplxTable, "mouvement_groupe_find");
		List<MouvementPersistant> listDataTemp = new ArrayList<>();
		
		//
	   	for (MouvementPersistant mvmStockViewP : listDataGroupe) {
	   		MouvementPersistant mvmStockViewPTemp = (MouvementPersistant) ReflectUtil.cloneBean(mvmStockViewP);
	   		
   			totalHt = null;
   			totalTtc = null;
   			List<MouvementPersistant> listEnfants = mapTotaux.get(mvmStockViewP.getId());
   			if(listEnfants != null){
	   			for (MouvementPersistant mvmEnfant : listEnfants) {
	   				totalHt = BigDecimalUtil.add(totalHt, mvmEnfant.getMontant_ht());
	   				totalTtc = BigDecimalUtil.add(totalTtc, mvmEnfant.getMontant_ttc());
				}
   			}
   			listEnfants = (listEnfants == null) ? new ArrayList<>() : listEnfants;
   			List<MouvementPersistant> listAvoirEnfants = mapAvoirTotaux.get(mvmStockViewP.getId());
   			if(listAvoirEnfants != null){
	   			for (MouvementPersistant mvmEnfant : listAvoirEnfants) {
	   				totalHt = BigDecimalUtil.substract(totalHt, mvmEnfant.getMontant_ht());
	   				totalTtc = BigDecimalUtil.substract(totalTtc, mvmEnfant.getMontant_ttc());
				}
	   			listEnfants.addAll(listAvoirEnfants);
   			}
   			mvmStockViewPTemp.setMontant_ht(totalHt);
   			mvmStockViewPTemp.setMontant_ttc(totalTtc);
   			
   			mvmStockViewPTemp.setList_groupe(listEnfants);
   			
	   		listDataTemp.add(mvmStockViewPTemp);
	   	}
	   	
		cplxTable.setDataExport(listDataTemp);
		httpUtil.setRequestAttribute("list_mouvement", listDataTemp);
	   	//
		httpUtil.setDynamicUrl("/domaine/stock/mouvement_groupe_list.jsp");
	}

	/**
	 * 
	 */
	@Override
	public void work_update(ActionUtil httpUtil) {
		MouvementBean mouvementBean = null;
		mouvementBean = setDataList(httpUtil);
		
		mouvementBean.setList_paiement((List<PaiementPersistant>) httpUtil.getMenuAttribute("PAIEMENT_DATA"));
		mouvementService.update(mouvementBean);
		
		managePieceJointe(httpUtil, mouvementBean.getId(), "mvm");
		
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "Mise à jour", "Le mouvement est mise à jour.");
		
		// Gérer le back
		FrontFilter.restaureBackParams(httpUtil.getRequest(), null, true);
		
		// Centrale
		if(ContextAppli.getAbonementBean().isOptPlusEtsCentrale()) { 
			if(ContextAppli.TYPE_MOUVEMENT_ENUM.t.toString().equals(mouvementBean.getType_mvmnt())) {
				if(emplacementService.findById(mouvementBean.getOpc_destination().getId()).getOrigine_id() != null) {// Cas emplacement externe
					centraleService.addElementToSynchronise("TRANSF", mouvementBean.getOpc_destination().getOrigine_auth(), mouvementBean.getId(), false);
				} 
			} else if(ContextAppli.TYPE_MOUVEMENT_ENUM.v.toString().equals(mouvementBean.getType_mvmnt())) {
				if(mouvementBean.getOpc_client() != null) {
					ClientPersistant cliP = mouvementService.findById(ClientPersistant.class, mouvementBean.getOpc_client().getId());
					if(StringUtil.isNotEmpty(cliP.getOrigine_auth())) {
						centraleService.addElementToSynchronise("VENTE", cliP.getOrigine_auth(), mouvementBean.getId(), false);
					}
				}
			}
		}
		
		FrontFilter.restaureBackParams(httpUtil.getRequest(), null, true);
		work_find(httpUtil);
	}
	
	/**
	 * 
	 */
	@Override
	@WorkForward(useFormValidator=true, useBean=true)
	public void work_create(ActionUtil httpUtil) {
		MouvementBean mouvementBean = setDataList(httpUtil);
		
		boolean isAchat = mouvementBean.getType_mvmnt().equals(ContextAppli.TYPE_MOUVEMENT_ENUM.a.toString());
		if(!isAchat) {
			boolean isRepondu = false;
			if(!isRepondu){
				String msgStockInsuffisant = mouvementService.getMsgStockInsuffisant(mouvementBean);
				if(StringUtil.isNotEmpty(msgStockInsuffisant) ){
					isRepondu = MessageService.addDialogConfirmMessage("STOCK_INSUFF", "stock.mouvement.work_create", msgStockInsuffisant);
				} else{
					isRepondu = true;
				}
			}
			
			if(!isRepondu){
				return;
			}
		}
		mouvementBean.setList_paiement((List<PaiementPersistant>) httpUtil.getMenuAttribute("PAIEMENT_DATA"));
		mouvementService.create(mouvementBean);

		// Centrale
		if(ContextAppli.getAbonementBean().isOptPlusEtsCentrale()) {
			if(ContextAppli.TYPE_MOUVEMENT_ENUM.t.toString().equals(mouvementBean.getType_mvmnt())) {
				EmplacementBean emplB = emplacementService.findById(mouvementBean.getOpc_destination().getId());
				if(BooleanUtil.isTrue(emplB.getIs_externe())) {// Cas emplacement externe
					centraleService.addElementToSynchronise("TRANSF", emplB.getOrigine_auth(), mouvementBean.getId(), false);
				}
			} else if(ContextAppli.TYPE_MOUVEMENT_ENUM.v.toString().equals(mouvementBean.getType_mvmnt())) {
				if(mouvementBean.getOpc_client() != null && StringUtil.isNotEmpty(mouvementBean.getOpc_client().getOrigine_auth())) {
					centraleService.addElementToSynchronise("VENTE", mouvementBean.getOpc_client().getOrigine_auth(), mouvementBean.getId(), false);
				}
			}
		}
		
		managePieceJointe(httpUtil, mouvementBean.getId(), "mvm");
		MouvementPersistant mvmP = (MouvementPersistant) mouvementService.findById(MouvementPersistant.class, mouvementBean.getId());
        lotArticleService.manageLotAchat(mvmP);
		
		
		
		// Gérer le back
		FrontFilter.restaureBackParams(httpUtil.getRequest(), null, true);
				
		work_find(httpUtil);
	}
	
	@Override
	public void work_delete(ActionUtil httpUtil) {
		Long workId = httpUtil.getWorkIdLong();
		MouvementPersistant mvmP = mouvementService.findById(MouvementPersistant.class, workId);
		
		super.work_delete(httpUtil);
		
		// Centrale
		if(ContextAppli.getAbonementBean().isOptPlusEtsCentrale()) { 
			if(ContextAppli.TYPE_MOUVEMENT_ENUM.t.toString().equals(mvmP.getType_mvmnt())) {
				if(mvmP.getOpc_destination().getOrigine_id() != null) {// Cas emplacement externe
					centraleService.addElementToSynchronise("TRANSF", mvmP.getOpc_destination().getOrigine_auth(), mvmP.getId(), true);
				}
			} else if(ContextAppli.TYPE_MOUVEMENT_ENUM.v.toString().equals(mvmP.getType_mvmnt())) {
				if(mvmP.getOpc_client() != null && StringUtil.isNotEmpty(mvmP.getOpc_client().getOrigine_auth())) {
					centraleService.addElementToSynchronise("VENTE", mvmP.getOpc_client().getOrigine_auth(), mvmP.getId(), true);
				}
			}
		}
		
		//
		manageDeleteImage(workId, "mvm");
	}

	private List<MouvementArticlePersistant> loadTranfertArticles(MouvementBean mouvementBean){
		List<MouvementArticlePersistant> listFournArticle = new ArrayList<>();
		if(mouvementBean.getMouvementIds() != null) {
			for(Long mvmId : mouvementBean.getMouvementIds()){
				if(mouvementBean.getType_transfert().equals("P")) {// Préparation
					PreparationPersistant preparationP = (PreparationPersistant) mouvementService.findById(PreparationPersistant.class, mvmId);
					for (PreparationArticlePersistant preprationArticlePersistant : preparationP.getList_article()) {
						ArticlePersistant opcArticle = (ArticlePersistant) mouvementService.findById(ArticlePersistant.class, preprationArticlePersistant.getOpc_article().getId());
						MouvementArticlePersistant mouvementArticleNew = new MouvementArticlePersistant();
						mouvementArticleNew.setOpc_article(preprationArticlePersistant.getOpc_article());
						mouvementArticleNew.setQuantite(preprationArticlePersistant.getQuantite());
						
						mouvementArticleNew.setPrix_ht(opcArticle.getPrix_achat_ht());// On met le dernier prix d'achat connu
						mouvementArticleNew.setPrix_ttc(opcArticle.getPrix_achat_ttc());// On met le dernier prix d'achat connu
						
						listFournArticle.add(mouvementArticleNew);
					}
				} else {// BL ou facture
					MouvementPersistant mouvementP = mouvementService.findById(mvmId);
					listFournArticle.addAll(mouvementP.getList_article());
				}
			}
		}
		
		return listFournArticle;
	}
	
	/**
	 * @param httpUtil
	 */
	@SuppressWarnings({ "unchecked" })
	public MouvementBean setDataList(ActionUtil httpUtil) {
		if(mouvementService == null){// Cas appel inter action
			mouvementService = ServiceUtil.getBusinessBean(IMouvementService.class);
		}
		MouvementBean mouvementBean = (MouvementBean) httpUtil.getViewBean();
		Long mvmId = httpUtil.getWorkIdLong();
		mouvementBean.setId(mvmId);
		String type = (String)httpUtil.getMenuAttribute("type_mvmnt");
		List<MouvementArticlePersistant> listIhmArticle = new ArrayList<>();
		boolean isVente = "v".equals(type);
		
		if(mouvementBean.getId() != null) {
			Map<Long, BigDecimal> mapOldArticleInfo = new HashMap<>();
			MouvementBean mvmDb = mouvementService.findById(mouvementBean.getId());
			for (MouvementArticlePersistant mvmArtP : mvmDb.getList_article()) {
				mapOldArticleInfo.put(mvmArtP.getOpc_article().getId(), mvmArtP.getQuantite());
			}
			mouvementBean.setMapOldArticleInfo(mapOldArticleInfo);
		}
		
		listIhmArticle = (List<MouvementArticlePersistant>) httpUtil.buildListBeanFromMap("opc_article.id", MouvementArticlePersistant.class, "eaiid", "idxIhm",
															"opc_article.id", "is_remise_ratio", "quantite", 
															"prix_vente", "prix_ht","prix_ttc",
															"opc_tva_enum.id", "remise", "date_peremption");
		
		if("t".equals(type)){
			List<MouvementArticlePersistant> listFinal = new ArrayList<>();
			for(MouvementArticlePersistant mvm : listIhmArticle){
				if(!BigDecimalUtil.isZero(mvm.getQuantite())){
					listFinal.add(mvm);
				}
			}
			listIhmArticle = listFinal;
		}
		
		// Impacter la quantité par l'achat en cas de freinte
		if(mvmId == null && "a".equals(type)) {
			for (MouvementArticlePersistant mouvementArticleP : listIhmArticle) {
				if(mouvementArticleP.getOpc_article() != null && mouvementArticleP.getOpc_article().getId() != null){
					ArticlePersistant artDb = mouvementService.findById(ArticlePersistant.class, mouvementArticleP.getOpc_article().getId());
					if(artDb != null){
						Integer freinte = artDb.getFreinte_achat();
						if(freinte != null && freinte != 0){ 
							BigDecimal quantite = BigDecimalUtil.substract(mouvementArticleP.getQuantite(), BigDecimalUtil.divide(BigDecimalUtil.multiply(mouvementArticleP.getQuantite(), BigDecimalUtil.get(freinte)), BigDecimalUtil.get(100)));
							mouvementArticleP.setQuantite(quantite);
						}
					}
				}
			}
		}
		
		// Trier par ihmIDX
		Collections.sort(listIhmArticle, new SortByIhmIdx());
		
		// Ajout prix si non existant
		List<MouvementArticlePersistant> finalIhmList = new ArrayList<>();
		for(MouvementArticlePersistant det : listIhmArticle){
			if(det.getOpc_article() == null || det.getOpc_article().getId() == null) {
				continue;
			}
			ArticlePersistant artP = mouvementService.findById(ArticlePersistant.class, det.getOpc_article().getId());
			if(artP == null) {
				continue;
			}
			if(det.getPrix_ht() == null) {
				det.setPrix_ht(artP.getPrix_achat_ht());// On met le dernier prix d'achat connu
				det.setOpc_tva_enum(artP.getOpc_tva_enum());
				det.setPrix_ttc(artP.getPrix_achat_ttc());// On met le dernier prix d'achat connu
			}
			finalIhmList.add(det);
		}
		
		List<MouvementArticlePersistant> listArticle = new ArrayList<>();
		if (mouvementBean.getId() != null) {
			MouvementBean mvmBean = mouvementService.findById(mouvementBean.getId());
			listArticle = mvmBean.getList_article();
			// maj Lot
//			if(type.equals("a")) {
//				for (MouvementArticlePersistant mvmArtPer : listArticle) {
//					//supprimer les anciens enregistrements
//					if(mvmArtPer.getOpc_lot_article() != null || mvmArtPer.getDate_peremption() != null) {
//						LotArticlePersistant lotArt = mvmArtPer.getOpc_lot_article();
//						mvmArtPer.setOpc_lot_article(null);
//						lotArtService.delete(lotArt.getId());
//					}
//				}
//			}
			listArticle.clear();
		}
		
		listArticle.addAll(finalIhmList);
		
		// MAj prix
		BigDecimal mttTotalHtTotal = null, 
				mttTotalTtcTotal = null, 
				mttTotalHtRemise = null,
				mttTotalTTCRemise = null;
		
		for (MouvementArticlePersistant mvmArtP : listArticle) {
			BigDecimal mttTotalHt = BigDecimalUtil.multiply(mvmArtP.getPrix_ht(), mvmArtP.getQuantite());
			BigDecimal mttTotalTtc = BigDecimalUtil.multiply(mvmArtP.getPrix_ttc(), mvmArtP.getQuantite());
			if(isVente) {
				mttTotalTtc = BigDecimalUtil.multiply(mvmArtP.getPrix_vente(), mvmArtP.getQuantite());
			}
			
			
			boolean isPourcent = BooleanUtil.isTrue(mvmArtP.getIs_remise_ratio());
			if(!BigDecimalUtil.isZero(mvmArtP.getRemise())){
				BigDecimal remise = null;
				if(isPourcent){
					remise  = BigDecimalUtil.divide(BigDecimalUtil.multiply(mttTotalHt, mvmArtP.getRemise()), BigDecimalUtil.get(100));
				} else{
					remise = mvmArtP.getRemise();					
				}
				mttTotalHtRemise = BigDecimalUtil.add(mttTotalHtRemise, remise);
				
				// Remise TTC
				BigDecimal tva = null, remiseTtc = remise;
				if(mvmArtP.getOpc_tva_enum() != null){
					ValTypeEnumPersistant tvaBean = valEnumService.findById(mvmArtP.getOpc_tva_enum().getId());
					tva = BigDecimalUtil.ZERO;
					//
					if(NumericUtil.isNum(tvaBean.getLibelle()) || NumericUtil.isDecimal(tvaBean.getLibelle())){
						tva = BigDecimalUtil.get(tvaBean.getLibelle());
					} else if(NumericUtil.isNum(tvaBean.getCode()) || NumericUtil.isDecimal(tvaBean.getCode())){
						tva = BigDecimalUtil.get(tvaBean.getCode());
					}
					remiseTtc = BigDecimalUtil.divide(BigDecimalUtil.multiply(remise, tva), BigDecimalUtil.get(100));
					remiseTtc = BigDecimalUtil.add(remiseTtc, remise);
				}
				mttTotalTTCRemise = BigDecimalUtil.add(mttTotalTTCRemise, remiseTtc);
				
				mttTotalHt = BigDecimalUtil.substract(mttTotalHt, remise);
				mttTotalTtc = BigDecimalUtil.substract(mttTotalTtc, remiseTtc);
			}
			mvmArtP.setPrix_ht_total(mttTotalHt);
			mvmArtP.setPrix_ttc_total(mttTotalTtc);
			
			// Calcul total mouvement
			mttTotalHtTotal = BigDecimalUtil.add(mttTotalHtTotal, mttTotalHt);
			mttTotalTtcTotal = BigDecimalUtil.add(mttTotalTtcTotal, mttTotalTtc);
		}
		mouvementBean.setList_article(listArticle);
		
		mouvementBean.setMontant_ht(mttTotalHtTotal);
		mouvementBean.setMontant_tva(BigDecimalUtil.substract(mttTotalTtcTotal, mttTotalHtTotal));
		mouvementBean.setMontant_ttc(mttTotalTtcTotal);
		mouvementBean.setMontant_ht_rem(mttTotalHtRemise);
		mouvementBean.setMontant_ttc_rem(mttTotalTTCRemise);
		
		return mouvementBean;
	}
	
	/**
	 * @param httpUtil
	 */
	public void refreshDataMtt(ActionUtil httpUtil) {
		String typeMouvement = (String) httpUtil.getMenuAttribute("type_mvmnt");	
		boolean isVente = "v".equals(typeMouvement);
		
		StringBuilder result = new StringBuilder("");
		int nbrDec = ContextGloabalAppli.getNbrDecimalSaisie();
		
		String idx = httpUtil.getParameter("cpt");
		boolean isArt = httpUtil.getParameter("isArt") != null;
		boolean isTtc = StringUtil.isTrue(httpUtil.getParameter("isTtc"));
		
		BigDecimal quantite = null;
		BigDecimal prix_ttc = null;
		BigDecimal tva = BigDecimalUtil.ZERO;
		BigDecimal remiseTTC = null;
		//
		if(idx != null){
			// Effacer les données si pas d'article
			String artId = httpUtil.getParameter("opc_article.id_"+idx);
			if(isArt && StringUtil.isEmpty(artId)) {
				result.append("prix_ht_"+idx+":");
				result.append(";total_ln_"+idx+":");
				result.append(";quantite_"+idx+":");
				
				if(!isVente) {
					result.append(";prix_ttc_"+idx+":");
					result.append(";remise_"+idx+":");
					result.append(";opc_tva_enum.id_"+idx);
				} else {
					result.append(";prix_vente_"+idx+":");
				}
			} else {
				BigDecimal prix_ht = null;
				Long tvaId = null;
				
				// Si le montant TTC a été modifié par la saisie dans le champs
				if(isTtc) {
					if(!isVente) {
						prix_ttc = BigDecimalUtil.get(httpUtil.getParameter("prix_ttc_"+idx));
						
						if(StringUtil.isNotEmpty(httpUtil.getParameter("opc_tva_enum.id_"+idx))){
							tvaId = Long.valueOf(httpUtil.getParameter("opc_tva_enum.id_"+idx));
							ValTypeEnumBean tvaBean = valEnumService.findById(tvaId);
							tva = BigDecimalUtil.ZERO;
							//
							if(NumericUtil.isNum(tvaBean.getLibelle()) || NumericUtil.isDecimal(tvaBean.getLibelle())){
								tva = BigDecimalUtil.get(tvaBean.getLibelle());
							} else if(NumericUtil.isNum(tvaBean.getCode()) || NumericUtil.isDecimal(tvaBean.getCode())){
								tva = BigDecimalUtil.get(tvaBean.getCode());
							}
							
							BigDecimal coeficient = BigDecimalUtil.divide(BigDecimalUtil.add(BigDecimalUtil.get(100), tva), BigDecimalUtil.get(100));
							prix_ht = BigDecimalUtil.divide(prix_ttc, coeficient);
						} else {
							prix_ht = prix_ttc;
						}
					} else {
						prix_ttc = BigDecimalUtil.get(httpUtil.getParameter("prix_vente_"+idx));
						BigDecimal tauxVente = BigDecimalUtil.get(ContextGloabalAppli.getGlobalConfig(ContextAppli.PARAM_APPLI_ENUM.TVA_VENTE.toString()));
						prix_ht = BigDecimalUtil.substract(prix_ttc, BigDecimalUtil.multiply(prix_ttc, BigDecimalUtil.divide(tauxVente, BigDecimalUtil.get(100))));
					}
				} else {
					if(isArt) {
						if(NumericUtil.isNum(artId)){
							ArticlePersistant articleP = (ArticlePersistant)mouvementService.findById(ArticlePersistant.class, Long.valueOf(artId));
							if(articleP != null) {
								tvaId = (articleP.getOpc_tva_enum()!=null ? articleP.getOpc_tva_enum().getId() : null);
								prix_ht = articleP.getPrix_achat_ht();
							}
						}
					} else {
						prix_ht = BigDecimalUtil.get(httpUtil.getParameter("prix_ht_"+idx));
						if(!isVente) {
							if(StringUtil.isNotEmpty(httpUtil.getParameter("opc_tva_enum.id_"+idx))) {
								tvaId = Long.valueOf(httpUtil.getParameter("opc_tva_enum.id_"+idx));
							}
						}
					}
				}
				
				quantite = BigDecimalUtil.get(httpUtil.getParameter("quantite_"+idx));
				BigDecimal totalHtBrut = BigDecimalUtil.multiply(prix_ht, quantite);
				
				BigDecimal remise = BigDecimalUtil.get(httpUtil.getParameter("remise_"+idx));
				boolean isPourcent = StringUtil.isTrue(httpUtil.getParameter("is_remise_ratio_"+idx));
				
				if(!BigDecimalUtil.isZero(remise)){
					if(isPourcent){
						remise = BigDecimalUtil.divide(BigDecimalUtil.multiply(totalHtBrut, remise), BigDecimalUtil.get(100));
					}
				}
				
				if(!isTtc) {
					if(!isVente) {
						if(tvaId != null){
							ValTypeEnumBean tvaBean = valEnumService.findById(tvaId);
							//
							if(NumericUtil.isNum(tvaBean.getLibelle()) || NumericUtil.isDecimal(tvaBean.getLibelle())){
								tva = BigDecimalUtil.get(tvaBean.getLibelle());
							} else if(NumericUtil.isNum(tvaBean.getCode()) || NumericUtil.isDecimal(tvaBean.getCode())){
								tva = BigDecimalUtil.get(tvaBean.getCode());
							}
							
							if(StringUtil.isNotEmpty(prix_ht) && StringUtil.isNotEmpty(tva)){
								prix_ttc = BigDecimalUtil.add(prix_ht, BigDecimalUtil.multiply(prix_ht, BigDecimalUtil.divide(tva, BigDecimalUtil.get(100))));
							}
						}
					} else {
						BigDecimal tauxVente = BigDecimalUtil.get(ContextGloabalAppli.getGlobalConfig(ContextAppli.PARAM_APPLI_ENUM.TVA_VENTE.toString()));
						prix_ttc = BigDecimalUtil.add(prix_ht, BigDecimalUtil.multiply(prix_ht, BigDecimalUtil.divide(tauxVente, BigDecimalUtil.get(100))));
					}
				}
				
				result.append(";prix_ht_"+idx+":"+BigDecimalUtil.formatNumberZero(prix_ht, nbrDec));
				if(!isVente) {
					result.append(";opc_tva_enum.id_"+idx+":"+tvaId);
					result.append(";prix_ttc_"+idx+":"+BigDecimalUtil.formatNumberZero(prix_ttc, nbrDec));
				} else {
					result.append(";prix_vente_"+idx+":"+BigDecimalUtil.formatNumberZero(prix_ttc, nbrDec));
				}
				
				if(!BigDecimalUtil.isZero(remise)){
					BigDecimal remiseAdd = BigDecimalUtil.divide(BigDecimalUtil.multiply(remise, tva), BigDecimalUtil.get(100));
					remiseTTC = BigDecimalUtil.add(remise, remiseAdd);
				}
				
				result.append(";total_ln_"+idx+":"+BigDecimalUtil.formatNumberZero(BigDecimalUtil.substract(BigDecimalUtil.multiply(prix_ttc, quantite), remiseTTC), nbrDec));
				result.append(";total_lnHt_"+idx+":"+BigDecimalUtil.formatNumberZero(BigDecimalUtil.substract(totalHtBrut, remise), nbrDec));
			}
		}
		
		
		// Total
		Map<String, Object> params = (Map)httpUtil.getRequest().getAttribute(ProjectConstante.WORK_PARAMS);
		BigDecimal totalAll = null;
		//
		for(String key : params.keySet()){
			if(!key.startsWith("prix_ht")){
				continue;
			}
				String idx2 = key.substring(key.lastIndexOf("_")+1);

				if("0".equals(idx2)){
					continue;
				}
				
				// Ne pas traiter la ligne encours
				if(idx2.equals(idx)){
					if(!isVente) {
						totalAll = BigDecimalUtil.add(totalAll, BigDecimalUtil.substract(BigDecimalUtil.multiply(prix_ttc, quantite), remiseTTC));
					} else {
						totalAll = BigDecimalUtil.add(totalAll, BigDecimalUtil.multiply(prix_ttc, quantite));
					}
					
					continue; 
				}
				
				BigDecimal tvaC = BigDecimalUtil.ZERO;
				if(StringUtil.isNotEmpty(httpUtil.getParameter("opc_tva_enum.id_"+idx2))){
					Long tvaId = Long.valueOf(httpUtil.getParameter("opc_tva_enum.id_"+idx2));
					ValTypeEnumBean tvaBean = valEnumService.findById(tvaId);
					
					//
					if(NumericUtil.isNum(tvaBean.getLibelle()) || NumericUtil.isDecimal(tvaBean.getLibelle())){
						tvaC = BigDecimalUtil.get(tvaBean.getLibelle());
					} else if(NumericUtil.isNum(tvaBean.getCode()) || NumericUtil.isDecimal(tvaBean.getCode())){
						tvaC = BigDecimalUtil.get(tvaBean.getCode());
					}
				}
				
				BigDecimal quantiteC = BigDecimalUtil.get(""+params.get("quantite_"+idx2));
				//
				BigDecimal prixTtcC = BigDecimalUtil.get(""+params.get("prix_ttc_"+idx2));
				if(isVente) {
					prixTtcC = BigDecimalUtil.get(""+params.get("prix_vente_"+idx2));
				}
				BigDecimal prixHtC = BigDecimalUtil.get(""+params.get("prix_ht_"+idx2));
				BigDecimal totalHtBrut = BigDecimalUtil.multiply(prixHtC, quantiteC);
				BigDecimal totalTtcBrut = BigDecimalUtil.multiply(prixTtcC, quantiteC);
				//
				if(!isVente) {
					BigDecimal remiseC = BigDecimalUtil.get(httpUtil.getParameter("remise_"+idx2));
					boolean isPourcent = StringUtil.isTrue(httpUtil.getParameter("is_remise_ratio_"+idx2));

					
					if(!BigDecimalUtil.isZero(remiseC)){ 
						if(isPourcent){
							remiseC = BigDecimalUtil.divide(BigDecimalUtil.multiply(totalHtBrut, remiseC), BigDecimalUtil.get(100));
						}
					}
					BigDecimal remiseTTCC = null;
					if(!BigDecimalUtil.isZero(remiseC)){
						BigDecimal remiseAdd = BigDecimalUtil.divide(BigDecimalUtil.multiply(remiseC, tvaC), BigDecimalUtil.get(100));
						remiseTTCC = BigDecimalUtil.add(remiseC, remiseAdd);
					}
					totalAll = BigDecimalUtil.add(totalAll, BigDecimalUtil.substract(totalTtcBrut, remiseTTCC));
				} else {
					prixTtcC = BigDecimalUtil.get(""+params.get("prix_vente_"+idx2));
					totalAll = BigDecimalUtil.add(totalAll, totalTtcBrut);
				}
		}
		
		result.append(";total_all:"+BigDecimalUtil.formatNumberZero(totalAll, nbrDec));
		//
		httpUtil.writeResponse(result.toString());
	}
	
	public void etat_article_total(ActionUtil httpUtil) {
		if(httpUtil.getMenuAttribute("IS_TTL_MTT") == null) {
			httpUtil.setMenuAttribute("IS_TTL_MTT", 1);
		} else {
			httpUtil.removeMenuAttribute("IS_TTL_MTT");
		}
		etat_article_work_find(httpUtil);
	}
	
	/**
	 * @param httpUtil
	 */
	public void etat_article_work_find(ActionUtil httpUtil) {
		List listFamille = familleService.getListeFamille("ST", true, false);
		httpUtil.setRequestAttribute("listeFaimlle", listFamille);
		 
//		List<ArticlePersistant> listArticle = articleService.getListArticleStock(false);
//		httpUtil.setRequestAttribute("listArticle", listArticle);
		
		List<EmplacementBean> listEmplacement = emplacementService.findAll(Order.asc("titre"));
		httpUtil.setRequestAttribute("listEmplacement", listEmplacement);
		 
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_etat_article");
		List<ArticleStockInfoPersistant> listEtatArt = (List<ArticleStockInfoPersistant>) articleService.findByCriteriaByQueryId(cplxTable, "article_view_find");
		
		for (ArticleStockInfoPersistant etatArticle : listEtatArt) {
			List<FamillePersistant> familleStr = familleService.getFamilleParent("ST", etatArticle.getOpc_article().getOpc_famille_stock().getId());
			etatArticle.getOpc_article().setFamilleStr(familleStr);
		}
		
		if(httpUtil.getMenuAttribute("IS_TTL_MTT") != null) {
			List<ArticleStockInfoPersistant> listEtatArtTotal = (List<ArticleStockInfoPersistant>) articleService.findByCriteriaByQueryId(cplxTable, "article_view_find", false);
			BigDecimal totalEtatHt = null;
			BigDecimal totalEtatTtc = null;
			
			for (ArticleStockInfoPersistant etatArticle : listEtatArtTotal) {
				ArticlePersistant opc_article = etatArticle.getOpc_article();
				BigDecimal prix_achat_ht = opc_article.getPrix_achat_ht();
				BigDecimal prix_achat_ttc = opc_article.getPrix_achat_ttc();
				BigDecimal qte_restante = etatArticle.getQte_restante();
				
				totalEtatHt = BigDecimalUtil.add(totalEtatHt, BigDecimalUtil.multiply(prix_achat_ht, qte_restante));
				totalEtatTtc = BigDecimalUtil.add(totalEtatTtc, BigDecimalUtil.multiply(prix_achat_ttc, qte_restante));
			}
			
			httpUtil.setRequestAttribute("totalEtatHt", totalEtatHt);
			httpUtil.setRequestAttribute("totalEtatTtc", totalEtatTtc);
		}
		httpUtil.setRequestAttribute("listEtatArt", listEtatArt);
		
		httpUtil.setDynamicUrl("/domaine/stock/etat_article_list.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void etat_article_detail(ActionUtil httpUtil) {
		if(httpUtil.getParameter("mode_det") != null) {
			httpUtil.setMenuAttribute("mode_det", httpUtil.getParameter("mode_det"));
		}
		boolean isDet = "D".equals(httpUtil.getMenuAttribute("mode_det"));
		
		String[][] typeMvm = new String[][]{{"v", "Vente"}, {"vc", "Vente caisse"}, {"a", "Achat"}, {"av", "Avoir"}, {"p", "Perte"}
				, {"c", "Consommation"}, {"t", "Transfert"}, {"i", "Inventaire"}};
		httpUtil.setRequestAttribute("typeMvm", typeMvm);
				
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_etat_detail_article");
		Long articleId = httpUtil.getWorkIdLong();
		
		if(articleId != null){
			httpUtil.setMenuAttribute("articleId", articleId);
		} else{
			articleId = (Long) httpUtil.getMenuAttribute("articleId");
		}
		cplxTable.getFormBean().getFormCriterion().put("articleId", articleId);
		
		ArticleBean articleBean = articleService.findById(articleId);
		httpUtil.setRequestAttribute("articleLib", articleBean.getCode()+"-"+articleBean.getLibelle());
		List<MouvementArticlePersistant> listMvmArt = null;
		
		if(!isDet) {
			String reqInv = "from MouvementArticlePersistant mouvementArticle "
					+ "where mouvementArticle.opc_article.id='{articleId}' "
					+ "and mouvementArticle.opc_mouvement.type_mvmnt='i' " 
					+ "order by mouvementArticle.opc_mouvement.date_mouvement desc, mouvementArticle.id desc";
			List<MouvementArticlePersistant> listMvmArtInv = (List<MouvementArticlePersistant>) articleService.findByCriteria(cplxTable, reqInv);		
			Long lastinvId = (listMvmArtInv.size() > 0 ? listMvmArtInv.get(0).getId() : -99);
						
			String reqGroupe = "from MouvementArticlePersistant mouvementArticle "
					+ "where mouvementArticle.opc_article.id='{articleId}' and mouvementArticle.id >= "+lastinvId 
					+ " group by "
						+ "year(mouvementArticle.opc_mouvement.date_mouvement), "
						+ "month(mouvementArticle.opc_mouvement.date_mouvement), "
						+ "day(mouvementArticle.opc_mouvement.date_mouvement), "
						+ "mouvementArticle.opc_mouvement.type_mvmnt "
					+ "order by mouvementArticle.opc_mouvement.date_mouvement desc, mouvementArticle.id desc";
			
			String reqDet = "from MouvementArticlePersistant mouvementArticle "
					+ "where mouvementArticle.opc_article.id='{articleId}' and mouvementArticle.id >= "+lastinvId 
					+ " order by mouvementArticle.opc_mouvement.date_mouvement desc, "
					+ "mouvementArticle.id desc";
			listMvmArt = (List<MouvementArticlePersistant>) articleService.findByCriteria(cplxTable, reqDet, false);
			List<MouvementArticlePersistant> listMvmArtGroupe = (List<MouvementArticlePersistant>) articleService.findByCriteria(cplxTable, reqGroupe);
			
			Map<String, BigDecimal> mapTotauxQte = new HashMap<>();
			for (MouvementArticlePersistant mouvementArticleP : listMvmArt) {
				MouvementPersistant opc_mvm = mouvementArticleP.getOpc_mouvement();
				String key = DateUtil.dateToString(opc_mvm.getDate_mouvement(), "yyyy-MM-dd")
									+"_"+opc_mvm.getType_mvmnt();
				
				mapTotauxQte.put(key, BigDecimalUtil.add(mapTotauxQte.get(key), mouvementArticleP.getQuantite()));
			}
			
			httpUtil.setRequestAttribute("mapQteTot", mapTotauxQte);
			httpUtil.setRequestAttribute("listMvmArt", listMvmArtGroupe);

		} else {
			String reqDet = "from MouvementArticlePersistant mouvementArticle "
					+ "where mouvementArticle.opc_article.id='{articleId}' " 
					+ " order by mouvementArticle.opc_mouvement.date_mouvement desc, "
					+ "mouvementArticle.id desc";
			listMvmArt = (List<MouvementArticlePersistant>) articleService.findByCriteria(cplxTable, reqDet);
			httpUtil.setRequestAttribute("listMvmArt", listMvmArt);
		}

		//SOLDE STOCK
		Map<String, BigDecimal> mapStock = new HashMap<>();
		for (MouvementArticlePersistant mouvementArticleP : listMvmArt) {
			if(mouvementArticleP.getOpc_mouvement().getOpc_emplacement() != null) {
				String titre = mouvementArticleP.getOpc_mouvement().getOpc_emplacement().getTitre();
				if(mapStock.get(titre) == null) {
					List<ArticleStockInfoPersistant> listArtInfoP = articleService.getQuery("from ArticleStockInfoPersistant "
			  				+ "where opc_article.id=:idArt and opc_emplacement.id=:emplId")
			  				.setParameter("idArt", articleId)
			  				.setParameter("emplId", mouvementArticleP.getOpc_mouvement().getOpc_emplacement().getId())
			  				.getResultList();
			  			
					ArticleStockInfoPersistant artInfoP = (listArtInfoP.size() > 0 ? listArtInfoP.get(0) : null);
					
					mapStock.put(titre, (artInfoP != null ? artInfoP.getQte_reel() : BigDecimalUtil.ZERO));
				}
			}
			if(mouvementArticleP.getOpc_mouvement().getOpc_destination() != null) {
				String titre = mouvementArticleP.getOpc_mouvement().getOpc_destination().getTitre();
				if(mapStock.get(titre) == null) {
					List<ArticleStockInfoPersistant> listArtInfoP = articleService.getQuery("from ArticleStockInfoPersistant "
			  				+ "where opc_article.id=:idArt and opc_emplacement.id=:emplId")
			  				.setParameter("idArt", articleId)
			  				.setParameter("emplId", mouvementArticleP.getOpc_mouvement().getOpc_destination().getId())
			  				.getResultList();
					ArticleStockInfoPersistant artInfoP = (listArtInfoP.size() > 0 ? listArtInfoP.get(0) : null);
					
					mapStock.put(titre, (artInfoP != null ? artInfoP.getQte_reel() : BigDecimalUtil.ZERO));
				}	
			}
		}
		
		List<EmplacementBean> listEmplacement = emplacementService.findAll(Order.asc("titre"));
		httpUtil.setRequestAttribute("listEmplacement", listEmplacement);
		httpUtil.setRequestAttribute("mapStock", mapStock);
		
		httpUtil.setDynamicUrl("/domaine/stock/etat_article_detail_list.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void editTrEtatArticle(ActionUtil httpUtil) {
		String[] artEmpl = httpUtil.getParameter("art").split("-");
		Long articleId = Long.valueOf(artEmpl[0]);
		Long emplacementId = Long.valueOf(artEmpl[1]);
		ArticleStockInfoPersistant artEtatBean = mouvementService.getArticleEtatStock(articleId, emplacementId);
		
		httpUtil.setRequestAttribute("artEtatBean", artEtatBean);
		httpUtil.setDynamicUrl("/domaine/stock/etat_article_tr_consult.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void genererNumFac(ActionUtil httpUtil) {
		String type = (String)httpUtil.getMenuAttribute("type_mvmnt");
		if(StringUtil.isEmpty(type)) {
			type = TYPE_MOUVEMENT_ENUM.a.toString();
		}
		String numFac = mouvementService.generateNumFac(TYPE_MOUVEMENT_ENUM.valueOf(type));
		httpUtil.writeResponse(numFac);
	}
	
	/**
	 * @param httpUtil
	 */
	public void genererNumBL(ActionUtil httpUtil) {
		String type = (String)httpUtil.getMenuAttribute("type_mvmnt");
		if(StringUtil.isEmpty(type)) {
			MessageService.addBannerMessage("Le type de mouvement est inconnu.");
			return;
		}
		
		String numBl = mouvementService.generateNumBl(type);
		httpUtil.writeResponse(numBl);
	}
	
	/**
	 * @param httpUtil
	 */
	public void editTrMvm(ActionUtil httpUtil){
		Long mvmId = httpUtil.getLongParameter("art");
		
		IMouvementService mvmS = (IMouvementService) ServiceUtil.getBusinessBean(IMouvementService.class);
		MouvementBean mvmBean = mvmS.findById(mvmId);
		if(mvmBean == null) { //si on vient depuis pointage chèques
			PaiementPersistant paiementP = (PaiementPersistant) mvmS.findById(PaiementPersistant.class, mvmId);
			mvmBean = mvmS.findById(paiementP.getElementId());
		}
		
		List<MouvementArticlePersistant> listDetail = new ArrayList();
		if(BooleanUtil.isTrue(mvmBean.getIs_groupant())){
   			List<MouvementPersistant> listEnfants = mouvementService.getListMvmGroupe(mvmId);
   			for (MouvementPersistant mvmEnfant : listEnfants) {
   				listDetail.addAll(mvmEnfant.getList_article());
   			}
		} else{
			listDetail = mvmBean.getList_article();
		}
		
		httpUtil.setRequestAttribute("listArtDetail", listDetail);
		httpUtil.setRequestAttribute("mouvementBean", mvmBean);
		httpUtil.setDynamicUrl("/domaine/stock/mouvement_tr_consult.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void downloadPieceJointe(ActionUtil httpUtil) {
		httpUtil.manageInputFileView("mvm");
	}
	
	/**
	 * @param listArt
	 */
	private List<MouvementArticlePersistant> grouperArticles(List<MouvementArticlePersistant> listMvmArt) {
		Map<String, MouvementArticlePersistant> mapDetail = new HashMap<String, MouvementArticlePersistant>();
		
		for (MouvementArticlePersistant art : listMvmArt) {
			MouvementArticlePersistant mvmAClone0 = new MouvementArticlePersistant();
			ReflectUtil.copyProperties(mvmAClone0, art);
			mvmAClone0.setId(null);
			
			MouvementArticlePersistant mvmMap = mapDetail.get(mvmAClone0.getOpc_article().getCode());
			BigDecimal qte = (mvmMap != null ? mvmMap.getQuantite() : null);
			BigDecimal mtt = (mvmMap != null ? mvmMap.getPrix_ttc_total() : null);
			
			mvmAClone0.setQuantite(BigDecimalUtil.add(qte, mvmAClone0.getQuantite()));
			mvmAClone0.setPrix_ttc_total(BigDecimalUtil.add(mtt, mvmAClone0.getQuantite()));
			
			mapDetail.put(art.getOpc_article().getCode(), mvmAClone0);
		}
		listMvmArt.clear();
		for(String key : mapDetail.keySet()) {
			listMvmArt.add(mapDetail.get(key));
		}
		
		return listMvmArt;
	}
	
	public void genererBonCommandeSeuilStock(ActionUtil httpUtil){
		if(httpUtil.getParameter("isSub") == null){
			httpUtil.setRequestAttribute("listEmplacement", emplacementService.getEmplacementsInternes());
			
			httpUtil.setDynamicUrl("/domaine/stock/commande_emplacement_popup.jsp");
			return;
		}
		
		String[] emplacements = httpUtil.getRequest().getParameterValues("emplcements");
		int mvmCmd = mouvementService.genererBonCommandeSeuilStock(emplacements);
		
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "", mvmCmd+" mouvements ont crées.");
		
		work_find(httpUtil);
	}
	
	/**
	 * @param httpUtil
	 */
	public void work_post(ActionUtil httpUtil) {
		if(httpUtil.getRequestAttribute("demandeTrans") != null) {
			DemandeTransfertBean demandeTB = (DemandeTransfertBean) httpUtil.getRequestAttribute("demandeTrans");
			
			httpUtil.setViewBean((IViewBean) httpUtil.getRequestAttribute("mouvementB"));
			httpUtil.setRequestAttribute("listeDestination", emplacementService.getEmplacementsExternes(demandeTB.getOrigine_auth()));
		}
		
		Long mvmid = null;  
		MouvementBean mouvementBean = (MouvementBean)httpUtil.getViewBean();
		String action = httpUtil.getAction();
		
		if(mouvementBean != null && mouvementBean.getId() != null){
			mvmid = mouvementBean.getId();
		}
		
		if(httpUtil.getViewBean() != null && (ActionConstante.INIT_UPDATE.equals(action) || ActionConstante.EDIT.equals(action))) {
			httpUtil.setMenuAttribute("PAIEMENT_DATA", ((MouvementBean)httpUtil.getViewBean()).getList_paiement());
		}
		
		List<FournisseurChequeBean> listChequeFournisseur = fournisseurChequeService.getListChequeFournisseurActifs(null, mvmid);
		httpUtil.setRequestAttribute("listChequeFournisseur", listChequeFournisseur);
		
		if(mouvementBean != null){
			httpUtil.setRequestAttribute("type_transfert", mouvementBean.getType_transfert());
			// Si mois clos
			if(mouvementBean.getDate_mouvement() != null && etatFinancierService.isMoisClos(mouvementBean.getDate_mouvement())) {
				MessageService.addBannerMessage(MSG_TYPE.INFO, "Mois clos.");
				httpUtil.setRequestAttribute("is_mois_clos", true);
			}
		}
		
		if(action.equals(ActionConstante.EDIT) || action.equals(ActionConstante.INIT_UPDATE)) {
			if(StringUtil.isNotEmpty(mouvementBean.getTransfert_mvm_ids())) {
				String[] mvmIdsTransfert = StringUtil.getArrayFromStringDelim(mouvementBean.getTransfert_mvm_ids(), ";");
				Long[] mvmIdsTransfertLong = new Long[mvmIdsTransfert.length]; 
				for (int i=0; i<mvmIdsTransfert.length; i++) {
					mvmIdsTransfertLong[i] = Long.valueOf(mvmIdsTransfert[i]);
				}
				mouvementBean.setMouvementIds(mvmIdsTransfertLong);
			}
			
			// Charger les données des select d'avoir, facture, ....
			loadDataList(httpUtil, mouvementBean);
			
			// La date du mouvement doit ÃªtrpostÃ©rieure Ã  la date du dernier inventaire et le mois ne doit pas être clos
			if(action.equals(ActionConstante.INIT_UPDATE)){
				if(mouvementBean.getOpc_emplacement() != null){
					Date dateMax = inventaireService.getMaxDateInventaireValide(mouvementBean.getOpc_emplacement().getId());
					if(dateMax != null && mouvementBean.getDate_mouvement().before(dateMax)){
						httpUtil.setRequestAttribute("is_inv_prev", true);
					}
				}
				if(mouvementBean.getOpc_destination() != null){
					Date dateMax = inventaireService.getMaxDateInventaireValide(mouvementBean.getOpc_destination().getId());
					if(dateMax != null && mouvementBean.getDate_mouvement().before(dateMax)){
						httpUtil.setRequestAttribute("is_inv_prev", true);
					}
				}
			}
		}
		//
		String type = (String) httpUtil.getMenuAttribute("type_mvmnt");
		manageDataForm(httpUtil, "MVM_"+type);
	}
	
	public void getListArticles(ActionUtil httpUtil) {
		String value = httpUtil.getParameter("term");
		String type = (String) httpUtil.getMenuAttribute("type_mvmnt");
		boolean isStock = "dv".equals(type) ? false : true;
		
		List<ArticlePersistant> listArticle = articleService.getArticlesAutocomplete(value, false, isStock);

		Map<String, List> mapData = new HashMap<>();
		mapData.put("items", listArticle); 
		
		String json = ControllerUtil.getJSonDataAnnotStartegy(mapData);
		httpUtil.writeResponse(json);
	}
	public void getListArticlesNonStock(ActionUtil httpUtil) {
		String value = httpUtil.getParameter("term");
		List<ArticlePersistant> listArticle = articleService.getArticlesAutocomplete(value, false, false);

		Map<String, List> mapData = new HashMap<>();
		mapData.put("items", listArticle); 
		
		String json = ControllerUtil.getJSonDataAnnotStartegy(mapData);
		httpUtil.writeResponse(json);
	}
	
	//****************************************************************************//
//	public void editPdfFacture(ActionUtil httpUtil){
//		Long mvmId = httpUtil.getWorkIdLong();
//		MouvementPersistant mouvementP = mouvementService.findById(mvmId);
//		File pdfFile = new FactureVentePDF(mouvementP).exportPdf();
//		// Archiver le pdf dans la GED
//		mouvementService.archiverPdfFactureGed(mouvementP.getOpc_client(), mouvementP.getNum_facture(), mouvementP.getDate_mouvement(), pdfFile);
//		
//		httpUtil.doDownload(pdfFile, true);
//	}
	
	public void manage_travaux(ActionUtil httpUtil){
		Long elementId = httpUtil.getWorkIdLong();
		
		if(httpUtil.getParameter("isSub") != null) {
			Long travauxId = httpUtil.getLongParameter("travaux");
			TravauxPersistant opc_travaux = travauxId != null ? mouvementService.findById(TravauxPersistant.class, travauxId) : null;
			
			if(httpUtil.getParameter("src").equals("CD")) {
				ChargeDiversPersistant cdP = mouvementService.findById(ChargeDiversPersistant.class, elementId);
				cdP.setOpc_travaux(opc_travaux);
				//
				mouvementService.mergeEntity(cdP);
			} else {
				MouvementPersistant cdP = mouvementService.findById(MouvementPersistant.class, elementId);
				cdP.setOpc_travaux(opc_travaux);
				//
				mouvementService.mergeEntity(cdP);
			}
		
			if(httpUtil.getParameter("src").equals("CD")) {
				httpUtil.setDynamicUrl("stock.chargeDivers.work_find");
			} else {
				work_find(httpUtil);
			}
		} else {
			httpUtil.setRequestAttribute("src", httpUtil.getParameter("src"));
			httpUtil.setRequestAttribute("elementId", elementId);
			httpUtil.setRequestAttribute("currTrvId", httpUtil.getParameter("trv"));
			
			httpUtil.setRequestAttribute("listTravaux", mouvementService.findAll(TravauxPersistant.class, 
					Order.asc("opc_chantier.libelle"), Order.asc("libelle")));
			
			httpUtil.setDynamicUrl("/domaine/stock/travaux_select.jsp");
		}
	}
	public void editPdfBonCommande(ActionUtil httpUtil){
		Long mvmId = httpUtil.getWorkIdLong();
		File pdfFile = new FicheCommandePDF().createPdf(mvmId);
		
		httpUtil.doDownload(pdfFile, true);
	}
	public void editPdfAvoir(ActionUtil httpUtil){
		Long mvmId = httpUtil.getWorkIdLong();
		PdfBean pdfBean = new TransfertPdf().createPdf(mvmId);
		
		httpUtil.doDownload(pdfBean.getPdfFile(), true);
	}
	
	public void editPdfTransfert(ActionUtil httpUtil){
		Long mvmId = httpUtil.getWorkIdLong();
		PdfBean pdfBean = new TransfertPdf().createPdf(mvmId);
		
		httpUtil.doDownload(pdfBean.getPdfFile(), true);
	}
	public void validerAvoir(ActionUtil httpUtil){
		Long avoirId = httpUtil.getWorkIdLong();
		mouvementService.validerAvoir(avoirId);
		
		work_find(httpUtil);
	}
	public void validerBonCommande(ActionUtil httpUtil){
		Long cmdId = httpUtil.getWorkIdLong();
		mouvementService.validerBonCommande(cmdId);
		
		work_find(httpUtil);
	}
	public void genererAchatFromCmd(ActionUtil httpUtil){
		Long cmdId = httpUtil.getWorkIdLong();
		MouvementBean mvmBean = mouvementService.genererAchatFromCmd(cmdId);
		httpUtil.setViewBean(mvmBean);
		httpUtil.setFormReadOnly(false);
		
		httpUtil.setDynamicUrl("/domaine/stock/mouvement_edit.jsp");
	}
}

