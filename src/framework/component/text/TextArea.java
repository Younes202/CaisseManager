/**
 *
 */
package framework.component.text;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import framework.component.ComponentUtil;
import framework.model.common.constante.ActionConstante;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.service.MessageService;
import framework.model.common.util.StringUtil;

/**
 * @author 
 *
 */
public class TextArea extends TextBase {

	/**
	 *
	 */
	private static final long serialVersionUID = -3226046381606266358L;
	private String rows;
	private String cols;

	/* (non-Javadoc)
	 * @see front.component.Component#doBeforStartComponent()
	 */
	@Override
	public void doBeforStartComponent() throws JspException {
		setType("string");
	}

	/* (non-Javadoc)
	 * @see front.component.Component#releaseAll()
	 */
	@Override
	public void releaseAll() {
		rows = null;
		cols = null;
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
		if(StringUtil.isFalseOrNull(getReadOnly())){
			ComponentUtil.writeComponent(pageContext, "<br /><label id=\""+getId()+"_chars\" class=\"charsRemaining\" style=\"display:none;\"/>");
		}
	}

	/* (non-Javadoc)
	 * @see front.component.Component#writeStartComponent()
	 */
	public void writeStartComponent() throws JspException {
		StringBuilder sb = new StringBuilder();
		// Build text component
		HttpServletRequest request = getGuiOrContextHttpRequest();
		boolean isReadOnly = super.isReadOnlyAttributeForm();
		String defaultClass = "areatext";

		// Add id
		if(getId() == null){
			super.setId(super.getName());
		}

		if(isReadOnly){
			this.setReadOnly("true");
			this.appendStyle("background-color:#eeeeee;");
		}
			String action = (String) request.getAttribute(ProjectConstante.WORK_ACTION);
			// Put null in key field
			if(ActionConstante.INIT_DUPLIC.equals(action) && !MessageService.isError() && StringUtil.isTrue(this.getIskey())){
				setValue(null);
			}
			
			sb.append("<textarea" + getFullRows() + getCols() + getFullComponentAttrubutes(defaultClass)+ getFullTextAttrubutes() + ">"
					+ getValueSt() +
				"</textarea>");

		// Write result
		ComponentUtil.writeComponent(pageContext, sb.toString());
	}

	/**
	 * @return the rows
	 */
	public String getFullRows() {
		if (this.rows != null) {
			return " rows=\"" + this.rows + "\"";
		} else {
			return "";
		}
	}

	public String getRows() {
		return this.rows;
	}

	/**
	 * @param rows
	 *            the rows to set
	 */
	public void setRows(String rows) {
		this.rows = rows;
	}

	/**
	 * @return the cols
	 */
	public String getCols() {
		if (this.cols != null) {
			return " cols=\"" + this.cols + "\"";
		} else {
			return "";
		}
	}

	/**
	 * @param cols
	 *            the cols to set
	 */
	public void setCols(String cols) {
		this.cols = cols; 
	}
}
