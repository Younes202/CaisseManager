package framework.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;

public class FileUtilController {
	private final static Logger LOGGER = Logger.getLogger(FileUtilController.class);
	public static final int TIMEOUT_URL_CALL = 3*1000;// 5 secondes
	/**
	 * @param targetPath
	 * @param fieldName
	 * @return
	 */
	public static byte[] getFileToDownLoad(String path, Long elementId, String fieldName){
		path = StrimUtil.BASE_FILES_PATH+"/"+path + "/" + StringUtil.getValueOrEmpty(elementId);
		
		File file = new File(path + (StringUtil.isEmpty(fieldName) ? "" :("/"+fieldName)));
		
		try {
			return IOUtils.toByteArray(file.toURI());
		} catch (IOException e) {
			LOGGER.error("Erreur : ", e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * @param request
	 * @param fieldName
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public static void removeUplodedStreamsFromSession(HttpServletRequest request, String fieldName){
		Map<String, byte[]> mapStream = (Map<String, byte[]>)ControllerUtil.getMenuAttribute(fieldName, request);
		ControllerUtil.removeMenuAttributeIgnorePopup(fieldName, request);
	}
	
	/**
	 * @param request
	 * @param fieldName
	 * @param sourcePath
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, byte[]> uploadFilesToStrteamMap(HttpServletRequest request, String fieldName, String path, Long elementId){
		path = StrimUtil.BASE_FILES_PATH+"/"+path + "/" + StringUtil.getValueOrEmpty(elementId);
		File folder = new File(path);
		Map<String, byte[]> mapStream = (Map<String, byte[]>)ControllerUtil.getMenuAttribute(fieldName, request);		
		if(mapStream == null){
			mapStream = new HashMap<String, byte[]>();
			ControllerUtil.setMenuAttribute(fieldName, mapStream,request);
		}
		
		try {
			if(folder.listFiles() != null){
				for (final File file : folder.listFiles()) {
					mapStream.put(file.getName(), IOUtils.toByteArray(file.toURI()));
				}
			}
		} catch (IOException e) {
			LOGGER.error("Erreur : ", e);
			throw new RuntimeException(e);
		}
		
		return mapStream;
	}
	
	/**
	 * @param request
	 * @param fieldName
	 * @param path
	 * @param elementId
	 * @return
	 */
	public static String[] getFilesNames(HttpServletRequest request, String fieldName, String path, Long elementId){
		// Chargement pour affichage
		String[] piecesJointes = null;
		Map<String, byte[]> mapFiles = FileUtilController.uploadFilesToStrteamMap(request, fieldName, path, elementId);
		if(mapFiles != null && mapFiles.size() > 0){
			piecesJointes = new String[mapFiles.size()];
			int idx = 0;
			for(String fileName : mapFiles.keySet()){
				piecesJointes[idx]= fileName;
				idx++;
			}
		}
		
		return piecesJointes;
	}
	
	/**
	 * @param path
	 */
	public static void clearDir(String path, Long elementId){
		path = StrimUtil.BASE_FILES_PATH+"/"+path + "/" + StringUtil.getValueOrEmpty(elementId);
		File folder = new File(path);
		
		if(folder.listFiles() != null){
			for (final File file : folder.listFiles()) {
				file.delete();
			}
		}
	}
	
	/**
	 * @param path
	 * @return
	 */
	public static Map<String, String> getFileInfos(String path){
		path = StrimUtil.BASE_FILES_PATH+"/"+path;
		Map<String, String> mapInfos = new HashMap<String, String>();
		File file = new File(path);
		if(!file.isFile()){
			return mapInfos;
		}
		
		try {
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
			mapInfos.put("creationTime", df.format(attr.creationTime().toMillis()));
			mapInfos.put("lastAccessTime", df.format(attr.lastAccessTime().toMillis()));
			mapInfos.put("lastModifiedTime", df.format(attr.lastModifiedTime().toMillis()));
			
			String size = null;
			long sizeB = attr.size();
			long sizeKB = sizeB / 1024;
			
			if(sizeKB > 1024){
				size = (sizeKB / 1024) + " Mo";
			} else{
				size = sizeKB + " Ko";
			}
			mapInfos.put("size", ""+size);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return mapInfos;
	}
	
	/**
	 * @param path
	 * @param newName
	 * @return
	 */
	public static boolean renameFile(String path, String oldeName, String newName){
		File file = new File(StrimUtil.BASE_FILES_PATH+"/"+path+"/"+StringUtil.getValueOrEmpty(oldeName));
		File newFile = new File(StrimUtil.BASE_FILES_PATH+"/"+path+"/"+StringUtil.getValueOrEmpty(newName));
		return file.renameTo(newFile);
	}
	
	/**
	 * @param originePath
	 * @param destPath
	 * @param isFolder
	 * @return
	 */
	public static boolean moveFile(String originePath, String destPath, boolean isFolder){ 
		originePath = StrimUtil.BASE_FILES_PATH + "/" + originePath;
		destPath = StrimUtil.BASE_FILES_PATH + "/" + destPath;
		try {
			if(isFolder){
				FileUtils.moveDirectoryToDirectory(new File(originePath), new File(destPath), false);
			} else{
				FileUtils.moveFileToDirectory(new File(originePath), new File(destPath), false);
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param path
	 * @return
	 */
	public static boolean AddDirOrFile(String path){
		path = StrimUtil.BASE_FILES_PATH + "/" + path;
		File newDir = new File(path);
		
		return  newDir.mkdir();
	}
	
	/**
	 * @param path
	 * @return
	 */
	public static boolean deleteFile(String path){
		File file = new File(StrimUtil.BASE_FILES_PATH+"/"+path);
		if(file.isFile()){
			return file.delete();	
		}
		return false;
	}
	
	/**
	 * @param path
	 */
	public static boolean clearDir(String path){
		File file = new File(StrimUtil.BASE_FILES_PATH+"/"+path);
		try {
			FileUtils.deleteDirectory(file);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			
			try {
				FileUtils.deleteDirectory(file);
				return true;
			} catch (IOException e1) {
				return false;
			}
		}
	}
	
	/**
	 * @param request
	 * @param fieldName
	 * @param targetPath
	 */
	@SuppressWarnings("unchecked")
	public static void uploadFilesToDir(HttpServletRequest request, String fieldName, String path, Long elementId){
		path = StrimUtil.BASE_FILES_PATH+"/"+path;
		
		// Purge
		if(StringUtil.isNotEmpty(elementId)){
			clearDir(path, elementId);
		}
		
		// Copie
		Map<String, byte[]> mapStream = (Map<String, byte[]>)ControllerUtil.getMenuAttribute(fieldName, request);
		if(mapStream == null){
			return;
		}
		
		path = path  + "/" + StringUtil.getValueOrEmpty(elementId);
		try {
		    File file = new File(path);
		    // Create directory if not exist
			if(!file.exists()){
				//file.mkdir();
				FileUtils.forceMkdir(file);
			}
	
			for (String fileName : mapStream.keySet()) {
				FileOutputStream fos = new FileOutputStream(path+"/"+fileName);
				fos.write(mapStream.get(fileName));
				fos.close();
			}
		} catch (Exception e) {
			LOGGER.error("Erreur : ", e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * @param path
	 * @param elementId
	 * @return
	 */
	public static String getFilesCount(String path, Long elementId) {
		path = StrimUtil.BASE_FILES_PATH+"/"+path + "/" + elementId;
		File folder = new File(path);
		
		if(folder.listFiles() != null){
			return ""+folder.listFiles().length;
		} else{
			return "";
		}
	}

	/**
	 * @param path
	 * @return
	 */
	public static int getEspaceUtilise(String path){
		Long tailleReelle = new Long(0);
		path = StrimUtil.BASE_FILES_PATH + "/" + path;
		File file = new File(path);
		if(file.isDirectory()){
			Long uploadDirSize = FileUtils.sizeOfDirectory(file);
			tailleReelle = (uploadDirSize / 1024) / 1024;
		}
		
		return tailleReelle.intValue();
	}
	
	/**
	 * @param path
	 * @param file
	 */
	public static void copyGeneratedFiles(String path, File file){
		String resourcesPath = StrimUtil.BASE_FILES_PATH + "/" + path;

		try {
			File dirTarget = new File(resourcesPath.substring(0, resourcesPath.lastIndexOf("/")));
			if(!dirTarget.isFile()){
				dirTarget.mkdirs();
			}
			
			File newFile = new File(resourcesPath);
			newFile.createNewFile();
			InputStream targetStream = new FileInputStream(file); 
			Files.copy(targetStream, newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static boolean isInternetAvailable() {
	    try {
	    	String cloudUrl = StrimUtil.getGlobalConfigPropertie("caisse.cloud.url")+"/update";
	        final URL url = new URL("https://www.google.com");
	        final URLConnection conn = url.openConnection();
	        conn.connect();
	        conn.getInputStream().close();
	        return true;
	    } catch (MalformedURLException e) {
	    	return false;
	    } catch (IOException e) {
	        return false;
	    }
	}
	
	/**
	 * @param targetURL
	 * @param urlParameters
	 * @return
	 * @throws IOException 
	 */
	public static String callURL(String myURL) throws IOException {
	    URL url = null;
	    BufferedReader reader = null;
	    StringBuilder sb;
	    
	    try {
	      // create the HttpURLConnection
	      url = new URL(myURL);
	      
	      URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
	      String urlStr = uri.toASCIIString();
	      url = new URL(urlStr);
	      
	      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	      
	      // just want to do an HTTP GET here
	      connection.setRequestMethod("GET");
	      
	      // uncomment this if you want to write output to this url
	      //connection.setDoOutput(true);
	      
	      // give it 10 seconds to respond
	      connection.setReadTimeout(FileUtilController.TIMEOUT_URL_CALL);
	      
	      connection.connect();

	      // read the output from the server
	      reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	      sb = new StringBuilder();

	      String line = null;
	      while ((line = reader.readLine()) != null){
	        sb.append(line);
	      }
	      return sb.toString();
	    } catch (Exception e){
	    	System.out.println("URL==>"+url+" : "+e.getMessage());
	    	throw new RuntimeException(e);
	    } finally{
	      // close the reader; this can throw an exception too, so
	      // wrap it in another try/catch block.
	      if (reader != null) {
	        try {
	          reader.close();
	        } catch (IOException ioe){
	        	throw ioe;
	        }
	      }
	    }
	  }
	
	/**
	 * @param fileSource
	 * @param fileTarget
	 */
	public static void copyFile(File fileSource, File fileTarget){
		InputStream inStream = null;
		OutputStream outStream = null;
		try {
			inStream = new FileInputStream(fileSource);
    	    outStream = new FileOutputStream(fileTarget);

    	    byte[] buffer = new byte[1024];
    	    int length;
    	    //copy the file content in bytes
    	    while ((length = inStream.read(buffer)) > 0){
    	    	outStream.write(buffer, 0, length);
    	    }
    	    inStream.close();
    	    outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
