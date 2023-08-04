package appli.model.domaine.caisse.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.administration.bean.JourneeBean;
import appli.controller.domaine.caisse.ContextAppliCaisse;
import appli.controller.domaine.stock.bean.ArticleStockInfoBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.SOFT_ENVS;
import appli.controller.domaine.util_erp.ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_COMMANDE;
import appli.model.domaine.administration.persistant.UserPersistant;
import appli.model.domaine.caisse.persistant.ArticleStockCaisseInfoPersistant;
import appli.model.domaine.caisse.service.ICaisseMobileService;
import appli.model.domaine.caisse.service.ICaisseService;
import appli.model.domaine.caisse.service.ICaisseWebService;
import appli.model.domaine.caisse.service.ICarteFidelite2Service;
import appli.model.domaine.fidelite.persistant.CarteFideliteClientPersistant;
import appli.model.domaine.fidelite.persistant.CarteFideliteConsoPersistant;
import appli.model.domaine.fidelite.persistant.CarteFidelitePointsPersistant;
import appli.model.domaine.personnel.persistant.ClientPortefeuilleMvmPersistant;
import appli.model.domaine.personnel.persistant.OffrePersistant;
import appli.model.domaine.stock.dao.IFamilleDao;
import appli.model.domaine.stock.persistant.ArticleClientPrixPersistant;
import appli.model.domaine.stock.persistant.ArticleDetailPersistant;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.persistant.ArticleStockInfoPersistant;
import appli.model.domaine.stock.persistant.EmplacementPersistant;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.stock.persistant.FamilleStockPersistant;
import appli.model.domaine.stock.persistant.MouvementArticlePersistant;
import appli.model.domaine.stock.persistant.MouvementPersistant;
import appli.model.domaine.stock.service.IMouvementService;
import appli.model.domaine.vente.persistant.CaisseJourneePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementOffrePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementStatutPersistant;
import appli.model.domaine.vente.persistant.CaissePersistant;
import appli.model.domaine.vente.persistant.JourneePersistant;
import appli.model.domaine.vente.persistant.MenuCompositionDetailPersistant;
import appli.model.domaine.vente.persistant.MenuCompositionPersistant;
import framework.controller.ContextGloabalAppli;
import framework.controller.bean.PagerBean;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;
import framework.model.service.GenericJpaService;
import framework.model.util.audit.ReplicationGenerationEventListener;

@Named
public class CaisseWebService extends GenericJpaService<JourneeBean, Long> implements ICaisseWebService {
	@Inject
	private IFamilleDao familleDao;
	@Inject
	private ICaisseService caisseService;
	@Inject
	private IMouvementService mouvementService;
	@Inject
	private ICarteFidelite2Service carteFideliteService2;
	@Inject
	private ICaisseMobileService caisseMobileService;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FamilleStockPersistant> getListFamilleActive(PagerBean pagerBeanFam) {
		String req = "from FamilleStockPersistant where level=1 "
				+ "and (is_disable is null or is_disable=false)";
		
		if(pagerBeanFam != null){
			Long count = (Long) familleDao.getQuery("select count(0) "+req).getSingleResult();
			pagerBeanFam.setNbrLigne(count.intValue());
		}
		
		Query query = familleDao.getQuery(req + " order by b_left, libelle");
		if(pagerBeanFam != null){
			query.setMaxResults(pagerBeanFam.getElementParPage())
				.setFirstResult(pagerBeanFam.getStartIdx());
		}
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FamillePersistant> getListFamilleCaissePagination(Long caisseId, PagerBean pagerBean) {
		CaissePersistant currCaisse = null;
		
		if(caisseId != null && caisseId > 0){
			currCaisse = (CaissePersistant)familleDao.findById(CaissePersistant.class, caisseId);
		}
		boolean isCaisseSpecifique = (currCaisse != null && BooleanUtil.isTrue(currCaisse.getIs_specifique()));
		boolean isRestau = SOFT_ENVS.restau.toString().equals(StrimUtil.getGlobalConfigPropertie("context.soft"));
		
		String req = "from "+(isRestau ? "FamilleCuisinePersistant":"FamilleStockPersistant")+" where level=1 and (is_disable is null or is_disable=false) ";
		//
		if(isCaisseSpecifique){
	        req = req + " and caisse_target is not null and caisse_target like :currCaisse ";
	    } else{
	       	req = req + " and (caisse_target is null or caisse_target like :currCaisse) ";
	    }
		req = req + " and (is_noncaisse is null or is_noncaisse = 0) and code != 'GEN' ";
		
		
		if(pagerBean != null){
			Long count = (Long) familleDao.getQuery("select count(0) "+req)
					.setParameter("currCaisse", "%|"+caisseId+"|%")
					.getSingleResult();
			pagerBean.setNbrLigne(count.intValue());
		}
		
		Query query = familleDao.getQuery(req + " order by b_left, code, libelle")
				.setParameter("currCaisse", "%|"+caisseId+"|%");
		
		if(pagerBean != null){
			query.setMaxResults(pagerBean.getElementParPage())
				.setFirstResult(pagerBean.getStartIdx());
		}
		return query.getResultList();
	}
	
	@Override
	public OffrePersistant getOffreDisponible(String destination) {
		Date now = new Date();
		OffrePersistant offre = (OffrePersistant) familleDao.getSingleResult(familleDao
				.getQuery("from OffrePersistant where destination=:dest "
						+ "and date_debut<=:dateJ and (date_fin is null or date_fin>:dateJ)")
				.setParameter("dest", destination).setParameter("dateJ", now));
		
//		if(offre == null && "S".equals(destination)){// Creer offre société de livraison
//			offre = new OffrePersistant();
//			offre.setCode("SL");
//			offre.setDate_creation(now);
//			offre.setDate_debut(now);
//			offre.setSignature("ADMIN");
//			offre.setDestination("S");
//			offre.setLibelle("Société de livraison");
//			offre.setType_offre("R");
//			//
//			offre = familleDao.getEntityManager().merge(offre);
//		}
		
		return offre;
	}

	@Override
	public JourneePersistant getLastJourne() {
		List<JourneePersistant> listJournee = familleDao
				.getQuery("from JourneePersistant journee order by date_journee desc").getResultList();
		return (listJournee.size() > 0 ? listJournee.get(0) : null);
	}

	@Override
	public CaisseJourneePersistant getJourneCaisseOuverte(Long caisseId) {
		CaisseJourneePersistant caisseJourneeP = (CaisseJourneePersistant) familleDao.getSingleResult(familleDao
				.getQuery("from CaisseJourneePersistant where "
						+ "opc_journee.statut_journee=:statut and opc_caisse.id=:caisseId and statut_caisse=:statut")
				.setParameter("statut", ContextAppli.STATUT_JOURNEE.OUVERTE.getStatut().toString())
				.setParameter("caisseId", caisseId));
		return caisseJourneeP;
	}

	@Override
	public String loadNextCustomCall(Long journeeId) {
		JourneePersistant journeeP = (JourneePersistant) familleDao.findById(JourneePersistant.class, journeeId);
		String customInterval = ContextGloabalAppli.getGlobalConfig(ContextAppli.PARAM_APPLI_ENUM.CUSTOM_CALL.toString());
		String currToken = null;
		String[] tokensPanne = null;

		if (StringUtil.isEmpty(customInterval)) {
			return "";
		}

		if (StringUtil.isNotEmpty(journeeP.getCustomcall_out())) {
			tokensPanne = journeeP.getCustomcall_out().split(";");
		}
		String[] tokensInterval = customInterval.split("-");

		if (tokensInterval != null && tokensInterval.length == 2) {
			int start = Integer.valueOf(tokensInterval[0]);
			int end = Integer.valueOf(tokensInterval[1]);

			for (int i = start; i <= end; i++) {
				if (tokensPanne == null || !StringUtil.contains("" + i, tokensPanne)) {
					if (!isCustomCallUtilise(journeeId, "" + i)) {
						currToken = "" + i;
						break;
					}
				}
			}
		}
		return currToken;
	}
	
	@Override
	public CaisseMouvementPersistant getMouvementByTable(Long journeeId, String refTable, STATUT_CAISSE_MOUVEMENT_ENUM ... status){
		String statutAnnul = ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.ANNUL.toString();
		
		String req = "select distinct(opc_mouvement_caisse) from CaisseMouvementArticlePersistant artMvm "
				+ "where artMvm.opc_mouvement_caisse.opc_caisse_journee.opc_journee.id=:journeeId "
				+ "and artMvm.opc_mouvement_caisse.last_statut != '"+statutAnnul+"' "
				+ "and artMvm.opc_mouvement_caisse.opc_caisse_journee.date_cloture is null "
				+ "and ("
					+ "artMvm.opc_mouvement_caisse.last_statut in (:stats) "
					+ "or artMvm.opc_mouvement_caisse.mode_paiement is null "
				+ ") "
				+ "and artMvm.ref_table is not null and artMvm.ref_table=:refTable " 
				+ "order by artMvm.opc_mouvement_caisse.id desc";
		
		Set<String> statutSet = new HashSet<>();
		for (STATUT_CAISSE_MOUVEMENT_ENUM statutEnum : status) {
			statutSet.add(statutEnum.toString());
		}
		
		List<CaisseMouvementPersistant> listData = familleDao.getQuery(req)
					.setParameter("journeeId", journeeId)
					.setParameter("refTable", refTable)
					.setParameter("stats", statutSet)
					.getResultList();
		
		return listData.size()>0 ? listData.get(0) : null;
	}
	
	@Override
	public List<String> getListTableOccupee(Long journeeId, Long caisseId, Long serveurId) {
		String statutTemp = ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.TEMP.toString();
		String statutAnnul = ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.ANNUL.toString();
		
		String req = "from CaisseMouvementArticlePersistant artMvm "
				+ "where artMvm.opc_mouvement_caisse.opc_caisse_journee.opc_journee.id=:journeeId "
				+ "and (artMvm.ref_table is not null and artMvm.ref_table!='') " 
				+ "and artMvm.opc_mouvement_caisse.last_statut != '"+statutAnnul+"' "
				+ "and ( "
					+ "artMvm.opc_mouvement_caisse.last_statut='"+statutTemp+"' "
					+ "or artMvm.opc_mouvement_caisse.mode_paiement is null "
				+ ") ";
		
		// Si serveur alors ses commandes uniquement
		boolean isServeurProfile = ContextAppli.getUserBean().isInProfile("SERVEUR");
		if(isServeurProfile){
			serveurId = ContextAppli.getUserBean().getId();
		}
		
		// Si serveur ou caissier avec paramètrage alors on filtre les commande
		if(ContextAppli.getUserBean().isInProfile("SERVEUR")){
			req = req + " and artMvm.opc_mouvement_caisse.opc_serveur.id=:serveurId ";
		} else if(StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CAISS_FILTER_COM")) 
				&& ContextAppli.getUserBean().isInProfile("CAISSIER")){
			req = req + " and artMvm.opc_mouvement_caisse.opc_user.id=:userId ";
		}
		
		if(serveurId != null){
			req = req + " and artMvm.opc_mouvement_caisse.opc_serveur.id=:serveurId ";
		}
		if(caisseId != null){
			req = req + "and artMvm.opc_mouvement_caisse.opc_caisse_journee.opc_caisse.id=:caisseId ";
		}
		req = req + "order by artMvm.ref_table";
		
		Query query = familleDao.getQuery(req).setParameter("journeeId", journeeId);
		
		// Si serveur ou caissier avec paramètrage alors on filtre les commande
		if(ContextAppli.getUserBean().isInProfile("SERVEUR") ){
			query.setParameter("serveurId", ContextAppli.getUserBean().getId());
		} else if(StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CAISS_FILTER_COM")) 
				&& ContextAppli.getUserBean().isInProfile("CAISSIER")){
			query.setParameter("userId", ContextAppli.getUserBean().getId());
		}
				
		if(serveurId != null){
			query.setParameter("serveurId", serveurId);
		}
		if(caisseId != null){
			query.setParameter("caisseId", caisseId);
		}
		
		List<String> listTable = new ArrayList<>();
		List<CaisseMouvementArticlePersistant> listData = query.getResultList();
		for (CaisseMouvementArticlePersistant det : listData) {
			if(!listTable.contains(det.getRef_table())){
				listTable.add(det.getRef_table());
			}
		}
		
		return listTable;
	}
	
	@Override
	public List<CaisseMouvementPersistant> getListMouvementTemp(Long journeeId, Long caisseId, Long serveurId) {
		String statutTemp = ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.TEMP.toString();
		String statutAnnul = ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.ANNUL.toString();
		
		String req = "from CaisseMouvementPersistant mouvement "
				+ "where mouvement.opc_caisse_journee.opc_journee.id=:journeeId " 
				+ "and (mouvement.last_statut='"+statutTemp+"' or (mouvement.last_statut != '"+statutAnnul+"' and mouvement.mode_paiement is null)) ";
		

		// Si serveur alors ses commandes uniquement
		boolean isServeurProfile = ContextAppli.getUserBean().isInProfile("SERVEUR");
		if(isServeurProfile){
			serveurId = ContextAppli.getUserBean().getId();
		}
		
		
		if(serveurId != null){
			req = req + " and mouvement.opc_serveur.id=:serveurId ";
		}
		if(caisseId != null){
			req = req + "and mouvement.opc_caisse_journee.opc_caisse.id=:caisseId ";
		}
		req = req + "order by mouvement.id desc";
		
		Query query = familleDao.getQuery(req).setParameter("journeeId", journeeId);
		
		if(serveurId != null){
			query.setParameter("serveurId", serveurId);
		}
		if(caisseId != null){
			query.setParameter("caisseId", caisseId);
		}
		
		return query.getResultList();
	}
	
	 @Override
    @Transactional
    public void majMouvementPaiement(CaisseMouvementPersistant caisseMvm){
		 familleDao.getEntityManager().merge(caisseMvm);
	}
	    
	 @Override
	 @Transactional
	 public List<ArticleStockInfoBean> clearMvmCaisseStock(CaisseMouvementPersistant caisseMvm){
		 EntityManager em = familleDao.getEntityManager();
		 
		 if(StringUtil.isEmpty(caisseMvm.getMvm_stock_ids())){
			 return null;
		 }
		 String[] mvmIds = StringUtil.getArrayFromStringDelim(caisseMvm.getMvm_stock_ids(), ";");
		 String sens = (BooleanUtil.isTrue(caisseMvm.getIs_retour()) ? "S" : "E");
		 
		 List<ArticleStockInfoBean> listArt = new ArrayList<>(); 			 
		 for(String mvmId : mvmIds) {
			 if(StringUtil.isEmpty(mvmId)){
				continue;
			 }
			 MouvementPersistant mvmStock = familleDao.findById(MouvementPersistant.class, Long.valueOf(mvmId));
	 		 //on restocke les articles destockés----------------------------------------------------------------------------
	 		 if(mvmStock != null) {
	    		List<MouvementArticlePersistant> listDetail = mvmStock.getList_article();
	    		//
	    		for (MouvementArticlePersistant mvmDet : listDetail) {
	    			listArt.add(new ArticleStockInfoBean(mvmStock.getOpc_emplacement().getId(), mvmDet.getOpc_article().getId(), mvmDet.getQuantite(), sens));
	    		}	
		    	// Purger les mouvements et le détail des anciens mouvements
		    	familleDao.getQuery("delete from MouvementArticlePersistant where mouvement_id=:ids")
					.setParameter("ids", mvmStock.getId())
					.executeUpdate();
				em.flush();
				
				familleDao.getQuery("delete from MouvementPersistant where id=:ids")
					.setParameter("ids", mvmStock.getId())
					.executeUpdate();
				
				em.flush();
	 		 }
	    }
 		
 		return listArt;
	 }
	 

//	@Override
//	@Transactional
//	public List<ArticleStockInfoBean> destockArticleCuisineCaisse(CaisseMouvementPersistant caisseMvm) {
//		List<ArticleStockInfoBean> listArtInfos = new ArrayList<>();
//		EntityManager em = familleDao.getEntityManager();
//		
//		caisseMvm = em.find(CaisseMouvementPersistant.class, caisseMvm.getId());
//		
//		
//		// Destockage articles si commande au statut livrée 
//        // ==> NB : puisque les caisse à imprimante seule pass le statut à livrée donc on va destocker doublement dans notre cas ici
//		List<MouvementArticlePersistant> listDestockCaisse = destockerArticleCaisse(em, caisseMvm, true);
//		//
//		for (MouvementArticlePersistant detail : listDestockCaisse) {
//			listArtInfos.add(new ArticleStockInfoBean(caisseMvm.getOpc_caisse_journee().getOpc_caisse().getOpc_stock_cible().getId(), detail.getOpc_article().getId(), detail.getQuantite(), "S"));
//		}
//		
//		String[] elementsCuisine= StringUtil.getArrayFromStringDelim(caisseMvm.getCaisse_cuisine(), ";");
//		Set<Long> ecranCuisineIds = new HashSet<>();
//        if(elementsCuisine != null){
//	    	for (String element : elementsCuisine) {
//	    		String[] detailsCuisine = StringUtil.getArrayFromStringDelim(element, ":");
//	    		
//	    		if(StringUtil.isEmpty(detailsCuisine[0]) || StringUtil.isEmpty(detailsCuisine[1])){
//	    			continue;
//	    		}
//	    		Long currCaisseId = Long.valueOf(detailsCuisine[0]);
//	    		ecranCuisineIds.add(currCaisseId);
//			}
//        }
//        
//		// Destockage des produits cuisine
//        for (Long caisseEcranId : ecranCuisineIds) {
//        	CaissePersistant caisseCuisineP = (CaissePersistant) em.find(CaissePersistant.class, caisseEcranId);
//        	List<MouvementArticlePersistant> listDestock = destockerArticleCuisine(em, caisseMvm, caisseCuisineP);
//			//
//			for (MouvementArticlePersistant detail : listDestock) {
//				listArtInfos.add(new ArticleStockInfoBean(caisseCuisineP.getOpc_stock_cible().getId(), detail.getOpc_article().getId(), detail.getQuantite(), "S"));
//			}
//		}
//        
//        return listArtInfos;
//	}
//		
//	 @Override 
//	 @Transactional
//	 public void addMvmCaisseStock(List<ArticleStockInfoBean> listArtInfos){
//		 EntityManager em = familleDao.getEntirtyManager();
//		 
//		//em.clear();
//		/* caisseMvm = (CaisseMouvementPersistant) em.find(CaisseMouvementPersistant.class, caisseMvm.getId());
//		 List<CaisseBean> listCaisseCuisine = caisseService.getListCaisseCuisineActive();
//		 // Chercher s'il y a un écran cuisine avec emplacement
//	     boolean isCuisineExist = false;
//	   for (CaissePersistant caisseCuisineP : listCaisseCuisine) {
//     		isCuisineExist = (caisseCuisineP.getOpc_stock_cible() != null);r
//     		break;
// 		}
//	   // Si pas d'écran cuisine alors tout se destock depuis la caisse
//       boolean articleCuisineExist = false; 
//       for (CaisseMouvementArticlePersistant caisseMvmArt : caisseMvm.getList_article()) {
//	       	if(caisseMvmArt.getOpc_article() != null && "C".equals(caisseMvmArt.getOpc_article().getDestination())) {
//	       		articleCuisineExist = true;
//	       		break;
//	       	}
//		}*/
//       //if(!isCuisineExist || !articleCuisineExist || isPrintOnly) {
//	   		mouvementService.majQteArticleInfo(em, listArtInfos);
//	   	}
//	 }
	 
	 /**
     * @param caisseMvm
     * @param statut
     */
    @Override
    @Transactional
    public CaisseMouvementPersistant createMouvementCaisse(CaisseMouvementPersistant caisseMvm, 
    			ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM statut, boolean isPoints){
    	
    	JourneeService.recalculChiffresMvmJournee(caisseMvm);
    	
        EntityManager entityManager = familleDao.getEntityManager();
        boolean isCmdTemporaire = statut.toString().equals(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.TEMP.toString());
    	CaisseJourneePersistant opc_caisse_journee = caisseMvm.getOpc_caisse_journee();        
    	if(caisseMvm.getId() == null){
	    	Date dtMvm = opc_caisse_journee.getOpc_journee().getDate_journee();
	        caisseMvm.setDate_creation(new Date());
	        caisseMvm.setDate_vente(dtMvm);
	    }
    	
        BigDecimal totalArtOfferte = null;
        BigDecimal totalArtReduc = null;
        BigDecimal totalOffre = null;
        
        // Création d'un mouvement stock
		boolean isStatutAnnule = ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.ANNUL.equals(statut);
		if(!isStatutAnnule){
        	// Articles
            for(CaisseMouvementArticlePersistant detail : caisseMvm.getList_article()){
                if(detail.getIs_annule() == null || !detail.getIs_annule()){
                    // Total lignes ofertes
                    if(BooleanUtil.isTrue(detail.getIs_offert())){
                        totalArtOfferte = BigDecimalUtil.add(totalArtOfferte, detail.getMtt_total());
                    }
                    totalArtReduc = BigDecimalUtil.add(totalArtReduc, detail.getMtt_reduction());
                }
            }
            // Montant réduction
            for(CaisseMouvementOffrePersistant offreP : caisseMvm.getList_offre()){
                if(offreP.getIs_annule() == null || !offreP.getIs_annule()){
                    totalOffre = BigDecimalUtil.add(totalOffre, offreP.getMtt_reduction());
                }
            }
        }
        caisseMvm.setMtt_art_offert(totalArtOfferte);
        caisseMvm.setMtt_art_reduction(totalArtReduc);
        caisseMvm.setMtt_reduction(totalOffre);
        // Référence commande
        caisseMvm.setLast_statut(statut.toString());
        // Merge 
        caisseMvm = entityManager.merge(caisseMvm);
        entityManager.flush();
        
        // Statut commande
        CaisseMouvementStatutPersistant caisseStatut = new CaisseMouvementStatutPersistant();
        caisseStatut.setOpc_caisse_mouvement(caisseMvm);
        caisseStatut.setDate_statut(new Date());
        if(caisseMvm.getOpc_user() != null && caisseMvm.getOpc_user().getOpc_employe() != null){
        	caisseStatut.setOpc_employe(caisseMvm.getOpc_user().getOpc_employe());
        }
        caisseStatut.setStatut_cmd(statut.toString());
        // Sauvegarde commande **
        entityManager.merge(caisseStatut);
        
        // Maj infos stock si la commande non annulée ou temporaire
        if(!isStatutAnnule && !isCmdTemporaire){
        	// Maj des points de fidélité
	        if(isPoints){
	        	try{
	        		carteFideliteService2.majPointsCarteFideliteClient(caisseMvm);
	        	} catch(Exception e){
	        		e.printStackTrace();
	        	}
	        }
        }
        return caisseMvm;
  }
    
    //---------------------------------- Contrôle qte stock coté caisse --------------------------------//
    @Override
    public Map<Long, BigDecimal> getEtatStockCmdTmp(CaisseMouvementPersistant caisseMvm, Long journeeId){
    	IMouvementService mvmService = ServiceUtil.getBusinessBean(IMouvementService.class);
    	boolean isCtrlStockCaisse = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("INVENTAIRE_CAISSE"));
    	Map<Long, BigDecimal> mapQteTmp = new HashMap<>();
    	
		String req = "from CaisseMouvementPersistant mouvement "
		+ "where mouvement.opc_caisse_journee.opc_journee.id=:journeeId "
		+ "and (mouvement.is_annule is null or mouvement.is_annule = 0) ";
	
		if(caisseMvm != null && caisseMvm.getId() != null) {
			req = req + "and mouvement.id!="+caisseMvm.getId()+" ";
		}
		//
		req = req + "and (mouvement.last_statut = '"+ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.TEMP+"' or (mouvement.last_statut != '"+ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.ANNUL+"' and mouvement.mode_paiement is null)) ";// temporaire ou non payée (cas restau ou caisse autonome)
		
		List<CaisseMouvementPersistant> listCmdTmp = mvmService.getQuery(req)
				.setParameter("journeeId", journeeId)
				.getResultList();
		
		listCmdTmp.add(caisseMvm);
		
		// On ajoute la qte cmd en attente ou cuisine non encore destoquée
		if(isCtrlStockCaisse) {
			for (CaisseMouvementPersistant caisseMouvementP : listCmdTmp) {
				List<CaisseMouvementArticlePersistant> listDetailArt = caisseMouvementP.getList_article();
				
				for(CaisseMouvementArticlePersistant caisseMouvementArtP : listDetailArt) {
		    		if(caisseMouvementArtP.getOpc_article() == null || BooleanUtil.isTrue(caisseMouvementArtP.getIs_annule())){
		    			continue;
		    		}
		    		ArticlePersistant artPTmp = mouvementService.findById(ArticlePersistant.class, caisseMouvementArtP.getOpc_article().getId());
		    		if(artPTmp == null) {
						continue;
					}
					mapQteTmp.put(artPTmp.getId(), BigDecimalUtil.add(mapQteTmp.get(artPTmp.getId()), caisseMouvementArtP.getQuantite()));
		    	}
			 }
		} else {
			for (CaisseMouvementPersistant caisseMouvementP : listCmdTmp) {
				List<CaisseMouvementArticlePersistant> listDetailArt = caisseMouvementP.getList_article();
				
				for(CaisseMouvementArticlePersistant caisseMouvementArtP : listDetailArt) {
		    		if(caisseMouvementArtP.getOpc_article() == null || BooleanUtil.isTrue(caisseMouvementArtP.getIs_annule())){
		    			continue;
		    		}
		    		
		    		ArticlePersistant artPTmp = mouvementService.findById(ArticlePersistant.class, caisseMouvementArtP.getOpc_article().getId());
			    	List<ArticleDetailPersistant> list_article_det = artPTmp.getList_article();
					for(ArticleDetailPersistant artCompTmp : list_article_det) {
						 Long compIdTmp = artCompTmp.getOpc_article_composant().getId();
						 
						 if(compIdTmp == null) {
							 continue;
						 }
						 
						 mapQteTmp.put(compIdTmp, BigDecimalUtil.add(mapQteTmp.get(compIdTmp), BigDecimalUtil.multiply(artCompTmp.getQuantite(), caisseMouvementArtP.getQuantite())));
			    	}
		    	}
			 }
		}
		
		 return mapQteTmp;
    }
    
    @Override
    public boolean isEtatStockArticlesValide(CaisseMouvementPersistant caisseMvm){
    	EmplacementPersistant stockCible = ContextAppliCaisse.getCaisseBean().getOpc_stock_cible();
    	if(stockCible == null){
    		return true;
    	}
    	
    	// Article current cmd
    	List<Long> listCurrent = new ArrayList<>();
		List<CaisseMouvementArticlePersistant> listDetailArt = caisseMvm.getList_article();
		for(CaisseMouvementArticlePersistant caisseMouvementArtP : listDetailArt) {
			if(BooleanUtil.isTrue(caisseMouvementArtP.getIs_annule())){
				continue;
			}
			if(caisseMouvementArtP.getOpc_article() == null || BigDecimalUtil.isZero(caisseMouvementArtP.getQuantite())){
    			continue;
    		}
    		listCurrent.add(caisseMouvementArtP.getOpc_article().getId());
    	}
		//-------------------------------------------------------
		boolean isRestauDev = ContextAppli.IS_RESTAU_ENV();
    	boolean isCtrlStockCaisse = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("INVENTAIRE_CAISSE"));
    	Long jrnId = ContextAppliCaisse.getJourneeBean().getId();
    	Map<Long, BigDecimal> mapQteCmdTmp = getEtatStockCmdTmp(caisseMvm, jrnId);
    	
		EtablissementPersistant etsP = mouvementService.findById(EtablissementPersistant.class, ContextAppli.getEtablissementBean().getId());
		String[] famInvParams = StringUtil.getArrayFromStringDelim(etsP.getFam_caisse_inv(), ";");
		List<Long> famInventaire = new ArrayList<>();
		//
		if(famInvParams != null) {
			for (String famId : famInvParams) {
				if(StringUtil.isEmpty(famId)) {
					continue;
				}
				famInventaire.add(Long.valueOf(famId));	
			}
		}
		
    	if(isCtrlStockCaisse) {
    		for(Long artId : mapQteCmdTmp.keySet()) {
    			if(!listCurrent.contains(artId)) {
    				continue;
    			}
    			ArticlePersistant artP = mouvementService.findById(ArticlePersistant.class, artId);
    			Long famArtId = isRestauDev ? artP.getOpc_famille_cuisine().getId() : artP.getOpc_famille_stock().getId();
				if(!famInventaire.contains(famArtId)) {
					continue;
				}
				
    			List<ArticleStockCaisseInfoPersistant> listArt = mouvementService.getQuery("from ArticleStockCaisseInfoPersistant "
						+ "where opc_emplacement.id=:emplId "
						+ "and opc_article.id=:artId")
						.setParameter("emplId", stockCible.getId())
						.setParameter("artId", artId)
						.getResultList();
				ArticleStockCaisseInfoPersistant infoP = (listArt.size() > 0 ? listArt.get(0) : null);
				if(infoP != null) {
					BigDecimal qteFinal = BigDecimalUtil.substract(infoP.getQte_reel(), mapQteCmdTmp.get(artId));
					if(BigDecimalUtil.ZERO.compareTo(qteFinal) > 0){
						ArticlePersistant article = familleDao.findById(ArticlePersistant.class, artId);
						MessageService.addGrowlMessage("", "<h4>Stock inssufisant pour <b style='color:blue;'>"+article.getLibelle().toUpperCase()+"</b>. " 
								+"Il manque : <b style='color:blue;'>"+BigDecimalUtil.formatNumberZero(qteFinal.abs())+"</b></h4>");
					}
	    		}
    		}
    	} else {
	    	for(Long artId : mapQteCmdTmp.keySet()) {
	    		if(!listCurrent.contains(artId)) {
    				continue;
    			}
	    		ArticlePersistant artP = mouvementService.findById(ArticlePersistant.class, artId);
	    		Long famArtId = isRestauDev ? artP.getOpc_famille_cuisine().getId() : artP.getOpc_famille_stock().getId();
				if(!famInventaire.contains(famArtId)) {
					continue;
				}
				
				ArticleStockInfoPersistant infoP = mouvementService.getArticleEtatStock(artId, stockCible.getId());
				if(infoP != null) {
					BigDecimal qteFinal = BigDecimalUtil.substract(infoP.getQte_reel(), mapQteCmdTmp.get(artId));
					if(BigDecimalUtil.ZERO.compareTo(qteFinal) > 0){
						ArticlePersistant article = familleDao.findById(ArticlePersistant.class, artId);
						MessageService.addGrowlMessage("", "<h4>Stock inssufisant pour <b style='color:blue;'>"+article.getLibelle().toUpperCase()+"</b>. "
								+ "Il manque : <b style='color:blue;'>"+BigDecimalUtil.formatNumberZero(qteFinal.abs())+"</b></h4>");
					}
	    		}
	    	}
    	}
    	//--------------------------------------------------
    	return !MessageService.isError();
    }
    
    @Override
    @Transactional
    public void annulerMouvementCaisse(Long mvmId, UserPersistant userAnnul){
		CaisseMouvementPersistant caisseMvm = (CaisseMouvementPersistant) familleDao.findById(CaisseMouvementPersistant.class, mvmId);
		
		if(caisseMvm == null) {
			return;
		}
		String sens = (BooleanUtil.isTrue(caisseMvm.getIs_retour()) ? "S" : "E");
		String mvm_stock_ids = caisseMvm.getMvm_stock_ids();		
		Long caisseJourneeId = caisseMvm.getOpc_caisse_journee().getId();
		EntityManager em = familleDao.getEntityManager();
		
		if(!caisseMvm.getLast_statut().equals(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.TEMP.toString())){
			caisseMvm.setLast_statut(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.ANNUL.toString());
			caisseMvm.setIs_annule(true);
			if(caisseMvm.getOpc_user_annul() == null) {
				caisseMvm.setOpc_user_annul(userAnnul!=null?userAnnul:ContextAppli.getUserBean());
			}
			caisseMvm.setDate_annul(new Date());
			
	    	List<ArticleStockInfoBean> listArtInfos = new ArrayList<>();
	    	
			if(StringUtil.isNotEmpty(mvm_stock_ids)){
	    		String[] mvmIds = StringUtil.getArrayFromStringDelim(mvm_stock_ids, ";");
	    		for(String mvmIdStr : mvmIds) {
	    			if(StringUtil.isEmpty(mvmIdStr)){
						continue;
					}
	    			MouvementPersistant mvmP = familleDao.findById(MouvementPersistant.class, Long.valueOf(mvmIdStr));
	    			//
		    		for(MouvementArticlePersistant artDet :  mvmP.getList_article()) {
			    		ArticleStockInfoBean artInfo = new ArticleStockInfoBean(mvmP.getOpc_emplacement().getId(), artDet.getOpc_article().getId(), artDet.getQuantite(), sens);
			    		//
			    		listArtInfos.add(artInfo);
		    		}
	    		}
	    	}
	    	caisseMvm.setMvm_stock_ids(null);
			//
			em.merge(caisseMvm);
			em.flush();
	    	
	    	// Purger les mouvements et le détail des anciens mouvements
			if(StringUtil.isNotEmpty(mvm_stock_ids)){
	    		String[] mvmIds = StringUtil.getArrayFromStringDelim(mvm_stock_ids, ";");
	    		for(String mvmIdStr : mvmIds) {
	    			Long mvmStockId = Long.valueOf(mvmIdStr);
	    			
					//restocker les articles destockés
			        mouvementService.majQteArticleInfo(listArtInfos);
			        
			    	// Purger les mouvements et le détail des anciens mouvements
					familleDao.getQuery("delete from MouvementArticlePersistant where mouvement_id=:ids")
						.setParameter("ids", mvmStockId)
						.executeUpdate();
					em.flush();
					
					familleDao.getQuery("delete from MouvementPersistant where id=:ids")
						.setParameter("ids", mvmStockId)
						.executeUpdate();
					
					em.flush();
	    		}
	    	}
			
			boolean isCtrlStockCaisse =  StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("INVENTAIRE_CAISSE"));
			if(isCtrlStockCaisse) {
				mouvementService.majQteArticleCaisseInfo(caisseMvm, true);
			}
		}
		//------------------------------------- FIN PURGE ANNULEE --------------------------------------
    	
		// Commande validée et destockée
		if(!caisseMvm.getLast_statut().equals(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.TEMP.toString())){
			// Remettre les points --------------------------------------------------------------------
			List<CarteFidelitePointsPersistant> listCarteFPointP = familleDao.getQuery("from CarteFidelitePointsPersistant where opc_mvn_caisse.id=:mvmId")
						.setParameter("mvmId", mvmId)
						.getResultList();
			CarteFidelitePointsPersistant carteFPointP = (listCarteFPointP.size() > 0 ? listCarteFPointP.get(0) : null);
			
			if(carteFPointP != null){
				CarteFideliteClientPersistant opc_carte_client = carteFPointP.getOpc_carte_client();
				em.remove(carteFPointP);
				opc_carte_client.setMtt_total(BigDecimalUtil.substract(opc_carte_client.getMtt_total(), carteFPointP.getMtt_gain()));
			}
			// 
			List<CarteFideliteConsoPersistant> listCarteFConsoP = familleDao.getQuery("from CarteFideliteConsoPersistant "
					+ "where opc_mvn_caisse.id=:mvmId")
						.setParameter("mvmId", mvmId)
						.getResultList();
			
			CarteFideliteConsoPersistant carteFConsoP = (listCarteFConsoP.size() > 0 ? listCarteFConsoP.get(0) : null);
			
			if(carteFConsoP != null){
				CarteFideliteClientPersistant opc_carte_client = carteFConsoP.getOpc_carte_client();
				em.remove(carteFConsoP);
				opc_carte_client.setMtt_total(BigDecimalUtil.add(opc_carte_client.getMtt_total(), carteFConsoP.getMtt_conso()));
			}
			
			// Remettre le portefeuille ---------------------------------------------------------------
			List<ClientPortefeuilleMvmPersistant> listClientPortefeuilleMvm = familleDao.getQuery("from ClientPortefeuilleMvmPersistant "
					+ "where opc_mvn_caisse.id=:mvmId")
						.setParameter("mvmId", mvmId)
						.getResultList();
			ClientPortefeuilleMvmPersistant clientPortefeuilleMvm = (listClientPortefeuilleMvm.size() > 0 ? listClientPortefeuilleMvm.get(0) : null);
			if(clientPortefeuilleMvm != null){
				clientPortefeuilleMvm.setIs_annule(true);
				
				em.merge(clientPortefeuilleMvm);
			}
		} else {
			// Suppression des status
			familleDao.getQuery("delete from CaisseMouvementStatutPersistant where caisse_mvm_id=:mvmId")
				.setParameter("mvmId", mvmId)
				.executeUpdate();
			// Suppression des offres
			familleDao.getQuery("delete from CaisseMouvementOffrePersistant where mvm_caisse_id=:mvmId")
					.setParameter("mvmId", mvmId)
					.executeUpdate();
			
			em.remove(caisseMvm);
		}
		
		// Recalcul du shift
		caisseService.refreshDataShift(caisseJourneeId);
    }
    
    
   
	@Override
    public String getNextRefCommande() {
		Long journeeId = ContextAppliCaisse.getJourneeBean().getId();
		String dateJ = DateUtil.dateToString(new Date(), "ddMMyyyy");
	    String nbrNumCmdSimplifie = ContextGloabalAppli.getGlobalConfig("NUM_CMD_SIMPLIFIE");
	    boolean isNumCmdSimplifie = StringUtil.isNotEmpty(nbrNumCmdSimplifie);
	    
	    String maxRef = null;
	    int nextInt = new Random().nextInt(900);
	    //
	    if(!isNumCmdSimplifie){
	    	MessageService.getGlobalMap().put("NO_ETS", true);
	        String req = "select max(CAST(SUBSTR(ref_commande, 13, 3) AS UNSIGNED)) as next_ref from caisse_mouvement cm "
	        		+ "inner join caisse_journee cj on cm.caisse_journee_id=cj.id "+
			" where cj.journee_id=:journeeId and cm.etablissement_id=:etsId";
	        maxRef = ""+familleDao.getSingleResult(familleDao.getNativeQuery(req)
	        		.setParameter("journeeId", journeeId)
	        		.setParameter("etsId", ContextAppli.getEtablissementBean().getId())
	        		);
	        
	        MessageService.getGlobalMap().remove("NO_ETS");
	        
	        if(StringUtil.isNotEmpty(maxRef)){
	            maxRef = ""+(Integer.valueOf(maxRef) + 1);
	            // Ajuster la taille
	             if(maxRef.length() == 1){
	                maxRef = "00"+maxRef;
	            } else if(maxRef.length() == 2){
	                maxRef = "0"+maxRef;
	            }
			} else{
		        maxRef = "001";
		    }
			maxRef = "CM-"+dateJ+'-'+maxRef+"-"+nextInt;
	    } else{
	    	Integer nbrRandom = Integer.valueOf(nbrNumCmdSimplifie);
	        String req = "select max(CAST(SUBSTR(ref_commande, 13, 4) AS UNSIGNED)) "
	        		+ "from caisse_mouvement cm inner join caisse_journee cj on cm.caisse_journee_id=cj.id "
	        		+ "where "
	        		+ "LENGTH(ref_commande)=16 and cj.journee_id=:journeeId";
	        
	        if(ReplicationGenerationEventListener._IS_CLOUD_SYNCHRO_INSTANCE || ContextAppli.IS_FULL_CLOUD()){
	        	req += " and cm.etablissement_id="+ContextAppli.getEtablissementBean().getId();
	        }
	        
	        //-----------------------Etscond---------------------------
	        MessageService.getGlobalMap().put("NO_ETS", 1);
	        maxRef = ""+familleDao.getSingleResult(familleDao.getNativeQuery(req)
	        		.setParameter("journeeId", journeeId));
	        MessageService.getGlobalMap().remove("NO_ETS");
	        
	        int num = 1;
	        if(StringUtil.isNotEmpty(maxRef)){
	        	num = Integer.valueOf(maxRef) + 1;
	        }
	        maxRef = ""+num;
	        
	        int diff = 4-maxRef.length();
	        if(diff > 0 && nbrRandom > 0){
	        	int max = (nbrRandom == 1 ? 9 : (nbrRandom==2?99 : 999));
	        	maxRef = new Random().nextInt(max) +""+maxRef;
	        }
	        
	        while(maxRef.length() < 4){
	        	maxRef = "0"+maxRef;
	        }
	        
	        maxRef = dateJ+"-"+nextInt+maxRef;
	    }
        // Verfisier le numero
	    CaisseMouvementPersistant cm = (CaisseMouvementPersistant) familleDao.getSingleResult(familleDao.getQuery("from CaisseMouvementPersistant where ref_commande=:refCmd")
        	.setParameter("refCmd", maxRef));

        if(cm != null){
        	return getNextRefCommande();
        }
        
        return maxRef;
    }
	
	@SuppressWarnings("unchecked")
	@Override
    public boolean isCustomCallUtilise(Long journeeId, String token) {
        List<CaisseMouvementPersistant> listToken = familleDao.getQuery("from CaisseMouvementPersistant "
                + "where num_token_cmd=:token "
                + "and last_statut!=:statut1 "
                + "and last_statut!=:statut2 "
                + "and last_statut!=:statut3 "
                + "and opc_caisse_journee.opc_journee.id=:journeeId")
                .setParameter("token", token)
                .setParameter("statut1", ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.LIVRE.toString())
                .setParameter("statut2", ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.ANNUL.toString())
                .setParameter("statut3", ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.TEMP.toString())
                .setParameter("journeeId", journeeId)
                .getResultList();
        
        return (listToken != null && !listToken.isEmpty());
    }
	
    @Override
    public List<CaisseMouvementPersistant> findAllCuisine(Long serveurId, 
    		Long caisseId, 
    		Long journeeId, 
    		String typeCmd,
    		Integer startIdx, Integer limit, 
    				ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM ... statut) {
        if(journeeId == null){
            return new ArrayList<>();
        }
        
        String modeTravail =  ContextGloabalAppli.getGlobalConfig("MODE_TRAVAIL_CUISINE");
		modeTravail = (StringUtil.isEmpty(modeTravail) ? "PO" : modeTravail);
		boolean isPrintOnly = "PO".equals(modeTravail) || "PE".equals(modeTravail);
		
        
        List<String> listStatut = new ArrayList<>();
        for (ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM st : statut) {
            listStatut.add(st.toString());
        }
        
        String request = "select distinct det.opc_mouvement_caisse FROM CaisseMouvementArticlePersistant det left join det.opc_article art "
        		+ "WHERE " 
				+ " (det.is_annule is null or det.is_annule = 0) ";
        
				if(isPrintOnly) {
					request += "and ("
							+ "	(det.is_suite_lock is null or det.is_suite_lock = 0) "
							+ " or (det.is_suite_lock is not null and det.is_suite_end is not null)"
							+ ") ";
			      	}
				request += "and (det.opc_mouvement_caisse.is_annule is null or det.opc_mouvement_caisse.is_annule = 0) "
				+ "and (det.last_statut is not null and det.last_statut in (:statut)) ";
        
        if(StringUtil.isNotEmpty(typeCmd)) {
        	request += "and det.opc_mouvement_caisse.type_commande=:tpCmd ";
        }
        
        if(caisseId != null){// Cuisine
        	request += "and ( "
        			+ "		(det.is_menu is not null and det.is_menu=1) "
        			+ "  or (det.type_ligne='ART' and det.menu_idx is null and art.destination='C') "
        			+ "  or (det.type_ligne='ART_MENU' and det.menu_idx is not null and art.destination='C') "
        			+ ")  ";
        } else {// Si présentoire
        	request += "and ( "
        			+ "   (det.is_menu is not null and det.is_menu=1) "
        			+ "or (det.type_ligne='ART' and det.menu_idx is null) "
        			+ "or (det.type_ligne='ART_MENU' and det.menu_idx is not null) "
        			+ ")  ";
        }
		
		if(caisseId != null){
			request = request + "and det.opc_mouvement_caisse.caisse_cuisine like concat('%',"+caisseId+",':',det.id,'%') ";
		}
		
		if(serveurId != null){
			request = request + "and (det.opc_mouvement_caisse.opc_serveur.id is not null and det.opc_mouvement_caisse.opc_serveur.id=:serveurId) ";
		}
		
        request = request + "and det.opc_mouvement_caisse.opc_caisse_journee.opc_journee.id=:journeeId " 
        				  + "order by det.opc_mouvement_caisse.id desc, det.idx_element";
        //
		Query query = familleDao.getQuery(request)
        		.setParameter("statut", listStatut)
                .setParameter("journeeId", journeeId);
	
		if(serveurId != null){
			query.setParameter("serveurId", serveurId);
		}
		if(StringUtil.isNotEmpty(typeCmd)) {
			query.setParameter("tpCmd", typeCmd);
	    }
        if(startIdx != null){
            query.setFirstResult(startIdx);
        }
        if(limit != null){
            query.setMaxResults(limit);
        }    
        
        List<CaisseMouvementPersistant> listCaisseMvm = query.getResultList();
        
        return listCaisseMvm;
    }
    
    /**
     * changer le statut d'une caisse mouvement
     * @param id
     * @param statut
     */
    @Override
    @Transactional
    public Set<Long> changeStatut(Date dtMvm, CaissePersistant caisseP, Long[] caisseMvmId, String ihmStatut) {
   	 	Set<Long> idsDestocked = null;
    	for (Long mvmId : caisseMvmId) {
   	 		CaisseMouvementPersistant cmp = (CaisseMouvementPersistant) familleDao.findById(CaisseMouvementPersistant.class, mvmId);
   	 		// 
			STATUT_CAISSE_MOUVEMENT_ENUM newStatut;
			if(cmp.getLast_statut().equals(STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE.toString())){
				newStatut = STATUT_CAISSE_MOUVEMENT_ENUM.PREP;	
			} else if(cmp.getLast_statut().equals(STATUT_CAISSE_MOUVEMENT_ENUM.PREP.toString())) {
				newStatut = STATUT_CAISSE_MOUVEMENT_ENUM.PRETE;
			} else {
				newStatut = STATUT_CAISSE_MOUVEMENT_ENUM.LIVRE;
			}
   	 				
	        // Changer le statut de tous les détails
	        List<CaisseMouvementArticlePersistant> list_article = new ArrayList(cmp.getList_article());// Pour éviter concurrent modification exception
			for (CaisseMouvementArticlePersistant caisseMvmArtP :  list_article) {
				idsDestocked = changeStatutElement(caisseP, caisseMvmArtP.getId(), ihmStatut);
			}
			// REeverfier le statut de la CMD
			cmp = (CaisseMouvementPersistant) familleDao.findById(CaisseMouvementPersistant.class, mvmId);
			if(!cmp.getLast_statut().equals(newStatut.toString())) {
				cmp.setLast_statut(newStatut.toString());
				familleDao.getEntityManager().merge(cmp);
			}
			
			if(idsDestocked != null){
				idsDestocked.add(mvmId);
			}
   		}
   	 	return idsDestocked;
    }
    
    /**
     * changer le statut d'une caisse mouvement
     * @param id
     * @param statut
     */
    @Override
    @Transactional
    public Set<Long> changeStatutElement(CaissePersistant caisseP, Long elementId, String ihmStatut) {
		CaisseMouvementArticlePersistant cmpElement = (CaisseMouvementArticlePersistant) familleDao.findById(CaisseMouvementArticlePersistant.class, elementId);
		String currElementStatut = (cmpElement.getLast_statut() != null) ? cmpElement.getLast_statut() : ihmStatut;
		
		boolean isMenu = BooleanUtil.isTrue(cmpElement.getIs_menu());
		boolean isArticle = (cmpElement.getType_ligne().equals(ContextAppli.TYPE_LIGNE_COMMANDE.ART.toString()) 
						&& cmpElement.getMenu_idx() == null 
						&& "C".equals(cmpElement.getOpc_article().getDestination())) ||
				(cmpElement.getType_ligne().equals(ContextAppli.TYPE_LIGNE_COMMANDE.ART_MENU.toString()) 
						&& cmpElement.getMenu_idx() != null )
				;
		
		//  Articles et menus paramétrés dans cette caisse
		CaisseMouvementPersistant mvm = cmpElement.getOpc_mouvement_caisse();
		List<Long> listDetail = null;
		
		// 
		STATUT_CAISSE_MOUVEMENT_ENUM newStatut;
		if(currElementStatut.equals(STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE.toString())){
			newStatut = STATUT_CAISSE_MOUVEMENT_ENUM.PREP;	
		} else if(currElementStatut.equals(STATUT_CAISSE_MOUVEMENT_ENUM.PREP.toString())) {
			newStatut = STATUT_CAISSE_MOUVEMENT_ENUM.PRETE;
		} else {
			newStatut = STATUT_CAISSE_MOUVEMENT_ENUM.LIVRE;
		}
				
		//
		if(caisseP != null){
			 listDetail = new ArrayList<>();
	         String[] caisseDestArray = StringUtil.getArrayFromStringDelim(mvm.getCaisse_cuisine(), ";");
	 		 if(caisseDestArray != null){
	 			for(String caisseElement : caisseDestArray){
	 				String[] caisseElementArray = StringUtil.getArrayFromStringDelim(caisseElement, ":");
	 				Long caisseId = Long.valueOf(caisseElementArray[0]);
	 				if(caisseP.getId() == caisseId){
	 					Long currElmntId = Long.valueOf(caisseElementArray[1]);
	 					listDetail.add(currElmntId);
	 				}
	 			}
	 		 }
			
	 		 if(!listDetail.contains(elementId)){
	 			return null;
	 		 }
		}
		
		
        // Last statut
		if(isMenu || isArticle){
			cmpElement.setLast_statut(newStatut.toString());
			
			// PRETE:5;LIVRE:2;VALIDE:1
			int indexOfStatut = (cmpElement.getNbr_statut() != null ? cmpElement.getNbr_statut().indexOf(newStatut.toString()) : -1);
			if(indexOfStatut == -1){
				cmpElement.setNbr_statut(StringUtil.getValueOrEmpty(cmpElement.getNbr_statut())+";"+newStatut+":"+cmpElement.getQuantite());
			} else{
				String[] vals = StringUtil.getArrayFromStringDelim(cmpElement.getNbr_statut(), ";");
				for (String vs : vals) {
					String[] valsDet = StringUtil.getArrayFromStringDelim(vs, ":");
					if(valsDet[0].equals(newStatut.toString())){
						Integer qteCur = BigDecimalUtil.get(valsDet[1]).intValue();
						int newNbr = qteCur + (cmpElement.getQuantite().intValue()-qteCur);
						String newDet = newStatut+":"+newNbr;
						
						String start = cmpElement.getNbr_statut().substring(0, indexOfStatut);
						int indexOfEnd = cmpElement.getNbr_statut().indexOf(";", indexOfStatut+1);
						String end = (indexOfEnd!=-1 ? cmpElement.getNbr_statut().substring(indexOfEnd) : "");
						
						cmpElement.setNbr_statut(start+";"+newDet+";"+end);
						break;
					}
				}
			}
			cmpElement.setNbr_statut(cmpElement.getNbr_statut().replaceAll(";;", ";"));
			
			EntityManager entityManager = familleDao.getEntityManager();
	        
	        cmpElement = entityManager.merge(cmpElement);
			
			entityManager.flush();
		}
		
		//
//		if(!isMenu && !isArticle){
//			return changeCommandeStatut(caisseP, elementId, ihmStatut, newStatut, listDetail, mvm);
//		}
		
		return changeCommandeStatut(caisseP, elementId, ihmStatut, newStatut, listDetail, mvm);
    }
    
    private Set<Long> changeCommandeStatut(CaissePersistant caisseP, Long elementId, String ihmStatut, STATUT_CAISSE_MOUVEMENT_ENUM newStatut, List<Long> listDetail, CaisseMouvementPersistant mvm){
		
		// Changer le statut de toute la commande si tout est changé
		boolean isAllStatutOk = true;
		for (CaisseMouvementArticlePersistant mvmArt : mvm.getList_article()) {
			if(BooleanUtil.isTrue(mvmArt.getIs_annule())){
				continue;
			}
			boolean isMenuSub = BooleanUtil.isTrue(mvmArt.getIs_menu());
			boolean isArticleSub = (mvmArt.getType_ligne().equals(ContextAppli.TYPE_LIGNE_COMMANDE.ART.toString()) 
							&& mvmArt.getMenu_idx() == null 
							&& "C".equals(mvmArt.getOpc_article().getDestination()))
					|| (mvmArt.getType_ligne().equals(ContextAppli.TYPE_LIGNE_COMMANDE.ART_MENU.toString()) 
							&& mvmArt.getMenu_idx() != null );
			//
			if(!isMenuSub && !isArticleSub){
				continue;
			}
			
			//
			if(StringUtil.isNotEmpty(mvmArt.getLast_statut())){
				boolean isLockSuite = (BooleanUtil.isTrue(mvmArt.getIs_suite_lock()) 
								&& !BooleanUtil.isTrue(mvmArt.getIs_suite_end()));
				
				if(isLockSuite) {
					mvmArt.setLast_statut(ihmStatut);
					familleDao.getEntityManager().merge(mvmArt);
					familleDao.getEntityManager().flush();
				}
				
				// Controler si les statuts ne sont pas supérieur aux statut actuels
				boolean isNotStatutEquals = 
						(caisseP == null || listDetail.contains(elementId))
						&& !newStatut.toString().equals(mvmArt.getLast_statut()) 
						&& !STATUT_CAISSE_MOUVEMENT_ENUM.ANNUL.toString().equals(mvmArt.getLast_statut());
				if(isNotStatutEquals || isLockSuite){
					isAllStatutOk = false;
					break;
				}
			}
		}
		// Changer le statut de la commande et destocker
		Set<Long> destockedIds = null;
		if(isAllStatutOk){
			JourneePersistant journeeBean = ContextAppliCaisse.getJourneeBean();
			if(journeeBean == null){
				journeeBean = mvm.getOpc_caisse_journee().getOpc_journee();
			}
			destockedIds = changerStatutCaisseMvm(journeeBean.getDate_journee(), caisseP, new Long[]{mvm.getId()}, ihmStatut);
		}
		
		return destockedIds;
    }
    
    /**
     * Changer le statut de toute la commande
     */
    private Set<Long> changerStatutCaisseMvm(Date dtMvm, CaissePersistant caisseP, Long[] caisseMvmId, String ihmStatut){
    	EntityManager entityManager = familleDao.getEntityManager();
    	STATUT_CAISSE_MOUVEMENT_ENUM newStatut;
    	Set<Long> destockedIds = new HashSet<>();
    	
    	for (Long mvmId : caisseMvmId) {
    		CaisseMouvementPersistant cmp = (CaisseMouvementPersistant) familleDao.findById(CaisseMouvementPersistant.class, mvmId);
    		String currStatutDb = cmp.getLast_statut();
    		//
     		if(!currStatutDb.equals(ihmStatut)) {
    			continue;
    		}
     		
     		Long clientId = null;
     		if(cmp.getOpc_client() != null) {
     			clientId = cmp.getOpc_client().getId();
     		}
    		
    		if(currStatutDb.equals(STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE.toString())){
   				newStatut = STATUT_CAISSE_MOUVEMENT_ENUM.PREP;	
    		} else if(currStatutDb.equals(STATUT_CAISSE_MOUVEMENT_ENUM.PREP.toString())) {
    			newStatut = STATUT_CAISSE_MOUVEMENT_ENUM.PRETE;
    		} else {
    			newStatut = STATUT_CAISSE_MOUVEMENT_ENUM.LIVRE;
    			destockedIds.add(mvmId);
    		}
    		
	        // Last statut
	        cmp.setLast_statut(newStatut.toString());
	        
			entityManager.merge(cmp);
	
	        // Ajout nouveau statut
	        CaisseMouvementStatutPersistant cms = new CaisseMouvementStatutPersistant();
	        cms.setOpc_caisse_mouvement(cmp);
	        cms.setStatut_cmd(newStatut.toString());
	        if(ContextAppli.getUserBean() != null){
	        	cms.setOpc_employe(ContextAppli.getUserBean().getOpc_employe());
	        }
	        cms.setDate_statut(new Date());
	        //
	        entityManager.merge(cms);
	        
	        
	        // Si le statut de toute la commande change alors il faut changer le statut de tou s les détails de la commande
	    	for (CaisseMouvementArticlePersistant det : cmp.getList_article()) {
	    		det.setLast_statut(newStatut.toString());
	    		 entityManager.merge(det);
	    	}
			// Notification
			if(ContextAppli.getAbonementBean().isOptPlusCmdVitrine()) {
				String msg = "Votre commande ** "+cmp.getRef_commande()+" ** a changée de statut.<br>"
						+ "Nouveau statut ** "+ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.valueOf(cmp.getLast_statut()).getLibelle()+" **";
				caisseMobileService.notifierClient("MVM", mvmId, clientId, msg);
			}
    	}
    	
    	return destockedIds;
    }
    
	@Override
	public List<CaissePersistant> getListAfficheurs(Long caisseId) {
		return familleDao.getQuery("from CaissePersistant where opc_caisse.id is not null and opc_caisse.id=:caisseId")
				.setParameter("caisseId", caisseId)
				.getResultList();
	}

	@Override
	public List<CaisseMouvementPersistant> findAllMvmAfficheur(Long journeeId) {
		if(journeeId == null){
            return new ArrayList<>();
        }
        
        //
        String request = "select distinct det.opc_mouvement_caisse FROM CaisseMouvementArticlePersistant det "
        		+ "WHERE (det.is_annule is null or det.is_annule = 0) "
        		+ "and (det.opc_mouvement_caisse.is_annule is null or det.opc_mouvement_caisse.is_annule = 0) "
        		+ "and det.opc_mouvement_caisse.last_statut in :statut "
        		+ "and det.opc_mouvement_caisse.opc_caisse_journee.opc_journee.id=:journeeId ";
        
        request = request + "order by det.opc_mouvement_caisse.id desc";
		Query query = familleDao.getQuery(request)
        		.setParameter("statut", ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.PRETE.toString())
                .setParameter("journeeId", journeeId);
	
        List<CaisseMouvementPersistant> listCaisseMvm = query
        		.setMaxResults(50)
        		.getResultList();
        
        return listCaisseMvm;
	}

	@Override
	public Date getMaxJourneeDate() {
		return (Date)familleDao.getSingleResult(familleDao.getQuery("select max(date_journee) from JourneePersistant"));
	}
	
	//---------------------------------------- DESTOCKAGE DES ARTICLES ---------------------------------------------
	private List<ArticleStockInfoBean> getListDestockCaisse(CaisseMouvementPersistant caisseMvm){
		String type_commande = caisseMvm.getType_commande();
		Long emplId = caisseMvm.getOpc_caisse_journee().getOpc_caisse().getOpc_stock_cible().getId();
        List<ArticleStockInfoBean> listMvmArticle = new ArrayList<>();
        String sens = BooleanUtil.isTrue(caisseMvm.getIs_retour()) ? "E" : "S";
        
        //
        for(CaisseMouvementArticlePersistant detail : caisseMvm.getList_article()){
        	ArticlePersistant opc_article = detail.getOpc_article();
        	if(BooleanUtil.isTrue(detail.getIs_annule()) 
        			|| opc_article == null 
        			|| BigDecimalUtil.isZero(detail.getQuantite())
        			|| "C".equals(opc_article.getDestination())){
        		continue;
        	}
        	//
        	getListDestock(detail.getQuantite(), emplId, type_commande, listMvmArticle, opc_article, sens);
        }
        
        return listMvmArticle;
	}
	
	private List<ArticleStockInfoBean> getListDestock(
			BigDecimal qte,
			Long emplId,
			String type_commande,
			List<ArticleStockInfoBean> listArtInfo, 
			ArticlePersistant articleP, String sens){
		
		articleP = familleDao.findById(ArticlePersistant.class, articleP.getId());
		boolean isRestau = SOFT_ENVS.restau.toString().equals(StrimUtil.getGlobalConfigPropertie("context.soft"));
		List<ArticleDetailPersistant> list_article = null;
		//
		if(isRestau && !BooleanUtil.isTrue(articleP.getIs_stock())){
			list_article = articleP.getList_article();	
		} else if(BooleanUtil.isTrue(articleP.getIs_fiche())) {
			list_article = articleP.getList_article();
		} else {
			listArtInfo.add(new ArticleStockInfoBean(emplId, articleP.getId(), qte, sens));
			return listArtInfo;
		}
		
		if(list_article == null || list_article.size() == 0){
			return listArtInfo;
		}
		
		for (ArticleDetailPersistant artDetP : list_article) {
			// Ignorer les articles paramétrés dans composant avec mode destockage
   			if(StringUtil.isNotEmpty(artDetP.getMode_destock()) 
   	        			&& (
        					(type_commande.equals(TYPE_COMMANDE.E.toString()) && !artDetP.getMode_destock().equals("EL"))
        					|| (type_commande.equals(TYPE_COMMANDE.L.toString()) && !artDetP.getMode_destock().equals("EL"))
        					|| (type_commande.equals(TYPE_COMMANDE.P.toString()) && !artDetP.getMode_destock().equals("P")))){
   				continue;
   			}
   			
   			ArticlePersistant opc_article_composant = artDetP.getOpc_article_composant();
			if(!BooleanUtil.isTrue(opc_article_composant.getIs_fiche())) {
				listArtInfo.add(new ArticleStockInfoBean(emplId, opc_article_composant.getId(), BigDecimalUtil.multiply(qte, artDetP.getQuantite()), sens));
			} else {
				BigDecimal newQte = BigDecimalUtil.multiply(qte, artDetP.getQuantite());
				getListDestock(newQte , emplId, type_commande, listArtInfo, opc_article_composant, sens);
			}
		}
		
		return listArtInfo;
	}
	
	private List<ArticleStockInfoBean> getListDestockCuisine(CaisseMouvementPersistant caisseMvm){
		List<ArticleStockInfoBean> listMvmArticle = new ArrayList<>();
	    List<CaisseMouvementArticlePersistant> listMnuParametre = new ArrayList<>();
	    List<Long> listEmplMnu = new ArrayList<>();
	    String[] elementsCuisine= StringUtil.getArrayFromStringDelim(caisseMvm.getCaisse_cuisine(), ";");
	    String type_commande = caisseMvm.getType_commande();
	    String sens = BooleanUtil.isTrue(caisseMvm.getIs_retour()) ? "E" : "S";
	    // Les articles
	    if(elementsCuisine != null){
	    	for (String element : elementsCuisine) {
	    		String[] detailsCuisine = StringUtil.getArrayFromStringDelim(element, ":");
	    		
	    		if(StringUtil.isEmpty(detailsCuisine[0]) || StringUtil.isEmpty(detailsCuisine[1])){
	    			continue;
	    		}
	    		Long currCaisseId = Long.valueOf(detailsCuisine[0]);
		    	Long elementId = Long.valueOf(detailsCuisine[1]);
		    	//
				CaisseMouvementArticlePersistant cmaP = familleDao.findById(CaisseMouvementArticlePersistant.class, elementId);
				CaissePersistant caisseP = familleDao.findById(CaissePersistant.class, currCaisseId);
				
				if(BooleanUtil.isTrue(cmaP.getIs_annule()) 
						|| BigDecimalUtil.isZero(cmaP.getQuantite())
						|| caisseP.getOpc_stock_cible() == null){
	        		continue;
	        	}
				Long emplId = caisseP.getOpc_stock_cible().getId();
				
				 if(BooleanUtil.isTrue(cmaP.getIs_menu())){
					 listMnuParametre.add(cmaP);
					 listEmplMnu.add(emplId);
				 } else {// Si article
					 getListDestock(cmaP.getQuantite(), emplId, type_commande, listMvmArticle, cmaP.getOpc_article(), sens);
				 }
			}
	    }
	    // Traiter les menus
	    int idx = 0;
	    for(CaisseMouvementArticlePersistant mnuDet : listMnuParametre){
	    	MenuCompositionPersistant mvp = (MenuCompositionPersistant) familleDao.findById(MenuCompositionPersistant.class, mnuDet.getOpc_menu().getId());
	    	Long emplId = listEmplMnu.get(idx);
	    	idx++;
	    	
	    	// Ajouter les articles de destockages paramétrés dans les menu
	    	for (MenuCompositionDetailPersistant menuCompoDetP : mvp.getList_composition()) {
            	ArticlePersistant opc_article_destock = menuCompoDetP.getOpc_article_destock();
            	//
            	if(opc_article_destock != null) {
            		getListDestock(mnuDet.getQuantite(), emplId, type_commande, listMvmArticle, opc_article_destock, sens);
            	}
            }
	    	
	        for(CaisseMouvementArticlePersistant cmdDet : caisseMvm.getList_article()){
        		if(cmdDet.getOpc_article() == null
        				|| cmdDet.getMenu_idx() == null 
        				|| !cmdDet.getMenu_idx().equals(mnuDet.getMenu_idx())
        				|| !"C".equals(cmdDet.getOpc_article().getDestination())){
        			continue;
        		}
        		//
        		getListDestock(cmdDet.getQuantite(), emplId, type_commande, listMvmArticle, cmdDet.getOpc_article(), sens);
	        }
	    }
        
        return listMvmArticle;
    }

	@Override
	@Transactional
	public List<ArticleStockInfoBean> destockerArticleMvmRestau(CaisseMouvementPersistant caisseMvm){
		String statut = caisseMvm.getLast_statut();
		if(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.ANNUL.toString().equals(statut) 
				|| ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.TEMP.toString().equals(statut)){
			return null;
		}
		
		String sens = (BooleanUtil.isTrue(caisseMvm.getIs_retour()) ? "E" : "S");
		
		EntityManager em = familleDao.getEntityManager();
		Map<Long, BigDecimal> mapAllArt= new HashMap<>();
		for (CaisseMouvementArticlePersistant det : caisseMvm.getList_article()) {
			if(det.getOpc_article() == null) {
				continue;
			}
			ArticlePersistant opc_article = familleDao.findById(ArticlePersistant.class, det.getOpc_article().getId());
        	if(BooleanUtil.isTrue(det.getIs_annule()) 
        			|| opc_article == null 
        			|| opc_article.getList_article().isEmpty()
        			|| BigDecimalUtil.isZero(det.getQuantite())){
        		continue;
        	}
        	for(ArticleDetailPersistant artP : opc_article.getList_article()) {
        		if(artP.getOpc_article_composant() == null) {
        			continue;
        		}
        		BigDecimal qte = BigDecimalUtil.multiply(artP.getQuantite(), det.getQuantite());
				mapAllArt.put(artP.getOpc_article_composant().getId(), BigDecimalUtil.add(mapAllArt.get(opc_article.getId()), qte));
        	}
		}
		List<ArticleStockInfoBean> listAll = new ArrayList<>();
		listAll.addAll(getListDestockCaisse(caisseMvm));
		listAll.addAll(getListDestockCuisine(caisseMvm));
		// Ajouter les articles non destoqués par les postes cuisines
		for(Long artId : mapAllArt.keySet()) {
			boolean isFounded = false;
			for(ArticleStockInfoBean articleStockInfoP : listAll) {
				if(articleStockInfoP.getArticleId() == artId) {
					isFounded = true;
					break;
				}
			}
			if(!isFounded) {
				listAll.add(new ArticleStockInfoBean(caisseMvm.getOpc_caisse_journee().getOpc_caisse().getOpc_stock_cible().getId(), artId, mapAllArt.get(artId), sens));
			}
		}
		// Trier par emplacement
		Collections.sort(listAll, new Comparator<ArticleStockInfoBean>() {
           	public int compare(final ArticleStockInfoBean object1, final ArticleStockInfoBean object2) {
               	return object1.getEmplId().compareTo(object2.getEmplId());
           	}
        });
		// Un mouvement par emplacement
		Map<Long, List<ArticleStockInfoBean>> mapInfos = new HashMap<>();
		for (ArticleStockInfoBean articleStockInfoP : listAll) {
			Long emplId = articleStockInfoP.getEmplId();
			List<ArticleStockInfoBean> listInfos = mapInfos.get(emplId);
			if(listInfos == null) {
				listInfos = new ArrayList<>();
				mapInfos.put(emplId, listInfos);
			}
			listInfos.add(articleStockInfoP);
		}
		for(Long emplId : mapInfos.keySet()) {
			MouvementPersistant mouvementStock = new MouvementPersistant();
	        mouvementStock.setDate_creation(new Date());
	        mouvementStock.setDate_mouvement(caisseMvm.getDate_vente());
	        mouvementStock.setDate_maj(new Date());
			mouvementStock.setOpc_emplacement(familleDao.findById(EmplacementPersistant.class, emplId));
			
			if(BooleanUtil.isTrue(caisseMvm.getIs_retour())){
				mouvementStock.setType_mvmnt(ContextAppli.TYPE_MOUVEMENT_ENUM.rt.toString());
			} else {
				mouvementStock.setType_mvmnt(ContextAppli.TYPE_MOUVEMENT_ENUM.vc.toString());	
			}
	        
	        mouvementStock.setSignature("CAISSE");
	        
	        List<MouvementArticlePersistant> listDetStock = new ArrayList<>();
	        List<ArticleStockInfoBean> listInfos = mapInfos.get(emplId);
	        BigDecimal mttTotalHt = null, mttTotalTtc = null;
	        //
	        for (ArticleStockInfoBean articleStockInfoP : listInfos) {
				MouvementArticlePersistant mvmArticlePersistant = new MouvementArticlePersistant();
		        ArticlePersistant opc_article = familleDao.findById(ArticlePersistant.class, articleStockInfoP.getArticleId());
				mvmArticlePersistant.setOpc_article(opc_article);
		        mvmArticlePersistant.setQuantite(articleStockInfoP.getQte());
		        
		        mvmArticlePersistant.setPrix_ht(opc_article.getPrix_achat_ht());
		        mvmArticlePersistant.setPrix_ht_total(BigDecimalUtil.multiply(opc_article.getPrix_achat_ht(), articleStockInfoP.getQte()));
		        mvmArticlePersistant.setPrix_ttc(opc_article.getPrix_achat_ttc());
		        mvmArticlePersistant.setPrix_ttc_total(BigDecimalUtil.multiply(opc_article.getPrix_achat_ttc(), articleStockInfoP.getQte()));
		      
		        mttTotalHt = BigDecimalUtil.add(mttTotalHt, mvmArticlePersistant.getPrix_ht_total());
		        mttTotalTtc = BigDecimalUtil.add(mttTotalTtc, mvmArticlePersistant.getPrix_ttc_total());
		        
		        //
		        listDetStock.add(mvmArticlePersistant);
			}
	        
	        mouvementStock.setMontant_ttc(mttTotalTtc);
	        mouvementStock.setMontant_ht(mttTotalHt);
	        mouvementStock.setMontant_tva(BigDecimalUtil.substract(mttTotalTtc, mttTotalHt));
	        
	        mouvementStock.setList_article(listDetStock);
	        mouvementStock = em.merge(mouvementStock);
	        em.flush();
	        
	        CaisseMouvementPersistant caisseMvmP = familleDao.findById(CaisseMouvementPersistant.class, caisseMvm.getId());
	        caisseMvmP.setMvm_stock_ids(StringUtil.getValueOrEmpty(caisseMvmP.getMvm_stock_ids())+";"+mouvementStock.getId());
	        //
	        em.merge(caisseMvmP);
	        em.flush();
		}
		
		return listAll;
	}
	
	@Override
	@Transactional
	public List<ArticleStockInfoBean> destockerArticleMvmNonRestau(CaisseMouvementPersistant caisseMvm){
		EmplacementPersistant opc_stock_cible = caisseMvm.getOpc_caisse_journee().getOpc_caisse().getOpc_stock_cible(); 
		if(opc_stock_cible == null){
			return null;
		}
		
		EntityManager em = familleDao.getEntityManager();
		Date dtMvm = caisseMvm.getOpc_caisse_journee().getOpc_journee().getDate_journee();
		String statut = caisseMvm.getLast_statut();
		if(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.ANNUL.toString().equals(statut) 
				|| ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.TEMP.toString().equals(statut)){
			return null;
		}
		
		List<ArticleStockInfoBean> listInfos = getListDestockCaisse(caisseMvm);
		
		MouvementPersistant mouvementStock = new MouvementPersistant();
        mouvementStock.setDate_creation(new Date());
        mouvementStock.setDate_mouvement(caisseMvm.getDate_vente());
        mouvementStock.setDate_maj(new Date());
		mouvementStock.setOpc_emplacement(familleDao.findById(EmplacementPersistant.class, opc_stock_cible.getId()));
        
        if(BooleanUtil.isTrue(caisseMvm.getIs_retour())){
			mouvementStock.setType_mvmnt(ContextAppli.TYPE_MOUVEMENT_ENUM.rt.toString());
		} else {
			mouvementStock.setType_mvmnt(ContextAppli.TYPE_MOUVEMENT_ENUM.vc.toString());	
		}
        
        mouvementStock.setSignature("CAISSE");
        
        
        BigDecimal mttTotalHt = null, mttTotalTtc = null;
        List<MouvementArticlePersistant> listDetStock = new ArrayList<>();
        for (ArticleStockInfoBean articleStockInfoP : listInfos) {
			MouvementArticlePersistant mvmArticlePersistant = new MouvementArticlePersistant();
	        ArticlePersistant opc_article = familleDao.findById(ArticlePersistant.class, articleStockInfoP.getArticleId());
			mvmArticlePersistant.setOpc_article(opc_article);
	        mvmArticlePersistant.setQuantite(articleStockInfoP.getQte());
	        //
	        mvmArticlePersistant.setPrix_ht(opc_article.getPrix_achat_ht());
	        mvmArticlePersistant.setPrix_ht_total(BigDecimalUtil.multiply(opc_article.getPrix_achat_ht(), articleStockInfoP.getQte()));
	        mvmArticlePersistant.setPrix_ttc(opc_article.getPrix_achat_ttc());
	        mvmArticlePersistant.setPrix_ttc_total(BigDecimalUtil.multiply(opc_article.getPrix_achat_ttc(), articleStockInfoP.getQte()));
	      
	        mttTotalHt = BigDecimalUtil.add(mttTotalHt, mvmArticlePersistant.getPrix_ht_total());
	        mttTotalTtc = BigDecimalUtil.add(mttTotalTtc, mvmArticlePersistant.getPrix_ttc_total());
	        //
	        listDetStock.add(mvmArticlePersistant);
		}
        
        mouvementStock.setMontant_ttc(mttTotalTtc);
        mouvementStock.setMontant_ht(mttTotalHt);
        mouvementStock.setMontant_tva(BigDecimalUtil.substract(mttTotalTtc, mttTotalHt));
        
        mouvementStock.setList_article(listDetStock);
        mouvementStock = em.merge(mouvementStock);
        
        CaisseMouvementPersistant caisseMvmP = familleDao.findById(CaisseMouvementPersistant.class, caisseMvm.getId());
        caisseMvmP.setMvm_stock_ids(StringUtil.getValueOrEmpty(caisseMvmP.getMvm_stock_ids())+";"+mouvementStock.getId());
        //
        em.merge(caisseMvmP);
        em.flush();
        
	    return listInfos;
	}

	@Override
	public ArticleClientPrixPersistant getArticleClientPrix(Long clientId, Long articleId) {
		return (ArticleClientPrixPersistant) familleDao.getSingleResult(familleDao.getQuery("from ArticleClientPrixPersistant where opc_client.id=:clientId "
				+ "and opc_article.id=:articleId ")
				.setParameter("clientId", clientId)
				.setParameter("articleId", articleId) 
		);
	}
}
