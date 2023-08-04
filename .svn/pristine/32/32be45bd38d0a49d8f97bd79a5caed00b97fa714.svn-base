package appli.model.domaine.stock.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import appli.controller.domaine.stock.bean.ArticleBean;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import framework.model.service.IGenericJpaService;

public interface IEtatStockService extends IGenericJpaService<ArticleBean, Long> {

	Map<String, Object> getEtatDetail(Date dateDebut, List<ArticlePersistant> listArticle);

	Date[] getMinMaxDate();

}
