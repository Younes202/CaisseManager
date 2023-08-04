package appli.model.domaine.caisse.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.stock.bean.ArticleBean;
import appli.model.domaine.caisse.persistant.ArticleBalancePersistant;
import appli.model.domaine.caisse.service.IArticle2Service;
import appli.model.domaine.stock.persistant.ArticleDetailPersistant;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.persistant.FamilleCuisinePersistant;
import appli.model.domaine.stock.service.IArticleService;
import appli.model.domaine.stock.validator.ArticleValidator;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.StringUtil;
import framework.model.service.GenericJpaService;

@WorkModelClassValidator(validator=ArticleValidator.class)
@Named
@SuppressWarnings("unchecked")
public class Article2Service extends GenericJpaService<ArticleBean, Long> implements IArticle2Service{
	@Inject
	private IArticleService articleService;
	
	@Override
	@Transactional
	public void dupliquerEnFicheArticle(String familleCuisine, String dest, Long[] listComposantsIds) {
		FamilleCuisinePersistant famille = (FamilleCuisinePersistant)articleService.findById(FamilleCuisinePersistant.class, Long.valueOf(familleCuisine));
		
		for (Long composantId : listComposantsIds) {
			ArticlePersistant composantP = articleService.findById(ArticlePersistant.class, composantId);
			ArticlePersistant ficheP = null;
			List<ArticleDetailPersistant> listDetail = null;
			// Vérification existance article
			List<ArticlePersistant> listData = articleService.getQuery("from ArticlePersistant where (is_stock is null or is_stock=0) and code=:code")
				.setParameter("code", composantP.getCode()+"_C")
				.getResultList();
			//
			if(listData.size() > 0){
				ficheP = listData.get(0);
				listDetail = ficheP.getList_article();
				listDetail.clear();
			} else{
				ficheP = new ArticlePersistant();
				listDetail = new ArrayList<>();
			}
			
			ficheP.setCode(composantP.getCode()+"_C");
			ficheP.setLibelle(composantP.getLibelle());
			ficheP.setIs_stock(false);
			ficheP.setDestination(dest);
			ficheP.setOpc_famille_cuisine(famille);
			ficheP.setCode_barre(composantP.getCode_barre());
			// Infos vente
			ficheP.setOpc_unite_vente_enum(composantP.getOpc_unite_achat_enum());
			ficheP.setPrix_vente(composantP.getPrix_vente());
			ficheP.setPrix_vente_ht(composantP.getPrix_vente_ht());
//			ficheP.setUnite_vente_quantite(composantP.getUnite_vente_quantite());
					
			ArticleDetailPersistant artDet = new ArticleDetailPersistant();
			artDet.setIdxIhm(1);
			artDet.setOpc_article(ficheP);
			artDet.setOpc_article_composant(composantP);
			artDet.setQuantite(BigDecimalUtil.get(1));
			
			listDetail.add(artDet);
			ficheP.setList_article(listDetail);
			
			getEntityManager().merge(ficheP);
		}
	}
	
	@Override
	@Transactional
	public void ajouterFavorisCaisse(Long artId) {
		ArticlePersistant artP = articleService.findById(ArticlePersistant.class, artId);
		//
		if(BooleanUtil.isTrue(artP.getIs_fav_caisse())){
			artP.setIs_fav_caisse(false);
		} else{
			artP.setIs_fav_caisse(true);
		}
		
		articleService.getEntityManager().merge(artP);
	}
	
	@Override
	public ArticleBalancePersistant getArticlesBalanceByCode(String codePese) {
		return (ArticleBalancePersistant) articleService.getSingleResult(articleService.getQuery("from ArticleBalancePersistant art where code=:code ")
				.setParameter("code", codePese));
	}
	
	@Override
	public List<ArticlePersistant> getArticlesByCodeBarre(String codeBarre, String codeLibelle, String codePese) {
		if(StringUtil.isNotEmpty(codePese)) {
			List<ArticlePersistant> listArt = new ArrayList<>();
			ArticleBalancePersistant artPeseP = getArticlesBalanceByCode(codePese);
			if(artPeseP != null){
				listArt.add(artPeseP.getOpc_article());
			}
			return listArt;
		}
		String req = "from ArticlePersistant art where 1=1 ";
		if(StringUtil.isNotEmpty(codeLibelle)) {
			req = req + "and (upper(art.code) like :code or upper(art.libelle) like :code) ";
		}
		if(StringUtil.isNotEmpty(codeBarre)) {
			req = req + "and art.code_barre like :codeBarre ";
		}
		req = req + "and (is_stock is null or is_stock=0) ";
		
		Query query = articleService.getQuery(req);
		if(StringUtil.isNotEmpty(codeLibelle)) {
			query.setParameter("code", codeLibelle.toUpperCase()+"%");
		}
		if(StringUtil.isNotEmpty(codeBarre)) {
			query.setParameter("codeBarre", codeBarre+"%");
		}
		return query.getResultList();
	}

	@Override
	public List<ArticlePersistant> getArticlesByCodeBarreAndMarque(String codeBarre, String marque) {
		String req = "from ArticlePersistant art where 1=1 ";
		if(StringUtil.isNotEmpty(marque)) {
			req = req + "and (upper(art.code) like :code "
					+ "or upper(art.libelle) like :code "
					+ "or upper(composition) like :code "
					+ "or upper(opc_marque.code) like :code "
					+ "or upper(opc_marque.libelle) like :code) ";
		}
		if(StringUtil.isNotEmpty(codeBarre)) {
			req = req + "and art.code_barre like :codeBarre ";
		}
		
		Query query = articleService.getQuery(req);
		if(StringUtil.isNotEmpty(marque)) {
			query.setParameter("code", marque.toUpperCase()+"%");
		}
		if(StringUtil.isNotEmpty(codeBarre)) {
			query.setParameter("codeBarre", codeBarre+"%");
		}
		return query.getResultList();
	}
}
