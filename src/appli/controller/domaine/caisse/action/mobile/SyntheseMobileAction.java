package appli.controller.domaine.caisse.action.mobile;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import appli.controller.domaine.administration.action.LoginAction;
import appli.controller.domaine.administration.bean.JourneeBean;
import appli.controller.domaine.administration.bean.UserBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.administration.service.IUserService;
import appli.model.domaine.caisse.service.IJourneeService;
import appli.model.domaine.stock.persistant.FamilleCuisinePersistant;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.stock.service.IFamilleService;
import appli.model.domaine.stock.service.impl.RepartitionBean;
import appli.model.domaine.vente.persistant.CaisseJourneePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant;
import appli.model.domaine.vente.persistant.JourneePersistant;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.Context;
import framework.controller.ContextGloabalAppli;
import framework.controller.annotation.WorkController;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.EncryptionEtsUtil;
import framework.model.common.util.DateUtil.TIME_ENUM;
import framework.model.common.util.StringUtil;

@WorkController(nameSpace="caisse", bean=JourneeBean.class, jspRootPath="/domaine/caisse/mobile-synthese")
public class SyntheseMobileAction extends ActionBase {
	@Inject
	private IJourneeService journeeService;
	@Inject
	private IFamilleService familleService;
	@Inject
	private IUserService userService;
	
	public void work_init(ActionUtil httpUtil){
		Long journeeId = (Long)httpUtil.getMenuAttribute("journeeId");
		if(journeeId == null) {
			JourneePersistant lastJournee = journeeService.getLastJournee();
			if(lastJournee != null) {
				httpUtil.setMenuAttribute("journeeId", lastJournee.getId());	
			}
		}	
	}
	public void load_synthese(ActionUtil httpUtil){
		Long journeeId = (Long)httpUtil.getMenuAttribute("journeeId");
		
		if(journeeId == null) {
			httpUtil.setDynamicUrl("/domaine/caisse/mobile-synthese/synthese_journee.jsp");
			return;
		}
		
		JourneePersistant journeeV = journeeService.getJourneeView(journeeId);
		
		List<CaisseJourneePersistant> listDataShift = journeeService.getJourneeCaisseView(journeeId);
		httpUtil.setRequestAttribute("listDataShift", listDataShift);
		
		httpUtil.setRequestAttribute("journeeView", journeeV);
		
		JourneePersistant journeeDB = journeeService.getJourneeView(journeeId);
		boolean isDoubleCloture = BooleanUtil.isTrue(ContextAppli.getUserBean().getIs_admin()) 
										|| (StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("DOUBLE_CLOTURE")) && Context.isOperationAvailable("DBLCLO"));
		httpUtil.setRequestAttribute("isDoubleCloture", isDoubleCloture);
		
		httpUtil.setRequestAttribute("journeeView", journeeDB);
		httpUtil.setMenuAttribute("CURR_JRN_ID", journeeId);
		
		Map<String, Map<String, RepartitionBean>> listDataEmploye = journeeService.getChiffresServeurLivreurCaissier(null, null, journeeId);
		httpUtil.setRequestAttribute("mapDataEmploye", listDataEmploye);
		
		
		httpUtil.setDynamicUrl("/domaine/caisse/mobile-synthese/synthese_journee.jsp");
	}

	/**
	 * @param httpUtil
	 */
	public void load_situation(ActionUtil httpUtil){
		Map<String, CaisseMouvementArticlePersistant> mapData = null;
		
		List<FamillePersistant> listFamille = (List<FamillePersistant>) familleService.getListeFamille("CU", true, false);
		// Ajout menu
		FamillePersistant fpf = new FamilleCuisinePersistant();
		fpf.setId(Long.valueOf(-999));
		fpf.setB_left(1);
		fpf.setB_right(2);
		fpf.setCode("MNU");
		fpf.setLibelle("Menus");
		fpf.setLevel(2);
		listFamille.add(0, fpf);
		//
		httpUtil.setRequestAttribute("list_famille", listFamille);
		
		Long familleId = null;
		if(StringUtil.isEmpty(httpUtil.getParameter("curr_famille"))) {
		
		} else {
			familleId = httpUtil.getLongParameter("curr_famille");
		}
		httpUtil.setRequestAttribute("curr_famille", familleId);
		httpUtil.setRequestAttribute("familleId", familleId);
		
		Long journeeId = (Long)httpUtil.getMenuAttribute("journeeId");
		JourneePersistant journeeDebut = journeeService.findById(JourneePersistant.class, journeeId);
    	JourneePersistant journeeFin = journeeService.findById(JourneePersistant.class, journeeId);
		
		mapData = journeeService.getRepartitionVenteArticle(journeeDebut, journeeFin, familleId, false);
		httpUtil.setRequestAttribute("dataRepartion", mapData);
		
		httpUtil.setDynamicUrl("/domaine/caisse/mobile-synthese/situation_finance.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void load_histo_journee(ActionUtil httpUtil){
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_journee");
		Map<String, Object> formCriterion = cplxTable.getFormBean().getFormCriterion();
		boolean isFilterAct = StringUtil.isTrue(httpUtil.getRequest().getParameter("is_filter_act"));
		if(httpUtil.getParameter("tp") != null){
			httpUtil.setMenuAttribute("tpj", httpUtil.getParameter("tp"));
		}
		String tp = (String)httpUtil.getMenuAttribute("tpj");
		tp = StringUtil.isEmpty(tp) ? "std" : tp;
		//
		//----------------------------- Date -------------------------
		Date dateDebut = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateDebut"));
		Date dateFin = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateFin"));
		boolean isDbFin = true;
		if(dateDebut == null) {
			JourneePersistant lastJrn = journeeService.getLastJournee();
			Date dateLast = (lastJrn == null ? new Date() : lastJrn.getDate_journee());
			
			dateDebut = (httpUtil.getMenuAttribute("dateDebut")==null ? dateLast : (Date)httpUtil.getMenuAttribute("dateDebut"));
			dateFin = (httpUtil.getMenuAttribute("dateFin")==null ? dateLast : (Date)httpUtil.getMenuAttribute("dateFin"));
		} else if(httpUtil.getRequest().getParameter("prev") != null) {
			dateDebut = DateUtil.addSubstractDate(dateDebut, TIME_ENUM.MONTH, -1);
			dateFin = DateUtil.addSubstractDate(dateFin, TIME_ENUM.MONTH, -1);
		} else if(httpUtil.getRequest().getParameter("next") != null) {
			dateDebut = DateUtil.addSubstractDate(dateDebut, TIME_ENUM.MONTH, 1);
			dateFin = DateUtil.addSubstractDate(dateFin, TIME_ENUM.MONTH, 1);
		} else{
			isDbFin = false;
		}
		if(isDbFin){
			Calendar cal = DateUtil.getCalendar(dateDebut);
			String dateString = (cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR);
			dateDebut = DateUtil.stringToDate("01/"+dateString);
			dateFin = DateUtil.stringToDate(DateUtil.getMaxMonthDate(dateDebut)+"/"+dateString);
		}
		// Postionner l'heure
		dateDebut = DateUtil.getStartOfDay(dateDebut);
		dateFin = DateUtil.getEndOfDay(dateFin);
		
		httpUtil.setRequestAttribute("dateDebut", dateDebut);
		httpUtil.setRequestAttribute("dateFin", dateFin);
		httpUtil.setMenuAttribute("dateDebut", dateDebut);
		httpUtil.setMenuAttribute("dateFin", dateFin);
		
		if(!isFilterAct){
			formCriterion.put("dateDebut", dateDebut);
			formCriterion.put("dateFin", dateFin);
		} else{
			formCriterion.remove("dateDebut");
			formCriterion.remove("dateFin");
		}
		//-----------------------------------------------------------		

		List<JourneePersistant> listData = (List<JourneePersistant>) journeeService.findByCriteriaByQueryId(cplxTable, "journee_find");
	   	
		if(listData.size() >0) {
			journeeService.setDataJourneeFromView(listData.get(0));
		}
		httpUtil.setRequestAttribute("list_journee", listData);
	   	
	   	JourneePersistant jp = new JourneePersistant();
	   	List<JourneePersistant> listDataAll = (List<JourneePersistant>) journeeService.findByCriteriaByQueryId(cplxTable, "journee_find", false);
	   	for (JourneePersistant jvp : listDataAll) {
	   		if("O".equals(jvp.getStatut_journee())){
	   			journeeService.setDataJourneeFromView(jvp);
	   		}
	   		jp.setMtt_reduction(BigDecimalUtil.add(jp.getMtt_reduction(), jvp.getMtt_reduction()));
	   		jp.setMtt_art_offert(BigDecimalUtil.add(jp.getMtt_art_offert(), jvp.getMtt_art_offert()));
	   		jp.setMtt_art_reduction(BigDecimalUtil.add(jp.getMtt_art_reduction(), jvp.getMtt_art_reduction()));
	   		jp.setMtt_livraison(BigDecimalUtil.add(jp.getMtt_livraison(), jvp.getMttLivraisonGlobal()));
	   		//
	   		if("std".equals(tp)){
		   		jp.setNbr_vente((jp.getNbr_vente()!=null?jp.getNbr_vente():0)+ (jvp.getNbr_vente()!=null?jvp.getNbr_vente():0));
		   		jp.setMtt_ouverture(BigDecimalUtil.add(jp.getMtt_ouverture(), jvp.getMtt_ouverture()));
		   		
		   		// CALCULE
		   		BigDecimal mttTotalNet = jvp.getMtt_total_net();//, jvp.getMtt_livraison());
		   		jp.setMtt_total_net(BigDecimalUtil.add(jp.getMtt_total_net(), mttTotalNet));
	   			
		   		// CLOTURE
		   		if("C".equals(jvp.getStatut_journee())){
		   			BigDecimal netCloture = BigDecimalUtil.substract(jvp.getMtt_cloture_caissier(), jvp.getMtt_ouverture());
		   			jp.setMtt_cloture_caissier(BigDecimalUtil.add(jp.getMtt_cloture_caissier(), netCloture));
		   			
		   			// ECART
		   			BigDecimal mttEcar = jvp.getEcartNet();
			   		jp.setMtt_total(BigDecimalUtil.add(jp.getMtt_total(), mttEcar));
		   		}
		   		// MARGE
		   		BigDecimal mttMarge = jp.getMtt_total_achat()!=null ? BigDecimalUtil.substract(jp.getMtt_total_net(),jp.getMttLivraisonPartLivreur(), jp.getMtt_total_achat())
							: BigDecimalUtil.substract(jvp.getMtt_total_net(), jvp.getMttLivraisonPartLivreur(), jvp.getMtt_total_achat());
		   		jp.setMtt_cloture_caissier_cb(BigDecimalUtil.add(jp.getMtt_cloture_caissier_cb(), mttMarge));
		   		
		   		jp.setMtt_donne_point(BigDecimalUtil.add(jp.getMtt_donne_point(), jvp.getMtt_donne_point()));
	   			jp.setMtt_portefeuille(BigDecimalUtil.add(jp.getMtt_portefeuille(), jvp.getMtt_portefeuille()));
	   			
	   			jp.setMtt_annule(BigDecimalUtil.add(jp.getMtt_annule(), jvp.getMtt_annule()));
	   			jp.setMtt_annule_ligne(BigDecimalUtil.add(jp.getMtt_annule_ligne(), jvp.getMtt_annule_ligne()));
	   		} else{
	   			jp.setMtt_cheque(BigDecimalUtil.add(jp.getMtt_cheque(), jvp.getMtt_cheque()));
	   			jp.setMtt_espece(BigDecimalUtil.add(jp.getMtt_espece(), jvp.getMtt_espece()));
	   			jp.setMtt_cb(BigDecimalUtil.add(jp.getMtt_cb(), jvp.getMtt_cb()));
	   			jp.setMtt_dej(BigDecimalUtil.add(jp.getMtt_dej(), jvp.getMtt_dej()));
	   			jp.setMtt_donne_point(BigDecimalUtil.add(jp.getMtt_donne_point(), jvp.getMtt_donne_point()));
	   			jp.setMtt_portefeuille(BigDecimalUtil.add(jp.getMtt_portefeuille(), jvp.getMtt_portefeuille()));
	   			
	   			jp.setMtt_annule(BigDecimalUtil.add(jp.getMtt_annule(), jvp.getMtt_annule()));
	   			jp.setMtt_annule_ligne(BigDecimalUtil.add(jp.getMtt_annule_ligne(), jvp.getMtt_annule_ligne()));
	   		}
		} 
	   	httpUtil.setRequestAttribute("journee_total", jp);
	   	
	   	
	   	
		httpUtil.setDynamicUrl("/domaine/caisse/mobile-synthese/histo_journee.jsp");
	}
	
	public void load_alerte(ActionUtil httpUtil){
		httpUtil.setDynamicUrl("/domaine/caisse/mobile-synthese/alerte_dash.jsp");
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
		
		login = login.replaceAll("-", "");
		UserBean userBean = userService.getUserByLoginAndPw(login, encryptionUtil.encrypt(password)); 
		if(userBean == null){
			MessageService.addGrowlMessage("", "Le login ou le mot de passse est erroné");	
			return;
		}
		if(userBean.isEnvGranted("mob-synthese", userBean)){
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
			if(!LoginAction.checkDoitProfileEnv(userBean.getAllEnvs(), ContextAppli.APPLI_ENV.synthese.toString())) {
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
