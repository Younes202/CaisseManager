package framework.component.text;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import framework.component.ComponentBase;
import framework.component.ComponentUtil;
import framework.model.common.constante.ActionConstante;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.service.MessageService;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.StringUtil;

public class Hidden extends ComponentBase {
	
	private String basic;

	@Override
	public void doBeforStartComponent() throws JspException {
	}

	@Override
	public void writeStartComponent() throws JspException {
	}

	@Override
	public void writeEndComponent() throws JspException {
		
		if(this.getName().endsWith(".id")){
			this.setValue(EncryptionUtil.encrypt(""+getValue()));
		}
		
		HttpServletRequest request = getGuiOrContextHttpRequest();
		String action = (String) request.getAttribute(ProjectConstante.WORK_ACTION);
		// Put null in key field
		if(ActionConstante.INIT_DUPLIC.equals(action) && !MessageService.isError() && StringUtil.isTrue(this.getIskey())){
			setValue(null);
		}
		
		if(StringUtil.isTrue(this.basic)){
			ComponentUtil.writeComponent(getContextOrJspContext(), "<input type='hidden' " + this.getFullId() + this.getFullName() + " value=\""+getValue()+"\"/>");
			return;
		}
		
		//
		String s = "<input type='hidden' " + this.getFullId() + this.getFullName() + getFullValue() + "/>";
		// Write result
		ComponentUtil.writeComponent(getContextOrJspContext(), s);
	}

	@Override
	public void doAfterEndComponent() throws JspException {
	}

	@Override
	public void releaseAll() {
		this.basic = null;
	}

	public String getBasic() {
		return basic;
	}

	public void setBasic(String basic) {
		this.basic = basic;
	}

}
