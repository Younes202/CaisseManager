package appli.controller.domaine.personnel.action.paie;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.itextpdf.text.DocumentException;

import appli.controller.domaine.administration.bean.ValTypeEnumBean;
import appli.controller.domaine.personnel.bean.paie.SalariePaieBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.administration.service.IValTypeEnumService;
import appli.model.domaine.compta.persistant.PaiementPersistant;
import appli.model.domaine.personnel.persistant.EmployePersistant;
import appli.model.domaine.personnel.persistant.paie.PointagePersistant;
import appli.model.domaine.personnel.persistant.paie.SalairePersistant;
import appli.model.domaine.personnel.service.IEmployeService;
import appli.model.domaine.personnel.service.IPointageService;
import appli.model.domaine.personnel.service.ISalariePaieService;
import appli.model.domaine.personnel.service.impl.FichePaiePDF;
import appli.model.domaine.stock.persistant.TravauxPersistant;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.common.constante.ActionConstante;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.ModelConstante;

@WorkController(nameSpace="paie", bean=SalariePaieBean.class, jspRootPath="/domaine/personnel/paie/")
public class SalariePaieAction extends ActionBase {
	@Inject
	private IEmployeService employeService;
	@Inject
	private ISalariePaieService salariePaieService;
	@Inject
	private IValTypeEnumService valEnumService;
	@Inject
	private IPointageService pointageService;
	
	/**
	 * @param httpUtil
	 */
	public void work_init(ActionUtil httpUtil) {
		if(httpUtil.isEditionPage()) {
			List<ValTypeEnumBean> listeFinancement = valEnumService.getListValeursByType(ModelConstante.ENUM_FINANCEMENT);
			httpUtil.setRequestAttribute("listeFinancement", listeFinancement);
			//
			// Paiement
			if(httpUtil.isCrudOperation()){
				if(httpUtil.getViewBean() != null){
					((SalariePaieBean)httpUtil.getViewBean()).setList_paiement((List<PaiementPersistant>) httpUtil.getMenuAttribute("PAIEMENT_DATA"));
				}
			}
			
			httpUtil.setRequestAttribute("indemniteHisto", salariePaieService.getHistoLibelle("I"));
			httpUtil.setRequestAttribute("primeHisto", salariePaieService.getHistoLibelle("P"));
		}
		
		if(httpUtil.getParameter("tpMnu") != null) {
			httpUtil.setMenuAttribute("tpMnu", httpUtil.getParameter("tpMnu"));
		}
		String tpMnu = (httpUtil.getMenuAttribute("tpMnu") == null ? "H" : (String) httpUtil.getMenuAttribute("tpMnu"));
		httpUtil.setMenuAttribute("tpMnu", tpMnu);
		
		// Si on arrive la prmière fois sur l'écran
		String pattern = "MM/yyyy";
		if("M".equals(tpMnu)){
			pattern = "yyyy";
		} else if("H".equals(tpMnu)){
			pattern = "dd/MM/yyyy";
		}
		
		if(httpUtil.getParameter("isFltr") != null) {
			String dtStr = httpUtil.getParameter("dateDebut");
			if(StringUtil.isNotEmpty(dtStr)){
				httpUtil.setMenuAttribute("CURR_DATE", dtStr);
				httpUtil.setMenuAttribute("CURR_DATE_DT", DateUtil.stringToDate(dtStr, pattern));
			} else {
				Date currentDate = DateUtil.stringToDate(DateUtil.dateToString(DateUtil.getCurrentDate(), pattern), pattern);
				httpUtil.setMenuAttribute("CURR_DATE", DateUtil.dateToString(currentDate, pattern));
				httpUtil.setMenuAttribute("CURR_DATE_DT", currentDate);
			}
			String employeId = httpUtil.getParameter("employe");
			if(StringUtil.isNotEmpty(employeId)) {
				httpUtil.setMenuAttribute("CURR_EMPL", Long.valueOf(employeId));
			} else {
				httpUtil.removeMenuAttribute("CURR_EMPL");
			}
			
			String employe_cin = httpUtil.getParameter("cin");
			if(StringUtil.isNotEmpty(employe_cin)){
				httpUtil.setMenuAttribute("CURR_CIN", employe_cin);
			} else {
				httpUtil.removeMenuAttribute("CURR_CIN");
			}
		}
		
		if(httpUtil.getMenuAttribute("CURR_DATE") == null) {
			String dateStr = DateUtil.dateToString(DateUtil.getCurrentDate(), pattern);
			Date currentDate = DateUtil.stringToDate(dateStr, pattern);
			httpUtil.setMenuAttribute("CURR_DATE", dateStr);
			httpUtil.setMenuAttribute("CURR_DATE_DT", currentDate);
		}
		
		// Requête avec paggination ----------------------------------------------
		String currDt = (String) httpUtil.getMenuAttribute("CURR_DATE");
		
		if(!(currDt.length() == 4 && tpMnu.equals("M"))
				&& !(currDt.length() == 10 && tpMnu.equals("H"))
				&& !(currDt.length() == 5 && (tpMnu.equals("J") || tpMnu.equals("JD")))) {
			String dateStr = DateUtil.dateToString(DateUtil.getCurrentDate(), pattern);
			Date currentDate = DateUtil.stringToDate(dateStr, pattern);
			httpUtil.setMenuAttribute("CURR_DATE", dateStr);
			httpUtil.setMenuAttribute("CURR_DATE_DT", currentDate);
		}
		
		currDt = (String) httpUtil.getMenuAttribute("CURR_DATE");
		Long currEmpl = (Long) httpUtil.getMenuAttribute("CURR_EMPL");
		Long currCin = (Long) httpUtil.getMenuAttribute("CURR_CIN");
		
		String requete = "from EmployePersistant employe where "
				+ " ((employe.is_disable is null or employe.is_disable=0) "
				+ "or (employe.is_disable=1 and employe.date_sortie >='[dt_debut1]'))"
				+ " AND (employe.date_sortie is null or employe.date_sortie>='[dt_debut2]') ";
		
		if(currEmpl != null){
			requete = requete+" AND employe.id = '[iid]'";
		}
		if(StringUtil.isNotEmpty(currCin)){
			requete = requete+" AND employe.cin = '[ccin]'";
		}
		requete = requete+ "ORDER BY employe.numero";
		
		RequestTableBean cplxTable = getTableBean(httpUtil, "paie_table_body");
		Date firstDayOfYear = DateUtil.stringToDate(("M".equals(tpMnu) ? "01/01/" : "01/")+currDt);
		Map<String, Object> formCriterion = cplxTable.getFormBean().getFormCriterion();
		
		formCriterion.put("dt_debut1", firstDayOfYear);
		formCriterion.put("dt_debut2", firstDayOfYear);
		
		if(currEmpl != null){
			formCriterion.put("iid", currEmpl);
		}
		if(StringUtil.isNotEmpty(currCin)){
			formCriterion.put("ccin", currCin);
		}
		//
		List<EmployePersistant> listEmployePagger = (List<EmployePersistant>) employeService.findByCriteria(cplxTable, requete);
		List<EmployePersistant> listEmployePaggerNoLimite = (List<EmployePersistant>) employeService.findByCriteria(cplxTable, requete, false);
		
		int idxCpt = 1;
		for (EmployePersistant employePersistant : listEmployePaggerNoLimite) {
			employePersistant.setIdx(idxCpt);
			idxCpt++;
		}
		
		httpUtil.setRequestAttribute("listEmployePagger", listEmployePagger);
		httpUtil.setRequestAttribute("listEmployeCombo", employeService.getListEmployeActifs());
		// ----------------------------------------------FIN
				
		httpUtil.setMenuAttribute("IS_PAIE_MNU", true);
	}
	
	public void loadVueJour(ActionUtil httpUtil) {
		String mnu = (String)httpUtil.getMenuAttribute("tpMnu");
		Date currDt = (Date) httpUtil.getMenuAttribute("CURR_DATE_DT");
		List<EmployePersistant> listEmployePagger = employeService.getListEmployeActifs(); //(List<EmployePersistant>) httpUtil.getRequestAttribute("listEmployePagger");
		
		List<PointagePersistant> listPointage = pointageService.getListPointage(listEmployePagger, currDt);
		Map<String, Object> mapDetPointage= pointageService.getPointageHoraire(listEmployePagger, currDt);
		
		Map<String, PointagePersistant> mapDataPointage = new HashMap<String, PointagePersistant>();
		for (PointagePersistant pointagePersistant : listPointage) {
			String key = pointagePersistant.getOpc_employe().getId()
							+ "-"+pointagePersistant.getType()
							+"-"+DateUtil.dateToString(pointagePersistant.getDate_event());
			//
			mapDataPointage.put(key, pointagePersistant);
		}
		
		httpUtil.setRequestAttribute("mapDataPointage", mapDataPointage);
		httpUtil.setRequestAttribute("mapHorairePointage", mapDetPointage.get("mapHorairePointage"));
		httpUtil.setRequestAttribute("mapTotalHorairePointage", mapDetPointage.get("mapTotalHorairePointage"));
		
		if("H".equals(mnu)) {
			httpUtil.setDynamicUrl("/domaine/personnel/paie/pointage_heure.jsp");
		} else if("JD".equals(mnu)){
			httpUtil.setDynamicUrl("/domaine/personnel/paie/pointage_jour_detail.jsp");
		} else {
			httpUtil.setDynamicUrl("/domaine/personnel/paie/pointage_jour.jsp");
		}
	}
	
//	/**
//	 * @param httpUtil
//	 */
//	public void loadVueSemaine(ActionUtil httpUtil) {
//		int startIdx = 0, nbrElmnts = 7, nbr = 7;
//		
//		if(StringUtil.isEmpty(httpUtil.getParameter("pg"))){
//			if(httpUtil.getMenuAttribute("currPage") == null) {
//				Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
//			    int day = calendar.get(Calendar.DATE);
//				int currPage =  (day%nbr == 0) ? (day / nbr) :  (day / nbr + 1) ;
//				startIdx = ( (currPage-1) * nbrElmnts)+1;
//				httpUtil.setMenuAttribute("currPage", currPage+"");
//			} else {
//				int currPage = Integer.valueOf((String) httpUtil.getMenuAttribute("currPage"));
//				startIdx = ( (currPage-1) * nbrElmnts)+1;
//			}
//		} else {
//			String page = httpUtil.getParameter("pg");
//			startIdx = ( (Integer.valueOf(page)-1) * nbrElmnts)+1;
//			httpUtil.setMenuAttribute("currPage", page);
//		}
//		
////		EtablissementPersistant resraurantBean = ContextGloabalAppli.getEtablissementBean();
////		if(StringUtil.isEmpty(resraurantBean.getPointeuse_db_path()) && 
////				(StringUtil.isEmpty(resraurantBean.getPointeuse_ip())
////				|| StringUtil.isEmpty(resraurantBean.getPointeuse_port()))){
////			return;
////		}
//		
//		String currDt = (String) httpUtil.getMenuAttribute("CURR_DATE");
//		Long currEmpl = (Long) httpUtil.getMenuAttribute("CURR_EMPL");
//		
//		Date currentDate = DateUtil.stringToDate(currDt, "MM/yyyy");
//		Map[] tableauMap = pointageService.getListPointageEmploye(currEmpl, currentDate, startIdx);
//		httpUtil.setRequestAttribute("tableauMap", tableauMap);
//		
//		httpUtil.setDynamicUrl("/domaine/personnel/paie/pointage_semaine.jsp");
//	}
	
	public void loadVueMois(ActionUtil httpUtil){
		String[] moisArray = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", 
						"Août", "Septembre", "Octobre", "Novembre", "Decembre"};
		Map<String, String> mapMois = new LinkedHashMap<>();
		int idxM = 1;
		for (String mois : moisArray) {
			mapMois.put((idxM<10?"0"+idxM:""+idxM), mois);
			idxM++;
		}

		String dt = ""+httpUtil.getMenuAttribute("CURR_DATE");
		if(dt.indexOf("/") != -1) {
			dt = dt.substring(dt.indexOf("/")+1);
		}
		if(dt.indexOf("/") != -1) {
			dt = dt.substring(dt.indexOf("/")+1);
		}
		Integer currDt = Integer.valueOf(dt);
		
		Map<String, SalairePersistant> mapPaie = salariePaieService.getMapPaie(currDt);
		Map<Integer, Boolean> mapEtatMois = salariePaieService.getMapEtatMois(currDt);
		
		httpUtil.setRequestAttribute("mapMois", mapMois);
		httpUtil.setRequestAttribute("mapEtatMois", mapEtatMois);
		httpUtil.setRequestAttribute("mapPaie", mapPaie);
		
		httpUtil.setDynamicUrl("/domaine/personnel/paie/pointage_mois.jsp");
	}
	
	@Override
	public void work_merge(ActionUtil httpUtil) {		
		String idxMois = httpUtil.getParameter("idxMois");
		Long employe_id = httpUtil.getLongParameter("employe_id");
		
		SalariePaieBean salaireB = (SalariePaieBean) httpUtil.getViewBean();
		salaireB.setId(httpUtil.getWorkIdLong());
		
		EmployePersistant employeP = (EmployePersistant) employeService.findById(EmployePersistant.class, employe_id);
		salaireB.setMois(Integer.valueOf(idxMois));
		salaireB.setAnnee(DateUtil.getCalendar(new Date()).get(Calendar.YEAR));
		salaireB.setOpc_employe(employeP);
		
		// Si depuis travaux
		if(httpUtil.getMenuAttribute("IS_TRV_MNU") != null) {
			salaireB.setOpc_travaux((TravauxPersistant) httpUtil.getMenuAttribute("IS_TRV_MNU"));
		}
		salaireB.setList_paiement((List<PaiementPersistant>) httpUtil.getMenuAttribute("PAIEMENT_DATA"));
		
		salariePaieService.merge(salaireB);
		//
		
		if(httpUtil.getMenuAttribute("IS_TRV_MNU") != null) {// Si depuis travaux
			httpUtil.setRequestAttribute("bck",true);
			httpUtil.setDynamicUrl("stock.travaux.work_init_update");
			return;
		} else {
			httpUtil.setRequestAttribute("currEmployePaie", salaireB);
			httpUtil.setDynamicUrl("/domaine/personnel/paie/salariePaie_td_include.jsp");
		}
	}
	
	@Override
	public void work_delete(ActionUtil httpUtil) {
		super.work_delete(httpUtil);
		//
		httpUtil.setDynamicUrl("/domaine/personnel/paie/salariePaie_td_include.jsp");
	}
	
	public void downloadEmployePaiePDF(ActionUtil httpUtil) throws IOException, DocumentException{
		Integer mois = Integer.valueOf(httpUtil.getParameter("mois"));
		Integer annee = Integer.valueOf(httpUtil.getParameter("annee"));
		EtablissementPersistant etsP = ContextAppli.getEtablissementBean();
		
		String modeExport = httpUtil.getParameter("mode");
		List<SalairePersistant> listEmplPaie = new ArrayList<>();
		
		if(modeExport.equals("allMonth")){
			List<SalairePersistant> emplPaie = salariePaieService.getPaieByYearMonth(annee, mois);
			if(emplPaie != null) {
				listEmplPaie = emplPaie;
			} else {
				MessageService.addGrowlMessage(MSG_TYPE.ERROR, "", "Aucune paie disponible pour le mois "+ mois);
				return;
			}
		} else if(modeExport.equals("oneMonth")) {
			Long employe_id = httpUtil.getLongParameter("empl");
			
			SalairePersistant emplPaie = salariePaieService.getPaieByEmployeYearMonth(employe_id, annee, mois);
			if(emplPaie != null){
				listEmplPaie.add(emplPaie);
			} else{
				SalairePersistant salarieP = new SalairePersistant();
				salarieP.setOpc_employe((EmployePersistant) employeService.findById(EmployePersistant.class, employe_id));
				listEmplPaie.add(salarieP);
			}
		}
		
		httpUtil.doDownload(new FichePaiePDF().exportPdf(etsP, listEmplPaie, modeExport), true);
	}
	
	public void calculSalaireFromPointage(ActionUtil httpUtil) {
		Date currDt = (Date) httpUtil.getMenuAttribute("CURR_DATE_DT");
		Integer mois = httpUtil.getIntParameter("mois");
		Calendar cal = DateUtil.getCalendar(currDt);
		cal.set(Calendar.MONTH, (mois - 1));
		
		salariePaieService.calculSalaireFromPointage(cal.getTime());
		
		loadVueMois(httpUtil);
	}
	
	/**
	 * @param httpUtil
	 */
	public void clore_mois(ActionUtil httpUtil) {
		Integer currAnnee = (Integer) httpUtil.getMenuAttribute("CURR_DATE");
		String idxMois = httpUtil.getParameter("idxMois");
		salariePaieService.cloreMois(currAnnee, Integer.valueOf(idxMois));
		//
		Map<Integer, Boolean> mapEtatMois = salariePaieService.getMapEtatMois(currAnnee);
		httpUtil.setMenuAttribute("mapEtatMois", mapEtatMois);
		
		loadVueMois(httpUtil);
	}
	
	public void work_init_update(ActionUtil httpUtil) {
		editDataMois(httpUtil);
		httpUtil.setDynamicUrl("/domaine/personnel/paie/salariePaie_edit.jsp");
	}
	
	public void editDataMois(ActionUtil httpUtil) {
		String idxMois = httpUtil.getParameter("idxMois");
		Long employe_id = httpUtil.getLongParameter("empl");
		int year = DateUtil.getCalendar(new Date()).get(Calendar.YEAR);
		Integer annee = StringUtil.isEmpty(httpUtil.getParameter("annee")) ?
				year
				: Integer.valueOf(httpUtil.getParameter("annee"));
		
		SalariePaieBean salairePaieBean = null;
		int lastDay = DateUtil.getMaxMonthDate(DateUtil.stringToDate( "01/"+idxMois+"/"+year));
		SalairePersistant salaireP = salariePaieService.getPaieByEmployeYearMonth(employe_id, annee, Integer.valueOf(idxMois));
		if(salaireP != null) {
			salairePaieBean = ServiceUtil.persistantToBean(SalariePaieBean.class, salaireP);
		} else {
			salairePaieBean = new SalariePaieBean();
		}
		
		if(salaireP != null) {
			SalairePersistant salaire_ref = employeService.getLastSalaire(salaireP.getOpc_employe().getId());
			if(salaire_ref != null) {
				if(salaire_ref.getNbr_conge() != null) {
					BigDecimal nbrJourTrv = BigDecimalUtil.substract(BigDecimalUtil.get(lastDay), salaire_ref.getNbr_conge());
					salairePaieBean.setNbr_jours(nbrJourTrv.intValue());
				}
			}
		} else {
			SalairePersistant lastSalaire = employeService.getLastSalaire(employe_id);
			if(lastSalaire != null) {
				salairePaieBean.setTarif_jour(lastSalaire.getTarif_jour());
			}
			
			salairePaieBean.setDate_paiement(new Date());
			salairePaieBean.setDate_debut(DateUtil.stringToDate( "01/"+idxMois+"/"+year));
			salairePaieBean.setDate_fin(DateUtil.stringToDate( lastDay+"/"+idxMois+"/"+year));
			salairePaieBean.setNbr_jours(BigDecimalUtil.get(lastDay).intValue());				
		}
		
		httpUtil.setViewBean(salairePaieBean);
		httpUtil.setRequestAttribute("idxMois", idxMois);
		httpUtil.setRequestAttribute("employe_id", employe_id);
	}
	
	public void work_post(ActionUtil httpUtil) {
		if(httpUtil.isEditionPage()) {
			if(ActionConstante.EDIT.equals(httpUtil.getAction()) 
						|| ActionConstante.INIT_CREATE.equals(httpUtil.getAction())) {
				editDataMois(httpUtil);
			}
		}
	}
}
