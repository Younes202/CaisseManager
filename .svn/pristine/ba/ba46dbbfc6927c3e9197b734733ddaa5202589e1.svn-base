package appli.controller.domaine.stock.action;

import java.util.List;

import javax.inject.Inject;

import appli.controller.domaine.stock.bean.FournisseurChequeBean;
import appli.controller.domaine.stock.bean.MouvementBean;
import appli.model.domaine.compta.persistant.PaiementPersistant;
import appli.model.domaine.stock.persistant.ChargeDiversPersistant;
import appli.model.domaine.stock.persistant.FournisseurPersistant;
import appli.model.domaine.stock.service.IFournisseurChequeService;
import appli.model.domaine.stock.service.IFournisseurService;
import appli.model.domaine.stock.service.IMouvementService;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.common.service.MessageService;
import framework.model.common.util.StringUtil;

@WorkController(nameSpace = "stock", bean = FournisseurChequeBean.class, jspRootPath = "/domaine/stock/")
public class FournisseurChequeAction extends ActionBase {

	@Inject
	private IFournisseurChequeService fournisseurChequeService;
	@Inject
	private IFournisseurService fournisseurService;
	@Inject
	private IMouvementService mouvementService;
	
	/**
	 * @param httpUtil
	 */
	public void work_init(ActionUtil httpUtil) {
		List<FournisseurPersistant> listFournisseur = fournisseurService.getListFournisseur(true, false);
		 httpUtil.setRequestAttribute("listeFournisseur", listFournisseur);
		 
		 httpUtil.setRequestAttribute("listStatut", new String[][]{{"NU", "Non utilisé"}, {"U", "Utilisé"}, {"A", "Annulé"}});
	}
	
	/**
	 * @param httpUtil
	 */
	public void editTrMvm(ActionUtil httpUtil){
		String[] mvmArray = StringUtil.getArrayFromStringDelim(httpUtil.getParameter("art"), "-");
		Long paiementId = Long.valueOf(mvmArray[0]);
		PaiementPersistant paiementPer = (PaiementPersistant) fournisseurChequeService.findById(PaiementPersistant.class, paiementId);
		FournisseurChequeBean fcB = fournisseurChequeService.findById(paiementPer.getOpc_fournisseurCheque().getId());
		
		if(fcB.getSource().equals("ACHAT") || fcB.getSource().equals("AVOIR") || fcB.getSource().equals("VENTE") || fcB.getSource().equals("RETOUR")){
			MouvementBean mvmBean = mouvementService.findById(fcB.getElementId());
			httpUtil.setRequestAttribute("mouvementBean", mvmBean);
			httpUtil.setDynamicUrl("/domaine/stock/mouvement_tr_consult.jsp");
		} else if(fcB.getSource().equals("RECETTE") || fcB.getSource().equals("DEPENSE")){
			ChargeDiversPersistant elmntBean = (ChargeDiversPersistant) fournisseurChequeService.findById(ChargeDiversPersistant.class, fcB.getElementId());
			httpUtil.setRequestAttribute("mouvementBean", elmntBean);
			httpUtil.setDynamicUrl("/domaine/stock/chargeDivers_tr_consult.jsp");
		}
	}
	
	@Override
	public void work_find(ActionUtil httpUtil) {
		String statut = httpUtil.getParameter("statutCheque");
		httpUtil.setRequestAttribute("currStatut", statut);
		
		String req = "from FournisseurChequePersistant fournisseurCheque where 1=1 ";
		
		if("U".equals(statut)){
			req = req + "and (fournisseurCheque.elementId is not null) ";
			req = req + "and fournisseurCheque.date_annulation is not null ";
		} else if("NU".equals(statut)){
			req = req + "and fournisseurCheque.elementId is null and fournisseurCheque.date_annulation is null ";
		} else if("A".equals(statut)){
			req = req + "and fournisseurCheque.date_annulation is not null ";
		}
		req = req + "order by opc_fournisseur.libelle, fournisseurCheque.id desc";
		
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_fournisseurCheque");
		
		List listData = fournisseurChequeService.findByCriteria(cplxTable, req);
		
		httpUtil.setRequestAttribute("list_fournisseurCheque", listData);
		
		httpUtil.setDynamicUrl("/domaine/stock/fournisseurCheque_list.jsp");
	}
	
	@Override
	public void work_merge(ActionUtil httpUtil) { 
		FournisseurChequeBean fournisseurBean = (FournisseurChequeBean)httpUtil.getViewBean();
		fournisseurBean.setId(httpUtil.getWorkIdLong());
		
		if(fournisseurBean.getId() == null) {
			boolean isSaisi = false;
			String[] numsCheque = httpUtil.getRequest().getParameterValues("fournisseurCheque.num_cheque");
			for (String num : numsCheque) { 
				if(StringUtil.isNotEmpty(num)) {
					isSaisi = true;
					fournisseurBean.setNum_cheque(num);
					fournisseurChequeService.create(fournisseurBean);
				}
			}
			
			if(!isSaisi) {
				MessageService.addBannerMessage("Veuillez saisir un numéro de chèque.");
			}
		} else {
			fournisseurChequeService.update(fournisseurBean);
		}
		
		super.work_find_refresh(httpUtil);
	}
	
	/**
	 * @param httpUtil
	 */
	public void annulerCheque(ActionUtil httpUtil) {
		Long cfId = httpUtil.getWorkIdLong();
		
		fournisseurChequeService.annulerFournisseurCheque(cfId);
		super.work_find(httpUtil);
	}
}