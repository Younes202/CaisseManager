/**
 *
 */
package framework.component.box;

import javax.servlet.jsp.JspException;

import framework.component.ComponentUtil;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;

/**
 * @author 
 *
 */
public class CheckBox extends BoxBase {

	private static final long serialVersionUID = -1442132197739139436L;

	/* (non-Javadoc)
	 * @see front.component.Component#doBeforStartComponent()
	 */
	@Override
	public void doBeforStartComponent() throws JspException {
		setType(ProjectConstante.TYPE_DATA_ENUM.BOOLEAN.getType());
	}

	/* (non-Javadoc)
	 * @see front.component.Component#releaseAll()
	 */
	@Override
	public void releaseAll() {
		super.releaseAll();
	}

	/* (non-Javadoc)
	 * @see front.component.Component#writeEndComponent()
	 */
	public void writeEndComponent() throws JspException {

	}

	/* (non-Javadoc)
	 * @see front.component.ComponentBase#doAfterEndComponent()
	 */
	@Override
	public void doAfterEndComponent() throws JspException {

	}

	/**
	 * Add label if read only
	 * @return
	 */
	private boolean writeReadOnlyComponent(){
		String defaultClass = "inputROnly";
		boolean isReadOnly = super.isReadOnlyAttributeForm();
		String labelVal = StrimUtil.label("no");
		String checkVal = StringUtil.isNotEmpty(getChecked()) ? getValueSt(): getValueSt();
		boolean isChecked = StringUtil.isTrue(checkVal) || StringUtil.isTrue(getChecked());
		
		//
		if(StringUtil.isNotEmpty(checkVal) || StringUtil.isNotEmpty(getChecked())){
			labelVal = (isChecked?StrimUtil.label("yes"): labelVal);
		}

		//
		if(isReadOnly){
			String label = "<label " + getFullClassStyle(defaultClass) + ">" + labelVal + "</label>\n";
			label += "<input type='hidden' " + this.getFullName() + " value='" + isChecked + "'/>";
			//
			ComponentUtil.writeComponent(getContextOrJspContext(), label);
			return true;
		}

		return false;
	}

	/* (non-Javadoc)
	 * @see front.component.Component#writeStartComponent()
	 */
	public void writeStartComponent() throws JspException {
		//
		if(writeReadOnlyComponent()) return;

		String checkValue = getValueSt();
		StringBuilder sb = new StringBuilder();
		String checkedBloc = "";
		// Build text component
		String defaultClass = "check-stl";

		// Check or uncheck checkbox
		if (StringUtil.isTrue(checkValue) || (StringUtil.isTrue(getChecked()))) {
			checkedBloc = " checked='checked'";
		}
		// Add id
		if(getId() == null){
			super.setId(super.getName());
		}

		// Build checkbox
		sb.append("<input type='checkbox'" + getFullComponentAttrubutes(defaultClass) + checkedBloc + (StringUtil.isNotEmpty(getValue()) ? getFullValue():" value='true'")+" />");

		// Fix probleme of submit disable component
		if(super.isReadOnlyAttributeForm()){
			sb.append("\n<input type='hidden' "+getName()+ (StringUtil.isNotEmpty(checkedBloc)?" value='true'" : "''") +"/>");
		}
		// Write result
		ComponentUtil.writeComponent(getContextOrJspContext(), sb.toString());
	}
}
