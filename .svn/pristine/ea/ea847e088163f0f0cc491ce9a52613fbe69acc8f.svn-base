package framework.model.beanContext;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "compte_bancaire", indexes={
		@Index(name="IDX_CPT_BNQ_FUNC", columnList="code_func")
	})
@NamedQueries({
	@NamedQuery(name="compteBancaire_find", query="from CompteBancairePersistant compteBancaire order by compteBancaire.libelle"),
})
public class CompteBancairePersistant extends BasePersistant { 
	@Column(length = 80, nullable = false)
	private String libelle;

	@Column(length = 80)
	
	private String banque;
	@Column(length = 50)
	private String titulaire;
	
	@Column(length = 5)
	private String rib_banque;

	@Column(length = 5)
	private String rib_guichet;

	@Column
	private Boolean is_default;// Si c'esr le compte par défaut
	
	@Column(length = 16)
	private String rib_numero;

	@Column(length = 2)
	private String rib_cle;
	@Column
	private Boolean is_disable;

    @Column(length = 120)
    private String adresse_rue;
    @Column(length = 120)
    private String adresse_compl;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ville_id", referencedColumnName="id")           
    private VillePersistant opc_ville;
    @Column(length = 20)
	private String type_compte;
	
	@Transient
	private  BigDecimal mtt_solde;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "compte_id", referencedColumnName="id")
	private ComptePersistant opc_compte;
    
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	
	public String getBanque() {
		return banque;
	}
	public void setBanque(String banque) {
		this.banque = banque;
	}
	public String getTitulaire() {
		return titulaire;
	}
	public void setTitulaire(String titulaire) {
		this.titulaire = titulaire;
	}
	public String getRib_banque() {
		return rib_banque;
	}
	public void setRib_banque(String rib_banque) {
		this.rib_banque = rib_banque;
	}
	public String getRib_guichet() {
		return rib_guichet;
	}
	public void setRib_guichet(String rib_guichet) {
		this.rib_guichet = rib_guichet;
	}
	public String getRib_numero() {
		return rib_numero;
	}
	public void setRib_numero(String rib_numero) {
		this.rib_numero = rib_numero;
	}
	public String getRib_cle() {
		return rib_cle;
	}
	public void setRib_cle(String rib_cle) {
		this.rib_cle = rib_cle;
	}
	public String getAdresse_rue() {
		return adresse_rue;
	}
	public void setAdresse_rue(String adresse_rue) {
		this.adresse_rue = adresse_rue;
	}
	public String getAdresse_compl() {
		return adresse_compl;
	}
	public void setAdresse_compl(String adresse_compl) {
		this.adresse_compl = adresse_compl;
	}
	public VillePersistant getOpc_ville() {
		return opc_ville;
	}
	public void setOpc_ville(VillePersistant opc_ville) {
		this.opc_ville = opc_ville;
	}
	public Boolean getIs_default() {
		return is_default;
	}
	public void setIs_default(Boolean is_default) {
		this.is_default = is_default;
	}
	public BigDecimal getMtt_solde() {
		return mtt_solde;
	}
	public void setMtt_solde(BigDecimal mtt_solde) {
		this.mtt_solde = mtt_solde;
	}
	public Boolean getIs_disable() {
		return is_disable;
	}
	public void setIs_disable(Boolean is_disable) {
		this.is_disable = is_disable;
	}
	public String getType_compte() {
		return type_compte;
	}
	public void setType_compte(String type_compte) {
		this.type_compte = type_compte;
	}
	public ComptePersistant getOpc_compte() {
		return opc_compte;
	}
	public void setOpc_compte(ComptePersistant opc_compte) {
		this.opc_compte = opc_compte;
	}
}
