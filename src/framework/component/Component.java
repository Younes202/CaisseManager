package framework.component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import framework.component.complex.table.TdTag;
import framework.component.complex.table.ThTag;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;

@SuppressWarnings("serial")
public abstract class Component extends TagSupport implements IComponent { 
	private String id;
	private String name;
	private String disable;
	private String readOnly;
	private String forceWriten;
	private String tooltip;
	private String tooltipKey;
	private String title;
	private String titleKey;
	private String classStyle;
	private String style;
	private Object value;
	private String valueKey;
	/* \----------------Events------------------\ */
	private String onClick;
	private String onBlur;
	private String onChange;
	private String onDbClick;
	private String onFocus;
	private String onSelect;
	/* \----------------Key------------------\ */
	private String onKeyDown;
	private String onKeyPress;
	private String onKeyUp;
	/* \----------------Mouse------------------\ */
	private String onMouseDown;
	private String onMouseMove;
	private String onMouseOut;
	private String onMouseOver;
	private String onMouseUp;
	/* \----------------Added------------------\ */
	private String validator;
	private String type;
	private String required;
	private String max;
	private String min;
	private String minlength;
	private String maxlength;
	private String visible;
	private String noDisable;
	private String autoValue;
	private String iskey;

	// For managing in HttpGuiUtil
	HttpServletRequest requestGui;
	PageContext jspPageContext;
	private boolean isGuiManage;

	/**
	 * @param requestGui
	 */
	public void setRequestGui(HttpServletRequest requestGui) {
		this.requestGui = requestGui;
	}

	/**
	 * @return
	 */
	public HttpServletRequest getRequestGui() {
		return this.requestGui;
	}

	/**
	 * @return
	 */
	public HttpServletRequest getGuiOrContextHttpRequest(){
		if((pageContext != null) && (pageContext.getRequest() != null)){
			return ((HttpServletRequest) pageContext.getRequest());
		} else{
			return getRequestGui();
		}
	}

	/**
	 * @return
	 */
	protected PageContext getContextOrJspContext(){
		return (pageContext != null) ? pageContext : jspPageContext;
	}

	/**
	 * @param defaultClass
	 * @return
	 */
	protected String getFullComponentAttrubutes(String defaultClass) {
		// Build result
		String result = getFullId() + getFullName() + getFullClassStyle(defaultClass) + getFullDisable() + getFullNoDisable() + getFullVisible()
				+ getFullReadOnly() + getFullOnBlur() + getFullOnChange() + getFullOnClick() + getFullOnDbClick() + getFullOnFocus() + getFullOnKeyDown()
				+ getFullOnKeyPress() + getFullOnKeyUp() + getFullOnMouseDown() + getFullOnMouseMove() + getFullOnMouseOut() + getFullOnMouseUp()
				+ getFullTooltip() + getFullStyle();

		return result;
	}

	public boolean isReadOnlyAttributeForm(){
		return !StringUtil.isTrue(getForceWriten()) && ComponentUtil.isReadOnlyAttributeForm(getGuiOrContextHttpRequest());
	}

	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#release()
	 */
	public void release(){
		super.release();
		//
		this.id = null;
		this.name = null;
		this.disable = null;
		this.readOnly = null;
		this.classStyle = null;
		this.style = null;
		this.value = null;
		this.valueKey = null;
		this.tooltip = null;
		this.tooltipKey = null;
		this.title = null;
		this.titleKey = null;
		/* \----------------Events------------------\ */
		this.onClick = null;
		this.onBlur = null;
		this.onChange = null;
		this.onDbClick = null;
		this.onFocus = null;
		this.onSelect = null;
		/* \----------------Key------------------\ */
		this.onKeyDown = null;
		this.onKeyPress = null;
		this.onKeyUp = null;
		/* \----------------Mouse------------------\ */
		this.onMouseDown = null;
		this.onMouseMove = null;
		this.onMouseOut = null;
		this.onMouseOver = null;
		this.onMouseUp = null;
		/* \----------------Added------------------\ */
		this.validator = null;
		this.required = null;
		this.max = null;
		this.min = null;
		this.visible = null;
		this.autoValue = null;
		this.type = null;
		this.minlength = null;
		this.maxlength = null;
		this.noDisable = null;
		// For managing in HttpGuiUtil
		this.requestGui = null;
		this.iskey = null;
		this.forceWriten = null;
	}

	/*
	 * \-----------------------------------Getter and Setter---------------------------------\
	 */

	/**
	 * @return
	 */
	public Object getFullValue() {
		return " value=\"" + StringUtil.getValueOrEmpty(this.value)+ "\"";
	}

	public Object getValue() {
		return this.value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getValueOrValueKey(){
		if((this.value == null) && (this.valueKey != null)){
			return StrimUtil.label(getValueKey());
		}

		return StringUtil.getValueOrEmpty(this.value);
	}

	public String getValueSt() {
		return StringUtil.getValueOrEmpty(this.value);
	}

	/**
	 * @param defaultClass
	 * @return
	 */
	protected String getFullClassStyle(String defaultClass) {
		if (this.classStyle != null){
			return " class=\"" + this.classStyle+ "\"";
		} else if (!StringUtil.isEmpty(defaultClass)) {
			return " class=\"" + defaultClass+ "\"";
		} else {
			return "";
		}
	}

	protected String getClassStyle() {
		return this.classStyle;
	}

	/**
	 * @return
	 */
	protected String getFullStyle() {
		if (this.style != null){
			return " style=\"" + this.style+ "\"";
		} else {
			return "";
		}
	}

	protected String getStyle() {
		return this.style;
	}

	/**
	 * @return
	 */
	public String getId(){
		return this.id;
	}

	/**
	 * @return the id
	 */
	public String getFullId() {
		if (this.id != null){
			return " id=\"" + this.id+ "\"";
		} else {
			return "";
		}
	}

	/**
	 * @param id
	 *            the id to set
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getFullName() {
		if (this.name != null){
			return " name=\"" + this.name+ "\"";
		} else {
			return "";
		}
	}

	/**
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	public String getJQueryName(){
		return ComponentUtil.replaceAll(this.name, '.', "\\\\.");
	}
	public String getJQueryId(){
		return ComponentUtil.replaceAll(this.id, '.', "\\\\.");
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param classStyle
	 *            the classStyle to set
	 */
	public void setClassStyle(String classStyle) {
		this.classStyle = classStyle;
	}

	/**
	 * @param style
	 *            the style to set
	 */
	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * @param style
	 */
	public void appendStyle(String style) {
		this.style = StringUtil.getValueOrEmpty(this.style) + style;
	}

	/**
	 * @return the disable
	 */
	public String getFullDisable() {
		if (StringUtil.isTrue(this.disable)){
			return " disabled=\"disabled\"";
		} else {
			return "";
		}
	}

	public String getDisable() {
		return this.disable;
	}

	/**
	 * @param disable
	 *            the disable to set
	 */
	public void setDisable(String disable) {
		this.disable = disable;
	}

	/**
	 * @return the readOnly
	 */
	public String getFullReadOnly() {
		if (this.readOnly != null){
			if(StringUtil.isFalse(this.readOnly)){
				return "";
			}
			return " readonly=\"" + this.readOnly+ "\"";
		} else {
			return "";
		}
	}

	public String getReadOnly() {
		return this.readOnly;
	}

	/**
	 * @param readOnly
	 *            the readOnly to set
	 */
	public void setReadOnly(String readOnly) {
		this.readOnly = readOnly;
	}

	/**
	 * @return the onClick
	 */
	public String getFullOnClick() {
		if (this.onClick != null){
			return " onClick=\"" + this.onClick + "\"";
		} else {
			return "";
		}
	}

	public String getOnClick() {
		return this.onClick;
	}

	/**
	 * @param onClick
	 *            the onClick to set
	 */
	public void setOnClick(String onClick) {
		this.onClick = onClick;
	}

	/**
	 * @return the onBlur
	 */
	public String getFullOnBlur() {
		if (this.onBlur != null){
			return " onBlur=\"" + this.onBlur + "\"";
		} else {
			return "";
		}
	}

	public String getOnBlur() {
		return this.onBlur;
	}

	/**
	 * @param onBlur
	 *            the onBlur to set
	 */
	public void setOnBlur(String onBlur) {
		this.onBlur = onBlur;
	}

	/**
	 * @return the onChange
	 */
	public String getFullOnChange() {
		if (this.onChange != null){
			return " onChange=\"" + this.onChange + "\"";
		} else {
			return "";
		}
	}

	public String getOnChange() {
		return this.onChange;
	}

	/**
	 * @param onChange
	 *            the onChange to set
	 */
	public void setOnChange(String onChange) {
		this.onChange = onChange;
	}

	/**
	 * @return the onDbClick
	 */
	public String getFullOnDbClick() {
		if (this.onDbClick != null){
			return " onDbClick=\"" + this.onDbClick + "\"";
		} else {
			return "";
		}
	}

	public String getOnDbClick() {
		return this.onDbClick;
	}

	/**
	 * @param onDbClick
	 *            the onDbClick to set
	 */
	public void setOnDbClick(String onDbClick) {
		this.onDbClick = onDbClick;
	}

	/**
	 * @return the onFocus
	 */
	public String getFullOnFocus() {
		if (this.onFocus != null){
			return " onFocus=\"" + this.onFocus + "\"";
		} else {
			return "";
		}
	}

	public String getOnFocus() {
		return this.onFocus;
	}

	/**
	 * @param onFocus
	 *            the onFocus to set
	 */
	public void setOnFocus(String onFocus) {
		this.onFocus = onFocus;
	}

	/**
	 * @return the onSelect
	 */
	public String getFullOnSelect() {
		if (this.onSelect != null) {
			return " onSelect=\"" + this.onSelect + "\"";
		} else {
			return "";
		}
	}

	public String getOnSelect() {
		return this.onSelect;
	}

	/**
	 * @param onSelect
	 *            the onSelect to set
	 */
	public void setOnSelect(String onSelect) {
		this.onSelect = onSelect;
	}

	/**
	 * @return the onKeyDown
	 */
	public String getFullOnKeyDown() {
		if (this.onKeyDown != null){
			return " onKeyDown=\"" + this.onKeyDown + "\"";
		} else {
			return "";
		}
	}

	public String getOnKeyDown() {
		return this.onKeyDown;
	}

	/**
	 * @param onKeyDown
	 *            the onKeyDown to set
	 */
	public void setOnKeyDown(String onKeyDown) {
		this.onKeyDown = onKeyDown;
	}

	/**
	 * @return the onKeyPress
	 */
	public String getFullOnKeyPress() {
		if (this.onKeyPress != null){
			return " onKeyPress=\"" + this.onKeyPress + "\"";
		} else {
			return "";
		}
	}

	public String getOnKeyPress() {
		return this.onKeyPress;
	}

	/**
	 * @param onKeyPress
	 *            the onKeyPress to set
	 */
	public void setOnKeyPress(String onKeyPress) {
		this.onKeyPress = onKeyPress;
	}

	/**
	 * @return the onKeyUp
	 */
	public String getFullOnKeyUp() {
		if (this.onKeyUp != null){
			return " onKeyUp=\"" + this.onKeyUp + "\"";
		} else {
			return "";
		}
	}

	public String getOnKeyUp() {
		return this.onKeyUp;
	}

	/**
	 * @param onKeyUp
	 *            the onKeyUp to set
	 */
	public void setOnKeyUp(String onKeyUp) {
		this.onKeyUp = onKeyUp;
	}

	/**
	 * @return the onMouseDown
	 */
	public String getFullOnMouseDown() {
		if (this.onMouseDown != null){
			return " onMouseDown=\"" + this.onMouseDown + "\"";
		} else {
			return "";
		}
	}

	public String getOnMouseDown() {
		return this.onMouseDown;
	}

	/**
	 * @param onMouseDown
	 *            the onMouseDown to set
	 */
	public void setOnMouseDown(String onMouseDown) {
		this.onMouseDown = onMouseDown;
	}

	/**
	 * @return the onMouseMove
	 */
	public String getFullOnMouseMove() {
		if (this.onMouseMove != null){
			return " onMouseMove=\"" + this.onMouseMove + "\"";
		} else {
			return "";
		}
	}

	public String getOnMouseMove() {
		return this.onMouseMove;
	}

	/**
	 * @param onMouseMove
	 *            the onMouseMove to set
	 */
	public void setOnMouseMove(String onMouseMove) {
		this.onMouseMove = onMouseMove;
	}

	/**
	 * @return the onMouseOut
	 */
	public String getFullOnMouseOut() {
		if (this.onMouseOut != null){
			return " onMouseOut=\"" + this.onMouseOut + "\"";
		} else {
			return "";
		}
	}

	public String getOnMouseOut() {
		return this.onMouseOut;
	}

	/**
	 * @param onMouseOut
	 *            the onMouseOut to set
	 */
	public void setOnMouseOut(String onMouseOut) {
		this.onMouseOut = onMouseOut;
	}

	/**
	 * @return the onMouseOver
	 */
	public String getFullOnMouseOver() {
		if (this.onMouseOver != null) {
			return " onMouseOver=\"" + this.onMouseOver + "\"";
		} else {
			return "";
		}
	}

	public String getOnMouseOver() {
		return this.onMouseOver;
	}

	/**
	 * @param onMouseOver
	 *            the onMouseOver to set
	 */
	public void setOnMouseOver(String onMouseOver) {
		this.onMouseOver = onMouseOver;
	}

	/**
	 * @return the onMouseUp
	 */
	public String getFullOnMouseUp() {
		if (this.onMouseUp != null){
			return " onMouseUp=\"" + this.onMouseUp + "\"";
		} else {
			return "";
		}
	}

	public String getOnMouseUp() {
		return this.onMouseUp;
	}

	/**
	 * @param onMouseUp
	 *            the onMouseUp to set
	 */
	public void setOnMouseUp(String onMouseUp) {
		this.onMouseUp = onMouseUp;
	}

	/**
	 * @return the validator
	 */
	public String getValidator() {
		return validator;
	}

	/**
	 * @param validator
	 *            the validator to set
	 */
	public void setValidator(String validator) {
		this.validator = validator;
	}

	/**
	 * @return the required
	 */
	public String getRequired() {
		return required;
	}

	/**
	 * @param required
	 *            the required to set
	 */
	public void setRequired(String required) {
		this.required = required;
	}

	/**
	 * @return the max
	 */
	public String getMax() {
		return max;
	}

	/**
	 * @param max
	 *            the max to set
	 */
	public void setMax(String max) {
		this.max = max;
	}

	/**
	 * @return the min
	 */
	public String getMin() {
		return min;
	}

	/**
	 * @param min
	 *            the min to set
	 */
	public void setMin(String min) {
		this.min = min;
	}

	/**
	 * @return the visible
	 */
	public String getFullVisible() {
		if (this.visible != null){
			if(StringUtil.isFalse(this.visible)){
				appendStyle(" display: none;");
			}
		}

		return "";
	}

	/**
	 * @return the visible
	 */
	public String getVisible() {
		return visible;
	}

	/**
	 * @param visible the visible to set
	 */
	public void setVisible(String visible) {
		this.visible = visible;
	}

	/**
	 * @return the valueKey
	 */
	public String getValueKey() {
		return valueKey;
	}

	/**
	 * @param valueKey the valueKey to set
	 */
	public void setValueKey(String valueKey) {
		this.valueKey = valueKey;
	}

	public String getFullNoDisable() {
		if(StringUtil.isTrue(noDisable)){
			return " noDisable='true'";
		}

		return "";
	}

	public void setNoDisable(String noDisable) {
		this.noDisable = noDisable;
	}

	public boolean isGuiManage() {
		return isGuiManage;
	}

	/**
	 * @return the autoValue
	 */
	public String getAutoValue() {
		return autoValue;
	}

	/**
	 * @param autoValue the autoValue to set
	 */
	public void setAutoValue(String autoValue) {
		this.autoValue = autoValue;
	}

	/**
	 * @return the iskey
	 */
	public String getIskey() {
		return iskey;
	}

	/**
	 * @param iskey
	 *            the iskey to set
	 */
	public void setIskey(String iskey) {
		this.iskey = iskey;
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

	/**
	 * @return the tooltip
	 */
	public String getFullTooltip() {
		if(ComponentUtil.isLinkField(this) || this instanceof TdTag   || this instanceof ThTag){
			String tool = getTooltipOrTooltipeKey();
			if(StringUtil.isNotEmpty(tool)){
				return " title=\"" + tool + "\"";
			}
		}

		return "";
	}

	public String getTooltip() {
		return this.tooltip;
	}

	/**
	 * @param tooltip the tooltip to set
	 */
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	/**
	 * @param tooltipKey the tooltipKey to set
	 */
	public void setTooltipKey(String tooltipKey) {
		this.tooltipKey = tooltipKey;
	}

	/**
	 * @return
	 */
	public String getTooltipOrTooltipeKey(){
		if((this.tooltip == null) && (this.tooltipKey != null)){
			return StrimUtil.label(tooltipKey);
		}

		return StringUtil.getValueOrEmpty(this.tooltip);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleOrTitleKey(){
		if((this.title == null) && (this.titleKey != null)){
			return StrimUtil.label(titleKey);
		}

		return StringUtil.getValueOrEmpty(this.title);
	}

	public String getTitleKey() {
		return titleKey;
	}

	public void setTitleKey(String titleKey) {
		this.titleKey = titleKey;
	}

	public String getTooltipKey() {
		return tooltipKey;
	}
	/**
	 * @param jspPageContext the jspPageContext to set
	 */
	public void setJspPageContext(PageContext jspPageContext) {
		this.jspPageContext = jspPageContext;
	}


	/**
	 * @return the minlength
	 */
	public String getMinlength() {
		return minlength;
	}

	/**
	 * @param minlength the minlength to set
	 */
	public void setMinlength(String minlength) {
		this.minlength = minlength;
	}

	/**
	 * @return the maxlength
	 */
	public String getMaxlength() {
		return maxlength;
	}

	/**
	 * @param maxlength the maxlength to set
	 */
	public void setMaxlength(String maxlength) {
		this.maxlength = maxlength;
	}

	public String getForceWriten() {
		return forceWriten;
	}

	public void setForceWriten(String forceWriten) {
		this.forceWriten = forceWriten;
	}

}
