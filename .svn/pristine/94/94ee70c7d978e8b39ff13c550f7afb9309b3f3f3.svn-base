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
@Table(name = "preparation_transfo_article", indexes={
		@Index(name="IDX_PREP_TRANS_ART_FUNC", columnList="code_func")
	})
public class PreparationTransfoArticlePersistant extends BasePersistant {

	@Column(length = 15, scale = 6, precision = 13, nullable = false)
	private  BigDecimal quantite;
	
/*********************** Liens ************************************/
	
	@ManyToOne
	@JoinColumn(name = "transfo_id", referencedColumnName="id")
	private PreparationTransfoPersistant opc_transfo;
	
	@ManyToOne
	@JoinColumn(name = "composant_id", nullable=false, referencedColumnName="id")
	private ArticlePersistant opc_composant;
	
	@Transient
	private int idxIhm;

	public BigDecimal getQuantite() {
		return quantite;
	}

	public void setQuantite(BigDecimal quantite) {
		this.quantite = quantite;
	}

	public PreparationTransfoPersistant getOpc_transfo() {
		return opc_transfo;
	}

	public void setOpc_transfo(PreparationTransfoPersistant opc_transfo) {
		this.opc_transfo = opc_transfo;
	}

	public ArticlePersistant getOpc_composant() {
		return opc_composant;
	}

	public void setOpc_composant(ArticlePersistant opc_composant) {
		this.opc_composant = opc_composant;
	}

	public int getIdxIhm() {
		return idxIhm;
	}

	public void setIdxIhm(int idxIhm) {
		this.idxIhm = idxIhm;
	}
}
