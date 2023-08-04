package framework.model.beanContext;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import framework.model.common.util.StringUtil;
import framework.model.util.GsonExclude;

@SuppressWarnings("serial")
@Entity
@Table(name = "societe")

@NamedQuery(name="societe_find", query="from SocietePersistant societe order by societe.raison_sociale")
public class SocietePersistant implements Serializable {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 120)
	private String code_func;
    
    @Column(length = 120, nullable = false)
    private String raison_sociale;
    
	@Column(length = 120)
	private String adresse_rue;
	@Column(length = 120)
	private String adresse_compl;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ville_id", referencedColumnName = "id")
	private VillePersistant opc_ville;
    @Column
    private Boolean is_disable;
    @Column(length = 20)
    private String telephone1;
    @Column(length = 20)
    private String telephone2;
    
    @Column(length = 50)
    private String mail;
    @Column(length = 80)
    private String site;
    
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date date_creation;
    
    @Column(length = 20)
    private String numero_rcs;
    
    @Column(length = 20)
    private String numero_ice;
    
    @Column(length = 20)
    private String numero_tva;
    
    @Column(length = 50)
    private String identifiant_fiscal;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "compte_esp_id", referencedColumnName = "id")
	private CompteBancairePersistant opc_compte_esp;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "compte_autre_id", referencedColumnName = "id")
	private CompteBancairePersistant opc_compte_autre;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "abonne_id", referencedColumnName = "id")
	private AbonnePersistant opc_abonne;    
    @GsonExclude
	@OrderBy("id")
	@OneToMany
	@JoinColumn(name = "societe_id", referencedColumnName = "id", updatable=false, insertable=false)
	private List<EtablissementPersistant> list_etablissement;

	@Transient
	private String sync_key;
	@Transient
	private String sync_opr_id;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRaison_sociale() {
		return raison_sociale;
	}

	public void setRaison_sociale(String raison_sociale) {
		this.raison_sociale = raison_sociale;
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

	public String getTelephone1() {
		return telephone1;
	}

	public void setTelephone1(String telephone1) {
		this.telephone1 = telephone1;
	}

	public String getTelephone2() {
		return telephone2;
	}

	public void setTelephone2(String telephone2) {
		this.telephone2 = telephone2;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public Date getDate_creation() {
		return date_creation;
	}

	public void setDate_creation(Date date_creation) {
		this.date_creation = date_creation;
	}

	public String getNumero_rcs() {
		return numero_rcs;
	}

	public void setNumero_rcs(String numero_rcs) {
		this.numero_rcs = numero_rcs;
	}

	public String getNumero_ice() {
		return numero_ice;
	}

	public void setNumero_ice(String numero_ice) {
		this.numero_ice = numero_ice;
	}

	public String getNumero_tva() {
		return numero_tva;
	}

	public void setNumero_tva(String numero_tva) {
		this.numero_tva = numero_tva;
	}

	public List<EtablissementPersistant> getList_etablissement() {
		return list_etablissement;
	}

	public void setList_etablissement(List<EtablissementPersistant> list_etablissement) {
		this.list_etablissement = list_etablissement;
	}

	public String getAdresse() {
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

	public String getCode_func() {
		return code_func;
	}

	public void setCode_func(String code_func) {
		this.code_func = code_func;
	}

	public String getIdentifiant_fiscal() {
		return identifiant_fiscal;
	}

	public void setIdentifiant_fiscal(String identifiant_fiscal) {
		this.identifiant_fiscal = identifiant_fiscal;
	}

	public Boolean getIs_disable() {
		return is_disable;
	}

	public void setIs_disable(Boolean is_disable) {
		this.is_disable = is_disable;
	}

	public String getSync_key() {
		return sync_key;
	}

	public void setSync_key(String sync_key) {
		this.sync_key = sync_key;
	}

	public String getSync_opr_id() {
		return sync_opr_id;
	}

	public void setSync_opr_id(String sync_opr_id) {
		this.sync_opr_id = sync_opr_id;
	}

	public CompteBancairePersistant getOpc_compte_esp() {
		return opc_compte_esp;
	}

	public void setOpc_compte_esp(CompteBancairePersistant opc_compte_esp) {
		this.opc_compte_esp = opc_compte_esp;
	}

	public CompteBancairePersistant getOpc_compte_autre() {
		return opc_compte_autre;
	}

	public void setOpc_compte_autre(CompteBancairePersistant opc_compte_autre) {
		this.opc_compte_autre = opc_compte_autre;
	}

	public AbonnePersistant getOpc_abonne() {
		return opc_abonne;
	}

	public void setOpc_abonne(AbonnePersistant opc_abonne) {
		this.opc_abonne = opc_abonne;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}
}
