/**
 *
 */
package framework.controller;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import framework.component.complex.table.RequestTableBean;
import framework.controller.bean.MenuBean;
import framework.controller.bean.PagerBean;
import framework.controller.bean.action.IViewBean;
import framework.model.common.constante.ActionConstante;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.constante.ProjectConstante.SESSION_SCOPE_ENUM;
import framework.model.common.service.FrameworkMessageService;
import framework.model.common.service.MessageIdsService;
import framework.model.common.service.MessageService;
import framework.model.common.util.NumericUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.AnnotationExclusionStrategy;
import framework.model.util.HibernateProxyTypeAdapter;
import framework.model.util.MenuMappingService;

/**
 * @author 
 *
 */
public class ControllerUtil {

	private final static Logger LOGGER = Logger.getLogger(ControllerUtil.class);
	
	// Pagination coté front -----------------------------------
	public static PagerBean managePager(HttpServletRequest request, String type){
		String act = request.getParameter("sens");
		PagerBean pagerBean = (PagerBean) getUserAttribute("PAGER_BEAN_"+type, request);
        if(pagerBean == null){
        	pagerBean = new PagerBean();
        	
        	String nbrElmnt = ContextGloabalAppli.getGlobalConfig("NBR_ELEMENT_PAGE_FAM");
			Integer nbrElmentParPage = Integer.valueOf(StringUtil.isEmpty(nbrElmnt) ? "15" : nbrElmnt);
        	pagerBean.setElementParPage(nbrElmentParPage);
            setUserAttribute("PAGER_BEAN_"+type, pagerBean, request);
        }
        
        if(act == null){
        	act = "FP";
        }
        
        if(act.equals("NP")){
        	pagerBean.setCurrPage(pagerBean.getCurrPage()+1);
        } else if(act.equals("PP")){
        	pagerBean.setCurrPage(pagerBean.getCurrPage()-1);
        } else if(act.equals("LP")){
        	pagerBean.setCurrPage(pagerBean.getNbrPage());
        } else if(act.equals("FP")){
        	pagerBean.setCurrPage(0);
        }
        
        //
        if(pagerBean.getCurrPage() <= 0){
        	pagerBean.setCurrPage(0);
        } else if(pagerBean.getCurrPage() >= pagerBean.getNbrPage()){
        	pagerBean.setCurrPage(pagerBean.getNbrPage() == 0 ? 0 : pagerBean.getNbrPage()-1);
        }
        
        pagerBean.setStartIdx(pagerBean.getElementParPage()*pagerBean.getCurrPage());
         
        return pagerBean;
	}
	//---------------------------------------------------------------
	
	public static <T> List<T> getObjectFromJson(String json, Class<T> clazz){
		GsonBuilder b = new GsonBuilder();
		 Gson gson = b.create();
		 Object [] array = (Object[])java.lang.reflect.Array.newInstance(clazz, 0);

		 array = gson.fromJson(json, array.getClass());
		 
		 List<T> list = new ArrayList<T>();
		 if(array != null) {
		     for (int i=0 ; i<array.length ; i++) {
		        list.add(clazz.cast(array[i]));
		     }
		 }
		 
	     return list; 
	}
	public static <T> T getSingleObjectFromJson(String json, Class<T> clazz){
		GsonBuilder b = new GsonBuilder();
		 Gson gson = b.create();
		 return gson.fromJson(json, clazz);
	}
	public static String getJSonDataAnnotStartegy(Object data){
		 GsonBuilder b = new GsonBuilder();
			 b.setExclusionStrategies(new AnnotationExclusionStrategy()).create();
	     b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
	     Gson gson = b.create();
	     String json = gson.toJson(data);
	     
	     return json;
	}
	
	/**
	 * @throws Exception *
	 * 
	 */
	public static String sendJsonPOST(String data, String url){
		String response = "";
		String type = "application/json; charset=UTF-8";
		try {
			URL u;
			u = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			
			conn.setDoOutput(true);
			
			conn.setRequestMethod("POST");
			conn.setRequestProperty( "Content-Type", type );
			conn.setRequestProperty( "Content-Length", String.valueOf(data.length()));
			
			// give it 10 seconds to respond
		    conn.setReadTimeout(FileUtilController.TIMEOUT_URL_CALL);
		      
			OutputStream os = conn.getOutputStream();
			os.write(data.getBytes());
			os.close();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine = "";
			while((inputLine = in.readLine()) != null) {
				response = response + inputLine;
			}
			in.close();
		} catch (Exception e) {
			System.out.println(e.getMessage()+" : "+url);
			return "";
	        //e.printStackTrace();
	        //response = "KO";
	    } 
		
		return response;
	}
	
	/**
	 * @param request
	 * @return
	 */
	public static boolean isWorkFormSubmited(HttpServletRequest request){
		return request.getParameter(ProjectConstante.WORK_FORM_ACTION) != null;
	}
	
	/**
	 * If doDownLoad method is called
	 * @param request
	 * @return
	 */
	public static boolean isDownLoadMode(HttpServletRequest request){
		return StringUtil.isTrue(""+request.getAttribute("IS_EXPORT_MODE"));
	}

	/**
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isControllActionFound(HttpServletRequest request){ Map<String, Object> params = (Map)request.getAttribute(ProjectConstante.WORK_PARAMS);
		return params.get(ProjectConstante.WORK_FORM_ACTION) != null;
	}

	/** --------------------- End Pagination metodes ------------------------*/

	/**
	 * Convert String[] value on String in params map
	 * @param params
	 */
	public static Map<String, Object> refreshMapValues(Map<String, Object> params){
		for(String key : params.keySet()){
			Object obj = params.get(key);
			
			if(obj instanceof String[]){
				String[] tabValues = (String[])obj;
				if(tabValues.length >= 1){
					if(!params.containsKey(key+"[]**")){
						params.put(key, tabValues[(tabValues.length-1)]);
					} else{
						if(tabValues.length == 1 && StringUtil.isEmpty(tabValues[0])){
							params.put(key, null);
						}
					}
				}
			}
		}

		return params;
	}

	public static boolean isEditionPage(HttpServletRequest request){
		return isEditionPage(request, null);
	}
	
	/**
	 * @param request
	 * @return
	 */
	public static boolean isEditionPage(HttpServletRequest request, String ... actionToInclude){
		String action = getAction(request);

		if(actionToInclude != null){
			for (String actionInclude : actionToInclude) {
				if(action.equals(actionInclude)){
					return true;
				}
			}
		}
		return (ActionConstante.INIT_CREATE.equals(action) || ActionConstante.INIT_UPDATE.equals(action) 
						|| ActionConstante.INIT_DUPLIC.equals(action)
						|| ActionConstante.EDIT.equals(action)
				|| (
						(ActionConstante.CREATE.equals(action) || ActionConstante.UPDATE.equals(action) || "work_merge".equals(action)
						) && MessageService.isError()
					)
			);
	}
	
	/**
	 * @param request
	 * @return
	 */
	public static boolean isEditionWritePage(HttpServletRequest request){
		String action = getAction(request);

		return (
					ActionConstante.INIT_CREATE.equals(action) || ActionConstante.INIT_UPDATE.equals(action)
					|| ActionConstante.INIT_DUPLIC.equals(action)
					|| ((MessageService.isError() || MessageService.isQuestionExist() ) && (
							ActionConstante.CREATE.equals(action) || ActionConstante.UPDATE.equals(action)
							)
						)
			);
	}
	
	/**
	 * @param request
	 * @return
	 */
	public static boolean isEditionCreateAction(HttpServletRequest request){
		String action = getAction(request);

		return (
					ActionConstante.INIT_CREATE.equals(action)
					|| ActionConstante.INIT_DUPLIC.equals(action)
					|| (MessageService.isError() && (
							ActionConstante.CREATE.equals(action))
						)
			);
	}
	
	/**
	 * @param request
	 * @return
	 */
	public static boolean isCrudOperationOK(HttpServletRequest request){
		return (isCrudOperation(request) && !MessageService.isError());
	}

	/**
	 * @param request
	 * @return
	 */
	public static boolean isCrudOperation(HttpServletRequest request){
		String action = getAction(request);
		return (
						   ActionConstante.CREATE.equals(action)
						|| ActionConstante.UPDATE.equals(action)
						|| ActionConstante.DELETE.equals(action)
						|| "work_merge".equals(action)
				);
	}

	/**
	 * @param request
	 * @return
	 */
	public static boolean isResultPage(HttpServletRequest request) {
		String action = getAction(request);

		return !(!ActionConstante.FIND.equals(action) 
				&& !(ActionConstante.DELETE.equals(action)));
	}

	/**
	 * @param request
	 * @return
	 */
	public static boolean isFindAction(HttpServletRequest request){
		String action = getAction(request);
		return ActionConstante.FIND.equals(action);
	}

	/**
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getPageTitle(HttpServletRequest request){
		String title = "";
		//
		if(isAvailableConnexion(request)){
			String curMenuId = (String)MessageService.getGlobalMap().get(ProjectConstante.LEFT_MENU_ID);
			//curMenuId = (String)ControllerUtil.getUserAttribute(ProjectConstante.LEFT_MENU_ID, request);
			
			
			if(curMenuId != null){
				MenuBean menu = MenuMappingService.getMenuById(curMenuId);
				if(menu != null){
					title = menu.getPageTitle() + " (" + request.getAttribute("page_title") + ")";
				}
			}
		}
		
		return title;
	}

	/***********************  Request and session attributes ****************************/
	/**
	 * @param key
	 * @param value
	 * @param request
	 */
	public static void setMenuAttribute(String key, Object value, HttpServletRequest request) {
		String tabId = StringUtil.getValueOrEmpty(request.getParameter("curr_tab_id"));
		String menuPrefix = SESSION_SCOPE_ENUM.MENU.getType();
		HttpSession session = request.getSession(false);
		if(session != null){
			if(StringUtil.isEmpty(tabId)) {
				tabId = "tab_nav_0";
			}
			session.setAttribute(tabId + menuPrefix + key, value);
		}
	}
	
	/**
	 * @param key
	 * @param request
	 */
	public static void setUserAttribute(String key, Object value, HttpServletRequest request) {
		 HttpSession session = request.getSession(false);
		if(session != null){
			session.setAttribute(key, value);
		}
	}

	/**
	 * @param key
	 * @param request
	 * @return
	 */
	public static Object getUserAttribute(String key, HttpServletRequest request) {
		 HttpSession session = request.getSession(false);
		if(session != null){
			return session.getAttribute(key);
		}
		return null;
	}

	/**
	 * @param key
	 * @param request
	 */
	public static void removeUserAttribute(String key, HttpServletRequest request) {
		 HttpSession session = request.getSession(false);
		if(session != null){
			session.removeAttribute(key);
		}
	}

	/**
	 * @param key
	 * @return
	 */
	public static Object getMenuAttribute(String key, HttpServletRequest request) {
		String tabId = StringUtil.getValueOrEmpty(request.getParameter("curr_tab_id"));
		String menuPrefix = SESSION_SCOPE_ENUM.MENU.getType();
		//-------------------------------------------------------------------------------
		if(StringUtil.isEmpty(tabId)) {
			tabId = "tab_nav_0";
		}
		HttpSession session = request.getSession(false);
		return (session != null) ? session.getAttribute(tabId + menuPrefix + key) : null;
	}

	/**
	 * @param key
	 * @param request
	 */
	public static void removeMenuAttributeIgnorePopup(String key, HttpServletRequest request) {
		String tabId = StringUtil.getValueOrEmpty(request.getParameter("curr_tab_id"));
		if(StringUtil.isEmpty(tabId)) {
			tabId = "tab_nav_0";
		}
		HttpSession session = request.getSession(false);
		if(session != null){
			String menuPrefix = SESSION_SCOPE_ENUM.MENU.getType();
			session.removeAttribute(tabId + menuPrefix + key);
		}
	}
	
	/**
	 * @param key
	 * @param request
	 */
	public static void removeMenuAttribute(String key, HttpServletRequest request) {
		String tabId = StringUtil.getValueOrEmpty(request.getParameter("curr_tab_id"));
		if(StringUtil.isEmpty(tabId)) {
			tabId = "tab_nav_0";
		}
		HttpSession session = request.getSession(false);
		if(session != null){
			String menuPrefix = SESSION_SCOPE_ENUM.MENU.getType();
			//-------------------------------------------------------------------------------
			session.removeAttribute(tabId + menuPrefix + key);
		}
	}

	/**
	 * @param request
	 * @return
	 */
	public static Object getPersistantValueId(Map<String, Object> params){
		Object beanId = null;
		//
		if(params != null){
			beanId = params.get(ProjectConstante.WORK_ID);
		}
		return beanId;
	}

	/**
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isLeftMenuAction(HttpServletRequest request){
		Map<String, Object> params = (Map)request.getAttribute(ProjectConstante.WORK_PARAMS);
		return (params.get("lmnu") != null);
	}

	/**
	 * @param request
	 * @return
	 */
	public static boolean isAjaxBackJob(HttpServletRequest request){
		return (request.getParameter(ProjectConstante.IS_AJAX_BACK_JOB_CALL) != null);
	}

	/**
	 * @param request
	 * @return
	 */
	public static boolean isBodyTableAction(HttpServletRequest request){
		return (ControllerUtil.getParam(request, ActionConstante.BODY_TABLE) != null);
	}

	/**
	 * Test if session and user was availible
	 * @param request
	 * @return
	 */
	public static boolean isAvailableConnexion(HttpServletRequest request){
		HttpSession session = request.getSession(false);

		return (session != null && /*request.isRequestedSessionIdValid() &&*/ (MessageService.getGlobalMap().get(ProjectConstante.SESSION_GLOBAL_USER) != null));
	}

	/**
	 * @param request
	 */
	public static void cleanAll(HttpServletRequest request){
		// Clear all messages
		MessageService.clearMessages();
		FrameworkMessageService.clearMessages();
		MessageIdsService.clearQuestionAction();
	}

	/**
	 * @param request
	 * @return
	 */
	public static String[] getCtrlActionArray(HttpServletRequest request, String ctrlActCall) {
		String controllerAct = null;
		String[] ctrlActionBack = new String[3];

		controllerAct = (ctrlActCall != null) ? ctrlActCall : getCtrlAction(request);
		String[] ctrlAction = StringUtil.getArrayFromStringDelim(controllerAct, ".");
		// Put controller and action in request
		if (ctrlAction != null) {
			ctrlActionBack[0] = ctrlAction[0];// Name space
			if(ctrlAction.length == 1){
				ctrlActionBack[1] = ctrlAction[0];// Controller
			} else{
				ctrlActionBack[1] = ctrlAction[0] + "." + ctrlAction[1];// Controller
				ctrlActionBack[2] = (ctrlAction.length > 2) ? ctrlAction[2] : null;// Action
			}
		}

		return ctrlActionBack;
	}

	/**
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getCtrlAction(HttpServletRequest request){
		Object controllerAct = getParam(request, ProjectConstante.WORK_FORM_ACTION);
		return ""+controllerAct;
	}

	public static String getParam(HttpServletRequest request, String key){
		Map<String, Object> params = (Map)request.getAttribute(ProjectConstante.WORK_PARAMS);
		return (params==null || params.get(key) == null) ? null : ""+params.get(key);
	}
	
	/**
	 * @param request
	 * @return
	 */
	public static String getAction(HttpServletRequest request){
		String[] ctrlActionArray = getCtrlActionArray(request, null);

		String action = ((ctrlActionArray != null) && (ctrlActionArray.length == 3)) ? ctrlActionArray[2] : null;
		// This case if action is refresh
		if(StringUtil.isEmpty(action) && (ctrlActionArray.length == 3)){
			action = ctrlActionArray[1];
		}

		return action;
	}

	/**
	 * @param request
	 * @return
	 */
	public static String getController(HttpServletRequest request){
		String strlAct = null;
		//
		String[] ctrlActionArray = getCtrlActionArray(request, strlAct);
		if((ctrlActionArray != null) && (ctrlActionArray.length == 3)){
			return ctrlActionArray[1];
		}

		return null;
	}

	/**
	 * @param tableName
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static RequestTableBean getRequestTableBean(String tableName, HttpServletRequest request) {
		String fullTableName = tableName + "_" + ProjectConstante.WORK_CPLX_TABLE;
		RequestTableBean cplxTableBean = (RequestTableBean)ControllerUtil.getMenuAttribute(fullTableName, request);
		if(cplxTableBean == null){
			cplxTableBean = new RequestTableBean();
			cplxTableBean.setTableName(tableName);
			ControllerUtil.setMenuAttribute(fullTableName, cplxTableBean, request);
		}

		return cplxTableBean;
	}

	/**
	 * @param tableName
	 * @param request
	 * @return
	 */
	public static Long[] getCheckedElementsLong(String tableName, Map<String, Object> params){
		String idsStr = (String)params.get(tableName + "_" + ProjectConstante.CHECK_SAVE_STR);
		// If idsStr is null, we are in edition form. The checked elements are saved in hidden field work_elements_id
		if(idsStr == null){
			idsStr = (String)params.get(ProjectConstante.ELEMENTS_IDS);
		}
		Long[] ids = null;
		if(idsStr != null){
			String[] values = StringUtil.getArrayFromStringDelim(idsStr, ProjectConstante.SEPARATOR);
			// Delete check all id
			if(values != null){
				for(int i = 0; i < values.length; i++){
					if(values[i].startsWith("all_") || StringUtil.isEmpty(values[i])){
						values[i] = null;
					}
				}
				//
				ids = NumericUtil.stringArrayToLongArray(values);
			}
		}
		// Send updated elements count
		if(ids != null){
			params.put("nbrElementsGroup", ids.length);
		}

		return ids;
	}

	/**
	 * @param viewBeanClass
	 * @return
	 */
	public static String getBeanAlias(Class<? extends IViewBean> viewBeanClass){
		if(viewBeanClass != null){
			String endBean = StrimUtil.getGlobalConfigPropertie("bean.end");
			String beanName = viewBeanClass.getSimpleName();
			String beanNameLower = beanName.toLowerCase();
			int idxBean = beanNameLower.indexOf(endBean.toLowerCase());
			if(idxBean != -1){
				beanNameLower = beanName.substring(0, idxBean);
			}
			return StringUtil.firstCharToLowerCase(beanNameLower);
		}
		return null;
	}

	/**
	 * @param viewBean
	 * @return
	 */
	public static String getBeanAlias(IViewBean viewBean){
		if(viewBean != null){
			return getBeanAlias(viewBean.getClass());
		}
		return null;
	}

	/**
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Map<String, String>> getMapValidator(HttpServletRequest request){
		return (Map<String, Map<String, String>>)ControllerUtil.getMenuAttribute(ProjectConstante.MAP_VALIDATORS, request);
	}

	/**
	 * @param request
	 */
	public static void removeAllValidatorMap(HttpServletRequest request){
		
		if(request.getAttribute("SKIP_CLEAR_VALIDATOR") != null) {
			return;
		}
		
		
		HttpSession session = request.getSession(false);
		Enumeration<?> attributes = session.getAttributeNames();
		String menuPrefix = SESSION_SCOPE_ENUM.MENU.getType();
		//
		while(attributes.hasMoreElements()){
			String attribute = (String)attributes.nextElement();
			if(attribute.startsWith(menuPrefix + ProjectConstante.MAP_VALIDATORS)){
				session.removeAttribute(attribute);
			}
		}
	}

	/**
	 * @param paramMap
	 * @param params
	 */
	public static void putParamsInParamsMap(Map<String, String> paramMap, String params){
	String[] paramsArray = StringUtil.getArrayFromStringDelim(params, "&");
	if(paramsArray != null){
		for(String param : paramsArray){
			String[] keyValueArray = StringUtil.getArrayFromStringDelim(param, "=");
			if(keyValueArray != null){
				paramMap.put(keyValueArray[0], ((keyValueArray.length > 1) ? keyValueArray[1] : ""));
				}
			}

		}
	}

	 /**
     *  Sends a file to the ServletResponse output stream.  Typically
     *  you want the browser to receive a different name than the
     *  name the file has been saved in your local database, since
     *  your local names need to be unique.
     *
     *  @param req The request
     *  @param resp The response
     *  @param filename The name of the file you want to download.
     *  @param original_filename The name the browser should receive.
     */
	public static void doDownload(HttpServletRequest request, HttpServletResponse response, File file) {
        int length   	= 0;
        String mimetype 		= request.getServletContext().getMimeType(file.getPath());
        String fileName = file.getName();
		String fileType = fileName.substring(fileName.indexOf(".") + 1, fileName.length());
        
        //  Set the response and go!
		if (fileType.trim().equalsIgnoreCase("txt")) {
			response.setContentType((mimetype != null) ? mimetype : "text/plain" );
		} else if (fileType.trim().equalsIgnoreCase("doc")) {
			response.setContentType((mimetype != null) ? mimetype : "application/msword" );
		} else if (fileType.trim().equalsIgnoreCase("xls")) {
			response.setContentType((mimetype != null) ? mimetype : "application/vnd.ms-excel");
		} else if (fileType.trim().equalsIgnoreCase("pdf")) {
			response.setContentType((mimetype != null) ? mimetype : "application/pdf" );
		} else if (fileType.trim().equalsIgnoreCase("ppt")) {
			response.setContentType((mimetype != null) ? mimetype : "application/ppt" );
		} else {
			response.setContentType((mimetype != null) ? mimetype : "application/octet-stream" );
		}

        response.setContentLength( (int)file.length() );
        response.setHeader( "Content-Disposition", "attachment; filename=\"" + fileName + "\"" );
        
        //  Stream to the requester.
        byte[] bbuf = new byte[NumericUtil.toInteger(ProjectConstante.MAX_BUFFER_SIZE)];
       

		try {
	        ServletOutputStream op 	= response.getOutputStream();
	        DataInputStream in = new DataInputStream(new FileInputStream(file));
	        //
	        while ((in != null) && ((length = in.read(bbuf)) != -1)) {
	            op.write(bbuf,0,length);
	        }
	
	        in.close();
	        op.flush();
	        op.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		request.setAttribute("IS_EXPORT_MODE", true);
	}
	
	/**
	 * @param request
	 * @param response
	 * @param fileName
	 * @param bbuf
	 */
	public static void doDownload(HttpServletRequest request, HttpServletResponse response, String fileName, byte[] bbuf, boolean isViewMode) {
		//  Stream to the requester
		if(isViewMode){
			response.setHeader( "Content-Disposition", "inline; filename=\"" + fileName + "\"" );
		} else{
			response.setHeader( "Content-Disposition", "attachment; filename=\"" + fileName + "\"" );
		}

		String fileType = fileName.substring(fileName.indexOf(".") + 1, fileName.length());
		if (fileType.trim().equalsIgnoreCase("txt")) {
			response.setContentType( "text/plain" );
		} else if (fileType.trim().equalsIgnoreCase("doc")) {
			response.setContentType( "application/msword" );
		} else if (fileType.trim().equalsIgnoreCase("xls")) {
			response.setContentType( "application/vnd.ms-excel");
		} else if (fileType.trim().equalsIgnoreCase("pdf")) {
			response.setContentType( "application/pdf" );
		} else if (fileType.trim().equalsIgnoreCase("ppt")) {
			response.setContentType( "application/ppt" );
		} else {
			response.setContentType( "application/octet-stream" );
		}
		
		//  Stream to the requester.
		try {
			ServletOutputStream outs 	= response.getOutputStream();
			outs.write(bbuf);
		    outs.flush();
		    outs.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		request.setAttribute("IS_EXPORT_MODE", true);
	}
	
	/**
	 * Forward to specified page.
	 *
	 * @param request
	 * @param response
	 * @param destination Page/servlet to forward to.
	 */
	public static void forward(ServletContext context, ServletRequest request, ServletResponse response,
			String destination) {

		try {
			RequestDispatcher dispatcher = context.getRequestDispatcher(destination);
			dispatcher.forward(request, response);
		} catch (Exception e) {
			LOGGER.error("destination=" + destination + ", " + e.toString(), e);
		}
	}
	
	public static boolean isInitialisation(HttpServletRequest request){
		return ControllerUtil.getMenuAttribute("IS_INITIALISE", request) != null;
	}

	 /**
	 * @param request
	 * @param response
	 * @param destination
	 * @throws IOException
	 */
	public static void redirect(HttpServletResponse response, String destination) {
		try {
			response.sendRedirect(response.encodeRedirectURL(destination));
		} catch (Exception e) {
			LOGGER.error("destination=" + destination + ", " + e.toString(), e);
		}
	}
	
	public static String getJSonData(Object data){
		 GsonBuilder b = new GsonBuilder();
	     Gson gson = b.create();
	     String json = gson.toJson(data);
	     
	     return json;
	}
}
