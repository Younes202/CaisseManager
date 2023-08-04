package appli.model.domaine.stock.persistant;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import framework.model.beanContext.BasePersistant;

@Entity
@Table(name = "demande_transfert", indexes={
		@Index(name="IDX_DEM_TRANS_FUNC", columnList="code_func")
	})
@NamedQuery(name="demandeTransfert_find", query="from DemandeTransfertPersistant demandeTransfert" +
		" order by demandeTransfert.date_souhaitee desc, demandeTransfert.date_creation desc")
public class DemandeTransfertPersistant extends BasePersistant {
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date date_souhaitee;
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date date_emission;
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date date_transfert;
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date date_reception;
	
	@Column(length = 80, nullable = false)
	private String login;
	@Column(length = 120)
	private String nom;
	
	@Column(length = 20)
	private String statut;// ENRENGISTREE, TRAITEE, ANNULEE
	@Column(length = 20)
	private String statut_sync;// OK, KO
	
	//----------------------- CHAMPS INSTANCE CENTRALE ---------------------------------//
	@Column(length = 20)
	private Long origine_id;// Id source de la demande
	@Column(length = 120)
	private String origine_auth;// Code auth ets source de la demande
	//-----------------------------------------------------------------------------------
	
	@OrderBy("id")
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "demande_transfert_id", referencedColumnName = "id")
	List<DemandeTransfertArticlePersistant> list_article;

	public Date getDate_souhaitee() {
		return date_souhaitee;
	}

	public void setDate_souhaitee(Date date_souhaitee) {
		this.date_souhaitee = date_souhaitee;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public List<DemandeTransfertArticlePersistant> getList_article() {
		return list_article;
	}

	public void setList_article(List<DemandeTransfertArticlePersistant> list_article) {
		this.list_article = list_article;
	}

	public Date getDate_transfert() {
		return date_transfert;
	}

	public void setDate_transfert(Date date_transfert) {
		this.date_transfert = date_transfert;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public Date getDate_emission() {
		return date_emission;
	}

	public void setDate_emission(Date date_emission) {
		this.date_emission = date_emission;
	}

	public Long getOrigine_id() {
		return origine_id;
	}

	public void setOrigine_id(Long origine_id) {
		this.origine_id = origine_id;
	}

	public Date getDate_reception() {
		return date_reception;
	}

	public void setDate_reception(Date date_reception) {
		this.date_reception = date_reception;
	}

	public String getOrigine_auth() {
		return origine_auth;
	}

	public void setOrigine_auth(String origine_auth) {
		this.origine_auth = origine_auth;
	}

	public String getStatut_sync() {
		return statut_sync;
	}

	public void setStatut_sync(String statut_sync) {
		this.statut_sync = statut_sync;
	}
}
