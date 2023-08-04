package appli.controller.domaine.caisse.action.mobile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import appli.controller.domaine.administration.bean.UserBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.administration.service.IEtablissementService;
import framework.controller.ControllerUtil;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.service.MessageService;
import framework.model.common.util.ServiceUtil;

@WebServlet({"/mob-client", "/mob-livreur"})
public class CaisseMobileFrontController extends HttpServlet{
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String env = request.getRequestURI();
		String origineEnv = env.substring(env.lastIndexOf("/")+1);
		env = origineEnv.replaceAll("mob-", "mobile-");
		
		HttpSession session = request.getSession(false);
		boolean isAvailableConnexion = (session != null && (MessageService.getGlobalMap().get(ProjectConstante.SESSION_GLOBAL_USER) != null));
//		String codeAuthen = "XXXXX";
		
		//
		ControllerUtil.setUserAttribute("CURRENT_ENV", env, request);	
		ControllerUtil.setUserAttribute("PATH_JSP_CAISSE", "/domaine/caisse/"+env, request);
		ControllerUtil.setUserAttribute("PATH_JSP_CM", "mobile", request);
		
		MessageService.getGlobalMap().remove("GLOBAL_ETABLISSEMENT");

		request.getSession(true).setAttribute("ENV_MOBILE", env);
		
		if(!isAvailableConnexion && env.indexOf("client") == -1) {
			ControllerUtil.forward(this.getServletContext(), request, response, "/domaine/caisse/"+env+"/login.jsp");
			return;
		} else {
			// Utilisateur générique
			if(ContextAppli.getUserBean() == null) {
				UserBean userB = new UserBean();
				userB.setLogin("CLIENT_GEN");
				
				MessageService.getGlobalMap().put(ProjectConstante.SESSION_GLOBAL_USER, userB);
			}
			
			IEtablissementService etsService = (IEtablissementService)ServiceUtil.getBusinessBean(IEtablissementService.class);
			Map<String, List<EtablissementPersistant>> mapEts = etsService.getListEtablissement();
			request.setAttribute("mapEts", mapEts);
		}
		
//		ICaisseService caisseService = (ICaisseService)ServiceUtil.getBusinessBean(ICaisseService.class);
//		IArticleService articleService = (IArticleService)ServiceUtil.getBusinessBean(IArticleService.class);
//		ICaisseWebService caisseWebService = (ICaisseWebService)ServiceUtil.getBusinessBean(ICaisseWebService.class);
//		IMenuCompositionService menuCompoService = (IMenuCompositionService)ServiceUtil.getBusinessBean(IMenuCompositionService.class);
//		
//		request.setAttribute("listArticleFavoris", caisseService.getFavorisCaisse(null));
//		// Familles
//		List<FamillePersistant> listFamille = caisseWebService.getListFamilleCaissePagination(null, null);
//		request.setAttribute("listFamille", listFamille);
//		
//		// Menus
//		List<MenuCompositionPersistant> listMenu = menuCompoService.getListeMenuCaissePagination(null, null);
//		request.setAttribute("listMenu", listMenu);
//		
//		CaisseMouvementPersistant CURRENT_COMMANDE = (CaisseMouvementPersistant) ControllerUtil.getUserAttribute("CURRENT_COMMANDE", request);
//		if (CURRENT_COMMANDE == null) {
//			CaisseMouvementPersistant newCmd = new CaisseMouvementPersistant();
//	        newCmd.setRef_commande(""+System.currentTimeMillis());
//	        newCmd.setDate_vente(new Date());
//	        newCmd.setOpc_caisse_journee(ContextAppliCaisse.getJourneeCaisseBean());
//	        //
//	        newCmd.setList_article(new ArrayList<>());
//	        newCmd.setList_offre(new ArrayList<>());
//	        
//			newCmd.setMax_idx_client(1);
//			ControllerUtil.setUserAttribute("CURRENT_IDX_CLIENT", 1, request);// Index client
//			
//			ControllerUtil.setUserAttribute("CURRENT_COMMANDE", newCmd, request);
//		}
//		String adresseIp = null;
//		List<CaissePersistant> listCmientCaisse = caisseService.getListCaisseActive(ContextAppli.TYPE_CAISSE_ENUM.CAISSE.toString(), true);
//		
//        if(listCmientCaisse.size() == 0){
//            adresseIp = request.getRemoteAddr();
//            if (adresseIp.equalsIgnoreCase("0:0:0:0:0:0:0:1")) {// Si on est sur la même machine (local ost)
//                InetAddress inetAddress = InetAddress.getLocalHost();
//                adresseIp = inetAddress.getHostAddress();
//            }
//        } else{
//        	adresseIp = listCmientCaisse.get(0).getAdresse_mac();
//        }
//		// On initialise car sans authentification
//		 CaissePersistant caisse = (CaissePersistant) articleService.getSingleResult(articleService.getQuery("from CaissePersistant where adresse_mac=:mac "
//            		+ "and type_ecran=:typeEcran")
//                  .setParameter("mac", adresseIp)
//                  .setParameter("typeEcran", ContextAppli.TYPE_CAISSE_ENUM.CAISSE.toString()));
//		
//		 if(caisse != null){
//			 JourneePersistant journeeOuverte = caisseWebService.getLastJourne();
//			 CaisseJourneePersistant journeeCaisseOuverte = caisseWebService.getJourneCaisseOuverte(caisse.getId());
//	         MessageService.getGlobalMap().put("CURRENT_JOURNEE_CAISSE", journeeCaisseOuverte);
//	         MessageService.getGlobalMap().put("CURRENT_JOURNEE", journeeOuverte);
//		 }
		 
		 ControllerUtil.forward(this.getServletContext(), request, response, "/domaine/caisse/"+env+"/index.jsp");
    } 
}
