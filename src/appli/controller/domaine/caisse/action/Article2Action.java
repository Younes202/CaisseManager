package appli.controller.domaine.caisse.action;

import javax.inject.Inject;

import appli.controller.domaine.caisse.bean.CaisseBean;
import appli.controller.domaine.util_erp.ContextAppli.SOFT_ENVS;
import appli.model.domaine.caisse.service.IArticle2Service;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.service.MessageService;
import framework.model.common.util.StrimUtil;

@WorkController(nameSpace="caisse", bean=CaisseBean.class, jspRootPath="/domaine/caisse/")
public class Article2Action extends ActionBase {
	@Inject
	private IArticle2Service articleService2;

	/**
	 * @param httpUtil
	 */
	public void ajouterFavorisCaisse(ActionUtil httpUtil) {
		boolean isRestau = SOFT_ENVS.restau.toString().equals(StrimUtil.getGlobalConfigPropertie("context.soft"));
		
		Long artId = httpUtil.getWorkIdLong();
		articleService2.ajouterFavorisCaisse(artId);
		
		if(isRestau){
			httpUtil.setDynamicUrl("stock.article.work_find");			
		} else{
			httpUtil.setDynamicUrl("stock.composant.work_find");
		}
	}
	
	/**
	 * @param httpUtil
	 */
	public void dupliquer_fiche_article(ActionUtil httpUtil){
		Long[] listComposantsIds = httpUtil.getCheckedElementsLong("list_article");
		if(listComposantsIds == null || listComposantsIds.length == 0){
			MessageService.addBannerMessage("Vous devez cocher au moins un composant avant de valider.");
			return;
		} else{
			boolean isRepondu = MessageService.addDialogConfirmMessage("dupplic-art", "stock.composant.dupliquer_fiche_article", "Vous aller créer des fiches articles pour chaque composant coché."
					+ "<br>Si l'article existe déjà, ses informations seront <b>regénérées</b>.<br>"
					+ "<br>Souhaitez-vous continuer ?");
			if(isRepondu){
				articleService2.dupliquerEnFicheArticle(httpUtil.getParameter("famille_cuisine"), httpUtil.getParameter("destination"), listComposantsIds);
			}
			
			work_find(httpUtil);
		}
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "Mise à jour", "L'article est généré avec succès.");
	}
}
