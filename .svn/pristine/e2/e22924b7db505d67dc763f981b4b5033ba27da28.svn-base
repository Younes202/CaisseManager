package appli.controller.domaine.stock.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.criterion.Order;

import appli.controller.domaine.stock.bean.ArticleBean;
import appli.controller.domaine.stock.bean.PreparationBean;
import appli.model.domaine.stock.persistant.PreparationArticlePersistant;
import appli.model.domaine.stock.service.IArticleService;
import appli.model.domaine.stock.service.IPreparationService;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.common.util.BigDecimalUtil;

@WorkController(nameSpace="stock", bean=PreparationBean.class, jspRootPath="/domaine/stock/")
public class PreparationAction extends ActionBase {
	@Inject
	private IArticleService articleService;	
	@Inject
	private IPreparationService preparationService;	
	
	public void work_init(ActionUtil httpUtil) {
		List<ArticleBean> listArticle = articleService.findAll(Order.asc("libelle"));
		httpUtil.setRequestAttribute("listArticle", listArticle);
	}
	
	@Override
	public void work_merge(ActionUtil httpUtil) {
		setDataList(httpUtil);
		
		super.work_merge(httpUtil);
	}

	/**
	 * @param httpUtil
	 */
	@SuppressWarnings({ "unchecked" })
	private void setDataList(ActionUtil httpUtil){
		PreparationBean preparationBean = (PreparationBean)httpUtil.getViewBean();
		preparationBean.setId(httpUtil.getWorkIdLong()); 

		List<PreparationArticlePersistant> listFournArticle = (List<PreparationArticlePersistant>) httpUtil.buildListBeanFromMap("opc_article.id", PreparationArticlePersistant.class, "eaiid", "opc_article.id", "quantite");
		BigDecimal mttHt = null;
		BigDecimal mttTtc = null;
		for (PreparationArticlePersistant preparationTransfoArtP : listFournArticle) {
			mttHt = BigDecimalUtil.add(mttHt, preparationTransfoArtP.getOpc_article().getPrix_achat_moyen_ht());
			mttTtc = BigDecimalUtil.add(mttTtc, preparationTransfoArtP.getOpc_article().getPrix_achat_moyen_ttc());
		}
		preparationBean.setMontant_ht(mttHt);
		preparationBean.setMontant_ttc(mttTtc);
		
		List<PreparationArticlePersistant> listArticle = new ArrayList<>();
		//
		if(preparationBean.getId() != null){
			PreparationBean prepBean = preparationService.findById(preparationBean.getId());
			listArticle = prepBean.getList_article();
			listArticle.clear();
		}
		listArticle.addAll(listFournArticle);
		preparationBean.setList_article(listArticle);
	}
	
	public void genererCode(ActionUtil httpUtil) {
		String code = preparationService.genererCode();
		httpUtil.writeResponse(code);
	}
}
