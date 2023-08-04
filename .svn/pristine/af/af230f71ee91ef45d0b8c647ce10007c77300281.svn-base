package appli.model.domaine.stock.service;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import appli.controller.domaine.stock.bean.ArticleBean;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.persistant.ArticleStockInfoPersistant;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.stock.persistant.InventairePersistant;
import appli.model.domaine.stock.persistant.MouvementPersistant;
import appli.model.domaine.vente.persistant.CaissePersistant;
import framework.controller.bean.PagerBean;
import framework.model.beanContext.DataValuesPersistant;
import framework.model.service.IGenericJpaService;

public interface IArticleService extends IGenericJpaService<ArticleBean, Long> {

	void activerDesactiverElement(Long articleId);

	List<ArticlePersistant> getListArticleStock(boolean isActifOnly);

	List<ArticlePersistant> getListFicheComposant(boolean isActifOnly);

	List<ArticlePersistant> getListArticleNonStock(boolean isActifOnly);

	List<ArticlePersistant> getListArticleNetAndStock(boolean isActifOnly);

	List<ArticlePersistant> getListArticleNonStock();

	String generateCode(Long famId, String type);

	List<ArticleStockInfoPersistant> getListArticleView(Long articleId);

	ArticlePersistant getArticleByCode(String code);

	List<ArticlePersistant> getListFamilleArticle(Long famId, PagerBean pagerBeanArt);
	List<ArticlePersistant> getListArticleStockActif(Long famId);
	List<ArticlePersistant> getListArticleNonStockActif(Long famId);
	List<ArticlePersistant> getListArticleCuisineActif(Long famId);

	List<ArticlePersistant> getListArticleChecked(Long[] checkedArticles);

	Map<ArticlePersistant, BigDecimal[]> calculMargeArticles(List<ArticlePersistant> listArticles);

	void dupliquerEnFicheArticle(Long[] listComposantsIds, Long famId, String dest, BigDecimal prixVente);

	void addGenericFamille();

	FamillePersistant getGenericFamille();

	ArticlePersistant getGenericArticle(FamillePersistant genFamP);

	List<ArticlePersistant> getArticlesAutocomplete(String value, boolean isActifOnly, boolean isStock);

	ArticlePersistant getArticleByCodeBarre(String codeBarre, boolean isArticle);

	String generateCleBalance(Long famId);

	List<CaissePersistant> getListCaisseActive(String typeCaisse);

	void mergeFastUpdate(List<ArticlePersistant> listArticle);

	void synchroniserArticleBalance(CaissePersistant balanceCaisse);

	List<ArticlePersistant> getListComposantsActifs(Long familleId);

	String generateCodeBarre(Long famId);

	File exporteComposants(String[] familles);

	void changerFamille(Long familleId, Long[] artIds);

	InventairePersistant importerComposants(String fileName, boolean generateCB,  boolean isDisComposant, Date dateInventaire, Long emplcementId);

	int fusionnerArticleDoublon();

	void updateRowsOrder(String[] orderArray);

	List<ArticlePersistant> getListArticleActifs(Long familleId, boolean isMobile);

	List<ArticlePersistant> getListArticleActifs(Long familleId, PagerBean pagerBean, boolean isMobile);

	List<ArticlePersistant> getListPharmaPrinceps();

	void purgerDoublonStockInfo();

	Map<String, BigDecimal[]> controleMarge();

	void fusionnerArticle(Long source, Long dest);

	List<DataValuesPersistant> getListDataValues(String groupe, Long elementId);
}
