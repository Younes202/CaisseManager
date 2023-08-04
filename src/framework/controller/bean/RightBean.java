package framework.controller.bean;

public class RightBean {
	private String id;
	private String parentId;
	private String label;
	private boolean isGlobal;
	
	public RightBean(String id, String parentId, String label, boolean isGlobal){
		this.id = id;
		this.parentId = parentId;
		this.label = label;
		this.isGlobal = isGlobal;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public boolean isGlobal() {
		return isGlobal;
	}
	public void setGlobal(boolean isGlobal) {
		this.isGlobal = isGlobal;
	}
	
}
