package appli.model.domaine.personnel.persistant.paie;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import appli.model.domaine.personnel.persistant.EmployePersistant;
import framework.model.beanContext.BasePersistant;

/**
 * @author Dev1
 *
 */
@Entity
@Table(name = "pointage_recap", indexes={
		@Index(name="IDX_PNT_RECAP_FUNC", columnList="code_func")
	})
public class PointageRecapPersistant extends BasePersistant  {
	@Column(length = 15, scale = 2, precision = 4)
	private BigDecimal duree_travail;// En heure

	@Column(length = 15, scale = 2, precision = 4)
	private BigDecimal duree_ref;// Nombre d'heure d'heures de travail référence

	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date_pointage;
	
	/*********************** Liens ************************************/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employe_id", referencedColumnName="id", nullable=false)
	private EmployePersistant opc_employe;

	public BigDecimal getDuree_travail() {
		return duree_travail;
	}

	public void setDuree_travail(BigDecimal duree_travail) {
		this.duree_travail = duree_travail;
	}

	public BigDecimal getDuree_ref() {
		return duree_ref;
	}

	public void setDuree_ref(BigDecimal duree_ref) {
		this.duree_ref = duree_ref;
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
	
	
}