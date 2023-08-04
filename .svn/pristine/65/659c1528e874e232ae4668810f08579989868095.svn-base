package appli.model.domaine.personnel.persistant;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import appli.model.domaine.stock.persistant.PersonneBasePersistant;
import framework.model.util.GsonExclude;

@Entity
@Table(name = "societe_livraison")
public class SocieteLivrPersistant extends PersonneBasePersistant {

    @Column(length = 15, scale = 6, precision = 15)
    private BigDecimal taux_marge;
    @Column(length = 15, scale = 6, precision = 15)
    private BigDecimal mtt_marge;
    @Column
    private Boolean is_ventille;
	@Column
	private Boolean is_solde_neg;// Autoriser solde négatif
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal plafond_dette;
	
	@Column
    private Boolean is_portefeuille;
    @Column(length = 15, scale = 6, precision = 15)
    private BigDecimal taux_portefeuille;
    @Column(length = 15, scale = 6, precision = 15)
	private BigDecimal solde_portefeuille;// Champs calculé par les rechargements carte

    @GsonExclude
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "fournisseur_id", referencedColumnName = "id")
    List<SocieteLivrContactPersistant> list_contact;
    
	public BigDecimal getMtt_marge() {
		return mtt_marge;
	}

	public void setMtt_marge(BigDecimal mtt_marge) {
		this.mtt_marge = mtt_marge;
	}

	public BigDecimal getTaux_marge() {
		return taux_marge;
	}

	public void setTaux_marge(BigDecimal taux_marge) {
		this.taux_marge = taux_marge;
	}

//	public List<CarteFideliteClientPersistant> getList_cartes() {
//		return list_cartes;
//	}
//
//	public void setList_cartes(List<CarteFideliteClientPersistant> list_cartes) {
//		this.list_cartes = list_cartes;
//	}

	public Boolean getIs_ventille() {
		return is_ventille;
	}

	public void setIs_ventille(Boolean is_ventille) {
		this.is_ventille = is_ventille;
	}

	public List<SocieteLivrContactPersistant> getList_contact() {
		return list_contact;
	}

	public void setList_contact(List<SocieteLivrContactPersistant> list_contact) {
		this.list_contact = list_contact;
	}

	public Boolean getIs_solde_neg() {
		return is_solde_neg;
	}

	public void setIs_solde_neg(Boolean is_solde_neg) {
		this.is_solde_neg = is_solde_neg;
	}

	public BigDecimal getPlafond_dette() {
		return plafond_dette;
	}

	public void setPlafond_dette(BigDecimal plafond_dette) {
		this.plafond_dette = plafond_dette;
	}

	public Boolean getIs_portefeuille() {
		return is_portefeuille;
	}

	public void setIs_portefeuille(Boolean is_portefeuille) {
		this.is_portefeuille = is_portefeuille;
	}

	public BigDecimal getTaux_portefeuille() {
		return taux_portefeuille;
	}

	public void setTaux_portefeuille(BigDecimal taux_portefeuille) {
		this.taux_portefeuille = taux_portefeuille;
	}

	public BigDecimal getSolde_portefeuille() {
		return solde_portefeuille;
	}

	public void setSolde_portefeuille(BigDecimal solde_portefeuille) {
		this.solde_portefeuille = solde_portefeuille;
	}
}
