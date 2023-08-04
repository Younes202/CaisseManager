/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appli.model.domaine.util_srv.printCom.raz;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.vente.persistant.CaisseJourneePersistant;
import framework.controller.ContextGloabalAppli;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.printGen.PrintCommunUtil;
import framework.model.util.printGen.PrintPosBean;
import framework.model.util.printGen.PrintPosDetailBean;

public class PrintRazEmploye {
    private PrintPosBean printBean;

    private String titre;
    private String date;
    private List<CaisseJourneePersistant> listDataShift;
    Map<String, Map> mapEmpl;
    private String type;
    private Long currentUserId;
    private BigDecimal fraisLivraison;
    //
    public PrintRazEmploye(String titreP, String dateP, 
    		Long currentUserIdP,
    		String typeP, List<CaisseJourneePersistant> listDataShiftP, 
    		Map<String, Map> mapEmplP,
    		BigDecimal fraisLivraisonP) {
    	this.titre = titreP;
        this.date = dateP;
        this.listDataShift = listDataShiftP;
        this.mapEmpl = mapEmplP;
        this.type = typeP;
        this.currentUserId = currentUserIdP;
        this.fraisLivraison = fraisLivraisonP;
        
        this.printBean= new PrintPosBean();
        this.printBean.setTicketHeight(BigDecimalUtil.get(this.listDataShift.size()));
        this.printBean.setMaxLineLength(50);
        this.printBean.setPrinters(ContextGloabalAppli.getGlobalConfig("PRINT_RAZ"));
   	 
        List<PrintPosDetailBean> listDataToPrint = buildMapData();
    	this.printBean.setListDetail(listDataToPrint);
    }
    
    public PrintPosBean getPrintPosBean(){
    	return this.printBean;
    }
    
    public List<PrintPosDetailBean> buildMapData() {
    	List<PrintPosDetailBean> listPrintLins = new ArrayList<>();
		int posMtt = 180;
        int y = 10; // Décalage par rapport au logo

        // Société
        listPrintLins.add(new PrintPosDetailBean(ContextAppli.getEtablissementBean().getRaison_sociale().toUpperCase(), 0, y, PrintCommunUtil.CUSTOM_FONT_12_B, "C"));
        y = y + 20;
         
        listPrintLins.add(new PrintPosDetailBean(titre, 0, y, PrintCommunUtil.CUSTOM_FONT_12_B, "C"));
        y = y + 20;
        
        // Détail -----------------------------------------------------------------------------------------------
        listPrintLins.add(new PrintPosDetailBean(this.date, 0, y, PrintCommunUtil.CUSTOM_FONT_10_B, "C"));
    	y = y + 15; 

    	PrintPosDetailBean pdB = new PrintPosDetailBean(0, y, 220, y);
    	pdB.setType("LP");
    	listPrintLins.add(pdB);
    	y = y + 15; 
        
        String oldCaisse = null;
		int idxShift = 0;
		for(CaisseJourneePersistant data : listDataShift){
			if(type.equals("RE") && !data.getOpc_user().getId().equals(currentUserId)){
				continue;
			}
			
 			boolean isDblCloture = (data.getMtt_cloture_old_espece() != null);
 			BigDecimal mttClotureCaisserEsp = isDblCloture ? data.getMtt_cloture_old_espece() : data.getMtt_cloture_caissier_espece();
 			BigDecimal mttClotureCaisserCheq = isDblCloture ? data.getMtt_cloture_old_cheque() : data.getMtt_cloture_caissier_cheque();
 			BigDecimal mttClotureCaisserDej = isDblCloture ? data.getMtt_cloture_old_dej() : data.getMtt_cloture_caissier_dej();
 			BigDecimal mttClotureCaisserCb = isDblCloture ? data.getMtt_cloture_old_cb() : data.getMtt_cloture_caissier_cb();
 			BigDecimal mttClotureCaissier = BigDecimalUtil.add(mttClotureCaisserEsp, mttClotureCaisserCheq, mttClotureCaisserDej, mttClotureCaisserCb);
 			
 			BigDecimal mttClotureCaissierNet = (mttClotureCaisserEsp==null?null:BigDecimalUtil.substract(mttClotureCaissier, data.getMtt_ouverture()));
 			BigDecimal mttClotureCaissierEspNet = (mttClotureCaisserEsp==null?null:BigDecimalUtil.substract(mttClotureCaisserEsp, data.getMtt_ouverture()));
 			// On retire les montants non calculable
 			BigDecimal mttRef = BigDecimalUtil.substract(data.getMtt_total_net(), data.getMtt_portefeuille(), data.getMtt_donne_point());
 			BigDecimal ecartCaissier = BigDecimalUtil.substract(mttClotureCaissierNet, mttRef);
 			
			idxShift++;
			
			if(oldCaisse == null || !oldCaisse.equals(data.getOpc_caisse().getReference())){
				idxShift = 1;
				listPrintLins.add(new PrintPosDetailBean(data.getOpc_caisse().getReference(), 0, y, PrintCommunUtil.CUSTOM_FONT_10_B, "C"));
				y = y + 10;
			}
			oldCaisse = data.getOpc_caisse().getReference();
			
			// Detail shift
			String shift = "SHIFT "+idxShift;
			shift += "(";
			if("O".equals(data.getStatut_caisse())){
				shift += "OUVERT";
			} else if("E".equals(data.getStatut_caisse())){
				shift += "EN CLOTURE";
			} else{
				shift += "CLOS";
			}
			shift += ")";
			
			listPrintLins.add(new PrintPosDetailBean(shift, 0, y, PrintCommunUtil.CUSTOM_FONT_9_B, "C"));
			y = y + 10;
			
			String dateOuv = "de "+DateUtil.dateToString(data.getDate_ouverture(), "HH:mm:ss")
			+(data.getDate_cloture()!=null ? "à "+StringUtil.getValueOrEmpty(DateUtil.dateToString(data.getDate_cloture(), "HH:mm:ss")): "");

			listPrintLins.add(new PrintPosDetailBean(dateOuv, 0, y, PrintCommunUtil.CUSTOM_FONT_9, "C"));			
			y = y + 10;
			
			String ouvr = "Ouverture :";
			if(data.getOpc_user() != null){
				if(data.getOpc_user().getOpc_employe()==null){
					ouvr += data.getOpc_user().getLogin();
				} else{
					ouvr += data.getOpc_user().getOpc_employe().getNom() + StringUtil.getValueOrEmpty(data.getOpc_user().getOpc_employe().getPrenom());
				}
			}
			listPrintLins.add(new PrintPosDetailBean(ouvr, 0, y, PrintCommunUtil.CUSTOM_FONT_9, "C"));
			y = y + 10;
			
			if(data.getOpc_user_cloture() != null){
				String clo = "Clôture :";
				if(data.getOpc_user_cloture().getOpc_employe()==null){
					clo += data.getOpc_user_cloture().getLogin();
				} else{
					clo += data.getOpc_user_cloture().getOpc_employe().getNom() + StringUtil.getValueOrEmpty(data.getOpc_user_cloture().getOpc_employe().getPrenom());
				}
				listPrintLins.add(new PrintPosDetailBean(clo, 160, y, PrintCommunUtil.CUSTOM_FONT_9, "C"));
				y = y + 10;
			}
			
	        listPrintLins.add(new PrintPosDetailBean(0, y, 190, y));
	        y = y + 10;
			
	        if(!BigDecimalUtil.isZero(data.getMtt_ouverture())){
			listPrintLins.add(new PrintPosDetailBean("Fonds de roulement ", 0, y, PrintCommunUtil.CUSTOM_FONT_10));
			listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(data.getMtt_ouverture()), posMtt, y, PrintCommunUtil.CUSTOM_FONT_10, "R"));
			y = y + 10;
	        }
	        
			// Mtt total
	        if(!BigDecimalUtil.isZero(data.getMtt_total_net())){
	        listPrintLins.add(new PrintPosDetailBean("Montant net vente", 0, y, PrintCommunUtil.CUSTOM_FONT_10));
			listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(data.getMtt_total_net()), posMtt, y, PrintCommunUtil.CUSTOM_FONT_10, "R"));
			y = y + 10;
	        }
	        
	        
	        if(data.getNbr_vente() != null && data.getNbr_vente() > 0){
			listPrintLins.add(new PrintPosDetailBean("Nbr ventes", 0, y, PrintCommunUtil.CUSTOM_FONT_10));
			listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(data.getNbr_vente()), posMtt, y, PrintCommunUtil.CUSTOM_FONT_10, "R"));
			y = y + 10;
	        }
	        
			// Ligne séparateur
	        listPrintLins.add(new PrintPosDetailBean(0, y, 190, y));
	        y = y + 10;
	        
	        if(!BigDecimalUtil.isZero(data.getMtt_annule())){
	        	listPrintLins.add(new PrintPosDetailBean("Annulations CMD ", 0, y, PrintCommunUtil.CUSTOM_FONT_10));
	        	listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(data.getMtt_annule()), posMtt, y, PrintCommunUtil.CUSTOM_FONT_10, "R"));
	        	y = y + 10;
	        }
	        
	        if(!BigDecimalUtil.isZero(data.getMtt_annule_ligne())){
				listPrintLins.add(new PrintPosDetailBean("Annulations Ligne ", 0, y, PrintCommunUtil.CUSTOM_FONT_10));
				listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(data.getMtt_annule_ligne()), posMtt, y, PrintCommunUtil.CUSTOM_FONT_10, "R"));
				y = y + 10;
		        }
	        
	        if(data.getNbr_livraison() != null && data.getNbr_livraison() != 0 && data.getNbr_livraison() > 0){
			if(fraisLivraison != null){
				String val = BigDecimalUtil.formatNumber(BigDecimalUtil.multiply(fraisLivraison, BigDecimalUtil.get((data.getNbr_livraison()==null?0:data.getNbr_livraison()))));
				listPrintLins.add(new PrintPosDetailBean("Livraisons ", 0, y, PrintCommunUtil.CUSTOM_FONT_10));
				listPrintLins.add(new PrintPosDetailBean(val, posMtt, y, PrintCommunUtil.CUSTOM_FONT_10, "R"));
				y = y + 10;
			} 
	        }
	        if(!BigDecimalUtil.isZero(data.getMtt_reduction())){
			listPrintLins.add(new PrintPosDetailBean("Réductions ", 0, y, PrintCommunUtil.CUSTOM_FONT_10));
			listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(data.getMtt_reduction()), posMtt, y, PrintCommunUtil.CUSTOM_FONT_10, "R"));
			y = y + 10;
	        }
	        if(!BigDecimalUtil.isZero(data.getMtt_art_offert())){
			listPrintLins.add(new PrintPosDetailBean("Offerts ", 0, y, PrintCommunUtil.CUSTOM_FONT_10));
			listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(data.getMtt_art_offert()), posMtt, y, PrintCommunUtil.CUSTOM_FONT_10, "R"));
			y = y + 10;
	        }
			 // Ligne séparateur
	        listPrintLins.add(new PrintPosDetailBean(0, y, 190, y));
	        y = y + 10;
			
			// Detail
	        if(!BigDecimalUtil.isZero(data.getMtt_espece())){
	        listPrintLins.add(new PrintPosDetailBean("Especes ", 0, y, PrintCommunUtil.CUSTOM_FONT_10));
			listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(data.getMtt_espece()), posMtt, y, PrintCommunUtil.CUSTOM_FONT_9_B, "R"));
			y = y + 10;
	        }
	        if(!BigDecimalUtil.isZero(data.getMtt_cheque())){
			listPrintLins.add(new PrintPosDetailBean("Chèques ", 0, y, PrintCommunUtil.CUSTOM_FONT_10));
			listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(data.getMtt_cheque()), posMtt, y, PrintCommunUtil.CUSTOM_FONT_9_B, "R"));
			y = y + 10;
	        }
	        if(!BigDecimalUtil.isZero(data.getMtt_dej())){
			listPrintLins.add(new PrintPosDetailBean("Chèques déj.", 0, y, PrintCommunUtil.CUSTOM_FONT_10));
			listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(data.getMtt_dej()), posMtt, y, PrintCommunUtil.CUSTOM_FONT_9_B, "R"));
			y = y + 10;
	        }
	        if(!BigDecimalUtil.isZero(data.getMtt_cb())){
			listPrintLins.add(new PrintPosDetailBean("Carte bancaire ", 0, y, PrintCommunUtil.CUSTOM_FONT_10));
			listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(data.getMtt_cb()), posMtt, y, PrintCommunUtil.CUSTOM_FONT_9_B, "R"));
			y = y + 10;
	        }
	        
			if(!BigDecimalUtil.isZero(data.getMtt_portefeuille())){
				listPrintLins.add(new PrintPosDetailBean("Portefeuille ", 0, y, PrintCommunUtil.CUSTOM_FONT_10));
				listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(data.getMtt_portefeuille()), posMtt, y, PrintCommunUtil.CUSTOM_FONT_9_B, "R"));
				y = y + 10;
			} 
			if(!BigDecimalUtil.isZero(data.getMtt_donne_point())){ 
				listPrintLins.add(new PrintPosDetailBean("Points ", 0, y, PrintCommunUtil.CUSTOM_FONT_10));
				listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(data.getMtt_donne_point()), posMtt, y, PrintCommunUtil.CUSTOM_FONT_9_B, "R"));
				y = y + 10;
			} 
			
			 // Ligne séparateur
	        listPrintLins.add(new PrintPosDetailBean(0, y, 190, y));
	        y = y + 10;
	        
	        if(!BigDecimalUtil.isZero(mttClotureCaissierEspNet)){
			listPrintLins.add(new PrintPosDetailBean("Clôture espèces", 0, y, PrintCommunUtil.CUSTOM_FONT_10));
			listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(mttClotureCaissierEspNet), posMtt, y, PrintCommunUtil.CUSTOM_FONT_9_B, "R"));
			y = y + 10;
	        }
	        if(!BigDecimalUtil.isZero(mttClotureCaisserCheq)){
			listPrintLins.add(new PrintPosDetailBean("Clôture chèque", 0, y, PrintCommunUtil.CUSTOM_FONT_10));
			listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(mttClotureCaisserCheq), posMtt, y, PrintCommunUtil.CUSTOM_FONT_9_B, "R"));
			y = y + 10;
	        }
	        if(!BigDecimalUtil.isZero(mttClotureCaisserDej)){
			listPrintLins.add(new PrintPosDetailBean("Clôture déj", 0, y, PrintCommunUtil.CUSTOM_FONT_10));
			listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(mttClotureCaisserDej), posMtt, y, PrintCommunUtil.CUSTOM_FONT_9_B, "R"));
			y = y + 10;
	        }
	        if(!BigDecimalUtil.isZero(mttClotureCaisserCb)){
			listPrintLins.add(new PrintPosDetailBean("Clôture carte", 0, y, PrintCommunUtil.CUSTOM_FONT_10));
			listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(mttClotureCaisserCb), posMtt, y, PrintCommunUtil.CUSTOM_FONT_9_B, "R"));
			y = y + 10;
	        }
	        
			if("C".equals(data.getStatut_caisse())){
				 // Ligne séparateur
		        listPrintLins.add(new PrintPosDetailBean(0, y, 190, y));
		        y = y + 10;
		        
				BigDecimal ecartEspeses = BigDecimalUtil.substract(mttClotureCaissierEspNet, data.getMtt_espece());
				BigDecimal ecartCb = BigDecimalUtil.substract(mttClotureCaisserCb, data.getMtt_cb());
				BigDecimal ecartCheque = BigDecimalUtil.substract(mttClotureCaisserCheq, data.getMtt_cheque());
				BigDecimal ecartDej = BigDecimalUtil.substract(mttClotureCaisserDej, data.getMtt_dej());
				
				if(!BigDecimalUtil.isZero(ecartEspeses)){
				listPrintLins.add(new PrintPosDetailBean("Ecart espèces", 0, y, PrintCommunUtil.CUSTOM_FONT_10));
				listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(ecartEspeses), posMtt, y, PrintCommunUtil.CUSTOM_FONT_9_B, "R"));
				y = y + 10;
				}
				if(!BigDecimalUtil.isZero(ecartCheque)){
				listPrintLins.add(new PrintPosDetailBean("Ecart chèque", 0, y, PrintCommunUtil.CUSTOM_FONT_10));
				listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(ecartCheque), posMtt, y, PrintCommunUtil.CUSTOM_FONT_9_B, "R"));
				y = y + 10;
				}
				if(!BigDecimalUtil.isZero(ecartDej)){
				listPrintLins.add(new PrintPosDetailBean("Ecart déj", 0, y, PrintCommunUtil.CUSTOM_FONT_10));
				listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(ecartDej), posMtt, y, PrintCommunUtil.CUSTOM_FONT_9_B, "R"));
				y = y + 10;
				}
				if(!BigDecimalUtil.isZero(ecartCb)){
				listPrintLins.add(new PrintPosDetailBean("Ecart carte", 0, y, PrintCommunUtil.CUSTOM_FONT_10));
				listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(ecartCb), posMtt, y, PrintCommunUtil.CUSTOM_FONT_9_B, "R"));
				y = y + 10;
				}
				if(!BigDecimalUtil.isZero(ecartCaissier)){
				listPrintLins.add(new PrintPosDetailBean("** TOTAL ECART", 0, y, PrintCommunUtil.CUSTOM_FONT_10));
				listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(ecartCaissier), posMtt, y, PrintCommunUtil.CUSTOM_FONT_10, "R"));
				y = y + 10;
				}
			} 
		}
		
		
		Map<String, BigDecimal> mapMvm = mapEmpl.get("MVM");
 		Map<String, BigDecimal> mapAnnul = mapEmpl.get("ANNUL");
 		Map<String, BigDecimal> mapOffre = mapEmpl.get("OFFR");

		// Ligne séparateur
        listPrintLins.add(new PrintPosDetailBean(0, y, 190, y));
        y = y + 10;
        
 		for(String key : mapMvm.keySet()) {
 			listPrintLins.add(new PrintPosDetailBean(key, 0, y, PrintCommunUtil.CUSTOM_FONT_10_B));
 			y = y + 10;
 			
 			listPrintLins.add(new PrintPosDetailBean("TOTAL MOUVEMENTS", 0, y, PrintCommunUtil.CUSTOM_FONT_10));
 			listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(mapMvm.get(key)), posMtt, y, PrintCommunUtil.CUSTOM_FONT_10, "R"));
 			y = y + 10;
 			
 			listPrintLins.add(new PrintPosDetailBean("TOTAL ANNULATION", 0, y, PrintCommunUtil.CUSTOM_FONT_10));
 			listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(mapAnnul.get(key)), posMtt, y, PrintCommunUtil.CUSTOM_FONT_10, "R"));
 			y = y + 10;
 			
 			listPrintLins.add(new PrintPosDetailBean("TOTAL OFFRES", 0, y, PrintCommunUtil.CUSTOM_FONT_10));
 			listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(mapOffre.get(key)), posMtt, y, PrintCommunUtil.CUSTOM_FONT_10, "R"));
 			y = y + 10;
		}

        return listPrintLins;
    }
}
