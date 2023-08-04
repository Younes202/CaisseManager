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
@Table(name = "preparation_article", indexes={
		@Index(name="IDX_PREP_ART_FUNC", columnList="code_func")
	})
public class PreparationArticlePersistant extends BasePersistant  {
	@Column(length = 15, scale = 6, precision = 13, nullable = false)
	private  BigDecimal quantite;
	
/*********************** Liens ************************************/
	
	@ManyToOne
	@JoinColumn(name = "preparation_id", referencedColumnName="id")
	private PreparationPersistant opc_preparation;
	
	@ManyToOne
	@JoinColumn(name = "article_id", nullable=false, referencedColumnName="id")
	private ArticlePersistant opc_article;
	
	@Transient
	private int idxIhm;

	public BigDecimal getQuantite() {
		return quantite;
	}

	public void setQuantite(BigDecimal quantite) {
		this.quantite = quantite;
	}

	public PreparationPersistant getOpc_preparation() {
		return opc_preparation;
	}

	public void setOpc_preparation(PreparationPersistant opc_preparation) {
		this.opc_preparation = opc_preparation;
	}

	public ArticlePersistant getOpc_article() {
		return opc_article;
	}

	public void setOpc_article(ArticlePersistant opc_article) {
		this.opc_article = opc_article;
	}

	public int getIdxIhm() {
		return idxIhm;
	}

	public void setIdxIhm(int idxIhm) {
		this.idxIhm = idxIhm;
	}
}
