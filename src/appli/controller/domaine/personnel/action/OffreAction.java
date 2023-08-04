package appli.controller.domaine.personnel.action;

import java.util.List;

import javax.inject.Inject;

import appli.controller.domaine.personnel.bean.OffreBean;
import appli.model.domaine.personnel.service.IOffreService;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;

@WorkController(nameSpace = "pers", bean = OffreBean.class, jspRootPath = "/domaine/personnel/")
public class OffreAction extends ActionBase {
	
	@Inject
	private IOffreService offreService;
	
	public void work_init(ActionUtil httpUtil) {
		String[][] natureArray = {{"A", "GLOBALE"}, {"E", "EMPLOYÉ"}, {"C", "CLIENT"}};
		httpUtil.setRequestAttribute("types_offre", natureArray);
		
		if(httpUtil.isEditionPage()){
			Long offreId = httpUtil.getWorkIdLong();
			if(offreId != null){
				httpUtil.setMenuAttribute("offreId", offreId);
			} else{
				offreId = (Long)httpUtil.getMenuAttribute("offreId");
			}
			if(!httpUtil.isCreateAction()){
				// Gérer le retour sur cet onglet
				offreId = (Long)httpUtil.getMenuAttribute("offreId");
				if(offreId != null && !httpUtil.isCrudOperation()){
					loadBean(httpUtil, offreId);
				}
			} else{
				httpUtil.removeMenuAttribute("offreId");
			}
		}
		httpUtil.setRequestAttribute("typeOffreArray", new String[][]{{"P", "Prix d'achat"}, {"R", "Réduction/Majoration"}});
	}
	
	@Override
	public void work_find(ActionUtil httpUtil) {
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_offre");
		
		String req = "from OffrePersistant offre "
			+ "where offre.destination!='S' "
			+ getFilterStateRequest(httpUtil, "is_disable")
			+ "order by offre.destination, offre.ordre asc, offre.date_debut desc, offre.id desc";
		
		List<ArticlePersistant> listData = (List<ArticlePersistant>) offreService.findByCriteria(cplxTable, req);
		httpUtil.setRequestAttribute("list_offre", listData);
		
		httpUtil.setDynamicUrl("/domaine/personnel/offre_list.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void desactiver(ActionUtil httpUtil) {
		offreService.activerDesactiverElement(httpUtil.getWorkIdLong()); 
		work_find(httpUtil);
	}
}
