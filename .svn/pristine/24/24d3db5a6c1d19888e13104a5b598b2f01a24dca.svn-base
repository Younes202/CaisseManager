/**
 *
 */
package framework.component.action;

import javax.servlet.jsp.JspException;

import framework.component.ComponentUtil;
import framework.controller.Context;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.MenuMappingService;

/**
 * @author 
 *
 */
public class LinkPopup extends CompActionBase {
	
	private String madalTarger;
	private String noJsValidate;
	
	/**
	 *
	 */
	@Override
	public void doBeforStartComponent() throws JspException {

	}

	/* (non-Javadoc)
	 * @see front.component.Component#releaseAll()
	 */
	@Override
	public void releaseAll() {
		super.releaseAll();
		this.madalTarger = null;
	}

	/* (non-Javadoc)
	 * @see front.component.Component#writeEndComponent()
	 */
	@Override
	public void writeEndComponent() throws JspException {
		if("D".equals(getActionGroup()) && !Context.isOperationAvailable(MenuMappingService.RIGHT_ENUM.RIGHT_DELETE)){
			return;
		} else if("U".equals(getActionGroup()) && !Context.isOperationAvailable(MenuMappingService.RIGHT_ENUM.RIGHT_UPDATE)){
			return;
		} else if("C".equals(getActionGroup()) && !Context.isOperationAvailable(MenuMappingService.RIGHT_ENUM.RIGHT_CREATE)){
			return;
		}
		
		if("U".equals(getActionGroup()) && StringUtil.isEmpty(getWorkId()) && StringUtil.isFalseOrNull(getForceShow())){
			return;
		}
		
		ComponentUtil.writeComponent(pageContext, "</a>");
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
	@Override
	public void writeStartComponent() throws JspException {
		if("D".equals(getActionGroup()) && !Context.isOperationAvailable(MenuMappingService.RIGHT_ENUM.RIGHT_DELETE)){
			return;
		} else if("U".equals(getActionGroup()) && !Context.isOperationAvailable(MenuMappingService.RIGHT_ENUM.RIGHT_UPDATE)){
			return;
		} else if("C".equals(getActionGroup()) && !Context.isOperationAvailable(MenuMappingService.RIGHT_ENUM.RIGHT_CREATE)){
			return;
		}

		if("U".equals(getActionGroup()) && StringUtil.isEmpty(getWorkId()) && StringUtil.isFalseOrNull(getForceShow())){
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		// Default class
		String defaultClass = "btn btn-default crudpop";
		String params = StringUtil.getValueOrEmpty(this.getParams());
		
		if(StringUtil.isNotEmpty(getWorkId())){
			params = params + (StringUtil.isEmpty(params)?"":"&") + ProjectConstante.WORK_ID+"="+EncryptionUtil.encrypt(getWorkId());
		}
		
		if(StringUtil.isEmpty(this.madalTarger)){
			this.madalTarger = "generic_modal";
		}
		
		String onCompleteJs = "";
		if(StringUtil.isNotEmpty(super.getOnComplete())){
			onCompleteJs = " onComplete=\""+super.getOnComplete()+"\"";
		}
		
		String targetDiv = "";
		if(StringUtil.isEmpty(getTargetDiv())){
			targetDiv = " targetDiv=\"generic_modal_body\"";
		}
		
		String noval = "";
		if(StringUtil.isNotEmpty(noJsValidate)){
			noval = " noval=\"true\"";
		}

		// Form name
		sb.append("<a href=\"javascript:\" wact=\""+EncryptionUtil.encrypt(this.getAction())+"\" "+targetDiv + onCompleteJs + " params=\""+params+"\""+getFullComponentAttrubutes(defaultClass)+" data-backdrop=\"static\" data-keyboard=\"false\"  data-toggle=\"modal\" "+noval+" data-target=\"#"+this.madalTarger+"\">");
		if(StringUtil.isNotEmpty(getIcon())){
			sb.append("<i class=\"fa "+StringUtil.getValueOrEmpty(getIcon())+"\"> </i> ");
		}
		sb.append(getValueOrValueKey());
		
		// Write result
		ComponentUtil.writeComponent(pageContext, sb.toString());
	}

	public String getMadalTarger() {
		return madalTarger;
	}

	public void setMadalTarger(String madalTarger) {
		this.madalTarger = madalTarger;
	}

	public String getNoJsValidate() {
		return noJsValidate;
	}

	public void setNoJsValidate(String noJsValidate) {
		this.noJsValidate = noJsValidate;
	}
}
