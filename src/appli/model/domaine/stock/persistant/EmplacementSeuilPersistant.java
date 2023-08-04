package appli.model.domaine.stock.persistant;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import framework.model.beanContext.BasePersistant;

@Entity
@Table(name = "emplacement_seuil", indexes={
		@Index(name="IDX_EMPL_SEUIL_FUNC", columnList="code_func")
	})
public class EmplacementSeuilPersistant extends BasePersistant {
	@Column(length = 15, scale = 6, precision = 15, nullable=false)
    private BigDecimal qte_seuil;
	@ManyToOne
	@JoinColumn(name = "composant_id", referencedColumnName="id", nullable=false)
	private ArticlePersistant opc_composant;
	@ManyToOne
	@JoinColumn(name = "emplacement_id", referencedColumnName="id", nullable=false)
	private EmplacementPersistant opc_emplacement;
	
	public BigDecimal getQte_seuil() {
		return qte_seuil;
	}
	public void setQte_seuil(BigDecimal qte_seuil) {
		this.qte_seuil = qte_seuil;
	}
	public ArticlePersistant getOpc_composant() {
		return opc_composant;
	}
	public void setOpc_composant(ArticlePersistant opc_composant) {
		this.opc_composant = opc_composant;
	}
	public EmplacementPersistant getOpc_emplacement() {
		return opc_emplacement;
	}
	public void setOpc_emplacement(EmplacementPersistant opc_emplacement) {
		this.opc_emplacement = opc_emplacement;
	}
}