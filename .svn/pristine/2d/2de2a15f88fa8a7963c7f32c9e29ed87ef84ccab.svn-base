package appli.model.domaine.habilitation.dao;

import java.util.List;

import appli.model.domaine.habilitation.persistant.ProfileMenuPersistant;
import framework.model.util.IGenericJpaDao;


public interface IProfileMenuDao extends IGenericJpaDao<ProfileMenuPersistant, Long>{
	/**
	 * @param mnuId
	 * @param profileId
	 * @return
	 */
	public ProfileMenuPersistant getProfileMenuByMenuAndProfile(String mnuId, Long profileId);

	/**
	 * @param profileId
	 * @return
	 */
	public List<ProfileMenuPersistant> getListMenuProfileByProfile(Long profileId);
}
