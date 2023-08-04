package appli.model.domaine.caisse_restau.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.caisse.bean.MenuCompositionBean;
import appli.model.domaine.caisse_restau.dao.IMenuCompositionDao;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.stock.validator.FamilleValidator;
import appli.model.domaine.vente.persistant.MenuCompositionPersistant;
import framework.controller.Context;
import framework.controller.bean.NodeBean;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.common.annotation.validator.WorkModelMethodValidator;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.service.MessageService;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.tree.ElementBase;
import framework.model.common.util.tree.TreeService;
import framework.model.util.GenericJpaDao;

@Named
@WorkModelClassValidator(validator = FamilleValidator.class)
public class MenuCompositionDao extends GenericJpaDao<MenuCompositionPersistant, Long> implements IMenuCompositionDao {
	private String getReqCompl() {
		String leftMnu = (String) MessageService.getGlobalMap().get(ProjectConstante.LEFT_MENU_ID);
		String reqCompl = "";
		if("cai-menu".equals(leftMnu)) {
			reqCompl += " and (mnu_source is null or mnu_source='"+leftMnu+"') ";
		} else {
			reqCompl += " and mnu_source='"+leftMnu+"' ";
		}
		return reqCompl;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public MenuCompositionPersistant getMenuParent(MenuCompositionPersistant menu) {
		// On cherche le parent dans l'ancien exercice
		List<MenuCompositionPersistant> listMenus = getQuery(
				"from MenuCompositionPersistant cpt where "
						+ "cpt.b_left<:left and cpt.b_right>:right "
						+ getReqCompl()
						+ "order by cpt.b_left desc ")
				.setParameter("left", menu.getB_left())
				.setParameter("right", menu.getB_right())
				.getResultList();

		for (MenuCompositionPersistant menuPersistant : listMenus) {
			return menuPersistant;
		}
		return null;
	}

	@Override
	@Transactional
	@WorkModelMethodValidator
	public void createMenu(MenuCompositionBean menu) {
		String leftMnu = (String) MessageService.getGlobalMap().get(ProjectConstante.LEFT_MENU_ID);
		MenuCompositionPersistant parentCompte = findById(menu.getElement_id());

		int right = parentCompte.getB_right();
		// Update
		updateSuccessifElements(2, right, "+");

		MenuCompositionPersistant menuP = new MenuCompositionPersistant();
		
		// Insert new sheet
		ReflectUtil.copyProperties(menuP, menu);
		menuP.setLibelle(menu.getLibelle());
		menuP.setB_left(right);
		menuP.setB_right(right + 1);
		menuP.setLevel(parentCompte.getLevel() + 1);
		menuP.setDate_maj(new Date());
		menuP.setIs_menu(menu.getIs_menu());
		menuP.setSignature(Context.getUserLogin());
		menuP.setMtt_prix(menu.getMtt_prix());
		menuP.setList_composition(menu.getList_composition());
		menuP.setCaisse_target(menu.getCaisse_target());
		menuP.setMnu_source(leftMnu);
		
		// Ajouter le nouvel élément à l'arbre
		menuP = getEntityManager().merge(menuP);
		
		// Copier les propriété de l'entité
		ReflectUtil.copyProperties(menu, menuP);
		menu.setId(menuP.getId());
	}

	@Override
	@Transactional
	@WorkModelMethodValidator
	public void deleteMenu(Long id) {
		MenuCompositionPersistant menuPersistant = findById(id);
		int b_right = menuPersistant.getB_right();
		int b_left = menuPersistant.getB_left();

		// Update elements-----------------------------------------------------
		int decal = 2;

		if (b_right - b_left != 1) {
			decal = b_right - b_left + 1;
		}

		// Delete elements from DB
		Query deleteQuery = getQuery(
				"delete from MenuCompositionPersistant compte "
				+ "where compte.b_left>=:left and compte.b_right<=:right "
				+ getReqCompl()
				)
				.setParameter("left", b_left).setParameter("right", b_right);

		deleteQuery.executeUpdate();
		getEntityManager().flush();

		// Update
		updateSuccessifElements(decal, menuPersistant.getB_right(), "-");
	}

	/**
	 * Mise à jour des bornes à partir des éléments impactés
	 * 
	 * @param decal
	 * @param right
	 */
	@Transactional
	private void updateSuccessifElements(int decal, int right, String sens) {
		// Update right
		Query updateRightQuery = getQuery(
				"update MenuCompositionPersistant set b_right=b_right" + sens
						+ ":decal " + "where b_right>=:right"
						+ getReqCompl()
					)
				.setParameter("decal", decal).setParameter("right", right);
		updateRightQuery.executeUpdate();
		// Update left
		Query updateLeftQuery = getQuery(
				"update MenuCompositionPersistant set b_left=b_left" + sens + ":decal "
						+ "where b_left>=:right "
						+ getReqCompl()
					)
				.setParameter("decal", decal).setParameter("right", right);
		updateLeftQuery.executeUpdate();
	}

	@Override
	@Transactional
	@WorkModelMethodValidator
	public void updateMenu(MenuCompositionBean menuBean) {
		if(menuBean == null){
			return;
		}
		
		MenuCompositionPersistant menuDb = findById(menuBean.getId());
		
		 // Vérifier si le parent à changé --> déplacement
		MenuCompositionPersistant menuParentDB = getMenuParent(menuDb);
		if(menuParentDB != null) {
		if(menuParentDB.getLevel() != 0 ) {
			if (!menuBean.getParent_id().equals(menuParentDB.getId())) {
				MenuCompositionPersistant menuTarget = findById(menuBean.getParent_id());
				mouveElement(menuDb, menuTarget);
			}
		}
		}
		menuDb = findById(menuBean.getId()); 
		//
		menuDb.setCode(menuBean.getCode());
		menuDb.setLibelle(menuBean.getLibelle());
		menuDb.setMtt_prix(menuBean.getMtt_prix());
		menuDb.setNombre_max(menuBean.getNombre_max());
		menuDb.setIs_menu(menuBean.getIs_menu());
		menuDb.getList_composition().clear();
		menuDb.getList_composition().addAll(menuBean.getList_composition());
		menuDb.setCaisse_target(menuBean.getCaisse_target());
		menuDb.setDate_maj(new Date());	
		
		getEntityManager().merge(menuDb);
	}
	
	@Transactional
	@Override
	public void changerOrdre(Map<String, Object> mapOrder) {
		EntityManager entityManager = getEntityManager();
		List<MenuCompositionPersistant> listMenu = getQuery("from MenuCompositionPersistant").getResultList();
		for (MenuCompositionPersistant mcP : listMenu) {
			Object idxOrder = mapOrder.get(mcP.getId().toString());
			if(idxOrder != null) {
				mcP.setIdx_order(Integer.valueOf(""+idxOrder));
			}
		}
		
		NodeBean nb = new NodeBean();
		nb.setBleft("b_left");
		nb.setBright("b_right");
		nb.setId("id");
		nb.setLabel("libelle");
		nb.setLevel("level");
		nb.setSort("idx_order");
		
		TreeService ts = new TreeService(listMenu, nb);
		ts.sortTreeByOrderIdx();
		
		List<ElementBase> listElements = ts.getListElement();
		
		for (ElementBase elementBase : listElements) {
			MenuCompositionPersistant mnuP = findById((Long)elementBase.getE_id());
			if(!mnuP.getB_left().equals(elementBase.getE_left()) || !mnuP.getB_right().equals(elementBase.getE_right())
					|| !mnuP.getLevel().equals(elementBase.getE_level())) {
				mnuP.setB_left(elementBase.getE_left());
				mnuP.setB_right(elementBase.getE_right());
				mnuP.setLevel(elementBase.getE_level());
				//
				entityManager.merge(mnuP);
			}
		}
	}

	/**
	 * @param menuP
	 * @param menuTarget
	 */
	private void mouveElement(MenuCompositionPersistant menuP, MenuCompositionPersistant menuTargetP) {
		EntityManager entityManager = getEntityManager();
		List<FamillePersistant> listFamille = getQuery("from "+menuP.getClass().getSimpleName()).getResultList();
		NodeBean nb = new NodeBean();
		nb.setBleft("b_left");
		nb.setBright("b_right");
		nb.setLevel("level");
		nb.setId("id");
		nb.setLabel("libelle");
		
		TreeService ts = new TreeService(listFamille, nb);
		ts.moveElement(menuP.getId(), menuTargetP.getId());
		
		List<ElementBase> listElements = ts.getListElement();
		
		for (ElementBase elementBase : listElements) {
			MenuCompositionPersistant mnuP = findById((Long)elementBase.getE_id());
			if(!mnuP.getB_left().equals(elementBase.getE_left()) || !mnuP.getB_right().equals(elementBase.getE_right())
					|| !mnuP.getLevel().equals(elementBase.getE_level())) {
				mnuP.setB_left(elementBase.getE_left());
				mnuP.setB_right(elementBase.getE_right());
				mnuP.setLevel(elementBase.getE_level());
				//
				entityManager.merge(mnuP);
			}
		}
	}
}
