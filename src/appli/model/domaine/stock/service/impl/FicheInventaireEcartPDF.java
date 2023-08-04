package appli.model.domaine.stock.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.stock.persistant.InventaireDetailPersistant;
import appli.model.domaine.stock.persistant.InventairePersistant;
import framework.controller.Context;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.PdfBean;
import framework.model.util.PdfUtil;
import framework.model.util.PdfUtil.EnumBorder;

public class FicheInventaireEcartPDF {

	/**
	 * @param dest
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static PdfBean createPdf(String fileName, InventairePersistant inventaire, List<InventaireDetailPersistant> listInvDetailView) throws IOException, DocumentException {
		boolean isShowEcart = Context.isOperationAvailable("ECARTINV");
		
		PdfBean pdfDocument = PdfUtil.creerDocument(fileName);
		Document document = pdfDocument.getDocument();
		document.open();
		BaseColor greenColor = new BaseColor(0,100,0);
		
		// ***************TABLES***************************
		// ---------- TABLE TITRE -----------
		PdfPTable tableTitre = new PdfPTable(1);
		tableTitre.setWidthPercentage(50);

		Paragraph para = new Paragraph("Fiche inventaire", PdfUtil.FONT_16_BOLD_BLACK);
		para.setAlignment(Element.ALIGN_CENTER);
		
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		cell.setFixedHeight(40);
		cell.setVerticalAlignment(Element.ALIGN_CENTER);
		cell.addElement(para);
		tableTitre.addCell(cell);
		tableTitre.setHorizontalAlignment(10);
		tableTitre.setSpacingAfter(30f);

		// ---------- TABLE UTILISATEUR -----------
		PdfPTable tableInfo = new PdfPTable(3);
		tableInfo.setSpacingAfter(20f);
		tableInfo.setWidthPercentage(100);
		float[] widths = {45, 10f, 45f};
		tableInfo.setWidths(widths);
		
		String saisisseur1 = (inventaire.getOpc_saisisseur()!=null?inventaire.getOpc_saisisseur().getNom()+" "+StringUtil.getValueOrEmpty(inventaire.getOpc_saisisseur().getPrenom()):"");
		cell = PdfUtil.getCell(document, "1er Saisisseur: "+saisisseur1, PdfUtil.FONT_11_NORMAL_BLACK);
		PdfUtil.effacerBordure(cell, EnumBorder.BORDER_BOTTOM, EnumBorder.BORDER_RIGHT);
		cell.setPaddingLeft(10f);
		cell.setPaddingTop(10f);
		tableInfo.addCell(cell);
		
		cell = PdfUtil.getCellVide(document);
		PdfUtil.effacerBordure(cell, EnumBorder.BORDER_LEFT, EnumBorder.BORDER_RIGHT, EnumBorder.BORDER_BOTTOM);
		cell.setPaddingLeft(10f);
		tableInfo.addCell(cell);
		
		String saisisseur2 = (inventaire.getOpc_saisisseur2()!=null?inventaire.getOpc_saisisseur2().getNom()+" "+StringUtil.getValueOrEmpty(inventaire.getOpc_saisisseur2().getPrenom()):"");
		cell = PdfUtil.getCell(document, "2em Saisisseur: "+saisisseur2, PdfUtil.FONT_11_NORMAL_BLACK);
		PdfUtil.effacerBordure(cell, EnumBorder.BORDER_LEFT, EnumBorder.BORDER_BOTTOM);
		cell.setPaddingLeft(10f);
		tableInfo.addCell(cell);
		
		
		cell = PdfUtil.getCell(document, "Emplacement: "+inventaire.getOpc_emplacement().getTitre(), PdfUtil.FONT_11_NORMAL_BLACK);
		PdfUtil.effacerBordure(cell, EnumBorder.BORDER_TOP, EnumBorder.BORDER_RIGHT);
		cell.setPaddingLeft(10f);
		tableInfo.addCell(cell);
		
		cell = PdfUtil.getCellVide(document);
		PdfUtil.effacerBordure(cell, EnumBorder.BORDER_TOP, EnumBorder.BORDER_RIGHT, EnumBorder.BORDER_LEFT);
		cell.setPaddingLeft(10f);
		tableInfo.addCell(cell);
		
		String responsable = (inventaire.getOpc_responsable()!=null?inventaire.getOpc_responsable().getNom()+" "+inventaire.getOpc_responsable().getPrenom():"");
		cell = PdfUtil.getCell(document, "Responsable: "+responsable, PdfUtil.FONT_11_NORMAL_BLACK);
		PdfUtil.effacerBordure(cell, EnumBorder.BORDER_LEFT, EnumBorder.BORDER_TOP);
		cell.setPaddingLeft(10f);
		cell.setPaddingBottom(10f);
		tableInfo.addCell(cell);
		
		// ---------- TABLE ARTICLES TITRE -----------
		PdfPTable tableArticles = new PdfPTable(isShowEcart?5:2);
		tableArticles.setWidthPercentage(100);
		widths = new float[]{75f, 25f};
		
		if(isShowEcart){
			widths = new float[]{40f, 15f, 15f, 15f, 15f};
		}
		
		tableArticles.setWidths(widths);// largeur par cellule
		
		cell = PdfUtil.getCell(document, "Article", Element.ALIGN_LEFT, PdfUtil.FONT_9_BOLD_BLACK);
		cell.setBackgroundColor(PdfUtil.GRIS_CLAIR);
		tableArticles.addCell(cell);
		
		if(isShowEcart){
			cell = PdfUtil.getCell(document, "Théorique", Element.ALIGN_CENTER, PdfUtil.FONT_9_BOLD_BLACK);
			cell.setBackgroundColor(PdfUtil.GRIS_CLAIR);
			tableArticles.addCell(cell);
			cell = PdfUtil.getCell(document, "Réel 1", Element.ALIGN_CENTER, PdfUtil.FONT_9_BOLD_BLACK);
			cell.setBackgroundColor(PdfUtil.GRIS_CLAIR);
			tableArticles.addCell(cell);
			cell = PdfUtil.getCell(document, "Réel 2", Element.ALIGN_CENTER, PdfUtil.FONT_9_BOLD_BLACK);
			cell.setBackgroundColor(PdfUtil.GRIS_CLAIR);
			tableArticles.addCell(cell);
			cell = PdfUtil.getCell(document, "Ecart", Element.ALIGN_CENTER, PdfUtil.FONT_9_BOLD_BLACK);
			cell.setBackgroundColor(PdfUtil.GRIS_CLAIR);
			tableArticles.addCell(cell);
		}
		// ajouter les articles
		List<FamillePersistant> oldfamStr = null;
		for (InventaireDetailPersistant invDetailP : listInvDetailView) {
			List<FamillePersistant> familleParent = invDetailP.getFamilleStr();
			// Ajout des familles
			for (int i = 0; i < familleParent.size(); i++) {
				if(oldfamStr == null || i > (oldfamStr.size()-1) || familleParent.get(i).getId() != oldfamStr.get(i).getId()){
					cell = PdfUtil.getCell(document, familleParent.get(i).getCode() + "-" + familleParent.get(i).getLibelle(), Element.ALIGN_LEFT, PdfUtil.FONT_9_NORMAL_BLACK);
					cell.setColspan(isShowEcart?5:2);
					cell.setPaddingLeft(familleParent.get(i).getLevel()*4);
					cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
					tableArticles.addCell(cell);
				}
			}
			
			oldfamStr = familleParent;
			
			ArticlePersistant opc_article = invDetailP.getOpc_article();
			cell = PdfUtil.getCell(document, opc_article.getCode() + "-" + opc_article.getLibelleDataVal(), PdfUtil.FONT_9_NORMAL_BLACK);
			cell.setFixedHeight(25f);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			tableArticles.addCell(cell);
			
			if(isShowEcart){
				BigDecimal reel_0 = invDetailP.getQte_reel_0() == null ? invDetailP.getQte_reel() : invDetailP.getQte_reel_0();
				BigDecimal reel = invDetailP.getQte_reel_0() == null ? null : invDetailP.getQte_reel();
				
				cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(invDetailP.getQte_theorique()), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				tableArticles.addCell(cell);
				cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(reel_0), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				tableArticles.addCell(cell);
				cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(reel), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				tableArticles.addCell(cell);
				
				Font font = new Font(Font.getFamily("TIMES_ROMAN"), 10, Font.NORMAL);
				font.setColor(invDetailP.getQte_ecart().compareTo(BigDecimalUtil.ZERO)<0 ? BaseColor.RED : greenColor);
				cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(invDetailP.getQte_ecart()), Element.ALIGN_RIGHT, font);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				tableArticles.addCell(cell);
			}
		}

		// ajouter les différents tables au document
		document.add(tableTitre);
		document.add(tableInfo);
		document.add(tableArticles);
		//
		document.close();

		return pdfDocument;
	}
}
