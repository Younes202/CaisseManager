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
import appli.controller.domaine.util_erp.ContextAppli.PARAM_APPLI_ENUM;
import appli.controller.util_ctrl.MouvementDetailPrintBean;
import framework.controller.ContextGloabalAppli;
import framework.model.common.util.BigDecimalUtil;
import framework.model.util.printGen.PrintCommunUtil;
import framework.model.util.printGen.PrintPosBean;
import framework.model.util.printGen.PrintPosDetailBean;

public class PrintRazBoisson {
    private PrintPosBean printBean;

    private Map<String, MouvementDetailPrintBean> mapCmdBoissons;
    private String titre;
    private String date;
    
    //
    public PrintRazBoisson(String titre, Map<String, MouvementDetailPrintBean> mapCmdBoissons, String date) {
    	this.titre = titre;
        this.mapCmdBoissons = mapCmdBoissons;
        this.date = date;
        
        this.printBean= new PrintPosBean();
        this.printBean.setTicketHeight(BigDecimalUtil.get(this.mapCmdBoissons.size()));
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
        BigDecimal mttTotal = BigDecimalUtil.get(0), qteTotal = BigDecimalUtil.get(0);
//            BigDecimal mttTotalAnnul = BigDecimalUtil.get(0), qteTotalAnnul = BigDecimalUtil.get(0);
//            BigDecimal mttTotalOffert = BigDecimalUtil.get(0), qteTotalOffert = BigDecimalUtil.get(0);
        BigDecimal[] mttQteArray = null;
        // Menu ---------------------------------------------------------
        if (mapCmdBoissons.size() > 0) {
            y = y + 20;
            listPrintLins.add(new PrintPosDetailBean(titre, 0, y, PrintCommunUtil.CUSTOM_FONT_12_B));
            y = y + 10;
            listPrintLins.add(new PrintPosDetailBean(0, y, 190, y));// Ligne séparateur
            //
        
            mttQteArray = addDetail(listPrintLins, mapCmdBoissons, "", y);
            mttTotal = BigDecimalUtil.add(mttTotal, mttQteArray[0]);
            qteTotal = BigDecimalUtil.add(qteTotal, mttQteArray[1]);
            y = mttQteArray[2].intValue();
        }
        
        // Ligne séparateur
        listPrintLins.add(new PrintPosDetailBean(0, y, 190, y));
        y = y + 10;

        // Recapitulatif ******************************************************************
        y = y + 20;//------------------------------------------------------

        BigDecimal tauxTva = BigDecimalUtil.get(ContextGloabalAppli.getGlobalConfig(PARAM_APPLI_ENUM.TVA_VENTE.toString()));
        BigDecimal mttHt = BigDecimalUtil.getMttHt(mttTotal, tauxTva);
        BigDecimal mttTva = BigDecimalUtil.substract(mttTotal, mttHt); 

        y = y + 10;
        listPrintLins.add(new PrintPosDetailBean("TVA (" + tauxTva + "%) : " + BigDecimalUtil.formatNumberZero(mttTva), 0, y, PrintCommunUtil.CUSTOM_FONT_9_B));
        // end of the reciept
        
        return listPrintLins;
    }
    
    private BigDecimal[] addDetail(List<PrintPosDetailBean> listPrintLins, Map<String, MouvementDetailPrintBean> mapDetail, String type, int y) {
        int X_MTT_START = 187;
        int X_QTE_START = 135;
       
    	// Entete tableau
        y = ajouterEnteteTableau(listPrintLins, y);

        BigDecimal mttTotal = null;
        BigDecimal qteTotal = BigDecimalUtil.get(0);
        //
        for (String key : mapDetail.keySet()) {
            MouvementDetailPrintBean detail = mapDetail.get(key);
            
            if(!key.startsWith(type)){
                continue;
            }
            
            mttTotal = BigDecimalUtil.add(mttTotal, detail.getMtt_total());
            qteTotal = BigDecimalUtil.add(qteTotal, detail.getQuantite());

            BigDecimal quantite = detail.getQuantite();
            BigDecimal prix = detail.getMtt_total();
            String prixStr = BigDecimalUtil.formatNumberZero(prix);
            String qte = quantite != null ? "" + quantite.intValue() : "";

            listPrintLins.add(new PrintPosDetailBean(detail.getLibelle().toUpperCase(), 0, y, PrintCommunUtil.CUSTOM_FONT_9));
            listPrintLins.add(new PrintPosDetailBean(qte, X_QTE_START, y, PrintCommunUtil.CUSTOM_FONT_9, "R"));
            listPrintLins.add(new PrintPosDetailBean(prixStr, X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9, "R"));

            y = y + 10; // shifting drawing line
        }
        listPrintLins.add(new PrintPosDetailBean(0, y, 190, y));// Ligne séparateur
        y = y + 10;
         
        // Récap
        String totalSt = BigDecimalUtil.formatNumberZero(mttTotal);
        listPrintLins.add(new PrintPosDetailBean("Total", 0, y, PrintCommunUtil.CUSTOM_FONT_9_B));
        listPrintLins.add(new PrintPosDetailBean(""+qteTotal.intValue(), X_QTE_START, y, PrintCommunUtil.CUSTOM_FONT_9_B, "R"));
        listPrintLins.add(new PrintPosDetailBean(totalSt, X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9_B, "R"));
        y = y + 10;

        BigDecimal[] data = {mttTotal, qteTotal, BigDecimalUtil.get(y)};

        return data;
    }
    
    private int ajouterEnteteTableau(List<PrintPosDetailBean> listPrintLins, int y) {
        listPrintLins.add(new PrintPosDetailBean(0, y, 190, y));// Ligne séparateur
        y = y + 10;
        String[] colNames = {"Article", "Qte", "Montant"};
        int[] colonnePosition = {5, 110, 160};
        for (int i = 0; i < colNames.length; i++) {
            listPrintLins.add(new PrintPosDetailBean(colNames[i], colonnePosition[i], y, PrintCommunUtil.CUSTOM_FONT_9));
        }
        y = y + 10;
        listPrintLins.add(new PrintPosDetailBean(0, y, 190, y));// Ligne séparateur
        y = y + 10;
        
        return y;
    }
}