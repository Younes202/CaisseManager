package appli.model.domaine.stock.service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import appli.controller.domaine.stock.bean.ArticleStockInfoBean;
import appli.controller.domaine.stock.bean.MouvementBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.personnel.persistant.ClientPersistant;
import appli.model.domaine.stock.persistant.ArticleStockInfoPersistant;
import appli.model.domaine.stock.persistant.MouvementArticlePersistant;
import appli.model.domaine.stock.persistant.MouvementPersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import framework.model.service.IGenericJpaService;

public interface IMouvementService extends IGenericJpaService<MouvementBean, Long> {

	void create(MouvementBean mouvementBean); 

	String generateNumBl(String type);

	ArticleStockInfoPersistant getArticleEtatStock(Long articleId, Long emplacementId);

	void valideRegroupementBL(MouvementBean mvmBean);

	List<MouvementPersistant> getListMvmGroupe(Long mouvement_id);

	List<MouvementArticlePersistant> getListArticleByType(String[] ids, String parameter);
	String getMsgStockInsuffisant(MouvementBean mouvementBean);
	void mergeTransformation(MouvementBean mouvementBean);
	Map<Long, List<MouvementPersistant>> getMapMvmGroupe(List<MouvementPersistant> listGroup);
	List<MouvementPersistant> getMvmNonGroupe(Long fournisseurId, List<Long> ignoreIds, Date dateDebut, Date dateFin);
	void majQteStockArticle(Long emplId, Long destId, List<MouvementArticlePersistant> listDetail);
	void majPrixFichComposant(Long compId);
	void majQteArticleInfo(List<ArticleStockInfoBean> listArtInfos);
	List<MouvementPersistant> getMvmAvoirNonGroupe(Long founisseurId, List<Long> ids, Date dateDebut, Date dateFin);
	Map<Long, List<MouvementPersistant>> getMapMvmAvoirGroupe(List<MouvementPersistant> listGroup);
	void validerAvoir(Long avoirId);

	void validerBonCommande(Long cmdId);

	int genererBonCommandeSeuilStock(String[] emplacements);

	MouvementBean genererAchatFromCmd(Long cmdId);

	MouvementPersistant getMouvementByCodeBarre(String substring);

	MouvementPersistant getMouvementByReference(String refMvm);

	List<MouvementPersistant> getMouvementByTypeRef(String typeRef);

	Date getMaxDate(String type);

	MouvementPersistant getMouvementGroupeFacture(Long clientId, Date dateDebut, Date dateFin, String[] modeFinancement, String src);

	void archiverPdfFactureGed(ClientPersistant clientP, String numFacture, Date dateFacture, File exportPdf);

	String generateNumFac(ContextAppli.TYPE_MOUVEMENT_ENUM type);

	CaisseMouvementPersistant getMouvementGroupeFacture(Long clientId, Date dateDebut, Date dateFin,
			String[] modeFinancement);

	void majQteStockArticle(Date dateRef);

	void majQteArticleCaisseInfo(CaisseMouvementPersistant currCmd, boolean isDelete);
}
