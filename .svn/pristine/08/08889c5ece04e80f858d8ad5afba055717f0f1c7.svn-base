package appli.controller.domaine.caisse.action.mobile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import appli.controller.domaine.administration.action.LoginAction;
import appli.controller.domaine.administration.bean.JourneeBean;
import appli.controller.domaine.administration.bean.UserBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.administration.service.IUserService;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.service.MessageService;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.EncryptionEtsUtil;
import framework.model.common.util.StringUtil;

@WorkController(nameSpace="caisse", bean=JourneeBean.class, jspRootPath="/domaine/caisse/mobile-util")
public class BoUtilMobileAction extends ActionBase {
	
	@Inject
	private IUserService userService;
	
	public void work_init(ActionUtil httpUtil){
	
	}
	
	public void login(ActionUtil httpUtil) throws Exception{
		EncryptionEtsUtil encryptionUtil = null;
		encryptionUtil = new EncryptionEtsUtil(EncryptionEtsUtil.getDecrypKey());
		
		String login = httpUtil.getParameter("log.login");
		String password = httpUtil.getParameter("log.password");
		
		if(StringUtil.isEmpty(login)) {
			MessageService.addFieldMessage("log.login", "Le login est obligatoire");
			return;
		} else if(StringUtil.isEmpty(password)) {
			MessageService.addFieldMessage("log.password", "Le mot de passe est obligatoire");
			return;
		}
		
		UserBean userBean = userService.getUserByLoginAndPw(login, encryptionUtil.encrypt(password)); 
		if(userBean == null){
			MessageService.addGrowlMessage("", "Le login ou le mot de passse est erroné");	
			return;
		}
		if(userBean.isEnvGranted("mob-bo", userBean)){
			MessageService.addGrowlMessage("", "Votre profile ne permet d'accèder à cet espace.");	
			return;
		}
		
		// Si le compte est désactivé
		if(BooleanUtil.isTrue(userBean.getIs_desactive())){
			MessageService.addGrowlMessage("", "Votre est désactivé");
			return;
		}
		
		// Controler les droits d'accès
		if(userBean.getAllEnvs() != null) {
			if(!LoginAction.checkDoitProfileEnv(userBean.getAllEnvs(), ContextAppli.APPLI_ENV.utilitaire.toString())) {
				MessageService.addGrowlMessage("Profile", "Votre profile ne permet d'accèder à cet environnement");
				return;
			}
		}
		
		EtablissementPersistant etsP = userBean.getOpc_etablissement();
		
		if(BooleanUtil.isTrue(etsP.getIs_disable())) {
			MessageService.addGrowlMessage("Application désactivée", "L'application est désactivée suite depuis le Cloud.");
			MessageService.getGlobalMap().remove(ProjectConstante.SESSION_GLOBAL_USER);
			return;
		}
		
		MessageService.getGlobalMap().put(ProjectConstante.SESSION_GLOBAL_USER, userBean);
		
		httpUtil.writeResponse("REDIRECT:");
    }
	
	public void logout(ActionUtil httpUtil){
		HttpServletRequest request = httpUtil.getRequest();
		HttpSession session = request.getSession(false);
		
		// If session exists, destroy it
		if(session != null){
			session.invalidate();
		}
		httpUtil.writeResponse("REDIRECT:");
	}
}
