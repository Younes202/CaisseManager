package appli.controller.domaine.stock.action;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.hibernate.criterion.Order;

import appli.controller.domaine.personnel.bean.paie.SalariePaieBean;
import appli.controller.domaine.stock.bean.ChargeDiversBean;
import appli.controller.domaine.stock.bean.MouvementBean;
import appli.controller.domaine.stock.bean.TravauxBean;
import appli.model.domaine.personnel.persistant.EmployePersistant;
import appli.model.domaine.personnel.service.ISalariePaieService;
import appli.model.domaine.stock.persistant.ChargeDiversPersistant;
import appli.model.domaine.stock.persistant.EmplacementPersistant;
import appli.model.domaine.stock.persistant.MouvementPersistant;
import appli.model.domaine.stock.persistant.TravauxChantierPersistant;
import appli.model.domaine.stock.persistant.TravauxPersistant;
import appli.model.domaine.stock.service.IChargeDiversService;
import appli.model.domaine.stock.service.IMouvementService;
import appli.model.domaine.stock.service.ITravauxService;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.common.constante.ActionConstante;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.StringUtil;

@WorkController(nameSpace="stock", bean=TravauxBean.class, jspRootPath="/domaine/stock/")
public class TravauxAction extends ActionBase {
		@Inject
		private ITravauxService travauxService;
		@Inject
		private IChargeDiversService chargeDiversService;
		@Inject
		private IMouvementService mouvementService;
		@Inject
		private ISalariePaieService salariePaieService;
		
		public void work_init(ActionUtil httpUtil) {
			httpUtil.setMenuAttribute("IS_SUB_ADD","stock.travaux.work_init_create");
			boolean isBack = (httpUtil.getRequestAttribute("bck") != null || httpUtil.getParameter("bck") != null);
			TravauxBean travauxBean = (TravauxBean) httpUtil.getViewBean();
			if(travauxBean == null && httpUtil.getMenuAttribute("IS_TRV_MNU") != null 
					&& isBack) {
				travauxBean = (TravauxBean) httpUtil.getMenuAttribute("IS_TRV_MNU");
			}
			
			if(travauxBean != null && travauxBean.getEmploye_array() != null){
				String employeStr = "";
    			for (String employeId :  travauxBean.getEmploye_array()) {
    				if(StringUtil.isNotEmpty(employeId)){
    					employeStr = employeStr + employeId + ";";
    				}
    			}
    			travauxBean.setEmployes(employeStr);
    		}
    		
			httpUtil.setRequestAttribute("listeChantier", travauxService.findAll(TravauxChantierPersistant.class, Order.asc("libelle")));
			httpUtil.setRequestAttribute("listeEmploye", travauxService.findAll(EmployePersistant.class, Order.asc("nom")));
		}
		
		@Override
		public void work_merge(ActionUtil httpUtil) {
			super.work_merge(httpUtil);
			managePieceJointe(httpUtil, ((TravauxBean)httpUtil.getViewBean()).getId(), "travaux");
		}
		
		@Override
		public void work_delete(ActionUtil httpUtil) {
			super.work_delete(httpUtil);
			manageDeleteImage(httpUtil.getWorkIdLong(), "travaux");
		}
		
		public void downloadPieceJointe(ActionUtil httpUtil) {
			httpUtil.manageInputFileView("travaux");
		}
		
		private void find_annexesSalaire(ActionUtil httpUtil) {
			RequestTableBean cplxTable = getTableBean(httpUtil, "list_salaire");
			Map<String, Object> formCriterion = cplxTable.getFormBean().getFormCriterion();
			formCriterion.put("travauxId", httpUtil.getMenuAttribute("travauxId"));
			
			List<SalariePaieBean> listData = (List<SalariePaieBean>) salariePaieService.findByCriteriaByQueryId(cplxTable, "salaire_find");
		   	httpUtil.setRequestAttribute("list_salaire", listData);
		}
		private void find_annexesDepense(ActionUtil httpUtil) {
			RequestTableBean cplxTable = getTableBean(httpUtil, "list_chargeDivers");
			Map<String, Object> formCriterion = cplxTable.getFormBean().getFormCriterion();
			formCriterion.put("sens", "D");
			formCriterion.put("travauxId", httpUtil.getMenuAttribute("travauxId"));
			
			List<ChargeDiversPersistant> listData = (List<ChargeDiversPersistant>) chargeDiversService.findByCriteriaByQueryId(cplxTable, "chargeDivers_find");
			chargeDiversService.refreshEntities(listData);
			
			Map<Long, String> mapDateGroupement = new HashMap<>();
			for(ChargeDiversPersistant mouvementPersistant : listData) {
				if(mouvementPersistant.getCharge_group_id() != null){
		   			ChargeDiversBean mvmGroupant = chargeDiversService.findById(mouvementPersistant.getCharge_group_id());
		   			if(mvmGroupant == null) {
		   				continue;
		   			}
					mapDateGroupement.put(mvmGroupant.getId(), DateUtil.dateToString(mvmGroupant.getDate_mouvement()));
		   		}
			}
			// Calcul du total montants sans pagination
		   	BigDecimal totalTtcAll = null;
		   	List<ChargeDiversPersistant> listDataAll = (List<ChargeDiversPersistant>) chargeDiversService.findByCriteriaByQueryId(cplxTable, "chargeDivers_find", false);
			//
		   	if(listDataAll.size() > 0) {
			   	for (ChargeDiversPersistant mvmStockViewP : listDataAll) {
				   	totalTtcAll = BigDecimalUtil.add(totalTtcAll, mvmStockViewP.getMontant());
			   	}
			}
			
		   	httpUtil.setRequestAttribute("mapDateGroupement", mapDateGroupement);
		   	httpUtil.setRequestAttribute("totalTtc", totalTtcAll);
			httpUtil.setRequestAttribute("list_chargeDivers", listData);
		}
		private void find_annexesMvm(ActionUtil httpUtil, String type) {
			httpUtil.setRequestAttribute("listEmplacement", mouvementService.findAll(EmplacementPersistant.class, Order.asc("titre")));
			// Ajouter le paramétre dans la requête
			RequestTableBean cplxTable = getTableBean(httpUtil, "list_mouvement"+type);
			Map<String, Object> formCriterion = cplxTable.getFormBean().getFormCriterion();
			formCriterion.put("type", type.toLowerCase());
			formCriterion.put("travauxId", httpUtil.getMenuAttribute("travauxId"));
			
			List<MouvementPersistant> listData = (List<MouvementPersistant>) mouvementService.findByCriteriaByQueryId(cplxTable, "mouvement_find");
			
			//
			if("A".equals(type)) {
				Map<Long, String> mapDateGroupement = new HashMap<>();
				for(MouvementPersistant mouvementPersistant : listData) {
					if(mouvementPersistant.getMouvement_group_id() != null){
			   			MouvementBean mvmGroupant = mouvementService.findById(mouvementPersistant.getMouvement_group_id());
						mapDateGroupement.put(mvmGroupant.getId(), DateUtil.dateToString(mvmGroupant.getDate_mouvement()));
			   		}
				}
				// Calcul du total montants sans pagination
			   	BigDecimal totalHtAll = null, totalTtcAll = null, totalRemiseAll = null, totalTva = null;
			   	
			   	List<MouvementPersistant> listDataAll = (List<MouvementPersistant>) mouvementService.findByCriteriaByQueryId(cplxTable, "mouvement_find", false);
				//
			   	
			   	if(listDataAll.size() > 0) {
				   	for (MouvementPersistant mvmStockViewP : listDataAll) {
			   			totalHtAll = BigDecimalUtil.add(totalHtAll, mvmStockViewP.getMontant_ht());
				   		totalTtcAll = BigDecimalUtil.add(totalTtcAll, mvmStockViewP.getMontant_ttc());
				   		totalRemiseAll = BigDecimalUtil.add(totalRemiseAll, mvmStockViewP.getMontant_ttc_rem());
				   		totalTva = BigDecimalUtil.add(totalTva, mvmStockViewP.getMontant_tva());
				   	}
				}
			   	httpUtil.setRequestAttribute("totalHt", totalHtAll);
			   	httpUtil.setRequestAttribute("totalTtc", totalTtcAll);
			   	httpUtil.setRequestAttribute("totalRemise", totalRemiseAll);
			   	httpUtil.setRequestAttribute("totalTva", totalTva);
			   	
			   	httpUtil.setRequestAttribute("mapDateGroupement", mapDateGroupement);
			}
			httpUtil.setRequestAttribute("list_mouvement"+type, listData);
		}
		
		@Override
		public void work_find(ActionUtil httpUtil) {
			httpUtil.removeMenuAttribute("IS_TRV_MNU");
			
			RequestTableBean cplxTable = getTableBean(httpUtil, "list_travaux");
			List<TravauxPersistant> listData = (List<TravauxPersistant>) travauxService.findByCriteriaByQueryId(cplxTable, "travaux_find");
			travauxService.refreshEntities(listData);
			
			httpUtil.setRequestAttribute("list_travaux", listData);
			
			httpUtil.setDynamicUrl("/domaine/stock/travaux_list.jsp");
		}
		
		public void work_post(ActionUtil httpUtil){
			TravauxBean travauxBean = (TravauxBean) httpUtil.getViewBean();
			String action = httpUtil.getAction();
			boolean isBack = (httpUtil.getRequestAttribute("bck") != null || httpUtil.getParameter("bck") != null);
			
			if(httpUtil.getMenuAttribute("IS_TRV_MNU") != null && isBack) {
				travauxBean = (TravauxBean) httpUtil.getMenuAttribute("IS_TRV_MNU");
				httpUtil.setViewBean(travauxBean);
				httpUtil.setRequestAttribute("travaux", travauxBean);
			}
			
			if(travauxBean != null && travauxBean.getId() != null) {
				httpUtil.setMenuAttribute("IS_TRV_MNU", travauxBean);
				httpUtil.setMenuAttribute("travauxId", travauxBean.getId());
			}
			
			if(action.equals(ActionConstante.EDIT) || action.equals(ActionConstante.INIT_UPDATE)) {
				String[] employeArray = StringUtil.getArrayFromStringDelim(travauxBean.getEmployes(), ";");
				travauxBean.setEmploye_array(employeArray);
				
				//
				find_annexesDepense(httpUtil);
				find_annexesMvm(httpUtil, "A");
				find_annexesMvm(httpUtil, "C");
				find_annexesSalaire(httpUtil);
			}
			
			manageDataForm(httpUtil, "TRAVAUX");
		}
}
