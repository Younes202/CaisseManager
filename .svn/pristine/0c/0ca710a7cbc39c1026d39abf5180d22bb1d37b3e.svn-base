package appli.model.domaine.compta.validator;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.compta.bean.CompteBean;
import appli.model.domaine.compta.dao.ICompteDao;
import appli.model.domaine.compta.service.impl.ExerciceService;
import appli.model.domaine.personnel.persistant.EmployePersistant;
import appli.model.domaine.stock.persistant.FournisseurPersistant;
import framework.controller.ContextGloabalAppli;
import framework.controller.ContextGloabalAppli.STATUT_EXERCICE;
import framework.model.beanContext.ComptePersistant;
import framework.model.common.service.MessageService;
import framework.model.common.util.BooleanUtil;

@Named
public class CompteValidator {
	@Inject
	private ICompteDao compteDao;
	
	/* (non-Javadoc)
	 * @see framework.model.service.commun.validator.ActionValidator#validate(java.lang.Object, java.lang.String)
	 */
	public void updateCreateValidator(CompteBean compteBean) {
		// Tester l'unicité du code
		if(compteDao.isNotUnique(compteBean, "code")){
			MessageService.addFieldMessageKey("compte.code", "msg.valeur.exist");
		} 
		if(ContextGloabalAppli.getExerciceBean() == null || !STATUT_EXERCICE.OUVERT.getStatut().equals(ContextGloabalAppli.getExerciceBean().getStatut_cloture())){
			MessageService.addBannerMessage("Aucun exercice ouvert n'a été trouvé.");
			return;
		}
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void update(CompteBean compteBean) {
		updateCreateValidator(compteBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void create(CompteBean compteBean){
		updateCreateValidator(compteBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void delete(Long id){
		ComptePersistant compte = compteDao.findById(id);
		
		if(!BooleanUtil.isTrue(compte.getIs_ajoute())){
			MessageService.addBannerMessage("On ne peut supprimer que les comptes ajoutés.");
			return;
		}
		
		// Vérifier si la clé n'est pas utilisé ailleur 
//		if(compte.getListEcriture().size() > 0){
//			MessageService.addBannerMessage("Ce compte ("+compte.getCode()+" - " + compte.getLibelle() + ") ne peut pas être supprimé car des écritures lui sont liées");
//		} else if(compte.getListCompteBancaire().size() > 0){
//			MessageService.addBannerMessage("Ce compte ("+compte.getCode()+" - " + compte.getLibelle() + ") ne peut pas être supprimé car il est lié àr un compte bancaire ("+compte.getListCompteBancaire().iterator().next().getLibelle()+")");
//		} else if(compte.getListRecDepense().size() > 0){
//			MessageService.addBannerMessage("Ce compte ("+compte.getCode()+" - " + compte.getLibelle() + ") ne peut pas être supprimé car il est utilisé pour une dépense ("+compte.getListRecDepense().iterator().next().getLibelle()+")");
//		}
		// Employe
		List<EmployePersistant> listEmpl = compteDao.getQuery("from EmployePersistant where opc_compte.id=:compteId")
				.setParameter("compteId", id).getResultList();
		if(listEmpl.size() > 0){
			MessageService.addBannerMessage("Ce compte ("+compte.getCode()+" - " + compte.getLibelle() + ") ne peut pas être supprimé car il est lié à  un employé ("+listEmpl.get(0).getNom()+" "+listEmpl.get(0).getPrenom()+")");
			return;
		}
		// Fournisseur
		List<FournisseurPersistant> listFourn = compteDao.getQuery("from FournisseurPersistant where opc_compte.id=:compteId")
				.setParameter("compteId", id).getResultList();
		if(listFourn.size() > 0){
			MessageService.addBannerMessage("Ce compte ("+compte.getCode()+" - " + compte.getLibelle() + ") ne peut pas être supprimé car il est lié à un fournisseur ("+listFourn.get(0).getLibelle()+")");
			return;
		}
	}
	
	/**
	 * @param ids
	 */
	public void delete_group(Long[] ids) {
		for(Long id : ids){
			delete(id);
		}
	}
}
