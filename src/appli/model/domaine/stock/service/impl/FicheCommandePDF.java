package appli.model.domaine.stock.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.stock.persistant.MouvementArticlePersistant;
import appli.model.domaine.stock.persistant.MouvementPersistant;
import appli.model.domaine.stock.service.IMouvementService;
import framework.controller.ContextGloabalAppli;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.util.PdfBean;
import framework.model.util.PdfUtil;
import framework.model.util.PdfUtil.EnumBorder;

public class FicheCommandePDF {
	/**
	 * @param dest
	 * @throws IOException
	 * @throws DocumentException
	 */
	public File createPdf(Long mvmId) {
		PdfBean pdfBean = null;
		Document document = null;
		
		IMouvementService mvmService = ServiceUtil.getBusinessBean(IMouvementService.class);
		MouvementPersistant mouvementP = mvmService.findById(mvmId);
		
		try{
			pdfBean = PdfUtil.creerDocument("commande");
			document = pdfBean.getDocument();
			document.open();
			
			ajouterEntete(document, mouvementP.getOpc_etablissement());
			ajouterContenu(document, mouvementP);
			
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
	private void ajouterEntete(Document document, EtablissementPersistant pharmaP) throws DocumentException {
		IMouvementService mouvementService = (IMouvementService)ServiceUtil.getBusinessBean(IMouvementService.class);
		// Ajouter entête standard ---------------------
	    PdfPTable table = new PdfPTable(3);
	    table.setWidthPercentage(100f);
	    float[] widths = {40f, 20f, 40f};
		table.setWidths(widths);// largeur par cellule
		
		EtablissementPersistant restaurantP = ContextGloabalAppli.getEtablissementBean();
		
		// ---------------Ligne 1------------------------
		table.addCell (PdfUtil.getCellNoBorder(document, restaurantP.getRaison_sociale(), PdfUtil.FONT_16_BOLD_BLACK));
		table.addCell (PdfUtil.getCellVideNoBorder(document));
		
		// Logo
		Map<String, byte[]> imagep = mouvementService.getDataImage(restaurantP.getId(), "restau");
		PdfPCell cell = null;
		if(imagep.size() > 0){
			try{
				Image image = Image.getInstance(imagep.entrySet().iterator().next().getValue());
				image.scaleAbsolute(Float.valueOf(150), Float.valueOf(45));
				cell = new PdfPCell(image);
			} catch(Exception e){
				e.printStackTrace();
			}
		} else{
			cell = PdfUtil.getCellVideNoBorder(document);
		}
		
		cell.setRowspan(2);
		table.addCell (cell);
		
		// ---------------Ligne 2------------------------
		table.addCell (PdfUtil.getCellNoBorder(document, restaurantP.getAdresse(), PdfUtil.FONT_11_NORMAL_BLACK));
		table.addCell (PdfUtil.getCellVideNoBorder(document));
		
		document.add(table);
		
		PdfUtil.ajouterLigneVide(document, 1);
		
		// ------------- ligne 2 --------------------------
		table = new PdfPTable(4);
	    table.setWidthPercentage(100f);
	    float[] widths2 = {20f, 30f, 10f, 40f};
		table.setWidths(widths2);
		
		table.addCell(PdfUtil.getCellNoBorder(document, "RCS", PdfUtil.FONT_11_NORMAL_BLACK));
		table.addCell(PdfUtil.getCellNoBorder(document, restaurantP.getNumero_rcs(), PdfUtil.FONT_11_NORMAL_BLACK));
		// Cellule centre
		table.addCell (PdfUtil.getCellVideNoBorder(document));
		//
		cell = PdfUtil.getCellNoBorder(document, "FICHE COMMANDE", PdfUtil.FONT_11_NORMAL_BLACK);
		cell.setRowspan(2);
		table.addCell(cell);
		
		//--------------------------------------------------------
		table.addCell(PdfUtil.getCellNoBorder(document, "Numéro TVA", PdfUtil.FONT_11_NORMAL_BLACK));
		table.addCell(PdfUtil.getCellNoBorder(document, restaurantP.getNumero_tva(), PdfUtil.FONT_11_NORMAL_BLACK));		
		table.addCell (PdfUtil.getCellVideNoBorder(document));
		
		document.add(table);
		
		PdfUtil.ajouterLigneVide(document, 2);
	}

	/**
	 * @param document
	 * @param cupP
	 */
	private void ajouterContenu(Document document, MouvementPersistant mvmP) throws DocumentException {
		boolean isChiffre = BooleanUtil.isTrue(mvmP.getIs_chiffre());
		
		// ---------- TABLE UTILISATEUR -----------
		PdfPTable tableInfo = new PdfPTable(5);
		PdfPCell cell = new PdfPCell();
		tableInfo.setSpacingAfter(20f);
		tableInfo.setWidthPercentage(100);
		float[] widths = {20f, 20f, 7f, 23f, 20f};
		tableInfo.setWidths(widths);
		
		cell = PdfUtil.getCell(document, "Date commande :", PdfUtil.FONT_11_NORMAL_BLACK);
		PdfUtil.effacerBordure(cell, EnumBorder.BORDER_BOTTOM, EnumBorder.BORDER_RIGHT);
		cell.setPaddingTop(10f);
		cell.setPaddingLeft(10f);
		tableInfo.addCell(cell);
		
		cell = PdfUtil.getCell(document, DateUtil.dateToString(mvmP.getDate_mouvement()), PdfUtil.FONT_11_NORMAL_BLACK);
		PdfUtil.effacerBordure(cell, EnumBorder.BORDER_BOTTOM, EnumBorder.BORDER_RIGHT, EnumBorder.BORDER_LEFT);
		cell.setPaddingTop(10f);
		cell.setPaddingLeft(10f);
		tableInfo.addCell(cell);
		
		cell = PdfUtil.getCellVide(document);
		PdfUtil.effacerBordure(cell, EnumBorder.BORDER_LEFT, EnumBorder.BORDER_RIGHT, EnumBorder.BORDER_BOTTOM);
		cell.setPaddingLeft(10f);
		cell.setPaddingLeft(10f);
		tableInfo.addCell(cell);
		
		cell = PdfUtil.getCell(document, "Numéro de commande :", PdfUtil.FONT_11_NORMAL_BLACK);
		PdfUtil.effacerBordure(cell, EnumBorder.BORDER_LEFT, EnumBorder.BORDER_RIGHT, EnumBorder.BORDER_BOTTOM);
		cell.setPaddingTop(10f);
		cell.setPaddingLeft(10f);
		tableInfo.addCell(cell);
		
		cell = PdfUtil.getCell(document, mvmP.getNum_bl(), PdfUtil.FONT_11_NORMAL_BLACK);
		PdfUtil.effacerBordure(cell, EnumBorder.BORDER_BOTTOM, EnumBorder.BORDER_LEFT);
		cell.setPaddingTop(10f);
		cell.setPaddingLeft(10f);
		tableInfo.addCell(cell);
		
		cell = PdfUtil.getCell(document, "Fournisseur :", PdfUtil.FONT_11_NORMAL_BLACK);
		PdfUtil.effacerBordure(cell, EnumBorder.BORDER_TOP, EnumBorder.BORDER_RIGHT);
		cell.setPaddingTop(10f);
		cell.setPaddingBottom(10f);
		cell.setPaddingLeft(10f);
		tableInfo.addCell(cell);
		
		cell = PdfUtil.getCell(document, mvmP.getOpc_fournisseur().getLibelle(), PdfUtil.FONT_11_NORMAL_BLACK);
		PdfUtil.effacerBordure(cell, EnumBorder.BORDER_LEFT, EnumBorder.BORDER_RIGHT, EnumBorder.BORDER_TOP);
		cell.setPaddingTop(10f);
		cell.setPaddingBottom(10f);
		cell.setPaddingLeft(10f);
		tableInfo.addCell(cell);
		
		cell = PdfUtil.getCellVide(document);
		cell.setPaddingBottom(10f);
		PdfUtil.effacerBordure(cell, EnumBorder.BORDER_LEFT, EnumBorder.BORDER_RIGHT, EnumBorder.BORDER_TOP);
		tableInfo.addCell(cell);
		tableInfo.addCell(cell);
		cell = PdfUtil.getCellVide(document);
		cell.setPaddingBottom(10f);
		PdfUtil.effacerBordure(cell, EnumBorder.BORDER_LEFT, EnumBorder.BORDER_TOP);
		tableInfo.addCell(cell);
		
		document.add(tableInfo);
		
		// ---------- TABLE ARTICLES TITRE -----------
		PdfPTable tableArticles = new PdfPTable(isChiffre ? 3 : 2);
		tableArticles.setWidthPercentage(100);
		
		if(isChiffre){
			widths = new float[]{60f, 20f, 20f};	
		} else{
			widths = new float[]{80f, 20f};
		}
		
		tableArticles.setWidths(widths);// largeur par cellule
		
		cell = PdfUtil.getCell(document, "Article", Element.ALIGN_LEFT, PdfUtil.FONT_10_BOLD_BLACK);
		cell.setBackgroundColor(PdfUtil.GRIS_CLAIR);
		tableArticles.addCell(cell);
		cell = PdfUtil.getCell(document, "Quantité", Element.ALIGN_CENTER, PdfUtil.FONT_10_BOLD_BLACK);
		cell.setBackgroundColor(PdfUtil.GRIS_CLAIR);
		tableArticles.addCell(cell);
		
		if(isChiffre){
			cell = PdfUtil.getCell(document, "Prix", Element.ALIGN_CENTER, PdfUtil.FONT_10_BOLD_BLACK);
			cell.setBackgroundColor(PdfUtil.GRIS_CLAIR);
			tableArticles.addCell(cell);
		}

		List<FamillePersistant> oldfamStr = null;
		for (MouvementArticlePersistant cmdDetP : mvmP.getList_article()) {
			List<FamillePersistant> familleParent = cmdDetP.getOpc_article().getFamilleStr();
			// Ajout des familles
			for (int i = 0; i < familleParent.size(); i++) {
				if(oldfamStr == null || i > (oldfamStr.size()-1) || familleParent.get(i).getId() != oldfamStr.get(i).getId()){
					cell = new PdfPCell();
					cell = PdfUtil.getCell(document, familleParent.get(i).getCode() + "-" + familleParent.get(i).getLibelle().replaceAll("#", ""), Element.ALIGN_LEFT, PdfUtil.FONT_9_NORMAL_BLACK);
					cell.setPaddingLeft(familleParent.get(i).getLevel()*4);
					cell.setColspan(isChiffre ? 3 : 2);
					cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
					//
					tableArticles.addCell(cell);
				}
			}
			
			oldfamStr = familleParent;
			
			cell = new PdfPCell();
			cell = PdfUtil.getCell(document, cmdDetP.getOpc_article().getLibelle(), Element.ALIGN_LEFT, PdfUtil.FONT_9_NORMAL_BLACK);
			cell.setPaddingLeft(10f);
			tableArticles.addCell(cell);
			
			cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(cmdDetP.getQuantite()), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
			tableArticles.addCell(cell);
			
			
			if(isChiffre){
				cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(cmdDetP.getPrix_ttc()), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
				tableArticles.addCell(cell);
			}
		}
		
		document.add(tableArticles);
		document.close();
	}
}