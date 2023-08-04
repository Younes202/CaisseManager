/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appli.model.domaine.caisse.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import appli.controller.domaine.administration.bean.JourneeBean;
import appli.model.domaine.stock.service.impl.RepartitionBean;
import appli.model.domaine.vente.persistant.CaisseJourneePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementTracePersistant;
import appli.model.domaine.vente.persistant.JourneePersistant;
import framework.model.service.IGenericJpaService;

/**
 *
 * @author hp
 */ 
public interface IJourneeService extends IGenericJpaService<JourneeBean, Long> {
    
	public JourneePersistant getLastJournee();

	public void ouvrirJournee(JourneeBean viewBean); 

	public JourneePersistant getJourneeView(Long journeeId);

	public void updateBasicInfos(Long mvmCaisseId, String type_cmd, String mode_paiement, Long clientId, Long livreurId);

	public Date[] getMinMaxDate();

	void setDataJourneeFromView(JourneePersistant journeeBean);

	public void reOuvrirJournee(JourneeBean viewBean);

	Map<String, Map<String, RepartitionBean>> getChiffresServeurLivreurCaissier(Date dateDebut, Date dateFin, Long journeeId);

	JourneePersistant getLastJourneeClose();

	JourneePersistant getJourneeByDate(Date journee);

	void majDateJournee(Long journeeId, Date date_journee);

	void cloturerJournee(Long journeeId, boolean isRecloture, BigDecimal soldeCoffre, boolean isFromCaisse);

	JourneePersistant getJourneeOrNextByDate(Date dateJournee);

	JourneePersistant getJourneeOrLastByDate(Date dateJournee);

	public List<CaisseMouvementTracePersistant> getMouvementAnnomalie(Long journeeId);

	Map<String, Object> getRepartitionVenteStock(Long journeeId, Date dateDebut, Date dateFin);

	public void ajouterEcrituresJournee(JourneePersistant opc_journee);

	public Map<Long, CaisseMouvementPersistant> getMapMvmStockCaisse(Long id);

	public List<JourneePersistant> getListournee(Date debut, Date fin);

	List<CaisseJourneePersistant> getJourneeCaisseView(Long journeeId);

	Map getRepartitionVenteArticleRestau(JourneePersistant journeeIdStart, JourneePersistant journeeIdEnd, Long familleIncludeId,
			Long caisseFilterId, boolean isFromRaz);

	Map getRepartitionVenteArticle(JourneePersistant journeeIdStart, JourneePersistant journeeIdEnd, Long familleIncludeId, boolean isFromRaz);

	Map<String, Map> getRepartitionVenteArticleParPosteCuisine(JourneePersistant journeeStartId, JourneePersistant journeeEndId,
			Long familleIncludeId);

	JourneePersistant getJourneeOrPreviousByDate(Date dateJournee);

	BigDecimal getMontantRechargePortefeuille(Long journeeId);

	String recalculChiffresMvmJournee(Long journeeId);
}
