package framework.model.common.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import appli.controller.domaine.util_erp.ContextAppli;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.service.MessageService;

public class StrimUtil {
	
	
	// Maps
	public static Properties GLOBAL_CONFIG_MAP;
	private static Properties LABELS_MAP;
	public static String BASE_FILES_PATH = null;

	static{
		GLOBAL_CONFIG_MAP = MappingUtil.getGlobalConfigMap();
		LABELS_MAP = MappingUtil.getLabelsMap();
	}
	
	public static String _GET_PATH(String module) {
		return BASE_FILES_PATH+"/"+ContextAppli.getEtablissementBean().getId()+"/"+module;
	}
	
	private static Map<String, String> MAP_CONVERT_CHAR = createConvMap();
	private static Map<String, String> createConvMap(){
		Map<String, String> mapConvert = new HashMap<String, String>();
    	mapConvert.put("à", "0");
    	mapConvert.put("(", "5");
    	mapConvert.put("\\", "8");
    	mapConvert.put("\"", "3");
    	mapConvert.put("ç", "9");
    	mapConvert.put("é", "2");
    	mapConvert.put("_", "8");
    	mapConvert.put("-", "6");
    	mapConvert.put("'", "4");
    	mapConvert.put("è", "7");
    	mapConvert.put("&", "1");
    	
    	return mapConvert;
	}
	
	 public static String convertStrCardReader(String in) {
		 StringBuilder back = new StringBuilder("");
    	
 		char[] txt = in.toCharArray();    	
    	
 		for(char t  : txt) {
 			String eq = MAP_CONVERT_CHAR.get(""+t);
 			back.append(eq==null?"?":eq);
 		}
 		
 		return back.toString();
     }

	public static Properties getLabelsMap(){
		return LABELS_MAP;
	}

	/**
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String getGlobalConfigPropertie(String key){
		Object value = GLOBAL_CONFIG_MAP.get(key);
		if(value == null){
			//MessageService.addDialogMessage("This key - " + key + " - does n't existe in global config file (.properties)");
			return null;
		}

		return ""+value;
	}
	/**
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String setGlobalConfigPropertie(String key, String value){
		GLOBAL_CONFIG_MAP.put(key, value);

		return ""+value;
	}
	public static String getGlobalConfigPropertieIgnoreErreur(String key){
		Object value = GLOBAL_CONFIG_MAP.get(key);
		if(value == null){
			return null;
		}

		return ""+value;
	}
	/**
	 * @param key
	 * @param params
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public static String label(String key) {
		String label = LABELS_MAP.getProperty(key);
		// Error if label does n't exist
		if(label == null){
			MessageService.addDialogMessage(label("label.error", key));
		}

		return label;
	}

	/**
	 * @param key
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String label(String key, String ... params) {
		String label = label(key);
		// Replace all meta separator
		if((params != null) && params.length > 0){
			int idx = 1;
			for(String st : params){
				int idx_c = label.indexOf("{"+idx+"}");//label.indexOf(ProjectConstante.PARAM_LABEL);
				if(idx_c != -1){
							// Index of {x}					 Index of {x}+{x}.length to end of label
                    label = label.substring(0, idx_c) + st + label.substring(idx_c + ((""+idx).length()+2));
				}
				idx++;
			}
		}

		return label;
	}

	/**
	 * @param nameAndExt
	 * @param addTime
	 * @return
	 * @throws IOException
	 */
//	public static File ceateFile(String baseFolder, String nameAndExt, boolean addTime) throws IOException{
//		String name = nameAndExt;
//		String extension = ".txt";
//		String baseFold = "";
//		int indexOfDot = nameAndExt.indexOf(".");
//		//
//		if(indexOfDot != -1){
//			name = nameAndExt.substring(0, indexOfDot);
//			extension = nameAndExt.substring(indexOfDot);
//		}
//
//		
//		if(StringUtil.isNotEmpty(baseFolder)){
//			baseFold = ProjectConstante.TEMP_FILE_PATH + "/" + baseFolder;
//		} else{
//			baseFold = ProjectConstante.TEMP_FILE_PATH;
//		}
//		
//		//
//		File tempDirFile = new File(baseFold);
//		if(!tempDirFile.exists()){
//			//tempDirFile.mkdirs();
//			FileUtils.forceMkdir(tempDirFile);
//		}
//
//		String fileName = name + (addTime ? "_"+System.nanoTime() : "") + extension;
//		//
//		String fullFileName = baseFold + "/" + fileName;
//		//
//		File tempFile = new File(fullFileName);
//		tempFile.createNewFile();
//
//		return tempFile;
//	}

	/**
	 * @param file
	 * @param html
	 * @return
	 * @throws Exception
	 */
	public static String getLogFileContent(String fullPah, boolean html) throws Exception {
		File file = new File(fullPah);
		
		StringBuilder sb = new StringBuilder();
		// Open the file that is the first
		FileInputStream fstream = new FileInputStream(file);
		// Get the object of DataInputStream
	    DataInputStream in = new DataInputStream(fstream);
	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    String strLine;
	    //Read File Line By Line
	    while ((strLine = br.readLine()) != null){
	    	if(html){
	    		String type = strLine.substring(strLine.indexOf("[")+1, strLine.indexOf("]")).trim();
	    		strLine = strLine.substring(strLine.indexOf("]")+1).trim();
	    		// Build span
	    		if(MSG_TYPE.SUCCES.toString().equals(type)){
	    			strLine = "<span style=\"color: green;\">"+strLine+"</span><br />";
	    		} else if(MSG_TYPE.INFO.toString().equals(type)){
	    			strLine = "<span style=\"color: blue;\">"+strLine+"</span><br />";
	    		} else if(MSG_TYPE.WARNING.toString().equals(type)){
	    			strLine = "<span style=\"color: orange;\">"+strLine+"</span><br />";
	    		} else if(MSG_TYPE.ERROR.toString().equals(type)){
	    			strLine = "<span style=\"color: red;\">"+strLine+"</span><br />";
	    		} else{
	    			strLine = "<span style=\"color: blue;\">"+strLine+"</span><br />";
	    		}
			}
	    	// Add line
	    	sb.append(strLine);
	    }
	    //Close the input stream
	    in.close();

	    if(html){
	    	return sb.toString().replaceAll("\"", "\\\\\"");
	    } else{
	    	return sb.toString();
	    }
	}
	}
