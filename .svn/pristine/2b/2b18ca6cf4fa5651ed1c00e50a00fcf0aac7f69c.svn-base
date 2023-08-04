package appli.model.domaine.stock.persistant;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import framework.model.beanContext.BasePersistant;
import framework.model.beanContext.VillePersistant;
import framework.model.common.util.StringUtil;

@MappedSuperclass 
public abstract class PersonneBasePersistant extends BasePersistant {
	@Column(length = 20)
	private String numero;
	
	@Column(length = 1)
	private String civilite;
	@Column(length = 50)
	private String nom;
	@Column(length = 50)
	private String prenom;
	@Column(length = 20)
	private String cin;
	@Column(length = 50)
	private String code_barre;
	@Column
	private Boolean is_disable;
	
	// Personne morale ---------------
	@Column(length = 30)
	private String siret;
	@Column(length = 30)
	private String rcs;
	@Column(length = 30)
	private String ape;
    @Column(length = 20)
    private String ice;
    @Column(length = 20)
    private String numero_tva;
    @Column(length = 50)
    private String id_fiscal;
	//--------------------------------
	
	@Column(length = 20)
	private String telephone;
	@Column(length = 20)
	private String telephone2;
	@Column(length = 20)
	private String fax;
	@Column(length = 20)
	private String fax2;
	@Column(length = 20)
	private String portable;
	@Column(length = 50)
	private String mail;
	@Column(length = 50)
	private String site;
	@Column(length = 80)
	private String marque;
	
	@Column(length = 120)
	private String adresse_rue;
	@Column(length = 120)
	private String adresse_compl;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ville_id", referencedColumnName = "id")
	private VillePersistant opc_ville;
	//--------------------------- Facturation ------------------------
	@Column(length = 20)
    private String ice_facture;
	@Column(length = 50)
	private String nom_facture;
	@Column(length = 50)
	private String prenom_facture;
	@Column(length = 120)
	private String adresse_rue_facture;
	@Column(length = 120)
	private String adresse_compl_facture;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ville_fact_id", referencedColumnName = "id")
	private VillePersistant opc_ville_facture;
	//----------------------------------------------------------------
    /************* attributs pour situation fournisseur ***************/
	@Transient
	private String villeStr;
	@Transient
	private BigDecimal mtt_total;
	@Transient
	private BigDecimal mtt_paye;
	@Transient
	private BigDecimal mtt_non_paye;
	@Transient
	private BigDecimal mtt_avoir;
	
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getCivilite() {
		return civilite;
	}
	public void setCivilite(String civilite) {
		this.civilite = civilite;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getCin() {
		return cin;
	}
	public void setCin(String cin) {
		this.cin = cin;
	}
	public String getCode_barre() {
		return code_barre;
	}
	public void setCode_barre(String code_barre) {
		this.code_barre = code_barre;
	}
	public Boolean getIs_disable() {
		return is_disable;
	}
	public void setIs_disable(Boolean is_disable) {
		this.is_disable = is_disable;
	}
	public String getSiret() {
		return siret;
	}
	public void setSiret(String siret) {
		this.siret = siret;
	}
	public String getRcs() {
		return rcs;
	}
	public void setRcs(String rcs) {
		this.rcs = rcs;
	}
	public String getApe() {
		return ape;
	}
	public void setApe(String ape) {
		this.ape = ape;
	}
	public String getIce() {
		return ice;
	}
	public void setIce(String ice) {
		this.ice = ice;
	}
	public String getNumero_tva() {
		return numero_tva;
	}
	public void setNumero_tva(String numero_tva) {
		this.numero_tva = numero_tva;
	}
	public String getId_fiscal() {
		return id_fiscal;
	}
	public void setId_fiscal(String id_fiscal) {
		this.id_fiscal = id_fiscal;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getTelephone2() {
		return telephone2;
	}
	public void setTelephone2(String telephone2) {
		this.telephone2 = telephone2;
	}
	public String getPortable() {
		return portable;
	}
	public void setPortable(String portable) {
		this.portable = portable;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getMarque() {
		return marque;
	}
	public void setMarque(String marque) {
		this.marque = marque;
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
	public String getIce_facture() {
		return ice_facture;
	}
	public void setIce_facture(String ice_facture) {
		this.ice_facture = ice_facture;
	}
	public String getNom_facture() {
		return nom_facture;
	}
	public void setNom_facture(String nom_facture) {
		this.nom_facture = nom_facture;
	}
	public String getPrenom_facture() {
		return prenom_facture;
	}
	public void setPrenom_facture(String prenom_facture) {
		this.prenom_facture = prenom_facture;
	}
	public String getAdresse_rue_facture() {
		return adresse_rue_facture;
	}
	public void setAdresse_rue_facture(String adresse_rue_facture) {
		this.adresse_rue_facture = adresse_rue_facture;
	}
	public String getAdresse_compl_facture() {
		return adresse_compl_facture;
	}
	public void setAdresse_compl_facture(String adresse_compl_facture) {
		this.adresse_compl_facture = adresse_compl_facture;
	}
	public VillePersistant getOpc_ville_facture() {
		return opc_ville_facture;
	}
	public void setOpc_ville_facture(VillePersistant opc_ville_facture) {
		this.opc_ville_facture = opc_ville_facture;
	}
	public String getVilleStr() {
		return villeStr;
	}
	public void setVilleStr(String villeStr) {
		this.villeStr = villeStr;
	}
	public BigDecimal getMtt_total() {
		return mtt_total;
	}
	public void setMtt_total(BigDecimal mtt_total) {
		this.mtt_total = mtt_total;
	}
	public BigDecimal getMtt_paye() {
		return mtt_paye;
	}
	public void setMtt_paye(BigDecimal mtt_paye) {
		this.mtt_paye = mtt_paye;
	}
	public BigDecimal getMtt_non_paye() {
		return mtt_non_paye;
	}
	public void setMtt_non_paye(BigDecimal mtt_non_paye) {
		this.mtt_non_paye = mtt_non_paye;
	}	
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getFax2() {
		return fax2;
	}
	public void setFax2(String fax2) {
		this.fax2 = fax2;
	}
	public BigDecimal getMtt_avoir() {
		return mtt_avoir;
	}
	public void setMtt_avoir(BigDecimal mtt_avoir) {
		this.mtt_avoir = mtt_avoir;
	}
	public String getAdressFactureFull() {
		String adresse = "";
		if (StringUtil.isNotEmpty(this.adresse_rue_facture)) {
			adresse = adresse + this.adresse_rue_facture;
		}
		if (StringUtil.isNotEmpty(this.adresse_compl_facture)) {
			adresse = adresse + " - " + this.adresse_compl_facture;
		}
		if (this.opc_ville_facture != null) {
			adresse = adresse + " - " + StringUtil.getValueOrEmpty(this.opc_ville_facture.getCode_postal()) + " " + StringUtil.getValueOrEmpty(this.opc_ville_facture.getLibelle());
		}
		return adresse;
	}	
	public String getAdressFull() {
		String adresse = "";
		if (StringUtil.isNotEmpty(this.adresse_rue)) {
			adresse = adresse + this.adresse_rue;
		}
		if (StringUtil.isNotEmpty(this.adresse_compl)) {
			adresse = adresse + " - " + this.adresse_compl;
		}
		if (this.opc_ville != null) {
			adresse = adresse + " - " + StringUtil.getValueOrEmpty(this.opc_ville.getCode_postal()) + " " + StringUtil.getValueOrEmpty(this.opc_ville.getLibelle());
		}
		return adresse;
	}
	
	public String getNomPrenom() {
		return this.nom+(StringUtil.isNotEmpty(this.prenom) ? " "+this.prenom : "");
	}
}
