package appli.model.domaine.stock.dao.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import appli.model.domaine.stock.dao.IArticleDao;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import framework.model.util.GenericJpaDao;

@Named
public class ArticleDao extends GenericJpaDao<ArticlePersistant, Long> implements IArticleDao {
//	@Override
//	public Map<Long, Object[]> getInfosStock(EntityManager em, Long emplacementId, Set<Long> articleIds){
//		Map<Long, Object[]> mapArticleInfos = new HashMap();
////		String reqMax = "select det.article_id, max(det.id) as maxId from mouvement_article det inner join mouvement mv on mv.id=det.mouvement_id "+  
////				"where mv.inventaire_id is not null and det.article_id IN :articleId and mv.emplacement_id=:emplacementId group by det.article_id";
////		
////		Query query = getNativeQuery(em, reqMax)
////					.setParameter("articleId", articleIds)
////					.setParameter("emplacementId", emplacementId);
////		
////		List<Object[]> maxIdsArt = query.getResultList();
////		Map<Long, Integer> mapArticleMaxInv = new HashMap<>();
////		for (Object[] object : maxIdsArt) {
////			int maxIdInt = (object[1] == null) ? 0 : ((BigInteger)object[1]).intValue();
////			mapArticleMaxInv.put((Long)object[0], maxIdInt);
////		}
//		
//		String requete = "select" +
//				"				art.id,			                                                           " + 
//				"	            SUM(                                                                       " + 
//				"					case                                                                    " + 
//				"						when ((type_mvmnt='a' or type_mvmnt='t' or type_mvmnt='tr' or type_mvmnt='rt') and mvm.destination_id=empl.id) then IFNULL(artmvm.quantite,0)" + 
//				"						when (type_mvmnt='i' and mvm.emplacement_id=empl.id) then IFNULL(artmvm.quantite,0) " + 
//				"						else 0                                                                                        " + 
//				"					end                                                                                               " + 
//				"				) as qte_entree,                                                                                      " + 
//				"		        SUM(                                                                                                 " + 
//				"					case                                                                                              " + 
//				"						when ((type_mvmnt='av' or type_mvmnt='v' or type_mvmnt='vc' or type_mvmnt='t' or type_mvmnt='p' or type_mvmnt='c' or type_mvmnt='tr') and mvm.emplacement_id=empl.id) then IFNULL(artmvm.quantite,0)" + 
//				"					else 0                                                                                                                                                   " + 
//				"					end                                                                                                                                                      " + 
//				"			) as qte_sortie                                                                                                                                                  " + 
//				"	    from mouvement mvm                                                                                                                                                  " + 
//				"	    inner join mouvement_article artmvm on mvm.id=artmvm.mouvement_id                                                                                                   " + 
//				"		inner join article art on artmvm.article_id=art.id                                                                                                                  " + 
//				"	    left join emplacement empl on (mvm.emplacement_id=empl.id or mvm.destination_id=empl.id)                                                                            " +
//				"		left join (select det.article_id, max(det.id) as maxId from mouvement_article det inner join mouvement mv on mv.id=det.mouvement_id 								" + 
//				"				where mv.inventaire_id is not null and mv.emplacement_id=:emplacementId group by det.article_id) artInv	on artInv.article_id=art.id 						" +
//				"		where art.is_stock = 1 and artmvm.id>=IFNULL(artInv.maxId, 0) and empl.id=:emplacementId and art.id IN :articleId group by art.id                                                                    "; 
//		
//		Query query = getNativeQuery(em, requete)
////			.setParameter("maxId", maxIdInt)
//			.setParameter("articleId", articleIds)
//			.setParameter("emplacementId", emplacementId);
//		List<Object[]> result = query.getResultList();
//		 
//		for (Object[] objects : result) {
//			mapArticleInfos.put(Long.valueOf(""+objects[0]), new Object[] {objects[1], objects[2]});
//		}
//		
//		return mapArticleInfos;
//	}
	
	@Override
	public Object[] getInfosStock(EntityManager em ,Long emplacementId, Long articleId){
		String reqMax = "select max(det.id) as maxId from mouvement_article det inner join mouvement mv on mv.id=det.mouvement_id "+  
				"where mv.inventaire_id is not null and det.article_id=:articleId and mv.emplacement_id=:emplacementId"+getEtsCondition("det", true);
		
		Query query = getNativeQuery(em, reqMax).setParameter("articleId", articleId).setParameter("emplacementId", emplacementId);
		BigInteger maxId = (BigInteger) query.getSingleResult();
		int maxIdInt = (maxId == null) ? 0 : maxId.intValue();
		
		String requete = "select                                                                             " + 
				"	            SUM(                                                                       " + 
				"					case                                                                    " + 
				"						when ((type_mvmnt='a' or type_mvmnt='t' or type_mvmnt='rt' or type_mvmnt='tr') and mvm.destination_id=empl.id) then IFNULL(artmvm.quantite,0)" + 
				"						when (type_mvmnt='i' and mvm.emplacement_id=empl.id) then IFNULL(artmvm.quantite,0) " + 
				"						else 0                                                                                        " + 
				"					end                                                                                               " + 
				"				) as qte_entree,                                                                                      " + 
				"		        SUM(                                                                                                 " + 
				"					case                                                                                              " + 
				"						when ((type_mvmnt='av' or type_mvmnt='v' or type_mvmnt='vc' or type_mvmnt='t' or type_mvmnt='p' or type_mvmnt='c' or type_mvmnt='tr') and mvm.emplacement_id=empl.id) then IFNULL(artmvm.quantite,0)" + 
				"					else 0                                                                                                                                                   " + 
				"					end                                                                                                                                                      " + 
				"			) as qte_sortie                                                                                                                                                  " + 
				"	    from mouvement mvm                                                                                                                                                  " + 
				"	    inner join mouvement_article artmvm on mvm.id=artmvm.mouvement_id                                                                                                   " + 
				"		inner join article art on artmvm.article_id=art.id                                                                                                                  " + 
				"	    left join emplacement empl on (mvm.emplacement_id=empl.id or mvm.destination_id=empl.id)                                                                            " + 
				"	    where artmvm.id>=:maxId and empl.id=:emplacementId and art.id=:articleId"+getEtsCondition("mvm", true); 
		
		query = getNativeQuery(em, requete)
			.setParameter("maxId", maxIdInt)
			.setParameter("articleId", articleId)
			.setParameter("emplacementId", emplacementId);
		
		return (Object[]) query.getSingleResult();
	}
	
	@Override
	public List<Object[]> getMontantStock(Date dateMax){
		String requete = "select a.emplacement_id as emplacement_id, "
				+ "a.emplacement_titre as emplacement, "
				+ "(sum(a.qte_entree*a.prix_achat_ttc)-sum(a.qte_sortie*a.prix_achat_ttc)) as montant "
		+ "from ( "
				+ "select "
				+ "art.prix_achat_ht as prix_achat_ht, "
				+ "val.code as taux_tva, "
				+ "(art.prix_achat_ht * val.code)/100 as mtt_tva, "
				+ "(art.prix_achat_ht + ((art.prix_achat_ht * val.code)/100)) as prix_achat_ttc, "
				+ "avg(case  "
					+ "when (type_mvmnt='a') then artmvm.prix_ht " 
					+ "end "
				+ ") as prix_achat_moyen_ht, " 
		
        + "empl.id as emplacement_id, "
        + "empl.titre as emplacement_titre, "
        + "SUM( " 
        + "case  "
				+ "when ((type_mvmnt='a' or type_mvmnt='t' or type_mvmnt='tr') and mvm.destination_id=empl.id) then artmvm.quantite " 
				+ "else 0  "
			+ "end "
		+ ") as qte_entree, " 
        + "SUM(  "
        + "case   "
				+ "when ((type_mvmnt='av' or type_mvmnt='v' or type_mvmnt='vc' or type_mvmnt='t' or type_mvmnt='p' or type_mvmnt='c' or type_mvmnt='tr') and mvm.emplacement_id=empl.id) then artmvm.quantite " 
				+ "when (type_mvmnt='i' and mvm.emplacement_id=empl.id) then (artmvm.quantite*-1)  "
				+ "else 0  "
				+ "end  "
		+ ") as qte_sortie "
    + "from mouvement mvm  "
    + "inner join mouvement_article artmvm on mvm.id=artmvm.mouvement_id " 
	+ "inner join article art on artmvm.article_id=art.id  "
	+ "inner join famille fam on fam.id=art.famille_stock_id " 
    + "left join emplacement empl on (mvm.emplacement_id=empl.id or mvm.destination_id=empl.id) "
    + "left join val_type_enumere val on val.id=art.tva_enum_id "
	+ "where mvm.date_mouvement<=:dateMax "+getEtsCondition("mvm", true)
	+ "group by empl.id "
	+ ") a";

		return getNativeQuery(requete)
					.setParameter("dateMax", dateMax)
					.getResultList();
	}
}
