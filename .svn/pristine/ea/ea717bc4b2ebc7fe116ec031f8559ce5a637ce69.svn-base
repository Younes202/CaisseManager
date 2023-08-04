package appli.controller.domaine.personnel.action.paie;

import java.io.File;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.inject.Inject;

import org.hibernate.criterion.Order;

import appli.controller.domaine.personnel.bean.PointageBean;
import appli.model.domaine.personnel.persistant.EmployePersistant;
import appli.model.domaine.personnel.service.IEmployeService;
import appli.model.domaine.personnel.service.IPointageService;
import appli.softs.checkers.Pointeuse;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.ContextGloabalAppli;
import framework.controller.annotation.WorkController;
import framework.controller.annotation.WorkForward;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.service.MessageService;
import framework.model.common.util.DateUtil;
import framework.model.common.util.DateUtil.TIME_ENUM;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.audit.ReplicationGenerationEventListener;

@WorkController(nameSpace = "pers", bean = PointageBean.class, jspRootPath = "/domaine/personnel/")
public class PointageAction extends ActionBase {
	@Inject
	private IPointageService pointageService;
	@Inject
	private IEmployeService employeService;
	
	/**
	 * @param httpUtil
	 * @throws Exception
	 */
	public void work_init(ActionUtil httpUtil) {
		httpUtil.setFormReadOnly(false);
		
		String[][] typeCongeArray = {{"P", "Payé"}, {"NP", "Sans solde"}};
		String[][] typeSanctionArray = {{"R", "Retenue"}, {"B", "Blâme"}, {"A", "Autre"}};
		httpUtil.setRequestAttribute("typeCongeArray", typeCongeArray);
		httpUtil.setRequestAttribute("typeSanctionArray", typeSanctionArray);
		
		String[][] dureeArray = {{"1.0", "1"}, {"0.5", "0.5"}, {"0", "0"}};
		httpUtil.setRequestAttribute("dureeArray", dureeArray);
	}
	
	/**
	 * @param httpUtil
	 */
//	public void loadDataPointeuse(ActionUtil httpUtil) {
//		IPointageService pointageSrv = ServiceUtil.getBusinessBean(IPointageService.class);
//		String dtStr = httpUtil.getParameter("dateDebut");
//		EtablissementPersistant resraurantBean = ContextGloabalAppli.getEtablissementBean();
//		if(StringUtil.isEmpty(dtStr) || (StringUtil.isEmpty(resraurantBean.getPointeuse_db_path())
//				&& (StringUtil.isEmpty(resraurantBean.getPointeuse_ip())
//						|| StringUtil.isEmpty(resraurantBean.getPointeuse_port())))){
//			return;
//		}
//		
//		boolean isCloud = !"local".equals(StrimUtil.getGlobalConfigPropertieIgnoreErreur("context.install"));
//		if(isCloud) {
//			httpUtil.setRequestAttribute("IS_CLOUD_POINTAGE", true);
//		} else {
//			Date currentDate = DateUtil.stringToDate(dtStr, "MM/yyyy");
//			pointageSrv.loadDataPointeuse(currentDate);
//			// rafraish
//			refreshPointage(httpUtil);
//		}
//		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "Importation données", "L'import des données de la pointeuse est lancé.");
//	}
	
	/**
	 * @param httpUtil
	 */
	public void loadEmployeSynthese(ActionUtil httpUtil) {
		Date dateDebut = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateDebut"));
		Date dateFin = DateUtil.stringToDate(httpUtil.getRequest().getParameter("dateFin"));
		String[] employeIds = httpUtil.getRequest().getParameterValues("employe_Ids");
		
		if(dateDebut == null) {
			Calendar cal = DateUtil.getCalendar(new Date());
			String dateString = (cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR);
			dateDebut =DateUtil.stringToDate("01/"+dateString);
			dateFin = DateUtil.stringToDate(DateUtil.getMaxMonthDate(dateDebut)+"/"+dateString);
		} else if(httpUtil.getRequest().getParameter("prev") != null) {
			dateDebut = DateUtil.addSubstractDate(dateDebut, TIME_ENUM.MONTH, -1);
			dateFin = DateUtil.addSubstractDate(dateFin, TIME_ENUM.MONTH, -1);
		} else if(httpUtil.getRequest().getParameter("next") != null) {
			dateDebut = DateUtil.addSubstractDate(dateDebut, TIME_ENUM.MONTH, 1);
			dateFin = DateUtil.addSubstractDate(dateFin, TIME_ENUM.MONTH, 1);
		}
		// Postionner l'heure
		dateDebut = DateUtil.getStartOfDay(dateDebut);
		dateFin = DateUtil.getEndOfDay(dateFin);
		
		httpUtil.setRequestAttribute("dateDebut", dateDebut);
		httpUtil.setRequestAttribute("dateFin", dateFin);
		//-----------------------------------------------------------		
		httpUtil.setRequestAttribute("listEmploye", employeService.findAll(Order.asc("nom")));
		
		Map<String, BigDecimal[]> mapData = pointageService.getMapEmployeSynthese(employeIds, dateDebut, dateFin);
		httpUtil.setRequestAttribute("list_data", mapData);
		
		httpUtil.setDynamicUrl("/domaine/personnel/paie/employe_synthese.jsp");
	}
	
	public void runUploadPointeuseZktEcoIp(ActionUtil httpUtil){
		try {
			if(!ReplicationGenerationEventListener._IS_CLOUD_SYNCHRO_INSTANCE){
				String port = StrimUtil.getGlobalConfigPropertie("caisse.instance.port");
				String instance = StrimUtil.getGlobalConfigPropertie("caisse.instance.name");
				String host = null;
				
				if(StringUtil.isEmpty(port) || StringUtil.isEmpty(instance)) {
					host = "http://localhost:"+httpUtil.getRequest().getServerPort()
							+httpUtil.getRequest().getContextPath()
							+"/printCtrl";

				} else {
					port = (StringUtil.isEmpty(port) ? "8089" : port);
					host = "http://localhost"
							+":"+port
							+"/"+instance+"/printCtrl";					
				}
				
				System.out.println("host ===> "+host);
				
				String dir = Pointeuse.class.getResource("/").getPath().toString()+"appli/softs/checkers";
				dir = dir.substring(1);
				Process p = Runtime.getRuntime().exec("cmd START /wait cmd /c \"ZktManage.exe "+host+" > abc.txt\"", null, new File(dir));
			
				if(httpUtil.getRequestAttribute("isFromP") != null){
					Thread.sleep(5000);// Tempo pour exécution
				}
				
			} else {
				httpUtil.setRequestAttribute("LOAD_POINTAGE", true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "", "La synchronisation est effectuée.");
		
		// Si non from login page
		if(httpUtil.getRequestAttribute("isFromP") != null){
			httpUtil.setDynamicUrl("paie.salariePaie.loadVueSemaine");
		} else {
			httpUtil.setDynamicUrl("paie.salariePaie.loadVueMois");
		}
	}
	
	private void majPointageHoraire(ActionUtil httpUtil) {
		String dy = httpUtil.getParameter("dt");
		Long emplId = (Long)httpUtil.getLongParameter("empl");
		
		Map<String, Object> mapData = httpUtil.getValuesByStartName("heure_");
		Date currentDate = DateUtil.stringToDate(dy+"/"+(String) httpUtil.getMenuAttribute("CURR_DATE"), "dd/MM/yyyy");
		
		pointageService.majPointageHoraire(mapData, emplId, currentDate);
		
		httpUtil.setDynamicUrl("paie.salariePaie.loadVueJour");
	}
	
	/**
	 *  
	 * @param httpUtil
	 */
	@WorkForward(useBean=true)
	public void mergePointage(ActionUtil httpUtil) {
		String type = httpUtil.getParameter("tpp");
		if(type.equals("hr")) {
			majPointageHoraire(httpUtil);
			return;
		}
		Long emplId = (Long)httpUtil.getLongParameter("empl");
		PointageBean pointageBean = (PointageBean) httpUtil.getViewBean();
		pointageBean.setDate_event(DateUtil.stringToDate(httpUtil.getParameter("dt")));
		pointageBean.setType(type);
		EmployePersistant opc_employe = (EmployePersistant) pointageService.findById(EmployePersistant.class, emplId);
		pointageBean.setOpc_employe(opc_employe);
		
		PointageBean currPointage = pointageService.getPointageByTypeAndDate(opc_employe.getId(), pointageBean.getType(), pointageBean.getDate_event());
		if(currPointage != null) {
			pointageBean.setId(currPointage.getId());
		}
		//
		if(pointageBean.getId() == null) {
			pointageService.create(pointageBean);
		} else {
			pointageService.update(pointageBean);
		}
		//
		httpUtil.setDynamicUrl("paie.salariePaie.loadVueJour");
	}
	
	/**
	 * 
	 * @param httpUtil
	 */
	@WorkForward(useBean=true)
	public void deletePointage(ActionUtil httpUtil) {
		Long id = httpUtil.getWorkIdLong();
		//
		pointageService.delete(id);
		//
		httpUtil.setDynamicUrl("paie.salariePaie.loadVueJour");
	}
	
	/**
	 * @param httpUtil
	 */
	public void loadDataJour(ActionUtil httpUtil) {
		String type = httpUtil.getParameter("tpp");
		Long employeId = httpUtil.getLongParameter("empl");
		Date currentDate = DateUtil.stringToDate(httpUtil.getParameter("dt"));
		
		// Envoi données pour popup
		httpUtil.setRequestAttribute("dt", httpUtil.getParameter("dt"));
		httpUtil.setRequestAttribute("ttl", httpUtil.getParameter("ttl"));
		httpUtil.setRequestAttribute("tpp", type);
		httpUtil.setRequestAttribute("empl", employeId);
		
		EmployePersistant employeP = pointageService.findById(EmployePersistant.class, employeId);
		httpUtil.setRequestAttribute("employeP", employeP);
		
		// Ids des pointage journée
		if(type.equals("hr")) {
			Map<Long, String> mapData = pointageService.getPointageHorairesByDate(currentDate);
			
			int idx = 1;
			while(mapData.size() < 4) {
				mapData.put(-Long.valueOf(idx), "");
				idx++;
			}
			
			httpUtil.setRequestAttribute("mapHours", mapData);
		} else {
			PointageBean currPointage = pointageService.getPointageByTypeAndDate(employeId, type, currentDate);
			httpUtil.setViewBean(currPointage);

			if(currPointage != null){
				httpUtil.setRequestAttribute("tarifJourRef", currPointage.getTarif_ref());
			} else if(employeP != null){
				httpUtil.setRequestAttribute("tarifJourRef", employeP.getTarif());
			}			
		}
		
		httpUtil.setDynamicUrl("/domaine/personnel/paie/pointage_modal_edit.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void init_config(ActionUtil httpUtil){
		EtablissementPersistant etsB = ContextGloabalAppli.getEtablissementBean();
		if(httpUtil.getParameter("issave") != null){
			String path = httpUtil.getParameter("pointeuse.path");
			String ip = httpUtil.getParameter("pointeuse.zkt_ip");
			String port = httpUtil.getParameter("pointeuse.zkt_port");
			
			pointageService.updatePointeusePath(path, ip, port);
			etsB.setPointeuse_db_path(path);
			etsB.setPointeuse_ip(ip);
			etsB.setPointeuse_port(port);
			
			MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "", "La configuration de la pointeuse est sauvegardée.");
			//
			httpUtil.setDynamicUrl("paie.salariePaie.loadVueMois");
			return;
		} else{
			EtablissementPersistant restauP = (EtablissementPersistant) pointageService.findById(EtablissementPersistant.class, etsB.getId());
			httpUtil.setRequestAttribute("path", restauP.getPointeuse_db_path());
			httpUtil.setRequestAttribute("zktIp", restauP.getPointeuse_ip());
			httpUtil.setRequestAttribute("zktPort", restauP.getPointeuse_port());
			//
			httpUtil.setDynamicUrl("/domaine/personnel/paie/pointage_config.jsp");
		}
	}
	
	/**
	 * @param httpUtil
	 */
	public void work_post(ActionUtil httpUtil) {

	}
}
