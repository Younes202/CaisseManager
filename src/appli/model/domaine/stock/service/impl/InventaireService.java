package appli.model.domaine.stock.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.stock.bean.InventaireBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_MOUVEMENT_ENUM;
import appli.model.domaine.stock.dao.IInventaireDao;
import appli.model.domaine.stock.dao.IMouvementDao;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.persistant.ArticleStockInfoPersistant;
import appli.model.domaine.stock.persistant.InventaireDetailPersistant;
import appli.model.domaine.stock.persistant.InventairePersistant;
import appli.model.domaine.stock.persistant.MouvementArticlePersistant;
import appli.model.domaine.stock.persistant.MouvementPersistant;
import appli.model.domaine.stock.service.IInventaireService;
import appli.model.domaine.stock.validator.InventaireValidator;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.common.annotation.validator.WorkModelMethodValidator;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.StrimUtil;
import framework.model.service.GenericJpaService;

@WorkModelClassValidator(validator=InventaireValidator.class)
@Named
public class InventaireService extends GenericJpaService<InventaireBean, Long> implements IInventaireService{
	@Inject
	private IInventaireDao inventaireDao;
	@Inject
	private IMouvementDao mouvementDao;
	
	@Override
	public List<ArticleStockInfoPersistant> getArticleInventaireByEmplacementAndFamille(Long emplacementId, Set<Long> familleIdsAll, List<Long> articleIds, Set<Long> articleIdsExclude) {
		return inventaireDao.getArticleInventaireByEmplacementAndFamille(emplacementId, familleIdsAll, articleIds, articleIdsExclude);
	}
	
	@Override
	public List<ArticlePersistant> getArticleByFamille(Set<Long> familleIdsAll, Set<Long> articleIdsInclude, Set<Long> articleIdsExclude, boolean excludeDisable) {
		String req = "from ArticlePersistant article where "+(ContextAppli.IS_RESTAU_ENV() ? "article.is_stock = true " : "1=1 ");
		
		if(articleIdsInclude != null && articleIdsInclude.size()>0){
			req = req + "and article.id in (:artIds) ";
		}
		if(articleIdsExclude != null && articleIdsExclude.size()>0){
			req = req + "and article.id not in (:idsExclude) ";
		}
		if(familleIdsAll != null && familleIdsAll.size()>0){
			req = req + "and opc_famille_stock.id in (:familles) ";
		}
		
		if(excludeDisable){
			req = req + "and (article.is_disable is null or article.is_disable=0) ";
		}
		req = req + "order by article.opc_famille_stock.b_left, article.code, article.libelle";
		
		Query query = getQuery(req);
		
		
		if(articleIdsInclude != null && articleIdsInclude.size()>0){
			query.setParameter("artIds", articleIdsInclude);
		}
		if(articleIdsExclude != null && articleIdsExclude.size()>0){
			query.setParameter("idsExclude", articleIdsExclude);
		}
		if(familleIdsAll != null && familleIdsAll.size()>0){
			query.setParameter("familles", familleIdsAll);
		}
		
		List<ArticlePersistant> list = query.getResultList();
		
		return list;
	}

	@Override
	@Transactional
	public void mergeInventaire(InventaireBean inventaireBean, boolean isFromCuisine) {
		List<InventaireDetailPersistant> listInventaireDet = inventaireBean.getList_detail();
		
		for (InventaireDetailPersistant inventaireDetailIHM : listInventaireDet) {
			BigDecimal qteEcart = BigDecimalUtil.substract(inventaireDetailIHM.getQte_reel(), inventaireDetailIHM.getQte_theorique());
			BigDecimal qteTheorique = inventaireDetailIHM.getQte_theorique();
			
			inventaireDetailIHM.setQte_ecart(qteEcart);
			
			qteTheorique = qteTheorique.compareTo(BigDecimalUtil.get(0))==0 ? BigDecimalUtil.get(100) : (qteTheorique.compareTo(BigDecimalUtil.ZERO)<0?qteTheorique.negate():qteTheorique);
			inventaireDetailIHM.setPourcent_ecart(BigDecimalUtil.divide(BigDecimalUtil.multiply(qteEcart, BigDecimalUtil.get(100)), qteTheorique));
		}
		
		if(inventaireBean.getId() == null){
			create(inventaireBean);
			MessageService.addGrowlMessage(MSG_TYPE.SUCCES, StrimUtil.label("work.update.title"), StrimUtil.label("work.create.succes"));
		} else{
			update(inventaireBean);
			MessageService.addGrowlMessage(MSG_TYPE.SUCCES, StrimUtil.label("work.update.title"), StrimUtil.label("work.update.succes"));
		}
	}

	@Override
	@Transactional
	@WorkModelMethodValidator
	public MouvementPersistant validerInventaire(Long inventaireId) {
		if(inventaireId == null){
			return null;
		}
		
		InventairePersistant inventaireP = findById(InventairePersistant.class, inventaireId);
		inventaireP.setIs_valid(true);
		mergeEntity(inventaireP);
		
		List<InventaireDetailPersistant> listDet = getQuery("from InventaireDetailPersistant where inventaire_id=:invId")
					.setParameter("invId", inventaireId)
					.getResultList();
		
		//List<InventaireDetailPersistant> listDet = inventaireP.getList_detail();
		EntityManager em = getEntityManager();
		//
		for (InventaireDetailPersistant inventaireDetailP : listDet) {
			inventaireDetailP.setQte_theorique(inventaireDetailP.getQte_reel());
			inventaireDetailP.setQte_ecart(null);
			inventaireDetailP.setPourcent_ecart(null);
			//
			em.merge(inventaireDetailP);
		}
		
		// Si mouvement existe alors juste maj (cas correction
		MouvementPersistant mouvementP = (MouvementPersistant) getSingleResult(getQuery("from MouvementPersistant where opc_inventaire.id=:invId "
				+ "and type_mvmnt=:TypeMvmnt")
			.setParameter("invId", inventaireId)
			.setParameter("TypeMvmnt", TYPE_MOUVEMENT_ENUM.i.toString()));
		
		boolean isCorrection = (mouvementP==null?false:true);;
		List<MouvementArticlePersistant> list_article = null;
		// créer un mouvement
		if(!isCorrection){
			mouvementP = new MouvementPersistant();
			list_article = new ArrayList<>();
		} else{
			list_article = mouvementP.getList_article();
		}
		mouvementP.setCommentaire(inventaireP.getCommentaire());
		mouvementP.setDate_mouvement(inventaireP.getDate_realisation());
		mouvementP.setType_mvmnt(TYPE_MOUVEMENT_ENUM.i.toString());
		mouvementP.setOpc_emplacement(inventaireP.getOpc_emplacement());
		mouvementP.setOpc_inventaire(inventaireP);
		
		// Détail
		List<MouvementArticlePersistant> listArticleTemp = new ArrayList<>();
		for(InventaireDetailPersistant detail : listDet){
			MouvementArticlePersistant mvmArticle = null;
			
			//
			if(isCorrection){
				for(MouvementArticlePersistant artDetP : list_article){
					if(artDetP.getOpc_article().getId() == detail.getOpc_article().getId()){
						mvmArticle = artDetP;
						break;
					}
				}
			}
			if(mvmArticle == null){
				mvmArticle = new MouvementArticlePersistant();
			}
			mvmArticle.setOpc_article(detail.getOpc_article());
			mvmArticle.setQuantite(detail.getQte_reel());
			//
			listArticleTemp.add(mvmArticle);
		}
		list_article.clear();
		list_article.addAll(listArticleTemp);
		//
		mouvementP.setList_article(list_article);
		
		if(isCorrection){
			mouvementDao.update(mouvementP);	
		} else{
			mouvementDao.create(mouvementP);
		}
		
		em.flush();
		
		return findById(MouvementPersistant.class, mouvementP.getId());
	}

	@Override
	@Transactional
	@WorkModelMethodValidator
	public MouvementPersistant annulerValidation(Long inventaireId) {
		InventaireBean inventaireBean = findById(inventaireId);
		MouvementPersistant mouvementPersistant = (MouvementPersistant) getSingleResult(mouvementDao.getQuery("from MouvementPersistant mvm where mvm.opc_inventaire.id = :inventaireId")
												.setParameter("inventaireId", inventaireId));
		if(mouvementPersistant != null){
			mouvementDao.delete(mouvementPersistant);
		}
		inventaireBean.setIs_valid(false);
		
		inventaireDao.update(inventaireBean);
		
		return mouvementPersistant;
	}

	@Override
	public Date getMaxDateInventaireValide(Long emplacement) { 
		Date maxDate = (Date) getSingleResult(
					mouvementDao.getQuery("select max (date_realisation) from InventairePersistant "
							+ "where opc_emplacement.id=:emplId "
							+ "and is_valid=1").setParameter("emplId", emplacement));
		
		return maxDate;
	}
	
	@Override
	public Date getMinDateInventaireNonValide(Long emplacementId) { 
		Date minDate = (Date) getSingleResult(mouvementDao.getQuery("select min (date_realisation) from InventairePersistant "
				+ "where (is_valid is null or is_valid=0) "
				+ "and opc_emplacement.id=:emplId")
				.setParameter("emplId", emplacementId));
		
		return minDate;
	}
	
	@Override
	public List<InventairePersistant> getInventaireNonValide(Long emplacementId) { 
		return mouvementDao.getQuery("from InventairePersistant where (is_valid is null or is_valid=0) and "
				+ "opc_emplacement.id=:emplacementId")
					.setParameter("emplacementId", emplacementId)
					.getResultList();
	}
	@Override
	public List<InventaireDetailPersistant> getInventaireEcart(Long invId) {
		return mouvementDao.getQuery("from InventaireDetailPersistant where opc_inventaire.id=:invId and qte_ecart!=0 order by famille_bleft")
				.setParameter("invId", invId)
				.getResultList();
	}
	
	@Override
	public List<InventairePersistant> getListInventaireByDate(Date dateDebut, Date dateFin){
		return mouvementDao.getQuery("from InventairePersistant "
				+ "where date_realisation>=:dtRefDebut and date_realisation<=:dtRefFin ")
					.setParameter("dtRefDebut", dateDebut)
					.setParameter("dtRefFin", dateFin)
					.getResultList();
	}

}
