package framework.component.facade.action;

import framework.component.facade.ComponentBase;

public class UI_LinkPopup extends ComponentBase {
	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		manageGuiComponent("setAction", action);
	}
	
	public void setHeight(String height) {
		manageGuiComponent("setHeight", height);
	}

	public void setWidth(String width) {
		manageGuiComponent("setWidth", width);
	}

	public void setDraggable(String draggable) {
		manageGuiComponent("setDraggable", draggable);
	}


	public void setOnClose(String onClose) {
		manageGuiComponent("setOnClose", onClose);
	}

	public void setUrl(String url) {
		manageGuiComponent("setUrl", url);
	}

	public void setShowActBanner(String showActBanner) {
		manageGuiComponent("setAction", showActBanner);
	}

	public void setParams(String params) {
		manageGuiComponent("setAction", params);
	}

	public void setOnCloseRefreshTargets(String onCloseRefreshTargets) {
		manageGuiComponent("setAction", onCloseRefreshTargets);
	}

	public void setReadWriteMode(String readWriteMode) {
		manageGuiComponent("setAction", readWriteMode);
	}

	public void setModal(String modal) {
		manageGuiComponent("setAction", modal);
	}

	public void setOnCloseRefreshFunctions(String onCloseRefreshFunctions) {
		manageGuiComponent("setAction", onCloseRefreshFunctions);
	}

	public void setShowNavBanner(String showNavBanner) {
		manageGuiComponent("setAction", showNavBanner);
	}

}
