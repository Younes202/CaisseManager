
package appli.model.domaine.personnel.persistant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import framework.model.beanContext.BasePersistant;

@Entity
@Table(name = "client_contact", indexes={
		@Index(name="IDX_CLI_CNT_FUNC", columnList="code_func")
	})
@NamedQuery(name="clientContact_find", query="from ClientContactPersistant clientContact" +
		" order by clientContact.contact")
public class ClientContactPersistant extends BasePersistant  {
	@Column(length = 80, nullable = false)
	private String contact;

	@Column(length = 80)
	private String fonction;

	@Column(length = 80)
	private String coord;

	/*********************** Liens ************************************/

	@ManyToOne
	@JoinColumn(name = "client_id", referencedColumnName = "id")
	private ClientPersistant opc_client;

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

	public ClientPersistant getOpc_client() {
		return opc_client;
	}

	public void setOpc_client(ClientPersistant opc_client) {
		this.opc_client = opc_client;
	}
}
