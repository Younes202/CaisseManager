package appli.model.domaine.caisse.persistant;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.persistant.EmplacementPersistant;
import framework.model.beanContext.BasePersistant;
import framework.model.common.util.BigDecimalUtil;

@Entity
@Table(name = "article_stock_caisse_info", indexes={
		@Index(name="IDX_ART_CAISINF_FUNC", columnList="code_func")
	})
public class ArticleStockCaisseInfoPersistant extends BasePersistant {
	@Column(length = 15, scale = 6, precision = 15)
    private BigDecimal qte_reel;
	@Column(length = 200)
	private String article_lib;
	
	@ManyToOne
	@JoinColumn(name = "article_id", referencedColumnName="id")
	private ArticlePersistant opc_article;
	@ManyToOne
	@JoinColumn(name = "emplacement_id", referencedColumnName="id")
	private EmplacementPersistant opc_emplacement;

	public ArticleStockCaisseInfoPersistant() {
		
	}
	public ArticlePersistant getOpc_article() {
		return opc_article;
	}
	public void setOpc_article(ArticlePersistant opc_article) {
		this.opc_article = opc_article;
	}
	public EmplacementPersistant getOpc_emplacement() {
		return opc_emplacement;
	}
	public void setOpc_emplacement(EmplacementPersistant opc_emplacement) {
		this.opc_emplacement = opc_emplacement;
	}
	public BigDecimal getQte_reel() {
		return qte_reel;
	}
	public void setQte_reel(BigDecimal qte_reel) {
		this.qte_reel = qte_reel;
	}
	public String getArticle_lib() {
		return article_lib;
	}
	public void setArticle_lib(String article_lib) {
		this.article_lib = article_lib;
	}
}
