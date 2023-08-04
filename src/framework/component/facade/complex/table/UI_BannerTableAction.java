package framework.component.facade.complex.table;

import framework.component.complex.table.BannerTableButton;
import framework.component.facade.ComponentBase;

public class UI_BannerTableAction extends ComponentBase{
	
	/**
	 * @param buttonType
	 */
	public void addButton(BannerTableButton BannerTableButton){
		manageGuiComponent("addButton", BannerTableButton);
	}

	public void removeAllActifButton(){
		manageGuiComponent("removeAllActifButton", null);
	}

	/**
	 * @param buttonType
	 */
	public void removeButton(BannerTableButton buttonBanner){
		manageGuiComponent("removeButton", buttonBanner);
	}
	
	/**
	 * @param buttonType
	 */
	public void removeButton(String buttonId){
		manageGuiComponent("removeButton", buttonId);
	}

	public void setButtonsJspPath(String buttonsJspPath) {
		manageGuiComponent("setButtonsJspPath", buttonsJspPath);
	}
	
	public BannerTableButton getButton(String id){
		BannerTableButton BannerTableButton = new BannerTableButton(id, null, null, null, null);
		manageGuiComponent("addButton", BannerTableButton);
		return BannerTableButton;
	}
}
