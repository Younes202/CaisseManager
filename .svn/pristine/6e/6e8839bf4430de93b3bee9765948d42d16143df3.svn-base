package appli.controller.domaine.administration.action;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.hibernate.criterion.Order;

import appli.controller.domaine.administration.bean.TypeEnumBean;
import appli.controller.domaine.administration.bean.ValTypeEnumBean;
import appli.model.domaine.administration.service.ITypeEnumService;
import appli.model.domaine.administration.service.IValTypeEnumService;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.NumericUtil;

@WorkController(nameSpace="admin", bean=ValTypeEnumBean.class, jspRootPath="/domaine/administration/")
public class ValTypeEnumAction extends ActionBase {

	@Inject
	private ITypeEnumService typeEnumService;
	@Inject
	private IValTypeEnumService valTypeEnumService;
	
	public void work_init(ActionUtil httpUtil) {
		String tpId = httpUtil.getRequest().getParameter("valEnum.opc_typenum.id");
		if(tpId != null){
			if(!NumericUtil.isNum(tpId)){
				tpId = EncryptionUtil.decrypt(""+tpId);
			}

		   	httpUtil.setMenuAttribute("typeEnum", tpId);
		   	httpUtil.setRequestAttribute("typeEnum", tpId);
		} else{
			httpUtil.setRequestAttribute("typeEnum", httpUtil.getMenuAttribute("typeEnum"));
		}
		
		httpUtil.setRequestAttribute("listTypeEnum", typeEnumService.findAll(Order.asc("libelle"))); 
		
		httpUtil.setDynamicUrl("/domaine/administration/valTypeEnum_list.jsp"); 
	}
	
	@Override
	public void work_merge(ActionUtil httpUtil) {
		ValTypeEnumBean valBean = (ValTypeEnumBean) httpUtil.getViewBean();
		
		valBean.setId(httpUtil.getWorkIdLong());
		if(valBean.getId() == null){
			valBean.setCode("Code_"+System.nanoTime()); 
		} else{
			ValTypeEnumBean valBeandb = valTypeEnumService.findById(httpUtil.getWorkIdLong());
			valBean.setCode(valBeandb.getCode());
		}
		String typeId = (String) httpUtil.getMenuAttribute("typeEnum");
		if(!NumericUtil.isNum(typeId)){
			typeId = EncryptionUtil.decrypt(""+typeId);
		}
		
		TypeEnumBean typeBean =  typeEnumService.findById(Long.valueOf(typeId));
		valBean.setOpc_typenum(typeBean);
		
		
		if (httpUtil.getMenuAttribute("IS_SUB_ADD") == null) {
			super.work_merge(httpUtil);	
		} else {
			valTypeEnumService.merge(valBean);
			httpUtil.writeResponse("OK");
		}
	}
	
	/**
	 * @param httpUtil
	 */
	public void desactiver(ActionUtil httpUtil) {
		valTypeEnumService.activerDesactiverElement(httpUtil.getWorkIdLong());
		work_find(httpUtil);
	}
	
	/**
	 * @param httpUtil
	 */
	public void work_post(ActionUtil httpUtil) {
		String typeId = (String)httpUtil.getMenuAttribute("typeEnum");
		if(typeId != null){
			RequestTableBean cplxTable = getTableBean(httpUtil, "list_valeurs");
			//			
			Map<String, Object> formCriterion = cplxTable.getFormBean().getFormCriterion();
			formCriterion.put("typenumId", Long.valueOf(""+typeId));
			//
			List<ValTypeEnumBean> list_valeurs = (List<ValTypeEnumBean>) valTypeEnumService.findByCriteriaByQueryId(cplxTable, "valTypeEnum_find");
		   	httpUtil.setRequestAttribute("list_valeurs", list_valeurs);
		}
	}
}
