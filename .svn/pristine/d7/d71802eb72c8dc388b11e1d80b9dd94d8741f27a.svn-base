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
import appli.model.domaine.stock.service.impl.RepartitionBean;
import framework.controller.ContextGloabalAppli;
import framework.model.common.util.BigDecimalUtil;
import framework.model.util.printGen.PrintCommunUtil;
import framework.model.util.printGen.PrintPosBean;
import framework.model.util.printGen.PrintPosDetailBean;

public class PrintRazArticle {
    private PrintPosBean printBean;

    private String titre;
    private String date;
    private Map<Long, RepartitionBean> menuMap;
    private Map<Long, RepartitionBean> menuArtsMap;
    private Map<Long, RepartitionBean> artsMap;
    private BigDecimal offreMtt;
    private BigDecimal mttTotalNet;
    private BigDecimal mttTotal;
    private BigDecimal livraisonMtt;
    private final int X_MTT_START = 187;
    private final int X_QTE_START = 135;
    //
    public PrintRazArticle(String titre, String date,
    		Map data, boolean isQteOnly) {
    	this.titre = titre;
        
    	
    	Map<Long, RepartitionBean> menuMap = (Map<Long, RepartitionBean>) data.get("MENU");
		Map<Long, RepartitionBean> menuArtsMap = (Map<Long, RepartitionBean>) data.get("MENU_ARTS");
		Map<Long, RepartitionBean> artsMap = (Map<Long, RepartitionBean>) data.get("ARTS");
		BigDecimal offreMtt = (BigDecimal) data.get("OFFRE");
		BigDecimal livraisonMtt = (BigDecimal) data.get("LIVRAISON");
		BigDecimal mttTotalNet = (BigDecimal) data.get("VENTE_NET");
		BigDecimal mttTotal = (BigDecimal) data.get("VENTE");
		
		
    	this.menuMap = menuMap;
        this.menuArtsMap = menuArtsMap;
        this.artsMap = artsMap;
        
        this.offreMtt = offreMtt;
        this.livraisonMtt = livraisonMtt;
        
        this.mttTotalNet = mttTotalNet;
        this.mttTotal = mttTotal;
        
        this.date = date;
        
        this.printBean= new PrintPosBean();
        this.printBean.setTicketHeight(BigDecimalUtil.get((this.menuMap!=null?this.menuMap.size():0)+this.artsMap.size()+(menuArtsMap!=null?menuArtsMap.size():0)+15));
        this.printBean.setMaxLineLength(50);
        
        this.printBean.setPrinters(ContextGloabalAppli.getGlobalConfig("PRINT_RAZ"));
   	 
        List<PrintPosDetailBean> listDataToPrint = buildMapData(isQteOnly);
    	this.printBean.setListDetail(listDataToPrint);
    }
    
	public PrintPosBean getPrintPosBean(){
    	return this.printBean;
    }
    
    public List<PrintPosDetailBean> buildMapData(boolean isQteOnly) {
    	List<PrintPosDetailBean> listPrintLins = new ArrayList<>();
    	
        int y = 10; // Décalage par rapport au logo

        // Société
        listPrintLins.add(new PrintPosDetailBean(ContextAppli.getEtablissementBean().getRaison_sociale(), 0, y, PrintCommunUtil.CUSTOM_FONT_12_B, "C"));
        y = y + 20;
        
        // Détail -----------------------------------------------------------------------------------------------
        listPrintLins.add(new PrintPosDetailBean(this.titre, 0, y, PrintCommunUtil.CUSTOM_FONT_12_B, "C"));
        y = y + 10;
        
        // Titre
        listPrintLins.add(new PrintPosDetailBean(this.date, 0, y, PrintCommunUtil.CUSTOM_FONT_10_B, "C"));
        
//        BigDecimal mttTotal = BigDecimalUtil.get(0);
        
        // Menu
        if(!isQteOnly){
	        if(menuMap != null && menuMap.size() > 0){
	        	y = y + 30;
	       	 	listPrintLins.add(new PrintPosDetailBean("VENTE MENUS", 0, y, PrintCommunUtil.CUSTOM_FONT_10_B, "C"));
	       	 	
		         for(Long key : menuMap.keySet()){
					 RepartitionBean repB = menuMap.get(key);
					 y = y + 10;
					 
					 listPrintLins.add(new PrintPosDetailBean(repB.getLibelle(), 0, y, PrintCommunUtil.CUSTOM_FONT_9));
					 listPrintLins.add(new PrintPosDetailBean(repB.getQuantite().intValue(), X_QTE_START, y, PrintCommunUtil.CUSTOM_FONT_9, "R"));
					 listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumber(repB.getMontant()), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9, "R"));
		          	 
//		          	mttTotal = BigDecimalUtil.add(mttTotal, repB.getMontant());
		         }
	        }
	        // Article
	        if(artsMap.size() > 0){
	        	 y = y + 30;
	        	 listPrintLins.add(new PrintPosDetailBean("VENTE ARTICLES", 0, y, PrintCommunUtil.CUSTOM_FONT_10_B, "C"));
		         String oldFam = null;
		         BigDecimal subTotal = null;
		         for(Long key : artsMap.keySet()){
					 RepartitionBean repB = artsMap.get(key);
					 if(oldFam == null || !oldFam.equals(repB.getFamille())){
						 // Sous total -------------------------------------------
						 if(oldFam != null) {
							 listPrintLins.add(new PrintPosDetailBean(0, y, 190, y));// Ligne séparateur
							 y = y + 10;
							 listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumber(subTotal), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9_B, "R"));
							 y = y + 15;
							 subTotal = null;
						 }
						 // -------------------------------------------------------
						 
						 listPrintLins.add(new PrintPosDetailBean(repB.getFamille(), 0, y, PrintCommunUtil.CUSTOM_FONT_9_B));
						 y = y + 10;
					 }
					 
					 subTotal = BigDecimalUtil.add(subTotal, repB.getMontant());
					 
					 listPrintLins.add(new PrintPosDetailBean(repB.getLibelle(), 0, y, PrintCommunUtil.CUSTOM_FONT_8));
					 listPrintLins.add(new PrintPosDetailBean(repB.getQuantite().intValue(), X_QTE_START, y, PrintCommunUtil.CUSTOM_FONT_8, "R"));
					 listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumber(repB.getMontant()), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9, "R"));

					 y = y + 10;
					 
		          	oldFam = repB.getFamille();
//		          	mttTotal = BigDecimalUtil.add(mttTotal, repB.getMontant());
		         }
		         
		      // Sous total -------------------------------------------
				 if(oldFam != null) {
					 listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumber(subTotal), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9_B, "R"));
					 y = y + 10;
					 subTotal = null;
				 }
				 // -------------------------------------------------------
	        }
        
			if (!BigDecimalUtil.isZero(mttTotal)) {
	        	y = y + 20;
	        	listPrintLins.add(new PrintPosDetailBean("TOTAL", 0, y, PrintCommunUtil.CUSTOM_FONT_10_B));
				 listPrintLins.add(new PrintPosDetailBean("", X_QTE_START, y, PrintCommunUtil.CUSTOM_FONT_9));
				 listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumber(mttTotal), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_10_B, "R"));
	        }
	        
	        if(!BigDecimalUtil.isZero(offreMtt)){
		        y = y + 10;
		         listPrintLins.add(new PrintPosDetailBean("OFFRES", 0, y, PrintCommunUtil.CUSTOM_FONT_9));
				 listPrintLins.add(new PrintPosDetailBean("", X_QTE_START, y, PrintCommunUtil.CUSTOM_FONT_9));
				 listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumber(offreMtt), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9, "R"));
	        }
	        
	        if(!BigDecimalUtil.isZero(livraisonMtt)){
				 y = y + 10;
		     	 listPrintLins.add(new PrintPosDetailBean("LIVRAISONS", 0, y, PrintCommunUtil.CUSTOM_FONT_9));
				 listPrintLins.add(new PrintPosDetailBean("", X_QTE_START, y, PrintCommunUtil.CUSTOM_FONT_9));
				 listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumber(livraisonMtt), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9, "R"));
	        }
	        
	        if (!BigDecimalUtil.isZero(mttTotalNet)) {
	        	y = y + 20;
	        	listPrintLins.add(new PrintPosDetailBean("TOTAL NET", 0, y, PrintCommunUtil.CUSTOM_FONT_10_B));
				 listPrintLins.add(new PrintPosDetailBean("", X_QTE_START, y, PrintCommunUtil.CUSTOM_FONT_9));
				 listPrintLins.add(new PrintPosDetailBean(BigDecimalUtil.formatNumber(mttTotalNet), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_10_B, "R"));
	        }
        } else{
	        // Ajout articles menus manquants
	 		for(Long artId : menuArtsMap.keySet()){
	 			if(artsMap.get(artId) == null){
	 				artsMap.put(artId, null);
	 			}
	 		}
	        
	        // Article
	        if(artsMap.size() > 0){
	        	 y = y + 30;
	        	 listPrintLins.add(new PrintPosDetailBean("DETAIL ARTICLES", 0, y, PrintCommunUtil.CUSTOM_FONT_10_B, "C"));
		         String oldFam = null;
		         for(Long key : artsMap.keySet()){
					 RepartitionBean repB = artsMap.get(key);
					 if(repB == null){
						 continue;
					 }
					 y = y + 10;
					 if(oldFam == null || !oldFam.equals(repB.getFamille())){
						 listPrintLins.add(new PrintPosDetailBean(repB.getFamille(), 0, y, PrintCommunUtil.CUSTOM_FONT_9_B));
						 y = y + 10;
					 }
					 listPrintLins.add(new PrintPosDetailBean(repB.getLibelle(), 0, y, PrintCommunUtil.CUSTOM_FONT_9));
					 listPrintLins.add(new PrintPosDetailBean(repB.getQuantite().intValue(), X_MTT_START, y, PrintCommunUtil.CUSTOM_FONT_9, "R"));
		          	
		          	oldFam = repB.getFamille();
		         }
	        }
        }
        
        return listPrintLins;
    }
}
