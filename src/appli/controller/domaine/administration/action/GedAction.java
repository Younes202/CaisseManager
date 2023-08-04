package appli.controller.domaine.administration.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.hibernate.criterion.Order;

import appli.controller.domaine.administration.bean.GedBean;
import appli.model.domaine.administration.persistant.GedFichierPersistant;
import appli.model.domaine.administration.persistant.GedPersistant;
import appli.model.domaine.administration.service.IClientService;
import appli.model.domaine.administration.service.IGedService;
import appli.model.domaine.administration.service.ITreeService;
import appli.model.domaine.personnel.persistant.ClientPersistant;
import appli.model.domaine.personnel.persistant.EmployePersistant;
import appli.model.domaine.personnel.service.IEmployeService;
import appli.model.domaine.stock.persistant.FournisseurPersistant;
import appli.model.domaine.stock.service.IFournisseurService;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.ContextGloabalAppli;
import framework.controller.ControllerUtil;
import framework.controller.annotation.WorkController;
import framework.controller.annotation.WorkForward;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.service.MessageService;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.FileUtil;

@WorkController(nameSpace="admin", bean=GedBean.class, jspRootPath="/domaine/fichier/")
public class GedAction extends ActionBase {
	@Inject
	private IGedService gedService;
	@Inject
	private IClientService clientService;
	@Inject
	private IFournisseurService fournisseurService;
	@Inject
	private ITreeService treeService;
	@Inject
	private IEmployeService employeService;
	
	public void work_init(ActionUtil httpUtil) {
		httpUtil.setRequestAttribute( "listClient", clientService.findAll(Order.asc("numero")));
		httpUtil.setRequestAttribute( "listFournisseur", fournisseurService.findAll( Order.asc("numero")));
		httpUtil.setRequestAttribute( "listEmploye", employeService.findAll( Order.asc("numero")));
		
		String tp = httpUtil.getParameter("tp"); 
		if(tp != null){
		    httpUtil.setMenuAttribute("tp", tp);
		} else{
			tp = (String) httpUtil.getMenuAttribute("tp");
		}
		//		
		String parentId = httpUtil.getParameter("ged_parent");
		if(StringUtil.isNotEmpty(parentId)){
			httpUtil.setRequestAttribute("ged_parent", parentId);
		} else{
			GedPersistant gedP = treeService.getTreeRoot(GedPersistant.class);
			httpUtil.setRequestAttribute("ged_parent", EncryptionUtil.encrypt(gedP.getId().toString()));// Parent racine
		}
		
		if(httpUtil.getParameter("isTabMnu") != null) {
			httpUtil.setMenuAttribute("isTabMnu", true);
		}
	}
	
	@Override
	public void work_find(ActionUtil httpUtil) {
		gedService.getEntityManager().clear();
		
		List<GedPersistant> listData = treeService.getListeTree(GedPersistant.class, false, false);
		if(listData.size() == 0){
			GedPersistant gedP = new GedPersistant();
			gedP.setB_left(0);
			gedP.setB_right(1);
			gedP.setLevel(0);
			gedP.setCode("ROOT");
			gedP.setLibelle("Liste des fichiers");
			gedP.setDate_creation(new Date());
			gedP.setSignature("ADMIN");
			gedP.setDate_maj(new Date());
			
			treeService.mergeGedRoot(gedP);
			
			listData = treeService.getListeTree(GedPersistant.class, false, false);
		}
		
		httpUtil.setRequestAttribute("listGed", listData);
		
		httpUtil.setDynamicUrl("/domaine/fichier/ged_list.jsp");
	}
	
	@Override
	public void work_create(ActionUtil httpUtil) {
		Long parentId = httpUtil.getLongParameter("ged_parent");
		GedBean gedBean = (GedBean)httpUtil.getViewBean();
		gedBean.setParent_id(parentId);

		treeService.createTree(gedBean);
		
		work_find(httpUtil);
	}

	@Override
	public void work_update(ActionUtil httpUtil) {
		GedBean gedBean = (GedBean)httpUtil.getViewBean();
		gedBean.setId(httpUtil.getWorkIdLong());
		
		gedBean.setParent_id(httpUtil.getLongParameter("ged_parent"));
		treeService.updateTree(gedBean);
		//
		work_find(httpUtil);
	}

	@Override
	public void work_init_create(ActionUtil httpUtil) {
		String tp = httpUtil.getParameter("tpline");
		if("FC".equals(tp)){
			Long parentId = httpUtil.getLongParameter("ged_parent");
			httpUtil.setRequestAttribute("ged_parent", parentId);
			
			httpUtil.setDynamicUrl("/domaine/fichier/gedFichier_edit.jsp");
		} else{
			httpUtil.setDynamicUrl("/domaine/fichier/ged_edit.jsp");
		}
	}
	
	@Override
	public void work_init_update(ActionUtil httpUtil) {
		work_edit(httpUtil);
	}
	
	@Override
	public void work_edit(ActionUtil httpUtil) {
		String tp = httpUtil.getParameter("tpline");
		GedPersistant gedP = null;
		//
		if("FC".equals(tp)){
			Long workIdLong = httpUtil.getWorkIdLong();
			gedP = new GedPersistant();
			GedFichierPersistant gedFileP = (GedFichierPersistant) gedService.findById(GedFichierPersistant.class, workIdLong);
			
			httpUtil.setRequestAttribute("gedFichier", gedFileP);
			
			httpUtil.setDynamicUrl("/domaine/fichier/gedFichier_edit.jsp");
		} else{
			Long workIdLong = httpUtil.getLongParameter("ged_cur");// Lui même dans ce cas
			gedP = gedService.findById(GedPersistant.class, workIdLong);
			
			httpUtil.setRequestAttribute("ged_parent", ((GedPersistant)treeService.getTreeParent(GedBean.class, gedP.getId())).getId());
			//
			httpUtil.setDynamicUrl("/domaine/fichier/ged_edit.jsp");
			httpUtil.setViewBean(ServiceUtil.persistantToBean(GedBean.class, gedP));
		}
	}
	
	/**
	 * @param httpUtil
	 */
	public void downLoadDocument(ActionUtil httpUtil){
		try{
			httpUtil.manageInputFileView("ged");
		} catch(Exception e){
			httpUtil.writeResponse("<hr>Aucun fichier n'a été trouvé.");
		}
	}
	
	/**
	 * 
	 * @param httpUtil
	 */
	@WorkForward(bean=GedBean.class, useFormValidator=true, useBean=true)
	public void mergeDetailFichier(ActionUtil httpUtil){
		Long gedId = httpUtil.getLongParameter("ged_parent");
		GedBean gedBean = (GedBean) httpUtil.getViewBean();
		gedBean.setId(gedId);
		GedFichierPersistant gedFichierP = gedBean.getGedFichier();// Bean écran
		gedFichierP.setId(httpUtil.getWorkIdLong());
		
		// Cas création
		if(StringUtil.isEmpty(httpUtil.getParameter("photoX_name"))){
			MessageService.addBannerMessage("Veuillez joindre un document.");
			return;
		}
		
		Map mapFiles = (Map) ControllerUtil.getMenuAttribute("CAISSE_DOCUMENTS", httpUtil.getRequest());
		Object fichierByte = (mapFiles!=null ? mapFiles.get(EncryptionUtil.encrypt("photoX")) : null);
		if(fichierByte != null){
			String fileName = httpUtil.getParameter("photoX_name");
			String extention = fileName.substring(fileName.lastIndexOf(".")+1);
			String path = StrimUtil._GET_PATH("ged") + "/" + gedId;
			//
			gedFichierP.setExtention(extention);
			gedFichierP.setFile_name(fileName);
			gedFichierP.setPath(path);
			gedFichierP.setOpc_ged(gedService.findById(gedId));
		} else{// Maj
			GedFichierPersistant gedF_DB = (GedFichierPersistant) gedService.findById(GedFichierPersistant.class, httpUtil.getWorkIdLong());
			gedF_DB.setLibelle(gedFichierP.getLibelle());
			gedF_DB.setCommentaire(gedFichierP.getCommentaire());
			gedFichierP = gedF_DB;
		}
		
		gedFichierP.setOpc_ged(gedBean);
		gedFichierP = gedService.mergeDetail(gedFichierP);
		
		managePieceJointe(httpUtil, gedFichierP.getId(), "ged");
		
		work_find(httpUtil);
	}
	
	public void initOrder(ActionUtil httpUtil) {
		Long elementId = httpUtil.getLongParameter("ged_cur");
		//
		GedPersistant gedP = null;
		if(elementId != null) {
			gedP = treeService.findById(GedPersistant.class, elementId);
		} else {
			gedP = treeService.getTreeRoot(GedPersistant.class);
		}
		
		List<?> listCalibre = treeService.getTreeEnfants(GedBean.class, elementId, false);
		List finalList = new ArrayList<>();
		for (Object mcP : listCalibre) {
			int levelMcp = (int) ReflectUtil.getObjectPropertieValue(mcP, "level");
			int levelP = (int) ReflectUtil.getObjectPropertieValue(gedP, "level");
			
			if(levelP+1 == levelMcp) {
				finalList.add(mcP);
			}
		}
		httpUtil.setRequestAttribute("listGed", finalList);
		
		httpUtil.setDynamicUrl("/domaine/fichier/ged_order.jsp");
	}
	
	public void ordonner(ActionUtil httpUtil) {
		Map<String, Object> mapOrder = httpUtil.getValuesByStartName("treeOrder_");
		treeService.changerOrdre(mapOrder, GedBean.class);
		
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "", "L'ordre des éléments est mise à jour.");
		//
		work_find(httpUtil);
	}
	
	/**
	 * @param httpUtil
	 */
	public void deleteDetailFichier(ActionUtil httpUtil) {
		Long workId = httpUtil.getWorkIdLong();
		GedFichierPersistant gedFD = (GedFichierPersistant) gedService.findById(GedFichierPersistant.class, workId);
		String path = gedFD.getPath()+"/"+gedFD.getFile_name();
		//
		gedService.deleteDetail(workId);
		
		new File(path).delete();
		//
		work_find(httpUtil);
	}
	
	@Override
	public void work_delete(ActionUtil httpUtil) {
		Long workId = httpUtil.getLongParameter("ged_cur");
		GedPersistant gedFileP = gedService.findById(GedPersistant.class, workId);
		//
		if(!BooleanUtil.isTrue(gedFileP.getIs_not_supp())) {
			gedService.deleteGed(workId);	
		} else{
			MessageService.addBannerMessage("la suppression de ce fichier est impossible");
			return;
		}
		//
		FileUtil.clearDir(ContextGloabalAppli.getEtablissementBean().getId().toString()+"/ged", workId);
		
		String path = StrimUtil._GET_PATH("ged") + "/" + workId;
		new File(path).delete();
		
		//
		work_find(httpUtil);
	}
	
	public void getFile(ActionUtil httpUtil) {
		String name = httpUtil.getParameter("file_name");
		String extention = httpUtil.getParameter("extention");
		
		Long clientId = httpUtil.getLongParameter("client");
		Long fournisseurId = httpUtil.getLongParameter("fournisseur");
		Long employeId = httpUtil.getLongParameter("employe");
		
		if(StringUtil.isEmpty(name) 
				&& StringUtil.isEmpty(extention)
				&& StringUtil.isEmpty(fournisseurId)
				&& StringUtil.isEmpty(employeId)
				&& StringUtil.isEmpty(clientId)) {
			work_find(httpUtil);
			return;
		}
		
		List<GedPersistant> listGed = gedService.getFileByExtentionOrName(name, extention, clientId, fournisseurId, employeId);
		List<GedPersistant> listGedTemp = new ArrayList<>();
		for (GedPersistant gedPersistant : listGed) { 
			if(!listGedTemp.contains(gedPersistant)) {
				listGedTemp.add(gedPersistant);
			}
			List<GedPersistant> listParents = treeService.getTreeParents(GedPersistant.class, gedPersistant.getId(), false);
			for (GedPersistant gedPersistant2 : listParents) {
				if(!listGedTemp.contains(gedPersistant2)) {
					listGedTemp.add(gedPersistant2);
				}
			}
		}
		
		//
		Collections.sort(listGedTemp, new Comparator<GedPersistant>() {
			@Override
			public int compare(GedPersistant o1, GedPersistant o2) {
				int result = o1.getB_left().compareTo(o2.getB_left());
				return result;
			}
		});
		httpUtil.setRequestAttribute("listGed", listGedTemp);
		httpUtil.setRequestAttribute("file_name", name);
		httpUtil.setRequestAttribute("extention", extention);
		
		httpUtil.setRequestAttribute("clientId", clientId);
		httpUtil.setRequestAttribute("fournisseurId", fournisseurId);
		httpUtil.setRequestAttribute("employeId", employeId);
		
		httpUtil.setDynamicUrl("/domaine/fichier/ged_list.jsp");
	}
	
	public void work_post(ActionUtil httpUtil) {
		List<GedPersistant> listGeds = (List<GedPersistant>) treeService.getListeTree(GedPersistant.class, false, false);
		//
		httpUtil.setRequestAttribute("listGedParent", listGeds);
	}
	
	public void majArbreClient(ActionUtil httpUtil) {
		List<ClientPersistant> listClient = clientService.getClientsActifs();
		gedService.majArborscenceClient(listClient);
		work_find(httpUtil);
	}
	
	public void majArbreFournisseur(ActionUtil httpUtil) {
		List<FournisseurPersistant> listFournisseur = fournisseurService.getListFournisseur(true, true);
		gedService.majArborscenceFournisseur(listFournisseur);
		work_find(httpUtil);
	}
	
	public void majArbreEmploye(ActionUtil httpUtil) {
		List<EmployePersistant> listEmploye = employeService.getListEmployeActifs();
		gedService.majArborscenceEmploye(listEmploye);
		work_find(httpUtil);
	}
}
