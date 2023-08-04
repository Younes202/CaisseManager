package appli.model.domaine.administration.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.stock.bean.ArticleBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.administration.dao.ITreeDao;
import appli.model.domaine.administration.persistant.GedPersistant;
import appli.model.domaine.administration.service.ITreeService;
import appli.model.domaine.stock.persistant.FamillePersistant;
import framework.controller.bean.NodeBean;
import framework.model.common.service.MessageService;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.tree.ElementBase;
import framework.model.service.GenericJpaService;

@Named
public class TreeService extends GenericJpaService<ArticleBean, Long> implements ITreeService{
	
	@Inject
	ITreeDao treeDao;
	
	@Override
	public <T>List<T> getListeTree(Class<T> treeClass, boolean excludeParent, boolean isActifOnly) {
		String req = "from "+treeClass.getSimpleName()+" where 1=1 ";
		if(excludeParent){
			req = req + " and (code is null or code!=:code) ";
		}
		if(isActifOnly){
			req = req + " and (is_disable is null or is_disable=0) ";
		}
		req = req + " order by b_left";
		
		Query query = getQuery(req);
		if(excludeParent){
			query.setParameter("code", "ROOT");
		}
			
		return query.getResultList();
	}
	
	@Override
	public <T>T getTreeParent(Class<T> treeClass, Long elementId){
		Object famille = findById(treeClass.getSuperclass(), elementId);
		
		Class<T> superclass = (Class<T>) treeClass.getSuperclass();
		List<T> listFamille = getQuery("from "+superclass.getSimpleName()
                + " where b_left<:bLeft and b_right>:bRight and (code is null or code!='ROOT') "
                + " order by b_left desc")
                .setParameter("bLeft", ReflectUtil.getObjectPropertieValue(famille, "b_left"))
                .setParameter("bRight", ReflectUtil.getObjectPropertieValue(famille, "b_right"))
                .getResultList();
		
		T familleP = (listFamille.size() > 0) ? listFamille.get(0) : null;
		
		return (familleP == null) ? getTreeRoot(superclass) : familleP;
	}
	
	@Override
	public <T>List<T> getTreeParents(Class<T> treeClass, Long elementId, boolean isActifOnly){
		Object famille = findById(treeClass.getSuperclass(), elementId);
		
		Class<?> superclass = treeClass.getSuperclass();
		String req = "from "+superclass.getSimpleName()
                + " where b_left<:bLeft and b_right>:bRight and (code is null or code!='ROOT') ";
		
		if(isActifOnly){
        	req = req + " and (is_disable is null or is_disable=0) ";
        }
		req = req + " order by b_left desc";
		
		List<?> listFamille = getQuery(req)
				.setParameter("bLeft", ReflectUtil.getObjectPropertieValue(famille, "b_left"))
                .setParameter("bRight", ReflectUtil.getObjectPropertieValue(famille, "b_right"))
                .getResultList();
		
		return (List<T>) listFamille;
	}
	
	@Override
	public <T>T getTreeRoot(Class<T> treeClass) {
		return (T) getSingleResult(getQuery("from "+treeClass.getSimpleName()+" where code=:code")
				.setParameter("code", "ROOT"));
	}

	@Override
	public String generateTreeCode(Class<?> treeClass, Long parentId) {
		return treeDao.getNextCodeTree(treeClass.getSuperclass(), parentId);
	}

	@Override
	public <T>List<T> getTreeEnfants(Class<T> treeClass, Long elementId, boolean isActifOnly) {
		Object famille = getSingleResult(getQuery("from "+treeClass.getSimpleName()+" where id=:id")
				.setParameter("id", elementId));
		
        String req = "from "+treeClass.getSimpleName()+" where b_left>:bLeft and b_right<:bRight and (code is null or code!='ROOT') ";
        if(isActifOnly){
        	req = req + " and (is_disable is null or is_disable=0) ";
        }
        req = req + " order by b_left asc";

        List<?> listFamille = getQuery(req)
                .setParameter("bLeft", ReflectUtil.getObjectPropertieValue(famille, "b_left"))
                .setParameter("bRight", ReflectUtil.getObjectPropertieValue(famille, "b_right"))
                .getResultList();
        
        return (List<T>) listFamille;
	}
	
	@Override
	@Transactional
	public void createTree(Object treeBean) {
		Object parentCompte = findById(treeBean.getClass().getSuperclass(), (Long) ReflectUtil.getObjectPropertieValue(treeBean, "parent_id"));

		int right = (int) ReflectUtil.getObjectPropertieValue(parentCompte, "b_right");
		// Update
		updateSuccessifElements(2, right, "+", treeBean.getClass().getSuperclass());

		Object familleP = null;
		try {
			familleP = treeBean.getClass().getSuperclass().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Insert new sheet
		ReflectUtil.copyProperties(familleP, treeBean);
		
		ReflectUtil.setProperty(familleP, "libelle", ReflectUtil.getObjectPropertieValue(treeBean, "libelle"));
		ReflectUtil.setProperty(familleP, "b_left", right);
		ReflectUtil.setProperty(familleP, "b_right", right + 1);
		ReflectUtil.setProperty(familleP, "level", ((Integer)ReflectUtil.getObjectPropertieValue(parentCompte, "level"))+1);

		ReflectUtil.setProperty(familleP, "date_maj", new Date());
		ReflectUtil.setProperty(familleP, "signature", ContextAppli.getUserBean().getLogin());
		
		// Ajouter le nouvel élément � l'arbre
		familleP = getEntityManager().merge(familleP);
		
		// Copier les propriété de l'entité
		ReflectUtil.copyProperties(treeBean, familleP);
		
		ReflectUtil.setProperty(treeBean, "id", ReflectUtil.getObjectPropertieValue(familleP, "id"));
	}

	@Override
	@Transactional
	public void deleteTree(Class<?> treeClass, Long elementId) {
		Object famillePersistant = findById(treeClass.getSuperclass(), elementId);
		String code = (String) ReflectUtil.getObjectPropertieValue(famillePersistant, "code");
		
		if("ROOT".equals(code)) {
			MessageService.addBannerMessage("La racine de l'arborescence ne peut pas être supprimée.");
			return;
		}
		
		int b_right = (int) ReflectUtil.getObjectPropertieValue(famillePersistant, "b_right");
		int b_left = (int) ReflectUtil.getObjectPropertieValue(famillePersistant, "b_left");

		// Update elements-----------------------------------------------------
		int decal = 2;

		if (b_right - b_left != 1) {
			decal = b_right - b_left + 1;
		}

		// Delete elements from DB
		Query deleteQuery = getQuery(
				"delete from "+treeClass.getSuperclass().getSimpleName()+" compte where compte.b_left>=:left and compte.b_right<=:right")
				.setParameter("left", b_left).setParameter("right", b_right);

		deleteQuery.executeUpdate();
		getEntityManager().flush();

		// Update
		int b_rightN = (int) ReflectUtil.getObjectPropertieValue(famillePersistant, "b_right");
		updateSuccessifElements(decal, b_rightN, "-", treeClass.getSuperclass());
	}

	/**
	 * Mise à jour des bornes à partir des éléments impactés
	 * 
	 * @param decal
	 * @param right
	 */
	@Transactional
	private void updateSuccessifElements(int decal, int right, String sens, Class<?> treeClass) {
		// Update right
		Query updateRightQuery = getQuery(
				"update "+treeClass.getSimpleName()+" set b_right=b_right" + sens
						+ ":decal " + "where b_right>=:right")
				.setParameter("decal", decal).setParameter("right", right);
		updateRightQuery.executeUpdate();
		// Update left
		Query updateLeftQuery = getQuery(
				"update "+treeClass.getSimpleName()+" set b_left=b_left" + sens + ":decal "
						+ "where b_left>=:right")
				.setParameter("decal", decal).setParameter("right", right);
		updateLeftQuery.executeUpdate();
	}

	@Override
	@Transactional
	public void updateTree(Object familleBean) {
		Long familleId = (Long) ReflectUtil.getObjectPropertieValue(familleBean, "id");
		Object familleDb = findById(familleBean.getClass().getSuperclass(), familleId);
		
		// Vérifier si le parent à changé --> déplacement--------------------------------
		Object familleParentDB = getTreeParent(familleBean.getClass(), familleId);
		
		Long parentIdB = (Long) ReflectUtil.getObjectPropertieValue(familleBean, "parent_id");
		Long parentIdDb = (Long) ReflectUtil.getObjectPropertieValue(familleParentDB, "id");
		
		if (!parentIdB.equals(parentIdDb)) {
			Object familleTarget = findById(familleBean.getClass().getSuperclass(), parentIdB);
			
			// Ajouter contrôle si target n'est pas un enfant
			Integer targetLeft = (Integer) ReflectUtil.getObjectPropertieValue(familleTarget, "b_left");
			Integer targetRight = (Integer) ReflectUtil.getObjectPropertieValue(familleTarget, "b_right");
			Integer origineLeft = (Integer) ReflectUtil.getObjectPropertieValue(familleDb, "b_left");
			Integer origineRight = (Integer) ReflectUtil.getObjectPropertieValue(familleDb, "b_right");
			if(targetLeft > origineLeft  && targetRight < origineRight) {
				MessageService.addBannerMessage("Impossible de déplacer un répertoire vers son enfant.");
				return;
			}
	
			mouveElement(familleBean, familleTarget);
		}
		//------------------------------------------------------------------------------
		familleDb = findById(familleBean.getClass().getSuperclass(), familleId);
		//
		ReflectUtil.setProperty(familleDb, "code", ReflectUtil.getObjectPropertieValue(familleBean, "code"));
		ReflectUtil.setProperty(familleDb, "libelle", ReflectUtil.getObjectPropertieValue(familleBean, "libelle"));
		
		getEntityManager().merge(familleDb);
	}

	/**
	 * @param familleP
	 * @param familleTarget
	 */
	@Transactional
	private void mouveElement(Object familleP, Object familleTargetP) {
		EntityManager entityManager = getEntityManager();
		List<FamillePersistant> listFamille = getQuery("from "+familleP.getClass().getSuperclass().getSimpleName()+" order by b_left")
				.getResultList();
		NodeBean nb = new NodeBean();
		nb.setBleft("b_left");
		nb.setBright("b_right");
		nb.setId("id");
		nb.setLabel("libelle");
		nb.setLevel("level");
		
		Long familleId = (Long) ReflectUtil.getObjectPropertieValue(familleP, "id");
		Long familleTargetId = (Long) ReflectUtil.getObjectPropertieValue(familleTargetP, "id");
		
		framework.model.common.util.tree.TreeService ts = new framework.model.common.util.tree.TreeService(listFamille, nb);
		ts.moveElement(familleId, familleTargetId);
		
		List<ElementBase> listElements = ts.getListElement();
		
		for (ElementBase elementBase : listElements) {
			Object famP = findById(familleP.getClass().getSuperclass(), (Long)elementBase.getE_id());
			
			Integer left = (Integer) ReflectUtil.getObjectPropertieValue(famP, "b_left");
			Integer right = (Integer) ReflectUtil.getObjectPropertieValue(famP, "b_right");
			Integer level = (Integer) ReflectUtil.getObjectPropertieValue(famP, "level");
			
			if(!left.equals(elementBase.getE_left()) || !right.equals(elementBase.getE_right())
					|| !level.equals(elementBase.getE_level())) {
				ReflectUtil.setProperty(famP, "b_left", elementBase.getE_left());
				ReflectUtil.setProperty(famP, "b_right", elementBase.getE_right());
				ReflectUtil.setProperty(famP, "level", elementBase.getE_level());
				//
				entityManager.merge(famP);
			}
		}
	}
	
	@Transactional
	@Override
	public void changerOrdre(Map<String, Object> mapOrder, Class<?> treeClass) {
		EntityManager entityManager = getEntityManager();
		List<?> listFamille = getQuery("from "+treeClass.getSuperclass().getSimpleName()+" order by b_left")
				.getResultList();
		for (Object famP : listFamille) {
			Long familleId = (Long) ReflectUtil.getObjectPropertieValue(famP, "id");
			Object idxOrder = mapOrder.get(familleId.toString());
			if(idxOrder != null) {
				ReflectUtil.setProperty(famP, "idx_order", Integer.valueOf(""+idxOrder));
			}
		}
		
		NodeBean nb = new NodeBean();
		nb.setBleft("b_left");
		nb.setBright("b_right");
		nb.setId("id");
		nb.setLabel("libelle");
		nb.setLevel("level");
		nb.setSort("idx_order");
		
		framework.model.common.util.tree.TreeService ts = new framework.model.common.util.tree.TreeService(listFamille, nb);
		ts.sortTreeByOrderIdx();
		
		List<ElementBase> listElements = ts.getListElement();
		
		for (ElementBase elementBase : listElements) {
			Object famP = findById(treeClass.getSuperclass(), (Long)elementBase.getE_id());
			
			Integer left = (Integer) ReflectUtil.getObjectPropertieValue(famP, "b_left");
			Integer right = (Integer) ReflectUtil.getObjectPropertieValue(famP, "b_right");
			Integer level = (Integer) ReflectUtil.getObjectPropertieValue(famP, "level");
			
			if(!left.equals(elementBase.getE_left()) || !right.equals(elementBase.getE_right())
					|| !level.equals(elementBase.getE_level())) {
				ReflectUtil.setProperty(famP, "b_left", elementBase.getE_left());
				ReflectUtil.setProperty(famP, "b_right", elementBase.getE_right());
				ReflectUtil.setProperty(famP, "level", elementBase.getE_level());
				//
				entityManager.merge(famP);
			}
		}
	}

	@Override
	@Transactional
	public void activerDesactiverTree(Class<?> treeClass, Long elementId) {
		Object famillePersistant = findById(treeClass.getSuperclass(), elementId);
		Boolean isDisable = (Boolean) ReflectUtil.getObjectPropertieValue(famillePersistant, "is_disable");
		ReflectUtil.setProperty(famillePersistant, "is_disable", BooleanUtil.isTrue(isDisable) ? false : true);
		//
		getEntityManager().merge(famillePersistant);
	}

	@Override
	@Transactional
	public void mergeGedRoot(GedPersistant gedP) {
		getEntityManager().merge(gedP);
	}
	
}
