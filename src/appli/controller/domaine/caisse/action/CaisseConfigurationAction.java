package appli.controller.domaine.caisse.action;

import java.math.BigDecimal;
import java.util.Map;

import javax.inject.Inject;

import appli.controller.domaine.administration.ParametrageRightsConstantes;
import appli.controller.domaine.administration.ParametrageRightsConstantes.TYPE_PARAM;
import appli.controller.domaine.caisse.bean.CaisseBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_CAISSE_ENUM;
import appli.model.domaine.administration.service.IParametrageService;
import appli.model.domaine.caisse.service.ICaisseService;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.controller.annotation.WorkForward;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.service.MessageService;

@WorkController(nameSpace="caisse", bean=CaisseBean.class, jspRootPath="/domaine/caisse/")
public class CaisseConfigurationAction extends ActionBase {
	@Inject
	private ICaisseService caisseService;
	@Inject
	private IParametrageService paramService;
	
	public void work_post(ActionUtil httpUtil){
		Long caisseId = (Long)httpUtil.getMenuAttribute("caisseId");
		httpUtil.setRequestAttribute("caisseId", caisseId);
		
		httpUtil.setRequestAttribute("list_imprimante", ParametrageRightsConstantes.getListPrinters());
		
		String[][] orientationBalance = {{"L", "Paysage"}, {"LR", "Paysage inversé"}, {"P", "Portrait"}};
	    httpUtil.setRequestAttribute("orientationBalance", orientationBalance);
		
		CaisseBean caisseBean = caisseService.findById(caisseId);
		TYPE_CAISSE_ENUM typeEcran = TYPE_CAISSE_ENUM.valueOf(caisseBean.getType_ecran());
		
		//
		if(typeEcran.equals(TYPE_CAISSE_ENUM.AFFICHEUR)){
			ParametrageRightsConstantes.loadParamsSpecByGroup(httpUtil, TYPE_PARAM.AFFICHEUR_CAISSE_SPEC, caisseId);
			httpUtil.setDynamicUrl("/domaine/caisse/back-office/conf_caisse/afficheur.jsp");
		} else if(typeEcran.equals(TYPE_CAISSE_ENUM.CUISINE)){
			ParametrageRightsConstantes.loadParamsSpecByGroup(httpUtil, TYPE_PARAM.CUISINE_SPEC, caisseId);
			httpUtil.setDynamicUrl("/domaine/caisse/back-office/conf_caisse/cuisine.jsp");
		} else if(typeEcran.equals(TYPE_CAISSE_ENUM.BALANCE)){
			String[][] listModesBalance = {
					{"C", "Code barre"},
					{"M", "Saisie manuelle"}
			};
			httpUtil.setRequestAttribute("listModesBalance", listModesBalance);
			
			String[][] listBaseBalance = {
					{"mysql", "MYSQL"},
					{"access", "ACCESS"}
			};
			httpUtil.setRequestAttribute("listBaseBalance", listBaseBalance);
			
			String[][] listCompoBarre = {
					{"PR2;CB5;PD5;SU1", "Prefix(2)|Code barre(5)|Poids(5)|Suffix(1)"},
					{"PR2;PD5;CB5;SU1", "Prefix(2)|Poids(5)|Code barre(5)|Suffix(1)"},
					{"PR2;CB6;PD5", "Prefix(2)|Code barre(6)|Poids(5)"},
					{"PR2;PD5;CB5", "Prefix(2)|Poids(6)|Code barre(5)"}
				};
			httpUtil.setRequestAttribute("listCompoBarre", listCompoBarre);
			
			ParametrageRightsConstantes.loadParamsSpecByGroup(httpUtil, TYPE_PARAM.BALANCE_SPEC, caisseId);
			httpUtil.setDynamicUrl("/domaine/caisse/back-office/conf_caisse/balance.jsp");
		} else if(typeEcran.equals(TYPE_CAISSE_ENUM.CAISSE)){
			ParametrageRightsConstantes.loadParamsSpecByGroup(httpUtil, TYPE_PARAM.CAISSE_SPEC, caisseId);
			
			String[][] listTypeArrondi = {
					{"1_"+BigDecimal.ROUND_UP, "Supérieur avec virgule"},
					{"0_"+BigDecimal.ROUND_UP, "Supérieur net"},
					{"1_"+BigDecimal.ROUND_DOWN, "Inférieur avec virgule"},
					{"0_"+BigDecimal.ROUND_DOWN, "Inférieur net"},
					{"1_"+BigDecimal.ROUND_HALF_DOWN, "Supérieur/Inférieur avec virgule"},
					{"0_"+BigDecimal.ROUND_HALF_DOWN, "Supérieur/Inférieur net"},
			};
			httpUtil.setRequestAttribute("listTypeArrondi", listTypeArrondi);
			
			httpUtil.setDynamicUrl("/domaine/caisse/back-office/conf_caisse/caisse.jsp");
		} else if(typeEcran.equals(TYPE_CAISSE_ENUM.LECTEUR)){
			ParametrageRightsConstantes.loadParamsSpecByGroup(httpUtil, TYPE_PARAM.LECTEUR_PRIX_SPEC, caisseId);
			httpUtil.setDynamicUrl("/domaine/caisse/back-office/conf_caisse/lecteur.jsp");
		}
	}
	
	@Override
	public void work_init_update(ActionUtil httpUtil) {
	
	}
	
	@WorkForward(bean=CaisseBean.class, useBean=true)
	public void work_update(ActionUtil httpUtil){
		Long caisseId = (Long)httpUtil.getMenuAttribute("caisseId");
		Map<String, Object> params = httpUtil.getValuesByStartName("param_");
		
		CaisseBean caisseBean = caisseService.findById(caisseId);
		TYPE_CAISSE_ENUM typeEcran = TYPE_CAISSE_ENUM.valueOf(caisseBean.getType_ecran());
		//
		paramService.updateParams(params, caisseId);
		
		if(typeEcran.equals(TYPE_CAISSE_ENUM.AFFICHEUR)){
			EtablissementPersistant restauBean = ContextAppli.getEtablissementBean();
			managePieceJointe(httpUtil, restauBean.getId(), "param");// Image afficheur
		} else if(typeEcran.equals(TYPE_CAISSE_ENUM.CAISSE)){
			managePieceJointe(httpUtil, ContextAppli.getEtablissementBean().getId(), "paramTICK");// Image publicité
			managePieceJointe(httpUtil, ContextAppli.getEtablissementBean().getId(), "paramFE");// Image mise en veille
		}
		//
		work_edit(httpUtil);
		
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "Mise à jour", "La mise à jour est effectuée avec succès.");
	}
}
