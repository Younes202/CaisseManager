package framework.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import appli.controller.domaine.util_erp.ContextAppli;
import framework.controller.bean.MappingBean;
import framework.controller.bean.WorkCallBean;
import framework.controller.bean.action.ActionBean;
import framework.controller.bean.action.IViewBean;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.common.GlobalModelThread;
import framework.model.common.constante.ActionConstante;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.constante.ProjectConstante.TYPE_DATA_ENUM;
import framework.model.common.exception.ActionValidationException;
import framework.model.common.exception.BeanValidationException;
import framework.model.common.exception.ControllerException;
import framework.model.common.exception.ControllerNotFoundException;
import framework.model.common.service.CommunValidatorService;
import framework.model.common.service.IhmMappingService;
import framework.model.common.service.MessageService;
import framework.model.common.service.TypeValidatorService;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.ControllerBeanUtil;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;
import framework.model.service.IGenericJpaService;

public class ControllerBase {
	private final static Logger LOGGER = Logger.getLogger(ControllerBase.class);
	private static final Class<?>[] httpUtilType = new Class[]{ActionUtil.class};

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	public static String service(HttpServletRequest request, HttpServletResponse response, String ctrlAct, ServletContext context, boolean isErrorBack) throws Exception {
		String controller = null, action = null;
		IViewBean viewBean = null;
		ActionUtil actionUtil = null;
		// Put controller and action in request
		String[] ctrlAction = ControllerUtil.getCtrlActionArray(request, ctrlAct);
		controller = ctrlAction[1];
		action = ctrlAction[2];
		// Post controller alias and current action
		request.setAttribute(ProjectConstante.WORK_CONTROLLER, controller);
		request.setAttribute(ProjectConstante.WORK_ACTION, action);

		// Load mapping bean
		MappingBean mappingBean = IhmMappingService.getMappingBean(controller);
		if ((mappingBean == null)) {
			return null;
		}

		Object controllerClass = ServiceUtil.getBusinessBean(mappingBean.getController());
			
		boolean isValid = true, toValidate = false;
		boolean isBodyTableAction 	= ControllerUtil.isBodyTableAction(request);
		boolean isAjaxBackJob 		= ControllerUtil.isAjaxBackJob(request);

		// Action bean
		ActionBean actionBean = IhmMappingService.getActionBean(mappingBean, action);
		
		// Build form messages validators, formatters and developper message
		if(ControllerUtil.isWorkFormSubmited(request)
				&& request.getParameter(ProjectConstante.IS_AJAX_PARTIAL_INJECT) == null // partial ajax
				&& !isAjaxBackJob 
				&& !TableFormCriteriaUtil.isExporMode(request) 
				&& !isBodyTableAction
				&& (
						(actionBean != null) && BooleanUtil.isTrue(actionBean.isUseFormValidator())
					)
				&& (request.getAttribute(ProjectConstante.IS_FORWARD_ACTION) == null))	{
			toValidate = true;
		}
		// Params
		Map params = (Map)request.getAttribute(ProjectConstante.WORK_PARAMS);

		// Convert and validate types
		if(actionBean!=null && actionBean.isUseBean()!= null && actionBean.isUseBean().booleanValue()){
			isValid = updateDataTypes(request);
		}

		// Load bean view
		Class<? extends IViewBean> ihmBean = mappingBean.getBean();
		// Use forward bean
		if((actionBean != null) && (actionBean.getBean() != null)){
			ihmBean = actionBean.getBean();
		}

		//
		if(toValidate){
			isValid = validateFomulare(request);
		}

		//
		if(!isBodyTableAction && (actionBean != null) && BooleanUtil.isTrue(actionBean.isUseBean())
				&& (!MessageService.isError() || isErrorBack) && !StringUtil.isEmpty(action)) {
			// Convert to bean if data types is valid
			if(isValid){
				viewBean = (IViewBean) ControllerBeanUtil.mapToBean(ihmBean, params); 
			}
		}
		// Build Http util for methodes call
		actionUtil = buildActionUtil(request, response, viewBean, mappingBean);
		
		// Intanciate declared services
		Object defaultService = injectDecalaredServices(controllerClass);
		request.setAttribute("work_generic_service", defaultService);

		// Set bean and service
		if(ihmBean != null){
			ResourceBean resourceBean = new ResourceBean((IGenericJpaService) defaultService, ihmBean);
			ReflectUtil.invokeField(controllerClass, "resourceBean", resourceBean);
		}

		WorkCallBean returnParams = null;
		
		// Call "init" action if it exists in controller in all cases
		if(!StringUtil.isTrue(ControllerUtil.getParam(request, "skipI"))){
			returnParams = workMethodeCall(actionUtil, controllerClass, ActionConstante.INIT, viewBean, params);
			viewBean = returnParams.getViewBean();
		}

		// Call real action
		if(isValid || isErrorBack){
			// Call real action like create, update ...
			if (StringUtil.isNotEmpty(action)  && !ActionConstante.INIT.equals(action) && !ActionConstante.POST.equals(action)) {
				// Call target if data of formular are valide
				if (!MessageService.isError() || isErrorBack) {
					returnParams = workMethodeCall(actionUtil, controllerClass, action, viewBean, params);
					viewBean = returnParams.getViewBean();
					//
					if(!returnParams.isCalled()){
						String message = StrimUtil.label("work.methode.error", controller + "." + action);
						throw new ControllerException(message);
					}
				}
			}
		}

		// Call "post" action if it exists in controller in all cases
		if(!StringUtil.isTrue(ControllerUtil.getParam(request, "skipP"))){
			returnParams = workMethodeCall(actionUtil, controllerClass, ActionConstante.POST, viewBean, params);
			viewBean = returnParams.getViewBean();
		}
		
		// Purger les fichier supprimés des uploads files
		Map<String, String> mapFilesUnloaded = (Map<String, String>) ControllerUtil.getMenuAttribute("CAISSE_UNLOADED_DOCUMENTS", request);
		if(mapFilesUnloaded != null){
			mapFilesUnloaded.clear();
		}

		// Add back params for restaure finding params ------------------------------------------------------------------------------
//		if(ControllerUtil.isCrudOperationOK(request) || action.equals("work_delete") ||  action.equals("work_delete_rows")){
//			FrontFilter.restaureBackParams(request, ControllerUtil.getCtrlAction(request), false);
//		}
		//--------------------------------------------------------------------------------------------------
		
		// Add beanView in request for get value from jsp by {$myalias.attribute}
		if(viewBean != null){
			String nameBean = ControllerUtil.getBeanAlias(viewBean);
			request.setAttribute(nameBean, viewBean);
			request.setAttribute("work_bean_name", nameBean);
		}
		
		//-------------------------------------------
		 // Throw exception if message is exist
		 if(MessageService.isError() || !isValid){
			 GlobalModelThread.getGlobalList().clear();
			 throw new ActionValidationException("Error in bean data !");
		 }
		//-------------------------------------------
//		 if(StringUtil.isTrue(""+request.getAttribute("IS_FROM_MOBILE"))) {
//			 return null;
//		 }
		 
		 String jspPath = null;
		// Get jsp or servlet path from mapping config
		if(actionUtil != null){
			jspPath = actionUtil.getDynamicUrl();
		}
		if(StringUtil.isEmpty(jspPath) && StringUtil.isEmpty(params.get(ProjectConstante.IS_AJAX_BACK_JOB_CALL)) 
				&& StringUtil.isEmpty(params.get(ProjectConstante.IS_AJAX_PARTIAL_INJECT))
				&& !StringUtil.isTrue(""+request.getAttribute("IS_EXPORT_MODE"))){
			String message = StrimUtil.label("work.maping.error", controller);
			throw new ControllerNotFoundException(message);
		}
	
		return jspPath;
	}

	/**
	 * Instaciate decalared services in action (Injection of dependencies)
	 * @param controllerClass
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 */
	private static Object injectDecalaredServices(Object controllerClass) throws Exception {
		List<Field> fields = ReflectUtil.getListFieldByAnnotation(controllerClass.getClass(), Inject.class);
		Object defaultService = null;
		if(fields != null && fields.size() > 0){
			defaultService = ReflectUtil.getObjectPropertieValue(controllerClass, fields.get(0).getName());
		}
			
		if(defaultService == null){
			// Get default service from controller name
			String controllerNameEnd = StrimUtil.getGlobalConfigPropertie("controller.end");
			String serviceNameEnd = StrimUtil.getGlobalConfigPropertie("service.end");
			String controllerName = controllerClass.getClass().getSimpleName();
			String serviceName = controllerName.substring(0, controllerName.indexOf(controllerNameEnd)) + serviceNameEnd;
			
			// Get bean
			defaultService = ServiceUtil.getBusinessBean(serviceName);
		}
		
		return defaultService;
	}

	/**
	 * @param controllerClass
	 * @param methodeName
	 * @param httpUtil
	 * @param httpUtilType
	 * @param viewBean
	 * @throws ServletException
	 * @throws IOException
	 */
	private static WorkCallBean workMethodeCall(Object httpGuiClass, Object controllerClass, String methodeName, IViewBean viewBean, Map<String, ?> params) throws Exception {
		WorkCallBean returnParams = new WorkCallBean();
		returnParams.setViewBean(viewBean);
		// Test if methode is exist
		if (ReflectUtil.isMethodExist(controllerClass, methodeName, httpUtilType)) {
			try {
				ReflectUtil.invokeMethode(controllerClass, methodeName, new Object[]{httpGuiClass}, httpUtilType);
				viewBean = ((ActionUtil) httpGuiClass).getViewBean();
				returnParams.setViewBean(viewBean);
				returnParams.setCalled(true);
			} catch (Exception e) {
				String message = StrimUtil.label("work.controller.error", methodeName, controllerClass.getClass().getName());
				if(e.getCause().getClass().equals(ActionValidationException.class) || e.getCause().getClass().equals(BeanValidationException.class)){
					throw new ActionValidationException(e);
				} else{
					EtablissementPersistant etablissementBean = ContextAppli.getEtablissementBean();
					String url = StrimUtil.getGlobalConfigPropertie("caisse.cloud.url");
					if(etablissementBean != null && StringUtil.isNotEmpty(url)) {
						url = url + "/update";
						String soft = StrimUtil.getGlobalConfigPropertie("context.soft");
						String sujet = "BUG "+soft+"==>"+(etablissementBean != null ? etablissementBean.getNom() : "???") + " :: "+e.getCause().toString();
						String finalUrl = url += "?mt=bug&ets=" + etablissementBean.getNom()+"&sujet="+sujet;
						StringWriter sw = new StringWriter();
						PrintWriter pw = new PrintWriter(sw);
						e.printStackTrace(pw);
						
						new Thread(() -> {
							ControllerUtil.sendJsonPOST(sw.toString(), finalUrl);
						}).start();
					}	
				}
				throw new ControllerException(message, e);
			}
		}

		return returnParams;
	}

	/**
	 * @param request
	 * @param response
	 * @return
	 */
	private static ActionUtil buildActionUtil(HttpServletRequest request, HttpServletResponse response, Object viewBean, MappingBean mappingBean) {
		Object httpGuiClass = null;
		try {
			httpGuiClass = ActionUtil.class.newInstance();
			ReflectUtil.invokeMethode(httpGuiClass, "setRequest", new Object[] { request }, new Class[] { HttpServletRequest.class });
			ReflectUtil.invokeMethode(httpGuiClass, "setResponse", new Object[] { response },
					new Class[] { HttpServletResponse.class, });
			//
			if (viewBean != null) {
				ReflectUtil.invokeMethode(httpGuiClass, "setViewBean", new Object[] { viewBean }, new Class[] { IViewBean.class });
			}
			ReflectUtil.invokeMethode(httpGuiClass, "setMappingBean", new Object[] { mappingBean }, new Class[] { MappingBean.class });
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		return (ActionUtil)httpGuiClass;
	}

	/**
	 * Upadate type type by converting to good java type
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private static boolean updateDataTypes(HttpServletRequest request) throws Exception{
		Map<String, Map<String, String>> mapValidators = ControllerUtil.getMapValidator(request);
		Map<String, Object> params = (Map)request.getAttribute(ProjectConstante.WORK_PARAMS);
		//
		if((mapValidators != null) && (mapValidators.size() > 0)){
			for(String fieldName : mapValidators.keySet()){
				Map<String, String> mapAnalyser = mapValidators.get(fieldName);
				if(mapAnalyser == null) {
					continue;
				}
				String type = mapAnalyser.get(ProjectConstante.TYPE);
				if(type == null) {
					continue;
				}
				boolean isArray = type.equals(TYPE_DATA_ENUM.STRING_ARRAY.getType()) 
							|| type.equals(TYPE_DATA_ENUM.LONG_ARRAY.getType())
							|| type.equals(ProjectConstante.TYPE_DATA_ENUM.DECIMAL_ARRAY.getType());
				Object fieldValue = isArray ? request.getParameterValues(fieldName) : ""+params.get(fieldName);
				// Type
				Object retunedValue = null;
				if (!MessageService.isLastFieldError(fieldName)) {
					retunedValue = TypeValidatorService.validateType(TYPE_DATA_ENUM.getTypeData(type), fieldName, fieldValue);
					//
					params.put(fieldName, retunedValue);
				}
			}
		}

		return !MessageService.isError();
	}

	/**
	 * Important : Data are converted during this validation
	 * @param req
	 * @param action
	 * @throws Exception
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private static boolean validateFomulare(HttpServletRequest request) throws Exception {
		Map<String, Map<String, String>> mapValidators = ControllerUtil.getMapValidator(request);

		// Iterate map validators
		if((mapValidators != null) && (mapValidators.size() > 0)){
			// Map of params
			Map<String, Object> params = (Map)request.getAttribute(ProjectConstante.WORK_PARAMS);
			// Get maps analyser
			for(String fieldName : mapValidators.keySet()){
				boolean isToTest = true;
				if(isToTest){
					Map<String, String> mapAnalyser = mapValidators.get(fieldName);
					if(mapAnalyser == null) {
						continue;
					}
					
					Object retunedValue = params.get(fieldName);
					//
					String required = mapAnalyser.get(ProjectConstante.REQUIR);
					String validator = mapAnalyser.get(ProjectConstante.VALID);
					String maximum = mapAnalyser.get(ProjectConstante.MAX);
					String minimum = mapAnalyser.get(ProjectConstante.MIN);
					String minLength = mapAnalyser.get(ProjectConstante.MINLENGTH);
					String maxLength = mapAnalyser.get(ProjectConstante.MAXLENGTH);

					// Required
					if (StringUtil.isTrue(required)) {
						retunedValue = CommunValidatorService.validateField(ProjectConstante.REQUIRED_VALIDATOR, fieldName, retunedValue);
					}
					// Custom Validator
					if (!MessageService.isLastFieldError(fieldName) && (validator != null)) {
						retunedValue = CommunValidatorService.validateField(validator, fieldName, retunedValue);
					}
					// Min et Max
					if (!MessageService.isLastFieldError(fieldName) && ((maximum != null) || (minimum != null))) {
						retunedValue = CommunValidatorService.validateMinMax(maximum, minimum, fieldName, retunedValue);
					}
					// Min et Max length
					if (!MessageService.isLastFieldError(fieldName) && ((minLength != null) || (maxLength != null))) {
						retunedValue = CommunValidatorService.validateMinMaxLength(maxLength, minLength, fieldName, retunedValue);
					}
					// Build return
					params.put(fieldName, retunedValue);
				}

				// Erase value of current page to the old one
				if(MessageService.isError()){
					for(String key : params.keySet()){
						if(key.indexOf("_pager.cp") != -1){
							params.put(key, params.get(key+"_old"));
						}
					}
				}
			}
		}

		return !MessageService.isError();
	}
}
