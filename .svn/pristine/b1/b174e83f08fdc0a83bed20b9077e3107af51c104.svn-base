package appli.model.domaine.stock.service.impl;

import javax.inject.Named;

import appli.controller.domaine.stock.bean.ArticleClientPrixBean;
import appli.model.domaine.stock.persistant.ArticleClientPrixPersistant;
import appli.model.domaine.stock.service.IArticleClientPrixService;
import appli.model.domaine.stock.validator.ArticleClientPrixValidator;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.service.GenericJpaService;

@WorkModelClassValidator(validator=ArticleClientPrixValidator.class)
@Named
public class ArticleClientPrixService extends GenericJpaService<ArticleClientPrixBean, Long> implements IArticleClientPrixService {

	@Override
	public ArticleClientPrixPersistant getArticleClientPrixByCode(String code) {
		return (ArticleClientPrixPersistant) getSingleResult(getQuery("from ArticleClientPrixPersistant where code=:code").setParameter("code", code));
	}

}
