package appli.controller.domaine.stock.action;

import java.util.List;

import javax.inject.Inject;

import appli.controller.domaine.personnel.bean.ComposantClientPrixBean;
import appli.model.domaine.personnel.persistant.ClientPersistant;
import appli.model.domaine.personnel.service.IComposantClientPrixService;
import appli.model.domaine.stock.persistant.ArticleClientPrixPersistant;
import appli.model.domaine.stock.service.IArticleService;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;

@WorkController(nameSpace = "pers", bean = ComposantClientPrixBean.class, jspRootPath = "/domaine/personnel/")
public class ComposantClientPrixAction extends ActionBase {
	@Inject
	private IComposantClientPrixService composantClientPrixService;
	@Inject
	private IArticleService articleService;
	
	public void work_init(ActionUtil httpUtil) {
		if(httpUtil.getViewBean() != null){
			Long clientId = (Long)httpUtil.getMenuAttribute("clientId");
			((ArticleClientPrixPersistant)httpUtil.getViewBean()).setOpc_client((ClientPersistant) composantClientPrixService.findById(ClientPersistant.class, clientId));
		}
		
		httpUtil.setRequestAttribute("listArticle", articleService.getListArticleStock(false));
	}
	
	@Override
	public void work_find(ActionUtil httpUtil) {
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_prix_composant_find");
		
		Long clientId = (Long)httpUtil.getMenuAttribute("clientId");
		cplxTable.getFormBean().getFormCriterion().put("clientId", clientId);
		
		List<ArticleClientPrixPersistant> listData = (List<ArticleClientPrixPersistant>) composantClientPrixService.findByCriteriaByQueryId(cplxTable, "prix_composant_find");
		composantClientPrixService.refreshEntities(listData);
		
	   	httpUtil.setRequestAttribute("list_composantClientPrix", listData);
		httpUtil.setDynamicUrl("/domaine/personnel/composantClientPrix_list.jsp");
	}
}
