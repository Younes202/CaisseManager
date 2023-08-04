package framework.controller;

import java.util.List;
import java.util.Map;

import framework.controller.bean.MenuBean;
import framework.controller.bean.RightBean;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.service.MessageService;
import framework.model.common.util.BooleanUtil;
import framework.model.util.MenuMappingService;
import framework.model.util.MenuMappingService.RIGHT_ENUM;

public class Context {
	public static final String USER_BATCH = "BATCH";
	public static final String USER_ADMIN = "ADMIN";
	public static final String TOP_MENU_ID 		= "tmnu";
	public static final String LEFT_MENU_ID	 	= "lmnu";
	
	public static String getUserLogin(){
		return (ContextGloabalAppli.getUserBean()!=null?ContextGloabalAppli.getUserBean().getLogin():null);
	}
	
	public static boolean isOperationAvailable(RIGHT_ENUM operation){
		return isOperationAvailable(operation.getType());
	}
	
	public static boolean isOperationCaisseAvailable(String operation, String menuId){
		Map<String, Map<String, Integer>> mapMenuRights = (Map<String, Map<String, Integer>>)MessageService.getGlobalMap().get(ProjectConstante.SESSION_GLOBAL_RIGHT);
		boolean isAvailableInParams = false;
		boolean isAvailableInMenuConfig = false;
		
		if(BooleanUtil.isTrue(ContextGloabalAppli.getUserBean().getIs_admin())){
			return true;
		}
		
		if(menuId != null){
			if(mapMenuRights != null){
				Map<String, Integer> mapRights = mapMenuRights.get(menuId);
				if(mapRights != null){
					Integer value = mapRights.get(operation);
					isAvailableInParams = ((value != null) && (value == 1));
				}
			}
			
			if(isAvailableInParams) {
				// menu id in excluded menus
				Map<String, List<String>> mapExcludedRights = MenuMappingService.mapExcludedRights;
				List<String> listExludedRights = mapExcludedRights.get(menuId);
				// If excluded or it's parent is excluded
				if((listExludedRights != null)){
					if(listExludedRights.contains(operation)){
						isAvailableInMenuConfig = false;
					} else {
					  RightBean rightBean = MenuMappingService.getRightBean(MenuMappingService.mapRights, operation);
					  if(rightBean != null && listExludedRights.contains(rightBean.getParentId())){
						  isAvailableInMenuConfig = false;
					  }
					}
				}
			}
		}

		return isAvailableInMenuConfig && isAvailableInParams;
	}
	
	/**
	 * @param menuId
	 * @param operation
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isOperationAvailable(String operation){
		Map<String, Map<String, Integer>> mapMenuRights = (Map<String, Map<String, Integer>>)MessageService.getGlobalMap().get(ProjectConstante.SESSION_GLOBAL_RIGHT);
		// Menu id in db
		String menuId = (String)MessageService.getGlobalMap().get(ProjectConstante.LEFT_MENU_ID);
		boolean isAvailableInParams = true;
		boolean isAvailableInMenuConfig = true;
		
		if(menuId == null){
			menuId = (String)MessageService.getGlobalMap().get(ProjectConstante.LEFT_MENU_ID);
		}
		
		if(menuId != null){
			if(mapMenuRights != null){
				Map<String, Integer> mapRights = mapMenuRights.get(menuId);
				if(mapRights != null){
					Integer value = mapRights.get(operation);
					isAvailableInParams = ((value != null) && (value == 1));
				}
			}
			
			if(isAvailableInParams) {
				// menu id in excluded menus
				Map<String, List<String>> mapExcludedRights = MenuMappingService.mapExcludedRights;
				List<String> listExludedRights = mapExcludedRights.get(menuId);
				// If excluded or it's parent is excluded
				if((listExludedRights != null)){
					if(listExludedRights.contains(operation)){
						isAvailableInMenuConfig = false;
					} else {
					  RightBean rightBean = MenuMappingService.getRightBean(MenuMappingService.mapRights, operation);
					  if(rightBean != null && listExludedRights.contains(rightBean.getParentId())){
						  isAvailableInMenuConfig = false;
					  }
					}
				}
			}
		}

		return isAvailableInMenuConfig && isAvailableInParams;
	}

	/**
	 * @param menuId
	 * @param isNoeud
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isMenuAvailable(String menuId, boolean isNoeud){
		Map<String, Map<String, Integer>> mapMenuRights = (Map<String, Map<String, Integer>>)MessageService.getGlobalMap().get(ProjectConstante.SESSION_GLOBAL_RIGHT);

		if(mapMenuRights != null && mapMenuRights.size() > 0){
			// Menu id
			if(isNoeud){
				boolean isSheetFounded = false;
				boolean childreenExist = false;
				List<MenuBean> listChildrenMenu = MenuMappingService.getListChildrenSheet(menuId);
//				int sheetCount = listChildrenMenu.size();
//				int sheetIdx = 0; 
				for(String mnuKey : mapMenuRights.keySet()){
					boolean isExist = false;
					for(MenuBean mnuBean : listChildrenMenu){
						if(mnuBean.getId().equals(mnuKey)){
							isExist = true;
							break;
						}
					}
					// Get children
					if(isExist){
						childreenExist = true;
						//
						Map<String, Integer> mapRights = mapMenuRights.get(mnuKey);
						Integer value = mapRights.get(MenuMappingService.RIGHT_ENUM.RIGHT_SHOW.getType());
						if((value != null) && (value == 1)){
							isSheetFounded = true;
							break;
						}
//						sheetIdx++;
					}
				}
				return (isSheetFounded || /*(!isSheetFounded && (sheetCount != sheetIdx)) ||*/ childreenExist);
			} else{
				Map<String, Integer> mapRights = mapMenuRights.get(menuId);
				if(mapRights != null){
					Integer value = mapRights.get(MenuMappingService.RIGHT_ENUM.RIGHT_SHOW.getType());
					if((value != null) && (value == 1)){
						return true;
					}
				}
			}
		} else{
			boolean isAdmin = (ContextGloabalAppli.getUserBean() != null && BooleanUtil.isTrue(ContextGloabalAppli.getUserBean().getIs_admin()));
			if(isAdmin){	
				return true;// Si map Right null alors c'est un admin
			}
		}
		return false;
	}

	public static List<MenuBean> getMenuList() {
		return MenuMappingService.mapMenu;
	}

	/**
	 * @param menuId
	 * @param operation
	 * @return
	 */
	/*	@SuppressWarnings("unchecked")
	public static boolean isOperationAvailable(MenuMappingService.RIGHT_ENUM operation){
		Map<String, Map<String, Integer>> mapMenuRights = (Map<String, Map<String, Integer>>)MessageService.getGlobalMap().get(ProjectConstante.SESSION_GLOBAL_RIGHT);
		// Menu id in db
		String menuId = (String)MessageService.getGlobalMap().get(LEFT_MENU_ID);
		boolean isAvailableInParams = true;
		boolean isAvailableInMenuConfig = true;
		
		if(menuId == null){
			menuId = (String)MessageService.getGlobalMap().get(LEFT_MENU_ID);
		}
		
		if(menuId != null){
			menuId = menuId.substring(menuId.lastIndexOf(".")+1);
			//
			if(mapMenuRights != null){
				Map<String, Integer> mapRights = mapMenuRights.get(menuId);
				if(mapRights != null){
					Integer value = mapRights.get(operation.getType());
					isAvailableInParams = ((value != null) && (value == 1));
				}
			}
			
			if(isAvailableInParams) {
				// menu id in excluded menus
				Map<String, List<String>> mapExcludedRights = MenuMappingService.mapExcludedRights;
				List<RightBean> listRights = MenuMappingService.mapRights;
				List<String> listExludedRights = mapExcludedRights.get(menuId);
				// If excluded or it's parent is excluded
				if((listExludedRights != null)){
					if(listExludedRights.contains(operation.getType())){
						isAvailableInMenuConfig = false;
					} else {
					  RightBean rightBean = MenuMappingService.getRightBean(listRights, operation.getType());
					  if(rightBean != null && listExludedRights.contains(rightBean.getParentId())){
						  isAvailableInMenuConfig = false;
					  }
					}
				}
			}
		}

		return isAvailableInMenuConfig && isAvailableInParams;
	}*/

	/**
	 * @param menuId
	 * @param isNoeud
	 * @return
	 */
//	@SuppressWarnings("unchecked")
//	public static boolean isMenuAvailable(String menuId, boolean isNoeud){
//		Map<String, Map<String, Integer>> mapMenuRights = (Map<String, Map<String, Integer>>)MessageService.getGlobalMap().get(ProjectConstante.SESSION_GLOBAL_RIGHT);
//		if(mapMenuRights != null){
//			// Menu id
//			if(isNoeud){
//				boolean isSheetFounded = false;
//				boolean childreenExist = false;
//				for(String mnuKey : mapMenuRights.keySet()){
//					// Get children
//					if(mnuKey.startsWith(menuId)){
//						childreenExist = true;
//						//
//						Map<String, Integer> mapRights = mapMenuRights.get(mnuKey);
//						Integer value = mapRights.get(RIGHT_ENUM.RIGHT_SHOW.getType());
//						if((value != null) && (value == 1)){
//							isSheetFounded = true;
//							break;
//						}
//					}
//				}
//				return (isSheetFounded || childreenExist);
//			} else{
//				Map<String, Integer> mapRights = mapMenuRights.get(menuId);
//				if(mapRights != null){
//					Integer value = mapRights.get(RIGHT_ENUM.RIGHT_SHOW.getType());
//					if((value != null) && (value == 1)){
//						return true;
//					}
//				}
//			}
//		} else{
//			return true;// Si map Right null alors c'est un admin
//		}
//		return false;
//	}
}
