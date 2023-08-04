/**
 *
 */
package framework.model.common.formatter;

import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import framework.component.ComponentUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.StrimUtil;

/**
 * @author 
 *
 */
@SuppressWarnings("serial")
public class DateFormat extends TagSupport {

    private Date value;
    private String pattern;

    @Override
    public int doStartTag() throws JspException {

	if((value != null) && (value instanceof Date)){
	    String format = null;
	    if(pattern != null){
		format = pattern;
	    } else{
	    	format = StrimUtil.getGlobalConfigPropertie("ddMMyyyy.format");
	    }
		// Write result
	    String formattedValue = DateUtil.dateToString(value, format);
	    ComponentUtil.writeComponent(pageContext, formattedValue);
	}

	return SKIP_BODY;
    }

    /**
     * @return the value
     */
    public Date getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Date value) {
        this.value = value;
    }

    /**
     * @return the pattern
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * @param pattern the pattern to set
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }


}
