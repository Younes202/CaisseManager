package appli.controller.domaine.personnel.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.criterion.Order;

import appli.controller.domaine.personnel.bean.SocieteLivrBean;
import appli.model.domaine.administration.persistant.UserPersistant;
import appli.model.domaine.administration.service.IUserService;
import appli.model.domaine.administration.service.IValTypeEnumService;
import appli.model.domaine.caisse.persistant.LivreurPositionPersistant;
import appli.model.domaine.personnel.persistant.SocieteLivrContactPersistant;
import appli.model.domaine.personnel.service.ISocieteLivrService;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.ControllerUtil;
import framework.controller.annotation.WorkController;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.beanContext.VillePersistant;
import framework.model.common.service.MessageService;
import framework.model.common.util.DateUtil;
import framework.model.common.util.DateUtil.TIME_ENUM;
import framework.model.common.util.StringUtil;

@WorkController(nameSpace = "pers", bean = SocieteLivrBean.class, jspRootPath = "/domaine/personnel/")
public class SocieteLivrAction extends ActionBase {
	@Inject
	private ISocieteLivrService societeLivrService;
	@Inject
	private IValTypeEnumService valEnumService;
	@Inject
	private IUserService userService;
	
	public void work_init(ActionUtil httpUtil) {
		if(httpUtil.isEditionPage()){
			MessageService.getGlobalMap().put("NO_ETS", true);
			httpUtil.setRequestAttribute("listVille", societeLivrService.getListData(VillePersistant.class, "opc_region.libelle, libelle"));
			httpUtil.setRequestAttribute("listEtablissement", valEnumService.findAll(EtablissementPersistant.class, Order.asc("nom")));
			MessageService.getGlobalMap().remove("NO_ETS");
			
			String[][] typeTravail = {{"H", "Heure"}, {"J", "Jour"}};
			httpUtil.setRequestAttribute("typeTravail", typeTravail);
			
			Long societeLivrId = httpUtil.getWorkIdLong();
			if(societeLivrId != null){
				SocieteLivrBean empl = societeLivrService.findById(societeLivrId);
				httpUtil.setMenuAttribute("societeLivrId", societeLivrId);
				httpUtil.setMenuAttribute("currSocieteLivr", empl.getNom()+" "+empl.getPrenom());
			} else{
				societeLivrId = (Long)httpUtil.getMenuAttribute("societeLivrId");
			}
			if(!httpUtil.isCreateAction()){
				// Gérer le retour sur cet onglet
				societeLivrId = (Long)httpUtil.getMenuAttribute("societeLivrId");
				if(societeLivrId != null && !httpUtil.isCrudOperation()){
					loadBean(httpUtil, societeLivrId);
				}
			} else{
				httpUtil.removeMenuAttribute("societeLivrId");
			}
		}
		
		// Etablissement destinations -----------------------
//		if(ReplicationGenerationEventListener._IS_CLOUD_SYNCHRO_INSTANCE){
//			String action = httpUtil.getAction();
//			SocieteLivrBean dataBean = (SocieteLivrBean) httpUtil.getViewBean();
//			if(action.equals(ActionConstante.MERGE) || action.equals(ActionConstante.CREATE) || action.equals(ActionConstante.UPDATE)) {
//				String ets_ids = "";
//				if(dataBean.getEts_ids() != null){
//					for (Long etsId :  dataBean.getEts_ids()) {
//						ets_ids = ets_ids + etsId + ";";
//					}
//				}
//				dataBean.setEts_dest(ets_ids);
//			}
//		}//------------------------------------------------		
	}
	
	@Override
	public void work_find(ActionUtil httpUtil) {
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_societeLivr");
		String req = "from SocieteLivrPersistant societeLivr where 1=1"
		+ getFilterStateRequest(httpUtil, "is_disable");
		req = req + " order by societeLivr.nom";
		
		List<UserPersistant> listData = (List<UserPersistant>) societeLivrService.findByCriteria(cplxTable, req);
		
		httpUtil.setRequestAttribute("list_societeLivr", listData);
		
		httpUtil.setDynamicUrl("/domaine/personnel/societeLivr_list.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void genererNumBL(ActionUtil httpUtil) {
		String numBl = societeLivrService.generateNumero();
		httpUtil.writeResponse(numBl);
	}
	
	@Override
	public void work_merge(ActionUtil httpUtil) {
		setDataContact(httpUtil);
		
		super.work_merge(httpUtil);
	}
	
	/**
	 * @param httpUtil
	 */
	@SuppressWarnings({ "unchecked" })
	private void setDataContact(ActionUtil httpUtil){
		SocieteLivrBean societeLivrBean = (SocieteLivrBean)httpUtil.getViewBean();
		societeLivrBean.setId(httpUtil.getWorkIdLong());

		List<SocieteLivrContactPersistant> listFournContact = (List<SocieteLivrContactPersistant>) httpUtil.buildListBeanFromMap("contact", SocieteLivrContactPersistant.class, "eaiid", "contact", "fonction", "coord");
		
		List<SocieteLivrContactPersistant> listContact = new ArrayList<>();
		if(societeLivrBean.getId() != null){
			SocieteLivrBean societeLivBean = societeLivrService.findById(societeLivrBean.getId());
			listContact = societeLivBean.getList_contact();
			listContact.clear();
		}
		listContact.addAll(listFournContact);
		societeLivrBean.setList_contact(listContact);
	}
	
	public void find_position(ActionUtil httpUtil){
		httpUtil.setRequestAttribute("listLivreur", userService.getListUserActifsByProfile("LIVREUR"));
		
		httpUtil.setDynamicUrl("/domaine/caisse/normal/position_livreur.jsp");
	}
	
	public void find_mvm_societeLivr(ActionUtil httpUtil) {
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_societeLivrMvm");
		String req = "from CaisseMouvementPersistant caisseMouvement where"
					+ " caisseMouvement.opc_societe_livr.id="+httpUtil.getMenuAttribute("societeLivrId")
					+ " order by caisseMouvement.id desc";
		
		List<UserPersistant> listData = (List<UserPersistant>) societeLivrService.findByCriteria(cplxTable, req);
		
		httpUtil.setRequestAttribute("list_societeLivrMvm", listData);
		
		httpUtil.setDynamicUrl("/domaine/personnel/societeLivrMvm_list.jsp");
	}
	
	public void find_trajets(ActionUtil httpUtil) { 
		if(httpUtil.getParameter("isR")==null){
			httpUtil.setRequestAttribute("listLivreur", userService.getListUserActifsByProfile("LIVREUR"));
			httpUtil.setRequestAttribute("date_debut", DateUtil.getCurrentDate());
			httpUtil.setRequestAttribute("heure_debut", "00:00");
			httpUtil.setRequestAttribute("date_fin", DateUtil.addSubstractDate(DateUtil.getCurrentDate(), TIME_ENUM.DAY, 1));
			httpUtil.setRequestAttribute("heure_fin", "00:00");
			
			httpUtil.setDynamicUrl("/domaine/caisse/normal/trajet_livreur.jsp");
			return;
		}
				
		Long livreurId = httpUtil.getLongParameter("livreur_id");
		Date dateDebut = DateUtil.stringToDate(httpUtil.getParameter("date_debut"));
		Date dateFin = DateUtil.stringToDate(httpUtil.getParameter("date_fin"));
		
		if(dateDebut == null){
			MessageService.addBannerMessage("Veuillez sélectionner une date.");
			return;
		}
		if(livreurId == null){
			MessageService.addBannerMessage("Veuillez sélectionner un livreur.");
			return;
		}
		
		if(StringUtil.isNotEmpty(httpUtil.getParameter("heure_debut"))) {
			String heureCourse = httpUtil.getParameter("heure_debut");
			Integer heureCourse_H = Integer.valueOf(heureCourse.substring(0, heureCourse.indexOf(":")));
			Integer heureCourse_M = Integer.valueOf(heureCourse.substring(heureCourse.indexOf(":")+1, heureCourse.length()));
			
			Calendar calDepart = DateUtil.getCalendar(dateDebut);
			calDepart.set(calDepart.get(Calendar.YEAR), calDepart.get(Calendar.MONTH), calDepart.get(Calendar.DAY_OF_MONTH), heureCourse_H, heureCourse_M);
			dateDebut = calDepart.getTime();
		}
		
		if(StringUtil.isNotEmpty(httpUtil.getParameter("heure_fin"))) {
			String heureCourse = httpUtil.getParameter("heure_fin");
			Integer heureCourse_H = Integer.valueOf(heureCourse.substring(0, heureCourse.indexOf(":")));
			Integer heureCourse_M = Integer.valueOf(heureCourse.substring(heureCourse.indexOf(":")+1, heureCourse.length()));
			
			Calendar calDepart = DateUtil.getCalendar(dateFin);
			calDepart.set(calDepart.get(Calendar.YEAR), calDepart.get(Calendar.MONTH), calDepart.get(Calendar.DAY_OF_MONTH), heureCourse_H, heureCourse_M);
			dateFin = calDepart.getTime();
		}
		
		List<LivreurPositionPersistant> listPosLivreur = userService.getPositionsLivreur(livreurId, dateDebut, dateFin);
		
		String json = ControllerUtil.getJSonDataAnnotStartegy(listPosLivreur);
		httpUtil.writeResponse(json);
	}

	public void work_post(ActionUtil httpUtil){
//		if(ReplicationGenerationEventListener._IS_CLOUD_SYNCHRO_INSTANCE){
//			String action = httpUtil.getAction();
//			if(action.equals(ActionConstante.EDIT) || action.equals(ActionConstante.INIT_UPDATE)) {
//				SocieteLivrBean dataBean = (SocieteLivrBean) httpUtil.getViewBean();
//				if(StringUtil.isNotEmpty(dataBean.getEts_dest())) {
//					String[] etsIds = StringUtil.getArrayFromStringDelim(dataBean.getEts_dest(), ";");
//					Long[] etsIdsLong = new Long[etsIds.length]; 
//					for (int i=0; i<etsIds.length; i++) {
//						etsIdsLong[i] = Long.valueOf(etsIds[i]);
//					}
//					dataBean.setEts_ids(etsIdsLong);
//				}
//			}
//		}
//		//
		manageDataForm(httpUtil, "SOCIETE_LIV");
	}
}
