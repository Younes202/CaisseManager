package appli.model.domaine.personnel.persistant;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import framework.model.beanContext.BasePersistant;

@Entity
@Table(name = "employe_apperciation")
public class EmployeAppreciationPersistant extends BasePersistant  {
	/**
	 * 
	 */
	
	
	private static final long serialVersionUID = 1L;
	
	//add genre and conjoint


	@Column(length = 50, nullable = false)
	private String type;

	@Column(length = 255)
	private String justification;
	
	@Column
	private Date date_debut;
	
	@ManyToOne
    @JoinColumn(name = "employe_id", referencedColumnName = "id", nullable=false)
    private EmployePersistant opc_employe;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getJustification() {
		return justification;
	}

	public void setJustification(String justification) {
		this.justification = justification;
	}

	public Date getDate_debut() {
		return date_debut;
	}

	public void setDate_debut(Date date_debut) {
		this.date_debut = date_debut;
	}

	public EmployePersistant getOpc_employe() {
		return opc_employe;
	}

	public void setOpc_employe(EmployePersistant opc_employe) {
		this.opc_employe = opc_employe;
	}
	
//	private String img;
//	@Column(length = 1)
	
	// la date debut date entree 
	
	
}