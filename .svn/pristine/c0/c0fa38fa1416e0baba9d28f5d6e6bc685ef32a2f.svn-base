package appli.model.domaine.administration.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.administration.bean.CompteBancaireBean;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_ECRITURE;
import appli.model.domaine.administration.dao.ICompteBancaireDao;
import appli.model.domaine.administration.persistant.EcriturePersistant;
import appli.model.domaine.administration.service.ICompteBancaireService;
import appli.model.domaine.administration.validator.CompteBancaireValidator;
import appli.model.domaine.compta.persistant.PaiementPersistant;
import appli.model.domaine.personnel.persistant.ClientPersistant;
import appli.model.domaine.stock.persistant.FournisseurChequePersistant;
import appli.model.domaine.stock.persistant.FournisseurPersistant;
import appli.model.domaine.stock.service.IFournisseurChequeService;
import framework.model.beanContext.CompteBancairePersistant;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.BooleanUtil;
import framework.model.service.GenericJpaService;

@WorkModelClassValidator(validator=CompteBancaireValidator.class)
@Named
public class CompteBancaireService extends GenericJpaService<CompteBancaireBean, Long> implements ICompteBancaireService{
	@Inject
	private ICompteBancaireDao compteBancaireDao;
	@Inject
	private IFournisseurChequeService fournisseurChequeService; 
	
	@Override
	public BigDecimal getSoldeCompte(Long compteId){
		// Solde cloture
		List<EcriturePersistant> listSolde = getQuery("from EcriturePersistant "
				+ "where source='CLOTURE_ETAT' "
				+ "and opc_banque.id=:banqueId "
				+ "order by date_mouvement desc")
			.setParameter("banqueId", compteId)
			.getResultList();
		
		Long lastSoldeReportId = (listSolde.size()>0) ? listSolde.get(0).getId() : -1;
		
		String request = "select SUM(case when cp.sens='D' then -cp.montant "
				+ "	else cp.montant end) "
				+ " from EcriturePersistant cp where cp.opc_banque.id=:banque_id "
				+ " and id>=:lastSoldeReportId"+getEtsCondition("cp", true);
			
			return  (BigDecimal) getQuery(request)
					.setParameter("banque_id", compteId)
					.setParameter("lastSoldeReportId", lastSoldeReportId)
					.getSingleResult();
	}
	
	@Override
	public BigDecimal getSoldeCompte(Long compteId, Date dateFin) {
		// Solde cloture
		List<EcriturePersistant> listSolde = getQuery("from EcriturePersistant "
				+ "where source='CLOTURE_ETAT' and date_mouvement<=:maxDate "
				+ "and opc_banque.id=:banqueId "
				+ "order by date_mouvement desc")
			.setParameter("maxDate", dateFin)
			.setParameter("banqueId", compteId)
			.getResultList();
		
		Long lastSoldeReportId = (listSolde.size()>0) ? listSolde.get(0).getId() : -1;
		
		String request = "select SUM(case when cp.sens='D' then -cp.montant "
				+ "	else cp.montant end) "
				+ " from EcriturePersistant cp where cp.opc_banque.id=:banque_id "
				+ "and cp.date_mouvement<=:date_mouvement and cp.id>=:lastSoldeReportId"+getEtsCondition("cp", true);
		
		return  (BigDecimal) getQuery(request)
				.setParameter("banque_id", compteId)
				.setParameter("date_mouvement", dateFin)
				.setParameter("lastSoldeReportId", lastSoldeReportId)
				.getSingleResult();
	}

	@Override
	public BigDecimal getSoldeReportCompte(Long compteId, Date dateDebut) {
		// Solde cloture
		List<EcriturePersistant> listSolde = getQuery("from EcriturePersistant "
				+ "where source='CLOTURE_ETAT' and date_mouvement<=:maxDate "
				+ "and opc_banque.id=:banqueId "
				+ "order by date_mouvement desc")
			.setParameter("maxDate", dateDebut)
			.setParameter("banqueId", compteId)
			.getResultList();
		
		EcriturePersistant lastSoldeReportId = (listSolde.size()>0) ? listSolde.get(0) : null;
		
		return lastSoldeReportId==null ? BigDecimalUtil.ZERO : lastSoldeReportId.getMontant();
		
//		String request = "select SUM(case when cp.sens='D' then -cp.montant "
//				+ "	else cp.montant end) "
//				+ " from EcriturePersistant cp where cp.opc_banque.id=:banque_id "
//				+ "and id>=:lastSoldeReportId";
//		
//		return  (BigDecimal) getQuery(request)
//				.setParameter("banque_id", compteId)
//				.setParameter("date_mouvement", maxDate)
//				.setParameter("lastSoldeReportId", lastSoldeReportId)
//				.getSingleResult();
	}
	
	/**
	 * Utilisé depuis le tableux dans "/domaine/administration/compteEcriture_list.jsp"
	 * @return
	 */
//	@Override
//	public BigDecimal getTotalJourneeEcriture(Date dateEcriture){
//		BigDecimal result =  (BigDecimal) getSingleResult(
//					getQuery("select SUM(case when sens='D' then -montant "
//				+ "	else montant end) from EcriturePersistant where DATE(date_mouvement)>=:dtMvm and DATE(date_mouvement)<=:dtMvm")
//					.setParameter("dtMvm", dateEcriture)
//				);
//		
//		return result;
//	}
//	@Override
//	public BigDecimal getTotalJourneeEcritureSource(Date dateEcriture, String source){
//		BigDecimal result =  (BigDecimal) getSingleResult(
//					getQuery("select SUM(case when sens='D' then -montant "
//				+ "	else montant end) from EcriturePersistant where DATE(date_mouvement)>=:dtMvm and DATE(date_mouvement)<=:dtMvm "
//				+ "and source=:source")
//					.setParameter("dtMvm", dateEcriture)
//					.setParameter("source", source)
//				);
//		
//		return result;
//	}

	/**
	 * Utilisé depuis le tableux dans "/domaine/administration/compteEcriture_list.jsp"
	 * @return
	 */
	@Override
	public BigDecimal getTotalBanqueCaisse(Long banqueId, Date dateEcriture){
		BigDecimal result =  (BigDecimal) getSingleResult(
				getQuery("select SUM(case when sens='D' then -montant "
				+ "	else montant end) from EcriturePersistant "
				+ "where opc_banque.id=:banqueId and DATE(date_mouvement)>=:dtMvm and DATE(date_mouvement)<=:dtMvm"+getEtsCondition("", true)
				).setParameter("banqueId", banqueId)
				.setParameter("dtMvm", dateEcriture)
				);
		return result;
	}
	
	@Override
	@Transactional
	public void activerDesactiverElement(Long compteBancaireId) {
		CompteBancairePersistant comptePersistant = compteBancaireDao.findById(compteBancaireId);
		comptePersistant.setIs_disable(BooleanUtil.isTrue(comptePersistant.getIs_disable()) ? false : true);
		//
		getEntityManager().merge(comptePersistant);
	}
	@Override
	@Transactional
	public void mergePaiements(TYPE_ECRITURE sourceEcr, 
			List<PaiementPersistant> listPaiement, 
			FournisseurPersistant fournisseur,
			Long elementId, 
			String libelle, 
			String sens,
			Date dateMouvement) {
		mergePaiements(sourceEcr, listPaiement, fournisseur, null, elementId, libelle, sens, dateMouvement, false);
	}
	@Override
	@Transactional
	public void mergePaiements(TYPE_ECRITURE sourceEcr, 
			List<PaiementPersistant> listPaiement, 
			FournisseurPersistant fournisseur,
			ClientPersistant client,
			Long elementId, 
			String libelle, 
			String sens,
			Date dateMouvement, boolean isComptabilise) {
		EntityManager em = getEntityManager();
		//
		deletePaiements(elementId, sourceEcr);
		em.flush();
		//
		if(listPaiement != null){
			for (PaiementPersistant paiementPersistant : listPaiement) {
				if(BigDecimalUtil.isZero(paiementPersistant.getMontant())) {
					continue;
				}
				
				FournisseurChequePersistant cfP = null;
				if(paiementPersistant.getOpc_fournisseurCheque() != null){
					cfP = (FournisseurChequePersistant)fournisseurChequeService.findById(FournisseurChequePersistant.class, paiementPersistant.getOpc_fournisseurCheque().getId());
					paiementPersistant.setNum_cheque(cfP.getNum_cheque());
				}
				paiementPersistant.setId(null);
				paiementPersistant.setSource(sourceEcr.toString());
				paiementPersistant.setElementId(elementId);
				paiementPersistant.setLibelle(libelle);
				paiementPersistant.setOpc_fournisseur(fournisseur);
				paiementPersistant.setOpc_client(client);
				paiementPersistant.setDate_mouvement(dateMouvement);
				paiementPersistant.setSens(sens);
				
				if(paiementPersistant.getDate_echeance() == null && paiementPersistant.getDate_encaissement() == null) {
					paiementPersistant.setDate_encaissement(new Date());
				}
				
				//
				paiementPersistant = em.merge(paiementPersistant);
				
				// annuler ancien cheque fournisseur
				FournisseurChequePersistant oldcf = fournisseurChequeService.getChequeBySource(sourceEcr, elementId);
				if(oldcf != null){
					oldcf.setSource(null);
					oldcf.setElementId(null);
					oldcf.setMontant(null);
					
					em.merge(oldcf);
				}
				
				boolean isChequeF = cfP != null;
				// Maj cheque fournisseur -------------------------------------
				if(isChequeF){
					cfP.setSource(sourceEcr.toString());
					cfP.setElementId(elementId);
					cfP.setMontant(paiementPersistant.getMontant());
					//
					em.merge(cfP);
				}
				//--------------------------------------------------------------
				
				// Gérer l'écriture comptable
				if(libelle != null && sens != null && (paiementPersistant.getDate_echeance() == null || paiementPersistant.getDate_echeance().compareTo(new Date())<=0)){
					ajouterEcritureCompte(paiementPersistant, libelle, sens);
				}
			}
		}
	}
	
	/**
	 * @param mouvementBean
	 * @return
	 */
	@Transactional
	private void ajouterEcritureCompte(PaiementPersistant paiement, String libelle, String sens){
		// Supprimer l'ancienne écriture
		supprimerEcritureCompte(paiement);
		
		// Ajouter
		if(paiement.getOpc_compte_bancaire() != null && !BigDecimalUtil.isZero(paiement.getMontant())){
			EcriturePersistant ecritureP = new EcriturePersistant();
			ecritureP.setDate_mouvement(paiement.getDate_mouvement());
			ecritureP.setElementId(paiement.getElementId());
			ecritureP.setLibelle(libelle);
			ecritureP.setMontant(paiement.getMontant());
			ecritureP.setOpc_banque(paiement.getOpc_compte_bancaire());
			ecritureP.setSource(paiement.getSource());
			ecritureP.setSens(sens);
			ecritureP.setGroupe(1);
			//
			getEntityManager().merge(ecritureP);
		}
	}
	
	@Transactional
	public void supprimerEcritureCompte(PaiementPersistant paiement){
		getQuery("delete from EcriturePersistant where elementId=:elementId "
				+ "and source=:source")
			.setParameter("elementId", paiement.getElementId())
			.setParameter("source", paiement.getSource())
			.executeUpdate();
	}
	
	@Override
	@Transactional
	public void deletePaiements(Long elementId,  TYPE_ECRITURE sourceEcr) {
		List<PaiementPersistant> listPaiement = getQuery("from PaiementPersistant where elementId=:elementId and source=:source")
				.setParameter("elementId", elementId)
				.setParameter("source", sourceEcr.toString())
				.getResultList();
		
		// Numéro de chèque si chéque fournisseur
		for(PaiementPersistant paiement : listPaiement){
			if(paiement.getOpc_fournisseur() != null){
				if("CHEQUE_F".equals(paiement.getOpc_financement_enum().getCode()) || "CHEQUE".equals(paiement.getOpc_financement_enum().getCode())){
					FournisseurChequePersistant fcP = fournisseurChequeService.getChequeFournisseur(paiement.getOpc_fournisseur().getId(), paiement.getNum_cheque());
					if(fcP != null) {
						fcP.setSource(null);
						fcP.setElementId(null);
						fcP.setMontant(null);
						//
						getEntityManager().merge(fcP);
					}
				}
			}
		}
		
		getQuery("delete from EcriturePersistant where elementId=:elementId "
				+ "and source=:source")
			.setParameter("elementId", elementId)
			.setParameter("source", sourceEcr.toString())
			.executeUpdate();
		getQuery("delete PaiementPersistant where elementId=:elementId and source=:source")
			.setParameter("elementId", elementId)
			.setParameter("source", sourceEcr.toString())
			.executeUpdate();
	}
	
	@Override
	@Transactional
	public void pointerCheque(Long elementId, Date datePointage) {
		PaiementPersistant paiementP = (PaiementPersistant) findById(PaiementPersistant.class, elementId);
		
		boolean isNotPointage = paiementP.getDate_encaissement() != null;// Si dans base est déjà pointé alors annulation
		if(isNotPointage){
			paiementP.setDate_encaissement(null);
		} else{
			paiementP.setDate_encaissement(datePointage);	
		}
		getEntityManager().merge(paiementP);
		
		// Marquer chèque fournisseur
		if(paiementP.getOpc_fournisseurCheque() != null){
			FournisseurChequePersistant cf = (FournisseurChequePersistant) fournisseurChequeService.findById(FournisseurChequePersistant.class, paiementP.getOpc_fournisseurCheque().getId());
			if(isNotPointage){
				cf.setDate_encaissement(null);
			} else{
				cf.setDate_encaissement(datePointage);	
			}
			getEntityManager().merge(cf);
		}
	}

	@Override
	@Transactional
	public void pointerPaiementEcheance(Long elementId, Date dateEncaissement) {
		PaiementPersistant paiementP = (PaiementPersistant) findById(PaiementPersistant.class, elementId);
		paiementP.setDate_encaissement(dateEncaissement);	
		
		getEntityManager().merge(paiementP);
		
		// Supprimer l'ancienne écriture
		supprimerEcritureCompte(paiementP);
		
		// Ajouter
		if(paiementP.getOpc_compte_bancaire() != null && !BigDecimalUtil.isZero(paiementP.getMontant())){
			EcriturePersistant ecritureP = new EcriturePersistant();
			ecritureP.setDate_mouvement(paiementP.getDate_encaissement());
			ecritureP.setElementId(paiementP.getElementId());
			ecritureP.setLibelle(paiementP.getLibelle());
			ecritureP.setMontant(paiementP.getMontant());
			ecritureP.setOpc_banque(paiementP.getOpc_compte_bancaire());
			ecritureP.setSource(paiementP.getSource());
			ecritureP.setSens(paiementP.getSens());
			//
			getEntityManager().merge(ecritureP);
		}		
	}
	
	@Override
	public List<CompteBancairePersistant> getListCompteDepot() {
		return getQuery("from CompteBancairePersistant where type_compte='CDEP'").getResultList();
	}
}
