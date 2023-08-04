/**
 *
 */
package framework.model.common.formatter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import framework.component.ComponentUtil;
import framework.model.common.util.EncryptionUtil;

/**
 * @author 
 *
 */
@SuppressWarnings("serial")
public class EncryptTag extends TagSupport {

    private Object value;

    @Override
    public int doStartTag() throws JspException {
	    ComponentUtil.writeComponent(pageContext, EncryptionUtil.encrypt(""+value));
	    
	    return SKIP_BODY;
    }

    /**
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Object value) {
        this.value = value;
    }
}
