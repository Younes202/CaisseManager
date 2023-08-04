package framework.controller;

import java.util.Map;

import appli.controller.domaine.administration.ParametrageRightsConstantes;
import appli.controller.domaine.administration.bean.UserBean;
import framework.model.beanContext.AbonnementBean;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.beanContext.ExerciceBean;
import framework.model.beanContext.ExercicePersistant;
import framework.model.beanContext.SocietePersistant;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.StringUtil;

public class ContextGloabalAppli {
	
	public enum PARAM_APPLI_ENUM {
		NBR_DECIMAL, NBR_DECIMAL_SAISIE
	}
	
	public static enum STATUT_EXERCICE {
		EN_COURS_CLOTURE("EC", "En cours de clôture"),
		CLOTURE("C", "Clos"),
		CLOTURE_DEFINITIF("CD", "Clôture définitive"),
		EN_COURS_OUVERTURE("EO", "En cours d'ouverture"),
		OUVERT("O", "Ouvert");
		
		private String statut;
		private String libelle;
		STATUT_EXERCICE(String statut, String libelle){
			this.statut = statut;
			this.libelle = libelle;
		}
		public String getStatut() {
			return statut;
		}
		public String getLibelle() {
			return libelle;
		}
		public static String getLibelleFromStatut(String exeStatut){
			for (STATUT_EXERCICE currStatut : STATUT_EXERCICE.values()) {
				if(currStatut.getStatut().equals(exeStatut)){
					return currStatut.getLibelle();
				}
			}
			return STATUT_EXERCICE.OUVERT.getLibelle();
		}
	}
	
	/**
	 * @return
	 */
	public static SocietePersistant getSocieteBean() {
		return (SocietePersistant) MessageService.getGlobalMap().get("GLOBAL_SOCIETE");
	}
	/**
	 * @return
	 */
	public static EtablissementPersistant getEtablissementBean() {
		return (EtablissementPersistant) MessageService.getGlobalMap().get("GLOBAL_ETABLISSEMENT");
	}
	/**
	 * @return
	 */
	public static ExercicePersistant getExerciceBean() {
		return (ExercicePersistant) MessageService.getGlobalMap().get("CURRENT_EXERCICE");
	}
	public static boolean isExerciceOuvert(ExerciceBean exeBean) {
		return STATUT_EXERCICE.OUVERT.getStatut().toString().equals(exeBean.getStatut_cloture());
	}
	
	/**
	 * @return
	 */
	public static UserBean getUserBean() {
		return (UserBean) MessageService.getGlobalMap().get(ProjectConstante.SESSION_GLOBAL_USER);
	}
	
	public static AbonnementBean getAbonementBean(){
		return (AbonnementBean) MessageService.getGlobalMap().get("ABONNEMENT_BEAN");
	}
	
	public static String getGlobalConfig(Long caisseId, String key){ 
		if(MessageService.getGlobalMap().get("GLOBAL_CONFIG_SPEC") == null) {
			ParametrageRightsConstantes.loadAllMapSpecParams(true);
		}
		Map<Long, Map<String, String>> mapParams = (Map<Long, Map<String, String>>)MessageService.getGlobalMap().get("GLOBAL_CONFIG_SPEC");
		Map<String, String> mapParamsSpec = mapParams.get(caisseId); 
		
		return (mapParamsSpec != null ? mapParamsSpec.get(key) : null);
	}
	public static String getGlobalConfig(String key){ 
		Object object = MessageService.getGlobalMap().get("GLOBAL_CONFIG");
		
		if(object == null) {
			ParametrageRightsConstantes.loadAllMapGlobParams(true);	
			object = MessageService.getGlobalMap().get("GLOBAL_CONFIG");
		}
		String val = object!=null ? ((Map<String, String>) object).get(key) : null;
		String val2 = null;
		
		if(val == null || object == null) {
			if(MessageService.getGlobalMap().get("CURRENT_CAISSE") != null) {
				Long caisseId = (Long) ReflectUtil.getObjectPropertieValue(MessageService.getGlobalMap().get("CURRENT_CAISSE"), "id");
				val2 = getGlobalConfig(caisseId, key);
			}
		}
		return (val2 != null ? val2 : val);
	}
	
	public static int getNbrDecimal() {
		int nbr = 2;
		String nbrDec = ContextGloabalAppli.getGlobalConfig(PARAM_APPLI_ENUM.NBR_DECIMAL.toString());
		if (StringUtil.isEmpty(nbrDec)) {
			nbrDec = "" + nbr;
		}
		return Integer.valueOf(nbrDec);
	}
	public static int getNbrDecimalSaisie() {
		int nbr = 2;
		String nbrDec = ContextGloabalAppli.getGlobalConfig(PARAM_APPLI_ENUM.NBR_DECIMAL_SAISIE.toString());
		if (StringUtil.isEmpty(nbrDec)) {
			nbrDec = "" + nbr;
		}
		return BigDecimalUtil.get(nbrDec).intValue();
	}
}
