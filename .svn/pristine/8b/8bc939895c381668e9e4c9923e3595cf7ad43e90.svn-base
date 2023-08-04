/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appli.model.domaine.util_srv.printCom.ticket;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.PARAM_APPLI_ENUM;
import appli.model.domaine.personnel.persistant.ClientPersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import appli.model.domaine.vente.persistant.CaissePersistant;
import framework.controller.ContextGloabalAppli;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.FileUtil;
import framework.model.util.printGen.PrintCommunUtil;
import framework.model.util.printGen.PrintPosBean;
import framework.model.util.printGen.PrintPosDetailBean;

public class PrintTicketSituationUtil {
    private PrintPosBean printBean;
    
    public PrintTicketSituationUtil(CaisseMouvementPersistant caisseMvm, Map mapData) {
    	this.printBean = new PrintPosBean();
    	List<PrintPosDetailBean> listDataToPrint = buildMapData(caisseMvm, mapData);
    	this.printBean.setListDetail(listDataToPrint);
    	
		EtablissementPersistant etablissementB = ContextAppli.getEtablissementBean();
		boolean isPrintLogo = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("LOGO_TICKET"));
		Long restauId = etablissementB.getId();
		String startChemin = etablissementB.getId()+"/"+"restau/"+restauId;
		Map<String, byte[]> mapFilesLogo = FileUtil.getListFilesByte(startChemin);  
		startChemin = etablissementB.getId()+"/"+"paramTICK/"+restauId;
		
		BigDecimal heightTicket = BigDecimalUtil.get(20);
        
		if(isPrintLogo && mapFilesLogo.size() > 0){
        	heightTicket = BigDecimalUtil.add(heightTicket, BigDecimalUtil.get(20));
        }
        // Ajouter les groupes
        if(caisseMvm.getMax_idx_client() != null && caisseMvm.getMax_idx_client() > 2){
        	heightTicket = BigDecimalUtil.add(heightTicket, BigDecimalUtil.get(caisseMvm.getMax_idx_client()*6));
        }
        this.printBean.setTicketHeight(heightTicket);
        
        CaissePersistant caisseBean = (CaissePersistant) MessageService.getGlobalMap().get("CURRENT_CAISSE");
    	if(caisseBean == null && caisseMvm.getOpc_caisse_journee() != null) {
    		caisseBean = caisseMvm.getOpc_caisse_journee().getOpc_caisse();
    	}
    	
    	if(caisseBean == null){
    		return;
    	}
    	
    	int nbr_ticket = caisseBean.getNbr_ticket()==null?1:caisseBean.getNbr_ticket();
		this.printBean.setNbrTicket(nbr_ticket);
    	this.printBean.setPrinters(caisseBean.getImprimantes());
    }
    
    public PrintPosBean getPrintPosBean(){
    	return this.printBean;
    }
    
    /**
     * @return
     */
    private List<PrintPosDetailBean> buildMapData(CaisseMouvementPersistant mouvement, 
    		Map mapData) {
    	 
    	EtablissementPersistant restaurantP = ContextAppli.getEtablissementBean();
    	boolean isPrintLogo = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("LOGO_TICKET"));
    	
    	Font smallTxt = new Font("Roman", Font.PLAIN, StringUtil.isEmpty(ContextGloabalAppli.getGlobalConfig("TICKET_FONT_SMALL")) ? 7 : Integer.valueOf(ContextGloabalAppli.getGlobalConfig("TICKET_FONT_SMALL")));
    	Font bigTxt = new Font("Roman", Font.PLAIN, StringUtil.isEmpty(ContextGloabalAppli.getGlobalConfig("TICKET_FONT_BIG")) ? 8 : Integer.valueOf(ContextGloabalAppli.getGlobalConfig("TICKET_FONT_SMALL")));
    	Font smallTxtB = new Font("Roman", Font.BOLD, StringUtil.isEmpty(ContextGloabalAppli.getGlobalConfig("TICKET_FONT_SMALL")) ? 7 : Integer.valueOf(ContextGloabalAppli.getGlobalConfig("TICKET_FONT_SMALL")));
    	Font bigTxtB = new Font("Roman", Font.BOLD, StringUtil.isEmpty(ContextGloabalAppli.getGlobalConfig("TICKET_FONT_BIG")) ? 9 : Integer.valueOf(ContextGloabalAppli.getGlobalConfig("TICKET_FONT_SMALL")));
    	
    	int ecartEntete = StringUtil.isEmpty(ContextGloabalAppli.getGlobalConfig("ECART_ENTETE_TICKET")) ? 10 : Integer.valueOf(ContextGloabalAppli.getGlobalConfig("ECART_ENTETE_TICKET"));
    	int carcRetourLigne = StringUtil.isEmpty(ContextGloabalAppli.getGlobalConfig("BACKLINE_TICKET")) ? 50 : Integer.valueOf(ContextGloabalAppli.getGlobalConfig("BACKLINE_TICKET"));
    	
    	this.printBean.setMaxLineLength(carcRetourLigne);
    	
		Long restauId = restaurantP.getId();
		String startCheminLogo = restaurantP.getId()+"/"+"restau/"+restauId;
		Map<String, byte[]> mapFilesLogo = FileUtil.getListFilesByte(startCheminLogo);  
		
    	Map<String, String> mapConfig = (Map<String, String>) MessageService.getGlobalMap().get("GLOBAL_CONFIG");
    	if(mapConfig == null){
    		mapConfig = new HashMap<String, String>();
    	}
    	
        List<CaisseMouvementArticlePersistant> listMvm = (mouvement.getListEncaisse()!=null && mouvement.getListEncaisse().size()>0) ? mouvement.getListEncaisse():mouvement.getList_article();
        Map<String, String> ENTETE_TEXT = new LinkedHashMap<>();
        if (StringUtil.isNotEmpty(mapConfig.get(PARAM_APPLI_ENUM.TEXT_ENTETE_TICKET_1.toString()))) {
        	ENTETE_TEXT.put("ENT1", mapConfig.get(PARAM_APPLI_ENUM.TEXT_ENTETE_TICKET_1.toString()));
        }
        if (StringUtil.isNotEmpty(mapConfig.get(PARAM_APPLI_ENUM.TEXT_ENTETE_TICKET_2.toString()))) {
        	ENTETE_TEXT.put("ENT2", mapConfig.get(PARAM_APPLI_ENUM.TEXT_ENTETE_TICKET_2.toString()));
        }

        if (StringUtil.isTrue(mapConfig.get(PARAM_APPLI_ENUM.ADRESSE_ETABLISSEMENT.toString()))  
                    && StringUtil.isNotEmpty(restaurantP.getAdresse())){
        	ENTETE_TEXT.put("ADR", restaurantP.getAdresse());
        }
        if (StringUtil.isTrue(mapConfig.get(PARAM_APPLI_ENUM.ICE.toString()))  
                && StringUtil.isNotEmpty(restaurantP.getNumero_ice())) {
        	ENTETE_TEXT.put("ICE", "ICE : "+restaurantP.getNumero_ice());
        }

         if (StringUtil.isTrue(mapConfig.get(PARAM_APPLI_ENUM.INFORMATION_CONTACT_PHONE.toString()))  
                && StringUtil.isNotEmpty(restaurantP.getTelephone())){
        	 ENTETE_TEXT.put("PHONE", "Téléphone : "+restaurantP.getTelephone());
        }

        if (StringUtil.isTrue(mapConfig.get(PARAM_APPLI_ENUM.INFORMATION_CONTACT_MAIL.toString()))  
                && StringUtil.isNotEmpty(restaurantP.getMail())){
        	ENTETE_TEXT.put("MAIL", "Mail : "+restaurantP.getMail());
        }

        List<PrintPosDetailBean> listPrintLinrs = new ArrayList<>();
        
        try {
            /* Draw Header */
            int y = ecartEntete; // Décalage par rapport au logo

            // Entête
            if(ENTETE_TEXT.size() > 0){
	            for (String entete : ENTETE_TEXT.keySet()) {
	            	String value = ENTETE_TEXT.get(entete);
	            	
	                if("ENT1".equals(entete)){
	                	listPrintLinrs.add(new PrintPosDetailBean(value, 0, y, PrintCommunUtil.CUSTOM_FONT_11_B, "C"));
	                } else if("ENT2".equals(entete)){
	                	listPrintLinrs.add(new PrintPosDetailBean(value, 0, y, PrintCommunUtil.CUSTOM_FONT_9_B, "C"));
	                } else if("ADR".equals(entete)){
	                	listPrintLinrs.add(new PrintPosDetailBean(value, 0, y, PrintCommunUtil.CUSTOM_FONT_8, "C"));
	                } else if("ICE".equals(entete)){
	                	listPrintLinrs.add(new PrintPosDetailBean(value, 0, y, PrintCommunUtil.CUSTOM_FONT_8, "C"));
	                } else if("PHONE".equals(entete)){
	                	listPrintLinrs.add(new PrintPosDetailBean(value, 0, y, PrintCommunUtil.CUSTOM_FONT_8_B, "C"));
	                } else if("MAIL".equals(entete)){
	                	listPrintLinrs.add(new PrintPosDetailBean(value, 0, y, PrintCommunUtil.CUSTOM_FONT_8, "C"));
	                }
	                y = y + 10;
	            }
    			y = y + 5;
    		}
            // Remettre la police
            // Logo image --------------------------------------------------------------
    		if(isPrintLogo && mapFilesLogo != null && mapFilesLogo.size() > 0){
				try {
					restauId = restaurantP.getId();
					File file = new File(StrimUtil.BASE_FILES_PATH+"/"+startCheminLogo+"/"+mapFilesLogo.keySet().iterator().next());
					BufferedImage read = ImageIO.read(file);
					int xImg = 10; // print start at 100 on x axies
					int imagewidth = read.getWidth();
					int imageheight = read.getHeight();
					// Max 200
					if(imagewidth > 200){
						Dimension imgSize = new Dimension(read.getWidth(), read.getHeight());
						Dimension boundary = new Dimension(200, 200);
						
						Dimension ratioSize = PrintCommunUtil.getScaledDimension(imgSize, boundary);
						
						imagewidth = ratioSize.width;
						imageheight = ratioSize.height;
					}
					xImg = ((PrintCommunUtil.PAPER_WIDTH - imagewidth) / 2)+15;
					//
					listPrintLinrs.add(new PrintPosDetailBean(file, xImg, y, imagewidth, imageheight, "C"));
					y = y + imageheight + 5;
				} catch (IOException e) {
					e.printStackTrace();
				}
				y = y + 10;
    		}
    		// ----------------------------------------------------------------------------
			ClientPersistant opc_client = mouvement.getOpc_client();
			if(opc_client != null){
				String strCli = "CLIENT : " + opc_client.getNom()+" "+StringUtil.getValueOrEmpty(opc_client.getPrenom());
				listPrintLinrs.add(new PrintPosDetailBean(strCli, 10, y, smallTxt));
				if (StringUtil.isNotEmpty(opc_client.getAdresse_rue())) {
					y = y + 10;
					listPrintLinrs.add(new PrintPosDetailBean(opc_client.getAdresse_rue(), 10, y, PrintCommunUtil.CUSTOM_FONT_9_B));
				}
				if (StringUtil.isNotEmpty(opc_client.getAdresse_compl())) {
					y = y + 10;
					listPrintLinrs.add(new PrintPosDetailBean(opc_client.getAdresse_compl(), 10, y, PrintCommunUtil.CUSTOM_FONT_9_B));
				}
				if (StringUtil.isNotEmpty(opc_client.getVilleStr())) {
					y = y + 10;
					listPrintLinrs.add(new PrintPosDetailBean(" - " + opc_client.getVilleStr(), 10, y, PrintCommunUtil.CUSTOM_FONT_9_B));
				}
				if(StringUtil.isNotEmpty(opc_client.getTelephone())){
					y = y + 10;
					listPrintLinrs.add(new PrintPosDetailBean("Tél 1 : "+opc_client.getTelephone(), 10, y, PrintCommunUtil.CUSTOM_FONT_9_B));
				}
				if(StringUtil.isNotEmpty(opc_client.getTelephone2())){
					y = y + 10;
					listPrintLinrs.add(new PrintPosDetailBean("Tél 2 : "+opc_client.getTelephone2(), 10, y, PrintCommunUtil.CUSTOM_FONT_9_B));
				}
			    y = y + 10;
			}

			listPrintLinrs.add(new PrintPosDetailBean(0, y, 220, y));
            y = y + 30;

            listPrintLinrs.add(new PrintPosDetailBean("NBR COMMANDES", 5, y, bigTxt));
            listPrintLinrs.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero((BigDecimal) mapData.get("nbrAllCmd")), 170, y, bigTxt, "R"));
            y = y + 15;
//            listPrintLinrs.add(new PrintPosDetailBean("TOTAL COMMANDES", 5, y, bigTxt));
//            listPrintLinrs.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero((BigDecimal) mapData.get("totalCmd")), 170, y, bigTxt, "R"));
//            y = y + 30;

            listPrintLinrs.add(new PrintPosDetailBean("TOTAL RECHARGES", 5, y, bigTxt));
            listPrintLinrs.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero((BigDecimal) mapData.get("totalRecharge")), 170, y, bigTxt, "R"));            
            y = y + 15;
            listPrintLinrs.add(new PrintPosDetailBean("TOTAL CMD PORTEFEUILLE", 5, y, bigTxt));
            listPrintLinrs.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero((BigDecimal) mapData.get("totalMvmPortefeuille")), 170, y, bigTxt, "R"));
            y = y + 30;
            
            listPrintLinrs.add(new PrintPosDetailBean(0, y, 190, y));// Ligne séparateur
            y = y + 10;
            
            listPrintLinrs.add(new PrintPosDetailBean("SOLDE PORTEFEUILLE", 5, y, bigTxtB));
            listPrintLinrs.add(new PrintPosDetailBean(BigDecimalUtil.formatNumberZero((BigDecimal) mapData.get("solde")), 170, y, bigTxtB, "R"));
            y = y + 10;
        } catch (Exception r) {
            r.printStackTrace();
        }

        return listPrintLinrs;
    }

}
