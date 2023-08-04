package framework.component.complex.table;

import framework.component.ComponentUtil;
import framework.model.common.constante.ActionConstante;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.StrimUtil;

public class TableFooterBanner {
	private String frameHeight;
	private String frameWidth;
	private String tableName;
	private String controller;
	private boolean isFullTable;
	String defaultClass = "link";
	TableTag tableTag;
	
	/**
	 * @param tableTag 
	 * @param frameHeight
	 * @param frameWidth
	 * @param tableName
	 * @param controller
	 * @param isFullTable
	 */
	public TableFooterBanner(TableTag tableTag, String frameHeight, String frameWidth, String tableName, String controller, boolean isFullTable){
		this.frameHeight = frameHeight;
		this.frameWidth = frameWidth;
		this.tableName = tableName;
		this.controller = controller;
		this.isFullTable = isFullTable;
		this.tableTag = tableTag;
	}

	/**
	 * @return
	 */
	public String getCreateScript(){
		String imgAdd = "<img src=\""+ProjectConstante.IMG_ADD_PATH+"\" title=\""+StrimUtil.label("work.add")+"\" border=\"0\" class='link-footer-table'/>\n";
		
		return "<a href=\"javascript:\" onClick=\"showFrameDialog('');\" class=\""+defaultClass+"\">\n" +
				imgAdd + 
				"</a>\n";
	}

	/**
	 * @return
	 */
	public String getDeleteRowsScript(){
		if(!this.tableTag.isDeleteTrCondition()){
			String ctrlAct = controller + "." + ActionConstante.DELETE_ROWS;
			String defaultClass = "link";
			String event = "\"javascript:testCheckedDeleteRows('"+tableName+"', '" + EncryptionUtil.encrypt(ctrlAct) + "', 'nosave=true');\"\n";
	
			return "<a href=" + event + " class='"+defaultClass+"'>\n" +
				   "<img border='0' title='"+StrimUtil.label("button.delete.group")+"' src='resources/framework/img/table/action/delete_group.png' class='link-footer-table'/>\n" +
				   "</a>\n";
		}
		return "";
	}

	/**
	 * @return
	 */
	public String getExportRowsScript(){
		String defaultClass = "link";
		String formatTableName = ComponentUtil.getJQueryName(tableName);
		String event = "\"javascript:showExportBox('"+formatTableName+"', '"+EncryptionUtil.encrypt(tableTag.getInitAction())+"');\"";

		return "<a class=\"btn btn-default btn-xs shiny icon-only success\" onclick=" + event + " title='"+StrimUtil.label("work.export")+"' id='export_img' href=\"javascript:void(0);\" style=\"margin-right:15px;margin-top: 0px;\">"+
			   "<i class=\"fa fa-cloud-upload\"></i>\n" +
			   "</a>\n";
	}

	/**
	 * @return the frameHeight
	 */
	public String getFrameHeight() {
		return frameHeight;
	}

	/**
	 * @param frameHeight the frameHeight to set
	 */
	public void setFrameHeight(String frameHeight) {
		this.frameHeight = frameHeight;
	}

	/**
	 * @return the frameWidth
	 */
	public String getFrameWidth() {
		return frameWidth;
	}

	/**
	 * @param frameWidth the frameWidth to set
	 */
	public void setFrameWidth(String frameWidth) {
		this.frameWidth = frameWidth;
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @return the controller
	 */
	public String getController() {
		return controller;
	}

	/**
	 * @param controller the controller to set
	 */
	public void setController(String controller) {
		this.controller = controller;
	}

	/**
	 * @return the isFullTable
	 */
	public boolean isFullTable() {
		return isFullTable;
	}

	/**
	 * @param isFullTable the isFullTable to set
	 */
	public void setFullTable(boolean isFullTable) {
		this.isFullTable = isFullTable;
	}
}
