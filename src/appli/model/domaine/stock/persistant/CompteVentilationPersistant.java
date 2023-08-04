package appli.model.domaine.stock.persistant;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import framework.model.beanContext.BasePersistant;
import framework.model.beanContext.ComptePersistant;

@Entity
@Table(name = "compte_ventilation")
public class CompteVentilationPersistant extends BasePersistant implements Serializable {
	@Column(length = 12, scale = 2, precision = 13, nullable=false) 
	private BigDecimal montant;

	@Column(length = 12, scale = 2, precision = 13) 
	private BigDecimal montant_ventilation;

	/*********************** Liens ************************************/
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "te_tva", referencedColumnName="id")
//	private ValTypeEnumPersistant opc_tva;

	@Column(length = 12, scale = 2, precision = 13) 
	private BigDecimal montant_tva;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "compte_id", referencedColumnName="id")
	private ComptePersistant opc_compte;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "depence_id", referencedColumnName="id")
	private ChargeDiversPersistant opc_charge;

	public BigDecimal getMontant() {
		return montant;
	}

	public void setMontant(BigDecimal montant) {
		this.montant = montant;
	}

	public BigDecimal getMontant_ventilation() {
		return montant_ventilation;
	}

	public void setMontant_ventilation(BigDecimal montant_ventilation) {
		this.montant_ventilation = montant_ventilation;
	}

//	public ValTypeEnumPersistant getOpc_tva() {
//		return opc_tva;
//	}
//
//	public void setOpc_tva(ValTypeEnumPersistant opc_tva) {
//		this.opc_tva = opc_tva;
//	}

	public BigDecimal getMontant_tva() {
		return montant_tva;
	}

	public void setMontant_tva(BigDecimal montant_tva) {
		this.montant_tva = montant_tva;
	}

	public void setOpc_compte(ComptePersistant opc_compte) {
		this.opc_compte = opc_compte;
	}

	public ComptePersistant getOpc_compte() {
		return opc_compte;
	}

	public ChargeDiversPersistant getOpc_charge() {
		return opc_charge;
	}

	public void setOpc_charge(ChargeDiversPersistant opc_charge) {
		this.opc_charge = opc_charge;
	}

}
