package framework.model.common.util.itext;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RadioCheckField;


public class CheckboxCellEvent implements PdfPCellEvent {
        // The name of the check box field
	private String name;
       
	// We create a cell event
    public CheckboxCellEvent(String name) {
        this.name = name;
    }
    // We create and add the check box field
    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        PdfWriter writer = canvases[0].getPdfWriter();
        // define the coordinates of the middle
//        float x = (position.getLeft() + position.getRight()) / 2;
//        float y = (position.getTop() + position.getBottom()) / 2;
        
        float x = (position.getLeft() + position.getRight()) / 2;
        float y = position.getBottom() + 5;// + position.getBottom()) / 2;
        
        // define the position of a check box that measures 20 by 20
        Rectangle rect = new Rectangle(x - 6, y - 6, x + 6, y + 6);
        // define the check box
        RadioCheckField checkbox = new RadioCheckField(writer, rect, name, "Yes");
        checkbox.setBorderWidth(1f);
        checkbox.setBorderColor(BaseColor.BLACK);
        
        checkbox.setAlignment(Element.ALIGN_LEFT);
        
        // add the check box as a field
        try {
            writer.addAnnotation(checkbox.getCheckField());
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }
}