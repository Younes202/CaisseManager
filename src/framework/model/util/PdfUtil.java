package framework.model.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.LineSeparator;

import appli.controller.domaine.util_erp.ContextAppli;
import framework.model.beanContext.SocietePersistant;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.StringUtil;
import framework.model.common.util.export.ExportUtil;
import framework.model.common.util.itext.CheckboxCellEvent;
import framework.model.common.util.itext.TextFieldCellEvent;

public class PdfUtil {
	public static final int DEFAULT_WIDTH = 150;

	public static final Font FONT_7_NORMAL_BLACK = new Font(Font.getFamily("TIMES_ROMAN"), 7, Font.NORMAL);
	public static final Font FONT_7_BOLD_BLACK = new Font(Font.getFamily("TIMES_ROMAN"), 7, Font.BOLD);

	public static final Font FONT_8_NORMAL_BLACK = new Font(Font.getFamily("TIMES_ROMAN"), 8, Font.NORMAL);
	public static final Font FONT_8_BOLD_BLACK = new Font(Font.getFamily("TIMES_ROMAN"), 8, Font.BOLD);
	public static final Font FONT_8_BOLD_RED = new Font(Font.getFamily("TIMES_ROMAN"), 8, Font.BOLD, BaseColor.RED);
	
	public static final Font FONT_9_NORMAL_BLACK = new Font(Font.getFamily("TIMES_ROMAN"), 9, Font.NORMAL);
	public static final Font FONT_9_BOLD_BLACK = new Font(Font.getFamily("TIMES_ROMAN"), 9, Font.BOLD);
	public static final Font FONT_9_BOLD_RED = new Font(Font.getFamily("TIMES_ROMAN"), 9, Font.BOLD, BaseColor.RED);
	
	public static final Font FONT_10_NORMAL_BLACK = new Font(Font.getFamily("TIMES_ROMAN"), 10, Font.NORMAL);
	public static final Font FONT_10_BOLD_BLACK = new Font(Font.getFamily("TIMES_ROMAN"), 10, Font.BOLD);
	
	public static final Font FONT_12_NORMAL_RED = new Font(Font.getFamily("TIMES_ROMAN"), 12, Font.NORMAL, BaseColor.RED);

	public static final Font FONT_11_BOLD_BLACK = new Font(Font.getFamily("TIMES_ROMAN"), 11, Font.BOLD);
	public static final Font FONT_11_NORMAL_BLACK = new Font(Font.getFamily("TIMES_ROMAN"), 11, Font.NORMAL);
	public static final Font FONT_11_NORMAL_GRAY = new Font(Font.getFamily("TIMES_ROMAN"), 11, Font.NORMAL, BaseColor.LIGHT_GRAY);
	
	public static final Font FONT_12_BOLD_BLACK = new Font(Font.getFamily("TIMES_ROMAN"), 12, Font.BOLD);
	public static final Font FONT_12_NORMAL_BLACK = new Font(Font.getFamily("TIMES_ROMAN"), 12, Font.NORMAL);
	
	public static final Font FONT_14_BOLD_BLACK = new Font(Font.getFamily("TIMES_ROMAN"), 14, Font.BOLD);
	public static final Font FONT_14_NORMAL_BLACK = new Font(Font.getFamily("TIMES_ROMAN"), 14, Font.NORMAL);
	public static final Font FONT_14_BOLD_WHITE = new Font(Font.getFamily("TIMES_ROMAN"), 14, Font.BOLD, BaseColor.WHITE);
	
	public static final Font FONT_16_BOLD_BLACK = new Font(Font.getFamily("TIMES_ROMAN"), 16, Font.BOLD);
	public static final Font FONT_18_BOLD_BLACK = new Font(Font.getFamily("TIMES_ROMAN"), 18, Font.BOLD);
	
	public static final BaseColor GRIS_CLAIR = new BaseColor(224, 224, 224);
	public static final BaseColor GRIS_LEGER = new BaseColor(245,245,245);
	public static final BaseColor JAUNE_LEGER = new BaseColor(204, 229, 255);
	
	public static enum EnumBorder{
		BORDER_TOP,
		BORDER_BOTTOM,
		BORDER_RIGHT,
		BORDER_LEFT,
		NO_BORDER
	}
	
//	public static Document getDocument(String titre) throws IOException, DocumentException{
//		Document document = null;
//		File tempFile = null;
//		
//		FileOutputStream fileOutputStream = null;
//		tempFile = ExportUtil.getFullExportTempFileName(titre + "_" + DateUtil.dateToString(new Date(), "dd-MM-yyyy_HH-mm-ss"), "pdf");
//		fileOutputStream = new FileOutputStream(tempFile);
//		
//		PdfHeaderFooter event = new PdfHeaderFooter();
//		document = PdfUtil.creerDocument(fileOutputStream, event);
//		// Construction
////		UserBean userBean = ContextRestaurent.getUserBean();
//		//PdfUtil.ajouterMetaData(document, "Dépenses", "Dépenses", "Dépenses", userBean.getLogin(), userBean.getLogin());
//			
//		return document;
//
//	}
	
	/**
	 * Ajoute une ligne vide number fois dans le Paragraph passé en paramètre.
	 *
	 * @param paragraph A remplir avec les lignes vides.
	 * @param numberLignes Nombre de lignes vides à ajouter.
	 * @throws DocumentException 
	 */
	public static void ajouterLigneVide(Document document, int numberLignes) throws DocumentException {
	    for (int i = 0; i < numberLignes; i++) {
	    	document.add(new Paragraph("\n"));
	    }
	}
	
	public static PdfBean creerDocument(String nomPdf) throws DocumentException, IOException {
		return creerDocument(nomPdf, true, false);
	}
	
	/**
	 * Crée un Document vide.
	 *
	 * @param file Chemin du fichier à créer.
	 * @return Document s'il n'y a pas eu d'erreur.
	 * @throws IOException s'il y a eu une erreur avec le nom de fichier fourni.
	 * @throws DocumentException s'il y a eu une erreur côté iText.
	 */
	public static PdfBean creerDocument(String nomPdf, boolean piedPage, boolean addInfosSociete) throws DocumentException, IOException {
		String TEMP_FILE_PATH 	= "/temp";

		File tempFile = ExportUtil.getFullExportTempFileName(TEMP_FILE_PATH, nomPdf + "_"+DateUtil.dateToString(new Date(), "dd-MM-yyyy_HH-mm-ss"), "pdf");
		FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
//		Document document = PdfUtil.creerDocument(fileOutputStream, event);
		
		PdfHeaderFooter event = new PdfHeaderFooter();
		if(addInfosSociete) {
			SocietePersistant societeB = ContextAppli.getSocieteBean();
      		String infos = "";
      		infos += societeB.getRaison_sociale();
      		
      		if(StringUtil.isNotEmpty(societeB.getAdresse_rue())) {
      			infos += " - "+societeB.getAdresse_rue();
      		}
      		
	   		if(societeB.getOpc_ville() != null) {
	   			infos += societeB.getOpc_ville().getCode_postal()
	   						+ " "
	   						+ StringUtil.getValueOrEmpty(societeB.getOpc_ville().getLibelle());
	   		}
	   		
	   		if(StringUtil.isNotEmpty(societeB.getNumero_rcs())){
	   			infos += " - RC : "+StringUtil.getValueOrEmpty(societeB.getNumero_rcs());
	   		}
	   		if(StringUtil.isNotEmpty(societeB.getNumero_ice())){
	   			infos += " -ICE : "+StringUtil.getValueOrEmpty(societeB.getNumero_ice());
	   		}
	   		if(StringUtil.isNotEmpty(societeB.getNumero_tva())){
	   			infos += " - TVA : "+StringUtil.getValueOrEmpty(societeB.getNumero_tva());
	   		}
	   		if(StringUtil.isNotEmpty(societeB.getIdentifiant_fiscal())){
	   			infos += " - IF : "+StringUtil.getValueOrEmpty(societeB.getIdentifiant_fiscal());
	   		}
   		
			event.setFooterStr(infos);
		}
		
	    Document document = new Document();
	    // Construction
	    //	UserBean userBean = ContextRestaurent.getUserBean();
	    //PdfUtil.ajouterMetaData(document, "Dépenses", "Dépenses", "Dépenses", userBean.getLogin(), userBean.getLogin());
	    
	    PdfWriter pdfWriter = PdfWriter.getInstance(document, fileOutputStream);
	    if(piedPage){
	    	pdfWriter.setPageEvent(event);
	    }
	    pdfWriter.setBoxSize("art", new Rectangle(36, 54, 559, 788));
	    // Préférence de lecture : une  pages en colonne.
	    pdfWriter.setViewerPreferences(PdfWriter.PageLayoutOneColumn);
	    document.open();
	    
	    return new PdfBean(document, tempFile);
	}

	/**
	 * @param document
	 * @param fontTitreCentre
	 * @param titreHeader
	 * @param titreCenter
	 * @throws DocumentException
	 */
	public static void ajouterTitreFnt(Document document, Font fontTitreCentre, String titreHeader, String ... titreCenter) throws DocumentException{
		// Ajouter entête standard ---------------------
	    PdfPTable table = new PdfPTable(3);
	    table.setWidthPercentage(100f);
	    float[] widths = {40f, 20f, 40f};
		//table.setSpacingBefore(15f);
		table.setWidths(widths);// largeur par cellule
		// Cellule gauche
		PdfPCell cell = new PdfPCell(
				new Paragraph ("Domaine : "+(titreHeader!=null?"\n"+titreHeader : ""), 
				PdfUtil.FONT_9_NORMAL_BLACK)
			);
		cell.setHorizontalAlignment (Element.ALIGN_LEFT);
		table.addCell (cell);
		// Cellule centre
		cell = new PdfPCell();
		cell.setBorder(0);
		table.addCell (cell);
	    // Cellule droite	
		cell = new PdfPCell(
				new Paragraph ("Adresse : ", PdfUtil.FONT_9_NORMAL_BLACK)
			);
		cell.setHorizontalAlignment (Element.ALIGN_LEFT);
		table.addCell (cell);
		
		document.add(table);
		
		//
		if(fontTitreCentre != null){
			 ajouterLigneVide(document, 1);
		    // Ajouter titre central -----------------
		    for(int i=0; i<titreCenter.length; i++){
		    	Paragraph paragraph = new Paragraph(titreCenter[i], fontTitreCentre);
		    	paragraph.setAlignment(Element.ALIGN_CENTER);
			
		    	document.add(paragraph);
		    }

		} else{
		    // Ajouter titre central -----------------
		    for(int i=0; i<titreCenter.length; i++){
		    	Font font = (i==0) ? PdfUtil.FONT_10_BOLD_BLACK : PdfUtil.FONT_10_NORMAL_BLACK;
		    	Paragraph paragraph = new Paragraph(titreCenter[i], font);
		    	paragraph.setAlignment(Element.ALIGN_CENTER);
			
		    	document.add(paragraph);
		    }
		}
		//
		PdfUtil.ajouterLigneVide(document, 1);
	}
	
	/**
	 * @param document
	 * @param titreHeader
	 * @param titreCenter
	 * @throws DocumentException
	 */
	public static void ajouterTitre(Document document, String titreHeader, String ... titreCenter) throws DocumentException{
		ajouterTitreFnt(document, null, titreHeader, titreCenter);
	}
	
	/**
	 * @param document
	 * @param assembleeBean
	 */
	public static void ajouterMetaData(Document document, String title, String subject, String keyWords, String author, String creator) {
	    document.addTitle(StringUtil.getValueOrEmpty(title));
	    document.addSubject(StringUtil.getValueOrEmpty(subject));
	    document.addKeywords(StringUtil.getValueOrEmpty(keyWords));
	    document.addAuthor(StringUtil.getValueOrEmpty(author));
	    document.addCreator(StringUtil.getValueOrEmpty(creator));
	}
	
	public static void ajouterLigneSeparateurPontille(Document document) throws DocumentException{
		 DottedLineSeparator separator = new DottedLineSeparator();
	     separator.setPercentage(59500f / 523f);
	     Chunk linebreak = new Chunk(separator);
	     document.add(linebreak);
	}
	
	public static void ajouterligneSeparateur(Document document) throws DocumentException{
//		  Chunk CONNECT = new Chunk(
//		            new LineSeparator(0.5f, 95, BaseColor.BLUE, Element.ALIGN_CENTER, 3.5f));
//		        LineSeparator UNDERLINE =
//		            new LineSeparator(1, 100, null, Element.ALIGN_CENTER, -2);
		
		LineSeparator ls = new LineSeparator();
		document.add(new Chunk(ls));
	}
	
	public static PdfPCell getCell(Document document, String text, int alignement, Font font){
		PdfPCell cell = getCell(document, text, font);
		cell.setHorizontalAlignment(alignement);
		
		return cell;
	}
	
	public static PdfPCell getCellVideNoBorder(Document document){
		PdfPCell cell = new PdfPCell();
		effacerBordure(cell, EnumBorder.NO_BORDER);
		
		return cell;
	}

	public static PdfPCell getCellVide(Document document){
		return new PdfPCell();
	}

	
	public static PdfPCell getCellCheckbox(Document document, String name, String text, Boolean isBorder){
		PdfPCell cell = new PdfPCell();
		cell.setPaddingLeft(15f);
		
		if(text != null){
			cell.addElement(new Phrase(text, PdfUtil.FONT_9_NORMAL_BLACK));
		}
		cell.setCellEvent(new CheckboxCellEvent(name));
		
		if(isBorder == null || !isBorder){
			effacerBordure(cell, EnumBorder.NO_BORDER);
		}
		
		return cell;
	}
	public static PdfPCell getCellTextField(Document document, String name, Boolean isBorder){
		PdfPCell cell = new PdfPCell();
		cell.setCellEvent(new TextFieldCellEvent(name));
		
		if(isBorder == null || !isBorder){
			effacerBordure(cell, EnumBorder.NO_BORDER);
		}
		
		return cell;
	}
	
	public static PdfPCell getCellNoBorder(Document document, String text, Font font, int align){
		PdfPCell cell = getCell(document, text, font);
		effacerBordure(cell, EnumBorder.NO_BORDER);
		cell.setHorizontalAlignment(align);
		
		return cell;
	}
	
	public static PdfPCell getCellNoBorder(Document document, String text, Font font){
		return getCellNoBorder(document, text, font, Element.ALIGN_LEFT);
	}
	
	public static PdfPCell getCell(Document document, String text, Font font){
		Phrase phrase = null;
		if(font == null){
			font = PdfUtil.FONT_8_NORMAL_BLACK;
		}
		if(StringUtil.isNotEmpty(text)){
			phrase = new Phrase(text, font);
		}
		PdfPCell cell = new PdfPCell(phrase);
		return cell;
	}
	
	public static void effacerBordure(PdfPCell cell, EnumBorder ... borders){
		for (EnumBorder enumBorder : borders) {
			switch (enumBorder) {
				case BORDER_BOTTOM:{cell.setBorderWidthBottom(0f);cell.setBorderColorBottom(BaseColor.WHITE);}; break;
				case BORDER_LEFT:{cell.setBorderWidthLeft(0f);cell.setBorderColorLeft(BaseColor.WHITE);}; break;
				case BORDER_RIGHT:{cell.setBorderWidthRight(0f);cell.setBorderColorRight(BaseColor.WHITE);}; break;
				case BORDER_TOP:{cell.setBorderWidthTop(0f);cell.setBorderColorTop(BaseColor.WHITE);}; break;
				case NO_BORDER : {cell.setBorder(Rectangle.NO_BORDER);};
			}
		}
	}
	public static String getMttAddZero(BigDecimal mtt){
		return (mtt == null || mtt.compareTo(BigDecimalUtil.ZERO)==0) ? "0.00" : ""+mtt;
	}
	public static String getMtt(BigDecimal mtt){
		return (mtt == null || mtt.compareTo(BigDecimalUtil.ZERO)==0) ? "" : ""+mtt;
	}
	/**
	 * @param table
	 */
	public static void setCustomBorder(PdfPTable table){
		ArrayList<PdfPRow> lmist = table.getRows();
		for (PdfPRow pdfPRow : lmist) {
			PdfPCell[] cells = pdfPRow.getCells();
			for (PdfPCell pdfPCell : cells) {
				if(pdfPCell != null){
					pdfPCell.setBorderWidth(0.001f);
					pdfPCell.setBorderColor(BaseColor.GRAY);
				}
			} 
		}
	}

}

