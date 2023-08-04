package framework.model.common.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import framework.controller.bean.DialogButtonBean;
import framework.controller.bean.message.BannerMessageBean;
import framework.controller.bean.message.DialogMessageBean;
import framework.controller.bean.message.FieldMessageBean;
import framework.controller.bean.message.GrowlMessageBean;
import framework.controller.bean.message.IMessageBean;
import framework.controller.bean.message.NotifyMessageBean;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StrimUtil;

public class MessageService {

	private final static String LIST_MSG = "work_list_msg";

 
	//-----------------------------------------------------------------------------------------------------------------
	 private static ThreadLocal<Map<String, Object>> UNIQUE_LIST = new ThreadLocal<Map<String, Object>>() {
         protected synchronized Map<String, Object> initialValue() {
             return new HashMap<String, Object>();
         } 
     };

    public static Map<String, Object> getGlobalMap() {
    	try{
	    	// Utiliser la session si on est dans le context Web si non thread
	    	ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    	HttpSession session = attr.getRequest().getSession(true); // true == allow create
	    	if(session != null){
	    		Map<String, Object> mapData = (Map<String, Object>)session.getAttribute("WORK_GLOBAL_MAP");
	    		if(mapData == null){
	    			mapData = new HashMap<String, Object>();
	    			session.setAttribute("WORK_GLOBAL_MAP", mapData);
	    		}
	    		return mapData;
	    	}
    	} catch(Exception e){
    		// On nefait rien car cela veut dire qu'on est dans un thread
    	}
    	// Context Thread
        return UNIQUE_LIST.get();
    }

	//-----------------------------------------------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
	private static List<IMessageBean> getListMsg(){
		List<IMessageBean> listMessage = (List<IMessageBean>)MessageService.getGlobalMap().get(LIST_MSG);
		if(listMessage == null){
			listMessage = Collections.synchronizedList(new ArrayList<IMessageBean>());
			MessageService.getGlobalMap().put(LIST_MSG, listMessage);
		}

		return listMessage;
	}

	/**
	 * @param message
	 */
	protected static void addMessage(IMessageBean message){
		getListMsg().add(message);
	}

	/**
	 * @param field
	 * @param message
	 * @param type
	 */
	public static void addFieldMessage(MSG_TYPE type, String field, String message){
		FieldMessageBean fieldMessage = new FieldMessageBean(field, message, type);
		addMessage(fieldMessage);
	}
	public static void addFieldMessageKey(MSG_TYPE type, String field, String messageKey){
		addFieldMessage(type, field, StrimUtil.label(messageKey));
	}
	public static void addFieldMessageKey(MSG_TYPE type, String field, String messageKey, String ... params){
		addFieldMessage(type, field, StrimUtil.label(messageKey, params));
	}

	/**
	 * @param field
	 * @param message
	 */
	public static void addFieldMessage(String field, String message){
		FieldMessageBean fieldMessage = new FieldMessageBean(field, message, MSG_TYPE.ERROR);
		addMessage(fieldMessage);
	}
	public static void addFieldMessageKey(String field, String messageKey){
		addFieldMessage(field, StrimUtil.label(messageKey));
	}
	public static void addFieldMessageKey(String field, String messageKey, String ... params){
		addFieldMessage(field, StrimUtil.label(messageKey, params));
	}

	/**
	 * @param message
	 * @param type
	 */
	public static void addDialogMessage(MSG_TYPE type, String title, String message){
		DialogMessageBean dialogMessage = new DialogMessageBean(title, message, type);
		addMessage(dialogMessage);
	}
	public static void addDialogMessage(MSG_TYPE type, String message){
		DialogMessageBean dialogMessage = new DialogMessageBean(message, type);
		addMessage(dialogMessage);
	}
	public static void addDialogMessageKey(MSG_TYPE type, String messageKey){
		addDialogMessage(type, StrimUtil.label(messageKey));
	}
	public static void addDialogMessageKey(MSG_TYPE type, String messageKey, String ... params){
		addDialogMessage(type, StrimUtil.label(messageKey, params));
	}
	/**
	 * @param message
	 */
	public static void addDialogMessage(String message){
		DialogMessageBean dialogMessage = new DialogMessageBean(message, MSG_TYPE.ERROR);
		addMessage(dialogMessage);
	}
	public static void addDialogMessageKey(String messageKey){
		addDialogMessage(StrimUtil.label(messageKey));
	}
	public static void addDialogMessageKey(String messageKey, String ... params){
		addDialogMessage(StrimUtil.label(messageKey, params));
	}

	/*******************************************************************************************************************/
	public static void addGrowlMessage(String title, String message) {
		GrowlMessageBean growlMessage = new GrowlMessageBean(title, message, MSG_TYPE.ERROR);
		addMessage(growlMessage);
	}
	public static void addGrowlMessageKey(String title, String messageKey) {
		addGrowlMessage(title, StrimUtil.label(messageKey));
	}
	public static void addGrowlMessageKey(String title, String messageKey, String ... params){
		addGrowlMessage(title, StrimUtil.label(messageKey, params));
	}
	/**
	 * @param message
	 * @param type
	 */
	public static void addGrowlMessage(MSG_TYPE type, String title, String message){
		GrowlMessageBean dialogMessage = new GrowlMessageBean(title, message, type);
		addMessage(dialogMessage);
	}
	public static void addGrowlMessageKey(MSG_TYPE type, String title, String messageKey){
		addGrowlMessage(type, title, StrimUtil.label(messageKey));
	}
	public static void addGrowlMessageKey(MSG_TYPE type, String title, String messageKey, String ... params){
		addGrowlMessage(type, title, StrimUtil.label(messageKey, params));
	}

	/*******************************************************************************************************************/
	public static void addNotifyMessage(String id, String title, String message) {
		NotifyMessageBean notifyMessage = new NotifyMessageBean(id, title, message, MSG_TYPE.INFO);
		addMessage(notifyMessage);
	}
	public static void addNotifyMessageKey(String id, String title, String messageKey) {
		addNotifyMessage(id, title, StrimUtil.label(messageKey));
	}
	public static void addNotifyMessageKey(String id, String title, String messageKey, String ... params){
		addNotifyMessage(id, title, StrimUtil.label(messageKey, params));
	}
	public static void addNotifyMessage(String id, MSG_TYPE type, String title, String message){
		NotifyMessageBean notifyMessage = new NotifyMessageBean(id, title, message, type);
		addMessage(notifyMessage);
	}
	public static void addNotifyMessageKey(String id, MSG_TYPE type, String title, String messageKey){
		addNotifyMessage(id, type, title, StrimUtil.label(messageKey));
	}
	public static void addNotifyMessageKey(String id, MSG_TYPE type, String title, String messageKey, String ... params){
		addNotifyMessage(id, type, title, StrimUtil.label(messageKey, params));
	}
	
	/*******************************************************************************************************************/

	public static boolean addDialogConfirmMessage(String id, String action, String title, String message){
		boolean exist = MessageIdsService.containsId(id);
		if(!exist){
			DialogMessageBean dialogMessage = new DialogMessageBean(title, message, MSG_TYPE.QUESTION);
			dialogMessage.setId(id);
			dialogMessage.setAction(EncryptionUtil.encrypt(action));
			addMessage(dialogMessage);
		}

		return exist;
	}
	public static boolean addDialogConfirmMessageKey(String id, String action, String titleKey, String messageKey){
		return addDialogConfirmMessage(id, action, StrimUtil.label(titleKey), StrimUtil.label(messageKey));
	}
	public static boolean addDialogConfirmMessageKey(String id, String action, String titleKey, String messageKey, String ... params){
		return addDialogConfirmMessage(id, action, StrimUtil.label(titleKey), StrimUtil.label(messageKey, params));
	}

	/**
	 * @param message
	 */
	public static boolean addDialogConfirmMessage(String id, String action, String message){
		boolean exist = MessageIdsService.containsId(id);
		if(!exist){
			String title = StrimUtil.label("work.question");
			DialogMessageBean dialogMessage = new DialogMessageBean(title, message, MSG_TYPE.QUESTION);
			dialogMessage.setId(id);
			dialogMessage.setAction(EncryptionUtil.encrypt(action));
			addMessage(dialogMessage);
		}

		return exist;
	}
	public static boolean addDialogConfirmMessageKey(String id, String action, String messageKey){
		return addDialogConfirmMessage(id, action, StrimUtil.label(messageKey));
	}
	public static boolean addDialogConfirmMessageKey(String id, String action, String messageKey, String ... params){
		return addDialogConfirmMessage(id, action, StrimUtil.label(messageKey, params));
	}

	/**----------------------------------------------------------------------*/
	
	/**
	 * @param title
	 * @param message
	 * @param listButtons
	 */
	public static void addDialogQuestionMessage(List<DialogButtonBean> listButtons, String title, String message){
		DialogMessageBean dialogMessage = new DialogMessageBean(title, message, MSG_TYPE.QUESTION);
		dialogMessage.setListButtons(listButtons);
		addMessage(dialogMessage);
	}
	public static void addDialogQuestionMessageKey(List<DialogButtonBean> listButtons, String titleKey, String messageKey){
		addDialogQuestionMessage(listButtons, StrimUtil.label(titleKey), StrimUtil.label(messageKey));
	}
	public static void addDialogQuestionMessageKey(List<DialogButtonBean> listButtons, String titleKey, String messageKey, String ... params){
		addDialogQuestionMessage(listButtons, StrimUtil.label(titleKey), StrimUtil.label(messageKey, params));
	}

	/**
	 * @param title
	 * @param message
	 * @param validateButton
	 * @return
	 */
	public static void addDialogQuestionMessage(String title, String message, DialogButtonBean validateButton){
		List<DialogButtonBean> listButtons = new ArrayList<DialogButtonBean>(1);
		listButtons.add(validateButton);
		DialogMessageBean dialogMessage = new DialogMessageBean(title, message, MSG_TYPE.QUESTION);
		dialogMessage.setListButtons(listButtons);
		addMessage(dialogMessage);
	}
	public static void addDialogQuestionMessageKey(DialogButtonBean validateButton, String titleKey, String messageKey){
		addDialogQuestionMessage(StrimUtil.label(titleKey), StrimUtil.label(messageKey), validateButton);
	}
	public static void addDialogQuestionMessageKey(DialogButtonBean validateButton, String titleKey, String messageKey, String ... params){
		addDialogQuestionMessage(StrimUtil.label(titleKey), StrimUtil.label(messageKey, params), validateButton);
	}

	/**
	 * @param message
	 * @param listButtons
	 */
	public static void addDialogQuestionMessage(List<DialogButtonBean> listButtons, String message){
		String title = StrimUtil.label("work.question");
		DialogMessageBean dialogMessage = new DialogMessageBean(title, message, MSG_TYPE.QUESTION);
		dialogMessage.setListButtons(listButtons);
		addMessage(dialogMessage);
	}
	public static void addDialogQuestionMessageKey(List<DialogButtonBean> listButtons, String messageKey){
		addDialogQuestionMessage(listButtons, StrimUtil.label(messageKey));
	}
	public static void addDialogQuestionMessageKey(List<DialogButtonBean> listButtons, String messageKey, String ... params){
		addDialogQuestionMessage(listButtons, StrimUtil.label(messageKey, params));
	}

	/*******************************************************************************************************************/

	/**
	 * @param message
	 * @param type
	 */
	public static void addBannerMessage(MSG_TYPE type, String message){
		BannerMessageBean bannerMessage = new BannerMessageBean(message, type);
		addMessage(bannerMessage);
	}
	public static void addBannerMessageKey(MSG_TYPE type, String messageKey){
		addBannerMessage(type, StrimUtil.label(messageKey));
	}
	public static void addBannerMessageKey(MSG_TYPE type, String messageKey, String ... params){
		addBannerMessage(type, StrimUtil.label(messageKey, params));
	}

	/**
	 * @param message
	 */
	public static void addBannerMessage(String message){
		BannerMessageBean bannerMessage = new BannerMessageBean(message, MSG_TYPE.ERROR);
		addMessage(bannerMessage);
	}
	public static void addBannerMessageKey(String messageKey){
		addBannerMessage(StrimUtil.label(messageKey));
	}
	public static void addBannerMessageKey(String messageKey, String ... params){
		addBannerMessage(StrimUtil.label(messageKey, params));
	}

	/**
	 * @return
	 */
	public static List<IMessageBean> getAllMessages(){
		return getListMsg();
	}

	/**
	 * @return
	 */
	public static boolean isMessageExist(){
		return getListMsg().isEmpty();
	}

	/**
	 *
	 */
	public static void clearMessages(){
		getListMsg().clear();
	}

	/**
	 * @return
	 */
	public static List<FieldMessageBean> getListFieldMessageBean(){
		List<IMessageBean> listMessages = getListMsg();
		//
		if((listMessages != null) && (listMessages.size() > 0)){
			List<FieldMessageBean> listFieldMessage = new ArrayList<FieldMessageBean>();
			for(IMessageBean fieldMsg : listMessages){
				if(fieldMsg instanceof FieldMessageBean){
					listFieldMessage.add((FieldMessageBean)fieldMsg);
				}
			}

			return listFieldMessage;
		}

		return null;
	}

	/**
	 * @return
	 */
	public static List<DialogMessageBean> getListDialogMessageBean(){
		List<IMessageBean> listMessages = getListMsg();
		//
		if((listMessages != null) && (listMessages.size() > 0)){
			List<DialogMessageBean> listDialogMessage = new ArrayList<DialogMessageBean>();
			for(IMessageBean dialogMsg : listMessages){
				if(dialogMsg instanceof DialogMessageBean){
					listDialogMessage.add((DialogMessageBean)dialogMsg);
				}
			}

			return listDialogMessage;
		}

		return null;
	}

	/**
	 * @return
	 */
	public static List<GrowlMessageBean> getListGrowlMessageBean(){
		List<IMessageBean> listMessages = getListMsg();
		//
		if((listMessages != null) && (listMessages.size() > 0)){
			List<GrowlMessageBean> listGrowlMessage = new ArrayList<GrowlMessageBean>();
			for(IMessageBean growlMsg : listMessages){
				if(growlMsg instanceof GrowlMessageBean){
					listGrowlMessage.add((GrowlMessageBean)growlMsg);
				}
			}

			return listGrowlMessage;
		}

		return null;
	}

	/**
	 * @return
	 */
	public static List<BannerMessageBean> getListBannerMessageBean(){
		List<IMessageBean> listMessages = getListMsg();
		//
		if((listMessages != null) && (listMessages.size() > 0)){
			List<BannerMessageBean> listBannerMessage = new ArrayList<BannerMessageBean>();
			for(IMessageBean bannerMsg : listMessages){
				if(bannerMsg instanceof BannerMessageBean){
					listBannerMessage.add((BannerMessageBean)bannerMsg);
				}
			}

			return listBannerMessage;
		}

		return null;
	}
	
	/**
	 * @return
	 */
	public static List<NotifyMessageBean> getListNotifyMessageBean(){
		List<IMessageBean> listMessages = getListMsg();
		//
		if((listMessages != null) && (listMessages.size() > 0)){
			List<NotifyMessageBean> listNotifyMessage = new ArrayList<NotifyMessageBean>();
			for(IMessageBean notifyMsg : listMessages){
				if(notifyMsg instanceof NotifyMessageBean){
					listNotifyMessage.add((NotifyMessageBean)notifyMsg);
				}
			}

			return listNotifyMessage;
		}

		return null;
	}

	/**
	 * @return true if list of messages contains error type or type is null(Developper)
	 */
	public static boolean isError(){
		if(FrameworkMessageService.isError()){
			return true;
		} else{
			List<IMessageBean> listErrorMsg = getListErrorMessages();
			
			return ServiceUtil.isNotEmpty(listErrorMsg);
		}
	}

	/**
	 * @return
	 */
	public static List<IMessageBean> getListErrorMessages(){
		List<IMessageBean> listMsg = getAllMessages();
		List<IMessageBean> listErrorMsg = null;
		if((listMsg != null) && (listMsg.size() > 0)){
			listErrorMsg = new ArrayList<IMessageBean>();
			for(IMessageBean msg : listMsg){
				if(ProjectConstante.MSG_TYPE.ERROR.equals(msg.getType()) || (msg.getType() == null)){
					listErrorMsg.add(msg);
				}
			}
		}

		return listErrorMsg;
	}

	/**
	 * @return
	 */
	public static String toStringErrorMessages(){
		List<IMessageBean> listErrorMsg = getListErrorMessages();
		if(ServiceUtil.isNotEmpty(listErrorMsg)){
			StringBuilder sb = new StringBuilder();
			//
			for(IMessageBean msg : listErrorMsg){
				if(msg instanceof FieldMessageBean){
					FieldMessageBean fieldMsg = (FieldMessageBean) msg;
					sb.append(fieldMsg.getField() + ":  " + msg.getMessage() + "\n");
				} else{
					sb.append(msg.getMessage() + "\n");
				}
			}
			return sb.toString();
		}

		return null;
	}

	/**
	 * @return
	 */
	public static boolean isFieldError(){
		List<FieldMessageBean> listFieldMessage = getListFieldMessageBean();
		return (listFieldMessage != null) && (listFieldMessage.size() > 0);
	}

	/**
	 * @param fieldName
	 * @return
	 */
	public static boolean isFieldError(String fieldName){
		List<FieldMessageBean> list = getListFieldMessageBean();
		if((list != null) && (list.size() > 0)){
			for(FieldMessageBean msg : list){
				if(msg.getField().equals(fieldName)){
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * @param fieldName
	 * @return
	 */
	public static boolean isLastFieldError(String fieldName){
		List<FieldMessageBean> list = getListFieldMessageBean();
		if(list != null){
			int size = list.size();
			if((list != null) && (size > 0)){
				return list.get(size-1).getField().equals(fieldName);
			}
		}

		return false;
	}

	/**
	 * @return
	 */
	public static boolean isQuestionExist(){
		List<DialogMessageBean> list = getListDialogMessageBean();
		if((list != null) && (list.size() > 0)){
			for(DialogMessageBean msg : list){
				if(msg.getType().equals(MSG_TYPE.QUESTION)){
					return true;
				}
			}
		}

		return false;
	}
}
