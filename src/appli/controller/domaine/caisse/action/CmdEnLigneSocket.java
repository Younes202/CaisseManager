package appli.controller.domaine.caisse.action;

import java.io.IOException;
import java.util.Date;
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


@ServerEndpoint("/cmd-ligne")
public class CmdEnLigneSocket {
    public static Integer GLOBAL_CMD_LIGNE = 0;
    public static final Map<String, Session> GLOBAL_CLIENT_SESSIONS = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        // Handle the session opening if needed
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        // Handle the session closing if needed
        String keyRemove = null;
        for (String key : GLOBAL_CLIENT_SESSIONS.keySet()) {
            Session sessionTrack = GLOBAL_CLIENT_SESSIONS.get(key);
            if (sessionTrack.getId().equals(session.getId())) {
                keyRemove = key;
                break;
            }
        }
        if (keyRemove != null) {
            GLOBAL_CLIENT_SESSIONS.remove(keyRemove);
        }
    }

    public static void sendDataToScreen(Long caisseId, Integer newCommandCont) {
        if (newCommandCont > GLOBAL_CMD_LIGNE) {
            GLOBAL_CMD_LIGNE = newCommandCont;
            newCommandCont = StringUtil.isNotEmpty(newCommandCont) ? newCommandCont : 0;
            Session sessionTrack = GLOBAL_CLIENT_SESSIONS.get(caisseId.toString());
            if (sessionTrack == null || !sessionTrack.isOpen()) {
                return;
            }
            try {
                sessionTrack.getBasicRemote().sendText("" + newCommandCont);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
//    public static void sendDataToScreen(Session session, Integer newCommandCont) {
//        if (newCommandCont > GLOBAL_CMD_LIGNE) {
//            GLOBAL_CMD_LIGNE = newCommandCont;
//            newCommandCont = StringUtil.isNotEmpty(newCommandCont) ? newCommandCont : 0;
//            if (session == null || !session.isOpen()) {
//                // If the session is not available or not open, no client is connected, so no need to send data.
//                return;
//            }
//            try {
//                session.getBasicRemote().sendText("" + newCommandCont);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    @OnMessage
	public void onMessage(String caisseId, Session session){ 
		GLOBAL_CLIENT_SESSIONS.put(caisseId, session);
	}
	
    

    @OnError
    public void onError(Throwable e) {
        e.printStackTrace();
    }
}
