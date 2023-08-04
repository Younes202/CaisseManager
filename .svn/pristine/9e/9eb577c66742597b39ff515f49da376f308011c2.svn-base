/**
 *
 */
package framework.component.text;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import framework.component.ComponentUtil;
import framework.component.text.plugin.MaskPlugin;
import framework.model.common.util.DateUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;

/**
 * @author 
 *
 */
@SuppressWarnings("serial")
public class Date extends TextBase {
	/**
	 *
	 */
	private String picto;
	private String pattern;
	/**
	 * 	Restrict the range of selectable dates
	 * with the minDate and maxDate options. Set the beginning and end dates as actual dates
	 * (new Date(2009, 1 - 1, 26)), as a numeric offset from today (-20), or as a string of periods
	 * and units ('+1M +10D'). For the last, use 'D' for days, 'W' for weeks, 'M' for months, or 'Y' for years.
	 */
	private String dateMin;
	private String dateMax;

	/* (non-Javadoc)
	 * @see front.component.Component#doBeforStartComponent()
	 */
	@Override
	public void doBeforStartComponent() throws JspException {
		setType("date");
	}

	/* (non-Javadoc)
	 * @see front.component.Component#writeStartComponent()
	 */
	public void writeStartComponent() throws JspTagException{

	}

	/* (non-Javadoc)
	 * @see front.component.ComponentBase#doAfterEndComponent()
	 */
	@Override
	public void doAfterEndComponent() throws JspException {

	}

	/**
	 * @throws JspTagException
	 *
	 */
	public void writeEndComponent() throws JspTagException {
		// Pattern
		String patternSt = ((pattern == null) ? StrimUtil.getGlobalConfigPropertie("ddMMyyyy.format") : pattern);
		//
		if (getValue() instanceof java.util.Date) {
			java.util.Date date = (java.util.Date)getValue();
			String dateToString = DateUtil.dateToString(date, patternSt);
			setValue(dateToString);
		}
		//
		boolean isReadOnly = super.isReadOnlyAttributeForm();
		if(isReadOnly){
			this.setReadOnly("true");
			this.appendStyle("background-color:#eeeeee;");
		}
		

		StringBuilder sb = new StringBuilder();
//		HttpServletRequest request = getGuiOrContextHttpRequest();
		//
		if(getId() == null){
			super.setId(super.getName());
		}
		//
		String maskPattern = patternSt.replaceAll("[A-Z,a-z]", "9");
		String mascSkript = MaskPlugin.getMaskScript(getJQueryId(), maskPattern);
		sb.append(ComponentUtil.getJavascriptBloc(mascSkript + "\n"));
		
		if(isReadOnly){
			this.setPlaceholder(null);
			this.setPlaceholderKey(null);
			this.appendStyle("width: 160px;");
			sb.append("<input type='text'" + getFullComponentAttrubutes("form-control")+getFullTextAttrubutes()+ getFullValue() + "/>\n");
		} else{
			setStyle(StringUtil.getValueOrEmpty(getStyle())+";width: 112px;");
			sb.append("<div class=\"input-group\" style='width: 150px;'>"
					+ "<input type=\"text\" "+getFullComponentAttrubutes("form-control date-picker")+getFullTextAttrubutes()+ getFullValue()+" data-dateformat=\"dd/mm/yy\">"+
			"<span class=\"input-group-addon\"><i class=\"fa fa-calendar\"></i></span>"
			+ "</div>");
		}
		
		// Write result
		ComponentUtil.writeComponent(getContextOrJspContext(), sb);
	}


	/* (non-Javadoc)
	 * @see front.component.Component#releaseAll()
	 */
	@Override
	public void releaseAll() {
		super.releaseAttr();
		picto = null;
		pattern = null;
		dateMin = null;
		dateMax = null;
	}

	/*
	 * \------------------------------------End Bolc writer-----------------------------\
	 */

	/**
	 * @return the picto
	 */
	public String getPicto() {
		return picto;
	}

	/**
	 * @param picto
	 *            the picto to set
	 */
	public void setPicto(String picto) {
		this.picto = picto;
	}

	/**
	 * @return the pattern
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getDateMin() {
		return dateMin;
	}

	public void setDateMin(String dateMin) {
		this.dateMin = dateMin;
	}

	public String getDateMax() {
		return dateMax;
	}

	public void setDateMax(String dateMax) {
		this.dateMax = dateMax;
	}
}
