package framework.controller.bean.message;

import java.io.Serializable;
import java.util.List;

import framework.controller.bean.DialogButtonBean;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;

public class DialogMessageBean implements IMessageBean, Serializable {
	private String action;
	private String title;
	private String message;
	private String id;
	private ProjectConstante.MSG_TYPE type;
	private List<DialogButtonBean> listButtons;

	/**
	 * 
	 */
	public DialogMessageBean(){}

	/**
	 * @param field
	 * @param message
	 * @param type
	 */
	public DialogMessageBean(String title, String message, MSG_TYPE type){
		this.message = message;
		this.type = type;
		this.title = title;
	}
	
	/**
	 * @param message
	 * @param type
	 */
	public DialogMessageBean(String message, MSG_TYPE type){
		this.message = message;
		this.type = type;
	}
	
	/**
	 * @param field
	 * @param message
	 */
	public DialogMessageBean(String message){
		this.message = message;
		this.type = MSG_TYPE.ERROR;
	}
	
	/**
	 * @param title
	 * @param message
	 */
	public DialogMessageBean(String title, String message){
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
	
	/**
	 * @return the listButtons
	 */
	public List<DialogButtonBean> getListButtons() {
		return listButtons;
	}

	/**
	 * @param listButtons the listButtons to set
	 */
	public void setListButtons(List<DialogButtonBean> listButtons) {
		this.listButtons = listButtons;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
}
