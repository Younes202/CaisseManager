package appli.model.domaine.stock.persistant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import framework.model.beanContext.BasePersistant;

@Entity
@Table(name = "emplacement", indexes={
		@Index(name="IDX_EMPL_FUNC", columnList="code_func")
	})
@NamedQuery(name="emplacement_find", query="from EmplacementPersistant emplacement" +
		" where" + 
		" emplacement.titre like '[%emplacement.titre%]' " + 
		" order by emplacement.titre")
public class EmplacementPersistant  extends BasePersistant {
	@Column(length = 80, nullable = false)
	private String titre;
	@Column(length = 255)
	private String description;
	@Lob
	@Column
	private String articles_cmd;
	@Lob
	@Column
	private String familles_cmd;
	@Lob
	@Column
	private String articles_ex_cmd;
	@Lob
	@Column
	private String familles_ex_cmd;
	@Column
    private Boolean is_desactive;
	@Column
    private Boolean is_externe;
	
	//-------------------------- Synchro centrale --------------------------//
	@Column(length = 80)
	private String origine_auth;
	@Column
	private Long origine_id;
	//-----------------------------------------------------------------------
	
	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIs_desactive() {
		return is_desactive;
	}

	public void setIs_desactive(Boolean is_desactive) {
		this.is_desactive = is_desactive;
	}

	public String getArticles_cmd() {
		return articles_cmd;
	}

	public void setArticles_cmd(String articles_cmd) {
		this.articles_cmd = articles_cmd;
	}

	public String getFamilles_cmd() {
		return familles_cmd;
	}

	public void setFamilles_cmd(String familles_cmd) {
		this.familles_cmd = familles_cmd;
	}

	public String getArticles_ex_cmd() {
		return articles_ex_cmd;
	}

	public void setArticles_ex_cmd(String articles_ex_cmd) {
		this.articles_ex_cmd = articles_ex_cmd;
	}

	public String getFamilles_ex_cmd() {
		return familles_ex_cmd;
	}

	public void setFamilles_ex_cmd(String familles_ex_cmd) {
		this.familles_ex_cmd = familles_ex_cmd;
	}

	public Long getOrigine_id() {
		return origine_id;
	}

	public void setOrigine_id(Long origine_id) {
		this.origine_id = origine_id;
	}

	public String getOrigine_auth() {
		return origine_auth;
	}

	public void setOrigine_auth(String origine_auth) {
		this.origine_auth = origine_auth;
	}

	public Boolean getIs_externe() {
		return is_externe;
	}

	public void setIs_externe(Boolean is_externe) {
		this.is_externe = is_externe;
	}
}
