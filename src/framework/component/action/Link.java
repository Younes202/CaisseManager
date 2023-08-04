/**
 *
 */
package framework.component.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import framework.component.ComponentUtil;
import framework.controller.Context;
import framework.controller.ControllerUtil;
import framework.model.common.constante.ActionConstante;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.MenuMappingService;

/**
 * @author 
 *
 */
public class Link extends CompActionBase {
	private String noJsValidate;
	private String target;
	private String closeOnSubmit;
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
		this.noJsValidate = null;
		this.target = null;
		this.closeOnSubmit = null;
		super.releaseAll();
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

		HttpServletRequest request = ((HttpServletRequest) pageContext.getRequest());
		
		// Si form lecture seule ou suppression avec id null alors retour
		if(StringUtil.isNotEmpty(getActionGroup()) && StringUtil.isFalseOrNull(getForceShow())){
			boolean isFormWrite = ControllerUtil.isEditionWritePage(request);
			String actionGroup = getActionGroup();
			boolean isFormReadOnly = ComponentUtil.isReadOnlyFormSetted(request);
			
			if(ActionConstante.GROUP_DELETE.equals(actionGroup) 
					|| ActionConstante.GROUP_UPDATE.equals(actionGroup) 
					|| ActionConstante.GROUP_DUPLIC.equals(actionGroup)){
				if((!ActionConstante.GROUP_DUPLIC.equals(actionGroup) && isFormWrite) || isFormReadOnly || StringUtil.isEmpty(getWorkId())){
					return;
				}
			}
			
			if(ActionConstante.GROUP_MERGE.equals(actionGroup) && !isFormWrite){
				return;
			}
			
			if(ActionConstante.GROUP_CREATE.equals(actionGroup) && isFormReadOnly){
				return;
			}
					
		}
		
		StringBuilder sb = new StringBuilder();
		// Default class
		String defaultClass = "btn btn-default";
		String params = StringUtil.getValueOrEmpty(this.getParams());
		String targetDiv = "";
		
		String onCompleteJs = "";
		if(StringUtil.isNotEmpty(super.getOnComplete())){
			onCompleteJs = " onComplete=\""+super.getOnComplete()+"\"";
		}
		
		String currTarget="";
		if(StringUtil.isNotEmpty(target)){
			currTarget = " target=\""+target+"\"";
		}
		String closepop = "";
		if(StringUtil.isTrue(closeOnSubmit)){
			closepop = " closepop=\"true\"";
		}
		//
		if(StringUtil.isNotEmpty(getTargetDiv())){
			targetDiv = " targetDiv=\""+getTargetDiv()+"\"";
		}
		
		if(StringUtil.isNotEmpty(getWorkId())){
			params = params + (StringUtil.isEmpty(params)?"":"&") + ProjectConstante.WORK_ID+"="+EncryptionUtil.encrypt(getWorkId());
		}
		
		// Build component
		
		if(StringUtil.isEmpty(target)){
			// Form name
			String actionGroup = (StringUtil.isEmpty(this.getActionGroup()) ? "" : " targetBtn=\""+this.getActionGroup()+"\"");
			params = StringUtil.isEmpty(params) ? "" : " params=\""+params+"\"";
			
			String jsValidate = "";
			if(StringUtil.isNotEmpty(noJsValidate)){
				jsValidate = " noVal=\""+noJsValidate+"\"";
			}
			String action = StringUtil.isEmpty(this.getAction()) ? "" : " wact=\""+EncryptionUtil.encrypt(this.getAction())+"\"";
			sb.append("<a href=\"javascript:void(0);\"" + actionGroup + targetDiv + onCompleteJs + closepop + getFullComponentAttrubutes(defaultClass) + action + params + jsValidate + ">");
		} else{// Cas téléchargement
			String action = "front?"+ProjectConstante.WORK_FORM_ACTION + "=" + EncryptionUtil.encrypt(this.getAction())+"&"+params;
			sb.append("<a href=\""+action+"\"" + getFullComponentAttrubutes(defaultClass) + currTarget + ">");
		}
		
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

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getCloseOnSubmit() {
		return closeOnSubmit;
	}

	public void setCloseOnSubmit(String closeOnSubmit) {
		this.closeOnSubmit = closeOnSubmit;
	}
}
