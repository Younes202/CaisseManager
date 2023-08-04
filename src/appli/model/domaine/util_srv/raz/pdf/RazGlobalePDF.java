package appli.model.domaine.util_srv.raz.pdf;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import appli.model.domaine.util_srv.raz.RazDetail;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.DateUtil;
import framework.model.util.PdfBean;
import framework.model.util.PdfUtil;

public class RazGlobalePDF {
	
	/**
	 * @param dest
	 * @throws IOException
	 * @throws DocumentException
	 */
	public File getPdf(String titre, String dateTxt, Map<String, Map<String, RazDetail>> data) {
		PdfBean pdfBean = null;
		Document document = null;
		
		try{
			pdfBean = PdfUtil.creerDocument("raz_livraison_"+DateUtil.dateToString(new Date(), "dd-MM-yyyy"));
			document = pdfBean.getDocument();
			
			ajouterEntete(document, titre, dateTxt);
			ajouterContenu(document, data);	
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
	private void ajouterEntete(Document document, String titre, String dateTxt) throws DocumentException {
		PdfPTable table = new PdfPTable(1);
	    table.setWidthPercentage(100);
	    float[] widths = {100f};
		table.setWidths(widths);// largeur par cellule
		
		PdfPCell cell = PdfUtil.getCell(document, titre+" ["+dateTxt+"]", Element.ALIGN_CENTER, PdfUtil.FONT_14_BOLD_BLACK);
		cell.setBackgroundColor(PdfUtil.GRIS_CLAIR);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setFixedHeight(25);
		table.addCell(cell);
		
		document.add(table);
		PdfUtil.ajouterLigneVide(document, 1);
	}
	
	/**
	 * @param document
	 * @param cupP
	 */
	private void ajouterContenu(Document document, Map<String, Map<String, RazDetail>> data) throws DocumentException {
	    PdfPTable table = new PdfPTable(3);
	    table.setWidthPercentage(100f);
	    float[] widths2 = {60f, 20f, 20f};
		table.setWidths(widths2);// largeur par cellule
		
        PdfPCell cell = PdfUtil.getCell(document, "#", Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
     	table.addCell(cell);
     	cell = PdfUtil.getCell(document, "Quantité", Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
    	table.addCell(cell);
    	cell = PdfUtil.getCell(document, "Montant", Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
    	table.addCell(cell);
    	
		for(String key : data.keySet()){
			BigDecimal ttlMtt = null;
			Map<String, RazDetail> recapMap = data.get(key);
			if(recapMap.size() == 0){
				continue;
			}
			//
			cell = PdfUtil.getCell(document, key, Element.ALIGN_LEFT, new Font(Font.getFamily("TIMES_ROMAN"), 10, Font.BOLD, BaseColor.WHITE));
			cell.setColspan(3);
			cell.setBackgroundColor(new BaseColor(0, 0, 0));
         	table.addCell(cell);
	       	
         	//
	       	for(String key2 : recapMap.keySet()){	
	       		RazDetail recap = recapMap.get(key2);
	       		 
	    		cell = PdfUtil.getCell(document, recap.getLibelle(), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	         	table.addCell(cell);
	         	cell = PdfUtil.getCell(document, (recap.getQuantite()!=null && recap.getQuantite()>0?""+recap.getQuantite():""), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	        	table.addCell(cell);
	        	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(recap.getMontant()), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	        	table.addCell(cell);
	        	
	        	ttlMtt = BigDecimalUtil.add(ttlMtt, recap.getMontant());
	       	 }
	       	 
	       	cell = PdfUtil.getCell(document, "TOTAL", Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
	       	cell.setColspan(2);
	       	cell.setBackgroundColor(PdfUtil.GRIS_CLAIR);
        	table.addCell(cell);
        	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(ttlMtt), Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
        	cell.setBackgroundColor(PdfUtil.GRIS_CLAIR);
        	table.addCell(cell);
		}
		
		document.add(table);
	}

}
