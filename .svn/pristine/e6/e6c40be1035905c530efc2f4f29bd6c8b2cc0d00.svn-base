/**
 *
 */
package framework.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import framework.component.complex.table.RequestTableBean;
import framework.controller.bean.MappingBean;
import framework.controller.bean.RequestFormBean;
import framework.controller.bean.action.IViewBean;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.NumericUtil;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StringUtil;
import framework.model.service.IGenericJpaService;
import framework.model.util.FileUtil;

/**
 * @author 
 *
 */
class HttpUtil {
	private final static Logger LOGGER = Logger.getLogger(ActionUtil.class);
	private HttpServletRequest request;
	private HttpServletResponse response;
	private IViewBean viewBean;
	private String dynamicUrl;
	private MappingBean mappingBean;
	
	public MappingBean getMappingBean() {
		return mappingBean;
	}

	public void setMappingBean(MappingBean mappingBean) {
		this.mappingBean = mappingBean;
	}
	
	/**
	 * @return the viewBean
	 */
	public IViewBean getViewBean() {
		return viewBean;
	}

	/**
	 * @param viewBean
	 *            the viewBean to set
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setViewBean(IViewBean viewBean) {
		Long workId = ServiceUtil.getIdPersistantValue(viewBean);
		if(workId != null){
			((Map)getRequestAttribute(ProjectConstante.WORK_PARAMS)).put(ProjectConstante.WORK_ID, workId.toString());
		}
		this.viewBean = viewBean;
	}

	/**
	 * @param key
	 * @param value
	 */
	public void setRequestAttribute(String key, Object value) {
		request.setAttribute(key, value);
	}

	/** Request and session attributes */
	public void setMenuAttribute(String key, Object value) {
		ControllerUtil.setMenuAttribute(key, value, this.request);
	}
	
	/**
	 * @param key
	 */
	public void removeMenuAttribute(String key) {
		ControllerUtil.removeMenuAttribute(key, this.request);
	}

	/**
	 * @param key
	 * @return
	 */
	public Object getMenuAttribute(String key) {
		return ControllerUtil.getMenuAttribute(key, this.request);
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setUserAttribute(String key, Object value) {
		ControllerUtil.setUserAttribute(key, value, request);
	}
	/**
	 * @param key
	 */
	public void removeUserAttribute(String key) {
		ControllerUtil.removeUserAttribute(key, this.request);
	}

	/**
	 * @param request
	 *            the request to set
	 */
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * @param response
	 *            the response to set
	 */
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	/**
	 * @param key
	 * @return
	 */
	public Object getUserAttribute(String key) {
		return ControllerUtil.getUserAttribute(key, request);
	}

	/**
	 * @param key
	 * @return
	 */
	public Object getRequestAttribute(String key) {
		return this.request.getAttribute(key);
	}

	/** End ---- Request and session attributes --- */

	/**
	 * @return
	 */
	public HttpServletResponse getResponse() {
		return this.response;
	}

	/**
	 * @return
	 */
	public HttpServletRequest getRequest() {
		return this.request;
	}

	/**
	 * @return
	 */
	public RequestTableBean getTableBean(String tableName) {
		return TableFormCriteriaUtil.buildRequestTableBean(tableName, request);
	}

	/**
	 * @return
	 */
	public RequestFormBean getCompletionBean(){
		return TableFormCriteriaUtil.getRequestFormBean(request);
	}

	/**
	 * @return
	 */
	public boolean isError(){
		return MessageService.isError();
	}

	/** Util methodes */
	public int getIntParameter(String attributeName){
		return NumericUtil.getIntOrDefault(ControllerUtil.getParam(request, attributeName));
	}

	/** Util methodes */
	public String getParameter(String attributeName){
		return StringUtil.getValueOrNull(ControllerUtil.getParam(request, attributeName));
	}

	/** Util methodes */
	public Long getLongParameter(String attributeName){
		return NumericUtil.toLong(ControllerUtil.getParam(request, attributeName));
	}

	//-------------------------- Liste ids checked in table -------------------------------
	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getWorkId(){
		Map<String, Object> params = (Map)request.getAttribute(ProjectConstante.WORK_PARAMS);
		return (String)ControllerUtil.getPersistantValueId(params);
	}

	/**
	 * @return
	 */
	public Long getWorkIdLong(){
		return NumericUtil.toLong(getWorkId());
	}

	/**
	 * @param tableName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Long[] getCheckedElementsLong(String tableName){
		Map<String, Object> params = (Map)request.getAttribute(ProjectConstante.WORK_PARAMS);
		return ControllerUtil.getCheckedElementsLong(tableName, params);
	}

	/**
	 * @param data
	 * @param modeExlude : Exclude fields without expose annotation
	 */
	public void sendJSonData(Object data, boolean modeExlude){
		Gson gson = null;
		if(modeExlude){
			gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();	
		} else{
			gson = new GsonBuilder().create();
		}
		
		String json = gson.toJson(data);
		writeResponse(json);
	}

	/**
	 * @param data
	 */
	public void writeResponse(String data){
		setDynamicUrl("MSG_RES");
		try {
			response.getWriter().write(data);
		} catch (IOException e) {
			LOGGER.error("Erreur : ", e);
		}
	}

	/**
	 * @return
	 */
	public boolean isFirstShow(){
		Long id = getWorkIdLong();
		String action = ControllerUtil.getAction(request);

		return (
				(id != null)
				&& (
						(action != null)
						&& action.startsWith("work_init_")
					)
				&& !MessageService.isError()
				);
	}

	/**
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
//	@SuppressWarnings("unchecked")
//	public List<RowBean> getRowsAdded(Class<? extends IViewBean> bean) throws Exception{
//		Map<String, Object> params = (Map)request.getAttribute(ProjectConstante.WORK_PARAMS);
//		return RowsUtil.getRowsAdded(params, bean);
//	}
//
//	/**
//	 * @param tableName
//	 * @return
//	 * @throws Exception
//	 */
//	@SuppressWarnings("unchecked")
//	public List<RowBean> getRowsAdded() throws Exception{
//		Map<String, Object> params = (Map)request.getAttribute(ProjectConstante.WORK_PARAMS);
//		return RowsUtil.getRowsAdded(params, null);
//	}

	/**
	 * @param restrict
	 * @return
	 * @throws Exception
	 */
//	@SuppressWarnings("unchecked")
//	public List<RowBean> getRowsUpdated(boolean restrict) throws Exception{
//		Map<String, Object> params = (Map)request.getAttribute(ProjectConstante.WORK_PARAMS);
//		return RowsUtil.getRowsChangedRestrict(params, null, restrict);
//	}
//
//	/**
//	 * @param bean
//	 * @param restrict
//	 * @return
//	 * @throws Exception
//	 */
//	@SuppressWarnings("unchecked")
//	public List<RowBean> getRowsUpdated(Class<? extends IViewBean> bean, boolean restrict) throws Exception{
//		Map<String, Object> params = (Map)request.getAttribute(ProjectConstante.WORK_PARAMS);
//		return RowsUtil.getRowsChangedRestrict(params, bean, restrict);
//	}
//
//	/**
//	 * @return
//	 * @throws Exception
//	 */
//	@SuppressWarnings("unchecked")
//	public Map<String, List<String>> getUpdatedFieldsName() throws Exception{
//		Map<String, Object> params = (Map)request.getAttribute(ProjectConstante.WORK_PARAMS);
//		return RowsUtil.getUpdatedFieldsName(params);
//	}

//	/**
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public List<String> getGroupModifiedFields(){
//		Map<String, Object> params = (Map)request.getAttribute(ProjectConstante.WORK_PARAMS);
//		return RowsUtil.getGroupModifiedFields(params);
//	}

	/**
	 * @return
	 */
	public boolean isSubmit(){
		return ControllerUtil.isWorkFormSubmited(request);
	}

	/**
	 * @return
	 */
	public boolean isCrudOperationOK(){
		return ControllerUtil.isCrudOperationOK(request);
	}
	
	/**
	 * @return
	 */
	public boolean isCrudOperation(){
		return ControllerUtil.isCrudOperation(request);
	}
	
	/**
	 * @return
	 */
	public String getDynamicUrl() {
		return dynamicUrl;
	}

	/**
	 * @param dynamicUrl
	 */
	public void setDynamicUrl(String dynamicUrl) {
		this.dynamicUrl = dynamicUrl;
	}
	
	/**
	 * @param startName
	 * @return
	 */
	public Map<String, Object> getValuesByStartNameNatif(String startName){
		Map<String, Object> mapValues = new HashMap<String, Object>(); 
		//
		for(String key : getRequest().getParameterMap().keySet()){
			if(key.startsWith(startName)){
				String id = key.substring(key.indexOf(startName)+startName.length());
				mapValues.put(id, getRequest().getParameter(key));
			}
		}
		
		if(mapValues.size() == 0){
			mapValues = null;
		}
		
		return mapValues;
	}
	
	/**
	 * @param startName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getValuesByStartName(String startName){
		Map<String, Object> params = (Map)request.getAttribute(ProjectConstante.WORK_PARAMS);
		Map<String, Object> mapValues = new HashMap<String, Object>(); 
		//
		for(String key : params.keySet()){
			if(key.startsWith(startName)){
				String id = key.substring(key.indexOf(startName)+startName.length());
				mapValues.put(id, params.get(key));
			}
		}
		
		if(mapValues.size() == 0){
			mapValues = null;
		}
		
		return mapValues;
	}
	
	public List<?> buildListBeanFromMap(String keyField, String requiredField, Class<?> beanClass, String ... fieldNames){
		return buildListBeanFromMap(keyField, requiredField, null, beanClass, false, fieldNames);
	}
	
	public List<?> buildListBeanFromMap(String requiredField, Class<?> beanClass, boolean isFromRequest, String ... fieldNames){
		return buildListBeanFromMap("eaiid", requiredField, null, beanClass, isFromRequest, fieldNames);
	}
	public List<?> buildListBeanFromMap(String requiredField, Class<?> beanClass, String ... fieldNames){
		return buildListBeanFromMap("eaiid", requiredField, beanClass, fieldNames);
	}
	
	/**
	 * @param startName
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<?> buildListBeanFromMap(String keyField, String requiredField, String alias, Class<?> beanClass, boolean isFromRequest, String ... fieldNames){
		Map<String, Object> paramsReq = isFromRequest ? (Map) request.getParameterMap() : (Map)request.getAttribute(ProjectConstante.WORK_PARAMS);
		List listBean = new ArrayList();
		IGenericJpaService fs = (IGenericJpaService) ServiceUtil.getBusinessBean("familleService");
		
		if(paramsReq == null) {
			return listBean;
		}
		
		Map mapDb = new HashMap<>();
		if(fieldNames != null && fieldNames.length > 0){
			for(String key : paramsReq.keySet()){
				for (String fieldName2 : fieldNames) {
					if(keyField != null && keyField.equals(fieldName2)){
						String id = key.substring(key.lastIndexOf("_")+1);
						if(id.equals("0") || !NumericUtil.isInt(id)){
							continue;
						}
						Object value = isFromRequest ? request.getParameter(fieldName2+"_"+id) : paramsReq.get(fieldName2+"_"+id);
						if(StringUtil.isNotEmpty(value)){
							if(!NumericUtil.isNum(value)){
								value = Long.valueOf(EncryptionUtil.decrypt(value.toString()));	
							} else if(!(value instanceof Long)) {
								value = Long.valueOf(""+value);
							}
							fieldName2 = "id";
							Object dbVal = fs.getEntityManager().find(beanClass, value);
							if(dbVal != null){
								mapDb.put(id, dbVal);	
							}
						}
					}
				}
			}
			
			
			for(String key : paramsReq.keySet()){
				if(key.startsWith(requiredField+"_") && StringUtil.isNotEmpty(isFromRequest ? request.getParameter(key) : paramsReq.get(key))){
					String id = key.substring(key.lastIndexOf("_")+1);
					if(id.equals("0") || !NumericUtil.isInt(id)){
						continue;
					}
					
					Object bean = mapDb.get(id);
					if(bean == null){
						try { bean = beanClass.newInstance();} catch (Exception e){}
					}
					//
					for (String fieldName2 : fieldNames) {
						Object value = isFromRequest ? request.getParameter(fieldName2+"_"+id) : paramsReq.get(fieldName2+"_"+id);
						fieldName2 = (alias == null ? fieldName2 : fieldName2.replaceAll(alias+".", ""));
						
						boolean isConverted = false;
						
						// S'il s'agit d'un entitie liée
						if(fieldName2.indexOf(".") != -1){
							String[] fiedsArray = StringUtil.getArrayFromStringDelim(fieldName2, ".");
							
							if(StringUtil.isNotEmpty(value)){
								Method methodeOpc = ReflectUtil.getMethod(beanClass, "get"+StringUtil.firstCharToUpperCase(fiedsArray[0]), null);
								if(methodeOpc == null) {
									continue;
								}
								Class<?> returnType = methodeOpc.getReturnType();
								Object returnOpCMethode = null;
								try {
									returnOpCMethode = returnType.newInstance();
									if(StringUtil.isNotEmpty(value) && NumericUtil.isNum(value)){
										ReflectUtil.setProperty(returnOpCMethode, fiedsArray[1], Long.valueOf(""+value));
									}
									ReflectUtil.setProperty(bean, fiedsArray[0], returnOpCMethode);
								} catch (InstantiationException | IllegalAccessException e) {
									e.printStackTrace();
								}
							} else{
								ReflectUtil.setProperty(bean, fiedsArray[0], null);
							}
						} else{
							if(keyField != null && keyField.equals(fieldName2)){
								if(StringUtil.isNotEmpty(value)){
									if(!NumericUtil.isNum(value)){
										value = Long.valueOf(EncryptionUtil.decrypt(value.toString()));	
									} else if(!(value instanceof Long)) {
										value = Long.valueOf(""+value);
									}
									
									fieldName2 = "id";
									Object dbVal = fs.getEntityManager().find(beanClass, value);
									if(dbVal != null){
//										bean = dbVal;	
										isConverted = true;
									} else{
										isConverted = true;
									}
								}
							} else{
								// Convertion du type 
								if(StringUtil.isNotEmpty(value)){
									Method methodeOpc = ReflectUtil.getMethod(beanClass, "get"+StringUtil.firstCharToUpperCase(fieldName2), null);
									Class<?> returnType = methodeOpc.getReturnType();

									if(returnType.equals(Date.class) && !(value instanceof Date)){
										value = DateUtil.stringToDate(""+value);
										isConverted = true;
									} else if(returnType.equals(BigDecimal.class) && !(value instanceof BigDecimal)){
										value = BigDecimalUtil.get(""+value);
										isConverted = true;
									} else if((returnType.equals(Integer.class) || returnType.equals(int.class)) && !(value instanceof Integer)){
										value = Integer.valueOf(NumericUtil.replaceBlank(""+value));
										isConverted = true;
									} else if((returnType.equals(Long.class) || returnType.equals(long.class)) && !(value instanceof Long)){
										value = Long.valueOf(NumericUtil.replaceBlank(""+value));
										isConverted = true;
									} else if(returnType.equals(Boolean.class) || returnType.equals(boolean.class)){
										value = StringUtil.isTrue(""+value);
										isConverted = true;
									}
									if(value instanceof Date || value instanceof BigDecimal || value instanceof Integer || value instanceof Long) {
										isConverted = true;
									}
								}
							}
							//
							if(StringUtil.isNotEmpty(value)){
								ReflectUtil.setProperty(bean, fieldName2, (isConverted ? value : ""+value));
							} else{
								ReflectUtil.setProperty(bean, fieldName2, null);
							}
						}
					}
					ReflectUtil.setProperty(bean, "idxIhm", Integer.valueOf(id));
					listBean.add(bean);
				}
			}
		}

		return listBean;
	}
	
//	/**
//	 * @param file
//	 */
//	public void doDownload(File file, boolean deleteOnEnd){
//		try {
//			// Flag for stop redirection after action call
//			request.setAttribute("IS_EXPORT_MODE", "true");
//			ExportUtil.doDownload(request, response, file);
//			if(deleteOnEnd){
//				file.delete();				
//			}
//		} catch (IOException e) {
//			LOGGER.error("Erreur : ", e);
//		}
//	}
//	
	/**
	 * @param httpUtil
	 * @param fileName
	 * @param bbuf
	 */
	public void doDownload(String fileName, byte[] bbuf){
		ControllerUtil.doDownload(getRequest(), getResponse(), fileName, bbuf, false);
	}
	public void doDownloadModeView(String fileName, byte[] bbuf){
		ControllerUtil.doDownload(getRequest(), getResponse(), fileName, bbuf, true);
	}
	
	public void manageInputFileView(String shortPath){
		String elementIdstr = this.getParameter("pj");
		Long elementId = null;
		
		if(NumericUtil.isNum(elementIdstr)) {
			 elementId = Long.valueOf(elementIdstr);
		} else {
			elementId = Long.valueOf(EncryptionUtil.decrypt(elementIdstr));	
		}
		
		String fileName = this.getParameter("nm");
		boolean isDownLoad = StringUtil.isTrue(this.getParameter("isdown"));
		String startChemin = ContextGloabalAppli.getEtablissementBean().getId().toString()+"/"+shortPath;
		
		byte[] fileToDownLoad = FileUtil.getFileToDownLoad(startChemin, elementId, fileName);
		if(isDownLoad){
			this.doDownload(fileName, fileToDownLoad);
		} else{
			if(fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg") || fileName.toLowerCase().endsWith(".gif")|| fileName.toLowerCase().endsWith(".png")){
				InputStream in = new ByteArrayInputStream(fileToDownLoad);
				try{
					BufferedImage buf = ImageIO.read(in);
			        this.setRequestAttribute("width_viewer", buf.getWidth());
			        this.setRequestAttribute("height_viewer", buf.getHeight());
				} catch(Exception e){
					e.printStackTrace();
				}
				this.setRequestAttribute("img_viewer", FileUtil.getByte64(fileToDownLoad));
				this.setDynamicUrl("/commun/image_viewer.jsp");
			} else{
				this.doDownloadModeView(fileName, fileToDownLoad);
			}
		}
	}
	
	/**
	 * @param httpUtil
	 * @param fileName
	 * @param file
	 */
	public void doDownload(File file, boolean deleteOnEnd){
		ControllerUtil.doDownload(getRequest(), getResponse(), file);
		// If temporary file delete
		if(deleteOnEnd){
			file.delete();				
		}
	}
}