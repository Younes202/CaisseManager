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

import appli.model.domaine.stock.service.impl.RepartitionBean;
import appli.model.domaine.util_srv.raz.RazService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.DateUtil;
import framework.model.util.PdfBean;
import framework.model.util.PdfUtil;

public class RazArticlePDF {
	
	/**
	 * @param dest
	 * @throws IOException
	 * @throws DocumentException
	 */
	public File getPdf(String titre, String dtTxt, Map data, boolean isQteOnly) {
		PdfBean pdfBean = null;
		Document document = null;
		
		try{
			pdfBean = PdfUtil.creerDocument("raz_article_"+DateUtil.dateToString(new Date(), "dd-MM-yyyy"));
			document = pdfBean.getDocument();
			
			ajouterEntete(document, titre, dtTxt);
			ajouterContenu(document, data, isQteOnly);	
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
	private void ajouterEntete(Document document, String titre, String txtDate) throws DocumentException {
		PdfPTable table = new PdfPTable(1);
	    table.setWidthPercentage(100);
	    float[] widths = {100f};
		table.setWidths(widths);// largeur par cellule
		
		PdfPCell cell = PdfUtil.getCell(document, titre, Element.ALIGN_CENTER, PdfUtil.FONT_14_BOLD_BLACK);
		cell.setBackgroundColor(PdfUtil.GRIS_CLAIR);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setFixedHeight(25);
		table.addCell(cell);
		
		cell = PdfUtil.getCell(document, txtDate, Element.ALIGN_CENTER, PdfUtil.FONT_12_NORMAL_BLACK);
		cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
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
	private void ajouterContenu(Document document, Map data, boolean isQteOnly) throws DocumentException {
		Map<Long, RepartitionBean> menuMap = (Map<Long, RepartitionBean>) data.get("MENU");
		Map<Long, RepartitionBean> menuArtsMap = (Map<Long, RepartitionBean>) data.get("MENU_ARTS");
		Map<Long, RepartitionBean> artsMap = (Map<Long, RepartitionBean>) data.get("ARTS");
		BigDecimal offreMtt = (BigDecimal) data.get("OFFRE");
		BigDecimal livraisonMtt = (BigDecimal) data.get("LIVRAISON");
		BigDecimal mttTotalNet = (BigDecimal) data.get("VENTE_NET");
		BigDecimal mttTotal = (BigDecimal) data.get("VENTE");
		
		if(!isQteOnly){
			PdfPTable table = new PdfPTable(3);
		    table.setWidthPercentage(100f);
		    float[] widths3 = {60f, 20f, 20f};
			table.setWidths(widths3);// largeur par cellule
			
	         //
			 if(menuMap != null && menuMap.size() > 0){
				 PdfPCell cell = PdfUtil.getCell(document, "VENTES MENUS", Element.ALIGN_LEFT, PdfUtil.FONT_10_BOLD_BLACK);
					cell.setColspan(3);
					cell.setBackgroundColor(PdfUtil.GRIS_CLAIR);
			        table.addCell(cell);
				 for(Long key : menuMap.keySet()){
					 RepartitionBean repB = menuMap.get(key);
					 
		        	 cell = PdfUtil.getCell(document, repB.getLibelle(), Element.ALIGN_LEFT, PdfUtil.FONT_10_NORMAL_BLACK);
		             table.addCell(cell);
		            
		             cell = PdfUtil.getCell(document, ""+RazService.getQteFormatted(repB.getQuantite()), Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
		             table.addCell(cell);
		             
		             cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(repB.getMontant()), Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
		             table.addCell(cell);
		             
//		             mttTotal = BigDecimalUtil.add(mttTotal, repB.getMontant());
		         }
			 }
			 
			 PdfPCell cell;
			 String oldFam = null;
			if(!isQteOnly && artsMap.size() > 0){
				cell = PdfUtil.getCell(document, "VENTES ARTICLES", Element.ALIGN_LEFT, PdfUtil.FONT_10_BOLD_BLACK);
				cell.setColspan(3);
				cell.setBackgroundColor(PdfUtil.GRIS_CLAIR);
		        table.addCell(cell);
				 
		        oldFam = null;
		        for(Long key : artsMap.keySet()){
					 RepartitionBean repB = artsMap.get(key);
					 if(oldFam == null || !oldFam.equals(repB.getFamille())){
						 cell = PdfUtil.getCell(document, repB.getFamille(), Element.ALIGN_LEFT, PdfUtil.FONT_10_BOLD_BLACK);
						 cell.setBackgroundColor(PdfUtil.JAUNE_LEGER);
						 cell.setColspan(3);
						 table.addCell(cell);
					 }
					 
		        	 cell = PdfUtil.getCell(document, repB.getLibelle(), Element.ALIGN_LEFT, PdfUtil.FONT_10_NORMAL_BLACK);
		             table.addCell(cell);
		             
		             cell = PdfUtil.getCell(document, ""+RazService.getQteFormatted(repB.getQuantite()), Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
		             table.addCell(cell);
		             
		             cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(repB.getMontant()), Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
		             table.addCell(cell);
		             
		             oldFam = repB.getFamille();
//		             mttTotal = BigDecimalUtil.add(mttTotal, repB.getMontant());
		         }
			 }
			
			if(!isQteOnly){
				cell = PdfUtil.getCell(document, "TOTAL", Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
		   	 	cell.setBackgroundColor(PdfUtil.JAUNE_LEGER);
		        table.addCell(cell);
				
		        cell = PdfUtil.getCell(document, "", Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
		        cell.setBackgroundColor(PdfUtil.JAUNE_LEGER);
		        table.addCell(cell);
		        
		        cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(mttTotal), Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
		        cell.setBackgroundColor(PdfUtil.JAUNE_LEGER);
		        table.addCell(cell);
		        
				 // Offres
				if(!BigDecimalUtil.isZero(offreMtt)){
					 cell = PdfUtil.getCell(document, "OFFRES", Element.ALIGN_LEFT, PdfUtil.FONT_10_NORMAL_BLACK);
			         table.addCell(cell);
			         
			         cell = PdfUtil.getCell(document, "", Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
			         table.addCell(cell);
			         
			         cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(offreMtt), Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
			         table.addCell(cell);
				}
				
		        // Livraison
				if(!BigDecimalUtil.isZero(livraisonMtt)){
			         cell = PdfUtil.getCell(document, "LIVRAISONS", Element.ALIGN_LEFT, PdfUtil.FONT_10_NORMAL_BLACK);
			         table.addCell(cell);
			         cell = PdfUtil.getCell(document, "", Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
			         table.addCell(cell);
			         cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(livraisonMtt), Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
			         table.addCell(cell);
				}
		        
		   	 	cell = PdfUtil.getCell(document, "TOTAL NET", Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
		   	 	cell.setBackgroundColor(PdfUtil.JAUNE_LEGER);
		        table.addCell(cell);
		        
		        cell = PdfUtil.getCell(document, "", Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
		        cell.setBackgroundColor(PdfUtil.JAUNE_LEGER);
		        table.addCell(cell);
		        
		        cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(mttTotalNet), Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
		        cell.setBackgroundColor(PdfUtil.JAUNE_LEGER);
		        table.addCell(cell);
			}
			document.add(table);
		} else{
			PdfPTable table = new PdfPTable(2);
		    table.setWidthPercentage(100f);
		    float[] widths3 = {70f, 30f};
			table.setWidths(widths3);// largeur par cellule
			
			
			// Ajout articles menus manquants
	 		for(Long artId : menuArtsMap.keySet()){
	 			if(artsMap.get(artId) == null){
	 				artsMap.put(artId, menuArtsMap.get(artId));
	 			}
	 		}
			if(artsMap.size() > 0){
				PdfPCell cell = PdfUtil.getCell(document, "DETAIL ARTICLES", Element.ALIGN_LEFT, PdfUtil.FONT_10_BOLD_BLACK);
				cell.setColspan(2);
				cell.setBackgroundColor(PdfUtil.GRIS_CLAIR);
		        table.addCell(cell);
				 
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
			document.add(table);
		}
				
	}
}
