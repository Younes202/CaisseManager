package appli.controller.domaine.stock.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import appli.controller.domaine.stock.bean.FamilleBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.stock.service.IFamilleService;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.common.constante.ActionConstante;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.EncryptionEtsUtil;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;

@WorkController(nameSpace="stock", bean=FamilleBean.class, jspRootPath="/domaine/stock/")
public class FamilleAction extends ActionBase {
	@Inject
	private IFamilleService familleService;
	
	public void work_init(ActionUtil httpUtil) {
		String tp = httpUtil.getParameter("tp"); 
		if(tp != null){
		    httpUtil.setMenuAttribute("tp", tp);
		    httpUtil.setMenuAttribute("famPath", ("SP".equals(tp) ? "agenda":"stock"));
		} else{
			tp = (String) httpUtil.getMenuAttribute("tp");
		}
		String parentId = httpUtil.getParameter("fam");
		List listFamille = familleService.getListeFamille(tp, false, false);
		httpUtil.setRequestAttribute("listFamille", listFamille);
		
		String currParentId = httpUtil.getParameter("famille.parent_id");
		if(currParentId == null && parentId != null){
			FamillePersistant parentFamille = familleService.getFamilleParent(Long.valueOf(EncryptionUtil.decrypt(parentId)));
			if(parentFamille != null){
				currParentId = ""+parentFamille.getId();
			}
		}
		httpUtil.setRequestAttribute("parent_fam", currParentId);
		
		//
		if(StringUtil.isNotEmpty(parentId)){
			httpUtil.setRequestAttribute("fam", parentId);
		} else{
			httpUtil.setRequestAttribute("fam", EncryptionUtil.encrypt(familleService.getFamilleRoot(tp).getId().toString()));// Parent racine
		}
		
		if(ContextAppli.getAbonementBean().isOptPlusEtsCentrale() 
				&& StringUtil.isTrue(StrimUtil.getGlobalConfigPropertie("CTRL_CENTRALE"))) {
			httpUtil.setRequestAttribute("isEditable", false);
		} else {
			httpUtil.setRequestAttribute("isEditable", true);
		}
		
		// Caisse
		if(tp.equals("CU")){
			httpUtil.setRequestAttribute("listeCaisse", familleService.getListCaisseActive(ContextAppli.TYPE_CAISSE_ENUM.CAISSE.toString(), false));
		 
			if(httpUtil.isCrudOperation()) {
				if(httpUtil.getViewBean() != null) {
		        	FamilleBean familleBean = (FamilleBean)httpUtil.getViewBean();
	        		String caisse = StringUtil.getStringDelimFromStringArray(familleBean.getCaisses_target(), "|");
	        		familleBean.setCaisse_target(StringUtil.isNotEmpty(caisse) ? "|"+caisse+"|":null);
	        	} 
			} else if(httpUtil.getWorkIdLong() != null) {
				FamilleBean familleBean = familleService.findById(httpUtil.getWorkIdLong());
	        	if(StringUtil.isNotEmpty(familleBean.getCaisse_target())) {
		        	String[] caisseArray = StringUtil.getArrayFromStringDelim(familleBean.getCaisse_target(), "|");
		        	familleBean.setCaisses_target(caisseArray);
		        	httpUtil.setRequestAttribute("caisseArray", caisseArray);
	        	}
	        }
		}
	}
	 @Override 
	 public void work_init_create(ActionUtil httpUtil) {
		 String famStr = httpUtil.getParameter("fam");
		 String decrypt = EncryptionUtil.decrypt(famStr);
		if(!StringUtil.isEmpty(decrypt)) {
			long famLong = Long.parseLong(decrypt);
				FamilleBean familleBean = familleService.findById(famLong);
				httpUtil.setRequestAttribute("familleLevel", familleBean.getLevel());
		}
		else {
			httpUtil.setRequestAttribute("familleLevel", 0);
		}
			super.work_init_create(httpUtil);
	 }
	 
	 @Override
	 public void work_edit(ActionUtil httpUtil) {
	     String famStr = httpUtil.getParameter("fam");
	     String decrypt = EncryptionUtil.decrypt(famStr);

	     if (!StringUtil.isEmpty(decrypt)) {
	         long famLong = Long.parseLong(decrypt);
	         FamilleBean familleBean = familleService.findById(famLong);
	        // httpUtil.setRequestAttribute("familleLevel", familleBean.getLevel());
	         httpUtil.setUserAttribute("familleLevel", familleBean.getLevel());
	     } else {
	         httpUtil.setRequestAttribute("familleLevel", 0);
	     }

	     super.work_edit(httpUtil);
	 }


	@Override
	public void work_find(ActionUtil httpUtil) {
		String tp = (String) httpUtil.getMenuAttribute("tp");
		// Forcer le rechargement
		familleService.getEntityManager().clear();
		
//		List listFamilles = null;
		boolean actifOnly = false;
		if("on".equals(httpUtil.getParameter("actifs-activator"))){
			actifOnly = true;
			httpUtil.setRequestAttribute("isCheked", true);
		}
		List listFamilles = familleService.getListeFamille(tp, false, false);
		
		httpUtil.setRequestAttribute("actifOnly", actifOnly);
		httpUtil.setRequestAttribute("listFamilles", listFamilles);
		
		httpUtil.setDynamicUrl("/domaine/"+ httpUtil.getMenuAttribute("famPath")+"/famille_list.jsp");
	}
	
	@Override
	public void work_create(ActionUtil httpUtil) {
		Long parentId = httpUtil.getLongParameter("fam.id");
		FamilleBean familleBean = (FamilleBean)httpUtil.getViewBean();
		familleBean.setElement_id(parentId);
		
		familleBean.setType((String) httpUtil.getMenuAttribute("tp"));
		//
		familleService.createFamille(familleBean);
		//
		if(httpUtil.getMenuAttribute("IS_SUB_ADD") != null) {
			httpUtil.setDynamicUrl(""+httpUtil.getMenuAttribute("IS_SUB_ADD"));
		} else {
		    work_find(httpUtil);
		}
		managePieceJointe(httpUtil, familleBean.getId(), "famille", 300, 300);
	}
	
	@Override
	public void work_update(ActionUtil httpUtil) {
		FamilleBean familleBean = (FamilleBean)httpUtil.getViewBean();
		//
		familleService.updateFamille(familleBean);
		work_find(httpUtil);
		
		managePieceJointe(httpUtil, familleBean.getId(), "famille", 300, 300);
	}
	
	@Override
	public void work_delete(ActionUtil httpUtil) {
		Long workId = httpUtil.getWorkIdLong();
		familleService.deleteFamille(workId);
		//
		manageDeleteImage(workId, "famille");
		
		work_find(httpUtil);
	}
	
	/**
	 * @param httpUtil
	 */
	public void generer_code(ActionUtil httpUtil) {
		String tp = (String) httpUtil.getMenuAttribute("tp");
		Long parentId = httpUtil.getLongParameter("fam.id");
		//
		String code = familleService.generateCode(parentId, tp);
		httpUtil.writeResponse(code);
	}
	
	/**
	 * @param httpUtil
	 */
	public void desactiver(ActionUtil httpUtil) {
		familleService.activerDesactiverElement(httpUtil.getWorkIdLong());
		work_find(httpUtil);
	}
	
	/**
	 * @param httpUtil
	 */
	public void initOrderMenu(ActionUtil httpUtil) {
		String tp = (String) httpUtil.getMenuAttribute("tp");
		Long familleId = httpUtil.getWorkIdLong();
		FamillePersistant menu = null;
		//
		if(familleId != null) {
			menu = familleService.findById(familleId);
		} else {
			menu = familleService.getFamilleRoot(tp);
			familleId = menu.getId();
		}
		
		List<FamillePersistant> listFamille = familleService.getFamilleEnfants(tp, familleId, true);
		
		List<FamillePersistant> finalList = new ArrayList<>();
		for (FamillePersistant mcP : listFamille) {
			if(menu.getLevel()+1 == mcP.getLevel()) {
				finalList.add(mcP);
			}
		}
		httpUtil.setRequestAttribute("listFamilles", finalList);
		
		httpUtil.setDynamicUrl("/domaine/"+ httpUtil.getMenuAttribute("famPath")+"/famille_order.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void ordonnerMenu(ActionUtil httpUtil) {
		String tp = (String) httpUtil.getMenuAttribute("tp");
		Map<String, Object> mapOrder = httpUtil.getValuesByStartName("familleOrder_");
		familleService.changerOrdre(mapOrder, tp);
		
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "", "L'ordre des éléments est mise à jour.");
		//
		work_find(httpUtil);
	}
	
	public void work_post(ActionUtil httpUtil) {
		if(httpUtil.getAction().equals(ActionConstante.INIT_CREATE)
				|| httpUtil.getAction().equals(ActionConstante.INIT_UPDATE)
				|| httpUtil.getAction().equals(ActionConstante.EDIT)) {
			httpUtil.setDynamicUrl("/domaine/"+ httpUtil.getMenuAttribute("famPath")+"/famille_edit.jsp");
		}
	}
}
