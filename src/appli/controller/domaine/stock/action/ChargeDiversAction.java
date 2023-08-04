package appli.controller.domaine.stock.action;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import appli.controller.domaine.administration.bean.CompteBancaireBean;
import appli.controller.domaine.administration.bean.ValTypeEnumBean;
import appli.controller.domaine.stock.bean.ChargeDiversBean;
import appli.controller.domaine.stock.bean.FournisseurChequeBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.administration.persistant.GedPersistant;
import appli.model.domaine.administration.persistant.TypeEnumPersistant;
import appli.model.domaine.administration.service.ICompteBancaireService;
import appli.model.domaine.administration.service.IGedService;
import appli.model.domaine.administration.service.IValTypeEnumService;
import appli.model.domaine.compta.persistant.PaiementPersistant;
import appli.model.domaine.stock.persistant.ChargeDiversPersistant;
import appli.model.domaine.stock.persistant.CompteVentilationPersistant;
import appli.model.domaine.stock.persistant.FournisseurPersistant;
import appli.model.domaine.stock.persistant.LabelsGroupPersistant;
import appli.model.domaine.stock.persistant.TravauxPersistant;
import appli.model.domaine.stock.service.IChargeDiversService;
import appli.model.domaine.stock.service.IFamilleService;
import appli.model.domaine.stock.service.IFournisseurChequeService;
import appli.model.domaine.stock.service.IFournisseurService;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.ContextGloabalAppli;
import framework.controller.annotation.WorkController;
import framework.controller.annotation.WorkForward;
import framework.model.common.constante.ActionConstante;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.DateUtil.TIME_ENUM;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.ModelConstante;

@WorkController(nameSpace="stock", bean=ChargeDiversBean.class, jspRootPath="/domaine/stock/")
public class ChargeDiversAction extends ActionBase {

	@Inject
	private IFournisseurService fournisseurService;
	@Inject
	private IFamilleService familleService;
	@Inject
	private IValTypeEnumService valEnumService;
	@Inject 
	private IChargeDiversService chargeDiversService;
	@Inject 
	private ICompteBancaireService compteBancaireService;
	@Inject
	private IFournisseurChequeService fournisseurChequeService;
	@Inject
	private IGedService gedService;
	
	public void work_init(ActionUtil httpUtil) {
		httpUtil.setMenuAttribute("IS_SUB_ADD","stock.chargeDivers.work_init_create");
		httpUtil.setRequestAttribute("typeTVAEnumId", valEnumService.getOneByField(TypeEnumPersistant.class, "code", ModelConstante.ENUM_TVA).getId());

		if(httpUtil.isEditionPage()){
			List<ValTypeEnumBean> listeFinancement = valEnumService.getListValeursByType(ModelConstante.ENUM_FINANCEMENT);
			httpUtil.setRequestAttribute("listeFinancement", listeFinancement);
			httpUtil.setRequestAttribute("listeBanque", compteBancaireService.findAll());
		} else {
			httpUtil.setRequestAttribute("immoData", new String[][] { { "I", "Immobilisation" }, { "NI", "Sans immobilisation" }});
			httpUtil.setRequestAttribute("travauxData", new String[][] { { "T", "Pour travaux" }, { "NT", "Non travaux" }});
		}
		
		List<FournisseurPersistant> listFournisseur = fournisseurService.getListFournisseur(true, false);
		httpUtil.setRequestAttribute("listeFournisseur", listFournisseur);
		
		List listFamilleConsommation = familleService.getListeFamille("CO", true, false);
		httpUtil.setRequestAttribute("familleConsommation", listFamilleConsommation);
		
		List<ValTypeEnumBean> listTva = valEnumService.getListValeursByType(ModelConstante.ENUM_TVA);
		httpUtil.setRequestAttribute("listeTva", listTva);
		
		String action = httpUtil.getAction();
		if(ActionConstante.INIT_CREATE.equals(action)) {
			httpUtil.removeMenuAttribute("PAIEMENT_DATA");
		}
		
		httpUtil.setRequestAttribute("frequenceTypeArray", new String[][]{{"J", "Jours"}, {"M", "Mois"}});
		String type = httpUtil.getParameter("tp");
		if (type != null) {
			httpUtil.setMenuAttribute("tpD", httpUtil.getParameter("tp"));
		}
		// Paiement
		if(httpUtil.isCrudOperation()){
			if(httpUtil.getViewBean() != null){
				((ChargeDiversBean)httpUtil.getViewBean()).setList_paiement((List<PaiementPersistant>) httpUtil.getMenuAttribute("PAIEMENT_DATA"));
			}
		}
	}
	
	@Override
	public void work_merge(ActionUtil httpUtil){
		String type = (String) httpUtil.getMenuAttribute("tpD");
		boolean isDepense = "D".equals(type);
		ChargeDiversBean chargeBean = (ChargeDiversBean) httpUtil.getViewBean();
		chargeBean.setId(httpUtil.getWorkIdLong());
		
		if(StringUtil.isNotEmpty(httpUtil.getParameter("chargeDivers.libelle2"))) {
			chargeBean.setLibelle(httpUtil.getParameter("chargeDivers.libelle2"));
		}
		
		if(StringUtil.isEmpty(chargeBean.getLibelle())) {
			MessageService.addFieldMessage("chargeDivers.libelle", "Champs obligatoire");
			return;
		}
		
		if(chargeBean.getId() != null) {
			ChargeDiversBean mvmDb = chargeDiversService.findById(chargeBean.getId());
			List<CompteVentilationPersistant> listV = mvmDb.getList_ventilation();
			chargeBean.setList_ventilation(listV);
		}
		
		boolean isFromCaisse = httpUtil.getParameter("isCai") != null;
		if(isFromCaisse){
			chargeBean.setSens("D");
			chargeBean.setDate_mouvement(new Date());
			chargeBean.setSource("C");// CAISSE
			
			String cptEspeces = ContextGloabalAppli.getGlobalConfig("COMPTE_BANCAIRE_CAISSE");
			if(StringUtil.isNotEmpty(cptEspeces)){
				CompteBancaireBean compteB = compteBancaireService.findById(Long.valueOf(cptEspeces));
				List<PaiementPersistant> listPaiement = new ArrayList<>();
				PaiementPersistant paie = new PaiementPersistant();
				paie.setMontant(chargeBean.getMontant());
				paie.setSens("D");
				paie.setOpc_compte_bancaire(compteB);
				listPaiement.add(paie);
				chargeBean.setList_paiement(listPaiement);
			}
			
		} else{
//			if("D".equals(type)) {
//				List<CompteVentilationPersistant> listVentilIhm = getDataCompteList(httpUtil);
//				List<CompteVentilationPersistant> listVentil = new ArrayList<>();
//				if (chargeBean.getId() != null) {
//					ChargeDiversBean depBean = chargeDiversService.findById(chargeBean.getId());
//					listVentil = depBean.getList_ventilation();
//					listVentil.clear();
//				}
//				if(listVentilIhm != null){
//					listVentil.addAll(listVentilIhm);
//				}
//				chargeBean.setList_ventilation(listVentil);
//				
//				if(chargeBean.getList_ventilation() != null){
//					BigDecimal totalMtt = null;
//					for(CompteVentilationPersistant ventill : chargeBean.getList_ventilation()){
//						totalMtt = BigDecimalUtil.add(totalMtt, ventill.getMontant());
//					}
//					chargeBean.setMontant(totalMtt);
//				}
//			}
			//
			chargeBean.setSens(type);
			chargeBean.setSource("B");// Backoffice
		}
		chargeBean.setOpc_user(ContextAppli.getUserBean());
		
		chargeBean.setList_paiement((List<PaiementPersistant>) httpUtil.getMenuAttribute("PAIEMENT_DATA"));
		
		if(httpUtil.getMenuAttribute("IS_TRV_MNU") != null) {
			chargeBean.setOpc_travaux((TravauxPersistant) httpUtil.getMenuAttribute("IS_TRV_MNU"));
		}
		
		super.work_merge(httpUtil);
		
		if(isDepense) {
			managePieceJointe(httpUtil, chargeBean.getId(), "depense");
		}
		
		if(isFromCaisse){
			httpUtil.writeResponse("MSG_CUSTOM:La dépense est ajoutée.");
		}
		if(httpUtil.getMenuAttribute("IS_SUB_ADD") != null) {
			httpUtil.setDynamicUrl(""+httpUtil.getMenuAttribute("IS_SUB_ADD"));
		}
	}
	
	/**
	 * @param httpUtil
	 */
	private List<CompteVentilationPersistant> getDataCompteList(ActionUtil httpUtil) {
		List<CompteVentilationPersistant> listVentillation = (List<CompteVentilationPersistant>) httpUtil.buildListBeanFromMap("opc_compte.id", CompteVentilationPersistant.class, "eaiid", "idxIhm",
															"opc_compte.id", "montant", "montant_tva");
		int idx = 1;
		for (CompteVentilationPersistant compteVentilationPersistant : listVentillation) {
			if(compteVentilationPersistant.getMontant() == null){
				MessageService.addBannerMessage("Ventilation de la dépense : Ligne "+idx+" : Le montant est obligatoire.");
			}
			if(compteVentilationPersistant.getMontant_tva() == null){
				MessageService.addBannerMessage("Ventilation de la dépense : Ligne "+idx+" : Le montant TVA est obligatoire.");
			}
			idx++;
		}
		if(MessageService.isError()){
			return null;
		}
		
		return listVentillation;
	}
	
	@Override
	public void work_delete(ActionUtil httpUtil) {
		Long workId = httpUtil.getWorkIdLong();
		super.work_delete(httpUtil);
		//
		String type = (String) httpUtil.getMenuAttribute("tpD");
		if(type.equals("D")) {
			manageDeleteImage(workId, "depense");
		}
	}
	
	@Override
	public void work_find(ActionUtil httpUtil) {
		 // Retour aux travaux si on vient de ce module
		 if(httpUtil.getMenuAttribute("IS_TRV_MNU") != null){
			httpUtil.setRequestAttribute("bck",true); 
		   	httpUtil.setDynamicUrl("stock.travaux.work_init_update");
		   	return;
		 }

		String type = (String)httpUtil.getMenuAttribute("tpD");
		Date dateRef = chargeDiversService.getMaxDate(type);
		
		httpUtil.setMenuAttribute("IS_GRP_VIEW", false);
		
		boolean isFilterAct = StringUtil.isTrue(httpUtil.getRequest().getParameter("is_filter_act"));
		// Ajouter le paramétre dans la requête
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_chargeDivers");
		Map<String, Object> formCriterion = cplxTable.getFormBean().getFormCriterion();
		formCriterion.put("sens", type);
		
		String immoFilter = StringUtil.isNotEmpty(httpUtil.getRequest().getParameter("immoFilter")) ? 
				httpUtil.getRequest().getParameter("immoFilter") : null;
		String fournFilter = StringUtil.isNotEmpty(httpUtil.getRequest().getParameter("fournFilter")) ? 
				httpUtil.getRequest().getParameter("fournFilter") : null;
		String consoFilter = StringUtil.isNotEmpty(httpUtil.getRequest().getParameter("consoFilter")) ? 
				httpUtil.getRequest().getParameter("consoFilter") : null;
		String travauxFilter = StringUtil.isNotEmpty(httpUtil.getRequest().getParameter("travauxFilter")) ? 
				httpUtil.getRequest().getParameter("travauxFilter") : null;

		//----------------------------- Date -------------------------
		Date dateDebut = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateDebut"));
		Date dateFin = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateFin"));
		//
		if(httpUtil.getRequest().getParameter("is_fltr") == null) {
			dateDebut = DateUtil.stringToDate(httpUtil.getParameter("dateDebut"));
			dateFin = DateUtil.stringToDate(httpUtil.getParameter("dateFin"));
		}
		
		if(dateDebut == null) {
			dateDebut = (httpUtil.getMenuAttribute("dateDebut")==null ? dateRef : (Date)httpUtil.getMenuAttribute("dateDebut"));
			dateFin = (httpUtil.getMenuAttribute("dateFin")==null ? dateRef : (Date)httpUtil.getMenuAttribute("dateFin"));
			immoFilter = (httpUtil.getMenuAttribute("immoFilter")==null ? immoFilter : (String)httpUtil.getMenuAttribute("immoFilter"));
			fournFilter = (httpUtil.getMenuAttribute("fournFilter")==null ? fournFilter : (String)httpUtil.getMenuAttribute("fournFilter"));
			consoFilter = (httpUtil.getMenuAttribute("consoFilter")==null ? consoFilter : (String)httpUtil.getMenuAttribute("consoFilter"));
			travauxFilter = (httpUtil.getMenuAttribute("travauxFilter")==null ? travauxFilter : (String)httpUtil.getMenuAttribute("travauxFilter"));
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
		httpUtil.setMenuAttribute("immoFilter", immoFilter);
		httpUtil.setMenuAttribute("fournFilter", fournFilter);
		httpUtil.setMenuAttribute("consoFilter", consoFilter);
		httpUtil.setMenuAttribute("travauxFilter", travauxFilter);
		
		if(!isFilterAct){
			formCriterion.put("dateDebut", dateDebut);
			formCriterion.put("dateFin", dateFin);
		} else{
			formCriterion.remove("dateDebut");
			formCriterion.remove("dateFin");
		}
		//-----------------------------------------------------------
		String reqCD = "from ChargeDiversPersistant chargeDivers"
				+ " where chargeDivers.sens = '{sens}' and (chargeDivers.is_groupant is null or chargeDivers.is_groupant=0) " 
				+ "and chargeDivers.date_mouvement>='[dateDebut]' "
				+ "and chargeDivers.date_mouvement<='[dateFin]' "
				+ "and chargeDivers.opc_travaux.id='[travauxId]' ";
		
		if(immoFilter != null) {
			if(immoFilter.equals("I")) {// Avec immobilisation
				reqCD += "and chargeDivers.nbr_annee_amo>=0 ";
			} else if(immoFilter.equals("NI")) {// Sans immo
				reqCD += "and (chargeDivers.nbr_annee_amo is null or chargeDivers.nbr_annee_amo=0) ";
			}
		}
		if(fournFilter != null) {
			reqCD += " and chargeDivers.opc_fournisseur="+fournFilter;
		}
		if(consoFilter != null) {
			reqCD += " and chargeDivers.opc_famille_consommation="+consoFilter;
		}
		if(travauxFilter != null) {
			if(travauxFilter.equals("T")) {// Avec travaux
				reqCD += " and chargeDivers.opc_travaux!=null ";
			} else if(travauxFilter.equals("NT")) {// Sans tarvaux
				reqCD += " and chargeDivers.opc_travaux is null ";
			}
		}
		reqCD += " order by chargeDivers.date_mouvement desc";
		
		List<ChargeDiversPersistant> listData = (List<ChargeDiversPersistant>) chargeDiversService.findByCriteria(cplxTable, reqCD);
		chargeDiversService.refreshEntities(listData);
		
		Map<Long, String> mapDateGroupement = new HashMap<>();
		for(ChargeDiversPersistant mouvementPersistant : listData) {
			if(mouvementPersistant.getCharge_group_id() != null){
	   			ChargeDiversBean mvmGroupant = chargeDiversService.findById(mouvementPersistant.getCharge_group_id());
	   			if(mvmGroupant == null) {
	   				continue;
	   			}
				mapDateGroupement.put(mvmGroupant.getId(), DateUtil.dateToString(mvmGroupant.getDate_mouvement()));
	   		}
		}
		// Calcul du total montants sans pagination
	   	BigDecimal totalTtcAll = null;
	   	List<ChargeDiversPersistant> listDataAll = (List<ChargeDiversPersistant>) chargeDiversService.findByCriteria(cplxTable, reqCD, false);
		//
	   	if(listDataAll.size() > 0) {
		   	for (ChargeDiversPersistant mvmStockViewP : listDataAll) {
			   	totalTtcAll = BigDecimalUtil.add(totalTtcAll, mvmStockViewP.getMontant());
		   	}
		}
		
	   	httpUtil.setRequestAttribute("mapDateGroupement", mapDateGroupement);
	   	httpUtil.setRequestAttribute("totalTtc", totalTtcAll);
		httpUtil.setRequestAttribute("list_chargeDivers", listData);
	
	   	//
		httpUtil.setDynamicUrl("/domaine/stock/chargeDivers_list.jsp");
	}

	/**
	 * @param httpUtil
	 */
	public void find_charge_groupe(ActionUtil httpUtil){
		httpUtil.setMenuAttribute("IS_GRP_VIEW", true);
		httpUtil.setMenuAttribute("tpR", "CH");
		
		String type = (String)httpUtil.getMenuAttribute("tpD");
		boolean isFilterAct = StringUtil.isTrue(httpUtil.getRequest().getParameter("is_filter_act"));
		// Ajouter le paramétre dans la requête
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_chargeDivers");
		Map<String, Object> formCriterion = cplxTable.getFormBean().getFormCriterion();
		formCriterion.put("sens", type);
		
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
		// Calcul du total montants sans pagination
		BigDecimal totalTtcAll = null;
	   	List<ChargeDiversPersistant> listDataAll = (List<ChargeDiversPersistant>) chargeDiversService.findByCriteriaByQueryId(cplxTable, "chargeDivers_groupe_find", false);
	   	Map<Long, List<ChargeDiversPersistant>> mapTotaux = chargeDiversService.getMapChargeGroupe(listDataAll);
		//
	   	for (ChargeDiversPersistant mvmStockViewP : listDataAll) {
	   		List<ChargeDiversPersistant> listEnfants = mapTotaux.get(mvmStockViewP.getId());
	   		//
	   		if(listEnfants != null){
	   			for (ChargeDiversPersistant mvmEnfant : listEnfants) {
	   				totalTtcAll = BigDecimalUtil.add(totalTtcAll, mvmEnfant.getMontant());
				}
	   		}
	   	}
		
	   	httpUtil.setRequestAttribute("totalTtc", totalTtcAll);
	   	
		List<ChargeDiversPersistant> listDataGroupe = (List<ChargeDiversPersistant>) chargeDiversService.findByCriteriaByQueryId(cplxTable, "chargeDivers_groupe_find");
		List<ChargeDiversPersistant> listDataTemp = new ArrayList<>();
		//
	   	for (ChargeDiversPersistant mvmStockViewP : listDataGroupe) {
	   		ChargeDiversPersistant mvmStockViewPTemp = (ChargeDiversPersistant) ReflectUtil.cloneBean(mvmStockViewP);
	   		
	   		totalTtcAll = null;
   			List<ChargeDiversPersistant> listEnfants = mapTotaux.get(mvmStockViewP.getId());
   			if(listEnfants != null){
	   			for (ChargeDiversPersistant mvmEnfant : listEnfants) {
	   				totalTtcAll = BigDecimalUtil.add(totalTtcAll, mvmEnfant.getMontant());
				}
   			}
   			mvmStockViewPTemp.setMontant(totalTtcAll);
   			mvmStockViewPTemp.setList_groupe(listEnfants);
   			
	   		listDataTemp.add(mvmStockViewPTemp);
	   	}
	   	
		cplxTable.setDataExport(listDataTemp);
		httpUtil.setRequestAttribute("list_charge", listDataTemp);
	   	//
		httpUtil.setDynamicUrl("/domaine/stock/chargeDivers_groupe_list.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void generer_numBL(ActionUtil httpUtil) {
		
		String type = (String)httpUtil.getMenuAttribute("tpD");
		String numBl = chargeDiversService.generate_numBl(type);
		httpUtil.writeResponse(numBl);

	}
	
	/**
	 * Redirection vers la page d'enregistrement des informations sur l'automate
	 * @param httpUtil
	 */
	public void automateCharge(ActionUtil httpUtil){
		Long idChargeDivers = httpUtil.getWorkIdLong();
		httpUtil.setViewBean(chargeDiversService.findById(idChargeDivers));
		httpUtil.setDynamicUrl("/domaine/stock/chargeDivers_automate.jsp");
	}
	
	/**
	 *  Enegistrer les informations de l'automate
	 * @param httpUtil
	 */
	@WorkForward(useBean=true)
	public void update_automate(ActionUtil httpUtil){
	
		Long id = httpUtil.getWorkIdLong();
		ChargeDiversPersistant charge =  chargeDiversService.findById(id);
		ChargeDiversBean cdb = (ChargeDiversBean) httpUtil.getViewBean();

		if(cdb.getId() == null){
		chargeDiversService.update_automate(charge,cdb.getDate_debut_auto(),cdb.getDate_fin_auto(),cdb.getFrequence());
		
		}else{
			chargeDiversService.update(cdb);
		}
		work_find(httpUtil);
		
	}
	
	/**
	 * @param httpUtil
	 */
	public void editTrDep(ActionUtil httpUtil){
		Long mvmId = httpUtil.getLongParameter("art");
		ChargeDiversBean mvmBean = chargeDiversService.findById(mvmId);
		
		httpUtil.setRequestAttribute("mouvementBean", mvmBean);
		httpUtil.setDynamicUrl("/domaine/stock/chargeDivers_tr_consult.jsp");
	}
	
	/**
	 *  Activer ou désactiver une automate
	 * @param httpUtil
	 */
	public void controleAutomate(ActionUtil httpUtil){
		Long id = httpUtil.getWorkIdLong();
		chargeDiversService.controleAutomate(id);
		work_find(httpUtil);
		
	}
	
	public void delete_automate(ActionUtil httpUtil){
		Long id = httpUtil.getWorkIdLong();
		ChargeDiversPersistant charge =  chargeDiversService.findById(id);
		chargeDiversService.deleteAutomatisation(charge);
		work_find(httpUtil);
	}
	
	/**
	 * @param httpUtil
	 */
	public void editTrMvm(ActionUtil httpUtil){
		Long mvmId = httpUtil.getLongParameter("art");
		IChargeDiversService chargeDiversService= (IChargeDiversService) ServiceUtil.getBusinessBean(IChargeDiversService.class);
		ChargeDiversBean mvmBean = chargeDiversService.findById(mvmId);
		if(mvmBean == null) { //si on vient depuis pointage chèques
			PaiementPersistant paiementP = (PaiementPersistant) chargeDiversService.findById(PaiementPersistant.class, mvmId);
			mvmBean = chargeDiversService.findById(paiementP.getElementId());
		}
		
		httpUtil.setRequestAttribute("mouvementBean", mvmBean);
		httpUtil.setDynamicUrl("/domaine/stock/chargeDivers_tr_consult.jsp");
	}
	
	public void downloadPieceJointe(ActionUtil httpUtil) {
		httpUtil.manageInputFileView("depense");
	}
	
	public void load_labels(ActionUtil httpUtil) {
		boolean isSub = (httpUtil.getParameter("isSub") != null);
		String type = (String) httpUtil.getMenuAttribute("tpD");
		
		if(isSub) {
			List<LabelsGroupPersistant> listLib = (List<LabelsGroupPersistant>) httpUtil.buildListBeanFromMap(
					"libelle",
					LabelsGroupPersistant.class, "is_groupe", "libelle");
			//
			chargeDiversService.mergeLabels(listLib, ("D".equals(type) ? "DEP" : "REC"));
		}
		
		List<LabelsGroupPersistant> listDet = chargeDiversService.getLibelleParametres("D".equals(type) ? "DEP" : "REC"); 
		httpUtil.setRequestAttribute("list_charge_lib", listDet);
		httpUtil.setDynamicUrl("/domaine/stock/chargeDivers_labels.jsp");
	}
	
	public void work_post(ActionUtil httpUtil){
		Long depid = null;  
		String type = (String) httpUtil.getMenuAttribute("tpD");
		ChargeDiversBean viewBean = (ChargeDiversBean)httpUtil.getViewBean();
		if(viewBean != null && viewBean.getId() != null){
			depid = viewBean.getId();
		}
		
		String action = httpUtil.getAction();
		if(httpUtil.getViewBean() != null && (ActionConstante.INIT_UPDATE.equals(action) || ActionConstante.EDIT.equals(action))) {
			httpUtil.setMenuAttribute("PAIEMENT_DATA", ((ChargeDiversBean)httpUtil.getViewBean()).getList_paiement());
		}
		
		List<FournisseurChequeBean> listChequeFournisseur = fournisseurChequeService.getListChequeFournisseurActifs("CHARGE", depid);
		httpUtil.setRequestAttribute("listChequeFournisseur", listChequeFournisseur);
		//
		manageDataForm(httpUtil, "CHARGE_"+httpUtil.getMenuAttribute("tpD"));
		
		
		List<LabelsGroupPersistant> listDet = chargeDiversService.getLibelleParametres("D".equals(type) ? "DEP" : "REC");
		httpUtil.setRequestAttribute("list_charge_lib", listDet);
	}
}
