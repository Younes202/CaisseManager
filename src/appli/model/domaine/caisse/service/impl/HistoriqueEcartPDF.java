package appli.model.domaine.caisse.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import appli.model.domaine.vente.persistant.CaisseJourneePersistant;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.StrimUtil;
import framework.model.util.PdfBean;
import framework.model.util.PdfUtil;

public class HistoriqueEcartPDF {

	/**
	 * @param dest
	 * @throws IOException
	 * @throws DocumentException
	 */

	public File exportPdf(List<CaisseJourneePersistant> listCaisseMouvement, CaisseJourneePersistant totalMvm, Date dateDebut, Date dateFin) {
		PdfBean pdfBean = null;
		Document document = null;
		
		try{
			pdfBean = PdfUtil.creerDocument("ecarts");
			document = pdfBean.getDocument();
			
			ajouterTitlePage(document, listCaisseMouvement, dateDebut, dateFin);
			ajouterContenu(document, listCaisseMouvement);
			document.newPage();
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
	private void ajouterTitlePage(Document document, List<CaisseJourneePersistant> listCaisseMouvement, Date dateDebut, Date dateFin) throws DocumentException {
		// Ajouter entête standard ---------------------
	    PdfPTable table = new PdfPTable(3);
	    table.setWidthPercentage(100f);
	    float[] widths = {40f, 20f, 40f};
		table.setWidths(widths);// largeur par cellule
		
		// Titre
		Paragraph paragraph = new Paragraph("Rapport des écarts", PdfUtil.FONT_18_BOLD_BLACK);
		paragraph.setAlignment(Element.ALIGN_CENTER);
		document.add(paragraph);
		paragraph = new Paragraph("Du "+DateUtil.dateToString(dateDebut)+" au "+DateUtil.dateToString(dateFin), PdfUtil.FONT_14_NORMAL_BLACK);
		paragraph.setAlignment(Element.ALIGN_CENTER);
		document.add(paragraph);
		
		PdfUtil.ajouterLigneSeparateurPontille(document);
	}

	/**
	 * @param document
	 * @param cupP
	 */
	private void ajouterContenu(Document document, List<CaisseJourneePersistant> listJrnCaisse) throws DocumentException {
		PdfUtil.ajouterLigneSeparateurPontille(document);
		String devise = StrimUtil.getGlobalConfigPropertie("devise.symbole");
		
		// Ajouter entête standard ---------------------
	    PdfPTable table = new PdfPTable(11);
	    table.setWidthPercentage(100f);
	    float[] widths2 = {13f, 7f, 10f, 10f,10f, 10f , 10f , 10f , 9f, 9f, 9f, 9f};
		table.setWidths(widths2);// largeur par cellule
		
		// ---------------------------------------
		PdfPCell cell = PdfUtil.getCell(document, "Shift", PdfUtil.FONT_9_NORMAL_BLACK);
		cell.setBackgroundColor(new BaseColor(224, 224, 224));
		table.addCell(cell);
		
		cell = PdfUtil.getCell(document, "Nbr V", PdfUtil.FONT_9_NORMAL_BLACK);
		cell.setBackgroundColor(new BaseColor(224, 224, 224));
		table.addCell(cell);
		
		cell = PdfUtil.getCell(document, "Total", PdfUtil.FONT_9_NORMAL_BLACK);
		cell.setBackgroundColor(new BaseColor(224, 224, 224));
		table.addCell(cell);
		
		cell = PdfUtil.getCell(document, "Réduction", PdfUtil.FONT_9_NORMAL_BLACK);
		cell.setBackgroundColor(new BaseColor(224, 224, 224));
		table.addCell(cell);
		
		cell = PdfUtil.getCell(document, "Art Offer", PdfUtil.FONT_9_NORMAL_BLACK);
		cell.setBackgroundColor(new BaseColor(224, 224, 224));
		table.addCell(cell);
		
		cell = PdfUtil.getCell(document, "Réduction Article", PdfUtil.FONT_9_NORMAL_BLACK);
		cell.setBackgroundColor(new BaseColor(224, 224, 224));
		table.addCell(cell);
		
		cell = PdfUtil.getCell(document, "Annulé CMD", PdfUtil.FONT_9_NORMAL_BLACK);
		cell.setBackgroundColor(new BaseColor(224, 224, 224));
		table.addCell(cell);
		
		cell = PdfUtil.getCell(document, "Annulé Ligne", PdfUtil.FONT_9_NORMAL_BLACK);
		cell.setBackgroundColor(new BaseColor(224, 224, 224));
		table.addCell(cell);
		
		cell = PdfUtil.getCell(document, "Total Net", PdfUtil.FONT_9_NORMAL_BLACK);
		cell.setBackgroundColor(new BaseColor(224, 224, 224));
		table.addCell(cell);
		
		cell = PdfUtil.getCell(document, "Cloture", PdfUtil.FONT_9_NORMAL_BLACK);
		cell.setBackgroundColor(new BaseColor(224, 224, 224));
		table.addCell(cell);
		
		cell = PdfUtil.getCell(document, "Ecart", PdfUtil.FONT_9_NORMAL_BLACK);
		cell.setBackgroundColor(new BaseColor(224, 224, 224));
		table.addCell(cell);
		
		cell = PdfUtil.getCell(document, "Utilisateur", PdfUtil.FONT_9_NORMAL_BLACK);
		cell.setBackgroundColor(new BaseColor(224, 224, 224));
		table.addCell(cell);
		
		Long oldjour = null, oldCaisse = null;
		int idxShift = 1;
		for(CaisseJourneePersistant cJourneeP : listJrnCaisse){
			if(oldjour == null ||oldjour !=cJourneeP.getOpc_journee().getId()){
				cell = PdfUtil.getCell(document, DateUtil.dateToString(cJourneeP.getOpc_journee().getDate_journee()), PdfUtil.FONT_8_BOLD_BLACK);
				cell.setColspan(11);
				cell.setBackgroundColor(PdfUtil.JAUNE_LEGER);
				table.addCell(cell);
				idxShift = 1;
				oldCaisse = null;
			}
			if(oldCaisse == null ||oldCaisse !=cJourneeP.getOpc_caisse().getId()){
				cell = PdfUtil.getCell(document, cJourneeP.getOpc_caisse().getReference(), PdfUtil.FONT_8_BOLD_BLACK);
				cell.setColspan(11);
				cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
				cell.setPaddingLeft(10f);
				table.addCell(cell);
				idxShift = 1;
			}
			oldCaisse = cJourneeP.getOpc_caisse().getId();
			oldjour = cJourneeP.getOpc_journee().getId();
			
			cell = PdfUtil.getCell(document, "Shift "+idxShift+" ["+DateUtil.dateToString(cJourneeP.getDate_ouverture(), "HH:mm:ss")+"]", PdfUtil.FONT_7_NORMAL_BLACK);
			table.addCell(cell);
			table.addCell(PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(cJourneeP.getNbr_vente().intValue()), Element.ALIGN_RIGHT, PdfUtil.FONT_7_NORMAL_BLACK));
			table.addCell(PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(cJourneeP.getMtt_total())+" "+devise, Element.ALIGN_RIGHT, PdfUtil.FONT_7_NORMAL_BLACK));
			table.addCell(PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(cJourneeP.getMtt_reduction())+" "+devise, Element.ALIGN_RIGHT, PdfUtil.FONT_7_NORMAL_BLACK));
			table.addCell(PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(cJourneeP.getMtt_art_offert())+" "+devise, Element.ALIGN_RIGHT, PdfUtil.FONT_7_NORMAL_BLACK));
			table.addCell(PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(cJourneeP.getMtt_art_reduction())+" "+devise, Element.ALIGN_RIGHT, PdfUtil.FONT_7_NORMAL_BLACK));
			table.addCell(PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(cJourneeP.getMtt_annule())+" "+devise, Element.ALIGN_RIGHT, PdfUtil.FONT_7_NORMAL_BLACK));
			table.addCell(PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(cJourneeP.getMtt_annule_ligne())+" "+devise, Element.ALIGN_RIGHT, PdfUtil.FONT_7_NORMAL_BLACK));
			table.addCell(PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(cJourneeP.getMtt_total_net())+" "+devise, Element.ALIGN_RIGHT, PdfUtil.FONT_7_NORMAL_BLACK));
			BigDecimal ecart = BigDecimalUtil.substract(cJourneeP.getMtt_cloture_caissier(),cJourneeP.getMtt_ouverture());
			table.addCell(PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(ecart)+" "+devise, Element.ALIGN_RIGHT, PdfUtil.FONT_7_NORMAL_BLACK));
			
			Font font = new Font(Font.getFamily("TIMES_ROMAN"), 7, Font.NORMAL);
			if(cJourneeP.getEcartCalcule().compareTo(BigDecimalUtil.ZERO) >= 0){
				font.setColor(BaseColor.GREEN);
			} else{
				font.setColor(BaseColor.RED);
			}
			table.addCell(PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(cJourneeP.getEcartCalcule())+" "+devise, Element.ALIGN_RIGHT, font));
			table.addCell(PdfUtil.getCell(document, (cJourneeP.getOpc_user()!=null? cJourneeP.getOpc_user().getLogin():""), PdfUtil.FONT_7_NORMAL_BLACK));
			
			idxShift++;
		}
		document.add(table);
	}
}
