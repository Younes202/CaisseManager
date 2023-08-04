/**
 *
 */
package framework.component.action;

import javax.servlet.jsp.JspException;

import framework.component.ComponentUtil;
import framework.model.common.constante.ActionConstante;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.StringUtil;

/**
 * @author 
 *
 */
public class Button extends CompActionBase {
	private String noJsValidate;
	private String closeOnSubmit;
	
	@Override
	public void doBeforStartComponent() throws JspException {

	}

	@Override
	public void releaseAll() {
		super.releaseAll();
	}

	/* (non-Javadoc)
	 * @see front.component.Component#writeEndComponent()
	 */
	public void writeEndComponent() throws JspException {

		ComponentUtil.writeComponent(pageContext, "</button>");
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
		// Si form lecture seule ou suppression avec id null alors retour
		String actionGroup = getActionGroup();
		
		if(StringUtil.isNotEmpty(getActionGroup())){
			if((super.isReadOnlyAttributeForm() && (ActionConstante.GROUP_DELETE.equals(actionGroup) || ActionConstante.GROUP_MERGE.equals(actionGroup)) 
					|| (ActionConstante.GROUP_DELETE.equals(actionGroup)) && StringUtil.isEmpty(getWorkId()))){
				return;
			}
		}

		StringBuilder sb = new StringBuilder();
		// Default class
		String defaultClass = "btn btn-default";
		String params = this.getParams()+"&bck=1";
		
		if(StringUtil.isNotEmpty(getWorkId())){
			params = (StringUtil.isEmpty(params)?"":params+"&") + ProjectConstante.WORK_ID+"="+EncryptionUtil.encrypt(getWorkId());
		}
		String onCompleteJs = "";
		if(StringUtil.isNotEmpty(super.getOnComplete())){
			onCompleteJs = " onComplete=\""+super.getOnComplete()+"\"";
		}
		String targetDiv = "";
		//
		if(StringUtil.isNotEmpty(getTargetDiv())){
			targetDiv = " targetDiv=\""+getTargetDiv()+"\"";
		}
		
		// Form name
		actionGroup = (StringUtil.isEmpty(this.getActionGroup()) ? "" : " targetBtn=\""+this.getActionGroup()+"\"");
		String action = StringUtil.isEmpty(this.getAction()) ? "" : " wact=\""+EncryptionUtil.encrypt(this.getAction())+"\"";
		params = StringUtil.isEmpty(params) ? "" : " params=\""+params+"\"";
		
		String jsValidate = "";
		if(StringUtil.isNotEmpty(noJsValidate)){
			jsValidate = " noVal=\""+noJsValidate+"\"";
		}
		String closepop = "";
		if(StringUtil.isTrue(closeOnSubmit)){
			closepop = " closepop=\"true\"";
		}
		
		// Build component
		sb.append("<button type=\"button\" " + actionGroup + targetDiv + onCompleteJs + closepop + getFullComponentAttrubutes(defaultClass) + action + params + jsValidate + ">");
		if(StringUtil.isNotEmpty(getIcon())){
			sb.append("<i class=\"fa "+StringUtil.getValueOrEmpty(getIcon())+"\"> </i> ");
		}
		sb.append(getValueOrValueKey());

		// Write result
		ComponentUtil.writeComponent(pageContext, sb.toString());
	}
	
	public String getNoJsValidate() {
		return noJsValidate;
	}

	public void setNoJsValidate(String noJsValidate) {
		this.noJsValidate = noJsValidate;
	}
	public String getCloseOnSubmit() {
		return closeOnSubmit;
	}

	public void setCloseOnSubmit(String closeOnSubmit) {
		this.closeOnSubmit = closeOnSubmit;
	}
}