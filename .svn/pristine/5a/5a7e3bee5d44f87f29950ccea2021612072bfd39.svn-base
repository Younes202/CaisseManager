package appli.controller.domaine.stock.action;

import java.util.Map;

import javax.inject.Inject;

import appli.controller.domaine.administration.ParametrageRightsConstantes;
import appli.controller.domaine.administration.ParametrageRightsConstantes.TYPE_PARAM;
import appli.controller.domaine.stock.bean.ArticleBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.administration.persistant.EtatFinancePersistant;
import appli.model.domaine.administration.persistant.ParametragePersistant;
import appli.model.domaine.administration.service.ICompteBancaireService;
import appli.model.domaine.administration.service.IEtatFinanceService;
import appli.model.domaine.administration.service.IParametrageService;
import appli.model.domaine.stock.service.IMouvementService;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.StringUtil;

@WorkController(nameSpace = "caisse", bean = ArticleBean.class, jspRootPath = "/domaine/caisse/wizard")
public class WizardInstallAction extends ActionBase {
	@Inject
	private IMouvementService mouvementService;
	@Inject
	private IEtatFinanceService etatFinanceService;
	@Inject
	private IParametrageService paramService;
	@Inject
	private ICompteBancaireService banqueService;

	public void initBO(ActionUtil httpUtil) {
		ParametrageRightsConstantes.loadParamsGlobalByGroup(httpUtil, TYPE_PARAM.BACK_OFF);

		httpUtil.setDynamicUrl("/domaine/caisse/wizard/wizardInstall_BO.jsp");
	}

	public void initCaisse(ActionUtil httpUtil) {
		// save BO
		if ("s".equals(httpUtil.getParameter("sens"))) {
			Map<String, Object> params = httpUtil.getValuesByStartName("param_");
			paramService.updateParams(params, null);
		}

		String[][] listCompoBarre = { { "PR2;CB5;PD5;SU1", "Prefix(2)|Code barre(5)|Poids(5)|Suffix(1)" },
				{ "PR2;PD5;CB5;SU1", "Prefix(2)|Poids(5)|Code barre(5)|Suffix(1)" },
				{ "PR2;CB6;PD5", "Prefix(2)|Code barre(6)|Poids(5)" },
				{ "PR2;PD5;CB5", "Prefix(2)|Poids(6)|Code barre(5)" } };
		httpUtil.setRequestAttribute("listCompoBarre", listCompoBarre);

		ParametrageRightsConstantes.loadParamsGlobalByGroup(httpUtil, TYPE_PARAM.CAISSE_GLOB);
		 httpUtil.setDynamicUrl("/domaine/caisse/wizard/wizardInstall_caisse.jsp");
	
	}

	public void initTicket(ActionUtil httpUtil) {

		// save cuisine
//		if ("s".equals(httpUtil.getParameter("sens"))) {
//			Map<String, Object> params = httpUtil.getValuesByStartName("param_");
//			String statut = StringUtil.getStringDelimFromStringArray(httpUtil.getRequest().getParameterValues("param_ECRAN_STATUT"), ";");
//			params.put("ECRAN_STATUT", statut);
//			paramService.updateParams(params, null);
//		}

		String[][] formatArray = { { "TC", "Ticket de caisse" }, { "A4", "A4" } };
		httpUtil.setRequestAttribute("formatArray", formatArray);

		httpUtil.setRequestAttribute("restaurant", ContextAppli.getEtablissementBean());
		ParametrageRightsConstantes.loadParamsGlobalByGroup(httpUtil, TYPE_PARAM.TICKET_GLOB);
		httpUtil.setDynamicUrl("/domaine/caisse/wizard/wizardInstall_ticket.jsp");
	}

	public void initDivers(ActionUtil httpUtil) {
		// save ticket
		if ("s".equals(httpUtil.getParameter("sens"))) {
			Map<String, Object> params = httpUtil.getValuesByStartName("param_");
			String msgPublicite = null, titrePublicite = null;
			msgPublicite = httpUtil.getParameter("restaurant.msg_publicite");
			titrePublicite = httpUtil.getParameter("restaurant.titre_publicite");
			managePieceJointe(httpUtil, ContextAppli.getEtablissementBean().getId(), "paramTICK");// Image publicité
			paramService.updateParams(params, titrePublicite, msgPublicite, null);
		}

		httpUtil.setRequestAttribute("listeBanque", banqueService.findAll());
		ParametrageRightsConstantes.loadParamsGlobalByGroup(httpUtil, TYPE_PARAM.DIVERS_GLOB);
		httpUtil.setDynamicUrl("/domaine/caisse/wizard/wizardInstall_divers.jsp");
	}

	public void initMaintenance(ActionUtil httpUtil) {

		// save divers
		if ("s".equals(httpUtil.getParameter("sens"))) {
			Map<String, Object> params = httpUtil.getValuesByStartName("param_");
			// Retirer cette info si pas d"abonnement
			if (!BooleanUtil.isTrue(ContextAppli.getAbonementBean().isOptPlusOptimisation())) {
				params.put("TAUX_OPTIM", "");
				params.put("SEUIL_OPTIM", "");
			}
			paramService.updateParams(params, null);
		}

		EtatFinancePersistant etatF = etatFinanceService.getMoisClosNonPurge();
		if (etatF != null) {
			httpUtil.setRequestAttribute("moisPurge", etatF);
		}
		httpUtil.setRequestAttribute("systemInfos", paramService.getSystemInfos());

		httpUtil.setDynamicUrl("/domaine/caisse/wizard/wizardInstall_maintenance.jsp");
	}

	public void initCuisine(ActionUtil httpUtil) {

		// save caisse
		if ("s".equals(httpUtil.getParameter("sens"))) {
			Map<String, Object> params = httpUtil.getValuesByStartName("param_");
			managePieceJointe(httpUtil, ContextAppli.getEtablissementBean().getId(), "photoZ");// Image publicité
			paramService.updateParams(params, null);
		}

		httpUtil.setRequestAttribute("listModeTravail", new String[][]{{"PO", "Imprimantes seulement"}, {"EO", "Ecran seulement"}});
		ParametragePersistant paramStatutEcran = paramService.getParameterByCode("ECRAN_STATUT");
		httpUtil.setRequestAttribute("paramStatutArray", StringUtil.getArrayFromStringDelim(paramStatutEcran.getValeur(), ";"));
		
		ParametrageRightsConstantes.loadParamsGlobalByGroup(httpUtil, TYPE_PARAM.CUISINE_GLOB);
		httpUtil.setDynamicUrl("/domaine/caisse/wizard/wizardInstall_cuisine.jsp");
	}
	

	public void initFamille(ActionUtil httpUtil) {

		// save caisse
		
		httpUtil.setDynamicUrl("/domaine/caisse/wizard/wizardInstall_famille.jsp");
	}
	
	
	public void saveAll(ActionUtil httpUtil) {
		httpUtil.writeResponse("REDIRECT: ");
	}

}
