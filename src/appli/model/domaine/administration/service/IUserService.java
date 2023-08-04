package appli.model.domaine.administration.service;

import java.util.Date;
import java.util.List;

import appli.controller.domaine.administration.bean.UserBean;
import appli.model.domaine.administration.persistant.NotificationPersistant;
import appli.model.domaine.administration.persistant.UserPersistant;
import appli.model.domaine.caisse.persistant.LivreurPositionPersistant;
import appli.model.domaine.vente.persistant.CaisseJourneePersistant;
import appli.model.domaine.vente.persistant.JourneePersistant;
import framework.model.service.IGenericJpaService;

public interface IUserService extends IGenericJpaService<UserBean, Long> {
	void activerDesactiverElement(Long userId);
	UserBean getUserByLoginAndPw(String login, String pw);
	UserPersistant getUserByLogin(String login);
	void changerPw(String old, String newpw, String confirmpw);
	UserBean getUserByBadge(String badge);
	void updateEcheanceRestaurant(Date dateEcheance);
	boolean checkCodeAbonnement(String codeAbonnement);
	void updateFlagUpdate(String version, Date dateVersion);
	List<UserPersistant> getListUserActifsByProfile(String profileCode);
	void updateAboonement(String retourAbonnement);
	JourneePersistant getLastJourne();
	CaisseJourneePersistant getJourneCaisseOuverte(Long id);
	Object getListAfficheurs(Long id);
	List<UserPersistant> findAllUser(boolean actifOnly);
	List<NotificationPersistant> getListNotification(UserBean userBean);
	void updateNotification(Long userId, Long notifId);
	void synchroniseNotifications(List<NotificationPersistant> listNotif);
	void updateLastCheckCloud(Long etsId);
//	List<UserPersistant> getListUserActifsByPoste(String postCode);
	List<LivreurPositionPersistant> getPositionsLivreur(Long livreurId, Date dateDebut, Date dateFin);
	void updateNewLivreurInfosTmp();
	void unlockCommandes(Long cmdId, Long userId);
}
