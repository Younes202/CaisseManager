/**
 *
 */
package framework.component.facade.text;


/**
 * @author 
 *
 */
public class UI_Text extends TextBase {

	public void setMask(String mask) {
		manageGuiComponent("setMask", mask);
	}

	public void setPreventId(String preventId) {
		manageGuiComponent("setPreventId", preventId);
	}
}