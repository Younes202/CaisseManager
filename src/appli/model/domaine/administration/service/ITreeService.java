package appli.model.domaine.administration.service;

import java.util.List;
import java.util.Map;

import appli.controller.domaine.stock.bean.ArticleBean;
import appli.model.domaine.administration.persistant.GedPersistant;
import framework.model.service.IGenericJpaService;

public interface ITreeService extends IGenericJpaService<ArticleBean, Long> {

	<T>T getTreeRoot(Class<T> treeClass);

	String generateTreeCode(Class<?> treeClass, Long parentId);

	<T>List<T> getTreeEnfants(Class<T> treeClass, Long elementId, boolean isActifOnly);
	
	<T>List<T> getTreeParents(Class<T> treeClass, Long elementId, boolean isActifOnly);

	<T>T getTreeParent(Class<T> treeClass, Long elementId);

	void createTree(Object treeBean);

	void deleteTree(Class<?> treeClass, Long elementId);

	void updateTree(Object familleBean);

	void changerOrdre(Map<String, Object> mapOrder, Class<?> treeClass);

	void activerDesactiverTree(Class<?> treeClass, Long elementId);

	<T>List<T> getListeTree(Class<T> treeClass, boolean excludeParent, boolean isActifOnly);

	void mergeGedRoot(GedPersistant gedP);
}
