package appli.model.domaine.compta.service;

import java.math.BigDecimal;
import java.util.List;

import appli.controller.domaine.compta.bean.CompteBean;
import framework.model.beanContext.ComptePersistant;
import framework.model.service.IGenericJpaService;

public interface ICompteService extends IGenericJpaService<CompteBean, Long> {
	String calculateNewCodeCompte(ComptePersistant parentCompte, int taille);
	void updateSoldeInitialisation(Long compteId, BigDecimal montant, String sens);
	List<CompteBean> getListCompteChargeRecuperable(boolean isRecuperable);
	List<ComptePersistant> getPlanComptable(boolean ignoreRoot);
	void createCompte(CompteBean compteBean);
	void deleteCompte(Long id);
	void updateCompte(CompteBean compteBea);
	CompteBean getCompteByCode(String code);
	ComptePersistant getCompteRoot();
	ComptePersistant getCompteParent(Long valueOf);
	List<CompteBean> getListCompteByCode(String code);
}