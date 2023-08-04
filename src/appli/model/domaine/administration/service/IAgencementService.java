package appli.model.domaine.administration.service;


import java.util.List;

import appli.controller.domaine.administration.bean.AgencementBean;
import appli.model.domaine.administration.persistant.AgencementPersistant;
import framework.model.service.IGenericJpaService;

public interface IAgencementService extends IGenericJpaService<AgencementBean, Long>{

	void manageCalendrier(Long agencementId);

	List<AgencementPersistant> findAgencementByCalndrier();

}
