package framework.controller.bean.message;

import framework.model.common.constante.ProjectConstante;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;

public class NotifyMessageBean implements IMessageBean {
	private String id;
	private String title;
	private String message;
	private ProjectConstante.MSG_TYPE type;

	/**
	 * 
	 */
	public NotifyMessageBean(){}
	
	public NotifyMessageBean(String id, String title, String message, MSG_TYPE type){
		this.message = message;
		this.type = type;
		this.title = title;
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	/** -----------------------------*/ 

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ProjectConstante.MSG_TYPE getType() {
		return type;
	}
	public void setType(ProjectConstante.MSG_TYPE type) {
		this.type = type;
	}
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
}
