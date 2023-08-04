package appli.model.domaine.habilitation.service;

import java.util.List;
import java.util.Map;

import appli.controller.domaine.habilitation.bean.ProfileMenuBean;
import appli.model.domaine.habilitation.persistant.ProfileMenuPersistant;
import framework.model.service.IGenericJpaService;

public interface IProfileMenuService extends IGenericJpaService<ProfileMenuBean, Long> {

	/**
	 * @param mapDroits
	 */
	void createOrUpdate(Long profileId, Map<String, String> mapDroits);

	/**
	 * @param profileId
	 * @return
	 */
	List<ProfileMenuPersistant> getListMenuProfileByProfile(Long profileId);

	ProfileMenuPersistant getProfileMenuByMenuAndProfile(String mnuId, Long profileId);

	Map<String, String> getCaisseRight(ProfileMenuPersistant profileMenuP);

}
