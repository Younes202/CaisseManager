package framework.component.complex.table;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import framework.controller.TableFormCriteriaUtil;
import framework.controller.bean.RequestFormBean;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.util.NumericUtil;

public class RequestTableBean{
	private HttpServletRequest request;
	private int limitIndex;
	private int pageSize = NumericUtil.toInteger(ProjectConstante.DEFAULT_LINE_COUNT);
	private int startIndex;
	private int dataSize;
	private List<?> dataExport;
	private int pageCount;
	private int currentPage;
	private int[] pageBlocSize;
	private String tableName;
	private boolean isExport;
	private Map<String, String> orderSortMap;
	private Map<String, String> conditionCriteria;
	private Map<String, String> conditionType;
	private Map<String, String> fieldJoin;
	private Map<String, Object> filterCriteria;
	private RequestFormBean formBean;
	private Map<String, Object> queryCriteria;

	/**
	 * Default constructor
	 */
	public RequestTableBean(){
		if(this.formBean == null){
			this.formBean = new RequestFormBean();
		}
	}

	public Map<String, Object> getQueryCriteria(){
		if(this.queryCriteria == null){
			this.queryCriteria = new HashMap<String, Object>();
		}
		return this.queryCriteria;
	}

	public void setQueryCriteria(Map<String, Object> queryCriteria){
		this.queryCriteria = queryCriteria;
	}
	public Map<String, Object> getFilterCriteria() {
		if(this.filterCriteria == null){
			this.filterCriteria = new HashMap<String, Object>();
		}
		return filterCriteria;
	}
	public Map<String, String> getFieldJoin() {
		if(this.fieldJoin == null){
			this.fieldJoin = new HashMap<String, String>();
		}
		
		return fieldJoin;
	}

	public void setFieldJoin(Map<String, String> fieldJoin) {
		this.fieldJoin = fieldJoin;
	}

	public void setFilterCriteria(Map<String, Object> filterCriteria) {
		this.filterCriteria = filterCriteria;
	}
	public Map<String, String> getConditionCriteria() {
		if(this.conditionCriteria == null){
			this.conditionCriteria = new HashMap<String, String>();
		}
		return conditionCriteria;
	}
	public void setConditionCriteria(Map<String, String> conditionCriteria) {
		this.conditionCriteria = conditionCriteria;
	}
	/**
	 * @return the conditionType
	 */
	public Map<String, String> getConditionType() {
		if(this.conditionType == null){
			this.conditionType = new HashMap<String, String>();
		}
		return conditionType;
	}
	/**
	 * @param conditionType the conditionType to set
	 */
	public void setConditionType(Map<String, String> conditionType) {
		this.conditionType = conditionType;
	}
	/**
	 * @return the orderSort
	 */
	public Map<String, String> getOrderSortMap() {
		if(this.orderSortMap == null){
			this.orderSortMap = new HashMap<String, String>();
		}

		return orderSortMap;
	}
	/**
	 * @param orderSort the orderSort to set
	 */
	public void setOrderSortMap(Map<String, String> orderSort) {
		this.orderSortMap = orderSort;
	}


	//******************************************************

	public int getLimitIndex() {
		return limitIndex;
	}
	public void setLimitIndex(int limitIndex) {
		this.limitIndex = limitIndex;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	public int getDataSize() {
		return dataSize;
	}
	public void setDataSize(int dataSize) {
		this.dataSize = dataSize;
	}
	public RequestTableBean startPagger(int dataSize) {
		this.dataSize = dataSize;
		return TableFormCriteriaUtil.sendDataPager(this, tableName, request);
	}

	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public int[] getPageBlocSize() {
		return pageBlocSize;
	}
	public void setPageBlocSize(int[] pageBlocSize) {
		this.pageBlocSize = pageBlocSize;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public boolean isExport() {
		return isExport;
	}
	public void setExport(boolean isExport) {
		this.isExport = isExport;
	}
	public List<?> getDataExport() {
		return dataExport;
	}
	public void setDataExport(List<?> dataExport) {
		this.dataExport = dataExport;
	}
	public RequestFormBean getFormBean() {
		return formBean;
	}
	public void setFormBean(RequestFormBean formBean) {
		this.formBean = formBean;
	}
}
