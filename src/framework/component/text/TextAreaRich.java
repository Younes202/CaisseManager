/**
 *
 */
package framework.component.text;

import javax.servlet.jsp.JspException;

import framework.component.ComponentBase;
import framework.component.ComponentUtil;
import framework.model.common.util.StringUtil;

/**
 * @author 
 *
 */
public class TextAreaRich extends ComponentBase {

	/**
	 *
	 */
	private static final long serialVersionUID = -3226046381606266358L;
	private String width;
	private String height;

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
		height = null;
		width = null;
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
		boolean isReadOnly = super.isReadOnlyAttributeForm();
		String defaultClass = "areatext";

		// Add id
		if(getId() == null){
			super.setId(super.getName());
		}

		if(this.width == null){
			this.width = "100%";
		}
		if(this.height == null){
			this.height = "150";
		}
		
		if(isReadOnly){
			if(StringUtil.isNotEmpty(getValueSt())){
				sb.append("<div style=\"max-height:"+height+"px;width:"+width+";overflow-y: auto;"+getStyle()+"\" class=\"divSelect\">"
						+ getValueSt() +
					"</div>");
			}
		} else{
			sb.append("<textarea " + getFullComponentAttrubutes(defaultClass)+ ">"
					+ getValueSt() +
				"</textarea>");
		}
		
		StringBuilder jsSb = new StringBuilder();
		jsSb.append("$('#"+getJQueryName()+"').summernote({ width: '"+width+"', height: '"+height+"' });");
		
		jsSb.append("$('.note-editable').on('blur', function(){" +
        	"var editor = $(this).closest('.note-editor').siblings('textarea');" +
        	"editor.html(editor.code());" +
        "});");
		
		sb.append(ComponentUtil.getJavascriptOnReadyBloc(jsSb.toString()));

		// Write result
		ComponentUtil.writeComponent(pageContext, sb.toString());
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
}
