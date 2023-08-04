package appli.model.domaine.stock.service;

import java.util.List;

import appli.controller.domaine.stock.bean.DemandeTransfertBean;
import appli.model.domaine.stock.persistant.DemandeTransfertPersistant;
import framework.model.service.IGenericJpaService;

public interface IDemandeTransfertService extends IGenericJpaService<DemandeTransfertBean, Long> {

	List<DemandeTransfertPersistant> getListDemandeNonTransfere();

}
