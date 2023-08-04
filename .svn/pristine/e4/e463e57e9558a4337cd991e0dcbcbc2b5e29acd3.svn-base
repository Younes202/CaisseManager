package framework.model.util;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import appli.controller.domaine.util_erp.ContextAppli;
import framework.model.beanContext.SocietePersistant;
import framework.model.common.util.DateUtil;
import framework.model.common.util.StringUtil;

public class PdfHeaderFooter extends PdfPageEventHelper {
    /** The template with the total number of pages. */
    private PdfTemplate total;
    private String footerStr;
    
	public void setFooterStr(String footerStr) {
		this.footerStr = footerStr;
	}

	/**
     * Creates the PdfTemplate that will hold the total number of pages.
     * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(
     *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
     */
    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        total = writer.getDirectContent().createTemplate(30, 16);
    }
    
	/* (non-Javadoc)
	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onStartPage(com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
	 */
	@Override
	public void onStartPage(PdfWriter writer, Document document) {
	
	}
	
	/* (non-Javadoc)
	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
	 */
	@Override
    public void onEndPage (PdfWriter writer, Document document) {
		 PdfPTable table = new PdfPTable(3);
         try {
        	 Rectangle rect = writer.getBoxSize("art");
        	 
             table.setWidths(new float[]{88f, 10f, 2f});
             table.setTotalWidth(rect.getWidth());
             table.setLockedWidth(true);
             table.getDefaultCell().setFixedHeight(20);
             table.getDefaultCell().setBorder(Rectangle.BOTTOM);
             
             // ------------------------------- Option d'édition --------------------------------
//           	 mention = mention + "Edité par : "+ContextRestaurent.getUserBean().getLogin();
           	 String mention = DateUtil.getFullCurrentDate();
             
           	 if(StringUtil.isNotEmpty(this.footerStr)) {
           		 mention += " | " + this.footerStr; 
           	 }
           	 
             table.addCell(new Phrase(mention, PdfUtil.FONT_7_NORMAL_BLACK));
             
             
             table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
             table.addCell(new Phrase(String.format("Page %d /", writer.getPageNumber()),  PdfUtil.FONT_7_NORMAL_BLACK));
             
             PdfPCell cell = new PdfPCell(Image.getInstance(total));
             cell.setBorder(Rectangle.BOTTOM);
             table.addCell(cell);
             
             //(rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 18, 0)---> centre
             
             table.writeSelectedRows(0, -1, rect.getLeft(), rect.getBottom() - 18, writer.getDirectContent());
         }
         catch(DocumentException de) {
             throw new ExceptionConverter(de);
         }
    }
	
    /**
     * Fills out the total number of pages before the document is closed.
     * @see com.itextpdf.text.pdf.PdfPageEventHelper#onCloseDocument(
     *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
     */
   @Override
	public void onCloseDocument(PdfWriter writer, Document document) {
	   ColumnText.showTextAligned(total, Element.ALIGN_LEFT,
               new Phrase(String.valueOf(writer.getPageNumber() - 1), PdfUtil.FONT_8_NORMAL_BLACK), 0, 6, 0);
    }
}
