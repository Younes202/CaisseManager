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

import appli.model.domaine.stock.persistant.ArticlePersistant;
import framework.controller.ContextGloabalAppli;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.NumericUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.printGen.PrintCommunUtil;
import framework.model.util.printGen.PrintPosBean;
import framework.model.util.printGen.PrintPosDetailBean;

public class PrintCodeBarreBalanceUtil {
	private String codeBarre;
	private ArticlePersistant article;
	private BigDecimal width;
	private BigDecimal height;
	private BigDecimal bar_width;
	private BigDecimal bar_height; 
	private String bigFontParam;
	private String smallFontParam;
	private String orientation;
	private Integer posX = 0;
	private Integer posY = 10;
	private BigDecimal poids;
	private PrintPosBean printBean;
	
	public PrintCodeBarreBalanceUtil(ArticlePersistant article, BigDecimal poids) {
		this.printBean = new PrintPosBean();
		
		this.printBean.getTicketHeight();
		this.printBean.getTicketWidth();
		this.codeBarre = article.getCode_barre();
		this.article = article;
		this.poids = poids;
		
		List<PrintPosDetailBean> listLines = buildMapData();
		this.printBean.setListDetail(listLines);
	}
	
	/**
	 * @param type
	 * @return
	 */
	private Configuration buildCfg(String type) {
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
		String imprimante = ContextGloabalAppli.getGlobalConfig("ETIQUETTE_BALANCE_PRINT");
		this.printBean.setPrinters(imprimante);
		
		this.width = BigDecimalUtil.get(ContextGloabalAppli.getGlobalConfig("ETIQUETTE_BALANCE_WIDTH"));
		this.height = BigDecimalUtil.get(ContextGloabalAppli.getGlobalConfig("ETIQUETTE_BALANCE_HEIGHT"));
		this.orientation = ContextGloabalAppli.getGlobalConfig("ETIQUETTE_BALANCE_ORIENTATION");
		String x = ContextGloabalAppli.getGlobalConfig("ETIQUETTE_BALANCE_X");
		this.posX = StringUtil.isNotEmpty(x)?Integer.valueOf(x) : posX;
		String y = ContextGloabalAppli.getGlobalConfig("ETIQUETTE_BALANCE_Y");
		this.posY = StringUtil.isNotEmpty(y)?Integer.valueOf(y) : posY;
		this.bigFontParam = ContextGloabalAppli.getGlobalConfig("ETIQUETTE_BALANCE_BIG_TXT");
		this.smallFontParam = ContextGloabalAppli.getGlobalConfig("ETIQUETTE_BALANCE_XS_TXT");

		this.bar_width = BigDecimalUtil.get(ContextGloabalAppli.getGlobalConfig("ETIQUETTE_BARRE_BAR_WIDTH"));
		this.bar_height = BigDecimalUtil.get(ContextGloabalAppli.getGlobalConfig("ETIQUETTE_BARRE_BAR_HEIGHT"));
		
		this.printBean.setPosX(this.posX);
		this.printBean.setPosY(this.posY);
		this.printBean.setTicketWidth(this.width);
		this.printBean.setTicketHeight(this.height);
		this.printBean.setOrientation(this.orientation);
		
		String nbrTicketStr = ContextGloabalAppli.getGlobalConfig("ETIQUETTE_BARRE_NBR");
		int nbrTicket = StringUtil.isEmpty(nbrTicketStr) ? 1 : Integer.valueOf(nbrTicketStr);
		this.printBean.setNbrTicket(nbrTicket);
		
		BigDecimal widthB = this.width!=null ? this.width : BigDecimalUtil.get("8");
		BigDecimal heightB = this.height!=null ? this.height : BigDecimalUtil.get("3.5");
        double width = PrintCommunUtil.toPPI(widthB.doubleValue());
        double height = PrintCommunUtil.toPPI(heightB.doubleValue());

        List<PrintPosDetailBean> listPrintLines = new ArrayList<>();
        
    	if(StringUtil.isEmpty(imprimante)) {
    		return listPrintLines;
    	}
    	
    	if(StringUtil.isEmpty(this.codeBarre)) {
    		return listPrintLines;
    	}
    	
       	String path = StrimUtil._GET_PATH("temp/balance")+"/"+codeBarre+".jpg";
       	File fileImg = new File(path);
       	listPrintLines.add(new PrintPosDetailBean(fileImg, 7, 5,Double.valueOf(width- 1).intValue(), Double.valueOf(height - 1).intValue(), "C"));
        
        return listPrintLines;
	}

	public PrintPosBean getPrintPosBean(){
		return this.printBean;
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
	
	
	public void generateAndPrintCodeBarreEan13() {

    	String debutCodeBarre = ContextGloabalAppli.getGlobalConfig("CODE_BARRE_BALANCE");
    	//
    	if(StringUtil.isEmpty(debutCodeBarre)){
    		debutCodeBarre = "22";
    	}
    	
    	String mode = ContextGloabalAppli.getGlobalConfig("CODE_BARRE_BALANCE_COMPO");
    	String[] composition = getInfosCdeBarreBalance(mode, this.codeBarre);
    	String prefix = composition[0];
    	String barre = composition[1];
    	String pds = composition[2];
    	String suffix = composition[3];
    	
    	String codeStr = this.codeBarre;
		codeStr = debutCodeBarre.substring(0, prefix.length())+this.codeBarre.substring(0, barre.length())+(""+this.poids.intValue()).substring(0, pds.length());
		if(suffix.length() > 0){
			int codeCtrl = PrintCodeBarreBalanceUtil.CHECK_SUM(codeStr)-2;
			codeStr = codeStr + (""+codeCtrl).substring(0, suffix.length());
		}
		
    	// Initialiser les variables
    	this.width = (this.width==null) ? BigDecimalUtil.get(7) : this.width;
		this.height = (this.height==null) ? BigDecimalUtil.get(3) : this.height;
    	
		int bigFontStr = StringUtil.isNotEmpty(bigFontParam) && NumericUtil.isNum(bigFontParam) ? Integer.valueOf(bigFontParam) : 11;
		int xsFontStr = StringUtil.isNotEmpty(smallFontParam) && NumericUtil.isNum(smallFontParam) ? Integer.valueOf(smallFontParam) : 7;
		int x = (posX == null) ? 5 : posX;
        int y = (posY == null) ? 10 : posX;
        
		String path = StrimUtil._GET_PATH("temp/balance");
		
		Font bigFont = new Font("Roman", Font.BOLD, bigFontStr);
		Font smallFont = new Font("Roman", Font.PLAIN, xsFontStr);
		
			try {
				if(!new File(path).exists()){
					new File(path).mkdirs();
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
	        	String pathImg = StrimUtil._GET_PATH("/temp/balance")+"/"+codeBarre+".jpg";
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
                
                int yImg = this.height.intValue()+10;
				if(this.poids == null){
                	g.drawString(this.article.getLibelle()+" | "+BigDecimalUtil.formatNumber(this.article.getPrix_vente())+"Dhs", x, yImg);	
                } else{
                	String uniteVente = article.getOpc_unite_vente_enum().getCode();
                	g.drawString(this.article.getLibelle()+" | "+BigDecimalUtil.formatNumber(this.poids)+""+uniteVente, x, yImg);
                }
                g.dispose();

                ImageIO.write(ALTERED, "jpg", new File(pathImg));
                
                // Impression
               this.printBean.getListDetail().add(new PrintPosDetailBean(new File(pathImg), this.posX, this.posY, bar_width.intValue(), bar_height.intValue(), "C"));
	        	
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		
	}
	
    public static String[] getInfosCdeBarreBalance(String mode, String codeBarre) {
		if(StringUtil.isEmpty(codeBarre)){
			return null;
		}
		if(StringUtil.isEmpty(mode)){
			mode = "PR2;CB5;PD5;SU1";
		}
		
		String[] data = StringUtil.getArrayFromStringDelim(mode, ";");
		String prefix = null;
		String poids = null;
		String codeB = null;
		String suffix = null;
		//
		if(data != null){
			int idx = 0;
			for (String det : data) {
				if(StringUtil.isEmpty(det) || det.length() <= 2){
					continue;
				}
				int nbr = Integer.valueOf(det.substring(2));
				String val = codeBarre.substring(idx, idx+nbr);
				if(det.startsWith("PR")){
					prefix = val;
				} else if(det.startsWith("CB")){
					codeB = val;
				} else if(det.startsWith("PD")){
					poids = val;
				} else if(det.startsWith("SU")){
					suffix = val;
				}
				idx += val.length();
			}
		}
		
		return new String[]{prefix, codeB, poids, suffix};
	}
}
