package framework.controller.bean;

public class PagerBean {
	private int nbrPage;// Nombre total de pages
	private int nbrLigne;// Nombre total de lignes
	private int startIdx;// Index de début
	private int currPage;// Page actuelle
	private int elementParPage = 20;// Elément par page
	
	public int getNbrPage() {
		 this.nbrPage =  ((Double)Math.ceil(Double.valueOf(this.nbrLigne)/Double.valueOf(this.elementParPage))).intValue();
		 return this.nbrPage;
	}
	public int getNbrLigne() {
		return nbrLigne;
	}
	public void setNbrLigne(int nbrLigne) {
		this.nbrLigne = nbrLigne;
		
		if(this.currPage * this.elementParPage > this.nbrLigne){
        	this.currPage = ((Double)Math.ceil(Double.valueOf(this.nbrLigne)/Double.valueOf(this.elementParPage))).intValue();
        	this.startIdx = 0;
        }
	}
	public int getStartIdx() {
		return startIdx;
	}
	public void setStartIdx(int startIdx) {
		this.startIdx = startIdx;
	}
	public int getCurrPage() {
		return currPage;
	}
	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}
	public int getElementParPage() {
		return elementParPage;
	}
	public void setElementParPage(int elementParPage) {
		this.elementParPage = elementParPage;
	}
	
	public String getPagerText(){
		int nextP = (this.currPage+1)*this.elementParPage;
		if(nextP >= this.nbrLigne){
			nextP = this.nbrLigne;
		}
		return "Page ("+(this.currPage+1)+"/"+this.nbrPage+") "+(this.startIdx+1)+"-"+nextP+"/"+this.nbrLigne;
	}
}
