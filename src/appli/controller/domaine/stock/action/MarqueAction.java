package appli.controller.domaine.stock.action;

import javax.inject.Inject;

import appli.controller.domaine.stock.bean.MarqueBean;
import appli.model.domaine.stock.service.IMarqueService;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;

@WorkController(nameSpace="stock", bean=MarqueBean.class, jspRootPath="/domaine/stock/")
public class MarqueAction extends ActionBase {
	@Inject
	private IMarqueService marqueService;
	
	public void genererCode(ActionUtil httpUtil) {
		String code = marqueService.genererCode();
		httpUtil.writeResponse(code);
	}
	
	/**
	 * @param httpUtil
	 */
	public void desactiver(ActionUtil httpUtil) {
		marqueService.activerDesactiverElement(httpUtil.getWorkIdLong());
		work_find(httpUtil);
	}
	
	@Override
	public void work_merge(ActionUtil httpUtil) {
		MarqueBean marqueBean = (MarqueBean) httpUtil.getViewBean();
		marqueBean.setId(httpUtil.getWorkIdLong());
		
		super.work_merge(httpUtil);
		managePieceJointe(httpUtil, marqueBean.getId(), "marque");
		if(httpUtil.getMenuAttribute("IS_SUB_ADD") != null) {
			httpUtil.setDynamicUrl(""+httpUtil.getMenuAttribute("IS_SUB_ADD"));
		}
	}
	
	@Override
	public void work_delete(ActionUtil httpUtil) {
		Long workId = httpUtil.getWorkIdLong();
		
		super.work_delete(httpUtil);
		manageDeleteImage(workId, "marque");
	}
	
	public void work_post(ActionUtil httpUtil){
		manageDataForm(httpUtil, "MARQUE");
	}
}