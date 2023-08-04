package appli.model.domaine.caisse.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import appli.controller.domaine.caisse.bean.CaisseBean;
import appli.controller.domaine.util_erp.ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM;
import appli.model.domaine.caisse.persistant.ArticleStockCaisseInfoPersistant;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.vente.persistant.CaisseJourneePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import appli.model.domaine.vente.persistant.CaissePersistant;
import framework.controller.bean.PagerBean;
import framework.model.service.IGenericJpaService;

/**
 *
 * @author hp
 */ 
public interface ICaisseService extends IGenericJpaService<CaisseBean, Long> {

	public void activerDesactiverCaisse(Long caisseId);

	public void ouvrirCaisse(Long caisseId, BigDecimal mtt_ouverture);

	public CaisseJourneePersistant getJourneCaisseOuverte(Long id);

	public void validerLivraison(Long mvmId, Long livreurId);

	void setDataJourneeCaisseFromView(CaisseJourneePersistant caisseVenteP);

	List<CaisseBean> getListCaisseCuisineActive();

//	public void mergeCaissePilotage(String cmdVal, String cmdEncours, String methodeDispachNonAuto, String statut, Boolean isCmdAuto);

	List<CaissePersistant> getListCaisseActive(String typeCaisse, boolean activeOnly);

	void transfererCmdCuisine(STATUT_CAISSE_MOUVEMENT_ENUM currStatut, Long mvmId, Long detailId, Long caisseId, Long caisseIdTarget);

//	void initMergeCaissePilotage(String cmdVal, String cmdEnPreparation, String methodeDispachNonAuto, String statut/*, String modeTransfert*/);

	public void updateNbrOuvertureCaisse(Long journeeCaisseId);

	//void printEtiquettes(CaisseMouvementPersistant cmP);

	List<CaissePersistant> getListCaisse(boolean activeOnly);

//	CaisseMouvementStatutPersistant getCaisseMvmStatutByStatutAndCaisseMvm(Long caisse_mvm_id, String statut);

//	List<Date> getListDateStatutCmd();

	List<CaisseBean> getListCaissePilotageActive();

	void refreshDataShift(Long caisseJourneeId);

	List<ArticlePersistant> getFavorisCaisse(PagerBean pagerBean);

	List<ArticlePersistant> getFavorisBalance(PagerBean pagerBean);

	public String addArticleBalance(Long articleId, BigDecimal poids, boolean isCodeBarre);

	List<ArticlePersistant> getListArticleCaisseActif();

	void mergeCaisseCuisineConfig(CaisseBean caisseBean);

	void gestionEcranImprimante(CaisseMouvementPersistant caisseMvm);

	void gererPassasionShift(Long userOuvertureId, CaisseJourneePersistant caisseJourneeP, BigDecimal mtt_ouverture, boolean isRectMode);

	List<CaisseMouvementPersistant> getListCmdNonPaye(Long caisseJourneeId);

	List<CaisseMouvementPersistant> getListCmdTemp(Long caisseJourneeId);

	public Set<Long>[] deplacerMouvement(Long shiftTargetId, Long[] checkedTr);

	void updateChiffresShiftJour(Set<Long> listJrnIds, Set<Long> listShiftIds);
	void updateChiffresShiftJour(Long jrnId, Long shiftId);

	void recalculHistoriqueAnnulation();

	public void recalculMouvementsAchat();

	Map<String, Integer[]> getEvolutionTempsCmd(Date date_debut, Date date_fin);

	void cloturerDefinitive(CaisseJourneePersistant caisseJourneeP, 
			boolean isRecalcul, 
			BigDecimal mtt_clotureEspeces,
			BigDecimal mtt_clotureCb, 
			BigDecimal mtt_clotureChq, 
			BigDecimal mtt_clotureDej, 
			boolean isRectif, 
			boolean isPassassion);

	void deleteCaisseInfoInv(Set<Long> listFamIds, boolean isRestau);

	void updateCaisseInfoInv(String[] famInvFilter, List<ArticleStockCaisseInfoPersistant> listInventaireDetIHM,
			CaisseMouvementPersistant CURRENT_COMMANDE);

	List<Object> tempsGlobalParEmploye(Date date_debut, Date date_fin, String[] articleIds, String[] menuIds);
}
