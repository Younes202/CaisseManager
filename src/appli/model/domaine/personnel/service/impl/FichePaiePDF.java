package appli.model.domaine.personnel.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import appli.model.domaine.personnel.persistant.paie.SalairePersistant;
import appli.model.domaine.personnel.service.IEmployeService;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.beanContext.SocietePersistant;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.PdfBean;
import framework.model.util.PdfUtil;
import framework.model.util.PdfUtil.EnumBorder;

public class FichePaiePDF {
	
	public File exportPdf(EtablissementPersistant etsP, List<SalairePersistant> listEmplPaie, String modeExport) throws IOException, DocumentException {
		PdfBean pdfBean = null;
		Document document = null;
		
		try{
			pdfBean = PdfUtil.creerDocument("paie");
			document = pdfBean.getDocument();
			
			ajouterContenu(document, etsP, listEmplPaie, modeExport);
			document.newPage();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally{			
			document.close();
		}
		
		return pdfBean.getPdfFile();
	}
	
	private void ajouterEntete(Document document, EtablissementPersistant opc_ets) throws DocumentException {
		PdfPTable table = new PdfPTable(3);
	    table.setWidthPercentage(100);
	    float[] widths = {25f, 30f, 25f};
		table.setWidths(widths);// largeur par cellule
		
		SocietePersistant societeP = opc_ets.getOpc_societe();
		IEmployeService employePaieService = (IEmployeService)ServiceUtil.getBusinessBean(IEmployeService.class);
		// Logo
		Map<String, byte[]> imagep = employePaieService.getDataImage(societeP.getId(), "societe");
		PdfPCell cell = null;
		if(imagep.size() > 0){
			try{
				Image image = Image.getInstance(imagep.entrySet().iterator().next().getValue());
				image.scaleAbsolute(Float.valueOf(120), Float.valueOf(40));
				cell = new PdfPCell(image);
				PdfUtil.effacerBordure(cell, EnumBorder.NO_BORDER);
			} catch(Exception e){
				e.printStackTrace();
			}
		} else{
			cell = PdfUtil.getCellVideNoBorder(document);
		}

		table.addCell (cell);  
		
		cell = PdfUtil.getCellVide(document);
		PdfUtil.effacerBordure(cell, EnumBorder.NO_BORDER);
		table.addCell(cell);
		
		SocietePersistant opc_societe = opc_ets.getOpc_societe();
		String adresse = opc_societe.getRaison_sociale()+"\n";
		if (StringUtil.isNotEmpty(opc_societe.getAdresse_rue())) {
			adresse = adresse + opc_societe.getAdresse_rue()+"\n";
		}
		if (StringUtil.isNotEmpty(opc_societe.getAdresse_compl())) {
			adresse = adresse + opc_societe.getAdresse_compl()+"\n";
		}
		if (opc_societe.getOpc_ville() != null) {
			adresse = adresse + opc_societe.getOpc_ville().getCode_postal() + " " + opc_societe.getOpc_ville().getLibelle()+"\n";
		}

		
		
		cell = PdfUtil.getCell(document, adresse, Element.ALIGN_RIGHT, PdfUtil.FONT_11_NORMAL_BLACK);
		PdfUtil.effacerBordure(cell, EnumBorder.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setFixedHeight(20);
		table.addCell(cell);
		
		document.add(table);
		PdfUtil.ajouterLigneVide(document, 1);
		
		Paragraph entete = new Paragraph("BULLETIN DE PAIE", PdfUtil.FONT_12_BOLD_BLACK);
        entete.setAlignment(Element.ALIGN_CENTER);
        document.add(entete);
        PdfUtil.ajouterLigneVide(document, 2);
	}
	
	private void ajouterContenu(Document document, EtablissementPersistant etsP, List<SalairePersistant> listEmplPaie, String modeExport) throws DocumentException {
		for (SalairePersistant salaireP : listEmplPaie) {
			document.newPage();
			ajouterEntete(document, etsP);
			
			// Ajouter entête standard ---------------------
		    PdfPTable table = new PdfPTable(8);
		    table.setWidthPercentage(100f);
		    float[] widths = {12f, 12f, 12f, 12f, 12f, 12f, 16f, 12f};
			table.setWidths(widths);// largeur par cellule
			
			// ---------------------------------------
			PdfPCell cell = PdfUtil.getCell(document, "NOM-PRENOM", PdfUtil.FONT_8_NORMAL_BLACK);
			cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
			cell.setFixedHeight(15);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setColspan(2);
			table.addCell(cell);
			
			cell = PdfUtil.getCell(document, "QUALIFICATION", PdfUtil.FONT_8_NORMAL_BLACK);
			cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
			cell.setFixedHeight(15);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setColspan(4);
			table.addCell(cell);
			
			cell = PdfUtil.getCell(document, "MENS/HORAIRE", PdfUtil.FONT_8_NORMAL_BLACK);
			cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
			cell.setFixedHeight(15);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			cell = PdfUtil.getCell(document, "MATRICULE", PdfUtil.FONT_8_NORMAL_BLACK);
			cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
			cell.setFixedHeight(15);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			cell = PdfUtil.getCell(document, salaireP.getOpc_employe().getNom()+"-"+salaireP.getOpc_employe().getPrenom(), PdfUtil.FONT_8_NORMAL_BLACK);
			cell.setFixedHeight(15);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setColspan(2);
			table.addCell(cell);
			
			String poste = salaireP.getOpc_employe().getOpc_poste() != null ? salaireP.getOpc_employe().getOpc_poste().getIntitule() : "";
			cell = PdfUtil.getCell(document, poste, PdfUtil.FONT_8_NORMAL_BLACK);
			cell.setFixedHeight(15);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setColspan(4);
			table.addCell(cell);
			
			cell = PdfUtil.getCell(document, "", PdfUtil.FONT_8_NORMAL_BLACK);
			cell.setFixedHeight(15);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			cell = PdfUtil.getCell(document, salaireP.getOpc_employe().getNumero(), PdfUtil.FONT_8_NORMAL_BLACK);
			cell.setFixedHeight(15);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			cell = PdfUtil.getCell(document, "DATE EMB.", PdfUtil.FONT_8_NORMAL_BLACK);
			cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
			cell.setFixedHeight(15);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			cell = PdfUtil.getCell(document, "CIMR", PdfUtil.FONT_8_NORMAL_BLACK);
			cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
			cell.setFixedHeight(15);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			cell = PdfUtil.getCell(document, "CNSS", PdfUtil.FONT_8_NORMAL_BLACK);
			cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
			cell.setFixedHeight(15);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			cell = PdfUtil.getCell(document, "NAISSANCE", PdfUtil.FONT_8_NORMAL_BLACK);
			cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
			cell.setFixedHeight(15);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			cell = PdfUtil.getCell(document, "SF", PdfUtil.FONT_8_NORMAL_BLACK);
			cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
			cell.setFixedHeight(15);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			cell = PdfUtil.getCell(document, "DEDUCT", PdfUtil.FONT_8_NORMAL_BLACK);
			cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
			cell.setFixedHeight(15);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			cell = PdfUtil.getCell(document, "SALAIRE DE BASE/J", PdfUtil.FONT_8_NORMAL_BLACK);
			cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
			cell.setFixedHeight(15);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			cell = PdfUtil.getCell(document, "PERIODE PAIE", PdfUtil.FONT_8_NORMAL_BLACK);
			cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
			cell.setFixedHeight(15);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			cell = PdfUtil.getCell(document, DateUtil.dateToString(salaireP.getOpc_employe().getDate_entree()), PdfUtil.FONT_8_NORMAL_BLACK);
			cell.setFixedHeight(15);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			cell = PdfUtil.getCell(document, salaireP.getOpc_employe().getCimr(), PdfUtil.FONT_8_NORMAL_BLACK);
			cell.setFixedHeight(15);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			cell = PdfUtil.getCell(document, salaireP.getOpc_employe().getCnss(), PdfUtil.FONT_8_NORMAL_BLACK);
			cell.setFixedHeight(15);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			cell = PdfUtil.getCell(document, DateUtil.dateToString(salaireP.getOpc_employe().getDate_naissance()), PdfUtil.FONT_8_NORMAL_BLACK);
			cell.setFixedHeight(15);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			cell = PdfUtil.getCell(document, salaireP.getOpc_employe().getSituation_familiale(), PdfUtil.FONT_8_NORMAL_BLACK);
			cell.setFixedHeight(15);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			cell = PdfUtil.getCell(document, "0", PdfUtil.FONT_8_NORMAL_BLACK);
			cell.setFixedHeight(15);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			cell = PdfUtil.getCell(document, "", PdfUtil.FONT_8_NORMAL_BLACK);
			cell.setFixedHeight(15);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			cell = PdfUtil.getCell(document, DateUtil.dateToString(salaireP.getDate_paiement()), PdfUtil.FONT_8_NORMAL_BLACK);
			cell.setFixedHeight(15);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			document.add(table);
			PdfUtil.ajouterLigneVide(document, 1);
			/******************************************************************************************************************************/
			
			// Ajouter entête standard ---------------------
		    PdfPTable table2 = new PdfPTable(6);
		    table2.setWidthPercentage(100f);
		    float[] widths2 = {8f, 28f, 15f, 15f, 17f, 17f};
			table2.setWidths(widths2);// largeur par cellule
			
			BigDecimal tauxCnss = BigDecimalUtil.get("4.48");
			BigDecimal tauxAmo = BigDecimalUtil.get("2.26");
			//anciennete
			int tauxAncienete = 0;
			
			if(salaireP.getOpc_employe().getDate_entree() != null && salaireP.getDate_paiement() != null) {
				int nbrYears = DateUtil.getDiffYear(salaireP.getOpc_employe().getDate_entree(), salaireP.getDate_paiement());
				if(nbrYears<2){
					tauxAncienete = 0;
				} else if(nbrYears<5){
					tauxAncienete = 5;
				} else if(nbrYears<12){
					tauxAncienete = 10;
				} else if(nbrYears<20){
					tauxAncienete = 15;
				} else if(nbrYears<25){
					tauxAncienete = 20;
				} else{
					tauxAncienete = 25;
				}
			}
			BigDecimal primeAncienete = BigDecimalUtil.divide(BigDecimalUtil.multiply(BigDecimalUtil.get(tauxAncienete), salaireP.getMontant_net()), BigDecimalUtil.get(100));
			BigDecimal salaireBrut = BigDecimalUtil.add(salaireP.getMontant_net(), primeAncienete);
			BigDecimal cotisationCnss = BigDecimalUtil.divide(BigDecimalUtil.multiply(salaireBrut, tauxCnss), BigDecimalUtil.get(100));
			BigDecimal amo = BigDecimalUtil.divide(BigDecimalUtil.multiply(salaireBrut, tauxAmo), BigDecimalUtil.get(100));
			BigDecimal totalPaye = BigDecimalUtil.add(salaireBrut, salaireP.getMt_prime());
			BigDecimal totalRetenu = BigDecimalUtil.add(cotisationCnss, amo, salaireP.getMt_avance(), salaireP.getMt_pret());
			BigDecimal netPayer = BigDecimalUtil.substract(totalPaye, totalRetenu);
			// ------------------------------------------------
			PdfPCell cell2 = PdfUtil.getCell(document, "C.PAIE", PdfUtil.FONT_9_NORMAL_BLACK);
			cell2.setBackgroundColor(PdfUtil.GRIS_LEGER);
			cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell2.setFixedHeight(23);
			table2.addCell(cell2);
			
			cell2 = PdfUtil.getCell(document, "LIBELLE", PdfUtil.FONT_9_NORMAL_BLACK);
			cell2.setBackgroundColor(PdfUtil.GRIS_LEGER);
			cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table2.addCell(cell2);
			
			cell2 = PdfUtil.getCell(document, "BASE/NOMBRE", PdfUtil.FONT_9_NORMAL_BLACK);
			cell2.setBackgroundColor(PdfUtil.GRIS_LEGER);
			cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table2.addCell(cell2);

			cell2 = PdfUtil.getCell(document, "TAUX", PdfUtil.FONT_9_NORMAL_BLACK);
			cell2.setBackgroundColor(PdfUtil.GRIS_LEGER);
			cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table2.addCell(cell2);
			
			cell2 = PdfUtil.getCell(document, "A PAYER", PdfUtil.FONT_9_NORMAL_BLACK);
			cell2.setBackgroundColor(PdfUtil.GRIS_LEGER);
			cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table2.addCell(cell2);
			
			cell2 = PdfUtil.getCell(document, "A RETENIR", PdfUtil.FONT_9_NORMAL_BLACK);
			cell2.setBackgroundColor(PdfUtil.GRIS_LEGER);
			cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table2.addCell(cell2);
			

			
			//---------------------------------------------------
			cell2 = PdfUtil.getCellVide(document);
			cell2.setRowspan(12);
			table2.addCell(cell2);
			
			cell2 = PdfUtil.getCell(document, "GAINS", PdfUtil.FONT_9_BOLD_BLACK);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_BOTTOM);
			cell2.setPaddingTop(15f);
			cell2.setPaddingLeft(6f);
			table2.addCell(cell2);
			
			
			cell2 = PdfUtil.getCellVide(document);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_BOTTOM);
			table2.addCell(cell2);
			
			cell2 = PdfUtil.getCellVide(document);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_BOTTOM);
			table2.addCell(cell2);
			
			cell2 = PdfUtil.getCellVide(document);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_BOTTOM);
			table2.addCell(cell2);
			
			cell2 = PdfUtil.getCellVide(document);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_BOTTOM);
			table2.addCell(cell2);
			
			//---------------------------------------------------
			cell2 = PdfUtil.getCell(document, "SALAIRE DE BASE", PdfUtil.FONT_9_NORMAL_BLACK);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			cell2.setPaddingTop(6f);
			cell2.setPaddingLeft(6f);
			table2.addCell(cell2);
			
			cell2 = PdfUtil.getCellVide(document);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			table2.addCell(cell2);
			
			cell2 = PdfUtil.getCellVide(document);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			table2.addCell(cell2);
			
			cell2 = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(salaireP.getMontant_net()), PdfUtil.FONT_9_NORMAL_BLACK);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table2.addCell(cell2);
			
			cell2 = PdfUtil.getCellVide(document);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			table2.addCell(cell2);
			
			//---------------------------------------------------
			cell2 = PdfUtil.getCell(document, "PRIME D'ANCIENETE", PdfUtil.FONT_9_NORMAL_BLACK);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			cell2.setPaddingTop(6f);
			cell2.setPaddingLeft(6f);
			table2.addCell(cell2);
			
			cell2 = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(salaireP.getMontant_net()), PdfUtil.FONT_9_NORMAL_BLACK);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table2.addCell(cell2);
			
			if(tauxAncienete == 0) {
				cell2 = PdfUtil.getCellVide(document);
				PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
				table2.addCell(cell2);
			} else {
				cell2 = PdfUtil.getCell(document, tauxAncienete+"", PdfUtil.FONT_9_NORMAL_BLACK);
				PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
				cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
				table2.addCell(cell2);
			}
			
			if(BigDecimalUtil.isZero(primeAncienete)) {
				cell2 = PdfUtil.getCellVide(document);
				PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
				table2.addCell(cell2);
			} else {
				cell2 = PdfUtil.getCell(document, primeAncienete+"", PdfUtil.FONT_9_NORMAL_BLACK);
				PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
				cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
				table2.addCell(cell2);
			}
			
			cell2 = PdfUtil.getCellVide(document);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			table2.addCell(cell2);
			
			//---------------------------------------------------
			cell2 = PdfUtil.getCell(document, "PRIME", PdfUtil.FONT_9_NORMAL_BLACK);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			cell2.setPaddingTop(6f);
			cell2.setPaddingLeft(6f);
			table2.addCell(cell2);
			
			cell2 = PdfUtil.getCellVide(document);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			table2.addCell(cell2);
			
			cell2 = PdfUtil.getCellVide(document);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			table2.addCell(cell2);
			
			cell2 = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(salaireP.getMt_prime()), PdfUtil.FONT_9_NORMAL_BLACK);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table2.addCell(cell2);
			
			cell2 = PdfUtil.getCellVide(document);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			table2.addCell(cell2);
			
			//---------------------------------------------------
			cell2 = PdfUtil.getCell(document, "SALAIRE BRUT", PdfUtil.FONT_9_NORMAL_BLACK);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			cell2.setPaddingTop(6f);
			cell2.setPaddingLeft(6f);
			table2.addCell(cell2);
			
			cell2 = PdfUtil.getCellVide(document);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			table2.addCell(cell2);
			
			cell2 = PdfUtil.getCellVide(document);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			table2.addCell(cell2);
			
			cell2 = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(salaireBrut), PdfUtil.FONT_9_NORMAL_BLACK);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table2.addCell(cell2);
			
			cell2 = PdfUtil.getCellVide(document);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			table2.addCell(cell2);
			
			//---------------------------------------------------
			cell2 = PdfUtil.getCell(document, "DEDUCTIONS", PdfUtil.FONT_9_BOLD_BLACK);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			cell2.setPaddingTop(6f);
			cell2.setPaddingLeft(6f);
			table2.addCell(cell2);
			
			cell2 = PdfUtil.getCellVide(document);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			table2.addCell(cell2);
			
			cell2 = PdfUtil.getCellVide(document);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			table2.addCell(cell2);
			
			cell2 = PdfUtil.getCellVide(document);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			table2.addCell(cell2);
			
			cell2 = PdfUtil.getCellVide(document);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			table2.addCell(cell2);
			
			//---------------------------------------------------
			cell2 = PdfUtil.getCell(document, "COTISATION CNSS", PdfUtil.FONT_9_NORMAL_BLACK);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			cell2.setPaddingTop(6f);
			cell2.setPaddingLeft(6f);
			table2.addCell(cell2);
			
			cell2 = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(salaireBrut), PdfUtil.FONT_9_NORMAL_BLACK);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table2.addCell(cell2);
			
			cell2 = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(tauxCnss), PdfUtil.FONT_9_NORMAL_BLACK);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table2.addCell(cell2);
			
			cell2 = PdfUtil.getCellVide(document);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			table2.addCell(cell2);
			
			cell2 = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(cotisationCnss), PdfUtil.FONT_9_NORMAL_BLACK);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table2.addCell(cell2);

			//---------------------------------------------------
			cell2 = PdfUtil.getCell(document, "AMO", PdfUtil.FONT_9_NORMAL_BLACK);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			cell2.setPaddingTop(6f);
			cell2.setPaddingLeft(6f);
			table2.addCell(cell2);
			
			cell2 = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(salaireBrut), PdfUtil.FONT_9_NORMAL_BLACK);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table2.addCell(cell2);
			
			cell2 = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(tauxAmo), PdfUtil.FONT_9_NORMAL_BLACK);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table2.addCell(cell2);
			
			cell2 = PdfUtil.getCellVide(document);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			table2.addCell(cell2);
			
			cell2 = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(amo), PdfUtil.FONT_9_NORMAL_BLACK);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table2.addCell(cell2);
			
			//---------------------------------------------------
			cell2 = PdfUtil.getCell(document, "RETRAITE CIMIR", PdfUtil.FONT_9_NORMAL_BLACK);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			cell2.setPaddingTop(6f);
			cell2.setPaddingLeft(6f);
			table2.addCell(cell2);
			cell2 = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(salaireBrut), PdfUtil.FONT_9_NORMAL_BLACK);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table2.addCell(cell2);
			cell2 = PdfUtil.getCellVide(document);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			table2.addCell(cell2);
			cell2 = PdfUtil.getCellVide(document);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			table2.addCell(cell2);
			cell2 = PdfUtil.getCellVide(document);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			table2.addCell(cell2);
			
			//---------------------------------------------------
			cell2 = PdfUtil.getCell(document, "PRELEVEMENT IGR", PdfUtil.FONT_9_NORMAL_BLACK);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			cell2.setPaddingTop(6f);
			cell2.setPaddingLeft(6f);
			table2.addCell(cell2);
			cell2 = PdfUtil.getCellVide(document);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			table2.addCell(cell2);
			cell2 = PdfUtil.getCellVide(document);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			table2.addCell(cell2);
			cell2 = PdfUtil.getCellVide(document);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			table2.addCell(cell2);
			cell2 = PdfUtil.getCellVide(document);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			table2.addCell(cell2);
			
			//---------------------------------------------------
			cell2 = PdfUtil.getCell(document, "AVANCE", PdfUtil.FONT_9_NORMAL_BLACK);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			cell2.setPaddingTop(6f);
			cell2.setPaddingLeft(6f);
			table2.addCell(cell2);
			cell2 = PdfUtil.getCellVide(document);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			table2.addCell(cell2);
			cell2 = PdfUtil.getCellVide(document);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			table2.addCell(cell2);
			cell2 = PdfUtil.getCellVide(document);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			table2.addCell(cell2);
			cell2 = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(salaireP.getMt_avance()), PdfUtil.FONT_9_NORMAL_BLACK);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table2.addCell(cell2);
			
			//---------------------------------------------------
			cell2 = PdfUtil.getCell(document, "PRET", PdfUtil.FONT_9_NORMAL_BLACK);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			cell2.setPaddingTop(6f);
			cell2.setPaddingLeft(6f);
			cell2.setPaddingBottom(100f);
			table2.addCell(cell2);
			cell2 = PdfUtil.getCellVide(document);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			table2.addCell(cell2);
			cell2 = PdfUtil.getCellVide(document);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			table2.addCell(cell2);
			cell2 = PdfUtil.getCellVide(document);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			table2.addCell(cell2);
			cell2 = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(salaireP.getMt_pret()), PdfUtil.FONT_9_NORMAL_BLACK);
			PdfUtil.effacerBordure(cell2, EnumBorder.BORDER_TOP, EnumBorder.BORDER_BOTTOM);
			cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table2.addCell(cell2);
			
			//---------------------------------------------------
			cell2 = PdfUtil.getCellVide(document);
			cell2.setRowspan(4);
			cell2.setColspan(4);
			table2.addCell(cell2);
			cell2 = PdfUtil.getCell(document, "Total", PdfUtil.FONT_9_NORMAL_BLACK);
			cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell2.setFixedHeight(15);
			table2.addCell(cell2);
			cell2 = PdfUtil.getCell(document, "Total", PdfUtil.FONT_9_NORMAL_BLACK);
			cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell2.setFixedHeight(15);
			table2.addCell(cell2);
			
			
			cell2 = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(totalPaye), PdfUtil.FONT_9_NORMAL_BLACK);
			cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell2.setFixedHeight(15);
			table2.addCell(cell2);
			cell2 = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(totalRetenu), PdfUtil.FONT_9_NORMAL_BLACK);
			cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell2.setFixedHeight(15);
			table2.addCell(cell2);
			
			cell2 = PdfUtil.getCell(document, "NET A PAYER", PdfUtil.FONT_9_BOLD_BLACK);
			cell2.setBackgroundColor(PdfUtil.GRIS_LEGER);
			cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell2.setFixedHeight(15);
			cell2.setColspan(2);
			table2.addCell(cell2);
			
			cell2 = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(netPayer), PdfUtil.FONT_9_BOLD_BLACK);
			cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell2.setFixedHeight(15);
			cell2.setColspan(2);
			table2.addCell(cell2);
			
			document.add(table2);
			PdfUtil.ajouterLigneVide(document, 2);
			/******************************************************************************************************************************/
			
			// Ajouter entête standard ---------------------
		    PdfPTable table3 = new PdfPTable(4);
		    table3.setWidthPercentage(100f);
		    float[] widths3 = {25f, 25f, 25f, 25f};
			table3.setWidths(widths3);// largeur par cellule
			
			PdfPCell cell3 = PdfUtil.getCell(document, "CUMULS", PdfUtil.FONT_8_NORMAL_BLACK);
			cell3.setColspan(4);
			cell3.setFixedHeight(15);
			cell3.setBackgroundColor(PdfUtil.GRIS_LEGER);
			cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
			table3.addCell(cell3);
			
			cell3 = PdfUtil.getCell(document, "JOURS TRAVAILLES", PdfUtil.FONT_8_NORMAL_BLACK);
			cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell3.setFixedHeight(15);
			table3.addCell(cell3);
			cell3 = PdfUtil.getCell(document, "JOURS CONGE", PdfUtil.FONT_8_NORMAL_BLACK);
			cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
			table3.addCell(cell3);
			cell3 = PdfUtil.getCell(document, "SALAIRE DE BASE", PdfUtil.FONT_8_NORMAL_BLACK);
			cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
			table3.addCell(cell3);
			cell3 = PdfUtil.getCell(document, "PRIMES IMPOSABLES", PdfUtil.FONT_8_NORMAL_BLACK);
			cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
			table3.addCell(cell3);
			
			cell3 = PdfUtil.getCellVide(document);
			cell3.setFixedHeight(15);
			table3.addCell(cell3);
			cell3 = PdfUtil.getCellVide(document);
			table3.addCell(cell3);
			cell3 = PdfUtil.getCell(document,  BigDecimalUtil.formatNumber(salaireP.getMontant_net()), PdfUtil.FONT_8_NORMAL_BLACK);
			cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
			table3.addCell(cell3);
			cell3 = PdfUtil.getCellVide(document);
			table3.addCell(cell3);
			
			cell3 = PdfUtil.getCell(document, "PRIMES NON IMPOSABLES", PdfUtil.FONT_8_NORMAL_BLACK);
			cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell3.setFixedHeight(15);
			table3.addCell(cell3);
			cell3 = PdfUtil.getCell(document, "SALAIRE BRUT", PdfUtil.FONT_8_NORMAL_BLACK);
			cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
			table3.addCell(cell3);
			cell3 = PdfUtil.getCell(document, "SALAIRE BRUT IMPOSABLE", PdfUtil.FONT_8_NORMAL_BLACK);
			cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
			table3.addCell(cell3);
			cell3 = PdfUtil.getCell(document, "CNSS P.SAL", PdfUtil.FONT_8_NORMAL_BLACK);
			cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
			table3.addCell(cell3);
			
			cell3 = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(salaireP.getMt_prime()), PdfUtil.FONT_8_NORMAL_BLACK);
			cell3.setFixedHeight(15);
			cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
			table3.addCell(cell3);
			cell3 = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(salaireBrut), PdfUtil.FONT_8_NORMAL_BLACK);
			cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
			table3.addCell(cell3);
			cell3 = PdfUtil.getCellVide(document);
			table3.addCell(cell3);
			cell3 = PdfUtil.getCellVide(document);
			table3.addCell(cell3);

			cell3 = PdfUtil.getCell(document, "AMO", PdfUtil.FONT_8_NORMAL_BLACK);
			cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell3.setFixedHeight(15);
			table3.addCell(cell3);
			cell3 = PdfUtil.getCell(document, "CIMR P.SAL", PdfUtil.FONT_8_NORMAL_BLACK);
			cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
			table3.addCell(cell3);
			cell3 = PdfUtil.getCell(document, "ASSURANCE MALADIES P.SAL", PdfUtil.FONT_8_NORMAL_BLACK);
			cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
			table3.addCell(cell3);
			cell3 = PdfUtil.getCell(document, "IR", PdfUtil.FONT_8_NORMAL_BLACK);
			cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
			table3.addCell(cell3);
			
			cell3 = PdfUtil.getCellVide(document);
			cell3.setFixedHeight(15);
			table3.addCell(cell3);
			cell3 = PdfUtil.getCellVide(document);
			table3.addCell(cell3);
			cell3 = PdfUtil.getCellVide(document);
			table3.addCell(cell3);
			cell3 = PdfUtil.getCellVide(document);
			table3.addCell(cell3);
			
			document.add(table3);
		}
		
		
		
	}

}
