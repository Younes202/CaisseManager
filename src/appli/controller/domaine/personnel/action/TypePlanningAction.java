package appli.controller.domaine.personnel.action;

import appli.controller.domaine.personnel.bean.TypePlanningBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;

@WorkController(nameSpace = "pers", bean = TypePlanningBean.class, jspRootPath = "/domaine/personnel/")
public class TypePlanningAction extends ActionBase {
	
	/**
	 * @param httpUtil
	 */
	public void work_init(ActionUtil httpUtil) {
		
	}

}
