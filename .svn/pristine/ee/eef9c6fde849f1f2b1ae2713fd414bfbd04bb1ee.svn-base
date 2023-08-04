package appli.model.domaine.administration.persistant;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import framework.model.beanContext.BasePersistant;
import framework.model.util.GsonExclude;

@Entity
@Table(name = "etat_finance_paiement")
public class EtatFinancePaiementPersistant extends BasePersistant {
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal mtt_cheque;
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal mtt_carte;
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal mtt_espece;

	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal mtt_dej;
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal mtt_cheque_f;
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal mtt_virement;
	
	@Column(length = 10, nullable=false)
	private String type;
	
	@GsonExclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "etat_id", referencedColumnName="id")
	private EtatFinancePersistant opc_etat;
	
	public BigDecimal getMtt_dej() {
		return mtt_dej;
	}

	public void setMtt_dej(BigDecimal mtt_dej) {
		this.mtt_dej = mtt_dej;
	}

	public BigDecimal getMtt_cheque_f() {
		return mtt_cheque_f;
	}

	public void setMtt_cheque_f(BigDecimal mtt_cheque_f) {
		this.mtt_cheque_f = mtt_cheque_f;
	}

	public BigDecimal getMtt_cheque() {
		return mtt_cheque;
	}

	public void setMtt_cheque(BigDecimal mtt_cheque) {
		this.mtt_cheque = mtt_cheque;
	}

	public BigDecimal getMtt_carte() {
		return mtt_carte;
	}

	public void setMtt_carte(BigDecimal mtt_carte) {
		this.mtt_carte = mtt_carte;
	}

	public BigDecimal getMtt_espece() {
		return mtt_espece;
	}

	public void setMtt_espece(BigDecimal mtt_espece) {
		this.mtt_espece = mtt_espece;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public EtatFinancePersistant getOpc_etat() {
		return opc_etat;
	}

	public void setOpc_etat(EtatFinancePersistant opc_etat) {
		this.opc_etat = opc_etat;
	}

	public BigDecimal getMtt_virement() {
		return mtt_virement;
	}

	public void setMtt_virement(BigDecimal mtt_virement) {
		this.mtt_virement = mtt_virement;
	}
}

