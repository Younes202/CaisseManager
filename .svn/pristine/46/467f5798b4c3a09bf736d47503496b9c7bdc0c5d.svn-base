package appli.model.domaine.administration.persistant;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import framework.model.beanContext.BasePersistant;
import framework.model.util.GsonExclude;

@Entity
@Table(name = "etat_finance_detail", indexes={
		@Index(name="IDX_ETFIN_DET_FUNC", columnList="code_func")
	})
public class EtatFinanceDetailPersistant extends BasePersistant {
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal mtt_etat_prev;
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal mtt_etat_actuel;
	@Column(length = 10, nullable=false)
	private String type;
	@Column(length = 50, nullable=false)
	private String libelle;
	
	@GsonExclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "etat_id", referencedColumnName="id")
	private EtatFinancePersistant opc_etat;
	
	public BigDecimal getMtt_etat_prev() {
		return mtt_etat_prev;
	}

	public void setMtt_etat_prev(BigDecimal mtt_etat_prev) {
		this.mtt_etat_prev = mtt_etat_prev;
	}

	public BigDecimal getMtt_etat_actuel() {
		return mtt_etat_actuel;
	}

	public void setMtt_etat_actuel(BigDecimal mtt_etat_actuel) {
		this.mtt_etat_actuel = mtt_etat_actuel;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public EtatFinancePersistant getOpc_etat() {
		return opc_etat;
	}

	public void setOpc_etat(EtatFinancePersistant opc_etat) {
		this.opc_etat = opc_etat;
	}
}

