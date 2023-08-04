package appli.controller.domaine.stock.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.hibernate.criterion.Order;

import appli.controller.domaine.stock.bean.MouvementBean;
import appli.model.domaine.administration.service.IEtatFinanceService;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.persistant.EmplacementPersistant;
import appli.model.domaine.stock.persistant.MouvementArticlePersistant;
import appli.model.domaine.stock.persistant.MouvementPersistant;
import appli.model.domaine.stock.persistant.PreparationTransfoArticlePersistant;
import appli.model.domaine.stock.persistant.PreparationTransfoPersistant;
import appli.model.domaine.stock.service.IArticleService;
import appli.model.domaine.stock.service.IEmplacementService;
import appli.model.domaine.stock.service.IInventaireService;
import appli.model.domaine.stock.service.IMouvementService;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.FrontFilter;
import framework.controller.annotation.WorkController;
import framework.controller.annotation.WorkForward;
import framework.model.common.constante.ActionConstante;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.DateUtil.TIME_ENUM;
import framework.model.common.util.NumericUtil;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.StringUtil;

@WorkController(nameSpace = "stock", bean = MouvementBean.class, jspRootPath = "/domaine/stock/")
public class TransformationAction extends ActionBase {
	@Inject
	private IEmplacementService emplacementService;
	@Inject
	private IMouvementService mouvementService;
	@Inject
	private IArticleService articleService;
	@Inject 
	private IEtatFinanceService etatFinancierService;
	@Inject 
	private IInventaireService inventaireService;
	
	/**
	 * @param httpUtil
	 */
	public void work_init(ActionUtil httpUtil) {
		if(httpUtil.isEditionPage()){
			List<EmplacementPersistant> listEmplacement = emplacementService.getEmplacementsInternes();
			 httpUtil.setRequestAttribute("listeDestination", listEmplacement);
			 
			 httpUtil.setRequestAttribute("type_transformation", new String[][]{{"L", "Composants"}, {"P", "Préparation"}/*, {"D", "Décomposition"}*/});
			 
			 List<ArticlePersistant> listComposant = articleService.getListArticleStock(true);
			 List<ArticlePersistant> listComposantSortie = articleService.getListArticleNetAndStock(true);
			 
			 httpUtil.setRequestAttribute("listComposant", listComposant);
			 httpUtil.setRequestAttribute("listComposantSortie", listComposantSortie);
		}
		
		httpUtil.setMenuAttribute("type_mvmnt", "tr");
	}
	
	@Override
	public void work_find(ActionUtil httpUtil) {
		httpUtil.setRequestAttribute("listEmplacement", mouvementService.findAll(EmplacementPersistant.class, Order.asc("titre")));
		
		String type = (String) httpUtil.getMenuAttribute("type_mvmnt");
		// Ajouter le paramétre dans la requête
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_mouvement");
		Map<String, Object> formCriterion = cplxTable.getFormBean().getFormCriterion();
		formCriterion.put("type", type);
		boolean isFilterAct = StringUtil.isTrue(httpUtil.getRequest().getParameter("is_filter_act"));
		//----------------------------- Date -------------------------
		Date dateDebut = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateDebut"));
		Date dateFin = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateFin"));
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
		
		if(!isFilterAct){
			formCriterion.put("dateDebut", dateDebut);
			formCriterion.put("dateFin", dateFin);
		} else{
			formCriterion.remove("dateDebut");
			formCriterion.remove("dateFin");
		}
		//-----------------------------------------------------------
		
		List<MouvementPersistant> listData = (List<MouvementPersistant>) mouvementService.findByCriteriaByQueryId(cplxTable, "mouvement_transfo_find");
		List<MouvementPersistant> listData2 = new ArrayList<>();
		// Completer les infos destination
		for (MouvementPersistant mouvementPersistant : listData) {
			MouvementPersistant mvmClone = (MouvementPersistant)ReflectUtil.cloneBean(mouvementPersistant);
			mvmClone.setOpc_destination(mouvementPersistant.getOpc_mouvement().getOpc_destination());
			listData2.add(mvmClone);
		}
		httpUtil.setRequestAttribute("list_mouvement", listData2);
	   	//
		httpUtil.setDynamicUrl("/domaine/stock/transformation_list.jsp");
	}

	/**
	 * 
	 */
	@Override
	public void work_update(ActionUtil httpUtil) {
		String type = httpUtil.getParameter("type_transformation");
		MouvementBean mouvementBean = setDataList(httpUtil);
		
		if(type.equals("L")){
			if(mouvementBean.getListArtTarget().size() == 0){
				MessageService.addBannerMessage("Au moins un composant destination doit être ajouté.");
				return;
			}
			if(mouvementBean.getList_article().size() == 0){
				MessageService.addBannerMessage("Veuillez ajouter au moins un article.");
				return;
			}
		} else{
			if(mouvementBean.getList_article().size() == 0){
				MessageService.addBannerMessage("Veuillez séléctionner une préparation.");
				return;
			}
		}
		//
		mouvementService.mergeTransformation(mouvementBean);
		
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "", "La transformation est mise à jour.");
		
		// Gérer le back
		FrontFilter.restaureBackParams(httpUtil.getRequest(), null, true);
		
		work_find(httpUtil);
	}
	
	/**
	 * 
	 */
	@Override
	@WorkForward(useFormValidator=true)
	public void work_create(ActionUtil httpUtil) {
		String type = httpUtil.getParameter("type_transformation");
		// Data formulaire
		MouvementBean mouvementBean = setDataList(httpUtil);
		
		if(type.equals("L")){
			if(mouvementBean.getListArtTarget().size() == 0){
				MessageService.addBannerMessage("Au moins un composant destination doit être ajouté.");
				return;
			}
			if(mouvementBean.getList_article().size() == 0){
				MessageService.addBannerMessage("Veuillez ajouter au moins un article.");
				return;
			}
		} else{
			if(mouvementBean.getList_article().size() == 0){
				MessageService.addBannerMessage("Veuillez séléctionner une préparation.");
				return;
			}
		}
		//
		mouvementService.mergeTransformation(mouvementBean);
		
		// Gérer le back
		FrontFilter.restaureBackParams(httpUtil.getRequest(), null, true);
		
		work_find(httpUtil);
	}
	
	public void refreshMode(ActionUtil httpUtil) {
		String type = httpUtil.getParameter("type_transformation");
		
		httpUtil.setRequestAttribute("listPrepTransfo", emplacementService.findAll(PreparationTransfoPersistant.class, Order.asc("libelle")));
		
		httpUtil.setRequestAttribute("curr_type_transfo", type);
		httpUtil.setDynamicUrl("/domaine/stock/transformation_detart_include.jsp");
	}
	
	@Override
	public void work_delete(ActionUtil httpUtil) {
		super.work_delete(httpUtil);
	}

	/**
	 * @param httpUtil
	 */
	@SuppressWarnings({ "unchecked" })
	private MouvementBean setDataList(ActionUtil httpUtil) {
		String type = httpUtil.getParameter("type_transformation");
		MouvementBean mouvementBean = (MouvementBean) httpUtil.getViewBean();
		mouvementBean.setId(httpUtil.getWorkIdLong());
		
		List<MouvementArticlePersistant> listArticle = new ArrayList<>();
		List<MouvementArticlePersistant> listArticleTarget = new ArrayList<>();
		
		if(mouvementBean.getId() != null) {
			Map<Long, BigDecimal> mapOldArticleInfo = new HashMap<>();
			//
			MouvementBean mvmDb = mouvementService.findById(mouvementBean.getId());
			listArticle = mvmDb.getList_article();
			//
			for (MouvementArticlePersistant mvmArtP : listArticle) {
				mapOldArticleInfo.put(mvmArtP.getOpc_article().getId(), mvmArtP.getQuantite());
			}
			listArticle.clear();
			mouvementBean.setMapOldArticleInfo(mapOldArticleInfo);
		}
		

		Map<String, Object> params = (Map)httpUtil.getRequest().getAttribute(ProjectConstante.WORK_PARAMS);
		
		if(type.equals("L")){// Si composants libres
			List<MouvementArticlePersistant> listTransfoArticle = (List<MouvementArticlePersistant>) httpUtil.buildListBeanFromMap("opc_article.id",MouvementArticlePersistant.class, "eaiid", "idxIhm",
																"opc_article.id", "quantite");
			// Trier par ihmIDX
			Collections.sort(listTransfoArticle, new SortByIhmIdx());
			listArticle.addAll(listTransfoArticle);
			
			// Composant distination
			for(String key : params.keySet()){
				if(key.startsWith("composant_") && StringUtil.isNotEmpty(params.get(key))){
					String id = key.substring(key.lastIndexOf("_")+1);
					if(id.equals("0") || !NumericUtil.isInt(id)){
						continue;
					}
					Long composantId = httpUtil.getLongParameter("composant_"+id);
					BigDecimal qte = BigDecimalUtil.get(httpUtil.getParameter("quantite_cmp_"+id));
					
					if(composantId == null || BigDecimalUtil.isZero(qte)){
						continue;
					}
					ArticlePersistant artP = (ArticlePersistant)emplacementService.findById(ArticlePersistant.class, composantId);
					MouvementArticlePersistant mvmArt = new MouvementArticlePersistant();
					mvmArt.setOpc_article(artP);
					mvmArt.setQuantite(qte);
					
					listArticleTarget.add(mvmArt);
				}
			}
			// Trier par ihmIDX
			Collections.sort(listArticleTarget, new SortByIhmIdx());
		} else{// Si recette préparées
			for(String key : params.keySet()){
				if(key.startsWith("prep_transfo.id_") && StringUtil.isNotEmpty(params.get(key))){
					String id = key.substring(key.lastIndexOf("_")+1);
					if(id.equals("0") || !NumericUtil.isInt(id)){
						continue;
					}
					Long prepTransfoId = httpUtil.getLongParameter("prep_transfo.id_"+id);
					BigDecimal qte = BigDecimalUtil.get(httpUtil.getParameter("prep_quantite_"+id));
					
					if(prepTransfoId == null || BigDecimalUtil.isZero(qte)){
						continue;
					}
					
					PreparationTransfoPersistant transfoP = (PreparationTransfoPersistant)emplacementService.findById(PreparationTransfoPersistant.class, prepTransfoId);
					int idxIhm = 1;
					for(PreparationTransfoArticlePersistant det : transfoP.getList_composant()){
						MouvementArticlePersistant mvmArt = new MouvementArticlePersistant();
						mvmArt.setOpc_article(det.getOpc_composant());
						mvmArt.setQuantite(BigDecimalUtil.multiply(det.getQuantite(), qte));
						mvmArt.setIdxIhm(idxIhm);
						
						listArticle.add(mvmArt);
						idxIhm++;
					}
					
					// Articles target
					MouvementArticlePersistant mvmArt = new MouvementArticlePersistant();
					mvmArt.setOpc_article(transfoP.getOpc_composant_target().getOpc_composant());
					mvmArt.setQuantite(BigDecimalUtil.multiply(transfoP.getOpc_composant_target().getQuantite(), qte));
					listArticleTarget.add(mvmArt);
				}
			}
		}
		mouvementBean.setList_article(listArticle);
		mouvementBean.setListArtTarget(listArticleTarget);
		
		return mouvementBean;
	}
	
	/**
	 * @param httpUtil
	 */
	public void genererNumBL(ActionUtil httpUtil) {
		String numBl = mouvementService.generateNumBl("tr");
		httpUtil.writeResponse(numBl);
	}
	
	/**
	 * @param httpUtil
	 */
	public void editTrMvm(ActionUtil httpUtil){
		Long mvmId = httpUtil.getLongParameter("art");
		MouvementBean mvmBean = mouvementService.findById(mvmId);
		
		httpUtil.setRequestAttribute("mouvementBean", mvmBean);
		httpUtil.setRequestAttribute("mouvementDestBean", mvmBean.getOpc_mouvement());
		//
		httpUtil.setDynamicUrl("/domaine/stock/transformation_tr_consult.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void work_post(ActionUtil httpUtil) {
		MouvementBean mouvementBean = (MouvementBean)httpUtil.getViewBean();
		if(mouvementBean != null){
			// Si mois clos
			if(mouvementBean.getDate_mouvement() != null && etatFinancierService.isMoisClos(mouvementBean.getDate_mouvement())) {
				MessageService.addBannerMessage(MSG_TYPE.INFO, "Mois clos.");
				httpUtil.setRequestAttribute("is_mois_clos", true);
			}
		}
		
		// La date du mouvement doit ÃªtrpostÃ©rieure Ã  la date du dernier inventaire et le mois ne doit pas être clos
		if(httpUtil.getAction().equals(ActionConstante.INIT_UPDATE)){
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
		
		if(mouvementBean != null){
			httpUtil.setRequestAttribute("mvmDestBean", mouvementService.findById(mouvementBean.getId()).getOpc_mouvement());
		}
	} 	
}

