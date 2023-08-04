/**
 *
 */
package framework.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import framework.component.complex.table.export.ExportTableBean;
import framework.component.form.pluging.FormValidator;
import framework.controller.ControllerUtil;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.service.FrameworkMessageService;
import framework.model.common.service.MessageService;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.StringUtil;

/**
 * @author 
 *
 */
public class Form extends Component {
	/**
	 *
	 */
	private static final long serialVersionUID = 7222361043691579644L;
	private StringBuilder sbScript;

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest request = ((HttpServletRequest) pageContext.getRequest());
//		sbComponents = new StringBuilder();
		sbScript = new StringBuilder();
		
		if(StringUtil.isEmpty(this.getId())){
			this.setId(this.getName());
		}

		// Send redonly mode to components
		if(ComponentUtil.isReadOnlyAction(request)){
			request.setAttribute(ProjectConstante.IS_READ_ONLY_FORM, "true");
		}
		//
		if(!ComponentUtil.isBodyTableAction(pageContext)){
			writeStartComponent();
		}

		return EVAL_PAGE;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		HttpServletRequest request = ((HttpServletRequest) pageContext.getRequest());
		//
		if(!ComponentUtil.isBodyTableAction(pageContext)){
			writeEndComponent();
		}
		// Remove all components
		ControllerUtil.removeMenuAttribute(ProjectConstante.MAP_COMPONENT, request);
		// Purge des messages
		ControllerUtil.cleanAll(request);
		//
		super.release();
		//
		sbScript = null;

		return EVAL_PAGE;
	}

	/**
	 * @throws JspException
	 */
	public void writeStartComponent() throws JspException {
		StringBuilder sb = new StringBuilder();
		HttpServletRequest request = ((HttpServletRequest) pageContext.getRequest());
		
		if(StringUtil.isNotEmpty(request.getParameter(ProjectConstante.WORK_FORM_ACTION))){
			request.setAttribute(ProjectConstante.WORK_FORM_ACTION, request.getParameter(ProjectConstante.WORK_FORM_ACTION));
		}
				
		// Start form
		sb.append("<form method='post' " + getFullAttrubutes() + " accept-charset=\"utf-8\">\n");

		// Hidden for message question **
	    sb.append("<input type=\"hidden\" name=\"" + ProjectConstante.WORK_ID_QUEST_DIALOG + "\" id=\""+ProjectConstante.WORK_ID_QUEST_DIALOG+"\" />\n");
	    sb.append("<a href=\"javascript:\" noval=\"true\" id=\"refreshTabLink\" wact=\""+EncryptionUtil.encrypt("admin.parametrage.refreshTabAction")+"\" style=\"display: none;\"></a>");
				
		// Clean current Validator map
	    if(request.getAttribute("SKIP_CLEAR_VALIDATOR") == null){
			boolean isEditableForm = !ComponentUtil.isReadOnlyAction(request);
			Map<String, Map<String, String>> validatorMap = ControllerUtil.getMapValidator(request);
			if(!isEditableForm){
				ControllerUtil.removeMenuAttribute(ProjectConstante.MAP_VALIDATORS, request);
			} else if(validatorMap == null){
				ControllerUtil.setMenuAttribute(ProjectConstante.MAP_VALIDATORS, new HashMap<String, Map<String, String>>(), request);
			} else{
				validatorMap.clear();
			}
	    }
		
		String ctrlAct = EncryptionUtil.encrypt(request.getAttribute(ProjectConstante.WORK_CONTROLLER)+"."+request.getAttribute(ProjectConstante.WORK_ACTION));
		sb.append("<input type=\"hidden\" name=\""+ProjectConstante.WORK_FORM_ACTION+"\" id=\""+ProjectConstante.WORK_FORM_ACTION+"\" value=\""+ctrlAct+"\"/>");
		sb.append("<input type=\"hidden\" name=\"wfact_tab\" id=\"wfact_tab\" value=\""+ctrlAct+"\"/>");
		
		//
		ComponentUtil.writeComponent(pageContext, sb.toString());
	}

	/**
	 * @throws JspException
	 */
	@SuppressWarnings("unchecked")
	public void writeEndComponent() throws JspException {
		HttpServletRequest request = ((HttpServletRequest) pageContext.getRequest());
		StringBuilder sb = new StringBuilder();
		StringBuilder jsScript = new StringBuilder("");
		
		if(StringUtil.isNotEmpty(sbScript.toString())){
			ComponentUtil.writeComponent(pageContext, ComponentUtil.getJavascriptOnReadyBloc(sbScript.toString(), getName()+"_js_bloc"));
		}
		
		// End form
		sb.append("</form>\n");
		
		// Append validator block
		if(ComponentUtil.isFormUseValidator(pageContext)){
			jsScript.append(FormValidator.addValidatorScript(request, this.getName()));
		}
		
		ComponentUtil.writeComponent(pageContext, sb.toString());
		// Write result
		if(StringUtil.isNotEmpty(jsScript.toString())){
			ComponentUtil.writeComponent(pageContext, ComponentUtil.getJavascriptOnReadyBloc(jsScript.toString(), getName()+"_js_bloc2"));
		}
				
		// Add export block out of form
		List<ExportTableBean> listExport = (List<ExportTableBean>)request.getAttribute("export_beans");
		if(listExport != null) {
			for(ExportTableBean exportBean : listExport){
				request.setAttribute("export_bean", exportBean);
				ComponentUtil.insertFragment(pageContext, "/WEB-INF/commun/center/export_popup.jsp");
			}
		}
		// Insetion banniere messages 
		ComponentUtil.insertFragment(pageContext, "/WEB-INF/commun/messages.jsp");
		ComponentUtil.insertFragment(pageContext, "/WEB-INF/commun/center/filter_popup.jsp");
		// Clear messages
		FrameworkMessageService.clearMessages();
		MessageService.clearMessages();	
	}

	/*
	 * \*********************************** GETTER and SETTER ***********************************\
	 */

	/**
	 * @param defaultClass
	 * @return
	 */
	protected String getFullAttrubutes() {
		return getFullId() + getFullName() + getFullClassStyle("form-horizontal no-margin");
	}

	/**
	 * @return the sbScript
	 */
	public StringBuilder getSbScript() {
		return sbScript;
	}

	/**
	 * @param sbScript the sbScript to set
	 */
	public void setSbScript(StringBuilder sbScript) {
		this.sbScript = sbScript;
	}
}
