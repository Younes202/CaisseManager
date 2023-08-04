package appli.model.domaine.personnel.persistant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import framework.model.beanContext.BasePersistant;
import framework.model.util.GsonExclude;

@Entity
@Table(name = "societeLivr_contact", indexes={
		@Index(name="IDX_SOCL_FUNC", columnList="code_func")
	})
public class SocieteLivrContactPersistant extends BasePersistant  {
	@Column(length = 80, nullable = false)
	private String contact;

	@Column(length = 80)
	private String fonction;

	@Column(length = 80)
	private String coord;

	/*********************** Liens ************************************/
	@GsonExclude
	@ManyToOne
	@JoinColumn(name = "societeLivr_id", referencedColumnName = "id")
	private SocieteLivrPersistant opc_SocieteLivr;

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getFonction() {
		return fonction;
	}

	public void setFonction(String fonction) {
		this.fonction = fonction;
	}

	public String getCoord() {
		return coord;
	}

	public void setCoord(String coord) {
		this.coord = coord;
	}

	public SocieteLivrPersistant getOpc_SocieteLivr() {
		return opc_SocieteLivr;
	}

	public void setOpc_SocieteLivr(SocieteLivrPersistant opc_SocieteLivr) {
		this.opc_SocieteLivr = opc_SocieteLivr;
	}

}