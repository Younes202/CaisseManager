package appli.controller.domaine.stock.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import appli.controller.domaine.administration.bean.ValTypeEnumBean;
import appli.controller.domaine.stock.bean.DemandeTransfertBean;
import appli.controller.domaine.stock.bean.MouvementBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_MOUVEMENT_ENUM;
import appli.model.domaine.administration.service.IValTypeEnumService;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.persistant.DemandeTransfertArticlePersistant;
import appli.model.domaine.stock.persistant.MouvementArticlePersistant;
import appli.model.domaine.stock.service.IArticleService;
import appli.model.domaine.stock.service.ICentraleSynchroService;
import appli.model.domaine.stock.service.IDemandeTransfertService;
import appli.model.domaine.stock.service.IEmplacementService;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.ModelConstante;

@WorkController(nameSpace = "stock", bean = DemandeTransfertBean.class, jspRootPath = "/domaine/stock/")
public class DemandeTransfertAction extends ActionBase {
	@Inject
	private IArticleService articleService;
	@Inject
	private IValTypeEnumService valEnumService;
	@Inject
	private IDemandeTransfertService demandeTransService;
	@Inject
	private ICentraleSynchroService centraleService;
	@Inject
	private IEmplacementService emplacementService;
	
	/**
	 * @param httpUtil
	 */
	public void work_init(ActionUtil httpUtil) {
		if(!ContextAppli.getAbonementBean().isOptPlusSyncCentrale()
				&& !ContextAppli.getAbonementBean().isOptPlusEtsCentrale()){
			MessageService.addBannerMessage(MSG_TYPE.WARNING, " Aucune centrale n'est configurée pour votre établissement.");
		}
		
		List<ArticlePersistant> listArticle = articleService.getListArticleStock(true);
		 httpUtil.setRequestAttribute("listArticle", listArticle);
		 
		 List<ValTypeEnumBean> listVal = valEnumService.getListValeursByType(ModelConstante.ENUM_TVA);
		 httpUtil.setRequestAttribute("valTVA", listVal);
	}
	
	@Override
	public void work_merge(ActionUtil httpUtil) {
		DemandeTransfertBean demandeTransB = (DemandeTransfertBean) httpUtil.getViewBean();
		demandeTransB.setId(httpUtil.getWorkIdLong());

		List<DemandeTransfertArticlePersistant> listFournArticle = (List<DemandeTransfertArticlePersistant>) httpUtil.buildListBeanFromMap("opc_article.id",DemandeTransfertArticlePersistant.class, "eaiid", "idxIhm",
															"opc_article.id", "quantite");
		
		if(listFournArticle.size() < 1) {
			MessageService.addBannerMessage("Veuillez choisir au moins un article.");
			return;
		}
		
		int idx = 1;
		for (DemandeTransfertArticlePersistant demandeTransArtP : listFournArticle) {
			if(BigDecimalUtil.isZero(demandeTransArtP.getQuantite())){
				MessageService.addBannerMessage("La quantité est obligatoire ligne : "+idx);
				return;
			}
			idx++;
		}
		
		List<DemandeTransfertArticlePersistant> listArticle = new ArrayList<>();
		if (demandeTransB.getId() != null) {
			DemandeTransfertBean dmdTransBean = demandeTransService.findById(demandeTransB.getId());
			listArticle = dmdTransBean.getList_article();
			listArticle.clear();
		}
		
		listArticle.addAll(listFournArticle);
		demandeTransB.setList_article(listArticle);
		demandeTransB.setStatut("NON ENREGISTREE");
		
		for (DemandeTransfertArticlePersistant demandeTransfertArtP : demandeTransB.getList_article()) {
			ArticlePersistant articleP = (ArticlePersistant) demandeTransService.findById(ArticlePersistant.class, demandeTransfertArtP.getOpc_article().getId());
			demandeTransfertArtP.setCode(articleP.getCode());
		}
		
		demandeTransB.setLogin(ContextAppli.getUserBean().getLogin());
		demandeTransB.setDate_creation(new Date());
		
		if(ContextAppli.getUserBean().getOpc_employe() != null) {
			String nomComplet = ContextAppli.getUserBean().getOpc_employe().getNom() + " " + ContextAppli.getUserBean().getOpc_employe().getPrenom();
			demandeTransB.setNom(nomComplet);
		}
		
		demandeTransB.setOrigine_auth(ContextAppli.getEtablissementBean().getCode_authentification());
		
		super.work_merge(httpUtil);

		//
		String retour = centraleService.sendDemandeTransfertToCentrale(demandeTransB.getId());
		demandeTransB.setStatut_sync(retour);
		demandeTransService.merge(demandeTransB);
	}
	
	@Override
	public void work_delete(ActionUtil httpUtil) {
		Long workId = httpUtil.getWorkIdLong();
		DemandeTransfertBean demandeBean = demandeTransService.findById(workId);
		
		if(demandeBean.getStatut().equals("NON ENREGISTREE")) {
			super.work_delete(httpUtil);
		} else {
			demandeBean.setStatut("ANNULEE");
			demandeBean.setStatut_sync("OK");
			//
			demandeTransService.merge(demandeBean);
			
			String retour = centraleService.sendDemandeTransfertToCentrale(demandeBean.getId());
			demandeBean.setStatut_sync(retour);
			demandeTransService.merge(demandeBean);
			
			work_find(httpUtil);
		}
	}
	
	public void annulerDemande(ActionUtil httpUtil) {
		Long workId = httpUtil.getWorkIdLong();
		DemandeTransfertBean demandeBean = demandeTransService.findById(workId);
		String retour = centraleService.sendAnnulationDemandeTransfertToCentrale(demandeBean.getId());
		
		if(StringUtil.isEmpty(retour)) {
			MessageService.addGrowlMessage("", "La centrale est injoignable.");
			return;
		}
		demandeBean.setStatut("ANNULEE");
		demandeBean.setStatut_sync("OK");
		demandeTransService.merge(demandeBean);
		
		work_find(httpUtil);
	}
	
	public void synchroniserDemande(ActionUtil httpUtil) {
		Long workId = httpUtil.getWorkIdLong();
		DemandeTransfertBean demandeBean = demandeTransService.findById(workId);
		String retour = centraleService.sendDemandeTransfertToCentrale(demandeBean.getId());
		
		if(StringUtil.isEmpty(retour)) {
			MessageService.addGrowlMessage("", "La centrale est injoignable.");
			return;
		}
		demandeBean.setStatut("ENREGISTREE");
		demandeBean.setStatut_sync("OK");
		demandeTransService.merge(demandeBean);
		
		work_find(httpUtil);
	}
	
	public void init_transfert(ActionUtil httpUtil) {
		httpUtil.setMenuAttribute("type_mvmnt", "t");
		Long demandeId = httpUtil.getWorkIdLong();
		DemandeTransfertBean demandeTrans = demandeTransService.findById(demandeId);
		
		List<MouvementArticlePersistant> listMvmArticle = new ArrayList<>();
		for (DemandeTransfertArticlePersistant demandeP : demandeTrans.getList_article()) {
			MouvementArticlePersistant mvmArticle = new MouvementArticlePersistant();
			mvmArticle.setQuantite(demandeP.getQuantite());
			mvmArticle.setOpc_article(demandeP.getOpc_article());
			
			listMvmArticle.add(mvmArticle);
		}
		
		MouvementBean mouvementB = new MouvementBean();
		mouvementB.setType_mvmnt(TYPE_MOUVEMENT_ENUM.t.toString());
		mouvementB.setType_transfert("A");
		
		mouvementB.setOpc_demande(demandeTrans);
		mouvementB.setList_article(listMvmArticle);
		
		httpUtil.setRequestAttribute("mouvementB", mouvementB);
		httpUtil.setRequestAttribute("demandeTrans", demandeTrans);
		
		//
		httpUtil.setDynamicUrl("stock.mouvement.work_init_create");
	}
	
	@Override
	public void work_edit(ActionUtil httpUtil) {
		Long workId = httpUtil.getWorkIdLong();
		DemandeTransfertBean demandeBean = demandeTransService.findById(workId);
		
		boolean isEditable = false;
		if(demandeBean.getStatut().equals("NON ENREGISTREE")) {
			isEditable = true;
		}
		httpUtil.setRequestAttribute("isEditable", isEditable);
		super.work_edit(httpUtil);
	}	
}

