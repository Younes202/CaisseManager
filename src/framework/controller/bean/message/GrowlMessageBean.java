package framework.controller.bean.message;

import framework.model.common.constante.ProjectConstante;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;

public class GrowlMessageBean implements IMessageBean {
	
	private String title;
	private String message;
	private ProjectConstante.MSG_TYPE type;

	/**
	 * 
	 */
	public GrowlMessageBean(){}

	/**
	 * @param field
	 * @param message
	 * @param type
	 */
	public GrowlMessageBean(String title, String message, MSG_TYPE type){
		this.message = message;
		this.type = type;
		this.title = title;
	}
	
	/**
	 * @param message
	 * @param type
	 */
	public GrowlMessageBean(String message, MSG_TYPE type){
		this.message = message;
		this.type = type;
	}
	
	/**
	 * @param field
	 * @param message
	 */
	public GrowlMessageBean(String message){
		this.message = message;
		this.type = MSG_TYPE.ERROR;
	}
	
	/**
	 * @param title
	 * @param message
	 */
	public GrowlMessageBean(String title, String message){
		this.message = message;
		this.type = MSG_TYPE.ERROR;
		this.title = title;
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
