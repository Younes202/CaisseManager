package appli.model.domaine.habilitation.service;

import java.util.List;

import appli.controller.domaine.habilitation.bean.ProfileBean;
import appli.model.domaine.habilitation.persistant.ProfilePersistant;
import framework.model.service.IGenericJpaService;

public interface IProfileService extends IGenericJpaService<ProfileBean, Long> { 

	ProfilePersistant getProfilAdmin();

	List<ProfilePersistant> getListProfile();

	void activerDesactiverElement(Long workIdLong);

}
