package appli.model.domaine.stock.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.stock.bean.ArticleStockInfoBean;
import appli.controller.domaine.stock.bean.MouvementBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_ECRITURE;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_MOUVEMENT_ENUM;
import appli.model.domaine.administration.persistant.GedFichierPersistant;
import appli.model.domaine.administration.persistant.GedPersistant;
import appli.model.domaine.administration.persistant.ValTypeEnumPersistant;
import appli.model.domaine.administration.service.ICompteBancaireService;
import appli.model.domaine.administration.service.IGedService;
import appli.model.domaine.administration.service.ITreeService;
import appli.model.domaine.caisse.persistant.ArticleStockCaisseInfoPersistant;
import appli.model.domaine.compta.persistant.PaiementPersistant;
import appli.model.domaine.personnel.persistant.ClientPersistant;
import appli.model.domaine.stock.dao.IArticleDao;
import appli.model.domaine.stock.dao.IMouvementDao;
import appli.model.domaine.stock.persistant.ArticleDetailPersistant;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.persistant.ArticleStockInfoPersistant;
import appli.model.domaine.stock.persistant.EmplacementPersistant;
import appli.model.domaine.stock.persistant.EmplacementSeuilPersistant;
import appli.model.domaine.stock.persistant.FournisseurPersistant;
import appli.model.domaine.stock.persistant.LotArticlePersistant;
import appli.model.domaine.stock.persistant.MouvementArticlePersistant;
import appli.model.domaine.stock.persistant.MouvementPersistant;
import appli.model.domaine.stock.service.IFamilleService;
import appli.model.domaine.stock.service.IInventaireService;
import appli.model.domaine.stock.service.ILotArticleService;
import appli.model.domaine.stock.service.IMouvementService;
import appli.model.domaine.stock.validator.MouvementValidator;
import appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import framework.controller.ContextGloabalAppli;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.common.annotation.validator.WorkModelMethodValidator;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.DateUtil.TIME_ENUM;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;
import framework.model.service.GenericJpaService;

@WorkModelClassValidator(validator=MouvementValidator.class)
@Named
public class MouvementService extends GenericJpaService<MouvementBean, Long> implements IMouvementService{
	private final static Logger LOGGER = Logger.getLogger(MouvementService.class);
	
	@Inject
	private IGedService gedService;
	@Inject
	private IArticleDao articleDao;
	@Inject
	private IMouvementDao mouvementDao;
	@Inject
	private IInventaireService inventaireService;
	@Inject
	private ICompteBancaireService compteBancaireService;
	@Inject
	private IFamilleService familleService;
	@Inject
	private ILotArticleService lotArtService;
	@Inject
	private ITreeService treeService;
	
	@Override
	public List<MouvementPersistant> getMouvementByTypeRef(String typeRef) { // ref: Facture, BL et Recu
		
		if(typeRef.equals("F")) { // Facture
			return mouvementDao.getQuery("from MouvementPersistant where num_facture is not null order by num_facture")
					.getResultList();
		} else if(typeRef.equals("B")) { // BL
			return mouvementDao.getQuery("from MouvementPersistant where num_bl is not null order by num_bl")
					.getResultList();
		} if(typeRef.equals("R")) { // Recu
			return mouvementDao.getQuery("from MouvementPersistant where num_recu is not null order by num_recu")
					.getResultList();
		}
		return null;
	}
	@Override
	public MouvementPersistant getMouvementByReference(String refMvm){
			return (MouvementPersistant) getSingleResult(mouvementDao.getQuery("from MouvementPersistant where num_facture=:ref "
					+ "or num_bl=:ref"
					+ "or num_recu=:ref"
					+ "order by num_facture")
					.setParameter("ref", refMvm));
	}
	
	/**
	 * @param emplacementP
	 * @param articleP
	 * @param maxDateInventaireDest
	 */
	@Override
	@Transactional
	public void majQteArticleInfo(List<ArticleStockInfoBean> listArtInfos) {
		
		if(listArtInfos == null) {
			return;
		}
		EntityManager em = getEntityManager();
		em.flush();
		
		
		for(ArticleStockInfoBean artInfo : listArtInfos){
			if(artInfo.getEmplId() == null || artInfo.getArticleId() == null) {
				continue;
			}
			
			EmplacementPersistant emplacementP = (EmplacementPersistant) em.find(EmplacementPersistant.class, artInfo.getEmplId());
			List<ArticleStockInfoPersistant> listArts = getQuery("from ArticleStockInfoPersistant where opc_article.id=:articleId "
					+ "and opc_emplacement.id=:emplacementId")
					.setParameter("articleId", artInfo.getArticleId())
					.setParameter("emplacementId", emplacementP.getId())
					.getResultList();
			
			ArticleStockInfoPersistant articleInfoP = (listArts.size() > 0 ? listArts.get(0) : null);
	
			ArticlePersistant articleP = em.find(ArticlePersistant.class, artInfo.getArticleId());
			if(articleInfoP == null) {
				articleInfoP = new ArticleStockInfoPersistant();
				articleInfoP.setOpc_article(articleP);
				articleInfoP.setOpc_emplacement(emplacementP);
			}
			if(artInfo.getSens().equals("E")) {
				articleInfoP.setQte_entree(BigDecimalUtil.add(articleInfoP.getQte_entree(), artInfo.getQte()));
			} else {
				articleInfoP.setQte_sortie(BigDecimalUtil.add(articleInfoP.getQte_sortie(), artInfo.getQte()));	
			}
			articleInfoP.setQte_reel(BigDecimalUtil.substract(articleInfoP.getQte_entree(), articleInfoP.getQte_sortie()));
			
			em.merge(articleInfoP);
			em.flush();
		}
	}
	
	@PersistenceUnit
	EntityManagerFactory emf;
	@Override
    public void	majQteStockArticle(Long emplMvmId, Long destMvmId, List<MouvementArticlePersistant> listDetail) {
		if(listDetail == null || listDetail.size() == 0){
			return;	
		}
		System.out.println("DEBUT MAJ STOCK INFO .....");
		EntityManager em = emf.createEntityManager();
		try {
			EtablissementPersistant opcEts = null;
			Set<String> listArtEmpl = new HashSet<>();
			for (MouvementArticlePersistant mvmDet : listDetail) {
				MouvementArticlePersistant mvmDetSub = findById(MouvementArticlePersistant.class, mvmDet.getId());
				if(mvmDetSub != null){
					mvmDet = mvmDetSub;
				}
				//
				MouvementPersistant opc_mouvement = mvmDet.getOpc_mouvement();
				if(emplMvmId == null && opc_mouvement!=null && opc_mouvement.getOpc_emplacement()!=null){
					emplMvmId = opc_mouvement.getOpc_emplacement().getId();
				}
				if(destMvmId == null && opc_mouvement!=null && opc_mouvement.getOpc_destination()!=null){
					destMvmId = opc_mouvement.getOpc_destination().getId();
				}
				
				listArtEmpl.add(emplMvmId+"_"+mvmDet.getOpc_article().getId());
				
				if(destMvmId != null){
					listArtEmpl.add(destMvmId+"_"+mvmDet.getOpc_article().getId());
				}
				opcEts = opc_mouvement.getOpc_etablissement();
			}
			
	    	for (String emplArtKey : listArtEmpl) {
	    		String[] data = StringUtil.getArrayFromStringDelim(emplArtKey, "_");
	    		if(StringUtil.isEmpty(data[0]) || StringUtil.isEmpty(data[1])){
	    			continue;
	    		}
	    		
	    		em.getTransaction().begin();
	    		
	    		Long emplId = Long.valueOf(data[0]);
	    		Long artId = Long.valueOf(data[1]);
	    		
	    		List<ArticleStockInfoPersistant> listArticleInfo = em.createQuery("from ArticleStockInfoPersistant "
	    				+ "where opc_article.id=:articleId and opc_emplacement.id=:emplacementId")
						.setParameter("articleId", artId)
						.setParameter("emplacementId", emplId)
						.getResultList();
				
				ArticleStockInfoPersistant articleInfoP = null;
				if(listArticleInfo.size() > 0){
					articleInfoP = listArticleInfo.get(0);
				} else{
					articleInfoP = new ArticleStockInfoPersistant();
					articleInfoP.setOpc_article(em.find(ArticlePersistant.class, artId));
					articleInfoP.setOpc_emplacement(em.find(EmplacementPersistant.class, emplId));
					articleInfoP.setOpc_etablissement(opcEts);
					articleInfoP.setQte_entree(BigDecimalUtil.ZERO);
					articleInfoP.setQte_sortie(BigDecimalUtil.ZERO);
					articleInfoP.setQte_reel(BigDecimalUtil.substract(articleInfoP.getQte_entree(), articleInfoP.getQte_sortie()));
				}
				Object[] infosStock = articleDao.getInfosStock(em, emplId, artId);
				BigDecimal qte_entree = BigDecimalUtil.get(""+infosStock[0]);
				BigDecimal qte_sortie = BigDecimalUtil.get(""+infosStock[1]);
				//
				articleInfoP.setQte_entree(qte_entree);
				articleInfoP.setQte_sortie(qte_sortie);
				articleInfoP.setQte_reel(BigDecimalUtil.substract(articleInfoP.getQte_entree(), articleInfoP.getQte_sortie()));
				//
				em.merge(articleInfoP);
				
				em.getTransaction().commit();
	    	}
	    	
	    	System.out.println("FIN MAJ STOCK INFO ==> "+listArtEmpl.size());
		} catch(Exception e){
			em.getTransaction().rollback();
	    	LOGGER.error("Erreur : ", e);
	         throw e;
	    } finally {
			em.close();
		}
	}
	
//	@Override
//	public void majQteStockArticle(Set<Long> articleIds, Long emplSourceId, Long emplDestId) {
//		if(articleIds == null || (emplDestId==null && emplSourceId==null)) {
//			return;
//		}
//		EntityManager em = familleService.getEntityManager();
//		
//		//
//		for (Long article_id : articleIds) {
//			majQteArticleInfo(em , emplSourceId, articleIds, "");
//			majQteArticleInfo(emplDestId, articleIds);
//		}
//	}
//	
	private void majPrixAchat(MouvementPersistant mvmP, boolean isDelete) {
		EntityManager em = familleService.getEntityManager();
		for (MouvementArticlePersistant mvmArticleP : mvmP.getList_article()) {
			ArticlePersistant artP = (ArticlePersistant) em.find(ArticlePersistant.class, mvmArticleP.getOpc_article().getId());
			if(artP == null) {
				continue;
			}
			
			if(mvmArticleP.getOpc_tva_enum() != null) {
				ValTypeEnumPersistant valEnumP = (ValTypeEnumPersistant) mouvementDao.findById(ValTypeEnumPersistant.class, mvmArticleP.getOpc_tva_enum().getId());
				artP.setOpc_tva_enum(valEnumP);
			}
			artP.setPrix_achat_ht(mvmArticleP.getPrix_ht());
			
			String reqCompl = "";
			if(isDelete){
				reqCompl = " and artmvm.id != "+mvmArticleP.getId();
			}
			
			BigDecimal mttTva = BigDecimalUtil.ZERO;
			if(artP.getOpc_tva_enum() != null) {
				mttTva = (BigDecimalUtil.divide(BigDecimalUtil.multiply(artP.getPrix_achat_ht(), BigDecimalUtil.get(artP.getOpc_tva_enum().getCode())), BigDecimalUtil.get(100)));
			}
			BigDecimal mttAchatTTc = BigDecimalUtil.add(artP.getPrix_achat_ht(), mttTva);
			
			Date dateRefPrix = artP.getDate_ref_prix();
			if(dateRefPrix == null){
				String nbrMoisRef =  ContextGloabalAppli.getGlobalConfig("DATE_REF_PRIX");
				Integer nbrMois = (StringUtil.isNotEmpty(nbrMoisRef) ? BigDecimalUtil.get(nbrMoisRef).intValue() : 12);
				if(nbrMois != null){
					dateRefPrix = DateUtil.addSubstractDate(new Date(), TIME_ENUM.MONTH, -nbrMois);
				}
			}
				
			Query query = familleService.getQuery("select avg(case when (opc_mouvement.type_mvmnt='a') then artmvm.prix_ht end) as prix_achat_moyen_ht "
							+ "from MouvementArticlePersistant artmvm "
							+ "where opc_article.id=:artId and artmvm.opc_mouvement.date_mouvement>=:dateRef" + reqCompl);
			query.setParameter("dateRef", dateRefPrix);
			query.setParameter("artId", artP.getId());
			Double prixAchatMoyenHt = (Double) getSingleResult(query);
			
			 query = familleService.getQuery("select avg(case when (opc_mouvement.type_mvmnt='a') then artmvm.prix_ttc end) as prix_achat_moyen_ttc "
					+ "from MouvementArticlePersistant artmvm "
					+ "where opc_article.id=:artId and artmvm.opc_mouvement.date_mouvement>=:dateRef" + reqCompl);
			query.setParameter("dateRef", dateRefPrix);
			query.setParameter("artId", artP.getId());
			Double prixAchatMoyenTtc = (Double) getSingleResult(query);
			
			prixAchatMoyenHt = (prixAchatMoyenHt == null ? 0 : prixAchatMoyenHt);
			prixAchatMoyenTtc = (prixAchatMoyenTtc == null ? 0 : prixAchatMoyenTtc);
			
			artP.setPrix_achat_ttc(mttAchatTTc);
			artP.setMtt_tva(mttTva);
			artP.setPrix_achat_moyen_ht(BigDecimalUtil.get(prixAchatMoyenHt.toString()));
			artP.setPrix_achat_moyen_ttc(BigDecimalUtil.get(prixAchatMoyenTtc.toString()));
			//
			artP = familleService.getEntityManager().merge(artP);
			
			em.flush();
			// Maj prix fiche
			majPrixFichComposant(artP.getId());
		}
	}
	
	@Override
	@Transactional
	@WorkModelMethodValidator
	public MouvementBean update(MouvementBean mouvementBean) {
		boolean isAchat = mouvementBean.getType_mvmnt().equals(ContextAppli.TYPE_MOUVEMENT_ENUM.a.toString());
		boolean isAvoir = mouvementBean.getType_mvmnt().equals(ContextAppli.TYPE_MOUVEMENT_ENUM.av.toString());
		boolean isRetour = mouvementBean.getType_mvmnt().equals(ContextAppli.TYPE_MOUVEMENT_ENUM.rt.toString());
		boolean isVente = mouvementBean.getType_mvmnt().equals(ContextAppli.TYPE_MOUVEMENT_ENUM.v.toString());
//		boolean isTransfert = mouvementBean.getType_mvmnt().equals(ContextAppli.TYPE_MOUVEMENT_ENUM.t.toString());
//		boolean isTransformation = mouvementBean.getType_mvmnt().equals(ContextAppli.TYPE_MOUVEMENT_ENUM.tr.toString());
		Long mvmId = mouvementBean.getId();
		
		List<PaiementPersistant> listPaiement = null;
		try {
			if(mouvementBean.getList_paiement() != null){
				listPaiement = new ArrayList<>(mouvementBean.getList_paiement());
			}
		} catch(Exception e) {// For exception Lazy exception
			
		}
		boolean isInventairePrec = false;
		if(mouvementBean.getOpc_emplacement() != null){
			Date dateMax = inventaireService.getMaxDateInventaireValide(mouvementBean.getOpc_emplacement().getId());
			if(dateMax != null && mouvementBean.getDate_mouvement().before(dateMax)){
				isInventairePrec = true;
			}
		}
		if(mouvementBean.getOpc_destination() != null){
			Date dateMax = inventaireService.getMaxDateInventaireValide(mouvementBean.getOpc_destination().getId());
			if(dateMax != null && mouvementBean.getDate_mouvement().before(dateMax)){
				isInventairePrec = true;
			}
		}
		
		// Celui de la base pour remettre les valeurs en cas inventaire postérieur
		MouvementPersistant mvmDB = mouvementDao.findById(mouvementBean.getId());
		if(isInventairePrec){
			mouvementBean.setOpc_destination(mvmDB.getOpc_destination());
			mouvementBean.setOpc_emplacement(mvmDB.getOpc_emplacement());
			mouvementBean.setDate_mouvement(mvmDB.getDate_mouvement());
			mouvementBean.setList_article(mvmDB.getList_article());
			mouvementBean.setMontant_ht(mvmDB.getMontant_ht());;
			mouvementBean.setMontant_ttc(mvmDB.getMontant_ttc());;
			mouvementBean.setMontant_tva(mvmDB.getMontant_tva());
		} else{
			// Maj prix achat dans détail mouvement
			if(!mouvementBean.getType_mvmnt().equals(ContextAppli.TYPE_MOUVEMENT_ENUM.t.toString()) && !isAchat && !isVente){
				for(MouvementArticlePersistant det : mouvementBean.getList_article()){
					ArticlePersistant artP = (ArticlePersistant) findById(ArticlePersistant.class, det.getOpc_article().getId());
					det.setPrix_ht(artP.getPrix_achat_ht());// On met le dernier prix d'achat connu
					det.setOpc_tva_enum(artP.getOpc_tva_enum());
					det.setPrix_ttc(artP.getPrix_achat_ttc());// On met le dernier prix d'achat connu
				}
			}
			// Remettre les anciens articles à leurs emplacements
			manageStockInfo(mouvementBean, mouvementBean.getMapOldArticleInfo(), false);
		}
		//
		super.update(mouvementBean);

		// Maj des paiements
		if(!isInventairePrec){
			// Maj dernier prix et TVA si achat, prix TTC, prix moyen
			if(isAchat) {
				majPrixAchat(mouvementBean, false);
			}
			// Maj totaux
			updateMttMouvement(mouvementBean, mouvementBean.getId());
		}
		//--------------------------------------------------------------
		TYPE_ECRITURE source = null;
		String lib = "";
		if(isAchat){
			lib = "Achat";
			source = TYPE_ECRITURE.ACHAT;
		} else if(isAvoir){
			lib = "Avoir";
			source = TYPE_ECRITURE.AVOIR;
		} else if(isRetour){
			lib = "Retour commande " + mouvementBean.getRetour_ref_cmd();
			source = TYPE_ECRITURE.RETOUR;
		} else {
			lib = "Vente";
			source = TYPE_ECRITURE.VENTE;
		}
		if(mouvementBean.getOpc_fournisseur()!=null){
			FournisseurPersistant fournP = (FournisseurPersistant)findById(FournisseurPersistant.class, mouvementBean.getOpc_fournisseur().getId());
			lib += " - Fourn. : "+fournP.getLibelle();
		}
		if(StringUtil.isNotEmpty(mouvementBean.getNum_bl())){
			lib += " - BL : "+mouvementBean.getNum_bl(); 
		}
		if(StringUtil.isNotEmpty(mouvementBean.getNum_facture())){
			lib += " - Facture : "+mouvementBean.getNum_facture();
		}
		String sens = (isAchat || isRetour) ? "D":"C";
		compteBancaireService.mergePaiements(
				source, 
				listPaiement, 
				mouvementBean.getOpc_fournisseur(),
				mouvementBean.getOpc_client(),
				mvmId, 
				lib, 
				sens, 
				mouvementBean.getDate_mouvement(),
				BooleanUtil.isTrue(mouvementBean.getIs_facture_comptable()));
		mouvementBean.setList_paiement(listPaiement);
		
		return mouvementBean;
	}
	
	/*
	 * Mettre à jour les montant du mouvement afin de les fournir dans les transferts, 
	 * préparation, ...
	 */
	@Transactional
	private void updateMttMouvement(MouvementBean mouvementBean, Long currentId) {
		boolean isAchat = mouvementBean.getType_mvmnt().equals(ContextAppli.TYPE_MOUVEMENT_ENUM.a.toString());
		boolean isVente = mouvementBean.getType_mvmnt().equals(ContextAppli.TYPE_MOUVEMENT_ENUM.v.toString());
		boolean isRetour = mouvementBean.getType_mvmnt().equals(ContextAppli.TYPE_MOUVEMENT_ENUM.rt.toString());
		
		BigDecimal mttTotalHtTotal = null, mttTotalTtcTotal = null; 
//				mttTotalHtRemise = null,
//				mttTotalTTCRemise = null;
		if(mouvementBean.getList_article() != null) {
			for (MouvementArticlePersistant mouvementArticleP : mouvementBean.getList_article()) {
				// Prix unitaire
				ArticlePersistant article = (ArticlePersistant) findById(ArticlePersistant.class, mouvementArticleP.getOpc_article().getId());
				if(!isVente && !isRetour && !isAchat){ // && 
					mouvementArticleP.setPrix_ht(article.getPrix_achat_ht());
					mouvementArticleP.setPrix_ttc(article.getPrix_achat_ttc());
				} 
				else if(isVente || isRetour) {
					mouvementArticleP.setPrix_ttc(article.getPrix_vente());
					BigDecimal tauxVente = BigDecimalUtil.get(ContextGloabalAppli.getGlobalConfig(ContextAppli.PARAM_APPLI_ENUM.TVA_VENTE.toString()));
					BigDecimal coeficient = BigDecimalUtil.divide(BigDecimalUtil.add(BigDecimalUtil.get(100), tauxVente), BigDecimalUtil.get(100));
					BigDecimal prix_ht = BigDecimalUtil.divide(mouvementArticleP.getPrix_ttc(), coeficient);
					mouvementArticleP.setPrix_ht(prix_ht);
				}
				// Totaux
				BigDecimal mttTotalHt = BigDecimalUtil.multiply(mouvementArticleP.getPrix_ht(), mouvementArticleP.getQuantite());
				BigDecimal mttTotalTtc = BigDecimalUtil.multiply(mouvementArticleP.getPrix_ttc(), mouvementArticleP.getQuantite());
				mttTotalHtTotal = BigDecimalUtil.add(mttTotalHtTotal, mttTotalHt);
				mttTotalTtcTotal = BigDecimalUtil.add(mttTotalTtcTotal, mttTotalTtc);
			}
		}
		
		// Maj mtt total
		MouvementPersistant mP = mouvementDao.findById(currentId);
		
		if(mP.getMontant_ht() == null) {
			mP.setMontant_ht(mttTotalHtTotal);
			mP.setMontant_tva(BigDecimalUtil.substract(mttTotalTtcTotal, mttTotalHtTotal));
			mP.setMontant_ttc(mttTotalTtcTotal);
		}
		
		//en cas de transformation d'une commande en achat (les elements de la liste n'ont pas opc_mouvement)
		//if(mP.getList_article() != null) {
		for (MouvementArticlePersistant mvmArtP : mP.getList_article()) {
			if(mvmArtP.getOpc_mouvement() == null) {
				mvmArtP.setOpc_mouvement(mP);
			}
		}
		//}
		
		getEntityManager().merge(mP);
	}
	
	@Override
	@Transactional
	@WorkModelMethodValidator
	public void mergeTransformation(MouvementBean mouvementBean) {
		List<MouvementArticlePersistant> listArtTarget = mouvementBean.getListArtTarget();
		boolean isCreate = (mouvementBean.getId() == null);
		MouvementBean mouvementBean2 = (MouvementBean) ReflectUtil.cloneBean(mouvementBean);
		
		// Mouvement 1 --------------------------
		mouvementBean.setOpc_destination(null);
		//
		if(isCreate){
			create(mouvementBean);	
		} else{
			MouvementBean mvmDb = findById(mouvementBean.getId());
			mouvementBean.setDate_creation(mvmDb.getDate_creation());
			mouvementBean.setOpc_mouvement(mvmDb.getOpc_mouvement());
			//
			update(mouvementBean);
		}
		
		// Mouvement 2 -------------------------
		mouvementBean2.setOpc_emplacement(null);
		
		Map<Long, BigDecimal> mapOldArticleInfo = new HashMap<>();
		for (MouvementArticlePersistant mvmArtP : listArtTarget) {
			Long artId = mvmArtP.getOpc_article().getId();
			mapOldArticleInfo.put(artId, BigDecimalUtil.add(mapOldArticleInfo.get(artId), mvmArtP.getQuantite()));
		}
		mouvementBean2.setMapOldArticleInfo(mapOldArticleInfo);
		//
		if(isCreate){
			mouvementBean2.setId(null);
			mouvementBean2.setList_article(listArtTarget);
			//
			create(mouvementBean2);	
			
			// Associer le mouvement 2
			MouvementPersistant mvmP = findById(MouvementPersistant.class, mouvementBean.getId());
			mvmP.setOpc_mouvement(mouvementBean2);
			getEntityManager().merge(mvmP);
		} else{
			MouvementPersistant opc_mouvement = mouvementBean.getOpc_mouvement();
			mouvementBean2.setId(opc_mouvement.getId());
			mouvementBean2.setDate_creation(opc_mouvement.getDate_creation());
			mouvementBean2.setList_article(opc_mouvement.getList_article());
			
			mouvementBean2.getList_article().clear();
			mouvementBean2.getList_article().addAll(listArtTarget);
			
			update(mouvementBean2);
		}
	}
	
	@Override
	@Transactional
	@WorkModelMethodValidator
	public void create(MouvementBean mouvementBean) {
		List<PaiementPersistant> listPaiement = null;
		if(mouvementBean.getList_paiement() != null){
			listPaiement = new ArrayList<>(mouvementBean.getList_paiement());	
		}

		// Maj prix achat dans détail mouvement
//		boolean isTransfert = mouvementBean.getType_mvmnt().equals(ContextAppli.TYPE_MOUVEMENT_ENUM.t.toString());
		boolean isAchat = mouvementBean.getType_mvmnt().equals(ContextAppli.TYPE_MOUVEMENT_ENUM.a.toString());
		boolean isVente = mouvementBean.getType_mvmnt().equals(ContextAppli.TYPE_MOUVEMENT_ENUM.v.toString());
		boolean isAvoir = mouvementBean.getType_mvmnt().equals(ContextAppli.TYPE_MOUVEMENT_ENUM.av.toString());
		boolean isRetour = mouvementBean.getType_mvmnt().equals(ContextAppli.TYPE_MOUVEMENT_ENUM.rt.toString());
//		boolean isTransformation = mouvementBean.getType_mvmnt().equals(ContextAppli.TYPE_MOUVEMENT_ENUM.tr.toString());
		//
		
		super.create(mouvementBean);
		Long currMvmId = mouvementBean.getId();
		
		// Stock info
		manageStockInfo(mouvementBean, null, false);
		
		// Maj dernier prix et TVA si achat, prix TTC, prix moyen
		if(isAchat) {
			majPrixAchat(mouvementBean, false);
		}
		// Maj totaux
		updateMttMouvement(mouvementBean, currMvmId);
				
		// Maj des paiements
		String lib = "";
		TYPE_ECRITURE source = null;
		if(isAchat){
			lib = "Achat";
			source = TYPE_ECRITURE.ACHAT;
		} else if(isAvoir){
			lib = "Avoir";
			source = TYPE_ECRITURE.AVOIR;
		} else if(isVente){
			lib = "Vente";
			source = TYPE_ECRITURE.VENTE;
		} else if(isRetour){
			lib = "Retour commande " + mouvementBean.getRetour_ref_cmd();
			source = TYPE_ECRITURE.RETOUR;
		}
		
		if(source != null) {
			if(mouvementBean.getOpc_fournisseur()!=null){
				FournisseurPersistant fournP = (FournisseurPersistant)findById(FournisseurPersistant.class, mouvementBean.getOpc_fournisseur().getId());
				lib += " - Fourn. : "+fournP.getLibelle();
			}
			if(StringUtil.isNotEmpty(mouvementBean.getNum_bl())){
				lib += " - BL : "+mouvementBean.getNum_bl(); 
			}
			if(StringUtil.isNotEmpty(mouvementBean.getNum_facture())){
				lib += " - Facture : "+mouvementBean.getNum_facture();
			}
			String sens = (isAchat || isRetour) ? "D":"C";
			compteBancaireService.mergePaiements(
					source, 
					listPaiement, 
					mouvementBean.getOpc_fournisseur(),
					mouvementBean.getOpc_client(),
					currMvmId, 
					lib, 
					sens, 
					mouvementBean.getDate_mouvement(), 
					BooleanUtil.isTrue(mouvementBean.getIs_facture_comptable()));
		}
		
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, StrimUtil.label("work.update.title"), StrimUtil.label("work.create.succes"));
	}

	@Override
	@Transactional
	@WorkModelMethodValidator
	public void delete(Long id) {
		MouvementBean mvmP = findById(id);
		
		boolean isAchat = mvmP.getType_mvmnt().equals(ContextAppli.TYPE_MOUVEMENT_ENUM.a.toString());
		boolean isAvoir = mvmP.getType_mvmnt().equals(ContextAppli.TYPE_MOUVEMENT_ENUM.av.toString());
		boolean isVente = mvmP.getType_mvmnt().equals(ContextAppli.TYPE_MOUVEMENT_ENUM.v.toString());
		boolean isRetour = mvmP.getType_mvmnt().equals(ContextAppli.TYPE_MOUVEMENT_ENUM.rt.toString());
//		boolean isTransfert = mvmP.getType_mvmnt().equals(ContextAppli.TYPE_MOUVEMENT_ENUM.t.toString());
		boolean isTransformation = mvmP.getType_mvmnt().equals(ContextAppli.TYPE_MOUVEMENT_ENUM.tr.toString());
		
		MouvementBean mouvementBean = findById(id);
		
		Map<Long, BigDecimal> mapOldArticleInfo = new HashMap<>();
		for (MouvementArticlePersistant mvmArtP : mouvementBean.getList_article()) {
			Long artId = mvmArtP.getOpc_article().getId();
			mapOldArticleInfo.put(artId, BigDecimalUtil.add(mapOldArticleInfo.get(artId), mvmArtP.getQuantite()));
		}
		manageStockInfo(mouvementBean, mapOldArticleInfo, true);
		
		MouvementPersistant opc_mouvement = mvmP.getOpc_mouvement();
		if(isTransformation && opc_mouvement != null) {
			Map<Long, BigDecimal> mapOldArticleInfo2 = new HashMap<>();
			for (MouvementArticlePersistant mvmArtP : opc_mouvement.getList_article()) {
				Long artId = mvmArtP.getOpc_article().getId();
				mapOldArticleInfo2.put(artId, BigDecimalUtil.add(mapOldArticleInfo2.get(artId), mvmArtP.getQuantite()));
			}
			manageStockInfo(mouvementBean, mapOldArticleInfo2, true);
		}
		
		// Maj des groupes
		getQuery("update MouvementPersistant set mouvement_group_id=NULL where mouvement_group_id=:mvmGrpId")
				.setParameter("mvmGrpId", id)
				.executeUpdate();
		
		// purger les lots
		for (MouvementArticlePersistant mvmArtPer : mvmP.getList_article()) {
			if(mvmArtPer.getDate_peremption() != null) {
				if(mvmArtPer.getOpc_lot_article() != null) {
					LotArticlePersistant lotArt = mvmArtPer.getOpc_lot_article();
					mvmArtPer.setOpc_lot_article(null);
					lotArtService.delete(lotArt.getId());
				}
			}
		}
		
		// Maj dernier prix et TVA si achat, prix TTC, prix moyen
		majPrixAchat(mouvementBean, true);
		
		super.delete(id);
		
		// Suppression du mouvement lié en cas de transformation
		if(isTransformation && opc_mouvement != null){
			super.delete(opc_mouvement.getId());
		}
		
		TYPE_ECRITURE source = null;
		if(isAchat){
			source = TYPE_ECRITURE.ACHAT;
		} else if(isAvoir){
			source = TYPE_ECRITURE.AVOIR;
		} else if(isVente){
			source = TYPE_ECRITURE.VENTE;
		} else if(isRetour) {
			source = TYPE_ECRITURE.RETOUR;
		}
		
		// Supprimer les paiements
		if(source != null) {
			compteBancaireService.deletePaiements(id, source);
		}
	}

	@Override
	public Date getMaxDate(String type) {
		Date dateMax = (Date) getSingleResult(getQuery("select max(date_mouvement) from MouvementPersistant "
				+ "where type_mvmnt=:type")
				.setParameter("type", type));
		if(dateMax == null){
			return new Date();
		}
		return dateMax;
	}
		
	@Override
	public String generateNumFac(TYPE_MOUVEMENT_ENUM type) {
		Date currentDate = DateUtil.getCurrentDate(); 
		SimpleDateFormat formater =  new SimpleDateFormat("yyMMdd");
		String date = formater.format(currentDate);
		
		Query query = getQuery("select max(num_facture) from EtablissementPersistant");
		Integer max_num = (Integer)query.getSingleResult();
		
		if(max_num == null){
			max_num = 0;
		}
		max_num = max_num + 1;
		
		String max = ""+max_num;
		
		if(max.length() == 1){
			max = "00"+max;
		} else if(max.length() == 2){
			max = "0"+max;
		}
		
		String numFac = "";
		if(type.equals(TYPE_MOUVEMENT_ENUM.a)){
			numFac = "AC-"+date+"-"+max;
		} else if(type.equals(TYPE_MOUVEMENT_ENUM.v)){
			numFac = "VN-"+date+"-"+max;
		} else if(type.equals(TYPE_MOUVEMENT_ENUM.av)){
			numFac = "AV-"+date+"-"+max;
		} else if(type.equals(TYPE_MOUVEMENT_ENUM.tr)){
			numFac = "TR-"+date+"-"+max;
		} else if(type.equals(TYPE_MOUVEMENT_ENUM.cm)){
			numFac = "CMD-"+date+"-"+max;
		} else if(type.equals(TYPE_MOUVEMENT_ENUM.rt)){
			numFac = "RET-"+date+"-"+max;
		}else {
			numFac = "BL-"+date+"-"+max;
		}
		
		return numFac;
	}
	
	@Override
	public String generateNumBl(String type) {
		
		Date currentDate = DateUtil.getCurrentDate(); 
		SimpleDateFormat formater =  new SimpleDateFormat("yyMMdd");
		String date = formater.format(currentDate);
		
		int maxNum = mouvementDao.max_numBl(date);
		String max = "001";
		String numBl = "";
		
		if(StringUtil.isNotEmpty(maxNum)){
			max = maxNum+1+"";
		}
		if(max.length() == 1){
			max = "00"+max;
		} else if(max.length() == 2){
			max = "0"+max;
		}
		
		if(type.equals(TYPE_MOUVEMENT_ENUM.a.toString())){
			numBl = "AC-"+date+"-"+max;
		} else if(type.equals(TYPE_MOUVEMENT_ENUM.v.toString())){
			numBl = "VN-"+date+"-"+max;
		} else if(type.equals(TYPE_MOUVEMENT_ENUM.av.toString())){
			numBl = "AV-"+date+"-"+max;
		} else if(type.equals(TYPE_MOUVEMENT_ENUM.tr.toString())){
			numBl = "TR-"+date+"-"+max;
		} else if(type.equals(TYPE_MOUVEMENT_ENUM.cm.toString())){
			numBl = "CMD-"+date+"-"+max;
		} else if(type.equals(TYPE_MOUVEMENT_ENUM.rt.toString())){
			numBl = "RET-"+date+"-"+max;
		}else {
			numBl = "BL-"+date+"-"+max;
		}
		
		return numBl;
	}

	@Override
	@Transactional
	public ArticleStockInfoPersistant getArticleEtatStock(Long articleId, Long emplacementId) {
		List<ArticleStockInfoPersistant> listData = getQuery("from ArticleStockInfoPersistant "
				+ "where opc_article.id=:artId and opc_emplacement.id=:emplacementId")
					.setParameter("artId", articleId)
					.setParameter("emplacementId", emplacementId)
					.getResultList();
		
		if(listData.size() == 1) {
			return listData.get(0);
		} else if(listData.size() > 1) {
			int idx = 0;
			for(ArticleStockInfoPersistant det : listData) {
				if(idx > 0) {
					getEntityManager().remove(det);
				}
				idx++;
			}
			return listData.get(0);
		}
		
		return null;
	}

	@Override
	public List<MouvementPersistant> getMvmNonGroupe(Long fournisseurId, List<Long> ignoreIds, Date dateDebut, Date dateFin) {
		String req = "from MouvementPersistant where opc_fournisseur.id=:fournId and type_mvmnt='a' ";
		
		if(ignoreIds != null && ignoreIds.size() > 0) {
			req = req + "and (mouvement_group_id is null or id in (:ignoreIds)) ";
		} else{
			req = req + "and mouvement_group_id is null ";
		}
		
		if(dateDebut != null){
			req = req + "and date_mouvement>=:dtDebut ";
		}
		if(dateFin != null){
			req = req + "and date_mouvement<=:dtFin ";
		}
		req = req + "order by id";
		
		Query query = getQuery(req).setParameter("fournId", fournisseurId);
		
		if(ignoreIds != null && ignoreIds.size() > 0) {
			query.setParameter("ignoreIds", ignoreIds);
		}
		if(dateDebut != null){
			query.setParameter("dtDebut", dateDebut);
		}
		if(dateFin != null){
			query.setParameter("dtFin", dateFin);
		}
		
		List<MouvementPersistant> listMvm = query.getResultList();
		List<MouvementPersistant> listMvmNonPaye = new ArrayList<>();
		//
		for (MouvementPersistant mouvementP : listMvm) {
			BigDecimal mttPaye = mouvementP.getMttPaye();
			BigDecimal montant_ttc = mouvementP.getMontant_ttc();
			montant_ttc = (montant_ttc == null ? BigDecimalUtil.ZERO : montant_ttc);
			
			if(mttPaye.compareTo(montant_ttc) != 0) {
				listMvmNonPaye.add(mouvementP);
			}
		}

		return listMvmNonPaye;
	}
	
	@Override
	public List<MouvementPersistant> getMvmAvoirNonGroupe(Long fournisseurId, List<Long> ignoreIds, Date dateDebut, Date dateFin) {
		String req = "from MouvementPersistant where opc_fournisseur.id=:fournId and type_mvmnt='av' ";
		
		if(ignoreIds != null && ignoreIds.size() > 0) {
			req = req + "and (mouvement_group_id is null or id in (:ignoreIds)) ";
		} else{
			req = req + "and mouvement_group_id is null ";
		}
		
		if(dateDebut != null){
			req = req + "and date_mouvement>=:dtDebut ";
		}
		if(dateFin != null){
			req = req + "and date_mouvement<=:dtFin ";
		}
		req = req + "order by id";
		
		Query query = getQuery(req).setParameter("fournId", fournisseurId);
		
		if(ignoreIds != null && ignoreIds.size() > 0) {
			query.setParameter("ignoreIds", ignoreIds);
		}
		if(dateDebut != null){
			query.setParameter("dtDebut", dateDebut);
		}
		if(dateFin != null){
			query.setParameter("dtFin", dateFin);
		}
		
		List<MouvementPersistant> listMvm = query.getResultList();
		List<MouvementPersistant> listMvmNonPaye = new ArrayList<>();
		//
		for (MouvementPersistant mouvementP : listMvm) {
			if(mouvementP.getList_paiement().size() == 0) {
				listMvmNonPaye.add(mouvementP);
			}
		}

		return listMvmNonPaye;
	}

	@Override
	@Transactional
	public void valideRegroupementBL(MouvementBean mvmBean) {
		String login = (ContextAppli.getUserBean() != null ? ContextAppli.getUserBean().getLogin() : "AUTO_C");
		EntityManager entityManager = getEntityManager();
		
		List<PaiementPersistant> listPaiement = null;
		if(mvmBean.getList_paiement() != null){
			listPaiement = new ArrayList<>(mvmBean.getList_paiement());	
		}
		
		//
		MouvementPersistant mvmDb = mvmBean.getId()!=null ? mouvementDao.findById(mvmBean.getId()) : null;
		
		if(mvmDb == null) {
			mvmDb = new MouvementPersistant();
			mvmDb.setType_mvmnt("a");
			mvmDb.setDate_creation(new Date());
			mvmDb.setIs_groupant(true);
		} else {
			// Effacer les anciens
			getQuery("update MouvementPersistant set mouvement_group_id=NULL where mouvement_group_id=:mvmGrpId")
				.setParameter("mvmGrpId", mvmDb.getId())
				.executeUpdate();
		}
		mvmDb.setNum_facture(mvmBean.getNum_facture());
		mvmDb.setOpc_fournisseur(mvmBean.getOpc_fournisseur());
		getEntityManager().flush();
		// Mode de paiement
		mvmDb.setDate_mouvement(mvmBean.getDate_mouvement());
		mvmDb.setNum_facture(mvmBean.getNum_facture());
		//
		mvmDb.setDate_maj(new Date());
		mvmDb.setSignature(login);
		//--------------------------------------------
		mvmDb = entityManager.merge(mvmDb);
		mvmBean.setId(mvmDb.getId());
		
		compteBancaireService.mergePaiements(
				TYPE_ECRITURE.ACHAT, 
				listPaiement, 
				mvmDb.getOpc_fournisseur(),
				mvmDb.getOpc_client(),
				mvmDb.getId(), 
				"Regroupement achats", 
				"D", 
				mvmDb.getDate_mouvement(),
				false);
		
		// Maj des mouvement dépendants
		for(Long mvmId : mvmBean.getMouvementIds()) {
			// Effacer les anciens
			getQuery("update MouvementPersistant set mouvement_group_id=:grpId where id=:mvmId")
				.setParameter("grpId", mvmDb.getId())
				.setParameter("mvmId", mvmId)
				.executeUpdate();
		}
		
		// Maj des avoirs dépendants
		if(mvmBean.getAvoirIds() != null) {
			for(Long mvmAvoirId : mvmBean.getAvoirIds()) {
				// Effacer les anciens
				getQuery("update MouvementPersistant set mouvement_group_id=:grpId where id=:mvmId")
					.setParameter("grpId", mvmDb.getId())
					.setParameter("mvmId", mvmAvoirId)
					.executeUpdate();
			}
		}
	}

	@Override
	public List<MouvementPersistant> getListMvmGroupe(Long mouvement_id) {
		return mouvementDao.getQuery("from MouvementPersistant where mouvement_group_id is not null and mouvement_group_id=:parentId "
				+ "order by date_mouvement desc")
				.setParameter("parentId", mouvement_id)
				.getResultList();
	}
	@Override
	public Map<Long, List<MouvementPersistant>> getMapMvmGroupe(List<MouvementPersistant> listGroup) {
		Set<Long> listIds = new HashSet<>();
		Map<Long, List<MouvementPersistant>> mapData = new HashMap<>();
		//
		for (MouvementPersistant mvmP : listGroup) {
			listIds.add(mvmP.getId());
		}
		List<MouvementPersistant> listData = (listIds.size()>0 ? mouvementDao.getQuery("from MouvementPersistant mvm "
				+ "where mouvement_group_id in (:ids) and type_mvmnt='a' order by mvm.date_mouvement desc")
				.setParameter("ids", listIds)
				.getResultList() : new ArrayList<>());
		//
		for (MouvementPersistant mvm : listData) {
			List<MouvementPersistant> dataBD = mapData.get(mvm.getMouvement_group_id());
			if(dataBD == null){
				dataBD = new ArrayList<>();
				mapData.put(mvm.getMouvement_group_id(), dataBD);
			}
			dataBD.add(mvm);
		}
		
		return mapData;
	}
	
	@Override
	public Map<Long, List<MouvementPersistant>> getMapMvmAvoirGroupe(List<MouvementPersistant> listGroup) {
		Set<Long> listIds = new HashSet<>();
		Map<Long, List<MouvementPersistant>> mapData = new HashMap<>();
		//
		for (MouvementPersistant mvmP : listGroup) {
			listIds.add(mvmP.getId());
		}
		List<MouvementPersistant> listData = (listIds.size()>0 ? mouvementDao.getQuery("from MouvementPersistant mvm "
				+ "where mouvement_group_id in (:ids) and type_mvmnt='av' order by mvm.date_mouvement desc")
				.setParameter("ids", listIds)
				.getResultList() : new ArrayList<>());
		//
		for (MouvementPersistant mvm : listData) {
			List<MouvementPersistant> dataBD = mapData.get(mvm.getMouvement_group_id());
			if(dataBD == null){
				dataBD = new ArrayList<>();
				mapData.put(mvm.getMouvement_group_id(), dataBD);
			}
			dataBD.add(mvm);
		}
		
		return mapData;
	}

	@Override
	public List<MouvementArticlePersistant> getListArticleByType(String[] ids, String type){ 
		
		if(ids == null || ids.length == 0){
			return new ArrayList<>();
		}
		
		if(StringUtil.isEmpty(type)) {
			type = "bl";
		}
		
		String req = "from MouvementArticlePersistant det where "; 
		
		if(type.equals("bl")) {
			req = req + "det.opc_mouvement.num_bl in (:listIds)";
		} else if(type.equals("fc")) {
			req = req + "det.opc_mouvement.num_facture in (:listIds)";
		} else if(type.equals("rc")) {
			req = req + "det.opc_mouvement.num_recu in (:listIds)";
		}
		req = req + "order by det.opc_article.libelle";
		
		List<MouvementArticlePersistant> listDetail = mouvementDao.getQuery(req)
			.setParameter("listIds", Arrays.asList(ids))
			.getResultList();
		
		return listDetail;
	}
	
	@Override
	public String getMsgStockInsuffisant(MouvementBean mouvementBean){
		String msgManque = "";
		if(mouvementBean.getType_mvmnt().equals("t") || mouvementBean.getType_mvmnt().equals("tr") || mouvementBean.getType_mvmnt().equals("v") || mouvementBean.getType_mvmnt().equals("c")){
			List<MouvementArticlePersistant> listArticle = mouvementBean.getList_article();
			for (MouvementArticlePersistant mouvementArticlePersistant : listArticle) {
				Map<Long , BigDecimal> mapArticle = new HashMap<>();
				List<ArticleStockInfoPersistant> listArticleEtat = mouvementDao.getQuery("from ArticleStockInfoPersistant etatArt where etatArt.opc_emplacement.id=:destinationId")
						.setParameter("destinationId", mouvementBean.getOpc_emplacement().getId()).getResultList();
				for (ArticleStockInfoPersistant articleEtat : listArticleEtat) {
					BigDecimal qteStock = BigDecimalUtil.substract(articleEtat.getQte_entree(), articleEtat.getQte_sortie());
					mapArticle.put(articleEtat.getOpc_article().getId(), qteStock);
				}
				BigDecimal qteArticle = mapArticle.get(mouvementArticlePersistant.getOpc_article().getId());
				qteArticle = qteArticle == null ? BigDecimalUtil.ZERO : qteArticle;
				
				if(mouvementArticlePersistant.getQuantite() != null && mouvementArticlePersistant.getQuantite().compareTo(qteArticle) > 0){
					ArticlePersistant article = articleDao.findById(mouvementArticlePersistant.getOpc_article().getId());
					msgManque = msgManque + ("* Il manque "+BigDecimalUtil.formatNumber(BigDecimalUtil.substract(mouvementArticlePersistant.getQuantite(), qteArticle))+" éléments pour l'article "+article.getLibelle()+"<br>");
				}
			}
		}
		
		return msgManque;
	}

	@Override
	public void majQteStockArticle(Date dateRef) {
		if(dateRef != null){
			List<MouvementArticlePersistant> listDetail = mouvementDao.getQuery("from MouvementArticlePersistant "
					+ "	where opc_mouvement.date_creation>=:dtRef or opc_mouvement.date_maj>=:dtRef")
					.setParameter("dtRef", dateRef)
					.getResultList();
			majQteStockArticle(null, null, listDetail);
		}
	}
	
	@Override
	public void majPrixFichComposant(Long compId) {
		EntityManager entityManager = getEntityManager();
		
		// Lister les article ou ce composant est utilisé
		List<ArticlePersistant> listArts = getQuery("select opc_article from ArticleDetailPersistant det where det.opc_article_composant.id=:compId")
					.setParameter("compId", compId).getResultList();

		for(ArticlePersistant fiche : listArts){
			List<ArticleDetailPersistant> listComposants = fiche.getList_article();	
			BigDecimal prixActuel_ht = null;
			BigDecimal prixActuel_ttc = null;
			BigDecimal prixActuel_moy_ttc = null;
			BigDecimal prixActuel_moy_ht = null;
			ValTypeEnumPersistant tva = null;
			
			for (ArticleDetailPersistant articleDetailP : listComposants) {
				ArticlePersistant opcComposant = articleDetailP.getOpc_article_composant();
				prixActuel_ht = BigDecimalUtil.add(prixActuel_ht, BigDecimalUtil.multiply(opcComposant.getPrix_achat_ht(), articleDetailP.getQuantite()));
				prixActuel_ttc = BigDecimalUtil.add(prixActuel_ttc, BigDecimalUtil.multiply(opcComposant.getPrix_achat_ttc(), articleDetailP.getQuantite()));
				
				prixActuel_moy_ht = BigDecimalUtil.add(prixActuel_moy_ht, BigDecimalUtil.multiply(opcComposant.getPrix_achat_moyen_ht(), articleDetailP.getQuantite()));
				prixActuel_moy_ttc = BigDecimalUtil.add(prixActuel_moy_ttc, BigDecimalUtil.multiply(opcComposant.getPrix_achat_moyen_ttc(), articleDetailP.getQuantite()));
				
				tva = opcComposant.getOpc_tva_enum();
			}
			fiche.setPrix_achat_ht(prixActuel_ht);
			fiche.setPrix_achat_ttc(prixActuel_ttc);
			fiche.setPrix_achat_moyen_ttc(prixActuel_moy_ttc);
			fiche.setPrix_achat_moyen_ht(prixActuel_moy_ht);
			fiche.setOpc_tva_enum(tva);
			//
			entityManager.merge(fiche);
		}
	}
	
	@Override
	@Transactional
	public void validerAvoir(Long avoirId){
		MouvementPersistant mvmP = findById(MouvementPersistant.class, avoirId);
		mvmP.setIs_valide(true);
		
		getEntityManager().merge(mvmP);
	}
	@Override
	@Transactional
	public void validerBonCommande(Long cmdId){
		MouvementPersistant mvmP = findById(MouvementPersistant.class, cmdId);
		mvmP.setIs_valide(true);
		
		getEntityManager().merge(mvmP);
	}
	@Override
	public MouvementBean genererAchatFromCmd(Long cmdId){
		MouvementPersistant mvmP = findById(MouvementPersistant.class, cmdId);
		mvmP.setIs_valide(true);
	
		MouvementPersistant mvmAchatClone = (MouvementPersistant) ReflectUtil.cloneBean(mvmP);
		mvmAchatClone.setId(null);
		mvmAchatClone.setCode_func(null);
		mvmAchatClone.setOpc_mouvement(mvmP);
		mvmAchatClone.setType_mvmnt(TYPE_MOUVEMENT_ENUM.a.toString());
		
		List<MouvementArticlePersistant> listDetail = new ArrayList<>();
		
		for(MouvementArticlePersistant mvmDetP : mvmP.getList_article()){
			MouvementArticlePersistant mvmDetPClone = (MouvementArticlePersistant) ReflectUtil.cloneBean(mvmDetP);
			mvmDetPClone.setId(null);
			listDetail.add(mvmDetPClone);
		}
		mvmAchatClone.setList_article(listDetail);
		
		return ServiceUtil.persistantToBean(MouvementBean.class, mvmAchatClone);
	}

	@Override
	@Transactional
	public int genererBonCommandeSeuilStock(String[] emplacements) {
		if(emplacements == null){
			return 0;
		}
		String login = (ContextAppli.getUserBean() != null ? ContextAppli.getUserBean().getLogin() : "AUTO_C");
		EntityManager em = getEntityManager();
		Date currDate = new Date();
		EtablissementPersistant etsP = ContextAppli.getEtablissementBean();
		int idxMvm = 0;
		//
		Map<FournisseurPersistant, MouvementPersistant> mapData = new HashMap<>();
		for (String emplId : emplacements) {
			List<EmplacementSeuilPersistant> listEmpl = getQuery("from EmplacementSeuilPersistant "
					+ "where opc_emplacement.id=:emplId and qte_seuil is not null")
				.setParameter("emplId", Long.valueOf(emplId))
				.getResultList();
			
			for (EmplacementSeuilPersistant emplSeuilP : listEmpl) {
				ArticlePersistant opc_composant = emplSeuilP.getOpc_composant();
				BigDecimal qteRestant = (BigDecimal) getSingleResult(getQuery("select qte_reel from ArticleStockInfoPersistant "
						+ "where article_id.id=articleId"
						+ "and opc_emplacement.id=:emplacementId")
					.setParameter("articleId", opc_composant.getId())
					.setParameter("emplacementId", emplSeuilP.getOpc_emplacement().getId())
					);
				
				if(qteRestant.compareTo(emplSeuilP.getQte_seuil()) < 0){
					BigDecimal qteManquant = BigDecimalUtil.substract(emplSeuilP.getQte_seuil(), qteRestant);
					
					// Chercher le fournisseur
					List<MouvementArticlePersistant> listMvm = getQuery("from MouvementArticlePersistant "
							+ "where opc_mouvement.type_mvmnt='a' "
							+ "and opc_article.id=:articleId and opc_mouvement.opc_fournisseur.id is not null "
							+ "order by date_mouvement desc")
						.setParameter("articleId", opc_composant.getId())
						.getResultList();
					
					MouvementArticlePersistant mvmDetOri = (listMvm.size()>0 ? listMvm.get(0) : null);
					FournisseurPersistant opc_fournisseur = (mvmDetOri != null ? mvmDetOri.getOpc_mouvement().getOpc_fournisseur() : null);
					MouvementPersistant mvm = mapData.get(opc_fournisseur);
					
					if(mvm == null){
						mvm = new MouvementPersistant();
						mvm.setType_mvmnt(TYPE_MOUVEMENT_ENUM.cm.toString());
						mvm.setDate_creation(currDate);
						mvm.setNum_bl(generateNumBl(TYPE_MOUVEMENT_ENUM.cm.toString()));
						mvm.setOpc_fournisseur(opc_fournisseur);
						mvm.setDate_mouvement(currDate);
						mvm.setSignature(login);
						mvm.setList_article(new ArrayList<>());
						mvm.setOpc_destination(emplSeuilP.getOpc_emplacement());
						mvm.setOpc_etablissement(etsP);
						
						mapData.put(opc_fournisseur, mvm);
					}
					
					BigDecimal prixHt = null, prixTtc = null;
					if(mvmDetOri == null){
						prixHt = opc_composant.getPrix_achat_ht();
						prixTtc = opc_composant.getPrix_achat_ttc();
					} else{
						prixHt = mvmDetOri.getPrix_ht();
						prixTtc = mvmDetOri.getPrix_ttc();
					}
					
					//
					MouvementArticlePersistant mvmDet = new MouvementArticlePersistant();
					mvmDet.setOpc_article(opc_composant);
					mvmDet.setOpc_etablissement(etsP);
					mvmDet.setPrix_ht(prixHt);
					mvmDet.setPrix_ht_total(BigDecimalUtil.multiply(prixHt, qteManquant));
					mvmDet.setPrix_ttc(prixTtc);
					mvmDet.setPrix_ttc_total(BigDecimalUtil.multiply(prixTtc, qteManquant));
					mvmDet.setQuantite(qteManquant);
					mvmDet.setSignature(login);
					mvmDet.setDate_creation(new Date());
					//
					mvm.getList_article().add(mvmDet);
				}
			}
			
			for(FournisseurPersistant fournisseurP : mapData.keySet()){
				MouvementPersistant mvmP = mapData.get(fournisseurP);
				for(MouvementArticlePersistant det : mvmP.getList_article()){
					mvmP.setMontant_ht(BigDecimalUtil.add(mvmP.getMontant_ht(), det.getPrix_ht_total()));
					mvmP.setMontant_ttc(BigDecimalUtil.add(mvmP.getMontant_ttc(), det.getPrix_ttc_total()));
					//
					BigDecimal mttTva = BigDecimalUtil.substract(det.getPrix_ttc_total(), det.getPrix_ht_total());
					mvmP.setMontant_tva(BigDecimalUtil.add(mvmP.getMontant_tva(), mttTva));
				}
				//
				em.merge(mvmP);
				idxMvm++;
			}
		}
		
		return idxMvm;
	}

	@Override
	@Transactional
	public void archiverPdfFactureGed(ClientPersistant clientP, String numFacture, Date dateFacture, File exportPdf) {
		List<ClientPersistant> listCli = new ArrayList<>();
		listCli.add(clientP);
		//
		gedService.majArborscenceClient(listCli);
		
		List<GedPersistant> listEnfant = (List<GedPersistant>) treeService.getListeTree(GedPersistant.class, false, false);
		GedPersistant currGedCli = null;
		for (GedPersistant gedP : listEnfant) {
			if(gedP.getCode().equals(clientP.getNumero().trim())){
				currGedCli = gedP;
				break;
			}
		}
		if(currGedCli == null) {
			return;
		}
		
		String path = StrimUtil._GET_PATH("ged")+"/"+currGedCli.getId();
		
		GedFichierPersistant gedFichierP = new GedFichierPersistant();
		gedFichierP.setLibelle("Facture "+numFacture+" ["+DateUtil.dateToString(dateFacture)+"]");
		gedFichierP.setExtention("pdf");
		gedFichierP.setFile_name(exportPdf.getName());
		gedFichierP.setPath(path);
		gedFichierP.setOpc_ged(currGedCli);
		//
		gedService.mergeDetail(gedFichierP);
		
		try {
			FileUtils.copyFile(exportPdf, new File(path+"/"+exportPdf.getName()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public MouvementPersistant getMouvementByCodeBarre(String barre) {
		return (MouvementPersistant) getSingleResult(getQuery("from MouvementPersistant where code_barre=:barre")
			.setParameter("barre", barre));
	}
	@Override
	public MouvementPersistant getMouvementGroupeFacture(Long clientId, Date dateDebut, Date dateFin,
			String[] modeFinancement, String src) {
		
		String req = "from PaiementPersistant where date_mouvement>=:dateDebut and opc_client.id=:clientId";
		
		if(src.equals("cli")){
			req += " and source='VENTE'";
		} else if(src.equals("fourn")){
			req += " source='ACHAT'";
		}
		if(dateFin != null){
			req += " and date_mouvement<=:dateFin";
		}
		
		Set<Long> modes = new HashSet<>();
		if(modeFinancement != null){
			for (String mode : modeFinancement) {
				modes.add(Long.valueOf(mode));
			}
			req += " and opc_financement_enum.id in :modesPaiement";
		}
		
		Query query = getQuery(req)
			.setParameter("dateDebut", dateDebut)
			.setParameter("clientId", clientId);
		
		if(dateFin != null){
			query.setParameter("dateFin", dateFin);
		}
		if(modeFinancement != null){
			query.setParameter("modesPaiement", modes);
		}
		
		MouvementPersistant finalMvm = new MouvementPersistant();
		Map<Long, MouvementArticlePersistant> mapArt = new HashMap<>();
		List<PaiementPersistant> listPaiement = query.getResultList();
		//
		for (PaiementPersistant paiementP : listPaiement) {
			MouvementPersistant mvmP = findById(paiementP.getElementId());
			
			for(MouvementArticlePersistant artDet : mvmP.getList_article()){
				MouvementArticlePersistant finalDet = mapArt.get(artDet.getOpc_article().getId());
				if(finalDet == null){
					finalDet = new MouvementArticlePersistant();
					mapArt.put(artDet.getOpc_article().getId(), finalDet);
				}
				finalDet.setPrix_ht_total(BigDecimalUtil.add(finalDet.getPrix_ht_total(), artDet.getPrix_ht_total()));
				finalDet.setPrix_ttc_total(BigDecimalUtil.add(finalDet.getPrix_ttc_total(), artDet.getPrix_ttc_total()));
				finalDet.setPrix_vente(BigDecimalUtil.add(finalDet.getPrix_vente(), artDet.getPrix_vente()));
				finalDet.setPrix_vente_ht(BigDecimalUtil.add(finalDet.getPrix_vente_ht(), artDet.getPrix_vente_ht()));
			}
			
			finalMvm.setMontant_ttc(BigDecimalUtil.add(finalMvm.getMontant_ttc(), mvmP.getMontant_ttc()));
			finalMvm.setMontant_ht(BigDecimalUtil.add(finalMvm.getMontant_ht(), mvmP.getMontant_ht()));
			finalMvm.setMontant_tva(BigDecimalUtil.add(finalMvm.getMontant_tva(), mvmP.getMontant_tva()));
		}
		
		List<MouvementArticlePersistant> listArt = new ArrayList<>();
		for(Long artId : mapArt.keySet()){
			listArt.add(mapArt.get(artId));
		}
		finalMvm.setList_article(listArt);
		
		return finalMvm;
	}
	
	@Override
	public CaisseMouvementPersistant getMouvementGroupeFacture(Long clientId, Date dateDebut, Date dateFin,
			String[] modeFinancement) {
		String req = "from CaisseMouvementPersistant where date_vente>=:dateDebut and opc_client.id=:clientId";
		
		if(dateFin != null){
			req += " and date_vente<=:dateFin";
		}
		
		Set<String> modes = new HashSet<>();
		if(modeFinancement != null){
			for (String mode : modeFinancement) {
				modes.add(mode);
			}
			req += " and mode_paiement in (:modesPaiement)";
		}
		
		Query query = getQuery(req)
			.setParameter("dateDebut", dateDebut)
			.setParameter("clientId", clientId);
		
		if(dateFin != null){
			query.setParameter("dateFin", dateFin);
		}
		if(modeFinancement != null){
			query.setParameter("modesPaiement", modes);
		}
		
		CaisseMouvementPersistant finalMvm = new CaisseMouvementPersistant();
		Map<String, CaisseMouvementArticlePersistant> mapArt = new HashMap<>();
		List<CaisseMouvementPersistant> listCaisseMvm = query.getResultList();
		//
		for (CaisseMouvementPersistant caisseMvmP : listCaisseMvm) {
			for(CaisseMouvementArticlePersistant artDet : caisseMvmP.getList_article()){
				if(BooleanUtil.isTrue(artDet.getIs_annule())){
					continue;
				}
				if(BigDecimalUtil.isZero(artDet.getMtt_total())){
					continue;
				}
				
				CaisseMouvementArticlePersistant finalDet = mapArt.get(artDet.getLibelle());
				if(finalDet == null){
					finalDet = new CaisseMouvementArticlePersistant();
					mapArt.put(artDet.getLibelle(), finalDet);
				}
				finalDet.setMtt_total(BigDecimalUtil.add(finalDet.getMtt_total(), artDet.getMtt_total()));
				finalDet.setQuantite(BigDecimalUtil.add(finalDet.getQuantite(), artDet.getQuantite()));
			}
			
			finalMvm.setMtt_commande_net(BigDecimalUtil.add(finalMvm.getMtt_commande_net(), caisseMvmP.getMtt_commande_net()));
		}
		
		List<CaisseMouvementArticlePersistant> listArt = new ArrayList<>();
		for(String art : mapArt.keySet()){
			listArt.add(mapArt.get(art));
		}
		finalMvm.setList_article(listArt);
		
		return finalMvm;
	}
	
	private void addAnnulArtInfo(
			List<ArticleStockInfoBean> listArt,
			Map<Long, BigDecimal> mapOldArticleInfo, 
			Long emplId, 
			String sens) {
		if(emplId == null || mapOldArticleInfo == null) {
			return;
		}
		for(Long artId : mapOldArticleInfo.keySet()) {
			listArt.add(new ArticleStockInfoBean(emplId, artId, mapOldArticleInfo.get(artId), sens));
		}
	}
	private void addArtInfo(
			List<ArticleStockInfoBean> listArt,
			MouvementPersistant mouvementBean, 
			Long emplId, 
			String sens) {
		if(emplId == null || mouvementBean.getList_article() == null) {
			return;
		}
		for (MouvementArticlePersistant mvmArtP : mouvementBean.getList_article()) {
			listArt.add(new ArticleStockInfoBean(emplId, mvmArtP.getOpc_article().getId(), mvmArtP.getQuantite(), sens));
		}
	}
	
	@Transactional
	private void manageStockInfo(MouvementPersistant mouvementBean, 
			Map<Long, BigDecimal> mapOldArticleInfo,
			boolean isDelete){
		// Maj prix achat dans détail mouvement
		String typeMvm = mouvementBean.getType_mvmnt();
		List<ArticleStockInfoBean> listArt = new ArrayList<>();
		Long emplId = (mouvementBean.getOpc_emplacement() != null ? mouvementBean.getOpc_emplacement().getId() : null);
		Long destId = (mouvementBean.getOpc_destination() != null ? mouvementBean.getOpc_destination().getId() : null);
		//
		if(typeMvm.equals(ContextAppli.TYPE_MOUVEMENT_ENUM.a.toString()) || typeMvm.equals(ContextAppli.TYPE_MOUVEMENT_ENUM.rt.toString())){// Achat / Retour
			addAnnulArtInfo(listArt, mapOldArticleInfo, destId, "S");
			if(!isDelete) {
				addArtInfo(listArt, mouvementBean, destId, "E");
			}
		} else if(typeMvm.equals(ContextAppli.TYPE_MOUVEMENT_ENUM.v.toString())// Vente
				|| typeMvm.equals(ContextAppli.TYPE_MOUVEMENT_ENUM.vc.toString())// Vente caisse
				|| typeMvm.equals(ContextAppli.TYPE_MOUVEMENT_ENUM.p.toString())// Perte
				|| typeMvm.equals(ContextAppli.TYPE_MOUVEMENT_ENUM.c.toString())// Consommation
				|| typeMvm.equals(ContextAppli.TYPE_MOUVEMENT_ENUM.av.toString())){// Avoir
			addAnnulArtInfo(listArt, mapOldArticleInfo, emplId, "E");
			if(!isDelete) {
				addArtInfo(listArt, mouvementBean, emplId, "S");
			}
		} else if(typeMvm.equals(ContextAppli.TYPE_MOUVEMENT_ENUM.t.toString())
				|| typeMvm.equals(ContextAppli.TYPE_MOUVEMENT_ENUM.tr.toString())){// Transfert/Trasformation
			
			if(emplId != null) {
				addAnnulArtInfo(listArt, mapOldArticleInfo, emplId, "E");
				if(!isDelete) {
					addArtInfo(listArt, mouvementBean, emplId, "S");
				}
			}
			//
			if(mouvementBean.getOpc_destination() != null) {
				addAnnulArtInfo(listArt, mapOldArticleInfo, destId, "S");
				if(!isDelete) {
					addArtInfo(listArt, mouvementBean, destId, "E");
				}
			}
		} else {
			return;
		}
		
		//
		majQteArticleInfo(listArt);
	}
	
	@Override
	@Transactional
	public void majQteArticleCaisseInfo(CaisseMouvementPersistant currCmd, 
			boolean isDelete) {
		
		String login = (ContextAppli.getUserBean() != null ? ContextAppli.getUserBean().getLogin() : "AUTO_C");
		
		if(currCmd != null) {
			for(CaisseMouvementArticlePersistant det : currCmd.getList_article()) {
				if(BooleanUtil.isTrue(det.getIs_annule()) || det.getOpc_article() == null) {
					continue;
				}
				EmplacementPersistant opc_stock_cible = currCmd.getOpc_caisse_journee().getOpc_caisse().getOpc_stock_cible();
				if(opc_stock_cible == null) {
					continue;
				}
				
				List<ArticleStockCaisseInfoPersistant> listArt = familleService.getQuery("from ArticleStockCaisseInfoPersistant "
						+ "where opc_emplacement.id=:emplId "
						+ "and opc_article.id=:artId")
						.setParameter("emplId", opc_stock_cible.getId())
						.setParameter("artId", det.getOpc_article().getId())
						.getResultList();
				ArticleStockCaisseInfoPersistant infoP = (listArt.size() > 0 ? listArt.get(0) : null);
				if(infoP == null) {
					infoP = new ArticleStockCaisseInfoPersistant();
					infoP.setOpc_abonne(ContextAppli.getAbonneBean());
					infoP.setOpc_etablissement(ContextAppli.getEtablissementBean());
					infoP.setOpc_societe(ContextAppli.getSocieteBean());
					infoP.setDate_creation(new Date());
					infoP.setSignature(login);
					
					infoP.setOpc_article(det.getOpc_article());
					infoP.setOpc_emplacement(opc_stock_cible);
					//
					mergeEntity(infoP);
				}
				infoP.setDate_maj(new Date());
				
				if(isDelete) {
					infoP.setQte_reel(BigDecimalUtil.add(infoP.getQte_reel(), det.getQuantite()));
				} else {
					infoP.setQte_reel(BigDecimalUtil.substract(infoP.getQte_reel(), det.getQuantite()));					
				}
				mergeEntity(infoP);
			}
		}
	}
}
