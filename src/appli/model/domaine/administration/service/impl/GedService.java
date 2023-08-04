package appli.model.domaine.administration.service.impl;


import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.administration.bean.GedBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.administration.dao.IGedDao;
import appli.model.domaine.administration.persistant.GedFichierPersistant;
import appli.model.domaine.administration.persistant.GedPersistant;
import appli.model.domaine.administration.service.IGedService;
import appli.model.domaine.administration.service.ITreeService;
import appli.model.domaine.administration.validator.GedValidator;
import appli.model.domaine.personnel.persistant.ClientPersistant;
import appli.model.domaine.personnel.persistant.EmployePersistant;
import appli.model.domaine.stock.persistant.FournisseurPersistant;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.common.service.MessageService;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.StringUtil;
import framework.model.service.GenericJpaService;


@WorkModelClassValidator(validator=GedValidator.class)
@Named
public class GedService extends GenericJpaService<GedBean, Long> implements IGedService{
	@Inject
	private IGedDao gedDao;
	@Inject
	private ITreeService treeService;
	
	
//	@Override
//	@Transactional
//	public void createGed(GedBean typeGedBean){
//		gedDao.createGed(typeGedBean);
//	}
//	
//	@SuppressWarnings("unchecked")
//	@Override
//    public String getGedParent(String type, Long gedId) {
//		GedPersistant ged = findById(gedId);
//        List<GedPersistant> listGed = getQuery("from GedPersistant "
//                + " where b_left<:bLeft and b_right>:bRight "
//                + "and (code is null or code!=:code) "
//                + "and type=:type "
//                + "order by b_left asc")
//                .setParameter("bLeft", ged.getB_left())
//                .setParameter("bRight", ged.getB_right())
//                .setParameter("type", type)
//                .setParameter("code", "ROOT")
//                .getResultList();
//        
//        String gedStr = ged.getLibelle();
//        for (GedPersistant gedCuisinePersistant : listGed) {
//        	gedStr = gedStr +  " -> " + gedCuisinePersistant.getLibelle();
//		}
//        
//        return gedStr;
//    }
//	
//	/**
//	 * 
//	 */
//	@Override
//	@Transactional
//	@WorkModelMethodValidator
//	public void deleteGed(Long id){
//		gedDao.deleteGed(id);
//	}
//
//	@Override
//	@Transactional
//	public void updateGed(GedBean gedBean) {
//		gedDao.updateGed(gedBean);
//	}
//	
//	@Override
//	public GedPersistant getGedRoot(String type) {
//		return (GedPersistant) getQuery("from GedPersistant where code=:code and type=:type")
//			.setParameter("code", "ROOT")
//			.setParameter("type", type)
//			.getSingleResult();
//	}
//	
//	@Override
//	public List<GedPersistant> getListeGed(String type) {
//		return getQuery("from GedPersistant where (code is null or code!=:code) and type=:type order by b_left")
//			.setParameter("code", "ROOT")
//			.setParameter("type", type)
//			.getResultList();
//	}

	@Override
	@Transactional
	public GedFichierPersistant mergeDetail(GedFichierPersistant gedFichierBean) {
		gedFichierBean.setDate_maj(new Date());
		gedFichierBean.setSignature(ContextAppli.getUserBean().getLogin());
		
		gedFichierBean = getEntityManager().merge(gedFichierBean);
		
		return gedFichierBean;
	}


	
	
	
	@Override
	@Transactional
	public void deleteGed(Long gedId) {
		List<GedPersistant> listGed = treeService.getTreeEnfants(GedPersistant.class, gedId, false);
		listGed.add(findById(GedPersistant.class, gedId));
		
		for (GedPersistant gedP : listGed) {
			List<GedFichierPersistant> listFichier = getQuery("from GedFichierPersistant where opc_ged.id=:gedId")
					.setParameter("gedId", gedP.getId())
					.getResultList();
		
			for (GedFichierPersistant gedFichierP : listFichier) {
				if(!BooleanUtil.isTrue(gedFichierP.getIs_not_sup())){
					MessageService.addBannerMessage("Un des sous fichiers ("+gedFichierP.getLibelle()+") de ce répertoire ne peut pas être supprimé.");
					return;
				}
			}
		}
		
		getQuery("delete from GedFichierPersistant where opc_ged.id=:gedId")
				.setParameter("gedId", gedId)
				.executeUpdate();
		getEntityManager().flush();
		//
		treeService.deleteTree(GedBean.class, gedId);
	}
	
	@Override
	@Transactional
	public void deleteDetail(Long detId) {
		GedFichierPersistant gedFichierDB = (GedFichierPersistant) gedDao.findById(GedFichierPersistant.class, detId);
		getEntityManager().remove(gedFichierDB);
	}
	
	@Override
	public List<GedPersistant> getFileByExtentionOrName(String ged_name, String extention, 
						Long clientId, Long fournisseurId, Long employeId){
		String req = "select opc_ged from GedFichierPersistant fic where 1=1 ";
		
		if(StringUtil.isNotEmpty(ged_name)) {
			req = req + " and lower(fic.libelle) like :fileName ";
		}
		if(StringUtil.isNotEmpty(extention)) {
			req = req + " and lower(fic.extention) like :exten ";
		}
		
		//--------------------------------------------------------
		if(StringUtil.isNotEmpty(clientId)) {
			req = req + " and fic.opc_ged.type_ged='CL' and fic.opc_ged.source_id=:client ";
		}
		if(StringUtil.isNotEmpty(fournisseurId)) {
			req = req + " and fic.opc_ged.type_ged='FO' and fic.opc_ged.source_id=:fourn ";
		}
		if(StringUtil.isNotEmpty(employeId)) {
			req = req + " and fic.opc_ged.type_ged='EM' and fic.opc_ged.source_id=:empl ";
		}
		
		req = req + " order by fic.opc_ged.b_left";
		
		Query query = getQuery(req);
		
		if(StringUtil.isNotEmpty(ged_name)) {
			query.setParameter("fileName", "%"+ged_name.toLowerCase()+"%");
		}
		if(StringUtil.isNotEmpty(extention)) {
			query.setParameter("exten", "%"+extention.toLowerCase()+"%");
		}
		if(clientId != null) {
			query.setParameter("client",clientId);
		}
		if(fournisseurId != null) {
			query.setParameter("fourn", fournisseurId);
		}
		if(employeId != null) {
			query.setParameter("empl", employeId);
		}
		
		return query.getResultList();
	}
	
	@Override
	@Transactional
	public void majArborscenceClient(List<ClientPersistant> listClient) {
		List<GedData> listDataRef = new ArrayList<>();
		for (ClientPersistant clientP : listClient) {
			String lib = creatLibelleCL(clientP);
		}
		//
		majArborscence(listDataRef, "CLIENT", "Dossiers clients");
	}
	
	@Override
	public String creatLibelleFO(FournisseurPersistant opc_FO) {
		String num = null;
		if(StringUtil.isEmpty(opc_FO.getNumero())){
			num = opc_FO.getId().toString();
		} else {
			num = opc_FO.getNumero().toUpperCase().trim();
		}

		String lib = num+"-"+opc_FO.getLibelle().trim();
		
		return lib;
		
	}
	
	
	public String creatLibelleCL(ClientPersistant opc_CL) {
		String num = null;
		if(StringUtil.isEmpty(opc_CL.getNumero())){
			num = opc_CL.getId().toString();
		} else {
			num = opc_CL.getNumero().toUpperCase().trim();
		}

		String lib = num;
		if(StringUtil.isNotEmpty(opc_CL.getNom())) {
			lib += "-"+opc_CL.getNom().trim();				
		}
		if(StringUtil.isNotEmpty(opc_CL.getPrenom())) {
			lib += " "+StringUtil.getValueOrEmpty(opc_CL.getPrenom());
		}					
		return lib;
		
	}
	
	
	public String creatLibelleEM(EmployePersistant opc_EM) {
		String num = null;
		String lib_num = null;
		if(StringUtil.isEmpty(opc_EM.getNumero())){
			num = opc_EM.getId().toString();
		} else {
			num = opc_EM.getNumero().toUpperCase().trim();
		}

		String lib = num;
		if(StringUtil.isNotEmpty(opc_EM.getNom())) {
			lib += "-"+opc_EM.getNom().trim();				
		}
		if(StringUtil.isNotEmpty(opc_EM.getPrenom())) {
			lib += " "+StringUtil.getValueOrEmpty(opc_EM.getPrenom());
		}
		
		lib_num = lib+"|"+num;
		return lib_num;
		
	}
	
	
	
	@Override
	@Transactional
	public void majArborscenceEmploye(List<EmployePersistant> listEmploye) {
		List<GedData> listDataRef = new ArrayList<>();
		for (EmployePersistant employeP : listEmploye) {
				String lib = creatLibelleEM(employeP);

			listDataRef.add(new GedData(employeP.getId(), lib.substring(0,lib.indexOf("|")),lib.substring(lib.indexOf("|")+1,lib.length())));
		}
		//
		majArborscence(listDataRef, "EMPLOYE", "Dossiers employes");
	}
	
	@Override
	@Transactional
	public void majArborscenceFournisseur(List<FournisseurPersistant> listFournisseur) {
		List<GedData> listDataRef = new ArrayList<>();
		for (FournisseurPersistant fournisseurP : listFournisseur) {
			String lib = creatLibelleFO(fournisseurP);	
			listDataRef.add(new GedData(fournisseurP.getId(), lib, lib.substring(0,lib.indexOf("-"))));
		}
		//
		majArborscence(listDataRef, "FOURNISSEUR", "Dossiers fournisseurs");
	}
	
	/**
	 * @param listDataRef
	 * @param type
	 * @param libelle
	 * @return 
	 */
	@Override
	public void checkFileExist(GedPersistant gedRoot) {
		if(gedRoot != null && gedRoot.getList_fichier() != null) {
			for( GedFichierPersistant det : gedRoot.getList_fichier()){
				if(!new File(det.getPath()).exists()) {
					this.getEntityManager().remove(det);
				}
			}
		}
	}
	
	
	
	
	private void majArborscence(List<GedData> listDataRef, String type, String libelle) {
		List<GedPersistant> listData = (List<GedPersistant>) treeService.getListeTree(GedPersistant.class, false, false);
		GedPersistant gedPRoot = (GedPersistant) treeService.getTreeRoot(GedPersistant.class);
		
		EntityManager entityManager = getEntityManager();
		Long parentId = null;
			
		List<GedPersistant> listGedPe = getQuery("from GedPersistant "
				+ "where code=:code")
				.setParameter("code", type)
				.getResultList();
		
		GedPersistant gedPe = (listGedPe.size() > 0 ? listGedPe.get(0) : null);
		
		if(gedPe == null){
			GedBean gedBean = new GedBean();
			gedBean.setCode(type);
			gedBean.setLibelle(libelle);
			gedBean.setParent_id(gedPRoot.getId());
			gedBean.setDate_maj(new Date());
			gedBean.setOpc_etablissement(ContextAppli.getEtablissementBean());
			//
			treeService.createTree(gedBean);
			entityManager.flush();	
			parentId = gedBean.getId();
		} else {
			parentId = gedPe.getId();
		}
		
		GedPersistant rootDataRef = findById(GedPersistant.class, parentId);
		
		Map<String, Long> mapGed = new HashMap<>();
		List<GedPersistant> listGedAll = findAll(GedPersistant.class);
		for (GedPersistant gedPersistant : listGedAll) {
			if(gedPersistant.getB_left() > rootDataRef.getB_left() && gedPersistant.getB_right() < rootDataRef.getB_right()){
				mapGed.put(gedPersistant.getCode(), gedPersistant.getId());
			}
		}
		
		//
		for (GedData dataGed : listDataRef) {
			String num = dataGed.getNumero().trim();
			String nom = dataGed.getLibelle();
			
			if(mapGed.get(num) == null){
				GedBean gedBean = new GedBean();
				gedBean.setCode(num);
				gedBean.setType_ged(type.substring(0, 2));
				gedBean.setSource_id(dataGed.getElement_id());
				gedBean.setLibelle(nom);
				gedBean.setParent_id(parentId);
				gedBean.setDate_maj(new Date());
				gedBean.setOpc_etablissement(ContextAppli.getEtablissementBean());
				
				treeService.createTree(gedBean);
				
				GedPersistant gedP = new GedPersistant();
				ReflectUtil.copyProperties(gedP, gedBean);
				listData.add(gedP);
			} else{
				GedPersistant currGedP = findById(GedPersistant.class, mapGed.get(num));
				currGedP.setLibelle(nom);
				entityManager.merge(currGedP);
			}
			entityManager.flush();
		}
	}

	@Override
	@Transactional
	public void createFichier(File file, String path, GedPersistant gedRoot) {
		
		Query query = getQuery("from GedFichierPersistant "
				+ "where opc_ged.id=:gedId "
				+ "and libelle=:libelle ")
				.setParameter("gedId", gedRoot.getId())
				.setParameter("libelle", file.getName());
				
		GedPersistant gedPe = (GedPersistant) getSingleResult(query);
		
		if(gedPe == null) {
			GedFichierPersistant gedFichierP = new GedFichierPersistant();
			gedFichierP.setLibelle(file.getName());
			gedFichierP.setIs_not_sup(true);
			gedFichierP.setExtention(file.getName().substring(file.getName().lastIndexOf(".")+1));
			gedFichierP.setFile_name(file.getName());
			gedFichierP.setPath(path);
			gedFichierP.setOpc_ged(gedRoot);
			//
			this.mergeEntity(gedFichierP);
		}
	}
	@Transactional
	@Override
	public GedPersistant checkGed(String libelle, String type, Long elementId, GedPersistant parentGed) {
		String req = "from GedPersistant "
				+ "where type_ged=:type and libelle=:lib ";
		
			if(StringUtil.isNotEmpty(elementId)) {
				req += "and source_id=:elementId ";
			}
		
		 Query gedq = getQuery(req)
			.setParameter("type", type)
			.setParameter("lib", libelle);
			
		 if(StringUtil.isNotEmpty(elementId)) {
			gedq.setParameter("elementId", elementId);
		}
				
		GedPersistant gedPe = (GedPersistant) getSingleResult(gedq);
		
		if(gedPe == null) {
			if(parentGed == null) {
				parentGed = treeService.getTreeRoot(GedPersistant.class);
			}
			
			GedBean gedP = new GedBean();
			gedP.setCode(type);
			gedP.setType_ged(type);
			gedP.setLibelle(libelle);
			gedP.setIs_not_supp(true);
			gedP.setDate_creation(new Date());
			gedP.setSignature("ADMIN");
			gedP.setDate_maj(new Date());
			
			gedP.setParent_id(parentGed.getId());
			
			treeService.createTree(gedP);
			
			gedPe = gedP;
		}

		return gedPe;
	}

	@Override
	public GedPersistant checkGedDir(String type_enum, String libelle) {
		// TODO Auto-generated method stub
		return null;
	}
}

class GedData{
	private Long element_id;
	private String libelle;
	private String numero;
	
	public GedData(Long element_id, String libelle, String numero) {
		this.element_id = element_id;
		this.libelle = libelle;
		this.numero = numero;
	}
	
	
	
	public Long getElement_id() {
		return element_id;
	}
	public void setElement_id(Long element_id) {
		this.element_id = element_id;
	}
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
}
 