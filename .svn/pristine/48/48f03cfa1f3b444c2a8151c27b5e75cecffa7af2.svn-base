package appli.controller.domaine.stock.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.criterion.Order;

import appli.controller.domaine.stock.bean.PreparationTransfoBean;
import appli.model.domaine.stock.persistant.ArticleDetailPersistant;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.persistant.EmplacementPersistant;
import appli.model.domaine.stock.persistant.PreparationTransfoArticlePersistant;
import appli.model.domaine.stock.service.IArticleService;
import appli.model.domaine.stock.service.IPreparationTransfoService;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.StringUtil;

@WorkController(nameSpace="stock", bean=PreparationTransfoBean.class, jspRootPath="/domaine/stock/")
public class PreparationTransfoAction extends ActionBase {
	@Inject
	private IPreparationTransfoService preparationTransfoService;	
	@Inject
	private IArticleService articleService;	
	
	public void work_init(ActionUtil httpUtil) {
		if(httpUtil.isEditionPage()){
			 List<EmplacementPersistant> listEmplacement = (List<EmplacementPersistant>) preparationTransfoService.findAll(EmplacementPersistant.class, Order.asc("titre"));
			 httpUtil.setRequestAttribute("listeDestination", listEmplacement);
			 
			 List<ArticlePersistant> listComposantF = articleService.getListFicheComposant(true);
			 httpUtil.setRequestAttribute("listComposantF", listComposantF);
			 
			 List<ArticlePersistant> listComposant = articleService.getListArticleStock(true);
			 httpUtil.setRequestAttribute("listComposant", listComposant);
		}
	}
	
	@Override
	public void work_merge(ActionUtil httpUtil) {
		PreparationTransfoBean transfoBean = setDataList(httpUtil);
		
		if(StringUtil.isEmpty(httpUtil.getParameter("new_compo")) && transfoBean.getOpc_composant_target() == null){
			MessageService.addBannerMessage("Veuillez sélectionner l'article destination.");
			return;
		}
		
		if(transfoBean.getList_composant().size() == 0){
			MessageService.addBannerMessage("Veuillez ajouter au moins un article.");
			return;
		}
		
		for(PreparationTransfoArticlePersistant ptA : transfoBean.getList_composant()){ 
			if(BigDecimalUtil.isZero(ptA.getQuantite())){
				MessageService.addBannerMessage("Veuillez saisir la quantité des composants.");
				return;
			}
		}
		
		boolean isNewComp = StringUtil.isNotEmpty(httpUtil.getParameter("new_compo"));
		preparationTransfoService.merge(transfoBean, isNewComp);
		
		super.work_find(httpUtil);
	}
	

	/**
	 * @param httpUtil
	 */
	@SuppressWarnings({ "unchecked" })
	private PreparationTransfoBean setDataList(ActionUtil httpUtil) {
		PreparationTransfoBean prepTransfoBean = (PreparationTransfoBean) httpUtil.getViewBean();
		prepTransfoBean.setId(httpUtil.getWorkIdLong());
//		BigDecimal quantiteTransforme = BigDecimalUtil.get(httpUtil.getParameter("preparationTransfo.opc_composant_target.quantite"));
		
		List<PreparationTransfoArticlePersistant> listPrepArticle = (List<PreparationTransfoArticlePersistant>) httpUtil.buildListBeanFromMap("opc_composant.id",PreparationTransfoArticlePersistant.class, "eaiid", "idxIhm",
															"opc_composant.id", "quantite");
		
		BigDecimal mttHt = null;
		BigDecimal mttTtc = null;
		for (PreparationTransfoArticlePersistant preparationTransfoArtP : listPrepArticle) {
			ArticlePersistant firstArt = preparationTransfoService.findById(ArticlePersistant.class, preparationTransfoArtP.getOpc_composant().getId());
			
			mttHt = BigDecimalUtil.add(mttHt, BigDecimalUtil.multiply(preparationTransfoArtP.getQuantite(), firstArt.getPrix_achat_moyen_ht()));
			mttTtc = BigDecimalUtil.add(mttTtc, BigDecimalUtil.multiply(preparationTransfoArtP.getQuantite(), firstArt.getPrix_achat_moyen_ttc()));
		}
		
		// Trier par ihmIDX
		Collections.sort(listPrepArticle, new SortMvmByIhmIdx());
		
		List<PreparationTransfoArticlePersistant> listArticle = new ArrayList<>();
		if (prepTransfoBean.getId() != null) {
			PreparationTransfoBean mvmBean = preparationTransfoService.findById(prepTransfoBean.getId());
			listArticle = mvmBean.getList_composant();
			listArticle.clear();
		}
		listArticle.addAll(listPrepArticle);
		prepTransfoBean.setList_composant(listArticle);
		
		prepTransfoBean.setMontant_ht(mttHt);
		prepTransfoBean.setMontant_ttc(mttTtc);
		
		// Création du composant composé
		if(StringUtil.isNotEmpty(httpUtil.getParameter("new_compo"))){
			ArticlePersistant artP = new ArticlePersistant();
			artP.setLibelle(httpUtil.getParameter("new_compo"));
			
			ArticlePersistant firstArt = preparationTransfoService.findById(ArticlePersistant.class, listArticle.get(0).getOpc_composant().getId());
			artP.setOpc_famille_stock(firstArt.getOpc_famille_stock());
			artP.setIs_stock(true);
			artP.setIs_fiche(true);
			
			artP.setPrix_achat_ht(mttHt);
			artP.setPrix_achat_ttc(mttTtc);
			artP.setPrix_achat_moyen_ht(mttHt);
			artP.setPrix_achat_moyen_ttc(mttTtc);
			
			//
			PreparationTransfoArticlePersistant tart = new PreparationTransfoArticlePersistant();
			tart.setOpc_composant(artP);
			
			tart.setQuantite(BigDecimalUtil.get(1));
			//
			prepTransfoBean.setOpc_composant_target(tart);
			
			// Ajout list des composants
			List<ArticleDetailPersistant> listArticleComp = new ArrayList<>();
			for (PreparationTransfoArticlePersistant preparationTransfoArtP : listPrepArticle) {
				ArticleDetailPersistant artDetP = new ArticleDetailPersistant();
				artDetP.setOpc_article_composant(preparationTransfoArtP.getOpc_composant());
				// Retirer la freinte
				BigDecimal quantite = preparationTransfoArtP.getQuantite();
				Integer freinte = preparationTransfoArtP.getOpc_composant().getFreinte(); 
				//
				if(freinte != null && freinte != 0){
					quantite = BigDecimalUtil.substract(quantite, BigDecimalUtil.divide(BigDecimalUtil.multiply(quantite, BigDecimalUtil.get(freinte)), BigDecimalUtil.get(100)));
				}
				artDetP.setQuantite(quantite);
				
				listArticleComp.add(artDetP);
			}
			artP.setList_article(listArticleComp);
		} else{
			prepTransfoBean.getOpc_composant_target().setQuantite(BigDecimalUtil.get(1));		
		}
		
		return prepTransfoBean;
	}
	
	public void genererCode(ActionUtil httpUtil) {
		String code = preparationTransfoService.genererCode();
		httpUtil.writeResponse(code);
	}
}


class SortMvmByIhmIdx implements Comparator<PreparationTransfoArticlePersistant>{
	@Override
	public int compare(PreparationTransfoArticlePersistant o1, PreparationTransfoArticlePersistant o2) {
		int returnVal = 0;

	    if(o1.getIdxIhm() < o2.getIdxIhm()){
	        returnVal =  -1;
	    }else if(o1.getIdxIhm() > o2.getIdxIhm()){
	        returnVal =  1;
	    }
	    return returnVal;
	}
}
