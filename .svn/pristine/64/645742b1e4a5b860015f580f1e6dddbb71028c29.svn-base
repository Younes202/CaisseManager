package appli.model.domaine.stock.service;

import java.util.List;

import appli.controller.domaine.stock.bean.EmplacementBean;
import appli.model.domaine.stock.persistant.EmplacementPersistant;
import framework.model.service.IGenericJpaService;

public interface IEmplacementService extends IGenericJpaService<EmplacementBean, Long> {

	void activerDesactiverElement(Long emplacementId);

	List<EmplacementPersistant> getEmplacementsInternes();
	List<EmplacementPersistant> getEmplacementsExternes();
	List<EmplacementPersistant> getEmplacementsExternes(String codeAuth);
	List<EmplacementPersistant> getListEmplacementActifs();
}
