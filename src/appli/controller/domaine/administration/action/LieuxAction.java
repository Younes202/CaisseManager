package appli.controller.domaine.administration.action;


import javax.inject.Inject;

import org.hibernate.criterion.Order;

import appli.controller.domaine.administration.bean.LieuxBean;
import appli.model.domaine.administration.service.ILieuxService;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.beanContext.VillePersistant;
import framework.model.beanContext.VilleQuartierPersistant;

@WorkController(nameSpace="admin", bean=LieuxBean.class, jspRootPath="/domaine/administration/")
public class LieuxAction extends ActionBase {
	@Inject
	private ILieuxService lieuxService;
	
	public void work_init(ActionUtil httpUtil) {
		httpUtil.setRequestAttribute("listVille", lieuxService.findAllNoFilter(VillePersistant.class, Order.asc("opc_region.libelle"), Order.asc("libelle")));
		httpUtil.setRequestAttribute("listVilleQuartier", lieuxService.findAllNoFilter(VilleQuartierPersistant.class, Order.asc("libelle")));
	}
}
