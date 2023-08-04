package appli.controller.domaine.administration.action;

import appli.controller.domaine.administration.bean.VacanceBean;
import framework.controller.ActionBase;
import framework.controller.annotation.WorkController;

@WorkController(nameSpace="admin", bean=VacanceBean.class, jspRootPath="/domaine/administration/")
public class VacanceAction extends ActionBase {

}
