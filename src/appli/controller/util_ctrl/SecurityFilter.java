package appli.controller.util_ctrl;

import java.io.File;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import appli.controller.domaine.util_erp.ContextAppli;
import framework.controller.Context;
import framework.controller.ControllerUtil;
import framework.model.beanContext.AbonnementBean;
import framework.model.common.constante.ActionConstante;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.MenuMappingService;
import framework.model.util.audit.ReplicationGenerationEventListener;

public class SecurityFilter implements Filter {

	private ServletContext context;

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		if (context == null){
			return;
		}
		
		//
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String requestURI = request.getRequestURI();
		boolean isAvailableConnexion = ControllerUtil.isAvailableConnexion(request);
		boolean isRedirect = StringUtil.isTrue(request.getParameter("lgo")); 
		
		// Skip front agenda
		if(StringUtil.isTrue(request.getParameter("skipF")) 
				|| request.getServletPath().indexOf("/agenda") != -1) {
			chain.doFilter(req, res);
			return;
		}
		
		if(StringUtil.isNotEmpty(request.getParameter("src"))) {
			request.getSession(true).setAttribute("SRC_URL", request.getParameter("src"));
		}
		if(StringUtil.isNotEmpty(request.getParameter("isUnw"))) {
			if(requestURI.indexOf("mobile-qr.jsp") != -1){
				chain.doFilter(req, res);
				return;
			}
			ControllerUtil.forward(context, request, response, "/domaine/caisse/mobile-qr.jsp");
			return;
		}
		
		// Le token est obligatoire si cloud, donc passage dans CaisseMobileController obligatoire
		if(ReplicationGenerationEventListener._IS_CLOUD_SYNCHRO_INSTANCE) {
			String env = (String)request.getSession(true).getAttribute("ENV_MOBILE");
		
			String action = getAction(request);
			if(!"mobile-client".equals(env) && ContextAppli.getEtablissementBean() == null && request.getParameter("jtn") == null){
				response.getWriter().write("<h1>Token non saisi. Merci de vérifier votre lien.</h1>");
				return;
			}
			
			if(!isAvailableConnexion) {
				if(requestURI.indexOf("login.jsp") != -1 
						|| requestURI.indexOf("secure_page.jsp") != -1
						|| requestURI.indexOf("mobile-qr.jsp") != -1
						|| "merge_compte".equals(action) 
						|| "login".equals(action)){
					chain.doFilter(req, res);
					return;
				}
				
				if(requestURI.indexOf(".jsp") == -1 && requestURI.indexOf("secure_page") == -1){
					if("mobile-client".equals(env)) {
						ControllerUtil.forward(context, request, response, "/domaine/caisse/mobile-qr.jsp");
						return;
					}
				
					ControllerUtil.forward(context, request, response, "/commun/secure_page.jsp");
					return;
				}
				if(request.getParameter("w_f_act") == null) {
					response.getWriter().write("<h1>Token non valide</h1>");	
				} else {
					response.getWriter().write("REDIRECT:");
				}
				return;
			}
		}
		
		if(requestURI.contains("update")){
			chain.doFilter(req, res);
			return;
		} else if(request.getParameter("apkD") != null){
			ControllerUtil.doDownload(request, response, new File(this.getClass().getResource("/").getPath()+"appli/conf/CaisseManager.apk"));
			return;
		}
		
		//
		boolean isLoginPage = isLoginPage(request, requestURI) || req.getParameter("unlockopr") != null;
		
		if(!isLoginPage && !isAvailableConnexion && isRedirect && !requestURI.endsWith("login.jsp")){
			ControllerUtil.forward(context, request, response, "/login.jsp");
			return;
		}
		
		if(!isLoginPage && !isAvailableConnexion && !isRedirect){
			ControllerUtil.forward(context, request, response, "/commun/secure_page.jsp");
			return;
		}
		
		if(!isLoginPage && !requestURI.endsWith("accesdenied.jsp") && !isAccesGranted(request)){ 
			ControllerUtil.forward(context, request, response, "/commun/accesdenied.jsp");
			return;
		}
		
		if(!isAvailableConnexion && !isLoginPage && !requestURI.endsWith("accesdenied.jsp")){ 
			if(!isRedirect){
				ControllerUtil.forward(context, request, response, "/commun/accesdenied.jsp");
			}
			request.setAttribute("SKIP_FILTER", true);
		}
		boolean isJsp = requestURI.endsWith(".jsp");
		if(isJsp && (requestURI.endsWith("notfound.jsp") || requestURI.endsWith("_help.jsp") 
				|| requestURI.endsWith("accesdenied.jsp") || requestURI.endsWith("secure_page.jsp"))){
			request.setAttribute("SKIP_FILTER", true);
			chain.doFilter(req, res);
			return;
		}
		// Appeler le prochain filtre de la chaine
		chain.doFilter(req, res);
		return;
	}

	/**
	 * @param request
	 * @param requestURI
	 * @return
	 */
	private boolean isLoginPage(HttpServletRequest request, String requestURI){
		boolean isJsp = requestURI.endsWith(".jsp");
		String action = getAction(request);
		
		if(isJsp){
			return (requestURI.indexOf("secure_page.jsp") != -1 || requestURI.endsWith("login.jsp"));
		} else if("connect".equals(action) || "disconnect".equals(action)){
			return true;
		} else if("lgo".equals(request.getParameter(ProjectConstante.LEFT_MENU_ID))){
			return true;
		}

		return false;
	}

	/**
	 * @param request
	 * @return
	 */
	private boolean isAbonnementGranted(HttpServletRequest request){
		 String leftMenuId = request.getParameter(Context.LEFT_MENU_ID);
		 leftMenuId = ((leftMenuId!=null && leftMenuId.indexOf("-")!=-1) ? leftMenuId.substring(0, leftMenuId.indexOf("-")) : "XX");
		
		 if(StringUtil.isEmpty(leftMenuId) || "XX".equals(leftMenuId)){
			 return true;
		 }
		 
		 AbonnementBean abonementBean = ContextAppli.getAbonementBean();
		 if(abonementBean == null){
			 return false;
		 }
		 
		 boolean isCompta = abonementBean.isOptCompta();
		 boolean isPromo = abonementBean.isOptCommercial();
		 boolean isLivraison = abonementBean.isOptLivraison();
		 boolean isRh = abonementBean.isOptRh();
		 boolean isStock = abonementBean.isOptStock();
		 
		 // Contrôule 
		 if(!isStock && leftMenuId.startsWith("stock")
				|| (!isCompta && leftMenuId.startsWith("compta"))
				|| (!isPromo && leftMenuId.startsWith("fidelite"))
				|| (!isRh && leftMenuId.startsWith("paaie"))
			 	|| (!isLivraison && leftMenuId.startsWith("livr"))){
			 return false;
		 }
		 
		 String url = request.getRequestURI();
		 String satAbon = (String)ControllerUtil.getUserAttribute("SAT_ABONNEMENT", request); 							
		 String env = (String)ControllerUtil.getUserAttribute("CURRENT_ENV", request);
		 satAbon = (satAbon==null?"XX":satAbon);
		 
		 boolean isCuisine = abonementBean.isSatCuisine();
		 boolean isAffichCaisse = abonementBean.isSatAffCaisse(); 
		 boolean isAffichClient = abonementBean.isSatAffClient();
		 boolean isCaisseAutonome = abonementBean.isSatCaisseAuto();

		 if(env != null){
			 if((!isAffichCaisse && env.equals(ContextAppli.APPLI_ENV.affi_caisse.toString()))
					 
					 || (!isCuisine && env.equals(ContextAppli.APPLI_ENV.cuis.toString()))
					 || (!isCuisine && env.equals(ContextAppli.APPLI_ENV.pil.toString()))
					 || (!isCuisine && env.equals(ContextAppli.APPLI_ENV.pres.toString()))
					 
					 || (!isAffichClient && env.equals(ContextAppli.APPLI_ENV.affi_salle.toString()))
					 || (!isCaisseAutonome && env.equals(ContextAppli.APPLI_ENV.cais_cli.toString()))){
				 return false;
			 }
		 }
		 return true;
	}
	
	/**
	 * Test rights
	 * @param request
	 * @return
	 */
	private boolean isAccesGranted(HttpServletRequest request){
		if(ContextAppli.getUserBean() == null){
			return true;
		}
		if(!isAbonnementGranted(request)){
			return false;
		}
		if(BooleanUtil.isTrue(ContextAppli.getUserBean().getIs_admin())){
			return true;
		}
		
		// Test action
		String action = getAction(request);
		
		if(StringUtil.isNotEmpty(action)){
			if(ActionConstante.EDIT.equals(action)){
				return Context.isOperationAvailable(MenuMappingService.RIGHT_ENUM.RIGHT_EDIT);
			} else if(ActionConstante.UPDATE.equals(action)){
				return Context.isOperationAvailable(MenuMappingService.RIGHT_ENUM.RIGHT_UPDATE);
			} else if(ActionConstante.CREATE.equals(action)){
				return Context.isOperationAvailable(MenuMappingService.RIGHT_ENUM.RIGHT_CREATE);
			} else if(ActionConstante.DELETE.equals(action)){
				return Context.isOperationAvailable(MenuMappingService.RIGHT_ENUM.RIGHT_DELETE);
			}
		}

		// Test menu
		String topMenuId = request.getParameter(Context.TOP_MENU_ID);
		String leftMenuId = request.getParameter(Context.LEFT_MENU_ID);
		if(topMenuId != null){
			return Context.isMenuAvailable(topMenuId, true);
		} else if(leftMenuId != null){
			leftMenuId = leftMenuId.substring(leftMenuId.lastIndexOf(".")+1);
			return Context.isMenuAvailable(leftMenuId, false);
		}

		return true;
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		this.context = filterConfig.getServletContext();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
		
	}
	
	/**
	 * @param request
	 * @return
	 */
	private String getAction(HttpServletRequest request){
		String[] ctrlActionArray = getCtrlActionArray(request);

		String action = ((ctrlActionArray != null) && (ctrlActionArray.length == 3)) ? ctrlActionArray[2] : null;
		// This case if action is refresh
		if(StringUtil.isEmpty(action) && (ctrlActionArray.length == 3)){
			action = ctrlActionArray[1];
		}

		return action;
	}
	
	/**
	 * @param request
	 * @return
	 */
	private String[] getCtrlActionArray(HttpServletRequest request) {
		String[] ctrlActionBack = new String[3];

		String controllerAct = getCtrlAction(request);
		String[] ctrlAction = StringUtil.getArrayFromStringDelim(controllerAct, ".");
		// Put controller and action in request
		if (ctrlAction != null) {
			ctrlActionBack[0] = ctrlAction[0];// Name space
			if(ctrlAction.length == 1){
				ctrlActionBack[1] = ctrlAction[0];// Controller
			} else{
				ctrlActionBack[1] = ctrlAction[0] + "." + ctrlAction[1];// Controller
				ctrlActionBack[2] = (ctrlAction.length > 2) ? ctrlAction[2] : null;// Action
			}
		}

		return ctrlActionBack;
	}
	/**
	 * @param request
	 * @return
	 */
	private String getCtrlAction(HttpServletRequest request){
		Object controllerAct = request.getParameter(ProjectConstante.WORK_FORM_ACTION);
		if(controllerAct instanceof String[]){
			controllerAct = ((String[])controllerAct)[((String[])controllerAct).length-1];
		}

		return EncryptionUtil.decrypt(""+controllerAct);
	}
}
