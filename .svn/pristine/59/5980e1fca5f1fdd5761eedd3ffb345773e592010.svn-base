/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appli.model.domaine.util_srv.printCom.raz;

import java.awt.Font;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.PARAM_APPLI_ENUM;
import appli.controller.util_ctrl.MouvementDetailPrintBean;
import framework.controller.ContextGloabalAppli;
import framework.model.common.util.BigDecimalUtil;
import framework.model.util.printGen.PrintCommunUtil;
import framework.model.util.printGen.PrintPosBean;
import framework.model.util.printGen.PrintPosDetailBean;

public class PrintRazJournee {
    private final int X_MTT_START = 187;
    private final int X_QTE_START = 115;

    private PrintPosBean printBean;
    private List<MouvementDetailPrintBean> listDetail;
    private BigDecimal mttOffertGlobal;
    private String date;
    //
    public PrintRazJournee(List<MouvementDetailPrintBean> listDetail, BigDecimal mttOffertGlobal, String date) {
        this.listDetail = listDetail;
        this.mttOffertGlobal = mttOffertGlobal;
        this.date = date;
        //
        buildPrintBean();
    }
    
    private BigDecimal[] montantsRecap, montantsMode;
    public PrintRazJournee(BigDecimal[] montantsRecap, BigDecimal[] montantsMode, String date) {
        this.montantsRecap = montantsRecap;
        this.montantsMode = montantsMode;
        this.date = date;
        //
        buildPrintBean();
    }
    
    private String[][] data;
    public PrintRazJournee(String[][] data, String date) {
        this.data = data;
        this.date = date;
        //
        buildPrintBean();
    }
    
    public PrintPosBean getPrintPosBean(){
    	return this.printBean;
    }
    
    private void buildPrintBean() {
    	this.printBean= new PrintPosBean();
    	 double middleHeight = 5;//Ajout qqs lignes
         //
 		if(montantsRecap != null){
         	middleHeight  = middleHeight + (montantsMode.length + montantsRecap.length);
         } else if(data != null) {
         	middleHeight = middleHeight + data.length;
         }
         if(this.listDetail != null){
         	middleHeight = middleHeight + listDetail.size();
         }

         middleHeight = (middleHeight * 0.5);
         
        this.printBean.setTicketHeight(BigDecimalUtil.get(""+middleHeight));
         List<PrintPosDetailBean> listDataToPrint = buildMapData();
     	this.printBean.setListDetail(listDataToPrint);
     	this.printBean.setMaxLineLength(50);
     	
     	this.printBean.setPrinters(ContextGloabalAppli.getGlobalConfig("PRINT_RAZ"));
    }
    
    public List<PrintPosDetailBean> buildMapData() {
    	List<PrintPosDetailBean> listPrintLins = new ArrayList<>();
    	int y = 10; // Décalage par rapport au logo

      try {
          BigDecimal tauxTva = BigDecimalUtil.get(ContextGloabalAppli.getGlobalConfig(PARAM_APPLI_ENUM.TVA_VENTE.toString()));
          // Société
          Font font = PrintCommunUtil.CUSTOM_FONT_12_B;
          listPrintLins.add(new PrintPosDetailBean(ContextAppli.getEtablissementBean().getRaison_sociale(), 0, y, font, "C"));

          y = y + 20;
          
          if(montantsRecap != null){// Récapitulatif ------------------------------------------------------------------
              BigDecimal 
              		  mttTotalNetALL = montantsMode[0], 
            		  mttTotalBrutALL = montantsMode[1], 
            		  mttEspeceALL = montantsMode[2], 
                      mttCarteALL = montantsMode[3], 
                      mttChequeALL = montantsMode[4], 
                      mttDejALL = montantsMode[5],
              		  mttPointALL = montantsMode[6],
              		  mttReserveALL = montantsMode[7];
              BigDecimal 
              		  nbrNoteAll = montantsRecap[0], 
            		  mttMoyen = montantsRecap[1], 
            		  mttTotalReducClientALL = montantsRecap[2], 
            		  mttTotalReducEmployeALL = montantsRecap[3], 
                      mttTotalOffertClientALL = montantsRecap[4], 
                      mttTotalOffertEmployeALL = montantsRecap[5], 
                      mttTotalAnnuleALL = montantsRecap[6],
                      mttTotalReducAutreALL = montantsRecap[7],
                      mttTotalOffertAutreALL = montantsRecap[8],
                      mttTotalAnnuleArtALL = montantsRecap[9];
              
               // Titre
              font = PrintCommunUtil.CUSTOM_FONT_10_B;
              listPrintLins.add(new PrintPosDetailBean("Rapport du "+date, 0, y, font, "C"));
              y = y + 10;
              
              // Date et heure impression
              String currDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
              listPrintLins.add(new PrintPosDetailBean("Imprimé le "+currDate, 0, y, PrintCommunUtil.CUSTOM_FONT_9, "C"));
              y = y + 20;
              
              // Recap
              if(!BigDecimalUtil.isZero(mttEspeceALL)){
              listPrintLins.add(new PrintPosDetailBean("ESPECES :", 0, y, PrintCommunUtil.CUSTOM_FONT_9));
              listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(mttEspeceALL), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9, "R"));
              y = y + 10;
              }
              if(!BigDecimalUtil.isZero(mttCarteALL)){
              listPrintLins.add(new PrintPosDetailBean("CARTE :", 0, y, PrintCommunUtil.CUSTOM_FONT_9));
              listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(mttCarteALL), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9, "R"));
              y = y + 10;
              }
              if(!BigDecimalUtil.isZero(mttChequeALL)){
              listPrintLins.add(new PrintPosDetailBean("CHÈQUE :", 0, y, PrintCommunUtil.CUSTOM_FONT_9));
              listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(mttChequeALL), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9, "R"));
              y = y + 10;
              }
              if(!BigDecimalUtil.isZero(mttDejALL)){
              listPrintLins.add(new PrintPosDetailBean("CHÈQUE TABLE :", 0, y, PrintCommunUtil.CUSTOM_FONT_9));
              listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(mttDejALL), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9, "R"));
              y = y + 20;
              }
              
              if(!BigDecimalUtil.isZero(mttPointALL)){
                  listPrintLins.add(new PrintPosDetailBean("POINTS :", 0, y, PrintCommunUtil.CUSTOM_FONT_9));
                  listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(mttPointALL), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9, "R"));
                  y = y + 20;
              }
              if(!BigDecimalUtil.isZero(mttReserveALL)){
                  listPrintLins.add(new PrintPosDetailBean("RESERVE :", 0, y, PrintCommunUtil.CUSTOM_FONT_9));
                  listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(mttReserveALL), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9, "R"));
                  y = y + 20;
              }
              
              // Détail
              if(!BigDecimalUtil.isZero(nbrNoteAll)){
              listPrintLins.add(new PrintPosDetailBean("Nombre de notes payantes :", 0, y, PrintCommunUtil.CUSTOM_FONT_9));
              listPrintLins.add(new PrintPosDetailBean(""+nbrNoteAll.intValue(), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9, "R"));
              y = y + 10;
              }
              if(!BigDecimalUtil.isZero(mttMoyen)){
              listPrintLins.add(new PrintPosDetailBean("Moyenne notes :", 0, y, PrintCommunUtil.CUSTOM_FONT_9));
              listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(mttMoyen), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9, "R"));
              y = y + 10;
              }
              if(!BigDecimalUtil.isZero(mttTotalReducClientALL)){
              listPrintLins.add(new PrintPosDetailBean("Réductions clients :", 0, y, PrintCommunUtil.CUSTOM_FONT_9));
              listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(mttTotalReducClientALL), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9, "R"));
              y = y + 10;
              }
              if(!BigDecimalUtil.isZero(mttTotalReducEmployeALL)){
              listPrintLins.add(new PrintPosDetailBean("Réductions personnels :", 0, y, PrintCommunUtil.CUSTOM_FONT_9));
              listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(mttTotalReducEmployeALL), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9, "R"));
              y = y + 10;
              }
              if(!BigDecimalUtil.isZero(mttTotalReducAutreALL)){
                  listPrintLins.add(new PrintPosDetailBean("Réductions :", 0, y, PrintCommunUtil.CUSTOM_FONT_9));
                  listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(mttTotalReducAutreALL), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9, "R"));
                  y = y + 10;
              }
              if(!BigDecimalUtil.isZero(mttTotalOffertClientALL)){
	              listPrintLins.add(new PrintPosDetailBean("Offerts clients :", 0, y, PrintCommunUtil.CUSTOM_FONT_9));
	              listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(mttTotalOffertClientALL), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9, "R"));
	              y = y + 10;
              }
              if(!BigDecimalUtil.isZero(mttTotalOffertEmployeALL)){
	              listPrintLins.add(new PrintPosDetailBean("Offerts personnels :", 0, y, PrintCommunUtil.CUSTOM_FONT_9));
	              listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(mttTotalOffertEmployeALL), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9, "R"));
	              y = y + 10;
              }
              if(!BigDecimalUtil.isZero(mttTotalOffertAutreALL)){
                  listPrintLins.add(new PrintPosDetailBean("Offerts :", 0, y, PrintCommunUtil.CUSTOM_FONT_9));
                  listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(mttTotalOffertAutreALL), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9, "R"));
                  y = y + 10;
              }
              if(!BigDecimalUtil.isZero(mttTotalAnnuleALL)){
            	  listPrintLins.add(new PrintPosDetailBean("Annulations commandes :", 0, y, PrintCommunUtil.CUSTOM_FONT_9));
            	  listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(mttTotalAnnuleALL), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9, "R"));
            	  y = y + 10;
              }
              if(!BigDecimalUtil.isZero(mttTotalAnnuleArtALL)){
                  listPrintLins.add(new PrintPosDetailBean("Annulations lignes :", 0, y, PrintCommunUtil.CUSTOM_FONT_9));
                  listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(mttTotalAnnuleArtALL), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9, "R"));
                  y = y + 20;
              }
              // Total
              BigDecimal mttTva = BigDecimalUtil.divide(BigDecimalUtil.multiply(mttTotalNetALL, tauxTva), BigDecimalUtil.get(100)); 
              
              listPrintLins.add(new PrintPosDetailBean("Total BRUT :", 0, y, PrintCommunUtil.CUSTOM_FONT_9));
              listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(mttTotalBrutALL), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9, "R"));
              y = y + 10;
              listPrintLins.add(new PrintPosDetailBean("Total NET :", 0, y, PrintCommunUtil.CUSTOM_FONT_9));
              listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(mttTotalNetALL), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9, "R"));
              y = y + 20;
              listPrintLins.add(new PrintPosDetailBean("Montant TVA ("+BigDecimalUtil.formatNumberZero(tauxTva)+") :", 0, y, PrintCommunUtil.CUSTOM_FONT_9));
              listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(mttTva), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9, "R"));
              y = y + 15;
              
              return listPrintLins;
          } else if(data != null){ // Bila impression -------------------------------------------------------------
          	listPrintLins.add(new PrintPosDetailBean("* Cette impression est strictement pour usage interne.", 0, y, PrintCommunUtil.CUSTOM_FONT_9));
          	y = y + 10;
              // 
              for(String[] val : data){
                  listPrintLins.add(new PrintPosDetailBean(val[0], 0, y, PrintCommunUtil.CUSTOM_FONT_9));
                  listPrintLins.add(new PrintPosDetailBean(val[1], X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9, "R"));
                  y = y + 10;
              }
             return listPrintLins;
          }
          
          // Détail -----------------------------------------------------------------------------------------------
          // Titre
          font = PrintCommunUtil.CUSTOM_FONT_10_B;
          listPrintLins.add(new PrintPosDetailBean("Rapport du "+date, 0, y, font, "C"));
          y = y + 10;

          if(listDetail == null || listDetail.size() == 0) {
        	  y = y + 20;
              listPrintLins.add(new PrintPosDetailBean("Aucun mouvement trouvé", 0, y, PrintCommunUtil.CUSTOM_FONT_9));
              
              return listPrintLins;
          }
          
          
          // Remettre la police
          //---------- On collecte les taux de TVA -----------------------
          BigDecimal mttTotal = BigDecimalUtil.get(0), qteTotal = BigDecimalUtil.get(0);
          BigDecimal mttTotalAnnul = BigDecimalUtil.get(0), qteTotalAnnul = BigDecimalUtil.get(0);
          BigDecimal mttTotalOffert = BigDecimalUtil.get(0), qteTotalOffert = BigDecimalUtil.get(0);
          BigDecimal[] mttQteArray = null;
          // Menu ---------------------------------------------------------
          y = y + 20;
          listPrintLins.add(new PrintPosDetailBean("** LES VENTES EN MENUS", 0, y, PrintCommunUtil.CUSTOM_FONT_12_B));
          y = y + 10;

          listPrintLins.add(new PrintPosDetailBean(0, y, 190, y));
          //
          Map<String, MouvementDetailPrintBean> listDet = getListDetail("MENU");
          if (listDet.size() > 0) {
              mttQteArray = addDetail(listPrintLins, listDet, "Menus", y);
              mttTotal = BigDecimalUtil.add(mttTotal, mttQteArray[0]);
              qteTotal = BigDecimalUtil.add(qteTotal, mttQteArray[1]);
              y = mttQteArray[2].intValue();
          }
          listDet = getListDetail("ANNUL_MENU");
          if (listDet.size() > 0) {
              mttQteArray = addDetail(listPrintLins, listDet, "Menus annulés", y);
              mttTotalAnnul = BigDecimalUtil.add(mttTotalAnnul, mttQteArray[0]);
              qteTotalAnnul = BigDecimalUtil.add(qteTotalAnnul, mttQteArray[1]);
              y = mttQteArray[2].intValue();
          }
          listDet = getListDetail("OFFR_MENU");
          if (listDet.size() > 0) {
              mttQteArray = addDetail(listPrintLins, listDet, "Menus offerts", y);
              mttTotalOffert = BigDecimalUtil.add(mttTotalOffert, mttQteArray[0]);
              qteTotalOffert = BigDecimalUtil.add(qteTotalOffert, mttQteArray[1]);
              y = mttQteArray[2].intValue();
          }
          // Article
          listDet = getListDetail("ART_MENU");
          if (listDet.size() > 0) {
              mttQteArray = addDetail(listPrintLins, listDet, "Articles vendus", y);
              mttTotal = BigDecimalUtil.add(mttTotal, mttQteArray[0]);
              qteTotal = BigDecimalUtil.add(qteTotal, mttQteArray[1]);
              y = mttQteArray[2].intValue();
          }
          listDet = getListDetail("ANNUL_ART_MENU");
          if (listDet.size() > 0) {
              mttQteArray = addDetail(listPrintLins, listDet, "Articles annulés", y);
              mttTotalAnnul = BigDecimalUtil.add(mttTotalAnnul, mttQteArray[0]);
              qteTotalAnnul = BigDecimalUtil.add(qteTotalAnnul, mttQteArray[1]);
              y = mttQteArray[2].intValue();
          }
          listDet = getListDetail("OFFR_ART_MENU");
          if (listDet.size() > 0) {
              mttQteArray = addDetail(listPrintLins, listDet, "Articles offerts", y);
              mttTotalOffert = BigDecimalUtil.add(mttTotalOffert, mttQteArray[0]);
              qteTotalOffert = BigDecimalUtil.add(qteTotalOffert, mttQteArray[1]);
              y = mttQteArray[2].intValue();
          }
          // Hors menu ---------------------------------------------------
          y = y + 20;
          listPrintLins.add(new PrintPosDetailBean("** LES VENTES HORS MENUS", 0, y, PrintCommunUtil.CUSTOM_FONT_12_B));
          y = y + 10;
          listPrintLins.add(new PrintPosDetailBean(0, y, 190, y));// Ligne séparateur
          y = y + 10;
          //
          listDet = getListDetail("GROUPE_FAMILLE");
          if (listDet.size() > 0) {
              mttQteArray = addDetail(listPrintLins, listDet, "Familles", y);
              mttTotal = BigDecimalUtil.add(mttTotal, mttQteArray[0]);
              qteTotal = BigDecimalUtil.add(qteTotal, mttQteArray[1]);
              y = mttQteArray[2].intValue();
          }
          listDet = getListDetail("ANNUL_GROUPE_FAMILLE");
          if (listDet.size() > 0) {
              mttQteArray = addDetail(listPrintLins, listDet, "Familles annulées", y);
              mttTotalAnnul = BigDecimalUtil.add(mttTotalAnnul, mttQteArray[0]);
              qteTotalAnnul = BigDecimalUtil.add(qteTotalAnnul, mttQteArray[1]);
              y = mttQteArray[2].intValue();
          }
          listDet = getListDetail("OFFR_GROUPE_FAMILLE");
          if (listDet.size() > 0) {
              mttQteArray = addDetail(listPrintLins, listDet, "Familles offertes", y);
              mttTotalOffert = BigDecimalUtil.add(mttTotalOffert, mttQteArray[0]);
              qteTotalOffert = BigDecimalUtil.add(qteTotalOffert, mttQteArray[1]);
              y = mttQteArray[2].intValue();
          }
          // Article
          listDet = getListDetail("ART");
          if (listDet.size() > 0) {
              mttQteArray = addDetail(listPrintLins, listDet, "Articles vendus", y);
              mttTotal = BigDecimalUtil.add(mttTotal, mttQteArray[0]);
              qteTotal = BigDecimalUtil.add(qteTotal, mttQteArray[1]);
              y = mttQteArray[2].intValue();
          }
          listDet = getListDetail("ANNUL_ART");
          if (listDet.size() > 0) {
              mttQteArray = addDetail(listPrintLins, listDet, "Articles annulés", y);
              mttTotalAnnul = BigDecimalUtil.add(mttTotalAnnul, mttQteArray[0]);
              qteTotalAnnul = BigDecimalUtil.add(qteTotalAnnul, mttQteArray[1]);
              y = mttQteArray[2].intValue();
          }
          listDet = getListDetail("OFFR_ART");
          if (listDet.size() > 0) {
              mttQteArray = addDetail(listPrintLins, listDet, "Articles offerts", y);
              mttTotalOffert = BigDecimalUtil.add(mttTotalOffert, mttQteArray[0]);
              qteTotalOffert = BigDecimalUtil.add(qteTotalOffert, mttQteArray[1]);
              y = mttQteArray[2].intValue();
          }

          // Ligne séparateur
          listPrintLins.add(new PrintPosDetailBean(0, y, 190, y));
          y = y + 10;

          // Recapitulatif ******************************************************************
          if (!BigDecimalUtil.isZero(mttTotal)) {
              y = y + 10;
              listPrintLins.add(new PrintPosDetailBean("Total commandes", 0, y, PrintCommunUtil.CUSTOM_FONT_9_B));
              listPrintLins.add(new PrintPosDetailBean(""+qteTotal.intValue(), X_QTE_START, y, PrintCommunUtil.CUSTOM_FONT_9_B));
              listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(mttTotal), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9_B, "R"));
          }

          if (!BigDecimalUtil.isZero(mttTotalOffert)) {
              y = y + 10;
              listPrintLins.add(new PrintPosDetailBean("Total articles offers", 0, y, PrintCommunUtil.CUSTOM_FONT_9_B));
              listPrintLins.add(new PrintPosDetailBean(""+qteTotalOffert.intValue(), X_QTE_START, y, PrintCommunUtil.CUSTOM_FONT_9_B));
              listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(mttTotalOffert), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9_B, "R"));
          }

          if (!BigDecimalUtil.isZero(mttOffertGlobal)) {
              y = y + 10;
              listPrintLins.add(new PrintPosDetailBean("Total offers commandes", 0, y, PrintCommunUtil.CUSTOM_FONT_9_B));
              listPrintLins.add(new PrintPosDetailBean("", X_QTE_START, y, PrintCommunUtil.CUSTOM_FONT_9_B));
              listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(mttOffertGlobal), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9_B, "R"));
          }

          if (!BigDecimalUtil.isZero(mttTotalAnnul)) {
              y = y + 10;
              listPrintLins.add(new PrintPosDetailBean("Total annulé", 0, y, PrintCommunUtil.CUSTOM_FONT_9_B));
              listPrintLins.add(new PrintPosDetailBean(""+qteTotalAnnul.intValue(), X_QTE_START, y, PrintCommunUtil.CUSTOM_FONT_9_B));
              listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero(mttTotalAnnul), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9_B, "R"));
          }
          // Ligne TVA
          BigDecimal mttHt = BigDecimalUtil.getMttHt(mttTotal, tauxTva);
          BigDecimal mttTva = BigDecimalUtil.substract(mttTotal, mttHt); 

          //  for(String taux : mapTvaMontant.keySet()){
          y = y + 10;
          listPrintLins.add(new PrintPosDetailBean("TVA (" + tauxTva + "%) : " + BigDecimalUtil.formatNumberZero(mttTva), 0, y, PrintCommunUtil.CUSTOM_FONT_9_B, "R"));
          // end of the reciept
      } catch (Exception r) {
          r.printStackTrace();
      }
	
      return listPrintLins;
    }
    
    private Map<String, MouvementDetailPrintBean> getListDetail(String type) throws Exception {
        Map<String, MouvementDetailPrintBean> mapDet = new LinkedHashMap<>();
        for (MouvementDetailPrintBean det : listDetail) {
            if (det.getType().equals(type)) {
                final String key = det.getCode().trim().toUpperCase();
                MouvementDetailPrintBean mvm = mapDet.get(key);
                if (mvm == null) {
                    mvm = (MouvementDetailPrintBean) BeanUtils.cloneBean(det);
                    mapDet.put(key, mvm);
                } else {
                    mvm.setMtt_total(BigDecimalUtil.add(mvm.getMtt_total(), det.getMtt_total()));
                    mvm.setQuantite(BigDecimalUtil.add(mvm.getQuantite(), det.getQuantite()));
                }
            }
        }
        return mapDet;
    }
    
    private BigDecimal[] addDetail(List<PrintPosDetailBean> listPrintLins, Map<String, MouvementDetailPrintBean> mapDetail, String titre, int y) {
        // Titre -----------------------------------------
        y = y + 10;
        listPrintLins.add(new PrintPosDetailBean(titre, 0, y, PrintCommunUtil.CUSTOM_FONT_9_B));
        y = y + 10;
        //------------------------------------------------

        // Entete tableau
        y = ajouterEnteteTableau(listPrintLins, y);

        BigDecimal mttTotal = null;
        BigDecimal qteTotal = BigDecimalUtil.get(0);
        //
        for (String key : mapDetail.keySet()) {
        	MouvementDetailPrintBean detail = mapDetail.get(key);
        	BigDecimal quantite = detail.getQuantite();
            BigDecimal prix = detail.getMtt_total();
            
        	if(BigDecimalUtil.isZero(prix)){
	    		continue;
	    	}
            
            mttTotal = BigDecimalUtil.add(mttTotal, detail.getMtt_total());
            qteTotal = BigDecimalUtil.add(qteTotal, detail.getQuantite());

            String prixStr = BigDecimalUtil.formatNumberZero(prix);
            String qte = quantite != null ? "" + quantite.intValue() : "";
            
            listPrintLins.add(new PrintPosDetailBean(detail.getLibelle().toUpperCase(), 0, y, PrintCommunUtil.CUSTOM_FONT_9));
            listPrintLins.add(new PrintPosDetailBean(qte, X_QTE_START, y, PrintCommunUtil.CUSTOM_FONT_9));
            listPrintLins.add(new PrintPosDetailBean(prixStr, X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9, "R"));

            y = y + 10; // shifting drawing line
        }
        listPrintLins.add(new PrintPosDetailBean(0, y, 190, y));// Ligne séparateur
        y = y + 10;

        // Récap
        String totalSt = BigDecimalUtil.formatNumberZero(mttTotal);
        listPrintLins.add(new PrintPosDetailBean("Total", 0, y, PrintCommunUtil.CUSTOM_FONT_9));
        listPrintLins.add(new PrintPosDetailBean(""+qteTotal.intValue(), X_QTE_START, y, PrintCommunUtil.CUSTOM_FONT_9));
        listPrintLins.add(new PrintPosDetailBean(totalSt, X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9, "R"));

        y = y + 10;

        BigDecimal[] data = {mttTotal, qteTotal, BigDecimalUtil.get(y)};

        return data;
    }
    
    private int ajouterEnteteTableau(List<PrintPosDetailBean> listPrintLins, int y) {
    	listPrintLins.add(new PrintPosDetailBean(0, y, 190, y));// Ligne séparateur
    	
        y = y + 10;
        String[] colNames = {"Article", "Qte", "Montant"};
        int[] colonnePosition = {5, 115, 150};
        for (int i = 0; i < colNames.length; i++) {
        	listPrintLins.add(new PrintPosDetailBean(colNames[i], colonnePosition[i], y, PrintCommunUtil.CUSTOM_FONT_9));
        }
        y = y + 10;
        listPrintLins.add(new PrintPosDetailBean(0, y, 190, y));// Ligne séparateur
        y = y + 10;
        
        return y;
    }
}