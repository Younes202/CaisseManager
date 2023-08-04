package appli.controller.domaine.caisse.action;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.hibernate.criterion.Order;

import appli.controller.domaine.caisse.bean.CaisseBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.administration.persistant.UserPersistant;
import appli.model.domaine.administration.service.IClientService;
import appli.model.domaine.administration.service.IUserService;
import appli.model.domaine.caisse.service.ITicketCaisseService;
import appli.model.domaine.personnel.persistant.ClientPersistant;
import appli.model.domaine.personnel.persistant.EmployePersistant;
import appli.model.domaine.personnel.service.ISocieteLivrService;
import appli.model.domaine.stock.persistant.MouvementPersistant;
import appli.model.domaine.stock.service.IMouvementService;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.DateUtil.TIME_ENUM;
import framework.model.common.util.ServiceUtil;

@WorkController(nameSpace="caisse-erp", bean=CaisseBean.class, jspRootPath="/domaine/caisse/back-office")
public class LivraisonErpAction extends CaisseBaseAction {
	@Inject
	private IMouvementService mvmService;
	@Inject
	private IUserService userService;
	@Inject
	private ISocieteLivrService societeLivrsService;
	@Inject
	private IClientService clientService;
	@Inject
	private ITicketCaisseService ticketCaisseService;
	/**
	 * @param httpUtil
	 */
	private void initDataListFilter(ActionUtil httpUtil){
		String[][] modePaie = {
				  {ContextAppli.MODE_PAIEMENT.ESPECES.toString(), "Espèces"}, 
				  {ContextAppli.MODE_PAIEMENT.CHEQUE.toString(), "Chèque"}, 
				  {ContextAppli.MODE_PAIEMENT.CARTE.toString(), "Carte"},
				  {ContextAppli.MODE_PAIEMENT.VIREMENT.toString(), "Virement"}
			};
		httpUtil.setRequestAttribute("modePaie", modePaie);
		
		String[][] typeCmd = {
				{ContextAppli.TYPE_COMMANDE.E.toString(), "A emporter"}, 
				{ContextAppli.TYPE_COMMANDE.L.toString(), "Livraison"}
			};
		httpUtil.setRequestAttribute("typeCmd", typeCmd);
	
		String[][] statutArray = {
				  {ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.ANNUL.toString(), "Annulée"}, 
				  {ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE.toString(), "Validée"}, 
				  {ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.PREP.toString(), "En préparation"},
				  {ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.PRETE.toString(), "Prête"},
				  {ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.LIVRE.toString(), "livrée"},
				  {ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.TEMP.toString(), "En attente"}
			};
		httpUtil.setRequestAttribute("statutArray", statutArray);
	
		httpUtil.setRequestAttribute("listUser", mvmService.findAll(UserPersistant.class, Order.asc("login")));
		httpUtil.setRequestAttribute("listEmploye", mvmService.findAll(EmployePersistant.class, Order.asc("nom")));
		httpUtil.setRequestAttribute("listLivreur", userService.getListUserActifsByProfile("LIVREUR"));
		httpUtil.setRequestAttribute("listClient", mvmService.findAll(ClientPersistant.class, Order.asc("nom")));
	}
	
	/**
	 * @param httpUtil
	 */
	public void find_livraison(ActionUtil httpUtil){
		Date dateRef = mvmService.getMaxDate("v");
		if(dateRef == null){
			dateRef = new Date();
		}
		
		// Initialiser les listes pour les filtres
		initDataListFilter(httpUtil);
				
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_mouvement_livraison");
		
		//----------------------------- Date -------------------------
		Date dateDebut = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateDebut"));
		Date dateFin = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateFin"));
		//
		if(httpUtil.getRequest().getParameter("is_fltr") == null) {
			dateDebut = DateUtil.stringToDate(httpUtil.getParameter("dateDebut"));
			dateFin = DateUtil.stringToDate(httpUtil.getParameter("dateFin"));
		}
		
		if(dateDebut == null) {
			dateDebut = dateRef;
			dateFin = dateRef;
			httpUtil.getDate("dateDebut").setValue(dateDebut);
			httpUtil.getDate("dateFin").setValue(dateDebut);
		}
		
		if(httpUtil.getParameter("prev") != null) {
			dateDebut = DateUtil.addSubstractDate(dateDebut, TIME_ENUM.DAY, -1);
			dateFin = DateUtil.addSubstractDate(dateFin, TIME_ENUM.DAY, -1);
		} else if(httpUtil.getParameter("next") != null) {
			dateDebut = DateUtil.addSubstractDate(dateDebut, TIME_ENUM.DAY, 1);
			dateFin = DateUtil.addSubstractDate(dateFin, TIME_ENUM.DAY, 1);
		}
		
		dateDebut = DateUtil.getStartOfDay(dateDebut);
		dateFin = DateUtil.getEndOfDay(dateFin);
		
		httpUtil.setRequestAttribute("dateDebut", dateDebut);
		httpUtil.setRequestAttribute("dateFin", dateFin);
		
		Map<String, Object> formCriterion = cplxTable.getFormBean().getFormCriterion();
		formCriterion.put("dateDebut", dateDebut);
		formCriterion.put("dateFin", dateFin);
		
		//-----------------------------------------------------------
		formCriterion.put("typeLivraison", ContextAppli.TYPE_COMMANDE.L.toString());
		
		String etsCond = 
				(ContextAppli.IS_FULL_CLOUD() || ContextAppli.IS_CLOUD_MASTER() ? (" mouvement.opc_etablissement.id="+ContextAppli.getEtablissementBean().getId())+" and " : " ");
		
		String req = "from MouvementPersistant mouvement"
				+ "where "+etsCond+" mouvement.date_mouvement>='[dateDebut]' "
				+ "and mouvement.date_mouvement<='[dateFin]' "
				+ "and type_mvmnt='v' " 
				+ "and mouvement.type_commande = '[typeLivraison]' "
				+ "order by mouvement.date_mouvement desc, mouvement.id desc";
		
		List<MouvementPersistant> listCaisseMouvement = (List<MouvementPersistant>) mvmService.findByCriteria(cplxTable, req);
		httpUtil.setRequestAttribute("list_livraisonMouvement", listCaisseMouvement);

		// Total
		List<MouvementPersistant> listCaisseMouvementAll = (List<MouvementPersistant>) mvmService.findByCriteria(cplxTable, req, false);
		httpUtil.setRequestAttribute("list_livraisonMouvementNoPage", listCaisseMouvementAll);
		
		MouvementPersistant mvmTotal = new MouvementPersistant();
		for (MouvementPersistant mvmDet : listCaisseMouvementAll) {
			mvmTotal.setMontant_ttc_rem(BigDecimalUtil.add(mvmTotal.getMontant_ttc_rem(), mvmDet.getMontant_ttc_rem()));
			mvmTotal.setMontant_ttc(BigDecimalUtil.add(mvmTotal.getMontant_ttc(), mvmDet.getMontant_ttc()));
			if(!BooleanUtil.isTrue(mvmDet.getIs_annule())) {
				mvmTotal.setMontant_ttc(BigDecimalUtil.add(mvmTotal.getMontant_ttc(), mvmDet.getMontant_ttc()));
			}
			mvmTotal.setMontant_annul(BigDecimalUtil.add(mvmTotal.getMontant_annul(), mvmDet.getMontant_annul()));
		}
		httpUtil.setRequestAttribute("mvmDetTotal", mvmTotal);
		
		httpUtil.setDynamicUrl("/domaine/caisse/back-office/mouvements_livraison_list.jsp");
	}
	
	public void init_situation(ActionUtil httpUtil){
		boolean isSubmit = httpUtil.getParameter("isSub") != null;
		if(httpUtil.getParameter("curMnu") == null && !isSubmit){
			httpUtil.setDynamicUrl("/domaine/caisse/normal/situationErp/situation_init.jsp");
			return;
		}
		
		Long elementId = httpUtil.getLongParameter("elmentId");
		if(httpUtil.getMenuAttribute("clientId") != null){
			elementId = (Long)httpUtil.getMenuAttribute("clientId");
		}
		
		if(isSubmit && elementId == null){
			MessageService.addBannerMessage("Veuillez sélectionner un élément.");
			return;
		}
		
		//			
		Date dateDebut = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateDebut"));
		Date dateFin = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateFin"));
		
		if(dateDebut == null) {
			dateDebut = mvmService.getMaxDate("v");
			if(dateDebut == null){
				dateDebut = new Date();
			}
			Calendar cal = DateUtil.getCalendar(dateDebut);
			String dateString = (cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR);
			dateDebut = DateUtil.stringToDate("01/"+dateString);
			dateFin = DateUtil.stringToDate(DateUtil.getMaxMonthDate(dateDebut)+"/"+dateString);
		}
		// Postionner l'heure
		dateDebut = DateUtil.getStartOfDay(dateDebut);
		dateFin = DateUtil.getEndOfDay(dateFin);
		
		httpUtil.setRequestAttribute("dateDebut", dateDebut);
		httpUtil.setRequestAttribute("dateFin", dateFin);
		
		httpUtil.setFormReadOnly(false);
		
		httpUtil.setMenuAttribute("elementId", elementId);
		httpUtil.setMenuAttribute("dateDebut", dateDebut);
		httpUtil.setMenuAttribute("dateFin", dateFin);
		
		String mnu = httpUtil.getParameter("curMnu");
		if(mnu != null){
			httpUtil.setMenuAttribute("curMnu", mnu);
		}
		//httpUtil.setRequestAttribute("listLivreur",  employeService.getListEmployeActifs("LIVREUR"));
		httpUtil.setRequestAttribute("listLivreur",  userService.getListUserActifsByProfile("LIVREUR"));
		httpUtil.setRequestAttribute("listSociete", societeLivrsService.getSocieteLivrsActifs());
		httpUtil.setRequestAttribute("listClient", clientService.getClientsActifs());
		
		if(elementId == null || !isSubmit){
			httpUtil.setDynamicUrl("/domaine/caisse/normal/situationErp/situation.jsp");
			return;
		}
		mnu = (String) httpUtil.getMenuAttribute("curMnu");
		Map mapData = null;
		//
		if(mnu == null || mnu.equals("cli")){
			mapData = ticketCaisseService.getSituationClientErp(null, elementId, dateDebut, dateFin);	
		} else if(mnu.equals("livr")){
			mapData = ticketCaisseService.getSituationLivreurErp(null, elementId, dateDebut, dateFin);	
		} else if(mnu.equals("socLivr")){
			mapData = ticketCaisseService.getSituationSocieteLivrErp(null, elementId, dateDebut, dateFin);	
		}
		httpUtil.setRequestAttribute("mapData", mapData);
		init_situation_mvm(httpUtil);
		
		httpUtil.setDynamicUrl("/domaine/caisse/normal/situationErp/situation_detail.jsp"); 
	}
	
	public void init_situation_mvm(ActionUtil httpUtil){
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_situation");
		String mnu = (String) httpUtil.getMenuAttribute("curMnu");
		Map mapData = null;
		Long clientId = (Long) httpUtil.getMenuAttribute("elementId");
		Date dateDebut = (Date) httpUtil.getMenuAttribute("dateDebut");
		Date dateFin = (Date) httpUtil.getMenuAttribute("dateFin");
		//
		ITicketCaisseService ticketCaisseSrv = ServiceUtil.getBusinessBean(ITicketCaisseService.class);
		
		if(mnu == null || mnu.equals("cli")){
			mapData = ticketCaisseSrv.getSituationClientErp(cplxTable, clientId, dateDebut, dateFin);	
		} else if(mnu.equals("livr")){
			mapData = ticketCaisseSrv.getSituationLivreurErp(cplxTable, clientId, dateDebut, dateFin);	
		} else if(mnu.equals("socLivr")){
			mapData = ticketCaisseSrv.getSituationSocieteLivrErp(cplxTable, clientId, dateDebut, dateFin);	
		}
		
		if(clientId != null  && (mnu == null || mnu.equals("cli"))) {
			clientService = (clientService == null ? ServiceUtil.getBusinessBean(IClientService.class) : clientService);
			httpUtil.setRequestAttribute("cliSituation", clientService.findById(clientId));
		}
		
		httpUtil.setRequestAttribute("MapRazCli", mapData);// Pour impression raz solde
		httpUtil.setRequestAttribute("listMouvement", mapData.get("data"));
		httpUtil.setDynamicUrl("/domaine/caisse/normal/situationErp/situation_mvm.jsp");
	}
}
