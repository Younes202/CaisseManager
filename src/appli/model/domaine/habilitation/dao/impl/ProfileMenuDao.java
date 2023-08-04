package appli.model.domaine.habilitation.dao.impl;

import java.util.List;

import javax.inject.Named;

import appli.model.domaine.habilitation.dao.IProfileMenuDao;
import appli.model.domaine.habilitation.persistant.ProfileMenuPersistant;
import framework.model.util.GenericJpaDao;

@Named
public class ProfileMenuDao extends GenericJpaDao<ProfileMenuPersistant, Long> implements IProfileMenuDao{
	/* (non-Javadoc)
	 * @see monprojet.model.domaine.ad.dao.IProfileMenuDao#getProfileMenuByMenuAndProfile(java.lang.String, java.lang.Long)
	 */
	@Override
	public ProfileMenuPersistant getProfileMenuByMenuAndProfile(String mnuId, Long profileId) {
		String critRequest = "select profileMenu from ProfileMenuPersistant profileMenu " +
		"where profileMenu.menu_id=:menuId " +
		"and profileMenu.opc_profile.id=:profileId";

		return (ProfileMenuPersistant) getSingleResult(getQuery(critRequest)
			.setParameter("menuId", mnuId).setParameter("profileId", profileId));
	}

	/* (non-Javadoc)
	 * @see monprojet.model.domaine.ad.dao.IProfileMenuDao#getListMenuProfileByProfile(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProfileMenuPersistant> getListMenuProfileByProfile(Long profileId) { 
		return (List<ProfileMenuPersistant>)getQuery("from ProfileMenuPersistant pm where pm.opc_profile.id =:profile_id")
			.setParameter("profile_id", profileId).getResultList();
	}
}
