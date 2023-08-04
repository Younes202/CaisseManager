package appli.model.domaine.stock.service.impl;

import java.io.IOException;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.persistant.FamillePersistant;
import framework.model.util.PdfBean;
import framework.model.util.PdfUtil;

public class FicheInventairePDF {

	/**
	 * 
	 * 
	 * 
	 * 
	 * @param dest
	 * @throws IOException
	 * @throws DocumentException
	 */

	public static PdfBean createPdf(String fileName, List<ArticlePersistant> listArticlesView) throws IOException, DocumentException {

		PdfBean pdfDocument = PdfUtil.creerDocument(fileName);
		Document document = pdfDocument.getDocument();
		document.open();

		
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
		Paragraph paraA = new Paragraph("Saisisseur: ", PdfUtil.FONT_14_NORMAL_BLACK);
		PdfPTable tableInfo = new PdfPTable(4);
		PdfPCell cella = new PdfPCell();
		cella.setBorderWidthRight(0f);
		cella.setFixedHeight(50f);
		cella.addElement(paraA);
		cella.setPaddingLeft(10f);
		cella.setPaddingTop(10f);
		
		PdfPCell cellb = new PdfPCell();
		cellb.setBorderWidthRight(0f);
		cellb.setBorderWidthLeft(0f);
		cellb.setPaddingTop(15f);
		cella.setPaddingLeft(10f);
		
		Paragraph paraC = new Paragraph("Emplacement: ", PdfUtil.FONT_14_NORMAL_BLACK);
		PdfPCell cellc = new PdfPCell();
		cellc.addElement(paraC);
		cellc.setBorderWidthRight(0f);
		cellc.setBorderWidthLeft(0f);
		cellc.setPaddingLeft(10f);
		cellc.setPaddingTop(10f);
		
		PdfPCell celld = new PdfPCell();
		celld.setBorderWidthLeft(0f);
		celld.setPaddingTop(25f);
		cella.setPaddingLeft(10f);
		tableInfo.addCell(cella);
		tableInfo.addCell(cellb);
		tableInfo.addCell(cellc);
		tableInfo.addCell(celld);
		tableInfo.setSpacingAfter(20f);
		tableInfo.setWidthPercentage(100);

		// ---------- TABLE ARTICLES TITRE -----------
		PdfPTable tableArticles = new PdfPTable(4);
		tableArticles.setWidthPercentage(100);
		float[] widths = {55f, 15f, 15f, 15f};
		tableArticles.setWidths(widths);// largeur par cellule
		
		Paragraph paraD = new Paragraph("Article ", PdfUtil.FONT_12_BOLD_BLACK);
		cell = new PdfPCell();
		cell.addElement(paraD);
		cell.setPaddingLeft(10f);
		cell.setPaddingBottom(10f);
		tableArticles.addCell(cell);
		
		tableArticles.addCell(PdfUtil.getCell(document, " --/--/---- ", Element.ALIGN_CENTER, PdfUtil.FONT_12_NORMAL_BLACK));
		tableArticles.addCell(PdfUtil.getCell(document, " --/--/---- ", Element.ALIGN_CENTER, PdfUtil.FONT_12_NORMAL_BLACK));
		tableArticles.addCell(PdfUtil.getCell(document, " --/--/---- ", Element.ALIGN_CENTER, PdfUtil.FONT_12_NORMAL_BLACK));
		
		// ---------- TABLE ARTICLES BODY -----------
		PdfPTable tableArticlesBody = new PdfPTable(4);
		tableArticlesBody.setWidthPercentage(100);
		float[] widths2 = {55f, 15f, 15f, 15f};
		tableArticlesBody.setWidths(widths2);// largeur par cellule
		
		// create three rows
		// ajouter les articles
		List<FamillePersistant> oldfamStr = null;
		for (ArticlePersistant articleVP : listArticlesView) {
			List<FamillePersistant> familleParent = articleVP.getFamilleStr();
			// Ajout des familles
			for (int i = 0; i < familleParent.size(); i++) {
				if(oldfamStr == null || i > (oldfamStr.size()-1) || familleParent.get(i).getId() != oldfamStr.get(i).getId()){
					cell = new PdfPCell();
					cell.setColspan(4);
					cell.setPaddingLeft(familleParent.get(i).getLevel()*4);
					cell.addElement(new Paragraph(familleParent.get(i).getCode() + "-" + familleParent.get(i).getLibelle().replaceAll("#", ""), PdfUtil.FONT_10_NORMAL_BLACK));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
					//
					tableArticlesBody.addCell(cell);
				}
			}
			
			oldfamStr = familleParent;
			
			cell = new PdfPCell();
			cell = PdfUtil.getCell(document, articleVP.getCode() + "-" + articleVP.getLibelleDataVal(), PdfUtil.FONT_10_NORMAL_BLACK);
			cell.setFixedHeight(25f);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			tableArticlesBody.addCell(cell);
			
			tableArticlesBody.addCell(PdfUtil.getCellVide(document));
			tableArticlesBody.addCell(PdfUtil.getCellVide(document));
			tableArticlesBody.addCell(PdfUtil.getCellVide(document));
		}

		// ajouter les différents tables au document
		document.add(tableTitre);
		document.add(tableInfo);
		document.add(tableArticles);
		document.add(tableArticlesBody);
		//
		document.close();

		return pdfDocument;
	}
}
