package appli.controller.domaine.administration.action;

import java.util.List;

import javax.inject.Inject;

import appli.controller.domaine.administration.bean.JobBean;
import appli.model.domaine.administration.service.IJobService;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;

@WorkController(nameSpace = "admin", bean = JobBean.class, jspRootPath = "/domaine/administration/")
public class JobAction extends ActionBase {
	@Inject
	IJobService jobService;
	
	@Override
	public void work_find(ActionUtil httpUtil) {
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_job");
		//
		List<?> listData = jobService.findByCriteriaByQueryId(cplxTable, "job_find");
	   	httpUtil.setRequestAttribute("list_job", listData);
	   	
	   	httpUtil.setDynamicUrl("/domaine/administration/job_list.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void editDetail(ActionUtil httpUtil){
		Long jobId = httpUtil.getWorkIdLong();
		httpUtil.setViewBean(jobService.findById(jobId));
		
		httpUtil.setDynamicUrl("/domaine/administration/job_detail_popup.jsp");
	}
	
	@Override
	public void work_delete(ActionUtil httpUtil) {
		Long jobId = httpUtil.getWorkIdLong();
		jobService.delete(jobId);
		
		work_find(httpUtil);
	}
}
