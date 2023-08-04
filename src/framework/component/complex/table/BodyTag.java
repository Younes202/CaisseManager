package framework.component.complex.table;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import framework.component.ComponentUtil;
import framework.controller.ControllerUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;


@SuppressWarnings("serial")
public class BodyTag extends TagSupport {
	 private String controller;

	/**
	 * @return
	 * @throws JspException
	 */
	@Override
	public int doStartTag() throws JspTagException, JspException {
		writeStartComponent();

		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		writeEndComponent();
		this.controller = null;

		return EVAL_PAGE;
	}

	/**
	 * @throws JspTagException
	 * @throws JspException
	 */
	public void writeStartComponent() throws JspException {
		TableTag parentTable = (TableTag) findAncestorWithClass(this, TableTag.class);
		if (parentTable == null) {
			throw new JspTagException("Header Tag without Table Tag");
		}
		//
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String parentTableName = parentTable.getName();
		StringBuilder sb = new StringBuilder();
		String defaultTableClass = "sortable";

		// Add overidded controller
		request.setAttribute("overController", controller);

		// Content of table
		sb.append("<table id=\""+parentTableName+"_body\" cellspacing=\"0\" cellpadding=\"0\" class=\""+ defaultTableClass +"\" width=\"100%\" style=\"table-layout:fixed;\">\n");
			sb.append("<tbody>\n");

		ComponentUtil.writeComponent(pageContext, sb);
	}

	/**
	 * @throws JspException
	 */
	public void writeEndComponent() throws JspException {
		StringBuilder sb = new StringBuilder();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		TableTag parentTable = (TableTag) findAncestorWithClass(this, TableTag.class);
		String parentTableName = parentTable.getName();
		// Add row not element founded
		RequestTableBean cplxTable = ControllerUtil.getRequestTableBean(parentTableName, request);
		if(cplxTable.getDataSize() == 0){
			int cellAdd = StringUtil.isTrueOrNull(parentTable.getCheckable()) ? 2 : 1;
			sb.append("<tr align=\"center\"><td align=\"center\" colspan=\""+(parentTable.getListTh().size()+cellAdd)+"\">"+StrimUtil.label("table.empty")+"</td></tr>\n");
		}

		// End table
		sb.append("</tbody>\n" +
				"</table>\n");
		ComponentUtil.writeComponent(pageContext, sb);
	}

	public String getController() {
		return controller;
	}

	public void setController(String controller) {
		this.controller = controller;
	}
}
