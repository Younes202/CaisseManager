package framework.model.common.util.export;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import framework.component.complex.table.export.ExportTableBean;
import framework.controller.bean.ColumnsExportBean;
import framework.model.common.util.ControllerBeanUtil;
import framework.model.common.util.NumericUtil;
import framework.model.common.util.StringUtil;

public class TextExport {
	
	private static final int DEFAULT_WIDTH = 120;
	
	/**
	 * @param exportBean
	 * @param fileOutputStream
	 * @throws IOException
	 */
	public static void execute(ExportTableBean exportBean, OutputStream fileOutputStream) throws IOException{
		//
		String separator = "|";
		// Calculate header columns
		String headerColumn = "", lineSeparator = "";
		List<ColumnsExportBean> listColumn = exportBean.getListColumn();
		int [] columsWidth = new int[listColumn.size()];
		int idx = 0;
		for(ColumnsExportBean columnBean : listColumn){
			String width = columnBean.getWidth();
			String valueWidthBlank = getValueWidthBlank(columnBean.getLabel(), width, true);
			headerColumn = headerColumn + separator + valueWidthBlank;
			// Line separator
			lineSeparator = lineSeparator + separator;
			// Add separator
			for(int i=0; i<valueWidthBlank.length(); i++){
				lineSeparator = lineSeparator + "-";
			}
			// Save column width blank lenght
			columsWidth[idx] = valueWidthBlank.length();
			idx++;
		}
		lineSeparator = lineSeparator + separator;
		
		// Calculate body columns
		String bodyColumn = "";
		// Get export data
		List dataExport = exportBean.getDataExport();
		// Calculate body columns
		if(dataExport != null){
			idx = 0;
			for(Object bean : dataExport){
				String aliasBean = ControllerBeanUtil.getAliasBeanByObject(bean);
				//
				for(ColumnsExportBean columnBean : listColumn){
					String valueSt = ExportUtil.getValue(columnBean, bean, aliasBean);
					// Adjust value width
					String valueWidthBlank = getValueWidthBlank(StringUtil.getValueOrEmpty(valueSt), columnBean.getWidth(), false);
					int valueBlankLength = valueWidthBlank.length();
					if(valueBlankLength > columsWidth[idx]){
						valueWidthBlank = valueWidthBlank.substring(0, (valueBlankLength-1));
					} else if(valueBlankLength < columsWidth[idx]){
						valueWidthBlank = valueWidthBlank + " ";
					}
					//
					bodyColumn = bodyColumn + separator + valueWidthBlank;
					idx++;
				}
				bodyColumn = bodyColumn + separator + "\r\n";
				//
				idx = 0;
			}
		}
		//
		String fullColumns = lineSeparator + "\r\n" + headerColumn + separator + "\r\n"  + lineSeparator + "\r\n" + bodyColumn + lineSeparator;
		//
		fileOutputStream.write(fullColumns.getBytes());
	}
	
	/**
	 * @param value
	 * @param width
	 * @return
	 */
	private static String getValueWidthBlank(String value, String width, boolean isHeader){
		int columnWidth = DEFAULT_WIDTH;
		//
		if(StringUtil.isNotEmpty(width)){
			if(width != null){
				width = width.replaceAll("px", "");
				width = width.replaceAll("%", "");
			}
			
			columnWidth = NumericUtil.getIntOrDefault(width);
		}
		// Adjust width
		columnWidth = columnWidth / 6;
		
		// Value
		int valueLenght = StringUtil.isEmpty(value) ? 1 : value.length();
		int diff = columnWidth - valueLenght;
		// Calculate
		if(diff >= 0){
			int nbrBlankL = diff/2;
			int nbrBlankR = diff/2;
			
			while(((nbrBlankL+nbrBlankR) + valueLenght)  > columnWidth){
				nbrBlankL = nbrBlankL - 1;
			}
			while(((nbrBlankL+nbrBlankR) + valueLenght)  < columnWidth){
				nbrBlankL = nbrBlankL + 1;
			}
			// 
			String valueBlankL = "", valueBlankR = ""; 
			for(int i=0; i<nbrBlankL; i++){
				valueBlankL = valueBlankL + " ";
			}
			for(int i=0; i<nbrBlankR; i++){
				valueBlankR = valueBlankR + " ";
			}
			
			if(isHeader){
				value = valueBlankL + value + valueBlankR;
			} else{
				value = value + valueBlankL + valueBlankR;
			}
		} else{
			value = value.substring(0, (columnWidth-3)) + "...";
		}
		
		return value;
	}
}
