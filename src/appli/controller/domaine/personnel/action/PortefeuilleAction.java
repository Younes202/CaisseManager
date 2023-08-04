package appli.controller.domaine.personnel.action;

import java.util.List;

import javax.inject.Inject;

import appli.controller.domaine.stock.bean.ArticleBean;
import appli.model.domaine.administration.service.IClientService;
import appli.model.domaine.fidelite.dao.IPortefeuille2Service;
import appli.model.domaine.fidelite.persistant.CarteFideliteClientPersistant;
import appli.model.domaine.fidelite.service.ICarteFideliteClientService;
import appli.model.domaine.fidelite.service.ICarteFideliteService;
import appli.model.domaine.personnel.persistant.ClientPersistant;
import appli.model.domaine.personnel.persistant.ClientPortefeuilleMvmPersistant;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;

@WorkController(nameSpace = "fidelite", bean = ArticleBean.class, jspRootPath = "/domaine/fidelite/")
public class PortefeuilleAction extends ActionBase {
	@Inject
	private ICarteFideliteService carteFideliteService;
	@Inject
	private IClientService clientService;
	@Inject
	private IPortefeuille2Service portefeuille2Service;
	@Inject
	private ICarteFideliteClientService carteFideliteClientService;
	
	public void work_init(ActionUtil httpUtil) {
		httpUtil.setRequestAttribute("liste_client", clientService.getClientsActifs());
		httpUtil.setUserAttribute("MNU_FIDELITE", "PORTEFEUILLE");
	}
	
	@Override
	public void work_edit(ActionUtil httpUtil) {
		if(httpUtil.getWorkId() != null) {
			
			CarteFideliteClientPersistant carteCliActive = carteFideliteClientService.getCarteClientActive(httpUtil.getWorkIdLong());
			if(carteCliActive != null) {
				httpUtil.setMenuAttribute("carteClientId", httpUtil.getWorkIdLong());
			} else {
				httpUtil.removeMenuAttribute("carteClientId");
			}
			
			httpUtil.setMenuAttribute("clientId", httpUtil.getWorkIdLong());	
		}
		//
		find_recharge(httpUtil);
	}
	
	@Override
	public void work_find(ActionUtil httpUtil) {
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_portefeuille");
		//
		List<ClientPersistant> listData = clientService.findByCriteriaByQueryId(cplxTable, "portefeuille_find");
	   	httpUtil.setRequestAttribute("list_Clientportefeuille", listData);
	   	
		httpUtil.setDynamicUrl("/domaine/fidelite/portefeuille_list.jsp");
	}
	
	public void find_utilisation(ActionUtil httpUtil) {
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_portefeuilleMvm_utilisation");
		
		Long clientId = (Long)httpUtil.getMenuAttribute("clientId");
		Long societeLivrId = (Long)httpUtil.getMenuAttribute("societeLivrId");
		List<ClientPortefeuilleMvmPersistant> listCaisseMouvement = null;
		//
		if(clientId != null){
			cplxTable.getFormBean().getFormCriterion().put("clientId", clientId);
			listCaisseMouvement = (List<ClientPortefeuilleMvmPersistant>) carteFideliteService.findByCriteriaByQueryId(cplxTable, "portefeuille_mvm_cli_find");
		} else if(societeLivrId != null){
			cplxTable.getFormBean().getFormCriterion().put("societeLivrId", societeLivrId);
			listCaisseMouvement = (List<ClientPortefeuilleMvmPersistant>) carteFideliteService.findByCriteriaByQueryId(cplxTable, "portefeuille_mvm_soc_find");
		}
		cplxTable.getFormBean().getFormCriterion().put("sens", "D");
		
		httpUtil.setRequestAttribute("list_portefeuilleMvm", listCaisseMouvement);

		httpUtil.setDynamicUrl("/domaine/fidelite/portefeuille_utilisation_list.jsp");
	}
	
	public void find_recharge(ActionUtil httpUtil) {
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_portefeuilleMvm_recharge");
		
		Long clientId = (Long)httpUtil.getMenuAttribute("clientId");
		cplxTable.getFormBean().getFormCriterion().put("clientId", clientId);
		cplxTable.getFormBean().getFormCriterion().put("sens", "C");
		
		List<ClientPortefeuilleMvmPersistant> listCaisseMouvement = (List<ClientPortefeuilleMvmPersistant>) carteFideliteService.findByCriteriaByQueryId(cplxTable, "portefeuille_mvm_cli_find");
		httpUtil.setRequestAttribute("list_portefeuilleMvm", listCaisseMouvement);

		httpUtil.setDynamicUrl("/domaine/fidelite/portefeuille_recharge_list.jsp");
	}
	
	public void init_recharge(ActionUtil httpUtil) {
		httpUtil.setRequestAttribute("list_mode_paiement", new String[][]{{"CARTE", "CARTE"}, {"CHEQUE", "CHEQUE"}, {"CHEQUE. DEJ", "CHEQUE. DEJ"}, {"ESPECES", "ESPECES"}});
		//
		httpUtil.setDynamicUrl("/domaine/fidelite/portefeuille_recharge_edit.jsp");
	}
	
	public void delete_recharge(ActionUtil httpUtil) {
		portefeuille2Service.deleteRecharge(httpUtil.getWorkIdLong());
		//
		find_recharge(httpUtil);
	}
}
