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

public class PrintRazModePaiement {

    public static Font CUSTOM_FONT_9 = new Font("Monospaced", Font.PLAIN, 9); 
    public static Font CUSTOM_FONT_9_B = new Font("Monospaced", Font.BOLD, 9);
    public static Font CUSTOM_FONT_10_B = new Font("Monospaced", Font.BOLD, 10);
    public static Font CUSTOM_FONT_12_B = new Font("Arial", Font.BOLD, 12);
    public static final int PAPER_WIDTH = 180;
    private PrintPosBean printBean;

    private List<RazDetail> data;
    private String titre;
    private String date;
    private final int X_MTT_START = 167;
    private final int X_QTE_START = 125;
    
    //
    public PrintRazModePaiement(String titre, String date, List<RazDetail> data) {
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
        Font font = PrintRazModePaiement.CUSTOM_FONT_12_B;
        listPrintLins.add(new PrintPosDetailBean(ContextAppli.getEtablissementBean().getRaison_sociale(), 0, y, font, "C"));
        y = y + 20;
        
        // Détail -----------------------------------------------------------------------------------------------
        listPrintLins.add(new PrintPosDetailBean(titre, 0, y, PrintRazModePaiement.CUSTOM_FONT_12_B, "C"));
        y = y + 20;
        
        // Titre
        font = PrintRazModePaiement.CUSTOM_FONT_10_B;
        listPrintLins.add(new PrintPosDetailBean(this.date, 0, y, font, "C")); 
        y = y + 15;
       
        listPrintLins.add(new PrintPosDetailBean(0, y, 190, y));// Ligne séparateur
        //
        for (RazDetail recap : data) {
       	 	y = y + 10;
            listPrintLins.add(new PrintPosDetailBean(recap.getLibelle(), 0, y, PrintCommunUtil.CUSTOM_FONT_9));
            listPrintLins.add(new PrintPosDetailBean(recap.getQuantite(), X_QTE_START, y, PrintCommunUtil.CUSTOM_FONT_9));
            listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumber(recap.getMontant()), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9));
		}  
        
        return listPrintLins;
    }
}