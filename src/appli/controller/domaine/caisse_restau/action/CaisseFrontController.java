package appli.controller.domaine.caisse_restau.action;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import appli.controller.domaine.administration.ParametrageRightsConstantes;
import appli.controller.domaine.administration.bean.UserBean;
import appli.controller.domaine.caisse.ContextAppliCaisse;
import appli.controller.domaine.caisse.bean.CaisseBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.administration.persistant.UserPersistant;
import appli.model.domaine.caisse.service.ICaisseService;
import appli.model.domaine.caisse.service.ICaisseWebService;
import appli.model.domaine.caisse.service.IMenuCompositionService;
import appli.model.domaine.caisse_restau.persistant.CuisineJourneePersistant;
import appli.model.domaine.caisse_restau.service.ICuisineJourneeService;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.stock.service.IArticleService;
import appli.model.domaine.vente.persistant.CaisseJourneePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import appli.model.domaine.vente.persistant.CaissePersistant;
import appli.model.domaine.vente.persistant.JourneePersistant;
import appli.model.domaine.vente.persistant.MenuCompositionPersistant;
import framework.controller.ContextGloabalAppli;
import framework.controller.ControllerUtil;
import framework.controller.bean.PagerBean;
import framework.model.beanContext.AbonnementBean;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.service.MessageService;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StringUtil;

@WebServlet(urlPatterns="/caisse")
public class CaisseFrontController extends HttpServlet{

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ICaisseService caisseService = (ICaisseService)ServiceUtil.getBusinessBean(ICaisseService.class);
		ICaisseWebService caisseWebService = (ICaisseWebService)ServiceUtil.getBusinessBean(ICaisseWebService.class);
		String env = (String)ControllerUtil.getUserAttribute("CURRENT_ENV", request);
		
		// Si on vien d'un appel afficheur caisse 2 écran intégré
		if(StringUtil.isNotEmpty(request.getParameter("aff"))) {
			Long caisseId = Long.valueOf(request.getParameter("aff"));
			List<CaissePersistant> listAfficheur = caisseWebService.getListAfficheurs(caisseId);
			CaisseBean caisseBean = caisseService.findById(caisseId);
			CaissePersistant caisseBeanAff = null;
			List<CaissePersistant> listAff = caisseService.getListCaisseActive(ContextAppli.TYPE_CAISSE_ENUM.AFFICHEUR.toString(), true);
			//
			for (CaissePersistant caisseP : listAff) {
				if(caisseP.getOpc_caisse() != null && caisseP.getOpc_caisse().getId()==caisseId) {
					caisseBeanAff = caisseP;
					break;
				}
			}
			AbonnementBean abonnementBean = new AbonnementBean();
			abonnementBean.setSatAffCaisse(true);
			MessageService.getGlobalMap().put("ABONNEMENT_BEAN", abonnementBean);
			
			// Charger les paramètres ----------------------------------------------
			ParametrageRightsConstantes.loadAllMapGlobParams(false);
			ParametrageRightsConstantes.loadAllMapSpecParams(false);
			
			ParametrageRightsConstantes.setMapEffect(request);
			if(listAfficheur.size() > 0) {
				MessageService.getGlobalMap().put("CURRENT_AFFICHEUR", listAfficheur.get(0).getId());
			}
			
			MessageService.getGlobalMap().put("CURRENT_CAISSE", caisseBeanAff);
			ControllerUtil.setUserAttribute("CURRENT_ENV", ContextAppli.APPLI_ENV.affi_caisse.toString(), request);
			MessageService.getGlobalMap().put(ProjectConstante.SESSION_GLOBAL_USER, new UserBean());
			MessageService.getGlobalMap().put("GLOBAL_ETABLISSEMENT", caisseBean.getOpc_etablissement());
			
        	ControllerUtil.forward(this.getServletContext(), request, response, "/domaine/caisse/afficheur/client-trackcmd.jsp");
        	return;
		}
		
		if(env == null) {
			ControllerUtil.forward(request.getServletContext(), request, response, "/commun/secure_page.jsp");
			return;
		}
		
		// Skip header js hash event
		request.setAttribute("SKIP_HEADER_JS", true);
		
		CaissePersistant caisseBean = ContextAppliCaisse.getCaisseBean();
		
		
		if(env.startsWith(ContextAppli.APPLI_ENV.cais.toString())) {
			
			if(caisseBean == null){
				ControllerUtil.forward(this.getServletContext(), request, response, "/commun/secure_page.jsp");
				return;
			}
			
			boolean isMobile = env.equals(ContextAppli.APPLI_ENV.cais_mob.toString());
			
			IArticleService articleService = (IArticleService)ServiceUtil.getBusinessBean(IArticleService.class);
			IMenuCompositionService menuCompoService = (IMenuCompositionService)ServiceUtil.getBusinessBean(IMenuCompositionService.class);
			
			PagerBean pagerBean = isMobile ? null : ControllerUtil.managePager(request, "FAV");
			request.setAttribute("listArticleFavoris", caisseService.getFavorisCaisse(pagerBean));
			// Familles
			PagerBean pagerBeanFam = isMobile ? null : ControllerUtil.managePager(request, "FAM");
			List<FamillePersistant> listFamille = caisseWebService.getListFamilleCaissePagination(caisseBean.getId(), pagerBeanFam);
			request.setAttribute("listFamille", listFamille);
			
			// Menus
			PagerBean pagerBeanMnu = isMobile ? null : ControllerUtil.managePager(request, "MNU");
			List<MenuCompositionPersistant> listMenu = menuCompoService.getListeMenuCaissePagination(caisseBean.getId(), pagerBeanMnu);
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
			if(env.equals(ContextAppli.APPLI_ENV.cais_cli.toString())){
				String adresseIp = null;
				List<CaissePersistant> listCmientCaisse = caisseService.getListCaisseActive(ContextAppli.TYPE_CAISSE_ENUM.CAISSE_CLIENT.toString(), true);
				
	            if(listCmientCaisse.size() == 0){
		            adresseIp = request.getRemoteAddr();
		            if (adresseIp.equalsIgnoreCase("0:0:0:0:0:0:0:1")) {// Si on est sur la même machine (local ost)
		                InetAddress inetAddress = InetAddress.getLocalHost();
		                adresseIp = inetAddress.getHostAddress();
		            }
	            } else{
	            	adresseIp = listCmientCaisse.get(0).getAdresse_mac();
	            }
				// On initialise car sans authentification
				 CaissePersistant caisse = (CaissePersistant) articleService.getSingleResult(articleService.getQuery("from CaissePersistant where adresse_mac=:mac "
		            		+ "and type_ecran=:typeEcran")
		                  .setParameter("mac", adresseIp)
		                  .setParameter("typeEcran", ContextAppli.TYPE_CAISSE_ENUM.CAISSE_CLIENT.toString()));
				
				 if(caisse != null){
					 JourneePersistant journeeOuverte = caisseWebService.getLastJourne();
					 CaisseJourneePersistant journeeCaisseOuverte = caisseWebService.getJourneCaisseOuverte(caisse.getId());
			         MessageService.getGlobalMap().put("CURRENT_JOURNEE_CAISSE", journeeCaisseOuverte);
			         MessageService.getGlobalMap().put("CURRENT_JOURNEE", journeeOuverte);
				 }
				
				ControllerUtil.forward(this.getServletContext(), request, response, "/domaine/caisse_restau/client-place/home.jsp");
				return;
			} else if(env.equals(ContextAppli.APPLI_ENV.cais_mob.toString())){
				ControllerUtil.forward(this.getServletContext(), request, response, "/domaine/caisse_restau/mobile/caisse-main.jsp");
				return;
			} else{ 
				ControllerUtil.forward(this.getServletContext(), request, response, "/domaine/caisse_restau/normal/caisse-main.jsp");
				return;
			}
        } else if(env.equals(ContextAppli.APPLI_ENV.pres.toString())) {
        	ControllerUtil.forward(this.getServletContext(), request, response, "/domaine/caisse_restau/presentoire/presentoire-frame.jsp");
        	return;
        } else if(env.equals(ContextAppli.APPLI_ENV.cuis.toString())) {
        	// Parmétrage pilotage des écrans cuisine
        	//Changer statut VALIDEE vers EN PREPARATION depuis => EP=Ecran pilotage, EC=Ecran cuisine, ECP=Les deux 
        	boolean isValidationEcran = !"EP".equals(ContextGloabalAppli.getGlobalConfig("ECRAN_CMD_VALIDE"));
        	//cChanger statut EN PREPARATION vers PRETE depuis : => EP=Ecran pilotage, EC=Ecran cuisine, ECP=Les deux
        	boolean isPreparationEcran = !"EP".equals(ContextGloabalAppli.getGlobalConfig("ECRAN_CMD_ENPREPARATION"));
        	
        	// Onglets statuts à afficher dans la cuisine : => V=Validée, EP=En préparation, P=Prête
        	ControllerUtil.setUserAttribute("ecran_statut", ";"+ContextGloabalAppli.getGlobalConfig("ECRAN_STATUT")+";", request);
        	ControllerUtil.setUserAttribute("isValidationEcran", isValidationEcran, request);
        	ControllerUtil.setUserAttribute("isPreparationEcran", isPreparationEcran, request);
        	
        	//------------------------ // Parmétrage d'inventaire de cuisine
            ICuisineJourneeService cuisineJourneeService = (ICuisineJourneeService)ServiceUtil.getBusinessBean(ICuisineJourneeService.class);
            
        	boolean isInventaireRequired = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("INVENTAIRE_CUISINE"));
        	JourneePersistant journeeBean = ContextAppliCaisse.getJourneeBean();
        	
        	if(journeeBean == null) {
        		journeeBean = new JourneePersistant();
        	}
        	int nbrInventaire = cuisineJourneeService.getNbrInventaire(journeeBean.getId());
        	boolean isModeInventaire = false;  // Si True: Obligation de saisir l'inventaire

			CuisineJourneePersistant lastCuisineJournee = cuisineJourneeService.getLastCuisineJournee(caisseBean.getId());
    		
			if(isInventaireRequired && caisseBean.getOpc_stock_cible() != null){ 
    			if(lastCuisineJournee == null || !lastCuisineJournee.getOpc_journee().getId().equals(journeeBean.getId())) { 
    				isModeInventaire = true; // Changement de la journee
    			} else {
					UserPersistant opc_user_cloture = lastCuisineJournee.getOpc_user_cloture();
					if(opc_user_cloture != null && !opc_user_cloture.getId().equals(ContextAppli.getUserBean().getId()) && nbrInventaire < 2) {
						Long inventaireId = lastCuisineJournee.getOpc_inventaire_cloture().getId(); // au changement de shift/Utilisateur
						request.setAttribute("inventaireId", inventaireId);
						isModeInventaire = true;
					}else if(opc_user_cloture== null || opc_user_cloture.getId().equals(ContextAppli.getUserBean().getId()) && !lastCuisineJournee.getIs_Manuel()){
						isModeInventaire = false;
					}
				}
    		}
        	request.setAttribute("isModeInventaire", isModeInventaire);
        	
        	ControllerUtil.forward(this.getServletContext(), request, response, "/domaine/caisse_restau/cuisine/cuisine-frame.jsp");
        	return;
        } else if(env.equals(ContextAppli.APPLI_ENV.pil.toString())) {
        	boolean isValidationEcran = !"EC".equals(ContextGloabalAppli.getGlobalConfig("ECRAN_CMD_VALIDE"));
        	boolean isPreparationEcran = !"EC".equals(ContextGloabalAppli.getGlobalConfig("ECRAN_CMD_ENPREPARATION"));
        	ControllerUtil.setUserAttribute("isValidationEcran", isValidationEcran, request);
        	ControllerUtil.setUserAttribute("isPreparationEcran", isPreparationEcran, request);
        	
            List<CaissePersistant> listCaisse = caisseService.getListCaisseActive(ContextAppli.TYPE_CAISSE_ENUM.CUISINE.toString(), true);
    		request.setAttribute("listCaisseCuisine", listCaisse);
    		
        	ControllerUtil.forward(this.getServletContext(), request, response, "/domaine/caisse_restau/cuisine/pilotage-cuisine-frame.jsp");
        	return;
        } else if(env.equals(ContextAppli.APPLI_ENV.affi_caisse.toString())) {
        	ParametrageRightsConstantes.setMapEffect(request);
        	ControllerUtil.forward(this.getServletContext(), request, response, "/domaine/caisse/afficheur/client-trackcmd.jsp");
        	return;
        } else if(env.equals(ContextAppli.APPLI_ENV.affi_salle.toString())) {
        	ControllerUtil.forward(this.getServletContext(), request, response, "/domaine/caisse_restau/afficheur/afficheurClient-frame.jsp");
        	return;
        }
	} 
}
