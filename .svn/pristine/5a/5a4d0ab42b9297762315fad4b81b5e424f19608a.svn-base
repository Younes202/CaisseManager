/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appli.model.domaine.caisse.service;

import java.util.List;

import appli.controller.domaine.stock.bean.ArticleBean;
import appli.model.domaine.caisse.persistant.ArticleBalancePersistant;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import framework.model.service.IGenericJpaService;

/**
 *
 * @author hp
 */
public interface IArticle2Service extends IGenericJpaService<ArticleBean, Long> {

	void dupliquerEnFicheArticle(String familleCuisine, String dest, Long[] listComposantsIds);

	void ajouterFavorisCaisse(Long artId);

	List<ArticlePersistant> getArticlesByCodeBarre(String codeBarre, String codeLibelle, String codePese);

	ArticleBalancePersistant getArticlesBalanceByCode(String codePese);

	List<ArticlePersistant> getArticlesByCodeBarreAndMarque(String codeBarre, String marque);	
}
