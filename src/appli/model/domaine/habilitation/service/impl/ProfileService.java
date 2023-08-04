package appli.model.domaine.habilitation.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.habilitation.bean.ProfileBean;
import appli.model.domaine.habilitation.dao.IProfileDao;
import appli.model.domaine.habilitation.persistant.ProfilePersistant;
import appli.model.domaine.habilitation.service.IProfileService;
import appli.model.domaine.habilitation.validator.ProfileValidator;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.common.annotation.validator.WorkModelMethodValidator;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.DateUtil;
import framework.model.service.GenericJpaService;

@Named
@WorkModelClassValidator(validator=ProfileValidator.class)
public class ProfileService extends GenericJpaService<ProfileBean, Long> implements IProfileService{
	@Inject
	private IProfileDao profileDao;
	
	public enum PROFIL_CODE_ENUM {MANAGER, SUPERVISEUR, GESTIONNAIRE, LIVREUR, CAISSIER, ADMIN, SERVEUR};
	
	@Override
	public void create(ProfileBean e) {
		if(e.getCode() == null) {
			e.setCode(DateUtil.dateToString(DateUtil.getCurrentDate(), "ddMMyyyyHHmmss"));
		}
		super.create(e);
	}
	
	@Override
	public ProfileBean update(ProfileBean e) {
		ProfilePersistant profilDB = profileDao.findById(e.getId());
		profilDB.setLibelle(e.getLibelle());
		
		if(!profilDB.getCode().equals("ADMIN")) {
//			profilDB.setIs_backoffice(e.getIs_backoffice());
//			profilDB.setIs_caisse( e.getIs_caisse() == null ? false : true );
			profilDB.setEnvs(e.getEnvs());
		}
		profileDao.update(profilDB);
		
		return e;
	}
	
	@Override
	public ProfilePersistant getProfilAdmin(){
		return (ProfilePersistant) profileDao.getQuery("from ProfilePersistant profile where profile.code=:code_admin")
				.setParameter("code_admin", "ADMIN").getSingleResult(); 
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ProfilePersistant> getListProfile(){ 
		return profileDao.getQuery("from ProfilePersistant profile where "
				+ " profile.code!=:code_admin ")
				.setParameter("code_admin", "ADMIN")
				.getResultList();
	}
	
	@Override
	@Transactional
	public void activerDesactiverElement(Long userId) {
		ProfilePersistant profilePersistant = profileDao.findById(userId);
		profilePersistant.setIs_desactive(BooleanUtil.isTrue(profilePersistant.getIs_desactive()) ? false : true);
		//
		getEntityManager().merge(profilePersistant);
	}

	@Override
	@Transactional
	@WorkModelMethodValidator
	public void delete(Long id) {
		// Supression des profiles menus
		 profileDao.getQuery("delete from ProfileMenuPersistant where opc_profile.id=:profileId")
		 	.setParameter("profileId", id).executeUpdate();
		 profileDao.getEntityManager().flush();
		 //
		 super.delete(id);
	}
}
