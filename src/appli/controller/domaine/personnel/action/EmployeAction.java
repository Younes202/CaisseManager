package appli.controller.domaine.personnel.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.hibernate.criterion.Order;

import appli.controller.domaine.administration.bean.ValTypeEnumBean;
import appli.controller.domaine.personnel.bean.EmployeBean;
import appli.model.domaine.administration.persistant.TypeEnumPersistant;
import appli.model.domaine.administration.service.IValTypeEnumService;
import appli.model.domaine.personnel.persistant.EmployeAppreciationPersistant;
import appli.model.domaine.personnel.persistant.EmployePersistant;
import appli.model.domaine.personnel.service.IEmployeService;
import appli.model.domaine.personnel.service.IPosteService;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.stock.service.IFamilleService;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.beanContext.VillePersistant;
import framework.model.common.service.MessageService;
import framework.model.util.ModelConstante;

@WorkController(nameSpace = "pers", bean = EmployeBean.class, jspRootPath = "/domaine/personnel/")
public class EmployeAction extends ActionBase {
	@Inject
	private IEmployeService employeService;
	@Inject
	private IPosteService posteService;
	@Inject
	private IValTypeEnumService valEnumService;
	@Inject
	private IFamilleService familleService;
	
	public void work_init(ActionUtil httpUtil) {
		httpUtil.setMenuAttribute("IS_SUB_ADD","pers.employe.work_init_create");		
		
		if(httpUtil.isEditionPage()){
			MessageService.getGlobalMap().put("NO_ETS", true);
			httpUtil.setRequestAttribute("listVille", employeService.getListData(VillePersistant.class, "opc_region.libelle, libelle"));
			MessageService.getGlobalMap().remove("NO_ETS");
			
			String[][] typeTravail = {{"H", "Heure"}, {"J", "Jour"}};
			httpUtil.setRequestAttribute("typeTravail", typeTravail);
			
			Long employeId = httpUtil.getWorkIdLong();
			if(employeId != null){
				EmployeBean empl = employeService.findById(employeId);
				httpUtil.setMenuAttribute("employeId", employeId);
				httpUtil.setMenuAttribute("currEmploye", empl.getNom()+" "+empl.getPrenom());
			} else{
				employeId = (Long)httpUtil.getMenuAttribute("employeId");
			}
			if(!httpUtil.isCreateAction()){
				// Gérer le retour sur cet onglet
				employeId = (Long)httpUtil.getMenuAttribute("employeId");
				if(employeId != null && !httpUtil.isCrudOperation()){
					loadBean(httpUtil, employeId);
				}
			} else{
				httpUtil.removeMenuAttribute("employeId");
			}
			httpUtil.setMenuAttribute("salaireP", employeService.getLastSalaire(employeId));
			
			List listFamille = familleService.getListeFamille("EM", true, false);
			httpUtil.setRequestAttribute("listeFamille", listFamille);
		}
		
		// Etablissement destinations -----------------------
//		if(ReplicationGenerationEventListener._IS_CLOUD_SYNCHRO_INSTANCE){
//			String action = httpUtil.getAction();
//			EmployeBean dataBean = (EmployeBean) httpUtil.getViewBean();
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
		httpUtil.setRequestAttribute("listEtablissement", valEnumService.findAllNoFilter(EtablissementPersistant.class, Order.asc("nom")));
				
		List<ValTypeEnumBean> lisTypeSortie = valEnumService.getListValeursByType(ModelConstante.ENUM_TYPE_SORTIE);
		httpUtil.setRequestAttribute("lisTypeSortie", lisTypeSortie);
		List<ValTypeEnumBean> lisTypeContrat = valEnumService.getListValeursByType(ModelConstante.ENUM_TYPE_CONTRAT);
		httpUtil.setRequestAttribute("lisTypeContrat", lisTypeContrat);
		httpUtil.setRequestAttribute("listPoste", posteService.findAll(Order.asc("intitule")));
		httpUtil.setRequestAttribute("civiliteArray", new String[][]{{"H", "Homme"}, {"F", "Femme"}});
		httpUtil.setRequestAttribute("situationFamArray", new String[][]{{"CJ", "Conjoint"}, {"DS", "Dissocié"}});
		httpUtil.setRequestAttribute("appreciationsType", new String[][]{{"N", "Negative"}, {"P", "Positive"}});

		
		httpUtil.setRequestAttribute("typeEmployeEnumId", valEnumService.getOneByField(TypeEnumPersistant.class, "code", ModelConstante.ENUM_TYPE_CONTRAT).getId());
		httpUtil.setRequestAttribute("lisTypeSortieEnumId", valEnumService.getOneByField(TypeEnumPersistant.class, "code", ModelConstante.ENUM_TYPE_SORTIE).getId());
	}
	
	
	
	
	@Override
	public void work_merge(ActionUtil httpUtil) {
		EmployeBean employeBean = (EmployeBean) httpUtil.getViewBean();
		if(employeBean.getMode_paie() != null 
				&& employeBean.getMode_paie().equals("J") 
				&& employeBean.getHeureParJour() == null) {
			MessageService.addFieldMessage("employe.heureParJour", "Le champ est obligatoire");
			return;
		}
		
		super.work_merge(httpUtil);
		
		String[] types = httpUtil.getRequest().getParameterValues("type[]");
		String[] justifications = httpUtil.getRequest().getParameterValues("justification[]");
		String[] dateDebuts = httpUtil.getRequest().getParameterValues("date_debut[]");
        EmployeAppreciationPersistant appreciation = new EmployeAppreciationPersistant();
		if (types != null && justifications != null && dateDebuts != null &&
		        types.length == justifications.length && types.length == dateDebuts.length) {
		    for (int i = 0; i < types.length; i++) {
		        String type = types[i];
		        String justification = justifications[i];
		        
		        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		        dateFormat.setLenient(false);
		        Date dateDebut = null;
		        try {
		            dateDebut = dateFormat.parse(dateDebuts[i]);
		        } catch (ParseException e) {
		            // Handle the parse exception
		            e.printStackTrace();
		        }

		        appreciation.setType(type);
		        appreciation.setJustification(justification);
		        appreciation.setDate_debut(dateDebut);
		        appreciation.setOpc_employe(employeBean);
		        employeService.mergeEntity(appreciation);
		        // Set other properties of the appreciation
		    }
		}
		
		managePieceJointe(httpUtil, employeBean.getId(), "employe_pic", 300, 300);
		managePieceJointe(httpUtil, employeBean.getId(), "employe", 300, 300);
		
		if(!"pers.employe.work_init_create".equals(httpUtil.getMenuAttribute("IS_SUB_ADD"))) {
			httpUtil.setDynamicUrl(""+httpUtil.getMenuAttribute("IS_SUB_ADD"));
		}
	}
	
	@Override
	public void work_delete(ActionUtil httpUtil) {
		super.work_delete(httpUtil);
		manageDeleteImage(httpUtil.getWorkIdLong(), "employe_pic");
		manageDeleteImage(httpUtil.getWorkIdLong(), "employe");
		work_find(httpUtil);
	}
	
	@Override
	public void work_find(ActionUtil httpUtil) {
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_employe");
		String req = "from EmployePersistant employe where 1=1"
		+ getFilterStateRequest(httpUtil, "is_disable");
		req = req + " order by employe.nom";//employe.opc_famille.libelle, Ach kadir famille hnaya ?
		Map<String, Map<String, List<Object[]>>> listDataZ = familleService.getDetailEmp();
		
		Map<String, List<Object[]>> totalsMap = listDataZ.get("TotalEmp");
        List<Object[]> totalsList = totalsMap.get("totals");
        if (totalsList != null && !totalsList.isEmpty()) {
            Object[] valuesArray = totalsList.get(0); // Assuming there's only one element in the list

            int allConfirmedEmp = (int) valuesArray[0];
            int allUnconfirmedEmp = (int) valuesArray[1];

            httpUtil.setRequestAttribute("allConfirmedEmp", allConfirmedEmp);
            httpUtil.setRequestAttribute("allUnconfirmedEmp", allUnconfirmedEmp);
            
        }
		httpUtil.setRequestAttribute("MaplistEmploye", listDataZ);
		List<Object[]> listEmpNombreApp = employeService.getNomberOfApperciationAndNameAllEmp();
		httpUtil.setRequestAttribute("listEmpNombreApp", listEmpNombreApp);
		List<EmployePersistant> listData = (List<EmployePersistant>) employeService.findByCriteria(cplxTable, req);
		for (EmployePersistant emplP : listData) {
			if(emplP.getOpc_famille() != null) {
			List<FamillePersistant> familleStr = familleService.getFamilleParent("EM", emplP.getOpc_famille().getId());
			emplP.setFamilleStr(familleStr);
			}
		}
		String tpView= httpUtil.getParameter("tpView");
		if(tpView==null) {
		httpUtil.setRequestAttribute("showAdditionalColumns", true);
		}
		else if(tpView.equals(true)) {
			httpUtil.setRequestAttribute("showAdditionalColumns", true);
		}else {
			httpUtil.setRequestAttribute("showAdditionalColumns", false);
		}
		httpUtil.setRequestAttribute("list_employe", listData);
		httpUtil.setDynamicUrl("/domaine/personnel/employe_list.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void genererNumBL(ActionUtil httpUtil) {
		String numBl = employeService.generateNumero();
		httpUtil.writeResponse(numBl);
	}

	
	/**
	 * @param httpUtil
	 */
	public void find_reduction(ActionUtil httpUtil){
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_caisseMouvement");
		
		Long employeId = (Long)httpUtil.getMenuAttribute("employeId");
		
		cplxTable.getFormBean().getFormCriterion().put("employeId", employeId);
		
		List<CaisseMouvementPersistant> listCaisseMouvement = (List<CaisseMouvementPersistant>) employeService.findByCriteriaByQueryId(cplxTable, "caisseMouvement_reduc_find");
		httpUtil.setRequestAttribute("list_caisseMouvement", listCaisseMouvement);

		httpUtil.setDynamicUrl("/domaine/personnel/employe_reduction.jsp");
	}

	/**
	 * @param httpUtil
	 */
	public void desactiver(ActionUtil httpUtil) {
		employeService.activerDesactiverElement(httpUtil.getWorkIdLong());
		work_find(httpUtil);
	}
	
	public void work_post(ActionUtil httpUtil){
//		if(ReplicationGenerationEventListener._IS_CLOUD_SYNCHRO_INSTANCE){
//			String action = httpUtil.getAction();
//			if(action.equals(ActionConstante.EDIT) || action.equals(ActionConstante.INIT_UPDATE)) {
//				EmployeBean dataBean = (EmployeBean) httpUtil.getViewBean();
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
		//
		manageDataForm(httpUtil, "EMPLOYE");
	}
}
