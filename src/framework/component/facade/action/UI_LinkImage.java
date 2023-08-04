package framework.component.facade.action;

import framework.component.facade.ComponentBase;

public class UI_LinkImage extends ComponentBase {

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		manageGuiComponent("setAction", action);
	}
	
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		manageGuiComponent("setUrl", url);
	}

	/**
	 * @param img the img to set
	 */
	public void setImg(String img) {
		manageGuiComponent("setImg", img);
	}
}
