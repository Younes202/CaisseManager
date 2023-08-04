package appli.controller.domaine.caisse.action.mobile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.criterion.Order;

import appli.controller.domaine.caisse.ContextAppliCaisse;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.administration.persistant.IpAddressPersistant;
import appli.model.domaine.administration.persistant.UserPersistant;
import appli.model.domaine.administration.service.IParametrageService;
import appli.model.domaine.caisse.service.ICaisseMouvementService;
import appli.model.domaine.caisse.service.ICaisseService;
import appli.model.domaine.caisse.service.ICaisseWebService;
import appli.model.domaine.caisse.service.IMenuCompositionService;
import appli.model.domaine.personnel.service.IEmployeService;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.vente.persistant.CaisseJourneePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import appli.model.domaine.vente.persistant.CaissePersistant;
import appli.model.domaine.vente.persistant.JourneePersistant;
import appli.model.domaine.vente.persistant.MenuCompositionPersistant;
import framework.controller.ControllerUtil;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.beanContext.VillePersistant;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.service.MessageService;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;

@WebServlet({
	"/mob-caissier", // Mobile caissier
	//"/mob-client", // Mobile client restaurant
	"/mob-market", // Mobile client market
	"/mob-synthese", // Synthese BO pour patron
	"/bo", // Acces classique
	//"/mob-livreur", // Mobile livreur
	"/mob-util"}) // Mobile utilitaires
public class CloudFrontController extends HttpServlet{
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String mobileAction = request.getParameter("mobAct");
		if(mobileAction != null) {
			String retour = "";
			
			switch(mobileAction){
				case "getToken" : {
					retour = getToken(request, response);
				}; break;
				case "saveToken" : {
					retour = saveToken(request, response);
				}; break;
			}
			
			response.getWriter().write(retour);
			return;
		}
		
		request.getSession(true).removeAttribute("ENV_MOBILE");
		String tokenEts = request.getParameter("jtn");
		String login = request.getParameter("login");
		String pw = request.getParameter("pw");

		String pathInfo = request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1);
		String env = pathInfo.replaceAll("mob-", "mobile-");
		request.getSession(true).setAttribute("ENV_MOBILE", env);
		
		// Si on vient du BO
		if(ControllerUtil.getUserAttribute("CURRENT_ENV", request) != null) {
			MessageService.getGlobalMap().remove("GLOBAL_ETABLISSEMENT");
			MessageService.getGlobalMap().remove(ProjectConstante.SESSION_GLOBAL_USER);
			MessageService.getGlobalMap().remove("ABONNEMENT_BEAN");
		}
		
		if(StringUtil.isNotEmpty(login) && StringUtil.isNotEmpty(pw)) {
			request.setAttribute("login", login);
			request.setAttribute("password", pw);
		}
		
		IParametrageService paramsService = ServiceUtil.getBusinessBean(IParametrageService.class);
		
		//  Date temp
		if(env.equals("mobile-market")) {
			loadDataMarket(request);
		}
		
		if(StringUtil.isEmpty(tokenEts)) {
			if(ContextAppli.IS_CLOUD_MASTER() || ContextAppli.IS_FULL_CLOUD()) {
				if(env.equals("mobile-client")) {
					ControllerUtil.forward(this.getServletContext(), request, response, "/domaine/caisse/mobile-qr.jsp");
				} else {
					response.getWriter().write("<h1>Token client non trouvé dans le lien.</h1>");					
				}
				return;
			} else {
				EtablissementPersistant etsP = paramsService.getEtsOneOrCodeAuth();
				if(StringUtil.isEmpty(etsP.getToken())) {
					tokenEts = etsP.getCode_authentification().substring(0, 5);
					etsP.setToken(tokenEts);
					paramsService.mergeEntity(etsP);
					MessageService.getGlobalMap().put("GLOBAL_ETABLISSEMENT", etsP);
				} else {
					tokenEts = etsP.getToken();
				}
			}
		}		
		
		String oldToken = (ContextAppli.getEtablissementBean() != null ? ContextAppli.getEtablissementBean().getToken() : null);
		
		MessageService.getGlobalMap().remove("GLOBAL_ETABLISSEMENT");
		EtablissementPersistant etsP = paramsService.getOneByField(EtablissementPersistant.class, "token", tokenEts);
		
		if(etsP == null) {
			//response.getWriter().write("<h1>Token client non trouvé dans notre base de données.</h1>");
			ControllerUtil.forward(this.getServletContext(), request, response, "/domaine/caisse/mobile-qr.jsp");
			return;
		}
		 
		Object cloneBean = ReflectUtil.cloneBean(etsP);
		MessageService.getGlobalMap().put("GLOBAL_ETABLISSEMENT", cloneBean);
		request.getSession().setAttribute("GLOBAL_ETABLISSEMENT", cloneBean);
		
		if(ControllerUtil.getUserAttribute("PATH_JSP_CM", request) != null 
				&& !ControllerUtil.getUserAttribute("PATH_JSP_CM", request).equals(env)) {
			
			// Changement de l'établissement
			if(ContextAppli.getUserBean()  == null || !tokenEts.equals(oldToken)) {
				request.getSession(true).invalidate();
				MessageService.clearMessages();
	
				MessageService.getGlobalMap().put("GLOBAL_ETABLISSEMENT", cloneBean);
				request.getSession().setAttribute("GLOBAL_ETABLISSEMENT", cloneBean);
				
				if(env.indexOf("bo") != -1){
					ControllerUtil.forward(this.getServletContext(), request, response, "/login.jsp");
				} else {
					ControllerUtil.forward(this.getServletContext(), request, response, "/domaine/caisse/"+env+"/login.jsp");
				}
			} else {
				if(env.indexOf("bo") != -1){//====>Backoffice
					ControllerUtil.forward(this.getServletContext(), request, response, "/index.jsp");
				} else {//====>Other
					
					if(env.equals("mobile-livreur")) {
//						ICaisseMouvementService caisseMvmService = (ICaisseMouvementService)ServiceUtil.getBusinessBean(ICaisseMouvementService.class);
						String type = "A"; //commandes attentes
//						List<CaisseMouvementPersistant> listCommandes = caisseMvmService.getListCommandes(type);
						request.setAttribute("tp", type);
//						request.setAttribute("listCommandesAttente", listCommandes);
					}
					
					if(env.equals("mobile-market")) {
						ControllerUtil.forward(this.getServletContext(), request, response, "/domaine/caisse/"+env+"/login.jsp");
					} else {
						ControllerUtil.forward(this.getServletContext(), request, response, "/domaine/caisse/"+env+"/index.jsp");
					}
				}
			}
			return;
		}
		
		HttpSession session = request.getSession(false);
		boolean isAvailableConnexion = (session != null && (MessageService.getGlobalMap().get(ProjectConstante.SESSION_GLOBAL_USER) != null));

		//
		ControllerUtil.setUserAttribute("PATH_JSP_CAISSE", "/domaine/caisse/"+env, request);
		ControllerUtil.setUserAttribute("PATH_JSP_CM", env, request);
		
		request.getSession(true).setAttribute("ENV_MOBILE", env);		
		
		if(!isAvailableConnexion) {
			if(env.indexOf("bo") != -1){
				ControllerUtil.forward(this.getServletContext(), request, response, "/login.jsp");
			} else {
				ControllerUtil.forward(this.getServletContext(), request, response, "/domaine/caisse/"+env+"/login.jsp");
			}
			return;
		}
		
		if(env.indexOf("bo") != -1){//====>Backoffice
			ControllerUtil.forward(this.getServletContext(), request, response, "/index.jsp");
		
		} else {//====>Other
			if(env.indexOf("client") != -1){//====>Client
				// Init data Cmd
				initDataCmd(request);
			}
			
			if(env.equals("mobile-livreur")) {
		    	UserPersistant user = ContextAppli.getUserBean();
		    	
				ICaisseMouvementService caisseMvmService = (ICaisseMouvementService)ServiceUtil.getBusinessBean(ICaisseMouvementService.class);
				String type = "A"; //commandes attentes
				int page =0;
				List<CaisseMouvementPersistant> listCommandes = caisseMvmService.getListCommandes(user.getId(), type,page);
				request.setAttribute("tp", type);
				request.setAttribute("listCommandesAttente", listCommandes);
			}
			
			ControllerUtil.forward(this.getServletContext(), request, response, "/domaine/caisse/"+env+"/index.jsp");			
		}
    } 
	
	/**
	 * @param request
	 */
	private void initDataCmd(HttpServletRequest request) {
		ICaisseService caisseService = (ICaisseService)ServiceUtil.getBusinessBean(ICaisseService.class);
		ICaisseWebService caisseWebService = (ICaisseWebService)ServiceUtil.getBusinessBean(ICaisseWebService.class);
		IMenuCompositionService menuCompoService = (IMenuCompositionService)ServiceUtil.getBusinessBean(IMenuCompositionService.class);
		
//		request.setAttribute("listArticleFavoris", caisseService.getFavorisCaisse(null));
		// Familles
		List<FamillePersistant> listFamille = caisseWebService.getListFamilleCaissePagination(null, null);
		request.setAttribute("listFamille", listFamille);
		
		// Menus
		List<MenuCompositionPersistant> listMenu = menuCompoService.getListeMenuCaissePagination(null, null);
		request.setAttribute("listMenu", listMenu);
		
		CaisseMouvementPersistant CURRENT_COMMANDE = (CaisseMouvementPersistant) ControllerUtil.getUserAttribute("CURRENT_COMMANDE", request);
		if (CURRENT_COMMANDE == null) {
			CaisseMouvementPersistant newCmd = new CaisseMouvementPersistant();
	        newCmd.setRef_commande(""+System.currentTimeMillis());
	        newCmd.setDate_vente(new Date());
	        newCmd.setOpc_caisse_journee(ContextAppliCaisse.getJourneeCaisseBean());
	        //
	        newCmd.setList_article(new ArrayList<>());
	        newCmd.setList_offre(new ArrayList<>());
	        
			newCmd.setMax_idx_client(1);
			ControllerUtil.setUserAttribute("CURRENT_IDX_CLIENT", 1, request);// Index client
			
			ControllerUtil.setUserAttribute("CURRENT_COMMANDE", newCmd, request);
		}
		List<CaissePersistant> listCaisse = caisseService.getListCaisseActive(ContextAppli.TYPE_CAISSE_ENUM.CAISSE_CLIENT.toString(), true);
		if(listCaisse.size() == 0) {
			listCaisse = caisseService.getListCaisseActive(ContextAppli.TYPE_CAISSE_ENUM.CAISSE.toString(), true);
		}
		 
		 JourneePersistant journeeOuverte = caisseWebService.getLastJourne();
		 if(listCaisse.size() > 0) {
			 CaisseJourneePersistant journeeCaisseOuverte = caisseWebService.getJourneCaisseOuverte(listCaisse.get(0).getId());
			 MessageService.getGlobalMap().put("CURRENT_JOURNEE_CAISSE", journeeCaisseOuverte);
			 MessageService.getGlobalMap().put("CURRENT_CAISSE", listCaisse.get(0));
		 }
		 
         MessageService.getGlobalMap().put("CURRENT_JOURNEE", journeeOuverte);
	}
	
	private void loadDataMarket(HttpServletRequest request) {
		ICaisseWebService caisseWebService = ServiceUtil.getBusinessBean(ICaisseWebService.class);
		IMenuCompositionService menuCompService = ServiceUtil.getBusinessBean(IMenuCompositionService.class);
		
		// Familles
		List<FamillePersistant> listFamille = caisseWebService.getListFamilleCaissePagination(null, null);
		List<FamillePersistant> listFamilleLev1 = new ArrayList<>();
				
		for (FamillePersistant familleP : listFamille) {
			if(familleP.getLevel() == 1) {
				listFamilleLev1.add(familleP);
			}
		}
		request.setAttribute("listFamille", listFamilleLev1);
		
		// Menus
		List<MenuCompositionPersistant> listMenu = menuCompService.getListeMenu(true, true);
		List<MenuCompositionPersistant> listMenuLev1 = new ArrayList<>();
		
		for (MenuCompositionPersistant menuCompositionP : listMenu) {
			if(menuCompositionP.getLevel() == 1) {
				listMenuLev1.add(menuCompositionP);
			}
		}
		request.setAttribute("listMenu", listMenuLev1);
		
		request.setAttribute("listVille", menuCompService.findAll(VillePersistant.class, 
				Order.asc("opc_region.libelle"), 
				Order.asc("libelle")));
	}

	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private String getToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		IEmployeService etsService = ServiceUtil.getBusinessBean(IEmployeService.class);
		String ip = request.getRemoteHost();
		List<IpAddressPersistant> etsP = etsService.findByField(IpAddressPersistant.class, "ip", ip, Order.desc("id"));
		
		if(etsP.size()>0) {
			return etsP.get(0).getEts_token()+"|"+StrimUtil.getGlobalConfigPropertie("instance.url")+"|mob-client";
		}
		return "||";
	}
	
	/**
	 * @param httpUtil
	 * @throws IOException 
	 */
	public String saveToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String token = request.getParameter("tkn");
		request.getSession(true).setAttribute("DEVICE_TOKEN", token);
		
		return "OK";
	}
}
