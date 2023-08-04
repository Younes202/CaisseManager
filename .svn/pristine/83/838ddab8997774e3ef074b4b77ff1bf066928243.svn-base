package framework.model.common.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import framework.controller.annotation.WorkController;
import framework.controller.annotation.WorkForward;
import framework.controller.annotation.WorkValidator;
import framework.controller.bean.MappingBean;
import framework.controller.bean.action.ActionBean;
import framework.controller.bean.action.IViewBean;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.util.MappingUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;

@SuppressWarnings("unchecked")  
public class IhmMappingService {
	private final static Logger LOGGER = Logger.getLogger(IhmMappingService.class);
	
	private static final String BASE_PROJECT = StrimUtil.getGlobalConfigPropertie("project.root.path");
	private final static String GLOBAL_MAPPING_PATH 	= "global-navigation.xml";
	private static List<MappingBean> MAPPING_LIST;

	public static Map<String, Class<?>> VALIDATOR_MAP;
	public static Map<String, Map<String, Map<String, String>>> VALIDATOR_FIELD_MAP;

	static { 
		try {
			// Load acttions from project and framework
			List<Class> listController = MappingUtil.getListClassByAnnotation(WorkController.class, BASE_PROJECT);
			buildActionMapping(listController);
			LOGGER.info(".................. End loading ** project actions ** mapping");
			//listController = MappingUtil.getListClassFromJar(WorkController.class);
			listController = MappingUtil.getListClassByAnnotation(WorkController.class, "/");
			//listController = MappingUtil.getListClassByAnnotation(Controller.class, "/framework");
			buildActionMapping(listController);
			LOGGER.info(".................. End loading ** framework actions ** mapping");
			// Load validators
			buildValidatorMapping();
			LOGGER.info(".................. End loading framework ** validators ** mapping");
			// Load helpers
			LOGGER.info(".................. End loading framework ** helpers ** mapping");
		} catch (Exception e) {
			LOGGER.error("Erreur dans le chargement du mapping --->", e);
			FrameworkMessageService.addTechnicalErrorMessage();
		}
	}

	//* XML files *//
	public final static String getMappingConfigPath(){
	    return MappingUtil.getFullFilePath(BASE_PROJECT, GLOBAL_MAPPING_PATH);
	}

	/**
	 * @throws Exception
	 */
	private static void buildValidatorMapping() throws Exception {
		LOGGER.debug("** Chargements des validateurs ****************");
		VALIDATOR_MAP = new HashMap<String, Class<?>>();
		//
		//List<Class> listValidator = MappingUtil.getListClassFromJar(WorkValidator.class);
		List<Class> listValidator = MappingUtil.getListClassByAnnotation(WorkValidator.class, "/");
		//
		if(listValidator != null){
			for (Class validatorClass : listValidator) {
				WorkValidator validatorAnnot = (WorkValidator) validatorClass.getAnnotation(WorkValidator.class);
				VALIDATOR_MAP.put(validatorAnnot.alias(), validatorClass);
			}
		}
	}

	/**
	 * @param listController
	 * @throws Exception
	 */
	private static void buildActionMapping(List<Class> listController) throws Exception {
		LOGGER.debug("** Chargements des actions ****************");
		// Global mapping
		if(MAPPING_LIST == null){
			MAPPING_LIST = new ArrayList<MappingBean>();
		}
		//
		if(listController != null){
			for (Class controllerClass : listController) {
				WorkController controllerAnnot = (WorkController) controllerClass.getAnnotation(WorkController.class);
				MappingBean mappingBean = new MappingBean();
				// Mapping bean
				String simpleName = controllerClass.getSimpleName();
				String alias = StringUtil.firstCharToLowerCase(simpleName).substring(
								0, simpleName.indexOf(StrimUtil.getGlobalConfigPropertie("controller.end")));
				mappingBean.setAlias(alias);
				mappingBean.setNameSpace(controllerAnnot.nameSpace());
				mappingBean.setJspRootPath(controllerAnnot.jspRootPath());

				Class<? extends IViewBean> bean = (Class<? extends IViewBean>)controllerAnnot.bean();
				if ((bean != null) && !bean.equals(ProjectConstante.DEFAULT.class)) {
					mappingBean.setBean(bean);
				}
				//
				mappingBean.setController(controllerClass);
				// Actions
				List<ActionBean> listActionBean = new ArrayList<ActionBean>();
				Method[] methods = controllerClass.getDeclaredMethods();
				for (Method method : methods) {
					WorkForward forward = method.getAnnotation(WorkForward.class);
					String methodeName = method.getName();
					ActionBean actionBean = null;

					// Forward
					if (forward != null) {
						if(actionBean == null){
							actionBean = new ActionBean();
						}

						actionBean.setAction(methodeName);
						//
						if (forward != null) {
							if(forward.useBean()){
								actionBean.setUseBean(forward.useBean());
							}
							if(!forward.useFormValidator()){
								actionBean.setUseFormValidator(forward.useFormValidator());
							}
							//
							if(!forward.bean().equals(ProjectConstante.DEFAULT.class)){
								actionBean.setBean(forward.bean());
							}
						}
					}

					//
					if(actionBean != null){
						listActionBean.add(actionBean);
					}
				}
				// Add list actions
				mappingBean.setListAction(listActionBean);
				// Add to list mapping
				MAPPING_LIST.add(mappingBean);
			}
		}
	}


	/**
	 * @param alias
	 * @return
	 */
	public static MappingBean getMappingBean(String alias) {
		if (MAPPING_LIST != null) {
			if (!StringUtil.isEmpty(alias)) {
				for (MappingBean mappingBean : MAPPING_LIST) {
					if (alias.equals(mappingBean.getNameSpace() + "." + mappingBean.getAlias())) {
						return mappingBean;
					}
				}
			}
		}
		return null;
	}

	/**
	 * @param listAction
	 * @param action
	 * @return
	 */
	public static ActionBean getActionBean(MappingBean mappingBean,	String action) {
		List<ActionBean> listAction = mappingBean.getListAction();
		// iterate list actions
		if (listAction != null) {
			for (ActionBean actionBean : listAction) {
				if (actionBean.getAction().equals(action)) {
					return actionBean;
				}
			}
		}
		// Generic action
		mappingBean = getMappingBean("GENERIC.");
		for (ActionBean actionBean : mappingBean.getListAction()) {
			if (actionBean.getAction().equals(action)) {
				return actionBean;
			}
		}
		return null;
	}
}
