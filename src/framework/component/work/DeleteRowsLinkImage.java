package framework.component.work;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import framework.component.ComponentUtil;
import framework.component.action.CompActionBase;
import framework.component.complex.table.TableTag;
import framework.model.common.constante.ActionConstante;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;

@SuppressWarnings("serial")
public class DeleteRowsLinkImage extends CompActionBase {
	private String params;


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
		super.releaseAll();
		params = null;
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

	/* (non-Javadoc)
	 * @see front.component.Component#writeStartComponent()
	 */
	public void writeStartComponent() throws JspException {
		StringBuilder sb = new StringBuilder();
		HttpServletRequest request = ((HttpServletRequest) pageContext.getRequest());
		TableTag parentTable = (TableTag) findAncestorWithClass(this, TableTag.class);

		String tableName = parentTable.getName();
		String controller = (String)request.getAttribute(ProjectConstante.WORK_CONTROLLER);
		String ctrlAct = controller + "." + ActionConstante.DELETE_ROWS;
		String defaultClass = "link";
		// Link
		String event = "\"javascript:testCheckedDeleteRows('"+tableName+"', '" + ctrlAct + "', '" + StringUtil.getValueOrEmpty(params) + "');\"";
		sb.append("<a href=" + event + getFullComponentAttrubutes(defaultClass) + ">");
		sb.append("<img border='0' alt='"+StrimUtil.label("button.delete")+"' src='"+ProjectConstante.IMG_DELETE_PATH+"'/>");
		sb.append("</a>");

		// Write result
		ComponentUtil.writeComponent(pageContext, sb.toString());
	}

	/**
	 * @return the params
	 */
	public String getParams() {
		return params;
	}

	/**
	 * @param params the params to set
	 */
	public void setParams(String params) {
		this.params = params;
	}
}
