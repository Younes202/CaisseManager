package appli.controller.domaine.caisse_restau.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import appli.controller.domaine.administration.ParametrageRightsConstantes;
import appli.controller.domaine.administration.ParametrageRightsConstantes.TYPE_PARAM;
import appli.controller.domaine.caisse.bean.CaisseBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_CAISSE_ENUM;
import appli.model.domaine.administration.service.IParametrageService;
import appli.model.domaine.caisse.service.ICaisseService;
import appli.model.domaine.caisse.service.ICaisseWebService;
import appli.model.domaine.caisse.service.IJourneeService;
import appli.model.domaine.caisse.service.IMenuCompositionService;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.stock.service.IFamilleService;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import appli.model.domaine.vente.persistant.CaissePersistant;
import appli.model.domaine.vente.persistant.JourneePersistant;
import appli.model.domaine.vente.persistant.MenuCompositionPersistant;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.controller.annotation.WorkForward;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.service.MessageService;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.DateUtil.TIME_ENUM;
import framework.model.common.util.NumericUtil;
import framework.model.common.util.StringUtil;

@WorkController(nameSpace="caisse_restau", bean=CaisseBean.class, jspRootPath="/domaine/caisse/")
public class CaisseConfigurationRestauAction extends ActionBase {
	@Inject
	private IMenuCompositionService menuService;
	@Inject
	private ICaisseService caisseService;
	@Inject
	private ICaisseWebService caisseWebService;
	@Inject
	private IFamilleService familleService;
	@Inject
	private IJourneeService journeeService;
	@Inject
	private IParametrageService paramService;
	
	public void work_init_update(ActionUtil httpUtil){
	
	}
	
	@WorkForward(bean=CaisseBean.class, useBean=true)
	public void work_update(ActionUtil httpUtil){
		Long caisseId = (Long) httpUtil.getMenuAttribute("caisseId");
		CaisseBean caisseBean = (CaisseBean)httpUtil.getViewBean();
		caisseBean.setId(caisseId);
		
		String menus = "", articles = "", familles = "";
		if(caisseBean.getMenus_array() != null){
			for (String menuId :  caisseBean.getMenus_array()) {
				menus = menus + menuId + ";";
			}
		}
		if(caisseBean.getArticles_array() != null){
			for (String articleId :  caisseBean.getArticles_array()) {
				articles = articles + articleId + ";";
			}
		}
		if(caisseBean.getFamilles_array() != null){
			for (String familleId :  caisseBean.getFamilles_array()) {
				familles = familles + familleId + ";";
			}
			// AJouter les enfants de ces familles
			for (String familleId :  caisseBean.getFamilles_array()) {
				if(StringUtil.isNotEmpty(familleId) && NumericUtil.isNum(familleId)){
					List<FamillePersistant> listFamilleEnfant = familleService.getFamilleEnfants("CU", Long.valueOf(familleId), true);
					for (FamillePersistant familleEnfantP : listFamilleEnfant) {
						familles = familles + familleEnfantP.getId() + ";";
					}
				}
			}
		}
		
		caisseBean.setMenus_cmd(menus);
		caisseBean.setArticles_cmd(articles);
		caisseBean.setFamilles_cmd(familles);
		
		caisseService.mergeCaisseCuisineConfig(caisseBean);

		Map<String, Object> params = httpUtil.getValuesByStartName("param_");
		//
		paramService.updateParams(params, caisseId);
		
		//
		work_edit(httpUtil);
		
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "Mise à jour", "La mise à jour est effectuée avec succès.");
	}
	

	/**
	 * @param httpUtil
	 */
	public void initTransfererCmdCuisine(ActionUtil httpUtil){
		Long mvmId = httpUtil.getWorkIdLong();
		Long currentCaisseId = (Long) httpUtil.getMenuAttribute("caisseId");
		if(currentCaisseId == null){
			currentCaisseId = httpUtil.getLongParameter("caisseId");
			httpUtil.setRequestAttribute("is_synthese", true);
		}
		
		List<CaissePersistant> listCaisse = new ArrayList<>();
		
		for(CaissePersistant caisse : caisseService.getListCaisseCuisineActive()){
			if(currentCaisseId != caisse.getId()){
				listCaisse.add(caisse);
			}
		}
		
		httpUtil.setRequestAttribute("caisseId", currentCaisseId);
		httpUtil.setMenuAttribute("mvmId", mvmId);
		httpUtil.setRequestAttribute("listCaisse", listCaisse);
		
		httpUtil.setDynamicUrl("/domaine/caisse_restau/transfert_cmd_cuisine_popup2.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void transfererCmdCuisine(ActionUtil httpUtil){
		Long mvmId = (Long) httpUtil.getMenuAttribute("mvmId");
		Long caisseId = httpUtil.getLongParameter("caisse");
		Long caisseIdTarget = httpUtil.getLongParameter("caisse_target");
		
		CaisseMouvementPersistant mvmP = (CaisseMouvementPersistant)caisseService.findById(CaisseMouvementPersistant.class, mvmId);
		STATUT_CAISSE_MOUVEMENT_ENUM statut = ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.valueOf(mvmP.getLast_statut());
		//
		caisseService.transfererCmdCuisine(statut, mvmId, null, caisseId, caisseIdTarget);
		//
		
		find_ecrans_cuisine(httpUtil);
	}
	
	/**
	 * 
	 * @param httpUtil
	 */
	public void find_ecrans_cuisine(ActionUtil httpUtil){
		if(httpUtil.getParameter("statut") == null && httpUtil.getParameter("caisse_target") == null){
			httpUtil.setDynamicUrl("/domaine/caisse/normal/caisse_detail_main_list.jsp");	
		} else{
			httpUtil.setDynamicUrl("/domaine/caisse/normal/caisse_detail_list.jsp");
		}
		
		if(httpUtil.getParameter("tp") != null){
			httpUtil.setMenuAttribute("tp", httpUtil.getParameter("tp"));
		}
		JourneePersistant journeeP= journeeService.getLastJournee();
		if(journeeP == null){
			return;
		}
		
		List<CaisseBean> listCaisse = caisseService.getListCaisseCuisineActive();
		String statut = httpUtil.getParameter("statut");
		String prep = ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.PREP.toString();
		String val = ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE.toString();
		
		int idx = 0;
		for (CaissePersistant caissePersistant : listCaisse) {
			String requete = "from CaisseMouvementPersistant caisseMouvement "
				+ "where caisseMouvement.caisse_cuisine like '{caisseId}' "
				+ "and (caisseMouvement.is_annule is null or caisseMouvement.is_annule = 0) "
				+ "and caisseMouvement.opc_caisse_journee.opc_journee.id="+journeeP.getId()+" ";
				
			if(StringUtil.isNotEmpty(statut)){
				if(statut.equals(val)){
					requete = requete + "and (caisseMouvement.last_statut = '"+statut+"') ";
				} else{
					requete = requete + "and caisseMouvement.last_statut = '"+statut+"' ";
				}
			}else{
				requete = requete + "and (caisseMouvement.last_statut = '"+prep+"' or caisseMouvement.last_statut = '"+val+"') ";
			}
			requete = requete + "order by caisseMouvement.last_statut desc, caisseMouvement.id desc";
			RequestTableBean cplxTable = getTableBean(httpUtil, "list_caisse_"+idx);
			
			Map<String, Object> formCriterion = cplxTable.getFormBean().getFormCriterion();
			formCriterion.put("caisseId", "%;"+caissePersistant.getId()+":%");
			
			List<CaisseMouvementPersistant> listData = (List<CaisseMouvementPersistant>) journeeService.findByCriteria(cplxTable, requete, false);
			
			httpUtil.setRequestAttribute("list_mouvement_"+idx, listData);
			idx++;
		}
		
		String[][] typeCmd = {
				{ContextAppli.TYPE_COMMANDE.E.toString(), "A emporter"}, 
				{ContextAppli.TYPE_COMMANDE.P.toString(), "Sur place"}, 
				{ContextAppli.TYPE_COMMANDE.L.toString(), "Livraison"}
			};
		httpUtil.setRequestAttribute("typeCmd", typeCmd);
	
		httpUtil.setRequestAttribute("listStatut", 
					new String[][]{
							{ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE.toString(), ContextAppli.getLibelleStatut(STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE.toString())}, 
							{ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.PREP.toString(), ContextAppli.getLibelleStatut(STATUT_CAISSE_MOUVEMENT_ENUM.PREP.toString())},
							{ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.PRETE.toString(), ContextAppli.getLibelleStatut(STATUT_CAISSE_MOUVEMENT_ENUM.PRETE.toString())},
							{ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.LIVRE.toString(), ContextAppli.getLibelleStatut(STATUT_CAISSE_MOUVEMENT_ENUM.LIVRE.toString())}
					});
		
		String delaisRefresh = "10000";
		if(httpUtil.getParameter("delaisRefresh") != null){
			delaisRefresh = httpUtil.getParameter("delaisRefresh");
		}
		
		httpUtil.setRequestAttribute("list_caisse", listCaisse);
		httpUtil.setRequestAttribute("statut", statut);
		httpUtil.setRequestAttribute("delaisRefresh", delaisRefresh);
	}
	
	public void work_post(ActionUtil httpUtil){
		Long caisseId = (Long)httpUtil.getMenuAttribute("caisseId");
		httpUtil.setRequestAttribute("list_imprimante", ParametrageRightsConstantes.getListPrinters());
		String[][] orientationBalance = {{"L", "Paysage"}, {"LR", "Paysage inversé"}, {"P", "Portrait"}};
	    httpUtil.setRequestAttribute("orientationBalance", orientationBalance);
	    
		if(caisseId != null){
			CaisseBean caisseBean = caisseService.findById(caisseId);
			httpUtil.setViewBean(caisseBean);
			
			TYPE_CAISSE_ENUM typeEcran = TYPE_CAISSE_ENUM.valueOf(caisseBean.getType_ecran());
			httpUtil.setRequestAttribute("caisseId", caisseId);
			
			if(typeEcran.equals(TYPE_CAISSE_ENUM.CUISINE) 
					|| typeEcran.equals(TYPE_CAISSE_ENUM.PILOTAGE)
					|| typeEcran.equals(TYPE_CAISSE_ENUM.PRESENTOIRE)){
				httpUtil.setRequestAttribute("listModeTravail", new String[][]{{"PO", "Imprimantes seulement"}, {"EO", "Ecran seulement"}, {"PE", "Ecran et imprimanate"}});
			}
			
			if(typeEcran.equals(TYPE_CAISSE_ENUM.CUISINE)){
				// Isoler les menus uniquement
				List<MenuCompositionPersistant> listeMenu = new ArrayList<>();
				List<MenuCompositionPersistant> listeMenuAll = menuService.getListeMenu(true, true);
				for (MenuCompositionPersistant menuCompositionPersistant : listeMenuAll) {
					if(BooleanUtil.isTrue(menuCompositionPersistant.getIs_menu())){
						listeMenu.add(menuCompositionPersistant);
					}
				}
				
				httpUtil.setRequestAttribute("listMenus", listeMenu);
				httpUtil.setRequestAttribute("listFamilles", familleService.getListeFamille("CU", true, false));
				httpUtil.setRequestAttribute("listArticles", caisseService.getListArticleCaisseActif());
				
				if(caisseBean != null && BooleanUtil.isTrue(caisseBean.getIs_auto_cmd())) {
					String[] menusArray = StringUtil.getArrayFromStringDelim(caisseBean.getMenus_cmd(), ";");
					String[] articleArray = StringUtil.getArrayFromStringDelim(caisseBean.getArticles_cmd(), ";");
					String[] familleArray = StringUtil.getArrayFromStringDelim(caisseBean.getFamilles_cmd(), ";");
					
		        	caisseBean.setMenus_array(menusArray);
		        	caisseBean.setArticles_array(articleArray);
		        	caisseBean.setFamilles_array(familleArray);
		    	}
			}
			httpUtil.setDynamicUrl("/domaine/caisse_restau/normal/conf_caisse/cuisine.jsp");
			
			if(typeEcran.equals(TYPE_CAISSE_ENUM.PILOTAGE)){
				ParametrageRightsConstantes.loadParamsSpecByGroup(httpUtil, TYPE_PARAM.PILOTAGE_SPEC, caisseId);
				httpUtil.setDynamicUrl("/domaine/caisse_restau/normal/conf_caisse/pilotage.jsp");
			} else if(typeEcran.equals(TYPE_CAISSE_ENUM.PRESENTOIRE)){
				ParametrageRightsConstantes.loadParamsSpecByGroup(httpUtil, TYPE_PARAM.PRESENTOIRE_SPEC, caisseId);
				httpUtil.setDynamicUrl("/domaine/caisse_restau/normal/conf_caisse/presentoire.jsp");
			} else if(typeEcran.equals(TYPE_CAISSE_ENUM.AFFICLIENT)){
				ParametrageRightsConstantes.loadParamsSpecByGroup(httpUtil, TYPE_PARAM.AFFICHEUR_CAISSE_SPEC, caisseId);
				httpUtil.setDynamicUrl("/domaine/caisse_restau/normal/conf_caisse/afficheurclient.jsp");
			}else if(typeEcran.equals(TYPE_CAISSE_ENUM.CAISSE_CLIENT)){
				ParametrageRightsConstantes.loadParamsSpecByGroup(httpUtil, TYPE_PARAM.CAISSE_SALLE_SPEC, caisseId);
				httpUtil.setDynamicUrl("/domaine/caisse_restau/normal/conf_caisse/caisse_client.jsp");
			} else if(typeEcran.equals(TYPE_CAISSE_ENUM.CUISINE)){
				ParametrageRightsConstantes.loadParamsSpecByGroup(httpUtil, TYPE_PARAM.CUISINE_SPEC, caisseId);
				httpUtil.setDynamicUrl("/domaine/caisse_restau/normal/conf_caisse/cuisine.jsp");
			}
		}
	}
	
	/**
	 * @param httpUtil
	 */
	public void majStatutCmd(ActionUtil httpUtil){
		JourneePersistant journeeP = journeeService.getLastJournee();
		Long mvmId = httpUtil.getWorkIdLong();
		Long caisseId = Long.valueOf(httpUtil.getParameter("caisseId"));
		CaissePersistant caisseP = (CaissePersistant)caisseService.findById(CaissePersistant.class, caisseId);
		CaisseMouvementPersistant mvm = (CaisseMouvementPersistant) caisseService.findById(CaisseMouvementPersistant.class, mvmId); 
		Date dtMvm = journeeP.getDate_journee();
		//
		if(dtMvm != null){
			caisseWebService.changeStatut(dtMvm, caisseP, new Long[]{mvmId}, mvm.getLast_statut()/*, true*/);
		}
		//
		find_ecrans_cuisine(httpUtil);
	}
	
	public void load_temps_cuisine(ActionUtil httpUtil){
		Date dateDebut = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateDebut"));
		Date dateFin = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateFin"));
		boolean isDbFin = true;
		if(dateDebut == null) {
			JourneePersistant lastJrn = journeeService.getLastJournee();
			Date dateLast = (lastJrn == null ? new Date() : lastJrn.getDate_journee());
			
			dateDebut = (httpUtil.getMenuAttribute("dateDebut")==null ? dateLast : (Date)httpUtil.getMenuAttribute("dateDebut"));
			dateFin = (httpUtil.getMenuAttribute("dateFin")==null ? dateLast : (Date)httpUtil.getMenuAttribute("dateFin"));
		} else if(httpUtil.getRequest().getParameter("prev") != null) {
			dateDebut = DateUtil.addSubstractDate(dateDebut, TIME_ENUM.DAY, -1);
			dateFin = DateUtil.addSubstractDate(dateFin, TIME_ENUM.DAY, -1);
		} else if(httpUtil.getRequest().getParameter("next") != null) {
			dateDebut = DateUtil.addSubstractDate(dateDebut, TIME_ENUM.DAY, 1);
			dateFin = DateUtil.addSubstractDate(dateFin, TIME_ENUM.DAY, 1);
		} else{
			isDbFin = false;
		}
		if(isDbFin){
			dateFin = dateDebut;
		}
		// Postionner l'heure
		dateDebut = DateUtil.getStartOfDay(dateDebut);
		dateFin = DateUtil.getEndOfDay(dateFin);
		
		httpUtil.setRequestAttribute("dateDebut", dateDebut);
		httpUtil.setRequestAttribute("dateFin", dateFin);
		httpUtil.setMenuAttribute("dateDebut", dateDebut);
		httpUtil.setMenuAttribute("dateFin", dateFin);
		
		//-----------------------------------------------------------
		List<CaissePersistant> listCaisse = caisseService.getListCaisseActive(ContextAppli.TYPE_CAISSE_ENUM.CUISINE.toString(), true);
		List<CaisseMouvementPersistant> listCaisseMouvement = new ArrayList<>();
		List<JourneePersistant> listJournee = journeeService.getListournee(dateDebut, dateFin);
		
		for (JourneePersistant journeeP : listJournee) {
			listCaisseMouvement.addAll(caisseWebService.findAllCuisine(null, null, journeeP.getId(), null, 0, 1000, 
					STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE, 
					STATUT_CAISSE_MOUVEMENT_ENUM.PRETE, 
					STATUT_CAISSE_MOUVEMENT_ENUM.LIVRE));
		}
		httpUtil.setRequestAttribute("listCaisse", listCaisse);
		httpUtil.setRequestAttribute("listMvm", listCaisseMouvement);
		
		httpUtil.setMenuAttribute("tpSuivi", "temps");
	   	
		httpUtil.setDynamicUrl("/domaine/caisse_restau/cuisine/cuisine_time.jsp");
	}
	
	public void check_conf(ActionUtil httpUtil){
		List<CaisseBean> listCuisine = caisseService.getListCaisseCuisineActive();
		httpUtil.setRequestAttribute("listCuisine", listCuisine);
		
		Map<Long, List<List<String>>> mapData = new HashMap<>();
		
		for (CaissePersistant caisseP : listCuisine) {
			if(!BooleanUtil.isTrue(caisseP.getIs_auto_cmd())) {
				continue;
			}
			List<String> listeMenuStr = new ArrayList<>();
			List<String> listeFamilleStr = new ArrayList<>();
			List<String> listeArticleStr = new ArrayList<>();
			
			String[] menusArray = StringUtil.getArrayFromStringDelim(caisseP.getMenus_cmd(), ";");
			if(menusArray != null) {
				for (String mnuId : menusArray) {
					if(StringUtil.isEmpty(mnuId)) {
						continue;
					}
					MenuCompositionPersistant mnuP = familleService.findById(MenuCompositionPersistant.class, Long.valueOf(mnuId));
					if(!listeFamilleStr.contains(mnuP.getCode()+"-"+mnuP.getLibelle())) {
						listeMenuStr.add(mnuP.getCode()+"-"+mnuP.getLibelle());
					}
				}
			}
			String[] articleArray = StringUtil.getArrayFromStringDelim(caisseP.getArticles_cmd(), ";");
			if(articleArray != null) {
				for (String artId : articleArray) {
					if(StringUtil.isEmpty(artId)) {
						continue;
					}
					ArticlePersistant artP = familleService.findById(ArticlePersistant.class, Long.valueOf(artId));
					if(artP.getOpc_famille_cuisine() == null) {
						continue;
					}
					String key = artP.getCode()+"-"+artP.getLibelle()+" <span style='color:#ff8f00;'>["+artP.getOpc_famille_cuisine().getCode()+"-"+artP.getOpc_famille_cuisine().getLibelle()+"]</span>";
					if(!listeFamilleStr.contains(key)) {
						listeArticleStr.add(key);
					}
				}
			}
			String[] familleArray = StringUtil.getArrayFromStringDelim(caisseP.getFamilles_cmd(), ";");
			if(familleArray != null) {
				for (String famId : familleArray) {
					if(StringUtil.isEmpty(famId)) {
						continue;
					}
					FamillePersistant famP = familleService.findById(FamillePersistant.class, Long.valueOf(famId));
					if(famP != null) {
						if(!listeFamilleStr.contains(famP.getCode()+"-"+famP.getLibelle())) {
							listeFamilleStr.add(famP.getCode()+"-"+famP.getLibelle());
						}
					}
				}
			}
			List<List<String>> listData = new ArrayList<>();
			Collections.sort(listeArticleStr);
			Collections.sort(listeFamilleStr);
			Collections.sort(listeMenuStr);
			
			listData.add(listeArticleStr);
			listData.add(listeFamilleStr);
			listData.add(listeMenuStr);
			
			mapData.put(caisseP.getId(), listData);
		}		
		httpUtil.setRequestAttribute("mapData", mapData);
		
		httpUtil.setDynamicUrl("/domaine/caisse_restau/cuisine/check-conf-cuisine.jsp");
	}
}
