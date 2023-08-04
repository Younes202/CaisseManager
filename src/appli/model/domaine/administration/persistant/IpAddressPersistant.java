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
@Table(name = "ip_address")

public class IpAddressPersistant implements Serializable {
	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 45, nullable = false)
	private String ip;
	
	@Column(nullable = false, length = 255)
	private String ets_token;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date date_creation;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getEts_token() {
		return ets_token;
	}

	public void setEts_token(String ets_token) {
		this.ets_token = ets_token;
	}

	public Date getDate_creation() {
		return date_creation;
	}

	public void setDate_creation(Date date_creation) {
		this.date_creation = date_creation;
	}
}