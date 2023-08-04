package appli.controller.domaine.caisse.action;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import appli.controller.domaine.administration.bean.EtatFinanceBean;
import appli.model.domaine.administration.persistant.EtatFinancePersistant;
import appli.model.domaine.administration.service.IEtatFinanceService;
import appli.model.domaine.caisse.service.IJourneeService;
import appli.model.domaine.vente.persistant.JourneePersistant;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StringUtil;

@WorkController(nameSpace = "caisse", bean = EtatFinanceBean.class, jspRootPath = "/domaine/caisse/etat/")
public class EtatFinanceAction extends ActionBase {
	@Inject
	private IEtatFinanceService etatFinanceService;
	@Inject
	private IJourneeService journeeService;
	
	public void work_init(ActionUtil httpUtil) {
		httpUtil.setRequestAttribute("moisVente", new JourneePersistant());
		
		if(!httpUtil.isSubmit()){
			httpUtil.setRequestAttribute("dateDebut", DateUtil.dateToString(DateUtil.getCurrentDate(), "MM/yyyy"));
		}
		
		Date[] minMaxDate = journeeService.getMinMaxDate();
		if(minMaxDate[0] != null){
			httpUtil.setRequestAttribute("minDate", DateUtil.getDiffMonth(new Date(), minMaxDate[0]));
		}
		if(minMaxDate[1] != null){
			httpUtil.setRequestAttribute("maxDate", DateUtil.getDiffMonth(new Date(), minMaxDate[1]));
		}
	}
	
	@Override
	public void work_find(ActionUtil httpUtil) {
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_etat_finance");
		List<EtatFinancePersistant> listData = (List<EtatFinancePersistant>) journeeService.findByCriteriaByQueryId(cplxTable, "etat_finance_find");
		httpUtil.setRequestAttribute("list_etat_finance", listData);
	   	
		EtatFinancePersistant efTotal = new EtatFinancePersistant();
	   	List<EtatFinancePersistant> listDataAll = (List<EtatFinancePersistant>) journeeService.findByCriteriaByQueryId(cplxTable, "etat_finance_find", false);
	   	for (EtatFinancePersistant efP : listDataAll) {
	   		BigDecimal[] resultat = efP.calculTotalRecetteDepense();
	   		BigDecimal[] etats = efP.calculTotalEtat();
	   		
	   		efTotal.setMtt_recette_divers(BigDecimalUtil.add(efTotal.getMtt_recette_divers(), resultat[0]));
	   		efTotal.setMtt_depense_divers(BigDecimalUtil.add(efTotal.getMtt_depense_divers(), resultat[1]));
	   		efTotal.setMtt_resultat_net(BigDecimalUtil.add(efTotal.getMtt_resultat_net(), resultat[2]));
	   		
	   		efTotal.setMtt_achat(BigDecimalUtil.add(efTotal.getMtt_achat(), etats[0]));
	   		efTotal.setMtt_avoir(BigDecimalUtil.add(efTotal.getMtt_avoir(), etats[1]));
	   		efTotal.setMtt_salaire(BigDecimalUtil.add(efTotal.getMtt_salaire(), etats[2]));
		}
	   	httpUtil.setRequestAttribute("etat_finance_total", efTotal);
	   	//
	   //	loadFinanceDetail(httpUtil);
	   	Calendar cal = DateUtil.getCalendar(new Date());
		Date dateDebut = DateUtil.stringToDate("01/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR));
		EtatFinanceBean etatFinanceBean = etatFinanceService.getEtatByDate(dateDebut);
		
		httpUtil.setRequestAttribute("etatFinanceBean", etatFinanceBean);
		
		httpUtil.setDynamicUrl("/domaine/caisse/etat/etatFinance_list.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void loadFinanceDetail(ActionUtil httpUtil){
		Long financeId = httpUtil.getWorkIdLong();
		if(financeId != null){
			EtatFinanceBean etatFinanceBean = (EtatFinanceBean)ServiceUtil.persistantToBean(EtatFinanceBean.class, journeeService.findById(EtatFinancePersistant.class, financeId));
			
			httpUtil.setRequestAttribute("dateDebut", DateUtil.dateToString(etatFinanceBean.getDate_etat(), "MM/yyyy"));
			httpUtil.setRequestAttribute("etatFinanceBean", etatFinanceBean);
			httpUtil.setDynamicUrl("/domaine/caisse/etat/etatFinance_edit.jsp");
			return;
		}
		
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
		httpUtil.setDynamicUrl("/domaine/caisse/etat/etatFinance_edit.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void annuler_clore_mois(ActionUtil httpUtil) {
		boolean reponse = MessageService.addDialogConfirmMessage("CLORE_MOIS", "caisse.etatFinance.annuler_clore_mois", 
				"Annulation clôture du mois", "Vous êtes sur le point d'annuler la clôture de ce mois.<br>Vous-les vous continuer ?");
		Long etatId = httpUtil.getWorkIdLong();
		if(etatId != null) {
			httpUtil.setMenuAttribute("CURR_ETAT", etatId);
		}
		if(reponse) {
			etatFinanceService.annulerClotureMois((Long)httpUtil.getMenuAttribute("CURR_ETAT"));
			MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "Clôture annulée", "La clôture de l'état est annulée.");
		}
		work_find(httpUtil);
	}
	
	/**
	 * @param httpUtil
	 */
	public void clore_mois(ActionUtil httpUtil) {
		httpUtil.setRequestAttribute("dateDebut", httpUtil.getParameter("dateDebut"));
		
		boolean reponse = MessageService.addDialogConfirmMessage("CLORE_MOIS", "caisse.etatFinance.clore_mois", 
				"Clôture du mois", "Vous êtes sur le point de clore ce mois.<br>Vous-les vous continuer ?");
		if(reponse) {
			String dtDebut = httpUtil.getParameter("dateDebut");
			Date dateDebut = DateUtil.stringToDate("01/"+dtDebut);
			Date dateFin = DateUtil.stringToDate(DateUtil.getMaxMonthDate(dateDebut)+"/"+dtDebut);
			
			etatFinanceService.cloreMois(dateDebut, dateFin);
			MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "Clôture annulée", "Le mois est clôturé.");
		}
		work_find(httpUtil);
	}
}
