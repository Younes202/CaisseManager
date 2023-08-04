package appli.model.domaine.personnel.persistant;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import appli.model.domaine.administration.persistant.MessageDetailPersistant;
import appli.model.domaine.administration.persistant.ValTypeEnumPersistant;
import appli.model.domaine.stock.persistant.FamillePersistant;
import framework.model.beanContext.BasePersistant;
import framework.model.beanContext.ComptePersistant;
import framework.model.beanContext.VillePersistant;
import framework.model.common.util.StringUtil;
import framework.model.util.GsonExclude;

@Entity
@Table(name = "employe", indexes={
		@Index(name="IDX_EMPL_FUNC", columnList="code_func"),
		@Index(name="IDX_EMPL_NUM", columnList="numero"),
	})
public class EmployePersistant extends BasePersistant  {
	/**
	 * 
	 */
	
	
	private static final long serialVersionUID = 1L;
	
	//add genre and conjoint and periode de validation (cust) nombre de app postive and nombre negative 

	@Column(length = 20, nullable = false)
	private String numero;

	@Column(length = 50, nullable = false)
	private String nom;

	@Column(length = 50)
	private String prenom;
	
	@OneToMany
	@JoinColumn(name="employe_id", referencedColumnName="id", insertable=false, updatable=false)
	private List<EmployeAppreciationPersistant> list_appreciation;

	@Column(length = 20)
	private String cin;
	@Column
	private Boolean is_disable;
	@Column(length = 1)
	private String civilite;
	@Column(length = 120)
	private String situation_fam;
	@Column(length = 20)
	private String cnss;
	@Column(length = 20)
	private String cimr;
	@Column
	private Date date_entree;
	@Column
	private Date date_disable;
	@Column
	private Date date_sortie;
	@Column(length = 2)
	private Integer nbr_enfant;
	@Column(length = 2)
	private Integer periode_validation;
	@Column
	private Date date_naissance;
	@Column(length = 120)
	private String adresse_rue;
	@Column(length = 120)
	private String adresse_compl;
	@Column(length = 120)
	private String img;
	@Column(length = 1)
	private String mode_paie; //mode de paiement (par jour ou par heure)
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal tarif;
	@Column(length = 5, scale = 2, precision = 5)
	private  BigDecimal heureParJour;

	@Column(length = 1)
	private String situation_familiale;
	@Column(length = 3)
	private String mode_travail;
	
	@Column(length = 50)
	private String telephone;
	@Column(length = 50)
	private String mail;
	
	@Transient
	private Integer idx;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "compte_id", referencedColumnName="id")
	private ComptePersistant opc_compte;
	
	@GsonExclude
	@Transient
	private List<FamillePersistant> familleStr;// Arborescence de la famille 
	
	@ManyToOne
	@JoinColumn(name = "famille_id", referencedColumnName="id")
	private FamilleEmployePersistant opc_famille;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "poste_id", referencedColumnName = "id")
	private PostePersistant opc_poste;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type_sortie_enum", referencedColumnName = "id")
	private ValTypeEnumPersistant type_sortie_enum;
	@Column(length = 255)
	private String raison_sortie;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type_contrat_enum", referencedColumnName = "id")
	private ValTypeEnumPersistant type_contrat_enum;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ville_id", referencedColumnName = "id")
	private VillePersistant opc_ville;
		
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

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getCin() {
		return cin;
	}

	public void setCin(String cin) {
		this.cin = cin;
	}


	public Boolean getIs_disable() {
		return is_disable;
	}

	public void setIs_disable(Boolean is_disable) {
		this.is_disable = is_disable;
	}

	public String getCivilite() {
		return civilite;
	}

	public void setCivilite(String civilite) {
		this.civilite = civilite;
	}

	public String getCnss() {
		return cnss;
	}

	public void setCnss(String cnss) {
		this.cnss = cnss;
	}

	public String getCimr() {
		return cimr;
	}

	public void setCimr(String cimr) {
		this.cimr = cimr;
	}

	public Date getDate_entree() {
		return date_entree;
	}

	public void setDate_entree(Date date_entree) {
		this.date_entree = date_entree;
	}

	public Date getDate_sortie() {
		return date_sortie;
	}

	public void setDate_sortie(Date date_sortie) {
		this.date_sortie = date_sortie;
	}

	public Integer getNbr_enfant() {
		return nbr_enfant;
	}

	public void setNbr_enfant(Integer nbr_enfant) {
		this.nbr_enfant = nbr_enfant;
	}

	public Date getDate_naissance() {
		return date_naissance;
	}

	public void setDate_naissance(Date date_naissance) {
		this.date_naissance = date_naissance;
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

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public ValTypeEnumPersistant getType_sortie_enum() {
		return type_sortie_enum;
	}

	public void setType_sortie_enum(ValTypeEnumPersistant type_sortie_enum) {
		this.type_sortie_enum = type_sortie_enum;
	}

	public ValTypeEnumPersistant getType_contrat_enum() {
		return type_contrat_enum;
	}

	public void setType_contrat_enum(ValTypeEnumPersistant type_contrat_enum) {
		this.type_contrat_enum = type_contrat_enum;
	}

	public VillePersistant getOpc_ville() {
		return opc_ville;
	}

	public void setOpc_ville(VillePersistant opc_ville) {
		this.opc_ville = opc_ville;
	}

	public PostePersistant getOpc_poste() {
		return opc_poste;
	}

	public void setOpc_poste(PostePersistant opc_poste) {
		this.opc_poste = opc_poste;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getMode_paie() {
		return mode_paie;
	}

	public void setMode_paie(String mode_paie) {
		this.mode_paie = mode_paie;
	}

	public BigDecimal getTarif() {
		if(this.tarif == null && this.opc_poste != null) {
			return this.opc_poste.getTarif();
		}
		return tarif;
	}

	public void setTarif(BigDecimal tarif) {
		this.tarif = tarif;
	}

	public BigDecimal getHeureParJour() {
		return heureParJour;
	}

	public void setHeureParJour(BigDecimal heureParJour) {
		this.heureParJour = heureParJour;
	}

	public String getSituation_familiale() {
		return situation_familiale;
	}

	public void setSituation_familiale(String situation_familiale) {
		this.situation_familiale = situation_familiale;
	}

	public String getMode_travail() {
		return mode_travail;
	}

	public void setMode_travail(String mode_travail) {
		this.mode_travail = mode_travail;
	}

	public Integer getIdx() {
		return idx;
	}

	public void setIdx(Integer idx) {
		this.idx = idx;
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

//	public String getContrat() {
//		return (this.type_contrat_enum != null ? type_contrat_enum.getCode() : null);
//	}

	public Date getDate_disable() {
		return date_disable;
	}

	public String getRaison_sortie() {
		return raison_sortie;
	}

	public void setRaison_sortie(String raison_sortie) {
		this.raison_sortie = raison_sortie;
	}

	public void setDate_disable(Date date_disable) {
		this.date_disable = date_disable;
	}

	public ComptePersistant getOpc_compte() {
		return opc_compte;
	}

	public void setOpc_compte(ComptePersistant opc_compte) {
		this.opc_compte = opc_compte;
	}

	public String getNomPrenom() {
		return this.nom+(StringUtil.isNotEmpty(this.prenom) ? " "+this.prenom : "");
	}

	public FamilleEmployePersistant getOpc_famille() {
		return opc_famille;
	}

	public void setOpc_famille(FamilleEmployePersistant opc_famille) {
		this.opc_famille = opc_famille;
	}
	
	public List<FamillePersistant> getFamilleStr() {
		return familleStr;
	}

	public void setFamilleStr(List<FamillePersistant> familleStr) {
		this.familleStr = familleStr;
	}

	public String getSituation_fam() {
		return situation_fam;
	}

	public void setSituation_fam(String situation_fam) {
		this.situation_fam = situation_fam;
	}

	public Integer getPeriode_validation() {
		return periode_validation;
	}

	public void setPeriode_validation(Integer periode_validation) {
		this.periode_validation = periode_validation;
	}

	public List<EmployeAppreciationPersistant> getList_appreciation() {
		return list_appreciation;
	}

	public void setList_appreciation(List<EmployeAppreciationPersistant> list_appreciation) {
		this.list_appreciation = list_appreciation;
	}
}
