package appli.controller.domaine.caisse.bean;

import appli.model.domaine.vente.persistant.CaissePersistant;
import framework.controller.bean.action.IViewBean;

public class CaisseBean extends CaissePersistant implements IViewBean {
	private String[] imprimante_array;
	private String[] menus_array;
	private String[] articles_array;
	private String[] familles_array;
	private String[] familles_balance_array;

	public String[] getImprimante_array() {
		return imprimante_array;
	}

	public void setImprimante_array(String[] imprimante_array) {
		this.imprimante_array = imprimante_array;
	}

	public String[] getMenus_array() {
		return menus_array;
	}

	public void setMenus_array(String[] menus_array) {
		this.menus_array = menus_array;
	}

	public String[] getArticles_array() {
		return articles_array;
	}

	public void setArticles_array(String[] articles_array) {
		this.articles_array = articles_array;
	}

	public String[] getFamilles_array() {
		return familles_array;
	}

	public void setFamilles_array(String[] familles_array) {
		this.familles_array = familles_array;
	}

	public String[] getFamilles_balance_array() {
		return familles_balance_array;
	}

	public void setFamilles_balance_array(String[] familles_balance_array) {
		this.familles_balance_array = familles_balance_array;
	}
}
