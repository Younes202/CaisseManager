/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework.model.util.synchro;

import framework.model.service.IGenericJpaService;

/**
 *
 * @author hp
 */
public interface ISynchroniseService extends IGenericJpaService<SynchroniseBean, Long> {
	String synchroniser(String requestData, String etsCodeAuth);

	void addToSynchroniseQueu(String entitie);

	Object getOpcByCodeFunc(String entityName, String codeFunc);

	/**
	 * Appelé depuis un crone pour récuperer les données depuis 
	 * le cloud périodiquement
	 */
	void getDataFromCloud();

	/**
	 * Appelé depuis un crone pour poster les données vers  
	 * le cloud périodiquement
	 */
	void postDataToCloud();

	String getJsonDataToSynchronise(String etsCodeAuth);

	void deleteSynchronisedIds(String[] responseIds);

	void postDataToCloudAsync();

}
