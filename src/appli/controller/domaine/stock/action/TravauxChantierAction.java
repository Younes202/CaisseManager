package appli.controller.domaine.stock.action;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.criterion.Order;

import appli.controller.domaine.stock.bean.TravauxChantierBean;
import appli.controller.domaine.stock.bean.TravauxChantierBean;
import appli.model.domaine.personnel.persistant.EmployePersistant;
import appli.model.domaine.stock.persistant.TravauxChantierPersistant;
import appli.model.domaine.stock.service.ITravauxService;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;

@WorkController(nameSpace="stock", bean=TravauxChantierBean.class, jspRootPath="/domaine/stock/")
public class TravauxChantierAction extends ActionBase {
		@Inject
		private ITravauxService travauxService;
		
		public void work_init(ActionUtil httpUtil) {
			if(httpUtil.getMenuAttribute("IS_SUB_ADD") == null) {
				httpUtil.setMenuAttribute("IS_SUB_ADD","stock.travauxChantier.work_init_create");
			}		
		}
		
		@Override
		public void work_find(ActionUtil httpUtil) {
			RequestTableBean cplxTable = getTableBean(httpUtil, "list_travauxChantier");
			List<TravauxChantierPersistant> listData = (List<TravauxChantierPersistant>) travauxService.findByCriteriaByQueryId(cplxTable, "travauxChantier_find");
			httpUtil.setRequestAttribute("list_travauxChantier", listData);
			
			httpUtil.setDynamicUrl("/domaine/stock/travauxChantier_list.jsp");
		}
		
		@Override
		public void work_merge(ActionUtil httpUtil) {
			TravauxChantierBean travauxChantierBean = (TravauxChantierBean) httpUtil.getViewBean();
			travauxChantierBean.setId(httpUtil.getWorkIdLong());
			
			super.work_merge(httpUtil);
			managePieceJointe(httpUtil, ((TravauxChantierBean)httpUtil.getViewBean()).getId(), "travauxChantier");
			if(httpUtil.getMenuAttribute("IS_SUB_ADD") != null) {
				httpUtil.setDynamicUrl(""+httpUtil.getMenuAttribute("IS_SUB_ADD"));
			}
		}
		
		@Override
		public void work_delete(ActionUtil httpUtil) {
			super.work_delete(httpUtil);
			manageDeleteImage(httpUtil.getWorkIdLong(), "travauxChantier");
		}
		
		public void downloadPieceJointe(ActionUtil httpUtil) {
			httpUtil.manageInputFileView("travauxChantier");
		}
		
		public void work_post(ActionUtil httpUtil){
			manageDataForm(httpUtil, "CHANTIER");
		}
}
