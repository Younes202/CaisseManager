package framework.model.common.service;

import java.util.ArrayList;
import java.util.List;

public class MessageIdsService {

	private final static String LIST_MSG_IDS = "work_list_msgids";
	private final static String QUESTION_ACTION = "work_quest_act";
	
	@SuppressWarnings("unchecked")
	private static List<String> getListMsgIds(){
		List<String> listMessage = (List<String>)MessageService.getGlobalMap().get(LIST_MSG_IDS);
		
		if(listMessage == null){
			listMessage = new ArrayList<String>();
			MessageService.getGlobalMap().put(LIST_MSG_IDS, listMessage);
		}
		
		return listMessage; 
	}
	
	/**
	 * @param id
	 */
	public static void addConfirmDialogId(String id){
		getListMsgIds().add(id);
	}

	/**
	 * @return
	 */
	public static String getQuestionDialogAction(){
		return (String)MessageService.getGlobalMap().get(QUESTION_ACTION);
	}
	
	/**
	 * @param action
	 */
	public static void setQuestionDialogAction(String action){
		MessageService.getGlobalMap().put(QUESTION_ACTION, action);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static boolean containsId(String id){
		return getListMsgIds().contains(id);
	}
	
	/**
	 * 
	 */
	public static void clearConfirmIds(){
		getListMsgIds().clear();
	}
	
	/**
	 * 
	 */
	public static void clearQuestionAction(){
		MessageService.getGlobalMap().remove(QUESTION_ACTION);
	}
}
