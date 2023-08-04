package framework.model.common.util.tree;


public class ElementBase{
	private Integer e_left;
	private Integer e_right;
	private Integer e_level;
	private String e_label;
	private String e_tooltip;
	private Integer e_sort;
	private String e_href;
	private String e_icon;
	private Object e_id;
	private Object e_origineObject;
	
	public ElementBase(Object e_id){
		this.e_id = e_id;
	}

	public ElementBase(){
		
	}

	/**
	 * @return the e_left
	 */
	public Integer getE_left() {
		return e_left;
	}

	/**
	 * @param e_left the e_left to set
	 */
	public void setE_left(Integer e_left) {
		this.e_left = e_left;
	}

	/**
	 * @return the e_right
	 */
	public Integer getE_right() {
		return e_right;
	}

	/**
	 * @param e_right the e_right to set
	 */
	public void setE_right(Integer e_right) {
		this.e_right = e_right;
	}

	/**
	 * @return the e_level
	 */
	public Integer getE_level() {
		return e_level;
	}

	/**
	 * @param e_level the e_level to set
	 */
	public void setE_level(Integer e_level) {
		this.e_level = e_level;
	}

	/**
	 * @return the e_label
	 */
	public String getE_label() {
		return e_label;
	}

	/**
	 * @param e_label the e_label to set
	 */
	public void setE_label(String e_label) {
		this.e_label = e_label;
	}

	/**
	 * @return the e_tooltip
	 */
	public String getE_tooltip() {
		return e_tooltip;
	}

	/**
	 * @param e_tooltip the e_tooltip to set
	 */
	public void setE_tooltip(String e_tooltip) {
		this.e_tooltip = e_tooltip;
	}

	/**
	 * @return the e_id
	 */
	public Object getE_id() {
		return e_id;
	}

	/**
	 * @param e_id the e_id to set
	 */
	public void setE_id(Object e_id) {
		this.e_id = e_id;
	}
	
	public boolean isNode(){
		return (this.e_right-this.e_left) != 1; 
	}

	public String getE_href() {
		return e_href;
	}

	public void setE_href(String e_href) {
		this.e_href = e_href;
	}

	public String getE_icon() {
		return e_icon;
	}

	public void setE_icon(String e_icon) {
		this.e_icon = e_icon;
	}

	public Integer getE_sort() {
		return e_sort;
	}

	public void setE_sort(Integer e_sort) {
		this.e_sort = e_sort;
	}

	public Object getE_origineObject() {
		return e_origineObject;
	}

	public void setE_origineObject(Object e_origineObject) {
		this.e_origineObject = e_origineObject;
	}
	
}
