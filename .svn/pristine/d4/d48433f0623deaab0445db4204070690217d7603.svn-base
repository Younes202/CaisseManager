package appli.model.domaine.stock.persistant;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import framework.model.util.GsonExclude;

@Entity @DiscriminatorValue("CU")
public class FamilleCuisinePersistant extends FamillePersistant {
	@GsonExclude
	@OneToMany
	@JoinColumn(name="famille_cuisine_id", referencedColumnName="id", insertable=false, updatable=false)
	List<ArticlePersistant> list_article;
//	@GsonExclude
//	@OneToMany
//	@JoinColumn(name="famille_id", referencedColumnName="id", insertable=false, updatable=false)
//	List<ListChoixDetailPersistant> list_choix;
//	@GsonExclude
//	@OneToMany
//	@JoinColumn(name="famille_id", referencedColumnName="id", insertable=false, updatable=false)
//	List<MenuCompositionPersistant> list_menu;
//
	public List<ArticlePersistant> getList_article() {
		return list_article;
	}

	public void setList_article(List<ArticlePersistant> list_article) {
		this.list_article = list_article;
	}
//
//	public List<ListChoixDetailPersistant> getList_choix() {
//		return list_choix;
//	}
//
//	public void setList_choix(List<ListChoixDetailPersistant> list_choix) {
//		this.list_choix = list_choix;
//	}
//
//	public List<MenuCompositionPersistant> getList_menu() {
//		return list_menu;
//	}
//
//	public void setList_menu(List<MenuCompositionPersistant> list_menu) {
//		this.list_menu = list_menu;
//	}
}
