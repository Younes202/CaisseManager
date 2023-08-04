package appli.model.domaine.stock.service;



import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import appli.controller.domaine.stock.bean.FamilleBean;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.vente.persistant.CaissePersistant;
import framework.controller.bean.PagerBean;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.service.IGenericJpaService;

public interface IFamilleService extends IGenericJpaService<FamilleBean, Long> {

	void createFamille(FamilleBean familleBean);

	void deleteFamille(Long id);

	void updateFamille(FamilleBean familleBean);

	FamillePersistant getFamilleRoot(String type);

	List<FamillePersistant> getFamilleParent(String type, Long famId);

	FamillePersistant getFamilleParent(Long familleId);

	List<FamillePersistant> getListeFamille(String type, boolean excludeParent, boolean excludeDisabled);
	
	void activerDesactiverElement(Long familleId);

	List<FamillePersistant> getFamilleEnfants(String type, Long famId, boolean excludeDisabled);

	String generateCode(Long elementId, String type);

	void changerOrdre(Map<String, Object> mapOrder, String tp);

	List<FamillePersistant> getListeFamille(List<Long> familleIds, boolean excludeDisabled);
	
	int getListFamilleArticleSize(Long famId);

	EtablissementPersistant mergeConfRaz(Long familleBFroide, Long familleBChaude);

	FamillePersistant geteFamilleByCode(String code);

	List<FamillePersistant> getFamilleParentEnfantsByGrpCode(Class<?> typeClass, String code);

	List<FamillePersistant> getFamilleEnfants(String type, Long famId, boolean excludeDisabled, PagerBean pagerBean,boolean isOneLevel);

	List<FamillePersistant> getFamilleEnfantsOnLevel(String type, Long famId, boolean excludeDisabled);

	List<CaissePersistant> getListCaisseActive(String typeCaisse, boolean activeOnly);

	Map<String, Map<String, List<Object[]>>> getDetailEmp();

}
