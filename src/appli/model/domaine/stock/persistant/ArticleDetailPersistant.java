package appli.model.domaine.stock.persistant;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import framework.model.beanContext.BasePersistant;


@Entity
@Table(name = "article_detail", indexes={
		@Index(name="IDX_ART_DET_FUNC", columnList="code_func")
	})
public class ArticleDetailPersistant extends BasePersistant {
	@Column(length = 15, scale = 6, precision = 13, nullable = false)
	private  BigDecimal quantite;
	@Column(length = 10)
	private String mode_destock;
	
/*********************** Liens ************************************/
	@ManyToOne
	@JoinColumn(name = "article_id", referencedColumnName="id")
	private ArticlePersistant opc_article;
	
	@ManyToOne
	@JoinColumn(name = "article_composant_id", nullable=false, referencedColumnName="id")
	private ArticlePersistant opc_article_composant;
	
	@Transient
	private int idxIhm;
	@Transient
	private Long article_composant_id;
	
	public BigDecimal getQuantite() {
		return quantite;
	}

	public void setQuantite(BigDecimal quantite) {
		this.quantite = quantite;
	}

	public ArticlePersistant getOpc_article() {
		return opc_article;
	}

	public void setOpc_article(ArticlePersistant opc_article) {
		this.opc_article = opc_article;
	}

	public ArticlePersistant getOpc_article_composant() {
		return opc_article_composant;
	}

	public void setOpc_article_composant(ArticlePersistant opc_article_composant) {
		this.opc_article_composant = opc_article_composant;
	}

	public int getIdxIhm() {
		return idxIhm;
	}

	public void setIdxIhm(int idxIhm) {
		this.idxIhm = idxIhm;
	}

	public String getMode_destock() {
		return mode_destock;
	}

	public void setMode_destock(String mode_destock) {
		this.mode_destock = mode_destock;
	}

	public Long getArticle_composant_id() {
		return article_composant_id;
	}

	public void setArticle_composant_id(Long article_composant_id) {
		this.article_composant_id = article_composant_id;
	}
}
