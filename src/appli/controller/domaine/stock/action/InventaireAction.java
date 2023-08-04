package appli.controller.domaine.stock.action;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.itextpdf.text.DocumentException;

import appli.controller.domaine.administration.bean.ValTypeEnumBean;
import appli.controller.domaine.personnel.bean.EmployeBean;
import appli.controller.domaine.stock.bean.EmplacementBean;
import appli.controller.domaine.stock.bean.InventaireBean;
import appli.model.domaine.administration.persistant.TypeEnumPersistant;
import appli.model.domaine.administration.service.IValTypeEnumService;
import appli.model.domaine.personnel.service.IEmployeService;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.persistant.ArticleStockInfoPersistant;
import appli.model.domaine.stock.persistant.EmplacementPersistant;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.stock.persistant.InventaireDetailPersistant;
import appli.model.domaine.stock.persistant.InventairePersistant;
import appli.model.domaine.stock.persistant.MouvementArticlePersistant;
import appli.model.domaine.stock.persistant.MouvementPersistant;
import appli.model.domaine.stock.service.IEmplacementService;
import appli.model.domaine.stock.service.IFamilleService;
import appli.model.domaine.stock.service.IInventaireService;
import appli.model.domaine.stock.service.IMouvementService;
import appli.model.domaine.stock.service.impl.FicheInventaireEcartPDF;
import appli.model.domaine.stock.service.impl.FicheInventairePDF;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.common.constante.ActionConstante;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.ModelConstante;

@WorkController(nameSpace="stock", bean=InventaireBean.class, jspRootPath="/domaine/stock/")
public class InventaireAction extends ActionBase {
	@Inject
	private IValTypeEnumService valEnumService;
	@Inject
	private IEmplacementService emplacementService;
	@Inject
	private IEmployeService employeService;
	@Inject
	private IFamilleService familleService;
	@Inject
	private IInventaireService inventaireService;
	@Inject
	private IMouvementService mouvementService;
	
	private final static Logger LOGGER = Logger.getLogger(InventaireAction.class);
	
	public void work_init(ActionUtil httpUtil){
		httpUtil.setMenuAttribute("IS_SUB_ADD", "stock.inventaire.work_init_create");

		if(httpUtil.getParameter("tp") != null){
			httpUtil.setMenuAttribute("tp", httpUtil.getParameter("tp"));
		}
		
		List<EmplacementPersistant> listEmplacement = emplacementService.getEmplacementsInternes();
		httpUtil.setRequestAttribute("listEmplacement", listEmplacement);
		
		List<EmployeBean> listEmploye=employeService.findAll(Order.asc("nom"));
		httpUtil.setRequestAttribute("listEmploye", listEmploye);
		
		List<?> listFamille = familleService.getListeFamille("ST", false, false);
		httpUtil.setRequestAttribute("listFamille", listFamille);
		
		List<ValTypeEnumBean> listeType = valEnumService.getListValeursByType(ModelConstante.ENUM_TYPE_INVENTAIRE);
		httpUtil.setRequestAttribute("typeEnumId", valEnumService.getOneByField(TypeEnumPersistant.class, "code", ModelConstante.ENUM_TYPE_INVENTAIRE).getId());

		httpUtil.setRequestAttribute("listeType", listeType);
		
		List<ValTypeEnumBean> listValeurs = valEnumService.getListValeursByType(ModelConstante.ENUM_UNITE);
		httpUtil.setRequestAttribute("listeUnite", listValeurs);
		
		Long emplacementId = null;
		Long inventaireId = httpUtil.getWorkIdLong();
		if(StringUtil.isNotEmpty(inventaireId)){
			boolean isCorrection = httpUtil.getParameter("is_corr") != null;
			InventaireBean inventaireBean = inventaireService.findById(inventaireId);
			emplacementId = inventaireBean.getOpc_emplacement().getId();
			if(!isCorrection && inventaireBean.getIs_valid() != null && inventaireBean.getIs_valid() && httpUtil.isEditionPage()){
				httpUtil.setFormReadOnly();
			}
		} else{
			emplacementId = httpUtil.getLongParameter("inventaire.opc_emplacement.id");
		}
		httpUtil.setRequestAttribute("currEmplacementId", emplacementId);
		
		if(httpUtil.getAction().equals(ActionConstante.FIND)){
			httpUtil.removeMenuAttribute("MODE_CORECT");
		}
		//
		if(httpUtil.isCreateAction()){
			httpUtil.removeMenuAttribute("mapInventaire");
		}
	}
	
	@Override
	public void work_init_create(ActionUtil httpUtil) {
//		boolean isCuisine = "CUIS".equals(httpUtil.getMenuAttribute("tp"));
//		if(isCuisine){
//			loadFamilleEmplacement(httpUtil);
//			httpUtil.setDynamicUrl("/domaine/stock/inventaire_cuisine_edit.jsp");
//		} else{
			httpUtil.setDynamicUrl("/domaine/stock/inventaire_edit.jsp");
//		}
	}
	
	/**
	 * 
	 */
	@Override
	public void work_merge(ActionUtil httpUtil) {
		List<InventaireDetailPersistant> list_detail = new ArrayList<>();
		boolean isCorrection = StringUtil.isTrue(""+httpUtil.getMenuAttribute("MODE_CORECT"));
		boolean isCuisine = "CUIS".equals(httpUtil.getMenuAttribute("tp"));
		Map<Long, InventaireDetailPersistant> mapInventaire = (Map<Long, InventaireDetailPersistant>)httpUtil.getMenuAttribute("mapInventaire");
		//
		if(mapInventaire == null || mapInventaire.size() == 0){
			MessageService.addGrowlMessage("", "Aucun article saisi dans l'inventaire");
			return;
		} else{
			for(Long key : mapInventaire.keySet()){
				list_detail.add(mapInventaire.get(key));
			}
			InventaireBean inventaireBean = (InventaireBean) httpUtil.getViewBean();
			inventaireBean.setId(httpUtil.getWorkIdLong());
			//
			//A TRAITER REFACTO
//			if(isCuisine){
//				inventaireBean.setDate_realisation(ContextAppliCaisse.getJourneeBean().getDate_journee());
//				inventaireBean.setOpc_emplacement(ContextAppliCaisse.getCaisseBean().getOpc_stock_cible());
//				inventaireBean.setOpc_responsable(ContextAppli.getUserBean().getOpc_employe());
//				inventaireBean.setOpc_saisisseur(ContextAppli.getUserBean().getOpc_employe());
//				inventaireBean.setOpc_type_enum( valEnumService.getValeurByCode(ModelConstante.ENUM_TYPE_INVENTAIRE.toString(), "JOURNALIER"));
//			}
//			
			if(inventaireBean.getId() == null){
				inventaireBean.setList_detail(list_detail);				
			} else{
				InventaireBean inventaireDB = inventaireService.findById(inventaireBean.getId());
				// Sauvegarder les anciennes quantités
				if(isCorrection){
					for(InventaireDetailPersistant detailDb : inventaireDB.getList_detail()){
						for(InventaireDetailPersistant detailIhm : list_detail){
							detailIhm.setQte_reel_0(detailDb.getQte_reel());
						}
					}
				}
				
				inventaireDB.getList_detail().clear();
				inventaireDB.getList_detail().addAll(list_detail);
				inventaireBean.setList_detail(inventaireDB.getList_detail());
			}
			
			inventaireService.mergeInventaire(inventaireBean, isCuisine);
			
			if(isCuisine){
				httpUtil.setDynamicUrl("caisseInv.inventaire.manage_cuisine_merge");
			} else{
				super.work_find_refresh(httpUtil);
			}
		}
	}

	/**
	 * @param httpUtil
	 */
	@Override
	public void work_init_update(ActionUtil httpUtil) {
		boolean isCorrection = httpUtil.getParameter("is_corr") != null;
		httpUtil.setMenuAttribute("MODE_CORECT", isCorrection);
//		Long invId = httpUtil.getWorkIdLong();
		Long empId = null;
		super.work_init_update(httpUtil);
//		String env = (String)httpUtil.getUserAttribute("CURRENT_ENV");
		
		//A TRAITER REFACTO
//		if(invId != null && env.equals(ContextAppli.APPLI_ENV.cuis.toString())) {
//			empId = inventaireService.findById(invId).getOpc_emplacement().getId();
//		} else {
			empId = ((InventaireBean)httpUtil.getViewBean()).getOpc_emplacement().getId();
//		}
		//
		loadFamilleEmplacement(httpUtil, empId);
		loadInventaireBean(httpUtil);
		
		//A TRAITER REFACTO
//		if(invId != null && env.equals(ContextAppli.APPLI_ENV.cuis.toString())) {
//			httpUtil.setDynamicUrl("/domaine/stock/inventaire_cuisine_edit.jsp");
//		}
	}
	
	/**
	 * @param httpUtil
	 */
	@Override
	public void work_edit(ActionUtil httpUtil){
		super.work_edit(httpUtil);
		//
		loadFamilleEmplacement(httpUtil, ((InventaireBean)httpUtil.getViewBean()).getOpc_emplacement().getId());
		loadInventaireBean(httpUtil);
	}
	
	/**
	 * @param httpUtil
	 */
	private void loadInventaireBean(ActionUtil httpUtil){
		Map<Long, InventaireDetailPersistant> mapInventaire = new LinkedHashMap<Long, InventaireDetailPersistant>();
		InventaireBean inventaireBean = (InventaireBean) httpUtil.getViewBean();
		List<InventaireDetailPersistant> list_detail = inventaireBean.getList_detail();
		
		Long uniteIdFilter = httpUtil.getLongParameter("unite_fltr");
		httpUtil.setRequestAttribute("uniteIdFilter", uniteIdFilter);
		//
		
		Map<Long, List<FamillePersistant>> mapHistoFam = new HashMap<>();
		for (InventaireDetailPersistant inventaireDetailP : list_detail) {
			ArticlePersistant articlePers = familleService.findById(ArticlePersistant.class, inventaireDetailP.getOpc_article().getId());
			
			if(!(uniteIdFilter == null || 
					((articlePers.getOpc_unite_achat_enum() != null && articlePers.getOpc_unite_achat_enum().getId().equals(uniteIdFilter))
							|| (articlePers.getOpc_unite_vente_enum() != null && articlePers.getOpc_unite_vente_enum().getId().equals(uniteIdFilter))))){
				continue;
				
			}
			
			Long familleId = articlePers.getOpc_famille_stock().getId();
			
			List<FamillePersistant> familleStr = mapHistoFam.get(familleId);
			if(familleStr == null) {
				familleStr = familleService.getFamilleParent("ST", familleId);
				mapHistoFam.put(familleId, familleStr);
			}
			//
			inventaireDetailP.setFamilleStr(familleStr);
			inventaireDetailP.setArticle_lib(articlePers.getCode()+"-"+articlePers.getLibelle());
			//
			mapInventaire.put(inventaireDetailP.getOpc_article().getId(), inventaireDetailP);
		}
		//
		httpUtil.setMenuAttribute("mapInventaire", mapInventaire);
		httpUtil.setRequestAttribute("mapInventaire", mapInventaire);
	}
	
	/**
	 * @param httpUtil
	 */
	public void loadFamilleEmplacement(ActionUtil httpUtil){
		httpUtil.setDynamicUrl("/domaine/stock/inventaire_famille_emplacement.jsp");
		
		String emplId = EncryptionUtil.decrypt(httpUtil.getParameter("empl"));
		//
//		boolean isCuisine = "CUIS".equals(httpUtil.getMenuAttribute("tp"));
//		if(isCuisine){
//			emplId = ContextAppliCaisse.getCaisseBean().getOpc_stock_cible().getId().toString();
//		}
		
		if(StringUtil.isEmpty(emplId)){
			return;
		}
		Long emplacementId = Long.valueOf(emplId);
		// Controler inventaire non validé
		List<InventairePersistant> inventaireNonValide = inventaireService.getInventaireNonValide(emplacementId);
		if(inventaireNonValide.size()>0){
			//
//			if(isCuisine){
//				MessageService.addGrowlMessage("", "Un inventaire <b>non validé</b> est lié à cet emplacement. Veuillez le valider avant de continuer.");	
//			} else{
				MessageService.addGrowlMessage("", "Un inventaire <b>non validé</b> est lié à cet emplacement. Veuillez le valider avant de continuer.");
//			}
			return;
		}
		//
		loadFamilleEmplacement(httpUtil, emplacementId);
	}

	/**
	 * @param httpUtil
	 * @param emplacementId
	 */
	private void loadFamilleEmplacement(ActionUtil httpUtil, Long emplacementId) {
		EmplacementBean emplBean = emplacementService.findById(emplacementId);
		
		List<FamillePersistant> listFamille = null;
		String[] familles = StringUtil.getArrayFromStringDelim(emplBean.getFamilles_cmd(), ";");
		if(familles == null || familles.length == 0){
			listFamille = familleService.getListeFamille("ST", false, true);
		} else{
			List<Long> ids = new ArrayList<>();
			for(String id : familles){
				if(StringUtil.isEmpty(id)){
					continue;
				}
				Long idFamLong = Long.valueOf(id);
				ids.add(idFamLong);
				List<FamillePersistant> listEnfants = familleService.getFamilleEnfants("ST", idFamLong, true);
				for (FamillePersistant famillePersistant : listEnfants) {
					ids.add(famillePersistant.getId());
				}
			}
			listFamille = familleService.getListeFamille(ids, true);
		}
		// Exclude familles
		removeExcludedFamille(null, listFamille, emplBean);
		
		httpUtil.setRequestAttribute("listFamilleEmpl", listFamille);
	}
	
	private void removeExcludedFamille(Set<Long> listFamilleIds, List<FamillePersistant> listFamille, EmplacementBean emplBean){
		// Exclude familles
		List<Long> idsExclude = new ArrayList<>();
		String[] famillesEx = StringUtil.getArrayFromStringDelim(emplBean.getFamilles_ex_cmd(), ";");
		if(famillesEx != null){
			for(String id : famillesEx){
				if(StringUtil.isEmpty(id)){
					continue;
				}
				Long idFamLong = Long.valueOf(id);
				idsExclude.add(idFamLong);
				List<FamillePersistant> listEnfants = familleService.getFamilleEnfants("ST", idFamLong, true);
				for (FamillePersistant famillePersistant : listEnfants) {
					idsExclude.add(famillePersistant.getId());
				}
			}
		}
		
		if(listFamilleIds == null){
			for (Iterator<FamillePersistant> iterator = listFamille.iterator(); iterator.hasNext();) {
				FamillePersistant famP = iterator.next();
				if(idsExclude.contains(famP.getId())){
					iterator.remove();
				}
			}
		} else{
			for (Iterator<Long> iterator = listFamilleIds.iterator(); iterator.hasNext();) {
				Long famId = iterator.next();
				if(idsExclude.contains(famId)){
					iterator.remove();
				}
			}
		}
	}

	/**
	 * @param httpUtil
	 * @param isEdit
	 */
	public void loadSaisieInventaire(ActionUtil httpUtil){
		//boolean isCuisine = "CUIS".equals(httpUtil.getMenuAttribute("tp"));
//		Long emplacementId = null;
		String artInvFilter = httpUtil.getParameter("artInv");
		
		if(StringUtil.isNotEmpty(artInvFilter)) {
			artInvFilter = artInvFilter.toLowerCase();
			List<ArticleStockInfoPersistant> listInvDetTmp = new ArrayList<>();
			List<ArticleStockInfoPersistant> listInvDet = (List<ArticleStockInfoPersistant>) httpUtil.getMenuAttribute("inventaireDtail");
			for (ArticleStockInfoPersistant articleStockInfoP : listInvDet) {
				ArticlePersistant opc_article = articleStockInfoP.getOpc_article();
				if(opc_article.getCode().toLowerCase().startsWith(artInvFilter)
					|| opc_article.getLibelle().toLowerCase().startsWith(artInvFilter)
					|| opc_article.getCode_barre().toLowerCase().startsWith(artInvFilter)){
					listInvDetTmp.add(articleStockInfoP);
				}
			}
			httpUtil.setRequestAttribute("artInvFilter", artInvFilter);
			httpUtil.setRequestAttribute("inventaireDtail", listInvDetTmp);
			httpUtil.setRequestAttribute("mapInventaire", httpUtil.getMenuAttribute("mapInventaire"));
			
			httpUtil.setDynamicUrl("/domaine/stock/inventaire_saisie_popup.jsp");
			return;
		}
		
		//
//		if(isCuisine){
//			emplacementId = ContextAppliCaisse.getCaisseBean().getOpc_stock_cible().getId();
//		} else{
		
//		}

		if(StringUtil.isNotEmpty(httpUtil.getParameter("fam"))) {
			httpUtil.setMenuAttribute("famInvId", Long.valueOf(EncryptionUtil.decrypt(httpUtil.getParameter("fam"))));
		}
		if(StringUtil.isNotEmpty(httpUtil.getParameter("empl"))) {
			httpUtil.setMenuAttribute("emplInvId", Long.valueOf(EncryptionUtil.decrypt(httpUtil.getParameter("empl"))));
		}
		
		Long familleId = (Long)httpUtil.getMenuAttribute(("famInvId"));
		Long emplacementId = (Long)httpUtil.getMenuAttribute("emplInvId");
		
		EmplacementBean emplBean = emplacementService.findById(emplacementId);
		Set<Long> familleIdsAll = new HashSet<Long>();

		List<Long> ids = new ArrayList<>();
		String[] articles = StringUtil.getArrayFromStringDelim(emplBean.getArticles_cmd(), ";");
		if(articles != null && articles.length > 0){
			for(String id : articles){
				ids.add(Long.valueOf(id));
			}
		}
		
		//---------------------------- Si click root --------------------
		boolean isRootFam = familleId != null && familleService.getFamilleRoot("ST").getId().equals(familleId);
		Set<Long> idsFam = new HashSet<>();
		if(isRootFam){
			String[] familles = StringUtil.getArrayFromStringDelim(emplBean.getFamilles_cmd(), ";");
			if(familles != null && familles.length > 0){
				for(String id : familles){
					if(StringUtil.isEmpty(id)){
						continue;
					}
					Long idFamLong = Long.valueOf(id);
					idsFam.add(idFamLong);
					List<FamillePersistant> listEnfants = familleService.getFamilleEnfants("ST", idFamLong, true);
					for (FamillePersistant famillePersistant : listEnfants) {
						if(!idsFam.contains(famillePersistant.getId())){
							idsFam.add(famillePersistant.getId());
						}
					}
				}
			}
		}
		
		if(!isRootFam){
			familleIdsAll.add(familleId);
		}
		List<FamillePersistant> familleAll = familleService.getFamilleEnfants("ST", familleId, true);
		for (FamillePersistant famillePersistant : familleAll) {
			if(!isRootFam || idsFam.contains(famillePersistant.getId())){ 
				familleIdsAll.add(famillePersistant.getId());
			}
		}
		
		// Exclude
		removeExcludedFamille(familleIdsAll, null, emplBean);
		//---------------------------------------------------------------
		
		// Exclude
		Set<Long> articleIdsEx = new HashSet<>();
		String[] articlesEx = StringUtil.getArrayFromStringDelim(emplBean.getArticles_ex_cmd(), ";");
		if(articlesEx != null && articlesEx.length > 0){
			for(String id : articlesEx){
				articleIdsEx.add(Long.valueOf(id));
			}
		}
		
		List<ArticleStockInfoPersistant> listArticleView = inventaireService.getArticleInventaireByEmplacementAndFamille(emplacementId, familleIdsAll, ids, articleIdsEx);
		
//		for (ArticleStockInfoPersistant articlVP : listArticleView) {
//			List<FamillePersistant> familleStr = familleService.getFamilleParent("ST", articlVP.getOpc_article().getOpc_famille_stock().getId());
//			articlVP.getOpc_article().setFamilleStr(familleStr);
//		}
		
		httpUtil.setRequestAttribute("inventaireDtail", listArticleView);
		httpUtil.setMenuAttribute("inventaireDtail", listArticleView);
		httpUtil.setRequestAttribute("mapInventaire", httpUtil.getMenuAttribute("mapInventaire"));
		
		httpUtil.setDynamicUrl("/domaine/stock/inventaire_saisie_popup.jsp");
	}
	
	/**
	 * Sauvegarde temporaires
	 * @param httpUtil
	 */
	@SuppressWarnings("unchecked")
	public void saveSaisieInventaire(ActionUtil httpUtil){
		Map<Long, InventaireDetailPersistant> mapInventaire = (Map<Long, InventaireDetailPersistant>)httpUtil.getMenuAttribute("mapInventaire");
		if(mapInventaire == null){
			mapInventaire = new LinkedHashMap<Long, InventaireDetailPersistant>();
			httpUtil.setMenuAttribute("mapInventaire", mapInventaire);
		}
		
		List<InventaireDetailPersistant> listInventaireDetIHM = (List<InventaireDetailPersistant>) httpUtil.buildListBeanFromMap("opc_article.id",
				InventaireDetailPersistant.class,"eaiid", "opc_article.id", "article_lib", "qte_theorique", "qte_reel", "motif_ecart", "famille_bleft");
		
		// Purger celles qui n'existent plus -------------------------------------
		for (InventaireDetailPersistant inventaireDetailP : listInventaireDetIHM) {
			if(inventaireDetailP.getQte_reel() == null){
				mapInventaire.remove(inventaireDetailP.getOpc_article().getId());
			}
		}
		// ------------------------------------------------------------------------
		
		//
		for (InventaireDetailPersistant inventaireDetailP : listInventaireDetIHM) {
			if(inventaireDetailP.getQte_reel() == null){
				continue;
			}
			
			Long familleId = ((ArticlePersistant)familleService.findById(ArticlePersistant.class, inventaireDetailP.getOpc_article().getId())).getOpc_famille_stock().getId();
			List<FamillePersistant> familleStr = familleService.getFamilleParent("ST", familleId);
			inventaireDetailP.setFamilleStr(familleStr);
			// Calcul ecart et pourcentage
			BigDecimal qteEcart = BigDecimalUtil.substract(inventaireDetailP.getQte_reel(), inventaireDetailP.getQte_theorique());
			inventaireDetailP.setQte_ecart(qteEcart);
			
			BigDecimal qteTheorique = BigDecimalUtil.get(1);
			if(!BigDecimalUtil.isZero(inventaireDetailP.getQte_theorique())){
				qteTheorique = inventaireDetailP.getQte_theorique();
			}
			inventaireDetailP.setPourcent_ecart(BigDecimalUtil.divide(BigDecimalUtil.multiply(qteEcart, BigDecimalUtil.get(100)), qteTheorique));
			
			mapInventaire.put(inventaireDetailP.getOpc_article().getId(), inventaireDetailP);
		}
		
		// Tri par ordre b_left
		List<Map.Entry<Long, InventaireDetailPersistant>> entryList = new ArrayList<Map.Entry<Long, InventaireDetailPersistant>>(mapInventaire.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<Long, InventaireDetailPersistant>>() {
            	@Override
            	public int compare(Map.Entry<Long, InventaireDetailPersistant> invEntry, Map.Entry<Long, InventaireDetailPersistant> invEntry2) {
                	return invEntry.getValue().getFamille_bleft().compareTo(invEntry2.getValue().getFamille_bleft());
            	}
        	}
        );
		
        mapInventaire.clear();
        for (Entry<Long, InventaireDetailPersistant> entry : entryList) {
        	mapInventaire.put(entry.getKey(), entry.getValue());
		}
        
		httpUtil.setRequestAttribute("mapInventaire", mapInventaire);
		httpUtil.setDynamicUrl("/domaine/stock/inventaire_recap.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void valider_inventaire(ActionUtil httpUtil){
		Long inventaireId = httpUtil.getWorkIdLong();
		MouvementPersistant mvmInventaire = inventaireService.validerInventaire(inventaireId);
		
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
	            	LOGGER.error(e);
				}
               return null;
           }
       });
	    // --------------------------------------------------------------------------------------------------------------
	        
		super.work_find(httpUtil);
	}
	
	/**
	 * @param httpUtil
	 */
	public void annuler_validation(ActionUtil httpUtil){
		Long inventaireId = httpUtil.getWorkIdLong();
		MouvementPersistant mvmInventaire = inventaireService.annulerValidation(inventaireId);
		Long destId = mvmInventaire.getOpc_destination()!=null?mvmInventaire.getOpc_destination().getId() : null;
		Long emplId = mvmInventaire.getOpc_emplacement()!=null?mvmInventaire.getOpc_emplacement().getId():null;
		
		List<MouvementArticlePersistant> listArticle = mvmInventaire.getList_article();
		// --------------------------------------------------------------------------------------------------------------
	   	 ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor)ServiceUtil.getBusinessBean("taskExecutor");
	        taskExecutor.submit(new Callable() {
           public Object call() throws Exception {
               //  Création mouvement
        	   try{
        		   mouvementService.majQteStockArticle(emplId, destId, listArticle);
        	   } catch(Exception e){
					e.printStackTrace();
	            	LOGGER.error(e);
				}
               return null;
           }
       });
	    // --------------------------------------------------------------------------------------------------------------
	        
		super.work_find(httpUtil);
	}
	
	
	public void create_pdfInventaire(ActionUtil httpUtil){
		httpUtil.setDynamicUrl("/domaine/stock/inventaire_pdf.jsp");
	}
	
	/**
	 * @param httpUtil
	 * @throws IOException
	 * @throws DocumentException
	 */
	public void downloadInventairePDF(ActionUtil httpUtil) throws IOException, DocumentException{
		String fileName = "fiche_inventaire";
		String[] familles = httpUtil.getRequest().getParameterValues("familles");
		Long emplacementId = httpUtil.getLongParameter("emplacement.id");
		
		Set<Long> familleIdsAll = new HashSet<Long>();
		Set<Long> articleIds = new HashSet<>();
		Set<Long> articleIdsEx = new HashSet<>();
		//
		if(familles != null){
			for (String famIdStr : familles) {
				if(StringUtil.isEmpty(famIdStr)){
					continue;
				}
				familleIdsAll.add(Long.valueOf(famIdStr));
				
				List<FamillePersistant> familleAll = familleService.getFamilleEnfants("ST", Long.valueOf(famIdStr), true);
				for (FamillePersistant famillePersistant : familleAll) {
					familleIdsAll.add(famillePersistant.getId());
				}
			}
		} else if(emplacementId != null){
			EmplacementBean emplBean = emplacementService.findById(emplacementId);
			String[] famillesEmpl = StringUtil.getArrayFromStringDelim(emplBean.getFamilles_cmd(), ";");
			if(famillesEmpl != null && famillesEmpl.length > 0){
				for(String id : famillesEmpl){
					if(StringUtil.isEmpty(id)){
						continue;
					}
					Long idFamLong = Long.valueOf(id);
					familleIdsAll.add(idFamLong);
					List<FamillePersistant> listEnfants = familleService.getFamilleEnfants("ST", idFamLong, true);
					for (FamillePersistant famillePersistant : listEnfants) {
						familleIdsAll.add(famillePersistant.getId());
					}
				}
			}
			// Exclude familles
			removeExcludedFamille(familleIdsAll, null, emplBean);
			
			String[] articles = StringUtil.getArrayFromStringDelim(emplBean.getArticles_cmd(), ";");
			if(articles != null && articles.length > 0){
				for(String id : articles){
					articleIds.add(Long.valueOf(id));
				}
			}
			// Exclude
			String[] articlesEx = StringUtil.getArrayFromStringDelim(emplBean.getArticles_ex_cmd(), ";");
			if(articlesEx != null && articlesEx.length > 0){
				for(String id : articlesEx){
					articleIdsEx.add(Long.valueOf(id));
				}
			}
		}
		
		List<ArticlePersistant> listArticleView = inventaireService.getArticleByFamille(familleIdsAll, articleIds, articleIdsEx, true);
		
		for (ArticlePersistant articlVP : listArticleView) {
			List<FamillePersistant> familleStr = familleService.getFamilleParent("ST", articlVP.getOpc_famille_stock().getId());
			articlVP.setFamilleStr(familleStr);
		}
		httpUtil.doDownload(FicheInventairePDF.createPdf(fileName, listArticleView).getPdfFile(), true);
	}
	
	/**
	 * @param httpUtil
	 * @throws IOException
	 * @throws DocumentException
	 */
	public void downloadInventaireEcartPDF(ActionUtil httpUtil) throws IOException, DocumentException{
		String fileName = "fiche_ecart_inventaire";
		Long invId = httpUtil.getWorkIdLong();
		List<InventaireDetailPersistant> listArticleView = inventaireService.getInventaireEcart(invId);
		InventairePersistant inv = inventaireService.findById(invId);
		//
		for (InventaireDetailPersistant invDetP : listArticleView) {
			List<FamillePersistant> familleStr = familleService.getFamilleParent("ST", invDetP.getOpc_article().getOpc_famille_stock().getId());
			invDetP.setFamilleStr(familleStr);
		}
		httpUtil.doDownload(FicheInventaireEcartPDF.createPdf(fileName, inv, listArticleView).getPdfFile(), true);
	}
	
	/**
	 * @param httpUtil
	 */
	public void find_synth_inventaire(ActionUtil httpUtil) {
		String[][] listeType = new String[][]{{"U", "ARTICLE UNITAIRE"}, {"NU", "ARTICLE NON UNITAIRE"}, {"T", "SÉLÉCTIONNER TOUT"}};
		
		String uniteFilter = "";
		String unite = httpUtil.getParameter("typeUnite");
		
		if(StringUtil.isNotEmpty(unite)){
			if("U".equals(unite)) {
				uniteFilter = "'P', 'B'";
			} else if("NU".equals(unite)) {
				uniteFilter = "'KG', 'G', 'ML', 'L'";
			} 
		}
		
		Long inventaire_id = httpUtil.getWorkIdLong();
		if(inventaire_id == null) {
			inventaire_id = (Long)httpUtil.getMenuAttribute("inventaireId");
		} else {
			httpUtil.setMenuAttribute("inventaireId", inventaire_id);
		}
		
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_synthese_inv");
		cplxTable.getFormBean().getFormCriterion().put("inventaireId", inventaire_id);
		
		String req = "from InventaireDetailPersistant inventaireDetail "
		+ "where inventaireDetail.qte_ecart != 0 "
		+ "and inventaireDetail.opc_inventaire.id='{inventaireId}' " 
		+ (StringUtil.isNotEmpty(uniteFilter) ? " and inventaireDetail.opc_article.opc_unite_achat_enum.code in ("+uniteFilter+") " : "") 
		+ " order by inventaireDetail.opc_article.opc_famille_stock.b_left";
		
		List<InventaireDetailPersistant> listData = (List<InventaireDetailPersistant>) inventaireService.findByCriteria(cplxTable, req);
		//
		httpUtil.setRequestAttribute("list_synthese_inv", listData);
		httpUtil.setRequestAttribute("listeType", listeType);

		httpUtil.setDynamicUrl("/domaine/stock/inventaire_synthese.jsp");
	}

	/**
	 * @param httpUtil
	 */
	public void work_post(ActionUtil httpUtil){
		List<EmplacementPersistant> listEmplacement = (List<EmplacementPersistant>)emplacementService.findAll(EmplacementPersistant.class);
		// Une date par emplacement
		for (EmplacementPersistant emplacementBean : listEmplacement) {
			Date maxDateInventaire = inventaireService.getMaxDateInventaireValide(emplacementBean.getId());
			httpUtil.setRequestAttribute("dateMaxInven_"+emplacementBean.getId(), maxDateInventaire);
		}
		
//		httpUtil.setRequestAttribute("isInvetaireNonValide", inventaireService.getInventaireNonValide().size()>0);
	}
}
