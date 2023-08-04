/**
 *
 */
package framework.component.text;

import javax.servlet.jsp.JspException;

import framework.component.ComponentBase;
import framework.component.ComponentUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;

/**
 * @author 
 *
 */
public class Label extends ComponentBase {
	private String addSeparator;

	/**
	 *
	 */
	private static final long serialVersionUID = 5845380460001094536L;

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
		if(StringUtil.isNotEmpty(getName()) && StringUtil.isEmpty(getId())){
			setId(getName());
		}

		String defaultStyle = "label";
		StringBuilder sb = new StringBuilder("<label ");
		sb.append(getFullComponentAttrubutes(defaultStyle) + ">");
		//
		sb.append(getValueOrValueKey());

		if(StringUtil.isTrueOrNull(addSeparator)){
			sb.append(StrimUtil.getGlobalConfigPropertie("default.label.separator"));
		}

		sb.append("</label>");

		ComponentUtil.writeComponent(pageContext, sb.toString());
	}

	/* (non-Javadoc)
	 * @see front.component.Component#doBeforStartComponent()
	 */
	@Override
	public void doBeforStartComponent() throws JspException {

	}

	/* (non-Javadoc)
	 * @see front.component.Component#releaseAll()
	 */
	@Override
	public void releaseAll() {
		this.addSeparator = null;
	}

	public String getAddSeparator() {
		return addSeparator;
	}

	public void setAddSeparator(String addSeparator) {
		this.addSeparator = addSeparator;
	}
}
