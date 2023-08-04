package appli.model.domaine.caisse_restau.service.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.caisse.ContextAppliCaisse;
import appli.controller.domaine.caisse_restau.action.CuisineJourneeBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.administration.persistant.UserPersistant;
import appli.model.domaine.caisse_restau.dao.ICuisineJourneeDao;
import appli.model.domaine.caisse_restau.persistant.CuisineJourneePersistant;
import appli.model.domaine.caisse_restau.service.ICuisineJourneeService;
import appli.model.domaine.stock.dao.IInventaireDao;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.persistant.InventairePersistant;
import appli.model.domaine.vente.persistant.CaissePersistant;
import appli.model.domaine.vente.persistant.JourneePersistant;
import framework.model.service.GenericJpaService;

@Named
public class CuisineJourneeService extends GenericJpaService<CuisineJourneeBean, Long> implements ICuisineJourneeService{
	@Inject
	private ICuisineJourneeDao cuisineJourneeDao;
	@Inject
	private IInventaireDao inventaireDao;
	
	@Override
	public int getNbrInventaire(Long JourneeId) {
		if(JourneeId == null) {
			return 0;
		}
		return getQuery("from CuisineJourneePersistant "
				+ " where opc_user_ouverture.id=:user_id and opc_cuisine.id=:cuisine_id and opc_journee.id=:journee_id")
				.setParameter("user_id",ContextAppli.getUserBean().getId())
				.setParameter("cuisine_id",ContextAppliCaisse.getCaisseBean().getId())
			    .setParameter("journee_id", JourneeId)
			    .getResultList().size();
	}
	
	public CuisineJourneePersistant getLastCuisineJournee(Long cuisineId) {  
		List<CuisineJourneePersistant> listCuisineJournee = getQuery("from CuisineJourneePersistant journee where opc_cuisine.id=:cuisineId order by date_ouverture desc")
																			 .setParameter("cuisineId", cuisineId)
																			 .getResultList();
		return (listCuisineJournee.size() > 0 ? listCuisineJournee.get(0) : null);
	}
	
	@Override
	@Transactional
	public void setCuisineJourneeInventaire(Long inventaireId) {
		JourneePersistant journee = ContextAppliCaisse.getJourneeBean();
		CuisineJourneePersistant cuisineJournee = new CuisineJourneePersistant();
		
		UserPersistant user = ContextAppli.getUserBean();
		CaissePersistant cuisine = ContextAppliCaisse.getCaisseBean();
		InventairePersistant inventaire = inventaireDao.findById(inventaireId);
		Date date = new Date();
		CuisineJourneePersistant lastCuisineJournee = getLastCuisineJournee(cuisine.getId());
		if(lastCuisineJournee == null || lastCuisineJournee.getOpc_inventaire_ouverture() == null) {
			cuisineJournee.setDate_ouverture(date);
			cuisineJournee.setOpc_user_ouverture(user); 
			cuisineJournee.setOpc_inventaire_ouverture(inventaire);
			cuisineJournee.setIs_Manuel(true);
			if(getNbrInventaire(journee.getId()) == 1) {
				lastCuisineJournee.setOpc_inventaire_cloture(inventaire);
				lastCuisineJournee.setDate_cloture(date);
				lastCuisineJournee.setIs_Manuel(false);
				lastCuisineJournee.setOpc_user_cloture(user);
				
				getEntityManager().merge(lastCuisineJournee);
			}
		}else {
			cuisineJournee.setOpc_user_cloture(user);
			cuisineJournee.setOpc_inventaire_cloture(inventaire);
			cuisineJournee.setIs_Manuel(true);
			cuisineJournee.setDate_cloture(date);
		}
		cuisineJournee.setOpc_cuisine(cuisine);
		cuisineJournee.setOpc_cuisine(ContextAppliCaisse.getCaisseBean());
		cuisineJournee.setOpc_journee(journee);
		
		cuisineJourneeDao.create(cuisineJournee);
	} 
	    
	    @Override
		public List<ArticlePersistant> getListArticlePresentoire(boolean isActifOnly) {
			String req = "from ArticlePersistant art "
					+ "where art.destination='P' ";
			if(isActifOnly){
				req = req + " and (art.is_disable is null or art.is_disable=0) ";
			}
			req = req + "order by opc_famille_cuisine.code, opc_famille_cuisine.libelle, art.code, art.libelle ";
			
			return getQuery(req)
					.getResultList();
		}
		
		@Override
		public List<ArticlePersistant> getListArticleCuisine(boolean isActifOnly) {
			String req = "from ArticlePersistant art "
					+ "where art.destination='C' ";
			if(isActifOnly){
				req = req + " and (art.is_disable is null or art.is_disable=0) ";
			}
			req = req + "order by opc_famille_cuisine.code, opc_famille_cuisine.libelle, art.code, art.libelle ";
			return getQuery(req)
					.getResultList();
		}

}

