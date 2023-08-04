package appli.model.domaine.stock.dao.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Named;
import javax.persistence.Query;

import appli.controller.domaine.util_erp.ContextAppli.SOFT_ENVS;
import appli.model.domaine.stock.dao.IInventaireDao;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.persistant.ArticleStockInfoPersistant;
import appli.model.domaine.stock.persistant.InventairePersistant;
import framework.model.common.util.StrimUtil;
import framework.model.util.GenericJpaDao;

@Named
public class InventaireDao extends GenericJpaDao<InventairePersistant, Long> implements IInventaireDao{
	@SuppressWarnings("unchecked")
	@Override
	public List<ArticleStockInfoPersistant> getArticleInventaireByEmplacementAndFamille(Long emplacementId, Set<Long> familleIdsAll, List<Long> articleIds, Set<Long> articleIdsExclude) {
		boolean isRestau = SOFT_ENVS.restau.toString().equals(StrimUtil.getGlobalConfigPropertie("context.soft"));
		//
		String request = "from ArticlePersistant art "
				+ "where (is_disable is null or is_disable=0) "+(isRestau ? "and is_stock=true ":"")
				+ "and code != 'GEN' ";
	
		if(familleIdsAll.size() > 0){
			request = request + "and art.opc_famille_stock.id in (:familles) ";
		}
		
		if(articleIds.size() > 0){
			request = request + "and art.id in (:ids) ";
		}
		if(articleIdsExclude.size() > 0){
			request = request + "and art.id not in (:idsExclude) ";
		}
		request = request + "order by opc_famille_stock.b_left, art.code, art.libelle";
		
		Query query = getQuery(request);
		
		if(familleIdsAll.size() > 0){ 
			query.setParameter("familles", familleIdsAll);
		}
		if(articleIds.size() > 0){
			query.setParameter("ids", articleIds);
		}
		if(articleIdsExclude.size() > 0){
			query.setParameter("idsExclude", articleIdsExclude);
		}
		List<ArticlePersistant> listArticle = query.getResultList();
		
		// Stock infos
		request = "from ArticleStockInfoPersistant where opc_emplacement.id=:emplId "
				+(isRestau ? "and opc_article.is_stock=true ":"") 
				+ "and (opc_article.is_disable is null or opc_article.is_disable=0) ";
		if(familleIdsAll.size() > 0){
			request = request + "and opc_article.opc_famille_stock.id in (:familles) ";
		}
		if(articleIds.size() > 0){
			request = request + "and opc_article.id in (:ids) ";
		}
		if(articleIdsExclude.size() > 0){
			request = request + "and opc_article.id not in (:idsExclude) ";
		}
		request = request + "order by opc_article.opc_famille_stock.b_left, opc_article.code, opc_article.libelle";
		
		query = getQuery(request).setParameter("emplId", emplacementId);
		
		if(familleIdsAll.size() > 0){
			query.setParameter("familles", familleIdsAll);
		}
		if(articleIds.size() > 0){
			query.setParameter("ids", articleIds);
		}
		if(articleIdsExclude.size() > 0){
			query.setParameter("idsExclude", articleIdsExclude);
		}
		List<ArticleStockInfoPersistant> listStockInfo = query.getResultList();
		
		Map<Long, ArticleStockInfoPersistant> mapData = new LinkedHashMap<>();
		for (ArticleStockInfoPersistant artInfoP : listStockInfo) {
			mapData.put(artInfoP.getOpc_article().getId(), artInfoP);
		}
		
		List<ArticleStockInfoPersistant> listStockInfoTmp = new ArrayList<>();
		for (ArticlePersistant articlePersistant : listArticle) {
			ArticleStockInfoPersistant artViewP = mapData.get(articlePersistant.getId());
			if(artViewP == null){
				artViewP = new ArticleStockInfoPersistant();
				artViewP.setOpc_article(articlePersistant);
				
				listStockInfoTmp.add(artViewP);
			} else {
				listStockInfoTmp.add(artViewP);
			}
		}
		
		return listStockInfoTmp;
	}
}

//class SortByArticle implements Comparator<ArticleStockInfoPersistant>{
//	@Override
//	public int compare(ArticleStockInfoPersistant o1, ArticleStockInfoPersistant o2) {
//		return o1.getArticle_lib().compareTo(o2.getArticle_lib()) ;
//	}
//}
