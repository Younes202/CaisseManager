/**
 * 
 */
package framework.component.text;

import framework.component.ComponentBase;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;

/**
 * @author 
 * 
 */
@SuppressWarnings("serial")
public abstract class TextBase extends ComponentBase {

	private String maxlength;
	private String size;
	private String autocomplete;
	private String placeholder;
	private String placeholderKey; 
	
	/* (non-Javadoc)
	 * @see front.component.ComponentBase#releaseAll()
	 */
	public void releaseAttr() {
		this.maxlength = null;
		this.size = null;
	}
	
	/**
	 * @param defaultClass
	 * @return
	 */
	protected String getFullTextAttrubutes() {
		// Build result
		String result = getFullSize() + getFullMaxlength() + getFullMinlength() + getFullPlaceholder() + getFullAutocomplete();
		
		return result;
	}
	
	private String getFullMaxlength(){
		return (this.maxlength == null) ? "" : " maxlength=\""+maxlength+"\"";
	}
	
	private String getFullMinlength(){
		return (super.getMinlength() == null) ? "" : " minlength=\""+super.getMinlength()+"\"";
	}
	
	private String getFullSize(){
		return (this.size == null) ? "" : " size=\""+size+"\"";
	}
	
	/**
	 * @return the maxlength
	 */
	public String getMaxlength() {
		return maxlength;
	}

	/**
	 * @param maxlength
	 *            the maxlength to set
	 */
	public void setMaxlength(String maxlength) {
		this.maxlength = maxlength;
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
	

	public String getPlaceholderKey() {
		return placeholderKey;
	}

	public void setPlaceholderKey(String placeholderKey) {
		this.placeholderKey = placeholderKey;
	}
	public String getPlaceholderOrPlaceholderKey(){
		if((this.placeholder == null) && (this.placeholderKey != null)){
			return StrimUtil.label(placeholderKey);
		}

		return StringUtil.getValueOrEmpty(this.placeholder);
	}

	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}
	private String getFullPlaceholder(){
		String placeholderVal = this.getPlaceholderOrPlaceholderKey();
		return StringUtil.isEmpty(placeholderVal) ? "" : " placeholder=\""+placeholderVal+"\"";
	}

	public String getAutocomplete() {
		return autocomplete;
	}

	public void setAutocomplete(String autocomplete) {
		this.autocomplete = autocomplete;
	}
	private String getFullAutocomplete(){
		String autocomplete = this.getAutocomplete();
		return StringUtil.isFalse(autocomplete) ? " autocomplete=\"off\"" : "";
	}

}
