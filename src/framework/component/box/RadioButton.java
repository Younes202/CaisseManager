/**
 *
 */
package framework.component.box;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import framework.component.ComponentUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;

/**
 * @author 
 *
 */
@SuppressWarnings("serial")
public class RadioButton extends BoxBase { 

	private String label;
	private String labelKey;
	private String labelPosition;

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

	/* (non-Javadoc)
	 * @see front.component.Component#writeStartComponent()
	 */
	public void writeStartComponent() throws JspException {
		RadioGroup parentRadio = (RadioGroup) findAncestorWithClass(this, RadioGroup.class);
		//HttpServletRequest request = getGuiOrContextHttpRequest();
		boolean isReadOnly = super.isReadOnlyAttributeForm()
					&& !StringUtil.isFalse(super.getReadOnly()) && !StringUtil.isFalse(super.getDisable());

		if (parentRadio == null) {
			throw new JspTagException("Radio button must be used in RadioGroup component");
		}
		
		StringBuilder sb = new StringBuilder();
		String checkValue = getValueSt();
		String labelDefaultStyle = "labelROnly";
		String labelOrLabelKey = getLabelOrLabelKey();
		
		//Stop if is read only
		if(isReadOnly){
			if(checkValue != null && checkValue.equals(parentRadio.getValue()) || (StringUtil.isTrue(getChecked()))){
				sb.append("<label class=\""+labelDefaultStyle+"\">"+labelOrLabelKey+"</label>&nbsp;");
				ComponentUtil.writeComponent(pageContext, sb.toString());
			}
			return;
		}
		
		String checkedBloc = "";
		// Build text component
		String defaultClass = "radio-stl";

		// Check or uncheck checkbox
		if ((checkValue != null && checkValue.equals(parentRadio.getValue())) || (StringUtil.isTrue(getChecked()))) {
			checkedBloc = " checked='checked'";
			//setChecked("true");
		}

		String simpleName = parentRadio.getName();
		this.setName(simpleName);
		// Add id
		this.setId(simpleName);
		// Build checkbox
		if(StringUtil.isEmpty(this.labelPosition)){
			this.labelPosition = "left";
		} else if(!this.labelPosition.equals("left") || this.labelPosition.equals("right")){
			this.labelPosition = "left";
		}
		
		if(this.labelPosition.equals("left") && StringUtil.isNotEmpty(labelOrLabelKey)){
			sb.append("<label style='margin-left: 15px;margin-top: 7px;vertical-align:text-bottom;' class=\""+labelDefaultStyle+"\">"+labelOrLabelKey+"</label>&nbsp;");
		}
		
		sb.append("<input type='radio'" + getFullComponentAttrubutes(defaultClass) + checkedBloc + getFullValue() + "/>\n");
		
		if(this.labelPosition.equals("right") && StringUtil.isNotEmpty(labelOrLabelKey)){
			sb.append("&nbsp;<label class=\""+labelDefaultStyle+"\">"+labelOrLabelKey+"</label>");
		}

		// Write result
		ComponentUtil.writeComponent(pageContext, sb.toString());
	}

	@Override
	public void doBeforStartComponent() throws JspException {
		RadioGroup parentRadio = (RadioGroup) findAncestorWithClass(this, RadioGroup.class);
		this.setName(parentRadio.getName());
	}

	@Override
	public void releaseAll() {
		this.label = null;
		this.labelKey = null;
		super.releaseAll();
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabelKey() {
		return labelKey;
	}

	public void setLabelKey(String labelKey) {
		this.labelKey = labelKey;
	}

	public String getLabelOrLabelKey(){
		if((this.label == null) && (this.labelKey != null)){
			return StrimUtil.label(getLabelKey());
		}

		return StringUtil.getValueOrEmpty(this.label);
	}

	public String getLabelPosition() {
		return labelPosition;
	}

	public void setLabelPosition(String labelPosition) {
		this.labelPosition = labelPosition;
	}
}
