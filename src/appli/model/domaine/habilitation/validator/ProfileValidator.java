package appli.model.domaine.habilitation.validator;

import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.habilitation.bean.ProfileBean;
import appli.model.domaine.habilitation.dao.IProfileDao;
import appli.model.domaine.habilitation.persistant.ProfilePersistant;
import framework.model.common.service.MessageService;

@Named
public class ProfileValidator {

	@Inject
	private IProfileDao profileDao;

	/**
	 * @param profile
	 */
	private void updateCreateValidator(ProfileBean profile) {
		// Le libellé doit être unique
		if(profileDao.isNotUnique(profile, "libelle")){
			MessageService.addFieldMessage("profile.libelle", "Cette valeur existe déjà");
		}
	}

	/**
	 * @param profile
	 */
	public void update(ProfileBean profile) {
		updateCreateValidator(profile);
	}

	/**
	 * @param profile
	 */
	public void create(ProfileBean profile){
		updateCreateValidator(profile);
	}

	/**
	 * @param id
	 */
	public void delete(Long id){
		ProfilePersistant profilePersistant = profileDao.findById(id);
		if("ADMIN".equals(profilePersistant.getCode())){
			MessageService.addBannerMessage("Le profile administrateur ne peut pas être supprimé !");
			return;
		}
		if(profilePersistant.getList_user().size() > 0) {
//			String noms = "";
//			for(UserPersistant user :  profilePersistant.getList_user()) {
//				if(noms.indexOf(user.getFname() + " " + user.getSname() + " - ") == -1){
//					noms = noms + user.getFname() + " " + user.getSname() + " - ";
//				}
//			}
//			noms = noms.substring(0, noms.length()-2);
			MessageService.addBannerMessage("Ce profile est utilisé pour des utilisateurs.");
		}
	}
	
	/**
	 * @param ids
	 */
	public void delete_group(Long[] ids){
		for (Long artId : ids) {
			delete(artId);
		}
	}
}
