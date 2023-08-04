package framework.model.beanContext;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;



@Entity
@Table(name = "compte_infos", indexes={
		@Index(name="IDX_CPT_INF_FUNC", columnList="code_func")
	})
public class CompteInfosPersistant extends BasePersistant {
	@Column
	private Boolean is_opr_courante;

	@Column(length = 10)
	private BigDecimal taux_recuperation;
	
	/*********************** Liens ************************************/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "compte_id", nullable=false, referencedColumnName="id")
	public ComptePersistant opc_compte;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "exercice_id", referencedColumnName="id", nullable=false)
	private ExercicePersistant opc_exercice;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cle_id", referencedColumnName="id")
	private ClePersistant opc_cle;
	
	public Boolean getIs_opr_courante() {
		return is_opr_courante;
	}

	public void setIs_opr_courante(Boolean is_opr_courante) {
		this.is_opr_courante = is_opr_courante;
	}

	public ComptePersistant getOpc_compte() {
		return opc_compte;
	}

	public void setOpc_compte(ComptePersistant opc_compte) {
		this.opc_compte = opc_compte;
	}

	public ExercicePersistant getOpc_exercice() {
		return opc_exercice;
	}

	public void setOpc_exercice(ExercicePersistant opc_exercice) {
		this.opc_exercice = opc_exercice;
	}

	public BigDecimal getTaux_recuperation() {
		return taux_recuperation;
	}

	public void setTaux_recuperation(BigDecimal taux_recuperation) {
		this.taux_recuperation = taux_recuperation;
	}

	public ClePersistant getOpc_cle() {
		return opc_cle;
	}

	public void setOpc_cle(ClePersistant opc_cle) {
		this.opc_cle = opc_cle;
	}
}
