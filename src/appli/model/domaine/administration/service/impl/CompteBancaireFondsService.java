package appli.model.domaine.administration.service.impl;

import javax.inject.Named;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.administration.bean.CompteBancaireFondsBean;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_ECRITURE;
import appli.model.domaine.administration.persistant.EcriturePersistant;
import appli.model.domaine.administration.service.ICompteBancaireFondsService;
import appli.model.domaine.administration.validator.CompteBancaireFondsValidator;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.common.annotation.validator.WorkModelMethodValidator;
import framework.model.common.util.BigDecimalUtil;
import framework.model.service.GenericJpaService;

@WorkModelClassValidator(validator=CompteBancaireFondsValidator.class)
@Named
public class CompteBancaireFondsService extends GenericJpaService<CompteBancaireFondsBean, Long> implements ICompteBancaireFondsService{
	
	@Transactional
	private void supprimerEcritureCompte(CompteBancaireFondsBean mvmFonds){
		getQuery("delete from EcriturePersistant where elementId=:elementId "
				+ "and source=:source")
			.setParameter("elementId", mvmFonds.getId())
			.setParameter("source", "TRANSFERT")
			.executeUpdate();
	}
	
	/**
	 * @param mvmFonds
	 * @return
	 */
	@Transactional
	private void ajouterEcritureCompte(CompteBancaireFondsBean mvmFonds){
		// Supprimer l'ancienne écriture
		supprimerEcritureCompte(mvmFonds);
		
		// Ajouter
		if(!BigDecimalUtil.isZero(mvmFonds.getMontant())){
			EcriturePersistant ecritureP = new EcriturePersistant();
			ecritureP.setDate_mouvement(mvmFonds.getDate_mouvement());
			ecritureP.setElementId(mvmFonds.getId());
			ecritureP.setLibelle(mvmFonds.getLibelle());
			ecritureP.setMontant(mvmFonds.getMontant());
			ecritureP.setOpc_banque(mvmFonds.getOpc_banque_dest());
			ecritureP.setSource(TYPE_ECRITURE.TRANSFERT.toString());
			ecritureP.setSens("C");
			//
			getEntityManager().merge(ecritureP);
		}
		
		if(mvmFonds.getOpc_banque_source() != null && !BigDecimalUtil.isZero(mvmFonds.getMontant())){
			EcriturePersistant ecritureP = new EcriturePersistant();
			ecritureP.setDate_mouvement(mvmFonds.getDate_mouvement());
			ecritureP.setElementId(mvmFonds.getId());
			ecritureP.setLibelle(mvmFonds.getLibelle());
			ecritureP.setMontant(mvmFonds.getMontant());
			ecritureP.setOpc_banque(mvmFonds.getOpc_banque_source());
			ecritureP.setSource(TYPE_ECRITURE.TRANSFERT.toString());
			ecritureP.setSens("D");
			//
			getEntityManager().merge(ecritureP);
		}
	}
	
	@Override
	@Transactional
	@WorkModelMethodValidator
	public void create(CompteBancaireFondsBean mvmFonds) {
		super.create(mvmFonds);
		
		// Gérer l'écriture comptable
		ajouterEcritureCompte(mvmFonds);
	}
	
	@Override
	@Transactional
	@WorkModelMethodValidator
	public CompteBancaireFondsBean update(CompteBancaireFondsBean mvmFonds) {
		super.update(mvmFonds);
		
		// Gérer l'écriture comptable
		ajouterEcritureCompte(mvmFonds);
		
		return mvmFonds;
	}
	
	@Override
	@Transactional
	@WorkModelMethodValidator
	public void delete(Long id) {
		// Gérer l'écriture comptable
		CompteBancaireFondsBean cbB = findById(id);
		supprimerEcritureCompte(cbB);
		//		
		super.delete(id);
	}
}
