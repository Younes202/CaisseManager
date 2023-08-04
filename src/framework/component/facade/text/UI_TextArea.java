/**
 *
 */
package framework.component.facade.text;


/**
 * @author 
 *
 */
public class UI_TextArea extends TextBase {

	/**
	 * @param rows
	 *            the rows to set
	 */
	public void setRows(String rows) {
		manageGuiComponent("setRows", rows);
	}

	/**
	 * @param cols
	 *            the cols to set
	 */
	public void setCols(String cols) {
		manageGuiComponent("setCols", cols);
	}
}
