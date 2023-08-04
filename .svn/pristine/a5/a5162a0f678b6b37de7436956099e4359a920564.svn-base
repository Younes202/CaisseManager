//This sample is how to use websocket of Tomcat.
package appli.controller.domaine.caisse.action.mobile;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import appli.controller.domaine.caisse.action.ClientTrackCmdSocketController;


@ServerEndpoint("/mobile-track")
public class MobileTrackCmdSocketController {
	@OnOpen
	public void onOpen(Session session){
//		System.out.println("Open Connection ...");
	}

	@OnClose
	public void onClose(Session session, CloseReason closeReason){
		String keyRemove = null;
		for(String key : ClientTrackCmdSocketController.GLOBAL_CLIENT_SESSIONS.keySet()) {
			Session sessionTrack = ClientTrackCmdSocketController.GLOBAL_CLIENT_SESSIONS.get(key);
			if(sessionTrack.getId().equals(session.getId())) {
				keyRemove = key;
				break;
			}
		}
		if(keyRemove != null) {
			ClientTrackCmdSocketController.GLOBAL_CLIENT_SESSIONS.remove(keyRemove);
		}
		
		//System.out.println("Close Connection ...");
	}
	
	public static void sendDataToScreen(Long userId, String tp, String cmdJson) {
		Session sessionTrack = ClientTrackCmdSocketController.GLOBAL_CLIENT_SESSIONS.get(tp+"_"+userId);
		if(sessionTrack == null || !sessionTrack.isOpen()) {
			return;
		}
		try {
			sessionTrack.getBasicRemote().sendText(cmdJson);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@OnMessage
	public void onMessage(String tpUserId, Session session){ 
		ClientTrackCmdSocketController.GLOBAL_CLIENT_SESSIONS.put(tpUserId, session);
	}
	
	@OnError
	public void onError(Throwable e){
		e.printStackTrace();
	}
}