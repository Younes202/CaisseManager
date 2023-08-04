package appli.model.domaine.stock.persistant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import framework.model.beanContext.BasePersistant;

@Entity
@Table(name = "marque")
@NamedQuery(name="marque_find", query="from MarquePersistant marque" +
		" order by marque.libelle")
public class MarquePersistant  extends BasePersistant {
	@Column(length = 80, nullable = false)
	private String code;
	@Column(length = 120, nullable = false)
	private String libelle;
	@Column(length = 255)
	private String adresse;
	@Column(length = 50)
	private String telephone;
	@Column(length = 50)
	private String fax;
	@Column(length = 120)
	private String site_web;
	
	@Column(length = 255)
	private String description;
	@Column(length = 255)
	private String logo;
	@Column
	private Boolean is_disable;
	
	@ManyToOne
	@JoinColumn(name = "famille_fourn_id", referencedColumnName="id")
	private FamilleFournisseurPersistant opc_famille_fourn;
	
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Boolean getIs_disable() {
		return is_disable;
	}
	public void setIs_disable(Boolean is_disable) {
		this.is_disable = is_disable;
	}
	public String getAdresse() {
		return adresse;
	}
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getSite_web() {
		return site_web;
	}
	public void setSite_web(String site_web) {
		this.site_web = site_web;
	}
	public FamilleFournisseurPersistant getOpc_famille_fourn() {
		return opc_famille_fourn;
	}
	public void setOpc_famille_fourn(FamilleFournisseurPersistant opc_famille_fourn) {
		this.opc_famille_fourn = opc_famille_fourn;
	}
}