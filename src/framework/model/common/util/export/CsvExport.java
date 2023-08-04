package framework.model.common.util.export;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import framework.component.complex.table.export.ExportTableBean;
import framework.controller.bean.ColumnsExportBean;
import framework.model.common.util.ControllerBeanUtil;
import framework.model.common.util.StringUtil;

public class CsvExport {
	private static final String CSV_SEPARATOR = ";";

	/**
	 * @param request
	 * @param response
	 * @param context
	 * @throws IOException 
	 */
	public static void execute(ExportTableBean exportBean, OutputStream fileOutputStream) throws IOException{
		StringBuilder sb = new StringBuilder();
		// Header content
		List<ColumnsExportBean> listColumn = exportBean.getListColumn();
		int size = listColumn.size();
		int idx = 1;
		for(ColumnsExportBean columnBean : listColumn){
			sb.append(columnBean.getLabel() + (idx < size ? CSV_SEPARATOR : ""));
			idx++;
		}
		sb.append("\n");
		
		// Body content
		idx = 1;
		// Get export data
		List dataExport = exportBean.getDataExport();
		// Calculate body columns
		if(dataExport != null){
			for(Object bean : dataExport){
				String aliasBean = ControllerBeanUtil.getAliasBeanByObject(bean);
				//
				for(ColumnsExportBean columnBean : listColumn){
					String valueSt = ExportUtil.getValue(columnBean, bean, aliasBean);
					sb.append(StringUtil.getValueOrEmpty(valueSt) + (idx < size ? CSV_SEPARATOR : ""));
					idx++;
				}
				sb.append("\n");
				idx = 1;
			}
		}
		//
		fileOutputStream.write(sb.toString().getBytes());
	}	
}