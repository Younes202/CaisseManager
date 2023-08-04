package appli.model.domaine.personnel.validator;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.personnel.bean.PosteBean;
import appli.model.domaine.personnel.dao.IPosteDao;
import appli.model.domaine.personnel.persistant.EmployePersistant;
import framework.model.common.service.MessageService;

@Named
public class PosteValidator {
	@Inject
	private IPosteDao posteDao;
	
	/*
	 * 
	 */
	public void updateCreateValidator(PosteBean posteBean) {
		if(posteDao.isNotUnique(posteBean, "intitule")){
			MessageService.addFieldMessage("poste.intitule", "Cette valeur existe déjà.");
		}
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void update(PosteBean posteBean) {
		updateCreateValidator(posteBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void create(PosteBean posteBean){
		updateCreateValidator(posteBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void delete(Long id){
		List<EmployePersistant> listPost = posteDao.getQuery("from EmployePersistant where opc_poste.id=:currId")
			.setParameter("currId", id)
			.getResultList();
		
		if(!listPost.isEmpty()){
			MessageService.addBannerMessage("Ce poste est lié à des employés.");
		}
	}
	
}
