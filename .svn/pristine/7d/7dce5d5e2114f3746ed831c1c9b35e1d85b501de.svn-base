package appli.model.domaine.util_srv.raz.pdf;

import java.io.File;
import java.io.IOException;
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

public class RazLivraisonPDF {
	
	/**
	 * @param dest
	 * @throws IOException
	 * @throws DocumentException
	 */
	public File getPdf(String titre, String dateTxt, List<RazDetail> data) {
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
		PdfPTable table = new PdfPTable(3);
	    table.setWidthPercentage(100);
	    float[] widths = {10f, 80f, 10f};
		table.setWidths(widths);// largeur par cellule
		
		PdfPCell cell = PdfUtil.getCellVide(document);
		PdfUtil.effacerBordure(cell, EnumBorder.NO_BORDER);
		table.addCell(cell);
		
		cell = PdfUtil.getCell(document, titre, Element.ALIGN_CENTER, PdfUtil.FONT_14_BOLD_BLACK);
		cell.setBackgroundColor(PdfUtil.GRIS_CLAIR);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setFixedHeight(25);
		table.addCell(cell);
		
		cell = PdfUtil.getCell(document, dateTxt, Element.ALIGN_CENTER, PdfUtil.FONT_12_NORMAL_BLACK);
		cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setFixedHeight(25);
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
	    PdfPTable table = new PdfPTable(4);
	    table.setWidthPercentage(100f);
	    float[] widths2 = {40f, 20f, 20f, 20f};
		table.setWidths(widths2);// largeur par cellule
		
        PdfPCell cell = PdfUtil.getCell(document, "Livreur :", Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
     	table.addCell(cell);
     	cell = PdfUtil.getCell(document, "Quantité", Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
    	table.addCell(cell);
    	cell = PdfUtil.getCell(document, "Montant", Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
    	table.addCell(cell);
    	cell = PdfUtil.getCell(document, "Marge", Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
    	table.addCell(cell);
    	
    	for (RazDetail recap : data) {
    		cell = PdfUtil.getCell(document, recap.getLibelle(), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
         	table.addCell(cell);
         	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(recap.getQuantite()), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
        	table.addCell(cell);
        	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(recap.getMontant()), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
         	table.addCell(cell);
         	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(recap.getMontant2()), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
        	table.addCell(cell);
		}
	}

}
