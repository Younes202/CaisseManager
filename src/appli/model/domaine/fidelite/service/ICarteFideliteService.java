package appli.model.domaine.fidelite.service;

import appli.controller.domaine.fidelite.bean.CarteFideliteBean;
import appli.model.domaine.fidelite.persistant.CarteFidelitePersistant;
import framework.model.service.IGenericJpaService;

public interface ICarteFideliteService extends IGenericJpaService<CarteFideliteBean, Long> {
	
	void activerDesactiverElement(Long carteId);

	void autoPurgeElement(Long carteId);

	CarteFidelitePersistant getCarteOrCarteParDefaut(Long carteId);
	
}
