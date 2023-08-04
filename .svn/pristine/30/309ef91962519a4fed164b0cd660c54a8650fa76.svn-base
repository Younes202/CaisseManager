package appli.model.domaine.stock.persistant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import framework.model.beanContext.BasePersistant;

@Entity
@Table(name = "fournisseur_contact", indexes={
		@Index(name="IDX_FOU_CTC_FUNC", columnList="code_func")
	})
@NamedQuery(name="fournisseurContact_find", query="from FournisseurContactPersistant fournisseurContact" +
		" order by fournisseurContact.contact")
public class FournisseurContactPersistant extends BasePersistant  {
	@Column(length = 80, nullable = false)
	private String contact;

	@Column(length = 80)
	private String fonction;

	@Column(length = 80)
	private String coord;

	/*********************** Liens ************************************/

	@ManyToOne
	@JoinColumn(name = "fournisseur_id", referencedColumnName = "id")
	private FournisseurPersistant opc_fournisseur;

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

	public FournisseurPersistant getOpc_fournisseur() {
		return opc_fournisseur;
	}

	public void setOpc_fournisseur(FournisseurPersistant opc_fournisseur) {
		this.opc_fournisseur = opc_fournisseur;
	}

}
