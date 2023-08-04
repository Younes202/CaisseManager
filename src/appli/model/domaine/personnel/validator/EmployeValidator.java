package appli.model.domaine.personnel.validator;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.personnel.bean.EmployeBean;
import appli.model.domaine.personnel.dao.IEmployeDao;
import appli.model.domaine.personnel.persistant.EmployePersistant;
import appli.model.domaine.personnel.service.IEmployeService;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.stock.service.IFamilleService;
import framework.model.common.service.MessageService;

@Named
public class EmployeValidator {
	@Inject
	private IEmployeDao employeDao;
	@Inject
	private IFamilleService familleService;
	
	/* (non-Javadoc)
	 * @see model.service.commun.validator.ActionValidator#validate(java.lang.Object, java.lang.String)
	 */
	public void updateCreateValidator(EmployeBean employeBean) {
		// Le numéro de sécurité doit être unique
		if(employeDao.isNotUnique(employeBean, "numero")){
			MessageService.addFieldMessageKey("employe.numero", "msg.valeur.exist");
		}
		FamillePersistant opcFamille =null;
		if(employeBean.getId() == null && employeBean.getOpc_famille()!= null) {
		 opcFamille = (FamillePersistant) familleService.findById(FamillePersistant.class, employeBean.getOpc_famille().getId());
		
		List listData = employeDao.getQuery("SELECT COUNT(0) FROM EmployePersistant em WHERE em.opc_famille.id =: familleId")
				.setParameter("familleId", opcFamille.getId())
				.getResultList();
		int count = ((Number) listData.get(0)).intValue();
		if(opcFamille.getNbrPersonne()!= null) {
		if(count >= opcFamille.getNbrPersonne()) {
			MessageService.addBannerMessage("Vous depasser le nombre de membre permit pour cette famille");
			return;
		}
		}
		}

	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void update(EmployeBean employeBean) {
		updateCreateValidator(employeBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void create(EmployeBean employeBean){
		updateCreateValidator(employeBean);
	}
		
	public void delete(Long emplId){
		// User
		List listData = employeDao.getQuery("from UserPersistant where opc_employe.id=:emplId")
			.setParameter("emplId", emplId)
			.getResultList();
		if(listData.size() > 0){
			MessageService.addBannerMessage("Cet employé est associé à un utilisateur.");
			return;
		}
		// Mouvement
		listData = employeDao.getQuery("from CaisseMouvementPersistant where opc_employe.id=:emplId")
				.setParameter("emplId", emplId)
				.getResultList();
		if(listData.size() > 0){
			MessageService.addBannerMessage("Cet employé est associé à des mouvements caisse.");
			return;
		}
		
		// Mouvement
//		listData = employeDao.getQuery("from CaisseMouvementPersistant where opc_livreurU.id=:emplId")
//				.setParameter("emplId", emplId)
//				.getResultList();
//		if(listData.size() > 0){
//			MessageService.addBannerMessage("Cet employé est associé à des mouvements caisse.");
//			return;
//		}
				
		// Inventaire
		listData = employeDao.getQuery("from InventairePersistant where opc_responsable.id=:emplId or opc_saisisseur.id=:emplId")
				.setParameter("emplId", emplId)
				.getResultList();
		if(listData.size() > 0){
			MessageService.addBannerMessage("Cet employé est associé à des inventaires.");
			return;
		}
		
		listData = employeDao.getQuery("from InventairePersistant where opc_responsable.id=:emplId or opc_saisisseur.id=:emplId")
				.setParameter("emplId", emplId)
				.getResultList();
		if(listData.size() > 0){
			MessageService.addBannerMessage("Cet employé est associé à des inventaires.");
			return;
		}
		// SalairePersistant
		listData = employeDao.getQuery("from SalairePersistant where opc_employe.id=:emplId")
				.setParameter("emplId", emplId)
				.getResultList();
		if(listData.size() > 0){
			MessageService.addBannerMessage("Cet employé est associé à des salaires.");
			return;
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
