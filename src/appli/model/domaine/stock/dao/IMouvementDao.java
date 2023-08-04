package appli.model.domaine.stock.dao;

import appli.model.domaine.stock.persistant.MouvementPersistant;
import framework.model.util.IGenericJpaDao;

public interface IMouvementDao extends IGenericJpaDao<MouvementPersistant, Long>{
	
//	List<MouvementArticlePersistant> getListMvmArticle();
//	
//	Double moyenPrixHT(Long articleId, Long emplacementId);
//	
//	List<MouvementPersistant> getListEtatTva();
//	
	Integer max_numBl(String date);
}
