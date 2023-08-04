package framework.model.beanContext;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import framework.model.util.GsonExclude;

@Entity
@Table(name = "pays")
public class PaysPersistant {
	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 120, nullable = false)
	private String libelle;
	@Column(length = 120)
	private String libelle2;
    @Column(length = 10, nullable = false)
	private String code;
    @Column(length = 10)
	private String code2;
    @Column(length = 10)
	private String code_num;
    @Column(length = 20) 
	private String devise;
        
    @Column(length = 20)
	private String devise_symbole;
        
    @Column(length = 20)
	private String devise_symbole_html;
        
    @GsonExclude
    @OrderBy(value="libelle")
	@OneToMany
	@JoinColumn(name="pays_id", referencedColumnName="id", insertable=false, updatable=false)
	private List<VillePersistant> list_ville;
        
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDevise() {
        return devise;
    }

    public void setDevise(String devise) {
        this.devise = devise;
    }

    public String getDevise_symbole() {
        return devise_symbole;
    }

    public void setDevise_symbole(String devise_symbole) {
        this.devise_symbole = devise_symbole;
    }

    public String getDevise_symbole_html() {
        return devise_symbole_html;
    }

    public void setDevise_symbole_html(String devise_symbole_html) {
        this.devise_symbole_html = devise_symbole_html;
    }

	public String getLibelle2() {
		return libelle2;
	}

	public void setLibelle2(String libelle2) {
		this.libelle2 = libelle2;
	}

	public String getCode2() {
		return code2;
	}

	public void setCode2(String code2) {
		this.code2 = code2;
	}

	public String getCode_num() {
		return code_num;
	}

	public void setCode_num(String code_num) {
		this.code_num = code_num;
	}

	public List<VillePersistant> getList_ville() {
		return list_ville;
	}

	public void setList_ville(List<VillePersistant> list_ville) {
		this.list_ville = list_ville;
	}
}
