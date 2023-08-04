package appli.model.domaine.util_srv.raz.pdf;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import appli.controller.domaine.util_erp.ContextAppli.PARAM_APPLI_ENUM;
import appli.controller.domaine.util_erp.ContextAppli.SOFT_ENVS;
import appli.controller.util_ctrl.MouvementDetailPrintBean;
import appli.model.domaine.stock.persistant.FamillePersistant;
import framework.controller.ContextGloabalAppli;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.PdfBean;
import framework.model.util.PdfUtil;
import framework.model.util.PdfUtil.EnumBorder;

public class RazJourPDF {
	
//	public enum MODE_PDF {
//		MODEL, ARCHIVE, PAIE_ALL, PAIE
//	}
	
	/**
	 * @param dest
	 * @throws IOException
	 * @throws DocumentException
	 */
	public File getPdf(String titre, String date, Map<String, Object> mapData, boolean isPeriode) {
		PdfBean pdfBean = null;
		Document document = null;
		
		try{
			pdfBean = PdfUtil.creerDocument("raz_jour_"+DateUtil.dateToString(new Date(), "dd-MM-yyyy"));
			document = pdfBean.getDocument();
			
			List<Map<String, Object>> listJours = (List<Map<String, Object>>) mapData.get("list_days_mtt");
			
			if(listJours == null){
				ajouterEntete(document, titre, date);
				ajouterContenu(document, mapData, isPeriode);	
			} else{// Jour par jour
				for(Map<String, Object> mapJours : listJours){
					String dt = (String) mapJours.get("date_raz");
					
					ajouterTitre(document, dt);
					ajouterContenu(document, mapJours, isPeriode);	
					document.newPage();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally{			
			document.close();
		}
		
		return pdfBean.getPdfFile();
	}

	/**
	 * @param document
	 * @param cupP
	 * @throws DocumentException 
	 */
	private void ajouterEntete(Document document, String titre, String date) throws DocumentException {
		PdfPTable table = new PdfPTable(1);
	    table.setWidthPercentage(100);
	    float[] widths = {100f};
		table.setWidths(widths);// largeur par cellule
		
		PdfPCell cell = PdfUtil.getCellVide(document);
		PdfUtil.effacerBordure(cell, EnumBorder.NO_BORDER);
		table.addCell(cell);
		
		cell = PdfUtil.getCell(document, titre, Element.ALIGN_CENTER, PdfUtil.FONT_14_BOLD_BLACK);
		cell.setBackgroundColor(PdfUtil.GRIS_CLAIR);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setFixedHeight(25);
		table.addCell(cell);

		cell = PdfUtil.getCell(document, date, Element.ALIGN_CENTER, PdfUtil.FONT_12_NORMAL_BLACK);
		cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);
		
		cell = PdfUtil.getCellVide(document);
		PdfUtil.effacerBordure(cell, EnumBorder.NO_BORDER);
		table.addCell(cell);
		
		document.add(table);
		PdfUtil.ajouterLigneVide(document, 1);
	}
	
	private void ajouterTitre(Document document, String text) throws DocumentException{
		Paragraph titre = new Paragraph(text);
        titre.setFont(PdfUtil.FONT_11_BOLD_BLACK);
        titre.setAlignment(Element.ALIGN_CENTER);
        
        document.add(titre);
        PdfUtil.ajouterligneSeparateur(document);
	}
	
	/**
	 * @param document
	 * @param cupP
	 */
	private void ajouterContenu(Document document, Map<String, Object> mapRetour, boolean isPeriode) throws DocumentException {
	    PdfPTable table = new PdfPTable(2);
	    table.setWidthPercentage(100f);
	    float[] widths2 = {80f, 20f};
		table.setWidths(widths2);// largeur par cellule
		
		BigDecimal mttOffertGlobal = (BigDecimal) mapRetour.get("mtt_offert");
		BigDecimal[] montantsRecap = (BigDecimal[]) mapRetour.get("mtt_recap");
		BigDecimal[] montantsMode = (BigDecimal[]) mapRetour.get("mtt_mode");
		String[][] data = (String[][]) mapRetour.get("data_recap");
		
		 BigDecimal tauxTva = BigDecimalUtil.get(ContextGloabalAppli.getGlobalConfig(PARAM_APPLI_ENUM.TVA_VENTE.toString()));
		
         PdfPCell cell;
		if(montantsRecap != null){
        	
    		 BigDecimal mttTotalNetALL = montantsMode[0], 
    				 	mttTotalBrutALL = montantsMode[1], 
    				 	mttEspeceALL = montantsMode[2], 
    				 	mttCarteALL = montantsMode[3], 
    				 	mttChequeALL = montantsMode[4], 
    				 	mttDejALL = montantsMode[5],
    				 	mttPointsALL = montantsMode[6],
    				 	mttReserveALL = montantsMode[7];
    		 
             BigDecimal nbrNoteAll = montantsRecap[0], 
            		 	mttMoyen = montantsRecap[1], 
            		 	mttTotalReducClientALL = montantsRecap[2], 
            		 	mttTotalReducEmployeALL = montantsRecap[3], 
            		 	mttTotalOffertClientALL = montantsRecap[4], 
            		 	mttTotalOffertEmployeALL = montantsRecap[5], 
            		 	mttTotalAnnuleALL = montantsRecap[6],
                    	mttTotalReducAutreALL = montantsRecap[7],
                    	mttTotalOffertAutreALL = montantsRecap[8],
                    	mttTotalAnnuleArtALL = montantsRecap[9];
    		
            if(!BigDecimalUtil.isZero(mttEspeceALL)){
	            cell = PdfUtil.getCell(document, "ESPECES :", Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	         	table.addCell(cell);
	         	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(mttEspeceALL), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	        	table.addCell(cell);
            }
            if(!BigDecimalUtil.isZero(mttCarteALL)){
	        	cell = PdfUtil.getCell(document, "CARTE :", Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	         	table.addCell(cell);
	         	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(mttCarteALL), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	        	table.addCell(cell);
            }
            if(!BigDecimalUtil.isZero(mttChequeALL)){
	        	cell = PdfUtil.getCell(document, "CHÈQUE :", Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	         	table.addCell(cell);
	         	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(mttChequeALL), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	        	table.addCell(cell);
            }
            if(!BigDecimalUtil.isZero(mttDejALL)){
	        	cell = PdfUtil.getCell(document, "CHÈQUE TABLE :", Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	         	table.addCell(cell);
	         	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(mttDejALL), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	        	table.addCell(cell);
            }
            
            
            if(!BigDecimalUtil.isZero(mttPointsALL)){
	        	cell = PdfUtil.getCell(document, "POINTS :", Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	         	table.addCell(cell);
	         	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(mttPointsALL), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	        	table.addCell(cell);
            }
            if(!BigDecimalUtil.isZero(mttReserveALL)){
	        	cell = PdfUtil.getCell(document, "RESERVE :", Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	         	table.addCell(cell);
	         	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(mttReserveALL), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	        	table.addCell(cell);
            }
            
            
            if(!BigDecimalUtil.isZero(nbrNoteAll)){
	        	cell = PdfUtil.getCell(document, "Nombre de notes payantes :", Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	         	table.addCell(cell);
	         	cell = PdfUtil.getCell(document, ""+nbrNoteAll.intValue(), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	        	table.addCell(cell);
            }
            if(!BigDecimalUtil.isZero(mttMoyen)){
	        	cell = PdfUtil.getCell(document, "Moyenne notes :", Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	         	table.addCell(cell);
	         	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(mttMoyen), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	        	table.addCell(cell);
            }
            if(!BigDecimalUtil.isZero(mttTotalReducClientALL)){
	        	cell = PdfUtil.getCell(document, "Réductions clients :", Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	         	table.addCell(cell);
	         	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(mttTotalReducClientALL), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	        	table.addCell(cell);
            }
            if(!BigDecimalUtil.isZero(mttTotalReducEmployeALL)){
	        	cell = PdfUtil.getCell(document, "Réductions personnels :", Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	         	table.addCell(cell);
	         	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(mttTotalReducEmployeALL), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	        	table.addCell(cell);
            }
            if(!BigDecimalUtil.isZero(mttTotalReducAutreALL)){
	        	cell = PdfUtil.getCell(document, "Réductions :", Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	         	table.addCell(cell);
	         	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(mttTotalReducAutreALL), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	        	table.addCell(cell);
            }
            if(!BigDecimalUtil.isZero(mttTotalOffertClientALL)){
	        	cell = PdfUtil.getCell(document, "Offerts clients :", Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	         	table.addCell(cell);
	         	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(mttTotalOffertClientALL), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	        	table.addCell(cell);
            }
            if(!BigDecimalUtil.isZero(mttTotalOffertEmployeALL)){
	        	cell = PdfUtil.getCell(document, "Offerts personnels :", Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	         	table.addCell(cell);
	         	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(mttTotalOffertEmployeALL), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	        	table.addCell(cell);
            }
            if(!BigDecimalUtil.isZero(mttTotalOffertAutreALL)){
	        	cell = PdfUtil.getCell(document, "Offerts :", Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	         	table.addCell(cell);
	         	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(mttTotalOffertAutreALL), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	        	table.addCell(cell);
            }
            if(!BigDecimalUtil.isZero(mttTotalAnnuleALL)){
	        	cell = PdfUtil.getCell(document, "Annulations commandes :", Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	         	table.addCell(cell);
	         	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(mttTotalAnnuleALL), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	        	table.addCell(cell);
            }
            if(!BigDecimalUtil.isZero(mttTotalAnnuleArtALL)){
	        	cell = PdfUtil.getCell(document, "Annulations articles :", Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	         	table.addCell(cell);
	         	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(mttTotalAnnuleArtALL), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	        	table.addCell(cell);
            }
        	
            // Total
            BigDecimal mttTva = BigDecimalUtil.divide(BigDecimalUtil.multiply(mttTotalNetALL, tauxTva), BigDecimalUtil.get(100));
             
            cell = PdfUtil.getCell(document, "TOTAL BRUT :", Element.ALIGN_RIGHT, PdfUtil.FONT_9_BOLD_BLACK);
            cell.setBackgroundColor(PdfUtil.JAUNE_LEGER);
          	table.addCell(cell);
          	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(mttTotalBrutALL), Element.ALIGN_RIGHT, PdfUtil.FONT_9_BOLD_BLACK);
          	cell.setBackgroundColor(PdfUtil.JAUNE_LEGER);
          	table.addCell(cell);
             
         	cell = PdfUtil.getCell(document, "TOTAL NET :", Element.ALIGN_RIGHT, PdfUtil.FONT_9_BOLD_BLACK);
         	cell.setBackgroundColor(PdfUtil.JAUNE_LEGER);
         	table.addCell(cell);
         	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(mttTotalNetALL), Element.ALIGN_RIGHT, PdfUtil.FONT_9_BOLD_BLACK);
         	cell.setBackgroundColor(PdfUtil.JAUNE_LEGER);
        	table.addCell(cell);
             
        	cell = PdfUtil.getCell(document, "MONTANT TVA ("+BigDecimalUtil.formatNumberZero(tauxTva)+") :", Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
        	cell.setBackgroundColor(PdfUtil.JAUNE_LEGER);
        	table.addCell(cell);
         	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(mttTva), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
         	cell.setBackgroundColor(PdfUtil.JAUNE_LEGER);
        	table.addCell(cell);
        	
        	cell = PdfUtil.getCell(document, "TOTAL TTC :", Element.ALIGN_RIGHT, PdfUtil.FONT_9_BOLD_BLACK);
        	cell.setBackgroundColor(PdfUtil.JAUNE_LEGER);
         	table.addCell(cell);
         	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(mttTotalNetALL), Element.ALIGN_RIGHT, PdfUtil.FONT_9_BOLD_BLACK);
         	cell.setBackgroundColor(PdfUtil.JAUNE_LEGER);
        	table.addCell(cell);
        	
        	document.add(table);
         }
         
		// Détail -----------------------------------------------------------------------------------------------
		boolean isRestau = SOFT_ENVS.restau.toString().equals(StrimUtil.getGlobalConfigPropertie("context.soft"));
		List<MouvementDetailPrintBean> listDet = getListDetail(mapRetour, "MENU");
		// Menu ---------------------------------------------------------
		if(isRestau && listDet.size() > 0){
			PdfUtil.ajouterligneSeparateur(document);
			ajouterTitre(document, "LES VENTES EN MENUS");
		}
		
		table = new PdfPTable(3);
	    table.setWidthPercentage(100f);
	    float[] widths3 = {60f, 20f, 20f};
		table.setWidths(widths3);// largeur par cellule
		
         //---------- On collecte les taux de TVA -----------------------
         BigDecimal mttTotal = BigDecimalUtil.get(0), qteTotal = BigDecimalUtil.get(0);
         BigDecimal mttTotalAnnul = BigDecimalUtil.get(0), qteTotalAnnul = BigDecimalUtil.get(0);
         BigDecimal mttTotalOffert = BigDecimalUtil.get(0), qteTotalOffert = BigDecimalUtil.get(0);
         BigDecimal[] mttQteArray = null;
        	
         //
         if (listDet.size() > 0) {
             mttQteArray = addDetail(document, table, listDet, "Menus");
             
             mttTotal = BigDecimalUtil.add(mttTotal, mttQteArray[0]);
             qteTotal = BigDecimalUtil.add(qteTotal, mttQteArray[1]);
         }
         listDet = getListDetail(mapRetour, "ANNUL_MENU");
         if (listDet.size() > 0) {
             mttQteArray = addDetail(document, table,listDet, "Menus annulés");
             mttTotalAnnul = BigDecimalUtil.add(mttTotalAnnul, mttQteArray[0]);
             qteTotalAnnul = BigDecimalUtil.add(qteTotalAnnul, mttQteArray[1]);
         }
         listDet = getListDetail(mapRetour, "OFFR_MENU");
         if (listDet.size() > 0) {
             mttQteArray = addDetail(document, table,listDet, "Menus offerts");
             mttTotalOffert = BigDecimalUtil.add(mttTotalOffert, mttQteArray[0]);
             qteTotalOffert = BigDecimalUtil.add(qteTotalOffert, mttQteArray[1]);
             
         }
         // Article
         listDet = getListDetail(mapRetour, "ART_MENU");
         if (listDet.size() > 0) {
             mttQteArray = addDetail(document, table,listDet, "Articles vendus");
             mttTotal = BigDecimalUtil.add(mttTotal, mttQteArray[0]);
             qteTotal = BigDecimalUtil.add(qteTotal, mttQteArray[1]);
             
         }
         listDet = getListDetail(mapRetour, "ANNUL_ART_MENU");
         if (listDet.size() > 0) {
             mttQteArray = addDetail(document, table,listDet, "Articles annulés");
             mttTotalAnnul = BigDecimalUtil.add(mttTotalAnnul, mttQteArray[0]);
             qteTotalAnnul = BigDecimalUtil.add(qteTotalAnnul, mttQteArray[1]);
             
         }
         listDet = getListDetail(mapRetour, "OFFR_ART_MENU");
         if (listDet.size() > 0) {
             mttQteArray = addDetail(document, table,listDet, "Articles offerts");
             mttTotalOffert = BigDecimalUtil.add(mttTotalOffert, mttQteArray[0]);
             qteTotalOffert = BigDecimalUtil.add(qteTotalOffert, mttQteArray[1]);
             
         }
         if(isRestau){ 
        	 document.add(table);
         }
         // Hors menu ---------------------------------------------------
         
         if(isPeriode){
        	document.add(table);
        	return; 
         }
        PdfUtil.ajouterligneSeparateur(document);
        ajouterTitre(document, (isRestau ? "LES VENTES HORS MENUS" : "DÉTAIL DES VENTES"));
     	
        table = new PdfPTable(3);
  	    table.setWidthPercentage(100f);
  	    float[] widths4 = {60f, 20f, 20f};
  		table.setWidths(widths4);// largeur par cellule
  		
         //
         listDet = getListDetail(mapRetour, "GROUPE_FAMILLE");
         if (listDet.size() > 0) {
             mttQteArray = addDetail(document, table,listDet, "Familles");
             mttTotal = BigDecimalUtil.add(mttTotal, mttQteArray[0]);
             qteTotal = BigDecimalUtil.add(qteTotal, mttQteArray[1]);
             
         }
         listDet = getListDetail(mapRetour, "ANNUL_GROUPE_FAMILLE");
         if (listDet.size() > 0) {
             mttQteArray = addDetail(document, table,listDet, "Familles annulées");
             mttTotalAnnul = BigDecimalUtil.add(mttTotalAnnul, mttQteArray[0]);
             qteTotalAnnul = BigDecimalUtil.add(qteTotalAnnul, mttQteArray[1]);
             
         }
         listDet = getListDetail(mapRetour, "OFFR_GROUPE_FAMILLE");
         if (listDet.size() > 0) {
             mttQteArray = addDetail(document, table,listDet, "Familles offertes");
             mttTotalOffert = BigDecimalUtil.add(mttTotalOffert, mttQteArray[0]);
             qteTotalOffert = BigDecimalUtil.add(qteTotalOffert, mttQteArray[1]);
             
         }
         // Article
         listDet = getListDetail(mapRetour, "ART");
         if (listDet.size() > 0) {
             mttQteArray = addDetail(document, table,listDet, "Articles vendus");
             mttTotal = BigDecimalUtil.add(mttTotal, mttQteArray[0]);
             qteTotal = BigDecimalUtil.add(qteTotal, mttQteArray[1]);
             
         }
         listDet = getListDetail(mapRetour, "ANNUL_ART");
         if (listDet.size() > 0) {
             mttQteArray = addDetail(document, table,listDet, "Articles annulés");
             mttTotalAnnul = BigDecimalUtil.add(mttTotalAnnul, mttQteArray[0]);
             qteTotalAnnul = BigDecimalUtil.add(qteTotalAnnul, mttQteArray[1]);
             
         }
         listDet = getListDetail(mapRetour, "OFFR_ART");
         if (listDet.size() > 0) {
             mttQteArray = addDetail(document, table,listDet, "Articles offerts");
             mttTotalOffert = BigDecimalUtil.add(mttTotalOffert, mttQteArray[0]);
             qteTotalOffert = BigDecimalUtil.add(qteTotalOffert, mttQteArray[1]);
             
         }

         document.add(table);
         
         PdfUtil.ajouterligneSeparateur(document);
         
        table = new PdfPTable(3);
  	    table.setWidthPercentage(100f);
  	    float[] widths5 = {60f, 20f, 20f};
  		table.setWidths(widths5);// largeur par cellule

         if (!BigDecimalUtil.isZero(mttTotal)) {
        	 cell = PdfUtil.getCell(document, "TOTAL COMMANDES", Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
        	 cell.setBackgroundColor(PdfUtil.JAUNE_LEGER);
             table.addCell(cell);
             
             cell = PdfUtil.getCell(document, ""+qteTotal.intValue(), Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
             cell.setBackgroundColor(PdfUtil.JAUNE_LEGER);
             table.addCell(cell);
             
             cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(mttTotal), Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
             cell.setBackgroundColor(PdfUtil.JAUNE_LEGER);
             table.addCell(cell);
         }

         if (!BigDecimalUtil.isZero(mttTotalOffert)) {
        	 cell = PdfUtil.getCell(document, "TOTAL ARTICLES OFFERS", Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
        	 cell.setBackgroundColor(PdfUtil.JAUNE_LEGER);
             table.addCell(cell);
             
             cell = PdfUtil.getCell(document, ""+qteTotalOffert.intValue(), Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
             cell.setBackgroundColor(PdfUtil.JAUNE_LEGER);
             table.addCell(cell);
             
             cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(mttTotalOffert), Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
             cell.setBackgroundColor(PdfUtil.JAUNE_LEGER);
             table.addCell(cell);
         }

         if (!BigDecimalUtil.isZero(mttOffertGlobal)) {
        	 cell = PdfUtil.getCell(document, "TOTAL OFFERS COMMANDES", Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
        	 cell.setBackgroundColor(PdfUtil.JAUNE_LEGER);
             table.addCell(cell);
             
             cell = PdfUtil.getCell(document, "", Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
             cell.setBackgroundColor(PdfUtil.JAUNE_LEGER);
             table.addCell(cell);
             
             cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(mttOffertGlobal), Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
             cell.setBackgroundColor(PdfUtil.JAUNE_LEGER);
             table.addCell(cell);
         }

         if (!BigDecimalUtil.isZero(mttTotalAnnul)) {
        	 cell = PdfUtil.getCell(document, "TOTAL ANNULÉS ARTICLES", Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
        	 cell.setBackgroundColor(PdfUtil.JAUNE_LEGER);
             table.addCell(cell);
             
             cell = PdfUtil.getCell(document, ""+qteTotalAnnul.intValue(), Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
             cell.setBackgroundColor(PdfUtil.JAUNE_LEGER);
             table.addCell(cell);
             
             cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(mttTotalAnnul), Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
             cell.setBackgroundColor(PdfUtil.JAUNE_LEGER);
             table.addCell(cell);
         }
         // Ligne TVA
         BigDecimal mttHt = BigDecimalUtil.getMttHt(mttTotal, tauxTva);
         BigDecimal mttTva = BigDecimalUtil.substract(mttTotal, mttHt); 
         
         cell = PdfUtil.getCell(document, "TVA (" + tauxTva + "%)", Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
         cell.setBackgroundColor(PdfUtil.JAUNE_LEGER);
         table.addCell(cell);
         
         cell = PdfUtil.getCellVide(document);
         cell.setBackgroundColor(PdfUtil.JAUNE_LEGER);
         table.addCell(cell);
         
         cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(mttTva), Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
         cell.setBackgroundColor(PdfUtil.JAUNE_LEGER);
         table.addCell(cell);
		
		document.add(table);
	}

	private BigDecimal[] addDetail(Document document, PdfPTable table, List<MouvementDetailPrintBean> listDetail, String titre){
		// Titre -----------------------------------------
        PdfPCell cell = PdfUtil.getCell(document, titre.toUpperCase(), Element.ALIGN_CENTER, PdfUtil.FONT_9_BOLD_BLACK);
        cell.setColspan(3);
        cell.setBackgroundColor(PdfUtil.JAUNE_LEGER);
    	table.addCell(cell);
        
    	Collections.sort(listDetail, new Comparator<MouvementDetailPrintBean>() {
	       	public int compare(final MouvementDetailPrintBean object1, final MouvementDetailPrintBean object2) {
	           	if(object1.getFamille() != null && object2.getFamille() != null) {
	           		return object1.getFamille().getB_left().compareTo(object2.getFamille().getB_left());
	           	} else if(object1.getMenu() != null && object2.getMenu() != null) {
	           		return object1.getMenu().getCode().compareTo(object2.getMenu().getCode());
	           	} else {
	           		return object1.getLibelle().compareTo(object2.getLibelle());
	           	}
	       	}
        });
    	
		//
		BigDecimal mttTotal = null;
	    BigDecimal qteTotal = BigDecimalUtil.get(0);
	    //
	    String oldElementId     = null;
	    BigDecimal sousTotal    = null;
	    
		//
    	//
   	    List<FamillePersistant> oldFamilles = null;
   	    FamillePersistant oldFamille = null;
   	    Map<Long, BigDecimal> mapParentSousTotals = new HashMap<Long, BigDecimal>();
   	    //
	    for (MouvementDetailPrintBean detail : listDetail) {
	    	String currElement = null;
	    	// Gestion par famille
	    	if(detail.getFamille() != null){
	    		List<FamillePersistant> listfamilleParent = detail.getListfamilleParent();
	    		currElement = detail.getFamille().getCode();	    		
	    		int ecartLevel = (oldFamille!=null ? oldFamille.getLevel()-detail.getFamille().getLevel() : 0);
	    		
	    		if(oldElementId != null && !oldElementId.equals(currElement)){
	    			
    				cell = PdfUtil.getCell(document, "TOTAL".toUpperCase(), Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
    			    cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
    		    	table.addCell(cell);
    		    	
    		    	cell = PdfUtil.getCell(document, "", Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
    		    	cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
    		    	table.addCell(cell);
    		    	
    		    	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(sousTotal), Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
    		    	cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
    		    	table.addCell(cell);
    		    	
    				boolean isPrentChanged = false;
    				if(listfamilleParent != null && listfamilleParent.size() > 0 ) {	
    					for(int i=listfamilleParent.size()-2; i>=0; i--) {
    	    				FamillePersistant familleParent = listfamilleParent.get(i);
    	    				if(oldFamilles != null && i<oldFamilles.size()-1 && !familleParent.getCode().equals(oldFamilles.get(i).getCode())) {
    	    					isPrentChanged = true;
    	    					break; 
    	    				} else if( oldFamilles != null && i<oldFamilles.size()-1 && ecartLevel > 0&& familleParent.getCode().equals(oldFamilles.get(i).getCode()) ){
    	    					isPrentChanged = true;
    	    					break;
    	    				}
    					}
    				}
    				
    				if(isPrentChanged || (listfamilleParent != null && listfamilleParent.size() == 1)) {
	    				for(int i=ecartLevel; i>=0; i--){
	    					
	    					int ecart = oldFamilles.get(i).getLevel().intValue();
	    					String space = "";
	    					while(ecart>=0) {
		    					space += "     ";
		    					ecart--;
		    				}
	    					
		    				cell = PdfUtil.getCell(document, space+"TOTAL".toUpperCase(), Element.ALIGN_LEFT, PdfUtil.FONT_10_BOLD_BLACK);
		    			    cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
		    		    	table.addCell(cell);
		    		    	
		    		    	cell = PdfUtil.getCell(document, "", Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
		    		    	cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
		    		    	table.addCell(cell);
		    		    	
		    		    	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(mapParentSousTotals.get(oldFamilles.get(i).getId())), Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
		    		    	cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
		    		    	table.addCell(cell);
		    			}
    				}
	    			sousTotal = null;
	    		}
	    		
	    		 if(listfamilleParent != null && listfamilleParent.size() > 0 ) {
    	    		String str = "";
    	    		String space = "";
	    			for(int i=0; i<listfamilleParent.size(); i++) {
	    				FamillePersistant familleParent = listfamilleParent.get(i);
	    				int decal = listfamilleParent.get(i).getLevel() <=1 ? 0 : listfamilleParent.get(i).getLevel();
	    				
	    				while(decal>0) {
	    					space += "          ";
	    					decal--;
	    				}
	    				if(oldFamilles == null || i>oldFamilles.size()-1 || !familleParent.getCode().equals(oldFamilles.get(i).getCode())) {	
	    	    			str += (space + familleParent.getLibelle().replace("#", "")+"\n");
	    				}
    	    		}
	    			if(StringUtil.isNotEmpty(str)){
	    		    	cell = PdfUtil.getCell(document, str, Element.ALIGN_LEFT, PdfUtil.FONT_10_BOLD_BLACK);
	    		    	cell.setColspan(3);
	    		    	cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
	    		    	table.addCell(cell);
	    			}
    	    	}
	    	    	
				oldFamilles = listfamilleParent;
				oldFamille = detail.getFamille();
	    		oldElementId = detail.getFamille().getCode();
	    		
	    		if(listfamilleParent != null) {
		    		for(FamillePersistant famP : listfamilleParent) {
		    			mapParentSousTotals.put(famP.getId(),  BigDecimalUtil.add(mapParentSousTotals.get(famP.getId()), detail.getMtt_total()));
		    		}
	    		}
	    	} 
	    	// Gestion par menu
	    	else if(detail.getMenu() != null){
	    		currElement = detail.getMenu().getCode();
	    		
	    		if(oldElementId == null || !oldElementId.equals(currElement)) {
		    		if(oldElementId != null) { 
		    			cell = PdfUtil.getCell(document, "SOUS TOTAL".toUpperCase(), Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
	    			    cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
	    		    	table.addCell(cell);
	    		    	
	    		    	cell = PdfUtil.getCell(document, "", Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
	    		    	cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
	    		    	table.addCell(cell);
	    		    	
	    		    	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(sousTotal), Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
	    		    	cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
	    		    	table.addCell(cell);
		    			
	    		    	sousTotal = null;
		    		}
		    		String lib = detail.getMenu().getLibelle();

    		    	cell = PdfUtil.getCell(document, lib, Element.ALIGN_LEFT, PdfUtil.FONT_10_BOLD_BLACK);
    		    	cell.setColspan(3);
    		    	cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
    		    	table.addCell(cell);
	    			
    		    	sousTotal = null;
		    	}	    		
	    		
	    		oldElementId = detail.getMenu().getCode();
	    	}
	    	
            mttTotal = BigDecimalUtil.add(mttTotal, detail.getMtt_total());
            qteTotal = BigDecimalUtil.add(qteTotal, detail.getQuantite());

            sousTotal = BigDecimalUtil.add(sousTotal, detail.getMtt_total());
            
            BigDecimal quantite = detail.getQuantite();
            BigDecimal prix = detail.getMtt_total();
            String prixStr = BigDecimalUtil.formatNumberZero(prix);
            String qte = quantite != null ? "" + quantite.intValue() : "";
	
            cell = PdfUtil.getCell(document, detail.getLibelle().toUpperCase(), Element.ALIGN_LEFT, PdfUtil.FONT_9_NORMAL_BLACK);
	    	table.addCell(cell);
	    	
	    	cell = PdfUtil.getCell(document, qte, Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	    	table.addCell(cell);
	    	
	    	cell = PdfUtil.getCell(document, prixStr, Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	    	table.addCell(cell);
            
//            sb.append("<tr>");
//        	sb.append("<td align='right' style='width:50%;'>"+detail.getLibelle().toUpperCase()+"</td>");
//        	sb.append("<td align='right'>"+qte+"</td>");
//        	sb.append("<td align='right'>"+prixStr+"</td>");
//        	sb.append("</tr>");
	    }
	    
	    if(oldFamille != null){
    		cell = PdfUtil.getCell(document, "SOUS TOTAL".toUpperCase(), Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
		    cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
	    	table.addCell(cell);
	    	
	    	cell = PdfUtil.getCell(document, ""+qteTotal.intValue(), Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
	    	cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
	    	table.addCell(cell);
	    	
	    	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(sousTotal), Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
	    	cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
	    	table.addCell(cell);
	    	// -----------------------
	    	cell = PdfUtil.getCell(document, "SOUS TOTAL".toUpperCase(), Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
		    cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
	    	table.addCell(cell);
	    	
	    	cell = PdfUtil.getCell(document, ""+qteTotal.intValue(), Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
	    	cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
	    	table.addCell(cell);
	    	
	    	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(mapParentSousTotals.get(oldFamilles.get(0).getId())), Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
	    	cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
	    	table.addCell(cell);
		} else if(oldElementId != null) {   
			cell = PdfUtil.getCell(document, "SOUS TOTAL".toUpperCase(), Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
		    cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
	    	table.addCell(cell);
	    	
	    	cell = PdfUtil.getCell(document, "", Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
	    	cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
	    	table.addCell(cell);
	    	
	    	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(sousTotal), Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
	    	cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
	    	table.addCell(cell);
		}
	    
    	cell = PdfUtil.getCell(document, "TOTAL".toUpperCase(), Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
	    cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
    	table.addCell(cell);
    	
    	cell = PdfUtil.getCell(document, ""+qteTotal.intValue(), Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
    	cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
    	table.addCell(cell);
    	
    	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(mttTotal), Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
    	cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
    	table.addCell(cell);
    	
	    BigDecimal[] data = {mttTotal, qteTotal};

        return data;
	}

	/**
	 * @param type
	 * @return
	 */
	private List<MouvementDetailPrintBean> getListDetail(Map<String, Object> mapRetour, String type) {
		List<MouvementDetailPrintBean> listDetail = (List<MouvementDetailPrintBean>) mapRetour.get("list_cmd");
	    Map<String, MouvementDetailPrintBean> mapDet = new LinkedHashMap<>();
	    
	    if(listDetail != null){
		    for (MouvementDetailPrintBean det : listDetail) {
		        if (det.getType().equals(type)) {
		            final String key = det.getCode().trim().toUpperCase();
		            MouvementDetailPrintBean mvm = mapDet.get(key);
		            if (mvm == null) {
		                try {
							mvm = (MouvementDetailPrintBean) BeanUtils.cloneBean(det);
						} catch (Exception e) {
							e.printStackTrace();
						}
		                mapDet.put(key, mvm);
		            } else {
		                mvm.setMtt_total(BigDecimalUtil.add(mvm.getMtt_total(), det.getMtt_total()));
		                mvm.setQuantite(BigDecimalUtil.add(mvm.getQuantite(), det.getQuantite()));
		            }
		        }
		    }
	    }
	    List<MouvementDetailPrintBean> listData = new ArrayList<>();
	    for(String key : mapDet.keySet()){
	    	listData.add(mapDet.get(key));
	    }
	    
	    return listData;
	}
}
