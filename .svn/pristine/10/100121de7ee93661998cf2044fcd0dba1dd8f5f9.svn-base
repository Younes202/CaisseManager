package framework.component.complex.table.export;

import javax.servlet.jsp.JspException;

import framework.component.ComponentBase;
import framework.component.ComponentUtil;

@SuppressWarnings("serial")
public class ExportTag extends ComponentBase {
	@Override
	public int doStartTag() throws JspException {
		if(!ComponentUtil.isBodyTableAction(pageContext)){
			return EVAL_BODY_INCLUDE;
		}
		
		return SKIP_BODY;
	}

	@Override
	public void writeEndComponent() throws JspException {
	}

	@Override
	public void writeStartComponent() throws JspException {
	}

	@Override
	public void doBeforStartComponent() throws JspException {
	}
	
	/* (non-Javadoc)
	 * @see front.component.ComponentBase#doAfterEndComponent()
	 */
	@Override
	public void doAfterEndComponent() throws JspException {

	}

	@Override
	public void releaseAll() {
		
	}
}
