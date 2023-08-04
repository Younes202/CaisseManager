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
import java.awt.font.TextAttribute;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import appli.model.domaine.personnel.persistant.EmployePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import appli.model.domaine.vente.persistant.CaissePersistant;
import framework.controller.ContextGloabalAppli;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.printGen.PrintCommunUtil;

public class PrintCuisineSuiteCmdUtil {
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

    private CaisseMouvementPersistant caisseMvm;
    private CaisseMouvementArticlePersistant detail;
    private CaissePersistant caisseBean;
    private List<String> listDetail = new ArrayList<>();
    private int smallTxtSize = 7, smallTxtSize8 = 8, bigTxtSize = 9;
    
    public PrintCuisineSuiteCmdUtil(CaissePersistant caisseBean, 
    		CaisseMouvementPersistant caisseMvm, CaisseMouvementArticlePersistant detail) {
    	
    	String smallConfig = ContextGloabalAppli.getGlobalConfig("TICKET_CUIS_FONT_SMALL");
  		String bigConfig = ContextGloabalAppli.getGlobalConfig("TICKET_CUIS_FONT_BIG");
    	
  		int smallTxt = StringUtil.isEmpty(smallConfig) ? 7 : Integer.valueOf(smallConfig);
  		int bigTxt = StringUtil.isEmpty(bigConfig) ? 9 : Integer.valueOf(bigConfig);
  		
    	this.caisseMvm = caisseMvm;
    	this.detail = detail;
    	this.caisseBean = caisseBean;
    	this.smallTxtSize = smallTxt;
    	this.bigTxtSize = bigTxt;
    }
    
    /**
     *
     */
    public void print() {
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
                        Logger.getLogger(PrintCuisineSuiteCmdUtil.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }                    

                pj.setPrintable(new MyPrintable(), getPageFormat(pj));
           
                pj.print();
            } catch (PrinterException ex) {
                Logger.getLogger(PrintCuisineSuiteCmdUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }

    public PageFormat getPageFormat(PrinterJob pj) {
        PageFormat pf = pj.defaultPage();
        Paper paper = pf.getPaper();

        int heightTicket = 0;
        if(caisseMvm.getList_article() != null){
	        heightTicket = caisseMvm.getList_article().size();
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
                
                String refTablesDetail = caisseMvm.getRefTablesDetail();
                if(StringUtil.isNotEmpty(refTablesDetail)){
					g2d.drawString("TABLE : "+refTablesDetail, lineHeight, y);
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

				 y = y + 15;
				 g2d.setFont(PrintCommunUtil.CUSTOM_FONT_12_B);
				 g2d.drawString("*** SUITE COMMANDE ***", 0, y);
	             y = y + 15;

		       String qte = "";
	  	       if(detail.getQuantite() != null){
	  	    	   if(detail.getQuantite().doubleValue() % 1 != 0){
	  	    		 qte = BigDecimalUtil.formatNumber(detail.getQuantite());
	  	    	   } else{
	  	    		 qte = ""+detail.getQuantite().intValue();
	  	    	   }
	  	       }
				       
		    	g2d.setFont(smallTxt8);
		    	g2d.drawString(detail.getLibelle()+" ("+qte+")", 0, y);
		    	y = y + lineHeight;
            } catch (Exception r) {
                r.printStackTrace();
            }

            return PAGE_EXISTS;
        }
        
        private int getX_centre(Graphics2D g2d, Font font, String text) {
            FontMetrics metrics = g2d.getFontMetrics(font);
            // Determine the X coordinate for the text
            int x = ((PrintCuisineSuiteCmdUtil.PAPER_WIDTH - metrics.stringWidth(text)) / 2) + 15;
            return x;
        }
    }
}