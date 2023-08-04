package appli.model.domaine.administration.validator;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.administration.bean.UserBean;
import appli.model.domaine.administration.dao.IUserDao;
import appli.model.domaine.administration.persistant.UserPersistant;
import appli.model.domaine.habilitation.service.impl.ProfileService;
import framework.model.common.service.MessageService;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;

@Named
public class UserValidator {
	@Inject
	private IUserDao userDao;
	
	/**
	 * @param userBean
	 */
	public void create(UserBean userBean) {
		UserPersistant userP = (UserPersistant) userDao.getSingleResult(userDao.getQuery("from UserPersistant "
				+ "where login=:login")
				.setParameter("login", userBean.getLogin()));
		// Le mail doit être unique
		if(userP != null){
			MessageService.addFieldMessage("user.login", "Ce login est déjà utilisé");
		}
		if(StringUtil.isEmpty(userBean.getPassword())){
			MessageService.addFieldMessage("user.password", "Ce champs est obligatoire");
		}
	}
	
	/**
	 * @param userBean
	 */
	public void update(UserBean userBean) {
		// Le libellé doit être unique
		UserPersistant userP = (UserPersistant) userDao.getSingleResult(userDao.getQuery("from UserPersistant where login=:login and id!=:currId")
				.setParameter("login", userBean.getLogin())
				.setParameter("currId", userBean.getId())
				);
		if(userP != null){
			MessageService.addFieldMessage("user.login", "Ce login est déjà utilisé");
		}
		if(StringUtil.isEmpty(userBean.getPassword())){
			MessageService.addFieldMessage("user.password", "Ce champs est obligatoire");
		}
	}
	
	/**
	 * @param userId
	 */
	public void activerDesactiverElement(Long userId) {
		UserPersistant userPersistant = userDao.findById(userId);
		if(userPersistant.isInProfile("ADMIN")
				&& userPersistant.getLogin().equals("ADMINISTRATEUR")){
			MessageService.addBannerMessage("L'utilisateur ayant le profil ADMINISTRATEUR ne peut pas être désactivé.");
		}
	}
	
	/**
	 * @param id
	 */
	public void delete(Long id){
		UserPersistant userPersistant = userDao.findById(id);
		if(userPersistant.isInProfile("ADMIN")
				&& userPersistant.getOpc_profile().getLibelle().equals("ADMINISTRATEUR")){
			MessageService.addBannerMessage("L'utilisateur ayant le profil ADMINISTRATEUR ne peut pas être supprimé.");
		}
		// Vérifier s'il est utilisé dans les commandes
		List listElement = null;
		
		if(!"erp".equals(StrimUtil.getGlobalConfigPropertie("context.soft"))){
			listElement = userDao.getQuery("from CaisseJourneePersistant where opc_user.id=:userId or opc_user_cloture.id=:userId")
				.setParameter("userId", id).getResultList();
			if(listElement.size() > 0){
				MessageService.addBannerMessage("Cet utilisateur est lié à des enregistrements dans la caisse.");
			}
			listElement = userDao.getQuery("from JourneePersistant where opc_user.id=:userId")
					.setParameter("userId", id).getResultList();
				if(listElement.size() > 0){
					MessageService.addBannerMessage("Cet utilisateur est lié à des enregistrements de journées caisse.");
				}
				
		} else{
			listElement = userDao.getQuery("from VenteMouvementPersistant where opc_user.id=:userId")
					.setParameter("userId", id).getResultList();
			if(listElement.size() > 0){
				MessageService.addBannerMessage("Cet utilisateur est lié à des enregistrements dans la caisse.");
			}
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
