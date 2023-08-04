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
import appli.model.domaine.util_srv.raz.RazDetail;
import framework.controller.ContextGloabalAppli;
import framework.model.common.util.BigDecimalUtil;
import framework.model.util.printGen.PrintCommunUtil;
import framework.model.util.printGen.PrintPosBean;
import framework.model.util.printGen.PrintPosDetailBean;

public class PrintRazGlobale {
    private PrintPosBean printBean;

    Map<String, Map<String, RazDetail>> data;
    private String titre;
    private String date;
    private final int X_MTT_START = 187;
    private final int X_QTE_START = 105;
    
    //
    public PrintRazGlobale(String titre, String date, Map<String, Map<String, RazDetail>> data) {
    	this.titre = titre;
        this.data = data;
        this.date = date;
        
        this.printBean= new PrintPosBean();
        this.printBean.setTicketHeight(BigDecimalUtil.get(this.data.size()));
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
        Font font = PrintCommunUtil.CUSTOM_FONT_12_B;
        listPrintLins.add(new PrintPosDetailBean(ContextAppli.getEtablissementBean().getRaison_sociale(), 0, y, font, "C"));
        y = y + 20;
        
        // Détail -----------------------------------------------------------------------------------------------
        // Titre
        font = PrintCommunUtil.CUSTOM_FONT_10_B;
        listPrintLins.add(new PrintPosDetailBean(this.date, 0, y, font, "C"));
        y = y + 10;

        //---------- On collecte les taux de TVA -----------------------
        y = y + 20;
        listPrintLins.add(new PrintPosDetailBean(titre, 0, y, PrintCommunUtil.CUSTOM_FONT_12_B, "C"));
        y = y + 10;
        y = y + 10;
        
        //
        for(String key : data.keySet()){
        	BigDecimal ttlMtt = null;
        	Map<String, RazDetail> recapMap = data.get(key);
        	
        	if(recapMap.size() == 0){
				continue;
			}
        	
        	 y = y + 10;
        	 listPrintLins.add(new PrintPosDetailBean(0, y, 190, y));// Ligne séparateur
        	 y = y + 10;
             listPrintLins.add(new PrintPosDetailBean(key, 0, y, PrintCommunUtil.CUSTOM_FONT_10_B));
             y = y + 10;
         	 listPrintLins.add(new PrintPosDetailBean(0, y, 190, y));// Ligne séparateur
        	 
        	 for(String key2 : recapMap.keySet()){	
	        	 RazDetail recap = recapMap.get(key2);
	        	 y = y + 10;
	             listPrintLins.add(new PrintPosDetailBean(recap.getLibelle(), 0, y, PrintCommunUtil.CUSTOM_FONT_9));
	             listPrintLins.add(new PrintPosDetailBean(recap.getQuantite(), X_QTE_START, y, PrintCommunUtil.CUSTOM_FONT_9, "R"));
	             listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumber(recap.getMontant()), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9, "R"));
	             
	             ttlMtt = BigDecimalUtil.add(ttlMtt, recap.getMontant());
        	}
        	 
        	 y = y + 10;
        	 PrintPosDetailBean lnPointille = new PrintPosDetailBean(0, y, 190, y);
        	 lnPointille.setType("LP");// Ligne séparateur
        	 listPrintLins.add(lnPointille);
        	 
        	 y = y + 10;
        	 listPrintLins.add(new PrintPosDetailBean("TOTAL", 0, y, PrintCommunUtil.CUSTOM_FONT_9_B));
             listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumber(ttlMtt), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9_B, "R"));
             y = y + 10;
		}   
        return listPrintLins;
    }
}
