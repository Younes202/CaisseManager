package appli.model.domaine.stock.dao;

import java.util.List;
import java.util.Set;

import appli.model.domaine.stock.persistant.ArticleStockInfoPersistant;
import appli.model.domaine.stock.persistant.InventairePersistant;
import framework.model.util.IGenericJpaDao;

public interface IInventaireDao extends IGenericJpaDao<InventairePersistant, Long>{

	List<ArticleStockInfoPersistant> getArticleInventaireByEmplacementAndFamille(Long emplacementId,
			Set<Long> familleIdsAll, List<Long> articleIds, Set<Long> articleIdsExclude);
		
}
