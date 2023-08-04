package appli.model.domaine.caisse.service;



import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import appli.controller.domaine.administration.bean.JourneeBean;
import appli.controller.domaine.stock.bean.ArticleStockInfoBean;
import appli.controller.domaine.util_erp.ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM;
import appli.model.domaine.administration.persistant.UserPersistant;
import appli.model.domaine.personnel.persistant.OffrePersistant;
import appli.model.domaine.stock.persistant.ArticleClientPrixPersistant;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.stock.persistant.FamilleStockPersistant;
import appli.model.domaine.vente.persistant.CaisseJourneePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import appli.model.domaine.vente.persistant.CaissePersistant;
import appli.model.domaine.vente.persistant.JourneePersistant;
import framework.controller.bean.PagerBean;
import framework.model.service.IGenericJpaService;

public interface ICaisseWebService extends IGenericJpaService<JourneeBean, Long> {

	OffrePersistant getOffreDisponible(String destination);

	JourneePersistant getLastJourne();
 
	CaisseJourneePersistant getJourneCaisseOuverte(Long caisseId);

	String loadNextCustomCall(Long journeeId);

	boolean isCustomCallUtilise(Long journeeId, String token);

	String getNextRefCommande();

	List<CaisseMouvementPersistant> getListMouvementTemp(Long journeeId, Long caisseId, Long serveurId);

	List<CaissePersistant> getListAfficheurs(Long caisseId);

	List<CaisseMouvementPersistant> findAllMvmAfficheur(Long journeeId);

	Set<Long> changeStatutElement(CaissePersistant caisseP, Long elementId, String ihmStatut);

	List<CaisseMouvementPersistant> findAllCuisine(Long serveurId, 
			Long caisseId, 
			Long journeeId, 
			String typeCmd,
			Integer startIdx,
			Integer limit, STATUT_CAISSE_MOUVEMENT_ENUM ... statut);

	Date getMaxJourneeDate();

	void majMouvementPaiement(CaisseMouvementPersistant caisseMvm);

	List<String> getListTableOccupee(Long journeeId, Long caisseId, Long serveurId);

	ArticleClientPrixPersistant getArticleClientPrix(Long clientId, Long articleId);

	List<FamillePersistant> getListFamilleCaissePagination(Long caisseId, PagerBean pagerBeanFam);

	Set<Long> changeStatut(Date dtMvm, CaissePersistant caisseP, Long[] caisseMvmId, String ihmStatut);

	CaisseMouvementPersistant createMouvementCaisse(CaisseMouvementPersistant caisseMvm,
			STATUT_CAISSE_MOUVEMENT_ENUM statut, boolean isPoints);

	List<ArticleStockInfoBean> clearMvmCaisseStock(CaisseMouvementPersistant caisseMvm);

	boolean isEtatStockArticlesValide(CaisseMouvementPersistant caisseMvm);

	List<FamilleStockPersistant> getListFamilleActive(PagerBean pager);

	void annulerMouvementCaisse(Long mvmId, UserPersistant userAnnul);

	List<ArticleStockInfoBean> destockerArticleMvmNonRestau(CaisseMouvementPersistant caisseMvm);

	List<ArticleStockInfoBean> destockerArticleMvmRestau(CaisseMouvementPersistant caisseMvm);

	Map<Long, BigDecimal> getEtatStockCmdTmp(CaisseMouvementPersistant caisseMvm, Long journeeId);

	CaisseMouvementPersistant getMouvementByTable(Long journeeId, String refTable,
			STATUT_CAISSE_MOUVEMENT_ENUM ... status);
}
