package appli.model.domaine.caisse.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import appli.controller.domaine.administration.bean.UserBean;
import appli.controller.domaine.personnel.bean.ClientBean;
import appli.model.domaine.personnel.persistant.ClientPersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import framework.model.service.IGenericJpaService;

public interface ICaisseMobileService extends IGenericJpaService<UserBean, Long> {

	void createCompte(UserBean userBean, ClientBean clientBean);

	void desactiverCompte(String login, String pw);

	void activerCompte(String login, String pw);

	BigDecimal[] getSolde(ClientPersistant clientPersistant);

	int isNoter(ClientPersistant clientPersistant, Date dateAvis);

	List<CaisseMouvementPersistant> listHistorique(ClientPersistant clientPersistant);

	List<CaisseMouvementPersistant> getCmdClient(Long id);

	int getTotalCmdValidee(Long id);

	List<ClientPersistant> getListClientNonValide();

	void statutClient(Long workIdLong, String statut);

	void statutCommande(Long mvmId, String statut);

	//MVM, CPT, LIV
	void notifierClient(String type, Long elementId, Long clientId, String msg);

	List<CaisseMouvementPersistant> getListCmdNonValide(String tp);

//	void changerStatutLivreur(Long emplId, String newStatut);
}
