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
import framework.model.util.GsonExclude;


@Entity
@Table(name = "demande_transfert_article", indexes={
		@Index(name="IDX_DEM_TRANS_FUNC", columnList="code_func")
	})
public class DemandeTransfertArticlePersistant extends BasePersistant {
	@Column(length = 15, scale = 6, precision = 13, nullable = false)
	private  BigDecimal quantite;

	@ManyToOne
	@JoinColumn(name = "article_id", nullable=false, referencedColumnName="id")
	private ArticlePersistant opc_article;
	@GsonExclude
	@ManyToOne
	@JoinColumn(name = "demande_transfert_id", referencedColumnName="id")
	private DemandeTransfertPersistant opc_demande_transfert;

	@Transient
	private int idxIhm;
	@Transient
	private String code;

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

	public DemandeTransfertPersistant getOpc_demande_transfert() {
		return opc_demande_transfert;
	}

	public void setOpc_demande_transfert(DemandeTransfertPersistant opc_demande_transfert) {
		this.opc_demande_transfert = opc_demande_transfert;
	}

	public int getIdxIhm() {
		return idxIhm;
	}

	public void setIdxIhm(int idxIhm) {
		this.idxIhm = idxIhm;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
