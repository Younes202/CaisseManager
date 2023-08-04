package appli.model.domaine.util_srv.raz.pdf;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import appli.model.domaine.util_srv.raz.RazDetail;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.DateUtil;
import framework.model.util.PdfBean;
import framework.model.util.PdfUtil;
import framework.model.util.PdfUtil.EnumBorder;

public class RazPaiementPDF {
	
	/**
	 * @param dest
	 * @throws IOException
	 * @throws DocumentException
	 */
	public File getPdf(String titre, String date, List<RazDetail> data) {
		PdfBean pdfBean = null;
		Document document = null;
		
		try{
			pdfBean = PdfUtil.creerDocument("raz_paiement_"+DateUtil.dateToString(new Date(), "dd-MM-yyyy"));
			document = pdfBean.getDocument();
			
			ajouterEntete(document, titre, date);
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
	private void ajouterContenu(Document document, List<RazDetail> data) throws DocumentException {
	    PdfPTable table = new PdfPTable(3);
	    table.setWidthPercentage(100f);
	    float[] widths2 = {60f, 20f, 20f};
		table.setWidths(widths2);// largeur par cellule
		
        PdfPCell cell = PdfUtil.getCell(document, "MODE DE PAIEMENT", Element.ALIGN_LEFT, PdfUtil.FONT_9_NORMAL_BLACK);
        cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
     	table.addCell(cell);
     	
     	cell = PdfUtil.getCell(document, "NOMBRE DE COMMANDES", Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
     	cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
    	table.addCell(cell);
    	
    	cell = PdfUtil.getCell(document, "MONTANT", Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
    	cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
    	table.addCell(cell);
    	
    	int ttlQte = 0;
		BigDecimal ttlMtt = null;
    	for (RazDetail recap : data) {
    		cell = PdfUtil.getCell(document, recap.getLibelle(), Element.ALIGN_LEFT, PdfUtil.FONT_9_NORMAL_BLACK);
         	table.addCell(cell);
         	
         	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(recap.getQuantite()), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
        	table.addCell(cell);
        	
        	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(recap.getMontant()), Element.ALIGN_RIGHT, PdfUtil.FONT_9_BOLD_BLACK);
         	table.addCell(cell);
         	
         	ttlQte = ttlQte + recap.getQuantite();
	      	ttlMtt = BigDecimalUtil.add(ttlMtt, recap.getMontant());
		}
    	
    	cell = PdfUtil.getCell(document, "", Element.ALIGN_LEFT, PdfUtil.FONT_9_NORMAL_BLACK);
     	table.addCell(cell);
     	
     	cell = PdfUtil.getCell(document, ""+ttlQte, Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
     	cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
    	table.addCell(cell);
    	
    	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(ttlMtt), Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
    	cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
     	table.addCell(cell);
     	
    	document.add(table);
	}

}
