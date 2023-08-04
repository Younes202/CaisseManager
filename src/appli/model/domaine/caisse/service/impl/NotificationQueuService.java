package appli.model.domaine.caisse.service.impl;

import java.io.FileInputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.springframework.transaction.annotation.Transactional;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Message.Builder;
import com.google.firebase.messaging.Notification;

import appli.model.domaine.administration.persistant.UserPersistant;
import appli.model.domaine.caisse.persistant.NotificationQueuPersistant;
import appli.model.domaine.caisse.service.ICaisseMouvementService;
import appli.model.domaine.personnel.persistant.ClientPersistant;
import appli.model.domaine.personnel.persistant.EmployePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import framework.model.common.service.MessageService;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.DateUtil.TIME_ENUM;
import framework.model.common.util.StringUtil;

@Named
public class NotificationQueuService {
	private static final String _ANDROID_SMARTPHONE_CAISSE = "pushnotification-254d1";
	private static final String _FIREBASEIO_URL_CAISSE = "https://pushnotification-254d1.firebaseio.com";
	private static final String _FIREBASE_CAISSE = "/appli/conf/pushnotification-254d1-firebase-adminsdk-irzaw-4129de7695.json";
	
	private static FirebaseOptions fireBaseOptionCaisse = null;
	
	@Inject
	private ICaisseMouvementService mvmService;
	
	@PersistenceUnit
	EntityManagerFactory emf;
	
	static {
		try {
			// Envoi
			URL resource = NotificationQueuService.class.getResource(_FIREBASE_CAISSE);
			if(resource != null) {
			FileInputStream serviceAccount = new FileInputStream(resource.getFile());
				fireBaseOptionCaisse = new FirebaseOptions.Builder()
				    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
				    .setDatabaseUrl(_FIREBASEIO_URL_CAISSE)
				    .setProjectId(_ANDROID_SMARTPHONE_CAISSE)
				    .build();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param message
	 * @param tp_CH_CL
	 * @return
	 */
	private boolean sendNotification(Message message) {
		try {
			// Purger les anciennes notifications
			for(FirebaseApp fireBaseApp : FirebaseApp.getApps()){
				fireBaseApp.delete();
			}
			
			FirebaseApp.initializeApp(fireBaseOptionCaisse);

			String resp = FirebaseMessaging.getInstance().send(message);
			System.out.println("Successfully sent message: " + resp);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			
		}
		
		return true;
	}
	
	@Transactional
	public void addNotification(Map<String, String> mapMessage, UserPersistant user, 
				CaisseMouvementPersistant commande, Date date_notification) {
		
		if(user == null 
				|| StringUtil.isEmpty(user.getDevice_token())
				|| !BooleanUtil.isTrue(user.getIs_device_notif())) {
			return;
		}
		
		if(date_notification != null) {
			Calendar calDebut = DateUtil.getCalendar(date_notification), calFin = DateUtil.getCalendar(date_notification);
			calDebut.set(calDebut.get(Calendar.YEAR),  calDebut.get(Calendar.MONTH), calDebut.get(Calendar.DAY_OF_MONTH), 23, 0, 0); //23:00
			Date date_debut_nuit = calDebut.getTime();
			calFin.set(calFin.get(Calendar.YEAR), calFin.get(Calendar.MONTH), calFin.get(Calendar.DAY_OF_MONTH), 7, 00, 0); //07:00
			Date date_fin_nuit = calFin.getTime();
			
			if(date_notification.after(date_debut_nuit) && date_notification.before(date_fin_nuit)) {
				calDebut.set(calDebut.get(Calendar.YEAR),  calDebut.get(Calendar.MONTH), calDebut.get(Calendar.DAY_OF_MONTH), 22, 0, 0); //22:00
				date_notification = calDebut.getTime();
			}
		} else {
			date_notification = DateUtil.getCurrentDate();
		}
		
		StringBuilder sb = new StringBuilder();
		for(String key : mapMessage.keySet()) {
			sb.append(key+"|:|"+mapMessage.get(key)+"|;|");
		}

		NotificationQueuPersistant notificationP = new NotificationQueuPersistant();
		notificationP.setDate_notification(date_notification);
		notificationP.setDate_creation(DateUtil.getCurrentDate());
		
		if(commande != null) {
			notificationP.setCommande_id(commande.getId());
		}
		
		notificationP.setParams(sb.toString());
		notificationP.setTitle(mapMessage.get("titre"));
		notificationP.setMessage(mapMessage.get("message"));
		notificationP.setGroupe(mapMessage.get("type"));
		
		ClientPersistant opc_client = user.getOpc_client();
		EmployePersistant opc_employe = user.getOpc_employe();
		//
		if(opc_client != null){
			notificationP.setClient_id(opc_client.getId());
			notificationP.setClient(opc_client.getNomPrenom());
			notificationP.setDevice_token(user.getDevice_token());
		} else if(opc_employe != null) {
			notificationP.setEmploye(opc_employe.getNomPrenom());
			notificationP.setEmploye_id(opc_employe.getId());
			notificationP.setDevice_token(user.getDevice_token());
		}
		
		mvmService.mergeEntity(notificationP);
		mvmService.getEntityManager().flush();
		
		MessageService.getGlobalMap().put("IS_NOTIF_SENDED", true);
		
		sendAllTodayNotification();
	}
	
	public void sendAllTodayNotification() {
		EntityManager em = null;
		try{
			em = emf.createEntityManager();
			Date dateMaxOld = DateUtil.addSubstractDate(DateUtil.getCurrentDate(), TIME_ENUM.MINUTE, -30);// 30 min max ancienne
			List<NotificationQueuPersistant> listNotification = em.createQuery("from NotificationQueuPersistant "
				+ " where "
				+ " date_envoie is null "
				+ " and is_toRemove is null "
				//+ " and date_notification <= :date_debut "
				//+ " and date_notification >= :dateMaxOld "
				+ " and groupe like 'ETS_%'"
				+ " and device_token is not null ")
			//.setParameter("date_debut", DateUtil.getCurrentDate())
			//.setParameter("dateMaxOld", dateMaxOld)
			.getResultList();

			em.getTransaction().begin();
		
			for (NotificationQueuPersistant notifP : listNotification) {
				if(StringUtil.isEmpty(notifP.getDevice_token())) {
					notifP.setIs_toRemove(true);
					em.merge(notifP);
					continue;
				}
				
				Builder message = Message.builder();
				Notification notification = Notification.builder()
											.setTitle(notifP.getTitle())
											.setBody(notifP.getMessage()).build();
				
				if(notifP.getParams().indexOf("|:|") != -1) {
					String[] data = notifP.getParams().split("\\|;\\|");
					for (String dataArray : data) {
						String[] dataMsg = dataArray.split("\\|:\\|");
						message.putData(dataMsg[0], (dataMsg.length==1 ? "" : dataMsg[1]));
					}
				}
				
				message.setToken(notifP.getDevice_token());
				message.setNotification(notification);
				
				boolean isSended = sendNotification(message.build());
				
				if(isSended) {
					notifP.setDate_envoie(DateUtil.getCurrentDate());
				} else {
					notifP.setNbr_erreur((notifP.getNbr_erreur()!=null?notifP.getNbr_erreur():0)+1);
				}
			
				if(notifP.getNbr_erreur() != null && notifP.getNbr_erreur() > 3) {
					notifP.setIs_toRemove(true);
				}
				em.merge(notifP);
			}
			
			em.getTransaction().commit();
			
		} catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
		} finally {
			em.close();	
		}
	}

	public static void sendSms(int token, String phoneNumber) {
		// TODO Auto-generated method stub
		
	}
}
