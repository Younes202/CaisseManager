//package appli.model.domaine.caisse.service.impl;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.inject.Inject;
//import javax.inject.Named;
//
//import appli.controller.domaine.caisse.ContextAppliCaisse;
//import appli.controller.domaine.util_erp.ContextAppli;
//import appli.controller.domaine.util_erp.ContextAppli.TYPE_LIGNE_COMMANDE;
//import appli.controller.util_ctrl.MouvementDetailPrintBean;
//import appli.model.domaine.caisse.service.IEtatFinancierCaisseService;
//import appli.model.domaine.caisse.service.util.print.bean.PrintPosBean;
//import appli.model.domaine.caisse.service.util.raz.ticket.PrintRazBoisson;
//import appli.model.domaine.stock.persistant.ArticlePersistant;
//import appli.model.domaine.stock.persistant.FamilleCuisinePersistant;
//import appli.model.domaine.stock.persistant.FamillePersistant;
//import appli.model.domaine.stock.service.IFamilleService;
//import appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant;
//import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
//import appli.model.domaine.vente.persistant.JourneePersistant;
//import framework.controller.ContextGloabalAppli;
//import framework.model.common.util.BigDecimalUtil;
//import framework.model.common.util.BooleanUtil;
//import framework.model.common.util.DateUtil;
//import framework.model.common.util.StringUtil;
//
//@Named
//public class EtatFinancierBoissonServiceOLD {
//	@Inject
//	private IFamilleService familleService;
//	@Inject
//	private IEtatFinancierCaisseService etatFinancierService;
//	
//	public Map<String, Object> imprimerRazBoisson(String modeAffichage, Long journeeId) {
//		JourneePersistant journeeP = etatFinancierService.findById(JourneePersistant.class, journeeId);
//		List<CaisseMouvementPersistant> listMouvement = etatFinancierService.getListMouvementJourneeCaissePointe(journeeId);
//		
//		return imprimerRazBoisson(modeAffichage, listMouvement, journeeP.getDate_journee());
//	}
//	public Map<String, Object> imprimerRazBoisson(String modeAffichage, Date dateDebut, Date dateFin) {
//		 List<CaisseMouvementPersistant> listMouvement = etatFinancierService.getListMouvementMoisCaissePointe(dateDebut, dateFin);
//		 
//		 return imprimerRazBoisson(modeAffichage, listMouvement, dateDebut);
//	}
//	private Map<String, Object> imprimerRazBoisson(String modeAffichage, List<CaisseMouvementPersistant> listMouvement, Date dateDebut) {
//		Boolean is_local_print = null;
//		if(ContextAppliCaisse.getCaisseBean() != null) {
//			is_local_print = BooleanUtil.isTrue(ContextAppliCaisse.getCaisseBean().getIs_local_print());
//		} else {
//			is_local_print = !StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CAISSE_PRINT"));
//		}
//		
//		List<PrintPosBean> listPosBean = null;
//		if(!is_local_print){
//			listPosBean = new ArrayList<>();
//		}
//		
//		Calendar cal = DateUtil.getCalendar(dateDebut);
//		String mois = ""+(cal.get(Calendar.MONTH)+1);
//		String annee = ""+cal.get(Calendar.YEAR);
//        String rapport = "";
//        Map<String, Object> mapRetour = new HashMap<>();
//        
//        
//        Long boissonFroideId = ContextAppli.getEtablissementBean().getFam_boisson_froide();
//        Long boissonChaudeId = ContextAppli.getEtablissementBean().getFam_boisson_chaude();
//        
//        if(boissonFroideId == null && boissonChaudeId == null){
//        	mapRetour.put("rapport", "<span style='color:red;'><i class='fa fa-warning'></i> Les familles des boissons ne sont pas encore configurées.</span>");
//            return mapRetour;
//        }
//        
//        try {
//           
//            
//            if(listMouvement == null || listMouvement.isEmpty()){
//            	mapRetour.put("rapport", "Aucun mouvement trouvé");
//                return mapRetour;
//            }
//            //
//            Map<Long, FamillePersistant> artFamMap = new HashMap<>();
//            Map<String, MouvementDetailPrintBean> mapCmdFroides = new HashMap();
//            Map<String, MouvementDetailPrintBean> mapCmdChaudes = new HashMap();
//            for(CaisseMouvementPersistant mvmP : listMouvement){    
//                for (CaisseMouvementArticlePersistant caisseMvmArtP : mvmP.getList_article()) {
//                	ArticlePersistant opc_article = caisseMvmArtP.getOpc_article();
//					FamilleCuisinePersistant opc_famille_cuisine = (opc_article != null ? familleService.findById(FamilleCuisinePersistant.class, opc_article.getOpc_famille_cuisine().getId()) : null);
//					if(!(
//                            caisseMvmArtP.getType_ligne().equals(TYPE_LIGNE_COMMANDE.ART.toString()) 
//                            || caisseMvmArtP.getType_ligne().equals(TYPE_LIGNE_COMMANDE.ART_MENU.toString())
//                            )
//                            || opc_famille_cuisine == null
//                            ){
//                       continue;
//                    }
//                    
//                    if(BooleanUtil.isTrue(caisseMvmArtP.getIs_annule()) || BooleanUtil.isTrue(caisseMvmArtP.getIs_offert())){
//                         continue;
//                    }
//                    
//                    FamillePersistant parentFam = artFamMap.get(opc_article.getId());
//                    
//                    if(parentFam == null){
//                    	parentFam = familleService.getFamilleParent(opc_famille_cuisine.getId());
//                    	artFamMap.put(opc_article.getId(), parentFam);
//                    }
//                    
//                    
//                    if(parentFam == null){
//                        continue;
//                    }
//                    
//                    boolean isBoissonFroide = (boissonFroideId != null && parentFam.getId().equals(boissonFroideId));
//                    boolean isBoissonChaude = (boissonChaudeId != null && parentFam.getId().equals(boissonChaudeId));
//                    //
//                    if(!isBoissonFroide && !isBoissonChaude){
//                        continue;
//                    }
//                    MouvementDetailPrintBean det = null;
//                    if(isBoissonFroide){
//                        det = mapCmdFroides.get(""+opc_article.getId());
//                    } else{
//                        det = mapCmdChaudes.get(""+opc_article.getId());
//                    }
//                    if(det == null){
//                        det = new MouvementDetailPrintBean();
//                        det.setCode(caisseMvmArtP.getCode());
//                        det.setLibelle(caisseMvmArtP.getLibelle());
//                        det.setMtt_total(caisseMvmArtP.getMtt_total());
//                        det.setQuantite(caisseMvmArtP.getQuantite());
//                        //
//                        if(isBoissonFroide){
//                            mapCmdFroides.put("00"+opc_article.getId(), det);
//                        } else{
//                            mapCmdChaudes.put(""+opc_article.getId(), det);
//                        }
//                    } else{
//                        det.setMtt_total(BigDecimalUtil.add(det.getMtt_total(), caisseMvmArtP.getMtt_total()));
//                        det.setQuantite(BigDecimalUtil.add(det.getQuantite(), caisseMvmArtP.getQuantite()));
//                    }
//                }
//            }
//            if(modeAffichage == null){
//	            // Print
//	            if(mapCmdFroides.size() > 0){
//	                PrintRazBoisson pu = new PrintRazBoisson("Boissons froides", mapCmdFroides, (mois+"/"+annee));
//	                // Gérer vl'impression locale
//	                if(listPosBean != null) {
//	                	listPosBean.add(pu.getPrintPosBean());
//	                } else {
//	                	pu.printRecap();
//	                }
//	            }
//	            if(mapCmdChaudes.size() > 0){
//	                PrintRazBoisson pu = new PrintRazBoisson("Boissons chaudes", mapCmdChaudes, (mois+"/"+annee));
//	                // Gérer vl'impression locale
//	                if(listPosBean != null) {
//	                	listPosBean.add(pu.getPrintPosBean());
//	                } else {
//	                	pu.printRecap();
//	                }
//	            }
//            } else{
//            	mapRetour.put("data_froid", mapCmdFroides);
//            	mapRetour.put("data_chaud", mapCmdChaudes);
//            	mapRetour.put("date_raz", mois+"/"+annee);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            rapport = "<html><h4 style='color:red;'>Erreur lors de l'impression : "+e.getMessage()+" !</h4></html>";
//        }
//        rapport = rapport + "Toutes les commandes ont été imprimées.";
//        mapRetour.put("rapport", rapport);
//        
//        if("pdf".equals(modeAffichage)){
//        	mapRetour.put("pdf_file", new EtatFinancierRazBoissonPDF().loadData(mapRetour));
//        } else if("html".equals(modeAffichage)){
//        	mapRetour.put("html_data", new EtatFinancierRazBoissonHTML().loadData(mapRetour));
//        }
//        
//        return mapRetour;
//    }
//    
//}
