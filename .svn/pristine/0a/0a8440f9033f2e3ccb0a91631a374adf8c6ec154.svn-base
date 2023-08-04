package appli.model.domaine.caisse_restau.dao;

import java.util.Map;

import appli.controller.domaine.caisse.bean.MenuCompositionBean;
import appli.model.domaine.vente.persistant.MenuCompositionPersistant;
import framework.model.util.IGenericJpaDao;

public interface IMenuCompositionDao extends IGenericJpaDao<MenuCompositionPersistant, Long>{

	MenuCompositionPersistant getMenuParent(MenuCompositionPersistant menu);

	void createMenu(MenuCompositionBean menu);
	
	void updateMenu(MenuCompositionBean menu);

	void deleteMenu(Long id);

	void changerOrdre(Map<String, Object> mapOrder);
}
