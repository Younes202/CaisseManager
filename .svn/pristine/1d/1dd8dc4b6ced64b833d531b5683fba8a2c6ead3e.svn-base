package framework.model.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import framework.controller.bean.MenuBean;
import framework.controller.bean.RightBean;
import framework.model.common.util.StringUtil;


/**
 * @author 
 *
 */
public class MenuMappingService {
	private final static Logger LOGGER = Logger.getLogger(MenuMappingService.class);
	
	public static ServletContext servletContext;
//	private final static String MENU_ADMIN_PATH = "menu-admin_fr.xml";
	
//	private static Element racine = null;
	public static List<MenuBean> mapMenu;
	public static List<RightBean> mapRights;
	public static Map<String, List<String>> mapExcludedRights;
//	private static int leftBorn = 0, level = 0;
	
	// Real actions
	public static enum RIGHT_ENUM {
		RIGHT_SHOW("SHO"),
		RIGHT_EDIT("EDI"),
		RIGHT_UPDATE("UPD"),
		RIGHT_CREATE("CRE"),
		RIGHT_DUPLIC("DUP"),
		RIGHT_UPDATE_GRP("UGR"),
		RIGHT_DELETE("DEL"),
		RIGHT_DELETE_GRP("DGR");
		
		RIGHT_ENUM(String type){
			this.type = type;
		}

		private String type;
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
	}

	static {
		// Menu mapping
		mapMenu = MenuAdmin.loadMenus();// new ArrayList<MenuBean>();
		String compositeId = "";
		//
		int previousLevel = 0;
		for (MenuBean menuBean : mapMenu) {
			
			if(menuBean.getLevel() == 0){
				compositeId = "";
			}
			
			if(menuBean.getLevel() < previousLevel){
				menuBean.setCompositId(compositeId);
			}
			
			compositeId = compositeId + "." + menuBean.getId();
			
			previousLevel = menuBean.getLevel();
		}
		
		// Rights
		mapRights = MenuAdmin.loadRights();// new ArrayList<RightBean>();
		// Excluded rights
		mapExcludedRights = new HashMap<String, List<String>>();
		
		// Load list of mapping
//		leftBorn = 0; level = 0;
//		loadMenuMapping(MENU_ADMIN_PATH, mapMenu, mapRights);
		
		loadExcludedRights();
	}

	/**
	 *
	 */
//	private static void loadMenuMapping(String menuPath, List<MenuBean> listMenuBean, List<RightBean> listRightBean) {
//		// Insate SAX parser
//		SAXBuilder sxb = new SAXBuilder();
//
//		// Create new JDOM document
//		Document document;
//		try {
//			String url = ReflectUtil.class.getResource("/").getPath();
//			document = sxb.build(new File(url+"/appli/conf/"+menuPath));
//			racine = document.getRootElement();
//			// Menus
//			List<Element> listMenus = racine.getChildren("menu");
//			buildListMenu(listMenuBean, listMenus, null);
//			// Droits
//			Element right = racine.getChild("rights");
//			List<Element> listRights = right.getChildren();
//			buildListRights(listRights, listRightBean);
//			//
//			sortByBornegauche(listMenuBean);
//		} catch (JDOMException e) {
//			LOGGER.error(e.getMessage(), e);
//		} catch (IOException e) {
//			LOGGER.error(e.getMessage(), e);
//		}
//	}

	/**
	 * @param listRights
	 */
//	private static void buildListRights(List<Element> listElements, List<RightBean> listRightBean) {
//		//
//		for (Element rightElement : listElements) {
//			RightBean rightBean = new RightBean();
//			// Read attributes
//			rightBean.setId(rightElement.getAttributeValue("id"));
//			rightBean.setLabel(rightElement.getAttributeValue("label"));
//			rightBean.setParentId(rightElement.getAttributeValue("parent"));
//			rightBean.setGlobal(StringUtil.isTrue(rightElement.getAttributeValue("isGlobal")));
//			//
//			listRightBean.add(rightBean);
//		}
//	}

	/**
	 * @param id
	 * @return
	 */
	public static RightBean getRightBean(List<RightBean> listRightBean, String id) {
		for(RightBean rightBean : listRightBean){
			if(id.equals(rightBean.getId())){
				return rightBean;
			}
		}

		return null;
	}

//	/**
//	 * Load list mappin from xls configuration file
//	 */
//	private static void buildListMenu(){
//		
//	}


	/**
	 * Liste des droits exclus pour un point d'entr�e
	 * @return
	 */
//	private static void loadExcludedRights(){
//		// Add from mapping
//		List<MenuBean> listMenu = MenuMappingService.listMenu;
//		listExcludedRights = new HashMap<String, List<String>>();
//		//
//		for(MenuBean menu : listMenu){
//			String[] excludedRrights = menu.getExcludedRrights();
//			if((excludedRrights != null) && (excludedRrights.length > 0)){
//				List<String> listRighsSt = new ArrayList<String>();
//				//
//				for(String right : excludedRrights){
//					// Add current
//					listRighsSt.add(right);
//					listRighsSt= getListRightDependence(listRighsSt, right);
//				}
//				listExcludedRights.put(menu.getId(), listRighsSt);
//			}
//		}
//	}

	/**
	 * Get all childs of excluded right
	 * @param listRighsSt
	 * @param right
	 * @return
	 */
//	private static List<String> getListRightDependence(List<RightBean> listRightBean, List<String> listRighsSt, String right){
//		if(StringUtil.isNotEmpty(right)){
//			for(RightBean rightBean : listRightBean){
//				if(rightBean.getParentId() != null && rightBean.getParentId().equals(right)){
//					if(!listRighsSt.contains(rightBean.getId())){
//						listRighsSt.add(rightBean.getId());
//						getListRightDependence(listRightBean, listRighsSt, rightBean.getId());
//					}
//				}
//			}
//		}
//		
//		return listRighsSt;
//	}
	
	/**
	 * @param id
	 * @return
	 */
//	public static List<MenuBean> getListChildrenMenu(List<MenuBean> listMenuBean, String id){
//		List<MenuBean> listChildren = null;
//		if(listMenuBean != null){
//			listChildren = new ArrayList<MenuBean>();
//			int leftBorn = 0, rightBorn = 0;
//			// Get left and righte menu
//			MenuBean menuBean = getMenuById(listMenuBean, id);
//			if(menuBean != null){
//				leftBorn = menuBean.getLeftBorn();
//				rightBorn = menuBean.getRightBorn();
//			}
//			// Build new list of menu bean
//			for(MenuBean menu : listMenuBean){
//				if((menu.getLeftBorn() > leftBorn) && (menu.getRightBorn() < rightBorn)){
//					listChildren.add(menu);
//				}
//			}
//		}
//
//		return listChildren;
//	}

	/**
	 * @param menuId
	 * @return
	 */
	public static List<MenuBean> getListChildrenSheet(String menuId){
		List<MenuBean> listChildren = null;
		listChildren = new ArrayList<MenuBean>();
		// Get left and righte menu
		MenuBean menuBean = getMenuById(menuId);
		int level = -1;
		if(menuBean != null){
			level = menuBean.getLevel();
		}
		// Build new list of menu bean
		boolean isPassed = false;
		for(MenuBean menu : mapMenu){
			if(menu.getId().equals(menuId)){
				isPassed = true;
			}
			if(isPassed && level > 0 && menu.getLevel() > level){
				listChildren.add(menu);
			}
			
			if(isPassed && menu.getLevel() <= level && !menu.getId().equals(menuId)){
				break;
			}
		}
		
		return listChildren;
	}

	/**
	 * @param listMenu
	 * @return
	 */
//	public static MenuBean getDefaultMenu(List<MenuBean> listMenu){
//		if(listMenu != null){
//			for(MenuBean menu : listMenu){
//				if(menu.getLinkDefaultId() != null){
//					return menu;
//				}
//			}
//		}
//
//		return null;
//	}

	/**
	 * @param id
	 * @return
	 */
	public static MenuBean getMenuById(String id){
		if(id != null){
			// Top menu array
			String[] menuArray = StringUtil.getArrayFromStringDelim(id, ".");
			id = menuArray[menuArray.length-1];
			//
			for(MenuBean menu : mapMenu){
				if(menu.getId().equals(id)){
					 return menu;
				}
			}
		}
		return null;
	}

	/**
	 * @param id
	 * @return
	 */
//	public static List<MenuBean> getListParentMenu(List<MenuBean> listMenuBean, String id){
//		List<MenuBean> listParent = null;
//		if(listMenuBean != null){
//			listParent = new ArrayList<MenuBean>();
//			int leftBorn = 0, rightBorn = 0;
//			// Get left and righte menu
//			MenuBean menuBean = getMenuById(listMenuBean, id);
//			if(menuBean != null){
//				leftBorn = menuBean.getLeftBorn();
//				rightBorn = menuBean.getRightBorn();
//			}
//			// Build new list of menu bean
//			for(MenuBean menu : listMenuBean){
//				if((menu.getLeftBorn() < leftBorn) && (menu.getRightBorn() > rightBorn)){
//					listParent.add(menu);
//				}
//			}
//			if(menuBean != null){
//				listParent.add(menuBean);
//			}
//		}
//
//		return listParent;
//	}

	/**
	 *
	 */
//	public static void buildTree(List<MenuBean> listChildren){
//		for(MenuBean menu : listChildren){
//			String espace = "";
//			int niveau = menu.getLevel();
//
//			while(niveau != 0){
//        		espace = "---"+espace;
//        		niveau--;
//        	}
//        	if((menu.getRightBorn() - menu.getLeftBorn()) != 0){
//        		espace = "+" + espace;
//        	} else{
//        		espace = "-" + espace;
//        	}
//		}
//	}

	/**
	 * @author 
	 *
	 */
//	static class SortByLeftBorn implements Comparator<MenuBean>{
//		public int compare(MenuBean o1, MenuBean o2) {
//			return new Integer(o1.getLeftBorn()).compareTo(new Integer(o2.getLeftBorn())) ;
//		}
//	}

	/**
	 * Tri par ordre numerique sur la borne gauche
	 * @param listeTree
	 */
//	public static void sortByBornegauche(List<MenuBean> listeTree){
//		if(listeTree != null){
//			Collections.sort(listeTree, new SortByLeftBorn());
//		}
//	}
	
	/**
	 * Liste des droits exclus pour un point d'entr�e
	 * @return
	 */
	private static void loadExcludedRights(){
		// Add from mapping
		List<MenuBean> listMenu = MenuMappingService.mapMenu;
		//
		for(MenuBean menu : listMenu){
			String[] excludedRrights = menu.getExcludedRrights();
			if((excludedRrights != null) && (excludedRrights.length > 0)){
				List<String> listRighsSt = new ArrayList<String>();
				//
				for(String right : excludedRrights){
					// Add current
					listRighsSt.add(right);
					listRighsSt= getListRightDependence(listRighsSt, right);
				}
				mapExcludedRights.put(menu.getId(), listRighsSt);
			}
		}
	}
	
	/**
	 * Get all childs of excluded right
	 * @param listRighsSt
	 * @param right
	 * @return
	 */
	private static List<String> getListRightDependence(List<String> listRighsSt, String right){
		List<RightBean> listRights = mapRights;
		if(StringUtil.isNotEmpty(right)){
			for(RightBean rightBean : listRights){
				if(rightBean.getParentId() != null && rightBean.getParentId().equals(right)){
					if(!listRighsSt.contains(rightBean.getId())){
						listRighsSt.add(rightBean.getId());
						getListRightDependence(listRighsSt, rightBean.getId());
					}
				}
			}
		}
		
		return listRighsSt;
	}
	
	/**
	 * @param listMenu
	 * @return
	 */
//	public static MenuBean getDefaultMenu(List<MenuBean> listMenu){
//		if(listMenu != null){
//			for(MenuBean menu : listMenu){
//				if(menu.getLinkDefaultId() != null){
//					return menu;
//				}
//			}
//		}
//
//		return null;
//	}


}
