package appli.model.domaine.administration.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import appli.controller.domaine.administration.bean.CompteBancaireBean;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_ECRITURE;
import appli.model.domaine.compta.persistant.PaiementPersistant;
import appli.model.domaine.personnel.persistant.ClientPersistant;
import appli.model.domaine.stock.persistant.FournisseurPersistant;
import framework.model.beanContext.CompteBancairePersistant;
import framework.model.service.IGenericJpaService;

public interface ICompteBancaireService extends IGenericJpaService<CompteBancaireBean, Long> {
	BigDecimal getSoldeCompte(Long compteId);
	BigDecimal getSoldeCompte(Long compteId, Date dateFin);
	BigDecimal getSoldeReportCompte(Long compteId, Date dateDebut);
	BigDecimal getTotalBanqueCaisse(Long banqueId, Date dateEcriture);
	void activerDesactiverElement(Long compteBancaireId);
	void deletePaiements(Long elementId, TYPE_ECRITURE sourceEcr);
	void mergePaiements(
			TYPE_ECRITURE sourceEcr, 
			List<PaiementPersistant> listPaiement,
			FournisseurPersistant fournisseur, 
			ClientPersistant client, 
			Long elementId, 
			String libelle, 
			String sens, 
			Date dateMouvement, 
			boolean isComptabilise);
	void mergePaiements(
			TYPE_ECRITURE sourceEcr, 
			List<PaiementPersistant> listPaiement,
			FournisseurPersistant fournisseur, 
			Long elementId, 
			String libelle, 
			String sens, 
			Date dateMouvement);
	void pointerCheque(Long elementId, Date datePointage);
	void pointerPaiementEcheance(Long elementId, Date datePaiement);
	List<CompteBancairePersistant> getListCompteDepot();
	void supprimerEcritureCompte(PaiementPersistant opc_paiement); 
}
