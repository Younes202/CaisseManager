package appli.controller.util_ctrl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.SOFT_ENVS;
import framework.controller.FileUtilController;
import framework.model.common.service.MessageService;
import framework.model.common.util.EncryptionEtsUtil;
import framework.model.common.util.EncryptionUtil64;
import framework.model.common.util.MappingUtil;
import framework.model.common.util.NumericUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.audit.ReplicationGenerationEventListener;

public class ApplicationListener implements ServletContextListener, HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent event) {

	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {

	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		String contextPath = arg0.getServletContext().getContextPath();

		// Charger les properties
		new StrimUtil();
		String contextInstall = StrimUtil.getGlobalConfigPropertieIgnoreErreur("context.install");

		/* ------------------------- LES CHEMINS ------------------- */
		if (ContextAppli.IS_CLOUD_MASTER()) {
			ReplicationGenerationEventListener._IS_CLOUD_SYNCHRO_INSTANCE = true;
			StrimUtil.BASE_FILES_PATH = StrimUtil.getGlobalConfigPropertieIgnoreErreur("data.path");
			return;
		}
		if("localConf".equals(contextInstall)) {
			StrimUtil.BASE_FILES_PATH = StrimUtil.getGlobalConfigPropertieIgnoreErreur("data.path");
			return;
		}
		
		String dirBase = getPath(contextPath);
		System.out.println("ABSOLUTE PATH 2 => " + dirBase);

		if (new File(dirBase).exists()) {
			StrimUtil.BASE_FILES_PATH = StrimUtil.getGlobalConfigPropertieIgnoreErreur("data.path");
			dirBase = StrimUtil.BASE_FILES_PATH;
		}
		// Ressources
		if (new File("C://CAISSE_UPLOAD_FILES").exists()) {
			StrimUtil.BASE_FILES_PATH = "C://CAISSE_UPLOAD_FILES";
		} else {
			String resourcesFolder = dirBase + "/" + "resources";
			if (!new File(resourcesFolder).exists()) {
				new File(resourcesFolder).mkdirs();
			}
			StrimUtil.BASE_FILES_PATH = resourcesFolder;
		}

		// local localConf
		try {
			if ("local".equals(contextInstall)) {
				loadCaisseConf(contextPath);

				EncryptionEtsUtil encryptUtil = new EncryptionEtsUtil(
						StrimUtil.GLOBAL_CONFIG_MAP.getProperty("client.key"));
				StrimUtil.GLOBAL_CONFIG_MAP.put("db.pw",
						encryptUtil.decrypt(StrimUtil.getGlobalConfigPropertieIgnoreErreur("db.pw")));
				StrimUtil.GLOBAL_CONFIG_MAP.put("db.user",
						encryptUtil.decrypt(StrimUtil.getGlobalConfigPropertieIgnoreErreur("db.user")));

				System.out.println("Base ==> " + StrimUtil.getGlobalConfigPropertieIgnoreErreur("db.name"));
			}
			MessageService.clearMessages();

			boolean isSynchroAllowed = StringUtil.isTrue(StrimUtil.getGlobalConfigPropertieIgnoreErreur("cloud.synchro"))
						&& StringUtil.isNotEmpty(StrimUtil.getGlobalConfigPropertieIgnoreErreur("url.cloud"));
			if (isSynchroAllowed) {
				ReplicationGenerationEventListener._IS_LOCAL_SYNCHRO_INSTANCE = false;// true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void loadCaisseConf(String contextPath) throws Exception {
		// Chargement du du home "caisse-back.properties"
		Properties globalProperties = loadConfProperties(contextPath);
		String cryptageKey = globalProperties.getProperty("client.key");
		String softType = StrimUtil.getGlobalConfigPropertieIgnoreErreur("context.soft");

		if (SOFT_ENVS.agri.toString().equals(softType)) {
			return;
		}

		//
		if (StringUtil.isEmpty(cryptageKey)) {
			System.out.println("CONF PROBLEM ==================> Clé de cryptage vide");
			return;
		}

		// Set glabal key
		String cryptKey = EncryptionUtil64.decrypt64(cryptageKey);
		StrimUtil.GLOBAL_CONFIG_MAP.put("client.key", cryptKey);
		// Init encryptor
		EncryptionEtsUtil encryptUtil = new EncryptionEtsUtil(cryptKey);
		// Load config
		String config = encryptUtil.decrypt(globalProperties.getProperty("global.conf"));

		if (StringUtil.isEmpty(config)) {
			System.out.println("CONF PROBLEM ==================> Fichier de conf non lisible");
			return;
		}
		String cloudUrl = encryptUtil.decrypt(globalProperties.getProperty("caisse.cloud.url")) + "/update";
		String codeAuth = encryptUtil.decrypt(globalProperties.getProperty("caisse.code.auth"));

		try {
			String retourCheckCaisse = FileUtilController.callURL(cloudUrl + "?mt=checkd&auth=" + codeAuth);
			boolean isCaisseValable = ("" + retourCheckCaisse).trim().equals("true");

			if (!NumericUtil.isNum(retourCheckCaisse) && !isCaisseValable) {// Si non date et false
				return;
			}
			// Sauvegarder les options d'abonnement + base de données
			String retourAbonnement = FileUtilController.callURL(cloudUrl + "?mt=abonmnt&auth=" + codeAuth);

			// 0: abonnement, 1:conf, 2:cle client 3:isNewVersion
			if (StringUtil.isNotEmpty(retourAbonnement)) {
				String[] retourArray = retourAbonnement.split("\\|");
				// Update conf
				if (StringUtil.isNotEmpty(retourArray[1])) {
					updateConf(retourArray[1], retourArray[2], contextPath);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		if (StrimUtil.getGlobalConfigPropertieIgnoreErreur("db.pw") == null) {
			String conf = globalProperties.getProperty("global.conf");
			updateConf(conf, cryptageKey, contextPath);
		}

		// Chargement en mémoire
		Map<String, String> mapTech = new HashMap<>();

		// Charger le in
		for (Object key : StrimUtil.GLOBAL_CONFIG_MAP.keySet()) {
			mapTech.put("" + key, StringUtil.getValueOrEmpty(StrimUtil.GLOBAL_CONFIG_MAP.get(key)));
		}
		// Maj du global config in avec acces db
		String[] configArray = StringUtil.getArrayFromStringDelim(config, ",");
		for (String valAr : configArray) {
			String[] conf = StringUtil.getArrayFromStringDelim(valAr, "=");
			String key = conf[0].trim();
			String value = (conf.length > 1 ? conf[1].trim() : "");

			if (key.startsWith("db")) {
				mapTech.put(key, value);
			}
			// Charger la totalit de la conf en mémoire
			StrimUtil.GLOBAL_CONFIG_MAP.put(key, value);
		}
		StrimUtil.GLOBAL_CONFIG_MAP.put("caisse.cloud.url",
				encryptUtil.decrypt(globalProperties.getProperty("caisse.cloud.url")));
		StrimUtil.GLOBAL_CONFIG_MAP.put("caisse.code.auth",
				encryptUtil.decrypt(globalProperties.getProperty("caisse.code.auth")));

		String globalConfigPathIn = MappingUtil.getFullFilePath("/", MappingUtil.GLOBAL_CONFIG_PATH);
		try {
			FileOutputStream out = new FileOutputStream(globalConfigPathIn);
			for (String key : mapTech.keySet()) {
				StrimUtil.setGlobalConfigPropertie(key, mapTech.get(key));

				Properties props = new Properties();
				props.setProperty(key, mapTech.get(key));
				props.store(out, null);
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void updateConf(String retourConf, String cryptageKey, String contextPath) {
		Properties globalConfigHome = loadConfProperties(contextPath);

		// Ecraser la conf out
		globalConfigHome.put("client.key", StringUtil.getValueOrEmpty(cryptageKey));
		globalConfigHome.put("global.conf", StringUtil.getValueOrEmpty(retourConf));

		try {
			String softType = StrimUtil.getGlobalConfigPropertieIgnoreErreur("context.soft");
			if (SOFT_ENVS.restau.toString().equals(softType)) {
				softType = "caisse";
			}
			File fileConfPath = new File(getPath(contextPath) + "/" + softType + "-config.properties");
			FileOutputStream out = new FileOutputStream(fileConfPath);
			globalConfigHome.store(out, null);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getPath(String contextPath) {
		String absolutePath = new File(".").getAbsolutePath();
		absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf(File.separatorChar));

		String softType = StrimUtil.getGlobalConfigPropertieIgnoreErreur("context.soft");
		if (SOFT_ENVS.restau.toString().equals(softType)) {
			softType = "caisse";
		}
		File fileConfPath = new File(absolutePath + "/" + softType + "-config.properties");

		System.out.println("ABSOLUTE PATH 1 => " + fileConfPath);

		if (fileConfPath.exists() && fileConfPath.length() > 0) {
			return absolutePath;
		}

		String[] _CONF_PATH = { "C://caisse/" + contextPath, "C://caisse", "E://caisse/" + contextPath, "E://caisse",
				"D://caisse/" + contextPath, "D://caisse", "F://caisse/" + contextPath, "F://caisse" };
		for (String dir : _CONF_PATH) {
			if (new File(dir).exists()) {
				return dir;
			}
		}
		return null;
	}

	public static Properties loadConfProperties(String contextPath) {
		String path = getPath(contextPath);
		if (path == null) {
			System.out.println("CONF PROBLEM ==================> Aucun fichier de configuration n'a été trouvé");

			return null;
		}

		String softType = StrimUtil.getGlobalConfigPropertieIgnoreErreur("context.soft");
		if (SOFT_ENVS.restau.toString().equals(softType)) {
			softType = "caisse";
		}

		File fileConfPath = new File(path + "/" + softType + "-config.properties");
		if (!fileConfPath.exists()) {
			try {
				fileConfPath.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Properties homeProperties = new Properties();

		Properties globalProperties = new Properties();
		try {
			FileInputStream in = new FileInputStream(fileConfPath);

			homeProperties.load(in);
			in.close();

			in = new FileInputStream(fileConfPath);
			globalProperties.load(in);
			in.close();

			globalProperties.putAll(homeProperties);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return globalProperties;
	}
}
