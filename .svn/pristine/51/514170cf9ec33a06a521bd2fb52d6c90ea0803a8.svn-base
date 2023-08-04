
package framework.component.complex.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import framework.component.Component;
import framework.component.ComponentUtil;
import framework.component.complex.table.export.ExportTableBean;
import framework.controller.Context;
import framework.controller.ControllerUtil;
import framework.controller.bean.ColumnsExportBean;
import framework.model.common.FooterButtonEnum;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.MenuMappingService;

@SuppressWarnings("serial")
public class TableTag extends Component {
	private String transitionType;
	// Cells
	private List<ColumnsExportBean> listFilterCondition;
	private StringBuilder thCells;
	private StringBuilder thBodyCells;
	private String initAction;
	//
	private int selectedThIdx = 0;
	private int idxAlternate = 0;
	private boolean isCheckboxFound;
	private String width;
	private String trHeight;
	private String autoHeight;

	private String align;
	// Boolean
	private String sortable;
	private String dragable;
	private String dragableRows;
	private String showDataState;//Enable/Disable/All
	private String filtrable;
	private String checkable;
	private String alternate;
	private String paginate;
	private String exportable;
	private String showTitleBanner;
	private String forceFilter;
	// Blocks
	private List<ColumnsExportBean> listTh;
	private List<String> listThShow;
	private List<ColumnsExportBean> listExportTh;
	// Writable table
	private List<String> listTrComponent;
	private List<FooterButtonEnum> listFotterExcludeButton;
	private String footerController;
	private boolean isUpdateTrCondition;
	private boolean isDeleteTrCondition;
	
	private List<BannerTableButton> listFooterAddedButton;

	/**
	 * @return
	 * @throws JspException
	 */
	@Override
	public int doStartTag() throws JspTagException, JspException {
		boolean isBodyActOther = false;
		this.listFotterExcludeButton = new ArrayList<FooterButtonEnum>();
		this.listFooterAddedButton = new ArrayList<BannerTableButton>();

		String currentAjaxTable = ComponentUtil.getCurrentAjaxTable(pageContext);
		// Valuate field from map and gui action
		valuateFromGui();
		//
		if(ComponentUtil.isBodyTableAction(pageContext) && !this.getName().equals(currentAjaxTable)){
			isBodyActOther = true;
		}
		//
		if(!isBodyActOther && StringUtil.isTrueOrNull(super.getVisible())){
			writeStartComponent();

			return EVAL_BODY_INCLUDE;
		} else{
			return SKIP_BODY;
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		boolean isBodyActOther = false;
		String currentAjaxTable = ComponentUtil.getCurrentAjaxTable(pageContext);
		if(ComponentUtil.isBodyTableAction(pageContext) && !this.getName().equals(currentAjaxTable)){
			isBodyActOther = true;
		}
		//
		if(!isBodyActOther && StringUtil.isTrueOrNull(super.getVisible())){
			writeEndComponent();
			// Clear saved data
			releaseTable();
		}
		return EVAL_PAGE;
	}

	/**
	 * @throws JspException
	 */
	@SuppressWarnings("unchecked")
	public void writeStartComponent() throws JspException {
		StringBuilder sb = new StringBuilder();
		StringBuilder scriptSt = new StringBuilder();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		//
		String tableName = this.getName();
		this.setId(tableName);
		//
		if(listFilterCondition == null){
			listFilterCondition = new ArrayList<>();
		}
		//
		listTh = new ArrayList<ColumnsExportBean>();
		thCells = new StringBuilder();
		thBodyCells = new StringBuilder();
		listExportTh = new ArrayList<ColumnsExportBean>();
		listThShow = new ArrayList<String>();
		listTrComponent = new ArrayList<String>();
		
		if(align != null){
			int currWidth = Integer.parseInt((this.width == null ? "100" : (this.width.indexOf("%")!=-1 ? this.width.substring(0, this.width.indexOf("%")) : this.width)));
			align = " marging-left="+currWidth/2+";";
		}
		
		// Script show hide filter div
		sb.append(ComponentUtil.getJavascriptOnReadyBloc("addFilterEvent('"+tableName+"', '"+EncryptionUtil.encrypt(this.initAction)+"');"));
		//
		if(!ComponentUtil.isBodyTableAction(pageContext)){
			// Start global div
			sb.append("<div class=\"flexigrid\" id=\"flex_"+getJQueryName()+"\" style=\"width: "+width+";display:none;"+align+"\">\n");
			// Title banner
			if(StringUtil.isTrueOrNull(this.showTitleBanner)){
				sb.append(getTitleBannerBlock());
			}
			//---------------------------------------- Manage ajax refresh-------------------------------------
			if(ControllerUtil.getMenuAttribute(tableName+"_first_act", request) == null){
				String action = ControllerUtil.getCtrlAction(request);
				ControllerUtil.setMenuAttribute(tableName+"_first_act", action, request);
			}
			//-------------------------------------------------------------------------------------------------
		}
		
		// Start body table
		sb.append("<div id=\"corp_"+tableName+"\">");

		// Pager banner
//		if (StringUtil.isTrueOrNull(paginate)){
		request.setAttribute("show_paginate", StringUtil.isTrueOrNull(paginate));
		request.setAttribute("is_enabDis", StringUtil.isTrue(showDataState));
		request.setAttribute("is_filtrable", StringUtil.isTrueOrNull(filtrable));
		
			RequestTableBean cplxTable = ControllerUtil.getRequestTableBean(tableName, request);
			if(ComponentUtil.isBodyTableAction(pageContext) 
					|| (cplxTable.getDataSize() > 0) 
					|| StringUtil.isTrue(showDataState)// If filter dis/at is added
					|| StringUtil.isTrue(forceFilter)){
				request.setAttribute("pagerBean_name", this.getName());
				request.setAttribute("pagerBean_width", width);
				request.setAttribute("pagger_table_act", this.initAction);
				
				
				sb.append("<div class=\"tDiv\">\n");
				ComponentUtil.writeComponent(pageContext, sb);
				sb = new StringBuilder("");
				ComponentUtil.insertFragment(pageContext, "/WEB-INF/commun/center/banner_pager.jsp");
				sb.append("</div>\n");
			}
//		}

		if (StringUtil.isTrue(dragable)) {
			sb.append("<style>#"+tableName+"_body tr:HOVER {cursor: move !important;}</style>"
					+ "<input type='hidden' name='"+tableName+"_work_order' id='"+tableName+"_order-h'>");
			
			String initAct = this.initAction;
			initAct = initAct.substring(0, initAct.lastIndexOf(".")) + ".save_order";
			
			scriptSt.append(
				"<script type='text/javascript'>"
				+ "	 $(document).ready(function (){"
						+ "$('#"+tableName+"_body').tableDnD({"+
						"		onDrop: function(table, row) {"+
						"			buildOrder('"+tableName+"', '"+EncryptionUtil.encrypt(initAct)+"');"+
						"	 	}"+
					"});"
				+ "});"
			+ "</script>");
			sb.append(scriptSt);
		}

		// Write component
		ComponentUtil.writeComponent(pageContext, sb);
	}

	/**
	 * @throws JspException
	 */
	@SuppressWarnings("unchecked")
	public void writeEndComponent() throws JspException {
		String tableName = this.getName();
		String jqTableName = this.getJQueryName();
		StringBuilder sb = new StringBuilder();
		StringBuilder scriptSt = new StringBuilder();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		boolean isBodyTableAction = ComponentUtil.isBodyTableAction(pageContext);

		// Add row not element founded
		RequestTableBean cplxTable = ControllerUtil.getRequestTableBean(tableName, request);
		//
		if(isCheckboxFound){
			Map<?, ?> params = (Map)request.getAttribute(ProjectConstante.WORK_PARAMS);
			String valueCheck = (String)params.get(tableName+"_"+ProjectConstante.CHECK_SAVE_STR);
			sb.append("<input type='hidden' name='"+tableName+"_"+ProjectConstante.CHECK_SAVE_STR+"' id='"+tableName+"_"+ProjectConstante.CHECK_SAVE_STR+"' value='" + StringUtil.getValueOrEmpty(valueCheck) + "'/>\n");
		}
		
		// Script ----------------------------------------
		scriptSt.append("manageRefreshTable('"+jqTableName+"'," + StringUtil.isTrueOrNull(getCheckable())+");\n");
		
		//
		this.selectedThIdx = 0;

		// End body div ----
		sb.append("</div>\n");
		
		if(!isBodyTableAction){
			// Add footer block
			sb.append("</div>\n");
			//
			sb.append(getFooterBlock());
			// End flex grid
			sb.append("</div>\n");

			// Add standard box for delete actions script
			if(request.getAttribute("stdbox") == null){
				request.setAttribute("stdbox", "ok");
			}
		}
		// Write component
		ComponentUtil.writeComponent(pageContext, sb);

		// Context menu and export
		if(!isBodyTableAction){
			// If exportable
			if (StringUtil.isTrueOrNull(exportable) && (cplxTable.getDataSize() > 0)) {
				List<ExportTableBean> listExport = (List<ExportTableBean>)request.getAttribute("export_beans");
				if(listExport == null){
					listExport = new ArrayList<ExportTableBean>();
				}
				//
				ExportTableBean exportTable = new ExportTableBean();
				exportTable.setTitle(getTitleOrTitleKey());
				exportTable.setTableName(tableName);
				exportTable.setListColumn(listTh);
				exportTable.setListColumnAdded(listExportTh);
				//
				listExport.add(exportTable);
				request.setAttribute("export_beans", listExport);
			}
			
			if (StringUtil.isTrueOrNull(filtrable) && (cplxTable.getDataSize() > 0) || StringUtil.isTrue(forceFilter)) {
				request.setAttribute("filtrable_beans", this.listFilterCondition);
				request.setAttribute("filtrable_table", this.getName());
			}
		}
		// Add javascript
		ComponentUtil.writeComponent(pageContext, ComponentUtil.getJavascriptBloc(scriptSt));
	}

	/**
	 *
	 */
	private void releaseTable() {
		super.release();
		//
		this.thCells = null;
		this.thBodyCells = null;
		this.listFilterCondition = null;
//		this.conditionCells = null;
		this.listTh = null;
		this.listExportTh = null;
		this.listTrComponent = null;
		this.listFotterExcludeButton = null;
		this.listFooterAddedButton = null;
		this.listThShow = null;
		this.selectedThIdx = 0;
		this.idxAlternate = 0;
		this.isCheckboxFound = false;
		this.width = null;
		this.showDataState = null;
		// Boolean
		this.sortable = null;
		this.dragable = null;
		this.filtrable = null;
		this.checkable = null;
		this.alternate = null;
		this.paginate = null;
		this.exportable = null;
		this.transitionType = null;
		this.trHeight = null;
		this.align = null;
	}
	
	/**
	 * @param newController
	 */
	public void setFooterController(String newController){
		this.footerController = newController;
	}

	/**
	 * Get value from component stocked in session map
	 * @throws JspException
	 */
	@SuppressWarnings("unchecked")
	private void valuateFromGui() throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		Map<String, Map<String, Object>> componentMap = (Map<String, Map<String, Object>>)ControllerUtil.getMenuAttribute(ProjectConstante.MAP_COMPONENT, request);
		if(componentMap != null){
			Map<String, Object> methodesMap = componentMap.get(getName());
			if(methodesMap != null){
				for(String key : methodesMap.keySet()){
					List listValues = (List)methodesMap.get(key);
					//
					try {
						for(Object passedValue : listValues){
							ReflectUtil.callComponentFieldOrMethode(this, key, passedValue);
						}
					} catch (Exception e) {
						throw new JspException(e);
					}
				}
			}
		}
	}

	/**
	 * Add footer icons
	 * @return
	 * @throws JspTagException
	 */
	private String getFooterBlock() throws JspTagException{
		StringBuilder sb = new StringBuilder();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String controller = (this.footerController == null) ? (String)request.getAttribute(ProjectConstante.WORK_CONTROLLER) : this.footerController;
		String tableName = getName();
		boolean isCheckableAndData = false;
		boolean isReadOnly = ComponentUtil.isReadOnlyFormSetted(request);
		//
		String frameHeight = (String)request.getAttribute("frameHeight");
		String frameWidth = (String)request.getAttribute("frameWidth");
		//
		boolean isFullTable = "full".equals(transitionType);

		// Is checkable
		if(StringUtil.isTrueOrNull(checkable)){
			RequestTableBean cplxTable = ControllerUtil.getRequestTableBean(this.getName(), request);
			if(cplxTable.getDataSize() > 0){
				isCheckableAndData = true;
			}
		}
		// Build table footer
		TableFooterBanner tableFooter = new TableFooterBanner(this, frameHeight, frameWidth, tableName, controller, isFullTable);

		// Quick serch banner
		sb.append(
		// Export, delete group, update group ... banner
		"<div class=\"pDiv\">\n" +
		"<table width='100%'><tr>\n" +
		"<td align='left'>\n");
		
		if(StringUtil.isTrue(dragable)){
			sb.append("<span style='color: #d73d32;'>** Glisser les lignes pour changer l'ordre.</span>");
		}
		
		// Add element
		if(!isReadOnly && isFullTable && !listFotterExcludeButton.contains(FooterButtonEnum.CREATE)){
			sb.append(tableFooter.getCreateScript());
		}
		 //
		sb.append("</td>\n" +
		"<td>&nbsp;</td>\n");
		
		sb.append("<td align='right'>\n");
		if(isCheckableAndData){
			//*--------------------------- Added button---------------------------------
			for (BannerTableButton BannerTableButton : this.listFooterAddedButton) {
				String defaultClass = "link";
				String imgAdd = "<img src=\""+BannerTableButton.getStyleClass()+"\" border=\"0\" class='link-footer-table'/>\n";
				sb.append("<a href=\"javascript:\" onClick=\"submitAjaxForm('"+BannerTableButton.getAction()+"');\" class=\""+defaultClass+"\" title=\""+BannerTableButton.getTooltip()+"\">\n" +
						imgAdd + 
						"</a>");
			}
			// Delete group img
			if(!isReadOnly){
				if(!listFotterExcludeButton.contains(FooterButtonEnum.DELETE_ROWS)){
					boolean isDeleteAvailable = Context.isOperationAvailable(MenuMappingService.RIGHT_ENUM.RIGHT_DELETE);
					if(isDeleteAvailable){
						sb.append(tableFooter.getDeleteRowsScript());
					}
				}
			}
		}
		
		// Export
		if(StringUtil.isTrueOrNull(exportable) && !listFotterExcludeButton.contains(FooterButtonEnum.EXPORT)){
			sb.append("&nbsp;&nbsp;"+tableFooter.getExportRowsScript());
		}
		
		//
		sb.append("</td>\n" +
		"</tr>\n</table>\n" +
		"</div>\n");

		return sb.toString();
	}

	/**
	 * @return
	 */
	private String getTitleBannerBlock(){
		// Title banner
		StringBuilder sb = new StringBuilder();
		String titleTable = getTitleOrTitleKey();
		//
		sb.append("<div class=\"mDiv\">\n" +
			"<div class=\"ftitle\">\n" +
			"<table width=\"100%\">\n<tr><td align=\"left\">\n" + titleTable + "</td>\n" +
			"<td align=\"right\">"
//			+ "<a href=\"#\">" +
//			"<img border=\"0\" src=\"resources/framework/img/table/hide_rcondition.gif\" title=\"Afficher le tableau\" id=\""+getName()+"_hidetable\">" +
//			"</a>"
			+ "</td></tr></table>\n" +
		"</div></div>\n");

		return sb.toString();
	}

	public String getSortable() {
		return sortable;
	}

	public void setSortable(String sortable) {
		this.sortable = sortable;
	}

	public String getDragable() {
		return dragable;
	}

	public void setDragable(String dragable) {
		this.dragable = dragable;
	}

	public String getFiltrable() {
		return filtrable;
	}

	public void setFiltrable(String filtrable) {
		this.filtrable = filtrable;
	}

	public String getAlternate() {
		return alternate;
	}

	public void setAlternate(String alternate) {
		this.alternate = alternate;
	}

	public int getIdxAlternate() {
		return idxAlternate;
	}

	public void setIdxAlternate(int idxAlternate) {
		this.idxAlternate = idxAlternate;
	}

	/**
	 * @return the width
	 */
	public String getWidth() {
		if (this.width != null) {
			return " width='" + this.width + "'";
		} else {
			return "";
		}
	}

	/**
	 * @return the width
	 */
	public String getWidthStyle() {
		if (this.width != null) {
			return " width:" + this.width+"px;";
		} else {
			return "";
		}
	}

	public String getSimpleWidth(){
		return StringUtil.getValueOrEmpty(this.width);
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(String width) {
		this.width = width;
	}

	public int getSelectedThIdx() {
		return selectedThIdx;
	}

	public void setSelectedThIdx(int selectedThIdx) {
		this.selectedThIdx = selectedThIdx;
	}

	public boolean isCheckboxFound() {
		return isCheckboxFound;
	}

	public void setCheckboxFound(boolean isCheckboxFound) {
		this.isCheckboxFound = isCheckboxFound;
	}

//	public StringBuilder getFilterCells() {
//		return filterCells;
//	}
//
//	public void setFilterCells(StringBuilder filterCells) {
//		this.filterCells = filterCells;
//	}

	/**
	 * @return the thBlock
	 */
	public StringBuilder getThBlock() {
		return thCells;
	}

	/**
	 * @param thBlock the thBlock to set
	 */
	public void setThBlock(StringBuilder thBlock) {
		this.thCells = thBlock;
	}
	/**
	 * @return the checkable
	 */
	public String getCheckable() {
		return checkable;
	}

	public List<ColumnsExportBean> getListFilterCondition() {
		return listFilterCondition;
	}

	/**
	 * @param checkable the checkable to set
	 */
	public void setCheckable(String checkable) {
		this.checkable = checkable;
	}

	/**
	 * @return the paginate
	 */
	public String getPaginate() {
		return paginate;
	}

	/**
	 * @param paginate the paginate to set
	 */
	public void setPaginate(String paginate) {
		this.paginate = paginate;
	}

	/**
	 * @return the thCells
	 */
	public StringBuilder getThCells() {
		return thCells;
	}

	/**
	 * @param thCells the thCells to set
	 */
	public void setThCells(StringBuilder thCells) {
		this.thCells = thCells;
	}

	public String getExportable() {
		return exportable;
	}

	public void setExportable(String exportable) {
		this.exportable = exportable;
	}

	/**
	 * @return the listTh
	 */
	public List<ColumnsExportBean> getListTh() {
		return listTh;
	}
	/**
	 * @return the listExportTh
	 */
	public List<ColumnsExportBean> getListExportTh() {
		return listExportTh;
	}

	/**
	 * @param listExportTh the listExportTh to set
	 */
	public void setListExportTh(List<ColumnsExportBean> listExportTh) {
		this.listExportTh = listExportTh;
	}

	public StringBuilder getThBodyCells() {
		return thBodyCells;
	}

	public void setThBodyCells(StringBuilder thBodyCells) {
		this.thBodyCells = thBodyCells;
	}

	public List<String> getListThShow() {
		return listThShow;
	}

	public void setListThShow(List<String> listThShow) {
		this.listThShow = listThShow;
	}

	public List<String> getListTrComponent() {
		return listTrComponent;
	}

	public void setListTrComponent(List<String> listTrComponent) {
		this.listTrComponent = listTrComponent;
	}

	/**
	 * @param listFotterButton the listFotterButton to set
	 */
	public void removeFotterButton(FooterButtonEnum fotterButton) {
		this.listFotterExcludeButton.add(fotterButton);
	}
	public String getTransitionType() {
		return transitionType;
	}

	public void setTransitionType(String transitionType) {
		this.transitionType = transitionType;
	}

	public String getDragableRows() {
		return dragableRows;
	}

	public void setDragableRows(String dragableRows) {
		this.dragableRows = dragableRows;
	}

	public String getTrHeight() {
		return trHeight;
	}

	public void setTrHeight(String trHeight) {
		this.trHeight = trHeight;
	}

	public boolean isUpdateTrCondition() {
		return isUpdateTrCondition;
	}

	public void setUpdateTrCondition(boolean isUpdateTrCondition) {
		this.isUpdateTrCondition = isUpdateTrCondition;
	}

	public boolean isDeleteTrCondition() {
		return isDeleteTrCondition;
	}

	public void setDeleteTrCondition(boolean isDeleteTrCondition) {
		this.isDeleteTrCondition = isDeleteTrCondition;
	}
	

	/**
	 * @param buttonType
	 */
	public void addFooterButton(BannerTableButton BannerTableButton){
		this.listFooterAddedButton.add(BannerTableButton);
	}

	public String getAutoHeight() {
		return autoHeight;
	}

	public void setAutoHeight(String autoHeight) {
		this.autoHeight = autoHeight;
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

	public String getInitAction() {
		return initAction;
	}

	public void setInitAction(String initAction) {
		this.initAction = initAction;
	}

	public String getShowTitleBanner() {
		return showTitleBanner;
	}

	public void setShowTitleBanner(String showTitleBanner) {
		this.showTitleBanner = showTitleBanner;
	}

	public String getForceFilter() {
		return forceFilter;
	}

	public void setForceFilter(String forceFilter) {
		this.forceFilter = forceFilter;
	}
	public String getShowDataState() {
		return showDataState;
	}

	public void setShowDataState(String showDataState) {
		this.showDataState = showDataState;
	}
}
