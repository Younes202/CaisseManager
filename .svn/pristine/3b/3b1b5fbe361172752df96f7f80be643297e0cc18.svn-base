package framework.controller.bean.message;

import framework.model.common.constante.ProjectConstante;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;



public class FieldMessageBean implements IMessageBean {

	private String field;

	/**
	 * 
	 */
	public FieldMessageBean(){}
	
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
	public FieldMessageBean(String field, String message, MSG_TYPE type){
		this.message = message;
		this.type = type;
		this.field = field;
	}

	/**
	 * @param field
	 * @param message
	 */
	public FieldMessageBean(String field, String message){
		this.message = message;
		this.type = MSG_TYPE.ERROR;
		this.field = field;
	}

	/**
	 * @return the field
	 */
	public String getField() {
		return field;
	}

	/**
	 * @param field
	 *            the field to set
	 */
	public void setField(String field) {
		this.field = field;
	}

}
