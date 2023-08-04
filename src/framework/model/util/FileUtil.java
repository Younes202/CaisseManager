package framework.model.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Base64Utils;

import framework.controller.ControllerUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;

public class FileUtil {

	private final static Logger LOGGER = Logger.getLogger(FileUtil.class);
	
	/**
	 * @param targetPath
	 * @param fieldName
	 * @return
	 */
	public static byte[] getFileToDownLoad(String path, Long elementId, String fieldName){
		path = StrimUtil.BASE_FILES_PATH+"/"+ path + "/" + StringUtil.getValueOrEmpty(elementId);
		
		File file = new File(path + (StringUtil.isEmpty(fieldName) ? "" :("/"+fieldName)));
		
		try {
			return IOUtils.toByteArray(file.toURI());
		} catch (IOException e) {
			LOGGER.error("Erreur : ", e);
			throw new RuntimeException(e);
		}
	}
	
	public static Map<String, byte[]> getListFilesByte(String path){ 
		path = StrimUtil.BASE_FILES_PATH+"/"+path;
		File folder = new File(path);
		Map<String, byte[]> data = new HashMap<>();
		try {
			if(folder.listFiles() != null){
				for (final File file : folder.listFiles()) {
					data.put(file.getName(), IOUtils.toByteArray(file.toURI()));
				}
			}
		} catch (IOException e) {
			LOGGER.error("Erreur : ", e);
			throw new RuntimeException(e);
		}
		
		return data;
	}
	
	/**
	 * @param bytes
	 * @param path
	 * @param elementId
	 */
	public static void uploadFilesToDir(byte[] bytes, String path, Integer width, Integer height){
		path = StrimUtil.BASE_FILES_PATH+"/"+path;
		
		File dirTarget = new File(path.substring(0, path.lastIndexOf("/")));
		if(!dirTarget.exists()) {
			dirTarget.mkdirs();
		}
		
		try {
			File file = new File(path);
			FileUtils.writeByteArrayToFile(file, bytes);
			
			if(width != null || height != null){
				file = new File(path);
				//UploadFileUtil.resizeImageWithHint(file, width, height);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static List<String> getListFiles(String path){
		path = StrimUtil.BASE_FILES_PATH+"/"+path;
		File folder = new File(path);
		List<String> data = new ArrayList<>();
		
		if(folder.listFiles() != null){
			for (final File file : folder.listFiles()) {
				data.add(file.getName());
			}
		}
		return data;
	}
	
	/**
	 * @param request
	 * @param fieldName
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public static void removeUplodedStreamsFromSession(HttpServletRequest request, String fieldName){
		Map<String, byte[]> mapStream = (Map<String, byte[]>)ControllerUtil.getMenuAttribute(fieldName, request);
		ControllerUtil.removeMenuAttribute(fieldName, request);
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
		Map<String, byte[]> mapFiles = FileUtil.uploadFilesToStrteamMap(request, fieldName, path, elementId);
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
	public static boolean addDirOrFile(String path){
		path = StrimUtil.BASE_FILES_PATH + "/" + path;
		File newDir = new File(path);
		
		return  newDir.mkdirs();
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
//	public static void copyGeneratedFiles(String path, File file){
//		String StrimUtil.BASE_FILES_PATH = ContextAppli.CHEMIN_IMAGE;
//		StrimUtil.BASE_FILES_PATH = StrimUtil.BASE_FILES_PATH+"/"+ path;
//
//		try {
//			File dirTarget = new File(StrimUtil.BASE_FILES_PATH.substring(0, StrimUtil.BASE_FILES_PATH.lastIndexOf("/")));
//			if(!dirTarget.isFile()){
//				dirTarget.mkdirs();
//			}
//			
//			File newFile = new File(StrimUtil.BASE_FILES_PATH);
//			newFile.createNewFile();
//			InputStream targetStream = new FileInputStream(file); 
//			Files.copy(targetStream, newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//	}
	
	/**
	 * @param flux
	 * @return
	 */
	public static String getByte64(byte[] flux){ 
		if(flux == null) {
			return null;
		}
		byte[] encodeBase64 = Base64Utils.encode(flux);
		String base64Encoded = null;
		try {
			base64Encoded = new String(encodeBase64, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return base64Encoded;
	}

	/**
	 * @param path
	 * @return
	 */
	public static StringBuilder readFile(String path) {
		File file = new File(path);
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
	
	    try {
	    	br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
	    } catch (IOException e) {
	    	e.printStackTrace();
	    } finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	    return sb;
	}
}
