package appli.model.domaine.administration.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import appli.controller.domaine.administration.bean.EtatFinanceBean;
import appli.model.domaine.administration.persistant.EtatFinancePaiementPersistant;
import appli.model.domaine.administration.persistant.EtatFinancePersistant;
import framework.model.service.IGenericJpaService;

public interface IEtatFinanceService extends IGenericJpaService<EtatFinanceBean, Long> {
	EtatFinanceBean getEtatFinanceBean(Date dateDebut, Date dateFin);

	void cloreMois(Date dateDebut, Date dateFin);
	EtatFinanceBean getEtatByDate(Date dateDebut);
	void annulerClotureMois(Long etatId);
	EtatFinancePersistant getMoisClosNonPurge();
	boolean isMoisClos(Date dateRef);
	EtatFinancePaiementPersistant setDataPaiement(String type, List<Object[]> result);
	BigDecimal getMttEtatTva(Date dateDebut, Date dateFin);
}
