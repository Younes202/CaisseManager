package appli.controller.domaine.administration.bean;

import appli.model.domaine.administration.persistant.UserPersistant;
import framework.controller.bean.action.IViewBean;

@SuppressWarnings("serial")
public class UserBean extends UserPersistant implements IViewBean {

	public Boolean getIs_admin() {
		return isInProfile("ADMIN");
	}
	public Boolean getIs_RemoteAdmin() {
		return getLogin().equals("REMOTE_ADMIN");
	}

}
