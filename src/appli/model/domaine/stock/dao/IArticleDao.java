package appli.model.domaine.stock.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import appli.model.domaine.stock.persistant.ArticlePersistant;
import framework.model.util.IGenericJpaDao;

public interface IArticleDao extends IGenericJpaDao<ArticlePersistant, Long>{

	List<Object[]> getMontantStock(Date dateMax);

//	Map<Long, Object[]> getInfosStock(EntityManager em, Long emplacementId, Set<Long> articleIds);

	Object[] getInfosStock(EntityManager em, Long emplacementId, Long articleId);
}
