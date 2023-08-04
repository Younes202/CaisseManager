package appli.model.domaine.stock.persistant.centrale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import framework.model.beanContext.BasePersistant;

@Entity
@Table(name = "centrale_ets")
public class CentraleEtsPersistant extends BasePersistant {
	@Column(length = 150, nullable = false) 
	private String nom;
	@Column(length = 150, nullable = false) 
	private String code_auth;
	@Column
	private Boolean is_disable;
	
	@Column(length = 200, nullable = false) 
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getCode_auth() {
		return code_auth;
	}

	public void setCode_auth(String code_auth) {
		this.code_auth = code_auth;
	}

	public Boolean getIs_disable() {
		return is_disable;
	}

	public void setIs_disable(Boolean is_disable) {
		this.is_disable = is_disable;
	}
}
