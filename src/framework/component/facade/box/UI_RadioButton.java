/**
 *
 */
package framework.component.facade.box;


/**
 * @author 
 *
 */
public class UI_RadioButton extends BoxBase {

	public void setLabel(String label) {
		manageGuiComponent("setLabel", label);
	}

	public void setLabelKey(String labelKey) {
		manageGuiComponent("setLabelKey", labelKey);
	}
}
