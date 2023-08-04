package appli.model.domaine.stock.persistant;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import framework.model.util.GsonExclude;

@Entity @DiscriminatorValue("FO")
public class FamilleFournisseurPersistant extends FamillePersistant  {
	@GsonExclude
	@OneToMany
	@JoinColumn(name="famille_id", referencedColumnName="id", insertable=false, updatable=false)
	List<FournisseurPersistant> list_fournisseur;

	public List<FournisseurPersistant> getList_fournisseur() {
		return list_fournisseur;
	}

	public void setList_fournisseur(List<FournisseurPersistant> list_fournisseur) {
		this.list_fournisseur = list_fournisseur;
	}
}
