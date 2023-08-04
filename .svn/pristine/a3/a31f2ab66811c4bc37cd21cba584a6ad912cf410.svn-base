/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appli.model.domaine.util_srv.printCom.raz;

import java.awt.Font;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.stock.service.impl.RepartitionBean;
import appli.model.domaine.util_srv.raz.RazDetail;
import framework.controller.ContextGloabalAppli;
import framework.model.common.util.BigDecimalUtil;
import framework.model.util.printGen.PrintCommunUtil;
import framework.model.util.printGen.PrintPosBean;
import framework.model.util.printGen.PrintPosDetailBean;

public class PrintRazPoste {

    public static Font CUSTOM_FONT_9 = new Font("Monospaced", Font.PLAIN, 9); 
    public static Font CUSTOM_FONT_9_B = new Font("Monospaced", Font.BOLD, 9);
    public static Font CUSTOM_FONT_10_B = new Font("Monospaced", Font.BOLD, 10);
    public static Font CUSTOM_FONT_12_B = new Font("Arial", Font.BOLD, 12);
    public static final int PAPER_WIDTH = 180;
    private PrintPosBean printBean;

    private Map<String, List<RazDetail>> mapModePaiement;
    private Map<String, Map<Long, RepartitionBean>> mapRep;
    private String titre;
    private String date;
    private final int X_MTT_START = 167;
    private final int X_QTE_START = 125;
    
    //
    public PrintRazPoste(String titre, String date, Map<String, List<RazDetail>> mapModePaiement, Map<String, Map<Long, RepartitionBean>> mapRep) {
    	this.titre = titre;
        this.mapModePaiement = mapModePaiement;
        this.mapRep = mapRep;
        this.date = date;
        
        int hAdd = 0;
        for (String caisse : mapRep.keySet()) {
        	hAdd += mapRep.get(caisse).size();
        }
        
        this.printBean= new PrintPosBean();
        this.printBean.setTicketHeight(BigDecimalUtil.get(this.mapModePaiement.size() + hAdd + 10));
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
    	
        int y = 10; // Décalage par rapport au logo

        // Société
        Font font = PrintRazPoste.CUSTOM_FONT_12_B;
        listPrintLins.add(new PrintPosDetailBean(ContextAppli.getEtablissementBean().getRaison_sociale(), 0, y, font, "C"));
        y = y + 20;
        
        // Détail -----------------------------------------------------------------------------------------------
        listPrintLins.add(new PrintPosDetailBean(titre, 0, y, PrintRazPoste.CUSTOM_FONT_12_B, "C"));
        y = y + 20;
        
        // Titre
        font = PrintRazPoste.CUSTOM_FONT_10_B;
        listPrintLins.add(new PrintPosDetailBean(this.date, 0, y, font, "C")); 
        y = y + 15;
       
        listPrintLins.add(new PrintPosDetailBean("REPARTITION MODES PAIEMENT", 0, y, PrintCommunUtil.CUSTOM_FONT_10_B, "C"));
        y = y + 15;
        //
        for (String caisse : mapModePaiement.keySet()) {
        	y = y + 15;
        	listPrintLins.add(new PrintPosDetailBean(caisse, 0, y, PrintCommunUtil.CUSTOM_FONT_10_B));
        	y = y + 5;
        	listPrintLins.add(new PrintPosDetailBean(0, y, 220, y));
        	y = y + 15;
         	
			List<RazDetail> listDet = mapModePaiement.get(caisse);
			for (RazDetail recap : listDet) {
	            listPrintLins.add(new PrintPosDetailBean(recap.getLibelle(), 0, y, PrintCommunUtil.CUSTOM_FONT_9));
	            listPrintLins.add(new PrintPosDetailBean(recap.getQuantite(), X_QTE_START, y, PrintCommunUtil.CUSTOM_FONT_9, "R"));
	            listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumber(recap.getMontant()), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9, "R"));
	            y = y + 10;
			}
		}  
        
        y = y + 30;
        listPrintLins.add(new PrintPosDetailBean("VENTE ARTICLES", 0, y, PrintCommunUtil.CUSTOM_FONT_10_B, "C"));
        y = y + 15;
        
        // Détail article
        for (String caisse : mapRep.keySet()) {
        	y = y + 15;
        	listPrintLins.add(new PrintPosDetailBean(caisse, 0, y, PrintCommunUtil.CUSTOM_FONT_10_B));
        	y = y + 5;
        	listPrintLins.add(new PrintPosDetailBean(0, y, 220, y));
        	y = y + 15;
	   	 
        	Map<Long, RepartitionBean> artsMap = mapRep.get(caisse);
        	
	        String oldFam = null;
	        for(Long key : artsMap.keySet()){
				 RepartitionBean repB = artsMap.get(key);
				 if(oldFam == null || !oldFam.equals(repB.getFamille())){
					 listPrintLins.add(new PrintPosDetailBean(repB.getFamille(), 0, y, PrintCommunUtil.CUSTOM_FONT_9_B));
					 y = y + 10;
				 }
				 listPrintLins.add(new PrintPosDetailBean("   "+repB.getLibelle(), 0, y, PrintCommunUtil.CUSTOM_FONT_8));
				 listPrintLins.add(new PrintPosDetailBean(repB.getQuantite().intValue(), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_8, "R"));
				 y = y + 10;
				 
	         	oldFam = repB.getFamille();
	        }
        }
        
        return listPrintLins;
    }
}