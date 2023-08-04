package framework.component.facade;


public class UI_Tooltip extends ComponentBase {
	public void setSticky(String sticky) {
		manageGuiComponent("setSticky", sticky);
	}

	public void setHelpPath(String helpPath) {
		manageGuiComponent("setHelpPath", helpPath);
	}

	public void setContentKey(String contentKey) {
		manageGuiComponent("setContentKey", contentKey);
	}

	public void setContent(String content) {
		manageGuiComponent("setContent", content);
	}

	public void setImgVAlign(String imgVAlign) {
		manageGuiComponent("setImgVAlign", imgVAlign);
	}

	public void setWidth(String width) {
		manageGuiComponent("setWidth", width);
	}

	public void setHeight(String height) {
		manageGuiComponent("setHeight", height);
	}

	public void setMouseOutClose(String mouseOutClose) {
		manageGuiComponent("setMouseOutClose", mouseOutClose);
	}

	public void setTracking(String tracking) {
		manageGuiComponent("setTracking", tracking);
	}

	public void setDelayedClose(String delayedClose) {
		manageGuiComponent("setDelayedClose", delayedClose);
	}

}
