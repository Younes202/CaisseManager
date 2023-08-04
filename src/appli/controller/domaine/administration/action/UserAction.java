package appli.controller.domaine.administration.action;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.criterion.Order;

import appli.controller.domaine.administration.bean.UserBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.administration.persistant.UserPersistant;
import appli.model.domaine.administration.service.IUserService;
import appli.model.domaine.habilitation.service.IProfileService;
import appli.model.domaine.personnel.service.IEmployeService;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.ControllerUtil;
import framework.controller.annotation.WorkController;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.service.MessageService;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.EncryptionEtsUtil;
import framework.model.common.util.StringUtil;

@WorkController(nameSpace="admin", bean=UserBean.class, jspRootPath="/domaine/administration/")
public class UserAction extends ActionBase {
	@Inject
	private IUserService userService;
	@Inject
	private IProfileService profileService;
	@Inject
	private IEmployeService employeService;
	
	public void work_init(ActionUtil httpUtil) {
		httpUtil.setRequestAttribute("list_profile", profileService.findAll(Order.asc("libelle")));
		httpUtil.setRequestAttribute("list_employe", employeService.findAll(Order.asc("nom")));
	}
	
	public void desactiver(ActionUtil httpUtil) {
		userService.activerDesactiverElement(httpUtil.getWorkIdLong());
		httpUtil.setDynamicUrl("admin.user.work_find");
	}
	
	public void init_changerPw(ActionUtil httpUtil) {
		httpUtil.setDynamicUrl("/domaine/administration/user_pw.jsp");
	}
	
	@Override
	public void work_find(ActionUtil httpUtil) {
		String tpEmpl = httpUtil.getParameter("tpEmpl");
		
		if(tpEmpl != null){
			httpUtil.setMenuAttribute("tpEmpl", tpEmpl);
		}
		tpEmpl = (String) httpUtil.getMenuAttribute("tpEmpl");
		
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_user");
		
		String etsCond = 
				(ContextAppli.IS_FULL_CLOUD() || ContextAppli.IS_CLOUD_MASTER() ? (" and user.opc_etablissement.id="+ContextAppli.getEtablissementBean().getId())+" " : " ");
		
		String req = "";
		if(StringUtil.isNotEmpty(tpEmpl)){
			req = "select user from UserPersistant user "
				+ "left join user.opc_profile profile1 "
				+ "left join user.opc_profile2 profile2 "
				+ "left join user.opc_profile3 profile3 "
				+ "where user.login!='REMOTE_ADMIN' " + etsCond
				+ getFilterStateRequest(httpUtil, "user.is_desactive")
				+ " and (profile1.code='"+tpEmpl+"' OR profile2.code='"+tpEmpl+"' OR profile3.code='"+tpEmpl+"') "
				+ " order by user.opc_profile.code, user.login";
		} else {
			req = "from UserPersistant user "
				+ "where user.login!='REMOTE_ADMIN' " + etsCond
				+ getFilterStateRequest(httpUtil, "user.is_desactive")
				+ " order by user.opc_profile.code, user.login";
		}
		
		List<UserPersistant> listData = (List<UserPersistant>) userService.findByCriteria(cplxTable, req);
		userService.refreshEntities(listData);
		
		httpUtil.setRequestAttribute("list_user", listData);
		
		httpUtil.setDynamicUrl("/domaine/administration/user_list.jsp");
	}
	
	@Override
	public void work_merge(ActionUtil httpUtil) {
		UserBean userBean = (UserBean) httpUtil.getViewBean();
		userBean.setPassword(new EncryptionEtsUtil(EncryptionEtsUtil.getDecrypKey()).encrypt(userBean.getPassword()));
		
		super.work_merge(httpUtil);
	}
	
	public void changerPw(ActionUtil httpUtil) {
		userService.changerPw(httpUtil.getParameter("old_pw"), httpUtil.getParameter("new_pw"), httpUtil.getParameter("new_pw2"));
		
		if(!MessageService.isError()){
			httpUtil.getText("old_pw").setValue("");
			httpUtil.getText("new_pw").setValue("");
			httpUtil.getText("new_pw2").setValue("");
			MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "Mot de passe modifié", "Le mot de passe est mise à jour avec succès.");			
		}
		
		httpUtil.setDynamicUrl("/domaine/administration/user_pw.jsp");
	}
	
	public void manageFavNav(ActionUtil httpUtil){
		String mnu = httpUtil.getParameter("mnu");
		boolean isFav = StringUtil.isTrue(httpUtil.getParameter("isfav"));
		String favoris_nav = ContextAppli.getUserBean().getFavoris_nav();
		favoris_nav = (favoris_nav==null ? "" : favoris_nav);
		favoris_nav = favoris_nav.replaceAll(";"+mnu+";", "");
		
		if(!isFav){
			favoris_nav = favoris_nav.replaceAll(";"+mnu+";", "")+(";"+mnu+";");
		}
		
		if(StringUtil.getArrayFromStringDelim(favoris_nav, ";").length >=13){
			MessageService.addGrowlMessage("", "Vous avez atteint le nombre de favouris disponible");
			return;
		}
		ContextAppli.getUserBean().setFavoris_nav(favoris_nav);
		
		UserBean userDb = userService.findById(ContextAppli.getUserBean().getId());
		userDb.setFavoris_nav(favoris_nav);
		
		userService.merge(userDb);
	}
	
	/**
	 * @param httpUtil
	 */
	public void loadConfirmAnnule(ActionUtil httpUtil) {
		httpUtil.setFormReadOnly(false);
		List<UserPersistant> finalListUsers = new ArrayList<UserPersistant> ();
		List<UserPersistant> listUsers = userService.findAllUser(true);

		for (UserPersistant userPersistant : listUsers) {
			if(userPersistant.isInProfile("MANAGER")
					|| userPersistant.isInProfile("ADMIN")) {
				finalListUsers.add(userPersistant);
			}
		}
		httpUtil.setRequestAttribute("listUser", finalListUsers);
		
		httpUtil.setDynamicUrl("/domaine/administration/authentification-act-popup.jsp");
	}
	/**
	 * @param httpUtil
	 */
	public void confirmActCmd(ActionUtil httpUtil) {
		String badge = (String)ControllerUtil.getParam(httpUtil.getRequest(), "tkn");
		boolean isBadge = StringUtil.isNotEmpty(badge);
		UserBean userBean = null;
		//
		if(isBadge){
			userBean = userService.getUserByBadge(badge.trim());
			//
			if(userBean == null){
				MessageService.addBannerMessage("Ce badge n'a pas été encore enregistré.");
				return;
			}
		} else {
			if(StringUtil.isEmpty(httpUtil.getParameter("cmd.password"))) {
				MessageService.addFieldMessage("cmd.password", "Le mot depasse est obligatoire.");
				return;
			}
			if(StringUtil.isEmpty(httpUtil.getParameter("cmd.user.id"))) {
				MessageService.addFieldMessage("cmd.user.id", "Le login est obligatoire.");
				return;
			}
			
			Long userId = Long.valueOf(httpUtil.getParameter("cmd.user.id"));
			String pw = new EncryptionEtsUtil(EncryptionEtsUtil.getDecrypKey()).encrypt(httpUtil.getParameter("cmd.password"));
			userBean = userService.findById(userId);
			
			if(!pw.equals(userBean.getPassword())) {
				MessageService.addFieldMessage("cmd.password", "Le mot de passe est erroné.");
				return;
			}
		}
			
		// Si le compte est désactivé
		if(BooleanUtil.isTrue(userBean.getIs_desactive())){
			MessageService.addBannerMessage("Ce compte utilisateur est désactivé.</b>");
			return;
		}
			
		if(!userBean.isInProfile("ADMIN") &&
				!userBean.isInProfile("MANAGER")){
			MessageService.addBannerMessage("Cette action require un profile administrateur ou manager.");
			return;
		}
		
		MessageService.getGlobalMap().put("IS_ADMIN_MODE", true);
		
		httpUtil.writeResponse("MSG_CUSTOM:<h3>Vous êtes en mode ADMINISTRATEUR.</h3>");
	}
	
	public void releaseActCmd(ActionUtil httpUtil){
		MessageService.getGlobalMap().remove("IS_ADMIN_MODE");
		httpUtil.writeResponse("MSG_CUSTOM:<h3>Vous avez QUITTÉ le mode administrateur.</h3>");
	}
	
	/**
	 * @param httpUtil
	 */
	public void work_post(ActionUtil httpUtil) {

	}
}
