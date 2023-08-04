package framework.component.complex.table.export;

import java.util.List;

import framework.controller.bean.ColumnsExportBean;

public class ExportTableBean {
	private List<ColumnsExportBean> listColumn;
	private List<ColumnsExportBean> listColumnAdded;
	private String title;
	private String tableName;
	private String type;
	private List dataExport;

	public List<ColumnsExportBean> getListColumn() {
		return listColumn;
	}
	public void setListColumn(List<ColumnsExportBean> listColumn) {
		this.listColumn = listColumn;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List getDataExport() {
		return dataExport;
	}
	public void setDataExport(List dataExport) {
		this.dataExport = dataExport;
	}
	public List<ColumnsExportBean> getListColumnAdded() {
		return listColumnAdded;
	}
	public void setListColumnAdded(List<ColumnsExportBean> listColumnAdded) {
		this.listColumnAdded = listColumnAdded;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
