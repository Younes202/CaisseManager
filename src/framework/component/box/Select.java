/**
 *
 */
package framework.component.box;

import java.util.List;
import java.util.Set;

import javax.servlet.jsp.JspException;

import framework.component.ComponentUtil;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;

/**
 * @author 
 *
 */
public class Select extends BoxBase {

	/**
	 *
	 */
	private static final long serialVersionUID = 283918745450621002L;
	private String key;
	private String labels;
	private String groupKey;
	private String groupLabels;
	private String hiddenkey;
	private Object data = null;
	private String size;
	private String width;
	private String mode;
	private String multiple;
	private String addBlank;
	private String isTree;
	private String placeholder;
	private String placeholderKey; 

	// Local use for encryption
	private boolean isId;

	/*
	 * (non-Javadoc)
	 * 
	 * @see front.component.Component#writeEndComponent()
	 */
	public void writeEndComponent() throws JspException {
		if (mode.equals("free")) {
			ComponentUtil.writeComponent(pageContext, "</select>\n");
		}
	}

	/**
	 * Add label if read only
	 * 
	 * @return
	 * @throws JspException
	 */
	private boolean writeReadOnlyComponent() throws JspException {
		String defaultClass = "inputROnly";
		boolean isReadOnly = super.isReadOnlyAttributeForm();
		//
		if (isReadOnly) {
			String selectVal = appendSelectBody(null, true);
			//
			String label = "<label " + getFullClassStyle(defaultClass) + ">" + StringUtil.getValueOrEmpty(selectVal)
					+ "</label>\n";
			//
			ComponentUtil.writeComponent(getContextOrJspContext(), label);
			return true;
		}

		return false;
	}

	@Override
	public void doBeforStartComponent() throws JspException {
		// Test if is key label
		if (isKeyLabel()) {
			setValidator(null);
		}
		// Add id
		if (getId() == null) {
			super.setId(super.getName());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see front.component.ComponentBase#doAfterEndComponent()
	 */
	@Override
	public void doAfterEndComponent() throws JspException {

	}

	@Override
	public void releaseAll() {
		this.key = null;
		this.labels = null;
		this.data = null;
		this.size = null;
		this.mode = null;
		this.multiple = null;
		this.addBlank = null;
		this.hiddenkey = null;
		this.isId = false;

		super.releaseAll();
	}

	/**
	 * @throws JspException
	 */
	public void writeStartComponent() throws JspException {
		if(super.isReadOnlyAttributeForm()){
			this.appendStyle("background-color:#eeeeee;");
		};
		
		if (this.getName().endsWith(".id")) {
			this.isId = true;
		}

		StringBuilder sb = new StringBuilder();
		//
		if (StringUtil.isEmpty(mode)) {
			this.mode = "write";
		}

		if (super.isReadOnlyAttributeForm()) {
			this.setDisable("true");
		}

		//
		if ("write".equals(mode) || "free".equals(mode)) {
			// if(writeReadOnlyComponent()) return;
			sb.append(writeWritableSelectComponent());
		} else {
			// if(writeReadOnlyComponent()) return;
			sb.append(writeSelectComponent());
		}

		// Write result
		ComponentUtil.writeComponent(pageContext, sb);
	}

	/**
	 * @param isDisable
	 * @return
	 * @throws JspException
	 */
	private Object writeWritableSelectComponent() throws JspException {
		StringBuilder sb = new StringBuilder();
		String defaultClass = null;

		if (isKeyLabel()) {
			String valueSt = StringUtil.getValueOrEmpty(this.getValue());
			// Disable validator
			defaultClass = "labelROnly";
			sb.append("<label " + getFullClassStyle(defaultClass) + ">" + valueSt + "</label>\n");
			sb.append("<input type='hidden' " + this.getFullName() + " "+ this.getFullId() +" value='" + valueSt + "'/>");
		} else {
			defaultClass = "select2";
			//
			if (width != null) {
				appendStyle("width: " + width + "px;");
			}
			//
			// String allowClear = "";
			// if(StringUtil.isTrueOrNull(addBlank)){
			// allowClear = " data-allow-clear=\"true\"";
			// }

			String requiredAttr = StringUtil.isTrue(getRequired()) ? "req=\"1\"" : "";
			
			String placeHold = "";
			if(StringUtil.isNotEmpty(this.placeholder)) {
				placeHold = this.placeholder;
			} else if(StringUtil.isNotEmpty(this.placeholderKey)) {
				placeHold = StrimUtil.label(this.placeholderKey);
			} else {
				placeHold = StrimUtil.label("select.value");
			}

			// --------------------
			boolean writeReadOnlyComponent = super.isReadOnlyAttributeForm();
			if (super.isReadOnlyAttributeForm()) {
				this.setDisable("true");
			}

			if (StringUtil.isTrue(multiple) && !writeReadOnlyComponent) {
				sb.append("<input type=\"hidden\" name=\"" + getName() + "[]**\"/>\n");
			}

			// Fix probleme of submit disable component
			if (super.isReadOnlyAttributeForm() || StringUtil.isTrue(this.getDisable())) {
				String val = "";
				if(isId) {
					val = StringUtil.isEmpty(getValue()) ? "" : EncryptionUtil.encrypt("" + getValue());
				} else {
					val = ""+getValue();
				}
				sb.append("\n<input type='hidden' " + getFullName() + getFullId() + " value=\""+ StringUtil.getValueOrEmpty(val) + "\"/>");
				this.setId(this.getId()+"_old");
				this.setName(this.getName()+"_old");
			}
			
			// Select
			if (super.isReadOnlyAttributeForm()) {
				sb.append("<select "+requiredAttr+" data-placeholder=\"\"" + getFullComponentAttrubutes(defaultClass));
			} else {
				sb.append("<select "+requiredAttr+" data-placeholder=\"" + placeHold + "\" "
						+ getFullComponentAttrubutes(defaultClass));
			}

			// If multiple
			if (StringUtil.isTrue(multiple)) {
				if ("write".equals(mode)) {
					if (getType().indexOf("[]") == -1) {
						setType(getType() + "[]");
					}

					sb.append(" multiple ");
				}
			}
			sb.append(">");

			if (!mode.equals("free")) {
				// Body
				appendSelectBody(sb);
				// End
				sb.append("</select>\n");
			}
		}
		// write in jsp page
		return sb;
	}

	/**
	 * @throws JspException
	 *
	 */
	private StringBuilder writeSelectComponent() throws JspException {
		StringBuilder sb = new StringBuilder();
		String defaultClass = "select";
		String sizeSt = (size != null) ? " size='" + size + "'" : "";

		if (isKeyLabel()) {
			defaultClass = "label";
			String valueSt = StringUtil.getValueOrEmpty(this.getValue());
			// Disable validator
			sb.append("<label " + getFullClassStyle(defaultClass) + ">" + valueSt + "</label>\n");
			sb.append("<input type='hidden' " + this.getFullName() + " value='" + valueSt + "'/>");
		} else {
			if (width != null) {
				appendStyle("width: " + width + "px;");
			}

			// Select
			sb.append("<select " + getFullComponentAttrubutes(defaultClass) + sizeSt);

			// If multiple
			if (StringUtil.isTrue(multiple)) {
				sb.append(" multiple=\"multiple\">");
			}
			sb.append(">");

			// Body
			appendSelectBody(sb);
			// End
			sb.append("</select>\n");

			// Fix probleme of submit disable component
			if (super.isReadOnlyAttributeForm() || StringUtil.isTrue(this.getDisable())) {
				sb.append("\n<input type='hidden' " + getFullName() + getFullValue() + "/>");
			}
		}

		return sb;
	}

	/**
	 * @param sb
	 * @param itemKey
	 * @param itemValue
	 */
	private String getOptionTree(Object bean, String itemKey, String itemhiddenkey, String itemValue){
		String selected = "";
		// Add opttions
		if(StringUtil.isTrue(multiple)){
			if(getValue() != null && StringUtil.isNotEmpty(getValue())){
				for(Object id : (Object[])getValue()){
					if(StringUtil.isNotEmpty(itemKey) && itemKey.equals(""+id)){
						selected = " selected='selected'";
					}
				}
			}
		} else if(StringUtil.isNotEmpty(itemKey) && itemKey.equals(""+getValue())){
				selected = " selected='selected'";
		}
		
		String hiddenKey = "";
		if(StringUtil.isNotEmpty(itemhiddenkey)){
			hiddenKey = " hiddenkey=\""+itemhiddenkey+"\"";
		}

		if(isId){
			itemKey = EncryptionUtil.encrypt(itemKey);
		}
		

		String left = ReflectUtil.getStringPropertieValue(bean, "b_left");
		String right = ReflectUtil.getStringPropertieValue(bean, "b_right");
		String lev = ReflectUtil.getStringPropertieValue(bean, "level");
		
		int b_left = StringUtil.isNotEmpty(left) ? Integer.valueOf(left) : 0;
		int b_right = StringUtil.isNotEmpty(right) ? Integer.valueOf(right) : 0;
		int level = StringUtil.isNotEmpty(lev) ? Integer.valueOf(lev) : 0;
		
		String espace = "";
		if(level > 0){
			while(level != 1){
	    		espace = "--" + espace;
	    		level--;
	    	}
		}
		
		String img = (b_right - b_left > 1) ? "+" : "-";
		
		return "<option "+hiddenKey+" value=\"" + itemKey + "\"" + selected + "><h1>" + img + espace + itemValue + "</h1></option>\n";
	}

	/**
		 * @param sb
		 * @param itemKey
		 * @param itemValue
		 */
		private String getOption(String itemKey, String itemhiddenkey, String itemValue){
			String selected = "";
			// Add opttions
			if(StringUtil.isTrue(multiple)){
				if(getValue() != null && StringUtil.isNotEmpty(getValue())){
					for(Object id : (Object[])getValue()){
						if(itemKey.equals(""+id)){
							selected = " selected='selected'";
						}
					}
				}
			} else if(itemKey.equals(""+getValue())){
					selected = " selected='selected'";
			}
			
			String hiddenKey = "";
			if(StringUtil.isNotEmpty(itemhiddenkey)){
				hiddenKey = " hiddenkey=\""+itemhiddenkey+"\"";
			}

			if(isId){
				itemKey = EncryptionUtil.encrypt(itemKey);
			}
			
			return "<option "+hiddenKey+" value=\"" + itemKey + "\"" + selected + ">" + itemValue + "</option>\n";
		}

	/**
	 * @param sb
	 * @throws JspException
	 */
	private void appendSelectBody(StringBuilder sb) throws JspException {
		appendSelectBody(sb, false);
	}

	/**
	 * @param sb
	 * @throws JspException
	 */
	private String appendSelectBody(StringBuilder sb, boolean isReadOnly) throws JspException {
		String fullValue = "";

		if(data != null){
			String[] labelsArray = StringUtil.getArrayFromStringDelim(labels, ";");
			// Add blanc
			if(StringUtil.isTrueOrNull(addBlank)){
				if(!isReadOnly){
					sb.append(getOption("", "", "&nbsp;"));
				}
			}
			//
			if(data instanceof String[][]){
				String[][] dataArray = (String[][])data;
				for (int i = 0; i < dataArray.length; i++) {
					if(dataArray[i] != null){
						String itemKey = dataArray[i][0];
						String itemValue = dataArray[i][1];
						//
						if(isReadOnly){
							if(itemKey.equals(""+getValue())){
								return itemValue;
							}
						} else{
							// Append options
							sb.append(getOption(itemKey, "", itemValue));
						}
					}
				}
			} else if(data instanceof List){
				String oldGroup = null;
				List<?> dataArray = (List<?>) data;
				for(Object bean : dataArray){
					if(bean != null){
						String itemKey = ""+ReflectUtil.getStringPropertieValue(bean, key);
						
						boolean isDisable = StringUtil.isTrue(""+ReflectUtil.getObjectPropertieValue(bean, "is_disable"));
						if(isDisable && (getValue() == null || !(""+getValue()).equals(itemKey))) {
							continue;
						}
						
						String itemHiddenKey = "";
						String itemValue = "";
						
						try {
							for(String label : labelsArray){
								String attributeValue = "";
								//
								if(label.startsWith("'")){
									attributeValue = label.substring(1, (label.length()-1));
								} else{
									attributeValue = ReflectUtil.getStringPropertieValue(bean, label);
								}
								
								itemValue = itemValue + StringUtil.getValueOrEmpty(attributeValue);
							}
							
							
							if(StringUtil.isNotEmpty(hiddenkey)){
								String[] fields = StringUtil.getArrayFromStringDelim(hiddenkey, ";");
								for (String field : fields) {
									itemHiddenKey = itemHiddenKey+ReflectUtil.getStringPropertieValue(bean, field)+"|";
								}
							}
							
						} catch (Exception e) {
							throw new JspException(e);
						}
						//
						if(isReadOnly){
							if(StringUtil.isTrue(multiple)){
								if(getValue() != null){
									for(Object id : (Object[])getValue()){
										if(itemKey.equals(""+id)){
											fullValue = fullValue + " - " + itemValue + " - ";
										}
									}
								}
							} else{
								if(itemKey.equals(""+getValue())){
									return itemValue;
								}
							}
						} else{
							if(StringUtil.isNotEmpty(groupKey)){
								String groupkey = ReflectUtil.getStringPropertieValue(bean, groupKey);
								if(oldGroup == null || !oldGroup.equals(groupkey)){
									String grouplib = "";
								
									String[] fields = StringUtil.getArrayFromStringDelim(groupLabels, ";");
									for (String field : fields) {
										if(field.startsWith("'")){
											grouplib = grouplib + field.substring(1, (field.length()-1));
										} else{
											grouplib = grouplib + ReflectUtil.getStringPropertieValue(bean, field);
										}
									}
									sb.append("</optgroup>");
									sb.append("<optgroup label=\""+ StringUtil.getValueOrEmpty(grouplib) +"\">");
									
									oldGroup = groupkey;
								}
							}
							// Append options
							if(StringUtil.isTrue(this.isTree)){
								sb.append(getOptionTree(bean, itemKey, itemHiddenKey, itemValue));
							} else{
								sb.append(getOption(itemKey, itemHiddenKey, itemValue));
							}
						}
					}
				}
				if(StringUtil.isNotEmpty(groupKey)){
					sb.append("</optgroup>");
				}
				
			} else if(data instanceof Set){
				String oldGroup = null;
				Set<?> dataArray = (Set<?>) data;
				for(Object bean : dataArray){
					if(bean != null){
						String itemKey = ""+ReflectUtil.getStringPropertieValue(bean, key);;
						
						boolean isDisable = StringUtil.isTrue(""+ReflectUtil.getObjectPropertieValue(bean, "is_disable"));
						if(isDisable && (getValue() == null || !getValue().equals(itemKey))) {
							continue;
						}
						
						String itemHiddenKey = "";
						String itemValue = "";
						try {
							//
							for(String label : labelsArray){
								String attributeValue = "";
								//
								if(label.startsWith("'")){
									attributeValue = label.substring(1, (label.length()-1));
								} else{
									attributeValue = ReflectUtil.getStringPropertieValue(bean, label);
								}
								
								itemValue = itemValue +  StringUtil.getValueOrEmpty(attributeValue);
							}
							
							if(StringUtil.isNotEmpty(hiddenkey)){
								itemHiddenKey = ""+ReflectUtil.getStringPropertieValue(bean, hiddenkey);
							}
						} catch (Exception e) {
							throw new JspException(e);
						}
						//
						if(isReadOnly){
							if(StringUtil.isTrue(multiple)){
								if(getValue() != null){
									for(Object id : (Object[])getValue()){
										if(itemKey.equals(""+id)){
											fullValue = fullValue + " - " + itemValue + " - ";
										}
									}
								}
							} else{
								if(itemKey.equals(""+getValue())){
									return itemValue;
								}
							}
						} else{
							if(StringUtil.isNotEmpty(groupKey)){
								String groupkey = ReflectUtil.getStringPropertieValue(bean, groupKey);
								if(oldGroup != null && !oldGroup.equals(groupkey)){
									String grouplib = "";
								
									String[] fields = StringUtil.getArrayFromStringDelim(groupLabels, ";");
									for (String field : fields) {
										if(field.startsWith("'")){
											grouplib = grouplib + field.substring(1, (field.length()-1));
										} else{
											grouplib = grouplib + ReflectUtil.getStringPropertieValue(bean, field);
										}
									}
									sb.append("</optgroup>");
									sb.append("<optgroup label=\""+ grouplib+"\">");
									
									oldGroup = groupkey;
								}
							}
							// Append options
							if(StringUtil.isTrue(this.isTree)){
								sb.append(getOptionTree(bean, itemKey, itemHiddenKey, itemValue));
							} else{
								sb.append(getOption(itemKey, itemHiddenKey, itemValue));
							}
						}
					}
				}
				if(StringUtil.isNotEmpty(groupKey)){
					sb.append("</optgroup>");
				}
			} else{
				throw new JspException("The select component accept List or Set or String[][] for items argument !");
			}
		}
		return fullValue;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the label
	 */
	public String getLabels() {
		return labels;
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabels(String labels) {
		this.labels = labels;
	}

	/**
	 * @return the size
	 */
	public String getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(String size) {
		this.size = size;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getAddBlank() {
		return addBlank;
	}

	public void setAddBlank(String addBlank) {
		this.addBlank = addBlank;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getMultiple() {
		return multiple;
	}

	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}

	public String getHiddenkey() {
		return hiddenkey;
	}

	public void setHiddenkey(String hiddenkey) {
		this.hiddenkey = hiddenkey;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getIsTree() {
		return isTree;
	}

	public void setIsTree(String isTree) {
		this.isTree = isTree;
	}

	public boolean isId() {
		return isId;
	}

	public void setId(boolean isId) {
		this.isId = isId;
	}

	public String getGroupKey() {
		return groupKey;
	}

	public void setGroupKey(String groupKey) {
		this.groupKey = groupKey;
	}

	public String getGroupLabels() {
		return groupLabels;
	}

	public void setGroupLabels(String groupLabels) {
		this.groupLabels = groupLabels;
	}

	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	public String getPlaceholderKey() {
		return placeholderKey;
	}

	public void setPlaceholderKey(String placeholderKey) {
		this.placeholderKey = placeholderKey;
	}
}
