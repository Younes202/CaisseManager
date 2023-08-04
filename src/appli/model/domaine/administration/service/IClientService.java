package appli.model.domaine.administration.service;

import java.math.BigDecimal;
import java.util.List;

import appli.controller.domaine.personnel.bean.ClientBean;
import appli.model.domaine.compta.persistant.PaiementPersistant;
import appli.model.domaine.personnel.persistant.ClientPersistant;
import framework.model.service.IGenericJpaService;

public interface IClientService extends IGenericJpaService<ClientBean, Long> {

	void activerDesactiverElement(Long clientId);

	String generateNum();

	ClientBean getClientByLoginAndPw(String login, String pw);
	
	List<ClientPersistant> findByCin(String parameter);
	List<ClientPersistant> findByPhone(String parameter);

	List<ClientPersistant> findByNom(String nom);

	List<ClientPersistant> getClientsActifs();

	ClientPersistant getClientByCodeBarre(String codeBarre);

	ClientPersistant getClientByNumero(String code);

	void affecterEtatClient(ClientPersistant clientP);

	BigDecimal getSituationDetteClient(Long id);

	List<PaiementPersistant> getEcheancePasseClient(Long id);

	void addPortefeuille(Long clientId, boolean isSoldeNeg, BigDecimal mttPalafond, BigDecimal taux);

	Object[] detailEtatClient(Long clientId);
}
