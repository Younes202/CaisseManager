package appli.controller.domaine.stock.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.hibernate.criterion.Order;

import appli.controller.domaine.stock.bean.ChargeDiversBean;
import appli.controller.domaine.stock.bean.MouvementBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.compta.persistant.PaiementPersistant;
import appli.model.domaine.stock.persistant.ChargeDiversPersistant;
import appli.model.domaine.stock.persistant.FournisseurPersistant;
import appli.model.domaine.stock.persistant.MouvementPersistant;
import appli.model.domaine.stock.service.IChargeDiversService;
import appli.model.domaine.stock.service.IFournisseurService;
import appli.model.domaine.stock.service.IMouvementService;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.ControllerBeanUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.StringUtil;

@WorkController(nameSpace = "stock", bean = MouvementBean.class, jspRootPath = "/domaine/stock/")
public class RegroupementMvmAction extends ActionBase {
	@Inject
	private IMouvementService mouvementService;
	@Inject 
	private IFournisseurService fournisseurService;
	@Inject
	private IChargeDiversService chargeDiversService;
	
	/**
	 * @param httpUtil
	 */
	public void work_init(ActionUtil httpUtil){
		httpUtil.setRequestAttribute("list_fournisseur", fournisseurService.findAll(Order.asc("libelle")));
		String tp = httpUtil.getParameter("tpR");
		
		if(tp != null){
			httpUtil.setMenuAttribute("tpR", tp);
			
			if(tp.equals("CH")) {
				String tpCH = (String) httpUtil.getMenuAttribute("tp");
				httpUtil.setMenuAttribute("tpCH", tpCH);
			}
		} else{
			tp = (String)httpUtil.getMenuAttribute("tpR");
		}
		
		List<Long> ids = new ArrayList<>();
		Long workId = httpUtil.getWorkIdLong();
		Long founisseurId = null;
		//
		if(workId != null) {
			if(!httpUtil.isCrudOperation()){
				BigDecimal mttTotal = null;
				
				if(tp.equals("MVM")){
					MouvementBean mvmDet = mouvementService.findById(workId);
					founisseurId = mvmDet.getOpc_fournisseur().getId();
					List<MouvementPersistant> listEnfants = mouvementService.getListMvmGroupe(workId);
					
		   			for (MouvementPersistant mvmEnfant : listEnfants) {
		   				ids.add(mvmEnfant.getId());
		   				mttTotal = BigDecimalUtil.add(mttTotal, mvmEnfant.getMontant_ttc());
		   			}
		   			mvmDet.setMouvementIds(ids.toArray(new Long[ids.size()]));
		   			mvmDet.setId(workId);
		   			httpUtil.setRequestAttribute("mttTotal", mttTotal);
					httpUtil.setRequestAttribute("currMvm", mvmDet);
				} else{
					ChargeDiversBean mvmDet = chargeDiversService.findById(workId);
					founisseurId = mvmDet.getOpc_fournisseur().getId();
					
					List<ChargeDiversPersistant> listEnfants = chargeDiversService.getListCDGroupe(workId);
					if(listEnfants != null){
						for (ChargeDiversPersistant mvmEnfant : listEnfants) {
							ids.add(mvmEnfant.getId());
							mttTotal = BigDecimalUtil.add(mttTotal, mvmEnfant.getMontant());
						}
					}
					mvmDet.setMouvementIds(ids.toArray(new Long[ids.size()]));
					mvmDet.setId(workId);
					httpUtil.setRequestAttribute("currMvm", mvmDet);
					httpUtil.setRequestAttribute("mttTotal", mttTotal);
				}
			}
		}
		
		boolean isRefreshAction = httpUtil.getParameter("is_refresh") != null;
		// Bls
		founisseurId = isRefreshAction ? httpUtil.getLongParameter("fournisseur") : founisseurId;
		Date dateDebut = DateUtil.stringToDate(httpUtil.getRequest().getParameter("date_debut"));
		Date dateFin = DateUtil.stringToDate(httpUtil.getRequest().getParameter("date_fin"));
		BigDecimal mttTotal = null;
		BigDecimal mttAvoirTotal = null;
		BigDecimal mttRestePaie = null;
		BigDecimal mttPaie = null;
		//
		if(tp != null && tp.equals("MVM")){ 
			List<MouvementPersistant> listBl = mouvementService.getMvmNonGroupe(founisseurId, ids, dateDebut, dateFin);
			httpUtil.setRequestAttribute("list_bl", listBl);
			List<MouvementPersistant> listAvoir = mouvementService.getMvmAvoirNonGroupe(founisseurId, ids, dateDebut, dateFin);
			httpUtil.setRequestAttribute("list_avoir", listAvoir);
			//
			if(isRefreshAction){ 
				List<Long> currIds = new ArrayList<>();
				MouvementBean mvmDet = new MouvementBean();
				for (MouvementPersistant mouvementP : listBl) {
					currIds.add(mouvementP.getId());
					mttTotal = BigDecimalUtil.add(mttTotal, mouvementP.getMontant_ttc());
					
					//-----------------------------------------
					BigDecimal mttPaye = mouvementP.getMttPaye();
					mttPaie = BigDecimalUtil.add(mttPaie, mttPaye);
					mttRestePaie = BigDecimalUtil.add(mttRestePaie, BigDecimalUtil.substract(mouvementP.getMontant_ttc(), mttPaye));
				}
				mvmDet.setMouvementIds(currIds.toArray(new Long[currIds.size()]));
				httpUtil.setRequestAttribute("currMvm", mvmDet);
				httpUtil.setRequestAttribute("mttTotal", mttTotal);
				httpUtil.setRequestAttribute("mttPaie", mttPaie);
				httpUtil.setRequestAttribute("mttRestePaie", mttRestePaie);

				// Avoir
				List<Long> currAvoirIds = new ArrayList<>();
				MouvementBean mvmAvoirDet = new MouvementBean();
				for (MouvementPersistant mouvementPersistant : listAvoir) {
					currAvoirIds.add(mouvementPersistant.getId());
					mttAvoirTotal = BigDecimalUtil.add(mttAvoirTotal, mouvementPersistant.getMontant_ttc());
				}
				
				mvmDet.setAvoirIds(currAvoirIds.toArray(new Long[currAvoirIds.size()]));
				httpUtil.setRequestAttribute("currAvoirMvm", mvmAvoirDet);
				httpUtil.setRequestAttribute("mttAvoirTotal", mttAvoirTotal);
			}
		} else{
			List<ChargeDiversPersistant> listBl = chargeDiversService.getChargeNonGroupe(founisseurId, ids, dateDebut, dateFin);
			httpUtil.setRequestAttribute("list_bl", listBl);
			
			if(isRefreshAction){
				List<Long> currIds = new ArrayList<>();
				ChargeDiversBean mvmDet = new ChargeDiversBean();
				for (ChargeDiversPersistant mouvementPersistant : listBl) {
					currIds.add(mouvementPersistant.getId());
					mttTotal = BigDecimalUtil.add(mttTotal, mouvementPersistant.getMontant());
				}
				
				mvmDet.setMouvementIds(currIds.toArray(new Long[currIds.size()]));
				httpUtil.setRequestAttribute("currMvm", mvmDet);
				httpUtil.setRequestAttribute("mttTotal", mttTotal);
			}
		}
		httpUtil.setRequestAttribute("fourn_id", founisseurId); 
		
		httpUtil.setDynamicUrl("/domaine/stock/regroupementMvm_edit.jsp");		
	}
	
	@Override
	public void work_edit(ActionUtil httpUtil) {
		String tpR = (String) httpUtil.getMenuAttribute("tpR");
		if(tpR.equals("CH")) {
			Long workId = httpUtil.getWorkIdLong();
			ChargeDiversBean chrgDvBean = chargeDiversService.findById(workId);
			httpUtil.setRequestAttribute("chargeDivers", chrgDvBean);
		}
	}
	
	@Override
	public void work_init_update(ActionUtil httpUtil) {
		String tpR = (String) httpUtil.getMenuAttribute("tpR");
		if(tpR.equals("CH")) {
			Long workId = httpUtil.getWorkIdLong();
			ChargeDiversBean chrgDvBean = chargeDiversService.findById(workId);
			httpUtil.setRequestAttribute("chargeDivers", chrgDvBean);
		}
	}
	
	@Override
	public void work_delete(ActionUtil httpUtil) {
		Long workId = httpUtil.getWorkIdLong();
		mouvementService.delete(workId);
		
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "", "L'élément a été supprimé avec succès.");
		
		boolean isAchat = !"CH".equals(httpUtil.getMenuAttribute("tpR"));
		if(isAchat){
			if(StringUtil.isTrue(""+httpUtil.getMenuAttribute("IS_GRP_VIEW"))){
				httpUtil.setDynamicUrl("stock.mouvement.find_mvm_groupe");
			} else{
				httpUtil.setDynamicUrl("stock.mouvement.work_find");				
			}
		} else{
			manageDeleteImage(workId, "rgrpment_depense");
			
			if(StringUtil.isTrue(""+httpUtil.getMenuAttribute("IS_GRP_VIEW"))){
				httpUtil.setDynamicUrl("stock.chargeDivers.find_mvm_groupe");
			} else{
				httpUtil.setDynamicUrl("stock.chargeDivers.work_find");			
			}
		}
	}
	
	/**
	 * 
	 */
	public void work_merge(ActionUtil httpUtil) {
		Long mvmGroupId = httpUtil.getWorkIdLong();
		Map<String, Object> params = (Map)httpUtil.getRequest().getAttribute(ProjectConstante.WORK_PARAMS);
		boolean isAchat = !"CH".equals(httpUtil.getMenuAttribute("tpR"));
		
		Object dtObj = params.get("date_mouvement");
		Date dateMvm = (""+dtObj).indexOf("/") != -1 ? DateUtil.stringToDate(""+dtObj) : (Date)dtObj;
		String facture = (String)params.get("num_facture");
		
		// Mvms
		String[] mvmIds = httpUtil.getRequest().getParameterValues("mouvementIds");
		Long[] mvmIdsLong = new Long[mvmIds.length];
		for (int i=0; i<mvmIds.length; i++) {
			mvmIdsLong[i] = Long.valueOf(mvmIds[i]);
		}
		// Les avoirs
		String[] avoirIds = httpUtil.getRequest().getParameterValues("avoirIds");
		Long[] avoirIdsLong = null;
		if(avoirIds != null) {
			avoirIdsLong = new Long[avoirIds.length];
			for (int i=0; i<avoirIds.length; i++) {
				avoirIdsLong[i] = Long.valueOf(avoirIds[i]);
			}
		}
		
		if((mvmIdsLong == null || mvmIdsLong.length == 0) && (avoirIdsLong == null || avoirIdsLong.length == 0)){
			MessageService.addBannerMessage("Veuillez séléctionner au moins un numéro de BL ou avoir");
			return;
		}

		// Si bl du même fournisseur alors on l'affecte
		FournisseurPersistant opcFournisseur = null;
		boolean isMemeFourn = true;
		for (Long mvmId : mvmIdsLong) {
			if(isAchat){
				MouvementPersistant mvmP = (MouvementPersistant)mouvementService.findById(MouvementPersistant.class, mvmId);
				if(opcFournisseur != null && mvmP.getOpc_fournisseur()!=null && !opcFournisseur.getId().equals(mvmP.getOpc_fournisseur().getId())){
					isMemeFourn = false;
					break;
				}
				opcFournisseur = mvmP.getOpc_fournisseur();
			} else{
				ChargeDiversPersistant chargeP = (ChargeDiversPersistant)mouvementService.findById(ChargeDiversPersistant.class, mvmId);
				if(opcFournisseur != null && chargeP.getOpc_fournisseur()!=null && !opcFournisseur.getId().equals(chargeP.getOpc_fournisseur().getId())){
					isMemeFourn = false;
					break;
				}
				opcFournisseur = chargeP.getOpc_fournisseur();
			}
		}
		
		if(!isMemeFourn){
			MessageService.addBannerMessage("Les BL doivent appartenir au même fournisseur.");
			return;
		}
		
		List<PaiementPersistant> listPaiement = (List<PaiementPersistant>) httpUtil.getMenuAttribute("PAIEMENT_DATA");
		if(listPaiement != null){
			for (PaiementPersistant paiementPersistant : listPaiement) {
				paiementPersistant.setSource(isAchat ? ContextAppli.TYPE_ECRITURE.ACHAT.toString() : ContextAppli.TYPE_ECRITURE.DEPENSE.toString());
				if(isMemeFourn){
					paiementPersistant.setOpc_fournisseur(opcFournisseur);
				}
			}
		}
		
		//
		if(isAchat){
			MouvementBean mvmBean = (MouvementBean)ControllerBeanUtil.mapToBean(MouvementBean.class, params);
			mvmBean.setId(mvmGroupId);
			mvmBean.setMouvementIds(mvmIdsLong);
			
			if(avoirIdsLong != null) {
				mvmBean.setAvoirIds(avoirIdsLong);
			}
			mvmBean.setNum_facture(facture);
			mvmBean.setDate_mouvement(dateMvm);
			mvmBean.setOpc_fournisseur(opcFournisseur);
			mvmBean.setList_paiement(listPaiement);
			//
			mouvementService.valideRegroupementBL(mvmBean);
			managePieceJointe(httpUtil, mvmBean.getId(), "rgrpment_achat");
			//
			if(StringUtil.isTrue(""+httpUtil.getMenuAttribute("IS_GRP_VIEW"))){
				httpUtil.setDynamicUrl("stock.mouvement.find_mvm_groupe");
			} else{
				httpUtil.setDynamicUrl("stock.mouvement.work_find");				
			}
		} else{
			ChargeDiversBean mvmBean = new ChargeDiversBean();
			mvmBean.setId(mvmGroupId);
			mvmBean.setNum_bl(facture);
			mvmBean.setDate_mouvement(dateMvm);
			mvmBean.setMouvementIds(mvmIdsLong);
			mvmBean.setOpc_fournisseur(opcFournisseur);
			mvmBean.setList_paiement(listPaiement);
			//
			chargeDiversService.valideRegroupementBL(mvmBean);
			managePieceJointe(httpUtil, mvmBean.getId(), "rgrpment_depense");
			//
			if(StringUtil.isTrue(""+httpUtil.getMenuAttribute("IS_GRP_VIEW"))){
				httpUtil.setDynamicUrl("stock.chargeDivers.find_charge_groupe");
			} else{
				httpUtil.setDynamicUrl("stock.chargeDivers.work_find");			
			}
		}
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "", "Le regroupement est effectué.");
	}
	
	public void downloadPieceJointe(ActionUtil httpUtil) {
		httpUtil.manageInputFileView(httpUtil.getParameter("isa")==null?"rgrpment_depense":"rgrpment_achat");
	}
}