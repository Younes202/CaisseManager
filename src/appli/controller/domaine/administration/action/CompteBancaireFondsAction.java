package appli.controller.domaine.administration.action;

import javax.inject.Inject;

import org.hibernate.criterion.Order;

import appli.controller.domaine.administration.bean.CompteBancaireFondsBean;
import appli.model.domaine.administration.service.ICompteBancaireFondsService;
import appli.model.domaine.administration.service.ICompteBancaireService;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.beanContext.VillePersistant;
import framework.model.common.service.MessageService;

@WorkController(nameSpace = "admin", bean = CompteBancaireFondsBean.class, jspRootPath = "/domaine/administration/")
public class CompteBancaireFondsAction extends ActionBase {
	@Inject
	private ICompteBancaireFondsService compteService;
	@Inject
	private ICompteBancaireService compteBancaireService;
	
	/**
	 * @param httpUtil
	 */
	public void work_init(ActionUtil httpUtil){
		MessageService.getGlobalMap().put("NO_ETS", true);
		httpUtil.setRequestAttribute("listVille", compteService.getListData(VillePersistant.class, "opc_region.libelle, libelle"));
		MessageService.getGlobalMap().remove("NO_ETS");
		
		httpUtil.setRequestAttribute("listeCompteBancaire", compteBancaireService.findAll(Order.asc("libelle")));
	}
}
