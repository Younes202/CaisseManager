package framework.model.util.printGen;

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
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.AttributedString;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;

public class PrintCommunUtil {
	public static final int PAPER_WIDTH = 180;	
	private PrintPosBean printBean;

    public static Font CUSTOM_FONT_7 = new Font("Roman", Font.PLAIN, 7);//Monospaced
    public static Font CUSTOM_FONT_7_B = new Font("Roman", Font.BOLD, 7);//Monospaced 
    public static Font CUSTOM_FONT_8 = new Font("Roman", Font.PLAIN, 8);
    public static Font CUSTOM_FONT_8_B = new Font("Roman", Font.BOLD, 8);
    public static Font CUSTOM_FONT_11_B = new Font("Arial", Font.BOLD, 11);
    public static Font CUSTOM_FONT_13_B = new Font("Arial", Font.BOLD, 13);
    public static Font CUSTOM_FONT_14_B = new Font("Arial", Font.BOLD, 14);
    public static Font CUSTOM_FONT_15_B = new Font("Arial", Font.BOLD, 15);
    public static Font CUSTOM_FONT_16_B = new Font("Arial", Font.BOLD, 16);
    public static Font CUSTOM_FONT_17_B = new Font("Arial", Font.BOLD, 17);
    public static Font CUSTOM_FONT_18_B = new Font("Arial", Font.BOLD, 18);
    public static Font CUSTOM_FONT_19_B = new Font("Arial", Font.BOLD, 19);
    public static Font CUSTOM_FONT_20_B = new Font("Arial", Font.BOLD, 20);
    public static Font CUSTOM_FONT_9 = new Font("Monospaced", Font.PLAIN, 9); 
    public static Font CUSTOM_FONT_10 = new Font("Monospaced", Font.PLAIN, 10); 
    public static Font CUSTOM_FONT_9_B = new Font("Monospaced", Font.BOLD, 9);
    public static Font CUSTOM_FONT_10_B = new Font("Monospaced", Font.BOLD, 10);
    public static Font CUSTOM_FONT_12_B = new Font("Arial", Font.BOLD, 12);
    
	public PrintCommunUtil(PrintPosBean printBean) {
		this.printBean = printBean;
	}
	
	/**
	 *
	 */
	public void print() {
		if(printBean == null){
			return;
		}
		String[] listImprimante = getArrayFromStringDelim(printBean.getPrinters(), "|");
    	if(listImprimante == null || listImprimante.length == 0) {
    		return;
    	}
    	
    	int nbrTicket = this.printBean.getNbrTicket()==0 ? 1 : this.printBean.getNbrTicket();
    	
    	for(String imprimante : listImprimante){
    		if(isEmpty(imprimante)){
    			continue;
    		}
			for(int i=0; i<nbrTicket; i++){
        		startPrint(imprimante);
        	}
    	}
	}
	public static boolean isEmpty(Object valeur) {
		if ((valeur != null) && !"".equals(("" + valeur).trim()) && !"undefined".equals(("" + valeur).trim()) && !"null".equals(("" + valeur).trim())) {
			return false;
		}

		return true;
	}
	private static String[] getArrayFromStringDelim(String value, String separator) {
		if (isEmpty(value)) {
			return null;
		}

		String[] values;
		StringTokenizer stName = new StringTokenizer(value, separator);

		if (stName.countTokens() > 0) {
			values = new String[stName.countTokens()];
			int idx = 0;
			while (stName.hasMoreTokens()) {
				String val =  stName.nextToken();
				if(!isEmpty(val)){
					values[idx] = val;
					idx++;
				}
			}
			return values;
		}

		values = new String[2];
		values[0] = "";
		values[1] = "";

		return values;
	}
	
	/**
	 * @param imprimante
	 */
	private void startPrint(String imprimante) {
		new Thread(() -> {
			try {
				PrinterJob pj = PrinterJob.getPrinterJob();
				//
				if (imprimante != null && isNotEmpty(imprimante)) {
					try {
						AttributeSet attrSet = new HashPrintServiceAttributeSet(new PrinterName(imprimante, null));
						final PrintService[] lookupPrintServices = PrintServiceLookup.lookupPrintServices(null,
								attrSet);
						if (lookupPrintServices == null || lookupPrintServices.length == 0) {
							return;
						}

						pj.setPrintService(lookupPrintServices[0]);
					} catch (PrinterException ex) {
						Logger.getLogger(PrintCommunUtil.class.getName()).log(Level.SEVERE, null, ex);
					}
				}

				pj.setPrintable(new MyPrintable(), getPageFormat(pj));

				pj.print();
			} catch (PrinterException ex) {
				Logger.getLogger(PrintCommunUtil.class.getName()).log(Level.SEVERE, null, ex);
			}
		}).start();
	}

	public PageFormat getPageFormat(PrinterJob pj) {
		PageFormat pf = pj.defaultPage();
        Paper paper = pf.getPaper();

        BigDecimal widthTicket = printBean.getTicketWidth();
        widthTicket = ((widthTicket == null || widthTicket.compareTo(new BigDecimal(0))==0) ? new BigDecimal(8) : widthTicket);
        
        BigDecimal heightTicket = printBean.getTicketHeight();
        double middleHeight = heightTicket.doubleValue(); // paper.getHeight(); 
        // with the row count of
        // jtable
        
        double headerHeight = 0;
        double footerHeight = 0;
        
        if(printBean.getTicketWidth() == null){
        	headerHeight = 1.0; // fixed----->// Marge de l'entête
        	footerHeight = 20.0; // fixed----->// Marge du pied de page
        }
        
        double width = convert_CM_To_PPI(widthTicket.doubleValue()); // printer know only point per
        // inch.default value is 72ppi
        double height = convert_CM_To_PPI(headerHeight + middleHeight + footerHeight);
        paper.setSize(width, height);
        
        if(printBean.getTicketWidth() == null){
        	paper.setImageableArea(convert_CM_To_PPI(0.25), convert_CM_To_PPI(0.5), width - convert_CM_To_PPI(0.35),
                    height - convert_CM_To_PPI(1)); // define boarder size after
        } else{
        	paper.setImageableArea(0, 0, width, height); // define boarder size after
        }
        
        // that print area width is
        // about 180 points
        int pageFormat = PageFormat.PORTRAIT;
        if(isNotEmpty(printBean.getOrientation())){
        	if("L".equals(printBean.getOrientation())){
        		pageFormat = 0;
        	} else if("LR".equals(printBean.getOrientation())){
        		pageFormat = 2;
        	} else{
        		pageFormat = 1;
        	}
        }
        
        pf.setOrientation(pageFormat); // select orientation portrait
        // or landscape but for this
        // time portrait
        pf.setPaper(paper);

        return pf;
	}

	public static double convert_CM_To_PPI(double cm) {
		return toPPI(cm * 0.393600787);
	}

	public static double toPPI(double inch) {
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
//            int MAX_STR_LENGTH = printBean.getMaxLineLength();
            
            Stroke sttrokOrigin = g2d.getStroke();
            
            for(PrintPosDetailBean printPosBean : printBean.getListDetail()){
            	g2d.setStroke(sttrokOrigin);
            	
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
// 					int nbrLigne = Math.abs(text.length()/MAX_STR_LENGTH)+1 ;
//                     if(nbrLigne > 1){
//                     	for(int j=0; j<nbrLigne; j++){ 
//                     		int endLine = (j*MAX_STR_LENGTH)+MAX_STR_LENGTH > text.length() ? text.length() : (j*MAX_STR_LENGTH)+MAX_STR_LENGTH;
//                     		printPosBean.setData(text.substring(j*MAX_STR_LENGTH, endLine));
//                     		//
//                     		g2d.drawString(""+printPosBean.getData(), x+(j==0?0:2), printPosBean.getY());
//                     		printPosBean.setY(printPosBean.getY() + 10);
//                     	}
//                     } else{
                     	if(printPosBean.isBackground()) {
                     		addTextBackground(g2d, ""+printPosBean.getData(), x, printPosBean.getY());
                     	} else {
                     		g2d.drawString(""+printPosBean.getData(), x, printPosBean.getY());
                     	}
//                     }
                     //-------------------------------------------------------------------------------------------
            	} else if(printPosBean.getType().equals("I")){// Image
					try {
						BufferedImage read = ImageIO.read((File)printPosBean.getData());
						g2d.drawImage(read, printPosBean.getX(), printPosBean.getY(), printPosBean.getWidth(), printPosBean.getHeight(), null); // draw
					} catch (IOException e) {
						e.printStackTrace();
					}
            	} else if(printPosBean.getType().equals("LP")){// Ligne pointillée
	            		 float[] dash1 = { 2f };
	            		 BasicStroke bs1 = new BasicStroke(1, 
	            			        BasicStroke.CAP_BUTT, 
	            			        BasicStroke.JOIN_ROUND, 
	            			        1.0f, 
	            			        dash1,
	            			        2f);
	            		g2d.setStroke(bs1);
	                    g2d.drawLine(printPosBean.getX(), printPosBean.getY(), printPosBean.getWidth(), printPosBean.getHeight());
	            } else{// Séparateur
	            	g2d.drawLine(printPosBean.getX(), printPosBean.getY(), printPosBean.getWidth(), printPosBean.getHeight());
	            }
            }
            
			return PAGE_EXISTS;
		}
		
        private void addTextBackground(Graphics2D g2d, String text, int x, int y) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            AttributedString as1 = new AttributedString(text);
            as1.addAttribute(TextAttribute.BACKGROUND, Color.LIGHT_GRAY, 0, text.length());
//            if(isBarre) {
//            	as1.addAttribute(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
//        	}
            g2d.drawString(as1.getIterator(), x, y);
        }
        private int getX_centre(Graphics2D g2d, Font font, String text) {
            FontMetrics metrics = g2d.getFontMetrics(font);
            // Determine the X coordinate for the text
            int x = ((PAPER_WIDTH - metrics.stringWidth(text)) / 2) + 15;
            return x;
        }
	}
	
	/**
	 * @param valeur
	 * @return
	 */
	public static boolean isNotEmpty(Object valeur) {
		return !isEmpty(valeur);
	}
	
	 /**
     * Ouvrir le terroire uniquement
     */
     public static void openCashDrawer(String imprimantes) {
    	 String[] listImprimante = getArrayFromStringDelim(imprimantes, "|");
    	 if(listImprimante == null || listImprimante.length == 0) {
     		return;
     	 }
         String defaultImprimante = listImprimante[0];
         
        try {
            ByteArrayOutputStream commandSet = new ByteArrayOutputStream();
            final byte[] openCD = {27, 112, 0, 60, 120};
            commandSet.write(openCD);
            DocPrintJob job = null;
            // Imprimante par défaut
            try{
                AttributeSet attrSet = new HashPrintServiceAttributeSet(new PrinterName(defaultImprimante, null));
                PrintService[] lookupPrintServices = PrintServiceLookup.lookupPrintServices(null, attrSet);
                
                if(lookupPrintServices.length == 0){
                	return;
                }
				job = lookupPrintServices[0].createPrintJob();
            } catch (Exception ex) {
                Logger.getLogger(PrintCommunUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
            //
            if(job == null){
                job = PrintServiceLookup.lookupDefaultPrintService().createPrintJob();
            }
            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            Doc doc = new SimpleDoc(commandSet.toByteArray(), flavor, null);

            job.print(doc, null);
        } catch (PrintException | IOException ex) {
            Logger.getLogger(PrintCommunUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }     
}
