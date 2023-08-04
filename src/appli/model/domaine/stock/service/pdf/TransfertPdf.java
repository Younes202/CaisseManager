package appli.model.domaine.stock.service.pdf;

import java.io.IOException;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import appli.model.domaine.stock.dao.IMouvementDao;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.stock.persistant.MouvementArticlePersistant;
import appli.model.domaine.stock.persistant.MouvementPersistant;
import appli.model.domaine.stock.service.IFamilleService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.util.PdfBean;
import framework.model.util.PdfUtil;
import framework.model.util.PdfUtil.EnumBorder;

public class TransfertPdf {

	public PdfBean createPdf(Long mvmId) {
		try {
			return (mvmId!=null?createTransfertPdf(mvmId):createTransfertPdf());
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @param dest
	 * @throws IOException
	 * @throws DocumentException
	 */

	private PdfBean createTransfertPdf(Long mvmId) throws DocumentException, IOException {
		IMouvementDao mouvementDao = (IMouvementDao) ServiceUtil.getBusinessBean(IMouvementDao.class);
		IFamilleService familleService = (IFamilleService) ServiceUtil.getBusinessBean(IFamilleService.class);
		 
		MouvementPersistant mvm = mouvementDao.findById(mvmId);
		List<MouvementArticlePersistant> lisDetail = mouvementDao.getQuery("from MouvementArticlePersistant where opc_mouvement.id=:mvmId order by opc_article.opc_famille_stock.b_left")
				.setParameter("mvmId", mvmId)
				.getResultList();
		
		for (MouvementArticlePersistant articlVP : mvm.getList_article()) {
			List<FamillePersistant> familleStr = familleService.getFamilleParent("ST", articlVP.getOpc_article().getOpc_famille_stock().getId());
			articlVP.getOpc_article().setFamilleStr(familleStr);
		}
		
		PdfBean pdfDocument = PdfUtil.creerDocument("transfert");
		Document document = pdfDocument.getDocument();
		document.open();

		
		// ***************TABLES***************************
		// ---------- TABLE TITRE -----------
		PdfPTable tableTitre = new PdfPTable(1);
		tableTitre.setWidthPercentage(50);

		Paragraph para = new Paragraph("Fiche transfert", PdfUtil.FONT_16_BOLD_BLACK);
		para.setAlignment(Element.ALIGN_CENTER);
		
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
		cell.setFixedHeight(40);
		cell.setVerticalAlignment(Element.ALIGN_CENTER);
		cell.addElement(para);
		tableTitre.addCell(cell);
		tableTitre.setHorizontalAlignment(10);
		tableTitre.setSpacingAfter(30f);

		// ---------- TABLE UTILISATEUR -----------
		PdfPTable tableInfo = new PdfPTable(2);
		float[] widths = {50f, 50f};
		tableInfo.setWidths(widths);
		
		PdfPCell cella = PdfUtil.getCell(document, "Date mouvement : "+DateUtil.dateToString(mvm.getDate_mouvement()), PdfUtil.FONT_10_NORMAL_BLACK);
		PdfUtil.effacerBordure(cella, EnumBorder.BORDER_RIGHT, EnumBorder.BORDER_BOTTOM);
		cella.setBackgroundColor(PdfUtil.GRIS_CLAIR);
		cella.setPaddingTop(10);
		
		PdfPCell cellc = PdfUtil.getCell(document, "N° transfert : "+mvm.getNum_bl(), PdfUtil.FONT_10_NORMAL_BLACK);
		PdfUtil.effacerBordure(cellc, EnumBorder.BORDER_LEFT, EnumBorder.BORDER_BOTTOM);
		cellc.setBackgroundColor(PdfUtil.GRIS_CLAIR);
		cellc.setPaddingTop(10);
		
		tableInfo.addCell(cella);
		tableInfo.addCell(cellc);
		
		//----------------------------------------------------------------------------------------
		cella = PdfUtil.getCell(document, "Stock origine : "+mvm.getOpc_emplacement().getTitre(), PdfUtil.FONT_10_NORMAL_BLACK);
		PdfUtil.effacerBordure(cella, EnumBorder.BORDER_RIGHT, EnumBorder.BORDER_TOP);
		cella.setBackgroundColor(PdfUtil.GRIS_CLAIR);
		cella.setPaddingBottom(10);
		
		cellc = PdfUtil.getCell(document, "Stock destination : "+mvm.getOpc_destination().getTitre(), PdfUtil.FONT_10_NORMAL_BLACK);
		PdfUtil.effacerBordure(cellc, EnumBorder.BORDER_LEFT, EnumBorder.BORDER_TOP);
		cellc.setBackgroundColor(PdfUtil.GRIS_CLAIR);
		cellc.setPaddingBottom(10);
		
		tableInfo.addCell(cella);
		tableInfo.addCell(cellc);
		
		tableInfo.setSpacingAfter(20f);
		tableInfo.setWidthPercentage(100);

		// ---------- TABLE ARTICLES TITRE -----------
		PdfPTable tableArticles = new PdfPTable(2);
		tableArticles.setWidthPercentage(100);
		float[] widths2 = {85f, 15f};
		tableArticles.setWidths(widths2);// largeur par cellule
		
		Paragraph paraD = new Paragraph("Article ", PdfUtil.FONT_12_BOLD_BLACK);
		cell = new PdfPCell();
		cell.addElement(paraD);
		cell.setPaddingLeft(10f);
		cell.setPaddingBottom(10f);
		tableArticles.addCell(cell);
		
		tableArticles.addCell(PdfUtil.getCell(document, "Quantité", Element.ALIGN_CENTER, PdfUtil.FONT_12_NORMAL_BLACK));
		
		// ---------- TABLE ARTICLES BODY -----------
		PdfPTable tableArticlesBody = new PdfPTable(2);
		tableArticlesBody.setWidthPercentage(100);
		float[] widths3 = {85f, 15f};
		tableArticlesBody.setWidths(widths3);// largeur par cellule
		
		// create three rows
		// ajouter les articles
		List<FamillePersistant> oldfamStr = null;
		for (MouvementArticlePersistant articleVP : lisDetail) {
			ArticlePersistant opc_article = articleVP.getOpc_article();
			List<FamillePersistant> familleParent = opc_article.getFamilleStr();
			// Ajout des familles
			for (int i = 0; i < familleParent.size(); i++) {
				if(oldfamStr == null || i > (oldfamStr.size()-1) || familleParent.get(i).getId() != oldfamStr.get(i).getId()){
					cell = new PdfPCell();
					cell.setColspan(2);
					cell.setPaddingLeft(familleParent.get(i).getLevel()*4);
					cell.addElement(new Paragraph(familleParent.get(i).getCode() + "-" + familleParent.get(i).getLibelle(), PdfUtil.FONT_9_NORMAL_BLACK));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
					//
					tableArticlesBody.addCell(cell);
				}
			}
			
			oldfamStr = familleParent;
			
			cell = new PdfPCell();
			cell = PdfUtil.getCell(document, opc_article.getCode() + "-" + opc_article.getLibelle(), PdfUtil.FONT_9_NORMAL_BLACK);
			cell.setFixedHeight(25f);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			tableArticlesBody.addCell(cell);
			
			tableArticlesBody.addCell(PdfUtil.getCell(document, BigDecimalUtil.formatNumber(articleVP.getQuantite()), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK));
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
	
	/**
	 * @param dest
	 * @throws IOException
	 * @throws DocumentException
	 */

	private PdfBean createTransfertPdf() throws DocumentException, IOException {
		PdfBean pdfDocument = PdfUtil.creerDocument("transfert");
		Document document = pdfDocument.getDocument();
		document.open();

		
		// ***************TABLES***************************
		// ---------- TABLE TITRE -----------
		PdfPTable tableTitre = new PdfPTable(1);
		tableTitre.setWidthPercentage(50);

		Paragraph para = new Paragraph("Fiche transfert", PdfUtil.FONT_16_BOLD_BLACK);
		para.setAlignment(Element.ALIGN_CENTER);
		
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
		cell.setFixedHeight(40);
		cell.setVerticalAlignment(Element.ALIGN_CENTER);
		cell.addElement(para);
		tableTitre.addCell(cell);
		tableTitre.setHorizontalAlignment(10);
		tableTitre.setSpacingAfter(30f);

		// ---------- TABLE UTILISATEUR -----------
		PdfPTable tableInfo = new PdfPTable(2);
		float[] widths = {50f, 50f};
		tableInfo.setWidths(widths);
		
		PdfPCell cella = PdfUtil.getCell(document, "Date mouvement : ", PdfUtil.FONT_10_NORMAL_BLACK);
		PdfUtil.effacerBordure(cella, EnumBorder.BORDER_RIGHT, EnumBorder.BORDER_BOTTOM);
		cella.setBackgroundColor(PdfUtil.GRIS_CLAIR);
		cella.setPaddingTop(10);
		
		PdfPCell cellc = PdfUtil.getCell(document, "N° transfert : ", PdfUtil.FONT_10_NORMAL_BLACK);
		PdfUtil.effacerBordure(cellc, EnumBorder.BORDER_LEFT, EnumBorder.BORDER_BOTTOM);
		cellc.setBackgroundColor(PdfUtil.GRIS_CLAIR);
		cellc.setPaddingTop(10);
		
		tableInfo.addCell(cella);
		tableInfo.addCell(cellc);
		
		//----------------------------------------------------------------------------------------
		cella = PdfUtil.getCell(document, "Stock origine : ", PdfUtil.FONT_10_NORMAL_BLACK);
		PdfUtil.effacerBordure(cella, EnumBorder.BORDER_RIGHT, EnumBorder.BORDER_TOP);
		cella.setBackgroundColor(PdfUtil.GRIS_CLAIR);
		cella.setPaddingBottom(10);
		
		cellc = PdfUtil.getCell(document, "Stock destination : ", PdfUtil.FONT_10_NORMAL_BLACK);
		PdfUtil.effacerBordure(cellc, EnumBorder.BORDER_LEFT, EnumBorder.BORDER_TOP);
		cellc.setBackgroundColor(PdfUtil.GRIS_CLAIR);
		cellc.setPaddingBottom(10);
		
		tableInfo.addCell(cella);
		tableInfo.addCell(cellc);
		
		tableInfo.setSpacingAfter(20f);
		tableInfo.setWidthPercentage(100);

		// ---------- TABLE ARTICLES TITRE -----------
		PdfPTable tableArticles = new PdfPTable(2);
		tableArticles.setWidthPercentage(100);
		float[] widths2 = {85f, 15f};
		tableArticles.setWidths(widths2);// largeur par cellule
		
		Paragraph paraD = new Paragraph("Article ", PdfUtil.FONT_12_BOLD_BLACK);
		cell = new PdfPCell();
		cell.addElement(paraD);
		cell.setPaddingLeft(10f);
		cell.setPaddingBottom(10f);
		tableArticles.addCell(cell);
		
		tableArticles.addCell(PdfUtil.getCell(document, "Quantité", Element.ALIGN_CENTER, PdfUtil.FONT_12_NORMAL_BLACK));
		
		// ---------- TABLE ARTICLES BODY -----------
		PdfPTable tableArticlesBody = new PdfPTable(2);
		tableArticlesBody.setWidthPercentage(100);
		float[] widths3 = {85f, 15f};
		tableArticlesBody.setWidths(widths3);// largeur par cellule
		
		// create three rows
		// ajouter les articles
		for (int i=0; i<23; i++) {
			cell = new PdfPCell();
			cell = PdfUtil.getCell(document, "", PdfUtil.FONT_9_NORMAL_BLACK);
			cell.setFixedHeight(25f);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			tableArticlesBody.addCell(cell);
			
			tableArticlesBody.addCell(PdfUtil.getCell(document, "", Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK));
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
