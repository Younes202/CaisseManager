/**
 *
 */
package framework.model.common.formatter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import framework.component.ComponentUtil;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.StringUtil;

/**
 * @author 
 *
 */
@SuppressWarnings("serial")
public class DecimalFormat extends TagSupport {

	private Object value;
	private String zeroVal;

	@Override
	public int doStartTag() throws JspException {
		String formattedValue = null;
		if (value != null) {
			// Write result
			if(StringUtil.isTrue(zeroVal)){
				formattedValue = BigDecimalUtil.formatNumberZero(BigDecimalUtil.get(""+value));
			} else{
				formattedValue = BigDecimalUtil.formatNumber(BigDecimalUtil.get(""+value));
			}
			ComponentUtil.writeComponent(pageContext, formattedValue);
		}

		return SKIP_BODY;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getZeroVal() {
		return zeroVal;
	}

	public void setZeroVal(String zeroVal) {
		this.zeroVal = zeroVal;
	}

}
