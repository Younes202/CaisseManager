package appli.model.domaine.administration.validator;

import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.administration.bean.VacanceBean;
import appli.model.domaine.administration.dao.IVacanceDao;
import appli.model.domaine.administration.persistant.VacancePersistant;
import framework.model.common.service.MessageService;
import framework.model.common.util.DateUtil;

@Named
public class VacanceValidator {
	@Inject
	private IVacanceDao vacanceDao;
	
	/*
	 * 
	 */
	public void updateCreateValidator(VacanceBean vacanceBean) {
		if(vacanceDao.isNotUnique(vacanceBean, "id")){
			MessageService.addFieldMessage("vacance.id", "Cette valeur existe déjà");
		}
		choixDate(vacanceBean);
		checkNbrJours(vacanceBean);
		
	}
		
		// Date debut doit être inférieure à la date de fin
		private void choixDate(VacanceBean vacanceBean) {
			int nbrCurrent = DateUtil.getDiffDays(vacanceBean.getDate_debut(), vacanceBean.getDate_fin());
	
			if(nbrCurrent <= 0 ){
				MessageService.addBannerMessage("La date de début est supérieur à la date de fin" );
			}
		}
	
		// Nbr jours vacances ne doit pas dépasser 150 jours
		private void checkNbrJours(VacancePersistant vacanceBean){
			int nbrJoursAutor = 150;
			int nbrCurrent = DateUtil.getDiffDays(vacanceBean.getDate_debut(), vacanceBean.getDate_fin());
			
			if(nbrCurrent >= nbrJoursAutor){
				MessageService.addBannerMessage("Vous avez dépassé le nombre des jours de vacance autorisés ("+nbrJoursAutor+") jours" );
			}
		}
		
		
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void update(VacanceBean vacanceBean) {
		updateCreateValidator(vacanceBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void create(VacanceBean vacanceBean){
		updateCreateValidator(vacanceBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void delete(Long id){
/*		List<VacancePersistant> listVacance = vacanceDao.getQuery("from VacancePersistant "
				+ "where opc_vacance.id=:vacanceId")
				.setParameter("vacanceId", id)
				.getResultList();
		if(listVacance.size() > 0){
			MessageService.addBannerMessage("Vacances prises");
		}*/
	}
	
}
