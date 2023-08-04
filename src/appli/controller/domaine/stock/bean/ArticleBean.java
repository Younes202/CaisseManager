package appli.controller.domaine.stock.bean;

import appli.model.domaine.stock.persistant.ArticlePersistant;
import framework.controller.bean.action.IViewBean;

public class ArticleBean extends ArticlePersistant implements IViewBean {
	public String[] fournisseursArray;
	private String articleDet;

	public String[] getFournisseursArray() {
		return fournisseursArray;
	}

	public void setFournisseursArray(String[] fournisseursArray) {
		this.fournisseursArray = fournisseursArray;
	}

	public String getArticleDet() {
		return articleDet;
	}

	public void setArticleDet(String articleDet) {
		this.articleDet = articleDet;
	}
}
