package appli.controller.domaine.fidelite.action;

import javax.inject.Inject;

import appli.controller.domaine.fidelite.bean.CarteFideliteBean;
import appli.model.domaine.fidelite.service.ICarteFideliteService;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;

@WorkController(nameSpace="fidelite", bean=CarteFideliteBean.class, jspRootPath="/domaine/fidelite/")
public class CarteFideliteAction extends ActionBase {
	@Inject
	ICarteFideliteService carteFideliteService;

	@Override
	public void work_edit(ActionUtil httpUtil) {
		if(httpUtil.getWorkIdLong() != null){
			httpUtil.setMenuAttribute("carteId", httpUtil.getWorkIdLong());
		}
		Long carteId = (Long) httpUtil.getMenuAttribute("carteId");
		httpUtil.setViewBean(carteFideliteService.findById(carteId));
		//
		httpUtil.setDynamicUrl("/domaine/fidelite/carteFidelite_edit.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void desactiver(ActionUtil httpUtil) {
		carteFideliteService.activerDesactiverElement(httpUtil.getWorkIdLong());
		work_find(httpUtil);
	}
}
