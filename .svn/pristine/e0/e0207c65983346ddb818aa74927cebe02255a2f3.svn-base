package framework.component.complex.table;

import java.math.BigDecimal;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import framework.component.Component;
import framework.component.ComponentUtil;
import framework.controller.bean.ColumnsExportBean;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.StringUtil;


@SuppressWarnings("serial")
public class TdTag extends Component {
	private String align;

	@Override
	public int doStartTag() throws JspException {
		BodyTag parentBody = (BodyTag) findAncestorWithClass(this, BodyTag.class);
		if(parentBody != null){
			writeStartComponent();
		}

		return EVAL_BODY_INCLUDE;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		BodyTag parentBody = (BodyTag) findAncestorWithClass(this, BodyTag.class);
		if(parentBody != null){
			writeEndComponent();
		}
		//
		super.release();

		return EVAL_PAGE;
	}

	/**
	 * @throws JspTagException
	 */
	public void writeStartComponent() throws JspException {
		TableTag parentTable = (TableTag) findAncestorWithClass(this, TableTag.class);
		TrTag parentTr = (TrTag) findAncestorWithClass(this, TrTag.class);
		StringBuilder sb = new StringBuilder();
		String tdValue = getValueSt();
		int idxCurrTd = parentTr.getIdxTd();

		//
		parentTr.setIdxTd( idxCurrTd + 1);

		if(this.align != null){
			this.align = " align='"+this.align+"'"; 
		}
		//
		ColumnsExportBean columnBean = parentTable.getListTh().get(idxCurrTd);
		String type = columnBean.getType();
		//
		sb.append("<td" + getFullComponentAttrubutes(null) + StringUtil.getValueOrEmpty(this.align)+ ">\n");
		if(type != null){
			// mange groupValues if type was String[][]
			if(columnBean.getGroupValues() != null && StringUtil.isNotEmpty(tdValue) && columnBean.getGroupValues() instanceof String[][]){
				for (String[] val : (String[][])columnBean.getGroupValues()) {
					if(val[0] != null && val[0].equals(tdValue)){
						tdValue = val[1];
						break;
					}
				}
			}
			
			if(StringUtil.isFalse(columnBean.getFormatable())){
				sb.append(tdValue);
			} else if(ProjectConstante.TYPE_DATA_ENUM.BOOLEAN.getType().equalsIgnoreCase(type)){
				boolean isTrue = StringUtil.isTrue(tdValue);
				sb.append("<span class='badge badge-"+(isTrue ? "success" : "info") + "'>"+(isTrue ? "Oui":"Non")+"</span>");
			} else if(ProjectConstante.TYPE_DATA_ENUM.DATE.getType().equalsIgnoreCase(type)){
				String dateValue = DateUtil.dateToString((Date)getValue());
				sb.append(StringUtil.getHtmlEmpty(dateValue));
			} else if(ProjectConstante.TYPE_DATA_ENUM.DATE_TIME.getType().equalsIgnoreCase(type)){
				String dateValue = DateUtil.dateTimeToString((Date)getValue());
				sb.append(StringUtil.getHtmlEmpty(dateValue));
			} else if(ProjectConstante.TYPE_DATA_ENUM.INTEGER.getType().equalsIgnoreCase(type)){ 
				Object integerValue = getValue();
				if(integerValue instanceof Integer){
					integerValue = BigDecimalUtil.formatNumber((Integer)integerValue);
				} else if(integerValue instanceof Long){
					integerValue = ((Long)integerValue).intValue();
				} else if(integerValue instanceof BigDecimal){
					integerValue = ((BigDecimal)integerValue).intValue();
				}
				sb.append(StringUtil.getHtmlEmpty(integerValue));
			} else if(ProjectConstante.TYPE_DATA_ENUM.LONG.getType().equalsIgnoreCase(type)){
				Object longValue = getValue();
				if(longValue instanceof BigDecimal){
					//longValue = String.format("%,14.2f", longValue);
					longValue = BigDecimalUtil.formatNumber((BigDecimal)longValue);
				}
				sb.append(StringUtil.getHtmlEmpty(longValue));
			} else if(ProjectConstante.TYPE_DATA_ENUM.DECIMAL.getType().equalsIgnoreCase(type)){
				Object longValue = getValue();
				if(longValue instanceof BigDecimal){
					longValue = BigDecimalUtil.formatNumber((BigDecimal)longValue);// String.format("%,14.2f", longValue) + " "+StrimUtil.getGlobalConfigPropertie("devise.html");
				}
				sb.append(StringUtil.getHtmlEmpty(longValue));
			} else{
					sb.append(tdValue);
			}
		} else{
			sb.append(StringUtil.getHtmlEmpty(tdValue));
		}
		//
		ComponentUtil.writeComponent(pageContext, sb);
	}

	/**
	 *  End
	 */
	public void writeEndComponent() throws JspException {
		StringBuilder sb = new StringBuilder();
		sb.append("</td>\n");

		// Add cell for scroll
		TrTag parentTr = (TrTag) findAncestorWithClass(this, TrTag.class);
		TableTag parentTable = (TableTag) findAncestorWithClass(this, TableTag.class);
		int idxCurrTd = parentTr.getIdxTd();
		int thCount = parentTable.getListTh().size();
		//
		if(idxCurrTd == thCount){
			// Td for scroll
			sb.append("<td width=\"1\">&nbsp;</td>\n");
		}
		//
		ComponentUtil.writeComponent(pageContext, sb);

		//
		releaseTd();
	}
	
	/**
	 * 
	 */
	private void releaseTd() {
		//
		this.align = null;
		//
		super.release();
	}

	/**
	 * @return the align
	 */
	public String getAlign() {
		return align;
	}

	/**
	 * @param align the align to set
	 */
	public void setAlign(String align) {
		this.align = align;
	}
}
