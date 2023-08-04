package appli.model.domaine.compta.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import appli.controller.domaine.compta.bean.CompteBean;
import framework.model.beanContext.ComptePersistant;
import framework.model.util.IGenericJpaDao;

public interface ICompteDao extends IGenericJpaDao<ComptePersistant, Long>{

	String getMaxCodeCompte(ComptePersistant parentCompte, int taille);

	List<ComptePersistant>  getListCompteByCode(String code);
	ComptePersistant getCompteByCode(String code);

	List<ComptePersistant> getListCompteChargeRecuperable(boolean isRecuperable);

	BigDecimal getMontantInitialisation(String... compte_code);

	BigDecimal getMontantInitialisation(char sens,	String... compte_code);

	ComptePersistant getParentCompte(ComptePersistant oldCompte);

	void createCompte(CompteBean compte);

	void deleteCompte(Long id);

	ComptePersistant getCompteParent(ComptePersistant compte);

	void updateCompte(CompteBean compteBean);

	void changerOrdre(Map<String, Object> mapOrder);

	List<ComptePersistant> getPlanComptabe(boolean ignoreRoot);

	List<ComptePersistant> getListeCompteEnfants(Long parentId);

	ComptePersistant getCompteParent(Long compteId);

	List<ComptePersistant> getPlanComptabe(Long exerciceId, boolean ignoreRoot);
}
