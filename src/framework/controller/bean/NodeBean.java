package framework.controller.bean;

public class NodeBean{
	private String id;
	private String label;
	private String level;
	private String tooltip;
	private String sort;
	private String bleft;
	private String bright;
	private String href;
	private String icon;
	
	/**
	 * @param id
	 * @param label
	 */
	public NodeBean(String id, String label) {
		this.id = id; 
		this.label = label;
	}
	
	/**
	 * @param id
	 * @param label
	 * @param tooltip
	 */
	public NodeBean(String id, String label, String tooltip) {
		this.id = id; 
		this.label = label;
		this.tooltip = tooltip;
	}
	
	public NodeBean(){
		
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getTooltip() {
		return tooltip;
	}
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}
	public String getBleft() {
		return bleft;
	}
	public void setBleft(String bleft) {
		this.bleft = bleft;
	}
	public String getBright() {
		return bright;
	}
	public void setBright(String bright) {
		this.bright = bright;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
	
}