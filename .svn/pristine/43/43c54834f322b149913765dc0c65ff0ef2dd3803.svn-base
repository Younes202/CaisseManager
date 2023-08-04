package appli.controller.domaine.caisse_restau.action;

import javax.inject.Inject;

import org.hibernate.criterion.Order;

import appli.controller.domaine.caisse_restau.bean.TokenBean;
import appli.model.domaine.administration.service.IUserService;
import appli.model.domaine.caisse_restau.service.ITokenService;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;

@WorkController(nameSpace="admin", bean=TokenBean.class, jspRootPath="/domaine/caisse_restau/back/")
public class TokenAction extends ActionBase {

	@Inject
	private ITokenService tokenService;
	
	@Inject
	private IUserService userService;
	
	public void work_init(ActionUtil httpUtil) {
		httpUtil.setRequestAttribute("listeUser", userService.findAll(Order.asc("login")));
	}
	
	/**
	 * @param httpUtil
	 */
	@Override
	public void work_merge(ActionUtil httpUtil) {
		TokenBean tokenBean = (TokenBean) httpUtil.getViewBean();
		if(httpUtil.getWorkIdLong() != null){
			TokenBean tokendb = tokenService.findById(httpUtil.getWorkIdLong());
			tokenBean.setIs_actif(tokendb.getIs_actif());
		}
		super.work_merge(httpUtil);
	}
	
	/**
	 * @param httpUtil
	 */
	public void activer_desactiver(ActionUtil httpUtil) {
		tokenService.activerDesactiver(httpUtil.getWorkIdLong());
		super.work_find(httpUtil);
	}
}
