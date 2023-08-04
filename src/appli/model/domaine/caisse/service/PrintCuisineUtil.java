/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appli.model.domaine.caisse.service;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.font.TextAttribute;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterAbortException;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.AttributedString;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;

import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_LIGNE_COMMANDE;
import appli.model.domaine.personnel.persistant.EmployePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import appli.model.domaine.vente.persistant.CaissePersistant;
import framework.controller.ContextGloabalAppli;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.printGen.PrintCommunUtil;

public class PrintCuisineUtil {
    public static Font CUSTOM_FONT_7 = new Font("Roman", Font.PLAIN, 7);//Monospaced
    public static Font CUSTOM_FONT_7_B = new Font("Roman", Font.BOLD, 7);//Monospaced
    public static Font CUSTOM_FONT_8_B = new Font("Roman", Font.BOLD, 8);
    public static Font CUSTOM_FONT_8 = new Font("Roman", Font.PLAIN, 8);
    public static Font CUSTOM_FONT_9_B = new Font("Roman", Font.BOLD, 9);
    public static Font CUSTOM_FONT_9 = new Font("Roman", Font.PLAIN, 9);
    public static Font CUSTOM_FONT_11_B = new Font("Arial", Font.BOLD, 11);
    public static Font CUSTOM_FONT_12_B = new Font("Arial", Font.BOLD, 12);
    public static Font CUSTOM_FONT_13_B = new Font("Arial", Font.BOLD, 13);
    public static Font CUSTOM_FONT_14_B = new Font("Arial", Font.BOLD, 14);
    public static Font CUSTOM_FONT_15_B = new Font("Arial", Font.BOLD, 15);
    public static Font CUSTOM_FONT_16_B = new Font("Arial", Font.BOLD, 16);
    public static Font CUSTOM_FONT_17_B = new Font("Arial", Font.BOLD, 17);
    public static Font CUSTOM_FONT_18_B = new Font("Arial", Font.BOLD, 18);
    public static Font CUSTOM_FONT_19_B = new Font("Arial", Font.BOLD, 19);
    public static Font CUSTOM_FONT_20_B = new Font("Arial", Font.BOLD, 20);
    public static final int PAPER_WIDTH = 180;

//    private static AttributedCharacterIterator getAttributeFont(String txt, Font plainFont, Color color, boolean barre){
//    	AttributedString as = new AttributedString(txt);
//	    as.addAttribute(TextAttribute.FONT, plainFont);
//	    as.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, 1, 11);
//	    if(barre){
//	    	as.addAttribute(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
//	    }
//	    as.addAttribute(TextAttribute.FOREGROUND, color);
//	    
//	    return as.getIterator();
//    }
    
    private CaisseMouvementPersistant caisseMvm;
    private CaissePersistant caisseBean;
    private List<String> listDetail = new ArrayList<>();
    private boolean isHideDetail;
    private boolean isOnlyNewTicketCuis;
    private int smallTxtSize = 7, smallTxtSize8 = 8, bigTxtSize = 9;
    
    public PrintCuisineUtil(CaissePersistant caisseBean, CaisseMouvementPersistant caisseMvm) {
    	
    	boolean isHideDetTicket =  StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig(caisseBean.getId(), "TICKET_CUIS_NODET"));
    	boolean isOnlyNewTicketCuis =  StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig(caisseBean.getId(), "TICKET_CUIS_NEW_ONLY"));
    	String smallConfig = ContextGloabalAppli.getGlobalConfig("TICKET_CUIS_FONT_SMALL");
  		String bigConfig = ContextGloabalAppli.getGlobalConfig("TICKET_CUIS_FONT_BIG");
  		
  		int smallTxt = StringUtil.isEmpty(smallConfig) ? 7 : Integer.valueOf(smallConfig);
  		int bigTxt = StringUtil.isEmpty(bigConfig) ? 9 : Integer.valueOf(bigConfig);
  		
    	this.caisseMvm = caisseMvm;
    	this.caisseBean = caisseBean;
    	this.isHideDetail = isHideDetTicket;
    	this.isOnlyNewTicketCuis = isOnlyNewTicketCuis;
    	this.smallTxtSize = smallTxt;
    	this.bigTxtSize = bigTxt;
    }
    
    private boolean isToPrint() {
        return false;
    }    

    /**
     *
     */
    public void print() {
    	
//    	if(!isToPrint()) {
//    		return;
//    	}
    	
		String[] listImprimante = StringUtil.getArrayFromStringDelim(caisseBean.getImprimantes(), "|");
        String[] caisseDestArray = StringUtil.getArrayFromStringDelim(caisseMvm.getCaisse_cuisine(), ";");
 		 if(caisseDestArray != null){
 			for(String caisseElement : caisseDestArray){
 				String[] caisseElementArray = StringUtil.getArrayFromStringDelim(caisseElement, ":");
 				Long caisseId = Long.valueOf(caisseElementArray[0]);
 				if(caisseBean.getId() == caisseId){
 					Long elementId = Long.valueOf(caisseElementArray[1]);
 					this.listDetail.add(""+elementId);
 				}
 			}
 		 }
 		 if(listImprimante != null) {
	        for (String imprimante : listImprimante) {
	        	int nbr_ticket = this.caisseBean.getNbr_ticket()!=null?this.caisseBean.getNbr_ticket():1;
				for(int i=0; i<nbr_ticket; i++){
	        		startPrint(imprimante);
	        	}
	        }
 		 }
	}
    
    /**
     * @param imprimante 
     */
    private void startPrint(String imprimante) {
        new Thread(() -> {
            try {
                PrinterJob pj = PrinterJob.getPrinterJob();
                // ...
                if (StringUtil.isNotEmpty(imprimante)) {
                    try {
                        AttributeSet attrSet = new HashPrintServiceAttributeSet(new PrinterName(imprimante, null));
                        final PrintService[] lookupPrintServices = PrintServiceLookup.lookupPrintServices(null, attrSet);
                        if (lookupPrintServices == null || lookupPrintServices.length == 0) {
                            return;
                        }
                        pj.setPrintService(lookupPrintServices[0]);
                    } catch (PrinterException ex) {
                        Logger.getLogger(PrintCuisineUtil.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                pj.setPrintable(new MyPrintable(), getPageFormat(pj));
               
                pj.print();
                
            } catch (PrinterAbortException ex) {
                Logger.getLogger(PrintCuisineUtil.class.getName()).log(Level.SEVERE, "Printing operation was aborted.", ex);
            } catch (PrinterException ex) {
                Logger.getLogger(PrintCuisineUtil.class.getName()).log(Level.SEVERE, "Error occurred during printing.", ex);
            }
        }).start();
       
    }


    public PageFormat getPageFormat(PrinterJob pj) {
        PageFormat pf = pj.defaultPage();
        Paper paper = pf.getPaper();

        int heightTicket = 0;
        if(caisseMvm.getList_article() != null){
	        heightTicket = caisseMvm.getList_article().size() + 20;// Ajout marge
        }
        
        double middleHeight = heightTicket * 1.0; // paper.getHeight(); 
        // with the row count of
        // jtable
        double headerHeight = 1.0; // fixed----->// Marge de l'entête
        double footerHeight = 20.0; // fixed----->// Marge du pied de page

        double width = convert_CM_To_PPI(8); // printer know only point per
        // inch.default value is 72ppi
        double height = convert_CM_To_PPI(headerHeight + middleHeight + footerHeight);
        paper.setSize(width, height);
        paper.setImageableArea(convert_CM_To_PPI(0.25), convert_CM_To_PPI(0.5), width - convert_CM_To_PPI(0.35),
                height - convert_CM_To_PPI(1)); // define boarder size after
        // that print area width is
        // about 180 points

        pf.setOrientation(PageFormat.PORTRAIT); // select orientation portrait
        // or landscape but for this
        // time portrait
        pf.setPaper(paper);

        return pf;
    }

    protected static double convert_CM_To_PPI(double cm) {
        return toPPI(cm * 0.393600787);
    }

    protected static double toPPI(double inch) {
        return inch * 72d;
    }

	public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {
	    int original_width = imgSize.width;
	    int original_height = imgSize.height;
	    int bound_width = boundary.width;
	    int bound_height = boundary.height;
	    int new_width = original_width;
	    int new_height = original_height;

	    // first check if we need to scale width
	    if (original_width > bound_width) {
	        //scale width to fit
	        new_width = bound_width;
	        //scale height to maintain aspect ratio
	        new_height = (new_width * original_height) / original_width;
	    }

	    // then check if we need to scale even with the new height
	    if (new_height > bound_height) {
	        //scale height to fit instead
	        new_height = bound_height;
	        //scale width to maintain aspect ratio
	        new_width = (new_height * original_width) / original_height;
	    }

	    return new Dimension(new_width, new_height);
	}
	
    public class MyPrintable implements Printable {
        @Override
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
            if (pageIndex > 0) {
                return NO_SUCH_PAGE;
            }
            boolean isToPrint = false;
            
            Font smallTxt = new Font("Roman", Font.PLAIN, smallTxtSize); 
            Font smallTxt8 = new Font("Roman", Font.PLAIN, smallTxtSize8); 
        	Font bigTxt = new Font("Roman", Font.PLAIN, bigTxtSize);
        	Font smallTxtB = new Font("Roman", Font.BOLD, smallTxtSize);
        	Font bigTxtB = new Font("Roman", Font.BOLD, bigTxtSize);
        	
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate((int) pageFormat.getImageableX(), (int) pageFormat.getImageableY());
            
            Map attributes = new Font("Arial", Font.BOLD, 9).getAttributes();
            attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
            Font CUSTOM_FONT_9_B_BARREE = new Font(attributes); 
            
            attributes = new Font("Arial", Font.PLAIN, 9).getAttributes();
            attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
            Font CUSTOM_FONT_9_BARREE = new Font(attributes);
            
            try {
                /* Draw Header */
                int y = 10; // Décalage par rapport au logo

                // Remettre la police
                g2d.setFont(smallTxt8);
                //--------------------------------------------------
                int lineHeight = 16;
				g2d.drawString(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()), lineHeight, y); // print date
				y = y + lineHeight;
				g2d.setFont(smallTxt);
				
				g2d.setFont(new Font("Arial", Font.PLAIN, 11));
				String cmd = "COMMANDE : *** " + (caisseMvm.getRef_commande().length()>12 ? caisseMvm.getRef_commande().substring(12) : caisseMvm.getRef_commande())+" ***";
				int x = getX_centre(g2d, g2d.getFont(), cmd);
				g2d.drawString(cmd, x, y);
                y = y + lineHeight;
                // Serveur
				if(caisseMvm.getOpc_serveur() != null){
					String serveur = caisseMvm.getOpc_serveur().getLogin();
                    g2d.setFont(bigTxt);
                	g2d.drawString("SERVEUR : " + StringUtil.getValueOrEmpty(serveur), lineHeight, y);
                    y = y + lineHeight;
                }
				// Caissier
				if(caisseMvm.getOpc_user() != null){
						String caissier = "";
						EmployePersistant opc_employe = caisseMvm.getOpc_user().getOpc_employe();
						if(opc_employe != null) {
							caissier = opc_employe.getPrenom();
						} else {
							caissier = caisseMvm.getOpc_user().getLogin();
						}

                	g2d.setFont(bigTxt);
                    g2d.drawString("CAISSIER : " + StringUtil.getValueOrEmpty(caissier), lineHeight, y);
                    y = y + lineHeight;
                }

                //
                g2d.setFont(bigTxt);

                // Type de commande
                String typeCmd = "";
                if(ContextAppli.TYPE_COMMANDE.E.toString().equals(caisseMvm.getType_commande())){
                    typeCmd = "A EMPORTER";
                } else if(ContextAppli.TYPE_COMMANDE.P.toString().equals(caisseMvm.getType_commande())){
                     typeCmd = "SUR PLACE";
                } else{
                    typeCmd = "LIVRAISON";
                }
               
                g2d.drawString("Imprimante : " + caisseBean.getReference(), lineHeight, y);
                y = y + lineHeight;
                
                g2d.setFont(bigTxtB);
                
                Map<String, Integer> nbrCouvertTable = new HashMap<>();
                List<CaisseMouvementArticlePersistant> listArt = caisseMvm.getList_article();
               for(CaisseMouvementArticlePersistant art : listArt) {
               	if(art.getNbr_couvert() != null){
               		nbrCouvertTable.put(art.getRef_table(), art.getNbr_couvert());
               	 }
               	}
                
                String refTablesDetail = caisseMvm.getRefTablesDetail();
                String couverts = nbrCouvertTable.get(refTablesDetail)!=null?" ("+nbrCouvertTable.get(refTablesDetail)+" couverts)":"";
                if(StringUtil.isNotEmpty(refTablesDetail)){
					g2d.drawString("TABLE : "+refTablesDetail+couverts, lineHeight, y);
	                y = y + lineHeight;
                }
                
                g2d.setFont(smallTxt);
                
              
                // Numéro token commande
                if(StringUtil.isNotEmpty(caisseMvm.getNum_token_cmd())){     
                    g2d.drawString("COASTER CALL : " + caisseMvm.getNum_token_cmd(), lineHeight, y);
                    y = y + lineHeight;
                }

                g2d.setFont(bigTxtB);
                g2d.drawString(typeCmd, getX_centre(g2d, bigTxtB, typeCmd), y);
                y = y + lineHeight;

                
				// ANNULATION **************************
				 boolean isCmdAnnullee = caisseMvm.getLast_statut() != null && ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.ANNUL.toString().equals(caisseMvm.getLast_statut());
				 
				if(isCmdAnnullee){
					 y = y + 15;
					 g2d.setFont(PrintCommunUtil.CUSTOM_FONT_12_B);
					 g2d.drawString("*** COMMANDE ANNULLEE ***", 0, y);
		             y = y + 15;
				 }
				//**************************************
                
                String[] colNames = {"Article", "Qte"};
                int[] colonnePosition = {5, 180};
                int X_QTE_START = 190;
                
                g2d.setFont(smallTxt);
                g2d.drawLine(0, y, 250, y);// Ligne séparateur
                y = y + 10;

                for (int i = 0; i < colNames.length; i++) {
                    g2d.drawString(colNames[i], colonnePosition[i], y);
                }

                y = y + 10;
                g2d.drawLine(0, y, 250, y);// Ligne séparateur

                //---------- On collecte les taux de TVA -----------------------
               // Integer idxArticle = 0;
                // Recenser les clients
                List<Integer> listIdxClient = new ArrayList<>();
				for(CaisseMouvementArticlePersistant caisseMvm : caisseMvm.getList_article()){
					/*if(BooleanUtil.isTrue(caisseMvm.getIs_annule())){
						continue;
					}*/
					if(!listIdxClient.contains(caisseMvm.getIdx_client()) && caisseMvm.getIdx_client() != null){
						listIdxClient.add(caisseMvm.getIdx_client()); 
					}
				}
                Collections.sort(listIdxClient);
                
                if(listIdxClient != null && listIdxClient.size()>0 && listIdxClient.get(listIdxClient.size()-1) > caisseMvm.getMax_idx_client()){
                	caisseMvm.setMax_idx_client(listIdxClient.get(listIdxClient.size()-1));
            	}
                
                // Les articles -------------------------------------------
				int nbrClient = (caisseMvm.getMax_idx_client() == null ? 1 : caisseMvm.getMax_idx_client());
				
				g2d.setFont(smallTxt);
				
                for(int i=1; i<=nbrClient; i++){
					if(!listIdxClient.contains(i)){
						continue;
					}
					
					if(nbrClient > 1 && !isHideDetail){
						g2d.setFont(smallTxt);
//						  y = y + 10;
//		 	    		  g2d.drawLine(0, y, 250, y);// Ligne séparateur
//		 	    		  y = y + 10;
		 	    		    
		 	    		 y = y + 10;
                		 g2d.drawString("CLIENT "+i, 0, y);
                		 y = y + 10;
                		 
//                		 g2d.drawLine(0, y, 250, y);// Ligne séparateur
//                		 y = y + 10;
					}
					
					String oldMenu = null;
					String mnuIdxCaisseConfig = null;
					int idx = 0;
					Map<String, String> mapMnu = new HashMap<>();
					
					for(CaisseMouvementArticlePersistant caisseMvmDetail : caisseMvm.getList_article()){
						boolean isAnnule = false;
						String type = caisseMvmDetail.getType_ligne();
				       if((caisseMvmDetail.getIdx_client()!=null && caisseMvmDetail.getIdx_client()!=i)){
				           continue;
				       }
				       
				       if((type.equals(TYPE_LIGNE_COMMANDE.GROUPE_FAMILLE.toString()) || type.equals(TYPE_LIGNE_COMMANDE.GROUPE_MENU.toString())) 
				    		   && caisseMvmDetail.getLevel() == 1){
				    	   continue;
				       }
				       
				       g2d.setFont(smallTxt);// Par défaut
				       
				       if(idx == 0){
			        	   y = y + lineHeight;
				       }
				       idx++;
				       
				       String styleConfig = "";
				       boolean isSubMenuArtExist = false;
				       boolean isInCaisseConf = false;
				       
				    // Si menu voir si un détail est en statut no  pret et livre
            		  if(BooleanUtil.isTrue(caisseMvmDetail.getIs_menu())){
            			  String mnuIdx = caisseMvmDetail.getMenu_idx();
					       for (CaisseMouvementArticlePersistant detail2 : caisseMvm.getList_article()) {
	         				  if(detail2.getMenu_idx() != null && detail2.getMenu_idx().equals(mnuIdx)
	         						  && !STATUT_CAISSE_MOUVEMENT_ENUM.PRETE.toString().equals(caisseMvmDetail.getLast_statut())
	         						  && !STATUT_CAISSE_MOUVEMENT_ENUM.LIVRE.toString().equals(caisseMvmDetail.getLast_statut())
	         						  && listDetail != null 
	         				  		  && listDetail.contains(mnuIdx)){
	         					  isSubMenuArtExist = true;
	         					  break;
	         				  }
	         			  }
            		  }
	            		  
				       if((!STATUT_CAISSE_MOUVEMENT_ENUM.PRETE.toString().equals(caisseMvmDetail.getLast_statut()) || isSubMenuArtExist)
				    		   && (listDetail.contains(""+caisseMvmDetail.getId()) || (caisseMvmDetail.getMenu_idx() != null && caisseMvmDetail.getMenu_idx().equals(mnuIdxCaisseConfig)))){
				    		if(caisseMvmDetail.getMenu_idx() != null){
				    			mnuIdxCaisseConfig = caisseMvmDetail.getMenu_idx();
				    		} else{
				    			mnuIdxCaisseConfig = null;
				    		}
				    		styleConfig = "X";
				    		isInCaisseConf = true;
				       }
				       
				       if(!"X".equals(styleConfig)){
				    	   g2d.setFont(smallTxt);// Par défaut
				       } else{
				    	   g2d.setFont(bigTxtB);// Par défaut
				       }

				       boolean isArticle = false;
				       boolean isArticleNoMnu = false;
				       
				       // Ajout du numéro dans le tableau
				       String libCmd = caisseMvmDetail.getLibelle();
//				       if(type == null){
//				           type = "XXX";
//				       }
				       
				       if(BooleanUtil.isTrue(caisseMvmDetail.getIs_suite_lock())){
 				    		libCmd = libCmd + " [Suite]";
 				     	}
				       
				       String qte = "";
			  	       if(caisseMvmDetail.getQuantite() != null){
			  	    	   if(caisseMvmDetail.getQuantite().doubleValue() % 1 != 0){
			  	    		 qte = BigDecimalUtil.formatNumber(caisseMvmDetail.getQuantite());
			  	    	   } else{
			  	    		 qte = ""+caisseMvmDetail.getQuantite().intValue();
			  	    	   }
			  	       }
				       
				       if(caisseMvmDetail.getType_opr() == Integer.valueOf(2) && caisseMvmDetail.getOld_qte_line() != null) {
				    	   libCmd = libCmd + " (ttl:" + qte+")";
				    	   if(caisseMvmDetail.getOld_qte_line().compareTo(caisseMvmDetail.getQuantite()) == -1) {
				    		   int v = BigDecimalUtil.substract(caisseMvmDetail.getQuantite(), caisseMvmDetail.getOld_qte_line()).intValue();
				    		   qte = (v==0 ? "":"+")+v;
				    	   } else {
				    		   int v = BigDecimalUtil.substract(caisseMvmDetail.getOld_qte_line(), caisseMvmDetail.getQuantite()).intValue();
				    		   qte = (v==0 ? "":"-")+v;
				    	   }
				       } 
				       
				       if(BigDecimalUtil.isZero(BigDecimalUtil.get(qte))) {
				    	   return NO_SUCH_PAGE;
				       }
				       
				       String styleTd = "";
				       if(caisseMvmDetail.getMenu_idx() != null || type.equals(TYPE_LIGNE_COMMANDE.MENU.toString())){
				    	  if(type.equals(TYPE_LIGNE_COMMANDE.MENU.toString())) {
				    		  if(caisseMvmDetail.getLevel() != null && caisseMvmDetail.getLevel() == 1){
					        	  continue;
				    		  } else{
				    			  g2d.setFont(isInCaisseConf ? bigTxtB : smallTxt);// Par défaut
				    			  
				    			 // On affiche l'entete menu
				    			  mapMnu.put(caisseMvmDetail.getMenu_idx(), caisseMvmDetail.getOpc_menu().getLibelle());
				    		  }
				           } else if(type.equals(TYPE_LIGNE_COMMANDE.ART_MENU.toString())) {
				        	   isArticle = true;
				        	   
				        	   if(BooleanUtil.isTrue(caisseMvmDetail.getIs_annule())){
				        		   isAnnule = true;
				        		   g2d.setFont( (isInCaisseConf && caisseMvmDetail.getType_opr() == Integer.valueOf(3)) ? CUSTOM_FONT_9_B_BARREE : CUSTOM_FONT_9_BARREE);// Par défaut
				        	   } else{
				        		   g2d.setFont( (isInCaisseConf && (caisseMvmDetail.getType_opr() == Integer.valueOf(1) || caisseMvmDetail.getType_opr() == Integer.valueOf(2) ) ) ? bigTxtB : smallTxt);// Par défaut				        		   
				        	   }
				           } else if(type.equals(TYPE_LIGNE_COMMANDE.GROUPE_MENU.toString())){
				        	   g2d.setFont(smallTxt);// Par défaut
				           } else if(type.equals(TYPE_LIGNE_COMMANDE.GROUPE_FAMILLE.toString())){
				        	   g2d.setFont(smallTxt);// Par défaut
				           }
				       } else{
				    	   if(type.equals(TYPE_LIGNE_COMMANDE.ART.toString())){
				    		   isArticle = true;
				    		   isArticleNoMnu = true;
				    		   if(BooleanUtil.isTrue(caisseMvmDetail.getIs_annule())){
				    			   isAnnule = true;
				        		   g2d.setFont( (isInCaisseConf && caisseMvmDetail.getType_opr() == Integer.valueOf(3)) ? CUSTOM_FONT_9_B_BARREE : CUSTOM_FONT_9_BARREE);// Par défaut
				        	   } else{
				        		   g2d.setFont( (isInCaisseConf && (caisseMvmDetail.getType_opr() == Integer.valueOf(1) || caisseMvmDetail.getType_opr() == Integer.valueOf(2) )) ? bigTxtB : smallTxt);// Par défaut				        		   
				        	   }
				           } else if(type.equals(TYPE_LIGNE_COMMANDE.GROUPE_FAMILLE.toString())){
				        	   // Ajouter une ligne e séparation
//				        	   if(!isFamillePassed){
//				        		    y = y + 10;
//				 	    		    g2d.drawLine(0, y, 220, y);// Ligne séparateur
//				 	    		    y = y + 10;
//			                		isFamillePassed = true;
//				        	   }
				        	  // g2d.setFont(PrintCuisineUtil.CUSTOM_FONT_9);// Par défaut
				        	   
				        	  // g2d.drawString(libCmd+"--------------", 0, y);
				        	   //y = y + lineHeight;
				           } 
				       }
				       if(caisseMvmDetail.getLevel() != null){
				    	   if(caisseMvmDetail.getLevel() != null && caisseMvmDetail.getLevel() > 1){
				    		   for(int ix=0; ix<caisseMvmDetail.getLevel(); ix++){
						    	   styleTd = styleTd + " ";
						       }
				    		   if(!type.equals(TYPE_LIGNE_COMMANDE.MENU.toString())){
				    			   styleTd = styleTd + "  ";
				    		   }
					       } else if(isArticle){
					    	   styleTd = styleTd + "    ";
					       }
				       }
				      
				       boolean isToAdd = isCmdAnnullee || (!caisseMvmDetail.getLibelle().startsWith("#") && !type.equals(TYPE_LIGNE_COMMANDE.LIVRAISON.toString()));
				       //
				       if (isToAdd && (!isArticle || (isArticle && "C".equals(caisseMvmDetail.getOpc_article().getDestination())))) {
				    	   boolean isNotArt = (!type.equals(TYPE_LIGNE_COMMANDE.ART.toString()) && !type.equals(TYPE_LIGNE_COMMANDE.ART_MENU.toString()));
				         // Entete menu ----------
				    	   if(!listDetail.contains(""+caisseMvmDetail.getId()) || (BooleanUtil.isTrue(caisseMvmDetail.getIs_menu()) 
				    			   && caisseMvmDetail.getLevel() != 1) || (!BooleanUtil.isTrue(caisseMvmDetail.getIs_menu()) && isNotArt)){
				    		   	if(!isHideDetail){
				    			   	g2d.setFont(smallTxt);// Par défaut
				    		   		
				    			   	isToPrint = true;
				    			   	
				    			   	if(isAnnule) {
				    			   		addTextBackground(g2d, styleTd+getLibelleMaxLength(libCmd), 0, y, true);
				    			   	} else {
				    			   		g2d.drawString(styleTd+getLibelleMaxLength(libCmd), 0, y);
				    			   	}
				    		   		y = y + lineHeight;
				    		   		
					    		   	// Commentaire ------------
								    if(StringUtil.isNotEmpty(caisseMvmDetail.getCommentaire())){
								    	g2d.setFont(smallTxt8);
								    	g2d.drawString("**...."+caisseMvmDetail.getCommentaire(), 0, y);
								    	y = y + lineHeight;
								    }
				    		   	}
				          
				    	   // Entete famille ---------
				    	  /* else if(caisseMvmDetail.getMenu_idx() == null && type.equals(TYPE_LIGNE_COMMANDE.GROUPE_FAMILLE.toString())){
				        	 y = y + 10;
	                		 g2d.drawString(styleTd+libCmd, 0, y);
	                		 y = y + 10;
				         } */
				    	 // Article --------------  
				    	   } else if(isArticle 
				    			   && (isOnlyNewTicketCuis || (!isOnlyNewTicketCuis 
				    					   && ((BooleanUtil.isTrue(caisseMvmDetail.getIs_annule()) && caisseMvmDetail.getType_opr()!=null) || bigTxtB.equals(g2d.getFont()))))){
				    		   
				    		 isToPrint = true;
				    		   
				    		 // Entete menu
				    		 if(caisseMvmDetail.getMenu_idx() != null && (oldMenu == null || !caisseMvmDetail.getMenu_idx().equals(oldMenu))){
				    			 
				    			 y = y + 10;
				    			 g2d.drawString(mapMnu.get(caisseMvmDetail.getMenu_idx()), 0, y);
				    			 y = y + 7;
				    			 // Ligne séparateur
				    			 addLignePointille(g2d, 0, y, 250, y);
		                		 y = y + 15;
		                		 //--------------------
				    		 }
				    		 oldMenu = caisseMvmDetail.getMenu_idx();
				    		 
				    		 if(isArticleNoMnu) {
				    			 // Ligne séparateur
				    			 addLignePointille(g2d, 0, y, 250, y);
		                		 y = y + 15;
		                		 //--------------------
				    		 }
				    		 // ------------- Article lib
				    		 if(isAnnule) {
			    			   	addTextBackground(g2d, getLibelleMaxLength((isInCaisseConf ? "*=>":"        ")+libCmd), 0, y, true);
			    			 } else {
			    			   	g2d.drawString(getLibelleMaxLength((isInCaisseConf ? "*=>":"        ")+libCmd), 0, y);
			    			 }
				    		 
				    		 if(isAnnule) {
				    			addTextBackground(g2d, qte, (X_QTE_START-g2d.getFontMetrics().stringWidth(qte)), y, true);
				    		 } else {
				    			g2d.drawString(qte, (X_QTE_START-g2d.getFontMetrics().stringWidth(qte)), y);
				    	     }
	                		 y = y + lineHeight;
	                		 
	                		// Commentaire ------------
	 					    if(StringUtil.isNotEmpty(caisseMvmDetail.getCommentaire())){
	 					    	g2d.setFont(smallTxt8);
	 					    	g2d.drawString("**...."+caisseMvmDetail.getCommentaire(), 0, y);
	 					    	y = y + lineHeight;
	 					    }
				        }
				   }
				 }
             }
            } catch (Exception r) {
                r.printStackTrace();
            }

            return (isToPrint ? PAGE_EXISTS : NO_SUCH_PAGE);
        }
        
        private String getLibelleMaxLength(String lib) {
        	int carcRetourLigne = StringUtil.isEmpty(ContextGloabalAppli.getGlobalConfig("BACKLINE_TICKET_CUIS")) ? 
  					45 : Integer.valueOf(ContextGloabalAppli.getGlobalConfig("BACKLINE_TICKET_CUIS"));
        	
        	if(lib != null && lib.length() > carcRetourLigne) {
        		lib = (lib.substring(0, carcRetourLigne-3))+"...";
        	}
        	return lib;
        }
        
        private void addLignePointille(Graphics2D g2d, int x, int y, int w, int h) {
        	Stroke sttrokOrigin = g2d.getStroke();
        	float[] dash1 = { 2f };
	   		 BasicStroke bs1 = new BasicStroke(1, 
	   			        BasicStroke.CAP_BUTT, 
	   			        BasicStroke.JOIN_ROUND, 
	   			        1.0f, 
	   			        dash1,
	   			        2f);
	   		g2d.setStroke(bs1);
            g2d.drawLine(x, y, w, h);
        }
        
        private void addTextBackground(Graphics2D g2d, String text, int x, int y, boolean isBarre) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            AttributedString as1 = new AttributedString(text);
            as1.addAttribute(TextAttribute.FOREGROUND, Color.WHITE, 0, text.length());
            as1.addAttribute(TextAttribute.BACKGROUND, Color.BLACK, 0, text.length());
            if(isBarre) {
            	as1.addAttribute(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
        	}
            g2d.drawString(as1.getIterator(), x, y);
        }
        
        private int getX_centre(Graphics2D g2d, Font font, String text) {
            FontMetrics metrics = g2d.getFontMetrics(font);
            // Determine the X coordinate for the text
            int x = ((PrintCuisineUtil.PAPER_WIDTH - metrics.stringWidth(text)) / 2) + 15;
            return x;
        }
    }
}