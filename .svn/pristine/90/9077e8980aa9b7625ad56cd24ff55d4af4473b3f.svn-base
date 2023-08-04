package appli.controller.domaine.compta.action;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.hibernate.criterion.Order;

import appli.controller.domaine.compta.bean.EcritureBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_ECRITURE;
import appli.model.domaine.administration.persistant.EcriturePersistant;
import appli.model.domaine.compta.service.ICompteService;
import appli.model.domaine.compta.service.IEcritureService;
import appli.model.domaine.compta.service.IExerciceService;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.ContextGloabalAppli;
import framework.controller.annotation.WorkController;
import framework.model.beanContext.ComptePersistant;
import framework.model.beanContext.ExercicePersistant;
import framework.model.beanContext.SocietePersistant;
import framework.model.common.constante.ActionConstante;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;

/**
  *
 */
@WorkController(nameSpace="compta", bean=EcritureBean.class, jspRootPath="/domaine/compta/")
public class EcritureAction extends ActionBase {
	@Inject
	private IEcritureService ecritureService;
	@Inject
	IExerciceService exerciceService;
	@Inject
	private ICompteService compteService;
	
	/**
	 * @param httpUtil
	 * @throws Exception
	 */
	public void work_init(ActionUtil httpUtil) {
		// Ajouter le retour à la page précédente
		if(httpUtil.isEditionPage()){
			// Liste des comptes
			if(ContextGloabalAppli.getExerciceBean() != null) {
				List<ComptePersistant> listComptes = compteService.getPlanComptable(false);
				httpUtil.setRequestAttribute("listCompte", listComptes);
			}
		}
		// Liste des exercices
		httpUtil.setRequestAttribute("list_exercices", exerciceService.findAll());
		httpUtil.setRequestAttribute("listSociete", exerciceService.findAll(SocietePersistant.class, Order.asc("raison_sociale")));
	}
	
	@Override
	public void work_edit(ActionUtil httpUtil) {
		super.work_edit(httpUtil);
		
//		EcritureBean ecritureBean = (EcritureBean) httpUtil.getViewBean();
//		if(ecritureBean != null){
//			List<EcriturePersistant> listEct = ecritureService.getListEcritureByOrigineAndTypeAndGroup(ecritureBean.getOpc_societe().getId(), TYPE_ECRITURE.ECRITURE, ecritureBean.getGroupe());
//			
//			httpUtil.setRequestAttribute("listEcritureGroupee", listEct);
//		}
		// Lecture seule si exercice non ouvert
//		if(!exerciceService.is_exercice_date_ouvert(ecritureBean.getDate_mouvement())) {
//			httpUtil.setFormReadOnly();
//		}
	}
	
	@Override
	public void work_update(ActionUtil httpUtil) {
		List<EcriturePersistant> listEcriture = getDataList(httpUtil);
		for (EcriturePersistant ecriturePersistant : listEcriture) {
			if(ecriturePersistant.getOpc_compte() == null || BigDecimalUtil.isZero(ecriturePersistant.getMontant())){
				MessageService.addBannerMessage("Le compte et le montant sont obligatoires pour toutes les lignes.");
				return;
			}
		}
		//
		ecritureService.updateEcriture(listEcriture,  httpUtil.getWorkIdLong());
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "", "La mise à jour est effectuée.");
		
		work_find(httpUtil);
	}
	
	@Override
	public void work_create(ActionUtil httpUtil) {
		List<EcriturePersistant> listEcriture = getDataList(httpUtil);
		for (EcriturePersistant ecriturePersistant : listEcriture) {
			if(ecriturePersistant.getOpc_compte() == null || BigDecimalUtil.isZero(ecriturePersistant.getMontant())){
				MessageService.addBannerMessage("Le compte et le montant sont obligatoires pour toutes les lignes");
				return;
			}
		}
		//
		ecritureService.createEcriture(listEcriture);
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "", "La mise à jour est effectuée.");
		
		work_find(httpUtil);
	}
	
	/**
	 * @param httpUtil
	 */
	@SuppressWarnings({ "unchecked" })
	private List<EcriturePersistant> getDataList(ActionUtil httpUtil) {
		EcritureBean ecritureIhm = (EcritureBean) httpUtil.getViewBean();
		Long ectId = httpUtil.getWorkIdLong();
		Integer nextGroupe = null;
		//
		if(ectId == null){
			nextGroupe = ecritureService.getNextGroupParOrigine(ContextAppli.TYPE_ECRITURE.ECRITURE, ecritureIhm.getOpc_societe().getId());			
		} else{
			EcritureBean ectBean = ecritureService.findById(ectId);
			nextGroupe = ectBean.getGroupe();
		}
		
		List<EcriturePersistant> listEcriture = (List<EcriturePersistant>) httpUtil.buildListBeanFromMap("opc_compte.id", EcriturePersistant.class, "eaiid", "idxIhm",
															"opc_compte.id", "sens", "montant");
		
		for (EcriturePersistant ecriturePersistant : listEcriture) {
			ecriturePersistant.setIs_compta(ecritureIhm.getIs_compta());
			ecriturePersistant.setOpc_etablissement(ecritureIhm.getOpc_etablissement());
			ecriturePersistant.setDate_mouvement(ecritureIhm.getDate_mouvement());
			ecriturePersistant.setLibelle(ecritureIhm.getLibelle());
			ecriturePersistant.setSource(ContextAppli.TYPE_ECRITURE.ECRITURE.toString());
			ecriturePersistant.setElementId(ecritureIhm.getOpc_societe().getId());
			ecriturePersistant.setGroupe(nextGroupe);
		}
		
		return listEcriture;
	}
	
	/**
	 * @param httpUtil
	 * @throws Exception
	 */
	public void work_find(ActionUtil httpUtil) {
		// Ecriture table
		RequestTableBean cplxTable = httpUtil.getTableBean("list_ecriture");

		// Exercice
		ExercicePersistant activeExercice = ContextGloabalAppli.getExerciceBean();
		if(activeExercice == null) {
			MessageService.addBannerMessage("Aucun exercice actif n'a été trouvé ! Veuillez créer un nouvel exercice !");
		}

		Map<String, Object> formCriterion = cplxTable.getFormBean().getFormCriterion();
		formCriterion.put("date_debut", activeExercice.getDate_debut());
		formCriterion.put("date_fin", activeExercice.getDate_fin());
		
		formCriterion.put("type_ecriture", TYPE_ECRITURE.ECRITURE.toString());
		formCriterion.put("sens", "D");
		
		//
		List<EcriturePersistant> listEcriture = (List<EcriturePersistant>) ecritureService.findByCriteriaByQueryId(cplxTable, "ecriture_find");
		// Calcul montant
		for (EcriturePersistant ecriturePersistant : listEcriture) {
			BigDecimal soldeC = null;
			List<EcriturePersistant> listEct = ecritureService.getListEcritureByOrigineAndTypeAndGroup(ecriturePersistant.getOpc_societe().getId(), TYPE_ECRITURE.ECRITURE, ecriturePersistant.getGroupe());
			for (EcriturePersistant ecriturePersistant2 : listEct) {
				if(ecriturePersistant2.getSens().equals("C")){
					soldeC = BigDecimalUtil.add(soldeC, ecriturePersistant2.getMontant());
				}
			}
			ecriturePersistant.setMontant(soldeC);
		}
	   	httpUtil.setRequestAttribute("listEcriture", listEcriture);
	   	
	   	httpUtil.setDynamicUrl("/domaine/compta/ecriture_list.jsp");
	}
	
	@Override
	public void work_delete(ActionUtil httpUtil) {
		Long ectId = httpUtil.getWorkIdLong();
		ecritureService.delete_from_ecriture(ectId);
	}
	
	public void work_post(ActionUtil httpUtil) {
		String action = httpUtil.getAction();
		if(action.equals(ActionConstante.INIT_UPDATE) || action.equals(ActionConstante.EDIT)) {
			EcritureBean ecritureBean = (EcritureBean) httpUtil.getViewBean();
			if(ecritureBean != null){
				if(!exerciceService.is_exercice_date_ouvert(ecritureBean.getDate_mouvement())) {
					httpUtil.setFormReadOnly();
				}
				
				List<EcriturePersistant> listEct = ecritureService.getListEcritureByOrigineAndTypeAndGroup(ecritureBean.getOpc_societe().getId(), TYPE_ECRITURE.ECRITURE, ecritureBean.getGroupe());
				
				httpUtil.setRequestAttribute("listEcritureGroupee", listEct);
			}
		}
	}
}
