package framework.component.complex.table;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import framework.component.ComponentUtil;
import framework.controller.ControllerUtil;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.util.NumericUtil;
import framework.model.common.util.StringUtil;


@SuppressWarnings("serial")
public class HeaderTag extends TableTag{

	private int idxTh = 0;

	@Override
	public int doStartTag() throws JspTagException, JspException {
		writeStartHeaderComponent();

		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		writeEndHeaderComponent();

		return EVAL_PAGE;
	}

	/**
	 *
	 */
	private void writeStartHeaderComponent() {
		idxTh = 0;
	}

	/**
	 * @throws JspTagException
	 */
	@SuppressWarnings("unchecked")
	private void writeEndHeaderComponent() throws JspTagException {
		TableTag parentTable = (TableTag) findAncestorWithClass(this, TableTag.class);
		if (parentTable == null) {
			throw new JspTagException("Header Tag without Table Tag");
		}
		//
		StringBuilder sb = new StringBuilder();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String parentTableName = parentTable.getName();
		RequestTableBean cplxTable = ControllerUtil.getRequestTableBean(parentTableName, request);
		// Start header sorter and filter of table
		sb.append("<table id=\""+parentTableName+"\" cellspacing=\"0\" cellpadding=\"0\" style=\"height: 30px;table-layout:fixed;\" class=\"sortable grid-header-bg\" width=\"100%\"><thead>\n")
	    .append("<tr>\n");
		// Add column with checkbox
		boolean isChackable = StringUtil.isTrueOrNull(parentTable.getCheckable());
		int dataSize = cplxTable.getDataSize();
		if(isChackable){
			String checkId = parentTableName + "_" + ProjectConstante.CHECK_CHECK_ALL;
			String checkValue = "all_" + cplxTable.getCurrentPage();
			String disbled =  (dataSize > 0) ? "" : " disabled ";
			// Start
			String thWidth = " width='"+ProjectConstante.TD_MIN_WIDTH+"'";
			
			if(!ComponentUtil.isReadOnlyFormSetted(request)){
				// Checkbox
				sb.append("<th " + thWidth + " id=\"c\">");
				sb.append("<div style=\"text-align: center; width: "+ProjectConstante.TD_MIN_WIDTH+"px;\">");
				sb.append("<input type='checkbox' id='"+checkId+"' name='"+checkId+"' value='"+checkValue+"' "+disbled+"");
				// Restaure old values
				if(dataSize > 0){
					parentTable.setCheckboxFound(true);
					Map<String, Object> params = (Map)request.getAttribute(ProjectConstante.WORK_PARAMS);
					String check = (String)params.get(parentTableName + "_" + ProjectConstante.CHECK_SAVE_STR);
					List<String> listElements = StringUtil.getElementsList(check, "|", false);
					if(listElements != null){
						if(listElements.contains(checkValue)){
							sb.append(" checked='checked'");
						}
					}
				}
				sb.append("/>");
				sb.append("</div>");
				sb.append("</th>\n");
			}
		}
		// Add header columns
		sb.append(parentTable.getThBlock()+"\n");
		// End thead
		sb.append("</thead></table>\n");

		// Adapt height table if not data found
		String tableHeight = "355";
		
		if (dataSize == 0){
			tableHeight = "60";
		} else if(parentTable.getTrHeight() != null){
			int customTrHeight = NumericUtil.getIntOrDefault(parentTable.getTrHeight());
			tableHeight = "" + (dataSize* ((dataSize == 0) ? 1 : customTrHeight));
		} else if(dataSize < 2){
			dataSize = (dataSize == 0) ? 1 : dataSize;
			tableHeight = "" + (dataSize* 50);
		} else if(dataSize < 10){
			tableHeight = "" + (dataSize* 40);
		}
		
		tableHeight = "height: "+tableHeight+"px;";
		if(StringUtil.isTrue(parentTable.getAutoHeight())){
			tableHeight = "";
		}

		String scroll = "scroll";
		if(StringUtil.isFalseOrNull(getAutoHeight())){
			scroll = "hidden";
		}
		sb.append("<div id=\"scroll_"+parentTableName+"\" style=\"overflow-x:hidden; overflow-y: "+scroll+";border: 1px solid #ccc; "+tableHeight+" width: 100%;\">\n");
		//
		ComponentUtil.writeComponent(pageContext, sb);

		//
		super.release();
		idxTh = 0;
	}

	public int getIdxTh() {
		return idxTh;
	}

	public void setIdxTh(int idxTh) {
		this.idxTh = idxTh;
	}
}
