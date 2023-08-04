package appli.controller.domaine.caisse.action;

import javax.inject.Inject;

import appli.controller.domaine.personnel.bean.EmployeBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.service.IArticleService;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.common.service.MessageService;
import framework.model.common.util.StringUtil;

@WorkController(nameSpace = "caisse-web", bean = EmployeBean.class, jspRootPath = "/domaine/caisse/normal/lecteur/")
public class LecteurPrixAction extends ActionBase {
	@Inject
	private IArticleService articleService;
	
	public void work_init(ActionUtil httpUtil) {
		
	}

	/**
	 * @param httpUtil
	 */
	public void loadArtCodeBarre(ActionUtil httpUtil) {
		String codeBarre = httpUtil.getParameter("cb");
		String codeBarreSaisie = httpUtil.getParameter("cb_srh");
		
		if(StringUtil.isEmpty(codeBarre) && StringUtil.isEmpty(codeBarreSaisie)) {
			MessageService.addGrowlMessage("Erreur saisie", "Veuillez scanner ou saisir un article.");
			return;
		}
		codeBarre = StringUtil.isEmpty(codeBarre) ? codeBarreSaisie : codeBarre;
		//
		ArticlePersistant articleBarre = articleService.getArticleByCodeBarre(codeBarre, ContextAppli.IS_RESTAU_ENV());
		httpUtil.setRequestAttribute("articlePrd", articleBarre);
		
		httpUtil.setDynamicUrl("/domaine/caisse/normal/lecteur/barrecode_reader_result.jsp");
	}
}
