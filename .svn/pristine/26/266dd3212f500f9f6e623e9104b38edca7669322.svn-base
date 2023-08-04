package framework.model.common.util.itext;

import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfBorderDictionary;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.TextField;

public class TextFieldCellEvent implements PdfPCellEvent {

    // The name of the check box field
    private String name;

    // We create a cell event
    public TextFieldCellEvent(String name) {
        this.name = name;
    }
    
	/**
     * Creates and adds a text field that will be added to a cell.
     * @see com.itextpdf.text.pdf.PdfPCellEvent#cellLayout(com.itextpdf.text.pdf.PdfPCell,
     *      com.itextpdf.text.Rectangle, com.itextpdf.text.pdf.PdfContentByte[])
     */
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        PdfWriter writer = canvases[0].getPdfWriter();
        
        
        float x = position.getLeft();
        float y = position.getBottom() + 5;
        
        // define the position of a check box that measures 20 by 20
        Rectangle rect = new Rectangle(x - 6, y - 6, x + 60, y + 10);
        
        
        TextField text = new TextField(writer, rect, name);
        
       // text.setBackgroundColor(new GrayColor(0.75f));
        text.setBorderStyle(PdfBorderDictionary.STYLE_SOLID);
        text.setBorderColor(BaseColor.BLACK);
        text.setBorderWidth(1);
        text.setMaxCharacterLength(10);
        text.setFontSize(9);
//        text.setOptions(TextField.COMB);
        
        //text.setFontSize(0);
        //text.setAlignment(Element.ALIGN_CENTER);
        //    text.setOptions(TextField.REQUIRED);
//            break;
//        case 2:
//            text.setMaxCharacterLength(8);
//            text.setOptions(TextField.COMB);
//            text.setBorderStyle(PdfBorderDictionary.STYLE_SOLID);
//            text.setBorderColor(BaseColor.BLUE);
//            text.setBorderWidth(2);
//            break;
//        case 3:
//            text.setBorderStyle(PdfBorderDictionary.STYLE_INSET);
//            text.setOptions(TextField.PASSWORD);
//            text.setVisibility(TextField.VISIBLE_BUT_DOES_NOT_PRINT);
//            break;
//        case 4:
//            text.setBorderStyle(PdfBorderDictionary.STYLE_DASHED);
//            text.setBorderColor(BaseColor.RED);
//            text.setBorderWidth(2);
//            text.setFontSize(8);
//            text.setText(
//                "Enter the reason why you want to win a free accreditation for the Foobar Film Festival");
//            text.setOptions(TextField.MULTILINE | TextField.REQUIRED);
//            break;
//        }
        try {
            PdfFormField field = text.getTextField();
            writer.addAnnotation(field);
        }
        catch(IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
        catch(DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

}
