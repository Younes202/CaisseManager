package appli.model.domaine.habilitation.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.habilitation.bean.ProfileMenuBean;
import appli.model.domaine.habilitation.dao.IProfileDao;
import appli.model.domaine.habilitation.dao.IProfileMenuDao;
import appli.model.domaine.habilitation.persistant.ProfileMenuPersistant;
import appli.model.domaine.habilitation.persistant.ProfilePersistant;
import appli.model.domaine.habilitation.service.IProfileMenuService;
import framework.model.service.GenericJpaService;

@Named
public class ProfileMenuService extends GenericJpaService<ProfileMenuBean, Long> implements IProfileMenuService{
	@Inject
	private IProfileMenuDao profileMenuDao;
	@Inject
	private IProfileDao profileDao;

	/* (non-Javadoc)
	 * @see monprojet.model.domaine.ad.service.IProfileMenuService#createOrUpdate(front.controller.bean.ProfileMenuBean)
	 */
	@Override
	public void createOrUpdate(Long profileId, Map<String, String> mapMenu) {
		
		ProfilePersistant profilePersistant = profileDao.findById(profileId);
		
		for(String menuId : mapMenu.keySet()){
			String rights = mapMenu.get(menuId);
			ProfileMenuPersistant  profileMenuPers = profileMenuDao.getProfileMenuByMenuAndProfile(menuId, profileId);
			
			// Au moins un de séléctionné
			if(rights.indexOf("1") == -1){
				if(profileMenuPers != null){// Suppression si plus de droits 
					profileMenuDao.delete(profileMenuPers);
				}
			} else{
				if(profileMenuPers == null){
					profileMenuPers = new ProfileMenuPersistant();
					profileMenuPers.setMenu_id(menuId);
					profileMenuPers.setOpc_profile(profilePersistant);
					profileMenuPers.setRights(rights);
				} else{
					profileMenuPers.setRights(rights);
				}
				getEntityManager().merge(profileMenuPers);
			}
			
		}
	}

	/* (non-Javadoc)
	 * @see monprojet.model.domaine.ad.service.IProfileMenuService#getListMenuProfileByProfile(java.lang.Long)
	 */
	@Override
	public List<ProfileMenuPersistant> getListMenuProfileByProfile(Long profileId){
		List<ProfileMenuPersistant> resultList = ((IProfileMenuDao)profileMenuDao).getListMenuProfileByProfile(profileId);

		return resultList;
	}
	
	@Override
	public ProfileMenuPersistant getProfileMenuByMenuAndProfile(String mnuId, Long profileId) {
		String critRequest = "select profileMenu from ProfileMenuPersistant profileMenu " +
			"where profileMenu.menu_id=:menuId " +
			"and profileMenu.opc_profile.id=:profileId";

		return (ProfileMenuPersistant) getSingleResult(getQuery(critRequest)
			.setParameter("menuId", mnuId).setParameter("profileId", profileId));
	}
	
	@Override
	public Map<String, String> getCaisseRight(ProfileMenuPersistant profileMenuP) {
		Map<String, String> mapData = new HashMap<>();
		
		String valeur = profileMenuP.getRights().substring(profileMenuP.getRights().indexOf(";CAISEMPL:") + (";CAISEMPL:").length(), profileMenuP.getRights().indexOf(";CAISEMPL:") + (";CAISEMPL:").length()+1);
		mapData.put("param_CAISEMPL", valeur);
		//
		valeur = profileMenuP.getRights().substring(profileMenuP.getRights().indexOf(";CAISCLI:") + (";CAISCLI:").length(), profileMenuP.getRights().indexOf(";CAISCLI:") + (";CAISCLI:").length()+1);
		mapData.put("param_CAISCLI", valeur);
		//
		valeur = profileMenuP.getRights().substring(profileMenuP.getRights().indexOf(";CLOJRN:") + (";CLOJRN:").length(), profileMenuP.getRights().indexOf(";CLOJRN:") + (";CLOJRN:").length()+1);
		mapData.put("param_CLOJRN", valeur);
		//
		valeur = profileMenuP.getRights().substring(profileMenuP.getRights().indexOf(";IMPRAZ:") + (";IMPRAZ:").length(), profileMenuP.getRights().indexOf(";IMPRAZ:") + (";IMPRAZ:").length()+1);
		mapData.put("param_IMPRAZ", valeur);
		//
		valeur = profileMenuP.getRights().substring(profileMenuP.getRights().indexOf(";HISTOCAIS:") + (";HISTOCAIS:").length(), profileMenuP.getRights().indexOf(";HISTOCAIS:") + (";HISTOCAIS:").length()+1);
		mapData.put("param_HISTOCAIS", valeur);
		
		return mapData;
	}

}
