package appli.model.domaine.stock.persistant;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity @DiscriminatorValue("MNU")
public class FamilleMenuPersistant extends FamillePersistant  {
	@OneToMany
	@JoinColumn(name="famille_id", referencedColumnName="id", insertable=false, updatable=false)
	List<MouvementPersistant> list_article_menu;

	public List<MouvementPersistant> getList_article_menu() {
		return list_article_menu;
	}

	public void setList_article_menu(List<MouvementPersistant> list_article_menu) {
		this.list_article_menu = list_article_menu;
	}
}
