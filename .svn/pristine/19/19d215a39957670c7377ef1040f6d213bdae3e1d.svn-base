package appli.model.domaine.compta.validator;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import appli.model.domaine.administration.persistant.EcriturePersistant;
import appli.model.domaine.compta.dao.IEcritureDao;
import appli.model.domaine.compta.service.IExerciceService;
import framework.controller.ContextGloabalAppli;
import framework.model.beanContext.ExercicePersistant;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.DateUtil;

@Named
public class EcritureValidator {
	@Inject
	private IExerciceService execiceService;
	@Inject
	private IEcritureDao ecritureDao;
	
	/* (non-Javadoc)
	 * @see framework.model.service.commun.validator.ActionValidator#validate(java.lang.Object, java.lang.String)
	 */
	private void updateCreateValidator(List<EcriturePersistant> listEcritures) {
		
		if(listEcritures.size() == 0) {
			MessageService.addBannerMessage("Veuillez saisir au moins une ligne d'écriture.");
			return;
		}
		
		EcriturePersistant ecritureBean = listEcritures.get(0);
		ExercicePersistant currExercice = ContextGloabalAppli.getExerciceBean();
		// Le compte debit et le compte credit doivent être destinct
		if (ecritureBean.getDate_mouvement().before(currExercice.getDate_debut())){
			MessageService.addFieldMessage("ecriture.date", "La date doit être postérieure à la date de début de cet exercice ("+DateUtil.dateToString(currExercice.getDate_debut())+")");
			return;
		}
		
		// la date doit être dans un exercice ouvert
		if(!execiceService.is_exercice_date_ouvert(ecritureBean.getDate_mouvement())){
			MessageService.addFieldMessage("ecriture.date", "La date doit correspondre à un exercice ouvert");
			return;
		}
		
		// Le total débit doit être égal au total crédit
		BigDecimal totalDebit = null;
		BigDecimal totalCredit = null;
		for (EcriturePersistant ecritureB : listEcritures) {
			if(ecritureB.getSens().equals("D")){
				totalDebit = BigDecimalUtil.add(totalDebit, ecritureB.getMontant());
			} else{
				totalCredit = BigDecimalUtil.add(totalCredit, ecritureB.getMontant());
			}
		}
		
		if(totalDebit == null || totalCredit == null || totalDebit.compareTo(totalCredit) != 0){
			MessageService.addBannerMessage("Le total des montants débits doit être égal au total des montants crédits.");
		}
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void updateEcriture(List<EcriturePersistant> listEcritures, Long ecritureOrigineId){
		updateCreateValidator(listEcritures);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void createEcriture(List<EcriturePersistant> listEcritures){
		updateCreateValidator(listEcritures);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void delete(Long id){
		EcriturePersistant ecriturePersistant = ecritureDao.findById(id);
		boolean isExeEcritureOuvert = execiceService.is_exercice_date_ouvert(ecriturePersistant.getDate_mouvement());
		if(!isExeEcritureOuvert){
			// Il n'est pas possible de supprimer une écriture si exercice clos
			MessageService.addBannerMessage("Une écriture appartenant à un exercice non ouvert ne peut pas être supprimée");
		}
	}
	
	public void delete_from_ecriture(Long ectId) {
		EcriturePersistant ecritureP = ecritureDao.findById(ectId);
		boolean isExeEcritureOuvert = execiceService.is_exercice_date_ouvert(ecritureP.getDate_mouvement());
		if(!isExeEcritureOuvert){
			// Il n'est pas possible de supprimer une écriture si exercice clos
			MessageService.addBannerMessage("Une écriture appartenant à un exercice non ouvert ne peut pas être supprimée");
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
