package appli.model.domaine.stock.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

import appli.controller.domaine.stock.bean.ArticleBean;
import appli.controller.domaine.stock.bean.EtatStockBean;
import appli.model.domaine.stock.dao.IArticleDao;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.persistant.EmplacementPersistant;
import appli.model.domaine.stock.service.IEmplacementService;
import appli.model.domaine.stock.service.IEtatStockService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.service.GenericJpaService;

@Named
public class EtatStockService extends GenericJpaService<ArticleBean, Long> implements IEtatStockService{
	@Inject
	IArticleDao articleDao;
	@Inject
	private IEmplacementService emplacementService;
	
	@Override
	public Map<String, Object> getEtatDetail(Date dateDebutMois, List<ArticlePersistant> listArticle){
		List<EmplacementPersistant> listEmplacementRef = emplacementService.getEmplacementsInternes();
		// Inventaires -------------------------------------------------------------------------------------------
		String reqInvPrec = "select max(det.id) as maxId "
				+ " from mouvement_article det inner join mouvement mv on mv.id=det.mouvement_id "
				+ " inner join inventaire inv on inv.id=mv.inventaire_id "
				+ " where mv.inventaire_id is not null "
				+ " and inv.date_realisation<=:dateRef " 
				+ " and (inv.is_valid is not null and inv.is_valid=1) " 
				+ " and mv.emplacement_id=:emplId and det.article_id=:articleId";
		
		String reqInvActu = "select min(det.id) as maxId "
				+ " from mouvement_article det inner join mouvement mv on mv.id=det.mouvement_id "
				+ " inner join inventaire inv on inv.id=mv.inventaire_id "
				+ " where mv.inventaire_id is not null "
				+ " and inv.date_realisation>:dateRef " 
				+ " and (inv.is_valid is not null and inv.is_valid=1) " 
				+ " and mv.emplacement_id=:emplId and det.article_id=:articleId";
		
		String infoInv = "select det.quantite as qte, (det.quantite*art.prix_achat_ttc) as mtt "
				+ "from mouvement_article det "
				+ "inner join article art on art.id=det.article_id "
				+ "where det.id=:detailId";

		Map<Long, EtatStockBean> mapEmpl = new HashMap();
		HashMap<String, Object> mapDataArt = new LinkedHashMap<String, Object>();
		//
		for (ArticlePersistant art : listArticle) {
			
			EtatStockBean etaBeanArt = new EtatStockBean();
			mapEmpl.put(art.getId(), etaBeanArt);
			
			// Inventaires + calcul des dates début et fin selon les inventaires
			for(EmplacementPersistant emplP : listEmplacementRef){

				//
				EtatStockBean etatBean = new EtatStockBean();
				etatBean.setArticle(art);
				etatBean.setEmplacement(emplP);
				
				mapDataArt.put(art.getId()+"-"+emplP.getId(), etatBean);

				Object dataInvP = articleDao.getNativeQuery(reqInvPrec)
					.setParameter("dateRef", dateDebutMois)
					.setParameter("articleId", art.getId())
					.setParameter("emplId", emplP.getId())
					.getSingleResult();
			
				long detPrecId = (dataInvP!=null ? ((BigInteger)dataInvP).longValue():new Long(0)); 
				
				// Inventaire actuel
				Object dataActuP = articleDao.getNativeQuery(reqInvActu)
					.setParameter("dateRef", dateDebutMois)
					.setParameter("articleId", art.getId())
					.setParameter("emplId", emplP)
					.getSingleResult();
				
				long detActuId = (dataActuP!=null?((BigInteger)dataActuP).longValue():new Long(0));
				
				if(dataInvP != null){
					Object[] objects = (Object[])articleDao.getSingleResult(articleDao.getNativeQuery(infoInv)
							.setParameter("detailId", detPrecId));
					// Inventaire precedent -----------------------------------------------
					if(objects != null){
						etatBean.setQteInvPrec((BigDecimal) objects[0]);
						etatBean.setMttInvPrec((BigDecimal) objects[1]);
						//
						etaBeanArt.setQteInvPrec(BigDecimalUtil.add(etaBeanArt.getQteInvPrec(), (BigDecimal) objects[0]));
						etaBeanArt.setMttInvPrec(BigDecimalUtil.add(etaBeanArt.getMttInvPrec(), (BigDecimal) objects[1]));
					}
				}
				
				// inventaire actuel --------------------------------------------------
				if(dataActuP != null){
					Object[] objects = (Object[])articleDao.getSingleResult(articleDao.getNativeQuery(infoInv)
							.setParameter("detailId", detActuId));
					if(objects != null){
						etatBean.setQteInvAct((BigDecimal) objects[0]);
						etatBean.setMttInvAct((BigDecimal) objects[1]);
						//
						etaBeanArt.setQteInvAct(BigDecimalUtil.add(etaBeanArt.getQteInvAct(), (BigDecimal) objects[0]));
						etaBeanArt.setMttInvAct(BigDecimalUtil.add(etaBeanArt.getMttInvAct(), (BigDecimal) objects[1]));
					}
				}
				
				// Dans l'ordre : Transfert IN, Transfert OUT, Transformation IN, Transformation OUT, Vente caisse, Vente hors caisse, Avoir, Perte, Consommation
				Object[] data = getInfoStock(emplP.getId(), art.getId(), detPrecId);
				if(data != null){
					etatBean.setQteTransfertIn((BigDecimal) data[0]);
					etatBean.setMttTransfertIn((BigDecimal) data[1]);
					
					etatBean.setQteTransfertOut((BigDecimal) data[2]);
					etatBean.setMttTransfertOut((BigDecimal) data[3]);
					
					etatBean.setQteTransfoIn((BigDecimal) data[4]);
					etatBean.setMttTransfoIn((BigDecimal) data[5]);
					
					etatBean.setQteTransfoOut((BigDecimal) data[6]);
					etatBean.setMttTransfoOut((BigDecimal) data[7]);
					
					etatBean.setQteVenteCaisse((BigDecimal) data[8]);
					etatBean.setMttVenteCaisse((BigDecimal) data[9]);
					
					etatBean.setQteVenteHorsCaisse((BigDecimal) data[10]);
					etatBean.setMttVenteHorsCaisse((BigDecimal) data[11]);
					
					etatBean.setQteAvoirCaisse((BigDecimal) data[12]);
					etatBean.setMttAvoirCaisse((BigDecimal) data[13]);
					
					etatBean.setQtePerte((BigDecimal) data[14]);
					etatBean.setMttPerte((BigDecimal) data[15]);
					
					etatBean.setQteConsommation((BigDecimal) data[16]);
					etatBean.setMttConsommation((BigDecimal) data[17]);
					
					etatBean.setQteTotalAchat((BigDecimal) data[18]);
					etatBean.setMttTotalAchat((BigDecimal) data[19]);
					
					// --------------------------------
					etaBeanArt.setQteTransfertIn(BigDecimalUtil.add(etaBeanArt.getQteTransfertIn(), (BigDecimal) data[0]));
					etaBeanArt.setMttTransfertIn(BigDecimalUtil.add(etaBeanArt.getMttTransfertIn(), (BigDecimal) data[1]));
					
					etaBeanArt.setQteTransfertOut(BigDecimalUtil.add(etaBeanArt.getQteTransfertOut(), (BigDecimal) data[2]));
					etaBeanArt.setMttTransfertOut(BigDecimalUtil.add(etaBeanArt.getMttTransfertOut(), (BigDecimal) data[3]));
					
					etaBeanArt.setQteTransfoIn(BigDecimalUtil.add(etaBeanArt.getQteTransfoIn(), (BigDecimal) data[4]));
					etaBeanArt.setMttTransfoIn(BigDecimalUtil.add(etaBeanArt.getMttTransfoIn(), (BigDecimal) data[5]));
					
					etaBeanArt.setQteTransfoOut(BigDecimalUtil.add(etaBeanArt.getQteTransfoOut(), (BigDecimal) data[6]));
					etaBeanArt.setMttTransfoOut(BigDecimalUtil.add(etaBeanArt.getMttTransfoOut(), (BigDecimal) data[7]));
					
					etaBeanArt.setQteVenteCaisse(BigDecimalUtil.add(etaBeanArt.getQteVenteCaisse(), (BigDecimal) data[8]));
					etaBeanArt.setMttVenteCaisse(BigDecimalUtil.add(etaBeanArt.getMttVenteCaisse(), (BigDecimal) data[9]));
					
					etaBeanArt.setQteVenteHorsCaisse(BigDecimalUtil.add(etaBeanArt.getQteVenteHorsCaisse(), (BigDecimal) data[10]));
					etaBeanArt.setMttVenteHorsCaisse(BigDecimalUtil.add(etaBeanArt.getMttVenteHorsCaisse(), (BigDecimal) data[11]));
					
					etaBeanArt.setQteAvoirCaisse(BigDecimalUtil.add(etaBeanArt.getQteAvoirCaisse(), (BigDecimal) data[12]));
					etaBeanArt.setMttAvoirCaisse(BigDecimalUtil.add(etaBeanArt.getMttAvoirCaisse(), (BigDecimal) data[13]));
					
					etaBeanArt.setQtePerte(BigDecimalUtil.add(etaBeanArt.getQtePerte(), (BigDecimal) data[14]));
					etaBeanArt.setMttPerte(BigDecimalUtil.add(etaBeanArt.getMttPerte(), (BigDecimal) data[15]));
					
					etaBeanArt.setQteConsommation(BigDecimalUtil.add(etaBeanArt.getQteConsommation(), (BigDecimal) data[16]));
					etaBeanArt.setMttConsommation(BigDecimalUtil.add(etaBeanArt.getMttConsommation(), (BigDecimal) data[17]));
					
					etaBeanArt.setQteTotalAchat(BigDecimalUtil.add(etaBeanArt.getQteTotalAchat(), (BigDecimal) data[18]));
					etaBeanArt.setMttTotalAchat(BigDecimalUtil.add(etaBeanArt.getMttTotalAchat(), (BigDecimal) data[19]));
				}
				
				// Total sortie
				etatBean.setQteTotalSortie(
						BigDecimalUtil.add(	
								etatBean.getQteAvoirCaisse(), 
								etatBean.getQteConsommation(),
								etatBean.getQtePerte(), 
								etatBean.getQteVenteCaisse(),
								etatBean.getQteVenteHorsCaisse(),
								etatBean.getQteTransfertOut(),
								etatBean.getQteTransfoOut()
							)
						);
				etatBean.setMttTotalSortie(
						BigDecimalUtil.add(
								etatBean.getMttAvoirCaisse(), 
								etatBean.getMttConsommation(),
								etatBean.getMttPerte(), 
								etatBean.getMttVenteCaisse(),
								etatBean.getMttVenteHorsCaisse(),
								etatBean.getMttTransfertOut(),
								etatBean.getMttTransfoOut()
							)
						);
				etaBeanArt.setQteTotalSortie(BigDecimalUtil.add(etaBeanArt.getQteTotalSortie(), etatBean.getQteTotalSortie()));
				etaBeanArt.setMttTotalSortie(BigDecimalUtil.add(etaBeanArt.getMttTotalSortie(), etatBean.getMttTotalSortie()));
				
				// Total entrée
				etatBean.setQteTotalApprovisionnement(
						BigDecimalUtil.add(
								etatBean.getQteTotalAchat(), 
								etatBean.getQteTransfertIn(), 
								etatBean.getQteTransfoIn(), 
								etatBean.getQteInvPrec()
							)
						);
				etatBean.setMttTotalApprovisionnement(
						BigDecimalUtil.add(
								etatBean.getMttTotalAchat(), 
								etatBean.getMttTransfertIn(), 
								etatBean.getMttTransfoIn(), 
								etatBean.getMttInvPrec()
							)
					);
				etaBeanArt.setQteTotalApprovisionnement(BigDecimalUtil.add(etaBeanArt.getQteTotalApprovisionnement(), etatBean.getQteTotalApprovisionnement()));
				etaBeanArt.setMttTotalApprovisionnement(BigDecimalUtil.add(etaBeanArt.getMttTotalApprovisionnement(), etatBean.getMttTotalApprovisionnement()));
				
				// Situation
				etatBean.setQteTotalSituation(BigDecimalUtil.substract(etatBean.getQteTotalApprovisionnement(), etatBean.getQteTotalSortie()));
				etatBean.setMttTotalSituation(BigDecimalUtil.substract(etatBean.getMttTotalApprovisionnement(), etatBean.getMttTotalSortie()));
				
				etaBeanArt.setQteTotalSituation(BigDecimalUtil.add(etaBeanArt.getQteTotalSituation(), etatBean.getQteTotalSituation()));
				etaBeanArt.setMttTotalSituation(BigDecimalUtil.add(etaBeanArt.getMttTotalSituation(), etatBean.getMttTotalSituation()));
				
				if(etatBean.getQteInvAct() != null) {
					etatBean.setQteEcart(BigDecimalUtil.substract(etatBean.getQteTotalSituation(), etatBean.getQteInvAct()));
					etatBean.setMttEcart(BigDecimalUtil.substract(etatBean.getMttTotalSituation(), etatBean.getMttInvAct()));
				}
				
				etaBeanArt.setQteEcart(BigDecimalUtil.add(etaBeanArt.getQteEcart(), etatBean.getQteEcart()));
				etaBeanArt.setMttEcart(BigDecimalUtil.add(etaBeanArt.getMttEcart(), etatBean.getMttEcart()));
			} // FIN BOUCLE -----------------------
		}
		
		mapDataArt.put("map_total", mapEmpl);
		
		return mapDataArt;
	}
	
	/**
	 * @param emplId
	 * @param articleId
	 * @param detInvPrecId
	 * @return
	 */
	private Object[] getInfoStock(Long emplId, Long articleId, Long detInvPrecId){
		Query query = articleDao.getNativeQuery("select "
				// Transfert IN
				+ "sum( "
					+ "case when (mvm.type_mvmnt='t' and mvm.destination_id=:emplId) then mvmdet.quantite else 0 end "
					+ ") as qteTransfertIn, " 
				+ "sum( "
					+ "case when (mvm.type_mvmnt='t' and mvm.destination_id=:emplId) then (mvmdet.quantite*art.prix_achat_ttc) else 0 end "
				+ ") as mttTransfertIn, " 
				// Transfert OUT
				+ "sum( "
					+ "case when (mvm.type_mvmnt='t' and mvm.emplacement_id=:emplId) then mvmdet.quantite else 0 end "
					+ ") as qteTransfertOut, " 
				+ "sum( "
					+ "case when (mvm.type_mvmnt='t' and mvm.emplacement_id=:emplId) then (mvmdet.quantite*art.prix_achat_ttc) else 0 end "
				+ ") as mttTransfertOut, " 
				// Transformation IN
				+ "sum( "
					+ "case when (mvm.type_mvmnt='tr' and mvm.destination_id=:emplId) then mvmdet.quantite else 0 end "
					+ ") as qteTransfoIn, " 
				+ "sum( "
					+ "case when (mvm.type_mvmnt='tr' and mvm.destination_id=:emplId) then (mvmdet.quantite*art.prix_achat_ttc) else 0 end "
				+ ") as mttTransfoIn, "
				// Transformation OUT
				+ "sum( "
					+ "case when (mvm.type_mvmnt='tr' and mvm.emplacement_id=:emplId) then mvmdet.quantite else 0 end "
					+ ") as qteTransfoOut, " 
				+ "sum( "
					+ "case when (mvm.type_mvmnt='tr' and mvm.emplacement_id=:emplId) then (mvmdet.quantite*art.prix_achat_ttc) else 0 end "
				+ ") as mttTransfoOut, " 		
				// Vente caisse
				+ "sum( "
					+ "case when (mvm.type_mvmnt='vc' and mvm.emplacement_id=:emplId) then mvmdet.quantite else 0 end "
					+ ") as qteVenteCaisse, " 
				+ "sum( "
					+ "case when (mvm.type_mvmnt='vc' and mvm.emplacement_id=:emplId) then (mvmdet.quantite*art.prix_achat_ttc) else 0 end "
				+ ") as mttVenteCaisse, " 		
				// Vente hors caisse 
				+ "sum( "
					+ "case when (mvm.type_mvmnt='v' and mvm.emplacement_id=:emplId) then mvmdet.quantite else 0 end "
					+ ") as qteVenteHsCaisse, " 
				+ "sum( "
					+ "case when (mvm.type_mvmnt='v' and mvm.emplacement_id=:emplId) then (mvmdet.quantite*art.prix_achat_ttc) else 0 end "
				+ ") as mttVenteHsCaisse, " 		
				// Avoir
				+ "sum( "
					+ "case when (mvm.type_mvmnt='av' and mvm.emplacement_id=:emplId) then mvmdet.quantite else 0 end "
					+ ") as qteAvoir, " 
				+ "sum( "
					+ "case when (mvm.type_mvmnt='av' and mvm.emplacement_id=:emplId) then (mvmdet.quantite*art.prix_achat_ttc) else 0 end "
				+ ") as mttAvoir, " 		
				// Perte
				+ "sum( "
					+ "case when (mvm.type_mvmnt='p' and mvm.emplacement_id=:emplId) then mvmdet.quantite else 0 end "
					+ ") as qtePerte, " 
				+ "sum( "
					+ "case when (mvm.type_mvmnt='p' and mvm.emplacement_id=:emplId) then (mvmdet.quantite*art.prix_achat_ttc) else 0 end "
				+ ") as mttPerte, " 		
				// Consommation
				+ "sum( "
					+ "case when (mvm.type_mvmnt='c' and mvm.emplacement_id=:emplId) then mvmdet.quantite else 0 end "
				+ ") as qteConso, " 
					+ "sum( "
					+ "case when (mvm.type_mvmnt='c' and mvm.emplacement_id=:emplId) then (mvmdet.quantite*art.prix_achat_ttc) else 0 end "
				+ ") as mttConso, "
				// Achat
				+ "sum( "
					+ "case when (mvm.type_mvmnt='a' and mvm.destination_id=:emplId) then mvmdet.quantite else 0 end "
					+ ") as qteAchat, " 
				+ "sum( "
					+ "case when (mvm.type_mvmnt='a' and mvm.destination_id=:emplId) then (mvmdet.quantite*mvmdet.prix_ttc) else 0 end "
				+ ") as mttAchat " 
				
				+ "from mouvement_article mvmdet inner join mouvement mvm on mvmdet.mouvement_id=mvm.id "
				+ "inner join article art on art.id=mvmdet.article_id "
				//+ "left join val_type_enumere val on val.id=mvm.financement_enum "
				+ "where mvmdet.article_id=:articleId "
				+ "and (1=1 "+(detInvPrecId == null ? "":" and mvmdet.id>:detPrecId")+") "//+(detActuId == null ?" ":" and mvmdet.id<:detActuId")+") "
				+ "and (mvm.emplacement_id=:emplId or mvm.destination_id=:emplId) ");
		query.setParameter("emplId", emplId)
			.setParameter("articleId", articleId);
//			.setParameter("typeMvm", typeMvm);
		
		if(detInvPrecId != null){
			query.setParameter("detPrecId", detInvPrecId);
		}
//		if(detActuId != null){
//			query.setParameter("detActuId", detActuId);
//		}				
		return (Object[]) articleDao.getSingleResult(query);
	}
	
	@Override
	public Date[] getMinMaxDate() { 
		Object[] minMax = (Object[]) articleDao.getSingleResult(articleDao.getQuery("select min(date_mouvement) as min_date, max(date_mouvement) as max_date from MouvementPersistant"));
		
		Date dateDebut = (Date) minMax[0];
		Date dateFin = (Date) minMax[1];
		
		return (minMax == null)	 ? new Date[2] : new Date[] {dateDebut, dateFin};
	}
}
