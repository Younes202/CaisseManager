package appli.controller.domaine.habilitation.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import appli.controller.domaine.habilitation.bean.ProfileMenuBean;
import appli.model.domaine.administration.service.IParametrageService;
import appli.model.domaine.habilitation.persistant.ProfileMenuPersistant;
import appli.model.domaine.habilitation.persistant.ProfilePersistant;
import appli.model.domaine.habilitation.service.IProfileMenuService;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.controller.annotation.WorkForward;
import framework.controller.bean.MenuBean;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.service.MessageService;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.MenuMappingService;

/**
 * Servlet implementation class for Servlet: Welcome
 *
 */
@WorkController(nameSpace="hab", bean=ProfileMenuBean.class, jspRootPath="/domaine/habilitation/")
public class HabilitationAction extends ActionBase{ 
	@Inject
	private IProfileMenuService profileMenuService;
	@Inject
	private IParametrageService parametrageService;
	
	/**
	 * @param httpUtil
	 */
	public void work_init(ActionUtil httpUtil) {
//		httpUtil.setDynamicUrl("/domaine/admin/habilitation_edit.jsp");
	}
	
	/* (non-Javadoc)
	 * @see controller.ActionBase#work_find(controller.util.ActionUtil)
	 */
	public void work_find(ActionUtil httpUtil) {
		
	}
	
	@Override
	public void work_init_update(ActionUtil httpUtil) {
		if(StringUtil.isEmpty(httpUtil.getParameter("isCais"))) {
			httpUtil.setDynamicUrl("/domaine/habilitation/habilitation_edit.jsp");
		} else {
			Long profile_id = (Long) httpUtil.getMenuAttribute("profileId");
			httpUtil.setRequestAttribute("profile_id", profile_id);
			ProfileMenuPersistant profileMenuP = profileMenuService.getProfileMenuByMenuAndProfile("caisse-right", profile_id);
			
			if(profileMenuP != null) {
				Map<String, String> mapDroitsCaisse = profileMenuService.getCaisseRight(profileMenuP);
				for (String right : mapDroitsCaisse.keySet()) {
					if("1".equals(mapDroitsCaisse.get(right))) {
						httpUtil.setRequestAttribute(right, 1);
					}
				}
			} else {
				httpUtil.setRequestAttribute("param_CAISEMPL", parametrageService.getParameterByCode("CAISSE_EMPLOYE").getValeur());
				httpUtil.setRequestAttribute("param_CAISCLI", parametrageService.getParameterByCode("CAISSE_CLIENT").getValeur());
			}
			
			httpUtil.setDynamicUrl("/domaine/habilitation/droit_caisse_edit.jsp");
		}
	}
	
	@Override
	public void work_edit(ActionUtil httpUtil) {
		if(StringUtil.isNotEmpty(httpUtil.getParameter("isCais"))) {
			Long profile_id = (Long) httpUtil.getMenuAttribute("profileId");
			httpUtil.setRequestAttribute("profile_id", profile_id);
			
			ProfileMenuPersistant profileMenuP = profileMenuService.getProfileMenuByMenuAndProfile("caisse-right", profile_id);
			
			if(profileMenuP != null) {
				Map<String, String> mapDroitsCaisse = profileMenuService.getCaisseRight(profileMenuP);
				for (String right : mapDroitsCaisse.keySet()) {
					if("1".equals(mapDroitsCaisse.get(right))) {
						httpUtil.setRequestAttribute(right, 1);
					}
				}
			} else {
				httpUtil.setRequestAttribute("param_CAISEMPL", parametrageService.getParameterByCode("CAISSE_EMPLOYE").getValeur());
				httpUtil.setRequestAttribute("param_CAISCLI", parametrageService.getParameterByCode("CAISSE_CLIENT").getValeur());
			}
			
			httpUtil.setDynamicUrl("/domaine/habilitation/droit_caisse_edit.jsp");
		} else {
			httpUtil.setDynamicUrl("/domaine/habilitation/habilitation_edit.jsp");
		}
	}

	/**
	 * @param httpUtil
	 */
	@WorkForward(useBean=true)
	public void work_update(ActionUtil httpUtil){
		if(StringUtil.isEmpty(httpUtil.getParameter("isCais"))) {
			Map<String, Object> mapData = httpUtil.getValuesByStartName("chck_");
			Long profileId = (Long) httpUtil.getMenuAttribute("profileId");
			
			Map<String, String> mapDroits = new HashMap<String, String>();
			for (String key : mapData.keySet()) {
				String droit = key.substring(0, key.indexOf("_"));
				String menuId = key.substring((key.indexOf("_")+1), key.lastIndexOf("_"));
				mapDroits.put(menuId, StringUtil.getValueOrEmpty(mapDroits.get(menuId))+";"+droit+":"+(StringUtil.isTrue(""+mapData.get(key)) ? 1 : 0));
			}
			profileMenuService.createOrUpdate(profileId, mapDroits);
			//
			httpUtil.setDynamicUrl("hab.habilitation.work_edit");
			MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "Mise à jour des droits", "Les droits applicatifs ont été mis à jour avec succès.");
		} else {
			Long profile_id = (Long) httpUtil.getMenuAttribute("profileId");
			ProfilePersistant profilePersistant = (ProfilePersistant) profileMenuService.findById(ProfilePersistant.class, profile_id);
			ProfileMenuPersistant profileMenuP = profileMenuService.getProfileMenuByMenuAndProfile("caisse-right", profile_id);
			
			String rights = "";
				rights = rights + ";CAISCLI:" + (httpUtil.getParameter("param_CAISCLI")!=null ? "1" : "0") 
						+ ";CAISEMPL:" + (httpUtil.getParameter("param_CAISEMPL")!=null ? "1" : "0")
						+ ";CLOJRN:" + (httpUtil.getParameter("param_CLOJRN")!=null ? "1" : "0")
						+ ";IMPRAZ:" + (httpUtil.getParameter("param_IMPRAZ")!=null ? "1" : "0")
						+ ";HISTOCAIS:" + (httpUtil.getParameter("param_HISTOCAIS")!=null ? "1" : "0");
			
			if(profileMenuP != null) {
				profileMenuP.setRights(rights);
				profileMenuService.update( (ProfileMenuBean) ServiceUtil.persistantToBean(ProfileMenuBean.class, profileMenuP));
			} else {
				ProfileMenuBean profileMenuB = new ProfileMenuBean();
				profileMenuB.setMenu_id("caisse-right");
				profileMenuB.setOpc_profile(profilePersistant);
				profileMenuB.setRights(rights);
				profileMenuService.create(profileMenuB);
			}
			
			httpUtil.setDynamicUrl("hab.profile.work_find");
		}
	}
	
	/**
	 * @param httpUtil
	 * @throws Exception
	 */
	public void work_post(ActionUtil httpUtil) {
		// List menus
		List<MenuBean> listMenu = new ArrayList<MenuBean>(MenuMappingService.mapMenu);
		listMenu.remove(0);
		listMenu.remove(0);
		httpUtil.setRequestAttribute("listMenu", listMenu); 
		
		// Get profile menus
		Long profileId = (Long)httpUtil.getMenuAttribute("profileId");
		List<ProfileMenuPersistant> listProfileMenu = profileMenuService.getListMenuProfileByProfile(profileId);
		httpUtil.setRequestAttribute("listProfileMenu", listProfileMenu);
	}
}
