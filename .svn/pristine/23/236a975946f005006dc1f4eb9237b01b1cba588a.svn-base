package appli.model.domaine.administration.persistant;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import framework.model.beanContext.BasePersistant;
import framework.model.beanContext.VillePersistant;
import framework.model.beanContext.VilleQuartierPersistant;
import framework.model.common.util.StringUtil;
   
@Entity
@Table(name="lieux", indexes= {
	@Index(name="IDX_LIEUX_FUNC", columnList="code_func")
	})
@NamedQuery(name="lieux_find", query="from LieuxPersistant lieux "
		+ "order by lieux.libelle asc")
public class LieuxPersistant extends BasePersistant {
	@Column(length=120, nullable = false)
	private String libelle;
	
	@Column(length = 250)
	private String adresse_rue;
	@Column(length = 250)
	private String adresse_compl;
	@ManyToOne
	@JoinColumn(name = "ville_id", referencedColumnName = "id")
	private VillePersistant opc_ville;
	@ManyToOne
	@JoinColumn(name = "quartier_id", referencedColumnName = "id")
	private VilleQuartierPersistant opc_quartier;
	
    @Column(length = 25, scale = 20, precision = 25)
	private BigDecimal position_lat;
    @Column(length = 25, scale = 20, precision = 25)
	private BigDecimal position_lng;
    
	@Column(length = 20)
	private String telephone;
	@Column(length = 20)
	private String portable;
	@Column(length = 50)
	private String mail;
	@Column(length = 50)
	private String site;
	
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public BigDecimal getPosition_lat() {
		return position_lat;
	}
	public void setPosition_lat(BigDecimal position_lat) {
		this.position_lat = position_lat;
	}
	public BigDecimal getPosition_lng() {
		return position_lng;
	}
	public void setPosition_lng(BigDecimal position_lng) {
		this.position_lng = position_lng;
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
	public VilleQuartierPersistant getOpc_quartier() {
		return opc_quartier;
	}
	public void setOpc_quartier(VilleQuartierPersistant opc_quartier) {
		this.opc_quartier = opc_quartier;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
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
	
	public String getAdressFull() {
		String adresse = "";
		if (StringUtil.isNotEmpty(this.adresse_rue)) {
			adresse += this.adresse_rue;
		}
		if (StringUtil.isNotEmpty(this.adresse_compl)) {
			adresse += " - " + this.adresse_compl;
		}
		if (this.opc_quartier != null) {
			adresse += " - " + this.opc_quartier.getLibelle();
		}
		if (this.opc_ville != null) {
			adresse = adresse + " - " 
					+ StringUtil.getValueOrEmpty(this.opc_ville.getCode_postal()) + " " 
					+ StringUtil.getValueOrEmpty(this.opc_ville.getLibelle());
		}
		return adresse;
	}
}
