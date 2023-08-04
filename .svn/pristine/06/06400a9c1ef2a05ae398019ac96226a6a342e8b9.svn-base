package framework.model.common.util.export;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import framework.component.complex.table.RequestTableBean;
import framework.component.complex.table.export.ExportTableBean;
import framework.controller.ControllerUtil;
import framework.controller.bean.ColumnsExportBean;
import framework.model.common.service.FrameworkMessageService;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;

public class ExportTable {

	private final static Logger LOGGER = Logger.getLogger(ExportTable.class);

	public static void execute(HttpServletRequest request, HttpServletResponse response, ServletContext context) throws Exception{
		String exportFormat = ControllerUtil.getParam(request, "work_export_format");
		String exportTitle = ControllerUtil.getParam(request, "work_export_title");
		String exportOrder = ControllerUtil.getParam(request, "work_export_order");

		// Pour regler le probleme de l'encodage
		exportTitle = new String(exportTitle.getBytes("ISO8859-1"),"UTF-8");
		//
		ExportTableBean exportBean = new ExportTableBean();
		List<ColumnsExportBean> listColumn = new ArrayList<ColumnsExportBean>();
		// Table data
		String tableName = ControllerUtil.getParam(request, "tableExportName");
		RequestTableBean cplxTable = ControllerUtil.getRequestTableBean(tableName, request);
		List dataExport = cplxTable.getDataExport();

		//
		String[] orderArray = StringUtil.getArrayFromStringDelim(exportOrder, "|");
		for(String id : orderArray){
			String type  = ControllerUtil.getParam(request, id+"_" + "work_export_type");
			if(type != null){
				String value = ControllerUtil.getParam(request, id+"_" + "work_export_label");
				String field = ControllerUtil.getParam(request, id+"_" + "work_export_field");
				String width = ControllerUtil.getParam(request, id+"_" + "work_export_width");
				//String group = ControllerUtil.getParam(request, id+"_" + "work_export_group");
				// Pour regler le probleme de l'encodage
//				value = new String(value.getBytes("ISO8859-1"),"UTF-8");
				//
				ColumnsExportBean column = new ColumnsExportBean();
				column.setLabel(value);
				column.setField(field);
				column.setWidth(width);
				column.setType(type);

			/*	if(group != null){
					String[][] groupValues = StringUtil.stringDelimToArray(group);
					column.setGroupValues( groupValues);
				}*/
				//
				listColumn.add(column);
			}
		}
		//
		exportBean.setListColumn(listColumn);
		exportBean.setTitle(exportTitle);
		exportBean.setDataExport(dataExport);
		// Type
		exportBean.setType(exportFormat);

		// Stop if no columns
		if(listColumn.size() <= 0){
			return;
		}

		// Dispach to type export
		FileOutputStream fileOutputStream = null;
		File tempFile = null;
		//
		try {
			String TEMP_FILE_PATH 	= StrimUtil._GET_PATH("temp");
			tempFile = ExportUtil.getFullExportTempFileName(TEMP_FILE_PATH, null, exportBean.getType());
			fileOutputStream = new FileOutputStream(tempFile);
			//
			if("pdf".equals(exportFormat)){
				PdfExport.execute(exportBean, fileOutputStream);
			} else if("xls".equals(exportFormat)){
				ExcelExport.execute(exportBean, fileOutputStream);
			} else if("csv".equals(exportFormat)){
				CsvExport.execute(exportBean, fileOutputStream);
			} else if("txt".equals(exportFormat)){
				TextExport.execute(exportBean, fileOutputStream);
			}
			// Export file
			ControllerUtil.doDownload(request, response, tempFile);
		} catch (Exception e) {
			LOGGER.error("Erreur technique : --->" , e);
			FrameworkMessageService.addTechnicalErrorMessage();

		} finally{
			try {
				if(fileOutputStream != null){
					fileOutputStream.close();
				}
				if(tempFile != null){
					tempFile.delete();
				}
			} catch (IOException e) {
				LOGGER.error("Erreur technique : --->" , e);
				FrameworkMessageService.addTechnicalErrorMessage();
			}
		}
		//
		cplxTable.setDataExport(null);
		exportBean = null;
	}
}
