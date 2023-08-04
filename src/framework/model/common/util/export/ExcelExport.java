package framework.model.common.util.export;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import framework.component.complex.table.export.ExportTableBean;
import framework.controller.bean.ColumnsExportBean;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.util.ControllerBeanUtil;
import framework.model.common.util.NumericUtil;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormat;
import jxl.write.NumberFormats;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExcelExport {

	private static final int DEFAULT_WIDTH = 150;

	/**
	 * @param request
	 * @param response
	 * @param context
	 * @throws WriteException
	 * @throws IOException
	 */
	public static void execute(ExportTableBean exportBean, OutputStream fileOutputStream) throws WriteException, IOException{
	      WorkbookSettings ws = new WorkbookSettings();
	      ws.setLocale(new Locale("fr", "FR"));
	      WritableWorkbook workbook = Workbook.createWorkbook(fileOutputStream, ws);
	      //
	      WritableSheet s = workbook.createSheet("Onglet 1", 0);
	      writeDataSheet(s, exportBean);
	      //
	      workbook.write();
	      workbook.close();
	}

	 /**
	 * @param ws
	 * @throws WriteException
	 */
	private static void writeDataSheet(WritableSheet ws, ExportTableBean exportBean) throws WriteException{
	    //-------------------------------------------------------------------------------
		List<ColumnsExportBean> listColumn = exportBean.getListColumn();

	    /* Column Font */
	    WritableFont cfont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
	    WritableCellFormat ccf = new WritableCellFormat(cfont);
	    ccf.setWrap(true);
	    ccf.setAlignment(Alignment.CENTRE);
		// Body font
	    WritableFont bfont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
	    WritableCellFormat bcf = new WritableCellFormat(bfont);
	    bcf.setWrap(true);
	    bcf.setAlignment(Alignment.LEFT);

	    // Set column widths
	    int idx = 0;
	    for(ColumnsExportBean columnBean : listColumn){
			String width = columnBean.getWidth();
			if(width != null){
				width = width.replaceAll("px", "");
				width = width.replaceAll("%", "");
			}
			int intWidth = StringUtil.isEmpty(width) ? DEFAULT_WIDTH : NumericUtil.getIntOrDefault(width);
    		ws.setColumnView(idx, intWidth/6);
	    	idx++;
	    }
		// Add header columns
		idx = 0;
	    for(ColumnsExportBean columnBean : listColumn){
			//int width = NumericUtil.getInt(columnBean.getWidth());
		    Label l = new Label(idx, 0, columnBean.getLabel(), ccf);
		    ws.addCell(l);
		    idx++;
		}

	    // Add body content
	    int colIdx = 0;
	    int rowIdx = 1;
		// Get export data
		List dataExport = exportBean.getDataExport();
		// Calculate body columns
		if(dataExport != null){
			String dateFormat = StrimUtil.getGlobalConfigPropertie("ddMMyyyy.format");
			 DateFormat customDateFormat = new DateFormat (dateFormat);
			 NumberFormat doubleFormat = new NumberFormat("#.##");
			 WritableCellFormat integerFormat = new WritableCellFormat(bfont, NumberFormats.INTEGER);
			 WritableCellFormat dateCell = new WritableCellFormat(bfont, customDateFormat);
			 WritableCellFormat doubleCell = new WritableCellFormat(bfont, doubleFormat);
			 
			for(Object bean : dataExport){
				String aliasBean = ControllerBeanUtil.getAliasBeanByObject(bean);
				//
				for(ColumnsExportBean columnBean : listColumn){
					Object value = null;
					WritableCell cellValue = null;
					String fieldName = columnBean.getField() ;
					WritableCellFormat cell = null;
					if(StringUtil.isNotEmpty(fieldName)){
						// Field name
						if(fieldName.indexOf(aliasBean) != -1){
							fieldName = fieldName.substring(fieldName.indexOf(".")+1);
						}
						value = ReflectUtil.getObjectPropertieValue(bean, fieldName);
						// Add cell
						if(columnBean.getType().equals(ProjectConstante.TYPE_DATA_ENUM.BOOLEAN.getType())){
							value = StringUtil.isTrue(""+value) ? StrimUtil.label("yes") : StrimUtil.label("no");
							cellValue = new Label(colIdx, rowIdx, ""+value, bcf);
						} else if(columnBean.getType().equals(ProjectConstante.TYPE_DATA_ENUM.DATE.getType())){
							if(StringUtil.isNotEmpty(value)){
								cellValue = new DateTime(colIdx, rowIdx, (Date)value, dateCell);
							}
						} else if(columnBean.getType().equals(ProjectConstante.TYPE_DATA_ENUM.DECIMAL.getType())){
							if(StringUtil.isNotEmpty(value)){
								cellValue = new Number(colIdx, rowIdx, NumericUtil.getDoubleOrDefault(value), doubleCell);
							}
						} else if(columnBean.getType().equals(ProjectConstante.TYPE_DATA_ENUM.LONG.getType())){
							if(StringUtil.isNotEmpty(value) && ((""+value).indexOf(".") == -1)){
								cellValue = new Number(colIdx, rowIdx, NumericUtil.getIntOrDefault(value), integerFormat);
							} else{
								cellValue = new Number(colIdx, rowIdx, NumericUtil.getLongOrDefault(value), doubleCell);
							}
						} else{
							cellValue = new Label(colIdx, rowIdx, StringUtil.getValueOrEmpty(value), bcf);
						}
					}
					//
					if(cellValue == null){
						cellValue = new Label(colIdx, rowIdx, "", bcf);
					}
					// Add cell
					ws.addCell(cellValue);
					//
					colIdx++;
				}
				colIdx = 0;
				rowIdx++;
			}
		}
	    //------------------------------------------------------------------------------------------------



	    /* Creates Label and writes date to one cell of sheet*/
	/*    Label l = new Label(0,0,"Date",cf);
	    ws.addCell(l);
	    WritableCellFormat cf1 = new WritableCellFormat(DateFormats.FORMAT9);
	    DateTime dt = new DateTime(0,1,new Date(), cf1, DateTime.GMT);
	    ws.addCell(dt);
	    /* Creates Label and writes float number to one cell of sheet*/
	/*    l = new Label(2,0,"Float", cf);
	    ws.addCell(l);
	    WritableCellFormat cf2 = new WritableCellFormat(NumberFormats.FLOAT);
	    Number n = new Number(2,1,3.1415926535,cf2);
	    ws.addCell(n);

	    n = new Number(2,2,-3.1415926535, cf2);
	    ws.addCell(n);

	    /* Creates Label and writes float number upto 3
	       decimal to one cell of sheet */
	/*    l = new Label(3,0,"3dps",cf);
	    ws.addCell(l);
	    NumberFormat dp3 = new NumberFormat("#.###");
	    WritableCellFormat dp3cell = new WritableCellFormat(dp3);
	    n = new Number(3,1,3.1415926535,dp3cell);
	    ws.addCell(n);

	    /* Creates Label and adds 2 cells of sheet*/
	/*    l = new Label(4, 0, "Add 2 cells",cf);
	    ws.addCell(l);
	    n = new Number(4,1,10);
	    ws.addCell(n);
	    n = new Number(4,2,16);
	    ws.addCell(n);
	    Formula f = new Formula(4,3, "E1+E2");
	    ws.addCell(f);

	    /* Creates Label and multipies value of one cell of sheet by 2*/
	/*    l = new Label(5,0, "Multipy by 2",cf);
	    ws.addCell(l);
	    n = new Number(5,1,10);
	    ws.addCell(n);
	    f = new Formula(5,2, "F1 * 3");
	    ws.addCell(f);

	    /* Creates Label and divide value of one cell of sheet by 2.5 */
	/*    l = new Label(6,0, "Divide",cf);
	    ws.addCell(l);
	    n = new Number(6,1, 12);
	    ws.addCell(n);
	    f = new Formula(6,2, "F1/2.5");
	    ws.addCell(f);*/
	  }

	  /**
	 * @param s
	 * @throws WriteException
	 */
/*	private static void writeImageSheet(WritableSheet s) throws WriteException{
	    /* Creates Label and writes image to one cell of sheet*/
/*	    Label l = new Label(0, 0, "Image");
	    s.addCell(l);
	    WritableImage wi = new WritableImage(0, 3, 5, 7, new File("C:\\Documents and Settings\\t.\\Bureau\\Nouvelle image.bmp"));
	    s.addImage(wi);

	    /* Creates Label and writes hyperlink to one cell of sheet*/
/*	    l = new Label(0,15, "HYPERLINK");
	    s.addCell(l);
	    Formula f = new Formula(1, 15, "HYPERLINK(\"http://www.andykhan.com/jexcelapi\", \"JExcelApi Home Page\")");
	    s.addCell(f);
    }*/
}
