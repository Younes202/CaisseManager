package appli.model.domaine.administration.validator;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.administration.bean.CompteBancaireFondsBean;
import appli.model.domaine.administration.dao.ICompteBancaireFondsDao;
import appli.model.domaine.administration.persistant.CompteBancaireFondsPersistant;
import appli.model.domaine.administration.persistant.EtatFinancePersistant;
import framework.model.common.service.MessageService;
import framework.model.common.util.DateUtil;

@Named
public class CompteBancaireFondsValidator {
	@Inject
	private ICompteBancaireFondsDao compteBancaireFondsDao;
	
	/* (non-Javadoc)
	 * @see model.service.commun.validator.ActionValidator#validate(java.lang.Object, java.lang.String)
	 */
	public void updateCreateValidator(CompteBancaireFondsBean mvmFonds) {
		if(mvmFonds.getOpc_banque_dest().getId().equals(mvmFonds.getOpc_banque_source().getId())){
			MessageService.addBannerMessage("Le compte source et destination doivent être différents");
		}
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void update(CompteBancaireFondsBean mvmFonds) {
		updateCreateValidator(mvmFonds);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void create(CompteBancaireFondsBean mvmFonds){
		updateCreateValidator(mvmFonds);
		
		Calendar cal = DateUtil.getCalendar(mvmFonds.getDate_mouvement());
		Date dateDebut = DateUtil.stringToDate("01/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR));
		//
		EtatFinancePersistant etatP = (EtatFinancePersistant) compteBancaireFondsDao.getSingleResult(compteBancaireFondsDao.getQuery("from EtatFinancePersistant where date_etat=:dateDebut")
				.setParameter("dateDebut", dateDebut));
		
		if(etatP != null){
			MessageService.addBannerMessage("La date du mouvement appartient à un mois clos.");
		}
	}
	
	public void delete(Long id) {
		CompteBancaireFondsPersistant cbP = compteBancaireFondsDao.findById(id);
		
		Calendar cal = DateUtil.getCalendar(cbP.getDate_mouvement());
		Date dateDebut = DateUtil.stringToDate("01/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR));
		//
		EtatFinancePersistant etatP = (EtatFinancePersistant) compteBancaireFondsDao.getSingleResult(compteBancaireFondsDao.getQuery("from EtatFinancePersistant where date_etat=:dateDebut")
				.setParameter("dateDebut", dateDebut));
		
		if(etatP != null){
			MessageService.addBannerMessage("Ce mouvement appartient à un mois clos.");
		}
	}
}
