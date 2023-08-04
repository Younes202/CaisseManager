package appli.controller.domaine.mobile.action;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import appli.controller.domaine.administration.bean.UserBean;
import appli.model.domaine.administration.service.IUserService;
import framework.controller.ActionUtil;
import framework.controller.ControllerUtil;
import framework.controller.annotation.WorkController;
import framework.controller.annotation.WorkForward;
import framework.model.common.util.BooleanUtil;

/**
 * @author 
 *
 */
@WorkController(nameSpace="mobile") 
public class MobileAction {
	@Inject
	private IUserService userService;
	
    public void connect(ActionUtil httpUtil) throws Exception{
		HttpServletRequest request = httpUtil.getRequest();
		String mail = (String)ControllerUtil.getParam(request, "login");
		String pw = (String)ControllerUtil.getParam(request, "pw");
		//
		mail = (mail != null) ? mail.trim() : null; 
		pw = (pw != null) ? pw.trim() : null;
		UserBean userBean = null; 
		//
		userBean = userService.getUserByLoginAndPw(mail, pw); 
		
		//
		if(userBean == null){
			httpUtil.writeResponse("AUTH_ERROR");
			return;
		} else{
			// Si le compte est désactivé
			if(BooleanUtil.isTrue(userBean.getIs_desactive())){
				httpUtil.writeResponse("AUTH_DISABLE");
				return;
			}
			httpUtil.writeResponse("AUTH_OK");
		}
    }

	/**
	 * @param httpUtil
	 */
	@WorkForward(useBean=false)
	public void disconnect(ActionUtil httpUtil){
		HttpServletRequest request = httpUtil.getRequest();
		HttpSession session = request.getSession(false);

		// If session exists, destroy it
		if(session != null){
			session.invalidate();
		}
		
		httpUtil.setDynamicUrl("/commun/secure_page.jsp");
	}
}
