package appli.controller.domaine.personnel.action;

import java.util.List;

import javax.inject.Inject;

import appli.controller.domaine.administration.bean.ValTypeEnumBean;
import appli.controller.domaine.personnel.bean.PosteBean;
import appli.model.domaine.administration.service.IValTypeEnumService;
import appli.model.domaine.personnel.service.IPosteService;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.util.ModelConstante;

@WorkController(nameSpace="pers", bean=PosteBean.class, jspRootPath="/domaine/personnel/")
public class PosteAction extends ActionBase {

	@Inject
	private IValTypeEnumService valTypeEnumService;
	@Inject
	private IPosteService posteService;
	
	public void work_init(ActionUtil httpUtil) {
		List<ValTypeEnumBean> listeType = valTypeEnumService.getListValeursByType(ModelConstante.ENUM_TYPE_POSTE);
		httpUtil.setRequestAttribute("listType", listeType);
		List<ValTypeEnumBean> listeQualification = valTypeEnumService.getListValeursByType(ModelConstante.ENUM_QUALIFICATION);
		httpUtil.setRequestAttribute("listQualification", listeQualification);
	}
	
	@Override
	public void work_merge(ActionUtil httpUtil) {
		if(httpUtil.getMenuAttribute("IS_SUB_ADD") != null) {
			posteService.merge((PosteBean) httpUtil.getViewBean());
			httpUtil.setDynamicUrl(""+httpUtil.getMenuAttribute("IS_SUB_ADD"));
		} else {
			super.work_merge(httpUtil);
		}
	}
}



