package appli.model.domaine.stock.dao;

import java.util.Map;

import appli.controller.domaine.stock.bean.FamilleBean;
import appli.model.domaine.stock.persistant.FamillePersistant;
import framework.model.util.IGenericJpaDao;

public interface IFamilleDao extends IGenericJpaDao<FamillePersistant, Long>{

	FamillePersistant getFamilleParent(FamillePersistant famille);

	void createFamille(FamilleBean famille);
	
	void updateFamille(FamilleBean famille);

	void deleteFamille(Long id);
	
//	FamillePersistant getFamilleRoot(String type);

	String getNextCode(Long elementId, String type);

	void changerOrdre(Map<String, Object> mapOrder, String type);

	FamillePersistant getFamilleRoot(String type);
	
}
