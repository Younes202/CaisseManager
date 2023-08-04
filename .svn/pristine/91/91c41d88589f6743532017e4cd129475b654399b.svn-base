/**
 *
 */
package framework.component.text;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import framework.component.ComponentUtil;
import framework.component.text.plugin.MaskPlugin;
import framework.controller.ContextGloabalAppli;
import framework.model.common.constante.ActionConstante;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.service.MessageService;
import framework.model.common.util.StringUtil;

/**
 * @author 
 *
 */
public class Text extends TextBase {

	private static final long serialVersionUID = -7023075176444610090L;
	private String mask;
	private StringBuilder subSb;

	@Override
	public void doBeforStartComponent() throws JspException {
		// Test if is key label
		if(isKeyLabel()){
			setValidator(null);
		}
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
		mask = null;
		subSb = null;
	}

	@Override
	public void writeStartComponent() throws JspException {

	}

	/**
	 * @throws JspException
	 */
	public void writeEndComponent() throws JspException {
		// Write label if is read only
//		if(writeReadOnlyComponent()) {return;}
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

		// Build text component
		if (isKeyLabel()) {
			String defaultClass =  "labelROnly";
			String valueSt = StringUtil.getValueOrEmpty(this.getValue());
			// Disable validator
			sb.append("<label " + getFullClassStyle(defaultClass) + ">" + valueSt + "</label>\n");
			sb.append("<input type='hidden' " + this.getFullName() + getFullValue() + "/>");
		} else {
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
			
			// Add numeric type
			if(getType() != null){
				if("decimal".equals(getType())){
					String stMask = "";
					for(int i=0;i<ContextGloabalAppli.getNbrDecimalSaisie(); i++){
						stMask = stMask+"9";
					}
					appendStyle("text-align:right;"); // Right align
					String min = "", max = "";
					if(getMin() != null){
						min = "'"+getMin()+"'";
					} else{
						min = "'-9999999999."+stMask+"'";
					}
					if(getMax() != null){
						max = "'"+getMax()+"'";
					} else{
						max = "'9999999999."+stMask+"'";
					}
					jsSb.append("$('#"+this.getJQueryName()+"').autoNumeric('init', {vMin: "+min+",vMax: "+max+", aSep: ' '});");
				} else if("long".equals(getType())){
					appendStyle("text-align:right;"); // Right align
					String min = "", max = "";
					if(getMin() != null){
						min = "'"+getMin()+"'";
					} else{
						min = "'-9999999999'";
					}
					if(getMax() != null){
						max = "'"+getMax()+"'";
					} else{
						max = "'9999999999'";
					}
					jsSb.append("$('#"+this.getJQueryName()+"').autoNumeric('init', {vMin: "+min+",vMax: "+max+", aSep: ' '});");
				}
			}
			
			// Add completion script
			String defaultClass = "form-control";

			// Add mask script
			if(mask != null){
				jsSb.append(MaskPlugin.getMaskScript(getJQueryId(), mask));
			}

			// Default class
			sb.append("<input type='text'" + getFullComponentAttrubutes(defaultClass) + getFullTextAttrubutes() + getFullValue() + "/>\n");

			// Add helper block (image link)
			if(subSb != null){
				sb.append(subSb);
			}
			
			if(jsSb.length() > 0){
				sb.append(ComponentUtil.getJavascriptBloc(jsSb));
			}
		}

		// Write result
		ComponentUtil.writeComponent(getContextOrJspContext(), sb);
	}

	/*
	 * \------------------------------------End Bolc writer-----------------------------\
	 */


	/**
	 * @param block
	 */
	public void appendBlock(StringBuilder block) {
		if(subSb == null){
			subSb = new StringBuilder();
		}
		//
		this.subSb.append(block);
	}

	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}
}