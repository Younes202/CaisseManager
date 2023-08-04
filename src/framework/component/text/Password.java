/**
 *
 */
package framework.component.text;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import framework.component.ComponentUtil;
import framework.model.common.constante.ActionConstante;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.service.MessageService;
import framework.model.common.util.StringUtil;

/**
 * @author 
 *
 */
public class Password extends TextBase {

	private static final long serialVersionUID = -7023075176444610090L;

	@Override
	public void doBeforStartComponent() throws JspException {
	}

	/* (non-Javadoc)
	 * @see front.component.ComponentBase#doAfterEndComponent()
	 */
	@Override
	public void doAfterEndComponent() throws JspException {

	}

	/* (non-Javadoc)
	 * @see front.component.Component#releaseAll()
	 */
	public void releaseAll() {
	}

	@Override
	public void writeStartComponent() throws JspException {

	}

	/**
	 * @throws JspException
	 */
	public void writeEndComponent() throws JspException {
		if(super.isReadOnlyAttributeForm()){
			this.setReadOnly("true");
			this.setPlaceholder(null);
			this.setPlaceholderKey(null);
			this.appendStyle("background-color:#eeeeee;");
		};
		

		StringBuilder sb = new StringBuilder();
		StringBuilder jsSb = new StringBuilder();
		HttpServletRequest request = getGuiOrContextHttpRequest();
		String action = (String) request.getAttribute(ProjectConstante.WORK_ACTION);

		// Add id
		if(getId() == null){
			super.setId(super.getName());
		}
		// Put null in key field
		if(ActionConstante.INIT_DUPLIC.equals(action) && !MessageService.isError() && StringUtil.isTrue(this.getIskey())){
			setValue(null);
		}
		// Add upper case style
		if((getValidator() != null) && (getValidator().indexOf("upper") != -1)){
			appendStyle("text-transform:uppercase;");
		}
		
		// Add completion script
		String defaultClass = "form-control";

		// Default class
		sb.append("<input type='password'" + getFullComponentAttrubutes(defaultClass) + getFullTextAttrubutes() + getFullValue() + "/>\n");

		if(jsSb.length() > 0){
			sb.append(ComponentUtil.getJavascriptBloc(jsSb));
		}

		// Write result
		ComponentUtil.writeComponent(getContextOrJspContext(), sb);
	}
}