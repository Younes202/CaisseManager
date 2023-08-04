package appli.model.domaine.stock.persistant;

import java.io.Serializable;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity @DiscriminatorValue("CO")
public class FamilleConsommationPersistant extends FamillePersistant implements Serializable {
	@OneToMany
	@JoinColumn(name="famille_conso_id", referencedColumnName="id", insertable=false, updatable=false)
	List<MouvementPersistant> list_mvm_consommation;

	public List<MouvementPersistant> getList_mvm_consommation() {
		return list_mvm_consommation;
	}

	public void setList_mvm_consommation(
			List<MouvementPersistant> list_mvm_consommation) {
		this.list_mvm_consommation = list_mvm_consommation;
	}
	
}
