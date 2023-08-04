package framework.model.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;

import org.apache.log4j.Logger;

import framework.model.common.service.IhmMappingService;

public class MappingUtil {
	private final static Logger LOGGER = Logger.getLogger(StrimUtil.class);
	public final static String GLOBAL_CONFIG_PATH 		= "global-config.properties";
	private final static String LABELS_PATH 			= "-labels_fr.properties";

	// ------------------ Get path of config and params files ---------------------
	//* Properties files *//
	public final static Properties getGlobalConfigMap(){
		return getPropertie(getFullFilePath("/", GLOBAL_CONFIG_PATH));
	}
	public final static Properties getLabelsMap(){
		List<String> listFilesPath = getListFilesPath("/", LABELS_PATH);
		if(listFilesPath != null){
			Properties labelProps = new Properties();
			for(String path : listFilesPath){
				labelProps.putAll(getPropertie(path));
			}
			return labelProps;
		}

		return null;
	}

	/**
	 * Get value from properties file
	 *
	 * @param key
	 * @return
	 */
	private static Properties getPropertie(String filePath) {
		if (StringUtil.isEmpty(filePath))
			return null;

		try {
			Properties prop = new Properties();
			FileInputStream in = new FileInputStream(filePath);
			prop.load(in);
			in.close();
			// Extraction des propri�t�s
			return prop;
		} catch (Exception e) {
		    LOGGER.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * @param properties
	 * @param is
	 * @param resourceName
	 * @throws IOException
	 */
	/*private static void loadUTF8Properties(Properties properties, InputStream is, String resourceName) throws IOException {
	      BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8")); //$NON-NLS-1$
	      String line = reader.readLine();
	      while (line != null) {
	         int index = line.indexOf('#');
	         if (index >= 0) {
	            line = line.substring(0, index);
	         }
	         if (line.trim().length() > 0) {
	            index = line.indexOf('=');
	            if (index < 0) throw new IOException("Wrong properties file format (" + resourceName + ")!"); //$NON-NLS-1$ //$NON-NLS-2$
	            String key = line.substring(0, index).trim();
	            String value = line.substring(index + 1);
	            properties.put(key, value);
	         }
	         line = reader.readLine();
	      }
	   }*/

	/*\ ---------------------------------------- Manage translate files ------------------------------------------\*/

	/**
	 * @param fileName
	 * @param language
	 * @param country
	 * @return
	 */
/*	private static ResourceBundle getResourceBundle(String fileName, String language, String country){
	      Locale currentLocale;
	      ResourceBundle messages;

		  String url = StrimUtil.class.getResource("/").getPath();
		  String fullFileName = fileName + "_" + language + "_" + country + ".properties";
		  String filePath = getFilePath(url, fullFileName);

		  url = replace(url, "/", ".").replace("\\", ".").substring(1, url.length());
		  String tempUrl = replace(filePath, "/", ".").replace("\\", ".");
		  tempUrl = replace(tempUrl, fullFileName, "");

		  tempUrl = replace(tempUrl, url, "");

	      currentLocale = new Locale(language, country);
	      messages = ResourceBundle.getBundle((tempUrl + fileName), currentLocale);

	      return messages;
	}*/

	/**
	 * @param refClass
	 * @param fullFileName
	 * @return
	 */
	public static String getFullFilePath(String basePath, String fullFileName){
		String url = StrimUtil.class.getResource(basePath).getPath();
		String filePath = getFilePath(url, fullFileName);

	    return filePath;
	}

	/**
	 * @param refClass
	 * @param base
	 * @param endFileName
	 * @return
	 */
	public static List<String> getListFilesPath(String basePath, String endFileName){
		  String url = StrimUtil.class.getResource(basePath).getPath();
		  List<String> listFiles = new ArrayList<String>();
		  getFilePath(url, endFileName, listFiles);

	      return listFiles;
	}

	 /**
	 * @param url
	 * @param fileName
	 * @return
	 */
	private static String getFilePath(String url, String fileName){
	   File file;
	   String list[];
	   file = new File(url);

	    if (file.isDirectory()){
	    	list = file.list();
	    	for (int i = 0; i < list.length; i++){
	    		String path = url + File.separatorChar + list[i];
	    		String filePath = getFilePath(path, fileName);
	    		if(filePath != null){
	    			return filePath;
	    		}
	    	}
	    } else{
	    	if(fileName.equals(file.getName())){
	    		return file.toString();
	    	}
	   	}

	    return null;
	 }

	 /**
	 * @param url
	 * @param fileName
	 * @return
	 */
	private static String getFilePath(String url, String endFileName, List<String> listFiles){
	   File file;
	   String list[];
	   file = new File(url);

	    if (file.isDirectory()){
	    	list = file.list();
	    	for (int i = 0; i < list.length; i++){
	    		String path = url + File.separatorChar + list[i];
	    		String filePath = getFilePath(path, endFileName, listFiles);
	    		if(filePath != null){
	    			listFiles.add(filePath);
	    		}
	    	}
	    } else{
	    	if(file.getName().toLowerCase().endsWith(endFileName.toLowerCase())){
	    		return file.toString();
	    	}
	   	}

	    return null;
	}

	/**
	 * @param annotationClass
	 * @param classList
	 * @param startBasePath : Attention ex : \\package\\package
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static List<Class> getListClassByAnnotation(Class annotationClass, String basePath) throws Exception{
		List<Class> list = null;
		List<String> classList = getListFilesPath(basePath, ".class");
		//
		if((classList != null) && (classList.size() > 0)){
			list = new ArrayList<Class>();
			//
			for (String className : classList) {
				// For windows
				boolean isDoubleSlash = (className.indexOf("\\") != -1);

				if(isDoubleSlash){
					basePath = basePath.replace("/", "\\");
				}
//				if(StringUtil.isNotEmpty(basePath) && !"/".equals(basePath)  && !"\\".equals(basePath)){
//					className = className.substring(className.lastIndexOf(basePath), className.indexOf(".class"));
//				}
				
				className = className.substring(className.indexOf("classes")+7, className.lastIndexOf(".class"));
				if(isDoubleSlash) {
					className = className.replace('\\', '.').substring(1);
				} else{
					className = className.replace('/', '.').substring(1);					
				}

				if(className.indexOf(IhmMappingService.class.getSimpleName()) == -1){
					Class annotatedClass = Class.forName(className);
					//
					if (annotatedClass != null) {
						Annotation validatorAnnot = annotatedClass.getAnnotation(annotationClass);
						if (validatorAnnot != null) {
							list.add(annotatedClass);
						}
					}
				}
			}
			if(list.size() == 0){
				list = null;
			}
		}

		return list;
	}

	/**
	 * @param interfaceClass
	 * @param basePath
	 * @return
	 * @throws Exception
	 */
	public static List<Class<?>> getListClassByInterface(Class<?> interfaceClass, String basePath) throws Exception{
		List<Class<?>> list = null;
		List<String> classList = getListFilesPath(basePath, ".class");
		//
		if((classList != null) && (classList.size() > 0)){
			list = new ArrayList<Class<?>>();
			//
			for (String className : classList) {
				// For windows
				boolean isDoubleSlash = (className.indexOf("\\") != -1); 
				if(isDoubleSlash){
					basePath = basePath.replace("/", "\\");
				}				
				///basePath = basePath.replace("/", "\\");
//				className = className.substring(className.lastIndexOf(basePath), className.indexOf(".class"));
				className = className.substring(className.indexOf("classes")+7, className.lastIndexOf(".class"));
				if(isDoubleSlash) {
					className = className.replace('\\', '.').substring(1);
				} else{
					className = className.replace('/', '.').substring(1);					
				}
				//className = className.replace('\\', '.').substring(1);

				if(className.indexOf(IhmMappingService.class.getSimpleName()) == -1){
					Class<?> findedClass = Class.forName(className);
					//
					if (findedClass != null) {
						Class<?>[] interfaces = findedClass.getInterfaces();
						if (interfaces != null) {
							for(Class <?>interf : interfaces){
								if(interf.getName().equals(interfaceClass.getName())){
									list.add(findedClass);
									break;
								}
							}
						}
					}
				}
			}
		}

		if((list != null) && (list.size() == 0)){
			list = null;
		}

		return list;
	}

	/**
	 * @param jarName
	 * @return
	 * @throws MalformedURLException
	 */
//	public static String getJarPath() throws MalformedURLException{
//		Set<String> libJars = MenuMappingService.servletContext.getResourcePaths("/WEB-INF/lib");
//		for (String jarName : libJars){
//			if(jarName.endsWith(FRAMEWORK_JAR_NAME)){
//				//URL resource = MenuMappingService.servletContext.getResource((String) jarName);
//				String resource2 = MenuMappingService.servletContext.getRealPath((String) jarName);
//
//				return resource2;
//				//return resource.getPath();
//			}
//		}
//
//		return null;

		/*String classPath = System.getProperty("java.class.path");
		String[] listContent = StringUtil.getArrayFromStringDelim(classPath, ";");
		jarName = (jarName.indexOf(".jar") == -1) ? (jarName+".jar") : jarName;
		for(String contentName : listContent){
			if(contentName.endsWith(jarName)){
				return contentName;
			}
		}

		return null;*/
//	}

	/**
	 * @param jarName
	 * @return
	 * @throws Exception
	 */
//	@SuppressWarnings("unchecked")
//	public static  List<Class> getListClassFromJar(Class annotationClass) throws Exception{
//		String jarPath = getJarPath();
//		List<Class> listClass = null;
//		//
//		if(jarPath != null){
//			List<String> listClassSt = new ArrayList<String>();
//			JarFile jarFile = new JarFile(jarPath);
//			// Add paths
//		    Enumeration enumd = jarFile.entries();
//		    
//		    while (enumd.hasMoreElements()) {
//		    	 process(enumd.nextElement(), listClassSt);
//		    }
//		    // Add classes
//			if(listClassSt.size() > 0){
//				listClass = new ArrayList<Class>();
//				//
//				for(String className : listClassSt){
//					className = className.replace('/', '.').substring(0, className.indexOf(".class"));
//					//
//					if(className.indexOf(IhmMappingService.class.getSimpleName()) == -1){
//						Class annotatedClass = Class.forName(className);
//						//
//						if (annotatedClass != null) {
//							Annotation validatorAnnot = annotatedClass.getAnnotation(annotationClass);
//							if (validatorAnnot != null) {
//								listClass.add(annotatedClass);
//							}
//						}
//					}
//				}
//			}
//			
//			jarFile.close();
//		}
//
//		return listClass;
//	}

     /**
     * @param obj
     * @param listClass
     */
    @SuppressWarnings("unused")
	private static void process(Object obj, List<String> listClass) {
	       JarEntry entry = (JarEntry)obj;
	       String path = entry.getName();
	       long size = entry.getSize();
	       long compressedSize = entry.getCompressedSize();
	       if(path.endsWith(".class")){
	    	   listClass.add(path);
	       }
	  }
}
