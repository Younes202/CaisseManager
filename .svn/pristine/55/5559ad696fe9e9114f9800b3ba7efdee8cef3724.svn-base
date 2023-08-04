package appli.controller.domaine.caisse.action;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.hibernate.criterion.Order;

import com.itextpdf.text.DocumentException;

import appli.controller.domaine.caisse.bean.CaisseMouvementBean;
import appli.controller.domaine.stock.bean.MouvementBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_MOUVEMENT_ENUM;
import appli.model.domaine.caisse.service.ICaisseMouvementService;
import appli.model.domaine.caisse.service.IJourneeService;
import appli.model.domaine.compta.persistant.PaiementPersistant;
import appli.model.domaine.personnel.persistant.ClientPersistant;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.persistant.ChargeDiversPersistant;
import appli.model.domaine.stock.persistant.MouvementPersistant;
import appli.model.domaine.stock.service.IArticleService;
import appli.model.domaine.stock.service.IMouvementService;
import appli.model.domaine.stock.service.impl.FactureVentePDF;
import appli.model.domaine.util_srv.printCom.PrintTicketPdf;
import appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import appli.model.domaine.vente.persistant.JourneePersistant;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.ContextGloabalAppli;
import framework.controller.annotation.WorkController;
import framework.controller.annotation.WorkForward;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.DateUtil.TIME_ENUM;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.StringUtil;

@WorkController(nameSpace = "stock-caisse", bean = MouvementBean.class, jspRootPath = "/domaine/stock/")
public class MouvementStockAction extends ActionBase {
	@Inject
	private ICaisseMouvementService caisseMvmService;
	@Inject
	private IMouvementService mouvementService;
	@Inject
	private IArticleService articleService;
	@Inject
	private IJourneeService journeeService;
	
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
		
		List<MouvementPersistant> listData = (List<MouvementPersistant>) mouvementService.findByCriteriaByQueryId(cplxTable, "mouvement_tva_find");
		List<MouvementPersistant> listDataAll = (List<MouvementPersistant>) mouvementService.findByCriteriaByQueryId(cplxTable, "mouvement_tva_find", false);
		List<CaisseMouvementPersistant> listCaisseMvm = (List<CaisseMouvementPersistant>) caisseMvmService.getListMouvementCaisse(dateDebut, dateFin);
		// Charges divers
		List<ChargeDiversPersistant> listCharge = (List<ChargeDiversPersistant>) mouvementService.findByCriteriaByQueryId(cplxTableCharge, "chargeDivers_tva_find");
		List<ChargeDiversPersistant> listChargeAll = (List<ChargeDiversPersistant>) mouvementService.findByCriteriaByQueryId(cplxTableCharge, "chargeDivers_tva_find", false);
		
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
			if(StringUtil.isEmpty(caisseMvmP.getMvm_stock_ids())){
				continue;
			}
			mapCaisseMvm.put(";"+caisseMvmP.getMvm_stock_ids()+";", caisseMvmP.getMtt_commande_net());
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
				BigDecimal mttTva = BigDecimalUtil.divide(BigDecimalUtil.multiply(mttCaisse, tauxVente), BigDecimalUtil.get(100));
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
	
	
	
	
	public void initRetourCaisse(ActionUtil httpUtil){
		Long caisseMvmId = httpUtil.getWorkIdLong();
		//
		if(caisseMvmId != null) {
			CaisseMouvementBean caisseMvmB = caisseMvmService.findById(caisseMvmId);
//			CaisseMouvementPersistant caisseMvmP = caisseMvmService.getCommandeByReference(mvmBean.getRetour_ref_cmd());
//			httpUtil.setRequestAttribute("caisseMouvement_id", caisseMvmP.getId());
			httpUtil.setViewBean(caisseMvmB);
			
			if(StringUtil.isEmpty(httpUtil.getParameter("isUpd"))){
				httpUtil.setFormReadOnly(true);
				httpUtil.setRequestAttribute("isEdit", true);
				List<ArticlePersistant> listArticle = articleService.getListArticleNonStock(true);
				httpUtil.setRequestAttribute("listArticle", listArticle);
			} else {
				httpUtil.setRequestAttribute("ref_commande", caisseMvmB.getRef_commande());
				CaisseMouvementBean viewBean = caisseMvmService.findById(caisseMvmB.getId());
				httpUtil.setRequestAttribute("caisseMouvement", viewBean);
				httpUtil.setRequestAttribute("isCodeFounded", true);
				httpUtil.setRequestAttribute("isUpd", true);
			}
		}
		
		httpUtil.setRequestAttribute("editFromPaiement", true);
		httpUtil.setDynamicUrl("/domaine/stock/retourCommandeCaisse_edit.jsp");
	}

	@WorkForward(useFormValidator=true,  bean=MouvementBean.class, useBean=true)
	public void mergeRetourCaisse(ActionUtil httpUtil) {
		MouvementBean mouvementBean = (MouvementBean) httpUtil.getViewBean();
		mouvementBean.setId(httpUtil.getWorkIdLong());
		String type = (String)httpUtil.getMenuAttribute("type_mvmnt");
		mouvementBean.setType_mvmnt(type);
		
		mouvementBean.setList_paiement((List<PaiementPersistant>) httpUtil.getMenuAttribute("PAIEMENT_DATA"));
		if(mouvementBean.getList_paiement() == null && mouvementBean.getList_paiement().size() == 0) {
			MessageService.addBannerMessage(MSG_TYPE.ERROR, "Veuillez ajouter un mode de paiement.");
			return;
		}
		
		//detail commande
		List<CaisseMouvementArticlePersistant> listCaisseMvmArt = (List<CaisseMouvementArticlePersistant>) httpUtil.buildListBeanFromMap("quantite",
				CaisseMouvementArticlePersistant.class, "opc_article.id", "quantite", "mtt_total", "prix_unitaire", "old_qte");
		//si on augmente la quantite d'une ligne -> erreur
		for (CaisseMouvementArticlePersistant caisseMmArtP : listCaisseMvmArt) {
			if(caisseMmArtP.getOld_qte().compareTo(caisseMmArtP.getQuantite()) == -1) {
				MessageService.addBannerMessage(MSG_TYPE.ERROR, "La quantité de l'article : "+caisseMmArtP.getLibelle()+" dépasse sa quantité initiale.");
				return;
			}
		}
		
		caisseMvmService.mergeRetourCaisse(mouvementBean, listCaisseMvmArt);
	
		findRetourCmdCaisse(httpUtil);
	}
	
	public void findRetourCmdCaisse(ActionUtil httpUtil) {
		Date dateRef = null;
		JourneePersistant lastJrn = journeeService.getLastJournee();
		if(lastJrn != null){
			dateRef = lastJrn.getDate_journee();
		} else{
			dateRef = new Date();
		}
		
		// Ajouter le paramétre dans la requête
		boolean isFilterAct = StringUtil.isTrue(httpUtil.getParameter("is_filter_act"));
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_retourClient");
		Map<String, Object> formCriterion = cplxTable.getFormBean().getFormCriterion();
		formCriterion.put("type", "rt");
		//----------------------------- Date -------------------------
		Date dateDebut = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateDebut"));
		Date dateFin = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateFin"));
		
		if(dateDebut == null) {
			dateDebut = (httpUtil.getMenuAttribute("dateDebut")==null ? dateRef : (Date)httpUtil.getMenuAttribute("dateDebut"));
			dateFin = (httpUtil.getMenuAttribute("dateFin")==null ? dateRef : (Date)httpUtil.getMenuAttribute("dateFin"));
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
		String req = "from CaisseMouvementPersistant caisseMouvement "
				+ "where caisseMouvement.date_vente>='[dateDebut]' and caisseMouvement.date_vente<='[dateFin]' "
				+ "and caisseMouvement.is_retour is not null and caisseMouvement.is_retour=1 "
	   			+ "order by caisseMouvement.date_vente desc";
		
		List<CaisseMouvementPersistant> listData = (List<CaisseMouvementPersistant>) mouvementService.findByCriteria(cplxTable, req);
		List<CaisseMouvementPersistant> listDataAll = (List<CaisseMouvementPersistant>) mouvementService.findByCriteria(cplxTable, req, false);
		   	
	   	BigDecimal totalTtc = null;
	   	
	   	for (CaisseMouvementPersistant caisseMouvementP : listDataAll) {
	   		totalTtc = BigDecimalUtil.add(totalTtc, caisseMouvementP.getMtt_commande_net());
		}
	   	httpUtil.setRequestAttribute("totalTtc", totalTtc);
		
		httpUtil.setRequestAttribute("list_retourClient", listData);
		httpUtil.setDynamicUrl("/domaine/stock/retourCommandeCaisse_list.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void manageRetourCaisseCommande(ActionUtil httpUtil) {
		String codeBarre = httpUtil.getParameter("cb");
		String ref_commande = httpUtil.getParameter("ref");
		httpUtil.removeUserAttribute("RETOUR_CMD");
		
//		if(StringUtil.isNotEmpty(codeBarre)) {
//			if(codeBarre.length() <= 12){
//				MessageService.addBannerMessage("Le code barre n'est pas valide");
//				return;
//			} 
//		}
		CaisseMouvementPersistant commandeP = null;
		if(StringUtil.isNotEmpty(codeBarre)) {
			String cb = (codeBarre.length() > 12 ? codeBarre.substring(0, 12) : codeBarre);
			commandeP = caisseMvmService.getCommandeByCodeBarre(cb);
			if(commandeP == null){
				MessageService.addBannerMessage("Aucune commande ne correspond à ce code barre");
				return;
			}
		} else if(StringUtil.isNotEmpty(ref_commande)){
			commandeP = caisseMvmService.getCommandeByReference(ref_commande);
			if(commandeP == null){
				MessageService.addBannerMessage("Aucune commande ne correspond à cette référence");
				return;
			}
		}
		if(commandeP != null) {
				httpUtil.setUserAttribute("RETOUR_CMD", commandeP.getRef_commande());
//				Map<String, Object> params = (Map)httpUtil.getRequestAttribute(ProjectConstante.WORK_PARAMS);
//				params.put(ProjectConstante.WORK_ID, commandeP.getId());
//				new CaisseWebBaseAction().restituerInfosCommande(httpUtil);
//				return;
//			} else{
//				CaisseMouvementBean viewBean = caisseMvmService.findById(commandeP.getId());
//				httpUtil.setRequestAttribute("ref_commande", ref_commande);
//				httpUtil.setRequestAttribute("caisseMouvement", viewBean);
//				httpUtil.setRequestAttribute("isCodeFounded", true);
//			}
//		} else{// Saisie libre
					
//			return;
		}
		new CaisseWebBaseAction().initNewCommande(httpUtil);
		httpUtil.setUserAttribute("IS_RETOUR", true);	
		httpUtil.addJavaScript("$('#home_lnk').trigger('click');");
		httpUtil.setDynamicUrl(new CaisseWebBaseAction().getCommande_detail_path(httpUtil));
		
//		httpUtil.setRequestAttribute("editFromPaiement", true);
		
//		httpUtil.setDynamicUrl("/domaine/stock/retourCommandeCaisse_edit.jsp");
	}
	
	/**
	 * @param httpUtil
	 * @throws IOException
	 * @throws DocumentException
	 */
	public void editFactureVentePDF(ActionUtil httpUtil) throws IOException, DocumentException{
		if(httpUtil.getParameter("refresh") != null){
			init_pdf_facture(httpUtil);
			return;
		}
		String source = httpUtil.getParameter("src");// Source si depuis mouvement caisse ou mouvement
		Long mvmId = Long.valueOf(EncryptionUtil.decrypt(""+httpUtil.getParameter("mvm")));
		
		Long clientId = httpUtil.getLongParameter("client");
		String numFacture = httpUtil.getParameter("numero");
		String dateFacture = httpUtil.getParameter("dateFac");
		String nomClient = httpUtil.getParameter("nomClient");
		String adresseClient = httpUtil.getParameter("adresseClient");
		String iceClient = httpUtil.getParameter("iceClient");
		
		ClientPersistant cliIhm = new ClientPersistant();
		if(clientId != null) {
			ClientPersistant clieP = mouvementService.findById(ClientPersistant.class, clientId);
			cliIhm.setNumero(clieP.getNumero());
		} else {
			cliIhm.setNumero("--");				
		}
		cliIhm.setNom(nomClient);
		cliIhm.setAdresse_compl(adresseClient);
		cliIhm.setIce(iceClient);
		
		if(StringUtil.isEmpty(source) || source.equals("cai")){
			CaisseMouvementPersistant caisseWebP = (CaisseMouvementPersistant) mouvementService.findById(CaisseMouvementPersistant.class, mvmId);
			
			FactureVentePDF pdfFacture = new FactureVentePDF(caisseWebP, StringUtil.isTrue(httpUtil.getParameter("isDet")));
			pdfFacture.getFactureBean().setOpc_client(cliIhm);
			pdfFacture.getFactureBean().setNumero_facture(numFacture);
			
			if(StringUtil.isEmpty(dateFacture)) {
				pdfFacture.getFactureBean().setDate_facture(caisseWebP.getDate_vente());
			} else {
				pdfFacture.getFactureBean().setDate_facture(DateUtil.stringToDate(dateFacture));
			}
			
			File exportPdf = pdfFacture.exportPdf();
			// Archiver le pdf dans la GED
			mouvementService.archiverPdfFactureGed(cliIhm, pdfFacture.getFactureBean().getNumero_facture(), pdfFacture.getFactureBean().getDate_facture(), exportPdf);
			
			if(source.equals("cai")) {// Direct impression
				
				String imprimanteFacture = ContextGloabalAppli.getGlobalConfig("PRINT_FACTURE_PRINTER"); 
				if(StringUtil.isNotEmpty(imprimanteFacture)) {
					PrintTicketPdf.printA4Pdf(imprimanteFacture, exportPdf);	
				}
				httpUtil.writeResponse("ok");	
			} else {
				httpUtil.doDownload(exportPdf, true);				
			}
		} else{
			MouvementPersistant mvmP = (MouvementPersistant)mouvementService.findById(MouvementPersistant.class, mvmId);
			
			FactureVentePDF pdfFacture = new FactureVentePDF(mvmP, StringUtil.isTrue(httpUtil.getParameter("isDet")));
			pdfFacture.getFactureBean().setOpc_client(cliIhm);
			pdfFacture.getFactureBean().setNumero_facture(numFacture);
			
			if(StringUtil.isEmpty(dateFacture)) {
				pdfFacture.getFactureBean().setDate_facture(mvmP.getDate_mouvement());
			} else {
				pdfFacture.getFactureBean().setDate_facture(DateUtil.stringToDate(dateFacture));
			}
			
			File exportPdf = pdfFacture.exportPdf();
			// Archiver le pdf dans la GED
			mouvementService.archiverPdfFactureGed(cliIhm, pdfFacture.getFactureBean().getNumero_facture(), pdfFacture.getFactureBean().getDate_facture(), exportPdf);
			httpUtil.doDownload(exportPdf, true);
		}
	}
	
	/**
	 * @param httpUtil
	 */
	public void init_pdf_facture(ActionUtil httpUtil){
		String source = httpUtil.getParameter("src");// Source si depuis mouvement caisse ou mouvement
		httpUtil.setRequestAttribute("mvmId", EncryptionUtil.encrypt(httpUtil.getWorkId()));
		httpUtil.setRequestAttribute("src_mvm", source);
		httpUtil.setRequestAttribute("listClient", mouvementService.findAll(ClientPersistant.class, Order.asc("nom")));
		httpUtil.setRequestAttribute("dateFac", new Date());
		
		if("cai".equals(source) && httpUtil.getWorkIdLong() != null) {
			CaisseMouvementPersistant cmP = caisseMvmService.findById(httpUtil.getWorkIdLong());
			if(cmP.getOpc_client() != null) {
				httpUtil.setRequestAttribute("currClient", cmP.getOpc_client());
				httpUtil.setRequestAttribute("currCli", cmP.getOpc_client().getId());
			}
		}
		
		Long clientSelect = httpUtil.getLongParameter("client");// Si event change combo
		if(clientSelect != null){
			ClientPersistant clientP = (ClientPersistant) mouvementService.findById(ClientPersistant.class, clientSelect);
			httpUtil.setRequestAttribute("currClient", clientP);
		}
		
		if(httpUtil.getMenuAttribute("IS_MENU_CMLIENT") != null){
			clientSelect = (Long)httpUtil.getMenuAttribute("clientId");
			ClientPersistant clientP = (ClientPersistant) mouvementService.findById(ClientPersistant.class, clientSelect);
			httpUtil.setRequestAttribute("currClient", clientP);
		}
		//
		httpUtil.setDynamicUrl("/domaine/stock/mouvement_facture_pdf.jsp");
	}
	
	public void init_retour_vente(ActionUtil httpUtil) {
		
	}
	
	public void find_retour_client(ActionUtil httpUtil) {
		// Ajouter le paramétre dans la requête
		boolean isFilterAct = StringUtil.isTrue(httpUtil.getParameter("is_filter_act"));
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_retourClient");
		Map<String, Object> formCriterion = cplxTable.getFormBean().getFormCriterion();
		formCriterion.put("type", "rt");
		//----------------------------- Date -------------------------
		Date dateDebut = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateDebut"));
		Date dateFin = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateFin"));
		
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
		
		List<MouvementPersistant> listData = (List<MouvementPersistant>) mouvementService.findByCriteriaByQueryId(cplxTable, "mouvement_find");
		   	
	   	BigDecimal totalHt = null;
	   	BigDecimal totalTtc = null;
	   	
	   	String req = "select sum(montant_ht), sum(montant_ttc) from MouvementPersistant mouvement where type_mvmnt='[type]' "
	   			+ "and mouvement.date_mouvement>='[dateDebut]' and mouvement.date_mouvement<='[dateFin]' "
	   			+ "order by mouvement.date_mouvement desc";
	   	List<Object[]> listDataAll = (List<Object[]>) mouvementService.findByCriteria(cplxTable, req, false);
	   	for (Object[] mvmStockViewP : listDataAll) {
	   		totalHt = (BigDecimal) mvmStockViewP[0];
	   		totalTtc = (BigDecimal) mvmStockViewP[1];
		}
	   	httpUtil.setRequestAttribute("totalHt", totalHt);
	   	httpUtil.setRequestAttribute("totalTtc", totalTtc);
		
		httpUtil.setRequestAttribute("list_retourClient", listData);
		httpUtil.setDynamicUrl("/domaine/stock/retourClient_list.jsp");
	}
}
