package appli.controller.domaine.caisse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import appli.controller.domaine.administration.bean.EtatFinanceBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.util_ctrl.ApplicationListener;
import appli.model.domaine.administration.service.IEtablissementService;
import appli.model.domaine.administration.service.IEtatFinanceService;
import appli.model.domaine.administration.service.IParametrageService;
import appli.model.domaine.administration.service.IUserService;
import appli.model.domaine.administration.service.impl.DataCloudDb;
import appli.model.domaine.caisse.service.IJourneeService;
import appli.model.domaine.personnel.persistant.EmployePersistant;
import appli.model.domaine.personnel.persistant.paie.PointageEventPersistant;
import appli.model.domaine.personnel.service.IEmployeService;
import appli.model.domaine.personnel.service.IPointageService;
import appli.model.domaine.stock.persistant.EmplacementPersistant;
import appli.model.domaine.stock.service.ICentraleSynchroService;
import appli.model.domaine.stock.service.IEmplacementService;
import appli.model.domaine.vente.persistant.JourneePersistant;
import framework.controller.ControllerUtil;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.common.service.MessageService;
import framework.model.common.util.DateUtil;
import framework.model.common.util.EncryptionEtsUtil;
import framework.model.common.util.MappingUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.audit.ReplicationGenerationEventListener;

/**
 * Servlet implementation class PrintController
 */
@WebServlet("/printCtrl")
public class PrintController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tp =  request.getParameter("tp");
		//
		if(tp == null){
			return;
		} 
		
		IParametrageService paramSrv = ServiceUtil.getBusinessBean(IParametrageService.class);
		IUserService userService = ServiceUtil.getBusinessBean(IUserService.class);
		
		//---------------------------------- START UPDATER------------------------------------------
		if(tp.equals("check")) {// Pour tester si caisse répond
//			userService.updateLastCheckCloud(); 
			response.getWriter().write("OK");
			return;
		} else if(tp.equals("majApp")) {// Maj application
			if("local".equals(StrimUtil.getGlobalConfigPropertieIgnoreErreur("context.install"))){// Non cloud
				Date dateVersion = DateUtil.stringToDate(request.getParameter("dtV"), "ddMMyyyy");
				String version = request.getParameter("v");
				// Maj flag exécution script au démarrage
				userService.updateFlagUpdate(version, dateVersion);
			}
		} else if(tp.equals("majConf")) { // MAj globale conf
			try {
				ApplicationListener.loadCaisseConf(request.getContextPath());
				MappingUtil.getGlobalConfigMap();
			} catch (Exception e) {
				
			}
		
		//------------------------------------ END UPDATER ----------------------------------------
		
		//------------------------------------ CENTRALE ----------------------------------------
		} else if(tp.equals("savePosition")) {
			String str_position = request.getParameter("data");
			String livreurIdStr = request.getParameter("livreurId");
			
			if(livreurIdStr != null) {
				IEtablissementService etsService = (IEtablissementService)ServiceUtil.getBusinessBean(IEtablissementService.class);
				EmployePersistant livreur = etsService.findById(EmployePersistant.class, Long.valueOf(livreurIdStr));
				etsService.majPositionLivreur(livreur, str_position);
			}
			
			response.getWriter().write("OK");
			return;
			
		} else if(tp.equals("centrale")) {
			String typeOpr = request.getParameter("opr");
			String retour = "";
			
			String codeAuth = request.getParameter("auth");
			String abonnement = null;
			EtablissementPersistant etsP = null;
			
			if(!"DEMTRANS".equals(typeOpr) && !"ANNTRANS".equals(typeOpr)) {
				etsP = paramSrv.getEtsOneOrCodeAuth(); //etsSrv.getOneByField(EtablissementPersistant.class, "code_authentification", codeAuth);
				if(etsP != null) {
					abonnement = new EncryptionEtsUtil(etsP.getDyc_key()).decrypt(etsP.getAbonnement());
				}
				if(abonnement == null) {
					response.getWriter().write("KO");
					return;
				}
			}
			
			if("syncEtat".equals(typeOpr)) {// Synchro synthese
				if(abonnement.indexOf("OPTPLUS_SYNC_CENTRALE") == -1){
					response.getWriter().write("KO");
					return;
				}
				
				IEtatFinanceService etatFSrv = ServiceUtil.getBusinessBean(IEtatFinanceService.class);
				Date debut = DateUtil.stringToDate(request.getParameter("dts"), "dd-MM-yyyy");
				Date fin = DateUtil.stringToDate(request.getParameter("dte"), "dd-MM-yyyy");
				
				List<EtatFinanceBean> listEtat = new ArrayList<>();
				EtatFinanceBean etatB = etatFSrv.getEtatFinanceBean(debut, fin);
				listEtat.add(etatB);
				
				retour = ControllerUtil.getJSonDataAnnotStartegy(listEtat);
				
				request.setCharacterEncoding("UTF-8");
				response.setContentType("text/html;charset=UTF-8");
				
			} else if("syncJournee".equals(typeOpr)) {// Synchro synthese
				if(abonnement.indexOf("OPTPLUS_SYNC_CENTRALE") == -1){
					response.getWriter().write("KO");
					return;
				}
				
				IJourneeService journeeSrv = ServiceUtil.getBusinessBean(IJourneeService.class);
				
				Date debut = DateUtil.stringToDate(request.getParameter("dts"), "dd-MM-yyyy");
				Date fin = DateUtil.stringToDate(request.getParameter("dte"), "dd-MM-yyyy");
				List<JourneePersistant> listJ = journeeSrv.getListournee(debut, fin);
				
				retour = ControllerUtil.getJSonDataAnnotStartegy(listJ);
				
				request.setCharacterEncoding("UTF-8");
				response.setContentType("text/html;charset=UTF-8");
				
			} else if("syncEmpl".equals(typeOpr)) {// Synchro emplacements
				if(abonnement.indexOf("OPTPLUS_SYNC_CENTRALE") == -1){
					response.getWriter().write("KO");
					return;
				}
				
				IEmplacementService centraleSrv = ServiceUtil.getBusinessBean(IEmplacementService.class);
				List<EmplacementPersistant> listEmpl = centraleSrv.getListEmplacementActifs();
				retour = ControllerUtil.getJSonDataAnnotStartegy(listEmpl);
				
				request.setCharacterEncoding("UTF-8");
				response.setContentType("text/html;charset=UTF-8");
				
			} else {// Synchro data
				ICentraleSynchroService centraleSrv = ServiceUtil.getBusinessBean(ICentraleSynchroService.class);
				boolean isToDisable = StringUtil.isTrue(request.getParameter("isDis"));
				
				request.setCharacterEncoding("UTF-8");
				String requestData = "", str;
				
				try {
					BufferedReader br = request.getReader();
					while ((str = br.readLine()) != null) {
						requestData += str;
					}
				} catch(Exception e){
					e.printStackTrace();
				}
				if("DEMTRANS".equals(typeOpr)) {// IN
					centraleSrv.synchroniseDemandeTransfert(requestData, codeAuth);
					retour = "OK";
				} else if("ANNTRANS".equals(typeOpr)) {// IN)
					Long demId = Long.valueOf(request.getParameter("demid")); 
					centraleSrv.annulerDemandeTransfert(demId, codeAuth);
					retour = "OK";
				} else {// OUT
					retour = centraleSrv.synchroniseInAll(etsP, typeOpr, requestData, isToDisable);
				}
			}
			response.getWriter().write(retour);
		//------------------------------------ END CENTRALE ----------------------------------------
			
		} else if(tp.equals("printers")) {
			String printers = URLDecoder.decode(request.getParameter("list"), "UTF-8").replace("**", "|");
			String auth = request.getParameter("auth");
			// On met toujours les imprimantes dans l'établissement pour utiliser dans le BO 
			paramSrv.mergeLocalPrinters(auth, printers);
			//
			response.getWriter().write("OK");
		} else if(tp.equals("pointeuse")) {
			IPointageService pointageSRv = ServiceUtil.getBusinessBean(IPointageService.class);
			//
			String codeAuth = request.getParameter("auth");
			String act = request.getParameter("act");
			
			EtablissementPersistant etsP = pointageSRv.getOneByField(EtablissementPersistant.class, "code_authentification", codeAuth);
			if(etsP == null) {
				response.getWriter().write("");
				return;
			}
			
			String path = etsP.getPointeuse_ip()+"|"+etsP.getPointeuse_port();
			
			if(ReplicationGenerationEventListener._IS_CLOUD_SYNCHRO_INSTANCE || ContextAppli.IS_FULL_CLOUD()){
				MessageService.getGlobalMap().put("GLOBAL_ETABLISSEMENT", etsP);
				MessageService.getGlobalMap().put("GLOBAL_SOCIETE", etsP.getOpc_societe());
			}
			
			//
			if(act.equals("infos")) {
				response.getWriter().write(path);
				
			} else if(act.equals("empl")) {
				IEmployeService employeSrv = ServiceUtil.getBusinessBean(IEmployeService.class);
				List<EmployePersistant> listEmploye = employeSrv.getListEmployeActifs();
				StringBuilder sb = new StringBuilder();
				for (EmployePersistant employeP : listEmploye) {
					String val = employeP.getNumero()+";"+employeP.getNom()+";"+StringUtil.getValueOrEmpty(employeP.getPrenom())+";"+(employeP.getDate_sortie()==null?0:1)+"|";
					sb.append(val);
				}
				response.getWriter().write(sb.toString());
			} else if(act.equals("pntg")) {
				String details = request.getParameter("det");
				List<PointageEventPersistant> listEmpl = new ArrayList<>();
				String[] data = StringUtil.getArrayFromStringDelim(details, "|");
				if(data != null){
					for (String det : data) {
						if(StringUtil.isEmpty(det)){
							continue;
						}
						String[] infos = StringUtil.getArrayFromStringDelim(det, ";");
						PointageEventPersistant ep = new PointageEventPersistant();
						ep.setNumero_client(infos[0].startsWith("#") ? infos[0].substring(1) : infos[0]);
						ep.setDate_pointage(DateUtil.stringToDate(infos[1], "yyyy-MM-dd HH:mm:ss"));
						ep.setOpc_etablissement(etsP);
						ep.setOpc_societe(etsP.getOpc_societe());
						ep.setOpc_abonne(etsP.getOpc_abonne());
						//
						listEmpl.add(ep);
					}
				}
				if(listEmpl.size() > 0){
					pointageSRv.mergePointageEmploye(listEmpl);
				}
				response.getWriter().write("OK");
			}
		} else if(tp.equals("print")) {
			String elementId =  request.getParameter("elmnt");
			sendObjectStrim(request, response, "CURR_PRINT_ID", elementId);
		} else if(tp.equals("syncCli")) {// Pour instance cloud synchro : appelé depuis admin pour gérer les ets
			DataCloudDb dataCloudSrv = ServiceUtil.getBusinessBean(DataCloudDb.class);
			
			String act = request.getParameter("act");
			String codeAuth = request.getParameter("auth");
			String authCr = request.getParameter("authCr");
			String syncTkn = request.getParameter("tkn");
			String ret = "OK";
			//
			if(act.equals("add")) {
				String abnName = request.getParameter("abn");
				String etsName = request.getParameter("ets");
				String remotePw = request.getParameter("rmpw");
				dataCloudSrv.initCLoudCliEts(abnName, etsName, syncTkn, codeAuth, authCr, remotePw);
			} else if(act.equals("dis")) {
				ret = dataCloudSrv.disableCLoudCliEts(codeAuth);
			} else if(act.equals("del")) {
				ret = dataCloudSrv.deleteCLoudCliEts(codeAuth);
			} else if(act.equals("conf")) {
				String conf = request.getParameter("conf");
				ret = dataCloudSrv.updateCLoudCliEts(codeAuth, syncTkn, conf);
			} else if(act.equals("down")) {// Téléchargement
				String data = dataCloudSrv.downloadDataBaseCLoudCliEts(codeAuth);
				response.getWriter().write(data);
				return;
			}
			response.getWriter().write(ret);
		}
	}

	/**
	 * @param request
	 * @param response
	 * @param prefix
	 * @param elementId
	 * @throws IOException
	 */
	private void sendObjectStrim(HttpServletRequest request, HttpServletResponse response, String prefix, String elementId) throws IOException {
		Object printPosData = request.getServletContext().getAttribute("CURR_PRINT_CMD_"+elementId);
        request.getServletContext().removeAttribute("CURR_PRINT_CMD_"+elementId); 
        
        ObjectOutputStream output = new ObjectOutputStream(response.getOutputStream());
        output.writeObject(printPosData);
        output.flush();
        output.close();
	}
}
