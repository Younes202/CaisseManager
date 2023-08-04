package appli.model.domaine.util_srv.printCom.ticket;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfiguration;
import org.krysalis.barcode4j.BarcodeGenerator;
import org.krysalis.barcode4j.BarcodeUtil;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import framework.controller.ContextGloabalAppli;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.NumericUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.printGen.PrintPosBean;
import framework.model.util.printGen.PrintPosDetailBean;

public class PrintCodeBarreUtil {
	private String codeBarre;
	private ArticlePersistant article;
	private BigDecimal width;
	private BigDecimal height; 
	private BigDecimal image_width;
	private BigDecimal image_height; 
	private String bigFontParam;
	private String smallFontParam;
	private String imprimante;
	private String orientation;
	private Integer posX = 0;
	private Integer posY = 10;
	private PrintPosBean printBean;
	
	public PrintCodeBarreUtil(String codeBarre, ArticlePersistant article) {
		this.printBean = new PrintPosBean();
		
		this.printBean.getTicketHeight();
		this.printBean.getTicketWidth();
		this.codeBarre = codeBarre;
		this.article = article;
		
		List<PrintPosDetailBean> listLines = buildMapData();
		this.printBean.setListDetail(listLines);
	}
	
	public PrintPosBean getPrintPosBean(){
		return this.printBean;
	}
	
	/**
	 * @param type
	 * @return
	 */
	public static Configuration buildCfg(String type) {
	    DefaultConfiguration cfg = new DefaultConfiguration("barcode");

	    //Bar code type
	    DefaultConfiguration child = new DefaultConfiguration(type);
	      cfg.addChild(child);
	    
	      //Human readable text position
	      DefaultConfiguration attr = new DefaultConfiguration("human-readable");
	      DefaultConfiguration subAttr = new DefaultConfiguration("placement");
	        subAttr.setValue("bottom");
	        attr.addChild(subAttr);
	        
	        child.addChild(attr);
	        
	        return cfg;
	  }
	
	private List<PrintPosDetailBean> buildMapData(){ 
		this.imprimante = ContextGloabalAppli.getGlobalConfig("ETIQUETTE_BARRE_PRINT");
		this.width = BigDecimalUtil.get(ContextGloabalAppli.getGlobalConfig("ETIQUETTE_BARRE_WIDTH"));
		this.height = BigDecimalUtil.get(ContextGloabalAppli.getGlobalConfig("ETIQUETTE_BARRE_HEIGHT"));
		this.orientation = ContextGloabalAppli.getGlobalConfig("ETIQUETTE_BARRE_ORIENTATION");
		String x = ContextGloabalAppli.getGlobalConfig("ETIQUETTE_BARRE_X");
		this.posX = StringUtil.isNotEmpty(x)?BigDecimalUtil.get(x).intValue() : posX;
		String y = ContextGloabalAppli.getGlobalConfig("ETIQUETTE_BARRE_Y");
		this.posY = StringUtil.isNotEmpty(y)?BigDecimalUtil.get(y).intValue() : posY;
		this.bigFontParam = ContextGloabalAppli.getGlobalConfig("ETIQUETTE_BARRE_BIG_TXT");
		this.smallFontParam = ContextGloabalAppli.getGlobalConfig("ETIQUETTE_BARRE_XS_TXT");
		this.image_width = BigDecimalUtil.get(ContextGloabalAppli.getGlobalConfig("ETIQUETTE_BARRE_BAR_WIDTH"));
		this.image_height = BigDecimalUtil.get(ContextGloabalAppli.getGlobalConfig("ETIQUETTE_BARRE_BAR_HEIGHT"));

		this.printBean.setPosX(this.posX);
		this.printBean.setPosY(this.posY);
		this.printBean.setTicketWidth(this.width);
		this.printBean.setTicketHeight(this.height);
		this.printBean.setOrientation(this.orientation);
		this.printBean.setPrinters(this.imprimante);
		
		String nbrTicketStr = ContextGloabalAppli.getGlobalConfig("ETIQUETTE_BARRE_NBR");
		int nbrTicket = StringUtil.isEmpty(nbrTicketStr) ? 1 : Integer.valueOf(nbrTicketStr);
		this.printBean.setNbrTicket(nbrTicket);
		
//		BigDecimal widthB = this.width!=null ? this.width : BigDecimalUtil.get("8");
//		BigDecimal heightB = this.height!=null ? this.height : BigDecimalUtil.get("3.5");
//        double width = fromCMToPPI(widthB.doubleValue());
//        double height = fromCMToPPI(heightB.doubleValue());
//
//        int bigFontStr = StringUtil.isNotEmpty(bigFontParam) && NumericUtil.isNum(bigFontParam) ? Integer.valueOf(bigFontParam) : 11;
//		int xsFontStr = StringUtil.isNotEmpty(smallFontParam) && NumericUtil.isNum(smallFontParam) ? Integer.valueOf(smallFontParam) : 7;
		
        List<PrintPosDetailBean> listPrintLines = new ArrayList<>();
        
//       	String path = StrimUtil.PATH_RESOURCES+"/balance/"+this.codeBarre+".jpg";
//       	File fileImg = new File(path);
//       	listPrintLines.add(new PrintPosDetailBean(fileImg, this.posX, this.posY,Double.valueOf(width- 1).intValue(), Double.valueOf(height - 1).intValue(), "C"));
        
        return listPrintLines;
	}
	
	public static int CHECK_SUM (String Input) {
		int evens = 0; //initialize evens variable
		int odds = 0; //initialize odds variable
		int checkSum = 0; //initialize the checkSum
		for (int i = 0; i < Input.length(); i++) {
			//check if number is odd or even
			if ((int)Input.charAt(i) % 2 == 0) { // check that the character at position "i" is divisible by 2 which means it's even
				evens += (int)Input.charAt(i);// then add it to the evens
			} else {
				odds += (int)Input.charAt(i); // else add it to the odds
			}
		}
		odds = odds * 3; //multiply odds by three
		int total = odds + evens; //sum odds and evens
		if (total % 10 == 0){ //if total is divisible by ten, special case
			checkSum = 0;//checksum is zero
		} else { //total is not divisible by ten
			checkSum = 10 - (total % 10); //subtract the ones digit from 10 to find the checksum
		}
		return checkSum;
	}
	
	/**
	 * @param codeBarre
	 */
//	public void generateAndPrintCodeBarreEan137() {
//		//
//    	if(StringUtil.isEmpty(imprimante)) {
//    		return;
//    	}
//    	
//    	// Initialiser les variables
//    	this.width = (this.width==null) ? BigDecimalUtil.get(7) : this.width;
//		this.height = (this.height==null) ? BigDecimalUtil.get(3) : this.height;
//		this.orientation = ContextGloabalAppli.getGlobalConfig("ETIQUETTE_ORIENTATION");
//    	
//		int bigFontStr = StringUtil.isNotEmpty(bigFontParam) && NumericUtil.isNum(bigFontParam) ? Integer.valueOf(bigFontParam) : 11;
//		int xsFontStr = StringUtil.isNotEmpty(smallFontParam) && NumericUtil.isNum(smallFontParam) ? Integer.valueOf(smallFontParam) : 7;
//		int x = (posX == null) ? 5 : posX;
//        int y = (posY == null) ? 10 : posX;
//        
//		String path = StrimUtil.PATH_RESOURCES+"/balance";
//		
//		Font bigFont = new Font("Roman", Font.BOLD, bigFontStr);
//		Font smallFont = new Font("Roman", Font.PLAIN, xsFontStr);
//		
////		new Thread(() -> {
//			try {
//				if(!new File(path).exists()){
//					new File(path).mkdirs();
//				}
//				
//				String codeStr = this.codeBarre;
//				if(codeStr.length() < 10){
//					codeStr = "999"+this.codeBarre;
//					int codeCtrl = PrintCodeBarreUtil.CHECK_SUM(codeStr)-2;
//					codeStr = codeStr + codeCtrl;
//				}
//				
//				BarcodeUtil util = BarcodeUtil.getInstance();
//			    BarcodeGenerator gen = util.createBarcodeGenerator(buildCfg("ean-13"));
//	
//			    OutputStream fout = new FileOutputStream(path + "/"+this.codeBarre+".jpg");
//			    int resolution = 400;
//			    BitmapCanvasProvider canvas = new BitmapCanvasProvider(
//			        fout, "image/jpeg", resolution, BufferedImage.TYPE_BYTE_BINARY, false, 0);
//			    
//			    gen.generateBarcode(canvas, codeStr);
//			    canvas.finish();
//			    fout.close();//Important
//			    
//	        	// Ajouter du text à l'image
//	        	String pathImg = StrimUtil.PATH_RESOURCES+"/balance/"+codeBarre+".jpg";
//              	BufferedImage ORIGINAL = ImageIO.read(new File(pathImg));
//              	
//              	// Resize ----------------------------------------------------------------------------------
//              	GraphicsConfiguration config = 
//              	        GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
//              	
//              	BufferedImage ALTERED = config.createCompatibleImage(
//                        ORIGINAL.getWidth(), 
//                        ORIGINAL.getHeight() + 20);
//              	// --------------------------------------------------------------------------------
//              	
//              	Graphics g = ALTERED.createGraphics();
//              	// Ajouter un espace de 20 px en haut de l'image
//              	g.setColor(Color.WHITE);
//              	g.fillRect(0, 0, ALTERED.getWidth(), 20);
//              	g.drawImage(ORIGINAL, 0, 20, null);
//              	//-----------------------------------------------------
//              	
//              	g.setColor(Color.BLACK);
//                g.setFont(bigFont);
//                
//                int yImg = this.height.intValue()+10;
//                if(this.poids == null){
//                	g.drawString(this.article.getLibelle()+" | "+BigDecimalUtil.formatNumber(this.article.getPrix_vente())+"Dhs", x, yImg);	
//                } else{
//                	String uniteVente = article.getOpc_unite_vente_enum().getCode();
//                	g.drawString(this.article.getLibelle()+" | "+BigDecimalUtil.formatNumber(this.poids)+""+uniteVente, x, yImg);
//                }
//                g.dispose();
//
//                ImageIO.write(ALTERED, "jpg", new File(pathImg));
//	        	
//			} catch (Exception ex) {
//				throw new RuntimeException(ex);
//			}
////	    }).start();
//	}
	
	
	public String generateAndPrintCodeBarreEan13() { 
		//
    	if(StringUtil.isEmpty(imprimante)) {
    		return null;
    	}
    	
    	if(StringUtil.isEmpty(this.codeBarre)) {
    		return null;
    	}
    	
    	// Initialiser les variables
    	this.width = (this.width==null) ? BigDecimalUtil.get(7) : this.width;
		this.height = (this.height==null) ? BigDecimalUtil.get(3) : this.height;
    	
		int bigFontStr = StringUtil.isNotEmpty(bigFontParam) && NumericUtil.isNum(bigFontParam) ? Integer.valueOf(bigFontParam) : 11;
		int xsFontStr = StringUtil.isNotEmpty(smallFontParam) && NumericUtil.isNum(smallFontParam) ? Integer.valueOf(smallFontParam) : 7;
		int x = (posX == null) ? 5 : posX;
        int y = (posY == null) ? 10 : posY;
        
        String startCheminLogo = "restau/"+ContextAppli.getEtablissementBean().getId();
		String path = StrimUtil._GET_PATH("temp/balance");
		
		Font bigFont = new Font("Roman", Font.BOLD, bigFontStr);
		Font smallFont = new Font("Roman", Font.PLAIN, xsFontStr);
		
//		new Thread(() -> {
			try {
				if(!new File(path).exists()){
					new File(path).mkdirs();
				}
				
				String codeStr = this.codeBarre;
				if(codeStr.length() < 10){
					codeStr = "999"+this.codeBarre;
					int codeCtrl = PrintCodeBarreUtil.CHECK_SUM(codeStr)-2;
					codeStr = codeStr + codeCtrl;
				}
				
				BarcodeUtil util = BarcodeUtil.getInstance();
			    BarcodeGenerator gen = util.createBarcodeGenerator(buildCfg("ean-13"));
	
			    OutputStream fout = new FileOutputStream(path + "/"+this.codeBarre+".jpg");
			    int resolution = 400;
			    BitmapCanvasProvider canvas = new BitmapCanvasProvider(
			        fout, "image/jpeg", resolution, BufferedImage.TYPE_BYTE_BINARY, false, 0);
			    
			    gen.generateBarcode(canvas, codeStr);
			    canvas.finish();
			    fout.close();//Important
			    
	        	// Ajouter du text à l'image
	        	String pathImg = StrimUtil._GET_PATH("temp/balance")+"/"+this.codeBarre+".jpg";
              	BufferedImage ORIGINAL = ImageIO.read(new File(pathImg));
              	
              	// Resize ----------------------------------------------------------------------------------
              	GraphicsConfiguration config = 
              	        GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
              	
              	BufferedImage ALTERED = config.createCompatibleImage(
                        ORIGINAL.getWidth(), 
                        ORIGINAL.getHeight() + 20);
              	// --------------------------------------------------------------------------------
              	
              	Graphics g = ALTERED.createGraphics();
              	// Ajouter un espace de 20 px en haut de l'image
              	g.setColor(Color.WHITE);
              	g.fillRect(0, 0, ALTERED.getWidth(), 20);
              	g.drawImage(ORIGINAL, 0, 20, null);
              	//-----------------------------------------------------
              	
              	g.setColor(Color.BLACK);
                g.setFont(bigFont);
                
                int yImg = 20;//this.height.intValue()+10;
                g.drawString(this.article.getLibelle()+" | "+BigDecimalUtil.formatNumber(this.article.getPrix_vente())+"Dhs", x, yImg);	
                g.dispose();

                ImageIO.write(ALTERED, "jpg", new File(pathImg));
                
                int widthBar = image_width == null ? 120 : image_width.intValue();
        		int heightBar = image_height == null ? 60 : image_height.intValue();
        		
                PrintPosDetailBean ppDB = new PrintPosDetailBean(new File(pathImg), this.posX, this.posY,widthBar, heightBar, "C");
                this.printBean.getListDetail().add(ppDB);
			} catch (Exception ex) {
				System.out.print("====>"+ex.getMessage());
//				ex.printStackTrace();
				return "Le code barre *"+StringUtil.getValueOrEmpty(article.getCode_barre())+"* n'est pas conforme à la norme";
			}
			return null;
	}
	
	protected static double fromCMToPPI(double cm) {            
        return toPPI(cm * 0.393700787);            
    }

    protected static double toPPI(double inch) {            
        return inch * 72d;            
    }
}
