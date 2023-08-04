package framework.component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import framework.controller.ControllerUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;

@SuppressWarnings("serial")
public class Tooltip extends ComponentBase {
	private String sticky;
	private String helpPath;
	private String contentKey;
	private String content;
	private String width;
	private String height;
	private String mouseOutClose;
	private String tracking;
	private String delayedClose;
	private String imgVAlign;
	private String forceShow;

	@Override
	public void doAfterEndComponent() throws JspException {
	}

	@Override
	public void doBeforStartComponent() throws JspException {
	}

	@Override
	public void releaseAll() {
		this.sticky = null;
		this.helpPath = null;
		this.contentKey = null;
		this.content = null;
		this.imgVAlign = null;
		this.width = null;
		this.height = null;
		this.mouseOutClose = null;
		this.tracking = null;
		this.delayedClose = null;
	}

	@Override
	public void writeEndComponent() throws JspException {
	}

	@Override
	public void writeStartComponent() throws JspException {
		HttpServletRequest request = getGuiOrContextHttpRequest();
		if((!ControllerUtil.isEditionWritePage(request) && StringUtil.isFalseOrNull(forceShow)) || (StringUtil.isEmpty(content) && StringUtil.isEmpty(contentKey) &&
			StringUtil.isEmpty(helpPath))){
			return;
		}

		String defaultClass = "link";
		StringBuilder sb = new StringBuilder();
		String currId = ""+new java.util.Random().nextInt(1000);
		String currTitle = getTitleOrTitleKey();

		if(helpPath == null){
			String title = "title=\""+(StringUtil.isNotEmpty(currTitle) ? currTitle+"|" : "")+getContentOrContentKey()+"\"";
			sb.append("<a id=\"" + currId + "_tool\" href=\"#\" "+title+" class=\""+defaultClass+"\">\n");
		} else{
			String title = StringUtil.isNotEmpty(currTitle) ? "title=\""+currTitle+"\"" : "";
			sb.append("<a id=\"" + currId + "_tool\" href=\"#\" rel=\""+helpPath+"\"" + title + " class=\""+defaultClass+"\">\n");
		}
		//
		if((imgVAlign == null) || super.isReadOnlyAttributeForm()){
			imgVAlign = "vertical-align: middle;";
		} else{
			imgVAlign = "vertical-align: "+imgVAlign+";";
		}
		//
		sb.append("<img border=\"0\" src=\"resources/framework/img/table/action/field_help.png\" style=\"  margin-top: -15px;"+imgVAlign+"\">" +
				"</a>\n");
		//
		if(sticky == null){
			sticky = "false";
		}
		//
		StringBuilder jsSb = new StringBuilder();
		jsSb.append(
			"\n$(document).ready(function(){\n" +
			"$(\"#"+currId+"_tool\").cluetip({\n" +
			"cluetipClass: 'jtip',\n" +
			"arrows: true,\n");

			if(StringUtil.isNotEmpty(width)){
				jsSb.append("width: "+this.width+",\n");
			}
			if(StringUtil.isNotEmpty(height)){
				jsSb.append("height: "+this.height+",\n");
			}
			if(StringUtil.isNotEmpty(mouseOutClose)){
				jsSb.append("mouseOutClose:"+this.mouseOutClose+",\n");
			}
			if(StringUtil.isNotEmpty(tracking)){
				jsSb.append("tracking:"+this.tracking+",\n");
			}
			if(StringUtil.isNotEmpty(delayedClose)){
				jsSb.append("delayedClose:"+this.delayedClose+",\n");
			}

		jsSb.append("dropShadow: false,\n" +
			"sticky: "+sticky+",\n" +
			"mouseOutClose: true,\n" +
			"closePosition: 'title',\n" +
			"closeText: '<img src=\"resources/framework/img/table/action/remove.png\" alt=\""+StrimUtil.label("work.short.close")+"\" />',\n" +
			(StringUtil.isEmpty(currTitle) ? "showTitle: false,\n" : "") +
			"hoverIntent: false\n" +
			(StringUtil.isEmpty(helpPath) ? ",splitTitle: '|'" : "") +
		"});\n});\n");

		sb.append(ComponentUtil.getJavascriptBloc(jsSb));

		ComponentUtil.writeComponent(pageContext, sb);
	}

	public String getSticky() {
		return sticky;
	}

	public void setSticky(String sticky) {
		this.sticky = sticky;
	}

	public String getHelpPath() {
		return helpPath;
	}

	public void setHelpPath(String helpPath) {
		this.helpPath = helpPath;
	}

	public String getContentKey() {
		return contentKey;
	}

	public void setContentKey(String contentKey) {
		this.contentKey = contentKey;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImgVAlign() {
		return imgVAlign;
	}

	public void setImgVAlign(String imgVAlign) {
		this.imgVAlign = imgVAlign;
	}

	/**
	 * @return
	 */
	private String getContentOrContentKey(){
		if((this.content == null) && (this.contentKey != null)){
			return StrimUtil.label(contentKey);
		}

		return StringUtil.getValueOrEmpty(this.content);
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getMouseOutClose() {
		return mouseOutClose;
	}

	public void setMouseOutClose(String mouseOutClose) {
		this.mouseOutClose = mouseOutClose;
	}

	public String getTracking() {
		return tracking;
	}

	public void setTracking(String tracking) {
		this.tracking = tracking;
	}

	public String getDelayedClose() {
		return delayedClose;
	}

	public void setDelayedClose(String delayedClose) {
		this.delayedClose = delayedClose;
	}

	public String getForceShow() {
		return forceShow;
	}

	public void setForceShow(String forceShow) {
		this.forceShow = forceShow;
	}

}
