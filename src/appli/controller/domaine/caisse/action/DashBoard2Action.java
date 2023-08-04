package appli.controller.domaine.caisse.action;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import appli.controller.domaine.caisse.bean.CaisseBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_MOUVEMENT_ENUM;
import appli.model.domaine.administration.service.IParametrageService;
import appli.model.domaine.caisse.service.ICaisseMouvementService;
import appli.model.domaine.caisse.service.IDashBoard2Service;
import appli.model.domaine.caisse.service.IJourneeService;
import appli.model.domaine.stock.persistant.ChargeDiversPersistant;
import appli.model.domaine.stock.persistant.FamilleCuisinePersistant;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.stock.persistant.MouvementPersistant;
import appli.model.domaine.stock.service.IFamilleService;
import appli.model.domaine.vente.persistant.CaisseJourneePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import appli.model.domaine.vente.persistant.JourneePersistant;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.Context;
import framework.controller.ContextGloabalAppli;
import framework.controller.annotation.WorkController;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StringUtil;

@WorkController(nameSpace="dash", bean=CaisseBean.class, jspRootPath="/domaine/caisse/")
public class DashBoard2Action extends ActionBase {
	@Inject
	private IJourneeService journeeService;
	@Inject
	private IFamilleService familleService;
	@Inject
	private IDashBoard2Service dashBoardService;
	@Inject
	private ICaisseMouvementService caisseMvmService;
	
	/**
	 * @param httpUtil
	 */
	public void init_indicateur_repartition(ActionUtil httpUtil){
		List<FamillePersistant> listFamille = (List<FamillePersistant>) familleService.getListeFamille("CU", true, false);
		// Ajout menu
		FamillePersistant fpf = new FamilleCuisinePersistant();
		fpf.setId(Long.valueOf(-999));
		fpf.setB_left(1);
		fpf.setB_right(2);
		fpf.setCode("MNU");
		fpf.setLibelle("Menus");
		fpf.setLevel(2);
		listFamille.add(0, fpf);
		//
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
			JourneePersistant dateLastJ = journeeService.getLastJournee();
			Date dateLast = null;
			if(dateLastJ == null) {
				dateLast = new Date();
			} else {
				dateLast = dateLastJ.getDate_journee();
			}
			Calendar cal = DateUtil.getCalendar(dateLast);
			dtDebut = "01/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR);
		}
		dateDebut = DateUtil.stringToDate(dtDebut);
		
		if(StringUtil.isEmpty(dtFin)){
			Calendar cal = DateUtil.getCalendar(dateDebut);
			dtFin = DateUtil.getMaxMonthDate(dateDebut)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR);
		}
		dateFin = DateUtil.stringToDate(dtFin);
		
		httpUtil.setRequestAttribute("curr_dtDebut", dateDebut);
		httpUtil.setRequestAttribute("curr_dtFin", dateFin);
		
		Map<String,Object> variables = (Map<String,Object>)httpUtil.getRequestAttribute("dataIndicateur");
		if(variables == null) {
			variables = new HashMap<String,Object>();
			httpUtil.setRequestAttribute("dataIndicateur", variables);
		}
		
		JourneePersistant journeeDebut = journeeService.getJourneeOrNextByDate(dateDebut);
    	JourneePersistant journeeFin = journeeService.getJourneeOrPreviousByDate(dateFin);
		
    	if(journeeDebut != null) {
    		//MessageService.addGrowlMessage("", "Aucune journée ne correspond à ces dates.");
    		//return;
    		Map mapData = journeeService.getRepartitionVenteArticle(journeeDebut, journeeFin, familleId, false);			
    		httpUtil.setRequestAttribute("dataRepartion", mapData);
    		httpUtil.setRequestAttribute("familleId", familleId);
    	}
    			
		httpUtil.setDynamicUrl("/domaine/dashboard_erp/dashboard_repartition_include.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void detail_journee(ActionUtil httpUtil){
		
		httpUtil.setMenuAttribute("IS_DASH_JRN", true);
		
		if(Context.isOperationAvailable("DETJRN")) {
			JourneePersistant lastJournee = dashBoardService.getLastJourneCaisse();
			if(lastJournee == null){
				MessageService.addGrowlMessage(MSG_TYPE.WARNING, "", "Aucune journée disponible.");
				httpUtil.setDynamicUrl("caisse.journee.work_find");
				return;
			}
			
			Long journeeId = lastJournee.getId();
			JourneePersistant journeeV = journeeService.getJourneeView(journeeId );
			boolean isDoubleCloture = BooleanUtil.isTrue(ContextAppli.getUserBean().getIs_admin()) 
											|| (StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("DOUBLE_CLOTURE")) && Context.isOperationAvailable("DBLCLO"));
			httpUtil.setRequestAttribute("isDoubleCloture", isDoubleCloture);
			
			httpUtil.setRequestAttribute("journeeView", journeeV);
			httpUtil.setRequestAttribute("journee", journeeV);
			
			httpUtil.setMenuAttribute("journeeId", journeeId);
			httpUtil.setMenuAttribute("CURR_JRN_ID", journeeId);
			
			httpUtil.setDynamicUrl("/domaine/caisse/back-office/journee_edit.jsp");
		}else {
			MessageService.addGrowlMessage("", "Vous n'êtes pas autorisé à visualiser cette page.");
		}
	}
	
	/**
	 * @param httpUtil
	 */
	public void init_chiffres_journee(ActionUtil httpUtil){
		JourneePersistant lastJournee = dashBoardService.getLastJourneCaisse();

		try {
			if(lastJournee == null){
				httpUtil.setRequestAttribute("journeeVente", new JourneePersistant());
				httpUtil.setDynamicUrl("/domaine/caisse/dashboard/dashboard_chifres_include.jsp");
				return;
			}
			JourneePersistant journeeDetail = dashBoardService.getJourneeDetail(lastJournee.getId());
			if("O".equals(journeeDetail.getStatut_journee())) {
				journeeService.setDataJourneeFromView(journeeDetail);
			}
			httpUtil.setRequestAttribute("journeeVente", journeeDetail);
		} catch(Exception e) {
			e.printStackTrace();
			IParametrageService parametrageService = ServiceUtil.getBusinessBean(IParametrageService.class);
			parametrageService.executerScriptView();
			httpUtil.writeResponse("REDIRECT");
			return;
		}
		httpUtil.setDynamicUrl("/domaine/caisse/dashboard/dashboard_chifres_include.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void init_indicateur(ActionUtil httpUtil){
		JourneePersistant lastJournee = dashBoardService.getLastJourneCaisse();
		Map<String,Object> variables = new HashMap<String,Object>();
		init_data_achats(httpUtil);
		
		if(lastJournee == null){
			variables.put("journeeVente", new CaisseJourneePersistant());
			httpUtil.setRequestAttribute("dataIndicateur",variables);
			httpUtil.setDynamicUrl("/domaine/caisse/dashboard/dashboard_indicateur.jsp");
			return;
		}
		
		
		variables.put("ventes", dashBoardService.getEvolutionVentes());
		variables.put("depences", dashBoardService.getEvolutionDepences());
		variables.put("recetes", dashBoardService.getEvolutionRecettes());
		variables.put("achats", dashBoardService.getEvolutionAchats());
		variables.put("mttResultatNetParMois", dashBoardService.getMttResultatNetParMois());
		
		httpUtil.setRequestAttribute("dataIndicateur", variables);
		// Répartition des compositions des commandes
		init_indicateur_repartition(httpUtil);
		httpUtil.setDynamicUrl("/domaine/caisse/dashboard/dashboard_indicateur.jsp");
	}
	
	public void init_data_achats(ActionUtil httpUtil){
		List<FamillePersistant> listFamille = (List<FamillePersistant>) familleService.getListeFamille("ST", true, false);
		Date dtDebut = null, dtFin = null;
		
       httpUtil.setRequestAttribute("list_familleStock", listFamille);
		
		Long familleId = null;
		if(StringUtil.isEmpty(httpUtil.getParameter("curr_famille"))) {
			if(!listFamille.isEmpty()) {
				familleId = listFamille.get(0).getId();
			}
		} else {
			familleId = httpUtil.getLongParameter("curr_famille");
		}
		httpUtil.setRequestAttribute("curr_famille", familleId);
		
		// Date début
		String dateDebutParam = httpUtil.getParameter("rep_dt_debut");
		if(StringUtil.isEmpty(dateDebutParam)) {
			Calendar cal = DateUtil.getCalendar(DateUtil.getCurrentDate());
			int mt = cal.get(Calendar.MONTH)+1;
			String mois = (""+mt).length() == 1 ? "0"+mt : ""+mt;
			dtDebut = DateUtil.stringToDate("01"+"/"+mois+"/"+cal.get(Calendar.YEAR));
		} else {
			dtDebut = DateUtil.stringToDate(dateDebutParam);
		}
		// Date fin
		String dateFinParam = httpUtil.getParameter("rep_dt_fin");
		if(StringUtil.isEmpty(dateFinParam)) {
			Calendar cal = DateUtil.getCalendar(dtDebut);
			int mt = cal.get(Calendar.MONTH)+1;
			String mois = (""+mt).length() == 1 ? "0"+mt : ""+mt;
			dtFin = DateUtil.stringToDate(cal.getActualMaximum(Calendar.DAY_OF_MONTH)+"/"+mois+"/"+cal.get(Calendar.YEAR));
		} else {
			dtFin = DateUtil.stringToDate(dateFinParam);
		}
		
		dtDebut = DateUtil.getStartOfDay(dtDebut);
		dtFin = DateUtil.getEndOfDay(dtFin);
		
		httpUtil.setRequestAttribute("curr_dtDebut", dtDebut);
		httpUtil.setRequestAttribute("curr_dtFin", dtFin);
		
		List<Object[]> dataAchats = dashBoardService.getRepartitionAchatArticlePie(dtDebut, dtFin,familleId);
		List<Object[]> dataDepences = dashBoardService.getRepartitionDepencesPie(dtDebut, dtFin);
		List<Object[]> dataRecetes = dashBoardService.getRepartitionRecettesPie(dtDebut, dtFin);
		//List<Object[]> dataRecetes = dashBoardService.getRepartitionVenteArticle(dtDebut, dtFin);

		httpUtil.setRequestAttribute("achatsPie", dataAchats);
		httpUtil.setRequestAttribute("depencesPie", dataDepences);
		httpUtil.setRequestAttribute("recetesPie", dataRecetes);

		httpUtil.setDynamicUrl("/domaine/caisse/dashboard/dashboard_indi_achat.jsp");
	}
	
	
	/**
	 * @param httpUtil
	 */
	public void etat_tva(ActionUtil httpUtil) {
		String[][] typeMvmArray = new String[][]{{TYPE_MOUVEMENT_ENUM.a.toString(), "Achat"}, {TYPE_MOUVEMENT_ENUM.v.toString(), "Vente hors caisse"}, {TYPE_MOUVEMENT_ENUM.vc.toString(), "Vente caisse"}};
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
		
		List<MouvementPersistant> listData = (List<MouvementPersistant>) journeeService.findByCriteriaByQueryId(cplxTable, "mouvement_tva_find");
		List<MouvementPersistant> listDataAll = (List<MouvementPersistant>) journeeService.findByCriteriaByQueryId(cplxTable, "mouvement_tva_find", false);
		List<CaisseMouvementPersistant> listCaisseMvm = (List<CaisseMouvementPersistant>) caisseMvmService.getListMouvementCaisse(dateDebut, dateFin);
		// Charges divers
		List<ChargeDiversPersistant> listCharge = (List<ChargeDiversPersistant>) journeeService.findByCriteriaByQueryId(cplxTableCharge, "chargeDivers_tva_find");
		List<ChargeDiversPersistant> listChargeAll = (List<ChargeDiversPersistant>) journeeService.findByCriteriaByQueryId(cplxTableCharge, "chargeDivers_tva_find", false);
		
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
		
		Map<String, BigDecimal> mapCaisseMvm = new HashMap<>();
		for (CaisseMouvementPersistant caisseMvmP : listCaisseMvm) {
			if(StringUtil.isNotEmpty(caisseMvmP.getMvm_stock_ids())){
				mapCaisseMvm.put(caisseMvmP.getMvm_stock_ids(), caisseMvmP.getMtt_commande_net());
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
			} else if(mvmP.getType_mvmnt().equals(TYPE_MOUVEMENT_ENUM.vc.toString())){
				BigDecimal mttCaisse = null;
				for(String mvmIds : mapCaisseMvm.keySet()) {
					if(mvmIds.indexOf(";"+mvmP.getId()+";") != -1) {
						mttCaisse = mapCaisseMvm.get(mvmIds);
						break;
					}
				}
				BigDecimal mttTva = BigDecimalUtil.multiply(
            			BigDecimalUtil.divide(mttCaisse, BigDecimalUtil.get("1"+tauxVente)), 
            			tauxVente);
				
				BigDecimal mttHt = BigDecimalUtil.substract(mttCaisse, mttTva);
				
				mvmP.setMontant_ht(mttHt);
				mvmP.setMontant_ttc(mttCaisse);
				mvmP.setMontant_tva(mttTva);
				
				mttVenteADonnerTTC = BigDecimalUtil.add(mttVenteADonnerTTC, mttCaisse);
				mttVenteADonnerHT = BigDecimalUtil.add(mttVenteADonnerHT, mttHt);
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
}
