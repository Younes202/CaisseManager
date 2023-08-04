package appli.model.domaine.util_srv.raz;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Named;
import javax.persistence.Query;

import org.apache.commons.io.FileUtils;

import appli.controller.domaine.administration.bean.GedBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.SOFT_ENVS;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_COMMANDE;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_LIGNE_COMMANDE;
import appli.controller.util_ctrl.MouvementDetailPrintBean;
import appli.model.domaine.administration.persistant.GedFichierPersistant;
import appli.model.domaine.administration.persistant.GedPersistant;
import appli.model.domaine.administration.persistant.UserPersistant;
import appli.model.domaine.administration.service.IGedService;
import appli.model.domaine.administration.service.ITreeService;
import appli.model.domaine.administration.service.impl.EtatFinanceValidator;
import appli.model.domaine.caisse.service.IJourneeService;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.persistant.FamilleCuisinePersistant;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.stock.persistant.FamilleStockPersistant;
import appli.model.domaine.stock.service.IArticleService;
import appli.model.domaine.stock.service.IFamilleService;
import appli.model.domaine.stock.service.IMouvementService;
import appli.model.domaine.stock.service.impl.RepartitionBean;
import appli.model.domaine.util_srv.printCom.raz.PrintRazArticle;
import appli.model.domaine.util_srv.printCom.raz.PrintRazBoisson;
import appli.model.domaine.util_srv.printCom.raz.PrintRazEmploye;
import appli.model.domaine.util_srv.printCom.raz.PrintRazGlobale;
import appli.model.domaine.util_srv.printCom.raz.PrintRazJournee;
import appli.model.domaine.util_srv.printCom.raz.PrintRazLivraison;
import appli.model.domaine.util_srv.printCom.raz.PrintRazModePaiement;
import appli.model.domaine.util_srv.printCom.raz.PrintRazPoste;
import appli.model.domaine.util_srv.printCom.raz.PrintRazSocieteLivraison;
import appli.model.domaine.util_srv.raz.html.RazArticleHTML;
import appli.model.domaine.util_srv.raz.html.RazBoissonHTML;
import appli.model.domaine.util_srv.raz.html.RazEmployeHTML;
import appli.model.domaine.util_srv.raz.html.RazGlobaleHTML;
import appli.model.domaine.util_srv.raz.html.RazJourHTML;
import appli.model.domaine.util_srv.raz.html.RazLivraisonHTML;
import appli.model.domaine.util_srv.raz.html.RazPaiementHTML;
import appli.model.domaine.util_srv.raz.html.RazPosteHTML;
import appli.model.domaine.util_srv.raz.html.RazSocieteLivraisonHTML;
import appli.model.domaine.util_srv.raz.pdf.RazArticlePDF;
import appli.model.domaine.util_srv.raz.pdf.RazBoissonPDF;
import appli.model.domaine.util_srv.raz.pdf.RazEmployePDF;
import appli.model.domaine.util_srv.raz.pdf.RazGlobalePDF;
import appli.model.domaine.util_srv.raz.pdf.RazJourPDF;
import appli.model.domaine.util_srv.raz.pdf.RazLivraisonPDF;
import appli.model.domaine.util_srv.raz.pdf.RazPaiementPDF;
import appli.model.domaine.util_srv.raz.pdf.RazPostePDF;
import appli.model.domaine.util_srv.raz.pdf.RazSocieteLivraisonPDF;
import appli.model.domaine.vente.persistant.CaisseJourneePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementOffrePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import appli.model.domaine.vente.persistant.CaissePersistant;
import appli.model.domaine.vente.persistant.JourneePersistant;
import framework.controller.Context;
import framework.controller.ContextGloabalAppli;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.printGen.PrintPosBean;

@Named
@WorkModelClassValidator(validator=EtatFinanceValidator.class)
public class RazService {
	
	public static String getQteFormatted(BigDecimal qteB) {
       String qte = "";
       if(qteB != null){
    	   if(qteB.doubleValue() % 1 != 0){
    		 qte = BigDecimalUtil.formatNumber(qteB);
    	   } else{
    		 qte = ""+qteB.intValue();
    	   }
       }
       
       return qte;
	}
	
	/**
	 * To prevent lazy exception
	 * @param cmvmP
	 * @return
	 */
	public static List<CaisseMouvementOffrePersistant> getListOffre(CaisseMouvementPersistant cmvmP){
	   	List<CaisseMouvementOffrePersistant> listOffres = null;
	   	try{
	   		listOffres = cmvmP.getList_offre();
	   		if(listOffres != null) {
	   			listOffres.size();// Id lazy exception
	   		}
	   	} catch(Exception e){
	   		if(cmvmP.getId() != null) {
		   		IArticleService service = (IArticleService)ServiceUtil.getBusinessBean(IArticleService.class);
		   		CaisseMouvementPersistant cmvP = service.findById(CaisseMouvementPersistant.class, cmvmP.getId());
		   		listOffres = cmvP.getList_offre();
		   		cmvmP.setList_offre(listOffres);
	   		}
	   	}
	   	return listOffres;
	}
	
	public Map<String, Object> imprimerRaz(String typeRaz, 
			String format, 
			JourneePersistant journeeDebut, 
			JourneePersistant journeeFin, 
			String dateTitle,
			Long userId,
			boolean marquerMvmPrint, 
			boolean isFromRaz,
			boolean isOptimCheck){ 
		
		List<PrintPosBean> listPos = new ArrayList<>();
		IJourneeService journeeService = ServiceUtil.getBusinessBean(IJourneeService.class);
		Object[] retourArray = new Object[2];
		Object retourFile = null;
		List<CaisseMouvementPersistant> listMouvement = getListMouvementByDate(journeeDebut.getId(), journeeFin.getId());
		
		if(typeRaz.equals("RP")){// RAZ PERIODE----------------------------------------------
			Map<String, Object> data = getRazDetailleeData(listMouvement, format, 
					false, true,
					journeeDebut, journeeFin, isOptimCheck);	
			List<Long> listIdsTraite = (List<Long>) data.get("listIdsTraite");
			String tplib = "RAZ PÉRIODE";
			
			if("PRINT".equals(format)){
				List<Map<String, Object>> listDetailDays = (List<Map<String, Object>>) data.get("list_days_mtt");
				if(listDetailDays != null){
					for (Map<String, Object> map : listDetailDays) {
						List<MouvementDetailPrintBean> listDetCmd = (List<MouvementDetailPrintBean>) map.get("list_cmd");
						BigDecimal mttTotalOffertHorsDet = (BigDecimal) map.get("mtt_offert");
						String dt = (String) map.get("date_raz");
						
						PrintRazJournee pu = new PrintRazJournee(listDetCmd, mttTotalOffertHorsDet, dt);
						listPos.add(pu.getPrintPosBean());
					}
				}
				
				BigDecimal[] mttsRecap = (BigDecimal[]) data.get("mtt_recap");
				BigDecimal[] mttsMode = (BigDecimal[]) data.get("mtt_mode");
	        	String dateRaz = (String) data.get("date_raz");
				
				PrintRazJournee pu = new PrintRazJournee(mttsRecap, mttsMode, dateRaz);
				listPos.add(pu.getPrintPosBean());
				
				if(marquerMvmPrint){// Depuis backoffice
					marquerMvmImprimed(listIdsTraite);
				}
			} else if("PDF".equals(format)){
				retourFile = new RazJourPDF().getPdf("RAZ PÉRIODE", dateTitle, data, true);
            	// Archiver le pdf dans la GED
				if(marquerMvmPrint){
					archiverPdfRazGed(typeRaz, tplib, journeeDebut, (File) retourFile);
				}
            } else if("HTML".equals(format)){
            	retourFile = new RazJourHTML().getHtml("RAZ PÉRIODE", dateTitle, data);
            }
			retourArray[1] = data.get("rapport");
		} else if(typeRaz.equals("RCJ")){ // Raz caisse journée ----------------------------------------------------------
			Map<String, Object> data = getRazDetailleeData(listMouvement, format, 
					false, false,
					journeeDebut, journeeFin, isOptimCheck);	
			List<Long> listIdsTraite = (List<Long>) data.get("listIdsTraite");
			
			if("PRINT".equals(format)){
				List<Map<String, Object>> listDetailDays = (List<Map<String, Object>>) data.get("list_days_mtt");
				if(listDetailDays != null){
					for (Map<String, Object> map : listDetailDays) {
						List<MouvementDetailPrintBean> listDetCmd = (List<MouvementDetailPrintBean>) map.get("list_cmd");
						BigDecimal mttTotalOffertHorsDet = (BigDecimal) map.get("mtt_offert");
						String dt = (String) map.get("date_raz");
						
						PrintRazJournee pu = new PrintRazJournee(listDetCmd, mttTotalOffertHorsDet, dt);
						listPos.add(pu.getPrintPosBean());
					}
				}
				
				BigDecimal[] mttsRecap = (BigDecimal[]) data.get("mtt_recap");
				BigDecimal[] mttsMode = (BigDecimal[]) data.get("mtt_mode");
	        	String dateRaz = (String) data.get("date_raz");
	        	
	        	if(dateRaz == null) {
	        		if(journeeFin == null || journeeDebut.getId() == journeeFin.getId()) {
	        			dateRaz = DateUtil.dateToString(journeeDebut.getDate_journee(), "dd/MM/yyyy");
	        		} else {
	        			dateRaz = DateUtil.dateToString(journeeDebut.getDate_journee(), "dd/MM/yyyy")+"-"+DateUtil.dateToString(journeeFin.getDate_journee(), "dd/MM/yyyy");	        			
	        		}
	        	}
				
				PrintRazJournee pu = new PrintRazJournee(mttsRecap, mttsMode, dateRaz);
				listPos.add(pu.getPrintPosBean());
				
				if(marquerMvmPrint){// Depuis backoffice
					marquerMvmImprimed(listIdsTraite);
				}
			} else if("PDF".equals(format)){
				retourFile = new RazJourPDF().getPdf("RAZ JOURNÉES", dateTitle, data, false);
            	// Archiver le pdf dans la GED
				if(marquerMvmPrint){
					archiverPdfRazGed(typeRaz, "RAZ JOURNÉES", journeeDebut, (File) retourFile);
				}
            } else if("HTML".equals(format)){
            	retourFile = new RazJourHTML().getHtml("RAZ JOURNÉES", dateTitle, data);
            }
			retourArray[1] = data.get("rapport");
			
		} else if(typeRaz.equals("RE") || typeRaz.equals("REA")){ // RAZ employé ----------------------------------------------------------
			JourneePersistant journeeV = journeeService.getJourneeView(journeeDebut.getId());
			List<CaisseJourneePersistant> listDataShift = journeeService.getJourneeCaisseView(journeeDebut.getId());
			BigDecimal fraisLivraison = journeeV.getTarif_livraison();
			boolean isAll = typeRaz.equals("REA") || userId == null;
			typeRaz = isAll ? "REA" : "RE";
			Map<String, Map> mapEmpl = new HashMap<>();
			Map<String, BigDecimal> mapEmplMvm = new HashMap<>();
			Map<String, BigDecimal> mapEmplAnnul = new HashMap<>();
			Map<String, BigDecimal> mapEmplOffr = new HashMap<>();
			//
			if(listDataShift == null){
				return null;
			}
			if(!Context.isOperationAvailable("SHIFT") || !Context.isOperationAvailable("SHIFTCL")){
				return null;
			}
			
			for (CaisseMouvementPersistant caisseMouvementP : listMouvement) {
				if(caisseMouvementP.getOpc_user() == null){
					continue;
				}
				Long keyId = caisseMouvementP.getOpc_user().getId();
				if(!isAll && !keyId.equals(userId)){
					continue;
				}

				String key = caisseMouvementP.getOpc_user().getLogin();
				if(caisseMouvementP.getOpc_user().getOpc_employe()!=null){
					key = caisseMouvementP.getOpc_user().getOpc_employe().getNom()+" "+StringUtil.getValueOrEmpty(caisseMouvementP.getOpc_user().getOpc_employe().getPrenom());
				}
				
				if(BooleanUtil.isTrue(caisseMouvementP.getIs_annule())) {
					mapEmplAnnul.put(key, BigDecimalUtil.add(mapEmplAnnul.get(key), caisseMouvementP.getMtt_commande_net()));
				} else {
					mapEmplAnnul.put(key, BigDecimalUtil.add(mapEmplAnnul.get(key), caisseMouvementP.getMtt_annul_ligne()));
					mapEmplMvm.put(key, BigDecimalUtil.add(mapEmplMvm.get(key), caisseMouvementP.getMtt_commande_net()));
					mapEmplOffr.put(key, BigDecimalUtil.add(mapEmplOffr.get(key), caisseMouvementP.getMtt_art_offert()));
				}
			}
			mapEmpl.put("MVM", mapEmplMvm);
			mapEmpl.put("OFFR", mapEmplOffr);
			mapEmpl.put("ANNUL", mapEmplAnnul);
			
			//
			if("PRINT".equals(format)){
				PrintRazEmploye pu = new PrintRazEmploye(
						"RAZ CHIFFRES EMPLOYÉS", 
						dateTitle, 
						userId, 
						typeRaz, 
						listDataShift, 
						mapEmpl, 
						fraisLivraison);
				listPos.add(pu.getPrintPosBean());
			} else if("PDF".equals(format)){
				retourFile = new RazEmployePDF().getPdf(
						"RAZ CHIFFRES EMPLOYÉS", 
						dateTitle, 
						userId, 
						typeRaz, 
						listDataShift, 
						mapEmpl, 
						fraisLivraison);
            } else if("HTML".equals(format)){
            	retourFile = new RazEmployeHTML().getHtml(
            			"RAZ CHIFFRES EMPLOYÉS", 
            			dateTitle, 
            			userId, 
            			typeRaz, 
            			listDataShift, 
            			mapEmpl, 
            			fraisLivraison);
            }
			
		} else if(typeRaz.equals("RA") || typeRaz.equals("RAQTE")){ // RAZ article ----------------------------------------------------------
			Map<String, Object> data = getRazArticle(format, journeeDebut, journeeFin, isFromRaz);
			boolean isQteOnly = typeRaz.equals("RAQTE");
			
			if(format.equals("PRINT")){
	            // Print
	            PrintRazArticle pu = new PrintRazArticle(
	            		"RAZ ARTICLES", 
	            		dateTitle, data, isQteOnly);
	            listPos.add(pu.getPrintPosBean());
			} else if("PDF".equals(format)){
				retourFile = new RazArticlePDF().getPdf(
						"RAZ ARTICLES", 
						dateTitle, data, isQteOnly
					);
			} else if("HTML".equals(format)){
				retourFile = new RazArticleHTML().getHtml(
						"RAZ ARTICLES", 
						dateTitle, data, isQteOnly
					);
			}
			retourArray[1] = data.get("rapport");
			
		} else if(typeRaz.equals("RB")){ // RAZ boisson ----------------------------------------------------------
			Map<String, Object> data = getRazBoissonData(format, listMouvement);
			Map<String, MouvementDetailPrintBean> data_froid = (Map<String, MouvementDetailPrintBean>) data.get("data_froid");
			Map<String, MouvementDetailPrintBean> data_chaud = (Map<String, MouvementDetailPrintBean>) data.get("data_chaud");
			String dateRaz = (String) data.get("date_raz");
        	
			if(format.equals("PRINT")){
	            // Print
	            if(data_froid != null && data_froid.size() > 0){
	                PrintRazBoisson pu = new PrintRazBoisson("Boissons froides", data_froid, dateRaz);
	                listPos.add(pu.getPrintPosBean());
	            }
	            if(data_chaud != null && data_chaud.size() > 0){
	                PrintRazBoisson pu = new PrintRazBoisson("Boissons chaudes", data_chaud, dateRaz);
	                listPos.add(pu.getPrintPosBean());
	            }
			} else if("PDF".equals(format)){
				retourFile = new RazBoissonPDF().getPdf("RAZ BOISSONS", dateTitle, data);
			} else if("HTML".equals(format)){
				retourFile = new RazBoissonHTML().getHtml("RAZ BOISSONS", dateTitle, data);
			}
			retourArray[1] = data.get("rapport");
			
		} else if(typeRaz.equals("RMP")){ // RAZ mode de paiement -----------------------------------------------
			Map<String, List<RazDetail>> data = getRazModePaiement(listMouvement);
			if(format.equals("PRINT")){
	            // Print
	            PrintRazModePaiement pu = new PrintRazModePaiement("RAZ MODES DE PAIEMENTS", dateTitle, data.get("list_data"));
	            listPos.add(pu.getPrintPosBean());
			} else {
				if("PDF".equals(format)){
					retourFile = new RazPaiementPDF().getPdf(
							"RAZ MODES DE PAIEMENTS", 
							dateTitle,
							data.get("list_data")
						);
				} else if("HTML".equals(format)){
					retourFile = new RazPaiementHTML().getHtml(
							"RAZ MODES DE PAIEMENTS", 
							dateTitle,
							data.get("list_data")
						);
				}
			}
			retourArray[1] = data.get("rapport");
			
		} else if(typeRaz.equals("RSL")){	// RAZ société de livraison -----------------------------------------
			Map<String, List<RazDetail>> data = getRazSocieteLivraison(listMouvement);
			if(format.equals("PRINT")){
	            // Print
	            PrintRazSocieteLivraison pu = new PrintRazSocieteLivraison("RAZ SOCIÉTÉ DE LIVRAISON", dateTitle, data.get("list_data"), data.get("mode_paie"));
	            listPos.add(pu.getPrintPosBean());
			} else if("PDF".equals(format)){
				retourFile = new RazSocieteLivraisonPDF().getPdf(
						"RAZ SOCIÉTÉ DE LIVRAISON", 
						dateTitle, 
						data.get("list_data"), data.get("mode_paie")
					);
			} else if("HTML".equals(format)){
				retourFile = new RazSocieteLivraisonHTML().getHtml(
						"RAZ SOCIÉTÉ DE LIVRAISON", 
						dateTitle, 
						data.get("list_data"), 
						data.get("mode_paie")
					);
			}
			retourArray[1] = data.get("rapport");
			
		} else if(typeRaz.equals("RL")){	// RAZ  livraison -----------------------------------------------------
			Map<String, List<RazDetail>> data = getRazLivraison(listMouvement);
			if(format.equals("PRINT")){
	            // Print
	            PrintRazLivraison pu = new PrintRazLivraison("RAZ LIVREURS", dateTitle, data.get("list_data"));
//	            new PrintCommunUtil(pu.getPrintPosBean()).print();
	            listPos.add(pu.getPrintPosBean());
			} else if("PDF".equals(format)){
				retourFile = new RazLivraisonPDF().getPdf(
						"RAZ LIVREURS", 
						dateTitle,
						data.get("list_data")
					);
			} else if("HTML".equals(format)){
				retourFile = new RazLivraisonHTML().getHtml(
						"RAZ LIVREURS", 
						dateTitle,
						data.get("list_data"));
			}
			retourArray[1] = data.get("rapport");
			
		} else if(typeRaz.equals("GLO")){	// RAZ globale -----------------------------------------------------
			Map<String, Map<String, RazDetail>> data = getRazGlobale(listMouvement);
			if(format.equals("PRINT")){
	            // Print
				PrintRazGlobale pu = new PrintRazGlobale("RAZ GLOBALE", dateTitle, data);
	            listPos.add(pu.getPrintPosBean());
			} else if("PDF".equals(format)){
				retourFile = new RazGlobalePDF().getPdf(
						"RAZ GLOBALE", 
						dateTitle,
						data
					);
			} else if("HTML".equals(format)){
				retourFile = new RazGlobaleHTML().getHtml( 
						"RAZ GLOBALE", 
						dateTitle,
						data);
			}
			retourArray[1] = data.get("rapport");
		} else if(typeRaz.equals("POS")){	// RAZ poste -----------------------------------------------------
			Map<String, Map> mapRetour = getRazPoste(listMouvement);
			
			Map<String, List<RazDetail>> mapModePaiement = mapRetour.get("MODE_PAIEMENT");
			Map<String, Map<Long, RepartitionBean>> mapRep = mapRetour.get("REP_ART");
			
			if(format.equals("PRINT")){
	            // Print
				PrintRazPoste pu = new PrintRazPoste("RAZ POSTE", dateTitle, mapModePaiement, mapRep);
	            listPos.add(pu.getPrintPosBean());
			} else if("PDF".equals(format)){
				retourFile = new RazPostePDF().getPdf(
						"RAZ POSTE", 
						dateTitle,
						mapModePaiement, mapRep
					);
			} else if("HTML".equals(format)){
				retourFile = new RazPosteHTML().getHtml( 
						"RAZ POSTE", 
						dateTitle,
						mapModePaiement, mapRep);
			}
			retourArray[1] = null;//data.get("rapport");
		}
		
		retourArray[0] = retourFile;
		
		Map<String, Object> mapRetour = new HashMap<>();
		mapRetour.put("print", listPos);
		mapRetour.put("obj1", retourArray[0]);
		mapRetour.put("obj2", retourArray[1]);
		
		return mapRetour;
	}
	
	private Map<String, Map> getRazPoste(List<CaisseMouvementPersistant> listMouvement){
		Map<String, Map<String, RazDetail>> mapAll = new HashMap<>();
		Map<String, List<RazDetail>> mapAllDet = new HashMap<>();
		Map<String, Map<Long, RepartitionBean>> mapQte = new LinkedHashMap<>();
		Set<String> setCaisse = new HashSet<>();
		
		for (CaisseMouvementPersistant caisseMouvementP : listMouvement) {
			if(BooleanUtil.isTrue(caisseMouvementP.getIs_annule())){
				continue;
			}
			
			String caisse = caisseMouvementP.getOpc_caisse_journee().getOpc_caisse().getAdresse_mac();
			setCaisse.add(caisse);
			
			//---------------------------------------------------------------
			for(CaisseMouvementArticlePersistant det : caisseMouvementP.getList_article()) {
				if(det.getOpc_article() != null) {
					Long artId = det.getOpc_article().getId();
					FamilleCuisinePersistant opc_famille_cuisine = det.getOpc_article().getOpc_famille_cuisine();
					String lib = det.getOpc_article().getCode()+"-"+det.getOpc_article().getLibelle();
					if("GEN".equals(det.getOpc_article().getCode())){
						lib = det.getLibelle();
						artId = Long.valueOf(lib.hashCode());
					}
					
					Map<Long, RepartitionBean> mapRep = mapQte.get(caisse);
					if(mapRep == null) {
						mapRep = new LinkedHashMap<>();
						mapQte.put(caisse, mapRep);
					}
					
					RepartitionBean repArtMenu = mapRep.get(artId);
					if(repArtMenu == null){
						repArtMenu = new RepartitionBean();
						repArtMenu.setElementId(artId);
						repArtMenu.setFamille(opc_famille_cuisine.getCode()+"-"+opc_famille_cuisine.getLibelle());
						repArtMenu.setLibelle(lib);
						mapRep.put(artId, repArtMenu);
					}
					repArtMenu.setQuantite(BigDecimalUtil.add(repArtMenu.getQuantite(), det.getQuantite()));
					repArtMenu.setMontant(BigDecimalUtil.add(repArtMenu.getMontant(), det.getMtt_total()));
				}
			}
			//----------------------------------------------------------------
			Map<String, RazDetail> mapMtt = mapAll.get(caisse);	
			if(mapMtt == null) {
				mapMtt = new HashMap<>();
				mapAll.put(caisse, mapMtt);
			}
			
			boolean isMode = false;
			
			if(!BigDecimalUtil.isZero(caisseMouvementP.getMtt_donne())) {
				setModePaie(mapMtt, "ESPECES", caisseMouvementP.getMtt_donne()); 
				isMode = true;
			}
			if(!BigDecimalUtil.isZero(caisseMouvementP.getMtt_donne_cheque())) {
				setModePaie(mapMtt, "CHEQUE", caisseMouvementP.getMtt_donne_cheque()); 
				isMode = true;
			}
			if(!BigDecimalUtil.isZero(caisseMouvementP.getMtt_donne_cb())) {
				setModePaie(mapMtt, "CARTE", caisseMouvementP.getMtt_donne_cb()); 
				isMode = true;
			}
			if(!BigDecimalUtil.isZero(caisseMouvementP.getMtt_donne_dej())) {
				setModePaie(mapMtt, "CHEQUE TABLE", caisseMouvementP.getMtt_donne_dej()); 
				isMode = true;
			}
			if(!BigDecimalUtil.isZero(caisseMouvementP.getMtt_donne_point())) {
				setModePaie(mapMtt, "POINTS", caisseMouvementP.getMtt_donne_point()); 
				isMode = true;
			}
			if(!BigDecimalUtil.isZero(caisseMouvementP.getMtt_portefeuille())) {
				setModePaie(mapMtt, "RESERVE", caisseMouvementP.getMtt_portefeuille()); 
				isMode = true;
			}
			if(!isMode) {
				setModePaie(mapMtt, "* NON ENCAISSEES", caisseMouvementP.getMtt_commande_net());
			}
		}

		for(String caisse : setCaisse) {
			if(mapAll.get(caisse).size() == 0) {
				mapAll.remove(caisse);
			}
		}
		
		for(String caisse : mapAll.keySet()) {
			Map<String, RazDetail> mapMtt = mapAll.get(caisse);
			List<RazDetail> listRazPaie = new ArrayList<>();
			//
			for(String mode : mapMtt.keySet()) {
				listRazPaie.add(mapMtt.get(mode));
			}
			mapAllDet.put(caisse, listRazPaie);
		}
		
		Map<String, Map> mapRetour = new HashMap<>();
		mapRetour.put("MODE_PAIEMENT", mapAllDet);
		mapRetour.put("REP_ART", mapQte);
		
		return mapRetour;
	}
	
	private Map<String, List<RazDetail>> getRazModePaiement(List<CaisseMouvementPersistant> listMouvement){
		Map<String, RazDetail> mapMtt = new HashMap<>();
		List<RazDetail> listRazPaie = new ArrayList<>();
		for (CaisseMouvementPersistant caisseMouvementP : listMouvement) {
			if(BooleanUtil.isTrue(caisseMouvementP.getIs_annule())){
				continue;
			}
			
			if(!BigDecimalUtil.isZero(caisseMouvementP.getMtt_donne())) {
				setModePaie(mapMtt, "ESPECES", caisseMouvementP.getMtt_donne());
			}
			if(!BigDecimalUtil.isZero(caisseMouvementP.getMtt_donne_cheque())) {
				setModePaie(mapMtt, "CHEQUE", caisseMouvementP.getMtt_donne_cheque());
			}
			if(!BigDecimalUtil.isZero(caisseMouvementP.getMtt_donne_cb())) {
				setModePaie(mapMtt, "CARTE", caisseMouvementP.getMtt_donne_cb());
			}
			if(!BigDecimalUtil.isZero(caisseMouvementP.getMtt_donne_dej())) {
				setModePaie(mapMtt, "CHEQUE TABLE", caisseMouvementP.getMtt_donne_dej());
			}
			if(!BigDecimalUtil.isZero(caisseMouvementP.getMtt_donne_point())) {
				setModePaie(mapMtt, "POINTS", caisseMouvementP.getMtt_donne_point());
			}
			if(!BigDecimalUtil.isZero(caisseMouvementP.getMtt_portefeuille())) {
				setModePaie(mapMtt, "RESERVE", caisseMouvementP.getMtt_portefeuille());
			}
		}
		
		for(String mode : mapMtt.keySet()) {
			listRazPaie.add(mapMtt.get(mode));
		}
		
		Map<String, List<RazDetail>> mapData = new HashMap<>();
		mapData.put("list_data", listRazPaie);
		
		return mapData;
	}
	private void setModePaie(Map<String, RazDetail> mapMtt, String mode, BigDecimal mtt) {
		RazDetail razDetail = mapMtt.get(mode);
		if(razDetail == null){
			razDetail = new RazDetail();
			razDetail.setLibelle(mode);
			mapMtt.put(mode, razDetail);
		}
		razDetail.setQuantite(razDetail.getQuantite()+1);
		razDetail.setMontant(BigDecimalUtil.add(razDetail.getMontant(), mtt));
	}
	
	private Map<String, List<RazDetail>> getRazSocieteLivraison(List<CaisseMouvementPersistant> listMouvement){
		Map<Long, RazDetail> mapEmpl = new HashMap<>();
		Map<String, RazDetail> mapPaie = new HashMap<>();
		List<RazDetail> listRaz = new ArrayList<>();
		List<RazDetail> listRazPaie = new ArrayList<>();
		Map<String, List<RazDetail>> mapData = new HashMap<>();
		// Ventes
		for (CaisseMouvementPersistant caisseMouvementP : listMouvement) {
			if(caisseMouvementP.getOpc_societe_livr() != null){
				Long key = caisseMouvementP.getOpc_societe_livr().getId();
				
				RazDetail razDetail = mapEmpl.get(key);
				if(razDetail == null){
					razDetail = new RazDetail();
					razDetail.setLibelle(caisseMouvementP.getOpc_societe_livr().getNom());
					mapEmpl.put(key, razDetail);
					//
					listRaz.add(razDetail);
				}
				razDetail.setQuantite(razDetail.getQuantite()+1);
				razDetail.setMontant(BigDecimalUtil.add(razDetail.getMontant(), caisseMouvementP.getMtt_commande_net()));
				
				// Modes de paiements
				String keyPaie = caisseMouvementP.getMode_paiement();
				razDetail = mapPaie.get(keyPaie);
				if(razDetail == null){
					razDetail = new RazDetail();
					razDetail.setLibelle(caisseMouvementP.getOpc_societe_livr().getNom());
					mapPaie.put(keyPaie, razDetail);
					//
					listRazPaie.add(razDetail);
				}
				razDetail.setMontant(BigDecimalUtil.add(razDetail.getMontant(), caisseMouvementP.getMtt_commande_net()));
				razDetail.setMontant2(BigDecimalUtil.add(razDetail.getMontant2(), caisseMouvementP.getMtt_livraison_livr()));
			}
		}
		
		mapData.put("list_data", listRaz);
		mapData.put("mode_paie", listRazPaie);
		
		return mapData;
	}
	private Map<String, List<RazDetail>> getRazLivraison(List<CaisseMouvementPersistant> listMouvement){
		Map<Long, RazDetail> mapEmpl = new HashMap<>();
		List<RazDetail> listRaz = new ArrayList<>();
		//
		for (CaisseMouvementPersistant caisseMouvementP : listMouvement) {
			if(caisseMouvementP.getOpc_livreurU() != null){
				Long key = caisseMouvementP.getOpc_livreurU().getId();
				
				RazDetail razDetail = mapEmpl.get(key);
				if(razDetail == null){
					razDetail = new RazDetail();
					razDetail.setLibelle(caisseMouvementP.getOpc_livreurU().getLogin());
					mapEmpl.put(key, razDetail);
					//
					listRaz.add(razDetail);
				}
				razDetail.setQuantite(razDetail.getQuantite()+1);
				razDetail.setMontant(BigDecimalUtil.add(razDetail.getMontant(), caisseMouvementP.getMtt_commande_net()));
			}
		}
		
		Map<String, List<RazDetail>> mapData = new HashMap<>();
		mapData.put("list_data", listRaz);
		
		return mapData;
	}
	
//	private BigDecimal getMtAchat(CaisseMouvementPersistant caisseMouvementP){
//		BigDecimal mttTotalAchatAll = null;
//		if(caisseMouvementP.getOpc_mouvement() != null){
//			List<MouvementArticlePersistant> listMvmArt = caisseMouvementP.getOpc_mouvement().getList_article();
//			for (MouvementArticlePersistant cmaP : listMvmArt) {
//				ArticlePersistant opc_article = cmaP.getOpc_article();
//				if(opc_article != null){
//					BigDecimal mttAchat = opc_article.getPrixAchatUnitaireTTC();
//					if(BigDecimalUtil.isZero(mttAchat)){
//						mttAchat = opc_article.getPrixAchatUnitaireHT();
//					}
//					if(!BigDecimalUtil.isZero(mttAchat)){
//						BigDecimal mttAchatQte = BigDecimalUtil.multiply(cmaP.getQuantite(), mttAchat);
//						mttTotalAchatAll = BigDecimalUtil.add(mttTotalAchatAll, mttAchatQte);
//					}
//				}
//			}
//		}
//		return mttTotalAchatAll;
//	}
	
	/**
	 * @param listMouvement
	 * @return
	 */
	private Map<String, Map<String, RazDetail>> getRazGlobale(List<CaisseMouvementPersistant> listMouvement){
		IJourneeService journeeService = ServiceUtil.getBusinessBean(IJourneeService.class);
		
		Map<String, RazDetail> mapCaissier = new HashMap<>();
		Map<String, RazDetail> mapCaisse = new HashMap<>();
		Map<String, RazDetail> mapLivraison = new HashMap<>();
		Map<String, RazDetail> mapModePaie = new HashMap<>();
		Map<String, RazDetail> mapTypeCmd = new HashMap<>();
		Map<String, RazDetail> mapPosteCuisine = new HashMap<>();
		Map<String, RazDetail> mapDetail = new HashMap<>();
		
		mapDetail.put("ANNULATION", new RazDetail("ANNULATION"));
		mapDetail.put("REDUCTION", new RazDetail("REDUCTION"));
		
		//
		for (CaisseMouvementPersistant caisseMouvementP : listMouvement) {
			
			if(BooleanUtil.isTrue(caisseMouvementP.getIs_annule())){
				mapDetail.get("ANNULATION").setMontant(BigDecimalUtil.add(mapDetail.get("ANNULATION").getMontant(), caisseMouvementP.getMtt_commande_net()));
				continue;
			}
			List<CaisseMouvementArticlePersistant> listMvm = caisseMouvementP.getList_article();
			//
			mapDetail.get("REDUCTION").setMontant(BigDecimalUtil.add(mapDetail.get("REDUCTION").getMontant(), caisseMouvementP.getMtt_reduction()));
			
			String typeCmd = caisseMouvementP.getType_commande();
			
			// Par caisse
			CaissePersistant opc_caisse = caisseMouvementP.getOpc_caisse_journee().getOpc_caisse();
			if(opc_caisse != null){
				String caisseReference = opc_caisse.getReference();
				RazDetail razDetail = mapCaisse.get(caisseReference);
				if(razDetail == null){
					razDetail = new RazDetail();
					razDetail.setLibelle(caisseReference);
					mapCaisse.put(caisseReference, razDetail);
				}
				razDetail.setQuantite(razDetail.getQuantite()+1);
				razDetail.setMontant(BigDecimalUtil.add(razDetail.getMontant(), caisseMouvementP.getMtt_commande_net()));
//				razDetail.setMontant2(BigDecimalUtil.add(razDetail.getMontant2(), getMtAchat(caisseMouvementP)));
			}
			// Par poste ----------------------------------------------------
			if(StringUtil.isNotEmpty(caisseMouvementP.getCaisse_cuisine())){
				String[] elementsArray = StringUtil.getArrayFromStringDelim(caisseMouvementP.getCaisse_cuisine(), ";");
	    		if(elementsArray != null){
	    			List<String> listTaite = new ArrayList<>();
	    			for(String element : elementsArray){
	    				String[] cuisMenus = StringUtil.getArrayFromStringDelim(element, ":");
	    				Long caisseId = Long.valueOf(cuisMenus[0]);
	    				Long detId = Long.valueOf(cuisMenus[1]);
	    				String key = "_"+detId;
	    				
	    				if(listTaite.contains(key)) {
	    					continue;
	    				}
	    				
	    				listTaite.add(key);
	    				
	    				RazDetail razDetail = mapPosteCuisine.get(caisseId.toString());
	    				if(razDetail == null){
	    					CaissePersistant caisseP = journeeService.findById(CaissePersistant.class, caisseId);
	    					razDetail = new RazDetail();
	    					razDetail.setLibelle(caisseP.getReference());
	    					mapPosteCuisine.put(caisseId.toString(), razDetail);
	    				}
						for(CaisseMouvementArticlePersistant detP : listMvm){
	    					if(!detP.getId().equals(detId)) {
	    						continue;
	    					}
							if(BigDecimalUtil.isZero(detP.getMtt_total())
									|| BooleanUtil.isTrue(detP.getIs_annule())
									|| BooleanUtil.isTrue(detP.getIs_offert())){
								break;
							}
    						//razDetail.setQuantite(razDetail.getQuantite()+detP.getQuantite().intValue());
    						razDetail.setMontant(BigDecimalUtil.add(razDetail.getMontant(), detP.getMtt_total()));
    						//
//	    						if(detP.getOpc_article() != null){
//	    							BigDecimal mttAchat = detP.getOpc_article().getPrixAchatUnitaireTTC();
//	    							if(BigDecimalUtil.isZero(mttAchat)){
//	    								mttAchat = detP.getOpc_article().getPrixAchatUnitaireHT();
//	    							}
//	    							if(!BigDecimalUtil.isZero(mttAchat)){
//	    								BigDecimal mttAchatQte = BigDecimalUtil.multiply(detP.getQuantite(), mttAchat);
//	    								razDetail.setMontant2(BigDecimalUtil.add(razDetail.getMontant2(), mttAchatQte));
//	    							}
//	    						}
    						break;
	    				}
	    			}
	    		}
			}
			
			// Par livreur
			UserPersistant opc_livreur = caisseMouvementP.getOpc_livreurU();
			if(opc_livreur != null){
				String key = opc_livreur.getLogin();
				RazDetail razDetail = mapLivraison.get(key);
				if(razDetail == null){
					razDetail = new RazDetail();
					razDetail.setLibelle(key);
					mapLivraison.put(key, razDetail);
				}
				razDetail.setQuantite(razDetail.getQuantite()+1);
				razDetail.setMontant(BigDecimalUtil.add(razDetail.getMontant(), caisseMouvementP.getMtt_commande_net()));
			}
			
			// Par caissier -------------------------------------------------
			UserPersistant opc_caissier = caisseMouvementP.getOpc_user();
			if(opc_caissier != null){
				RazDetail razDetail = mapCaissier.get(opc_caissier.getId().toString());
				if(razDetail == null){
					razDetail = new RazDetail();
					
					if(opc_caissier.getOpc_employe() != null){
						razDetail.setLibelle(opc_caissier.getOpc_employe().getNom()+" "+StringUtil.getValueOrEmpty(opc_caissier.getOpc_employe().getPrenom()));
					} else{
						razDetail.setLibelle(opc_caissier.getLogin());						
					}
					mapCaissier.put(opc_caissier.getId().toString(), razDetail);
				}
				razDetail.setQuantite(razDetail.getQuantite()+1);
				razDetail.setMontant(BigDecimalUtil.add(razDetail.getMontant(), caisseMouvementP.getMtt_commande_net()));
//				razDetail.setMontant2(BigDecimalUtil.add(razDetail.getMontant2(), getMtAchat(caisseMouvementP)));
			}
			
			// Par mode de paiement -----------------------------------------
			if(!BigDecimalUtil.isZero(caisseMouvementP.getMtt_donne())) {
				setModePaie(mapModePaie, "ESPECES", caisseMouvementP.getMtt_donne());
			}
			if(!BigDecimalUtil.isZero(caisseMouvementP.getMtt_donne_cheque())) {
				setModePaie(mapModePaie, "CHEQUE", caisseMouvementP.getMtt_donne_cheque());
			}
			if(!BigDecimalUtil.isZero(caisseMouvementP.getMtt_donne_cb())) {
				setModePaie(mapModePaie, "CARTE", caisseMouvementP.getMtt_donne_cb());
			}
			if(!BigDecimalUtil.isZero(caisseMouvementP.getMtt_donne_dej())) {
				setModePaie(mapModePaie, "CHEQUE TABLE", caisseMouvementP.getMtt_donne_dej());
			}
			if(!BigDecimalUtil.isZero(caisseMouvementP.getMtt_donne_point())) {
				setModePaie(mapModePaie, "POINTS", caisseMouvementP.getMtt_donne_point());
			}
			if(!BigDecimalUtil.isZero(caisseMouvementP.getMtt_portefeuille())) {
				setModePaie(mapModePaie, "RESERVE", caisseMouvementP.getMtt_portefeuille());
			}
			
			// Par type de commande ----------------------------------------
			String typeCmdN = StringUtil.isEmpty(typeCmd)?TYPE_COMMANDE.E.getLibelle() : TYPE_COMMANDE.valueOf(typeCmd).getLibelle();
			RazDetail razDetail = mapTypeCmd.get(typeCmdN);
			if(razDetail == null){
				razDetail = new RazDetail();
				
				razDetail.setLibelle(typeCmdN);
				mapTypeCmd.put(typeCmdN, razDetail);
			}
			razDetail.setQuantite(razDetail.getQuantite()+1);
			razDetail.setMontant(BigDecimalUtil.add(razDetail.getMontant(), caisseMouvementP.getMtt_commande_net()));
		}
		
		Map<String, Map<String, RazDetail>> mapData = new LinkedHashMap<>();
		mapData.put("POSTES CAISSE", mapCaisse);
		mapData.put("POSTES CUISINE (Ne prend pas en compte : offres, multi poste, réductions,...)", mapPosteCuisine);
		mapData.put("CAISSIERS", mapCaissier);
		mapData.put("LIVRAISON", mapLivraison);
		mapData.put("MODES DE PAIEMENT", mapModePaie);
		mapData.put("TYPES DE COMMANDES", mapTypeCmd);
		mapData.put("DETAILS", mapDetail);
		
		return mapData;
	}
	private Map<String, Object> getRazArticle(String format, JourneePersistant journeeDebutId, JourneePersistant journeeFinId, boolean isFromRaz){
		Map data = null;
		boolean isRestau = SOFT_ENVS.restau.toString().equals(StrimUtil.getGlobalConfigPropertie("context.soft"));
		
		if(isRestau){
			IJourneeService journeeService = ServiceUtil.getBusinessBean(IJourneeService.class);
			data = journeeService.getRepartitionVenteArticle(journeeDebutId, journeeFinId, null, isFromRaz);
		} else{
			data = getRepartitionVenteArticleNonRestau(journeeDebutId, journeeFinId, null);
		}
		
		return data;
	}
	
	private Map<String, Object> getRazDetailleeData(List<CaisseMouvementPersistant> listMouvement, String modeAffichage, 
			boolean isCaisse, boolean isPeriodePrint,
			JourneePersistant debut, JourneePersistant fin, boolean isOptimCheck) {
		
		IFamilleService familleService = ServiceUtil.getBusinessBean(IFamilleService.class);
		Map<String, Object> mapRetour = new HashMap<>();
		//
		if(listMouvement == null || listMouvement.isEmpty()){
        	mapRetour.put("rapport", "Aucun mouvement trouvé");
        	mapRetour.put("date_raz", DateUtil.dateToString(debut.getDate_journee())+"-"+DateUtil.dateToString(fin.getDate_journee()));
            return mapRetour;
        }
		
		BigDecimal tauxOptimisation = BigDecimalUtil.get(ContextGloabalAppli.getGlobalConfig(ContextAppli.PARAM_APPLI_ENUM.TAUX_OPTIM.toString()));
        BigDecimal seuilOptimisation = BigDecimalUtil.get(ContextGloabalAppli.getGlobalConfig(ContextAppli.PARAM_APPLI_ENUM.SEUIL_OPTIM.toString()));
		boolean isRestau = SOFT_ENVS.restau.toString().equals(StrimUtil.getGlobalConfigPropertie("context.soft"));
		Date dateDebut = listMouvement.get(0).getOpc_caisse_journee().getOpc_journee().getDate_journee();
		Calendar cal = DateUtil.getCalendar(dateDebut );
		String mois = ""+(cal.get(Calendar.MONTH)+1);
		String annee = ""+cal.get(Calendar.YEAR);
		
        String rapport = "";
        List<Long> listIds = new ArrayList<>();        
        try {
            boolean isOptim = isOptimCheck && !BigDecimalUtil.isZero(tauxOptimisation) && ContextAppli.getAbonementBean().isOptPlusOptimisation();
            // On regroupe par date
            Map<String, List<CaisseMouvementPersistant>> mapMvm = new HashMap<>();
            String oldDay = "X";
            BigDecimal mttTotalMontant = null;
            for (CaisseMouvementPersistant caisseMouvementP : listMouvement) {
                if(!BooleanUtil.isTrue(caisseMouvementP.getIs_annule())) {
                	mttTotalMontant = BigDecimalUtil.add(mttTotalMontant, caisseMouvementP.getMtt_commande_net());
                }
                Date date_journee = caisseMouvementP.getOpc_caisse_journee().getOpc_journee().getDate_journee();
                Calendar calendar = DateUtil.getCalendar(date_journee);
				int moisMvm = calendar.get(Calendar.MONTH)+1;
				int year = calendar.get(Calendar.YEAR);
				String dateMvm = calendar.get(Calendar.DAY_OF_MONTH)+"/"+moisMvm+"/"+year;
				
                List<CaisseMouvementPersistant> listMvm = mapMvm.get(dateMvm);
                //
                if (!dateMvm.equals(oldDay)) {
                    listMvm = new ArrayList<>();
                    mapMvm.put(dateMvm, listMvm);
                }
                listMvm.add(caisseMouvementP);
                oldDay = dateMvm;
            }
            // Si le seuille n'est pas atteint alors pas d'optimisation
            if(!BigDecimalUtil.isZero(seuilOptimisation) && mttTotalMontant != null && seuilOptimisation.compareTo(mttTotalMontant)>=0){
            	isOptim = false;
            }
            
            //
            int nbrCmdFinal = 0;
            int nbrCmdInitial = 0;
            BigDecimal mttTotalIitial = null;
            BigDecimal mttTotalFinal = null;
            //
            for (String day : mapMvm.keySet()) {
                List<CaisseMouvementPersistant> listMvmjour = mapMvm.get(day);
                BigDecimal maxMtt = null;
                
                if(isOptim){
                    BigDecimal mttEspeceCmdJour = null;
                    for (CaisseMouvementPersistant caisseMouvementP : listMvmjour) {
                    	if(BooleanUtil.isTrue(caisseMouvementP.getIs_annule())) {
                    		continue;
                    	}
                    	boolean isEspece = (caisseMouvementP.getMode_paiement()!=null && caisseMouvementP.getMode_paiement().contains(ContextAppli.MODE_PAIEMENT.ESPECES.toString()));
                    	if(isEspece) {
                    		mttEspeceCmdJour = BigDecimalUtil.add(mttEspeceCmdJour, caisseMouvementP.getMtt_commande_net());
                    	}
                    }
                    BigDecimal mttReduction = BigDecimalUtil.divide(BigDecimalUtil.multiply(mttEspeceCmdJour, tauxOptimisation), BigDecimalUtil.get(100));
                    maxMtt = BigDecimalUtil.substract(mttEspeceCmdJour, mttReduction);
                }
                
                BigDecimal mttTotalTraite = null;
                Iterator<CaisseMouvementPersistant> listMvmjourIter = listMvmjour.iterator();
                while(listMvmjourIter.hasNext()) {
                    CaisseMouvementPersistant caisseMouvementP = listMvmjourIter.next();
                    if(BooleanUtil.isTrue(caisseMouvementP.getIs_annule())) {
                    	continue;
                    }
                    
                    // Totaux initiaux---------------------
                    nbrCmdInitial++;
                    mttTotalIitial = BigDecimalUtil.add(mttTotalIitial, caisseMouvementP.getMtt_commande_net());
                    //-------------------------------------
                    //
                    if(!isCaisse && isOptim){
                        boolean isEspece = (caisseMouvementP.getMode_paiement()!=null && caisseMouvementP.getMode_paiement().contains(ContextAppli.MODE_PAIEMENT.ESPECES.toString()));
                        if (isEspece && !BooleanUtil.isTrue(caisseMouvementP.getIs_annule())) {
                            mttTotalTraite = BigDecimalUtil.add(mttTotalTraite, caisseMouvementP.getMtt_commande_net());
                            // On arrete si on atteint le max
                            if (mttTotalTraite != null && mttTotalTraite.compareTo(maxMtt) >= 0) {
                                listMvmjourIter.remove();
                                continue;
                            }
                        }
                    }
                    listIds.add(caisseMouvementP.getId());
                    // ---------------------------------------------------------------------
                    nbrCmdFinal++;
                    mttTotalFinal = BigDecimalUtil.add(mttTotalFinal, caisseMouvementP.getMtt_commande_net());
                }
            }
            
            // Imprimer l'entête
            BigDecimal mttTotalNetALL = null;
            BigDecimal mttTotalBrutALL = null;
            BigDecimal mttTotalAnnuleALL = null;
            BigDecimal mttTotalAnnuleArtALL = null;
            BigDecimal mttTotalReducClientALL = null;
            BigDecimal mttTotalReducEmployeALL = null;
            BigDecimal mttTotalReducAutreALL = null;
            BigDecimal mttTotalOffertClientALL = null;
            BigDecimal mttTotalOffertEmployeALL = null;
            BigDecimal mttTotalOffertAutreALL = null;
            
            BigDecimal mttEspeceALL = null;
            BigDecimal mttCarteALL = null;
            BigDecimal mttChequeALL = null;
            BigDecimal mttDejALL = null;
            BigDecimal mttPointALL = null;
            BigDecimal mttReserveALL = null;
            
            int nbrNoteOkAll = 0;
            
            List<Map<String, Object>> listDetailDays = new ArrayList<>();
            
            Map<Long, List<FamillePersistant>> mapFamDetail = new HashMap<>();
            for (String day : mapMvm.keySet()) {
                List<CaisseMouvementPersistant> listMvmjour = mapMvm.get(day);
	            for (CaisseMouvementPersistant caisseMvmP : listMvmjour) {
	            	if(BooleanUtil.isTrue(caisseMvmP.getIs_annule())) {
	            		continue;
	            	}
	            	nbrNoteOkAll++;
	            	 List<CaisseMouvementArticlePersistant> listDet = caisseMvmP.getList_article();
	            	 
	            	 for (CaisseMouvementArticlePersistant caisseMvmArtP : listDet) {
	            		 if(BooleanUtil.isTrue(caisseMvmArtP.getIs_annule())) {
	 	            		continue;
	 	            	}
	            		 
	            		 ArticlePersistant opc_article = caisseMvmArtP.getOpc_article();
	            		 FamilleCuisinePersistant opc_famille_cuisine = null;
	            		 FamilleStockPersistant opc_famille_stock = null;
	            		 //
	            		 if(opc_article != null){
	            		 	opc_famille_cuisine = opc_article.getOpc_famille_cuisine();
	            		 	opc_famille_stock = opc_article.getOpc_famille_stock();
	            		 }
	            		 
						 if(opc_article == null
	            				 || (isRestau ? opc_famille_cuisine == null : opc_famille_stock == null)
	            				 || (isRestau ? mapFamDetail.get(opc_famille_cuisine.getId()) != null : mapFamDetail.get(opc_famille_stock.getId()) != null)) {
	            			 continue;
	            		 }
	            		 
	            		 List<FamillePersistant> familleParent = null;
	            		 if(isRestau){
	            			 familleParent = familleService.getFamilleParent("CU", opc_famille_cuisine.getId());
							 mapFamDetail.put(opc_famille_cuisine.getId(), familleParent);
	            		 } else{
							 familleParent = familleService.getFamilleParent("ST", opc_famille_stock.getId());
							 mapFamDetail.put(opc_famille_stock.getId(), familleParent);
	            		 }
	            	 }
	            }
            }
            // Impression détail quotidien
            for (String day : mapMvm.keySet()) {
                List<CaisseMouvementPersistant> listMvmjour = mapMvm.get(day);
                List<MouvementDetailPrintBean> listDetCmd = new ArrayList<>();
                //
                 BigDecimal mttTotalOffertHorsDet = null;
                for (CaisseMouvementPersistant caisseMvmP : listMvmjour) {
                	/*if(BigDecimalUtil.isZero(caisseMvmP.getMtt_commande_net())){
                		continue;
                	}*/
                	 if(BooleanUtil.isTrue(caisseMvmP.getIs_annule())){
                     	mttTotalAnnuleALL = BigDecimalUtil.add(mttTotalAnnuleALL, caisseMvmP.getMtt_commande_net());
                        continue;
                     }
                    List<CaisseMouvementArticlePersistant> listDet = caisseMvmP.getList_article();
                    
                    //--------------------------------------Totaux------------------------------------------------------------
                    mttEspeceALL = BigDecimalUtil.add(mttEspeceALL, caisseMvmP.getMtt_donne());
                    mttChequeALL = BigDecimalUtil.add(mttChequeALL, caisseMvmP.getMtt_donne_cheque());
                    mttCarteALL = BigDecimalUtil.add(mttCarteALL, caisseMvmP.getMtt_donne_cb());
                    mttDejALL = BigDecimalUtil.add(mttDejALL, caisseMvmP.getMtt_donne_dej());
                    mttPointALL = BigDecimalUtil.add(mttPointALL, caisseMvmP.getMtt_donne_point());
                    mttReserveALL = BigDecimalUtil.add(mttReserveALL, caisseMvmP.getMtt_portefeuille());
                    
                    mttTotalOffertHorsDet = BigDecimalUtil.add(mttTotalOffertHorsDet, caisseMvmP.getMtt_reduction());
                    
                    if(caisseMvmP.getOpc_client() != null){
                        mttTotalOffertClientALL = BigDecimalUtil.add(mttTotalOffertClientALL, caisseMvmP.getMtt_art_offert());
                        mttTotalReducClientALL = BigDecimalUtil.add(mttTotalReducClientALL, caisseMvmP.getMtt_reduction());
                    } else if(caisseMvmP.getOpc_employe() != null){
                        mttTotalOffertEmployeALL = BigDecimalUtil.add(mttTotalOffertEmployeALL, caisseMvmP.getMtt_art_offert());
                        mttTotalReducEmployeALL = BigDecimalUtil.add(mttTotalReducEmployeALL, caisseMvmP.getMtt_reduction());
                    } else{
                    	mttTotalOffertAutreALL = BigDecimalUtil.add(mttTotalOffertAutreALL, caisseMvmP.getMtt_art_offert());
                        mttTotalReducAutreALL = BigDecimalUtil.add(mttTotalReducAutreALL, caisseMvmP.getMtt_reduction());
                    }
                    mttTotalNetALL = BigDecimalUtil.add(mttTotalNetALL, caisseMvmP.getMtt_commande_net());
                    mttTotalBrutALL = BigDecimalUtil.add(mttTotalBrutALL, caisseMvmP.getMtt_commande());
                    //--------------------------------------------------------------------------------------------------
                    //
                    for (CaisseMouvementArticlePersistant caisseMvmArtP : listDet) {
                        String pref = "";
                        if(BooleanUtil.isTrue(caisseMvmArtP.getIs_annule())){
                        	mttTotalAnnuleArtALL = BigDecimalUtil.add(mttTotalAnnuleArtALL, caisseMvmArtP.getMtt_total());
                            pref = "ANNUL_";
                        } else if(BooleanUtil.isTrue(caisseMvmArtP.getIs_offert())){
                            pref = "OFFR_";
                        }
                        FamilleCuisinePersistant opc_famille_cuisine = null;
                        FamilleStockPersistant opc_famille_stock = null;
                        Long familleId = null;
                        ArticlePersistant opc_article = caisseMvmArtP.getOpc_article();
                        if(isRestau){
                        	if(opc_article != null && opc_article.getOpc_famille_cuisine() != null){
                        		opc_famille_cuisine = (opc_article!=null ? familleService.findById(FamilleCuisinePersistant.class, opc_article.getOpc_famille_cuisine().getId()) : null);
                        	}
                        	familleId = (opc_famille_cuisine != null ? opc_famille_cuisine.getId() : null);
                        } else{
                        	opc_famille_stock = (opc_article!=null ? familleService.findById(FamilleStockPersistant.class, opc_article.getOpc_famille_stock().getId()) : null);	
                        	familleId = (opc_famille_stock != null ? opc_famille_stock.getId() : null);
                        }
						
						if(caisseMvmArtP.getMenu_idx() != null){//----------------------------------Menu
                            if(caisseMvmArtP.getType_ligne().equals("MENU")){
                            	CaisseMouvementArticlePersistant mnuParent = null;
                            	for (CaisseMouvementArticlePersistant det : listDet) {
									if(det.getCode().equals(caisseMvmArtP.getParent_code())) {
										mnuParent = det;
										break;
									}
                            	}                            	
                                listDetCmd.add(getDetailPrint(caisseMvmArtP.getCode(),caisseMvmArtP.getLibelle(), 1, caisseMvmArtP.getMtt_total(),  pref+"MENU", null, mnuParent, null));
                            } else if(caisseMvmArtP.getType_ligne().equals("GROUPE_MENU") || caisseMvmArtP.getType_ligne().equals("GROUPE_FAMILLE")){
                            	CaisseMouvementArticlePersistant mnuParent = null;
                            	for (CaisseMouvementArticlePersistant det : listDet) {
									if(det.getCode().equals(caisseMvmArtP.getParent_code())) {
										mnuParent = det;
										break;
									}
                            	}               
                            	// Si prix sur groupe alors on l'aditionne au menu
                                listDetCmd.add(getDetailPrint(caisseMvmArtP.getOpc_menu().getCode(),caisseMvmArtP.getLibelle(), 0,caisseMvmArtP.getMtt_total(),  pref+"MENU", null, mnuParent, null));
                            } else{
                                // Si article alors on l'isole
                                listDetCmd.add(getDetailPrint(caisseMvmArtP.getCode(),caisseMvmArtP.getLibelle(), ((caisseMvmArtP.getQuantite()==null) ? 0:caisseMvmArtP.getQuantite().intValue()),caisseMvmArtP.getMtt_total(),  pref+"ART_MENU", (isRestau ? opc_famille_cuisine:opc_famille_stock), null, mapFamDetail.get(familleId)));
                            }
                        } else{//-------------------------------------------------------------------Hors menu
                            if(caisseMvmArtP.getType_ligne().equals("GROUPE_FAMILLE")){
                                listDetCmd.add(getDetailPrint(caisseMvmArtP.getCode(),caisseMvmArtP.getLibelle(), ((caisseMvmArtP.getQuantite()==null) ? 0:caisseMvmArtP.getQuantite().intValue()),caisseMvmArtP.getMtt_total(),  pref+"GROUPE_FAMILLE", (isRestau ? opc_famille_cuisine:opc_famille_stock), null, mapFamDetail.get(familleId)));
                            } else{
                                listDetCmd.add(getDetailPrint(caisseMvmArtP.getCode(),caisseMvmArtP.getLibelle(), ((caisseMvmArtP.getQuantite()==null) ? 0:caisseMvmArtP.getQuantite().intValue()),caisseMvmArtP.getMtt_total(),  pref+"ART", (opc_article!=null?(isRestau ? opc_famille_cuisine:opc_famille_stock):null), null, mapFamDetail.get(familleId)));
                            }
                        }
                    }
                }
                
                mois = mois.length()==1 ? "0"+mois : mois;
                String dayShort = day.substring(0, day.indexOf("/"));
				String daySt = dayShort.length()==1 ? "0"+day : dayShort;
                
                // Print
                if(!isPeriodePrint) {
            		 Map<String, Object> mapDays = new LinkedHashMap<>();
            		 mapDays.put("list_cmd", listDetCmd);
            		 mapDays.put("mtt_offert", mttTotalOffertHorsDet);
            		 mapDays.put("date_raz", daySt+"/"+mois+"/"+annee);
            		 
            		 listDetailDays.add(mapDays);
                }
            }
            if(!isPeriodePrint) {
            	mapRetour.put("list_days_mtt", listDetailDays);
            }
            // Imprimer le pied de page
            BigDecimal mttMoyen = BigDecimalUtil.divide(
            				BigDecimalUtil.add(mttEspeceALL, mttCarteALL, mttChequeALL, mttDejALL, mttPointALL, mttReserveALL), 
            			BigDecimalUtil.get(nbrNoteOkAll));
            
            BigDecimal[] mttsMode = {
            		mttTotalNetALL, 
            		mttTotalBrutALL, 
            		mttEspeceALL, 
            		mttCarteALL, 
            		mttChequeALL, 
            		mttDejALL, 
            		mttReserveALL, 
            		mttPointALL
            	};
            
            BigDecimal[] mttsRecap = {
            		BigDecimalUtil.get(nbrNoteOkAll), 
            		mttMoyen, 
            		mttTotalReducClientALL, 
            		mttTotalReducEmployeALL, 
            		mttTotalOffertClientALL, 
            		mttTotalOffertEmployeALL, 
            		mttTotalAnnuleALL,
            		mttTotalReducAutreALL,
            		mttTotalOffertAutreALL,
            		mttTotalAnnuleArtALL
            	};
            
        	mapRetour.put("mtt_recap", mttsRecap);
        	mapRetour.put("mtt_mode", mttsMode);
        	
        	if(isPeriodePrint) {
        		mapRetour.put("date_raz", DateUtil.dateToString(debut.getDate_journee())+"-"+DateUtil.dateToString(fin.getDate_journee()));
        	}
            String[][] data = {
            	{"Comandes : ", nbrCmdFinal + "/"+nbrCmdInitial+"."},
            	{"Mtt initial : ", BigDecimalUtil.formatNumber(mttTotalIitial)+ "."},
            	{"Mtt total : ", BigDecimalUtil.formatNumber(mttTotalFinal)+ "."},
            	{"Nbr cmd retirées : ", (nbrCmdInitial-nbrCmdFinal)+"."},
            	{"Optimisation : ", BigDecimalUtil.formatNumber(BigDecimalUtil.substract(mttTotalIitial, mttTotalFinal)) + "."}
            };
            if(modeAffichage == null){// Mode affichage html ou pdf
            
            } else{
            	mapRetour.put("data_recap", data);
            	//mapRetour.put("date_raz", mois+"/"+annee);
            }
            // Impression rapport mensuel
            if(isOptim && !isCaisse){
            	rapport = "<html>"
                        + "<h4>" + nbrCmdFinal + " / "+nbrCmdInitial+" commandes initiales.</h4><br>"
                        + "<h4>Montant initial : " + BigDecimalUtil.formatNumber(mttTotalIitial)+ "</h4>"
                        + "<h4>Montant total : " + BigDecimalUtil.formatNumber(mttTotalFinal)+"</h4>";
                	rapport = rapport + "<h4 style='color:blue;'>Nbr commandes optimisées : " + (nbrCmdInitial-nbrCmdFinal) + "</h4>"
                        + "<h4 style='color:blue;'>Montant de l'optimisation : " + BigDecimalUtil.formatNumber(BigDecimalUtil.substract(mttTotalIitial, mttTotalFinal)) + "</h4>";
            } else{
            	rapport = "<html>"
                        + nbrCmdFinal+" commandes</h4><br>"
                        + "<h4>Montant total : " + BigDecimalUtil.formatNumber(mttTotalFinal)+"</h4>";
            }
            
            rapport = rapport + "</html>";
        } catch (Exception e) {
            e.printStackTrace();
            rapport = "<html><h4 style='color:red;'>Erreur lors de l'impression : "+e.getMessage()+" !</h4></html>";
        }
        mapRetour.put("listIdsTraite", listIds);
        mapRetour.put("rapport", "<h3>Récapitulatif de la RAZ</h3>"+rapport);
        
        return mapRetour;
    }
	private Map<String, Object> getRazBoissonData(String modeAffichage, List<CaisseMouvementPersistant> listMouvement) {
		IFamilleService familleService = ServiceUtil.getBusinessBean(IFamilleService.class);
		String rapport = "";
		Map<String, Object> mapRetour = new HashMap<>();
        Long boissonFroideId = ContextAppli.getEtablissementBean().getFam_boisson_froide();
        Long boissonChaudeId = ContextAppli.getEtablissementBean().getFam_boisson_chaude();
        
        if(boissonFroideId == null && boissonChaudeId == null){
        	mapRetour.put("rapport", "<span style='color:red;'><i class='fa fa-warning'></i> Les familles des boissons ne sont pas encore configurées.</span>");
            return mapRetour;
        }
        
        List<Long> listFroidIds = new ArrayList<>();
        listFroidIds.add(boissonFroideId);
        List<Long> listChaudIds = new ArrayList<>();
        listChaudIds.add(boissonChaudeId);
        
        List<FamillePersistant> listFamFroid = familleService.getFamilleEnfants("CU", boissonFroideId, true);
        if(listFamFroid != null){
	        for( FamillePersistant fam : listFamFroid){
	        	listFroidIds.add(fam.getId());
	        }
        }
        List<FamillePersistant> listFamChaud = familleService.getFamilleEnfants("CU", boissonChaudeId, true);
        for( FamillePersistant fam : listFamChaud){
        	listChaudIds.add(fam.getId());
        }
        
        try {
            if(listMouvement == null || listMouvement.isEmpty()){
            	mapRetour.put("rapport", "Aucun mouvement trouvé");
                return mapRetour;
            }
            
            Calendar cal = DateUtil.getCalendar(listMouvement.get(0).getOpc_caisse_journee().getOpc_journee().getDate_journee());
    		String mois = ""+(cal.get(Calendar.MONTH)+1);
    		String annee = ""+cal.get(Calendar.YEAR);
            
            //
            Map<String, MouvementDetailPrintBean> mapCmdFroides = new HashMap<>();
            Map<String, MouvementDetailPrintBean> mapCmdChaudes = new HashMap<>();
            
            for(CaisseMouvementPersistant mvmP : listMouvement){   
            	if(BooleanUtil.isTrue(mvmP.getIs_annule())){
            		continue;
            	}
                for (CaisseMouvementArticlePersistant caisseMvmArtP : mvmP.getList_article()) {
                	if(BooleanUtil.isTrue(caisseMvmArtP.getIs_annule())){
                		continue;
                	}
                	
                	ArticlePersistant opc_article = caisseMvmArtP.getOpc_article();
					FamilleCuisinePersistant opc_famille_cuisine = ((opc_article != null && opc_article.getOpc_famille_cuisine()!=null) 
									? familleService.findById(FamilleCuisinePersistant.class, opc_article.getOpc_famille_cuisine().getId()) : null);
					if(!(
                            caisseMvmArtP.getType_ligne().equals(TYPE_LIGNE_COMMANDE.ART.toString()) 
                            || caisseMvmArtP.getType_ligne().equals(TYPE_LIGNE_COMMANDE.ART_MENU.toString())
                            )
                            || opc_famille_cuisine == null
                            ){
                       continue;
                    }
                    
                    boolean isBoissonFroide = listFroidIds.contains(opc_famille_cuisine.getId());
                    boolean isBoissonChaude = listChaudIds.contains(opc_famille_cuisine.getId());
                    //
                    if(!isBoissonFroide && !isBoissonChaude){
                        continue;
                    }
                    MouvementDetailPrintBean det = null;
                    if(isBoissonFroide){
                        det = mapCmdFroides.get(""+opc_article.getId());
                    } else{
                        det = mapCmdChaudes.get(""+opc_article.getId());
                    }
                    if(det == null){
                        det = new MouvementDetailPrintBean();
                        det.setCode(caisseMvmArtP.getCode());
                        det.setLibelle(caisseMvmArtP.getLibelle());
                        det.setMtt_total(caisseMvmArtP.getMtt_total());
                        det.setQuantite(caisseMvmArtP.getQuantite());
                        //
                        if(isBoissonFroide){
                            mapCmdFroides.put(""+opc_article.getId(), det);
                        } else{
                            mapCmdChaudes.put(""+opc_article.getId(), det);
                        }
                    } else{
                        det.setMtt_total(BigDecimalUtil.add(det.getMtt_total(), caisseMvmArtP.getMtt_total()));
                        det.setQuantite(BigDecimalUtil.add(det.getQuantite(), caisseMvmArtP.getQuantite()));
                    }
                }
            }
            mapRetour.put("data_froid", mapCmdFroides);
            mapRetour.put("data_chaud", mapCmdChaudes);
            mapRetour.put("date_raz", mois+"/"+annee);
            	
            if(mapCmdFroides.size() > 0 || mapCmdChaudes.size() > 0){
            	rapport = rapport + "Toutes les commandes ont été imprimées.";
            }
                
        } catch (Exception e) {
            e.printStackTrace();
            rapport = "<html><h4 style='color:red;'>Erreur lors de l'impression : "+e.getMessage()+" !</h4></html>";
        }

        mapRetour.put("rapport", rapport);
        
        return mapRetour;
    }
	
    private void marquerMvmImprimed(List<Long> listIds) {
    	IMouvementService mouvementService = ServiceUtil.getBusinessBean(IMouvementService.class);
    	if(listIds == null || listIds.isEmpty()) {
    		return;
    	}
         // Maj flag impression
    	mouvementService.getQuery("update CaisseMouvementPersistant set is_imprime=true where id in :listIds")
                .setParameter("listIds", listIds)
                .executeUpdate();
    }
    
    private List<CaisseMouvementPersistant> getListMouvementByDate(Long journeeDebutId, Long journeeFinId) {
    	IMouvementService mouvementService = ServiceUtil.getBusinessBean(IMouvementService.class);
    	
    	return mouvementService.getQuery("from CaisseMouvementPersistant caisseMouvement where "
        		+ "caisseMouvement.opc_caisse_journee.opc_journee.id >=:journeeDebutId "
    			+ "and caisseMouvement.opc_caisse_journee.opc_journee.id <=:journeeFinId "
//    			+ "and (caisseMouvement.is_annule is null or caisseMouvement.is_annule=0) "
                + "order by id desc")
        		.setParameter("journeeDebutId", journeeDebutId)
				.setParameter("journeeFinId", journeeFinId)
                .getResultList();
    }
    private List<CaisseMouvementPersistant> getListMouvementByJournee(Long journeeId) {
    	IMouvementService mouvementService = ServiceUtil.getBusinessBean(IMouvementService.class);
        return mouvementService.getQuery("from CaisseMouvementPersistant where "
        		+ "opc_caisse_journee.opc_journee.id=:joureeId "
//        		+ "and (is_annule is null or is_annule=0) "
                + "order by id desc")
        		.setParameter("joureeId", journeeId)
                .getResultList();
    }
    private JourneePersistant getJourneeByDate(Date dateJournee) {
    	IMouvementService mouvementService = ServiceUtil.getBusinessBean(IMouvementService.class);
		Calendar cal = DateUtil.getCalendar(dateJournee);
		
		List<JourneePersistant> listJournee = mouvementService.getQuery("from JourneePersistant where day(date_journee)=:jour "
				+ "and month(date_journee)=:mois and year(date_journee)=:annee")
				.setParameter("jour", cal.get(Calendar.DAY_OF_MONTH))
				.setParameter("mois", cal.get(Calendar.MONTH)+1)
				.setParameter("annee", cal.get(Calendar.YEAR))
				.getResultList();
		
		return (listJournee.size() > 0 ? listJournee.get(0) : null); 
	}
    private List<CaisseMouvementPersistant> getListMouvementJourneeCaissePointe(Long journeeId) {
    	IMouvementService mouvementService = ServiceUtil.getBusinessBean(IMouvementService.class);
        return mouvementService.getQuery("from CaisseMouvementPersistant where opc_caisse_journee.opc_journee.id=:joureeId "
        		+ "and (is_annule is null or is_annule=0) "
                + "and is_imprime=1 "
                + "order by id desc")
                .setParameter("joureeId", journeeId)
                .getResultList();
    }

//    private List<CaisseMouvementPersistant> getListMouvementMoisCaissePointe(Date dateDebut, Date dateFin) {
//    	IMouvementService mouvementService = ServiceUtil.getBusinessBean(IMouvementService.class);
//    	JourneePersistant journeeDebut = getJourneeOrNextByDate(dateDebut);
//    	JourneePersistant journeeFin = getJourneeOrPreviousByDate(dateFin);
//    	if(journeeFin == null){
//			journeeFin = journeeDebut;
//		}
//    	
//        return mouvementService.getQuery("from CaisseMouvementPersistant where "
//        		+ "opc_caisse_journee.opc_journee.id >=:journeeDebutId "
//    			+ "and opc_caisse_journee.opc_journee.id <=:journeeFinId "
//    			+ "and (is_annule is null or is_annule=0) "
//                + "and is_imprime=1 "
//                + "order by id desc")
//        		.setParameter("journeeDebutId", journeeDebut.getId())
//				.setParameter("journeeDebutId", journeeFin.getId())
//                .getResultList();
//    }
    
//    private List<CaisseMouvementPersistant> getListMouvementMoisCaisse(Long journeeId) {
//        Date dateJournee = ContextRestaurant.getJourneeBean().getDate_journee();
//        Calendar dtCal = DateUtil.getCalendar(dateJournee);
//        
//        int mois = dtCal.get(Calendar.MONTH);
//        int annee = dtCal.get(Calendar.YEAR);
//        
//        Date dateDebut = DateUtil.stringToDate("01/"+mois+"/"+annee);
//        Date dateFin = DateUtil.stringToDate(dtCal.getMaximum(Calendar.DAY_OF_MONTH)+"/"+mois+"/"+annee);
//        
//        return mouvementService.getQuery("from CaisseMouvementPersistant where DATE(date_vente)>=:dtMvmDebt and DATE(date_vente)<=:dtMvmFin "
//                + "order by id desc")
//                .setParameter("dtMvmDebt", dateDebut)
//                .setParameter("dtMvmFin", dateFin)
//                .getResultList();
//    }
    
    private MouvementDetailPrintBean getDetailPrint(String code, String libelle, int qte, BigDecimal mtt, String type, FamillePersistant famille, CaisseMouvementArticlePersistant menu, List<FamillePersistant> listParents){
    	MouvementDetailPrintBean detPrint = new MouvementDetailPrintBean();
        detPrint.setCode(code);
        detPrint.setLibelle(libelle);
        detPrint.setType(type);
        detPrint.setFamille(famille);
        
        if(famille != null) {
        	detPrint.setListfamilleParent(listParents);
        }
        detPrint.setMenu(menu);
        detPrint.setMtt_total(BigDecimalUtil.add(detPrint.getMtt_total(), mtt));
        detPrint.setQuantite(BigDecimalUtil.add(detPrint.getQuantite(), BigDecimalUtil.get(""+qte)));
        
        return detPrint;
    }
    
	private void archiverPdfRazGed(String tp, String tpLib, JourneePersistant dateDebut, File pdfFile) {
		IGedService gedService = ServiceUtil.getBusinessBean(IGedService.class);
		IMouvementService mouvementService = ServiceUtil.getBusinessBean(IMouvementService.class);
		ITreeService treeService = ServiceUtil.getBusinessBean(ITreeService.class);
		List<GedPersistant> listData = (List<GedPersistant>) treeService.getListeTree(GedPersistant.class, false, false);
		GedPersistant gedPRoot = (GedPersistant) treeService.getTreeRoot(GedPersistant.class);
		
		Long parentId = null;
		
		// Création de l'arborscence des clients
		boolean isFounded = false;
		for (GedPersistant gedP : listData) {
			if("RAZ".equals(gedP.getCode())){
				parentId = gedP.getId();
				isFounded = true;
				break;
			}
		}
		if(!isFounded){
			GedBean gedBean = new GedBean();
			gedBean.setCode("RAZ");
			gedBean.setLibelle("Dossiers RAZ");
			gedBean.setParent_id(gedPRoot.getId());
			gedBean.setDate_maj(new Date());
			gedBean.setOpc_etablissement(ContextAppli.getEtablissementBean());
			//
			treeService.createTree(gedBean);
			
			parentId = gedBean.getId();
		}
		
		List<GedBean> listEnfant = treeService.getTreeEnfants(GedBean.class, parentId, false);
		GedPersistant currGedRaz = null;
		for (GedPersistant gedP : listEnfant) {
			if(gedP.getCode().equals(tp)){
				currGedRaz = gedP;
				break;
			}
		}
		GedBean gedBean = null;
		if(currGedRaz == null){
			gedBean = new GedBean();
			gedBean.setCode(tp);
			gedBean.setLibelle(tpLib);
			gedBean.setParent_id(parentId);
			gedBean.setType_ged("RAZ");
			gedBean.setDate_maj(new Date());
			gedBean.setOpc_etablissement(ContextAppli.getEtablissementBean());
			gedBean.setElement_id(parentId);
			//
			treeService.createTree(gedBean);
		} else{
			gedBean = ServiceUtil.persistantToBean(GedBean.class, currGedRaz);
		}
		
		String path = StrimUtil._GET_PATH("ged")+"/"+gedBean.getId();
		
		GedFichierPersistant gedFichierP = new GedFichierPersistant();
		gedFichierP.setLibelle("RAZ "+"_"+DateUtil.dateToString(dateDebut.getDate_journee()));
		gedFichierP.setExtention("pdf");
		gedFichierP.setFile_name(pdfFile.getName());
		gedFichierP.setPath(path);
		gedFichierP.setOpc_ged(gedBean);
		//
		gedService.mergeDetail(gedFichierP);
		
		try {
			FileUtils.copyFile(pdfFile, new File(path+"/"+pdfFile.getName()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private Map getRepartitionVenteArticleNonRestau(JourneePersistant journeeDebutId, JourneePersistant journeeFinId, Long familleIncludeId){
		IFamilleService familleService = ServiceUtil.getBusinessBean(IFamilleService.class);
		IMouvementService mouvementService = ServiceUtil.getBusinessBean(IMouvementService.class);
		Set<Long> familleIncludeIds = new HashSet<Long>();
		if(familleIncludeId != null && !familleIncludeId.toString().equals("-999")) { 
			List<FamillePersistant> familleAll = familleService.getFamilleEnfants("ST", familleIncludeId, true);
			for (FamillePersistant famillePersistant : familleAll) {
				familleIncludeIds.add(famillePersistant.getId());
			}
			familleIncludeIds.add(familleIncludeId);
		}
		//
		// Articles
		Query query = mouvementService.getNativeQuery("select det.elementId, sum(det.quantite) as qte, sum(det.mtt_total), det.libelle, fam.libelle as famille "
				+ "from caisse_mouvement_article det "
				+ "inner join caisse_mouvement mvm on det.mvm_caisse_id=mvm.id "
				+ "inner join caisse_journee cj on mvm.caisse_journee_id=cj.id "
				+ "inner join journee jr on cj.journee_id=jr.id "
				+ "inner join article art on det.article_id=art.id "
				+ "left join famille fam on art.famille_stock_id=fam.id "
				+ "where det.article_id is not null and "
				+ "jr.id>=:journeeDebutId and jr.id<=:journeeFinId "
				+(familleIncludeIds.size() > 0 ? "and fam.id in (:familleIncludeId) ":"")
				+ "and (mvm.is_annule is null or mvm.is_annule=0) "
				+ "and (det.is_annule is null or det.is_annule=0) "
				+ "group by det.elementId "
				+ "order by fam.b_left, qte desc");
			query.setParameter("journeeDebutId", journeeDebutId.getId());
			query.setParameter("journeeFinId", journeeFinId.getId());
		if(familleIncludeIds.size() > 0){
			query.setParameter("familleIncludeId", familleIncludeIds);
		}
		List<Object[]> listVenteArticle = query.getResultList();
		
		Map<Long, RepartitionBean> mapArticle = new LinkedHashMap<>();
		for (Object[] data : listVenteArticle) {
			String libelle = (String) data[3];
			Long elementId = ((BigInteger) data[0]).longValue();
			
			RepartitionBean repMenu = new RepartitionBean();
			repMenu.setLibelle(libelle);
			repMenu.setFamille((String) data[4]);
			repMenu.setElementId(elementId);
			repMenu.setQuantite(BigDecimalUtil.get(""+data[1]));
			repMenu.setMontant((BigDecimal)data[2]);
			
			mapArticle.put(elementId, repMenu);
		}
		
		//---------------------------------------------------------------------------------------------
		// Enlever les offres
		query = mouvementService.getNativeQuery("select sum(IFNULL(mvm.mtt_reduction, 0)) "
					+ "from caisse_mouvement mvm "
					+ "inner join caisse_journee cj on mvm.caisse_journee_id=cj.id "
					+ "inner join journee jr on cj.journee_id=jr.id "
					+ "where (mvm.is_annule is null or mvm.is_annule=0) and "
					+ "jr.id>=:journeeDebutId and jr.id<=:journeeFinId ");
		
			query.setParameter("journeeDebutId", journeeDebutId.getId());
			query.setParameter("journeeFinId", journeeFinId.getId());
		
		BigDecimal offreData = (BigDecimal) mouvementService.getSingleResult(query);
		
		Map mapRetour = new HashMap<>();
		mapRetour.put("ARTS", mapArticle);
		mapRetour.put("OFFRE", offreData);
		
		return mapRetour ;
	}
	
//	@SuppressWarnings("unchecked")
//	private Map getRepartitionVenteArticleRestau(Long journeeDebutId, Long journeeFinId, Long familleIncludeId){
//		IFamilleService familleService = ServiceUtil.getBusinessBean(IFamilleService.class);
//		IMouvementService mouvementService = ServiceUtil.getBusinessBean(IMouvementService.class);
//		EtablissementPersistant restauP = (EtablissementPersistant) mouvementService.findById(EtablissementPersistant.class, ContextAppli.getEtablissementBean().getId());
//		String[] familleExclude = StringUtil.getArrayFromStringDelim(restauP.getVente_familles(), ";");
//		String[] menuEquiv = StringUtil.getArrayFromStringDelim(restauP.getVente_menus_art(), ";");
//		Map<Long, List<Long>> mapMenuArtEquiv = new HashMap<>();
//		
//		if(menuEquiv != null){
//			for (String mnuArt : menuEquiv) {
//				String[] menuArtArray = StringUtil.getArrayFromStringDelim(mnuArt, ":");
//				Long menuId = Long.valueOf(menuArtArray[0]);
//				List<Long> artArray = new ArrayList<Long>();
//				String[] arts = StringUtil.getArrayFromStringDelim(menuArtArray[1], "-");
//				//
//				for (String art : arts) {
//					artArray.add(Long.valueOf(art));
//				}
//				mapMenuArtEquiv.put(menuId, artArray);
//			}
//		}
//		Set<Long> famExcludeIds = new HashSet<>();
//		if(familleExclude != null){
//			for(String fam : familleExclude){
//				famExcludeIds.add(Long.valueOf(fam));
//			}
//		}
//		
//		//		
//		Set<Long> familleIncludeIds = new HashSet<Long>();
//		if(familleIncludeId != null && !familleIncludeId.toString().equals("-999")) { 
//			List<FamillePersistant> familleAll = familleService.getFamilleEnfants("CU", familleIncludeId, true);
//			for (FamillePersistant famillePersistant : familleAll) {
//				familleIncludeIds.add(famillePersistant.getId());
//			}
//			familleIncludeIds.add(familleIncludeId);
//		}
//		
//		// Les menus
//		Query query = mouvementService.getNativeQuery("select det.menu_idx, det.elementId, det.mtt_total, det.libelle, compo.libelle as famille "
//				+ "from caisse_mouvement_article det "
//				+ "inner join caisse_mouvement mvm on det.mvm_caisse_id=mvm.id "
//				+ "inner join caisse_journee cj on mvm.caisse_journee_id=cj.id "
//				+ "inner join journee jr on cj.journee_id=jr.id "
//				+ "left join menu_composition compo on det.parent_code=concat('M_',compo.code) "
//				+ "where det.is_menu is not null and det.is_menu=1 and "
//				+ "jr.id>=:journeeDebutId and jr.id<=:journeeFinId "
//				+ "and (mvm.is_annule is null or mvm.is_annule=0) "
//				+ "and (det.is_annule is null or det.is_annule=0) "
//				+ "order by compo.b_left, det.libelle");
//			query.setParameter("journeeDebutId", journeeDebutId);
//			query.setParameter("journeeFinId", journeeFinId);
//		
//			List<Object[]> listVenteMenu = query.getResultList();
//		
//		// On cumul les montants
//		Map<Long, RepartitionBean> mapMenuRecap = new LinkedHashMap<>();
//		for(Object[] data : listVenteMenu){
//			String libelle = (String) data[3];
//			Long elementId = ((BigInteger) data[1]).longValue();
//			RepartitionBean repMenu = mapMenuRecap.get(elementId);
//			if(repMenu == null){
//				repMenu = new RepartitionBean();
//				repMenu.setElementId(elementId);
//				repMenu.setLibelle(libelle);
//				repMenu.setFamille((String) data[4]);
//				mapMenuRecap.put(elementId, repMenu);
//			}
//			repMenu.setQuantiteMenu(BigDecimalUtil.add(repMenu.getQuantiteMenu(), BigDecimalUtil.get(1)));
//			repMenu.setMontantMenu(BigDecimalUtil.add(repMenu.getMontantMenu(), (BigDecimal)data[2]));
//		}
//		
//		// Articles en hors menu
//		query = mouvementService.getNativeQuery("select det.elementId, sum(det.quantite) as qte, sum(det.mtt_total), det.libelle, fam.libelle as famille "
//				+ "from caisse_mouvement_article det "
//				+ "inner join caisse_mouvement mvm on det.mvm_caisse_id=mvm.id "
//				+ "inner join caisse_journee cj on mvm.caisse_journee_id=cj.id "
//				+ "inner join journee jr on cj.journee_id=jr.id "
//				+ "inner join article art on det.article_id=art.id "
//				+ "left join famille fam on art.famille_cuisine_id=fam.id "
//				+ "where det.menu_idx is null and det.article_id is not null and "
//				+ "jr.id>=:journeeDebutId and jr.id<=:journeeFinId "
//				+(famExcludeIds.size()>0 ? "and fam.id not in (:excludeIds) ":"")
//				+(familleIncludeIds.size() > 0 ? "and fam.id in (:familleIncludeId) ":"")
//				+ "and (mvm.is_annule is null or mvm.is_annule=0) "
//				+ "and (det.is_annule is null or det.is_annule=0) "
//				+ "group by det.elementId "
//				+ "order by fam.b_left, qte desc");
//			query.setParameter("journeeFinId", journeeFinId);
//			query.setParameter("journeeDebutId", journeeDebutId);
//		if(famExcludeIds!=null && famExcludeIds.size()>0){
//			query.setParameter("excludeIds", famExcludeIds);
//		}
//		if(familleIncludeIds.size() > 0){
//			query.setParameter("familleIncludeId", familleIncludeIds);
//		}
//		List<Object[]> listVenteArticleHorsMenu = query.getResultList();
//		
//		Map<Long, RepartitionBean> mapArticleHorsMenu = new LinkedHashMap<>();
//		for (Object[] data : listVenteArticleHorsMenu) {
//			String libelle = (String) data[3];
//			Long elementId = ((BigInteger) data[0]).longValue();
//			String famille = (String) data[4];
//			
//			RepartitionBean repMenu = new RepartitionBean();
//			repMenu.setLibelle(libelle);
//			repMenu.setFamille(famille);
//			repMenu.setElementId(elementId);
//			repMenu.setQuantiteHorsMenu(BigDecimalUtil.get(""+data[1]));
//			repMenu.setMontantHorsMenu((BigDecimal)data[2]);
//			
//			mapArticleHorsMenu.put(elementId, repMenu);
//		}
//		
//		// Articles en menu
//		query = mouvementService.getNativeQuery("select det.elementId, sum(det.quantite), sum(det.mtt_total), det.libelle, fam.libelle as famille "
//				+ "from caisse_mouvement_article det "
//				+ "inner join caisse_mouvement mvm on det.mvm_caisse_id=mvm.id "
//				+ "inner join caisse_journee cj on mvm.caisse_journee_id=cj.id "
//				+ "inner join journee jr on cj.journee_id=jr.id "
//				+ "inner join article art on det.article_id=art.id "
//				+ "left join famille fam on art.famille_cuisine_id=fam.id "
//				+ "where det.menu_idx is not null and det.article_id is not null and "
//				+ "jr.id>=:journeeDebutId and jr.id<=:journeeFinId "
//				+(famExcludeIds.size()>0 ? "and fam.id not in (:excludeIds) ":"")
//				+(familleIncludeIds.size() > 0 ? "and fam.id in (:familleIncludeId) ":"")
//				+ "and (mvm.is_annule is null or mvm.is_annule=0) "
//				+ "and (det.is_annule is null or det.is_annule=0) "
//				+ "group by det.elementId "
//				+ "order by fam.b_left, det.libelle");
//			query.setParameter("journeeDebutId", journeeDebutId);
//			query.setParameter("journeeFinId", journeeFinId);
//		
//		if(famExcludeIds!=null && famExcludeIds.size()>0){
//			query.setParameter("excludeIds", famExcludeIds);
//		}
//		if(familleIncludeIds.size() > 0){
//			query.setParameter("familleIncludeId", familleIncludeIds);
//		}
//		
//		List<Object[]> listVenteArticleMenu = query.getResultList();
//		Map<Long, RepartitionBean> mapArticleMenu = new LinkedHashMap<>();
//		for (Object[] data : listVenteArticleMenu) {
//			String libelle = (String) data[3];
//			Long elementId = ((BigInteger) data[0]).longValue();
//			String famille = (String) data[4];
//			
//			RepartitionBean repMenu = new RepartitionBean();
//			repMenu.setLibelle(libelle);
//			repMenu.setFamille(famille);
//			repMenu.setElementId(elementId);
//			repMenu.setQuantiteMenu(BigDecimalUtil.get(""+data[1]));
//			repMenu.setMontantMenu((BigDecimal)data[2]);
//			
//			mapArticleMenu.put(elementId, repMenu);
//		}
//		
//		// Regroupement les menu
//		List<Long> idsToRemove = new ArrayList<>();
//		for(Long elementId : mapMenuRecap.keySet()){
//			List<Long> artsEquiv = mapMenuArtEquiv.get(elementId);
//			if(artsEquiv == null){
//				continue;
//			}
//			
//			RepartitionBean repBean = mapMenuRecap.get(elementId);
//			//
//			for(Long artId : artsEquiv){
//				RepartitionBean repHmBean = mapArticleHorsMenu.get(artId);
//				if(repHmBean != null){
//					idsToRemove.add(artId);
//					repBean.setMontantHorsMenu(BigDecimalUtil.add(repBean.getMontantHorsMenu(), repHmBean.getMontantHorsMenu()));
//					repBean.setQuantiteHorsMenu(BigDecimalUtil.add(repBean.getQuantiteHorsMenu(), repHmBean.getQuantiteHorsMenu()));
//				}
//			}
//		}
//		
//		// Supprimer les articles regroupés
//		for(Long elementId : idsToRemove){
//			mapArticleHorsMenu.remove(elementId);	
//		}
//		
//		// Regroupement les articles
//		Map<Long, RepartitionBean> mapArticleFinal = new LinkedHashMap<>();
//		for(Long elementId : mapArticleMenu.keySet()){
//			RepartitionBean repBean = mapArticleMenu.get(elementId);
//			for(Long elementHmId : mapArticleHorsMenu.keySet()){
//				RepartitionBean repHmBean = mapArticleHorsMenu.get(elementHmId);
//				if(repHmBean != null){
//					repBean.setMontantHorsMenu(BigDecimalUtil.add(repBean.getMontantHorsMenu(), repHmBean.getMontantHorsMenu()));
//					repBean.setQuantiteHorsMenu(BigDecimalUtil.add(repBean.getQuantiteHorsMenu(), repHmBean.getQuantiteHorsMenu()));
//				}
//			}
//			mapArticleFinal.put(elementId, repBean);
//		}
//		for(Long elementId : mapArticleHorsMenu.keySet()){
//			RepartitionBean repBean = mapArticleHorsMenu.get(elementId);
//			for(Long elementHmId : mapArticleMenu.keySet()){
//				RepartitionBean repHmBean = mapArticleMenu.get(elementHmId);
//				if(repHmBean != null){
//					repBean.setMontantMenu(BigDecimalUtil.add(repBean.getMontantMenu(), repHmBean.getMontantMenu()));
//					repBean.setQuantiteMenu(BigDecimalUtil.add(repBean.getQuantiteMenu(), repHmBean.getQuantiteMenu()));
//				}
//			}
//			mapArticleFinal.put(elementId, repBean);
//		}
//		
//		//---------------------------------------------------------------------------------------------
//		// Enlever les offres
//		query = mouvementService.getNativeQuery("select sum(IFNULL(mvm.mtt_reduction, 0) + IFNULL(mvm.mtt_art_offert, 0)) "
//					+ "from caisse_mouvement mvm "
//					+ "inner join caisse_journee cj on mvm.caisse_journee_id=cj.id "
//					+ "inner join journee jr on cj.journee_id=jr.id "
//					+ "where (mvm.is_annule is null or mvm.is_annule=0) and "
//					+ "jr.id>=:journeeDebutId and jr.id<=:journeeFinId "
//					+ "and (mvm.is_annule is null or mvm.is_annule=0) ");
//			query.setParameter("journeeDebutId", journeeDebutId);
//			query.setParameter("journeeFinId", journeeFinId);
//		
//		BigDecimal offreData = (BigDecimal) mouvementService.getSingleResult(query);
//		
//		// Ajouter les livraisons
//		// Articles en menu
//		query = mouvementService.getNativeQuery("select sum(det.mtt_total) "
//				+ "from caisse_mouvement_article det "
//				+ "inner join caisse_mouvement mvm on det.mvm_caisse_id=mvm.id "
//				+ "inner join caisse_journee cj on mvm.caisse_journee_id=cj.id "
//				+ "inner join journee jr on cj.journee_id=jr.id "
//				+ "where "
//				+ "jr.id>=:journeeDebutId and jr.id<=:journeeFinId "
//				+ "and (mvm.is_annule is null or mvm.is_annule=0) "
//				+ "and (det.is_annule is null or det.is_annule=0) "
//				+ "and det.type_ligne='LIVRAISON' "
//				+ "group by det.elementId "
//				+ "order by det.libelle");
//			query.setParameter("journeeDebutId", journeeDebutId);
//			query.setParameter("journeeFinId", journeeFinId);
//		BigDecimal livraisonData = (BigDecimal) mouvementService.getSingleResult(query);
//		
//		Map mapRetour = new HashMap();
//		mapRetour.put("MENU", mapMenuRecap);
//		mapRetour.put("ARTS", mapArticleFinal);
//		mapRetour.put("OFFRE", offreData);
//		mapRetour.put("LIVRAISON", livraisonData);
//		
//		return mapRetour ;
//	}
}
