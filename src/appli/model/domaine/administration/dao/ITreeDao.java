package appli.model.domaine.administration.dao;

import appli.model.domaine.stock.persistant.ArticlePersistant;
import framework.model.util.IGenericJpaDao;

public interface ITreeDao extends IGenericJpaDao<ArticlePersistant, Long>{
	String getNextCodeTree(Class treeClass, Long parentId);
}
