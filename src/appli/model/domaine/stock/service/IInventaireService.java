package appli.model.domaine.stock.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import appli.controller.domaine.stock.bean.InventaireBean;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.persistant.ArticleStockInfoPersistant;
import appli.model.domaine.stock.persistant.InventaireDetailPersistant;
import appli.model.domaine.stock.persistant.InventairePersistant;
import appli.model.domaine.stock.persistant.MouvementPersistant;
import framework.model.service.IGenericJpaService;

public interface IInventaireService extends IGenericJpaService<InventaireBean, Long> {
	public List<ArticleStockInfoPersistant> getArticleInventaireByEmplacementAndFamille(Long emplacementId, Set<Long> familleIdsAll, List<Long> articleIds, Set<Long> articleIdsExclude);

	public MouvementPersistant validerInventaire(Long inventaireId);

	public MouvementPersistant annulerValidation(Long inventaireId);

	List<InventairePersistant> getInventaireNonValide(Long emplId);

	Date getMinDateInventaireNonValide(Long emplacementId);

	Date getMaxDateInventaireValide(Long emplacementId);

	public List<InventaireDetailPersistant> getInventaireEcart(Long invId);

	List<ArticlePersistant> getArticleByFamille(Set<Long> familleIdsAll, Set<Long> articleId,Set<Long> articleIdsExclued,  boolean excludeDisable);

	void mergeInventaire(InventaireBean inventaireBean, boolean isFromCuisine);

	List<InventairePersistant> getListInventaireByDate(Date dateDebut, Date dateFin);

}
