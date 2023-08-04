/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appli.model.domaine.util_srv.printCom.ticket;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.PARAM_APPLI_ENUM;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import appli.model.domaine.vente.persistant.CaissePersistant;
import appli.model.domaine.vente.persistant.TicketCaisseConfDetailPersistant;
import appli.model.domaine.vente.persistant.TicketCaisseConfPersistant;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.printGen.PrintPosBean;
import framework.model.util.printGen.PrintPosDetailBean;

public class PrintTicketCaisseCustomUtil {
	private CaisseMouvementPersistant caisseMouvement;
	private List<TicketCaisseConfPersistant> listTicketCaisseConf;
	private Integer clientIdx;
	List<PrintBean> listData = new ArrayList<>();
	private PrintPosBean printBean;
	
	public PrintTicketCaisseCustomUtil(List<TicketCaisseConfPersistant> listTicketCaisseConf, CaisseMouvementPersistant caisseMouvement, Integer clientIdx) {
		this.listTicketCaisseConf = listTicketCaisseConf;
		this.caisseMouvement = caisseMouvement;
		this.clientIdx = clientIdx;
		this.adjustData(listTicketCaisseConf, caisseMouvement);
		
		
		CaissePersistant opc_caisse = this.caisseMouvement.getOpc_caisse_journee().getOpc_caisse();
    	if(StringUtil.isEmpty(opc_caisse.getImprimente_special())) {
    		return;
    	}
    	this.printBean = new PrintPosBean();
        this.printBean.setOrientation("P");
        this.printBean.setTicketWidth(BigDecimalUtil.get(21));
        this.printBean.setTicketHeight(BigDecimalUtil.get(29));
//        this.printBean.setPosX(posX);
//        this.printBean.setPosY(posY);
        this.printBean.setPrinters(opc_caisse.getImprimente_special());
        
        this.printBean.setListDetail(new ArrayList<>());
        //
    	buildData();
	}

	public PrintPosBean getPrintPosBean(){
    	return this.printBean;
    }
	
	private void adjustData(List<TicketCaisseConfPersistant> listTicketCaisseConf, CaisseMouvementPersistant caisseMvmBean) {
		Map<String, String> mapConfig = (Map<String, String>) MessageService.getGlobalMap().get("GLOBAL_CONFIG");
		
		int globalY = 0;
		for(TicketCaisseConfPersistant ticketCaisseConf : listTicketCaisseConf) {
			for(TicketCaisseConfDetailPersistant confDetP : ticketCaisseConf.getList_detail()){
				if(StringUtil.isEmpty(confDetP.getCorrespondance())){
					continue;
				}
				
				if(!BigDecimalUtil.isZero(confDetP.getPosY())) {
					globalY = confDetP.getPosY().intValue();
				}
				
				int posX = confDetP.getPosX().intValue();
						
				String value = "";
				int stepY = ticketCaisseConf.getVertical_space();
				switch (confDetP.getCorrespondance()) {
					case "ref" : {value = caisseMvmBean.getRef_commande(); }; break;
					case "mtt_cmd" : {value = BigDecimalUtil.formatNumber(caisseMvmBean.getMtt_commande_net()); }; break;
					case "Type" : {value = caisseMvmBean.getType_commande() != null ? ContextAppli.getLibelleStatus(caisseMvmBean.getType_commande()) : "";}; break;
					case "nom_client" : {value = caisseMvmBean.getOpc_client() != null? StringUtil.getValueOrEmpty(caisseMvmBean.getOpc_client().getPrenom())+" "+caisseMvmBean.getOpc_client().getNom():null;}; break;
					case "address_client" : {value = caisseMvmBean.getOpc_client().getAdresse_compl();}; break;
					case "livreur" : {value = caisseMvmBean.getOpc_livreurU() != null? caisseMvmBean.getOpc_livreurU().getLogin() : null;}; break;
					case "serveur" : {value = caisseMvmBean.getOpc_serveur() != null? caisseMvmBean.getOpc_serveur().getLogin():null;}; break;
					case "caissier" : {
						if(caisseMvmBean.getOpc_employe() != null){
							value = StringUtil.getValueOrEmpty(caisseMvmBean.getOpc_employe().getPrenom())+" "+caisseMvmBean.getOpc_employe().getNom();
						}
					}; break;
					case "date_vente" : {value = DateUtil.dateToString(caisseMvmBean.getId()!=null?caisseMvmBean.getDate_creation() : new Date(), "dd/MM/yyyy HH:mm:ss");}; break;
					case "libre" : {value = confDetP.getValeur_defaut();}; break;
					
					case "montant_net" : {value = ""+caisseMvmBean.getMtt_commande_net();}; break;
					case "montant_reduction" : {value = ""+caisseMvmBean.getMtt_reduction().shortValue();}; break;
					case "code_wifi" : {value = mapConfig.get(PARAM_APPLI_ENUM.WIFI.toString());}; break;
					case "nom_restqaurant" : {value = ContextAppli.getEtablissementBean().getNom();}; break;
					case "ref_table" : {value = caisseMvmBean.getRefTablesDetail();}; break;
					
					case "famille" : {
						for(CaisseMouvementArticlePersistant mvmDet : caisseMvmBean.getList_article()){
							
							if(caisseMouvement.getMax_idx_client() == 1 || mvmDet.getIdx_client() == clientIdx) {
								
								ArticlePersistant opc_article = mvmDet.getOpc_article();
								if(opc_article != null && opc_article.getOpc_famille_cuisine() != null
										&& opc_article.getOpc_famille_cuisine().getId().equals(confDetP.getFamille())){
									String qte = mvmDet.getQuantite() != null ? ""+mvmDet.getQuantite().intValue() : "";
									String val = "["+qte+"]" + mvmDet.getOpc_article().getLibelle().trim();
									
									PrintBean printBean = new PrintBean();
									printBean.setX(posX);
									printBean.setY(globalY);
									printBean.setValue(val);
									
									this.listData.add(printBean);
									globalY = globalY + stepY;
								}
							}
						}
					}; break;
				}
				if(StringUtil.isNotEmpty(value)) {
					PrintBean printBean = new PrintBean();
					printBean.setX(posX);
					printBean.setY(globalY);
					printBean.setValue(value);
					
					this.listData.add(printBean);
					
					globalY = globalY + stepY;
				}
			}
		}
	}
	
	private void buildData(){
		List<PrintPosDetailBean> listPrintLinrs = this.printBean.getListDetail();
		
		for(TicketCaisseConfPersistant ticketCaisseConf : listTicketCaisseConf) {

			int size = (ticketCaisseConf.getFont_size() == null ? 9 : ticketCaisseConf.getFont_size());
			int weight = "B".equals(ticketCaisseConf.getFont_weight()) ? Font.BOLD : Font.PLAIN;
			Font font = new Font("Monospaced", weight, size);
//			g2d.setFont(font);
			
			Integer MAX_STR_LENGTH = ticketCaisseConf.getBack_pos();
			
	        try{
	            for (PrintBean detail : listData) {
	            	// Convertir en pixel
	            	//1 centimeter = 37.79527559055 pixel (X)
	            	
	            	int pixelX = BigDecimalUtil.multiply(BigDecimalUtil.get(detail.getX()), BigDecimalUtil.get("0.25")).intValue();
	            	int pixelY = BigDecimalUtil.multiply(BigDecimalUtil.get(detail.getY()), BigDecimalUtil.get("0.25")).intValue();
	            	String textOri = StringUtil.getValueOrEmpty(detail.getValue());
	            	 
	            	if(MAX_STR_LENGTH != null){
	 					int nbrLigne = Math.abs(textOri.length()/MAX_STR_LENGTH)+1 ;
	                     if(nbrLigne > 1){
	                     	for(int j=0; j<nbrLigne; j++){ 
	                     		int endLine = (j*MAX_STR_LENGTH)+MAX_STR_LENGTH > textOri.length() ? textOri.length() : (j*MAX_STR_LENGTH)+MAX_STR_LENGTH;
	                     		String text = textOri.substring(j*MAX_STR_LENGTH, endLine);
	                     		//
//	                     		g2d.drawString(text, pixelX+(j==0?0:2), pixelY);
	                     		
	                     		
	                     		listPrintLinrs.add(new PrintPosDetailBean(text, pixelX+(j==0?0:2), pixelY, font)); 
	                     		
	                     		pixelY = pixelY + 10;
	                     	}
	                     } else{
//	                     	g2d.drawString(textOri, pixelX, pixelY);
	                     	listPrintLinrs.add(new PrintPosDetailBean(textOri, pixelX, pixelY, font));
	                     }
	            	} else{
//	            		g2d.drawString(textOri, pixelX, pixelY);
	            		listPrintLinrs.add(new PrintPosDetailBean(textOri, pixelX, pixelY, font));
	            	}
	            }
            } catch (Exception r) {
                r.printStackTrace();
            }
		}
	}
}

class PrintBean{
	private int x;
	private int y;
	private String value;
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
