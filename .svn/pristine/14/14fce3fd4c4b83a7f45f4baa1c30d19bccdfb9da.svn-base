package framework.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import appli.model.domaine.administration.persistant.ValTypeEnumPersistant;
import framework.component.complex.table.RequestTableBean;
import framework.controller.annotation.WorkController;
import framework.controller.annotation.WorkForward;
import framework.controller.bean.FileInputInfos;
import framework.controller.bean.MappingBean;
import framework.controller.bean.action.IViewBean;
import framework.model.beanContext.DataValuesPersistant;
import framework.model.common.constante.ActionConstante;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.service.MessageService;
import framework.model.common.util.DateUtil;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;
import framework.model.service.GenericJpaService;
import framework.model.service.IGenericJpaService;
import framework.model.util.printGen.PrintCommunUtil;
import framework.model.util.printGen.PrintPosBean;

@WorkController(nameSpace="GENERIC") 
public abstract class ActionBase {
	
	private ResourceBean resourceBean;

	/**
	 * @param httpUtil
	 * @throws Exception
	 */
	@WorkForward(useBean=true, useFormValidator=false)
	public void work_edit(ActionUtil httpUtil) {
		loadBean(httpUtil);
		
		MappingBean mappingBean = httpUtil.getMappingBean();
		
		String path = mappingBean.getJspRootPath();
		if(!path.endsWith("/")){
			path = path + "/";
		}
		httpUtil.setDynamicUrl(path + mappingBean.getAlias()+"_edit.jsp");
	}

	/**
	 * @param httpUtil
	 * @throws Exception
	 */
	public void work_init_duplic(ActionUtil httpUtil) {
		loadBean(httpUtil);
		
		MappingBean mappingBean = httpUtil.getMappingBean();
		String path = mappingBean.getJspRootPath();
		if(!path.endsWith("/")){
			path = path + "/";
		}
		httpUtil.setDynamicUrl(path + mappingBean.getAlias()+"_edit.jsp");
	}

	/**
	 * @param httpUtil
	 * @throws Exception
	 */
	public void work_init_create(ActionUtil httpUtil) {
		MappingBean mappingBean = httpUtil.getMappingBean();
		String path = mappingBean.getJspRootPath();
		if(!path.endsWith("/")){
			path = path + "/";
		}
		httpUtil.setDynamicUrl(path + mappingBean.getAlias()+"_edit.jsp");
	}
	
	/**
	 * @param httpUtil
	 * @throws Exception
	 */
	public void work_init_update(ActionUtil httpUtil) {
		loadBean(httpUtil);
		
		MappingBean mappingBean = httpUtil.getMappingBean();
		
		String path = mappingBean.getJspRootPath();
		if(!path.endsWith("/")){
			path = path + "/";
		}
		httpUtil.setDynamicUrl(path + mappingBean.getAlias()+"_edit.jsp");
	}

	/**
	 * @param httpUtil
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void loadBean(ActionUtil httpUtil) {
		Long id = httpUtil.getWorkIdLong();
		if(id != null && !MessageService.isError()){
			IViewBean bean = (IViewBean)resourceBean.getService().findById(id);
			httpUtil.setViewBean(bean);
		}
	}
	
	/**
	 * @param httpUtil
	 * @param workId
	 * @throws Exception
	 */
	public void loadBean(ActionUtil httpUtil, Long workId)  {
		IViewBean bean = (IViewBean)resourceBean.getService().findById(workId);
		httpUtil.setViewBean(bean);
	}
	
	@WorkForward(useBean=true, useFormValidator=true)
	public void work_merge(ActionUtil httpUtil) {
		IViewBean bean = (IViewBean) httpUtil.getViewBean();
		Long id = httpUtil.getWorkIdLong();
		
		if(bean != null){
			if(id == null){
				work_create(httpUtil);
			} else{
				ReflectUtil.setProperty(bean, "id", id);
				work_update(httpUtil);
			}
		}
	}

	/**
	 * @param httpUtil
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@WorkForward(useBean=true, useFormValidator=true)
	public void work_create(ActionUtil httpUtil) {
		IViewBean bean = (IViewBean) httpUtil.getViewBean();
		resourceBean.getService().create(bean);
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, StrimUtil.label("work.update.title"), StrimUtil.label("work.create.succes"));
		
		if(ControllerUtil.isCrudOperationOK(httpUtil.getRequest())){
			// Gérer le back
			FrontFilter.restaureBackParams(httpUtil.getRequest(), null, true);
		}
		
		//
		work_find(httpUtil);
	}

	/**
	 * @param httpUtil
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@WorkForward(useBean=true, useFormValidator=true)
	public void work_update(ActionUtil httpUtil) {
		IViewBean bean = (IViewBean)httpUtil.getViewBean();
		resourceBean.getService().update(bean);
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, StrimUtil.label("work.update.title"), StrimUtil.label("work.update.succes"));

		if(ControllerUtil.isCrudOperationOK(httpUtil.getRequest())){
			// Gérer le back
			FrontFilter.restaureBackParams(httpUtil.getRequest(), null, true);
		}
		
		//
		work_find(httpUtil);
	}

	/**
	 * @param httpUtil
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public void work_delete(ActionUtil httpUtil) {
	    Long id = httpUtil.getWorkIdLong();
	    resourceBean.getService().delete(id);
	    MessageService.addGrowlMessage(MSG_TYPE.SUCCES, StrimUtil.label("work.update.title"), StrimUtil.label("work.delete.succes"));
	    //
	    
		// Gérer le back
		FrontFilter.restaureBackParams(httpUtil.getRequest(), null, true);
	    
	    work_find(httpUtil);
	}

	/**
	 * @param httpUtil
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void work_delete_rows(ActionUtil httpUtil) {
	    Long[] ids = httpUtil.getCheckedElementsLong(httpUtil.getParameter(ProjectConstante.WORK_TABLE_NAME));
	    resourceBean.getService().delete_group(ids);
	    if(ids != null && ids.length > 0){
	    	httpUtil.getRequest().setAttribute("nbrElementsGroup", ids.length);
	    	
			// Audit
			if(httpUtil.getRequestAttribute("auditBean") == null){
				String idsSt = "";
				for (Long id : ids) {
					idsSt = idsSt + id;
				}
			}
	    }
	    MessageService.addGrowlMessage(MSG_TYPE.SUCCES, StrimUtil.label("work.update.title"), StrimUtil.label("work.deleterows.succes", ""+ids.length));
	    
		// Gérer le back
		FrontFilter.restaureBackParams(httpUtil.getRequest(), null, true);
	    
	    //
	    work_find(httpUtil);
	}
	
	/**
	 * @param httpUtil
	 * @throws Exception
	 */
	public void work_find(ActionUtil httpUtil) {
		findElements(httpUtil, true, false);
	}
	
	public void work_find_nl(ActionUtil httpUtil) {
		findElements(httpUtil, false, false);
	}
	
	public void work_find_refresh(ActionUtil httpUtil) {
		findElements(httpUtil, true, true);
	}
	
	@SuppressWarnings("rawtypes")
	private void findElements(ActionUtil httpUtil, boolean isLimit, boolean forceRefresh){
		String[] ctrlAction = ControllerUtil.getCtrlActionArray(httpUtil.getRequest(), null);
		String name = ctrlAction[1]; 
		if(name.indexOf(".") != -1){
			name = name.substring(name.indexOf(".")+1); 
		}
		
		IGenericJpaService genericService = (IGenericJpaService) httpUtil.getRequest().getAttribute("work_generic_service");
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_"+name);
		//
		List<?> listData = null;
		if(isLimit){	
			listData = genericService.findByCriteriaByQueryId(cplxTable, name+"_find");
		} else{
			listData = genericService.findByCriteriaByQueryId(cplxTable, name+"_find", false);
		}
		
		// Refresh data
		if(forceRefresh) {
			genericService.refreshEntities(listData);
		}
		
	   	httpUtil.setRequestAttribute("list_"+name, listData);
	   	
		MappingBean mappingBean = httpUtil.getMappingBean();
		
		String path = mappingBean.getJspRootPath();
		if(!path.endsWith("/")){
			path = path + "/";
		}
		httpUtil.setDynamicUrl(path + mappingBean.getAlias()+"_list.jsp");
	}

	/**
	 * @return
	 */
	public RequestTableBean getTableBean(ActionUtil httpUtil, String tableName) {
		return TableFormCriteriaUtil.buildRequestTableBean(tableName, httpUtil.getRequest());
	}

	public ResourceBean getResourceBean(){
		return this.resourceBean;
	}
	
	public void manageDeleteImage(Long elementId, String path) {
		GenericJpaService.mergeDataFile(elementId, path, null, null, null, null);
	}
	
	//----------------------------- COM AND PRINT----------------------------------------//
	public void forwardToPriterJsp(ActionUtil httpUtil) {
		httpUtil.setDynamicUrl("/commun/print-local.jsp");
	}
	public void sendComData(ActionUtil httpUtil, String data) {
		httpUtil.setRequestAttribute("SCREEN_COM", data);
	}
	public boolean printData(ActionUtil httpUtil, PrintPosBean printBean) {
		return printData(httpUtil, printBean, false);
	}
	public boolean printData(ActionUtil httpUtil, List<PrintPosBean> listPrintBean) {
		boolean isAsync = false;
		if(httpUtil.getUserAttribute("IS_EMBDED_MOBILE_PRINTER") != null 
				|| !StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CAISSE_PRINT"))){
			httpUtil.setRequestAttribute("PRINT_POS_LIST", listPrintBean);
	        isAsync = true;
		} else {
	    	for(PrintPosBean ppB : listPrintBean){
	    		new PrintCommunUtil(ppB).print();
	    	}
		}
		return isAsync;
	}
	public boolean printData(ActionUtil httpUtil, PrintPosBean printBean, boolean isDash) {
		boolean isAsync = false;
		if(httpUtil.getUserAttribute("IS_EMBDED_MOBILE_PRINTER") != null 
				|| !StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CAISSE_PRINT"))){
			httpUtil.setRequestAttribute("PRINT_POS_BEAN", printBean);
			if(isDash) {
				httpUtil.setRequestAttribute("OPEN_DASH", true);
			}
	        isAsync = true;
		} else {
	    	 new PrintCommunUtil(printBean).print();
	    	 if(isDash) {
	    		 PrintCommunUtil.openCashDrawer(printBean.getPrinters());
	    	 }
		}
		return isAsync;
	}
	public boolean openDash(ActionUtil httpUtil, String printers) {
		boolean isAsync = false;
		if(!StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CAISSE_PRINT"))){
			httpUtil.setRequestAttribute("OPEN_DASH_ONLY", printers);
			isAsync = true;
		} else {
			PrintCommunUtil.openCashDrawer(printers);	
		}
		return isAsync;
	}
	//----------------------------------------------------------------------------//
	
	public void managePieceJointe(ActionUtil httpUtil, Long elementId, String path) {
		managePieceJointe(httpUtil, elementId, path, null, null);
	}
	
	/**
	 * @param httpUtil
	 */
	public void managePieceJointe(ActionUtil httpUtil, Long elementId, String path, Integer width, Integer height) {
		Map<String, byte[]> images = new HashMap<>();
		Map<String, Object> mapPhotos = httpUtil.getValuesByStartNameNatif("photo");
		if(mapPhotos != null) {
			for (String key : mapPhotos.keySet()) {
				if(key.indexOf("_") == -1) {
					continue;
				}
				String idx = key.substring(0, key.indexOf("_"));
				
				String imageName = httpUtil.getRequest().getParameter("photo"+idx+"_name");
				if(StringUtil.isNotEmpty(imageName)) {
					FileInputInfos fileInfos = UploadFileUtil.getFileInfos(httpUtil.getRequest(), EncryptionUtil.encrypt("photo"+idx), path);
					
					if(fileInfos != null){
						try {
							images.put(imageName, IOUtils.toByteArray(fileInfos.getFileContent()));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
 				}
			}
		}
		
		Map<String, String> mapFilesUnloaded = (Map<String, String>) httpUtil.getMenuAttribute("CAISSE_UNLOADED_DOCUMENTS");
		GenericJpaService.mergeDataFile(elementId, path, images, mapFilesUnloaded, width, height);
	}
	
	/* ----------------------------- DATA FORM ----------------------------------------*/
	public void manageDataForm(ActionUtil httpUtil, String groupe){
		String elemen = ReflectUtil.getStringPropertieValue(httpUtil.getViewBean(), "id");
		Long elmntId = (elemen == null ? null : Long.valueOf(elemen));
		IGenericJpaService familleService = (IGenericJpaService)ServiceUtil.getBusinessBean("familleService");
		
		// Cas perte action si rediretion
		String action = EncryptionUtil.decrypt(httpUtil.getRequest().getParameter(ProjectConstante.WORK_FORM_ACTION));
		if(action != null && action.indexOf(".") != -1) {
			action = StringUtil.getArrayFromStringDelim(action, ".")[2];
		} else {
			action = httpUtil.getAction();
		}
		
		//
		if(ActionConstante.EDIT.equals(action) || ActionConstante.INIT_UPDATE.equals(action) || ActionConstante.INIT_CREATE.equals(action)){
			httpUtil.setRequestAttribute("listDataValue", familleService.loadDataForm(elmntId, groupe));
			Map<String, List<ValTypeEnumPersistant>> mapEnum = familleService.loadDataENumForm(groupe);
			for(String key : mapEnum.keySet()){
				httpUtil.setRequestAttribute(key, mapEnum.get(key));
			}
			
		} else if(ActionConstante.UPDATE.equals(action) || ActionConstante.CREATE.equals(action) || ActionConstante.MERGE.equals(action)){
			List<DataValuesPersistant> listDataValues = (List<DataValuesPersistant>) httpUtil.buildListBeanFromMap("data_value", 
					DataValuesPersistant.class,
					true,
					"eaiid", "data_value");
			familleService.deleteDataForm(elmntId, groupe);
			familleService.mergeDataForm(listDataValues, elmntId, groupe);
		} else if(ActionConstante.DELETE.equals(action)){
			familleService.deleteDataForm(elmntId, groupe);
		}
	}
	/* --------------------------------------------------------------------------------*/
	
	public String getFilterStateRequest(ActionUtil httpUtil, String filed){
		String[] ctrlAction = ControllerUtil.getCtrlActionArray(httpUtil.getRequest(), null);
		String name = ctrlAction[1]; 
		if(name.indexOf(".") != -1){
			name = name.substring(name.indexOf(".")+1);
		}
		return getFilterStateRequest(httpUtil, filed, "list_"+name);
	}
	public String getFilterStateRequest(ActionUtil httpUtil, String field, String tableName){
		String state = httpUtil.getParameter(tableName+"_pager.flt_sub");
		httpUtil.setRequestAttribute("currFilter", state);
		
		if(StringUtil.isNotEmpty(state)){
			if(state.equals("E")){
				return " and ("+ field +" is null or "+ field +"=0) ";
			} else if(state.equals("D")){
				return " and ("+ field +" is not null and "+ field +"=1) ";
			} else if(state.equals("A")){
				return "";
			}
		}
		return " and ("+ field +" is null or "+ field +"=0) ";
	}
	
	public Object checkInstanceDate(ActionUtil httpUtil, Object value) {
		Date currentDate = null;
		if(StringUtil.isEmpty(value)) {
			return currentDate;
		}
		if(value instanceof Date) {
			currentDate = (Date)value;
		} else if((""+value).length() == 10){
			currentDate = DateUtil.stringToDate((String) value, "dd/MM/yyyy");	
		} else {
			currentDate = DateUtil.stringToDate((String) value, "MM/yyyy");				
		}
		return currentDate;
	}
	
	/**
	 * @param httpUtil
	 */
	public void save_order(ActionUtil httpUtil){
		String tableName = httpUtil.getParameter("tbl"); 
		String[] orderArray = httpUtil.getRowsOrder(tableName);
		
		if(orderArray == null){
			httpUtil.writeResponse("MSG_CUSTOM:Aucun élément à ordonner.");
			return;
		}
		
		for (int i=0; i<orderArray.length; i++) {
			orderArray[i] = orderArray[i];
		}
		IGenericJpaService genericService = (IGenericJpaService) httpUtil.getRequest().getAttribute("work_generic_service");
		if(orderArray != null){ 
			genericService.updateRowsOrder(orderArray);
		}
		httpUtil.writeResponse("MSG_CUSTOM:L'ordre est mise à jour.");
	}

}
