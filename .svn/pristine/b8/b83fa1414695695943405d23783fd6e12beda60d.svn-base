package appli.model.domaine.personnel.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.personnel.bean.FraisBean;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_ECRITURE;
import appli.model.domaine.administration.service.ICompteBancaireService;
import appli.model.domaine.compta.persistant.PaiementPersistant;
import appli.model.domaine.personnel.service.IFraisService;
import appli.model.domaine.personnel.validator.FraisValidator;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.service.GenericJpaService;

@WorkModelClassValidator(validator=FraisValidator.class)
@Named
public class FraisService extends GenericJpaService<FraisBean, Long> implements IFraisService{
	@Inject
	private ICompteBancaireService compteBancaireService;
	
	@Override
	@Transactional
	public void rembourserDemande(FraisBean fraisBean, PaiementPersistant paiementP) {
		//impacter le compte
		List<PaiementPersistant> listPaiement = new ArrayList<>();
		listPaiement.add(paiementP);
		String libelle = "Remboursement frais à " + fraisBean.getOpc_employe().getNom() + " " + fraisBean.getOpc_employe().getPrenom();
		
		compteBancaireService.mergePaiements(TYPE_ECRITURE.REMBOURS, listPaiement, null, fraisBean.getId(), libelle, "D", new Date());
		
		//mise à jour de la note frais
		PaiementPersistant paiementPer = (PaiementPersistant) getSingleResult(getQuery("from PaiementPersistant where elementId=:elementId and source=:source")
				.setParameter("elementId", fraisBean.getId())
				.setParameter("source", TYPE_ECRITURE.REMBOURS.toString()));
		fraisBean.setOpc_paiement(paiementPer);
		fraisBean.setStatut("VALIDATED");
		update(fraisBean);
	}
	
	@Override
	@Transactional
	public void annulerDemande(FraisBean fraisBean) {
		PaiementPersistant opc_paiement = fraisBean.getOpc_paiement();
		fraisBean.setOpc_paiement(null);
		fraisBean.setStatut(null);
		update(fraisBean);
		
		compteBancaireService.supprimerEcritureCompte(opc_paiement);
		
		getQuery("delete from PaiementPersistant where elementId=:elementId "
				+ "and source=:source")
			.setParameter("elementId", opc_paiement.getElementId())
			.setParameter("source", opc_paiement.getSource())
			.executeUpdate();
	}
	
}
