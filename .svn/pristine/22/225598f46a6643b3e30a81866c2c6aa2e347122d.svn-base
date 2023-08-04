package appli.model.domaine.stock.validator;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

import appli.controller.domaine.stock.bean.ArticleClientPrixBean;
import appli.model.domaine.stock.dao.IArticleClientPrixDao;

@Named
public class ArticleClientPrixValidator {
	@Inject
	private IArticleClientPrixDao articleClientPrixDao;
	
	/* (non-Javadoc)
	 * @see model.service.commun.validator.ActionValidator#validate(java.lang.Object, java.lang.String)
	 */
	public void updateCreateValidator(ArticleClientPrixBean articleClientPrixBean) {
		String request = "from ArticleClientPrixPersistant where opc_client.id=:clientId and "
				+ "opc_article.id=articleId";
		
		if(articleClientPrixBean.getId() != null){
			request = request + " and id!=:currId";
		}
		
		Query query = articleClientPrixDao.getQuery(request)
			.setParameter("clientId", articleClientPrixBean.getOpc_client().getId())
			.setParameter("articleId", articleClientPrixBean.getOpc_article().getId());
		
		if(articleClientPrixBean.getId() != null){
			query.setParameter("currId", articleClientPrixBean.getId());
		}
		
		query.getResultList();
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void update(ArticleClientPrixBean articleClientPrixBean) {
		updateCreateValidator(articleClientPrixBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void create(ArticleClientPrixBean articleClientPrixBean){
		updateCreateValidator(articleClientPrixBean);
	}
}
