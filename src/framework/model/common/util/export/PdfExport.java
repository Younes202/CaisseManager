package framework.model.common.util.export;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import framework.component.complex.table.export.ExportTableBean;
import framework.controller.bean.ColumnsExportBean;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.ControllerBeanUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.NumericUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.PdfUtil;

public class PdfExport {

	private static final int DEFAULT_WIDTH = 150;

	/** Times Roman 18 Bold */
	private static final Font CATFONT = new Font(Font.getFamily("TIMES_ROMAN"), 18, Font.BOLD);
	/** Times Roman 10 */
	private static final Font SMALL_10 = new Font(Font.getFamily("TIMES_ROMAN"), 10, Font.NORMAL);

	/**
	 * @param exportBean
	 * @param fileOutputStream
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void execute(ExportTableBean exportBean, FileOutputStream fileOutputStream) throws IOException, DocumentException{
		Document document = creerDocument(fileOutputStream);
		addMetaData(document, exportBean);
		addTitlePage(document, exportBean);
		addContent(document, exportBean);
		document.close();
		
		// Add logo
		//Image image = Image.getInstance ("devi.jpg");
		//Paragraph para=new Paragraph("RoseIndia.net sdfsd sdfsd fs");
		//document.add(para);
		//document.add(image);
		//
	//	document.add(table);
		//
		//document.close();
	}
	
	private static void addContent(Document document, ExportTableBean exportBean) throws DocumentException {
		PdfPTable table = createTable(document, exportBean);
		document.add(table);
	}

	private static PdfPTable createTable(Document document, ExportTableBean exportBean) throws DocumentException {
		java.util.List<ColumnsExportBean> listColumn = exportBean.getListColumn();
		// Start table
		PdfPTable table = new PdfPTable(listColumn.size());
		
		// List widths
		int idx = 0;
		int[] widths = new int[listColumn.size()];
		for(ColumnsExportBean columnBean : listColumn){
			String width = columnBean.getWidth();
			if(width != null){
				width = width.replaceAll("px", "");
				width = width.replaceAll("%", "");
			}
			widths[idx] = StringUtil.isEmpty(width) ? DEFAULT_WIDTH : NumericUtil.getIntOrDefault(width);
			idx++;
		}
		//float[] widths = {0.5f,0.14f,0.14f,0.04f,0.06f,0.015f,0.015f,0.015f,0.015f,0.015f,0.045f};
		table.setWidthPercentage(100f);
		table.setWidths(widths);// largeur par cellule

		// Title of table
		Font headerFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLDITALIC, BaseColor.DARK_GRAY);
		Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
		//
		PdfPCell cell = null;

		// Calculate header columns
		for(ColumnsExportBean columnBean : listColumn){
			cell = new PdfPCell (new Paragraph (columnBean.getLabel(), headerFont));
			cell.setHorizontalAlignment (Element.ALIGN_CENTER);
			cell.setBackgroundColor (new BaseColor(255, 255, 224));
			cell.setPadding (2.0f);
			table.addCell (cell);
		}

		// Get export data
		java.util.List<?> dataExport = exportBean.getDataExport();
		// Calculate body columns
		if(dataExport != null){
			for(Object bean : dataExport){
				String aliasBean = ControllerBeanUtil.getAliasBeanByObject(bean);
				//
				for(ColumnsExportBean columnBean : listColumn){
					String valueSt = ExportUtil.getValue(columnBean, bean, aliasBean);
					valueSt = (valueSt != null) ? valueSt.replaceAll("null", "") : "";
					
					if(ProjectConstante.TYPE_DATA_ENUM.INTEGER.getType().equalsIgnoreCase(columnBean.getType())){
						String formatNumber = StringUtil.isNotEmpty(valueSt) ? BigDecimalUtil.formatNumber(Integer.valueOf(valueSt)) : "";
						table.addCell(PdfUtil.getCell(document, formatNumber, Element.ALIGN_RIGHT, bodyFont));
					} else if(ProjectConstante.TYPE_DATA_ENUM.LONG.getType().equalsIgnoreCase(columnBean.getType())){
						String formatNumber = StringUtil.isNotEmpty(valueSt) ? BigDecimalUtil.formatNumber(BigDecimalUtil.get(valueSt)) : "";
						table.addCell(PdfUtil.getCell(document, formatNumber, Element.ALIGN_RIGHT, bodyFont));
					} else if(ProjectConstante.TYPE_DATA_ENUM.DECIMAL.getType().equalsIgnoreCase(columnBean.getType())){
						String formatNumber = StringUtil.isNotEmpty(valueSt) ? BigDecimalUtil.formatNumber(BigDecimalUtil.get(valueSt)) : "";
						table.addCell(PdfUtil.getCell(document, formatNumber, Element.ALIGN_RIGHT, bodyFont));
					} else if(ProjectConstante.TYPE_DATA_ENUM.DATE.getType().equalsIgnoreCase(columnBean.getType())){
						table.addCell(PdfUtil.getCell(document, valueSt, Element.ALIGN_CENTER, bodyFont));	
					} else{
						table.addCell(PdfUtil.getCell(document, valueSt, bodyFont));	
					}
				}
			}
		}
		
	    // Ajout d'un espace entre la PdfPTable et l'élément précédent.
	    table.setSpacingBefore(15f);
	 
	    return table;
	}
	
	/**
	 * Ajoute 4 paragraphes / - Titre du document. - Rapport généré par ... - Ce
	 * document décrit ... - Ce document est ...
	 *
	 * @param document Document à enrichir
	 * @throws DocumentException Si un problème survient lors de l'ajout des
	 *             données.
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	private static void addTitlePage(Document document, ExportTableBean exportBean) throws DocumentException, MalformedURLException, IOException {
	    // Ecriture du header
		/*Image image = Image.getInstance (new URL("/resources/framework/img/logo-small.png"));
		image.setAlignment(Element.ALIGN_LEFT);
		document.add(image);*/
		
	    Paragraph titleParagraphe = new Paragraph(exportBean.getTitle(), CATFONT);
	    titleParagraphe.setAlignment(Element.ALIGN_CENTER);
	    ajouterLigneVide(titleParagraphe, 2);
	    document.add(titleParagraphe);
		
	    Paragraph preface = new Paragraph();
	    preface.add(new Paragraph("Date de génération : " + DateUtil.getFullCurrentDate(), SMALL_10));
//	    preface.add(new Paragraph("Auteur : " + StringUtil.getValueOrEmpty(exportBean.getAutor()) , SMALL_10));
	    ajouterLigneVide(preface, 1);
	    document.add(preface);
	 
	    // Debut d'une nouvelle page
	    //document.newPage();
	}
	
	/**
	 * Ajoute une ligne vide number fois dans le Paragraph passé en paramètre.
	 *
	 * @param paragraph A remplir avec les lignes vides.
	 * @param number Nombre de lignes vides à ajouter.
	 */
	private static void ajouterLigneVide(Paragraph paragraph, int number) {
	    for (int i = 0; i < number; i++) {
	        paragraph.add(new Paragraph(" "));
	    }
	}
	
	/**
	 * Crée un Document vide.
	 *
	 * @param file Chemin du fichier à créer.
	 * @return Document s'il n'y a pas eu d'erreur.
	 * @throws IOException s'il y a eu une erreur avec le nom de fichier fourni.
	 * @throws DocumentException s'il y a eu une erreur côté iText.
	 */
	private static Document creerDocument(OutputStream fileOutput) throws DocumentException, IOException {
	    Document document = new Document();
	 
	    PdfWriter pdfWriter = PdfWriter.getInstance(document, fileOutput);
	    //pdfWriter.setPageEvent(this);
	    // Préférence de lecture : 2 pages en colonne.
	    pdfWriter.setViewerPreferences(PdfWriter.PageLayoutOneColumn);
	    document.open();
	 
	    return document;
	}
	
	/**
	 * Ajout de données de type Metadata au document.
	 *
	 * @param document Document auquel il faut rajouter les metadatas.
	 */
	private static void addMetaData(Document document, ExportTableBean exportBean) {
	    document.addTitle(StringUtil.getValueOrEmpty(exportBean.getTitle()));
	    document.addSubject("Export des données du résultat de recherche");
	    document.addKeywords("PDF");
//	    document.addAuthor(StringUtil.getValueOrEmpty(exportBean.getAutor()));
//	    document.addCreator(StringUtil.getValueOrEmpty(exportBean.getAutor()));
	}
}
