/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appli.model.domaine.caisse.service;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;

import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_LIGNE_COMMANDE;
import appli.model.domaine.administration.persistant.UserPersistant;
import appli.model.domaine.personnel.persistant.ClientPersistant;
import appli.model.domaine.stock.dao.IFamilleDao;
import appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import appli.model.domaine.vente.persistant.CaissePersistant;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.printGen.PrintPosBean;
import framework.model.util.printGen.PrintPosDetailBean;

public class PrintPilotageUtil {
    private static Font CUSTOM_FONT_7 = new Font("Roman", Font.PLAIN, 7);
    public static Font CUSTOM_FONT_7_B = new Font("Roman", Font.BOLD, 7);
    private static Font CUSTOM_FONT_8_B = new Font("Roman", Font.BOLD, 8);
    private static Font CUSTOM_FONT_9_B = new Font("Roman", Font.BOLD, 9);
    private static Font CUSTOM_FONT_13_B = new Font("Arial", Font.BOLD, 13);
    private static final int PAPER_WIDTH = 180;
    private List<Long> listChangedDetailIds = new ArrayList<>();

    private PrintPosBean printBean;
    public PrintPilotageUtil(CaissePersistant caisseBean, CaisseMouvementPersistant caisseMvm, List<Long> listChanged) {
    	this.printBean = new PrintPosBean();
    	this.listChangedDetailIds = listChanged;
    	
    	List<PrintPosDetailBean> listDataToPrint = buildMapData(caisseMvm);

    	this.printBean.setListDetail(listDataToPrint);
    	this.printBean.setMaxLineLength(50);
    	
		BigDecimal heightTicket = null;
        if(caisseMvm.getList_article() != null){
	        heightTicket = BigDecimalUtil.get(caisseMvm.getList_article().size());
        }
        Integer nbr_ticket = caisseBean.getNbr_ticket()==null?1:caisseBean.getNbr_ticket();
		this.printBean.setNbrTicket(nbr_ticket);
        this.printBean.setTicketHeight(heightTicket);
    	this.printBean.setPrinters(caisseBean.getImprimantes());
    }
    
    public PrintPosBean getPrintPosBean(){
    	return this.printBean;
    }
    
    /**
     * @return
     */
    private List<PrintPosDetailBean> buildMapData(CaisseMouvementPersistant mouvement) {
    	Map<String, String> mapConfig = (Map<String, String>) MessageService.getGlobalMap().get("GLOBAL_CONFIG");
    	if(mapConfig == null){
    		mapConfig = new HashMap<String, String>();
    	}
    	
        if(mouvement == null) {
            IFamilleDao famDao = (IFamilleDao)ServiceUtil.getBusinessBean(IFamilleDao.class);
            mouvement = (CaisseMouvementPersistant) famDao.findById(CaisseMouvementPersistant.class, mouvement.getId());
        }
        List<PrintPosDetailBean> listPrintLinrs = new ArrayList<>();
        
        try {
            /* Draw Header */
            int y = 10; // Décalage par rapport au logo

            y = y + 20;
            listPrintLinrs.add(new PrintPosDetailBean(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()), 10, y, PrintPilotageUtil.CUSTOM_FONT_7));
            y = y + 10;

            if(mouvement.getOpc_user() != null && mouvement.getOpc_user().getOpc_employe() != null){
                listPrintLinrs.add(new PrintPosDetailBean("CAISSIER : " + 
                				StringUtil.firstCharToUpperCase(mouvement.getOpc_user().getOpc_employe().getNom().substring(0,1))+". "+
                				StringUtil.getValueOrEmpty(mouvement.getOpc_user().getOpc_employe().getPrenom()), 
                				10, y, PrintPilotageUtil.CUSTOM_FONT_7));
                y = y + 10;
            }
            // Table
            String refTables = mouvement.getRefTablesDetail();
            if(StringUtil.isNotEmpty(refTables)){
            	 listPrintLinrs.add(new PrintPosDetailBean("TABLE : "+refTables, 10, y, PrintPilotageUtil.CUSTOM_FONT_9_B));
                 y = y + 10;
            }
            // Serveur
            if(mouvement.getOpc_serveur() != null){
                listPrintLinrs.add(new PrintPosDetailBean("SERVEUR : " + 
                		mouvement.getOpc_serveur().getLogin().toUpperCase(), 10, y, PrintPilotageUtil.CUSTOM_FONT_7));
                y = y + 10;
            }
            
            // Marquer commande non terminée
            String[] tempStatus = {
            		ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.TEMP.toString(), 
            		ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.ANNUL.toString()
            	};
            
            if(mouvement.getLast_statut() != null && StringUtil.contains(mouvement.getLast_statut(), tempStatus)){
                String statutCmd = "** COMMANDE NON VALIDEE **";
                //
            	if(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.ANNUL.toString().equals(mouvement.getLast_statut())){
                	statutCmd = "** COMMANDE ANNULLEE **";
            	} else if(StringUtil.isEmpty(mouvement.getMode_paiement())){
            		statutCmd = "** COMMANDE NON REGLEE **";
            	}
                listPrintLinrs.add(new PrintPosDetailBean(statutCmd, 0, y, PrintPilotageUtil.CUSTOM_FONT_13_B, "C"));
                y = y + 10;
            }
            
            // Type de commande
            String typeCmd = "";
            if(ContextAppli.TYPE_COMMANDE.E.toString().equals(mouvement.getType_commande())){
                typeCmd = "A EMPORTER";
            } else if(ContextAppli.TYPE_COMMANDE.P.toString().equals(mouvement.getType_commande())){
                 typeCmd = "SUR PLACE";
            } else{
                typeCmd = "LIVRAISON";
            }
            listPrintLinrs.add(new PrintPosDetailBean("COMMANDE : *** " + (mouvement.getRef_commande().length()>12 ? mouvement.getRef_commande().substring(12):mouvement.getRef_commande())+" ***", 10, y, new Font("Arial", Font.PLAIN, 11), "C"));
            y = y + 15;
            
            // Numéro token commande
            if(StringUtil.isNotEmpty(mouvement.getNum_token_cmd())){     
                listPrintLinrs.add(new PrintPosDetailBean("COASTER CALL : " + mouvement.getNum_token_cmd(), 10, y, PrintPilotageUtil.CUSTOM_FONT_7));
                y = y + 10;
            }

            if(!ContextAppli.TYPE_COMMANDE.L.toString().equals(mouvement.getType_commande())){
            } else {
            	UserPersistant livreur = mouvement.getOpc_livreurU();
            	if(livreur != null){
            		String strCli = "LIVREUR : " + 
            				StringUtil.firstCharToUpperCase(livreur.getLogin());
					listPrintLinrs.add(new PrintPosDetailBean(strCli, 10, y, PrintPilotageUtil.CUSTOM_FONT_7));
					y = y + 10;
            	}
            	
				ClientPersistant opc_client = mouvement.getOpc_client();
				if(opc_client != null){
					String strCli = "CLIENT : " + opc_client.getNom()+" "+StringUtil.getValueOrEmpty(opc_client.getPrenom());
					listPrintLinrs.add(new PrintPosDetailBean(strCli, 10, y, PrintPilotageUtil.CUSTOM_FONT_7));
					if (StringUtil.isNotEmpty(opc_client.getAdresse_rue())) {
						y = y + 10;
						listPrintLinrs.add(new PrintPosDetailBean(opc_client.getAdresse_rue(), 10, y, PrintPilotageUtil.CUSTOM_FONT_9_B));
					}
					if (StringUtil.isNotEmpty(opc_client.getAdresse_compl())) {
						y = y + 10;
						listPrintLinrs.add(new PrintPosDetailBean(opc_client.getAdresse_compl(), 10, y, PrintPilotageUtil.CUSTOM_FONT_9_B));
					}
					if (StringUtil.isNotEmpty(opc_client.getVilleStr())) {
						y = y + 10;
						listPrintLinrs.add(new PrintPosDetailBean(" - " + opc_client.getVilleStr(), 10, y, PrintPilotageUtil.CUSTOM_FONT_9_B));
					}
					if(StringUtil.isNotEmpty(opc_client.getTelephone())){
						y = y + 10;
						listPrintLinrs.add(new PrintPosDetailBean("Tél 1 : "+opc_client.getTelephone(), 10, y, PrintPilotageUtil.CUSTOM_FONT_9_B));
					}
					if(StringUtil.isNotEmpty(opc_client.getTelephone2())){
						y = y + 10;
						listPrintLinrs.add(new PrintPosDetailBean("Tél 2 : "+opc_client.getTelephone2(), 10, y, PrintPilotageUtil.CUSTOM_FONT_9_B));
					}
				    y = y + 10;
				}
			}
            y = y + 5;
            listPrintLinrs.add(new PrintPosDetailBean(typeCmd, 10, y, PrintPilotageUtil.CUSTOM_FONT_8_B, "C"));
            y = y + 10;

            listPrintLinrs.add(new PrintPosDetailBean(0, y, 220, y));
            y = y + 10;

            String[] colNames = {"Article", "Qte"};
            int[] colonnePosition = {5, 170};

            for (int i = 0; i < colNames.length; i++) {
                listPrintLinrs.add(new PrintPosDetailBean(colNames[i], colonnePosition[i], y, PrintPilotageUtil.CUSTOM_FONT_7));
            }

            y = y + 10;
            listPrintLinrs.add(new PrintPosDetailBean(0, y, 220, y));

            //---------- On collecte les taux de TVA -----------------------
            int X_QTE_START = 180;

            int nbrNiveau = 0;
            Integer idxArticle = 0;
            CaisseMouvementArticlePersistant ligneLivraison = null;
            
            
            // Recenser les clients
            List<CaisseMouvementArticlePersistant> listMvm = (mouvement.getListEncaisse()!=null && mouvement.getListEncaisse().size()>0) ? mouvement.getListEncaisse():mouvement.getList_article();
            List<Integer> listIdxClient = new ArrayList<>();
            for(CaisseMouvementArticlePersistant caisseMvmP : listMvm){
            	if(BooleanUtil.isTrue(caisseMvmP.getIs_annule())){
            		continue;
            	}
            	if(!listIdxClient.contains(caisseMvmP.getIdx_client()) && caisseMvmP.getIdx_client() != null){
            		listIdxClient.add(caisseMvmP.getIdx_client());
            	}
            }
            Collections.sort(listIdxClient);
            
            if(listIdxClient != null && listIdxClient.size()>0 && listIdxClient.get(listIdxClient.size()-1) > mouvement.getMax_idx_client()){
            	mouvement.setMax_idx_client(listIdxClient.get(listIdxClient.size()-1));
        	}
            
            // Les articles -------------------------------------------
            for(int i=1; i<=mouvement.getMax_idx_client(); i++){
            	if(!listIdxClient.contains(i)){
            		continue;
            	}
            	//
            	if(listIdxClient.size() > 1 && i != listIdxClient.get(0)){
 	    		    y = y + 10;
 	    		   listPrintLinrs.add(new PrintPosDetailBean(0, y, 220, y));
 	                y = y + 10;
            	}
            	
            	if(listIdxClient.size()>1){
            		 y = y + 10;
            		 listPrintLinrs.add(new PrintPosDetailBean("CLIENT "+i, 0, y, PrintPilotageUtil.CUSTOM_FONT_7));
            		 y = y + 10;
            		 listPrintLinrs.add(new PrintPosDetailBean(0, y, 220, y));
            		 y = y + 10;
            	}
            	
            	// Détail des articles
                for (CaisseMouvementArticlePersistant detail : listMvm) {
                    if((detail.getIs_annule() != null && detail.getIs_annule()) || (detail.getIdx_client()!=null && detail.getIdx_client()!=i)){
                        continue;
                    }
                    //
					if("LIVRAISON".equals(detail.getType_ligne())){
						ligneLivraison = detail;
						continue;
					}
					
//                    String article = detail.getLibelle();
                    String type = detail.getType_ligne();
                    
                    boolean isArtMenu = type.equals(ContextAppli.TYPE_LIGNE_COMMANDE.ART_MENU.toString());
					boolean isArt = type.equals(ContextAppli.TYPE_LIGNE_COMMANDE.ART.toString());

                    int startLine = 0;
                    Font font = PrintPilotageUtil.CUSTOM_FONT_7;// Par défaut
                    
                    if(type.equals(ContextAppli.TYPE_LIGNE_COMMANDE.MENU.toString())) {
                    	font = PrintPilotageUtil.CUSTOM_FONT_8_B;
                        startLine = 0;
                    } else if(type.equals(ContextAppli.TYPE_LIGNE_COMMANDE.GROUPE_MENU.toString())){
                    	font = PrintPilotageUtil.CUSTOM_FONT_7_B;
                        startLine = 10;
                        nbrNiveau++;
                    } else if(detail.getMenu_idx() == null && type.equals(ContextAppli.TYPE_LIGNE_COMMANDE.GROUPE_FAMILLE.toString())){
                    	font = PrintPilotageUtil.CUSTOM_FONT_7_B;
                        startLine = (detail.getLevel()!=null?detail.getLevel()*3:0);
                        nbrNiveau++;
                    } else if(detail.getMenu_idx() != null && type.equals(ContextAppli.TYPE_LIGNE_COMMANDE.GROUPE_FAMILLE.toString())){
                    	font = PrintPilotageUtil.CUSTOM_FONT_7_B;
                        startLine = (detail.getLevel()!=null?detail.getLevel()*3:0);
                        nbrNiveau++;
                    } else if(isArtMenu) {
					    startLine = 20;
					    nbrNiveau = 0;
					} else if(isArt){
					    startLine = 10;
					    nbrNiveau = 0;
					} 

                    Integer NBR_NIVEAU_CAISSE = StringUtil.isNotEmpty(mapConfig.get("NBR_NIVEAU_CAISSE"))?Integer.valueOf(mapConfig.get("NBR_NIVEAU_CAISSE")):null;
                    boolean isArticle = (isArt || isArtMenu);
                    boolean isToAdd = (isArticle || NBR_NIVEAU_CAISSE == null || nbrNiveau <= NBR_NIVEAU_CAISSE) && !detail.getLibelle().startsWith("#");
                    
                    if(isToAdd){
                    	
                       Font fontTicket = null;
 				       if(!this.listChangedDetailIds.contains(detail.getId())){
 				    	  fontTicket = PrintCuisineUtil.CUSTOM_FONT_7;
				       } else{
				    	   boolean isNotArt = (!type.equals(TYPE_LIGNE_COMMANDE.ART.toString()) && !type.equals(TYPE_LIGNE_COMMANDE.ART_MENU.toString()));
				    	   if((BooleanUtil.isTrue(detail.getIs_menu()) && detail.getLevel() != 1) || (!BooleanUtil.isTrue(detail.getIs_menu()) && isNotArt)){
				    		   fontTicket = PrintCuisineUtil.CUSTOM_FONT_7;				    		   
				    	   } else{
				    		   fontTicket = PrintCuisineUtil.CUSTOM_FONT_9_B;
				    	   }
				       }
 				       
 				      String libCmd = detail.getLibelle();
 				      
 				  // MENUS ----------------------------------
                    if(type.equals(TYPE_LIGNE_COMMANDE.MENU.toString()) && (detail.getLevel() == null || detail.getLevel() > 1)) {
                        idxArticle++;
                        libCmd = idxArticle + "-" + libCmd;
                    } else if(type.equals(TYPE_LIGNE_COMMANDE.ART.toString())){
                        idxArticle++;
                        libCmd = idxArticle + "-" + libCmd;
                    }
 	                    
 				       String qte = "";
 			  	       if(detail.getQuantite() != null){
 			  	    	   if(detail.getQuantite().doubleValue() % 1 != 0){
 			  	    		 qte = BigDecimalUtil.formatNumber(detail.getQuantite());
 			  	    	   } else{
 			  	    		 qte = ""+detail.getQuantite().intValue();
 			  	    	   }
 			  	       }
 			  	       
				       if(detail.getType_opr() == Integer.valueOf(2) && detail.getOld_qte_line() != null) {
				    	   libCmd = libCmd + " (" + qte+")";
				    	   if(detail.getOld_qte_line().compareTo(detail.getQuantite()) == -1) {
				    		   qte = "+"+BigDecimalUtil.substract(detail.getQuantite(), detail.getOld_qte_line()).intValue();
				    	   } else {
				    		   qte = "-"+BigDecimalUtil.substract(detail.getOld_qte_line(), detail.getQuantite()).intValue();
				    	   }
				       } 
				       
//	                    BigDecimal quantite = detail.getQuantite();
//	                    String qte = quantite != null ? ""+quantite.intValue() : "";
//	                    y = y + 5; // shifting drawing line
	
	                    // Afficher l'article et gérer le retour à la ligne
                    	y = y + 10;
                    	listPrintLinrs.add(new PrintPosDetailBean(libCmd, startLine, y, fontTicket));
	                    //
	                    if((!isArticle && StringUtil.isNotEmpty(qte) && !qte.equals("1,00") && !qte.equals("1")) || isArticle){
	                    	listPrintLinrs.add(new PrintPosDetailBean(qte, X_QTE_START, y, fontTicket, "R"));
	                    }
	                    if(StringUtil.isNotEmpty(detail.getCommentaire())){
	                        y = y + 10; // shifting drawing line
	                        listPrintLinrs.add(new PrintPosDetailBean("**...."+detail.getCommentaire(), 0, y, font));
	                    }
                    }
                }
            }
            
            if(listIdxClient.size() > 1){
            	y = y + 10;
            	listPrintLinrs.add(new PrintPosDetailBean(0, y, 220, y));
	            y = y + 10;
            }
            
            // Ligne livraison
            if(ligneLivraison != null) {
            	y = y + 10;
            	// Aligner à droire
                listPrintLinrs.add(new PrintPosDetailBean(ligneLivraison.getLibelle(), 0, y, PrintPilotageUtil.CUSTOM_FONT_8_B));
                listPrintLinrs.add(new PrintPosDetailBean("", X_QTE_START, y, PrintPilotageUtil.CUSTOM_FONT_8_B));
            }

            // Ligne séparateur
            y = y + 10;
            listPrintLinrs.add(new PrintPosDetailBean(0, y, 220, y));
        } catch (Exception r) {
            r.printStackTrace();
        }

        return listPrintLinrs;
    }

    /**
     *
     */
    public void print() {
    	String[] listImprimante = StringUtil.getArrayFromStringDelim(printBean.getPrinters(), "|");
    	if(listImprimante == null || listImprimante.length == 0) {
    		return;
    	}
        for (String imprimante : listImprimante) {
        	if(StringUtil.isEmpty(imprimante)){
    			continue;
    		}
        	for(int i=0; i<this.printBean.getNbrTicket(); i++){
        		startPrint(imprimante);
        	}
        }
    }
    
    /**
     * @param imprimante 
     */
    private void startPrint(String imprimante){
         new Thread(() -> {
              try {
                PrinterJob pj = PrinterJob.getPrinterJob();
                // 
                if(StringUtil.isNotEmpty(imprimante)){
                    try{
                        AttributeSet attrSet = new HashPrintServiceAttributeSet(new PrinterName(imprimante, null));
                        final PrintService[] lookupPrintServices = PrintServiceLookup.lookupPrintServices(null, attrSet);
                        if(lookupPrintServices == null || lookupPrintServices.length == 0){
                            return;
                        } 
                        
                        pj.setPrintService(lookupPrintServices[0]);
                    } catch (PrinterException ex) {
                        Logger.getLogger(PrintPilotageUtil.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }                    

                pj.setPrintable(new MyPrintable(), getPageFormat(pj));
           
                pj.print();
            } catch (PrinterException ex) {
                Logger.getLogger(PrintPilotageUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }

    public PageFormat getPageFormat(PrinterJob pj) {
        PageFormat pf = pj.defaultPage();
        Paper paper = pf.getPaper();

        BigDecimal heightTicket = printBean.getTicketHeight();
        double middleHeight = heightTicket.doubleValue(); // paper.getHeight(); 
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
           
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate((int) pageFormat.getImageableX(), (int) pageFormat.getImageableY());
            int MAX_STR_LENGTH = printBean.getMaxLineLength();
            
            for(PrintPosDetailBean printPosBean : printBean.getListDetail()){
            	if(printPosBean.getType().equals("T")){// Text
            		 g2d.setFont(printPosBean.getFont());
            		 
            		 int x = printPosBean.getX();
            		 if("C".equals(printPosBean.getAlign())){// Centre
            			 x = getX_centre(g2d, printPosBean.getFont(), ""+printPosBean.getData());
            		 } else if("R".equals(printPosBean.getAlign())){// A droite
            			x = (x-g2d.getFontMetrics().stringWidth(""+printPosBean.getData())); 
            		 }
            		// Afficher l'article et gérer le retour à la ligne -----------------------------------------
                     String text = ""+printPosBean.getData();
 					int nbrLigne = Math.abs(text.length()/MAX_STR_LENGTH)+1 ;
                     if(nbrLigne > 1){
                     	for(int j=0; j<nbrLigne; j++){ 
                     		int endLine = (j*MAX_STR_LENGTH)+MAX_STR_LENGTH > text.length() ? text.length() : (j*MAX_STR_LENGTH)+MAX_STR_LENGTH;
                     		printPosBean.setData(text.substring(j*MAX_STR_LENGTH, endLine));
                     		//
                     		g2d.drawString(""+printPosBean.getData(), x+(j==0?0:2), printPosBean.getY());
                     		printPosBean.setY(printPosBean.getY() + 5);
                     	}
                     } else{
                     	g2d.drawString(""+printPosBean.getData(), x, printPosBean.getY());
                     }
                     //-------------------------------------------------------------------------------------------
            	} else if(printPosBean.getType().equals("I")){// Image
					try {
						BufferedImage read = ImageIO.read((File)printPosBean.getData());
						g2d.drawImage(read, printPosBean.getX(), printPosBean.getY(), printPosBean.getWidth(), printPosBean.getHeight(), null); // draw
					} catch (IOException e) {
						e.printStackTrace();
					}
            	} else{// Séparateur
            		g2d.drawLine(printPosBean.getX(), printPosBean.getY(), printPosBean.getWidth(),  printPosBean.getHeight());
            	}
            }
            
            return PAGE_EXISTS;
        }

        private int getX_centre(Graphics2D g2d, Font font, String text) {
            FontMetrics metrics = g2d.getFontMetrics(font);
            // Determine the X coordinate for the text
            int x = ((PrintPilotageUtil.PAPER_WIDTH - metrics.stringWidth(text)) / 2) + 15;
            return x;
        }
    }    
}
