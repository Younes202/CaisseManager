package appli.controller.domaine.caisse_restau.action;

import javax.inject.Inject;

import appli.controller.domaine.administration.bean.AgencementBean;
import appli.model.domaine.administration.service.IAgencementService;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;

@WorkController(nameSpace="admin", bean=AgencementBean.class, jspRootPath="/domaine/caisse_restau/normal/")
public class AgencementAction extends ActionBase {
	@Inject
	private IAgencementService agencementService;
	
	public void work_init(ActionUtil httpUtil) {
	
	}
	
	public void manageCalendrier(ActionUtil httpUtil) {
		agencementService.manageCalendrier(httpUtil.getWorkIdLong());
		super.work_find(httpUtil);
	}
}
