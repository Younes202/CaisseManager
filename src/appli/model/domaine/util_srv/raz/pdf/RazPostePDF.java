package appli.model.domaine.util_srv.raz.pdf;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import appli.model.domaine.stock.service.impl.RepartitionBean;
import appli.model.domaine.util_srv.raz.RazDetail;
import appli.model.domaine.util_srv.raz.RazService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.DateUtil;
import framework.model.util.PdfBean;
import framework.model.util.PdfUtil;
import framework.model.util.PdfUtil.EnumBorder;

public class RazPostePDF {
	
	/**
	 * @param dest
	 * @throws IOException
	 * @throws DocumentException
	 */
	public File getPdf(String titre, String date, Map<String, List<RazDetail>> mapModePaiement, Map<String, Map<Long, RepartitionBean>> mapRep) {
		PdfBean pdfBean = null;
		Document document = null;
		
		try{
			pdfBean = PdfUtil.creerDocument("raz_poste_"+DateUtil.dateToString(new Date(), "dd-MM-yyyy"));
			document = pdfBean.getDocument();
			
			ajouterEntete(document, titre, date);
			ajouterContenu(document, mapModePaiement, mapRep);	
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
	private void ajouterContenu(Document document, Map<String, List<RazDetail>> mapModePaiement, Map<String, Map<Long, RepartitionBean>> mapRep) throws DocumentException {
	    PdfPTable table = new PdfPTable(3);
	    table.setWidthPercentage(100f);
	    float[] widths2 = {60f, 20f, 20f};
		table.setWidths(widths2);// largeur par cellule
		
		PdfPCell cell = PdfUtil.getCell(document, "MODES DE PAIEMENT", Element.ALIGN_CENTER, PdfUtil.FONT_10_BOLD_BLACK);
		cell.setColspan(3);
		cell.setBackgroundColor(PdfUtil.GRIS_CLAIR);
        table.addCell(cell);
		
        cell = PdfUtil.getCell(document, "MODE DE PAIEMENT", Element.ALIGN_LEFT, PdfUtil.FONT_9_NORMAL_BLACK);
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
		for (String caisse : mapModePaiement.keySet()) {
			cell = PdfUtil.getCell(document, caisse, Element.ALIGN_LEFT, PdfUtil.FONT_9_NORMAL_BLACK);
			cell.setColspan(3);
         	table.addCell(cell);
         	
			List<RazDetail> listDet = mapModePaiement.get(caisse);
			for (RazDetail recap : listDet) {
	    		cell = PdfUtil.getCell(document, recap.getLibelle(), Element.ALIGN_LEFT, PdfUtil.FONT_9_NORMAL_BLACK);
	         	table.addCell(cell);
	         	
	         	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(recap.getQuantite()), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
	        	table.addCell(cell);
	        	
	        	cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(recap.getMontant()), Element.ALIGN_RIGHT, PdfUtil.FONT_9_BOLD_BLACK);
	         	table.addCell(cell);
	         	
	         	ttlQte = ttlQte + recap.getQuantite();
		      	ttlMtt = BigDecimalUtil.add(ttlMtt, recap.getMontant());
			}
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
    	
    	// Détail articles
    	
		table = new PdfPTable(2);
	    table.setWidthPercentage(100f);
	    float[] widths3 = {70f, 30f};
		table.setWidths(widths3);// largeur par cellule
		
		cell = PdfUtil.getCell(document, "DETAIL ARTICLES", Element.ALIGN_CENTER, PdfUtil.FONT_10_BOLD_BLACK);
		cell.setColspan(2);
		cell.setBackgroundColor(PdfUtil.GRIS_CLAIR);
        table.addCell(cell);
		
		
		// Ajout articles menus manquants
		for (String caisse : mapRep.keySet()) {
		    Map<Long, RepartitionBean> artsMap = mapRep.get(caisse);
		    
		    cell = PdfUtil.getCell(document, caisse, Element.ALIGN_LEFT, PdfUtil.FONT_10_BOLD_BLACK);
			cell.setColspan(2);
			cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
	        table.addCell(cell);
	        
			if(artsMap.size() > 0){
		        String oldFam = null;
		        for(Long key : artsMap.keySet()){
					 RepartitionBean repB = artsMap.get(key);
					 if(oldFam == null || !oldFam.equals(repB.getFamille())){
						 cell = PdfUtil.getCell(document, repB.getFamille(), Element.ALIGN_LEFT, PdfUtil.FONT_10_BOLD_BLACK);
						 cell.setBackgroundColor(PdfUtil.JAUNE_LEGER);
						 cell.setColspan(2);
						 table.addCell(cell);
					 }
					 
		        	 cell = PdfUtil.getCell(document, repB.getLibelle(), Element.ALIGN_LEFT, PdfUtil.FONT_10_NORMAL_BLACK);
		             table.addCell(cell);
		             
		             cell = PdfUtil.getCell(document, ""+RazService.getQteFormatted(repB.getQuantite()), Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
		             table.addCell(cell);
		             
		             oldFam = repB.getFamille();
		         }
			 }		
		}
		
		document.add(table);
	}
}
