
package appli.controller.domaine.caisse.action.mobile;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.locationtech.jts.geom.Coordinate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import appli.controller.domaine.administration.action.LoginAction;
import appli.controller.domaine.administration.bean.EtablissementBean;
import appli.controller.domaine.administration.bean.JourneeBean;
import appli.controller.domaine.administration.bean.UserBean;
import appli.controller.domaine.caisse.ContextAppliCaisse;
import appli.controller.domaine.caisse.action.CaisseWebBaseAction;
import appli.controller.domaine.caisse.action.ClientTrackCmdSocketController;
import appli.controller.domaine.caisse.action.CmdEnLigneSocket;
import appli.controller.domaine.habilitation.bean.ProfileBean;
import appli.controller.domaine.personnel.bean.ClientBean;
import appli.controller.domaine.stock.bean.ArticleStockInfoBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.SRC_CMD;
import appli.controller.domaine.util_erp.ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM;
import appli.model.domaine.administration.persistant.IpAddressPersistant;
import appli.model.domaine.administration.persistant.UserPersistant;
import appli.model.domaine.administration.service.IClientService;
import appli.model.domaine.administration.service.IEtablissementService;
import appli.model.domaine.administration.service.IUserService;
import appli.model.domaine.caisse.service.ICaisseMobileService;
import appli.model.domaine.caisse.service.ICaisseService;
import appli.model.domaine.caisse.service.ICaisseWebService;
import appli.model.domaine.caisse.service.IMenuCompositionService;
import appli.model.domaine.fidelite.dao.IPortefeuille2Service;
import appli.model.domaine.habilitation.persistant.ProfilePersistant;
import appli.model.domaine.habilitation.service.IProfileService;
import appli.model.domaine.personnel.persistant.ClientContactPersistant;
import appli.model.domaine.personnel.persistant.ClientPersistant;
import appli.model.domaine.personnel.service.IEmployeService;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.stock.service.IArticleService;
import appli.model.domaine.stock.service.IFamilleService;
import appli.model.domaine.stock.service.IMouvementService;
import appli.model.domaine.vente.persistant.CaisseJourneePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import appli.model.domaine.vente.persistant.ListChoixDetailPersistant;
import appli.model.domaine.vente.persistant.ListChoixPersistant;
import appli.model.domaine.vente.persistant.MenuCompositionDetailPersistant;
import appli.model.domaine.vente.persistant.MenuCompositionPersistant;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.ContextGloabalAppli;
import framework.controller.ControllerUtil;
import framework.controller.annotation.WorkController;
import framework.model.beanContext.AbonnementBean;
import framework.model.beanContext.EtablissementOuverturePersistant;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.DateUtil.TIME_ENUM;
import framework.model.common.util.EncryptionEtsUtil;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.audit.ReplicationGenerationEventListener;

@WorkController(nameSpace="caisse", bean=JourneeBean.class, jspRootPath="/domaine/caisse/mobile-client")
public class ClientMobileAction extends ActionBase {
	@Inject
	private ICaisseMobileService caisseMobileService;
	@Inject
	private IUserService userService;
	@Inject
	private IEtablissementService etsService;
	@Inject
	private IClientService clientService;
	@Inject
	private IProfileService profileService;
	@Inject
	private ICaisseWebService caisseWebService;
	@Inject
	private IPortefeuille2Service portefeuilleService2;
	@Inject
	private ICaisseService caisseService;
	@Inject
	private IMouvementService mouvementService;
	@Inject
	private IFamilleService familleService;
	@Inject
	private IArticleService articleService;
	public static Integer nombreDeCommande = 0;
	
	// Backoffice commande en ligne -------------------------------------------
	public void initCmdLigne(ActionUtil httpUtil){
		httpUtil.setMenuAttribute("isMnuCaiBo", true);
		
	   	httpUtil.setRequestAttribute("list_commande", caisseMobileService.getListCmdNonValide("I"));
	   	httpUtil.setRequestAttribute("list_commandeAvenir", caisseMobileService.getListCmdNonValide("A"));
	   	//Session session = httpUtil.getRequest().getSession(false);
	   	httpUtil.setRequestAttribute("isToNotify",false);
	   	if(caisseMobileService.getListCmdNonValide("I").size()>nombreDeCommande) {
	   	httpUtil.setRequestAttribute("isToNotify",true);
	   	nombreDeCommande = caisseMobileService.getListCmdNonValide("I").size(); 
	   	}

	   	//	CmdEnLigneSocket.sendDataToScreen(session, 5);

		httpUtil.setDynamicUrl("/domaine/caisse/normal/cmd-ligne/cmd_ligne_validation.jsp");
	}
	
	public void initCmdLigneClient(ActionUtil httpUtil){
		httpUtil.setMenuAttribute("isMnuCaiBo", true);
		
	   	httpUtil.setRequestAttribute("list_client", caisseMobileService.getListClientNonValide());
		
		httpUtil.setDynamicUrl("/domaine/caisse/normal/cmd-ligne/cli_ligne_validation.jsp");
	}
	
	/**
	 * Statut client commande en ligne depuis BO
	 * @param httpUtil
	 */
	public void statClient(ActionUtil httpUtil){
		String statut = httpUtil.getParameter("stat");
		caisseMobileService.statutClient(httpUtil.getWorkIdLong(), statut);
		
		initCmdLigne(httpUtil);
	}
	
	
	public void sub_menu(ActionUtil httpUtil) {
		
		Long menuId = httpUtil.getWorkIdLong();
		if (menuId == null) {
			return;
		}
		IMenuCompositionService menuService = ServiceUtil.getBusinessBean(IMenuCompositionService.class);
		MenuCompositionPersistant mn = menuService.findById(menuId);
		httpUtil.setRequestAttribute("nbrMax", mn.getNombre_max());
		
		Map<String, List<ArticlePersistant>> mapDetailMnu = new LinkedHashMap<>();
		
		List<MenuCompositionPersistant> listMenu = menuService.getListeMenuEnfants(menuId,null,false);
		if(listMenu != null) {
			for(MenuCompositionPersistant menu : listMenu ) {
				List<ArticlePersistant> listArt = mapDetailMnu.get(menu.getLibelle());
				if(listArt == null) {
					listArt = new ArrayList<>();
					mapDetailMnu.put("M_"+menu.getId()+"-"+menu.getLibelle(), listArt);
				}
				
				for(MenuCompositionDetailPersistant det : menu.getList_composition()){
					if(det.getOpc_article() != null) {
						listArt.add(det.getOpc_article());
					} else if(det.getOpc_famille() != null) {
						listArt = new ArrayList<>();
						getArticlesFromFamille(det.getOpc_famille(), listArt, mapDetailMnu, menu);
					} else if(det.getOpc_list_choix() != null) {
						listArt = new ArrayList<>();
						getArticlesFromListChoix(det.getOpc_list_choix(), listArt, mapDetailMnu, menu);
					}
				}
			 }
		  }
		 httpUtil.setRequestAttribute("MapMenuSub", mapDetailMnu);
		 httpUtil.setDynamicUrl("/domaine/caisse/self-cmd/sub-menu.jsp");
	}



	private void getArticlesFromFamille(FamillePersistant opc_famille, List<ArticlePersistant> listArt, Map<String, List<ArticlePersistant>> mapDetailMnu,MenuCompositionPersistant menu) {
		
		 List<ArticlePersistant> listArt1 = new ArrayList<>();
 		mapDetailMnu.put("M_"+menu.getId()+"-"+"F_"+opc_famille.getId()+"-"+opc_famille.getLibelle(), listArt1);
 		List<ArticlePersistant> listArticle = articleService.getListFamilleArticle(opc_famille.getId(), null);
 		for( ArticlePersistant art : listArticle) {
 			listArt1.add(art);
 		}
 
 		
 		List<FamillePersistant> listFamEnfant = familleService.getFamilleEnfants("CU", opc_famille.getId(), true);
 		for (FamillePersistant famP : listFamEnfant) {
 			listArt1 = new ArrayList<>();
 			mapDetailMnu.put("M_"+menu.getId()+"-"+"F_"+famP.getId()+"-"+famP.getLibelle(), listArt1);
 			List<ArticlePersistant> listArticleChild = articleService.getListFamilleArticle(famP.getId(),null);
 			for( ArticlePersistant art : listArticleChild) {
 				listArt1.add(art);
 			}
 
 		}
	}
	
	
	private void getArticlesFromListChoix(ListChoixPersistant opc_list_choix, List<ArticlePersistant> listArt,Map<String, List<ArticlePersistant>> mapDetailMnu,MenuCompositionPersistant menu) {
		if(opc_list_choix.getList_choix_detail() != null) {
			for(ListChoixDetailPersistant choix : opc_list_choix.getList_choix_detail()) {
				if(choix.getOpc_article() != null) {
					listArt.add(choix.getOpc_article());
				} else if(choix.getOpc_famille() != null) {
					listArt = new ArrayList<>();
					mapDetailMnu.put("M_"+menu.getId()+"-"+"F_"+choix.getOpc_famille().getId()+"-"+choix.getOpc_famille().getLibelle(), listArt);
					getArticlesFromFamille(choix.getOpc_famille(), listArt, mapDetailMnu, menu);
					
					List<FamillePersistant> listFamEnfant = familleService.getFamilleEnfants("CU", choix.getOpc_famille().getId(), true);
					for (FamillePersistant famP : listFamEnfant) {
						listArt = new ArrayList<>();
						mapDetailMnu.put("M_"+menu.getId()+"-"+"F_"+famP.getId()+"-"+famP.getLibelle(), listArt);
						getArticlesFromFamille(famP, listArt, mapDetailMnu, menu);
					 } 
					
					//getArticlesFromFamille(choix.getOpc_famille(), listArt, mapDetailMnu, menu);
				   } else if(choix.getOpc_list_choix() != null) {
					listArt = new ArrayList<>();
					mapDetailMnu.put("M_"+menu.getId()+"-"+"C_"+choix.getOpc_list_choix().getId()+"-"+choix.getOpc_list_choix().getLibelle(), listArt);
					getArticlesFromListChoix(choix.getOpc_list_choix(), listArt, mapDetailMnu, menu);
				}
			}
		}
	}
	

	
	
	
	/**
	 * Statut commande en ligne depuis BO
	 * @param httpUtil
	 */
	public void statCmd(ActionUtil httpUtil){
		String statut = httpUtil.getParameter("stat");
		Long mvmId = httpUtil.getWorkIdLong();
		
        caisseMobileService.statutCommande(mvmId, statut); 
		
		initCmdLigne(httpUtil);
	}
	// -----------------------------------------------------------------------------
	
	/*public void initHome(ActionUtil httpUtil){
		Map<String, List<EtablissementPersistant>> mapEts = etsService.getListEtablissement();
		httpUtil.setRequestAttribute("mapEts", mapEts);
		
		httpUtil.setDynamicUrl("/domaine/caisse/"+httpUtil.getUserAttribute("ENV_MOBILE")+"/etablissement-list.jsp");
	}*/
	
	public void initFavoris(ActionUtil httpUtil){
		List<EtablissementBean> listEts = etsService.findAll();
		httpUtil.setRequestAttribute("listEts", listEts);
		
		httpUtil.setDynamicUrl("/domaine/caisse/"+httpUtil.getUserAttribute("ENV_MOBILE")+"/favoris-list.jsp");
	}
	
	public void manageFavoris(ActionUtil httpUtil){
		Long etsId = httpUtil.getWorkIdLong();
		boolean isFromFav = httpUtil.getParameter("isF") != null;
		
		ClientPersistant clientP = ContextAppli.getUserBean().getOpc_client();
		ClientBean clientB = clientService.findById(clientP.getId());
		
		String finder = ";"+etsId+";";
		//
		boolean isAdded = false;
		if(clientP.getEts_fav() != null && clientP.getEts_fav().indexOf(finder) != -1) {
			clientB.setEts_fav(clientP.getEts_fav().replaceAll(finder, "")); 
			isAdded = true;
		} else {
			clientB.setEts_fav(clientP.getEts_fav()+finder);
		}
		clientService.merge(clientB);
		ContextAppli.getUserBean().setOpc_client(clientB);
		
		if(isFromFav) {
			initFavoris(httpUtil);
		} else {
			httpUtil.writeResponse("MSG_CUSTOM:"+(isAdded ? "Retiré des favoris." : "Ajouté aux favoris."));
		}
	}
	
	public void initHistorique(ActionUtil httpUtil){
		UserPersistant userP = caisseMobileService.findById(UserPersistant.class, ContextAppli.getUserBean().getId());
		List<CaisseMouvementPersistant> listCmd = null; 
		if(userP.getOpc_client() != null) {
			listCmd = caisseMobileService.getCmdClient(userP.getOpc_client().getId());
		}
		
		httpUtil.setRequestAttribute("listCmd", listCmd);
		
		httpUtil.setDynamicUrl("/domaine/caisse/"+httpUtil.getUserAttribute("ENV_MOBILE")+"/historique-list.jsp");
	}
	public void selectHistorique(ActionUtil httpUtil) {
		Long mvmId = httpUtil.getWorkIdLong();
		httpUtil.setRequestAttribute("caisseMouvement", caisseMobileService.findById(CaisseMouvementPersistant.class, mvmId));
		
		httpUtil.setDynamicUrl("/domaine/caisse/"+httpUtil.getUserAttribute("ENV_MOBILE")+"/historique-edit.jsp");
	}
	
	public void initPaiement(ActionUtil httpUtil){
		EtablissementPersistant etablissementBean = ContextAppli.getEtablissementBean();
		//
		if(etablissementBean.getDuree_cmd() != null && etablissementBean.getDuree_cmd() > 0) {
			int nbrCmd = caisseMobileService.getTotalCmdValidee(ContextAppliCaisse.getJourneeBean().getId());
			int totalTimeMinute = nbrCmd * etablissementBean.getDuree_cmd();
			
			Date dateEstimee = DateUtil.addSubstractDate(new Date(), TIME_ENUM.MINUTE, totalTimeMinute);
			
			httpUtil.setRequestAttribute("dateEstimee", DateUtil.dateToString(dateEstimee, "dd/MM/yyyy HH:mm"));
		}
		
		httpUtil.setDynamicUrl("caisse-web.caisseWeb.initPaiement");
	}
	
	/**
	 * @param httpUtil
	 */
	public void valider_commande(ActionUtil httpUtil){
		String addr = httpUtil.getRequest().getRemoteHost();
		
		CaisseMouvementPersistant CURRENT_COMMANDE = (CaisseMouvementPersistant) httpUtil.getUserAttribute("CURRENT_COMMANDE");
		String type_commande = httpUtil.getParameter("typeCmd");
		
		if(StringUtil.isEmpty(type_commande)) {
			MessageService.addBannerMessage("Veuillez sélectionner un type de commande.");
			return;
		}
		if(StringUtil.isEmpty(httpUtil.getParameter("mode_paie"))) {
			MessageService.addBannerMessage("Veuillez sélectionner un mode de paiement.");
			return;
		}
		BigDecimal fraisLivrason = BigDecimalUtil.get(ContextGloabalAppli.getGlobalConfig(ContextAppli.PARAM_APPLI_ENUM.FRAIS_LIVRAISON.toString()));
		boolean isFraisLivraison = !BigDecimalUtil.isZero(fraisLivrason);
		CaisseJourneePersistant journeeCaisseOuverte = ContextAppliCaisse.getJourneeCaisseBean();
		CaisseWebBaseAction cwA = new CaisseWebBaseAction();
		boolean isPoints =  StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("POINTS"));
		boolean isPortefeuille =  StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("PORTEFEUILLE"));
		
		CURRENT_COMMANDE.setType_commande(type_commande);
		CURRENT_COMMANDE.setOpc_client(ContextAppli.getUserBean().getOpc_client());
		CURRENT_COMMANDE.setSrc_cmd(SRC_CMD.MOB_ENV.toString());
		
		if(StringUtil.isNotEmpty(httpUtil.getParameter("date_souhaite"))) {
			String heureSouhaitee = httpUtil.getParameter("heure_souhaite");
			if(StringUtil.isEmpty(heureSouhaitee)) {
				heureSouhaitee = DateUtil.dateToString(new Date(), "HH:mm");
			}
			
			String dtSouhaitee = httpUtil.getParameter("date_souhaite") + " " + heureSouhaitee; 
			CURRENT_COMMANDE.setDate_souhaite(DateUtil.stringToDate(dtSouhaitee, "dd/MM/yyyy HH:mm"));
		} else {
			CURRENT_COMMANDE.setDate_souhaite(null);
		}
		
		// Contrôle par rapport au paramétage------------------------
		EtablissementPersistant etablissementBean = ContextAppli.getEtablissementBean();
		etablissementBean = caisseMobileService.findById(EtablissementPersistant.class, etablissementBean.getId());
		
		if(etablissementBean.getMax_dist() != null 
				&& etablissementBean.getMax_dist() > 0
				&& etablissementBean.getPosition_lat() != null
				&& CURRENT_COMMANDE.getOpc_client().getPosition_lat()!= null) {
			
			
			Map data = LivreurMobileAction.getMapInfos(
					new Coordinate(etablissementBean.getPosition_lat().intValue(), etablissementBean.getPosition_lng().intValue()), 
					new Coordinate(CURRENT_COMMANDE.getOpc_client().getPosition_lat().intValue(), CURRENT_COMMANDE.getOpc_client().getPosition_lng().intValue()));
			BigDecimal dist = (BigDecimal) data.get("distance");
			
			if(dist != null && dist.compareTo(BigDecimalUtil.get(etablissementBean.getMax_dist())) > 0) {
				MessageService.addBannerMessage("La distance maximale autoriséee ("+dist+") est dépassée");
				return;
			}
		}
		if(etablissementBean.getMax_heure_cmd() != null 
				&& etablissementBean.getMax_heure_cmd() > 0
				&& CURRENT_COMMANDE.getDate_souhaite() != null) {
			Date dateMax = DateUtil.addSubstractDate(new Date(), TIME_ENUM.HOUR, etablissementBean.getMax_heure_cmd());
			if(CURRENT_COMMANDE.getDate_souhaite().after(dateMax)) {
				MessageService.addBannerMessage("La date maximale autorisée pour cette commande est : "+DateUtil.dateToString(dateMax, "dd/MM/yyyy HH:mm"));
				return;	
			}
		}

		if(BooleanUtil.isTrue(etablissementBean.getIs_cmd_ets_ferme())){
			if(etablissementBean.getList_ouverture() != null) {
				boolean isInclude = false;
				Date currDate = new Date();
				for(EtablissementOuverturePersistant etsOuv : etablissementBean.getList_ouverture()) {
					if(StringUtil.isEmpty(etsOuv.getJour_ouverture())) {
						continue;
					}
					String jourCal = DateUtil.getCalendar(currDate).getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG_FORMAT, Locale.FRANCE);
					if(!jourCal.toLowerCase().equals(etsOuv.getJour_ouverture().toLowerCase())) {
						continue;
					}
					
					String[] hDebutMatin = StringUtil.getArrayFromStringDelim((etsOuv.getHeure_debut_matin()==null ? "00:00" : etsOuv.getHeure_debut_matin()), ":");
					String[] hFinMatin = StringUtil.getArrayFromStringDelim((etsOuv.getHeure_fin_matin() == null ? etsOuv.getHeure_fin_midi() : etsOuv.getHeure_fin_matin()), ":");

					String[] hDebutMidi = StringUtil.getArrayFromStringDelim((etsOuv.getHeure_debut_midi()==null?etsOuv.getHeure_debut_matin() : etsOuv.getHeure_debut_midi()), ":");
					String[] hFinMidi = StringUtil.getArrayFromStringDelim((etsOuv.getHeure_fin_midi()==null ? "23:59" : etsOuv.getHeure_fin_midi()), ":");
					
					Calendar currCal = DateUtil.getCalendar(currDate);
					currCal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hDebutMatin[0]));
					currCal.set(Calendar.MINUTE, Integer.valueOf(hDebutMatin[1]));
					Date dateDebutMat= currCal.getTime();
					
					currCal = DateUtil.getCalendar(currDate);
					currCal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hFinMatin[0]));
					currCal.set(Calendar.MINUTE, Integer.valueOf(hFinMatin[1]));
					Date dateFinMat= currCal.getTime();
					
					currCal = DateUtil.getCalendar(currDate);
					currCal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hDebutMidi[0]));
					currCal.set(Calendar.MINUTE, Integer.valueOf(hDebutMidi[1]));
					Date dateDebutMid= currCal.getTime();
					
					currCal = DateUtil.getCalendar(currDate);
					currCal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hFinMidi[0]));
					currCal.set(Calendar.MINUTE, Integer.valueOf(hFinMidi[1]));
					Date dateFinMid= currCal.getTime();
					
					if(DateUtil.isInDates(currDate, dateDebutMat, currDate, dateFinMat, true)) {
						isInclude = true;
						break;
					}
					if(DateUtil.isInDates(currDate, dateDebutMid, currDate, dateFinMid, true)) {
						isInclude = true;
						break;
					}
				}
				if(isInclude) {
					MessageService.addBannerMessage("L'établissement n'est pas disponible pour la commande à cette heure");
					return;	
				}
			}
		}
		cwA.majTotalMontantCommande(httpUtil, CURRENT_COMMANDE);
		String modePaie = httpUtil.getParameter("mode_paie");
		CURRENT_COMMANDE.setMode_paiement(modePaie);
		
		//
		STATUT_CAISSE_MOUVEMENT_ENUM statut = null;
//		boolean isAutoValidateCB = BooleanUtil.isTrue(etablissementBean.getIs_valid_auto_cb());
		boolean isToValidateEsp = (!BooleanUtil.isTrue(etablissementBean.getIs_valid_auto_esp()) && "ESPECES".equals(modePaie));
		
		if(isToValidateEsp
				|| (CURRENT_COMMANDE.getDate_souhaite() != null && CURRENT_COMMANDE.getDate_souhaite().after(new Date()))
				){
			CURRENT_COMMANDE.setIs_tovalidate(true);
			CURRENT_COMMANDE.setIs_annule(true);
			statut = ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.ANNUL;
			CURRENT_COMMANDE.setLast_statut(statut.toString());
		} else {
			CURRENT_COMMANDE.setIs_tovalidate(false);
			statut = ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE;
			CURRENT_COMMANDE.setLast_statut(statut.toString());
		}
			
		//Mode de paiement
		if(!isToValidateEsp) {
			BigDecimal mtt_commande_net = CURRENT_COMMANDE.getMtt_commande_net();
			CURRENT_COMMANDE.setMtt_donne_all(mtt_commande_net);
			
			if("ESPECES".equals(modePaie)) {
				CURRENT_COMMANDE.setMtt_donne(mtt_commande_net);
			} else if("CARTE".equals(modePaie)) {
		        CURRENT_COMMANDE.setMtt_donne_cb(mtt_commande_net);
			} else if("POINTS".equals(modePaie)) {
		        CURRENT_COMMANDE.setMtt_donne_point(BigDecimalUtil.add(CURRENT_COMMANDE.getMtt_donne_point(), mtt_commande_net));
			} else if("RESERVE".equals(modePaie)) {
				CURRENT_COMMANDE.setMtt_portefeuille(BigDecimalUtil.add(CURRENT_COMMANDE.getMtt_portefeuille(), mtt_commande_net));
			}
		}
		
        cwA.addMargeCaissier(CURRENT_COMMANDE); 
        CURRENT_COMMANDE.setOpc_caisse_journee(journeeCaisseOuverte);
        
        String ref_commande = caisseWebService.getNextRefCommande();
        CURRENT_COMMANDE.setRef_commande(ref_commande);
        
        STATUT_CAISSE_MOUVEMENT_ENUM statutF = statut;
        boolean isCmdValidee = !BooleanUtil.isTrue(CURRENT_COMMANDE.getIs_tovalidate());
        
        AbonnementBean abonnementBean = ContextAppli.getAbonementBean();
        
		ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor)ServiceUtil.getBusinessBean("taskExecutor");
    	taskExecutor.submit(new Callable<Object>() {
            public Object call() throws Exception {
            	try{
	                //  Création mouvement
            		CaisseMouvementPersistant currCmd = caisseWebService.createMouvementCaisse(CURRENT_COMMANDE, statutF, isPoints);
	            	portefeuilleService2.majSoldePortefeuilleMvm(CURRENT_COMMANDE.getOpc_client().getId(), "CLI");
	            	
	            	if(isCmdValidee) {
	            		caisseService.gestionEcranImprimante(currCmd);
	            		
	            		if(abonnementBean.isOptStock()) {
	            			List<ArticleStockInfoBean> listArtInfos = caisseWebService.destockerArticleMvmRestau(currCmd);
	            			CURRENT_COMMANDE.setMvm_stock_ids(currCmd.getMvm_stock_ids());
	            			mouvementService.majQteArticleInfo(listArtInfos);
	            		}
	            		
	            		boolean isCtrlStockCaisse =  StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("INVENTAIRE_CAISSE"));
	        			if(isCtrlStockCaisse) {
	        				mouvementService.majQteArticleCaisseInfo(CURRENT_COMMANDE, false);
	        			}
	            	}
            	} catch(Exception e){
            		e.printStackTrace();  
            	}
            	return null;
            }
    	});
		
		initNewCmd(httpUtil);

        MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "Commande validée", "<h3>La commande est validée avec succès.</h3>");
		httpUtil.setDynamicUrl("/domaine/caisse/"+httpUtil.getUserAttribute("ENV_MOBILE")+"/commande-detail.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void annulerCommande(ActionUtil httpUtil) {
		CaisseMouvementPersistant currCmd = (CaisseMouvementPersistant) httpUtil.getUserAttribute("CURRENT_COMMANDE");
			
        if(currCmd != null && currCmd.getId() != null){
        	UserBean userAnnulBean = (UserBean) httpUtil.getRequestAttribute("user_annul");
            // Création de la comm
            caisseWebService.annulerMouvementCaisse(currCmd.getId(), userAnnulBean);
            if(currCmd.getOpc_client() != null){
            	portefeuilleService2.majSoldePortefeuilleMvm(currCmd.getOpc_client().getId(), "CLI");
            }
        } else {
        	initNewCmd(httpUtil);
        }
        MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "Commande annulée", "La commande est annulée avec succès.");
        
        httpUtil.setDynamicUrl("/domaine/caisse/"+httpUtil.getUserAttribute("ENV_MOBILE")+"/commande-detail.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	private void initNewCmd(ActionUtil httpUtil){
		// Rest curr cmd
		httpUtil.removeUserAttribute("CURRENT_MENU_NUM");
        httpUtil.removeUserAttribute("HISTORIQUE_NAV");
        httpUtil.removeUserAttribute("CURRENT_MENU_COMPOSITION");
        httpUtil.removeUserAttribute("CURRENT_ITEM_ADDED");
        
        CaisseMouvementPersistant newCmd = new CaisseMouvementPersistant();
        newCmd.setRef_commande(""+System.currentTimeMillis());
        newCmd.setDate_vente(new Date());
        newCmd.setOpc_caisse_journee(ContextAppliCaisse.getJourneeCaisseBean());
        //
        newCmd.setList_article(new ArrayList<>());
        newCmd.setList_offre(new ArrayList<>());
		newCmd.setMax_idx_client(1);
		httpUtil.setUserAttribute("CURRENT_IDX_CLIENT", 1);// Index client
		httpUtil.removeUserAttribute("CURRENT_TABLE_REF");// Ref table
		httpUtil.removeMenuAttribute("COUVERTS_TABLE"); // Nombre de couverts
		httpUtil.setUserAttribute("CURRENT_COMMANDE", newCmd);
	}
	
	public void backupLoginPw(ActionUtil httpUtil){
		EncryptionEtsUtil encryptionUtil = new EncryptionEtsUtil(EncryptionEtsUtil.getDecrypKey());
		
		String login = httpUtil.getParameter("log.login");
		String mail = httpUtil.getParameter("log.mail");
		
		List<ClientPersistant> listClientP = userService.findByField(ClientPersistant.class, "mail", mail);
		List<UserPersistant> listUserP = userService.findByField(UserPersistant.class, "login", login);
		
		if(listClientP.size() == 0 || listUserP.size() == 0) {
			MessageService.addFieldMessage("log.login", "Le numéro de téléphone ou l'email est erroné");
			return;
		}
		ClientPersistant clientP = listClientP.get(0);
		UserPersistant userP = listUserP.get(0);
				
		if(userP.getOpc_client() == null || !userP.getOpc_client().getId().equals(clientP.getId())) {
			MessageService.addFieldMessage("log.login", "Le numéro de téléphone ou l'email est erroné");
			return;
		}
		
		// Envoi mail
		// ...
		
		httpUtil.writeResponse("<h2 class='cli-msg-success'>Les informations de connexion ont été envoyés par mail.</h2>");
	}

	public void setEtsIpToken(ActionUtil httpUtil) {
		String store = httpUtil.getParameter("store");
		String token = httpUtil.getParameter("jtn");

		IEmployeService etsService = ServiceUtil.getBusinessBean(IEmployeService.class);
		IpAddressPersistant ip_table = new IpAddressPersistant();
		ip_table.setIp(httpUtil.getRequest().getRemoteHost());
		ip_table.setEts_token(token);
		ip_table.setDate_creation(new Date());
		//
 		etsService.mergeEntity(ip_table);
 		
		String url = "AND".equals(store) ? 
							"https://play.google.com/store/apps/dev?id=5700313618786177705"
							: "https://itunes.apple.com/us/app/keynote/id361285480?mt=8";
		
		ControllerUtil.redirect(httpUtil.getResponse(), url);
	}
	
	public void init_panier(ActionUtil httpUtil){
		httpUtil.setDynamicUrl("/domaine/caisse/mobile-market/panier.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void init_login(ActionUtil httpUtil){
		String src = httpUtil.getParameter("act");
		
		if("pw".equals(src)) {
			httpUtil.setDynamicUrl("/domaine/caisse/mobile-market/auth-forgot.jsp");			
		} else if("reg".equals(src)) {
			httpUtil.setDynamicUrl("/domaine/caisse/mobile-market/auth-register.jsp");	
		} else if("log".equals(src)) {
			httpUtil.setDynamicUrl("/domaine/caisse/mobile-market/auth-login.jsp");	
		} else if("verif".equals(src)) {
			httpUtil.setDynamicUrl("/domaine/caisse/mobile-market/auth-verif.jsp");	
		} else {
			httpUtil.setDynamicUrl("/domaine/caisse/mobile-market/auth.jsp");
		}
	}
	
	public void login(ActionUtil httpUtil){
			
		EncryptionEtsUtil encryptionUtil = null;
		encryptionUtil = new EncryptionEtsUtil(EncryptionEtsUtil.getDecrypKey());
		
		String login = httpUtil.getParameter("log.login");
		String password = httpUtil.getParameter("log.password");

		if(StringUtil.isEmpty(login) && httpUtil.getParameter("isA") != null) {
			login = httpUtil.getParameter("log");
			password = httpUtil.getParameter("pw");
		}
		
		if(StringUtil.isEmpty(login)) {
			MessageService.addFieldMessage("log.login", "Le téléphone est obligatoire");
			return;
		} else if(StringUtil.isEmpty(password)) {
			MessageService.addFieldMessage("log.password", "Le mot de passe est obligatoire");
			return;
		}
		
		login = login.replaceAll("-", "");
		UserBean userBean = userService.getUserByLoginAndPw(login, encryptionUtil.encrypt(password)); 
		if(userBean == null){
			MessageService.addGrowlMessage("", "Le téléphone ou le mot de passse est erroné");	
			return;
		}
		if(!userBean.isInProfile("CLIENT")){
			MessageService.addGrowlMessage("", "Votre profile ne permet d'accèder à cet espace.");	
			return;
		} else if(userBean.getOpc_client() == null) {
			MessageService.addGrowlMessage("", "Ce compte n'est pas associé à un compte client.");	
			return;
		}
		
		// Si le compte est désactivé
		if(BooleanUtil.isTrue(userBean.getIs_tovalidate())){
			MessageService.addGrowlMessage("", "Votre Compte est en attente de validation");
			return;
		} else if(BooleanUtil.isTrue(userBean.getIs_desactive())){
			MessageService.addGrowlMessage("", "Votre Compte est désactivé");
			return;
		}
		
		EtablissementPersistant etsP = userBean.getOpc_etablissement();
		
		if(BooleanUtil.isTrue(etsP.getIs_disable())) {
			MessageService.addGrowlMessage("Application désactivée", "L'application est désactivée depuis le Cloud.");
			MessageService.getGlobalMap().remove(ProjectConstante.SESSION_GLOBAL_USER);
			return;
		}

		LoginAction.loadAbonnement();
		AbonnementBean abnBean = (AbonnementBean) MessageService.getGlobalMap().get("ABONNEMENT_BEAN");
		
		if(!BooleanUtil.isTrue(abnBean.isOptPlusCmdVitrine())) {
			//MessageService.addGrowlMessage("", "Votre établissement n'est abonné à ce service");
			//return;
		}
		
		//Maj date derniere connexion
		userBean.setDate_connexion(new Date()); 
		userService.update(userBean);

		MessageService.getGlobalMap().put(ProjectConstante.SESSION_GLOBAL_USER, userBean);
		MessageService.getGlobalMap().put("GLOBAL_ETABLISSEMENT", ReflectUtil.cloneBean(etsP));
		MessageService.getGlobalMap().put("CURRENT_JOURNEE", userService.getLastJourne());
		
//		CaisseJourneePersistant journeeCaisseOuverte = caisseWebService.getJourneCaisseOuverte(caisseP.getId());
//		MessageService.getGlobalMap().put("CURRENT_JOURNEE_CAISSE", journeeCaisseOuverte);
		
		if(!"MOB_ENV".equals(httpUtil.getUserAttribute("SRC_URL")) && StringUtil.isNotEmpty(etsP.getUrl_vitrine())) {
			httpUtil.setUserAttribute("URL_ABONNE", etsP.getUrl_vitrine());
		}
		
//		if(envMobile != null) {
//			envMobile = envMobile.replaceAll("mobile-", "mob-");
//			httpUtil.writeResponse("REDIRECT:"+contextPath+"/"+envMobile + tkn);
//			return;
//		}
		
		String deviceToken = (String)httpUtil.getUserAttribute("DEVICE_TOKEN");
		if(StringUtil.isNotEmpty(deviceToken)) {
			UserPersistant userP = userService.findById(UserPersistant.class, userBean.getId());
			userP.setDevice_token(deviceToken);
			userService.mergeEntity(userP);
		}
		httpUtil.writeResponse("REDIRECT:");
	}
	
	public void loadHome(ActionUtil httpUtil) {
		// Familles
		List<FamillePersistant> listFamille = caisseWebService.getListFamilleCaissePagination(null, null);
		List<FamillePersistant> listFamilleLev1 = new ArrayList<>();
				
		for (FamillePersistant familleP : listFamille) {
			if(familleP.getLevel() == 1) {
				listFamilleLev1.add(familleP);
			}
		}
		httpUtil.setRequestAttribute("listFamille", listFamilleLev1);
		
		IMenuCompositionService menuCompositionService = ServiceUtil.getBusinessBean(IMenuCompositionService.class);
		
		// Menus
		List<MenuCompositionPersistant> listMenu = menuCompositionService.getListeMenu(true, true);
		List<MenuCompositionPersistant> listMenuLev1 = new ArrayList<>();
		
		for (MenuCompositionPersistant menuCompositionP : listMenu) {
			if(menuCompositionP.getLevel() == 1) {
				listMenuLev1.add(menuCompositionP);
			}
		}
		httpUtil.setRequestAttribute("listMenu", listMenuLev1);
		
		httpUtil.setDynamicUrl("/domaine/caisse/mobile-market/index.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void loadMenuDetail(ActionUtil httpUtil) {
		Long menuId = httpUtil.getWorkIdLong();
		IMenuCompositionService menuCompositionService = ServiceUtil.getBusinessBean(IMenuCompositionService.class);
		List<MenuCompositionPersistant> listDetail = menuCompositionService.getListeMenuEnfants(menuId, null, true);
		//
		httpUtil.setRequestAttribute("listDetail", listDetail);
		httpUtil.setDynamicUrl("/domaine/caisse/mobile-market/detail-cmd.jsp");
	}
	
	public void loadMenuEtapes(ActionUtil httpUtil) {
		httpUtil.setDynamicUrl("/domaine/caisse/mobile-market/detail-menu.jsp");
	}
	
	public void loadCompte(ActionUtil httpUtil) {
		if(StringUtil.isEmpty(httpUtil.getParameter("act"))) {
			httpUtil.setDynamicUrl("/domaine/caisse/mobile-market/compte.jsp");			
		} else {
			httpUtil.setDynamicUrl("/domaine/caisse/mobile-market/compte-"+httpUtil.getParameter("act")+".jsp");
		}
	}
	
	public void loadFavoris(ActionUtil httpUtil) {
		httpUtil.setDynamicUrl("/domaine/caisse/mobile-market/favoris.jsp");
	}	

	public void loadTendance(ActionUtil httpUtil) {
		httpUtil.setDynamicUrl("/domaine/caisse/mobile-market/tendance.jsp");
	}

	public void loadContact(ActionUtil httpUtil) {
		if(StringUtil.isEmpty(httpUtil.getParameter("act"))) {
			httpUtil.setDynamicUrl("/domaine/caisse/mobile-market/aide.jsp");			
		} else {
			httpUtil.setDynamicUrl("/domaine/caisse/mobile-market/aide-"+httpUtil.getParameter("act")+".jsp");
		}
	}
	
	public void loadHistorique(ActionUtil httpUtil) {
		httpUtil.setDynamicUrl("/domaine/caisse/mobile-market/historique.jsp");
	}

	public void loadTrackCmd(ActionUtil httpUtil) {
		httpUtil.setDynamicUrl("/domaine/caisse/mobile-market/cmd-localisation.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void loadMenus(ActionUtil httpUtil) {
		Long menuId = httpUtil.getWorkIdLong();
		IMenuCompositionService menuCompositionService = ServiceUtil.getBusinessBean(IMenuCompositionService.class);
		List<MenuCompositionPersistant> listDetail = menuCompositionService.getListeMenuEnfants(menuId, null, true);
		List<MenuCompositionPersistant> listEnfants = new ArrayList<>();
		//
		for (MenuCompositionPersistant menuP : listDetail) {
			if(BooleanUtil.isTrue(menuP.getIs_menu())) {
				listEnfants.add(menuP);
			}
		}
		//
		httpUtil.setRequestAttribute("menu", articleService.findById(MenuCompositionPersistant.class, menuId));
		httpUtil.setRequestAttribute("listMenu", listEnfants);
		
		httpUtil.setDynamicUrl("/domaine/caisse/mobile-market/menus-list.jsp");
	}	
	/**
	 * @param httpUtil
	 */
	public void loadFamilles(ActionUtil httpUtil) {
		Long familleId = httpUtil.getWorkIdLong();
		List<ArticlePersistant> listArticle = new ArrayList<>(); 
		
		listArticle.addAll(articleService.getListArticleActifs(familleId, false));
		
		List<FamillePersistant> listEnfants = familleService.getFamilleEnfants("CU", familleId, true);
		for (FamillePersistant familleP : listEnfants) {
			listArticle.addAll(articleService.getListArticleActifs(familleP.getId(), false));	
		}
		
		//
		httpUtil.setRequestAttribute("famille", articleService.findById(FamillePersistant.class, familleId));
		httpUtil.setRequestAttribute("listArticle", listArticle);
		
		httpUtil.setDynamicUrl("/domaine/caisse/mobile-market/famille-list.jsp");
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

//	public void generateSmsToken(ActionUtil httpUtil){
//		Random rand = new Random();
//		int token = rand.nextInt(10000);
//		System.out.println(token);
//		String phoneNumber = httpUtil.getParameter("client.telephone");
//		NotificationQueuService.sendSms(token, phoneNumber);
//		httpUtil.setUserAttribute("token_val", token);
//		httpUtil.writeResponse("OK");
//	}

	public void merge_compte(ActionUtil httpUtil){
		if(httpUtil.getParameter("isSub") == null) {// Si consultation
			if(ContextAppli.getUserBean() != null && ContextAppli.getUserBean().getOpc_client() != null) {
				ClientBean clientBean = clientService.findById(ContextAppli.getUserBean().getOpc_client().getId());
				httpUtil.setViewBean(clientBean);
			}
			
			httpUtil.setDynamicUrl("/domaine/caisse/"+httpUtil.getUserAttribute("ENV_MOBILE")+"/compte.jsp");
			return;
		}
		
		EncryptionEtsUtil encryptionUtil = null;
		encryptionUtil = new EncryptionEtsUtil(EncryptionEtsUtil.getDecrypKey());
		
		Long clientId = httpUtil.getWorkIdLong();
		String nom = httpUtil.getParameter("client.nom");
		String phone = httpUtil.getParameter("client.telephone");
		String mail = httpUtil.getParameter("client.mail");
		String adresse = httpUtil.getParameter("client.adresse");
		String password = httpUtil.getParameter("client.password");
		boolean is_notif = httpUtil.getParameter("client.is_statut_cmd") != null;
		boolean is_offre = httpUtil.getParameter("client.is_offre_cmd") != null;
		
		BigDecimal pos_lat = null;
		BigDecimal pos_lng = null;
		
		if(StringUtil.isNotEmpty(httpUtil.getParameter("adresse"))) {
			adresse = httpUtil.getParameter("adresse");
			pos_lat = BigDecimalUtil.get(httpUtil.getParameter("position_lat"));
			pos_lng = BigDecimalUtil.get(httpUtil.getParameter("position_lng"));
		}
		
		if(StringUtil.isEmpty(phone)) {
			MessageService.addFieldMessage("client.telephone", "Le numéro de téléphone est obligatoire");
			return;
		} else if(StringUtil.isEmpty(password)) {
			MessageService.addFieldMessage("client.password", "Le mot de passe est obligatoire");
			return;
		}
		if(StringUtil.isEmpty(nom)) {
			MessageService.addFieldMessage("client.nom", "Le nom est obligatoire");
			return;
		}
		phone = phone.replaceAll("-", "");
		// ----------------------
		ClientBean clientB = null; 
		UserBean userB = null;
		UserPersistant userP = userService.getUserByLogin(phone);
		if(userP != null) {
			userB = userService.findById(userP.getId());
		}
		
		ClientPersistant clientLog = clientService.getOneByField(ClientPersistant.class, "login", phone);
		if(clientId != null) {//Cas maj 
			clientB = clientService.findById(clientId);
			if(clientLog != null && !clientLog.getId().equals(clientB.getId())){
				MessageService.addGrowlMessage("", "Ce numéro est déjà utilisé.");	
				return;
			}
		} else if(clientLog != null) {
			MessageService.addGrowlMessage("", "Ce numéro est déjà utilisé.");	
			return;
		}
		
		// Envoi code SMS
//		if(StringUtil.isEmpty(httpUtil.getParameter("token_val"))) {
//			MessageService.addGrowlMessage("", "Code SMS obligatoire.");	
//			return;
//		} else if(!httpUtil.getParameter("token_val").equals(httpUtil.getUserAttribute("token_val").toString())) {
//			MessageService.addGrowlMessage("", "Code SMS invalide.");	
//			return;
//		}
		
		//------------------------
		EtablissementPersistant etablissementP = etsService.findById(EtablissementPersistant.class, ContextAppli.getEtablissementBean().getId());
		
		if(clientB == null) {
			clientB = new ClientBean();
			clientB.setNumero(clientService.generateNum());
		}
		clientB.setNom(nom);
		clientB.setTelephone(phone);
		clientB.setLogin(phone);
		clientB.setPassword(password);
		clientB.setMail(mail);
		clientB.setAdresse_rue(adresse);
		clientB.setPosition_lat(pos_lat);
		clientB.setPosition_lat(pos_lng);
		clientB.setIs_offre_cmd(is_offre);
		clientB.setIs_statut_cmd(is_notif);
		clientB.setType_client("PP");
		clientB.setOpc_etablissement(etablissementP);
		clientB.setOpc_societe(etablissementP.getOpc_societe());
		
		List<ClientContactPersistant> listContact = new ArrayList<>();
		clientB.setList_contact(listContact);
		
		boolean isCompteAvalider = BooleanUtil.isTrue(etablissementP.getIs_valid_cpt());
		if(!isCompteAvalider) {
			clientB.setIs_disable(true);
		}
		
		clientB = clientService.merge(clientB);
		
		if(userB == null) {
			userB = new UserBean();
			
			ProfilePersistant opc_profile = null;
			List<ProfilePersistant> listProfil = profileService.findAll(ProfilePersistant.class);
			for (ProfilePersistant profileB : listProfil) {
				if("CLIENT".equals(profileB.getCode())){
					opc_profile = profileB; 
					break;
				}
			}
			if(opc_profile == null) {
				ProfileBean profileB = new ProfileBean();
				profileB.setCode("CLIENT");
				profileB.setLibelle("CLIENT");
				profileB.setIs_caisse(false);
				profileB.setIs_backoffice(false);
				profileB.setOpc_etablissement(etablissementP);
				profileB.setOpc_societe(etablissementP.getOpc_societe());

				//
				opc_profile = profileService.merge(profileB);
			}
			userB.setOpc_profile(opc_profile);
		}
		userB.setLogin(phone);
		userB.setPassword(encryptionUtil.encrypt(password));
		userB.setOpc_etablissement(etablissementP);
		userB.setOpc_societe(etablissementP.getOpc_societe());
		userB.setOpc_client(clientB);
		
		// Si validation obligatoire, attente
		if(!isCompteAvalider) {
			userB.setIs_tovalidate(true);
			userB.setIs_desactive(true);
		}
		//
		userB = userService.merge(userB);
		
		// reload + 
		if(!isCompteAvalider) {
			httpUtil.setUserAttribute("MSG_CPT_CREATE", "<h3 class='cli-msg-success'>Votre compte est crée avec succès.<br>"
					+ "Dés que le compte est validé, vous pouvez vous connecter.</h3>");
		} else {
			MessageService.getGlobalMap().put(ProjectConstante.SESSION_GLOBAL_USER, userB);
		}
		httpUtil.writeResponse("REDIRECT:");
	}
	
	
	// Depuis app
	
	public void loadEts(ActionUtil httpUtil){
		Long etsIdParam = httpUtil.getLongParameter("etsId");
		
		if(etsIdParam == null) {// Depuis navigation
			String[] etsIds = StringUtil.getArrayFromStringDelim(ContextAppli.getUserBean().getClientEts_ids(), ";");
			List<EtablissementPersistant> listEts = new ArrayList<>();
			
			if(etsIds != null) {
				for (String etsId : etsIds) {
					if(!etsId.equals(ContextAppli.getEtablissementBean().getId().toString())) {// Ne pas ajouter elui enours
						EtablissementPersistant etsP = etsService.findById(EtablissementPersistant.class, Long.valueOf(etsId));
						listEts.add(etsP);
					}
				}
			}
			httpUtil.setRequestAttribute("listEts", listEts);
			
			httpUtil.setDynamicUrl("/domaine/caisse/"+httpUtil.getUserAttribute("ENV_MOBILE")+"/etablissement-list.jsp");
		} else {// Switcher ets
			EtablissementPersistant etsP = etsService.findById(EtablissementPersistant.class, etsIdParam);
			String contextPath = httpUtil.getRequest().getServletContext().getContextPath();
			httpUtil.writeResponse("REDIRECT:"+contextPath+"/mob-client?jtn="+etsP.getToken());
		}
	}
	public void loadApp(ActionUtil httpUtil){
		String appEnv = httpUtil.getParameter("app");
		String remoteHost = "";
		String localHost = "";
		//
		if(ReplicationGenerationEventListener._IS_CLOUD_SYNCHRO_INSTANCE || ContextAppli.IS_FULL_CLOUD()){
			remoteHost = StrimUtil.getGlobalConfigPropertie("instance.url");
			localHost = remoteHost;
		} else{
			String port = StrimUtil.getGlobalConfigPropertie("caisse.instance.port");
			port = (StringUtil.isEmpty(port) ? "8089" : port);
			String instance = StrimUtil.getGlobalConfigPropertie("caisse.instance.name");
			instance = (StringUtil.isEmpty(instance) ? "caisse-manager-origine" : instance);
			try {
				localHost = "http://"+InetAddress.getLocalHost().getHostAddress()+":"+port+"/"+instance;
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			remoteHost = StrimUtil.getGlobalConfigPropertie("acces.distant.url");
			//
			if(StringUtil.isEmpty(remoteHost)){
				remoteHost = localHost;
			}
		}
		
		Map<String, String> mapEnvsLocal = new HashMap<>();
		Map<String, String> mapEnvsDist = new HashMap<>();
		mapEnvsLocal.put("mob-caissier", "Caissier");
		mapEnvsLocal.put("mob-util", "Utilitaires");
		mapEnvsLocal.put("bo", "Back-office");
		
		mapEnvsLocal.put("mob-client", "Client");
		mapEnvsLocal.put("mob-livreur", "Livreur");
		mapEnvsLocal.put("mob-synthese", "Synthèse");
			
		if(appEnv == null) {// Depuis navigation
			String[] envs = StringUtil.getArrayFromStringDelim(ContextAppli.getUserBean().getAllEnvs(), ";");
			Map<String, String> mapEnvsSelected = new HashMap<>();
			//
			if(envs != null) {
				for (String env : envs) {
					if(httpUtil.getRequest().getContextPath().indexOf(env) == -1) {// Ne pas affiher celui en cours
						mapEnvsSelected.put(env, (mapEnvsLocal.get(env)!=null?mapEnvsLocal.get(env):mapEnvsDist.get(env)));
					}
				}
			}
			httpUtil.setRequestAttribute("mapEnv", mapEnvsSelected);
			
			httpUtil.setDynamicUrl("/domaine/caisse/app-list.jsp");
		} else {// Switcher app
			httpUtil.writeResponse("REDIRECT:"+(mapEnvsLocal.get(appEnv) == null ? localHost : remoteHost)
					+"/"+appEnv+"?jtn="+ContextAppli.getEtablissementBean().getToken());
		}
	}
	
	public void addAppClient(ActionUtil httpUtil){
		String qrStr = EncryptionUtil.decrypt(httpUtil.getParameter("qr").substring(4));
		String tp = httpUtil.getParameter("qr").substring(0, 3);

		if(!"APP".equals(tp)){
			MessageService.addGrowlMessage("", "Le code application n'est pas valide.");
			return;
		}
		
		String[] qr = StringUtil.getArrayFromStringDelim(qrStr, "|");
		if(qr == null || qr.length != 3) {
			MessageService.addGrowlMessage("", "Le code application n'est pas valide.");
			return;
		}
		
		String url = qr[0];
		String jtn = qr[1];
		String env = qr[2];
	
		UserBean userB = ContextAppli.getUserBean();
		UserPersistant userP = (userB!=null ? etsService.findById(UserPersistant.class, userB.getId()) : null);
		
		if(userP != null && (userP.getApps_types() == null || userP.getApps_types().indexOf(";"+env+";") == -1)) {
			String app_envs = StringUtil.getValueOrEmpty(userP.getApps_types()) + ";"+env+";";
			userP.setApps_types(app_envs);
			etsService.mergeEntity(userP);
			userB.setApps_types(app_envs);
			httpUtil.writeResponse("MSG_CUSTOM:L'application est ajoutée avec succès.");
			return;
		}
		if(userB == null) {
			httpUtil.writeResponse("REDIRECT:"+url+"/"+env+"?jtn="+jtn);
		}
	}
	
	/**
	 * @param httpUtil
	 */
	public void addEtsClient(ActionUtil httpUtil){
		boolean isSubmit = (httpUtil.getParameter("isSub") != null);
		String tp = null;
		String qrStr = null;
				
		if(isSubmit) {
			if(StringUtil.isEmpty(httpUtil.getParameter("code_ets_mob"))) {
				MessageService.addGrowlMessage("", "Le code établissement n'est pas valide.");
				return;
			}
			tp = "ETS";
			qrStr = StrimUtil.getGlobalConfigPropertie("instance.url")+"|"+httpUtil.getParameter("code_ets_mob")+"|mob-client";
		} else {
			String qrParam = httpUtil.getParameter("qr");
			if(StringUtil.isEmpty(qrParam) || qrParam.length() < 10) {
				MessageService.addGrowlMessage("", "Le code établissement n'est pas valide.");
				return;
			}
			qrStr = EncryptionUtil.decrypt(qrParam.substring(4));
			tp = qrParam.substring(0, 3);
		}
			
		if(!"ETS".equals(tp)){
			MessageService.addGrowlMessage("", "Le code application n'est pas valide.");
			return;
		}
		
		String[] qr = StringUtil.getArrayFromStringDelim(qrStr, "|");
		if(qr == null || qr.length != 3) {
			MessageService.addGrowlMessage("", "Le code établissement n'est pas valide.");
			return;
		}
		
		String url = qr[0];
		String jtn = qr[1];
		String env = qr[2];

		EtablissementPersistant etsP = etsService.getOneByField(EtablissementPersistant.class, "token", jtn);
		if(etsP == null) {
			MessageService.addGrowlMessage("", "Le code établissement n'est pas valide.");
			return;
		}
		
		UserBean userB = ContextAppli.getUserBean();
		UserPersistant userP = (userB!=null ? etsService.findById(UserPersistant.class, userB.getId()) : null);
		
		if(userP != null && (userP.getClientEts_ids() == null || userP.getClientEts_ids().indexOf(";"+etsP.getId()+";") == -1)) {
			String ets_ids = StringUtil.getValueOrEmpty(userP.getClientEts_ids()) + ";"+etsP.getId()+";";
			userP.setClientEts_ids(ets_ids);
			etsService.mergeEntity(userP);
			userB.setClientEts_ids(ets_ids);
			
			httpUtil.writeResponse("MSG_CUSTOM:L'établissement est ajouté avec succès.");
			return;
		}
		
		if(userB == null) {
			httpUtil.writeResponse("REDIRECT:"+url+"/"+env+"?jtn="+jtn);
		}
	}
	
	public void removeEtsApp(ActionUtil httpUtil){
		UserBean userB = ContextAppli.getUserBean();
		Long etsId = httpUtil.getLongParameter("etsId");
		String app = httpUtil.getParameter("app");
		
		UserPersistant userP = etsService.findById(UserPersistant.class, userB.getId());
		
		if(etsId != null) {
			if(userP.getClientEts_ids() != null) {
				userP.setClientEts_ids(userP.getClientEts_ids().replaceAll(";"+etsId+";", ""));
				etsService.mergeEntity(userP);
			}
			loadEts(httpUtil);
		} else {
			if(userP.getApps_types() != null) {
				userP.setApps_types(userP.getApps_types().replaceAll(";"+app+";", ""));
				etsService.mergeEntity(userP);
			}
			loadApp(httpUtil);
		}
	}

}
