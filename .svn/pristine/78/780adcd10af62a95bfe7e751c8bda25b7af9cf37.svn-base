package framework.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspTagException;

import framework.component.complex.table.RequestTableBean;
import framework.controller.bean.RequestFormBean;
import framework.model.common.constante.ActionConstante;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.util.NumericUtil;
import framework.model.common.util.StringUtil;

public class TableFormCriteriaUtil {
	/**
	 * Build all parameters needed for user pager
	 * @param request
	 * @throws JspTagException
	 */
	public static RequestTableBean sendDataPager(RequestTableBean cplxTable, String tableName, HttpServletRequest request) {
		Map params = (Map)request.getAttribute(ProjectConstante.WORK_PARAMS);
		if(params == null){
			return cplxTable;
		}
		// Sort
		int lineCount 	 = NumericUtil.getIntOrDefault(params.get(tableName + "_pager.lc"));// Line
		boolean isNavig  = ((params.get(tableName + "_pager.cp") == null) /*&& params.get(ProjectConstante.IS_NAVIGATION) != null*/);
		boolean isSubCp  = (isNavig|| StringUtil.isEmpty(params.get(tableName + "_pager.cp")));
		int currentPage  = NumericUtil.getIntOrDefault(params.get((isSubCp ? tableName + "_pager.cp_sub" : tableName + "_pager.cp")));// Current
		int elementCount = cplxTable.getDataSize();

		if (currentPage == 0)// Current page
			currentPage = 1;
		if (lineCount == 0)// Line count
			lineCount = NumericUtil.toInteger(ProjectConstante.DEFAULT_LINE_COUNT);

		// Pages count
		while ((((currentPage - 1) * lineCount) > elementCount) && currentPage > 1) {
			currentPage--;
		}
		// Start
		int startIndex = (lineCount * (currentPage - 1));
		if (lineCount >= elementCount) {
			startIndex = 0;
		}
		// Claculate Limit
		int limit = (startIndex + lineCount);
		if (limit > elementCount) {
			limit = elementCount;
		}
		//
		cplxTable.setCurrentPage(currentPage);
		cplxTable.setStartIndex(startIndex);
		cplxTable.setLimitIndex(limit);
		//cplxTable.setDataSize(elementCount);
		cplxTable.setPageSize(lineCount);

		return cplxTable;
	}

	/**
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static RequestFormBean getRequestFormBean(HttpServletRequest request){
		Map<String, Object> params = (Map)request.getAttribute(ProjectConstante.WORK_PARAMS);
		RequestFormBean requestBean = new RequestFormBean();
		Map crit = new HashMap();
		for(String key : params.keySet()){
			Object value = params.get(key);
			if(!StringUtil.isEmpty(value)){
				if((key.indexOf("work_") == -1) && (key.indexOf("menu") == -1)){
					crit.put(key, value);
				}
			}
		}
		//
		if(crit.size() == 0){
			crit = null;
		}
		//
		requestBean.setFormCriterion(crit);
		//

		String types = ControllerUtil.getParam(request, "compl_types");
		if(types != null){
			Map<String, String> typesCriterion = new HashMap<String, String>();
			String[] typesArray = StringUtil.getArrayFromStringDelim(types);
			for(String st : typesArray){
				String[] paramsArray = StringUtil.getArrayFromStringDelim(st, ":");
				typesCriterion.put(paramsArray[0], paramsArray[1]);
			}
			//
			requestBean.setTypeCriterion(typesCriterion);
		}

		return requestBean;
	}

	/**
	 * @param request
	 * @return
	 * @throws JspTagException
	 */
	@SuppressWarnings("unchecked")
	public static RequestTableBean buildRequestTableBean(String tableName, HttpServletRequest request) {
		Map<String, Object> params = (Map)request.getAttribute(ProjectConstante.WORK_PARAMS);
		String fullTableName = tableName + "_" + ProjectConstante.WORK_CPLX_TABLE;

		// If reinit action, remove all table informations
		if((params != null) && "-1".equals(params.get(tableName+"_pager.cp"))){
			ControllerUtil.removeMenuAttribute(fullTableName, request);
			//
			for(String key : params.keySet()){
				if(key.startsWith(tableName)){
					params.put(key, null);
				}
			}
		}

		RequestTableBean cplxTableBean = (RequestTableBean)ControllerUtil.getMenuAttribute(fullTableName, request); 
		String fieldOrderField = (String)params.get(tableName + "_pager.fie");
		Map<String, String> orderSortMap = null;
		String[] fieldOrderArray = StringUtil.getArrayFromStringDelim(fieldOrderField, ";");

		//
		if(cplxTableBean == null){
			cplxTableBean = new RequestTableBean();
			cplxTableBean.setTableName(tableName);
		}
		
		if(params.get("is_filter_act") != null){
			// Clear conditions and filters
//			cplxTableBean.getQueryCriteria().clear();
			cplxTableBean.getFilterCriteria().clear();
			params.remove("is_filter_act");
		}
		// Restaure old values if form is submited
		if(ControllerUtil.getParam(request, ProjectConstante.WORK_FORM_ACTION) != null){
			cplxTableBean.getFormBean().getFormCriterion().putAll(getFormCriterion(params));

			String filterStart	= tableName + "_" + ProjectConstante.PAGER_FILTER_PREFIX;
			String condStart	= tableName + "_" + ProjectConstante.PAGER_CONDITION_PREFIX;
			String typeStart 	= tableName + "_" + ProjectConstante.PAGER_TYPE_PREFIX;
			String joinStart 	= tableName + "_" + ProjectConstante.PAGER_JOIN_PREFIX;
			//
			cplxTableBean.getFilterCriteria().putAll(getDataCriterion(tableName, params, filterStart)); 
			cplxTableBean.getConditionCriteria().putAll(getDataCriterion(tableName, params, condStart));
			cplxTableBean.getConditionType().putAll(getDataCriterion(tableName, params, typeStart));
			cplxTableBean.getFieldJoin().putAll(getDataCriterion(tableName, params, joinStart));
		}
		// Set request
		cplxTableBean.setRequest(request);

		// Set export mode
		if(isExporMode(request)){
			cplxTableBean.setExport(true);
		}

		// Criterion and order---------------------------------------------------------------------------------------
		if(fieldOrderArray != null){
			orderSortMap = new LinkedHashMap<String, String>();
			//
			for(int i=0; i<fieldOrderArray.length; i++){
				String[] fieldOrderArr = StringUtil.getArrayFromStringDelim(fieldOrderArray[i], ":");
				if(fieldOrderArr != null) {
					orderSortMap.put(fieldOrderArr [0], fieldOrderArr [1]);
				}
			}
		}
		//
		if(orderSortMap == null){
			orderSortMap = cplxTableBean.getOrderSortMap();
		}
		// Sort and order
		cplxTableBean.setOrderSortMap(orderSortMap);
		//
		ControllerUtil.setMenuAttribute(fullTableName, cplxTableBean, request);

		return cplxTableBean;
	}

	/**
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static Map getFormCriterion(Map<String, Object> params){
		Map crit = new HashMap();
		for(String key : params.keySet()){
			Object value = params.get(key);
			if(!StringUtil.isEmpty(value)){
				if((key.indexOf("work_") == -1) && (key.indexOf("menu") == -1)){
					crit.put(key, value);
				}
			}
		}

		if(crit.size() == 0){
			crit = null;
		}

		return crit;
	}

	/**
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static Map getDataCriterion(String tableName, Map<String, Object> params, String prefix){
		Map crit = new HashMap();
		for(String key : params.keySet()){
			Object value = params.get(key);
			if(!StringUtil.isEmpty(value)){
				if(key.startsWith(prefix)){
					crit.put(key.substring(prefix.length()), value);
				}
			}
		}

//		if(crit.size() == 0){
//			crit = null;
//		}

		return crit;
	}

	/**
	 * @param request
	 * @return
	 */
	public static boolean isExporMode(HttpServletRequest request){
		return (ControllerUtil.getParam(request, ActionConstante.EXPORT) != null);
	}

	/**
	 * Clear data size ---> Bug depend tables
	 * @param request
	 */
	public static void clearDataSize(HttpServletRequest request){
		HttpSession session = request.getSession(false);
		if(session != null) {
			Enumeration attributes = session.getAttributeNames();
			while(attributes.hasMoreElements()){
				String attribute = (String)attributes.nextElement();
				if(attribute.endsWith("_" + ProjectConstante.WORK_CPLX_TABLE)){
					RequestTableBean cplxTableBean = (RequestTableBean)session.getAttribute(attribute);
					if(cplxTableBean != null){
						cplxTableBean.setDataSize(0);
					}
				}
			}
		}
	}

}
