package appli.model.domaine.util_srv.printCom;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;

import javax.print.DocFlavor;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

import framework.model.common.util.StringUtil;

public class PrintTicketPdf {
	public static void printA4Pdf(String imprimnates, File pdfFile) {
		new Thread(() -> {
			String[] listImprimante = StringUtil.getArrayFromStringDelim(imprimnates, "|");
	    	if(listImprimante == null || listImprimante.length == 0) {
	    		return;
	    	}
	        for (String imprimante : listImprimante) {
	        	try {
					sendImpression(imprimante, pdfFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
		}).start(); 
	}
	
	private static void sendImpression(String imprimante, File pdfFile) throws IOException, PrinterException {
		PrintService myService = null;
		DocFlavor psInFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		PrintService[] ps = PrintServiceLookup.lookupPrintServices(psInFormat, aset);
		
		for (PrintService printService : ps) {
			System.out.println(printService.getName());
		}
		
	    for (PrintService printService : ps) {
	        if (printService.getName().equals(imprimante)) {
	            myService = printService;
	            break;
	        }
	    }
	    
	    PDDocument pdf = PDDocument.load(pdfFile);
	    PrinterJob job = PrinterJob.getPrinterJob();
	    job.setPrintService(myService);
	    job.setPageable(new PDFPageable(pdf));
	    job.print();
	}
	
	public static void main(String[] args) throws PrintException, IOException, PrinterException {
		PrintService myService = null;
		DocFlavor psInFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		PrintService[] ps = PrintServiceLookup.lookupPrintServices(psInFormat, aset);
		
		for (PrintService printService : ps) {
			System.out.println(printService.getName());
		}
		
	    for (PrintService printService : ps) {
	        if (printService.getName().equals("Canon E3100 series")) {
	            myService = printService;
	            break;
	        }
	    }
	    
	    PDDocument pdf = PDDocument.load(new File("C:/Users/ACER/Desktop/Facture_Caisse Manager_202300012.pdf"));
	    PrinterJob job = PrinterJob.getPrinterJob();
	    job.setPrintService(myService);
	    job.setPageable(new PDFPageable(pdf));
	    job.print();
	}

}