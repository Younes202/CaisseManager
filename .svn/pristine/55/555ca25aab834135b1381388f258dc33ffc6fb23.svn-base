package appli.model.domaine.fidelite.service;

import java.util.List;

import appli.controller.domaine.fidelite.bean.CarteFideliteClientBean;
import appli.model.domaine.fidelite.persistant.CarteFideliteClientPersistant;
import appli.model.domaine.personnel.persistant.ClientPersistant;
import framework.model.service.IGenericJpaService;

public interface ICarteFideliteClientService extends IGenericJpaService<CarteFideliteClientBean, Long> {

	void activerDesactiverElement(Long carteclientId);

	CarteFideliteClientPersistant getClientCarteByCodeBarre(String codeBarre);

	CarteFideliteClientPersistant getCarteClientActive(Long clientId);

	void offrirPoints(Long carteClientId, Integer nbrPoint);

	void deletePointsGagnes(Long carteClientPointId);

	void deletePointsConso(Long carteClientConsoId);

	List<ClientPersistant> getClientWithCarteActive();
}
