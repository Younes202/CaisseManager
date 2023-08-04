package appli.model.domaine.stock.dao.impl;

import javax.inject.Named;

import appli.model.domaine.stock.dao.IArticleClientPrixDao;
import appli.model.domaine.stock.persistant.ArticleClientPrixPersistant;
import framework.model.util.GenericJpaDao;


@Named
public class ArticleClientPrixDao extends GenericJpaDao<ArticleClientPrixPersistant, Long> implements IArticleClientPrixDao{

}
