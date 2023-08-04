package appli.model.domaine.stock.service.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.stock.bean.FournisseurChequeBean;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_ECRITURE;
import appli.model.domaine.stock.dao.IFournisseurChequeDao;
import appli.model.domaine.stock.persistant.FournisseurChequePersistant;
import appli.model.domaine.stock.service.IFournisseurChequeService;
import appli.model.domaine.stock.validator.FournisseurChequeValidator;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.service.GenericJpaService;

@WorkModelClassValidator(validator=FournisseurChequeValidator.class)
@Named
public class FournisseurChequeService extends GenericJpaService<FournisseurChequeBean, Long> implements IFournisseurChequeService{
	@Inject
	IFournisseurChequeDao fournisseurChequeDao;
	
	@Override
	@Transactional
	public void annulerFournisseurCheque(Long cfId) {
		FournisseurChequePersistant fcP = fournisseurChequeDao.findById(cfId);
		
		if(fcP.getDate_annulation() == null){
			fcP.setDate_annulation(new Date());
		} else{
			fcP.setDate_annulation(null);
		}
		//
		fournisseurChequeDao.update(fcP);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly=true)
	public List<FournisseurChequeBean> getListChequeFournisseurActifs(String source, Long elementId) { 
		String req = "from FournisseurChequePersistant where (date_encaissement is null "
				+ "and date_annulation is null and source is null) ";
		
		if(source != null){
			req = req + "or (source=:source and elementId=:elementId) ";
		}
		req = req + "order by num_cheque";
		
		Query query = getQuery(req);
		if(source != null){
			query.setParameter("source", source);
			query.setParameter("elementId", elementId);
		}
		
		return query.getResultList();
	}
	
	@Override
	public FournisseurChequePersistant getChequeFournisseur(Long fournId, String numCheque) {
		return (FournisseurChequePersistant) getSingleResult(getQuery("from FournisseurChequePersistant where opc_fournisseur.id=:fournId and num_cheque=:numCheque")
				.setParameter("fournId", fournId)
				.setParameter("numCheque", numCheque));
	}

	@Override
	public FournisseurChequePersistant getChequeBySource(TYPE_ECRITURE source, Long elementId) {
		return (FournisseurChequePersistant) getSingleResult(getQuery("from FournisseurChequePersistant where source=:source "
				+ "and elementId=:elementId")
					.setParameter("source", source.toString())
					.setParameter("elementId", elementId));
	}

//	@Override
//	public FournisseurChequePersistant getChequeByDepense(Long depenseId) {
//		return (FournisseurChequePersistant) getSingleResult(getQuery("from FournisseurChequePersistant where opc_depense.id=:depId")
//				.setParameter("depId", depenseId));
//	}
//	
//	@Override
//	public FournisseurChequePersistant getChequeByMouvement(Long mouvementId) {
//		return (FournisseurChequePersistant) getSingleResult(getQuery("from FournisseurChequePersistant where opc_mouvement.id=:mvmId")
//				.setParameter("mvmId", mouvementId));
//	}
}
