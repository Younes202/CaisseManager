package appli.model.domaine.administration.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import appli.model.domaine.administration.persistant.ParametragePersistant;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.service.IGenericJpaService;

public interface IParametrageService extends IGenericJpaService<ParametragePersistant, Long> {

	ParametragePersistant getParameterByCode(String param);

	void purgerDonneesBase(Long etatFinancierId, Map<String, ?> mapPurge);

	void dumpBase(String tagetDir);

	Map<String, String> getSystemInfos();
	
	void updateParams(Map<String, Object> params);

	void executerInitScript(Date currentDateSoft);

	void updateParams(Map<String, Object> params, String titrePublicite, String msgPublicite, Long terminalId);

	ParametragePersistant getParameterByCode(String code, Long caisseId);

	void mergeParams(ParametragePersistant ... params);

	List<ParametragePersistant> getParameterByGroupe(String groupe);

	Map<String, String> getCodeBarreBalanceStart();

	void updateParams(Map<String, Object> params, Long terminalId);

	void addDataRequired();

	void executerScriptView();

	void mergeLocalPrinters(String codeAuth, String imprimantes);

	EtablissementPersistant getEtsOneOrCodeAuth();

	void mergeEtablissement(EtablissementPersistant restaurantP, boolean isFromParam);

	void majInfosSaveDb(String path, String startDt1, String startDt2);
}
