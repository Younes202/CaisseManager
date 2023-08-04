package appli.model.domaine.administration.service;

import java.io.File;
import java.util.List;

import appli.controller.domaine.administration.bean.GedBean;
import appli.model.domaine.administration.persistant.GedFichierPersistant;
import appli.model.domaine.administration.persistant.GedPersistant;
import appli.model.domaine.personnel.persistant.ClientPersistant;
import appli.model.domaine.personnel.persistant.EmployePersistant;
import appli.model.domaine.stock.persistant.FournisseurPersistant;
import framework.model.service.IGenericJpaService;

public interface IGedService extends IGenericJpaService<GedBean, Long> {
	GedFichierPersistant mergeDetail(GedFichierPersistant gedFichierBean);
	
	void deleteDetail(Long detId);

	void majArborscenceClient(List<ClientPersistant> listClient);

	void majArborscenceFournisseur(List<FournisseurPersistant> listClient);

	void deleteGed(Long gedId);

	List<GedPersistant> getFileByExtentionOrName(String ged_name, String extention, Long clientId, Long fournisseurId, Long employeId);

	void majArborscenceEmploye(List<EmployePersistant> listEmploye);

	/**
	 * @param libelle 
	 * @param listDataRef
	 * @param type
	 * @param libelle
	 * @return 
	 */
	GedPersistant checkGedDir(String type_enum, String libelle);

	void createFichier(File file, String path, GedPersistant gedRootF);

	void checkFileExist(GedPersistant gedRoot);

	GedPersistant checkGed(String libelle, String type, Long elementId, GedPersistant parentGed);

	String creatLibelleFO(FournisseurPersistant opc_fourni);

	String creatLibelleCL(ClientPersistant opc_cli);

	String creatLibelleEM(EmployePersistant opc_responsable);

}
