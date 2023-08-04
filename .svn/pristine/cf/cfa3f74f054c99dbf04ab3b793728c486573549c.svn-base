package framework.component.complex.table.export;

import javax.servlet.jsp.JspException;

import framework.component.Component;
import framework.component.complex.table.TableTag;
import framework.controller.bean.ColumnsExportBean;

@SuppressWarnings("serial")
public class ExportThTag extends Component {
	
	private String field;
	private String width;
	private String type;

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		TableTag parentTable = (TableTag) findAncestorWithClass(this, TableTag.class);
		//
		ColumnsExportBean columnBean = new ColumnsExportBean();
		columnBean.setField(this.field);
		columnBean.setType(this.type);
		columnBean.setLabel(super.getValueOrValueKey());
		columnBean.setWidth(this.width);
		//
		parentTable.getListExportTh().add(columnBean);
		
		return SKIP_BODY;
	}
	
	@Override
	public int doEndTag() throws JspException {
		field = null;
		width = null;
		type = null;
		
		return EVAL_PAGE;
	}
	
	/**
	 * @return the field
	 */
	public String getField() {
		return field;
	}

	/**
	 * @param field the field to set
	 */
	public void setField(String field) {
		this.field = field;
	}

	/**
	 * @return the width
	 */
	public String getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(String width) {
		this.width = width;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
}
