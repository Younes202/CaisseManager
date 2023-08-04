package appli.controller.domaine.personnel.action;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.inject.Inject;

import org.hibernate.criterion.Order;

import appli.controller.domaine.administration.bean.ValTypeEnumBean;
import appli.controller.domaine.fidelite.bean.CarteFideliteClientBean;
import appli.controller.domaine.personnel.bean.ClientBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_MOUVEMENT_ENUM;
import appli.model.domaine.administration.persistant.ValTypeEnumPersistant;
import appli.model.domaine.administration.service.IClientService;
import appli.model.domaine.administration.service.IValTypeEnumService;
import appli.model.domaine.fidelite.persistant.CarteFideliteClientPersistant;
import appli.model.domaine.fidelite.persistant.CarteFidelitePersistant;
import appli.model.domaine.fidelite.service.ICarteFideliteClientService;
import appli.model.domaine.fidelite.service.ICarteFideliteService;
import appli.model.domaine.personnel.persistant.ClientContactPersistant;
import appli.model.domaine.personnel.persistant.ClientPersistant;
import appli.model.domaine.stock.persistant.MouvementPersistant;
import appli.model.domaine.stock.service.IMouvementService;
import appli.model.domaine.stock.service.impl.FactureVentePDF;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.beanContext.VillePersistant;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.ModelConstante;

@WorkController(nameSpace = "pers", bean = ClientBean.class, jspRootPath = "/domaine/personnel/")
public class ClientAction extends ActionBase {
	@Inject
	private IClientService clientService;
	@Inject
	private ICarteFideliteService carteFideliteService;
	@Inject
	private ICarteFideliteClientService carteFideliteClientService;
	@Inject
	private IValTypeEnumService valEnumService;
	@Inject
	private IMouvementService mouvementService;
	
	public void work_init(ActionUtil httpUtil) {
		if(httpUtil.getMenuAttribute("IS_SUB_ADD") == null) {
	     	httpUtil.setMenuAttribute("IS_SUB_ADD","pers.client.work_init_create");
		}
		httpUtil.removeUserAttribute("MNU_FIDELITE"); 
		httpUtil.setMenuAttribute("IS_MENU_CMLIENT", true);
		httpUtil.setRequestAttribute("liste_carte", carteFideliteService.findAll(Order.asc("libelle")));
		
		if(httpUtil.isEditionPage()){
			MessageService.getGlobalMap().put("NO_ETS", true);
			httpUtil.setRequestAttribute("listVille", clientService.getListData(VillePersistant.class, "opc_region.libelle, libelle"));
			MessageService.getGlobalMap().remove("NO_ETS");
			
			Long clientId = httpUtil.getWorkIdLong();
			if(clientId != null){
				httpUtil.setMenuAttribute("clientId", clientId);
				httpUtil.setMenuAttribute("clientSess", clientService.findById(ClientPersistant.class, clientId));
			} else{
				clientId = (Long)httpUtil.getMenuAttribute("clientId");
			}
			if(!httpUtil.isCreateAction()){
				// Gérer le retour sur cet onglet
				clientId = (Long)httpUtil.getMenuAttribute("clientId");
				if(clientId != null && !httpUtil.isCrudOperation()){
					loadBean(httpUtil, clientId);
					
					List<CarteFideliteClientPersistant> list_cartes = clientService.findById(ClientPersistant.class, clientId).getList_cartes();
					if(list_cartes.size() > 0) {
						httpUtil.setMenuAttribute("carteClientId", list_cartes.get(0).getId());
						httpUtil.setRequestAttribute("carteClient", list_cartes.get(0));
					}
				}
			} else{
				httpUtil.removeMenuAttribute("clientId");
			}
		}
		
		if(httpUtil.getParameter("tp") != null){
			httpUtil.setMenuAttribute("IS_MNU", true);
		}
		
		httpUtil.setRequestAttribute("civiliteArray", new String[][]{{"H", "Homme"}, {"F", "Femme"}});
		httpUtil.setRequestAttribute("typeClientArray", new String[][]{{"PP", "Personne physique"}, {"PM", "Personne morale"}});
	}
	
	/**
	 * @param httpUtil
	 */
	public void genererNum(ActionUtil httpUtil) {
		String numBl = clientService.generateNum();
		httpUtil.writeResponse(numBl);
	}
	
	/**
	 * @param httpUtil
	 */
	public void loadAjaxClient(ActionUtil httpUtil) {
		Long cliId = httpUtil.getLongParameter("cli");
		
		MessageService.getGlobalMap().put("NO_ETS", true);
		httpUtil.setRequestAttribute("listVille", clientService.getListData(VillePersistant.class, "opc_region.libelle, libelle"));
		MessageService.getGlobalMap().remove("NO_ETS");
		
		ClientBean clientBean = clientService.findById(cliId);
		httpUtil.setViewBean(clientBean);
		
		// --------------------------- DYN form --------------------------
		httpUtil.setRequestAttribute("listDataValueForm", clientService.loadDataForm(cliId, "CLIENT"));
		Map<String, List<ValTypeEnumPersistant>> mapEnum = clientService.loadDataENumForm("CLIENT");
		for(String key : mapEnum.keySet()){
			httpUtil.setRequestAttribute(key, mapEnum.get(key));
		}
		// ---------------------------------------------------------------
		
		CarteFideliteClientPersistant cbfP = carteFideliteClientService.getCarteClientActive(cliId);
		if(cbfP != null){
			httpUtil.setRequestAttribute("carte_id", cbfP.getOpc_carte_fidelite().getId());
		}
//		ClientPortefeuillePersistant portefeuilleC = portefeuilleService.getPortefeuilleClient(cliId);
		if(BooleanUtil.isTrue(clientBean.getIs_portefeuille())){
			httpUtil.setRequestAttribute("is_portefeuille", true);
		}
		httpUtil.setRequestAttribute("is_solde_neg", clientBean.getIs_solde_neg());
		
		httpUtil.setDynamicUrl("/domaine/caisse/normal/caisse_client_edit.jsp"); 
	}
	
	/**
	 * @param httpUtil
	 */
	public void find_reduction(ActionUtil httpUtil){
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_caisseMouvement");
		
		Long clientId = (Long)httpUtil.getMenuAttribute("clientId");
		cplxTable.getFormBean().getFormCriterion().put("clientId", clientId);
		
		List<CaisseMouvementPersistant> listCaisseMouvement = (List<CaisseMouvementPersistant>) clientService.findByCriteriaByQueryId(cplxTable, "caisseMouvement_reduc_find");
		httpUtil.setRequestAttribute("list_caisseMouvement", listCaisseMouvement);

		httpUtil.setDynamicUrl("/domaine/personnel/client_reduction.jsp");
	}
	
	
//	public void print_ticket_client(ActionUtil httpUtil){
//		Long clientId = httpUtil.getLongParameter("cli");
//		ClientPortefeuillePersistant portefeuilleClient = portefeuilleService.getPortefeuilleClient(clientId);
////		BigDecimal soldeClient = (portefeuilleClient != null ? portefeuilleClient.getSolde() : null);
//		
////		if(soldeClient == null || soldeClient.compareTo(BigDecimalUtil.ZERO) >= 0){
////			MessageService.addGrowlMessage("", "Aucune commande portefeuille à solder n'a été trouvée.");
////			return;
////		}
//		
//		
//		List<CaisseMouvementPersistant> listMvm = clientService.getListMouvementPortefeuille(clientId);
//		if(listMvm.size() == 0){
//			MessageService.addGrowlMessage("", "Aucune commande portefeuille n'a été trouvée.");
//			return;
//		}
//		
//		CaisseMouvementPersistant caisseMvmP = getMouvementGroupedPrint(listMvm);
//		
//		if(!StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CAISSE_PRINT"))){
//    		PrintUtil pu = new PrintUtil(caisseMvmP, null, null);
//    		PrintPosBean printPosBean = pu.getPrintPosBean();
//    		sendObjectPrint(httpUtil, printPosBean);
//    		httpUtil.setDynamicUrl("/domaine/"+httpUtil.getUserAttribute("PATH_JSP_CAISSE")+"/print-local.jsp");
//	    } else{
//	    	MessageService.getGlobalMap().put("CURRENT_CAISSE", ContextAppli.getCaisseBean());
//			PrintUtil pu = new PrintUtil(caisseMvmP, null, null);
//		    pu.print(false);
//	    }
//		httpUtil.writeResponse("");
//	}
//	
/*	private CaisseMouvementPersistant getMouvementGroupedPrint(List<CaisseMouvementPersistant> listMvm){
		CaisseMouvementPersistant caisseMvmP = new CaisseMouvementPersistant();
		CaisseMouvementPersistant caisseMvmSel = listMvm.get(0);
		ReflectUtil.copyProperties(caisseMvmP, caisseMvmSel);
		
		caisseMvmP.setId(null);
		caisseMvmP.setRef_commande(caisseMvmSel.getRef_commande());
		caisseMvmP.setLast_statut(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE.toString());
		caisseMvmP.setList_article(new ArrayList<>());
		//
		
		int idxCli = 1;
		for (CaisseMouvementPersistant caisseMvm : listMvm) {
//			if(soldeClient.compareTo(total) <= 0){// Iteration jusqu'à la valeur du portefeuille
//				break;
//			}
			List<CaisseMouvementArticlePersistant> list_article = caisseMvm.getList_article();
			if(list_article == null || list_article.size() == 0){
				continue;
			}
			for (CaisseMouvementArticlePersistant caisseMvmArticleP : list_article) {
				caisseMvmArticleP.setIdx_client(idxCli);
			}
			caisseMvmP.getList_article().addAll(list_article);
			
			idxCli++;
			
			
			BigDecimal mttNetCmd = null;
        	if(caisseMvm.getListEncaisse() != null && caisseMvm.getListEncaisse().size()>0){
    			for (CaisseMouvementArticlePersistant cmd : caisseMvm.getListEncaisse()) {
    				if(!BooleanUtil.isTrue(cmd.getIs_annule()) && !BooleanUtil.isTrue(cmd.getIs_offert())){
    					mttNetCmd = BigDecimalUtil.add(mttNetCmd, cmd.getMtt_total());
    				}
    	    	}
        	} else{
        		mttNetCmd = caisseMvm.getMtt_commande_net();
        	}
	        	
			caisseMvmP.setMtt_art_offert(BigDecimalUtil.add(caisseMvmP.getMtt_art_offert(), caisseMvm.getMtt_art_offert()));
			caisseMvmP.setMtt_commande(BigDecimalUtil.add(caisseMvmP.getMtt_commande(), caisseMvm.getMtt_commande()));
			caisseMvmP.setMtt_commande_net(BigDecimalUtil.add(caisseMvmP.getMtt_commande_net(), mttNetCmd));
			caisseMvmP.setMtt_reduction(BigDecimalUtil.add(caisseMvmP.getMtt_reduction(), caisseMvm.getMtt_reduction()));
		}
		caisseMvmP.setMax_idx_client(idxCli);
		
		return caisseMvmP;
	}
	*/
//	public void print_ticket_pdf(ActionUtil httpUtil){
//		Long[] checkedArray = httpUtil.getCheckedElementsLong("list_caisseMouvement");
//		if(checkedArray == null || checkedArray.length == 0){
//			MessageService.addGrowlMessage("", "Veuillez sélectionner au moins une commande.");
//			return;
//		}
//		
//		List<CaisseMouvementPersistant> listMvm = new ArrayList<>();
//		for (Long mvmId : checkedArray) {
//			CaisseMouvementPersistant caisseMvmSel  = clientService.findById(CaisseMouvementPersistant.class, mvmId);
//			listMvm.add(caisseMvmSel);
//		}
//		CaisseMouvementPersistant caisseMvmP = getMouvementGroupedPrint(listMvm);
//			
//		if("T".equals(httpUtil.getParameter("tpp"))){
//			if(!StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CAISSE_PRINT"))){
//	    		PrintUtil pu = new PrintUtil(caisseMvmP, null, null);
//	    		PrintPosBean printPosBean = pu.getPrintPosBean();
//	    		sendObjectPrint(httpUtil, printPosBean);
//	    		httpUtil.setDynamicUrl("/domaine/"+httpUtil.getUserAttribute("PATH_JSP_CAISSE")+"/print-local.jsp");
//		    } else{
//		    	MessageService.getGlobalMap().put("CURRENT_CAISSE", listMvm.get(0).getOpc_caisse_journee().getOpc_caisse());
//				PrintUtil pu = new PrintUtil(caisseMvmP, null, null);
//			    pu.print(false);
//		    }
//		} else{//Facture pdf
////			String nomClient = caisseMvmSel.getOpc_client().getNom();
////			String adresseClient = caisseMvmSel.getOpc_client().getAdresse_compl();
////			
////			httpUtil.doDownload(new FactureTicketPDF().exportPdf(caisseMvmP, nomClient, adresseClient), true);
//		}
//	}
	
//	/**
//	 * @param httpUtil
//	 */
//	public void find_ClientComposant(ActionUtil httpUtil){
//		RequestTableBean cplxTable = getTableBean(httpUtil, "list_prix_composant_find");
//		
//		Long clientId = (Long)httpUtil.getMenuAttribute("clientId");
//		cplxTable.getFormBean().getFormCriterion().put("clientId", clientId);
//		
//		List<ArticleClientPrixPersistant> listCaisseMouvement = (List<ArticleClientPrixPersistant>) clientService.findByCriteriaByQueryId(cplxTable, "prix_composant_find");
//		httpUtil.setRequestAttribute("list_caisseMouvement", listCaisseMouvement);
//
//		httpUtil.setDynamicUrl("/domaine/personnel/client_prix_composant.jsp");
//	}
	
	@Override
	public void work_find(ActionUtil httpUtil) {
		httpUtil.removeMenuAttribute("carteClientId");
		
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_client");
		String req = "from ClientPersistant client where 1=1 "
				+ getFilterStateRequest(httpUtil, "is_disable")
				+ "order by client.nom";
		
		List<ClientPersistant> listData = (List<ClientPersistant>) clientService.findByCriteria(cplxTable, req);
		
		httpUtil.setRequestAttribute("list_client", listData);
		
		httpUtil.setDynamicUrl("/domaine/personnel/client_list.jsp");
	}
	
	@Override
	public void work_merge(ActionUtil httpUtil) {
		ClientBean clientBean = (ClientBean) httpUtil.getViewBean();
		clientBean.setId(httpUtil.getWorkIdLong());
		
		setDataContact(httpUtil);
		
		super.work_merge(httpUtil);
		
		ClientPersistant clientP = clientService.findById(ClientPersistant.class, clientBean.getId());
		String carteId = httpUtil.getRequest().getParameter("carte_id");
		
		if(StringUtil.isEmpty(carteId)) {
			List<CarteFideliteClientPersistant> lstCFClient = clientService.findByField(CarteFideliteClientPersistant.class, "opc_client.id", clientP.getId());
			if(lstCFClient != null && lstCFClient.size() > 0) {
				for (CarteFideliteClientPersistant carteFideliteCliP : lstCFClient) {
					carteFideliteClientService.delete(carteFideliteCliP.getId());
				}
			}
			httpUtil.removeMenuAttribute("carteClientId");

		} else {
			String codeBarre = httpUtil.getRequest().getParameter("carte_code_bar");
			String dtDebut = httpUtil.getRequest().getParameter("carte_date_debut");
			Date dtFin = DateUtil.stringToDate(httpUtil.getRequest().getParameter("carte_date_fin"));
			
			CarteFidelitePersistant cfP = carteFideliteService.findById(CarteFidelitePersistant.class, Long.valueOf(carteId));
			
			List<CarteFideliteClientPersistant> lstCFClient = clientService.findByField(CarteFideliteClientPersistant.class, "opc_client.id", clientP.getId());
			CarteFideliteClientPersistant cbfP = null;
			
			if(lstCFClient != null && lstCFClient.size() > 0) {
				cbfP = lstCFClient.get(0);
			} else {
				cbfP = new CarteFideliteClientBean();
			}
			
			if(StringUtil.isEmpty(codeBarre)){
				codeBarre = clientBean.getId()+"_"+new Random(1000).nextInt();
			}
			cbfP.setCode_barre(codeBarre);
			
			Date dateDebut = null;
			if(StringUtil.isEmpty(dtDebut)){
				dateDebut = new Date();
			} else{
				dateDebut = DateUtil.stringToDate(dtDebut);
			}
			cbfP.setDate_debut(dateDebut);
			
			cbfP.setDate_fin(dtFin);
			cbfP.setOpc_carte_fidelite(cfP);
			cbfP.setOpc_client(clientP);
			
			cbfP = carteFideliteClientService.merge(ServiceUtil.persistantToBean(CarteFideliteClientBean.class, cbfP));
			
			httpUtil.setMenuAttribute("carteClientId", cbfP.getId());
			httpUtil.setRequestAttribute("carteClient", cbfP);
		}
		if(httpUtil.getMenuAttribute("IS_SUB_ADD") != null) {
			httpUtil.setDynamicUrl(""+httpUtil.getMenuAttribute("IS_SUB_ADD"));
		}
		
		/*
		 // Envoyer mail à l'admin
					if(StringUtil.isNotEmpty(abonneBean.getMail())) {
						IMailUtilService mailService = (IMailUtilService) ServiceUtil.getBusinessBean(IMailUtilService.class);
						
						MailQueuePersistant mailP = new MailQueuePersistant();
						mailP.setDate_mail(DateUtil.getCurrentDate());
						mailP.setDate_creation(DateUtil.getCurrentDate());
						mailP.setExpediteur_mail(ContextVtc.getGlobalConfig("MAIL_FROM"));
						mailP.setExpediteur_nom(ContextVtc.getGlobalConfig("MAIL_FROM_NAME"));
						mailP.setMail_signature("ADMIN");
						mailP.setDestinataires(abonneBean.getMail());
						mailP.setSujet("Nouvelle inscription client");
						
						String prenom = clientB.getPrenom()!=null ? clientB.getPrenom() : "";
						
						String message = mailService.getGenericMailContent(abonneBean, 
								"Bonjour, <br>" +
								"Le client "+clientB.getNom()+" "+prenom+" vient de finaliser son inscription sur votre site.<br>"
						);
						
						mailP.setMessage(message);
						mailP.setSource("COMPTE");
						mailP.setOrigine_id(abonneBean.getId());
						mailP.setOpc_abonne(abonneBean);
						
						mailService.addMailToQueue(mailP);
					} 
		  */
	}
	
	/**
	 * @param httpUtil
	 */
	public void  find_mvm_client(ActionUtil httpUtil){
		String[][] typeCmd = {{ContextAppli.TYPE_COMMANDE.E.toString(), "A emporter"}, 
				  {ContextAppli.TYPE_COMMANDE.P.toString(), "Sur place"}, 
				  {ContextAppli.TYPE_COMMANDE.L.toString(), "Livraison"}
				  };
		httpUtil.setRequestAttribute("typeCmd", typeCmd);
		String[][] statutArray = {
				{ContextAppli.STATUT_JOURNEE.OUVERTE.getStatut(), ContextAppli.STATUT_JOURNEE.OUVERTE.toString()}, 
				{ContextAppli.STATUT_JOURNEE.CLOTURE.getStatut(), ContextAppli.STATUT_JOURNEE.CLOTURE.toString()} 
			};
		httpUtil.setRequestAttribute("statutArray", statutArray);
		
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_caisseMouvement");
		
		Long clientId = (Long)httpUtil.getMenuAttribute("clientId");
		cplxTable.getFormBean().getFormCriterion().put("clientId", clientId);
		
		List<CaisseMouvementPersistant> listCaisseMouvement = (List<CaisseMouvementPersistant>) clientService.findByCriteriaByQueryId(cplxTable, "caisseMouvement_tiers_find");
		httpUtil.setRequestAttribute("list_caisseMouvement", listCaisseMouvement);
		
		// Total
		List<CaisseMouvementPersistant> listCaisseMouvementAll = (List<CaisseMouvementPersistant>) clientService.findByCriteriaByQueryId(cplxTable, "caisseMouvement_tiers_find", false);
		CaisseMouvementPersistant mvmTotal = new CaisseMouvementPersistant();
		for (CaisseMouvementPersistant mvmDet : listCaisseMouvementAll) {
			mvmTotal.setMtt_art_offert(BigDecimalUtil.add(mvmTotal.getMtt_art_offert(), mvmDet.getMtt_art_offert()));
			mvmTotal.setMtt_commande(BigDecimalUtil.add(mvmTotal.getMtt_commande(), mvmDet.getMtt_commande()));
			mvmTotal.setMtt_commande_net(BigDecimalUtil.add(mvmTotal.getMtt_commande_net(), mvmDet.getMtt_commande_net()));
			mvmTotal.setMtt_reduction(BigDecimalUtil.add(mvmTotal.getMtt_reduction(), mvmDet.getMtt_reduction()));
			mvmTotal.setMtt_annul_ligne(BigDecimalUtil.add(mvmTotal.getMtt_annul_ligne(), mvmDet.getMtt_annul_ligne()));
		}
		httpUtil.setRequestAttribute("mvmDetTotal", mvmTotal);
		
		httpUtil.setDynamicUrl("/domaine/personnel/client_mouvements.jsp");
	}
	
	/*----------------------------------------------------------------------*/
	public void init_situation(ActionUtil httpUtil) {
		Long clientId = (Long) httpUtil.getMenuAttribute("clientId");
		List<ClientPersistant> listData = null;
		//	
		if(clientId == null){
			RequestTableBean cplxTable = getTableBean(httpUtil, "list_etat_client");
			listData = (List<ClientPersistant>) clientService.findByCriteriaByQueryId(cplxTable, "client_find");
		} else{
			listData = new ArrayList<>();
			listData.add(clientService.findById(clientId));
		}
		
		BigDecimal total_mtt = null, 
				total_paye = null, 
				total_restant = null;
		
		for (ClientPersistant cliP : listData) {
			// Alimenter l'état
			clientService.affecterEtatClient(cliP);
			//
			total_mtt = BigDecimalUtil.add(total_mtt, cliP.getMtt_total());
	   		total_paye = BigDecimalUtil.add(total_paye, cliP.getMtt_paye());
	   		total_restant = BigDecimalUtil.add(total_restant, cliP.getMtt_non_paye());				
		}
		//			
		httpUtil.setRequestAttribute("total_mtt", total_mtt);
	   	httpUtil.setRequestAttribute("total_paye", total_paye);
	   	httpUtil.setRequestAttribute("total_restant", total_restant);
		
		httpUtil.setRequestAttribute("list_client", listData);
		
		httpUtil.setDynamicUrl("/domaine/personnel/situation_client.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void situation_detail(ActionUtil httpUtil){
		Long fournId = httpUtil.getLongParameter("fo");
		if(fournId != null) {
			httpUtil.setMenuAttribute("fo", fournId);
		}
		
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_mouvement");
		String req = "from MouvementPersistant mouvement where mouvement.opc_client.id='{currCliId}' "
				+ "order by mouvement.date_mouvement desc";
		cplxTable.getFormBean().getFormCriterion().put("currCliId", httpUtil.getMenuAttribute("fo"));
		List<MouvementPersistant> list_mouvement = (List<MouvementPersistant>) clientService.findByCriteria(cplxTable, req);
		
		httpUtil.setRequestAttribute("list_mouvement", list_mouvement);
		
		httpUtil.setDynamicUrl("/domaine/personnel/situation_client_detail.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	@SuppressWarnings({ "unchecked" })
	private void setDataContact(ActionUtil httpUtil){
		ClientBean clientBean = (ClientBean)httpUtil.getViewBean();
		clientBean.setId(httpUtil.getWorkIdLong());

		List<ClientContactPersistant> listFournContact = (List<ClientContactPersistant>) httpUtil.buildListBeanFromMap("contact", ClientContactPersistant.class, "eaiid", "contact", "fonction", "coord");
		
		List<ClientContactPersistant> listContact = new ArrayList<>();
		if(clientBean.getId() != null){
			ClientBean cliBean = clientService.findById(clientBean.getId());
			listContact = cliBean.getList_contact();
			listContact.clear();
		}
		listContact.addAll(listFournContact);
		clientBean.setList_contact(listContact);
	}
	/*------------------------------------------------------------------------------*/
	
	/**
	 * @param httpUtil
	 */
	public void init_facture_edit(ActionUtil httpUtil) {
		 List<ValTypeEnumBean> listeFinancement = valEnumService.getListValeursByType(ModelConstante.ENUM_FINANCEMENT);
		 httpUtil.setRequestAttribute("listeFinancement", listeFinancement);
		 
		if(httpUtil.getParameter("isSub") == null){
			httpUtil.setDynamicUrl("/domaine/personnel/facture_modal_edit.jsp");
			return;
		}
		Long clientId = (Long) httpUtil.getMenuAttribute("clientId");
		String typeMvm = httpUtil.getParameter("typeMvm");
		Date dateFacture = DateUtil.stringToDate(httpUtil.getParameter("dateFacture"));
		if(dateFacture == null){
			dateFacture = new Date();
		}
		String nomFacture = httpUtil.getParameter("nomFacture");
		String adresseFacture =  httpUtil.getParameter("adresseFacture");
		Date dateDebut = DateUtil.stringToDate(httpUtil.getParameter("dateDebut"));
		Date dateFin = null;
		//
		if(StringUtil.isNotEmpty(httpUtil.getParameter("dateFin"))){
			dateFin = DateUtil.stringToDate(httpUtil.getParameter("dateFin"));
		}
		
		String[] modeFinancement = null;
		if(StringUtil.isNotEmpty(httpUtil.getLongParameter("modePaiement"))){
			modeFinancement = httpUtil.getRequest().getParameterValues("modePaiement");
		}
		
		if(typeMvm.equals("CAISSE")){
			MouvementPersistant mouvementP = mouvementService.getMouvementGroupeFacture(clientId, dateDebut, dateFin, modeFinancement, "cli");
			mouvementP.setNum_facture(mouvementService.generateNumFac(TYPE_MOUVEMENT_ENUM.v));
			File pdfFile = new FactureVentePDF(mouvementP, true).exportPdf();
			httpUtil.doDownload(pdfFile, true);
			// Archiver le pdf dans la GED
			mouvementService.archiverPdfFactureGed(mouvementP.getOpc_client(), mouvementP.getNum_facture(), dateFacture, pdfFile);
		} else if(typeMvm.equals("VENTE")){
			CaisseMouvementPersistant caisseWebP = mouvementService.getMouvementGroupeFacture(clientId, dateDebut, dateFin, modeFinancement);
			caisseWebP.setAdresse_facture(adresseFacture);
			caisseWebP.setNom_facture(nomFacture);
			caisseWebP.setNum_facture(mouvementService.generateNumFac(TYPE_MOUVEMENT_ENUM.vc));
			caisseWebP.setDate_facture(dateFacture);
			
			File pdfFile = new FactureVentePDF(caisseWebP, true).exportPdf();
			httpUtil.doDownload(pdfFile, true);
			// Archiver le pdf dans la GED
			mouvementService.archiverPdfFactureGed(caisseWebP.getOpc_client(), caisseWebP.getNum_facture(), dateFacture, pdfFile);
		}
	}
	
	/**
	 * @param httpUtil
	 */
	public void desactiver(ActionUtil httpUtil) {
		clientService.activerDesactiverElement(httpUtil.getWorkIdLong());
		work_find(httpUtil);
	}
	
	public void work_post(ActionUtil httpUtil){
		manageDataForm(httpUtil, "CLIENT");
		
		// --------------------------- DYN form --------------------------
		httpUtil.setRequestAttribute("listDataValueForm", clientService.loadDataForm(null, "CLIENT"));
		// ---------------------------------------------------------------
	}
}
