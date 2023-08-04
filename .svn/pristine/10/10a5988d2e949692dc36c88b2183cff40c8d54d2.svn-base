package appli.model.domaine.stock.validator;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

import appli.controller.domaine.stock.bean.ArticleBean;
import appli.controller.domaine.util_erp.ContextAppli.SOFT_ENVS;
import appli.model.domaine.administration.persistant.NotificationPersistant;
import appli.model.domaine.caisse.persistant.ArticleBalancePersistant;
import appli.model.domaine.stock.dao.IArticleDao;
import appli.model.domaine.stock.persistant.ArticleClientPrixPersistant;
import appli.model.domaine.stock.persistant.ArticleDetailPersistant;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.persistant.DemandeTransfertArticlePersistant;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.stock.persistant.InventaireDetailPersistant;
import appli.model.domaine.stock.persistant.LotArticlePersistant;
import appli.model.domaine.stock.persistant.MouvementArticlePersistant;
import appli.model.domaine.stock.persistant.PreparationArticlePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant;
import appli.model.domaine.vente.persistant.ListChoixDetailPersistant;
import appli.model.domaine.vente.persistant.MenuCompositionDetailPersistant;
import framework.model.common.service.MessageService;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;

@Named
public class ArticleValidator {
	@Inject
	private IArticleDao articleDao;
	
	/*
	 * 
	 */
	public void updateCreateValidator(ArticleBean articleBean) {
		if(articleBean == null){
			return;
		}
		boolean isRestau = SOFT_ENVS.restau.toString().equals(StrimUtil.getGlobalConfigPropertie("context.soft"));

		
		FamillePersistant opc_famille = null;
		
		if(articleBean.getOpc_famille_stock() != null && articleBean.getOpc_famille_stock().getId() != null){
			opc_famille = articleDao.findById(FamillePersistant.class, articleBean.getOpc_famille_stock().getId());
		} else if(articleBean.getOpc_famille_cuisine() != null){
			opc_famille = articleDao.findById(FamillePersistant.class, articleBean.getOpc_famille_cuisine().getId());	
		}
		
		// Code barre
		if(StringUtil.isNotEmpty(articleBean.getCode_barre())){
			String req = "from ArticlePersistant where "+(isRestau ? "(is_stock is not null and is_stock=1)" : "1=1")+" and code_barre=:codeBarre";
			if(articleBean.getId() != null){
				req = req + " and id!=:currId";
			}
			Query query = articleDao.getQuery(req)
					.setParameter("codeBarre", articleBean.getCode_barre());
			if(articleBean.getId() != null){
				query.setParameter("currId", articleBean.getId());
			}
			if(query.getResultList().size() > 0){
				MessageService.addFieldMessage("article.code_barre", "Ce code est déjà utilisé");
			}	
		}
		
		if(StringUtil.isNotEmpty(articleBean.getCode_direct_bal())){
			if(articleDao.isNotUnique(articleBean, "code_direct_bal")){
				MessageService.addFieldMessage("article.code_direct_bal", "Ce code est déjà utilisé");
			}
		}
		
		// Code article
		String req = "from ArticlePersistant where code=:code ";
		
		if(opc_famille != null){
			if(BooleanUtil.isTrue(articleBean.getIs_stock())){
				req = req + "and famille_stock_id=:famId ";
			} else{
				req = req + "and famille_cuisine_id=:famId ";
			}
			if(articleBean.getId() != null){
				req = req + "and id!=:currId ";
			}
			
			Query query = articleDao.getQuery(req)
					.setParameter("famId", opc_famille.getId())
					.setParameter("code", articleBean.getCode());
			
			if(articleBean.getId() != null){
				query.setParameter("currId", articleBean.getId());
			}
			
			List artDb = query.getResultList();
			
			if(artDb.size() > 0) {
				MessageService.addBannerMessage("Ce code article <b>"+articleBean.getCode()+"</b> existe déjà");
				return;
			}
		}
		
		if(articleBean.getList_article() != null && articleBean.getList_article().size() > 0){
			List<ArticleDetailPersistant> listArticle = articleBean.getList_article();
			for (ArticleDetailPersistant articleDetailPersistant : listArticle) {
				int idxIhm = articleDetailPersistant.getIdxIhm();
				if(articleDetailPersistant.getQuantite() == null){
					MessageService.addFieldMessageKey("quantite_"+idxIhm, "work.required.error.short");
				}
			}
		}
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void update(ArticleBean articleBean) {
		updateCreateValidator(articleBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void create(ArticleBean articleBean){
		updateCreateValidator(articleBean);
		
//		if(!articleBean.getIs_stock() && ServiceUtil.isEmpty(articleBean.getList_article())){
//			MessageService.addBannerMessage("Vous devez ajouter au moins un article à cette composition");
//		}
	}
	
	/**
	 * @param id
	 */
	public void delete(Long id){
		ArticlePersistant articlePersistant = articleDao.findById(id);
		
		String[] entites = {
				MouvementArticlePersistant.class.getSimpleName(),
				CaisseMouvementArticlePersistant.class.getSimpleName(),
				NotificationPersistant.class.getSimpleName(),
				ArticleBalancePersistant.class.getSimpleName(),
				ArticleClientPrixPersistant.class.getSimpleName(),
				DemandeTransfertArticlePersistant.class.getSimpleName(),
				LotArticlePersistant.class.getSimpleName(),
				PreparationArticlePersistant.class.getSimpleName(),
				ListChoixDetailPersistant.class.getSimpleName(),
				InventaireDetailPersistant.class.getSimpleName(),
			};
		
		String[] msgs = {
			"mouvements de stock",
			"ventes caisse",
			"notifications",
			"paramètrage balance",
			"prix client",
			"demandes de transfert",
			"lot article",
			"préparations article",
			"liste de choix",
			"détails inventaire",
		};
		
		int idx = 0;
		for(String ent : entites) {
			List<ArticleDetailPersistant> listCaisseVente = articleDao.getQuery("from "+ent+" det "
					+ "where det.opc_article.id=:articleId")
					.setParameter("articleId", id)
					.getResultList();
			if(listCaisseVente.size() > 0){
				MessageService.addBannerMessage("Cet article ("+articlePersistant.getLibelle()+") est utilisé dans "+msgs[idx]);
				return;
			}
			idx++;
		}
		// Contrôle
		List<MenuCompositionDetailPersistant> listMenu = articleDao.getQuery("from MenuCompositionDetailPersistant det "
				+ "where det.opc_article.id=:articleId "
				+ "or det.opc_article_destock.id=:articleId "
				+ "or det.opc_article_inc.id=:articleId")
				.setParameter("articleId", id)
				.getResultList();
		if(listMenu.size() > 0){
			MessageService.addBannerMessage("Cet article ("+articlePersistant.getLibelle()+") est utilisé dans la composition des menus");
			return;
		}
		// Contrôle des composants article
		List<ArticleDetailPersistant> listComposants = articleDao.getQuery("from ArticleDetailPersistant det "
				+ "where det.opc_article_composant.id=:articleId "
				+ "and det.opc_article.id!=:articleId")
				.setParameter("articleId", id)
				.getResultList();
		if(listComposants.size() > 0){
			MessageService.addBannerMessage("Cet article ("+articlePersistant.getLibelle()+") est utilisé dans une autre composition d'articles");
			return;
		}
	}
	
	/**
	 * @param ids
	 */
	public void delete_group(Long[] ids){
		for (Long artId : ids) {
			delete(artId);
		}
	}
}
