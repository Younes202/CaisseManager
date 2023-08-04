package appli.controller.domaine.caisse.action;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import appli.controller.domaine.administration.bean.EtatFinanceBean;
import appli.controller.domaine.caisse.ContextAppliCaisse;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.APPLI_ENV;
import appli.model.domaine.administration.service.IParametrageService;
import appli.model.domaine.administration.service.IUserService;
import appli.model.domaine.caisse.service.IJourneeService;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.stock.service.IFamilleService;
import appli.model.domaine.util_srv.raz.RazService;
import appli.model.domaine.vente.persistant.JourneePersistant;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.service.MessageService;
import framework.model.common.util.DateUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.printGen.PrintPosBean;

@WorkController(nameSpace = "caisse", bean = EtatFinanceBean.class, jspRootPath = "/domaine/caisse/")
public class RazPrintAction extends ActionBase {
	@Inject
	private IJourneeService journeeService;
	@Inject
	private IParametrageService parametrageService;
	@Inject
	private IUserService userService;
	@Inject
	private IFamilleService familleService;
	
	public void work_init(ActionUtil httpUtil) { 

	}
	
	/**
	 * Depuis la caisse
	 * @param httpUtil
	 */
	public void init_raz_caisse(ActionUtil httpUtil){
		httpUtil.setDynamicUrl("/domaine/caisse/normal/situation/raz_init.jsp");
	}
	
	/**
	 * RAZ backoffice
	 * @param httpUtil
	 */
	public void init_raz_bo(ActionUtil httpUtil){
		journeeService = (IJourneeService) ServiceUtil.getBusinessBean(IJourneeService.class);
		// Min et Max dates disponibles
		Date[] minMaxDate = journeeService.getMinMaxDate();
		httpUtil.setRequestAttribute("dateDebut", minMaxDate[1]);//DateUtil.addSubstractDate(minMaxDate[1], TIME_ENUM.DAY, -30));
		httpUtil.setRequestAttribute("dateFin", minMaxDate[1]);
		
		httpUtil.setRequestAttribute("listUser", userService.findAllUser(true));
		
		httpUtil.setDynamicUrl("/domaine/compta/init_raz.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void config_boisson_raz(ActionUtil httpUtil) {
		List<FamillePersistant> listFamille = familleService.getListeFamille("CU", true, true);
		httpUtil.setRequestAttribute("listFamille", listFamille);
		
		if(httpUtil.getParameter("boisson_froide") != null){
			EtablissementPersistant restauP = familleService.mergeConfRaz(httpUtil.getLongParameter("boisson_froide"), httpUtil.getLongParameter("boisson_chaude"));
			// Maj session
			ContextAppli.getEtablissementBean().setFam_boisson_froide(restauP.getFam_boisson_froide());
			ContextAppli.getEtablissementBean().setFam_boisson_chaude(restauP.getFam_boisson_chaude());
			
			MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "", "La configuration des la RAZ est mise à jour.");
			
			init_raz_bo(httpUtil);
		} else{
			httpUtil.setDynamicUrl("/domaine/stock/famille_config_raz.jsp");
		}
	}
	
	/**
	 * Configuration imprimante
	 * @param httpUtil
	 */
	public void init_print_conf(ActionUtil httpUtil){
        if(httpUtil.getParameter("isupd") != null){
        	Map<String, Object> params = httpUtil.getValuesByStartName("param_");
    		parametrageService.updateParams(params);
    		
    		httpUtil.writeResponse("MSG_CUSTOM:Imprimante mise à jour");	
    		return;
        } else{
        	// Liste des imprimantes
            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
            if(printServices != null) {
    	        String[][] listImprimantes = new String[printServices.length][2];
    	    	//
    	        int idx = 0;
                for (PrintService printer : printServices){
                	listImprimantes[idx][0] = printer.getName();
                	listImprimantes[idx][1] = printer.getName();
                	idx++;
            	}
                httpUtil.setRequestAttribute("list_imprimante", listImprimantes);
            }
            
        	httpUtil.setRequestAttribute("parametre", parametrageService.getParameterByCode("PRINT_RAZ"));
        	httpUtil.setDynamicUrl("/domaine/caisse/print/raz_print_conf.jsp");	
        }
	}
	
//	public void imprimer_raz_bo(ActionUtil httpUtil) {
//		boolean reponse = false;
//		String mnuSource = httpUtil.getParameter("mnu");
//		String format = httpUtil.getParameter("format");
//		String mode = httpUtil.getParameter("mode");//J=journee, M=mensuel, L=Libre
//		
//		BigDecimal tauxOptimisation = BigDecimalUtil.get(ContextGloabalAppli.getGlobalConfig(ContextAppli.PARAM_APPLI_ENUM.TAUX_OPTIM.toString()));
//			reponse = MessageService.addDialogConfirmMessage("PRINT_RAZ", "caisse.razPrint.imprimer_raz_bo", 
//					"Impression RAZ journalière", "Vous êtes sur le point de lancer l'impression de la RAZ.<br>"
//							+ (BooleanUtil.isTrue(ContextAppli.getAbonementBean().isOptPlusOptimisation())?
//							 "<span style='color:orange'>Le taux d'optimisation <b>** " + tauxOptimisation + "% **</b> sera appliqué.</span><br>"
//							: "")
//							+ "<br>Vous-les vous continuer ?");
//		
//		if(reponse) {
//			Date dateDebut = null;
//			Date dateFin = null;
//			Long journeeId = null;
//			//
//			if(mode.equals("J")){
//				journeeId = httpUtil.getLongParameter("journeeId");
//			} else if(mode.equals("M")){
//				dateDebut = DateUtil.stringToDate(httpUtil.getParameter("dateDebut"));
//				dateFin = DateUtil.stringToDate(httpUtil.getParameter("dateFin"));
//			} else if(mode.equals("L")){
//				Calendar cal = DateUtil.getCalendar(dateDebut);
//				dateDebut = DateUtil.stringToDate("01/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR));
//				dateFin = DateUtil.stringToDate(DateUtil.getMaxMonthDate(dateDebut)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR));
//			}
//			
//			String rapport = "";
//			if(journeeId != null){
//				rapport = (String)new RazService().imprimerRaz(mnuSource, format, journeeId, null, null)[1];
//			} else{
//				rapport = (String)new RazService().imprimerRaz(mnuSource, format, null, dateDebut, dateFin)[1];
//			}
//			MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "Rapport d'impression", rapport);
//		}
//		httpUtil.setRequestAttribute("typePrint", mode);
//		
//			httpUtil.setRequestAttribute("dateDebut", httpUtil.getParameter("dateDebut"));
//			init_raz_bo(httpUtil);
//	}
	
	/**
	 * @param httpUtil
	 */
	public void imprimer_raz_caisse(ActionUtil httpUtil) {
		String mnuSource = httpUtil.getParameter("mnu");
		String format = httpUtil.getParameter("format");
		JourneePersistant journeeBean = ContextAppliCaisse.getJourneeBean();
		Long loginUserId = httpUtil.getLongParameter("user");
		String env = (String) httpUtil.getUserAttribute("CURRENT_ENV");
		boolean isOptimCheck = StringUtil.isTrue(httpUtil.getParameter("optimOptPrint"));
		
		if("RE".equals(mnuSource) && APPLI_ENV.cais.toString().equals(env) && loginUserId == null){
			loginUserId = ContextAppli.getUserBean().getId();
			if(loginUserId == null){
				MessageService.addGrowlMessage("", "Veuillez sélectionner un employé.");
				return;
			}
		}
		//
		Map<String, Object> retour = null; 
		
		// Depuis BO
		if(journeeBean == null){
			boolean marquerMvmPrint = httpUtil.getParameter("marquerMvmPrint") != null;
			Date[] minMaxDate = journeeService.getMinMaxDate();
			Date dateDebut =  null, dateFin = null;
			if(StringUtil.isNotEmpty(httpUtil.getParameter("dateDebut"))){
				dateDebut = DateUtil.stringToDate(httpUtil.getParameter("dateDebut"));
			} else{
				dateDebut = minMaxDate[1];//DateUtil.addSubstractDate(minMaxDate[1], TIME_ENUM.DAY, -30);
			}
			if(StringUtil.isNotEmpty(httpUtil.getParameter("dateFin"))){
				dateFin = DateUtil.stringToDate(httpUtil.getParameter("dateFin"));
			} else{
				dateFin = minMaxDate[1];
			}
			
			if(dateFin.before(dateDebut)){
				MessageService.addGrowlMessage("", "La date de fin doit être postérieure ou égale à la date de début.");
				return;
			}
			
			JourneePersistant journeeDebut = journeeService.getJourneeOrNextByDate(dateDebut);
	    	JourneePersistant journeeFin = journeeService.getJourneeOrPreviousByDate(dateFin);
	    	
	    	if(journeeDebut == null) {
	    		MessageService.addGrowlMessage("", "Aucune journée ne correspond à ces dates.");
	    		return;
	    	}
			
	    	String titre = dateDebut.compareTo(dateFin)==0 ? DateUtil.dateToString(dateDebut) : (DateUtil.dateToString(dateDebut)+" au "+DateUtil.dateToString(dateFin));
	    	
			retour = new RazService().imprimerRaz(mnuSource, format, journeeDebut, journeeFin, titre,
					loginUserId, marquerMvmPrint, true, isOptimCheck);
			
		} else{// Depuis la caisse
			loginUserId = ContextAppli.getUserBean().getId();
			Long journeeId = journeeBean.getId();
			JourneePersistant journeeDebut = journeeService.findById(JourneePersistant.class, journeeId);
	    	JourneePersistant journeeFin = journeeService.findById(JourneePersistant.class, journeeId);
	    	
	    	String titre = DateUtil.dateToString(journeeDebut.getDate_journee());
	    	
			retour = new RazService().imprimerRaz(mnuSource, format, journeeDebut, journeeFin, titre, loginUserId, false, true, isOptimCheck);
		}
		
		if(retour == null){
			MessageService.addGrowlMessage("", "Aucun mouvement n'a été trouvé");
			return;
		}
		
		Object obj1 = retour.get("obj1");
		Object obj2 = retour.get("obj2");
		
		if(format.equals("PDF")){
			if(obj1 == null){
				MessageService.addGrowlMessage("", "Aucun mouvement n'a été trouvé");
				return;
			}
			httpUtil.doDownload((File)obj1, true);
		} else if(format.equals("HTML")){
			if(StringUtil.isNotEmpty(obj2)){
				MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "Rapport d'impression", ""+obj2);
			}
			
			if(obj1 == null){
				MessageService.addGrowlMessage("", "Aucun mouvement n'a été trouvé");
				return;
			}
			httpUtil.setRequestAttribute("html_data", obj1);
			httpUtil.setRequestAttribute("rapport", obj2);
			
			httpUtil.setDynamicUrl("/domaine/caisse/etat/etatFinanceRaz.jsp");
		} else{
			List<PrintPosBean> listData = (List<PrintPosBean>) retour.get("print");
			if(listData != null){
				boolean isAsync = printData(httpUtil, listData);
				if(isAsync) {
					forwardToPriterJsp(httpUtil);
					return;
				}
			}
			
			httpUtil.writeResponse("MSG_CUSTOM:Impression lancée<br>"+StringUtil.getValueOrEmpty(obj2));	
		}
	}
}
