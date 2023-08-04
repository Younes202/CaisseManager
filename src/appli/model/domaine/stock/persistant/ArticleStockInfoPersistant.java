package appli.model.domaine.stock.persistant;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

import framework.model.beanContext.BasePersistant;
import framework.model.common.util.BigDecimalUtil;

@Entity
@Table(name = "article_stock_info", indexes={
		@Index(name="IDX_ART_INF_FUNC", columnList="code_func")
	})
@NamedQueries({
	@NamedQuery(name="article_view_find", query="from ArticleStockInfoPersistant etatArticle "
			+ "where (etatArticle.opc_emplacement.origine_id is null or etatArticle.opc_emplacement.origine_id=0) "
			
		+ "and (etatArticle.opc_emplacement.familles_ex_cmd is null or etatArticle.opc_emplacement.familles_ex_cmd not like CONCAT('%;',etatArticle.opc_article.opc_famille_stock.id,';%')) "
		+ "and (etatArticle.opc_emplacement.articles_ex_cmd is null or etatArticle.opc_emplacement.articles_ex_cmd not like CONCAT('%;',etatArticle.opc_article.id,';%')) "
		+ "and (etatArticle.opc_emplacement.familles_cmd is null or etatArticle.opc_emplacement.familles_cmd like CONCAT('%;',etatArticle.opc_article.opc_famille_stock.id,';%')) "
		+ "and (etatArticle.opc_emplacement.articles_cmd is null or etatArticle.opc_emplacement.articles_cmd like CONCAT('%;',etatArticle.opc_article.id,';%')) "
		
			//+ "and (etatArticle.qte_entree>0 or etatArticle.qte_sortie>0) "
			+ "and (etatArticle.opc_article.is_disable is null or etatArticle.opc_article.is_disable=0) "
			+ "order by etatArticle.opc_article.opc_famille_stock.b_left, "
			+ "etatArticle.opc_article.code"),
	
	@NamedQuery(name="article_alert_find", query="from ArticleStockInfoPersistant articleStockInfo "
			+ "where (articleStockInfo.opc_article.is_disable is null or articleStockInfo.opc_article.is_disable=0) "
			+ "and (COALESCE(articleStockInfo.qte_reel, 0) < COALESCE(articleStockInfo.opc_emplacement_seuil.qte_seuil, 0)) " 
			+ " order by articleStockInfo.opc_emplacement.titre, articleStockInfo.opc_article.opc_famille_stock.b_left, articleStockInfo.opc_article.code, articleStockInfo.opc_article.libelle"),
})
public class ArticleStockInfoPersistant extends BasePersistant {
	@Column(length = 15, scale = 6, precision = 15)
    private BigDecimal qte_entree;
	@Column(length = 15, scale = 6, precision = 15)
    private BigDecimal qte_sortie;
	@Column(length = 15, scale = 6, precision = 15)
    private BigDecimal qte_reel;
	@ManyToOne
	@JoinColumn(name = "article_id", referencedColumnName="id")
	private ArticlePersistant opc_article;
	@ManyToOne
	@JoinColumn(name = "emplacement_id", referencedColumnName="id")
	private EmplacementPersistant opc_emplacement;
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name = "article_id", referencedColumnName="composant_id", insertable=false, updatable=false),
		@JoinColumn(name = "emplacement_id", referencedColumnName="emplacement_id", insertable=false, updatable=false),
	})
	@ForeignKey(name = "none")
	private EmplacementSeuilPersistant opc_emplacement_seuil;

	public ArticleStockInfoPersistant() {
		
	}
	public ArticleStockInfoPersistant(Long emplId, Long articleId, BigDecimal qte, String sens) {
			if(sens.equals("E")) {
				this.qte_entree = qte;	
			} else {
				this.qte_sortie = qte;	
			}
			
			this.opc_article = new ArticlePersistant();
			this.opc_article.setId(articleId);
			this.opc_emplacement = new EmplacementPersistant();
			this.opc_emplacement.setId(emplId);
	}
	public BigDecimal getQte_entree() {
		return qte_entree;
	}
	public void setQte_entree(BigDecimal qte_entree) {
		this.qte_entree = qte_entree;
	}
	public BigDecimal getQte_sortie() {
		return qte_sortie;
	}
	public void setQte_sortie(BigDecimal qte_sortie) {
		this.qte_sortie = qte_sortie;
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
	
	public BigDecimal getQte_restante() {
		return BigDecimalUtil.substract(qte_entree, qte_sortie);
	}
	public EmplacementSeuilPersistant getOpc_emplacement_seuil() {
		return opc_emplacement_seuil;
	}
	public void setOpc_emplacement_seuil(EmplacementSeuilPersistant opc_emplacement_seuil) {
		this.opc_emplacement_seuil = opc_emplacement_seuil;
	}
	public BigDecimal getQte_reel() {
		return qte_reel;
	}
	public void setQte_reel(BigDecimal qte_reel) {
		this.qte_reel = qte_reel;
	}
}
