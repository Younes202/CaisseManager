package appli.model.domaine.util_srv.printCom.ticket;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import framework.controller.ContextGloabalAppli;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.NumericUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.FileUtil;
import framework.model.util.printGen.PrintCommunUtil;
import framework.model.util.printGen.PrintPosBean;
import framework.model.util.printGen.PrintPosDetailBean;

public class PrintEtiquetteCuisineUtil {
	private BigDecimal width;
	private BigDecimal height; 
	private String bigFontParam;
	private String smallFontParam;
	private String imprimante;
	private String refCommande;
	private String orientation;
	private Map<String, byte[]> mapFilesLogo = null;
	private EtablissementPersistant restaurantP;
	private int posX = 0;
	private int posY = 10;
	List<CaisseMouvementArticlePersistant> listToPrint;
	private PrintPosBean printBean;
	
	public PrintEtiquetteCuisineUtil(Long caisseId, CaisseMouvementPersistant cmP) {
		this.restaurantP = ContextAppli.getEtablissementBean();
    	this.imprimante = ContextGloabalAppli.getGlobalConfig(caisseId, "ETIQUETTE_PRINT");
		this.width = BigDecimalUtil.get(ContextGloabalAppli.getGlobalConfig(caisseId, "ETIQUETTE_WIDTH"));
		this.height = BigDecimalUtil.get(ContextGloabalAppli.getGlobalConfig(caisseId, "ETIQUETTE_HEIGHT"));
		this.orientation = ContextGloabalAppli.getGlobalConfig(caisseId, "ETIQUETTE_ORIENTATION");
		this.posX = StringUtil.isNotEmpty(ContextGloabalAppli.getGlobalConfig(caisseId, "ETIQUETTE_X"))?Integer.valueOf(ContextGloabalAppli.getGlobalConfig("ETIQUETTE_X")) : posX;
		this.posY = StringUtil.isNotEmpty(ContextGloabalAppli.getGlobalConfig(caisseId, "ETIQUETTE_Y"))?Integer.valueOf(ContextGloabalAppli.getGlobalConfig("ETIQUETTE_Y")) : posY;
		this.bigFontParam = ContextGloabalAppli.getGlobalConfig(caisseId, "ETIQUETTE_BIG_TXT");
		this.smallFontParam = ContextGloabalAppli.getGlobalConfig(caisseId, "ETIQUETTE_XS_TXT");
		this.refCommande = cmP.getRef_commande();
		this.listToPrint = cmP.getList_article();

		Long restauId = restaurantP.getId();
		String startChemin = restaurantP.getId()+"/"+"restau/"+restauId;
		this.mapFilesLogo = FileUtil.getListFilesByte(startChemin);  
		
		this.printBean = new PrintPosBean();
        this.printBean.setOrientation(this.orientation);
        this.printBean.setTicketWidth(this.width);
        this.printBean.setTicketHeight(this.height);
        this.printBean.setPosX(posX);
        this.printBean.setPosY(posY);
        this.printBean.setPrinters(this.imprimante);
        
        String nbrTicketStr = ContextGloabalAppli.getGlobalConfig(caisseId, "ETIQUETTE_NBR");
		int nbrTicket = StringUtil.isEmpty(nbrTicketStr) ? 1 : Integer.valueOf(nbrTicketStr);
		this.printBean.setNbrTicket(nbrTicket);
        
		List<PrintPosDetailBean> listDataToPrint = buildMapData(cmP);
    	this.printBean.setListDetail(listDataToPrint);
	}
	
	public PrintPosBean getPrintPosBean(){
    	return this.printBean;
    }
	
	public List<PrintPosDetailBean> buildMapData(CaisseMouvementPersistant cmP) {
		List<PrintPosDetailBean> listPrintLinrs = new ArrayList<>();
			int y = posY;
			int x = posX;
	    	
	   	 // Logo image --------------------------------------------------------------
			if(mapFilesLogo != null && mapFilesLogo.size() > 0){
				try {
					Long restauId = restaurantP.getId();
					String startChemin = restaurantP.getId()+"/"+"restau/"+restauId;
					File file = new File(StrimUtil.BASE_FILES_PATH+"/"+startChemin+"/"+mapFilesLogo.keySet().iterator().next());
					BufferedImage read = ImageIO.read(file);
					int imagewidth = read.getWidth();
					int imageheight = read.getHeight();
					// Max 200
					if(imagewidth > 50){
						Dimension imgSize = new Dimension(read.getWidth(), read.getHeight());
						Dimension boundary = new Dimension(50, 50);
						
						Dimension ratioSize = PrintCommunUtil.getScaledDimension(imgSize, boundary);
						
						imagewidth = ratioSize.width;
						imageheight = ratioSize.height;
					}
					int xImg = posX/3;
					//
					listPrintLinrs.add(new PrintPosDetailBean(file, xImg, y, imagewidth, imageheight, "C"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	   	
			int bigFontStr = StringUtil.isNotEmpty(bigFontParam) && NumericUtil.isNum(bigFontParam) ? Integer.valueOf(bigFontParam) : 11;
			int xsFontStr = StringUtil.isNotEmpty(smallFontParam) && NumericUtil.isNum(smallFontParam) ? Integer.valueOf(smallFontParam) : 7;
			
			Font bigFont = new Font("Roman", Font.BOLD, bigFontStr);
			Font smallFont = new Font("Roman", Font.PLAIN, xsFontStr);

			listPrintLinrs.add(new PrintPosDetailBean((refCommande.substring(12).length()>12?refCommande.substring(12):refCommande.substring(12)), posX/3, y-5, smallFont));
	
			
	       //
	   	for (CaisseMouvementArticlePersistant detP : listToPrint) {
	   		Font font = null;
	   		if(BooleanUtil.isTrue(detP.getIs_menu())){
	   			font = bigFont;
	   		} else{
	   			font = smallFont;
	   		}

           listPrintLinrs.add(new PrintPosDetailBean(detP.getLibelle().toUpperCase(), x, y, font, "C"));
           y = y + 10;
	   	}
	   	
	   	return listPrintLinrs;
	}
	
	protected static double fromCMToPPI(double cm) {            
        return toPPI(cm * 0.393700787);            
    }

    protected static double toPPI(double inch) {            
        return inch * 72d;            
    }
}
