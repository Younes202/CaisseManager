package appli.model.domaine.caisse.service;



import java.util.List;
import java.util.Map;

import appli.controller.domaine.caisse.bean.MenuCompositionBean;
import appli.model.domaine.vente.persistant.MenuCompositionPersistant;
import framework.controller.bean.PagerBean;
import framework.model.service.IGenericJpaService;

public interface IMenuCompositionService extends IGenericJpaService<MenuCompositionBean, Long> {

	void createMenu(MenuCompositionBean menuBean);

	void deleteMenu(Long id);

	void updateMenu(MenuCompositionBean menuBean);

	MenuCompositionPersistant getMenuRoot();

	List<MenuCompositionPersistant> getListeMenu(boolean ignoreRoot, boolean excludeDisabled);

	MenuCompositionPersistant getMenuParent(MenuCompositionPersistant menu);

	MenuCompositionPersistant getMenuParent(Long menuCompId);

	void activerDesactiverElement(Long workIdLong);

	String generateCode(Long parentId);

	void changerOrdre(Map<String, Object> mapOrder);

	void saveConf(String menuArtStr, String familleStr);

	List<MenuCompositionPersistant> getListeMenuEnfants(Long parentId, Long caisseId, boolean excludeDisabled);

	void dupliquerMenu(MenuCompositionBean menuBean, Long workIdLong);

	List<MenuCompositionPersistant> getListeMenuCaissePagination(Long caisseId, PagerBean pagerBean);
}
