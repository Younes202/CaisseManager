package appli.controller.domaine.personnel.action;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.criterion.Order;

import appli.controller.domaine.administration.bean.ValTypeEnumBean;
import appli.controller.domaine.personnel.bean.FraisBean;
import appli.controller.domaine.stock.bean.FournisseurChequeBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.administration.persistant.GedFichierPersistant;
import appli.model.domaine.administration.persistant.GedPersistant;
import appli.model.domaine.administration.service.IClientService;
import appli.model.domaine.administration.service.ICompteBancaireService;
import appli.model.domaine.administration.service.IGedService;
import appli.model.domaine.administration.service.IValTypeEnumService;
import appli.model.domaine.compta.persistant.PaiementPersistant;
import appli.model.domaine.personnel.persistant.ClientPersistant;
import appli.model.domaine.personnel.persistant.EmployePersistant;
import appli.model.domaine.personnel.persistant.FraisDetailPersistant;
import appli.model.domaine.personnel.persistant.TypeFraisPersistant;
import appli.model.domaine.personnel.service.IEmployeService;
import appli.model.domaine.personnel.service.IFraisService;
import appli.model.domaine.stock.persistant.FournisseurChequePersistant;
import appli.model.domaine.stock.service.IFournisseurChequeService;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.controller.annotation.WorkForward;
import framework.model.beanContext.CompteBancairePersistant;
import framework.model.beanContext.ComptePersistant;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.ModelConstante;

@WorkController(nameSpace = "pers", bean = FraisBean.class, jspRootPath = "/domaine/personnel/")
public class FraisAction extends ActionBase {
	@Inject
	private IFraisService fraisService;
	@Inject
	private IEmployeService employeService;
	@Inject
	private IValTypeEnumService valEnumService;
	@Inject
	private IFournisseurChequeService fournisseurChequeService;
	@Inject
	private ICompteBancaireService compteBancaireService;
	@Inject
	private IGedService gedService;
	
	public void work_init(ActionUtil httpUtil) {
		httpUtil.setMenuAttribute("IS_SUB_ADD","pers.frais.work_init_create");
		httpUtil.setRequestAttribute("listeEmploye", employeService.getListEmployeActifs());
		List<TypeFraisPersistant> listTypeFrais = (List<TypeFraisPersistant>) fraisService.findAll(TypeFraisPersistant.class, Order.asc("libelle"));
		httpUtil.setRequestAttribute("listTypeFrais", listTypeFrais);
		List<?> listCompteBancaire = fraisService.findAll(CompteBancairePersistant.class, Order.asc("libelle"));
		httpUtil.setRequestAttribute("listeBanque", listCompteBancaire);
	}

	@Override
	@WorkForward(useFormValidator=true, useBean=true)
	public void work_merge(ActionUtil httpUtil) {
		FraisBean fraisBean = setDataList(httpUtil);
		fraisBean.setId(httpUtil.getWorkIdLong());
		super.work_merge(httpUtil);
		
		managePieceJointe(httpUtil, fraisBean.getId(), "frais");
		
	}
	
	@Override
	public void work_delete(ActionUtil httpUtil) {
		super.work_delete(httpUtil);
		manageDeleteImage(httpUtil.getWorkIdLong(), "frais");
	}
	
	private FraisBean setDataList(ActionUtil httpUtil) {
		FraisBean fraisBean = (FraisBean) httpUtil.getViewBean();
		fraisBean.setId(httpUtil.getWorkIdLong());
		List<FraisDetailPersistant> listDetail = (List<FraisDetailPersistant>) httpUtil.buildListBeanFromMap("opc_type_frais.id",FraisDetailPersistant.class, "eaiid", "idxIhm",
															"date_depense", "montant","opc_type_frais.id");
		
		List<FraisDetailPersistant> listFraisDet = new ArrayList<>();
		if (fraisBean.getId() != null) {
			FraisBean fraisB = fraisService.findById(fraisBean.getId());
			listFraisDet = fraisB.getList_detail();
			listFraisDet.clear();
		}
		
		//renseigner le montant à rembouser de chaque dépense
		for (FraisDetailPersistant fraisDetP: listDetail) {
			TypeFraisPersistant typeFraisP = (TypeFraisPersistant) fraisService.findById(TypeFraisPersistant.class, fraisDetP.getOpc_type_frais().getId());
			if(fraisDetP.getMontant().compareTo(typeFraisP.getMontant_max()) > 0) {
				fraisDetP.setMtt_rembours(typeFraisP.getMontant_max());
			} else {
				fraisDetP.setMtt_rembours(fraisDetP.getMontant());
			}
		}

		listFraisDet.addAll(listDetail);
		fraisBean.setList_detail(listFraisDet);
		
		//calcul du montant global à rembourser
		BigDecimal total_rebours = null;
		for (FraisDetailPersistant fraisDetP: listDetail) {
			total_rebours = BigDecimalUtil.add(total_rebours, fraisDetP.getMtt_rembours());
		}
		fraisBean.setMtt_rembours(total_rebours);
		
		return fraisBean;
	}
	
	public void downloadPieceJointe(ActionUtil httpUtil) {
		httpUtil.manageInputFileView("frais");
	}
	
	public void init_remboursDemande(ActionUtil httpUtil) {
		Long workId = httpUtil.getWorkIdLong();
		httpUtil.setRequestAttribute("frais_id", workId);
		
		List<ValTypeEnumBean> listeFinancement = valEnumService.getListValeursByType(ModelConstante.ENUM_FINANCEMENT);
		httpUtil.setRequestAttribute("listeFinancement", listeFinancement);
		httpUtil.setRequestAttribute("listeBanque", fraisService.findAll(CompteBancairePersistant.class));
		List<FournisseurChequeBean> listChequeFournisseur = fournisseurChequeService.getListChequeFournisseurActifs(null, null);
		httpUtil.setRequestAttribute("listChequeFournisseur", listChequeFournisseur);
		 
		httpUtil.setDynamicUrl("/domaine/personnel/remboursDemande_modal.jsp");
	}
	
	public void rembourserDemande(ActionUtil httpUtil) {
		Long workId = httpUtil.getWorkIdLong();
		FraisBean fraisBean = fraisService.findById(workId);
		
		String opc_financement_enum = httpUtil.getParameter("opc_financement_enum.id");
		if(StringUtil.isEmpty(opc_financement_enum)) {
			MessageService.addBannerMessage("Veuillez choisir un mode de paiement.");
			return;
		}
		
		String num_virement = httpUtil.getParameter("num_virement");
		String num_cheque = httpUtil.getParameter("num_cheque");
		String opc_fournisseurCheque_id = httpUtil.getParameter("opc_fournisseurCheque.id");
		String opc_compte_bancaire_id = httpUtil.getParameter("opc_compte_bancaire.id");
		Date date_echeance = DateUtil.stringToDate(httpUtil.getParameter("date_echeance"));
		
		if(opc_compte_bancaire_id == null){
			MessageService.addBannerMessage("Le compte est obligatoire.");
		}
		if(opc_financement_enum.equals("CHEQUE") && StringUtil.isEmpty(num_cheque)){
			MessageService.addBannerMessage("Le numéro de chèque est obligatoire.");
		} else if(opc_financement_enum.equals("CHEQUE_F") && opc_fournisseurCheque_id == null){
			MessageService.addBannerMessage("Le chèque fournisseur est obligatoire.");
		} else if(opc_financement_enum.equals("VIREMENT") && StringUtil.isEmpty(num_virement)){
			MessageService.addBannerMessage("Le numéro de virement est obligatoire.");
		}
		
		if(MessageService.isError()){
			return;
		}
		
		ValTypeEnumBean financement_enumP = valEnumService.findById(Long.valueOf(opc_financement_enum));
		
		PaiementPersistant paiementPersistant = new PaiementPersistant();
		paiementPersistant.setMontant(fraisBean.getMtt_rembours());
		paiementPersistant.setDate_echeance(date_echeance);
		paiementPersistant.setOpc_compte_bancaire(compteBancaireService.findById(Long.valueOf(opc_compte_bancaire_id)));
		paiementPersistant.setOpc_financement_enum(financement_enumP);
		if(StringUtil.isNotEmpty(opc_fournisseurCheque_id)) {
			paiementPersistant.setOpc_fournisseurCheque((FournisseurChequePersistant) compteBancaireService.findById(FournisseurChequePersistant.class, Long.valueOf(opc_fournisseurCheque_id)));
			paiementPersistant.setNum_cheque(num_cheque);
		}
		
		fraisService.rembourserDemande(fraisBean, paiementPersistant);
		
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "", "Demande validée avec succès.");
		work_find(httpUtil);
	}
	
	public void rejeterDemande(ActionUtil httpUtil) {
		Long workId = httpUtil.getWorkIdLong();
		FraisBean fraisBean = fraisService.findById(workId);
		
		fraisBean.setStatut("REJECTED");
		fraisService.update(fraisBean);
		
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "", "Demande rejetée avec succès.");
		work_find(httpUtil);
	}
	
	public void annuler_reboursDemande(ActionUtil httpUtil) {
		Long workId = httpUtil.getWorkIdLong();
		FraisBean fraisBean = fraisService.findById(workId);
		fraisService.annulerDemande(fraisBean);
		
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "", "Remboursement de la demande annulé avec succès.");
		work_find(httpUtil);
	}
}
