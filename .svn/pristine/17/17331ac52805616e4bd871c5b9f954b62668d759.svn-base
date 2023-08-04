package framework.component;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.StringUtil;

@SuppressWarnings("serial")
public class Ajax extends TagSupport {
	private String event;
	private String target;
	private String isInput;
	private String action;
	private String params;
	private String skipInit;
	private String skipPost;

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		Form parentForm = (Form)findAncestorWithClass(this, Form.class);
		Tag parentComponent = findAncestorWithClass(this, Tag.class);
		//
		if(parentComponent == null){
			throw new JspException("Ajax component must be used in other component");
		}

		String name = ReflectUtil.getStringPropertieValue(parentComponent, "name");
		String jQueryName = ComponentUtil.getJQueryName(name);
		boolean isSkipInit = !StringUtil.isFalse(skipInit);
		boolean isSkipPost = !StringUtil.isFalse(skipPost);

		if(StringUtil.isNotEmpty(action)){
			action = EncryptionUtil.encrypt(action);
		}
		
		// Important "live" permet de reassocier les évenements même après le rechargement des composants
		String ajaxEvent = "$(\"#"+jQueryName+"\").off(\""+event+"\").on(\""+event+"\", function(){\n" +
				"executePartialAjax($(this), '"+action+"', '"+target+"', "+isSkipInit+","+isSkipPost+",'"+params+"', "+StringUtil.isTrue(isInput)+");\n" +
				"});\n";


		parentForm.getSbScript().append(ajaxEvent);

		return SKIP_BODY;	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		releaseAll();

		return SKIP_BODY;
	}

	private void releaseAll(){
		this.event = null;
		this.action = null;
		this.params = null;
		this.target = null;
		this.skipInit = null;
		this.skipPost = null;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getSkipInit() {
		return skipInit;
	}

	public void setSkipInit(String skipInit) {
		this.skipInit = skipInit;
	}

	public String getSkipPost() {
		return skipPost;
	}

	public void setSkipPost(String skipPost) {
		this.skipPost = skipPost;
	}

	public String getIsInput() {
		return isInput;
	}

	public void setIsInput(String isInput) {
		this.isInput = isInput;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}
}
