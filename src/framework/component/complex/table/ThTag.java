package framework.component.complex.table;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import framework.component.ComponentUtil;
import framework.controller.ControllerUtil;
import framework.controller.bean.ColumnsExportBean;
import framework.controller.bean.KeyLabelBean;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.constante.ProjectConstante.QUERY_CONDITIONS;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.NumericUtil;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.StringUtil;

@SuppressWarnings("serial")
public class ThTag extends HeaderTag {
	private String align;
	private String type;
	private String field;
	private String fieldExport;
	private Object groupValues;
	private String groupKey;
	private String groupLabel;
	private String leftJoin;
	private String formatable;
	private String filterOnly;
	private String autocompleteAct;
	
	@Override
	public int doStartTag() throws JspException {
		writeThComponent();

		return SKIP_BODY;
	}

	/**
	 * @throws JspException
	 */
	private void writeThComponent() throws JspException {
		StringBuilder sb = new StringBuilder();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		HeaderTag parentHead = (HeaderTag) findAncestorWithClass(this, HeaderTag.class);
		TableTag parentTable = (TableTag) parentHead.getParent();

		if (parentTable == null) {
			throw new JspTagException("Th Tag without Table Tag");
		}
		
		if(align != null){
			super.setStyle(StringUtil.getValueOrEmpty(super.getStyle())+";text-align:"+align+";");
		}

		//
		String tableName = parentTable.getName();
		RequestTableBean cplxTable = ControllerUtil.getRequestTableBean(tableName, request);
		boolean isSortable = false, isFiltrable = false;
		String value = super.getValueOrValueKey();
		String thId = (value != null) ? value.replaceAll(" ", "_").toLowerCase() : null;

		// Increment idx
		int idx = parentHead.getIdxTh();
		parentHead.setIdxTh( idx + 1);
		//int idxTh = parentHead.getIdxTh();
		// Empty th
		if("empty".equals(type)){
			if(StringUtil.isEmpty(this.getWidth())){
				setWidth(ProjectConstante.TD_MIN_WIDTH);
			}
			setFiltrable("false");
			setSortable("false");
		} else{
			parentTable.getListThShow().add("<input type=\"checkbox\" id=\"chccol_"+tableName+"_" + thId + "\" checked=\"checked\" value=\""+thId+"\"/>"+value+"\n");
		}
		// Add component to list of component and disable filtrable and sortable
		if("component".equals(type)){
			this.setFiltrable("false");
			this.setSortable("false");
			parentTable.getListTrComponent().add(this.getField());
		}

		// Is sortable
		if (StringUtil.isTrueOrNull(parentTable.getSortable())
				&& (StringUtil.isTrueOrNull(getSortable()))) {
			isSortable = true;
		}
		// Is filtrable
		if (StringUtil.isTrueOrNull(parentTable.getFiltrable()) && StringUtil.isTrueOrNull(getFiltrable())) {
			isFiltrable = true;
		}
		// Mark selected column
		if(isSortable){
			setClassStyle("head");
			setOnClick(getSortLink(request, getField(), cplxTable, parentTable, parentHead));
		}
		// Build TH beans
		buildThBean();
		//
		String thType = getType();
		// Add id for draging column
		this.setId(thId);

		if(StringUtil.isEmpty(getTooltipOrTooltipeKey())){
			setTooltip(value);
		}
		
		//
		
		if(StringUtil.isFalseOrNull(this.filterOnly)){
			sb.append("<th" + getFullComponentAttrubutes(null) + getWidth() + ">\n");
			sb.append("<h3>");
			sb.append(value);
			sb.append("</h3>");
			sb.append("</th>\n");
			//
			parentTable.getThBlock().append(sb);
		}

		// If filter
		if (StringUtil.isTrueOrNull(parentTable.getFiltrable())) {
			Map params = (Map)request.getAttribute(ProjectConstante.WORK_PARAMS);
			String filterId = tableName + "_" + ProjectConstante.PAGER_FILTER_PREFIX + getField();
			Object filterValue = cplxTable.getFilterCriteria().get(getField());
			
			// Conditional and filter id
			String conditionId = tableName + "_" + ProjectConstante.PAGER_CONDITION_PREFIX + getField();
			String conditionTypeId = tableName + "_" + ProjectConstante.PAGER_TYPE_PREFIX + getField();
						
			if(StringUtil.isEmpty(filterValue) || NumericUtil.getLongOrDefault(filterValue) == 0){
				filterValue = "";
			}
			
			StringBuilder sbCondition = new StringBuilder();
			StringBuilder sbFilter = new StringBuilder();
			
			// Join type
			if(StringUtil.isTrue(leftJoin) || groupValues != null){
				String joinTypeId = tableName + "_" + ProjectConstante.PAGER_JOIN_PREFIX + getField();
				sbFilter.append("<input type=\"hidden\" id=\"" + joinTypeId + "\" name=\"" + joinTypeId + "\"  value=\""+StringUtil.isTrue(leftJoin)+"|"+groupKey+"|"+groupLabel+"\"/>\n");
			}
			//
			if(isFiltrable){
				if(groupValues == null && autocompleteAct == null){
					sbCondition.append(ComponentUtil.getConditionsSelect(conditionId, params.get(conditionId), thType));
				} else{
					sbCondition.append("<select class=\"select\" id=\""+conditionId+"\" name=\""+conditionId+"\" style=\"padding-left:0px;padding-right:0px;\">");
					sbCondition.append("<option value=\""+QUERY_CONDITIONS.NUMERIC_CONDITION_EQUALS.getCode()+"\">"+QUERY_CONDITIONS.NUMERIC_CONDITION_EQUALS.getLibelle()+"</option>");
					sbCondition.append("</select>");
				}
				//
				if(ProjectConstante.TYPE_DATA_ENUM.BOOLEAN.getType().equals(thType)){//----------------- Boolean
					sbFilter.append("<select class='form-control select' name='" + filterId + "' id='"+filterId+"'>\n");
					for(String[] keyValue : ProjectConstante.YES_NO_CONDITIONS){
						String selectedBloc = keyValue[0].equals(filterValue) ? " selected='selected'" : "";
						sbFilter.append("<option value='"+keyValue[0]+"'" + selectedBloc + ">"+keyValue[1]+"</option>\n");
					}
					sbFilter.append("</select>\n");
				} else if(ProjectConstante.TYPE_DATA_ENUM.DATE.getType().equals(thType) || ProjectConstante.TYPE_DATA_ENUM.DATE_TIME.getType().equals(thType)){//----------------- Date
					sbFilter.append("<input type='text' class='form-control date-picker' name='" + filterId + "' id='"+filterId+"' value='" + filterValue +"'\" data-dateformat=\"dd/mm/yy\" style=\"float: left;margin-right: 5px;\" />\n");
				} else{//----------------- Text
					if(autocompleteAct != null){
						// Pour le multiple
						sbFilter.append("<input type=\"hidden\" name=\"" + filterId + "[]**\"/>\n");
						sbFilter.append("<select class='form-control select2 autoSlct' multiple='multiple' data-placeholder='' name='" + filterId + "' id='"+filterId+"' style=\"width:80%;\">\n");
						sbFilter.append("</select>\n");
						
						sbFilter.append(ComponentUtil.getJavascriptOnReadyBloc("loadTableSelectAuto('"+filterId+"', '"+EncryptionUtil.encrypt(autocompleteAct)+"', \""+groupKey+"\", \""+groupLabel+"\")"));
						
					} else if(groupValues == null){
						sbFilter.append("<input type='text' class='form-control' name='" + filterId + "' id='"+filterId+"' value='" + filterValue +"' style=\"float: left;margin-right: 5px;\"/>\n");
					} else{
						
						// Pour le multiple
						sbFilter.append("<input type=\"hidden\" name=\"" + filterId + "[]**\"/>\n");
						
						sbFilter.append("<select class='form-control select2 filtrSlct' multiple='multiple' data-placeholder='' name='" + filterId + "' id='"+filterId+"' style=\"width:80%;\">\n");
						sbFilter.append("<option value=''></option>\n");
						
						List<KeyLabelBean> listData = ComponentUtil.getArrayData(groupValues, groupKey, groupLabel);
						
						for(KeyLabelBean keyLabel : listData){
							String key = keyLabel.getKey();
							String selectedBloc = "";
							//
							if(key != null){
								if(filterValue instanceof String[]){
									for(String val : (String[])filterValue){
										if(StringUtil.isNotEmpty(val) && val.equals(key)){
											selectedBloc =  " selected='selected'";
											break;
										}
									}
								} else{
									selectedBloc = key.equals(filterValue) ? " selected='selected'" : "";									
								}
								//
								sbFilter.append("<option value='"+key+"'" + selectedBloc + ">"+keyLabel.getLabel()+"</option>\n");
							}
						}
						sbFilter.append("</select>\n");
					}
				}
				//
				sbFilter.append("<input type=\"hidden\" name=\"" + conditionTypeId + "\" id=\"" + conditionTypeId + "\" value=\"" + thType + "\"/>\n");
				
				//
				ColumnsExportBean filterCondBean = new ColumnsExportBean();
				filterCondBean.setLabel(this.getValueOrValueKey());
				filterCondBean.setField(this.getField());
				filterCondBean.setFiltrable(sbFilter.toString());
				filterCondBean.setSortable(sbCondition.toString());
				parentTable.getListFilterCondition().add(filterCondBean);
				
			} else{
				// End parent condtion
				sbCondition.append("&nbsp;");
				// End parent filter
				sbFilter.append("&nbsp;");
			}
			// End parent condtion
			sbCondition.append("</td>\n");
			// End parent filter
			sbFilter.append("</td>\n");
		}
	}

	/**
	 * @param parentTable
	 */
	private void buildThBean() {
		HeaderTag parentHead = (HeaderTag) findAncestorWithClass(this, HeaderTag.class);
		TableTag parentTable = (TableTag) parentHead.getParent();
		ColumnsExportBean columnBean = new ColumnsExportBean();

		columnBean.setFormatable(getFormatable());
		columnBean.setWidth(getSimpleWidth());
		columnBean.setType(getType());
		columnBean.setLabel(super.getValueOrValueKey());
		columnBean.setField(StringUtil.isNotEmpty(fieldExport) ? getFieldExport() : getField());
		// Add group values only if type was String[][] for manage labels
		if(this.groupValues != null && this.groupValues instanceof String[][]){
			columnBean.setGroupValues(groupValues);
		}
		if(StringUtil.isFalseOrNull(filterOnly)){
			parentTable.getListTh().add(columnBean);
		}
	}

	/**
	 * 
	 */
	private void releaseTh() {
		align = null;
		this.type = null;
		this.field = null;
		this.setSortable(null);
		this.setFiltrable(null);
		this.groupValues = null;
		this.groupKey = null;
		this.groupLabel = null;
		this.leftJoin = null;
		this.formatable = null;
		this.autocompleteAct = null;
		//
		super.release();
	}

	
	@Override
	public int doEndTag() throws JspException {
		releaseTh();
		
		return EVAL_PAGE;
	}

	/**
	 * @param field
	 * @param cplxTable
	 * @param parentTable
	 * @param parentHead
	 * @return
	 * @throws JspTagException
	 */
	private String getSortLink(HttpServletRequest request, String field, RequestTableBean cplxTable, TableTag parentTable, HeaderTag parentHead) throws JspTagException {
		StringBuilder sb = new StringBuilder();
		String classe = null;
		String order = null;
		Map<String, String> orderSortMap = cplxTable.getOrderSortMap();

		// Inverse order sorting
		if (orderSortMap == null) {
			order = "desc";
			classe = "head";
		} else if (orderSortMap.get(getField()) != null) {
			order = orderSortMap.get(getField());
			parentTable.setSelectedThIdx(parentHead.getIdxTh());
			if ("asc".equalsIgnoreCase(order)) {
				order = "desc";
				classe = "desc";
			} else {
				order = "asc";
				classe = "asc";
			}
		} else {
			order = "desc";
			classe = "head";
		}
		setClassStyle(classe);
		// Href
		if(cplxTable.getDataSize() > 0){
			int curent_page = cplxTable.getCurrentPage();
			sb.append("javascript:sort_act('"+EncryptionUtil.encrypt(parentTable.getInitAction())+"','" + parentTable.getName() + "', '" + field + "', '" + order + "', '" + curent_page + "', event);");
		}

		return sb.toString();
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	public Object getGroupValues() {
		return groupValues;
	}

	public void setGroupValues(Object groupValues) {
		this.groupValues = groupValues;
	}


	public String getGroupKey() {
		return groupKey;
	}

	public void setGroupKey(String groupKey) {
		this.groupKey = groupKey;
	}

	public String getGroupLabel() {
		return groupLabel;
	}

	public void setGroupLabel(String groupLabel) {
		this.groupLabel = groupLabel;
	}

	public String getLeftJoin() {
		return leftJoin;
	}

	public void setLeftJoin(String leftJoin) {
		this.leftJoin = leftJoin;
	}

	public String getFieldExport() {
		return fieldExport;
	}

	public void setFieldExport(String fieldExport) {
		this.fieldExport = fieldExport;
	}

	/**
	 * @return the align
	 */
	public String getAlign() {
		return align;
	}

	/**
	 * @param align the align to set
	 */
	public void setAlign(String align) {
		this.align = align;
	}

	public String getFormatable() {
		return formatable;
	}

	public void setFormatable(String formatable) {
		this.formatable = formatable;
	}

	public String getFilterOnly() {
		return filterOnly;
	}

	public void setFilterOnly(String filterOnly) {
		this.filterOnly = filterOnly;
	}

	public String getAutocompleteAct() {
		return autocompleteAct;
	}

	public void setAutocompleteAct(String autocompleteAct) {
		this.autocompleteAct = autocompleteAct;
	}
}
