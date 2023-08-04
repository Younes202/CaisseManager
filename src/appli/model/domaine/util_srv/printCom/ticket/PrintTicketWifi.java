/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appli.model.domaine.util_srv.printCom.ticket;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.vente.persistant.CaissePersistant;
import framework.controller.ContextGloabalAppli;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.printGen.PrintCommunUtil;
import framework.model.util.printGen.PrintPosBean;
import framework.model.util.printGen.PrintPosDetailBean;

public class PrintTicketWifi {
	 private PrintPosBean printBean;
	 
	public PrintTicketWifi(CaissePersistant caisseBean) {
		this.printBean = new PrintPosBean();
    	List<PrintPosDetailBean> listDataToPrint = buildMapData();
    	this.printBean.setListDetail(listDataToPrint);
    	this.printBean.setMaxLineLength(50);
    	
    	this.printBean.setTicketHeight(BigDecimalUtil.get(60));
		this.printBean.setNbrTicket(1);
    	this.printBean.setPrinters(caisseBean.getImprimantes());
	}
	
    public PrintPosBean getPrintPosBean(){
    	return this.printBean;
    }

	private List<PrintPosDetailBean> buildMapData(){
		List<PrintPosDetailBean> listPrintLins = new ArrayList<>();
		EtablissementPersistant etablissementBean = ContextAppli.getEtablissementBean();
		
		if(etablissementBean != null){
			listPrintLins.add(new PrintPosDetailBean(etablissementBean.getRaison_sociale(), 0, 10, PrintCommunUtil.CUSTOM_FONT_10_B, "C"));
		}
		String currDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        listPrintLins.add(new PrintPosDetailBean("Imprimé le "+currDate, 0, 20, PrintCommunUtil.CUSTOM_FONT_9, "C"));
        
        String codeWifi = ContextGloabalAppli.getGlobalConfig("WIFI");
        listPrintLins.add(new PrintPosDetailBean("CODE WIFI : "+StringUtil.getValueOrEmpty(codeWifi), 0, 50, PrintCommunUtil.CUSTOM_FONT_10_B, "C"));

        return listPrintLins;
	}
}
