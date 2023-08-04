package appli.model.domaine.administration.service.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import appli.model.domaine.administration.persistant.EtatFinancePersistant;
import appli.model.domaine.stock.dao.IFamilleDao;
import framework.model.common.service.MessageService;
import framework.model.common.util.BooleanUtil;

@Named
public class EtatFinanceValidator {
	@Inject
	IFamilleDao familleDao;
	
	public void annulerClotureMois(Long etatId) {
		EtatFinancePersistant etatP = (EtatFinancePersistant) familleDao.findById(EtatFinancePersistant.class, etatId);
		
		List<EtatFinancePersistant> listEtat = familleDao.getQuery("from EtatFinancePersistant where date_etat>:dateDebut")
				.setParameter("dateDebut", etatP.getDate_etat()).getResultList();
		//
		if(!listEtat.isEmpty()) {
			MessageService.addBannerMessage("Des mois postérieurs clos ont été trouvés. Veuillez annuler leurs clôture avant de continuer.");
			return;
		}
		if(BooleanUtil.isTrue(etatP.getIs_purge())){
			MessageService.addBannerMessage("Ce mois est déjà purgé. Il n'est plus possible d'annuler sa clôture.");
		}
	}

	public void cloreMois(Date dateDebut, Date dateFin) {
		
		List<EtatFinancePersistant> listEtat = familleDao.getQuery("from EtatFinancePersistant where date_etat>:dateDebut")
				.setParameter("dateDebut", dateDebut).getResultList();
		
		if(!listEtat.isEmpty()) {
			MessageService.addBannerMessage("Des mois postérieurs clos ont été trouvés. Veuillez annuler leurs clôture avant de continuer.");
			return;
		}
		
		EtatFinancePersistant etatP = (EtatFinancePersistant) familleDao.getSingleResult(familleDao.getQuery("from EtatFinancePersistant where date_etat=:dateDebut")
				.setParameter("dateDebut", dateDebut));
			
		if(etatP != null) {
			MessageService.addBannerMessage("Ce mois est déjà clos.");
		}
		
//		Date[] minMaxDate = journeeService.getMinMaxDate();
//		Date dateDebutJournee = minMaxDate[0];
//		Date dateFinJournee = minMaxDate[1];
		//
//		if((dateFinJournee != null && dateDebut.after(dateFinJournee)) || (dateDebutJournee != null && dateFin.before(dateDebutJournee))){
//			MessageService.addBannerMessage("Ce mois ne contient aucune opération de vente. Le mois doit être entre "+DateUtil.dateToString(dateDebutJournee)+" et "+DateUtil.dateToString(dateFinJournee));
//		}
	}	
}

