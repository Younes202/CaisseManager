package appli.controller.domaine.administration.action;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;

import org.apache.commons.io.IOUtils;

import appli.controller.domaine.administration.ParametrageRightsConstantes;
import appli.controller.domaine.administration.bean.JobBean;
import appli.controller.util_ctrl.ApplicationListener;
import appli.model.domaine.administration.service.IParametrageService;
import appli.model.domaine.administration.service.IRequeteurService;
import appli.model.domaine.administration.service.IUserService;
import appli.model.domaine.administration.service.impl.ParametrageService;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.FileUtilController;
import framework.controller.annotation.WorkController;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.service.MessageService;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.synchro.ISynchroniseService;

@WorkController(nameSpace = "admin", bean = JobBean.class, jspRootPath = "/domaine/administration/")
public class RequeteurAction extends ActionBase {
	@Inject
	private IRequeteurService requeteurService;
	@Inject
	private ISynchroniseService synchroService;
	@Inject
	private IParametrageService parametrageService;
	
	/**
	 * @param httpUtil
	 */
	public void work_init(ActionUtil httpUtil){
		httpUtil.setRequestAttribute("listTables", requeteurService.getListTables());
	}
	
	/**
	 * @param httpUtil
	 */
	public void find_logs(ActionUtil httpUtil){
		boolean isCustom = (httpUtil.getParameter("iscust") != null);
		
		String path = null;
		if(!isCustom){
			path = StrimUtil.getGlobalConfigPropertieIgnoreErreur("caisse.tomcat.dir");
		} else{
			path = "C://caisse"; 
		}
		
		File[] listLog = requeteurService.getListLogFiles(path);
		httpUtil.setRequestAttribute("listFiles", listLog);
		httpUtil.setDynamicUrl("/domaine/administration/logs_result.jsp");
	}
	
	public void download_logs(ActionUtil httpUtil){
		File currFile = new File(EncryptionUtil.decrypt(httpUtil.getParameter("path")));
		try {
			httpUtil.doDownloadModeView(currFile.getName(), IOUtils.toByteArray(currFile.toURI()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void executeRequest(ActionUtil httpUtil){
		String requete = httpUtil.getParameter("request");
		String way = httpUtil.getParameter("way");
		
		if(StringUtil.isEmpty(requete)){
			MessageService.addGrowlMessage("Erreur", "Veuillez saisir une requête valide.");
			return;
		}
		
		Integer startPagger = (Integer)httpUtil.getMenuAttribute("startPagger");
		startPagger = (startPagger == null) ? 0 : startPagger;
		if(way != null){
			if(startPagger != null){
				if(way.equals("prev")){
					if(startPagger > 0){
						startPagger = startPagger - 100;
					}
				} else{
					startPagger = startPagger + 100; 
				}
			}
		}
		
		httpUtil.setMenuAttribute("startPagger", startPagger);
		
		try {
			Map<String, Object> data = requeteurService.executeSql(requete, startPagger);
			
			if(data == null){// Cas CRUD
				httpUtil.setRequestAttribute("result_succes", "La requête est exécutée avec suucès.");
			} else{
				// Cas fin table
				if(((List)data.get("data")).size() == 0){
					httpUtil.setMenuAttribute("startPagger", startPagger-100);
				}
				
				httpUtil.setRequestAttribute("result_data", data);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			httpUtil.setRequestAttribute("result_error", e.getMessage());
		}
		
		httpUtil.setDynamicUrl("/domaine/administration/requeteur_result.jsp");
	}
	
	public void initAdminTasks(ActionUtil httpUtil){
		List<String> listEntite = new ArrayList<>(); 
		Metamodel metamodel = parametrageService.getEntityManager().getMetamodel();
		//
		for (ManagedType<?> managedType : metamodel.getManagedTypes()) {
			if (managedType.getJavaType().isAnnotationPresent(Entity.class)) {
				if(managedType.getJavaType().getSimpleName().indexOf("View") != -1){
					 continue;
				 }
				listEntite.add(managedType.getJavaType().getSimpleName());
			}
		}
		String[][] listEntiteStr = new String[listEntite.size()][2];
		int i = 0;
		for (String val : listEntite) {
			listEntiteStr[i][0] = val;
			listEntiteStr[i][1] = val;
			i++;
		}
		
		httpUtil.setRequestAttribute("listEntite", listEntiteStr);
		
		httpUtil.setDynamicUrl("/domaine/administration/supe_admin_tasks.jsp");
	}
	
	public void recharger_params_right(ActionUtil httpUtil){
		ParametrageRightsConstantes.loadAllMapGlobParams(true);
		ParametrageRightsConstantes.loadAllMapSpecParams(true);
		
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "", "Rechargement effectué.");
		
		initAdminTasks(httpUtil);
	}
	
	public void run_script_view(ActionUtil httpUtil){
		parametrageService.executerScriptView();
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "", "Synchronisation effectuée.");
		
		initAdminTasks(httpUtil);
	}
	
	public void synchroniseData(ActionUtil httpUtil){
		String entite = httpUtil.getParameter("dataSync");
		synchroService.addToSynchroniseQueu(StringUtil.isEmpty(entite) ? null : entite);
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "", "Synchronisation effectuée.");
		
		initAdminTasks(httpUtil);
	}

	public void synchroniseAllWaiting(ActionUtil httpUtil){
		synchroService.postDataToCloudAsync();
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "", "Synchronisation effectuée.");
		
		initAdminTasks(httpUtil);
	}
	
	public void force_conf_abn(ActionUtil httpUtil){
		forceLoadConfAbn(httpUtil.getRequest().getContextPath());
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "", "Le rechargement de l'abonnement et la conf est effectué.");
		
		initAdminTasks(httpUtil);
	}
	
	private void forceLoadConfAbn(String contextPath) {
		try {
			String codeAuth = ParametrageService.getEtsCodeAuth();			
			String cloudBaseUrl = StrimUtil.getGlobalConfigPropertie("caisse.cloud.url");
			String cloudUrl = cloudBaseUrl+"/update";
	
			// Sauvegarder les options d'abonnement + base de données
			if(StringUtil.isNotEmpty(cloudBaseUrl)) {
				IUserService userService = ServiceUtil.getBusinessBean(IUserService.class);
				
				String retourAbonnement = FileUtilController.callURL(cloudUrl+"?mt=abonmnt&auth="+codeAuth+"&isFConf=1&isFAbn=1");
				
				// 0: abonnement,     1:conf,            2:cle crytptage client               3:isNewVersion
				if(StringUtil.isNotEmpty(retourAbonnement)){
					String[] retourArray = retourAbonnement.split("\\|");
					// Maj abonnnement
					if(StringUtil.isNotEmpty(retourArray[0])){
						userService.updateAboonement(retourArray[0]);	
					}
					// Maj conf
					if(StringUtil.isNotEmpty(retourArray[1])){
						ApplicationListener.updateConf(retourArray[1], retourArray[2], contextPath);
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
