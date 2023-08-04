package appli.model.domaine.stock.service;

import java.util.List;

import appli.controller.domaine.stock.bean.FournisseurBean;
import appli.model.domaine.stock.persistant.FournisseurPersistant;
import framework.model.service.IGenericJpaService;

public interface IFournisseurService extends IGenericJpaService<FournisseurBean, Long> {

	void activerDesactiverElement(Long fournisseurId);

	List<FournisseurPersistant> getListFournisseur(boolean ignoreRoot, boolean onlyActif);
	
	public String genererCode();

	void affecterEtatFournisseur(FournisseurPersistant fournisseurP);

	List<FournisseurPersistant> getListFournisseurByFamilleCode(String code);

	//Object[] detailEtatFournisseur(Long fournId);
}
