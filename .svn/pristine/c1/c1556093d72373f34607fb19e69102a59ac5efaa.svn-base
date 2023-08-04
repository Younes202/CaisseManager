package framework.controller.bean.message;

import framework.model.common.constante.ProjectConstante;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;


public class BannerMessageBean implements IMessageBean {
	/**
	 * 
	 */
	public BannerMessageBean(){}
	
	private String message;
	private ProjectConstante.MSG_TYPE type;
	
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
	 * @param field
	 * @param message
	 * @param type
	 */
	public BannerMessageBean(String message, MSG_TYPE type){
		this.message = message;
		this.type = type;
	}
	
	/**
	 * @param message
	 */
	public BannerMessageBean(String message){
		this.message = message;
		this.type = MSG_TYPE.ERROR;
	}
}
