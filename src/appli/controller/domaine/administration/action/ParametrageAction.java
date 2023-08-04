package appli.controller.domaine.administration.action;

import java.io.File;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.hibernate.criterion.Order;

import appli.controller.domaine.administration.ParametrageRightsConstantes;
import appli.controller.domaine.administration.ParametrageRightsConstantes.TYPE_PARAM;
import appli.controller.domaine.administration.bean.ValTypeEnumBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.administration.persistant.EtatFinancePersistant;
import appli.model.domaine.administration.persistant.LieuxPersistant;
import appli.model.domaine.administration.persistant.ParametragePersistant;
import appli.model.domaine.administration.service.ICompteBancaireService;
import appli.model.domaine.administration.service.IEtatFinanceService;
import appli.model.domaine.administration.service.IParametrageService;
import appli.model.domaine.util_srv.job.JobSaveLocalDbCron;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.ContextGloabalAppli;
import framework.controller.ControllerUtil;
import framework.controller.annotation.WorkController;
import framework.model.beanContext.EtablissementOuverturePersistant;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.beanContext.SocietePersistant;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.constante.ProjectConstante.SESSION_SCOPE_ENUM;
import framework.model.common.service.MessageService;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.ControllerBeanUtil;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StringUtil;

@WorkController(nameSpace="admin", bean=ValTypeEnumBean.class, jspRootPath="/domaine/caisse/back-office")
public class ParametrageAction extends ActionBase {
	@Inject
	private IParametrageService paramService;
	@Inject
	private ICompteBancaireService banqueService;
	@Inject
	private IEtatFinanceService etatFinanceService;
	
	/**
	 * @param httpUtil
	 */
	public void work_init(ActionUtil httpUtil) {
		httpUtil.setRequestAttribute("list_imprimante", ParametrageRightsConstantes.getListPrinters());
        
		if(httpUtil.getParameter("tp") != null){
			httpUtil.setMenuAttribute("tpMnu", httpUtil.getParameter("tp"));
		}
		if(httpUtil.getMenuAttribute("tpMnu") == null){
			httpUtil.setMenuAttribute("tpMnu", httpUtil.getParameter("gen"));
		}
		
		if(httpUtil.getParameter("fmnu") != null) {
			httpUtil.setMenuAttribute("isCuis", true);
		}
	}
	
	@Override
	public void work_update(ActionUtil httpUtil) {
		String mnu = (String) httpUtil.getMenuAttribute("tpMnu");
		Map<String, Object> params = httpUtil.getValuesByStartName("param_");
		String msgPublicite = null, titrePublicite = null;
		
		String typeImpression = ContextGloabalAppli.getGlobalConfig("CAISSE_PRINT");
		
		//
		if(mnu.equals("cuis")){
			String statut = StringUtil.getStringDelimFromStringArray(httpUtil.getRequest().getParameterValues("param_ECRAN_STATUT"), ";");
			params.put("ECRAN_STATUT", statut);
			paramService.updateParams(params, null);
		} else if(mnu.equals("cais")){
			// Images
			managePieceJointe(httpUtil, ContextAppli.getEtablissementBean().getId(), "photoZ");// Image publicité
			paramService.updateParams(params, null);
		} else if(mnu.equals("tick")){
			msgPublicite = httpUtil.getParameter("restaurant.msg_publicite");
			titrePublicite = httpUtil.getParameter("restaurant.titre_publicite");
			managePieceJointe(httpUtil, ContextAppli.getEtablissementBean().getId(), "paramTICK");// Image publicité
			paramService.updateParams(params, titrePublicite, msgPublicite, null);
		} else if(mnu.equals("div")){
			// Retirer cette info si pas d"abonnement
			if(!BooleanUtil.isTrue(ContextAppli.getAbonementBean().isOptPlusOptimisation())){
				params.put("TAUX_OPTIM", "");
				params.put("SEUIL_OPTIM", "");
			}
			paramService.updateParams(params, null);
		} else if(mnu.equals("cmdl")){
			Map<String, Object> paramsE = (Map)httpUtil.getRequestAttribute(ProjectConstante.WORK_PARAMS);
			EtablissementPersistant etsP = (EtablissementPersistant) ControllerBeanUtil.mapToBean(EtablissementPersistant.class, paramsE);
			
			List<EtablissementOuverturePersistant> listOuverture = (List<EtablissementOuverturePersistant>) httpUtil.buildListBeanFromMap("jour", EtablissementOuverturePersistant.class, 
					"eaiid", "jour_ouverture", "heure_debut_matin", "heure_fin_matin", "heure_debut_midi", "heure_fin_midi");
			etsP.setList_ouverture(listOuverture);
			etsP.setId(ContextAppli.getEtablissementBean().getId());
			etsP.setOpc_societe(paramService.findById(SocietePersistant.class, ContextAppli.getEtablissementBean().getOpc_societe().getId()));
			
			paramService.mergeEtablissement(etsP, true);
			paramService.updateParams(params, null);
		} else{
			paramService.updateParams(params, null);
		}
		
		((Map)MessageService.getGlobalMap().get("GLOBAL_CONFIG")).put("CAISSE_PRINT", typeImpression);
		
		work_edit(httpUtil);
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "", "La mise à jour est effectuée.");
	}
	
	/**
	 * @param httpUtil
	 */
	@Override
	public void work_init_update(ActionUtil httpUtil){
		setRedirection(httpUtil);
	}
	
	@Override
	public void work_edit(ActionUtil httpUtil){
		setRedirection(httpUtil);
	}
	
	/**
	 * @param httpUtil
	 */
	public void purger_base(ActionUtil httpUtil) {
		Long etatId = Long.valueOf(EncryptionUtil.decrypt(httpUtil.getParameter("etat")));
		Map<String, ?> mapPurge = httpUtil.getValuesByStartName("purge_");
		
		paramService.purgerDonneesBase(etatId, mapPurge);
		//
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "Purge des données", "La purge est effectuée avec succès.");
		setRedirection(httpUtil);
	}
	
	/**
	 * @param httpUtil
	 */
	public void dumper_base(ActionUtil httpUtil) {
		String mode = httpUtil.getParameter("mode");
		String path = httpUtil.getParameter("cheminBckDb");
		String startDt1 = httpUtil.getParameter("startDt1");
		String startDt2 = httpUtil.getParameter("startDt2");
		
		if(StringUtil.isEmpty(path)) {
			MessageService.addGrowlMessage("", "Le chemin est obligatoire.");
			return;
		} else {
			try {
				if(!new File(path).exists()) {
					new File(path).mkdirs();
				}
			} catch(Exception e) {
				
			}
			
			if(!new File(path).isDirectory()) {
				MessageService.addBannerMessage("Ce chemin n'exsite pas dans le serveur.");
				return;
			}
		}
		
		if("A".equals(mode) && StringUtil.isEmpty(startDt1)) {
			MessageService.addGrowlMessage("", "L'heure de sauvegarde 1 est obligatoire.");
			return;
		}
		
		paramService.majInfosSaveDb(path, startDt1, startDt2);
		//
		if("A".equals(mode)) {
			JobSaveLocalDbCron.getInstance().init_job_save_db();
			MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "Sauvegarde de la base des données", "Le paramètrage de la sauvegarde est mise à jour.");
		} else {
			paramService.dumpBase(path);
			MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "Sauvegarde de la base des données", "La sauvegarde est effectuée avec succès.");
		}
		//
		setRedirection(httpUtil);
	}

	/**
	 * @param httpUtil
	 */
	private void setRedirection(ActionUtil httpUtil){
		String tp = httpUtil.getParameter("tp");
		tp = (tp == null) ? (String)httpUtil.getMenuAttribute("tpMnu") : tp;
		tp = (tp == null) ? "gen" : tp;
		
		switch(tp){
			case "cais" : {
				
				String[][] listCompoBarre = {
						{"PR2;CB5;PD5;SU1", "Prefix(2)|Code barre(5)|Poids(5)|Suffix(1)"},
						{"PR2;PD5;CB5;SU1", "Prefix(2)|Poids(5)|Code barre(5)|Suffix(1)"},
						{"PR2;CB6;PD5", "Prefix(2)|Code barre(6)|Poids(5)"},
						{"PR2;PD5;CB5", "Prefix(2)|Poids(6)|Code barre(5)"}
					};
				httpUtil.setRequestAttribute("listCompoBarre", listCompoBarre);
				
				ParametrageRightsConstantes.loadParamsGlobalByGroup(httpUtil, TYPE_PARAM.CAISSE_GLOB);
				httpUtil.setDynamicUrl("/domaine/caisse/back-office/parametrage/parametrage_caisse.jsp");
			}; break;
			case "div" : {
				httpUtil.setRequestAttribute("listeBanque", banqueService.findAll());
				ParametrageRightsConstantes.loadParamsGlobalByGroup(httpUtil, TYPE_PARAM.DIVERS_GLOB);
				httpUtil.setDynamicUrl("/domaine/caisse/back-office/parametrage/parametrage_divers.jsp");
			}; break;
			case "ihm" : {
				ParametrageRightsConstantes.loadParamsGlobalByGroup(httpUtil, TYPE_PARAM.IHM_GLOB);
				httpUtil.setDynamicUrl("/domaine/caisse/back-office/parametrage/parametrage_affichage_edit.jsp");
			}; break;
			case "cuis" : {
				httpUtil.setRequestAttribute("listModeTravail", new String[][]{{"PO", "Imprimantes seulement"}, {"EO", "Ecran seulement"}});
				ParametragePersistant paramStatutEcran = paramService.getParameterByCode("ECRAN_STATUT");
				httpUtil.setRequestAttribute("paramStatutArray", StringUtil.getArrayFromStringDelim(paramStatutEcran.getValeur(), ";"));
				
				ParametrageRightsConstantes.loadParamsGlobalByGroup(httpUtil, TYPE_PARAM.CUISINE_GLOB);
				httpUtil.setDynamicUrl("/domaine/caisse/back-office/parametrage/parametrage_cuisine.jsp");
			}; break;
			case "gen" : {
				ParametrageRightsConstantes.loadParamsGlobalByGroup(httpUtil, TYPE_PARAM.BACK_OFF);
				httpUtil.setDynamicUrl("/domaine/caisse/back-office/parametrage/parametrage_edit.jsp");
			}; break;
			case "mnt" : {
				EtatFinancePersistant etatF = etatFinanceService.getMoisClosNonPurge();
				if(etatF != null) {
					httpUtil.setRequestAttribute("moisPurge", etatF);
				}
				httpUtil.setRequestAttribute("systemInfos", paramService.getSystemInfos());
				
				httpUtil.setDynamicUrl("/domaine/caisse/back-office/parametrage/parametrage_maintenance.jsp");
			}; break;
			case "tick" : {
				String [][]formatArray ={{"TC", "Ticket de caisse"}, {"A4", "A4"}};
				httpUtil.setRequestAttribute("formatArray", formatArray);
				
				httpUtil.setRequestAttribute("restaurant", ContextAppli.getEtablissementBean());
				ParametrageRightsConstantes.loadParamsGlobalByGroup(httpUtil, TYPE_PARAM.TICKET_GLOB);
				httpUtil.setDynamicUrl("/domaine/caisse/back-office/parametrage/parametrage_ticket.jsp");
			}; break;
			case "cmdl" : {
				EtablissementPersistant etsP = paramService.findById(EtablissementPersistant.class, ContextAppli.getEtablissementBean().getId());
				MessageService.getGlobalMap().put("GLOBAL_ETABLISSEMENT", etsP);
				httpUtil.setRequestAttribute("etablissement",  etsP);
				ParametrageRightsConstantes.loadParamsGlobalByGroup(httpUtil, TYPE_PARAM.CAISSE_ETS_CMD);
				
				// utilisation reflection car spécifique agenda
				if(ContextAppli.IS_ERP_ENV()) {
					try { 
						Object listSpec = ReflectUtil.invokeMethode(ServiceUtil.getBusinessBean("ClientVisiteService"), "getSpecialiteAutocomplete", new Object[]{"", 9000}, new Class[] {String.class, Integer.class});
						httpUtil.setRequestAttribute("listeSpecialite",  listSpec);
						httpUtil.setRequestAttribute("listeLieux", paramService.findAll(LieuxPersistant.class, Order.asc("libelle")));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				httpUtil.setDynamicUrl("/domaine/caisse/back-office/parametrage/parametrage_ets_cmd.jsp");
			}; break;
		}
	}
	
	public void setValeurDefaut(ActionUtil httpUtil){
		List<ParametragePersistant> paramsAll = paramService.findAll(Order.asc("ordre")); Order.asc("id");
		for (ParametragePersistant parametragePersistant : paramsAll) {
			if(parametragePersistant.getGroupe().equals("BUTTON_COLOR") || parametragePersistant.getGroupe().equals("PANEL_COLOR")) {
				parametragePersistant.setValeur("");
			} 
		}
		httpUtil.setRequestAttribute("listParams", paramsAll);
		
		work_init_update(httpUtil);
	}
	
	/**
	 * @param httpUtil
	 */
	public void downloadPieceJointe(ActionUtil httpUtil) {
		httpUtil.manageInputFileView("param");
	}
	
	//--------- Framwork --------------//
	public void clearTabSession(ActionUtil httpUtil) {
		String tabId = httpUtil.getParameter("tab");
		String menuPrefix = SESSION_SCOPE_ENUM.MENU.getType();
		HttpSession session = httpUtil.getRequest().getSession(false);
		if(session != null){
			Enumeration attributes = session.getAttributeNames();
			while(attributes.hasMoreElements()){
				String attribute = (String)attributes.nextElement();
				//
				if(attribute.startsWith(tabId+menuPrefix)){
					session.removeAttribute(attribute);
				}
			}
		}
	}
	public void refreshTabAction(ActionUtil httpUtil) {
		httpUtil.setRequestAttribute("IS_FROM_MAP", "OK");
		ControllerUtil.removeAllValidatorMap(httpUtil.getRequest());
		httpUtil.setDynamicUrl(EncryptionUtil.decrypt(httpUtil.getRequest().getParameter("oldact")));
	}
	
}
