package appli.model.domaine.administration.persistant;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import appli.model.domaine.personnel.persistant.EmployePersistant;
import framework.model.beanContext.BasePersistant;

@Entity
@Table(name = "vacance",
indexes= {@Index(name="IDX_VAC_FUNC",columnList="code_func")	
})
@NamedQueries({
	@NamedQuery(name="vacance_find", query="from VacancePersistant vacance order by vacance.id")
})
public class VacancePersistant extends BasePersistant {
	@Temporal(TemporalType.TIMESTAMP)
	@Column 
	private Date date_debut;
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date date_fin;
	@Column
	private Boolean is_permanant;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "remplacant_id", referencedColumnName="id")
	private EmployePersistant opc_remplacant;

	public Date getDate_debut() {
		return date_debut;
	}

	public void setDate_debut(Date date_debut) {
		this.date_debut = date_debut;
	}

	public Date getDate_fin() {
		return date_fin;
	}

	public void setDate_fin(Date date_fin) {
		this.date_fin = date_fin;
	}

	public Boolean getIs_permanant() {
		return is_permanant;
	}

	public void setIs_permanant(Boolean is_permanant) {
		this.is_permanant = is_permanant;
	}

	public EmployePersistant getOpc_remplacant() {
		return opc_remplacant;
	}

	public void setOpc_remplacant(EmployePersistant opc_remplacant) {
		this.opc_remplacant = opc_remplacant;
	}

}
