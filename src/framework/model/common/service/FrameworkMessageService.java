package framework.model.common.service;

import framework.controller.bean.message.DialogMessageBean;
import framework.model.common.util.StrimUtil;

public class FrameworkMessageService {
	private final static String MSG_DEV = "work_list_msg_dev";

	private static void addFrameworkMessage(String title, String message){
		DialogMessageBean dialogMessage = new DialogMessageBean(title, message);
		MessageService.getGlobalMap().put(MSG_DEV, dialogMessage);
	}

	/**
	 *
	 */
	public static void addTechnicalErrorMessage(){
		String title = StrimUtil.label("work.technic.error.title");
		String msg = StrimUtil.label("work.technic.error");
		//
		addFrameworkMessage(title, msg);
	}

	/**
	 * @return
	 */
	public static DialogMessageBean getFrameworkErrorMessage(){
		return (DialogMessageBean)MessageService.getGlobalMap().get(MSG_DEV);
	}

	/**
	 * @param message
	 * @param type
	 */
	public static void addDevelopperMessage(String message){
		String title = StrimUtil.label("work.technic.error.title");
		//
		addFrameworkMessage(title, message);
	}

	/**
	 *
	 */
	public static void clearMessages(){
		MessageService.getGlobalMap().remove(MSG_DEV);
	}

	/**
	 * @return true if list of messages contains error type (technical message)
	 */
	public static boolean isError(){
		if(getFrameworkErrorMessage() != null){
			return true;
		}

		return false;
	}
}
