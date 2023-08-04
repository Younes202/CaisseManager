package framework.model.common.util.export;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import framework.controller.bean.ColumnsExportBean;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.util.DateUtil;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;

public class ExportUtil {
	
	/**
	 * @param type
	 * @return
	 * @throws IOException 
	 */
	public static File getFullExportTempFileName(String path, String fileName, String type) throws IOException{
		if(StringUtil.isEmpty(fileName)){
			fileName = "tempFile_" +System.nanoTime()+Math.random();
		}
		//
		String fullFileName = path + "/" + fileName + "." + type;
		//
		File tempFile = new File(fullFileName);
		
		
		// ---------------
		if (!tempFile.getParentFile().exists()){
			tempFile.getParentFile().mkdirs();
		}
		if (!tempFile.exists()){
			tempFile.createNewFile();
		}
		
		return tempFile;
	}
	
	 /**
     *  Sends a file to the ServletResponse output stream.  Typically
     *  you want the browser to receive a different name than the
     *  name the file has been saved in your local database, since
     *  your local names need to be unique.
     *
     *  @param req The request
     *  @param resp The response
     *  @param filename The name of the file you want to download.
     *  @param original_filename The name the browser should receive.
//     */
//    public static void doDownload(HttpServletRequest req, HttpServletResponse resp,
//                             File f) throws IOException {
//        int length   	= 0;
//        ServletOutputStream op 	= resp.getOutputStream();
//        String mimetype 		= req.getServletContext().getMimeType(f.getPath());
//        //  Set the response and go!
//        resp.setContentType( (mimetype != null) ? mimetype : "application/octet-stream" );
//        resp.setContentLength( (int)f.length() );
//        resp.setHeader( "Content-Disposition", "attachment; filename=\"" + f.getName() + "\"" );
//        
//        //  Stream to the requester.
//        byte[] bbuf = new byte[NumericUtil.toInteger(ProjectConstante.MAX_BUFFER_SIZE)];
//        DataInputStream in = new DataInputStream(new FileInputStream(f));
//
//        //
//        while ((in != null) && ((length = in.read(bbuf)) != -1)) {
//            op.write(bbuf,0,length);
//        }
//
//        in.close();
//        op.flush();
//        op.close();
//    }
    
    /**
     * @param columnBean
     * @param bean
     * @param aliasBean
     * @return
     */
    public static String getValue(ColumnsExportBean columnBean, Object bean, String aliasBean){
    	String fieldNames = columnBean.getField() ;
    	//
    	if(StringUtil.isNotEmpty(fieldNames)){
    		String valueSt = "";
    		String[] fieldsName = StringUtil.getArrayFromStringDelim(fieldNames, "|");
    		int idx = 0;
    		for(String fieldName : fieldsName){
    			if(StringUtil.isEmpty(fieldName)){
    				continue;
    			}
	    		//
	    		if(fieldName.indexOf(aliasBean) != -1){
					fieldName = fieldName.substring(fieldName.indexOf(".")+1);
				}
				Object value = ReflectUtil.getObjectPropertieValue(bean, fieldName);
				// Convert to type
				if( StringUtil.isNotEmpty(value)){
					if(columnBean.getType().equals(ProjectConstante.TYPE_DATA_ENUM.BOOLEAN.getType())){
						valueSt = valueSt + (idx!=0?" ":"") + (StringUtil.isTrue(""+value) ? StrimUtil.label("yes") : StrimUtil.label("no"));
					} else if (columnBean.getType().equals(ProjectConstante.TYPE_DATA_ENUM.DATE.getType())){
						valueSt = valueSt + (idx!=0?" ":"") + DateUtil.dateToString((Date)value);
					} else{
						//String[][] groupValues = columnBean.getGroupValues();
						//if(groupValues == null){
							valueSt = valueSt + (idx!=0?" ":"") + StringUtil.getValueOrEmpty(value);
						/*} else{
							for(String[] keyValue : groupValues){
								if(keyValue[0].equals(value)){
									valueSt = keyValue[1];
									break;
								}
							}
						}*/
					}
				}
				idx++;
			}
			return valueSt;
    	}
    	
    	return "";
    }
}
