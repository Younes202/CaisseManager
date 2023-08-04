
package appli.controller.domaine.dashboard.action;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.hibernate.criterion.Order;

import appli.controller.domaine.administration.bean.EtatFinanceBean;
import appli.model.domaine.administration.persistant.EcriturePersistant;
import appli.model.domaine.administration.service.ICompteBancaireService;
import appli.model.domaine.administration.service.IEtatFinanceService;
import appli.model.domaine.caisse.service.IJourneeService;
import appli.model.domaine.compta.persistant.PaiementPersistant;
import appli.model.domaine.dashboard.service.IDashBoardService;
import appli.model.domaine.stock.persistant.MouvementPersistant;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.beanContext.CompteBancairePersistant;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.StringUtil;

/**
 * @author 
 *
 */
@WorkController(nameSpace="dash", jspRootPath="/domaine/dashboard_erp/") 
public class DashComptaAction extends ActionBase {
	@Inject
	private IDashBoardService dashBoardService;
	@Inject
	private ICompteBancaireService compteBancaireService;
	@Inject
	private IEtatFinanceService etatFinanceService;
	@Inject 
	private IJourneeService journeeService;
	
	/**
	 * 
	 * @param httpUtil
	 */
	public void work_init(ActionUtil httpUtil)  {
		
		httpUtil.setRequestAttribute("dateDebut", DateUtil.dateToString(DateUtil.getCurrentDate(), /*etatFinanceBean.getDate_etat()*/ "MM/yyyy"));
		httpUtil.setDynamicUrl("/domaine/dashboard_erp/compta/dashboard_compta.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void init_dashboard(ActionUtil httpUtil){
		// Bilan financier
		
		// Evolution des comptes
		
		// Synthèse
		
		// Masse salariale
		
		// Etat des chèques	
	}
	
	/**
	 * @param httpUtil
	 */
	public void initEtatFinance(ActionUtil httpUtil){
		
   /*	Long financeId = httpUtil.getWorkIdLong();
		if(financeId != null){
			EtatFinanceBean etatFinanceBean = (EtatFinanceBean)ServiceUtil.persistantToBean(EtatFinanceBean.class, journeeService.findById(EtatFinancePersistant.class, financeId));
			
			httpUtil.setRequestAttribute("dateDebut", DateUtil.dateToString(etatFinanceBean.getDate_etat(), "MM/yyyy"));
			httpUtil.setRequestAttribute("etatFinanceBean", etatFinanceBean);
			httpUtil.setDynamicUrl("/domaine/dashboard_erp/compta/etat_financier.jsp");
			return;
		}
	*/	
		  String dtDebut = httpUtil.getParameter("dateDebut");
		 // Min et Max dates disponibles
		Date[] minMaxDate = journeeService.getMinMaxDate();
		
		if(StringUtil.isNotEmpty(httpUtil.getRequestAttribute("dateDebut"))) {
			dtDebut = (String) httpUtil.getRequestAttribute("dateDebut");
		}
		Date dateDebut = null;
		Date dateFin = null;
		
		if(StringUtil.isEmpty(dtDebut)){
			Calendar cal = DateUtil.getCalendar(new Date());
			dtDebut = (cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR);
		}
		
		dateDebut = DateUtil.stringToDate("01/"+dtDebut);
		dateFin = DateUtil.stringToDate(DateUtil.getMaxMonthDate(dateDebut)+"/"+dtDebut);
		
		if(minMaxDate[1] != null) {
			Calendar calendarDtFin = DateUtil.getCalendar(minMaxDate[1]);
			dtDebut = (calendarDtFin.get(Calendar.MONTH)+1)+"/"+calendarDtFin.get(Calendar.YEAR);
			dateDebut = DateUtil.stringToDate("01/"+dtDebut);
		}
		
		dateDebut = DateUtil.getStartOfDay(dateDebut);
		dateFin = DateUtil.getEndOfDay(dateFin);
		
		// Vérifier s'il existe déjà
		EtatFinanceBean etatFinanceBean = etatFinanceService.getEtatByDate(dateDebut);
		
		if(etatFinanceBean == null) {
			etatFinanceBean = etatFinanceService.getEtatFinanceBean(dateDebut, dateFin);
		}
		
		if(minMaxDate[0] != null){
			httpUtil.setRequestAttribute("minDate", DateUtil.getDiffMonth(new Date(), minMaxDate[0]));
		}
		if(minMaxDate[1] != null){
			httpUtil.setRequestAttribute("maxDate", DateUtil.getDiffMonth(new Date(), minMaxDate[1]));
		}
		
		httpUtil.setRequestAttribute("etatFinanceBean", etatFinanceBean);

		if(httpUtil.getParameter("dateDebut") != null) {
			httpUtil.setRequestAttribute("dateDebut", httpUtil.getParameter("dateDebut"));
		} else {
			httpUtil.setRequestAttribute("dateDebut", (dtDebut.length()==6 ? "0"+dtDebut:dtDebut));
		}
		httpUtil.setDynamicUrl("/domaine/dashboard_erp/compta/etat_financier.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void gestionPaiementEcheance(ActionUtil httpUtil){
		RequestTableBean cplxTableNonPointe = getTableBean(httpUtil, "list_paiement_echeance");
		List<PaiementPersistant> listEcheance = (List<PaiementPersistant>) dashBoardService.findByCriteriaByQueryId(cplxTableNonPointe, "list_paiement_echeance");
		List<PaiementPersistant> listEcheanceAll = (List<PaiementPersistant>) dashBoardService.findByCriteriaByQueryId(cplxTableNonPointe, "list_paiement_echeance", false);
		BigDecimal ttl_echeance = null;
		//
		for (PaiementPersistant mvp : listEcheanceAll) {
			ttl_echeance = BigDecimalUtil.add(ttl_echeance, mvp.getMontant());
		}
		httpUtil.setRequestAttribute("ttl_echeance", ttl_echeance);
		httpUtil.setRequestAttribute("listEcheance", listEcheance);
		
		httpUtil.setDynamicUrl("/domaine/dashboard_erp/compta/paiement_echeance.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void pointer_paiement_echeance(ActionUtil httpUtil) {
		httpUtil.setRequestAttribute("currentDate", DateUtil.getCurrentDate());
		String dateSt = httpUtil.getRequest().getParameter("date_paiement");
		Long elementId = httpUtil.getWorkIdLong();
		httpUtil.setRequestAttribute("elementId", elementId);
		
		if(dateSt == null){
			httpUtil.setDynamicUrl("/domaine/dashboard_erp/compta/echeance_pointe_popup.jsp");
			return;
		}
		
		Date datePointage = DateUtil.stringToDate(dateSt);
		//
		compteBancaireService.pointerPaiementEcheance(elementId, datePointage);
		
		gestionPaiementEcheance(httpUtil);
		
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "", "La mise à jour est effectuée.");
	}
	
	/**
	 * @param httpUtil
	 */
	public void gestionChequeNonPointe(ActionUtil httpUtil){
		RequestTableBean cplxTableNonPointe = getTableBean(httpUtil, "list_cheque_nonpointe");
		Map<Long, List<MouvementPersistant>> mapAllChequeNonPointe = new HashMap<>();
		List<PaiementPersistant> listDataNonPointe = (List<PaiementPersistant>) dashBoardService.findByCriteriaByQueryId(cplxTableNonPointe, "cheques_non_pointes_find");
		
		List<PaiementPersistant> listDataNonPointeAll = (List<PaiementPersistant>) dashBoardService.findByCriteriaByQueryId(cplxTableNonPointe, "cheques_non_pointes_find", false);
		BigDecimal ttl_Nonpointe = null;
		//
		for (PaiementPersistant mvp : listDataNonPointeAll) {
			ttl_Nonpointe = BigDecimalUtil.add(ttl_Nonpointe, mvp.getMontant());
		}
		httpUtil.setRequestAttribute("ttl_non_pointe", ttl_Nonpointe);
		httpUtil.setRequestAttribute("listDataNonPointe", listDataNonPointe);
		
		httpUtil.setDynamicUrl("/domaine/dashboard_erp/compta/etat_cheque.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void pointer_cheque(ActionUtil httpUtil) {
		Long elementId = httpUtil.getWorkIdLong();
		httpUtil.setRequestAttribute("currentDate", DateUtil.getCurrentDate());
		httpUtil.setRequestAttribute("elementId", elementId);
		
		String dateSt = httpUtil.getRequest().getParameter("date_encaissement");
		if(dateSt == null){
			httpUtil.setDynamicUrl("/domaine/dashboard_erp/compta/cheque_pointe_popup.jsp");
			return;
		}
		Date datePointage = DateUtil.stringToDate(dateSt);
		//
		compteBancaireService.pointerCheque(elementId, datePointage);
		
		gestionChequeNonPointe(httpUtil);
		
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "", "La mise à jour est effectuée.");
	}
	
	/**
	 * @param httpUtil
	 */
	public void evolution_banque(ActionUtil httpUtil){
		List<CompteBancairePersistant> listBanque = (List<CompteBancairePersistant>) dashBoardService.findAll(CompteBancairePersistant.class, Order.asc("libelle"));
		httpUtil.setRequestAttribute("list_banque", listBanque);
		
		Long banqueId = null;
		
		if(StringUtil.isEmpty(httpUtil.getParameter("curr_banque"))) {
			if(!listBanque.isEmpty()) {
				banqueId = listBanque.get(0).getId();
			}
		} else {
			banqueId = httpUtil.getLongParameter("curr_banque");
		}
		
		httpUtil.setRequestAttribute("curr_banque", banqueId);
		
		List<EcriturePersistant> banqueEvolution = dashBoardService.getBanqueEvolution(banqueId);
		httpUtil.setRequestAttribute("compteEvol", banqueEvolution);
		
		httpUtil.setDynamicUrl("/domaine/dashboard_erp/compta/evolution_compte.jsp");
	}
	
}
