package appli.controller.domaine.caisse_restau.action;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.criterion.Order;

import appli.controller.domaine.caisse_restau.bean.ListChoixBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.caisse_restau.service.IListChoixService;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.service.IArticleService;
import appli.model.domaine.stock.service.IFamilleService;
import appli.model.domaine.vente.persistant.ListChoixDetailPersistant;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.common.constante.ActionConstante;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;

@WorkController(nameSpace="caisse", bean=ListChoixBean.class, jspRootPath="/domaine/caisse_restau/normal/")
public class ListChoixAction extends ActionBase {
	@Inject
	IListChoixService listChoixService;
	@Inject
	private IArticleService articleService;
	@Inject
	private IFamilleService familleService;
	
	public void work_init(ActionUtil httpUtil) {
		List<ArticlePersistant> listArticle = articleService.getListArticleNonStock(true);
		httpUtil.setRequestAttribute("listArticle", listArticle);
		
		List listFamille = familleService.getListeFamille("CU", true, false);
		httpUtil.setRequestAttribute("listFamille", listFamille);
		
		List<ListChoixBean> listChoix = listChoixService.findAll(Order.asc("code"), Order.asc("libelle"));
		httpUtil.setRequestAttribute("listChoixAll", listChoix);
		
		String[][] typeAjoutArray = {{"OU", "Ou"}, {"ET", "Et"}};
		httpUtil.setRequestAttribute("typeAjoutArray", typeAjoutArray);
		
		if(ActionConstante.MERGE.equals(httpUtil.getAction())){
			setDataList(httpUtil);
		}
		
		if(ContextAppli.getAbonementBean().isOptPlusEtsCentrale() 
				&& StringUtil.isTrue(StrimUtil.getGlobalConfigPropertie("CTRL_CENTRALE"))) {
			httpUtil.setRequestAttribute("isEditable", false);
		} else {
			httpUtil.setRequestAttribute("isEditable", true);
		}
	}
	
	/**
	 * @param httpUtil
	 */
	@SuppressWarnings({ "unchecked" })
	private void setDataList(ActionUtil httpUtil) {
		ListChoixBean listChoixBean = (ListChoixBean) httpUtil.getViewBean();
		listChoixBean.setId(httpUtil.getWorkIdLong());
		
		List<ListChoixDetailPersistant> list_choix_detail = (List<ListChoixDetailPersistant>) httpUtil.buildListBeanFromMap("quantite",
				ListChoixDetailPersistant.class, "eaiid", "opc_article.id", "opc_famille.id", "opc_list_choix.id", "quantite", "nombre");

		List<ListChoixDetailPersistant> list_detail = new ArrayList<>();
		if (listChoixBean.getId() != null) {
			ListChoixBean listChoixBeanDB = listChoixService.findById(listChoixBean.getId());
			list_detail = listChoixBeanDB.getList_choix_detail();
			list_detail.clear();
		}
		list_detail.addAll(list_choix_detail);
		listChoixBean.setList_choix_detail(list_detail);
	}
	
	/**
	 * @param httpUtil
	 */
	public void desactiver(ActionUtil httpUtil) {
		listChoixService.activerDesactiverElement(httpUtil.getWorkIdLong());
		work_find(httpUtil);
	}
	
	/**
	 * @param httpUtil
	 */
	public void work_post(ActionUtil httpUtil) {
	}
	
	public void genererCode(ActionUtil httpUtil) {
		String code = listChoixService.genererCode();
		httpUtil.writeResponse(code);
	}
}
