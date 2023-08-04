package appli.model.domaine.stock.validator;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.stock.bean.PreparationBean;
import appli.model.domaine.stock.dao.IPreparationDao;
import appli.model.domaine.stock.persistant.PreparationArticlePersistant;
import framework.model.common.service.MessageService;

@Named
public class PreparationValidator {
	@Inject
	private IPreparationDao preparationDao;
	
	/*
	 * 
	 */
	public void updateCreateValidator(PreparationBean preparationBean) {
		
		if(preparationDao.isNotUnique(preparationBean, "code")){ 
			MessageService.addFieldMessageKey("preparation.code", "msg.unique.val");
		} else if(preparationDao.isNotUnique(preparationBean, "libelle")){
			MessageService.addFieldMessageKey("preparation.libelle", "msg.unique.val");
		} else{
			if(preparationBean.getList_article() == null || preparationBean.getList_article().size() == 0){
				MessageService.addBannerMessageKey("preparation.msg.articles");
			} else{
				List<PreparationArticlePersistant> listArticle = preparationBean.getList_article();
				for (PreparationArticlePersistant prepArticlePersistant : listArticle) {
					int idxIhm = prepArticlePersistant.getIdxIhm(); 
					if(prepArticlePersistant.getOpc_article() == null){
						MessageService.addFieldMessageKey("opc_article.id_"+idxIhm, "work.required.error.short");
					}
					if(prepArticlePersistant.getQuantite() == null){
						MessageService.addFieldMessageKey("quantite_"+idxIhm, "work.required.error.short");
					}
				}
			}
		}
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void update(PreparationBean preparationBean) {
		updateCreateValidator(preparationBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void create(PreparationBean preparationBean){
		updateCreateValidator(preparationBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
//	public void delete(Long id){
//	
//	}
}
