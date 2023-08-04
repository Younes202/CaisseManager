package appli.model.domaine.stock.persistant;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import framework.model.util.GsonExclude;

@Entity @DiscriminatorValue("ST")
public class FamilleStockPersistant extends FamillePersistant  {
	@GsonExclude
	@OneToMany
	@JoinColumn(name="famille_stock_id", referencedColumnName="id", insertable=false, updatable=false)
	List<ArticlePersistant> list_article;

	public List<ArticlePersistant> getList_article() {
		return list_article;
	}

	public void setList_article(List<ArticlePersistant> list_article) {
		this.list_article = list_article;
	}
}
