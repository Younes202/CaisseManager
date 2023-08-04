package appli.model.domaine.caisse.validator;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

import appli.controller.domaine.administration.bean.JourneeBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.administration.persistant.CaisseVenteView;
import appli.model.domaine.administration.service.IEtatFinanceService;
import appli.model.domaine.caisse.dao.IJourneeDao;
import appli.model.domaine.vente.persistant.CaisseJourneePersistant;
import appli.model.domaine.vente.persistant.JourneePersistant;
import framework.controller.ContextGloabalAppli;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.StringUtil;

@Named
public class JourneeValidator {
	@Inject
	private IJourneeDao journeeDao;
	@Inject
	private IEtatFinanceService etatFinancierService;
	
	/*
	 * 
	 */
	public void updateCreateValidator(JourneeBean journeeBean) {
		if(journeeDao.isNotUnique(journeeBean, "date_journee")){
			MessageService.addFieldMessage("journee.date_journee", "Cette date est déjà utilisée.");
		}
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void update(JourneeBean journeeBean) {
		updateCreateValidator(journeeBean);
		
		MessageService.addBannerMessage("On ne peut pas modifier une journée de caisse.");
	}
	
	/**
	 * @param id
	 */
	public void delete(Long id){
		JourneePersistant journeePersistant = journeeDao.findById(id);
		if(journeePersistant.getList_caisse_journee().size() > 0){
			MessageService.addBannerMessage("Cette caisse est liée à des shifts caisses.");
		}
	}
	
	/**
	 * @param journeeBean
	 */
	public void ouvrirJournee(JourneeBean journeeBean) {
		updateCreateValidator(journeeBean);
		//
		Query query = journeeDao.getQuery("select max(journee.date_journee) from JourneePersistant journee");
		Date dateJourneeMax = (Date) journeeDao.getSingleResult(query);
		if(dateJourneeMax != null && journeeBean.getDate_journee().compareTo(dateJourneeMax) <= 0){
			MessageService.addBannerMessage("La date de la journée doit être supérieure à la date de la dernière journée clôturée ("+DateUtil.dateToString(dateJourneeMax)+")");
		}
		JourneePersistant journee = (JourneePersistant) journeeDao.getSingleResult(journeeDao.getQuery("from JourneePersistant where statut_journee='"+ContextAppli.STATUT_JOURNEE.OUVERTE.getStatut()+"'"));
		// une seule journée ouverte
		if(journee != null){
			MessageService.addBannerMessage("Une journée ouverte existe déjà en date du ("+DateUtil.dateToString(journee.getDate_journee())+")");
		}
	}
	
	public void reOuvrirJournee(JourneeBean journeeBean) {
		List listJournee = journeeDao.getQuery("from JourneePersistant where statut_journee=:statut and id!=:currId")
			.setParameter("statut", "O")
			.setParameter("currId", journeeBean.getId())
			.getResultList();
		
		if(listJournee.size() > 0) {
			MessageService.addBannerMessage("Une autre journée ouverte ("+DateUtil.dateToString(journeeBean.getDate_journee())+") a été trouvée. Veuillez la clore avant de continuer.");
			return;
		}
		// Mois clos ---------------------
		if(etatFinancierService.isMoisClos(journeeBean.getDate_journee())) {
			MessageService.addBannerMessage("On ne peut pas ré-ouvrir cette journée car elle appartient à un mois clos.");
		}
	}
	
	/**
	 * @param journeeBean
	 */
	public void majDateJournee(Long journeeId, Date date_journee) {
		JourneePersistant journeeP = (JourneePersistant) journeeDao.getSingleResult(journeeDao.getQuery("from JourneePersistant where date_journee=:dateJournee and id!=:currId")
				.setParameter("dateJournee", date_journee)
				.setParameter("currId", journeeId));
		
		if(journeeP != null) {
			MessageService.addFieldMessage("journee.date_journee", "Cette date existe déjà");
		}
	}
			
	/**
	 * @param journeeBean
	 */
	public void cloturerJournee(Long journeeId, boolean isRecloture, BigDecimal soldeCoffre, boolean isFromCaisse) {
		JourneePersistant journeeP = journeeDao.findById(journeeId);
		List<CaisseJourneePersistant> listCaisseJournee = journeeP.getList_caisse_journee(); 
		
		//
		if(!isRecloture) {
			boolean isDblCloture = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("DOUBLE_CLOTURE"));
			boolean isDblClotureObligatoire = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("DOUBLE_CLOTURE_REQ"));
			//
			for (CaisseJourneePersistant caisseJourneePersistant : listCaisseJournee) {
				if(caisseJourneePersistant.getOpc_caisse().getType_ecran().equals(ContextAppli.TYPE_CAISSE_ENUM.CAISSE.toString()) 
						&& !caisseJourneePersistant.getStatut_caisse().equals(ContextAppli.STATUT_JOURNEE.CLOTURE.getStatut())){
					
					// Ne pas contôler les caisses sans chiffre
					CaisseVenteView caisseJourneeView = (CaisseVenteView) journeeDao.getSingleResult(journeeDao.getQuery("from CaisseVenteView where caisse_journee_id=:caisseJourneeId")
							.setParameter("caisseJourneeId", caisseJourneePersistant.getId()));
					if(caisseJourneeView == null || BigDecimalUtil.isZero(caisseJourneeView.getMtt_total())){
						continue;
					}
					
					if(isFromCaisse) {
						MessageService.addDialogMessage("La caisse *"+caisseJourneePersistant.getOpc_caisse().getReference()+"* n'est pas clôturée.");
					} else {
						MessageService.addBannerMessage("La caisse *"+caisseJourneePersistant.getOpc_caisse().getReference()+"* n'est pas clôturée.");
					}
					break;
				}
				// Vérifier la dublke clôture
				if(isDblCloture && isDblClotureObligatoire){
					if(caisseJourneePersistant.getOpc_caisse().getType_ecran().equals(ContextAppli.TYPE_CAISSE_ENUM.CAISSE.toString())  
							&& caisseJourneePersistant.getMtt_cloture_old_espece() == null){
						if(isFromCaisse) {
							MessageService.addDialogMessage("La caisse *"+caisseJourneePersistant.getOpc_caisse().getReference()+"* n'est pas clôturée par le manager.");
						} else {
							MessageService.addBannerMessage("La caisse *"+caisseJourneePersistant.getOpc_caisse().getReference()+"* n'est pas clôturée par le manager.");
						}
						break;
					}
				}
			}
		}
	}
}
