package appli.model.domaine.caisse.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.personnel.persistant.ClientPersistant;
import appli.model.domaine.personnel.persistant.EmployePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.PdfBean;
import framework.model.util.PdfUtil;

public class HistoriqueReductionPDF {

	/**
	 * @param dest
	 * @throws IOException
	 * @throws DocumentException
	 */

	public File exportPdf(List<CaisseMouvementPersistant> listMouvement, CaisseMouvementPersistant totalMvm, Date dateDebut, Date dateFin) {
		PdfBean pdfBean = null;
		Document document = null;
		
		try{
			pdfBean = PdfUtil.creerDocument("reductions");
			document = pdfBean.getDocument();
			
			ajouterTitlePage(document, listMouvement, dateDebut, dateFin);
			ajouterContenu(document, listMouvement);
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
	private void ajouterTitlePage(Document document, List<CaisseMouvementPersistant> listMouvement, Date dateDebut, Date dateFin) throws DocumentException {
		// Ajouter entête standard ---------------------
	    PdfPTable table = new PdfPTable(3);
	    table.setWidthPercentage(100f);
	    float[] widths = {40f, 20f, 40f};
		table.setWidths(widths);// largeur par cellule
		
		// Titre
		Paragraph paragraph = new Paragraph("Rapport des réductions", PdfUtil.FONT_18_BOLD_BLACK);
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
	private void ajouterContenu(Document document, List<CaisseMouvementPersistant> listMvmCaisse) throws DocumentException {
		PdfUtil.ajouterLigneSeparateurPontille(document);
		String devise = StrimUtil.getGlobalConfigPropertie("devise.symbole");
		
		Map<String, String> mapMode = new HashMap<>();
		mapMode.put(ContextAppli.TYPE_COMMANDE.E.toString(), "A emporter"); 
		mapMode.put(ContextAppli.TYPE_COMMANDE.P.toString(), "Sur place"); 
		mapMode.put(ContextAppli.TYPE_COMMANDE.L.toString(), "Livraison");
		

		// Ajouter entête standard ---------------------
	    PdfPTable table = new PdfPTable(9);
    	table.setWidthPercentage(100f);
    	float[] widths2 = {15f, 12f, 11f, 11f,11f, 10f , 9f , 9f , 9f, 9f};
    	table.setWidths(widths2);// largeur par cellule

	    PdfPCell cell = PdfUtil.getCell(document, "Référence", PdfUtil.FONT_9_NORMAL_BLACK);
		cell.setBackgroundColor(new BaseColor(224, 224, 224));
		table.addCell(cell);
		
		cell = PdfUtil.getCell(document, "Bénéficiaire", PdfUtil.FONT_9_NORMAL_BLACK);
		cell.setBackgroundColor(new BaseColor(224, 224, 224));
		table.addCell(cell);
		
		cell = PdfUtil.getCell(document, "Mtt Commande", PdfUtil.FONT_9_NORMAL_BLACK);
		cell.setBackgroundColor(new BaseColor(224, 224, 224));
		table.addCell(cell);
		
		cell = PdfUtil.getCell(document, "Mtt Réduction", PdfUtil.FONT_9_NORMAL_BLACK);
		cell.setBackgroundColor(new BaseColor(224, 224, 224));
		table.addCell(cell);
		
		cell = PdfUtil.getCell(document, "Type de Réduction", PdfUtil.FONT_9_NORMAL_BLACK);
		cell.setBackgroundColor(new BaseColor(224, 224, 224));
		table.addCell(cell);
		
		cell = PdfUtil.getCell(document, "Mtt Offert", PdfUtil.FONT_9_NORMAL_BLACK);
		cell.setBackgroundColor(new BaseColor(224, 224, 224));
		table.addCell(cell);
		
		cell = PdfUtil.getCell(document, "Mtt Réduction Art", PdfUtil.FONT_9_NORMAL_BLACK);
		cell.setBackgroundColor(new BaseColor(224, 224, 224));
		table.addCell(cell);
		
		cell = PdfUtil.getCell(document, "Mtt Net", PdfUtil.FONT_9_NORMAL_BLACK);
		cell.setBackgroundColor(new BaseColor(224, 224, 224));
		table.addCell(cell);
		
		cell = PdfUtil.getCell(document, "Type Commande", PdfUtil.FONT_9_NORMAL_BLACK);
		cell.setBackgroundColor(new BaseColor(224, 224, 224));
		table.addCell(cell);
		
		cell = PdfUtil.getCell(document, "Mode Paiement", PdfUtil.FONT_9_NORMAL_BLACK);
		cell.setBackgroundColor(new BaseColor(224, 224, 224));
		table.addCell(cell);
		
		Long oldjour = null, oldCaisse = null, oldjourCaisse = null;
		int idxShift = 1;
		for(CaisseMouvementPersistant cMouvementP : listMvmCaisse){
			if(oldjour == null ||oldjour !=cMouvementP.getOpc_caisse_journee().getOpc_journee().getId()){
				cell = PdfUtil.getCell(document, DateUtil.dateToString(cMouvementP.getDate_vente()), PdfUtil.FONT_8_BOLD_BLACK);
				cell.setColspan(9);
				cell.setBackgroundColor(PdfUtil.JAUNE_LEGER);
				table.addCell(cell);
				idxShift = 1;
				oldCaisse = null;
				oldjourCaisse = null;
			}
			if(oldCaisse == null ||oldCaisse !=cMouvementP.getOpc_caisse_journee().getOpc_caisse().getId()){
				cell = PdfUtil.getCell(document, cMouvementP.getOpc_caisse_journee().getOpc_caisse().getReference(), PdfUtil.FONT_8_BOLD_BLACK);
				cell.setColspan(9);
				cell.setBackgroundColor(PdfUtil.GRIS_CLAIR);
				cell.setPaddingLeft(10f);
				table.addCell(cell);
				idxShift = 1;
				oldjourCaisse = null;
			}
			if(oldjourCaisse == null || oldjourCaisse !=cMouvementP.getOpc_caisse_journee().getId()){
				cell = PdfUtil.getCell(document, "Shift "+idxShift+" ["+DateUtil.dateToString(cMouvementP.getOpc_caisse_journee().getDate_ouverture(), "HH:mm:ss")+"]", PdfUtil.FONT_8_BOLD_BLACK);
				cell.setColspan(9);
				cell.setPaddingLeft(20f);
				cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
				table.addCell(cell);
				idxShift++;
			}
			
			oldjour = cMouvementP.getOpc_caisse_journee().getOpc_journee().getId();
			oldCaisse = cMouvementP.getOpc_caisse_journee().getOpc_caisse().getId();
			oldjourCaisse = cMouvementP.getOpc_caisse_journee().getId();
			
			
			table.addCell(PdfUtil.getCell(document, cMouvementP.getRef_commande(), PdfUtil.FONT_7_NORMAL_BLACK));
			EmployePersistant opc_employe = cMouvementP.getOpc_employe();
			ClientPersistant opc_client = cMouvementP.getOpc_client();
			
			if(opc_employe !=null){
				table.addCell(PdfUtil.getCell(document, opc_employe.getNom()+" "+StringUtil.getValueOrEmpty(opc_employe.getPrenom()), PdfUtil.FONT_7_NORMAL_BLACK));
			} else if(opc_client!=null){
				table.addCell(PdfUtil.getCell(document,opc_client.getNom()+" "+StringUtil.getValueOrEmpty(opc_client.getPrenom()), PdfUtil.FONT_7_NORMAL_BLACK));
			} else{
				table.addCell(PdfUtil.getCellVide(document));
			}
			
			table.addCell(PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(cMouvementP.getMtt_commande())+" "+devise, Element.ALIGN_RIGHT, PdfUtil.FONT_7_NORMAL_BLACK));
			table.addCell(PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(cMouvementP.getMtt_reduction())+" "+devise, Element.ALIGN_RIGHT, PdfUtil.FONT_7_NORMAL_BLACK));
			table.addCell(PdfUtil.getCell(document, cMouvementP.getOffreStr(), Element.ALIGN_RIGHT, PdfUtil.FONT_7_NORMAL_BLACK));
			table.addCell(PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(cMouvementP.getMtt_art_offert())+" "+devise, Element.ALIGN_RIGHT, PdfUtil.FONT_7_NORMAL_BLACK));
			table.addCell(PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(cMouvementP.getMtt_art_reduction())+" "+devise, Element.ALIGN_RIGHT, PdfUtil.FONT_7_NORMAL_BLACK));
			table.addCell(PdfUtil.getCell(document, BigDecimalUtil.formatNumberZero(cMouvementP.getMtt_commande_net())+" "+devise, Element.ALIGN_RIGHT, PdfUtil.FONT_7_NORMAL_BLACK));
			table.addCell(PdfUtil.getCell(document, mapMode.get(cMouvementP.getType_commande()), PdfUtil.FONT_7_NORMAL_BLACK));
			table.addCell(PdfUtil.getCell(document, cMouvementP.getMode_paiement(), PdfUtil.FONT_7_NORMAL_BLACK));			
		}
		document.add(table);
	}
}
