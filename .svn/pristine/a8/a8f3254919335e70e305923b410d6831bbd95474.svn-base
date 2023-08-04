package appli.model.domaine.stock.persistant;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import framework.model.beanContext.BasePersistant;
import framework.model.util.GsonExclude;


@Entity
@Table(name = "lot_article", indexes={
		@Index(name="IDX_ART_LOT_FUNC", columnList="code_func")
	})
@NamedQuery(name="article_peremption_find", query="from LotArticlePersistant lotArticle where lotArticle.date_peremption<='[dateRef]' "
		+ "order by lotArticle.opc_emplacement.id, lotArticle.opc_article.opc_famille_stock.b_left, lotArticle.date_peremption asc")
public class LotArticlePersistant extends BasePersistant {

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date date_peremption;
	
	@Column(length = 15, scale = 6, precision = 15, nullable = false)
	private BigDecimal quantite;
	
	@ManyToOne
	@JoinColumn(name = "article_id", referencedColumnName="id", nullable = false)
	private ArticlePersistant opc_article;
	
	@ManyToOne
	@JoinColumn(name = "mvm_art_id", referencedColumnName="id", nullable = false)
	private MouvementArticlePersistant opc_mvm_article;
	
	@GsonExclude
	@ManyToOne
	@JoinColumn(name = "mvm_id", referencedColumnName="id", nullable = false)
	private MouvementPersistant opc_mvm;
	@ManyToOne
	@JoinColumn(name = "emplacement_id", referencedColumnName="id", nullable = false)
	private EmplacementPersistant opc_emplacement;
	
	public Date getDate_peremption() {
		return date_peremption;
	}
	public void setDate_peremption(Date date_peremption) {
		this.date_peremption = date_peremption;
	}
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
	public MouvementArticlePersistant getOpc_mvm_article() {
		return opc_mvm_article;
	}
	public void setOpc_mvm_article(MouvementArticlePersistant opc_mvm_article) {
		this.opc_mvm_article = opc_mvm_article;
	}
	public EmplacementPersistant getOpc_emplacement() {
		return opc_emplacement;
	}
	public void setOpc_emplacement(EmplacementPersistant opc_emplacement) {
		this.opc_emplacement = opc_emplacement;
	}
	public MouvementPersistant getOpc_mvm() {
		return opc_mvm;
	}
	public void setOpc_mvm(MouvementPersistant opc_mvm) {
		this.opc_mvm = opc_mvm;
	}
}
