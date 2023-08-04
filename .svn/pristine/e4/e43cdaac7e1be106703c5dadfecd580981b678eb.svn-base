package appli.model.domaine.administration.persistant;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import framework.model.beanContext.BasePersistant;

@Entity
@Table(name = "mail_queue", indexes={
		@Index(name="IDX_MAIL_FUNC", columnList="code_func")
	})
public class MailQueuePersistant extends BasePersistant implements Serializable {
	@Column(length = 100, nullable = false)
	private String sujet;
	
	@Column
	@Lob
	private String message;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date date_envoi;
	
	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date_mail;//--> Date à laquelle le mail doit partir

	@Column(length = 50)
	private String pieces_path;
	
	@Column(length = 255, nullable = false)
	private String destinataires;//Mails destinataires spéparés par des ";"

	@Column(length = 100, nullable = false)
	private String expediteur_nom;
	@Column(length = 100, nullable = false)
	private String expediteur_mail;
	@Column(length = 200)
	private String mail_signature;
	@Column(length=20)
	private Long origine_id;
	@Column(length = 10, nullable = false)
	private String source;
	@Column(length=3)
	private Integer nbr_erreur = 0;// Tentaives d'envoi
	
	public String getSujet() {
		return sujet;
	}
	public void setSujet(String sujet) {
		this.sujet = sujet;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getDate_envoi() {
		return date_envoi;
	}
	public void setDate_envoi(Date date_envoi) {
		this.date_envoi = date_envoi;
	}
	public String getPieces_path() {
		return pieces_path;
	}
	public void setPieces_path(String pieces_path) {
		this.pieces_path = pieces_path;
	}

	public Date getDate_mail() {
		return date_mail;
	}

	public void setDate_mail(Date date_mail) {
		this.date_mail = date_mail;
	}
	public String getDestinataires() {
		return destinataires;
	}
	public void setDestinataires(String destinataires) {
		this.destinataires = destinataires;
	}
	public String getExpediteur_nom() {
		return expediteur_nom;
	}
	public void setExpediteur_nom(String expediteur_nom) {
		this.expediteur_nom = expediteur_nom;
	}
	public String getExpediteur_mail() {
		return expediteur_mail;
	}
	public void setExpediteur_mail(String expediteur_mail) {
		this.expediteur_mail = expediteur_mail;
	}
	public Long getOrigine_id() {
		return origine_id;
	}
	public void setOrigine_id(Long origine_id) {
		this.origine_id = origine_id;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getMail_signature() {
		return mail_signature;
	}
	public void setMail_signature(String mail_signature) {
		this.mail_signature = mail_signature;
	}
	public Integer getNbr_erreur() {
		return (nbr_erreur!=null?nbr_erreur:0);
	}
	public void setNbr_erreur(Integer nbr_erreur) {
		this.nbr_erreur = nbr_erreur;
	}
}
