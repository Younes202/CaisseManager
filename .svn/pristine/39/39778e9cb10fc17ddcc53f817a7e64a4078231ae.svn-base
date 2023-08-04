package appli.model.domaine.personnel.service;

import appli.controller.domaine.personnel.bean.FraisBean;
import appli.model.domaine.compta.persistant.PaiementPersistant;
import framework.model.service.IGenericJpaService;

public interface IFraisService extends IGenericJpaService<FraisBean, Long> {

	void rembourserDemande(FraisBean fraisBean, PaiementPersistant paiementP);

	void annulerDemande(FraisBean fraisBean);

}
