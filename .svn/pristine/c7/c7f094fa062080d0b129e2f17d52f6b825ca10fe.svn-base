package appli.controller.domaine.caisse_restau.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.hibernate.criterion.Order;

import appli.controller.domaine.caisse.bean.MenuCompositionBean;
import appli.controller.domaine.caisse_restau.bean.ListChoixBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.caisse.service.ICaisseService;
import appli.model.domaine.caisse.service.IMenuCompositionService;
import appli.model.domaine.caisse_restau.service.IListChoixService;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.stock.service.IArticleService;
import appli.model.domaine.stock.service.IFamilleService;
import appli.model.domaine.vente.persistant.MenuCompositionDetailPersistant;
import appli.model.domaine.vente.persistant.MenuCompositionPersistant;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.common.constante.ActionConstante;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.service.MessageService;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;

@WorkController(nameSpace="caisse", bean=MenuCompositionBean.class, jspRootPath="/domaine/caisse_restau/normal/")
public class MenuCompositionAction extends ActionBase {
	@Inject
	private IMenuCompositionService menuService;
	@Inject
	private IArticleService articleService;
	@Inject
	private IListChoixService listChoixService;
	@Inject
	private IFamilleService familleService;
	@Inject
	ICaisseService caisseService;
	
	public void work_init(ActionUtil httpUtil) {
		String parentId = httpUtil.getParameter("fam");
		if(StringUtil.isNotEmpty(parentId)){
			httpUtil.setRequestAttribute("fam", parentId);
		} else{
			httpUtil.setRequestAttribute("fam", EncryptionUtil.encrypt(menuService.getMenuRoot().getId().toString()));// Parent racine
		}
		
		List<FamillePersistant> listFamille = familleService.getListeFamille("CU", true, false);
		httpUtil.setRequestAttribute("listFamille", listFamille);
		
		List<ArticlePersistant> listArticle = articleService.getListArticleNonStock(true);
		httpUtil.setRequestAttribute("listArticle", listArticle);
		List<ArticlePersistant> listArticleStock = articleService.getListArticleStock(true);
		httpUtil.setRequestAttribute("listArticleStock", listArticleStock);

		List<ListChoixBean> listChoix = listChoixService.findAll(Order.asc("code"), Order.asc("libelle"));
		httpUtil.setRequestAttribute("listChoix", listChoix);

		String[][] typeAjoutArray = {{"OU", "Ou"}, {"ET", "Et"}};
		httpUtil.setRequestAttribute("typeAjoutArray", typeAjoutArray);
		
		httpUtil.setRequestAttribute("parent_level", -1);
		
		String action = httpUtil.getAction();
		String currParentId = httpUtil.getParameter("menuComposition.parent_id");
		//
		if(currParentId == null && parentId != null){
			MenuCompositionPersistant parentMenu = menuService.getMenuParent(Long.valueOf(EncryptionUtil.decrypt(parentId)));
			if(parentMenu != null){
				currParentId = ""+parentMenu.getId();
				httpUtil.setRequestAttribute("parent_level", parentMenu.getLevel());
			}
		} else if(currParentId != null){
			MenuCompositionPersistant parentMenu = menuService.findById(Long.valueOf(currParentId));
			httpUtil.setRequestAttribute("parent_level", parentMenu.getLevel());
		}
		httpUtil.setRequestAttribute("parent_menu", currParentId);
		
		
		List<MenuCompositionPersistant> listMenu = menuService.getListeMenu(false, true);
		httpUtil.setRequestAttribute("listMenu", listMenu);
		
		if(ActionConstante.MERGE.equals(action)){
			setDataList(httpUtil);
		} else if(ActionConstante.INIT_CREATE.equals(action)){
			httpUtil.getText("menuComposition.nombre_max").setValue(0);
		}
		
		// Caisse
		httpUtil.setRequestAttribute("listeCaisse", caisseService.getListCaisseActive(ContextAppli.TYPE_CAISSE_ENUM.CAISSE.toString(), false));
	 
		if(httpUtil.isCrudOperation()) {
			if(httpUtil.getViewBean() != null) {
	        	MenuCompositionBean menuBean = (MenuCompositionBean)httpUtil.getViewBean();
        		String caisse = StringUtil.getStringDelimFromStringArray(menuBean.getCaisses_target(), "|");
        		menuBean.setCaisse_target(StringUtil.isNotEmpty(caisse) ? "|"+caisse+"|":null);
        	} 
		} else if(httpUtil.getWorkIdLong() != null) {
			MenuCompositionBean menuBean = menuService.findById(httpUtil.getWorkIdLong());
        	if(StringUtil.isNotEmpty(menuBean.getCaisse_target())) {
	        	String[] caisseArray = StringUtil.getArrayFromStringDelim(menuBean.getCaisse_target(), "|");
	        	menuBean.setCaisses_target(caisseArray);
	        	httpUtil.setRequestAttribute("caisseArray", caisseArray);
        	}
        }
		
		if(ContextAppli.getAbonementBean().isOptPlusEtsCentrale() 
				&& StringUtil.isTrue(StrimUtil.getGlobalConfigPropertie("CTRL_CENTRALE"))) {
			httpUtil.setRequestAttribute("isEditable", false);
		} else {
			httpUtil.setRequestAttribute("isEditable", true);
		}
		
		Long det = httpUtil.getWorkIdLong();
		if(det != null || httpUtil.getParameter("det") != null) {
			det = (det == null ? httpUtil.getLongParameter("det") : det);
			MenuCompositionBean currMenuBean = menuService.findById(det);
			httpUtil.setRequestAttribute("menuLevel", currMenuBean.getLevel());
		}
	}
	
	public void load_detail(ActionUtil httpUtil) {
		Long detId = httpUtil.getLongParameter("det");
		httpUtil.setViewBean(menuService.findById(detId));
		
		MenuCompositionPersistant parentMenu = menuService.getMenuParent(detId);
		if(parentMenu != null){
			httpUtil.setRequestAttribute("parent_level", parentMenu.getLevel());
		}
		
		httpUtil.setDynamicUrl("/domaine/caisse_restau/normal/menuComposition_detail.jsp");
	}
	
	@Override
	public void work_find(ActionUtil httpUtil) {
		menuService.getEntityManager().clear();
		
		boolean actifOnly = false;
		if("on".equals(httpUtil.getParameter("actifs-activator"))){
			actifOnly = true;
			httpUtil.setRequestAttribute("isCheked", true);
		}
		
		List<MenuCompositionPersistant> listMenus = menuService.getListeMenu(true, false);
		
		httpUtil.setRequestAttribute("listMenus", listMenus);
		httpUtil.setRequestAttribute("actifOnly", actifOnly);
		
		httpUtil.setDynamicUrl("/domaine/caisse_restau/normal/menuComposition_list.jsp");
	}
	
	@Override
	public void work_create(ActionUtil httpUtil) {
		Long parentId = httpUtil.getLongParameter("fam.id");
		String leftMnu = (String) MessageService.getGlobalMap().get(ProjectConstante.LEFT_MENU_ID);
		MenuCompositionBean menuBean = (MenuCompositionBean)httpUtil.getViewBean();
		menuBean.setElement_id(parentId);
		MenuCompositionPersistant parentMenu = menuService.findById(parentId);
		
		if(parentMenu.getLevel() == 0 && menuBean.getList_composition().size() > 0){
			MessageService.addBannerMessage("Le premier niveau du menu ne doit contenir de composition. ");
			return;
		}
		menuBean.setMnu_source(leftMnu);
		menuService.createMenu(menuBean);

		managePieceJointe(httpUtil, menuBean.getId(), "menu", 300, 300);
		
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "Création", "Création effectuée avec succès.");
		
		work_find(httpUtil);
	}
	
	@Override
	public void work_update(ActionUtil httpUtil) {
		boolean isDuplic = "dup".equals(httpUtil.getParameter("mode"));
		String leftMnu = (String) MessageService.getGlobalMap().get(ProjectConstante.LEFT_MENU_ID);
		MenuCompositionBean menuBean = (MenuCompositionBean)httpUtil.getViewBean();
		menuBean.setMnu_source(leftMnu);
		Long workIdLong = httpUtil.getWorkIdLong();
		if(!isDuplic){
			menuBean.setId(workIdLong);	
		} else{
			
			MenuCompositionPersistant cuurDup = menuService.findById(workIdLong);
			if(!BooleanUtil.isTrue(cuurDup.getIs_menu())){
				MessageService.addBannerMessage("Uniquement les menus peuvent être dupliqués.");
				return;
			}
			
			menuService.dupliquerMenu(menuBean, workIdLong);
			
			MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "Mise à jour", "Duplication effectuée avec succès.");
			
			work_find(httpUtil);
			return;
		}

		menuService.updateMenu(menuBean);
		
		managePieceJointe(httpUtil, menuBean.getId(), "menu", 300, 300);
		
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "Mise à jour", "Mise à jour effectuée avec succès.");
		
		work_find(httpUtil);
	}
	
	@Override
	public void work_delete(ActionUtil httpUtil) {
		Long workId = httpUtil.getWorkIdLong();
		menuService.deleteMenu(workId);
		manageDeleteImage(workId, "menu");
		
		work_find(httpUtil);
	}
	
	public void generer_code(ActionUtil httpUtil) {
		Long parentId = httpUtil.getLongParameter("fam.id");
		//
		String code = menuService.generateCode(parentId);
		httpUtil.writeResponse(code);
	}
	
	/**
	 * @param httpUtil
	 */
	public void desactiver(ActionUtil httpUtil) {
		menuService.activerDesactiverElement(httpUtil.getWorkIdLong());
		work_find(httpUtil);
	}
	
	public void initOrderMenu(ActionUtil httpUtil) {
		Long menuId = httpUtil.getWorkIdLong();
		MenuCompositionPersistant menu = null;
		//
		if(menuId != null) {
			menu = menuService.findById(menuId);
		} else {
			menu = menuService.getMenuRoot();
			menuId = menu.getId();
		}
		
		List<MenuCompositionPersistant> listMenu = menuService.getListeMenuEnfants(menuId, null, true);
		
		List<MenuCompositionPersistant> finalList = new ArrayList<>();
		if(listMenu != null){
			for (MenuCompositionPersistant mcP : listMenu) {
				if(menu.getLevel()+1 == mcP.getLevel()) {
					finalList.add(mcP);
				}
			}
		}
		httpUtil.setRequestAttribute("listMenus", finalList);
		
		httpUtil.setDynamicUrl("/domaine/caisse_restau/normal/menuComposition_order.jsp");
	}
	
	public void ordonnerMenu(ActionUtil httpUtil) {
		Map<String, Object> mapOrder = httpUtil.getValuesByStartName("menuOrder_");
		menuService.changerOrdre(mapOrder);
		
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "", "L'ordre des éléments est mise à jour.");
		//
		work_find(httpUtil);
	}
	
	/**
	 * @param httpUtil
	 */
	@SuppressWarnings({ "unchecked" })
	private void setDataList(ActionUtil httpUtil) {
		MenuCompositionBean menuBean = (MenuCompositionBean) httpUtil.getViewBean();
		menuBean.setId(httpUtil.getWorkIdLong());
		
		List<MenuCompositionDetailPersistant> listArticleDet = (List<MenuCompositionDetailPersistant>) httpUtil.buildListBeanFromMap("quantite",
				MenuCompositionDetailPersistant.class, "eaiid", "opc_article.id", "opc_article_destock.id", "opc_article_inc.id", "opc_list_choix.id", "opc_famille.id", "quantite", "prix", "nombre");
		menuBean.setList_composition(listArticleDet);
	}
	
	/**
	 * @param httpUtil
	 */
	public void duplic(ActionUtil httpUtil){
		super.work_init_update(httpUtil);
		httpUtil.setRequestAttribute("is_dup", 1);
	}
}
