package appli.model.domaine.personnel.persistant;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import appli.model.domaine.stock.persistant.FamillePersistant;
import framework.model.util.GsonExclude;

@Entity @DiscriminatorValue("EM")
public class FamilleEmployePersistant extends FamillePersistant  {
	@GsonExclude
	@OneToMany
	@JoinColumn(name="famille_id", referencedColumnName="id", insertable=false, updatable=false)
	List<EmployePersistant> list_employe;

	public List<EmployePersistant> getList_employe() {
		return list_employe;
	}

	public void setList_employe(List<EmployePersistant> list_employe) {
		this.list_employe = list_employe;
	}
	
}
