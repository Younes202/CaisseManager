package appli.model.domaine.personnel.validator;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

import appli.controller.domaine.personnel.bean.ComposantClientPrixBean;
import appli.model.domaine.personnel.dao.IComposantClientPrixDao;
import appli.model.domaine.stock.persistant.ArticleClientPrixPersistant;
import framework.model.common.service.MessageService;

@Named
public class ComposantClientPrixValidator {
	@Inject
	private IComposantClientPrixDao composantClientPrixDao;
	
	/* (non-Javadoc)
	 * @see model.service.commun.validator.ActionValidator#validate(java.lang.Object, java.lang.String)
	 */
	public void updateCreateValidator(ComposantClientPrixBean composantClientPrixBean) {
		String request = "from ArticleClientPrixPersistant where opc_client.id=:clientId and "
				+ "opc_article.id=:articleId";
		
		if(composantClientPrixBean.getId() != null){
			request = request + " and id!=:currId";
		}
		
		Query query = composantClientPrixDao.getQuery(request)
			.setParameter("clientId", composantClientPrixBean.getOpc_client().getId())
			.setParameter("articleId", composantClientPrixBean.getOpc_article().getId());
		
		if(composantClientPrixBean.getId() != null){
			query.setParameter("currId", composantClientPrixBean.getId());
		}
		
		List<ArticleClientPrixPersistant> listData = query.getResultList();
		
		if(listData.size() > 0){
			MessageService.addBannerMessage("Cet article est déjà paramétré pour ce client.");
		}
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void update(ComposantClientPrixBean composantClientPrixBean) {
		updateCreateValidator(composantClientPrixBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void create(ComposantClientPrixBean composantClientPrixBean){
		updateCreateValidator(composantClientPrixBean);
	}
}
