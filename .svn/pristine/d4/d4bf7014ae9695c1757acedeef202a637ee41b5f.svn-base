package appli.controller.domaine.stock.action;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import appli.controller.domaine.stock.bean.EmplacementBean;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.stock.service.IArticleService;
import appli.model.domaine.stock.service.IEmplacementService;
import appli.model.domaine.stock.service.IFamilleService;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.common.constante.ActionConstante;
import framework.model.common.util.NumericUtil;
import framework.model.common.util.StringUtil;

@WorkController(nameSpace="stock", bean=EmplacementBean.class, jspRootPath="/domaine/stock/")
public class EmplacementAction extends ActionBase {
	@Inject
	private IEmplacementService emplacementService;
	@Inject
	private IFamilleService familleService;
	@Inject
	private IArticleService articleService;
	
	public void work_init(ActionUtil httpUtil) {
		EmplacementBean emplacementBean = (EmplacementBean) httpUtil.getViewBean();
		String action = httpUtil.getAction();
		if(action.equals(ActionConstante.MERGE) || action.equals(ActionConstante.CREATE) || action.equals(ActionConstante.UPDATE)){
    		Set<String> articlesSet = new HashSet<>();
    		Set<String> famillesSet = new HashSet<>();
    		
    		
    		if(emplacementBean.getArticles_array() != null){
    			for (String articleId :  emplacementBean.getArticles_array()) {
    				if(StringUtil.isNotEmpty(articleId)){
    					articlesSet.add(articleId);
    				}
    			}
    		}
    		if(emplacementBean.getFamilles_array() != null){
    			for (String familleId :  emplacementBean.getFamilles_array()) {
    				if(StringUtil.isNotEmpty(familleId) && NumericUtil.isNum(familleId)){
    					famillesSet.add(familleId);
    				}
    			}
    			// AJouter les enfants de ces familles
    			for (String familleId :  emplacementBean.getFamilles_array()) {
    				if(StringUtil.isNotEmpty(familleId) && NumericUtil.isNum(familleId)){
    					List<FamillePersistant> listFamilleEnfant = familleService.getFamilleEnfants("ST", Long.valueOf(familleId), true);
    					for (FamillePersistant familleEnfantP : listFamilleEnfant) {
    						famillesSet.add(familleEnfantP.getId().toString());
    					}
    				}
    			}
    		}
    		
    		if(famillesSet.size() > 0) {
    			String familles = ";";
        		for (String famId : famillesSet) {
        			familles = familles + famId + ";"; 
    			}
        		emplacementBean.setFamilles_cmd(familles);
    		}
    		
			if(articlesSet.size() > 0) {
	    		String articles = ";";
	    		for (String artId : articlesSet) {
	    			articles = articles + artId + ";"; 
				}
	    		emplacementBean.setArticles_cmd(articles);
    		}
			
    		// Excluded
    		Set<String> articlesExSet = new HashSet<>();
    		Set<String> famillesExSet = new HashSet<>();
    		if(emplacementBean.getArticles_ex_array() != null){
    			for (String articleId :  emplacementBean.getArticles_ex_array()) {
    				if(StringUtil.isNotEmpty(articleId)){
    					articlesExSet.add(articleId);
    				}
    			}
    		}
    		if(emplacementBean.getFamilles_ex_array() != null){
    			for (String familleId :  emplacementBean.getFamilles_ex_array()) {
    				if(StringUtil.isNotEmpty(familleId) && NumericUtil.isNum(familleId)){
    					famillesExSet.add(familleId);
    				}
    			}
    			// AJouter les enfants de ces familles
    			for (String familleId :  emplacementBean.getFamilles_ex_array()) {
    				if(StringUtil.isNotEmpty(familleId) && NumericUtil.isNum(familleId)){
    					List<FamillePersistant> listFamilleEnfant = familleService.getFamilleEnfants("ST", Long.valueOf(familleId), true);
    					for (FamillePersistant familleEnfantP : listFamilleEnfant) {
    						famillesExSet.add(familleEnfantP.getId().toString());
    					}
    				}
    			}
    		}
    		
			if(famillesExSet.size() > 0) {
	    		String famillesEx = ";";
	    		for (String famExId : famillesExSet) {
	    			famillesEx = famillesEx + famExId + ";"; 
				}
	    		emplacementBean.setFamilles_ex_cmd(famillesEx);
    		}
    		
			if(articlesExSet.size() > 0) {
	    		String articlesEx = ";";
	    		for (String artExId : articlesExSet) {
	    			articlesEx = articlesEx + artExId + ";"; 
				}
	    		emplacementBean.setArticles_ex_cmd(articlesEx);
    		}
			
    	}
		
		// Etablissement destinations -----------------------
//		if(ReplicationGenerationEventListener._IS_CLOUD_SYNCHRO_INSTANCE){
//			EmplacementBean dataBean = (EmplacementBean) httpUtil.getViewBean();
//			if(action.equals(ActionConstante.MERGE) || action.equals(ActionConstante.CREATE) || action.equals(ActionConstante.UPDATE)) {
//				String ets_ids = "";
//				if(dataBean.getEts_ids() != null){
//					for (Long etsId :  dataBean.getEts_ids()) {
//						ets_ids = ets_ids + etsId + ";";
//					}
//				}
//				dataBean.setEts_dest(ets_ids);
//			}
//		}//------------------------------------------------

		//
		httpUtil.setRequestAttribute("listFamilles", familleService.getListeFamille("ST", true, false));
		httpUtil.setRequestAttribute("listArticles", articleService.getListArticleStock(true));
	}
	
	@Override
	public void work_merge(ActionUtil httpUtil) {
		super.work_merge(httpUtil);
		if(httpUtil.getMenuAttribute("IS_SUB_ADD") != null) {
			httpUtil.setDynamicUrl(""+httpUtil.getMenuAttribute("IS_SUB_ADD"));
		}
	}
	
	/**
	 * @param httpUtil
	 */
	public void desactiver(ActionUtil httpUtil) {
		emplacementService.activerDesactiverElement(httpUtil.getWorkIdLong());
		work_find(httpUtil);
	}
	
	/**
	 * @param httpUtil
	 */
	public void work_post(ActionUtil httpUtil) {
		EmplacementBean emplacementBean = (EmplacementBean) httpUtil.getViewBean();
		String action = httpUtil.getAction();
		if(action.equals(ActionConstante.EDIT) || action.equals(ActionConstante.INIT_UPDATE)) {
			String[] articleArray = StringUtil.getArrayFromStringDelim(emplacementBean.getArticles_cmd(), ";");
			String[] familleArray = StringUtil.getArrayFromStringDelim(emplacementBean.getFamilles_cmd(), ";");
        	emplacementBean.setArticles_array(articleArray);
        	emplacementBean.setFamilles_array(familleArray);
        	// Exclude
        	String[] articleArrayEx = StringUtil.getArrayFromStringDelim(emplacementBean.getArticles_ex_cmd(), ";");
			String[] familleArrayEx = StringUtil.getArrayFromStringDelim(emplacementBean.getFamilles_ex_cmd(), ";");
        	emplacementBean.setArticles_ex_array(articleArrayEx);
        	emplacementBean.setFamilles_ex_array(familleArrayEx);
		}
		
//		if(ReplicationGenerationEventListener._IS_CLOUD_SYNCHRO_INSTANCE){
//			if(action.equals(ActionConstante.EDIT) || action.equals(ActionConstante.INIT_UPDATE)) {
//				EmplacementBean dataBean = (EmplacementBean) httpUtil.getViewBean();
//				if(StringUtil.isNotEmpty(dataBean.getEts_dest())) {
//					String[] etsIds = StringUtil.getArrayFromStringDelim(dataBean.getEts_dest(), ";");
//					Long[] etsIdsLong = new Long[etsIds.length]; 
//					for (int i=0; i<etsIds.length; i++) {
//						etsIdsLong[i] = Long.valueOf(etsIds[i]);
//					}
//					dataBean.setEts_ids(etsIdsLong);
//				}
//			}
//		}
	}
}
