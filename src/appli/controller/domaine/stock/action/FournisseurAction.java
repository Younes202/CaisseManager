package appli.controller.domaine.stock.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import appli.controller.domaine.stock.bean.FournisseurBean;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_MOUVEMENT_ENUM;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.stock.persistant.FournisseurContactPersistant;
import appli.model.domaine.stock.persistant.FournisseurPersistant;
import appli.model.domaine.stock.persistant.MouvementPersistant;
import appli.model.domaine.stock.service.IFamilleService;
import appli.model.domaine.stock.service.IFournisseurService;
import appli.model.domaine.stock.service.IMouvementService;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.beanContext.VillePersistant;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;

@WorkController(nameSpace="stock", bean=FournisseurBean.class, jspRootPath="/domaine/stock/")
public class FournisseurAction extends ActionBase {
		@Inject
		private IFournisseurService fournisseurService;
		@Inject
		private IFamilleService familleService;
		@Inject
		private IMouvementService mouvementService;
		
		public void work_init(ActionUtil httpUtil) {
		     httpUtil.setMenuAttribute("IS_SUB_ADD","stock.fournisseur.work_init_create");
			
		     if(httpUtil.isEditionPage()) {
				MessageService.getGlobalMap().put("NO_ETS", true);
				httpUtil.setRequestAttribute("listVille", fournisseurService.getListData(VillePersistant.class, "opc_region.libelle, libelle"));
				MessageService.getGlobalMap().remove("NO_ETS");
			}
			
			List listFamille = familleService.getListeFamille("FO", true, false);
			httpUtil.setRequestAttribute("listeFaimlle", listFamille);
			
			String tp = httpUtil.getParameter("tp"); 
			if(tp != null){
			    httpUtil.setMenuAttribute("tp", tp);
				httpUtil.setMenuAttribute("IS_MNU", true);
			}
			
			Long fournisseurId = httpUtil.getWorkIdLong();
			if(fournisseurId != null){
				httpUtil.setMenuAttribute("fournisseurId", fournisseurId);
			} else{
				fournisseurId = (Long)httpUtil.getMenuAttribute("fournisseurId");
			}
			if(!httpUtil.isCreateAction()){
				// Gérer le retour sur cet onglet
				fournisseurId = (Long)httpUtil.getMenuAttribute("fournisseurId");
				if(fournisseurId != null && !httpUtil.isCrudOperation()){
					loadBean(httpUtil, fournisseurId);
				}
			} else{
				httpUtil.removeMenuAttribute("fournisseurId");
			}
		}
		
		@Override
		public void work_find(ActionUtil httpUtil) {
			RequestTableBean cplxTable = getTableBean(httpUtil, "list_fournisseur");
			String req = "from FournisseurPersistant fournisseur where 1=1 "
					+ getFilterStateRequest(httpUtil, "is_disable")
					+ "order by fournisseur.opc_famille.code, fournisseur.opc_famille.libelle, fournisseur.code, fournisseur.libelle";
			
			List<FournisseurPersistant> listData = (List<FournisseurPersistant>) fournisseurService.findByCriteria(cplxTable, req);
			
			for (FournisseurPersistant fournisseurPersistant : listData) {
				List<FamillePersistant> familleStr = familleService.getFamilleParent("FO", fournisseurPersistant.getOpc_famille().getId());
				fournisseurPersistant.setFamilleStr(familleStr);
			}
			
			httpUtil.setRequestAttribute("list_fournisseur", listData);
			
			httpUtil.setDynamicUrl("/domaine/stock/fournisseur_list.jsp");
		}
		
		@Override
		public void work_merge(ActionUtil httpUtil) {
			setDataContact(httpUtil);
			
			super.work_merge(httpUtil);
			
			if("stock.fournisseur.work_init_create".equals(httpUtil.getMenuAttribute("IS_SUB_ADD"))) {
				httpUtil.setDynamicUrl(""+httpUtil.getMenuAttribute("IS_SUB_ADD"));
			}
		}
		
		/**
		 * @param httpUtil
		 */
		public void findMouvement(ActionUtil httpUtil){
			httpUtil.setMenuAttribute("isTabMnu", true);
			
			String[][] typeMvmArray = new String[][]{{
					TYPE_MOUVEMENT_ENUM.a.toString(), TYPE_MOUVEMENT_ENUM.a.getLibelle()}, 
					{TYPE_MOUVEMENT_ENUM.av.toString(), TYPE_MOUVEMENT_ENUM.av.getLibelle()},
					{TYPE_MOUVEMENT_ENUM.c.toString(), TYPE_MOUVEMENT_ENUM.c.getLibelle()},
					{TYPE_MOUVEMENT_ENUM.cm.toString(), TYPE_MOUVEMENT_ENUM.cm.getLibelle()},
					{TYPE_MOUVEMENT_ENUM.dv.toString(), TYPE_MOUVEMENT_ENUM.dv.getLibelle()},
					{TYPE_MOUVEMENT_ENUM.i.toString(), TYPE_MOUVEMENT_ENUM.i.getLibelle()},
					{TYPE_MOUVEMENT_ENUM.p.toString(), TYPE_MOUVEMENT_ENUM.p.getLibelle()},
					{TYPE_MOUVEMENT_ENUM.rt.toString(), TYPE_MOUVEMENT_ENUM.rt.getLibelle()},
					{TYPE_MOUVEMENT_ENUM.t.toString(), TYPE_MOUVEMENT_ENUM.t.getLibelle()},
					{TYPE_MOUVEMENT_ENUM.tr.toString(), TYPE_MOUVEMENT_ENUM.tr.getLibelle()},
					{TYPE_MOUVEMENT_ENUM.v.toString(), TYPE_MOUVEMENT_ENUM.v.getLibelle()},
					{TYPE_MOUVEMENT_ENUM.vc.toString(), TYPE_MOUVEMENT_ENUM.vc.getLibelle()}};
			httpUtil.setRequestAttribute("typeMvmArray", typeMvmArray);
			
			RequestTableBean cplxTable = getTableBean(httpUtil, "list_fournisseur_mvm");
			cplxTable.getFormBean().getFormCriterion().put("fournisseurId", httpUtil.getMenuAttribute("fournisseurId"));
			
			List<MouvementPersistant> listData = (List<MouvementPersistant>) mouvementService.findByCriteriaByQueryId(cplxTable, "fournisseur_mvm_find");
			List<MouvementPersistant> listDataAll = (List<MouvementPersistant>) mouvementService.findByCriteriaByQueryId(cplxTable, "fournisseur_mvm_find", false);
			
			BigDecimal mttTva = null, mttHt = null, mttTtc = null;
			for (MouvementPersistant mvmVP : listDataAll) {
				mttHt = BigDecimalUtil.add(mttHt, mvmVP.getMontant_ht());
				mttTtc = BigDecimalUtil.add(mttTtc, mvmVP.getMontant_ttc());
				mttTva = BigDecimalUtil.add(mttTva, mvmVP.getMontant_tva());
			}			
			
			httpUtil.setRequestAttribute("mttHt", mttHt);
			httpUtil.setRequestAttribute("mttTtc", mttTtc);
			httpUtil.setRequestAttribute("mttTva", mttTva);
			
			httpUtil.setRequestAttribute("listMvmFournisseur", listData);
			
			httpUtil.setDynamicUrl("/domaine/stock/fournisseur_mvm_list.jsp");
		}
		
		/**
		 * @param httpUtil
		 */
		@SuppressWarnings({ "unchecked" })
		private void setDataContact(ActionUtil httpUtil){
			FournisseurBean fournisseurBean = (FournisseurBean)httpUtil.getViewBean();
			fournisseurBean.setId(httpUtil.getWorkIdLong());

			List<FournisseurContactPersistant> listFournContact = (List<FournisseurContactPersistant>) httpUtil.buildListBeanFromMap("contact", FournisseurContactPersistant.class, "eaiid", "contact", "fonction", "coord");
			
			List<FournisseurContactPersistant> listContact = new ArrayList<>();
			if(fournisseurBean.getId() != null){
				FournisseurBean fournBean = fournisseurService.findById(fournisseurBean.getId());
				listContact = fournBean.getList_contact();
				listContact.clear();
			}
			listContact.addAll(listFournContact);
			fournisseurBean.setList_contact(listContact);
		}
		
		/***
		 * @param httpUtil
		 */
		public void facture_edit(ActionUtil httpUtil){
			httpUtil.setDynamicUrl("/domaine/stock/facture_edition.jsp");
		}
		
		/**
		 * @param httpUtil
		 */
		public void desactiver(ActionUtil httpUtil) {
			fournisseurService.activerDesactiverElement(httpUtil.getWorkIdLong());
			work_find(httpUtil);
		}
		
		public void genererCode(ActionUtil httpUtil) {
			String code = fournisseurService.genererCode();
			httpUtil.writeResponse(code);
		}
		
		/*----------------------------------------------------------------------*/
		public void init_situation(ActionUtil httpUtil) {
			Long fournId = (Long) httpUtil.getMenuAttribute("fournisseurId");
			RequestTableBean cplxTable = getTableBean(httpUtil, "list_situation");

			if(fournId != null) {
				cplxTable.getFormBean().getFormCriterion().put("fournisseurId", fournId);
			}
			List<FournisseurPersistant> listData = (List<FournisseurPersistant>) fournisseurService.findByCriteriaByQueryId(cplxTable, "fournisseur_find");
			
			BigDecimal total_mtt = null, 
					total_paye = null, 
					total_avoir = null, 
					total_restant = null;
			
			for (FournisseurPersistant fournP : listData) {
				if(fournId == null || fournId.equals(fournP.getId())) {
					List<FamillePersistant> familleStr = familleService.getFamilleParent("FO", fournP.getOpc_famille().getId());
					fournP.setFamilleStr(familleStr);
					// Alimenter l'état
					fournisseurService.affecterEtatFournisseur(fournP);
					//
					total_mtt = BigDecimalUtil.add(total_mtt, fournP.getMtt_total());
			   		total_paye = BigDecimalUtil.add(total_paye, fournP.getMtt_paye());
			   		total_avoir = BigDecimalUtil.add(total_avoir, fournP.getMtt_avoir());
			   		total_restant = BigDecimalUtil.add(total_restant, fournP.getMtt_non_paye());
				}
			}
			
			//			
			httpUtil.setRequestAttribute("total_mtt", total_mtt);
		   	httpUtil.setRequestAttribute("total_paye", total_paye);
		   	httpUtil.setRequestAttribute("total_avoir", total_avoir);
		   	httpUtil.setRequestAttribute("total_restant", total_restant);
			
			httpUtil.setRequestAttribute("list_situation", listData);
//			httpUtil.setMenuAttribute("IS_MNU", true);
			
			httpUtil.setDynamicUrl("/domaine/stock/situation_fournisseur.jsp");
		}
		
		/**
		 * @param httpUtil
		 */
		public void situation_detail(ActionUtil httpUtil){
			Long fournId = httpUtil.getLongParameter("fo");
			if(fournId != null) {
				httpUtil.setMenuAttribute("fo", fournId);
			}
			
			RequestTableBean cplxTable = getTableBean(httpUtil, "list_mouvement");
			String req = "from MouvementPersistant mouvement where mouvement.opc_fournisseur.id='{currFournId}' "
					+ "order by mouvement.date_mouvement desc";
			cplxTable.getFormBean().getFormCriterion().put("currFournId", httpUtil.getMenuAttribute("fo"));
			List<MouvementPersistant> list_mouvement = (List<MouvementPersistant>) fournisseurService.findByCriteria(cplxTable, req);
			
			httpUtil.setRequestAttribute("list_mouvement", list_mouvement);
			
			httpUtil.setDynamicUrl("/domaine/stock/situation_fournisseur_detail.jsp");
		}
		/*------------------------------------------------------------------------------*/
		
		/**
		 * @param httpUtil
		 */
//		public void editTrSituation(ActionUtil httpUtil){
//			Long fournId = httpUtil.getLongParameter("art");
//			BigDecimal ttlAchat = null, ttlAvoir = null, ttlPaiement = null;
//			for (MouvementPersistant mouvementP : listMvm) {
//				if("a".equals(mouvementP.getType_mvmnt())) {
//					ttlAchat = BigDecimalUtil.add(ttlAchat, mouvementP.getMontant_ttc());	
//				} else {
//					ttlAvoir = BigDecimalUtil.add(ttlAvoir, mouvementP.getMontant_ttc());					
//				}
//			}
//
//			for (PaiementPersistant paiementP : listPaiement) {
//				ttlPaiement = BigDecimalUtil.add(ttlPaiement, paiementP.getMontant());
//			}
//
//			httpUtil.setRequestAttribute("ttlAvoir", ttlAvoir);
//			httpUtil.setRequestAttribute("ttlAchat", ttlAchat);
//			httpUtil.setRequestAttribute("ttlPaiement", ttlPaiement);
//			
//			httpUtil.setRequestAttribute("listMvm", listMvm);
//			
//			
//			httpUtil.setDynamicUrl("/domaine/stock/situation_fournisseur_tr_consult.jsp");
//		}
		
		/**
		 * @param httpUtil
		 */
		public void work_post(ActionUtil httpUtil){
			manageDataForm(httpUtil, "FOURNISSEUR");
			
			// --------------------------- DYN form --------------------------
			httpUtil.setRequestAttribute("listDataValueForm", fournisseurService.loadDataForm(null, "FOURNISSEUR"));
			// ---------------------------------------------------------------
		}
}
