package appli.model.domaine.stock.persistant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import framework.model.util.GsonExclude;

@Entity
@Table(name = "article_gen")
public class ArticleGeneriquePersistant {
	@Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(length = 120)
	private String code_barre;
	@Column(length = 120)
	private String code_barre_gen;
	
	@GsonExclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "article_id", referencedColumnName = "id")
	private ArticlePersistant opc_article;
	
	@GsonExclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "generic_id", referencedColumnName = "id")
	private ArticlePersistant opc_article_gen;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ArticlePersistant getOpc_article() {
		return opc_article;
	}
	public void setOpc_article(ArticlePersistant opc_article) {
		this.opc_article = opc_article;
	}
	public ArticlePersistant getOpc_article_gen() {
		return opc_article_gen;
	}
	public void setOpc_article_gen(ArticlePersistant opc_article_gen) {
		this.opc_article_gen = opc_article_gen;
	}
	public String getCode_barre() {
		return code_barre;
	}
	public void setCode_barre(String code_barre) {
		this.code_barre = code_barre;
	}
	public String getCode_barre_gen() {
		return code_barre_gen;
	}
	public void setCode_barre_gen(String code_barre_gen) {
		this.code_barre_gen = code_barre_gen;
	}
	
}
