package appli.model.domaine.personnel.persistant.paie;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import appli.model.domaine.personnel.persistant.EmployePersistant;
import framework.model.beanContext.BasePersistant;

@Entity
@Table(name = "employe_pointeuse", indexes={
		@Index(name="IDX_EMPL_POI_FUNC", columnList="code_func"),
		@Index(name="IDX_EMPL_POI_NUM", columnList="numero_client"),
	})
public class PointageEventPersistant extends BasePersistant  {
	@Column(length = 20, nullable = false)
	private String numero_client;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date date_pointage;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal tarif_ref;// tarif de référence (salaire, heur supp, ...)

	@ManyToOne
	@JoinColumn(name = "employe_id", referencedColumnName = "id", nullable = false)
	private EmployePersistant opc_employe;

	public String getNumero_client() {
		return numero_client;
	}

	public void setNumero_client(String numero_client) {
		this.numero_client = numero_client;
	}

	public Date getDate_pointage() {
		return date_pointage;
	}

	public void setDate_pointage(Date date_pointage) {
		this.date_pointage = date_pointage;
	}

	public EmployePersistant getOpc_employe() {
		return opc_employe;
	}

	public void setOpc_employe(EmployePersistant opc_employe) {
		this.opc_employe = opc_employe;
	}

	public BigDecimal getTarif_ref() {
		return tarif_ref;
	}

	public void setTarif_ref(BigDecimal tarif_ref) {
		this.tarif_ref = tarif_ref;
	}
}