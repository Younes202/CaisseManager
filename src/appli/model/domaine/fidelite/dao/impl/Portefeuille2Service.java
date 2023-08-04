package appli.model.domaine.fidelite.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.stock.bean.ArticleBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_ECRITURE;
import appli.model.domaine.administration.persistant.EcriturePersistant;
import appli.model.domaine.fidelite.dao.IPortefeuille2Service;
import appli.model.domaine.fidelite.persistant.CarteFideliteClientPersistant;
import appli.model.domaine.fidelite.service.ICarteFideliteClientService;
import appli.model.domaine.personnel.persistant.ClientPersistant;
import appli.model.domaine.personnel.persistant.ClientPortefeuilleMvmPersistant;
import appli.model.domaine.personnel.persistant.SocieteLivrPersistant;
import appli.model.domaine.stock.service.IArticleService;
import appli.model.domaine.util_srv.printCom.ticket.PrintTicketUtil;
import appli.model.domaine.vente.persistant.CaisseJourneePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import framework.controller.ContextGloabalAppli;
import framework.model.beanContext.CompteBancairePersistant;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.StringUtil;
import framework.model.service.GenericJpaService;
import framework.model.util.printGen.PrintPosBean;

@Named
public class Portefeuille2Service extends GenericJpaService<ArticleBean, Long> implements IPortefeuille2Service{
	@Inject
	private IArticleService articleSrv;
	@Inject
	private ICarteFideliteClientService carteClientService;
	
	@Override
	@Transactional
	public PrintPosBean ajouterRecharge(Long elementId, BigDecimal montant, String modePaiement, boolean isFromCaisse, String tp) {
		ClientPortefeuilleMvmPersistant pmvm = new ClientPortefeuilleMvmPersistant();
		CaisseMouvementPersistant newCmd = new CaisseMouvementPersistant();
		CaisseMouvementArticlePersistant cmaP = new CaisseMouvementArticlePersistant();
		
		CarteFideliteClientPersistant opc_carte_client = null;
		BigDecimal tauxPortefeuille = null;
		if(tp.equals("CLI")){
			ClientPersistant clientP = articleSrv.findById(ClientPersistant.class, elementId);
			tauxPortefeuille = clientP.getTaux_portefeuille();
			pmvm.setOpc_client(clientP);
			newCmd.setOpc_client(clientP);
			cmaP.setLibelle("Recharge portefeuille "+clientP.getNom()+" "+StringUtil.getValueOrEmpty(clientP.getPrenom()));
			
			opc_carte_client = carteClientService.getCarteClientActive(clientP.getId());
		} else if(tp.equals("SOC")){
			SocieteLivrPersistant socLivrP = articleSrv.findById(SocieteLivrPersistant.class, elementId);
			tauxPortefeuille = socLivrP.getTaux_portefeuille();
			pmvm.setOpc_societeLivr(socLivrP);
			newCmd.setOpc_societe_livr(socLivrP);
			cmaP.setLibelle("Recharge portefeuille "+socLivrP.getNom());
		}
		
		if(!BigDecimalUtil.isZero(tauxPortefeuille)){
			BigDecimal mttTaux = BigDecimalUtil.divide(BigDecimalUtil.multiply(montant, tauxPortefeuille), BigDecimalUtil.get(100));
			montant = BigDecimalUtil.add(montant, mttTaux);
		}
		//
		pmvm.setDate_mvm(new Date());
		pmvm.setMontant(montant);
		pmvm.setSens("C");
		pmvm.setMode_paie(modePaiement);
		//
		EntityManager entityManager = articleSrv.getEntityManager();
		entityManager.merge(pmvm);
		
		// Maj solde
//		portefeuilleCliP.setSolde(portefeuille2Service.getSoldePortefeuilleMvm(opc_carte_client.getOpc_client().getId()));
////		portefeuilleCliP.setSolde(BigDecimalUtil.add(portefeuilleCliP.getSolde(), montant));
//		portefeuilleCliP = entityManager.merge(portefeuilleCliP);
		
		// Ecriture
		if(!isFromCaisse){
			ajouterEcritureCompte(pmvm);
		} else{
	        newCmd.setRef_commande(""+System.currentTimeMillis());
	        newCmd.setDate_vente(new Date());
	        newCmd.setOpc_caisse_journee((CaisseJourneePersistant) MessageService.getGlobalMap().get("CURRENT_JOURNEE_CAISSE"));
	        newCmd.setDate_creation(new Date());
	        newCmd.setMode_paiement(modePaiement);
	        newCmd.setMtt_commande(montant);
	        newCmd.setMtt_commande_net(montant);
	        newCmd.setMtt_donne(montant);
	        newCmd.setLast_statut(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE.toString());
	        newCmd.setOpc_etablissement(ContextAppli.getEtablissementBean());
	        newCmd.setType_commande(ContextAppli.TYPE_COMMANDE.P.toString());
	        //
	        newCmd.setList_article(new ArrayList<>());
			newCmd.setMax_idx_client(1);
			
			cmaP.setType_ligne(ContextAppli.TYPE_LIGNE_COMMANDE.RECHARGE_PF.toString());
			cmaP.setMtt_total(montant);
			cmaP.setQuantite(BigDecimalUtil.get(1));
			cmaP.setCode(ContextAppli.TYPE_LIGNE_COMMANDE.RECHARGE_PF.toString());
			cmaP.setElementId(elementId);
			newCmd.getList_article().add(cmaP);
			
			newCmd = entityManager.merge(newCmd);
			//
			PrintTicketUtil pu = new PrintTicketUtil(newCmd, opc_carte_client);
            
			return pu.getPrintPosBean();
		}
		return null;
	}
	
	@Override
	@Transactional
	public void majSoldePortefeuilleMvm(Long elementId, String tp){
		BigDecimal solde = getSoldePortefeuilleMvm(elementId, tp);
		if(tp.equals("CLI")){
			ClientPersistant clientP = articleSrv.findById(ClientPersistant.class, elementId);
			clientP.setSolde_portefeuille(solde);
			articleSrv.getEntityManager().merge(clientP);
		} else if(tp.equals("SOC")){
			SocieteLivrPersistant socLivrP = articleSrv.findById(SocieteLivrPersistant.class, elementId);
			socLivrP.setSolde_portefeuille(solde);
			articleSrv.getEntityManager().merge(socLivrP);
		}
		
	}
	
	@Override
	public BigDecimal getSoldePortefeuilleMvm(Long clientId, String tp){
		BigDecimal[] data = getSoldePortefeuilleDetail(clientId, tp);
		return BigDecimalUtil.substract(data[0], data[1]);
	}
	@Override
	public BigDecimal[] getSoldePortefeuilleDetail(Long clientId, String tp){
		List<CaisseMouvementPersistant> listMvm = getListMouvementPortefeuille(clientId, tp);
		List<CaisseMouvementPersistant> listMvmRecharge = getListMouvementRechargePortefeuille(clientId, tp);
		
		int cpt = 0;
		BigDecimal soldeMvm = null;
		for (CaisseMouvementPersistant caisseMouvementP : listMvm) {
			if(BooleanUtil.isTrue(caisseMouvementP.getIs_annule())){
				continue;
			}
			// On prend uniquement montant paiement portefeuille
			soldeMvm = BigDecimalUtil.add(soldeMvm, caisseMouvementP.getMtt_portefeuille()); //caisseMouvementP.getMtt_commande_net());
			cpt++;
		}
		BigDecimal soldeRecharge = null;
		for (CaisseMouvementPersistant caisseMouvementP : listMvmRecharge) {
			if(BooleanUtil.isTrue(caisseMouvementP.getIs_annule())){
				continue;
			}
			soldeRecharge = BigDecimalUtil.add(soldeRecharge, caisseMouvementP.getMtt_commande_net());
		}
		
		return new BigDecimal[]{soldeRecharge, soldeMvm, BigDecimalUtil.get(cpt)};
	}
	
//	@Override
//	public void majSoldePortefeuilleClient(CaisseMouvementPersistant currentCommande) {
//		if(currentCommande.getOpc_client() == null || BigDecimalUtil.isZero(currentCommande.getMtt_portefeuille())){
//			return;
//		}
//		 ClientPortefeuilleMvmPersistant portefeuilleP = new ClientPortefeuilleMvmPersistant();
//		 // Ajout d'une trace
//		 portefeuilleP.setDate_mvm(new Date());
//		 portefeuilleP.setMontant(currentCommande.getMtt_portefeuille());
//		 portefeuilleP.setOpc_mvn_caisse(currentCommande);
//		 
//		 portefeuilleP.setOpc_client(currentCommande.getOpc_client());
//		 portefeuilleP.setOpc_societeLivr(currentCommande.getOpc_societe_livr());
//		 
//		 portefeuilleP.setSens("D");
//		 portefeuilleP.setMode_paie("ESPECES");// Non util dans cas débit mais obligatoire dans base !!
//		 // Sauvegarde
//		 EntityManager entityManager = articleSrv.getEntityManager();
//		 entityManager.merge(portefeuilleP);
//		 
//		 // Maj points et montants client carte
//		 solde maj
//	}
	
	
    /**
     * 
     * @param modePaiement
     * @return 
     */
	private CompteBancairePersistant getCompteBancaire(String modePaiement){
       if(modePaiement == null){
           return null;
       }
       Long compteId = null;
       // Conf du compte
       if(modePaiement.equals(ContextAppli.MODE_PAIEMENT.CARTE.toString())){
           String compte = ContextGloabalAppli.getGlobalConfig(ContextAppli.PARAM_APPLI_ENUM.COMPTE_BANCAIRE_CARTE.toString());
           if(StringUtil.isNotEmpty(compte)){
               compteId = Long.valueOf(compte);
           }
       } else if(modePaiement.equals(ContextAppli.MODE_PAIEMENT.CHEQUE.toString())){
           String compte = ContextGloabalAppli.getGlobalConfig(ContextAppli.PARAM_APPLI_ENUM.COMPTE_BANCAIRE_CHEQUE.toString());
           if(StringUtil.isNotEmpty(compte)){
               compteId = Long.valueOf(compte);
           }
       } else{
           String compteEsp = ContextGloabalAppli.getGlobalConfig(ContextAppli.PARAM_APPLI_ENUM.COMPTE_BANCAIRE_ESP_CAISSE.toString());
    	   if(StringUtil.isEmpty(compteEsp)) {
    		   compteEsp = ContextGloabalAppli.getGlobalConfig(ContextAppli.PARAM_APPLI_ENUM.COMPTE_BANCAIRE_CAISSE.toString());
    	   }
           if(StringUtil.isNotEmpty(compteEsp)){
               compteId = Long.valueOf(compteEsp);
           }
       }
       
       return compteId != null ? (CompteBancairePersistant)articleSrv.findById(CompteBancairePersistant.class, compteId) : null;
   } 
	
	
	/**
	 * @param mouvementBean
	 * @return
	 */
	@Transactional
	private void ajouterEcritureCompte(ClientPortefeuilleMvmPersistant pmvm){
		// Supprimer l'ancienne écriture
		supprimerEcritureCompte(pmvm);
		
		CompteBancairePersistant opcCompte = getCompteBancaire(pmvm.getMode_paie());
		if(opcCompte == null || BigDecimalUtil.isZero(pmvm.getMontant())){
			return;
		}
		
		// Ajouter
		EcriturePersistant ecritureP = new EcriturePersistant();
		ecritureP.setDate_mouvement(pmvm.getDate_mvm());
		ecritureP.setElementId(pmvm.getId());
		
		if(pmvm.getOpc_client() != null){
			ClientPersistant opc_client = pmvm.getOpc_client();
			ecritureP.setLibelle("Recharge portefeuille client : "+opc_client.getNom()+" "+StringUtil.getValueOrEmpty(opc_client.getPrenom()));
		} else if(pmvm.getOpc_societeLivr() != null){
			SocieteLivrPersistant opc_soc = pmvm.getOpc_societeLivr();
			ecritureP.setLibelle("Recharge portefeuille client : "+opc_soc.getNom());
		}
		
		ecritureP.setMontant(pmvm.getMontant());
		ecritureP.setOpc_banque(opcCompte);
		ecritureP.setSource(TYPE_ECRITURE.PORTEFEUILLE.toString());
		ecritureP.setSens("C");
		
		articleSrv.getEntityManager().merge(ecritureP);
	}
	
	@Transactional
	private void supprimerEcritureCompte(ClientPortefeuilleMvmPersistant pmvm){
		articleSrv.getQuery("delete from EcriturePersistant where elementId=:elementId "
				+ "and source=:source")
			.setParameter("elementId", pmvm.getId())
			.setParameter("source", "PORTEFEUILLE")
			.executeUpdate();
	}
	
	@Override
	@Transactional
	public void deleteRecharge(Long porteFeuilleMvmId) {
		ClientPortefeuilleMvmPersistant rechargeP = articleSrv.findById(ClientPortefeuilleMvmPersistant.class, porteFeuilleMvmId);
//		ClientPortefeuillePersistant opc_portefeuille = rechargeP.getOpc_portefeuille();
		// Maj mouvement associé
		if(rechargeP.getOpc_mvn_caisse() != null){
			rechargeP.getOpc_mvn_caisse().setIs_annule(true);
		}
		rechargeP.setIs_annule(true);
		EntityManager entityManager = articleSrv.getEntityManager();
		entityManager.merge(rechargeP);
		
		// Maj solde
		if(rechargeP.getOpc_client() != null){
			rechargeP.getOpc_client().setSolde_portefeuille(getSoldePortefeuilleMvm(rechargeP.getOpc_client().getId(), "CLI"));
			entityManager.merge(rechargeP.getOpc_client());
		} else if(rechargeP.getOpc_societeLivr() != null){
			rechargeP.getOpc_societeLivr().setSolde_portefeuille(getSoldePortefeuilleMvm(rechargeP.getOpc_client().getId(), "SOC"));
			entityManager.merge(rechargeP.getOpc_societeLivr());
		}
	}
	
	@Override
	public List<CaisseMouvementPersistant> getListMouvementPortefeuille(Long elementId, String tp){
		String req = "from CaisseMouvementPersistant where mtt_portefeuille != 0 and ";
		
		if(tp.equals("CLI")){
			req += "opc_client.id=:elementId";
		} else if(tp.equals("SOC")){
			req += "opc_societe_livr.id=:elementId";
		}
		
		req += " and (is_annule is null or is_annule=0) "
				+ "order by id desc";
		
		
		List<CaisseMouvementPersistant> listMvm = articleSrv.getQuery(req)
			.setParameter("elementId", elementId)
			.getResultList();
		
		return listMvm;
	}
	@Override
	public List<CaisseMouvementPersistant> getListMouvementRechargePortefeuille(Long elementId, String tp){
		String req = "select opc_mouvement_caisse from CaisseMouvementArticlePersistant caiMvm "
				+ "where caiMvm.type_ligne=:typeLigne and ";
		
		if(tp.equals("CLI")){
			req += "caiMvm.opc_mouvement_caisse.opc_client.id=:elementId ";
		} else if(tp.equals("SOC")){
			req += "caiMvm.opc_mouvement_caisse.opc_societe_livr.id=:elementId";
		}
		req += " and (caiMvm.opc_mouvement_caisse.is_annule is null or caiMvm.opc_mouvement_caisse.is_annule=0) "
				+ "and (caiMvm.is_annule is null or caiMvm.is_annule=0) "
				+ "order by caiMvm.opc_mouvement_caisse.id desc";
		return articleSrv.getQuery(req)
			.setParameter("elementId", elementId)
			.setParameter("typeLigne", ContextAppli.TYPE_LIGNE_COMMANDE.RECHARGE_PF.toString())
			.getResultList();
	}
}