package appli.controller.domaine.caisse.action;

import appli.controller.domaine.caisse.bean.CaisseJourneeBean;
import appli.controller.domaine.util_erp.ContextAppli.STATUT_JOURNEE;
import appli.model.domaine.personnel.persistant.EmployePersistant;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;

@WorkController(nameSpace="caisse", bean=CaisseJourneeBean.class, jspRootPath="/domaine/caisse/")
public class CaisseJourneeAction extends ActionBase {
	
	public void work_init(ActionUtil httpUtil){		
		Long caisseId = (Long) httpUtil.getMenuAttribute("caisseId");
		if(!httpUtil.isEditionPage()){
			RequestTableBean cplxTable = getTableBean(httpUtil, "list_caisseJournee");
			cplxTable.getFormBean().getFormCriterion().put("caisse_id", caisseId);
		}
		
		if(httpUtil.isEditionPage()){
			Long caisseJourneeId = httpUtil.getWorkIdLong();
			if(caisseJourneeId != null){
				httpUtil.setMenuAttribute("caisseJourneeId", caisseJourneeId);
			} else{
				caisseJourneeId = (Long)httpUtil.getMenuAttribute("caisseJourneeId");
			}
			if(!httpUtil.isCreateAction()){
				// Gérer le retour sur cet onglet
				caisseJourneeId = (Long)httpUtil.getMenuAttribute("caisseJourneeId");
				if(caisseJourneeId != null && !httpUtil.isCrudOperation()){
					loadBean(httpUtil, caisseJourneeId);
				}
			} else{
				httpUtil.removeMenuAttribute("caisseJourneeId");
			}
		} else{
			String [][]statCaisse ={{STATUT_JOURNEE.OUVERTE.getStatut(), STATUT_JOURNEE.OUVERTE.getLibelle()},
//					{STATUT_JOURNEE.EN_COURS_CLOTURE.getStatut(), STATUT_JOURNEE.EN_COURS_CLOTURE.getLibelle()},
					{STATUT_JOURNEE.CLOTURE.getStatut(), STATUT_JOURNEE.CLOTURE.getLibelle()}};
			httpUtil.setRequestAttribute("statCaisse", statCaisse);
		}
	}
	
	@Override
	public void work_edit(ActionUtil httpUtil){
		CaisseJourneeBean viewBean = (CaisseJourneeBean) httpUtil.getViewBean();
//		if(viewBean.getStatut_caisse().equals(ContextRestaurant.STATUT_JOURNEE.CLOTURE.getStatut())){
			EmployePersistant employeP = viewBean.getOpc_user().getOpc_employe();
			if(employeP != null){
				String employe = employeP.getNom()+' '+ employeP.getPrenom();
				httpUtil.setRequestAttribute("employe", employe);
			}
//			httpUtil.setDynamicUrl("/domaine/caisse/caisseJournee_edit_popup.jsp");
//		} else {
			httpUtil.setDynamicUrl("/domaine/caisse/caisseJournee_edit.jsp");
//		}
	}
}
