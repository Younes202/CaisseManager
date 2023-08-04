/**
 *
 */
package framework.component.box;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import framework.component.ComponentUtil;
import framework.model.common.util.StringUtil;

/**
 * @author 
 *
 */
@SuppressWarnings("serial")
public class RadioGroup extends BoxBase { 
	private String checkedValue;

	/**
	 * @throws JspTagException
	 *
	 */
	public void writeStartComponent() throws JspTagException{

	}

	/**
	 *
	 */
	public void writeEndComponent(){
	}

	@Override
	public void doBeforStartComponent() throws JspException {
		//ComponentUtil.writeComponent(pageContext, "<div>");
	}

	@Override
	public void releaseAll() {
		this.checkedValue = null;
		super.releaseAll();
	}

	@Override
	public void doAfterEndComponent() throws JspException {
		// Add required img
		StringBuilder sb = new StringBuilder();

		// If read only
		boolean isReadOnly = super.isReadOnlyAttributeForm();

		if(isReadOnly){
			String labelClass = "inputROnly";
			sb.append("<label class=\""+labelClass+"\"/>"+StringUtil.getValueOrEmpty(checkedValue)+"</label>");
		}

		// Fix probleme of submit disable component
		if(!isReadOnly && super.isReadOnlyAttributeForm()){
			sb.append("<input type='hidden' " + getFullName() + getFullValue() + getFullId() + "/>\n");
		}

		// Validator labels
		ComponentUtil.writeComponent(pageContext, sb);
	}

	public String getCheckedValue() {
		return checkedValue;
	}

	public void setCheckedValue(String checkedValue) {
		this.checkedValue = checkedValue;
	}
}
