package appli.model.domaine.caisse.persistant;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import framework.model.beanContext.BasePersistant;

@Entity
@Table(name = "notification_queue")
@NamedQuery(name="notification_find", query="from NotificationQueuPersistant notificationQueu where notificationQueu.etablissement_id='{etablissement_id}' order by notificationQueu.date_envoie asc")
public class NotificationQueuPersistant extends BasePersistant implements Serializable {
	@Column(length=120)
	private String employe;
	@Column(length=120)
	private String client;
	@Column(length=20)
	private Long employe_id;
	@Column(length=20)
	private Long client_id;
	@Column(length=20)
	private Long commande_id;
	@Column
	private Boolean is_toRemove;
	@Column(length=3)
	private Integer nbr_erreur = 0;// Tentaives d'envoi
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date date_envoie;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date date_notification;

	@Column
	@Lob
	private String device_token;// Token firebase 	
	
	@Column(length=50)
	private String groupe; 	

	@Column(length=200)
	private String title;	
	
	@Column
	@Lob
	private String message;	
	@Column
	@Lob
	private String params;	
	
	@Column(length = 1)
	private Integer cpt;	
	@Column(length = 120)
	private String statut; // ERREUR, SUCCES

	public Date getDate_envoie() {
		return date_envoie;
	}

	public void setDate_envoie(Date date_envoie) {
		this.date_envoie = date_envoie;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getDate_notification() {
		return date_notification;
	}

	public void setDate_notification(Date date_notification) {
		this.date_notification = date_notification;
	}

	public String getGroupe() {
		return groupe;
	}

	public void setGroupe(String groupe) {
		this.groupe = groupe;
	}

	public String getDevice_token() {
		return device_token;
	}

	public void setDevice_token(String device_token) {
		this.device_token = device_token;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getCpt() {
		return cpt;
	}

	public void setCpt(Integer cpt) {
		this.cpt = cpt;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}
	
	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public Long getClient_id() {
		return client_id;
	}

	public void setClient_id(Long client_id) {
		this.client_id = client_id;
	}

	public String getEmploye() {
		return employe;
	}

	public void setEmploye(String employe) {
		this.employe = employe;
	}

	public Long getEmploye_id() {
		return employe_id;
	}

	public void setEmploye_id(Long employe_id) {
		this.employe_id = employe_id;
	}

	public Long getCommande_id() {
		return commande_id;
	}

	public void setCommande_id(Long commande_id) {
		this.commande_id = commande_id;
	}

	public Boolean getIs_toRemove() {
		return is_toRemove;
	}

	public void setIs_toRemove(Boolean is_toRemove) {
		this.is_toRemove = is_toRemove;
	}

	public Integer getNbr_erreur() {
		return nbr_erreur;
	}

	public void setNbr_erreur(Integer nbr_erreur) {
		this.nbr_erreur = nbr_erreur;
	}
}
