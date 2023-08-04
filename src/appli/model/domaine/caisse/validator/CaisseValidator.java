package appli.model.domaine.caisse.validator;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.caisse.bean.CaisseBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_CAISSE_ENUM;
import appli.model.domaine.caisse.dao.ICaisseDao;
import appli.model.domaine.caisse.service.ICaisseService;
import appli.model.domaine.caisse.service.IJourneeService;
import appli.model.domaine.vente.persistant.CaisseJourneePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import appli.model.domaine.vente.persistant.CaissePersistant;
import appli.model.domaine.vente.persistant.JourneePersistant;
import framework.controller.ContextGloabalAppli;
import framework.model.common.service.MessageService;
import framework.model.common.util.StringUtil;

@Named
public class CaisseValidator {
	@Inject
	private ICaisseDao caisseDao;
	@Inject
	private IJourneeService journeeService;
	@Inject
	private ICaisseService caisseService;
	
	/*
	 * 
	 */
	public void updateCreateValidator(CaisseBean caisseBean) {
		if(caisseDao.isNotUnique(caisseBean, "adresse_mac", "type_ecran")){
			MessageService.addFieldMessage("caisse.adresse_mac", "Cette IP est déjà utilisée");
		}
		if(caisseDao.isNotUnique(caisseBean, "reference")){
			MessageService.addFieldMessage("caisse.reference", "Ce référence existe déjà");
		}
		
		if(TYPE_CAISSE_ENUM.AFFICHEUR.toString().equals(caisseBean.getType_ecran())) {
			if(caisseBean.getOpc_caisse() == null || StringUtil.isEmpty(caisseBean.getOpc_caisse().getId())){
				MessageService.addFieldMessage("caisse.opc_caisse.id", "Le champs est obligatoire");
			}
		} else if(!TYPE_CAISSE_ENUM.PRESENTOIRE.toString().equals(caisseBean.getType_ecran())
				&& !TYPE_CAISSE_ENUM.AFFICLIENT.toString().equals(caisseBean.getType_ecran())
				&& !TYPE_CAISSE_ENUM.PILOTAGE.toString().equals(caisseBean.getType_ecran())
				&& !TYPE_CAISSE_ENUM.BALANCE.toString().equals(caisseBean.getType_ecran())
				) {
			if(caisseBean.getOpc_stock_cible() == null || StringUtil.isEmpty(caisseBean.getOpc_stock_cible().getId())){
				MessageService.addFieldMessage("caisse.opc_stock_cible.id", "Le champs est obligatoire");
			}
		}
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void update(CaisseBean caisseBean) {
		updateCreateValidator(caisseBean);
		
		// Si changement du type de terminal
		CaissePersistant caissePDb = caisseDao.findById(caisseBean.getId());
		if(!caissePDb.getType_ecran().equals(caisseBean.getType_ecran())){
			checkTerminaux(caisseBean);			
		}
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void create(CaisseBean caisseBean){
		updateCreateValidator(caisseBean);
		//
		checkTerminaux(caisseBean);
	}
	
	private void checkTerminaux(CaissePersistant caisseBean){
		Integer nbrMaxTerminaux = null;
		int nbrCurrent = 0;
		
		String request = "from CaissePersistant where type_ecran=:type and (is_desactive is null or is_desactive=0)";
		// AFFICHEUR, AFFICLIENT, CAISSE, PRESENTOIRE, CAISSE_CLIENT, CUISINE, PILOTAGE
		if(caisseBean.getType_ecran().equals("CAISSE")){
			nbrMaxTerminaux = ContextAppli.getAbonementBean().getNbrCaisse();
//			nbrMaxTerminaux = ContextAppli.getAbonementBean().getNbrSatCaisseMob();
		} else if(caisseBean.getType_ecran().equals("CAISSE_CLIENT")){
			nbrMaxTerminaux = ContextAppli.getAbonementBean().getNbrSatCaisseAuto();
		} 
		
		else if(caisseBean.getType_ecran().equals("AFFICHEUR")){
			nbrMaxTerminaux = ContextAppli.getAbonementBean().getNbrSatAffCaisse();
		} else if(caisseBean.getType_ecran().equals("AFFICLIENT")){
			nbrMaxTerminaux = ContextAppli.getAbonementBean().getNbrSatAffClient();
		} 
		
		else if(caisseBean.getType_ecran().equals("CUISINE")){
			nbrMaxTerminaux = ContextAppli.getAbonementBean().getNbrSatCuisine();
		} else if(caisseBean.getType_ecran().equals("PILOTAGE")){
			nbrMaxTerminaux = ContextAppli.getAbonementBean().getNbrSatPilotage();
		} else if(caisseBean.getType_ecran().equals("PRESENTOIRE")){
			nbrMaxTerminaux = ContextAppli.getAbonementBean().getNbrSatPresentoire();
		}
		
		if(nbrMaxTerminaux == null || nbrMaxTerminaux == 0){
			nbrMaxTerminaux = 99;
		}
		
		nbrCurrent = caisseDao.getQuery(request)
				.setParameter("type", caisseBean.getType_ecran())
				.getResultList()
				.size();
		
		if(nbrCurrent >= nbrMaxTerminaux){
			MessageService.addBannerMessage("Vous avez dépassé le nombre de terminaux autorisés ("+nbrMaxTerminaux+"). Veuillez désactiver un autre "
					+ "terminal du même type ou contacter le support");
		}
	}
	
	/**
	 * @param id
	 */
	public void delete(Long id){
		CaissePersistant caissePersistant = caisseDao.findById(id);
		
		if(caissePersistant.getList_caisse_journee().size() > 0){
			MessageService.addBannerMessage("Cette caisse est liée à des journées.");
		}
	}

	/**
	 * @param caisseId
	 */
	public void activerDesactiverCaisse(Long caisseId) {
		CaissePersistant caisseP = caisseDao.findById(caisseId);
		if(caisseP.getType_ecran().equals(ContextAppli.TYPE_CAISSE_ENUM.CAISSE.toString()) 
					&& !ContextAppli.STATUT_JOURNEE.CLOTURE.getStatut().equals(caisseP.getStatutCaisse())){
			MessageService.addBannerMessage("La caisse doit être clôturée avant de la désactiver.");
		}
	}

	/**
	 * @param caisseJourneeId
	 * @param mtt_cloture_caissier
	 */
	public void cloturerDefinitive(CaisseJourneePersistant caisseJourneeP, boolean isRecalcul, BigDecimal mtt_clotureEspeces,
			BigDecimal mtt_clotureCb, BigDecimal mtt_clotureChq, BigDecimal mtt_clotureDej, boolean isRectif, boolean isPassassion) {
		
		if(isRecalcul) {
			return;
		}
		
		CaissePersistant caissePersistant = caisseService.findById(CaissePersistant.class, caisseJourneeP.getOpc_caisse().getId());
		if(!isPassassion && !isRectif && !ContextAppli.STATUT_JOURNEE.OUVERTE.getStatut().equals(caissePersistant .getStatutCaisse())){
			MessageService.addBannerMessage("La caisse doit être ouverte pour la clôturer.");
			return;
		}
		Long casseId = caissePersistant.getId();
		CaisseJourneePersistant caisseJ = caisseService.getJourneCaisseOuverte(casseId);
//		Long journeeId = null;
//		if(caisseJ != null){
//			journeeId = caisseJ.getOpc_journee().getId();
//		} else{
//			JourneePersistant journeeP = journeeService.getLastJournee();
//			if(journeeP != null){
//				journeeId = journeeP.getId();
//			}
//		}
		
		// Vérifier si des mouvements non validés existent
		List<CaisseMouvementPersistant> listCmdEnAttente = caisseService.getListCmdTemp(caissePersistant.getId());
		if(listCmdEnAttente.size() > 0){
			MessageService.addBannerMessage(listCmdEnAttente.size()+" commandes caisse non validées ont été trouvées. Veuillez les valider ou les supprimer.");
		}
		
		List<CaisseMouvementPersistant> listCmdNonPaye =  caisseService.getListCmdNonPaye(caissePersistant.getId());
		if(listCmdNonPaye.size() > 0){
			MessageService.addBannerMessage(listCmdNonPaye.size()+" commandes caisse non encaissées ont été trouvées. Veuillez les encaisser ou les supprimer.");
		}
		
		// Vérifier si des livraisons sans livreur existent -------------------------------------------------------
		if(caisseJ != null){
			boolean isLivreurOblige = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("LIVREUR_REQUIRED"));
	    	boolean isClientOblige = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CLIENT_REQUIRED"));
	    	 
	    	if(isLivreurOblige){
				List<CaisseMouvementPersistant> listCmdSansLivreur = caisseDao.getQuery("from CaisseMouvementPersistant"
						+ " where type_commande=:typeCmd "
						+ " and opc_livreurU.id is null and last_statut != :statutAnnul"
						+ " and opc_caisse_journee.id=:caisseJId")
						.setParameter("typeCmd", ContextAppli.TYPE_COMMANDE.L.toString())
						.setParameter("statutAnnul", ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.ANNUL.toString())
						.setParameter("caisseJId", caisseJ.getId())
						.getResultList();
				
				if(listCmdSansLivreur.size() > 0){
					MessageService.addBannerMessage(listCmdSansLivreur.size()+" commandes livraison sans livreur. Veuillez associer un livreur à chacune de ces commandes.");
					return;
				}
	    	}
			
			// Vérifier si des livraisons sans client exitent
	    	if(isClientOblige){
				List<CaisseMouvementPersistant> listCmdSansClient = caisseDao.getQuery("from CaisseMouvementPersistant where type_commande=:typeCmd "
						+ " and last_statut != :statutAnnul "
						+ " and opc_client.id is null and last_statut != :statutAnnul "
						+ "and opc_caisse_journee.id=:caisseJId")
						.setParameter("typeCmd", ContextAppli.TYPE_COMMANDE.L.toString())
						.setParameter("statutAnnul", ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.ANNUL.toString())
						.setParameter("caisseJId", caisseJ.getId())
						.getResultList();
				
				if(listCmdSansClient.size() > 0){
					MessageService.addBannerMessage(listCmdSansClient.size()+" commandes livraison sans client. Veuillez associer un client à chacune de ces commandes.");
					return;
				}
	    	}
		}
	}
	
	public void gererPassasionShift(Long userOuvertureId, CaisseJourneePersistant caisseJourneeP, BigDecimal mtt_ouverture, boolean isRectif){
		if(caisseJourneeP != null){
			CaissePersistant caissePersistant = caisseService.findById(CaissePersistant.class, caisseJourneeP.getOpc_caisse().getId());
			if(!isRectif && !ContextAppli.STATUT_JOURNEE.OUVERTE.getStatut().equals(caissePersistant .getStatutCaisse())){
				MessageService.addBannerMessage("La caisse doit être ouverte pour la clôturer.");
				return;
			}
			
			boolean isLivreurOblige = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("LIVREUR_REQUIRED"));
	    	boolean isClientOblige = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CLIENT_REQUIRED"));
	    	 
	    	if(isLivreurOblige){
				List<CaisseMouvementPersistant> listCmdSansLivreur = caisseDao.getQuery("from CaisseMouvementPersistant"
						+ " where type_commande=:typeCmd "
						+ " and opc_livreurU.id is null and last_statut != :statutAnnul"
						+ " and opc_caisse_journee.id=:caisseJId")
						.setParameter("typeCmd", ContextAppli.TYPE_COMMANDE.L.toString())
						.setParameter("statutAnnul", ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.ANNUL.toString())
						.setParameter("caisseJId", caisseJourneeP.getId())
						.getResultList();
				
				if(listCmdSansLivreur.size() > 0){
					MessageService.addBannerMessage(listCmdSansLivreur.size()+" commandes livraison sans livreur. Veuillez associer un livreur à chacune de ces commandes.");
					return;
				}
	    	}
			
			// Vérifier si des livraisons sans client exitent
	    	if(isClientOblige){
				List<CaisseMouvementPersistant> listCmdSansClient = caisseDao.getQuery("from CaisseMouvementPersistant where type_commande=:typeCmd "
						+ " and last_statut != :statutAnnul "
						+ " and opc_client.id is null and last_statut != :statutAnnul "
						+ "and opc_caisse_journee.id=:caisseJId")
						.setParameter("typeCmd", ContextAppli.TYPE_COMMANDE.L.toString())
						.setParameter("statutAnnul", ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.ANNUL.toString())
						.setParameter("caisseJId", caisseJourneeP.getId())
						.getResultList();
				
				if(listCmdSansClient.size() > 0){
					MessageService.addBannerMessage(listCmdSansClient.size()+" commandes livraison sans client. Veuillez associer un client à chacune de ces commandes.");
					return;
				}
	    	}
		}
	}
	
	/**
	 * @param caisseId
	 */
	public void ouvrirCaisse(Long caisseId, BigDecimal mtt_ouverture) {
		JourneePersistant lastJournee = journeeService.getLastJournee();
		
		if(lastJournee == null){
			MessageService.addBannerMessage("Aucune journée n'a été trouvée.");
		} else if(!lastJournee.getStatut_journee().equals(ContextAppli.STATUT_JOURNEE.OUVERTE.getStatut())){
			MessageService.addBannerMessage("La journée en cours n'est pas ouverte.");	
		}
	}
}
