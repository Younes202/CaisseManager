package appli.model.domaine.caisse_restau.service;

import java.util.List;

import appli.controller.domaine.caisse_restau.action.CuisineJourneeBean;
import appli.model.domaine.caisse_restau.persistant.CuisineJourneePersistant;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import framework.model.service.IGenericJpaService;

public interface ICuisineJourneeService extends IGenericJpaService<CuisineJourneeBean, Long> {

	CuisineJourneePersistant getLastCuisineJournee(Long cuisineId);
	
	int getNbrInventaire(Long journeeId);
	
	void setCuisineJourneeInventaire(Long inventaireId);

	List<ArticlePersistant> getListArticlePresentoire(boolean isActifOnly);

	List<ArticlePersistant> getListArticleCuisine(boolean isActifOnly);
}
