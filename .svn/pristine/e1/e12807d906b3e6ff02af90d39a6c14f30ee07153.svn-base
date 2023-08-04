package appli.model.domaine.util_srv.raz.pdf;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import appli.controller.domaine.util_erp.ContextAppli.PARAM_APPLI_ENUM;
import appli.controller.util_ctrl.MouvementDetailPrintBean;
import framework.controller.ContextGloabalAppli;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.DateUtil;
import framework.model.util.PdfBean;
import framework.model.util.PdfUtil;
import framework.model.util.PdfUtil.EnumBorder;

public class RazBoissonPDF {
	
	public enum MODE_PDF {
		MODEL, ARCHIVE, PAIE_ALL, PAIE
	}
	
	private BigDecimal mttTotalAll = BigDecimalUtil.get(0), qteTotalAll = BigDecimalUtil.get(0);
	
	/**
	 * @param dest
	 * @throws IOException
	 * @throws DocumentException
	 */
	public File getPdf(String titre, String date, Map<String, Object> mapData) {
		PdfBean pdfBean = null;
		Document document = null;
		
		try{
			pdfBean = PdfUtil.creerDocument("raz_boisson_"+DateUtil.dateToString(new Date(), "dd-MM-yyyy"));
			document = pdfBean.getDocument();
			
			Map<String, MouvementDetailPrintBean> mapCmdBoissonsFroide = (Map<String, MouvementDetailPrintBean>) mapData.get("data_froid");
			Map<String, MouvementDetailPrintBean> mapCmdBoissonsChaude = (Map<String, MouvementDetailPrintBean>) mapData.get("data_chaud");
			
			ajouterEntete(document, titre, date);
			
			PdfPTable table = new PdfPTable(3);
		    table.setWidthPercentage(100f);
		    float[] widths2 = {60f, 20f, 20f};
			table.setWidths(widths2);// largeur par cellule
			
			ajouterContenu(table, document, mapCmdBoissonsFroide, "Boissons froides");
			ajouterContenu(table, document, mapCmdBoissonsChaude, "Boissons chaudes");
	        
	        BigDecimal tauxTva = BigDecimalUtil.get(ContextGloabalAppli.getGlobalConfig(PARAM_APPLI_ENUM.TVA_VENTE.toString()));
	        BigDecimal mttHt = BigDecimalUtil.getMttHt(mttTotalAll, tauxTva);
	        BigDecimal mttTva = BigDecimalUtil.substract(mttTotalAll, mttHt); 

	        PdfPCell cell = PdfUtil.getCell(document, "TVA (" + tauxTva + "%)", Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK); 
	        cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
	    	table.addCell(cell);
	    	cell = PdfUtil.getCellVide(document);
	    	cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
	    	table.addCell(cell);
	    	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(mttTva), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	    	cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
	    	table.addCell(cell);
	    	
	    	cell = PdfUtil.getCell(document, "TOTAL", Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	        cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
	    	table.addCell(cell);
	    	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(qteTotalAll), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	    	cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
	    	table.addCell(cell);
	    	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(mttTotalAll), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	    	cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
	    	table.addCell(cell);
			
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
	
	/**
	 * @param document
	 * @param cupP
	 */
	private void ajouterContenu(PdfPTable table, Document document, Map<String, MouvementDetailPrintBean> mapCmdBoissons, String titre) throws DocumentException {
		PdfPCell cell = null;
        if (mapCmdBoissons != null && mapCmdBoissons.size() > 0) {
        	cell = PdfUtil.getCell(document, titre, Element.ALIGN_CENTER, PdfUtil.FONT_10_BOLD_BLACK);
        	cell.setColspan(3);
        	cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
        	table.addCell(cell);
        	
        	cell = PdfUtil.getCell(document, "ARTICLE", Element.ALIGN_CENTER, PdfUtil.FONT_9_BOLD_BLACK);
        	cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
        	table.addCell(cell);
        	
        	cell = PdfUtil.getCell(document, "QTE", Element.ALIGN_CENTER, PdfUtil.FONT_9_BOLD_BLACK);
        	cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
        	table.addCell(cell);
        	
        	cell = PdfUtil.getCell(document, "MONTANT", Element.ALIGN_CENTER, PdfUtil.FONT_9_BOLD_BLACK);
        	cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
        	table.addCell(cell);
             
        	//
        	BigDecimal mttTotal = null;
            BigDecimal qteTotal = BigDecimalUtil.get(0);
            //
            for (String key : mapCmdBoissons.keySet()) {
                MouvementDetailPrintBean detail = mapCmdBoissons.get(key);
                
                if(!key.startsWith("")){
                    continue;
                }
                
                mttTotal = BigDecimalUtil.add(mttTotal, detail.getMtt_total());
                qteTotal = BigDecimalUtil.add(qteTotal, detail.getQuantite());

                BigDecimal quantite = detail.getQuantite();
                BigDecimal prix = detail.getMtt_total();
                String prixStr = BigDecimalUtil.formatNumberZero(prix);
                String qte = quantite != null ? "" + quantite.intValue() : "";

                cell = PdfUtil.getCell(document, detail.getLibelle().toUpperCase(), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
            	table.addCell(cell);
            	cell = PdfUtil.getCell(document, qte, Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
            	table.addCell(cell);
            	cell = PdfUtil.getCell(document, prixStr, Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
            	table.addCell(cell);
            }
            cell = PdfUtil.getCell(document, "TOTAL".toUpperCase(), Element.ALIGN_RIGHT, PdfUtil.FONT_9_BOLD_BLACK);
            cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
        	table.addCell(cell);
        	cell = PdfUtil.getCell(document, ""+qteTotal.intValue(), Element.ALIGN_RIGHT, PdfUtil.FONT_9_BOLD_BLACK);
        	cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
        	table.addCell(cell);
        	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(mttTotal), Element.ALIGN_RIGHT, PdfUtil.FONT_9_BOLD_BLACK);
        	cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
        	table.addCell(cell);
        	
        	qteTotalAll = BigDecimalUtil.add(qteTotalAll, qteTotal);
        	mttTotalAll = BigDecimalUtil.add(mttTotalAll, mttTotal);
        }
		
		document.add(table);
	}
}
