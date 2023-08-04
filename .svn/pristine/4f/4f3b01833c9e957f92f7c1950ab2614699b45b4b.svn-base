package appli.controller.domaine.stock.action;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.criterion.Order;

import appli.controller.domaine.administration.bean.EtatFinanceBean;
import appli.controller.domaine.stock.bean.CentraleSynchroBean;
import appli.controller.domaine.util_erp.ContextAppli.SOFT_ENVS;
import appli.model.domaine.stock.persistant.DemandeTransfertPersistant;
import appli.model.domaine.stock.persistant.centrale.CentraleEtsPersistant;
import appli.model.domaine.stock.persistant.centrale.CentraleSynchroPersistant;
import appli.model.domaine.stock.service.ICentraleSynchroService;
import appli.model.domaine.stock.service.IDemandeTransfertService;
import appli.model.domaine.vente.persistant.JourneePersistant;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.DateUtil.TIME_ENUM;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;

@WorkController(nameSpace="stock", bean=CentraleSynchroBean.class, jspRootPath="/domaine/stock/centrale")
public class CentraleSynchroAction extends ActionBase {
	@Inject
	private ICentraleSynchroService centraleService;
	@Inject
	private IDemandeTransfertService demandeTrService;
	
	public void work_init(ActionUtil httpUtil) {	
		httpUtil.setRequestAttribute("listEts", centraleService.findActifsCentrale());
		
		boolean isRestau = SOFT_ENVS.restau.toString().equals(StrimUtil.getGlobalConfigPropertie("context.soft"));
		if(isRestau){
			httpUtil.setRequestAttribute("synchroList", new String[][]{
//				{"FAM", "Familles"},
				{"ART", "Article"}, 
				{"MNU", "Menus"},
				{"LCHOIX", "Liste de choix"},
				{"FOURN", "Fournisseur"},
				{"VENTE", "Vente"}
			});	
		} else {
			httpUtil.setRequestAttribute("synchroList", new String[][]{
//				{"FAM", "Familles"},
				{"ART", "Article"},
				{"FOURN", "Fournisseur"},
				{"VENTE", "Vente"}
			});
		}
	}

	@Override
	public void work_merge(ActionUtil httpUtil) {
		 boolean isDis = httpUtil.getParameter("is_disabled") != null;	
		 String[] synchroElmnts = httpUtil.getRequest().getParameterValues("synchro_elmnts");
		 String[] synchroEts = httpUtil.getRequest().getParameterValues("synchro_ets");
		 
		centraleService.addElementsToSynchronise(synchroElmnts, synchroEts, isDis);
		
		work_find(httpUtil);
	}
	
	public void synchroEts(ActionUtil httpUtil) {
		centraleService.loadEtsCentrale();
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "", "Les établissements ont été rechargés.");
		work_find(httpUtil);
	}
	
	public void synchroAll(ActionUtil httpUtil) {
		Long syncId = httpUtil.getWorkIdLong();
		String retour = centraleService.synchroniserOutAll(syncId);
		
		if(StringUtil.isEmpty(retour)) {
			retour = "Aucune donnée n'a été synchronisée.";
		}
		
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "", retour);
		work_find(httpUtil);
	}
	
	public void loadChiffresEts(ActionUtil httpUtil) {
		RequestTableBean cplxTableJ = getTableBean(httpUtil, "list_journee");
		RequestTableBean cplxTableE = getTableBean(httpUtil, "list_etat");
		//----------------------------- Date -------------------------
		Date dateDebut = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateDebut"));
		Date dateFin = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateFin"));
		boolean isDbFin = true;
		if(dateDebut == null) {
			Date dateLast = new Date();
			dateDebut = (httpUtil.getMenuAttribute("dateDebut")==null ? dateLast : (Date)httpUtil.getMenuAttribute("dateDebut"));
			dateFin = (httpUtil.getMenuAttribute("dateFin")==null ? dateLast : (Date)httpUtil.getMenuAttribute("dateFin"));
		} else if(httpUtil.getRequest().getParameter("prev") != null) {
			dateDebut = DateUtil.addSubstractDate(dateDebut, TIME_ENUM.DAY, -1);
			dateFin = DateUtil.addSubstractDate(dateFin, TIME_ENUM.DAY, -1);
		} else if(httpUtil.getRequest().getParameter("next") != null) {
			dateDebut = DateUtil.addSubstractDate(dateDebut, TIME_ENUM.DAY, 1);
			dateFin = DateUtil.addSubstractDate(dateFin, TIME_ENUM.DAY, 1);
		} else{
			isDbFin = false;
		}
		if(isDbFin){
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
		httpUtil.setMenuAttribute("dateDebut", dateDebut);
		httpUtil.setMenuAttribute("dateFin", dateFin);
		
		List<CentraleEtsPersistant> listEts = centraleService.findAll(CentraleEtsPersistant.class, Order.asc("nom"));
		int cptJ = 0;
		for (CentraleEtsPersistant centraleEtsP : listEts) {
			if(BooleanUtil.isTrue(centraleEtsP.getIs_disable())) {
				continue;
			}
			
			List<JourneePersistant> listJournee = centraleService.getJourneeInfosForCentrale(centraleEtsP, dateDebut, dateFin);
			JourneePersistant journeeTotal = getTotalJournee(listJournee);
			EtatFinanceBean etatB = centraleService.getEtatForCentrale(centraleEtsP, dateDebut, dateFin);
			
			cptJ++;
			
			httpUtil.setRequestAttribute(centraleEtsP.getNom()+"_listJ", listJournee);			
			httpUtil.setRequestAttribute(centraleEtsP.getNom()+"_etat", etatB);
			httpUtil.setRequestAttribute(centraleEtsP.getNom()+"_totalJ", journeeTotal);
		}
		httpUtil.setRequestAttribute("listEts", listEts);

		cplxTableJ.setDataSize(cptJ);
		cplxTableE.setDataSize(cptJ);
		
		httpUtil.setDynamicUrl("/domaine/stock/centrale/centraleSynchro_chiffres.jsp");
	}
	private JourneePersistant getTotalJournee(List<JourneePersistant> listDataAll) {
		JourneePersistant jp = new JourneePersistant();
		
		if(listDataAll == null) {
			return jp;
		}
		
	   	for (JourneePersistant jvp : listDataAll) {
	   		jp.setMtt_reduction(BigDecimalUtil.add(jp.getMtt_reduction(), jvp.getMtt_reduction()));
	   		jp.setMtt_art_offert(BigDecimalUtil.add(jp.getMtt_art_offert(), jvp.getMtt_art_offert()));
	   		jp.setMtt_livraison(BigDecimalUtil.add(jp.getMtt_livraison(), jvp.getMttLivraisonGlobal()));
	   		//
	   		jp.setNbr_vente((jp.getNbr_vente()!=null?jp.getNbr_vente():0)+ (jvp.getNbr_vente()!=null?jvp.getNbr_vente():0));
	   		jp.setMtt_ouverture(BigDecimalUtil.add(jp.getMtt_ouverture(), jvp.getMtt_ouverture()));
	   		
	   		// CALCULE
	   		BigDecimal mttTotalNet = jvp.getMtt_total_net();//, jvp.getMtt_livraison());
	   		jp.setMtt_total_net(BigDecimalUtil.add(jp.getMtt_total_net(), mttTotalNet));
   			
	   		// CLOTURE
	   		if("C".equals(jvp.getStatut_journee())){
	   			BigDecimal netCloture = BigDecimalUtil.substract(jvp.getMtt_cloture_caissier(), jvp.getMtt_ouverture());
	   			jp.setMtt_cloture_caissier(BigDecimalUtil.add(jp.getMtt_cloture_caissier(), netCloture));
	   			
	   			// ECART
	   			BigDecimal mttEcar = jvp.getEcartNet();
		   		jp.setMtt_total(BigDecimalUtil.add(jp.getMtt_total(), mttEcar));
	   		}
	   		// MARGE
	   		BigDecimal mttMarge = jp.getMtt_total_achat()!=null ? BigDecimalUtil.substract(jp.getMtt_total_net(),jp.getMttLivraisonPartLivreur(), jp.getMtt_total_achat())
						: BigDecimalUtil.substract(jvp.getMtt_total_net(), jvp.getMttLivraisonPartLivreur(), jvp.getMtt_total_achat());
	   		jp.setMtt_cloture_caissier_cb(BigDecimalUtil.add(jp.getMtt_cloture_caissier_cb(), mttMarge));
	   		
	   		jp.setMtt_donne_point(BigDecimalUtil.add(jp.getMtt_donne_point(), jvp.getMtt_donne_point()));
   			jp.setMtt_portefeuille(BigDecimalUtil.add(jp.getMtt_portefeuille(), jvp.getMtt_portefeuille()));
   			
   			jp.setMtt_annule(BigDecimalUtil.add(jp.getMtt_annule(), jvp.getMtt_annule()));
   			jp.setMtt_annule_ligne(BigDecimalUtil.add(jp.getMtt_annule_ligne(), jvp.getMtt_annule_ligne()));
		} 
	   	return jp;
	}
	
	/**
	 * @param httpUtil
	 */
	public void dash_demandeTrans(ActionUtil httpUtil) {
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_demandeTransfert2");
		
		List<DemandeTransfertPersistant> listData = (List<DemandeTransfertPersistant>) demandeTrService.findByCriteriaByQueryId(cplxTable, "demande_dash_find");
		httpUtil.setRequestAttribute("list_demandeTransfert", listData);
		
		List<DemandeTransfertPersistant> listAllDmdNonTransfere = demandeTrService.getListDemandeNonTransfere();
		Integer nbr_demande = (Integer) httpUtil.getMenuAttribute("old_nbr_demande");
		if(nbr_demande != null && nbr_demande != listAllDmdNonTransfere.size()) {
			httpUtil.setRequestAttribute("is_new_added", true);
			httpUtil.setMenuAttribute("old_nbr_demande", listAllDmdNonTransfere.size());
		}
		
		httpUtil.setDynamicUrl("/domaine/dashboard/dashboard_demandeTrans_include.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void dash_synchro(ActionUtil httpUtil) {
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_synchroElement");
		
		String req = "from CentraleSynchroPersistant synchroElement where "
				+ "date_synchro is null "
				+ "order by synchroElement.synchro.opc_centrale_ets.nom, id desc";
		
		//cplxTable.getFormBean().getFormCriterion().put("paramEtat", "'%:0%'");
		List<CentraleSynchroPersistant> listData = (List<CentraleSynchroPersistant>) centraleService.findByCriteria(cplxTable, req);
		
		httpUtil.setRequestAttribute("list_synchroElement", listData);
		httpUtil.setDynamicUrl("/domaine/dashboard/dashboard_synchro_include.jsp");
	}
}
