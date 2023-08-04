package appli.model.domaine.stock.service;


import java.util.Date;
import java.util.List;

import appli.controller.domaine.administration.bean.EtatFinanceBean;
import appli.controller.domaine.stock.bean.CentraleSynchroBean;
import appli.model.domaine.stock.persistant.DemandeTransfertPersistant;
import appli.model.domaine.stock.persistant.centrale.CentraleEtsPersistant;
import appli.model.domaine.vente.persistant.JourneePersistant;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.service.IGenericJpaService;

public interface ICentraleSynchroService extends IGenericJpaService<CentraleSynchroBean, Long>{
	String synchroniserOutAll(Long syncId);
	void loadEtsCentrale();
	void addElementsToSynchronise(String[] elements, String[] etsCIds, boolean isDisable);
	String sendDemandeTransfertToCentrale(Long demandeId);
	void synchroniseDemandeTransfert(String requestData, String codeAuthSrc);
	void addElementToSynchronise(String opr, String codeAuth, Long elementId, boolean isAnnul);
	List<CentraleEtsPersistant> findActifsCentrale(); 
	/**
	 * Reçu de la centrale
	 */
	String synchroniseInAll(EtablissementPersistant etsP, String typeOpr, String requestData, boolean isToDisable);

	List<JourneePersistant> getJourneeInfosForCentrale(CentraleEtsPersistant centraleEtsP, Date dateDebut,
			Date dateFin);
	EtatFinanceBean getEtatForCentrale(CentraleEtsPersistant centraleEtsP, Date dateDebut, Date dateFin);
	/**
	 * Méthode instance centrale 
	 * @param dmdTransP
	 * @return
	 */
	boolean annulerDemandeTransfert(Long demandeId, String codeAuth);
	String sendAnnulationDemandeTransfertToCentrale(Long demandeId);
}
