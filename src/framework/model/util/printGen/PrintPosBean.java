package framework.model.util.printGen;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class PrintPosBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String printers;
	private BigDecimal ticketHeight;
	private BigDecimal ticketWidth; 
	private String orientation;
	private List<PrintPosDetailBean> listDetail;
	private int posX = 0;
	private int posY = 10;
	private int maxLineLength = 50;
	private int nbrTicket = 1;
	private File A4File;// Cas impression A4 PDF
	
	public String getPrinters() {
		return printers;
	}
	public void setPrinters(String printers) {
		this.printers = printers;
	}
	public BigDecimal getTicketHeight() {
		return ticketHeight;
	}
	public void setTicketHeight(BigDecimal ticketHeight) {
		this.ticketHeight = ticketHeight;
	}
	public List<PrintPosDetailBean> getListDetail() {
		return listDetail;
	}
	public void setListDetail(List<PrintPosDetailBean> listDetail) {
		this.listDetail = listDetail;
	}
	public BigDecimal getTicketWidth() {
		return ticketWidth;
	}
	public void setTicketWidth(BigDecimal ticketWidth) {
		this.ticketWidth = ticketWidth;
	}
	public String getOrientation() {
		return orientation;
	}
	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}
	public int getPosX() {
		return posX;
	}
	public void setPosX(int posX) {
		this.posX = posX;
	}
	public int getPosY() {
		return posY;
	}
	public void setPosY(int posY) {
		this.posY = posY;
	}
	public int getMaxLineLength() {
		return maxLineLength;
	}
	public void setMaxLineLength(int maxLineLength) {
		this.maxLineLength = maxLineLength;
	}
	public File getA4File() {
		return A4File;
	}
	public void setA4File(File a4File) {
		A4File = a4File;
	}
	public int getNbrTicket() {
		return nbrTicket;
	}
	public void setNbrTicket(int nbrTicket) {
		this.nbrTicket = nbrTicket;
	}
	
}
