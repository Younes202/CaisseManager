package appli.model.domaine.util_srv.raz;

import java.math.BigDecimal;

public class RazDetail{
	private String libelle;
	private Integer quantite = 0;
	private BigDecimal montant;
	private BigDecimal montant2;
	
	public RazDetail(){
		
	}
	public RazDetail(String libelle){
		this.libelle = libelle;
	}
	public RazDetail(String libelle, BigDecimal montant){
		this.libelle = libelle;
		this.montant = montant;
	}
	
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public Integer getQuantite() {
		return quantite;
	}
	public void setQuantite(Integer quantite) {
		this.quantite = quantite;
	}
	public BigDecimal getMontant() {
		return montant;
	}
	public void setMontant(BigDecimal montant) {
		this.montant = montant;
	}
	public BigDecimal getMontant2() {
		return montant2;
	}
	public void setMontant2(BigDecimal montant2) {
		this.montant2 = montant2;
	}
}