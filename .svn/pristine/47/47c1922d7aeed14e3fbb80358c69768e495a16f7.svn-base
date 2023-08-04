package framework.component.work;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import framework.component.ComponentUtil;
import framework.component.action.CompActionBase;
import framework.component.complex.table.BodyTag;
import framework.component.complex.table.TrTag;
import framework.controller.Context;
import framework.model.common.constante.ActionConstante;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.MenuMappingService;

@SuppressWarnings("serial")
public abstract class LinkBase extends CompActionBase {
	private String params;

	protected static enum ACTION_TYPE {
		DELETE, EDIT
	}

	//
	protected abstract ACTION_TYPE getActionType();

	/* (non-Javadoc)
	 * @see front.component.Component#doBeforStartComponent()
	 */
	@Override
	public void doBeforStartComponent() throws JspException {

	}

	/* (non-Javadoc)
	 * @see front.component.Component#releaseAll()
	 */
	@Override
	public void releaseAll() {
		params = null;
	}

	/* (non-Javadoc)
	 * @see front.component.Component#writeEndComponent()
	 */
	public void writeEndComponent() throws JspTagException {

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
	public void writeStartComponent() throws JspTagException {
		StringBuilder sb = new StringBuilder();
		HttpServletRequest request = ((HttpServletRequest) pageContext.getRequest());
		String image = "";
		
		TrTag parentTr = (TrTag) findAncestorWithClass(this, TrTag.class);
		BodyTag parentBody = (BodyTag) findAncestorWithClass(this, BodyTag.class);
		
		String controller = (parentBody.getController() == null) ? (String)request.getAttribute(ProjectConstante.WORK_CONTROLLER) : parentBody.getController();
		String parentWorkId = parentTr.getWorkId();
		String currTableName = (String)request.getAttribute("pagerBean_name");
		String fullParams = "curTbl="+currTableName+"&workId=" + EncryptionUtil.encrypt(parentWorkId) + ((params == null) ? "" : ("&" + params));
		
		//
		boolean isDeleteAvailable = Context.isOperationAvailable(MenuMappingService.RIGHT_ENUM.RIGHT_DELETE);
		
		//
		switch(getActionType()){
			case DELETE : {//------------------------------------------------------------------
				if(isDeleteAvailable && !ComponentUtil.isReadOnlyFormSetted(request)){
					fullParams = fullParams+"&nosave=true";
					String ctrlAct = controller + "." + ActionConstante.DELETE;
					ctrlAct = EncryptionUtil.encrypt(ctrlAct);
					image = "<div targetBtn='D' "+getFullStyle()+" title=\""+StrimUtil.label("button.delete")+"\" class=\"btn btn-sm btn-danger\" wact=\""+ctrlAct+"\" params=\""+fullParams+"\"><span class=\"fa fa-trash-o\"></span></div>";
				}
			}; break;
			case EDIT : {//--------------------------------------------------------------------
				String ctrlAct = controller + "." + ActionConstante.EDIT;
				ctrlAct = EncryptionUtil.encrypt(ctrlAct);

				image = "<div targetBtn='E' "+getFullStyle()+" title=\""+StrimUtil.label("button.edit")+"\" class=\"btn btn-sm btn-primary\" wact=\""+ctrlAct+"\" params=\""+fullParams+"\"><span class=\"fa  fa-eye\"></span></div>";
				// Change icon for current selected
				Map<String, Object> paramsMap = (Map<String, Object>)request.getAttribute(ProjectConstante.WORK_PARAMS);
				if(currTableName != null && currTableName.equals(paramsMap.get("curTbl")) && parentWorkId != null && parentWorkId.equals(paramsMap.get(ProjectConstante.WORK_ID))){	
					image = "<div "+getFullStyle()+" title=\""+StrimUtil.label("button.edit")+"\" class=\"btn btn-sm btn-primary\" wact=\""+ctrlAct+"\" params=\""+fullParams+"\"><span class=\"fa fa-eye\" style='color:#ff9800;'></span></div>";
				}
				//
			}; break;
		}

		// Build link
		if(StringUtil.isNotEmpty(image)){
			sb.append(image);

			// Write result
			ComponentUtil.writeComponent(pageContext, sb.toString());
		}
	}

	/* (non-Javadoc)
	 * @see front.component.Component#release()
	 */
	public void release() {
		super.release();
		//
		params = null;
	}

	/**
	 * @return the params
	 */
	public String getParams() {
		return params;
	}

	/**
	 * @param params the params to set
	 */
	public void setParams(String params) {
		this.params = params;
	}
}
