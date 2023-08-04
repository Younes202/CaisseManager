package appli.model.domaine.administration.service;

import java.util.List;
import java.util.Map;

import appli.controller.domaine.administration.bean.EtablissementBean;
import appli.model.domaine.personnel.persistant.EmployePersistant;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.service.IGenericJpaService;


public interface IEtablissementService extends IGenericJpaService<EtablissementBean, Long>{

	void activerDesactiverElement(Long workIdLong);

	void majPositionLivreur(EmployePersistant livreur, String str_position);

	Map<String, List<EtablissementPersistant>> getListEtablissement();
}
