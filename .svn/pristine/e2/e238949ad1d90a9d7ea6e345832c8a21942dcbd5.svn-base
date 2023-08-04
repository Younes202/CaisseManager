package appli.model.domaine.stock.service;

import java.util.List;

import appli.controller.domaine.stock.bean.FournisseurChequeBean;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_ECRITURE;
import appli.model.domaine.stock.persistant.FournisseurChequePersistant;
import framework.model.service.IGenericJpaService;

public interface IFournisseurChequeService extends IGenericJpaService<FournisseurChequeBean, Long> {
	void annulerFournisseurCheque(Long cfId);
	FournisseurChequePersistant getChequeFournisseur(Long fournId, String numCheque);
	FournisseurChequePersistant getChequeBySource(TYPE_ECRITURE source, Long elementId);
	List<FournisseurChequeBean> getListChequeFournisseurActifs(String source, Long elementId);
}
