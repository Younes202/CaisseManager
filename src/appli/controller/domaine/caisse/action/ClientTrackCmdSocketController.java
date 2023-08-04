package appli.controller.domaine.caisse.action;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import framework.model.common.util.StringUtil;


@ServerEndpoint("/cmd-track")
public class ClientTrackCmdSocketController {
	public static final Map<String, Session> GLOBAL_CLIENT_SESSIONS = new ConcurrentHashMap<String, Session>();
	
	@OnOpen
	public void onOpen(Session session){
//		System.out.println("Open Connection ...");
	}

	@OnClose
	public void onClose(Session session, CloseReason closeReason){
		String keyRemove = null;
		for(String key : GLOBAL_CLIENT_SESSIONS.keySet()) {
			Session sessionTrack = GLOBAL_CLIENT_SESSIONS.get(key);
			if(sessionTrack.getId().equals(session.getId())) {
				keyRemove = key;
				break;
			}
		}
		if(keyRemove != null) {
			GLOBAL_CLIENT_SESSIONS.remove(keyRemove);
		}
		
		//System.out.println("Close Connection ...");
	}
	
	public static void sendDataToScreen(Long caisseId, String value, String type) {
		type = StringUtil.isNotEmpty(type) ? type+"_" : "";
		Session sessionTrack = GLOBAL_CLIENT_SESSIONS.get(type+caisseId.toString());
		if(sessionTrack == null || !sessionTrack.isOpen()) {
			return;
		}
		try {
			sessionTrack.getBasicRemote().sendText(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@OnMessage
	public void onMessage(String caisseId, Session session){ 
		GLOBAL_CLIENT_SESSIONS.put(caisseId, session);
	}
	
	@OnError
	public void onError(Throwable e){
		e.printStackTrace();
	}
}