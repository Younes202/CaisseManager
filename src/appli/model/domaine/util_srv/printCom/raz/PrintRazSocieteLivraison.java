/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appli.model.domaine.util_srv.printCom.raz;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.util_srv.raz.RazDetail;
import framework.controller.ContextGloabalAppli;
import framework.model.common.util.BigDecimalUtil;
import framework.model.util.printGen.PrintCommunUtil;
import framework.model.util.printGen.PrintPosBean;
import framework.model.util.printGen.PrintPosDetailBean;

public class PrintRazSocieteLivraison {
    private PrintPosBean printBean;

    private List<RazDetail> listData;
    private List<RazDetail> listPaiement;
    private String titre;
    private String date;
    private final int X_MTT_START = 187;
    private final int X_QTE_START = 135;
    
    //
    public PrintRazSocieteLivraison(String titre, String date, List<RazDetail> listData, List<RazDetail> listPaiement) {
    	this.titre = titre;
        this.listPaiement = listPaiement;
        this.listData = listData;
        this.date = date;
        
        this.printBean= new PrintPosBean();
        this.printBean.setTicketHeight(BigDecimalUtil.get(this.listData.size()+listPaiement.size()));
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
        listPrintLins.add(new PrintPosDetailBean(titre, 0, y, PrintCommunUtil.CUSTOM_FONT_12_B, "C"));
        y = y + 20;
        // Titre
        font = PrintCommunUtil.CUSTOM_FONT_10_B;
        listPrintLins.add(new PrintPosDetailBean(this.date, 0, y, font, "C"));
        y = y + 15;
        
        listPrintLins.add(new PrintPosDetailBean(0, y, 190, y));// Ligne séparateur
        
        // Recapitulatif ******************************************************************
        y = y + 20;//------------------------------------------------------
        
        listPrintLins.add(new PrintPosDetailBean(0, y, 190, y));// Ligne séparateur
        y = y + 10;
        String[] colNames = {"Société", "Montant", "Marge"};
        int[] colonnePosition = {5, 110, 160};
        for (int i = 0; i < colNames.length; i++) {
            listPrintLins.add(new PrintPosDetailBean(colNames[i], colonnePosition[i], y, PrintCommunUtil.CUSTOM_FONT_9));
        }
        y = y + 10;
        listPrintLins.add(new PrintPosDetailBean(0, y, 190, y));// Ligne séparateur
        y = y + 10;
        for(RazDetail recap : listData){
        	listPrintLins.add(new PrintPosDetailBean(recap.getLibelle(), 0, y, PrintCommunUtil.CUSTOM_FONT_9));
        	listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumber(recap.getMontant()), X_QTE_START, y, PrintCommunUtil.CUSTOM_FONT_9));
        	listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumber(recap.getMontant2()), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9));
        }
        
        listPrintLins.add(new PrintPosDetailBean(0, y, 190, y));// Ligne séparateur
        y = y + 10;
        colNames = new String[]{"Mode paiement", "Montant", "Marge"};
        colonnePosition = new int[]{5, 110, 160};
        for (int i = 0; i < colNames.length; i++) {
            listPrintLins.add(new PrintPosDetailBean(colNames[i], colonnePosition[i], y, PrintCommunUtil.CUSTOM_FONT_9));
        }
        y = y + 10;
        listPrintLins.add(new PrintPosDetailBean(0, y, 190, y));// Ligne séparateur
        y = y + 10;
        for(RazDetail recap : listPaiement){
        	listPrintLins.add(new PrintPosDetailBean(recap.getLibelle(), 0, y, PrintCommunUtil.CUSTOM_FONT_9));
        	listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumber(recap.getMontant()), X_QTE_START, y, PrintCommunUtil.CUSTOM_FONT_9));
        	listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumber(recap.getMontant2()), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9));
        }
        
        return listPrintLins;
    }
}
