package appli.model.domaine.util_srv.printCom.ticket;

import java.awt.Font;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import appli.model.domaine.stock.persistant.ArticlePersistant;
import framework.controller.ContextGloabalAppli;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.NumericUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.printGen.PrintPosBean;
import framework.model.util.printGen.PrintPosDetailBean;

public class PrintEtiquetteUtil {
	private BigDecimal width;
	private BigDecimal height; 
	private String bigFontParam;
	private String smallFontParam;
	private String imprimante;
	private String orientation;
	private int posX = 0;
	private int posY = 10;
	private int maxCarac = 10;
	private List<PrintPosBean> listPrintBean;
	
	public PrintEtiquetteUtil(List<ArticlePersistant> listArticle) {
		this.listPrintBean = new ArrayList<>();
		
		this.imprimante = ContextGloabalAppli.getGlobalConfig("ETIQUETTE_PRIX_PRINT");
		this.width = BigDecimalUtil.get(ContextGloabalAppli.getGlobalConfig("ETIQUETTE_PRIX_WIDTH"));
		this.height = BigDecimalUtil.get(ContextGloabalAppli.getGlobalConfig("ETIQUETTE_PRIX_HEIGHT"));
		this.orientation = ContextGloabalAppli.getGlobalConfig("ETIQUETTE_PRIX_ORIENTATION");
		String x = ContextGloabalAppli.getGlobalConfig("ETIQUETTE_PRIX_X");
		this.posX = StringUtil.isNotEmpty(x)?Integer.valueOf(x) : posX;
		String y = ContextGloabalAppli.getGlobalConfig("ETIQUETTE_PRIX_Y");
		this.posY = StringUtil.isNotEmpty(y)?Integer.valueOf(y) : posY;
		
		this.bigFontParam = ContextGloabalAppli.getGlobalConfig("ETIQUETTE_PRIX_BIG_TXT");
		this.smallFontParam = ContextGloabalAppli.getGlobalConfig("ETIQUETTE_PRIX_XS_TXT");
		
		String maxCarac = ContextGloabalAppli.getGlobalConfig("ETIQUETTE_PRIX_MAX_CARAC");
		if(StringUtil.isNotEmpty(maxCarac)) {
			this.maxCarac = BigDecimalUtil.get(maxCarac).intValue();
		}
		
		String nbrTicketStr = ContextGloabalAppli.getGlobalConfig("ETIQUETTE_PRIX_NBR");
		int nbrTicket = StringUtil.isEmpty(nbrTicketStr) ? 1 : Integer.valueOf(nbrTicketStr);
		
    	//
    	for(ArticlePersistant artP : listArticle){
			 List<PrintPosDetailBean> list = buildMapData(artP);
			 PrintPosBean printBean = new PrintPosBean();
		     printBean.setOrientation(this.orientation);
		     printBean.setTicketWidth(this.width);
		     printBean.setTicketHeight(this.height);
		     printBean.setPosX(posX);
		     printBean.setPosY(posY);  
			 printBean.setListDetail(list);
			 printBean.setPrinters(this.imprimante);
			 printBean.setNbrTicket(nbrTicket);
			 
			 // 
			 this.listPrintBean.add(printBean);
    	}
	}
	
	public List<PrintPosBean> getPrintPosBean(){
    	return this.listPrintBean;
    }
	
	public List<PrintPosDetailBean> buildMapData(ArticlePersistant articleP) {
		List<PrintPosDetailBean> listPrintLinrs = new ArrayList<>();
		int y = posY;
    	
		int bigFontStr = StringUtil.isNotEmpty(bigFontParam) && NumericUtil.isNum(bigFontParam) ? Integer.valueOf(bigFontParam) : 11;
		int xsFontStr = StringUtil.isNotEmpty(smallFontParam) && NumericUtil.isNum(smallFontParam) ? Integer.valueOf(smallFontParam) : 7;
		
		Font bigFont = new Font("Roman", Font.BOLD, bigFontStr);
		Font bigFontP = new Font("Roman", Font.BOLD, bigFontStr+2);
		Font smallFont = new Font("Roman", Font.PLAIN, xsFontStr);

		//
		String libelle = articleP.getLibelle().toUpperCase();
    	int nbrLigne = Math.abs(libelle.length()/this.maxCarac)+1 ;
        if(nbrLigne > 1){
        	for(int j=0; j<nbrLigne; j++){ 
        		if(j > 0){
        			y = y + 10;
        		}
        		int endLine = (j*this.maxCarac)+this.maxCarac > libelle.length() ? libelle.length() : (j*this.maxCarac)+this.maxCarac;
        		String txtTrun = libelle.substring(j*this.maxCarac, endLine);
        		//
        		listPrintLinrs.add(new PrintPosDetailBean(txtTrun, posX, y, bigFont, "C"));
        	}
        } else{
        	listPrintLinrs.add(new PrintPosDetailBean(libelle, posX, y, bigFont, "C"));
        }
        y = y + 10;
        
        if(StringUtil.isNotEmpty(articleP.getCode_barre())){
            listPrintLinrs.add(new PrintPosDetailBean(articleP.getCode_barre(), posX, y, smallFont, "C"));
            y = y + 20;
        }
        
        String prix = BigDecimalUtil.formatNumber(articleP.getPrix_vente());
        if(articleP.getOpc_unite_vente_enum() != null){
        	prix = prix+" Dhs / "+StringUtil.getValueOrEmpty(articleP.getOpc_unite_vente_enum().getLibelle());	
        } else{
        	prix = prix+" Dhs";
        }
        
        listPrintLinrs.add(new PrintPosDetailBean(prix, posX, y, bigFontP, "C"));
        
        return listPrintLinrs;
	}
}
