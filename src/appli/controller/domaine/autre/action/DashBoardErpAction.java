package appli.controller.domaine.autre.action;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.hibernate.criterion.Order;

import appli.controller.domaine.stock.bean.ArticleBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_MOUVEMENT_ENUM;
import appli.model.domaine.administration.persistant.UserPersistant;
import appli.model.domaine.caisse.persistant.JourneeVenteErpView;
import appli.model.domaine.caisse.service.IDashBoard2Service;
import appli.model.domaine.dashboard.service.IDashBoardService;
import appli.model.domaine.personnel.persistant.ClientPersistant;
import appli.model.domaine.personnel.persistant.EmployePersistant;
import appli.model.domaine.personnel.persistant.OffrePersistant;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.persistant.ChargeDiversPersistant;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.stock.persistant.MouvementPersistant;
import appli.model.domaine.stock.service.IArticleService;
import appli.model.domaine.stock.service.IFamilleService;
import appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.ContextGloabalAppli;
import framework.controller.annotation.WorkController;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.DateUtil.TIME_ENUM;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StringUtil;

@WorkController(nameSpace="dash", bean=ArticleBean.class, jspRootPath="/domaine/autre/")
public class DashBoardErpAction extends ActionBase {
	@Inject
	private IDashBoard2Service dashBoardService2;
	@Inject
	private IDashBoardService dashBoardService;
	@Inject
	private IArticleService articleService;
	@Inject
	private IFamilleService familleService;
	
	/**
	 * @param httpUtil
	 */
	public void etat_tva(ActionUtil httpUtil) {
		String[][] typeMvmArray = new String[][]{{TYPE_MOUVEMENT_ENUM.a.toString(), "Achat"}, {TYPE_MOUVEMENT_ENUM.v.toString(), "Vente"}};
		httpUtil.setRequestAttribute("typeMvmArray", typeMvmArray);
		Date dateDebut = null;
		Date dateFin = null;

		// Calcul des dates
		if(httpUtil.isSubmit()){ 
			dateDebut = DateUtil.stringToDate(httpUtil.getParameter("dateDebut"));
			dateFin = DateUtil.stringToDate(httpUtil.getParameter("dateFin"));
		} else{
			LocalDate today = LocalDate.now();
			dateDebut = Date.from(today.withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
			dateFin = Date.from(today.withDayOfMonth(today.lengthOfMonth()).atStartOfDay(ZoneId.systemDefault()).toInstant());
		}
		
		httpUtil.setRequestAttribute("dateDebut", dateDebut);
		httpUtil.setRequestAttribute("dateFin", dateFin);
		
		// Construction des données
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_etat_tva");
		RequestTableBean cplxTableCharge = getTableBean(httpUtil, "list_chargeDivers");
		
		// Ajouter les paramètres
		Map<String, Object> formCriterion = cplxTable.getFormBean().getFormCriterion();
		formCriterion.put("dateDebut", dateDebut);
		formCriterion.put("dateFin", dateFin);

		Map<String, Object> formCriterionCharge = cplxTableCharge.getFormBean().getFormCriterion();
		formCriterionCharge.put("dateDebut", dateDebut);
		formCriterionCharge.put("dateFin", dateFin);
		
		BigDecimal tauxVente = BigDecimalUtil.get(ContextGloabalAppli.getGlobalConfig(ContextAppli.PARAM_APPLI_ENUM.TVA_VENTE.toString()));
		BigDecimal mttVenteARecupererHT = BigDecimalUtil.ZERO;
		BigDecimal mttVenteADonnerHT = BigDecimalUtil.ZERO;
		BigDecimal mttVenteARecupererTTC = BigDecimalUtil.ZERO;
		BigDecimal mttVenteADonnerTTC = BigDecimalUtil.ZERO;
		
		List<MouvementPersistant> listData = (List<MouvementPersistant>) dashBoardService.findByCriteriaByQueryId(cplxTable, "mouvement_tva_find");
		List<MouvementPersistant> listDataAll = (List<MouvementPersistant>) dashBoardService.findByCriteriaByQueryId(cplxTable, "mouvement_tva_find", false);
		// Charges divers
		List<ChargeDiversPersistant> listCharge = (List<ChargeDiversPersistant>) dashBoardService.findByCriteriaByQueryId(cplxTableCharge, "chargeDivers_tva_find");
		List<ChargeDiversPersistant> listChargeAll = (List<ChargeDiversPersistant>) dashBoardService.findByCriteriaByQueryId(cplxTableCharge, "chargeDivers_tva_find", false);
		
		for (ChargeDiversPersistant chargeDiversPersistant : listChargeAll) {
			if(chargeDiversPersistant.getOpc_tva_enum() != null && chargeDiversPersistant.getOpc_tva_enum().getLibelle().equals("0")){
				BigDecimal tauxTva = BigDecimalUtil.get(chargeDiversPersistant.getOpc_tva_enum().getLibelle());
				BigDecimal mttTva = BigDecimalUtil.divide(BigDecimalUtil.multiply(chargeDiversPersistant.getMontant(), tauxTva), BigDecimalUtil.get(100));
				
				if(chargeDiversPersistant.getSens().equals("D")){
					mttVenteARecupererTTC = BigDecimalUtil.add(mttVenteARecupererTTC, chargeDiversPersistant.getMontant(), mttTva);
					mttVenteARecupererHT = BigDecimalUtil.add(mttVenteARecupererHT, chargeDiversPersistant.getMontant());	
				} else{
					mttVenteADonnerTTC = BigDecimalUtil.add(mttVenteADonnerTTC, chargeDiversPersistant.getMontant(), mttTva);
					mttVenteADonnerHT = BigDecimalUtil.add(mttVenteADonnerHT, chargeDiversPersistant.getMontant());
				}
			}
		}
		
		// tva ventes et achat
		for (MouvementPersistant mvmP : listDataAll) {
			if(mvmP.getType_mvmnt().equals(TYPE_MOUVEMENT_ENUM.a.toString())){
				mttVenteARecupererTTC = BigDecimalUtil.add(mttVenteARecupererTTC, mvmP.getMontant_ttc());
				mttVenteARecupererHT = BigDecimalUtil.add(mttVenteARecupererHT, mvmP.getMontant_ht());
			} else if(mvmP.getType_mvmnt().equals(TYPE_MOUVEMENT_ENUM.v.toString())){
				mttVenteADonnerTTC = BigDecimalUtil.add(mttVenteADonnerTTC, mvmP.getMontant_ttc());
				mttVenteADonnerHT = BigDecimalUtil.add(mttVenteADonnerHT, mvmP.getMontant_ht());
			}
		}
		httpUtil.setRequestAttribute("mttVenteARecupererHT", mttVenteARecupererHT);
		httpUtil.setRequestAttribute("mttVenteARecupererTTC", mttVenteARecupererTTC);
		httpUtil.setRequestAttribute("mttVenteADonnerHT", mttVenteADonnerHT);
		httpUtil.setRequestAttribute("mttVenteADonnerTTC", mttVenteADonnerTTC);
		
		httpUtil.setRequestAttribute("listCharge", listCharge);
		httpUtil.setRequestAttribute("listMvmTva", listData);
		
		httpUtil.setDynamicUrl("/domaine/stock/etat_tva_list.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void init_indicateur(ActionUtil httpUtil){
		Date lastDate = dashBoardService2.getLastDateVente();
		Map<String,Object> variables = new HashMap<String,Object>();
		
		variables.put("venteParJours", dashBoardService2.getVentesJours());
		variables.put("venteParMois", dashBoardService2.getVentesMois());
		variables.put("mttResultatNetParMois", dashBoardService.getMttResultatNetParMois());
		
		httpUtil.setRequestAttribute("dataIndicateur", variables);
		
		// Répartition des compositions des commandes
		init_indicateur_repartition(httpUtil);
		
		httpUtil.setDynamicUrl("/domaine/autre/dashboard_indicateur.jsp");
	}
	
	public void init_indicateur_repartition(ActionUtil httpUtil){
		IFamilleService familleService = (IFamilleService) ServiceUtil.getBusinessBean(IFamilleService.class);
		List<FamillePersistant> listFamille = (List<FamillePersistant>) familleService.getListeFamille("ST", true, false);
		httpUtil.setRequestAttribute("list_famille", listFamille);
		
		Long familleId = null;
		if(StringUtil.isEmpty(httpUtil.getParameter("curr_famille"))) {
			if(!listFamille.isEmpty()) {
				familleId = listFamille.get(0).getId();
			}
		} else {
			familleId = httpUtil.getLongParameter("curr_famille");
		}
		httpUtil.setRequestAttribute("curr_famille", familleId);
		//
		String dtDebut = httpUtil.getParameter("rep_dt_debut");
		String dtFin = httpUtil.getParameter("rep_dt_fin");
		Date dateDebut = null;
		Date dateFin = null;
		
		if(StringUtil.isEmpty(dtDebut)){
			Calendar cal = DateUtil.getCalendar(new Date());
			dtDebut = "01/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR);
		}
		dateDebut = DateUtil.stringToDate(dtDebut);
		
		if(StringUtil.isEmpty(dtFin)){
			Calendar cal = DateUtil.getCalendar(new Date());
			dtFin = DateUtil.getMaxMonthDate(dateDebut)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR);
		}
		dateFin = DateUtil.stringToDate(dtFin);
		
		httpUtil.setRequestAttribute("curr_dtDebut", dateDebut);
		httpUtil.setRequestAttribute("curr_dtFin", dateFin);
		
		Map mapData = dashBoardService2.getRepartitionVenteArticle(null, dateDebut, dateFin, familleId);
		httpUtil.setRequestAttribute("dataRepartion", mapData);
		httpUtil.setRequestAttribute("familleId", familleId);
		
		httpUtil.setDynamicUrl("/domaine/dashboard_erp/dashboard_repartition_include.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void init_chiffres_journee(ActionUtil httpUtil){
		Date lastDate = dashBoardService2.getLastDateVente();

		JourneeVenteErpView journeeDetail = dashBoardService2.getJourneeDetail(lastDate);
		httpUtil.setRequestAttribute("journeeVente", journeeDetail);
		
		httpUtil.setDynamicUrl("/domaine/autre/dashboard_chifres_include.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void find_reduction(ActionUtil httpUtil){
		// Initialiser les listes pour les filtres
		initDataListFilter(httpUtil);
		
		httpUtil.setRequestAttribute("listOffre", dashBoardService.findAll(OffrePersistant.class, Order.asc("libelle")));
		
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_mouvement_reduction");
		Map<String, Object> formCriterion = cplxTable.getFormBean().getFormCriterion();
		boolean isFilterAct = StringUtil.isTrue(httpUtil.getParameter("is_filter_act"));
		
		//----------------------------- Date -------------------------
		Date dateDebut = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateDebut"));
		Date dateFin = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateFin"));
		
		if(dateDebut == null) {
			dateDebut = (httpUtil.getMenuAttribute("dateDebut")==null ? new Date() : (Date)httpUtil.getMenuAttribute("dateDebut"));
			dateFin = (httpUtil.getMenuAttribute("dateFin")==null ? new Date() : (Date)httpUtil.getMenuAttribute("dateFin"));
			httpUtil.getDate("dateDebut").setValue(dateDebut);
			httpUtil.getDate("dateFin").setValue(dateDebut);
		}
		
		if(httpUtil.getParameter("prev") != null) {
			dateDebut = DateUtil.addSubstractDate(dateDebut, TIME_ENUM.DAY, -1);
			dateFin = DateUtil.addSubstractDate(dateFin, TIME_ENUM.DAY, -1);
		} else if(httpUtil.getParameter("next") != null) {
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
		List<CaisseMouvementPersistant> listCaisseMouvement = (List<CaisseMouvementPersistant>) dashBoardService.findByCriteriaByQueryId(cplxTable, "mouvementReduction_find");
		httpUtil.setRequestAttribute("list_caisseMouvement", listCaisseMouvement);
	
		CaisseMouvementPersistant jp = new CaisseMouvementPersistant();
		List<CaisseMouvementPersistant> listCaisseMouvementAll = (List<CaisseMouvementPersistant>) dashBoardService.findByCriteriaByQueryId(cplxTable, "mouvementReduction_find", false);
	   	for (CaisseMouvementPersistant jvp : listCaisseMouvementAll) {
	   		jp.setMtt_commande(BigDecimalUtil.add(jp.getMtt_commande(), jvp.getMtt_commande()));
	   		jp.setMtt_reduction(BigDecimalUtil.add(jp.getMtt_reduction(), jvp.getMtt_reduction()));
	   		jp.setMtt_commande_net(BigDecimalUtil.add(jp.getMtt_commande_net(), jvp.getMtt_commande_net()));
		}
	   	httpUtil.setRequestAttribute("mvm_total", jp);
	   	
		httpUtil.setDynamicUrl("/domaine/autre/mouvements_reduit_list.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
//	public void find_annulation(ActionUtil httpUtil){
//		// Initialiser les listes pour les filtres
//		initDataListFilter(httpUtil);
//		
//		RequestTableBean cplxTable = getTableBean(httpUtil, "list_mouvement_annulation");
//		//----------------------------- Date -------------------------
//		Date dateDebut = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateDebut"));
//		Date dateFin = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateFin"));
//		//
//		if(httpUtil.getRequest().getParameter("is_fltr") == null) {
//			dateDebut = DateUtil.stringToDate(httpUtil.getParameter("dateDebut"));
//			dateFin = DateUtil.stringToDate(httpUtil.getParameter("dateFin"));
//		}
//		
//		if(dateDebut == null) {
//			dateDebut = new Date();
//			dateFin = new Date();
//			httpUtil.getDate("dateDebut").setValue(dateDebut);
//			httpUtil.getDate("dateFin").setValue(dateDebut);
//		}
//		
//		if(httpUtil.getParameter("prev") != null) {
//			dateDebut = DateUtil.addSubstractDate(dateDebut, TIME_ENUM.DAY, -1);
//			dateFin = DateUtil.addSubstractDate(dateFin, TIME_ENUM.DAY, -1);
//		} else if(httpUtil.getParameter("next") != null) {
//			dateDebut = DateUtil.addSubstractDate(dateDebut, TIME_ENUM.DAY, 1);
//			dateFin = DateUtil.addSubstractDate(dateFin, TIME_ENUM.DAY, 1);
//		}
//		
//		dateDebut = DateUtil.getStartOfDay(dateDebut);
//		dateFin = DateUtil.getEndOfDay(dateFin);
//		
//		httpUtil.setRequestAttribute("dateDebut", dateDebut);
//		httpUtil.setRequestAttribute("dateFin", dateFin);
//		
//		cplxTable.getFormBean().getFormCriterion().put("dateDebut", dateDebut);
//		cplxTable.getFormBean().getFormCriterion().put("dateFin", dateFin);
//		//-----------------------------------------------------------
//		List<CaisseMouvementPersistant> listCaisseMouvement = (List<CaisseMouvementPersistant>) dashBoardService.findByCriteriaByQueryId(cplxTable, "annulationMouvement_find");
//		httpUtil.setRequestAttribute("list_annulationMouvement", listCaisseMouvement);
//
//		// Total
//		List<CaisseMouvementPersistant> listCaisseMouvementAll = (List<CaisseMouvementPersistant>) dashBoardService.findByCriteriaByQueryId(cplxTable, "annulationMouvement_find", false);
//		
//		CaisseMouvementPersistant mvmTotal = new CaisseMouvementPersistant();
//		for (CaisseMouvementPersistant mvmDet : listCaisseMouvementAll) {
//			mvmTotal.setMtt_commande(BigDecimalUtil.add(mvmTotal.getMtt_commande(), mvmDet.getMtt_commande()));
//			mvmTotal.setMtt_commande_net(BigDecimalUtil.add(mvmTotal.getMtt_commande_net(), mvmDet.getMtt_commande_net()));
//		}
//		httpUtil.setRequestAttribute("mvmDetTotal", mvmTotal);
//		
//		httpUtil.setDynamicUrl("/domaine/autre/mouvements_annulation_list.jsp");
//	}
	
	public void find_marge_vente(ActionUtil httpUtil) {
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_article");
		List<ArticlePersistant> list_article = articleService.getListArticleStock(true);
		for (ArticlePersistant articlePersistant : list_article) {
			List<FamillePersistant> familleStr = familleService.getFamilleParent("ST", articlePersistant.getOpc_famille_stock().getId());
			articlePersistant.setFamilleStr(familleStr);
		}
		
		cplxTable.setDataExport(list_article);
		cplxTable.setDataSize(list_article.size());
		httpUtil.setRequestAttribute("list_article", list_article);
		
		Map<ArticlePersistant, BigDecimal[]> mapData = dashBoardService2.calculMargeArticles();
		httpUtil.setRequestAttribute("mapData", mapData);
		List listFamille = familleService.getListeFamille("ST", true, false);
		httpUtil.setRequestAttribute("listeFaimlle", listFamille);
		
		httpUtil.setDynamicUrl("/domaine/autre/marge_vente_list.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void find_repartition(ActionUtil httpUtil){
		if(httpUtil.getParameter("tp") != null) {
			httpUtil.setMenuAttribute("IS_MNU", true);
		}
		Map<String, CaisseMouvementArticlePersistant> mapData = null;
		
		List<FamillePersistant> listFamille = (List<FamillePersistant>) familleService.getListeFamille("ST", true, false);
		httpUtil.setRequestAttribute("list_famille", listFamille);
		
		Long familleId = null;
		if(StringUtil.isEmpty(httpUtil.getParameter("curr_famille"))) {
//			if(!listFamille.isEmpty()) {
//				familleId = listFamille.get(0).getId();
//			}
		} else {
			familleId = httpUtil.getLongParameter("curr_famille");
		}
		httpUtil.setRequestAttribute("curr_famille", familleId);
		httpUtil.setRequestAttribute("familleId", familleId);
		
		//----------------------------- Date -------------------------
		if(httpUtil.getMenuAttribute("IS_MNU") != null) {
			Date dateDebut = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateDebut"));
			Date dateFin = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateFin"));
			//
			if(httpUtil.getRequest().getParameter("is_fltr") == null) {
				dateDebut = DateUtil.stringToDate(httpUtil.getParameter("dateDebut"));
				dateFin = DateUtil.stringToDate(httpUtil.getParameter("dateFin"));
			}
			
			if(dateDebut == null) {
				dateDebut = new Date();
				dateFin = new Date();
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
			
			httpUtil.setRequestAttribute("dateDebut", dateDebut);
			httpUtil.setRequestAttribute("dateFin", dateFin);
			//
			mapData = dashBoardService2.getRepartitionVenteArticle(null, dateDebut, dateFin, familleId);
		} else {
			Long journeeId = (Long)httpUtil.getMenuAttribute("journeeId");
			mapData = dashBoardService2.getRepartitionVenteArticle(journeeId, null, null, familleId);
		}
		//-----------------------------------------------------------
		httpUtil.setRequestAttribute("dataRepartion", mapData);
		
		httpUtil.setDynamicUrl("/domaine/dashboard_erp/repartition_article_vente_mnu.jsp");
	}
	
	public void init_raz(ActionUtil httpUtil){
		// Min et Max dates disponibles
		Date[] minMaxDate = dashBoardService2.getMinMaxDate();
		Calendar cal = DateUtil.getCalendar(new Date());
		String dtDebut = (cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR);
				
		if(minMaxDate[1] != null) {
			Calendar calendarDtFin = DateUtil.getCalendar(minMaxDate[1]);
			dtDebut = (calendarDtFin.get(Calendar.MONTH)+1)+"/"+calendarDtFin.get(Calendar.YEAR);
		}
		String dtDebutStr = dtDebut.length()==6 ? "0"+dtDebut:dtDebut;
		httpUtil.setRequestAttribute("dateJour", DateUtil.dateToString(minMaxDate[1]));
		if(httpUtil.getParameter("dateDebut") == null) {
			httpUtil.setRequestAttribute("dateDebut", dtDebutStr);
		}
		
		if(minMaxDate[0] != null){
			httpUtil.setRequestAttribute("minDate", DateUtil.getDiffMonth(new Date(), minMaxDate[0]));
		}
		if(minMaxDate[1] != null){
			httpUtil.setRequestAttribute("maxDate", DateUtil.getDiffMonth(new Date(), minMaxDate[1]));
		}
		httpUtil.setDynamicUrl("/domaine/autre/init_raz.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	private void initDataListFilter(ActionUtil httpUtil){
		String[][] modePaie = {
				  {ContextAppli.MODE_PAIEMENT.ESPECES.toString(), "Espèces"}, 
				  {ContextAppli.MODE_PAIEMENT.CHEQUE.toString(), "Chèque"}, 
				  {ContextAppli.MODE_PAIEMENT.DEJ.toString(), "Chèque déj."},
				  {ContextAppli.MODE_PAIEMENT.CARTE.toString(), "Carte"}
			};
		httpUtil.setRequestAttribute("modePaie", modePaie);
		
		String[][] statutArray = {
			  {ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.ANNUL.toString(), "Annulée"}, 
			  {ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE.toString(), "Validée"}, 
			  {ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.PREP.toString(), "En préparation"},
			  {ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.PRETE.toString(), "Prête"},
			  {ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.LIVRE.toString(), "livrée"},
			  {ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.TEMP.toString(), "En attente"}
		};
	httpUtil.setRequestAttribute("statutArray", statutArray);
		httpUtil.setRequestAttribute("listUser", dashBoardService.findAll(UserPersistant.class, Order.asc("login")));
		httpUtil.setRequestAttribute("listClient", dashBoardService.findAll(ClientPersistant.class, Order.asc("nom")));
		httpUtil.setRequestAttribute("listEmploye", dashBoardService.findAll(EmployePersistant.class, Order.asc("nom")));
	}
	
	private Integer getInt(Integer val){
		return (val==null ? 0 : val);
	}
	
}
