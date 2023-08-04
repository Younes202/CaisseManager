package appli.controller.domaine.administration.action;

import appli.controller.domaine.administration.bean.MessageBean;
import framework.controller.ActionBase;
import framework.controller.annotation.WorkController;

@WorkController(nameSpace="admin", bean=MessageBean.class, jspRootPath="/domaine/administration/")
public class MessageAction extends ActionBase {

}
