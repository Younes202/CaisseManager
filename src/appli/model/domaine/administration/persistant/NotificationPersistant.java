package appli.model.domaine.administration.persistant;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import appli.model.domaine.stock.persistant.ArticlePersistant;
import framework.model.beanContext.EtablissementPersistant;

@Entity
@Table(name = "notification")
@NamedQueries({
	@NamedQuery(name="notification_adm_find", query="from NotificationPersistant notification "
			+ "where notification.type_notif like '%ADM_' "
			+ "order by notification.date_creation desc"),
	@NamedQuery(name="notification_ets_find", query="from NotificationPersistant notification "
			+ "where notification.type_notif like '%ETS_' "
			+ "order by notification.date_creation desc")
})
public class NotificationPersistant implements Serializable {
	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 60, nullable = false)
	private String code;
	
	//REJ : Rejeté, ACC: Accepté, AFF : Affiché==>PHARMA
	@Column(length = 8, nullable = false)
	private String etat;
	
	@Column(length = 255)
	private String sujet;
	@Column(length = 255)
	private String user_read;// Utilisateur ayant lu
	@Column(length = 255)
	private String user_dest;// Utilisateur destination, vide si tous
	
	@Column
	@Lob
	private String message;
	
	//ADM_CREATE, ADM_UPDATE, ADM_REJECT, ADM_DELETE, ADM_NOTIF, ETS_CREATE, ETS_UPDATE==> PHARMA
	//NEWS, ALERT
	@Column(length = 10, nullable = false)
	private String type_notif; 
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date date_creation;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date date_reception;
	
	@ManyToOne
	@JoinColumn(name = "article_id", referencedColumnName="id")
	private ArticlePersistant opc_article;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "etablissement_id", referencedColumnName = "id")
	private EtablissementPersistant opc_etablissement;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getEtat() {
		return etat;
	}
	public void setEtat(String etat) {
		this.etat = etat;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getType_notif() {
		return type_notif;
	}
	public Date getDate_creation() {
		return date_creation;
	}
	public void setDate_creation(Date date_creation) {
		this.date_creation = date_creation;
	}
	public ArticlePersistant getOpc_article() {
		return opc_article;
	}
	public void setOpc_article(ArticlePersistant opc_article) {
		this.opc_article = opc_article;
	}
	public Date getDate_reception() {
		return date_reception;
	}
	public void setDate_reception(Date date_reception) {
		this.date_reception = date_reception;
	}
	public String getSujet() {
		return sujet;
	}
	public void setSujet(String sujet) {
		this.sujet = sujet;
	}
	public void setType_notif(String type_notif) {
		this.type_notif = type_notif;
	}
	public String getUser_read() {
		return user_read;
	}
	public void setUser_read(String user_read) {
		this.user_read = user_read;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public EtablissementPersistant getOpc_etablissement() {
		return opc_etablissement;
	}
	public void setOpc_etablissement(EtablissementPersistant opc_etablissement) {
		this.opc_etablissement = opc_etablissement;
	}
	public String getUser_dest() {
		return user_dest;
	}
	public void setUser_dest(String user_dest) {
		this.user_dest = user_dest;
	}
}
