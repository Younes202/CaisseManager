package appli.controller.domaine.caisse;

import appli.model.domaine.vente.persistant.CaisseJourneePersistant;
import appli.model.domaine.vente.persistant.CaissePersistant;
import appli.model.domaine.vente.persistant.JourneePersistant;
import framework.model.common.service.MessageService;

public class ContextAppliCaisse {
	/**
	 * @return
	 */
	public static CaissePersistant getCaisseBean() {
		return (CaissePersistant) MessageService.getGlobalMap().get("CURRENT_CAISSE");
	}
	
	/**
	 * @return
	 */
	public static CaisseJourneePersistant getJourneeCaisseBean() {
		return (CaisseJourneePersistant) MessageService.getGlobalMap().get("CURRENT_JOURNEE_CAISSE");
	}
	/**
	 * @return
	 */
	public static JourneePersistant getJourneeBean() {
		return (JourneePersistant) MessageService.getGlobalMap().get("CURRENT_JOURNEE");
	}

}
