package framework.model.util.synchro;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.Callable;

import javax.inject.Named;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;

import org.hibernate.proxy.HibernateProxy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.administration.persistant.JobPersistant;
import appli.model.domaine.administration.persistant.MailQueuePersistant;
import appli.model.domaine.administration.persistant.MessageDetailPersistant;
import appli.model.domaine.administration.persistant.MessagePersistant;
import appli.model.domaine.administration.persistant.TypeEnumPersistant;
import appli.model.domaine.administration.persistant.UserPersistant;
import appli.model.domaine.administration.persistant.ValTypeEnumPersistant;
import appli.model.domaine.administration.service.IUserService;
import appli.model.domaine.habilitation.persistant.ProfileMenuPersistant;
import appli.model.domaine.habilitation.persistant.ProfilePersistant;
import framework.controller.ControllerUtil;
import framework.controller.FileUtilController;
import framework.model.beanContext.AbonnePersistant;
import framework.model.beanContext.BasePersistant;
import framework.model.beanContext.CompteBancairePersistant;
import framework.model.beanContext.ComptePersistant;
import framework.model.beanContext.DataFormPersistant;
import framework.model.beanContext.DataValuesPersistant;
import framework.model.beanContext.EtablissementOuverturePersistant;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.beanContext.ExercicePersistant;
import framework.model.beanContext.RegionPersistant;
import framework.model.beanContext.SocietePersistant;
import framework.model.beanContext.VillePersistant;
import framework.model.common.service.MessageService;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.EncryptionEtsUtil;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;
import framework.model.service.GenericJpaService;
import framework.model.util.audit.ReplicationGenerationEventListener;
import framework.model.util.audit.SynchronisePersistant;

@Named
public class SynchroniseService extends GenericJpaService<SynchroniseBean, Long> implements ISynchroniseService{
	
	 @PersistenceUnit
	 private EntityManagerFactory emf;
	 
	/**
	 * Appelé depuis un crone pour récuperer les données depuis 
	 * le cloud périodiquement
	 */
	@Override
	public void getDataFromCloud() {
		String remoteUrl = StrimUtil.getGlobalConfigPropertieIgnoreErreur("url.cloud");
		if(StringUtil.isEmpty(remoteUrl)){
			return;
		}
		EtablissementPersistant etsP = getEtablissement();
		String etsCode = getDycCodeAuth(etsP, etsP.getCode_authentification());
		
		String response = "";
		try {
			response = FileUtilController.callURL(remoteUrl+"/synchroCtrl?mt=sync_in&ets="+etsCode);
			if("OK".equals(response)){
				return;
			}
		
			// Récupérer les données du cloud en local
			String ids = synchroniser(response, etsCode);
			// Poster au serveur les ids traités
			ControllerUtil.sendJsonPOST(ids, remoteUrl+"/synchroCtrl?mt=synch_ids&ets="+etsCode);
			
		} catch (Exception e) {
	 		System.out.println("GET FROM CLOUD ==>"+e.getMessage());
			return;
		}
	}
	
	@Override
	public void postDataToCloudAsync() {
		  ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor)ServiceUtil.getBusinessBean("taskExecutor");
		  taskExecutor.submit(new Callable<Object>() {
		     public Object call() throws Exception {
		    	 postDataToCloud();
		    	 return true;
		     }
		  });
	}
	
	/**
	 * Appelé depuis un crone pour poster les données vers  
	 * le cloud périodiquement
	 */
	@Override
	public void postDataToCloud() {
		String remoteUrl = StrimUtil.getGlobalConfigPropertieIgnoreErreur("url.cloud");
		if(StringUtil.isEmpty(remoteUrl)) {
			return;
		}
		
		EtablissementPersistant etsP = getEtablissement();
		String codeEts = getDycCodeAuth(etsP, etsP.getCode_authentification());
		
		try {
			String json = getJsonDataToSynchronise(codeEts);
			
			// On appel le serveur pour synchronisation
			if(StringUtil.isNotEmpty(json)){
				String syncResponse = ControllerUtil.sendJsonPOST(json, remoteUrl+"/synchroCtrl?mt=sync_out&ets="+codeEts);
				// On supprime les données synchronées en local
				if(StringUtil.isNotEmpty(syncResponse)) {
					String[] responseIds = StringUtil.getArrayFromStringDelim(syncResponse, "|");
					deleteSynchronisedIds(responseIds);
				}
			}
		} catch (Exception e) {
	 		System.out.println("POST TO CLOUD ==>"+e.getMessage());
			return;
		}
	}
	
	@Override
	public void deleteSynchronisedIds(String[] responseIds){
		if(responseIds == null || responseIds.length == 0){
			return;
		}
		
		EntityManager em = emf.createEntityManager();
		// Format => Classe type:action-id;action-id;action-id;action-id;
		if(responseIds != null) {
			Query query = em.createQuery("delete from SynchronisePersistant "
					+ "where id=:idSync");
			
			for(String idSync : responseIds) {
				if(idSync.indexOf("ERREUR:") != -1){
					continue;
				}
				
				EntityTransaction transaction = em.getTransaction();
				try{
					transaction.begin();
					query.setParameter("idSync", Long.valueOf(idSync))
							.executeUpdate();
					transaction.commit();
				} catch(Exception e){
					if(transaction.isActive()){
						transaction.rollback();
					}
					e.printStackTrace();
				}
			}
		}
		if(em.isOpen()){
			em.close();
		}
	}

	@Override
	public String getJsonDataToSynchronise(String etsCode) {
		EtablissementPersistant etsP = getEtablissement(etsCode);
		
		// Soi local avec avec synchro ou cloud avec établissement  ayant la sy,chro
		if(etsP== null || !BooleanUtil.isTrue(etsP.getIs_synchro_cloud())) {
			return "";
		}
		String operationId = System.currentTimeMillis()+"_"+new Random(4).nextInt();
		Map<String, List<Object>> mapData = getEntytiesToSynchronise(operationId, etsCode);
		String json = null;
		//
		if(mapData != null){
			Map<String, String> mapJson = new LinkedHashMap<>();
			for(String classType : mapData.keySet()){
				
				try {
					List<Object> listData = mapData.get(classType);
					List<Object> finalList = new ArrayList<>();
					for (Object object : listData) {
						if(object == null || StringUtil.isEmpty(object)) {
							continue;
						}
						if (object instanceof HibernateProxy) {
							object = ((HibernateProxy) object).getHibernateLazyInitializer().getImplementation();
						}
						finalList.add(object);
					}
					
					if(finalList.size() > 0) {
						System.out.println("Prepare JSON Class==>"+classType+"===>nbr===>"+finalList.size());
						
						json = ControllerUtil.getJSonDataAnnotStartegy(finalList);
						//
						mapJson.put(classType, json);
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			json = ControllerUtil.getJSonData(mapJson);
		} 
		
		return json;
	}
	
    /**
     * @return
     */
    private static EtablissementPersistant getEtablissement(){
    	IUserService userService = ServiceUtil.getBusinessBean(IUserService.class);
    
    	EtablissementPersistant etablissement = null;
 		etablissement = userService.findAll(EtablissementPersistant.class).get(0);
 	    etablissement = userService.findById(EtablissementPersistant.class, etablissement.getId());	
		
		return etablissement;
    }
	
	/**
	 * @param etsP
	 * @param codeAuth
	 * @return
	 */
	private static String getDycCodeAuth(EtablissementPersistant etsP, String codeAuth) {
		if(etsP == null || codeAuth == null) {
			return null;
		}
		EncryptionEtsUtil encryptionUtil = new EncryptionEtsUtil(EncryptionEtsUtil.getDecrypKey());
		codeAuth = encryptionUtil.decrypt(codeAuth);
		if(codeAuth == null) {
			codeAuth = etsP.getCode_authentification();
		}
		return codeAuth;
	}
	
	private Map<String, List<Object>> getEntytiesToSynchronise(String operationId, String etsCode){
		EtablissementPersistant etsP = getEtablissement(etsCode);
		
		// Soi local avec avec synchro ou cloud avec établissement  ayant la sy,chro
		if(etsP== null || !BooleanUtil.isTrue(etsP.getIs_synchro_cloud())) {
			return null;
		}
		List<SynchronisePersistant> data = null;
		
		if(ReplicationGenerationEventListener._IS_CLOUD_SYNCHRO_INSTANCE){
			data = getEntityManager().createQuery("from SynchronisePersistant "
					+ "where ets_code is null or ets_code=:etsCode "
					+ "order by id asc")
					.setParameter("etsCode", etsCode)
					.setMaxResults(100)
					.getResultList();
		} else{
			data = (List<SynchronisePersistant>) getEntityManager().createQuery("from SynchronisePersistant "
					+ "order by id asc")
						.setMaxResults(100)
						.getResultList();			
		}
		
		if(data == null || data.size() == 0) {
			return null;
		}
		Map<String, List<Object>> mapData = new LinkedHashMap<>();
		EntityManager em = getEntityManager();
		//
		for (SynchronisePersistant synchroP : data) {
			String keyClass = synchroP.getClassType();
			//
			List<Object> listData = mapData.get(keyClass);
			if(listData == null){
				listData = new ArrayList<>();
				mapData.put(keyClass, listData);
			}
			
			String action = synchroP.getAction();
			String sync_key = synchroP.getId()+"*|*"+synchroP.getClassType()+"*|*"+synchroP.getCode()+"*|*"+action;
			
			if("U".equals(action) || "C".equals(action)){
				Object dataE = getSingleResult( 
							em.createQuery("from "+synchroP.getClassType()+" where id=:elementId")
								.setParameter("elementId", synchroP.getElement_id())
							);
				if(dataE != null){
					Object clonedB = ReflectUtil.cloneBean(dataE);
					dataE = ((clonedB == null || StringUtil.isEmpty(clonedB)) ? dataE : clonedB);
					ReflectUtil.setProperty(dataE, "sync_key", sync_key);
					ReflectUtil.setProperty(dataE, "sync_opr_id", operationId);
					listData.add(dataE); 
				} else{// Si null alors transformer en suppression
					try {
						// Informer le serveur
						BasePersistant dataEn = (BasePersistant) findClassByName(synchroP.getClassType()).newInstance();
						dataEn.setSync_key(synchroP.getId()+"*|*"+synchroP.getClassType()+"*|*"+synchroP.getCode()+"*|*D");
						ReflectUtil.setProperty(dataEn, "sync_opr_id", operationId);
						listData.add(dataEn);
						// Supprimer de la base
						em.remove(synchroP);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else if("D".equals(action)){
				try {
					BasePersistant dataE = (BasePersistant) findClassByName(synchroP.getClassType()).newInstance();
					dataE.setSync_key(sync_key);
					ReflectUtil.setProperty(dataE, "sync_opr_id", operationId);
					listData.add(dataE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return mapData;
	}
	
	 
	 
	 
	 
	 
	 
	 
	 @Override
	 public String synchroniser(String requestData, String etsCodeAuth){
		Type typeOfHashMap = new TypeToken<Map<String, String>>() { }.getType();
		GsonBuilder b = new GsonBuilder();
		Gson gson = b.create();
		EntityManager em = emf.createEntityManager();
		EtablissementPersistant etsP = getEtablissement(etsCodeAuth);
		Map<String, String> newMap = gson.fromJson(requestData, typeOfHashMap); // This type must match TypeToken
		
		MessageService.getGlobalMap().put("IS_SYNCHRO", true);// Utilisée dans replication...service pour éviter double déclenchement de l'action
		
		Set<String> listSynchronised = new HashSet<>();
		
		List<Object> listDataAll = new ArrayList<>();
		for(String classTypeAction : newMap.keySet()){
			Class classTp = findClassByName(classTypeAction);
			if(classTp == null){
				continue;
			}
			listDataAll.addAll(ControllerUtil.getObjectFromJson(newMap.get(classTypeAction), classTp));	
		}
		
		Map<Long, Object> mapIdx = new LinkedHashMap<>();
		for (Object dataRemote : listDataAll){ 
			String syncKey = ReflectUtil.getStringPropertieValue(dataRemote, "sync_key");
			
			String[] responseKey = StringUtil.getArrayFromStringDelim(syncKey, "*|*");
			if(responseKey != null && responseKey.length > 0){
				Long synchroIdOrigine = Long.valueOf(responseKey[0]);
				mapIdx.put(synchroIdOrigine, dataRemote);
			}
		}
		
		// -------------------------------------------------------------------
		// Trier par ID pour garder le tri d'origine -------------------------
		SortedSet<Long> keys = new TreeSet<>(mapIdx.keySet());
		listDataAll.clear();
		//
		for (Long key : keys) {
			listDataAll.add(mapIdx.get(key));
		}
		// -------------------------------------------------------------------
				
		//	
		for (Object dataRemote : listDataAll) { 
			EntityTransaction transaction = em.getTransaction();
			
			try{
				transaction.begin();
				//Sync_key composition ==> dataP.getId()+"*|*"+dataP.getClassType()+"*|*"+dataP.getCode()+"*|*"+action
				String syncKey = ReflectUtil.getStringPropertieValue(dataRemote, "sync_key");
				String[] responseKey = StringUtil.getArrayFromStringDelim(syncKey, "*|*");
				String synchroIdOrigine = responseKey[0];
				String classType = responseKey[1];
				String code_func = responseKey[2];
				String action = responseKey[3];
				
				System.out.println("STEP ===>==>==>==>"+syncKey);
				
				if("D".equals(action)) {
					em.createQuery("delete from "+classType+" where code_func=:code_func")
						.setParameter("code_func", code_func)
						.executeUpdate();
				} else{
					Object dataLocal = getEntityByCodeFunc(em, classType, code_func);
					
					Date dateMajLocal = (dataLocal != null ? (Date) ReflectUtil.getObjectPropertieValue(dataLocal, "date_maj") : null);
					Date dateMajRemote = (dataRemote != null ? (Date) ReflectUtil.getObjectPropertieValue(dataRemote, "date_maj") : null);
					
				    if(dataLocal == null 
				    		|| dateMajRemote == null 
				    		|| dateMajLocal == null 
				    		|| (dateMajLocal.before(dateMajRemote) || dateMajLocal.compareTo(dateMajRemote) == 0)) {
				    	
				    	Field[] fields = dataRemote.getClass().getDeclaredFields();
				    	
				    	if(dataLocal != null) {// Cas MAJ
				    		Long idLocal = (Long) ReflectUtil.getObjectPropertieValue(dataLocal, "id"); 
				    		ReflectUtil.setProperty(dataRemote, "id", idLocal);
				    		// Remettre les listes
				    		for (Field field : fields) {
				    			if(field.getName().startsWith("list")){// Traiter le cas des listes avec cascade
				    				field.setAccessible(true);
				    				Object listData = field.get(dataLocal);
				    				if(listData != null){
				    					//listData.clear();
				    					ReflectUtil.setProperty(dataRemote, field.getName(), listData); 
				    				}
				    				field.setAccessible(false); 
				    			}
				    		}
				    	} else{// Cas création
				    		ReflectUtil.setProperty(dataRemote, "id", null);
				    	}
				    	// Les Opc
				    	for (Field field : fields) {
				    		if(field.getName().startsWith("opc_")){
				    			setOpcByCodeFunc(em, field, etsCodeAuth, dataRemote);
				    		} else if(field.getName().startsWith("list")){// Traiter le cas des listes avec cascade
				    			 OneToMany oneToMany = field.getAnnotation(OneToMany.class);
				    			 if(oneToMany != null && oneToMany.cascade() != null){
				    				 field.setAccessible(true);
				    				 
				    				 Object dataObj[] = null ;
				    				 if(field.get(dataRemote) instanceof Set) {
				    					 Set<BasePersistant> listData = (Set<BasePersistant>) field.get(dataRemote);
				    					 dataObj = (listData != null ? listData.toArray() : null);
				    				 } else {
				    					 List<BasePersistant> listData = (List<BasePersistant>) field.get(dataRemote);
				    					 dataObj = (listData != null ? listData.toArray() : null);
				    				 }
				    				 
				    				 if(dataObj != null){
					    				 for (Object baseP : dataObj) {
					    					 Field[] fieldsSub = baseP.getClass().getDeclaredFields();
				    				    	for (Field fieldSub : fieldsSub) {
				    				    		if(fieldSub.getName().startsWith("opc_") && !fieldSub.getType().equals(dataRemote.getClass())){
				    				    			setOpcByCodeFunc(em, fieldSub, etsCodeAuth, baseP);
				    				    		}
				    				    	}
				    				    	// Cas heritage
				    			    		Class superClass = baseP.getClass().getSuperclass();
				    						while(superClass.getDeclaredFields() != null && superClass.getDeclaredFields().length > 0){
				    			    			for (Field fieldSub : superClass.getDeclaredFields()) {
				    					    		if(fieldSub.getName().startsWith("opc_")){
				    					    			setOpcByCodeFunc(em, fieldSub, etsCodeAuth, baseP);
				    					    		}
				    			    			}
				    				    		superClass = superClass.getSuperclass();
				    			    		}
										}
				    				}
				    				field.setAccessible(false);
				    			 }
				    		 }
				    	}
				    	// Cas heritage
			    		Class superClass = dataRemote.getClass().getSuperclass();
						while(superClass.getDeclaredFields() != null && superClass.getDeclaredFields().length > 0){
			    			for (Field fieldSub : superClass.getDeclaredFields()) {
					    		if(fieldSub.getName().startsWith("opc_")){
					    			setOpcByCodeFunc(em, fieldSub, etsCodeAuth, dataRemote);
					    		}
			    			}
				    		superClass = superClass.getSuperclass();
			    		}
						
						ReflectUtil.setProperty(dataRemote, "opc_etablissement", null);
						ReflectUtil.setProperty(dataRemote, "opc_abonne", null);
			        	ReflectUtil.setProperty(dataRemote, "opc_societe", null);
			        	
						// Merge
			        	dataRemote = em.merge(dataRemote);
			        	em.flush();
			        	
			        	// Infos generiques
						ReflectUtil.setProperty(dataRemote, "opc_etablissement", etsP);
						ReflectUtil.setProperty(dataRemote, "opc_abonne", em.find(AbonnePersistant.class, etsP.getOpc_abonne().getId()));
			        	ReflectUtil.setProperty(dataRemote, "opc_societe", em.find(SocietePersistant.class, etsP.getOpc_societe().getId()));
			        	
			        	dataRemote = em.merge(dataRemote);
					    em.flush();
				    }
				}
				transaction.commit();
				
			 	listSynchronised.add(synchroIdOrigine);
		    } catch (Exception e) {
		    	if(transaction.isActive()){
		    		transaction.rollback();
		    	}
		    	e.printStackTrace();
			}
		}
		
		if(em.isOpen()){
			em.close();
		}
		MessageService.getGlobalMap().remove("IS_SYNCHRO");
		
		String ids = "";
		for(String key : listSynchronised){
			System.out.println("IDS REAL SYNCHRONISE OKK =============>"+key);
			ids += "|"+key;
		}
		
		return ids;
	}
	 
	@Override
	 public void addToSynchroniseQueu(String entitie){
		  AbonnePersistant abonneP = ContextAppli.getAbonneBean();
		  SocietePersistant societeP = ContextAppli.getSocieteBean();
		  EtablissementPersistant etablissementP = ContextAppli.getEtablissementBean();
		  
//		  String etsCodeAuth = etablissementP.getCode_authentification();
		  EntityManager em = emf.createEntityManager();
		  // Trier la liste dans certain ordre pour éviter les contraintes des clés étrangére
		  List<String> entiesTable = new ArrayList<>();
		  Metamodel metamodel = em.getMetamodel();

		  for (ManagedType<?> managedType : metamodel.getManagedTypes()) {
			  if (managedType.getJavaType().isAnnotationPresent(Entity.class)) {
				  if(managedType.getJavaType().getSimpleName().indexOf("View") != -1){
					  continue;
				  }
				  entiesTable.add(managedType.getJavaType().getSimpleName());
			  }
		  }
		  
		  entiesTable = getEntitiesOrder(entiesTable);
		  
		  em.close();
		  
		  final List<String> entiesTableFinal = entiesTable;
		  ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor)ServiceUtil.getBusinessBean("taskExecutor");
		  taskExecutor.submit(new Callable<Object>() {
		     public Object call() throws Exception {
		    	 
		    	 MessageService.getGlobalMap().put("IS_SYNCHRO", true);
		    	 
		    	 Date date_creation = new Date();
		    	 EntityManager em = emf.createEntityManager();
				 EntityTransaction trx = em.getTransaction();
				  
				 // Maj du code fonctionnel
		    	 try{
				  for(String entityName : entiesTableFinal){
					  if(entityName.indexOf("View") != -1){
						  continue;
					  }
					  if(entitie != null && !entitie.equals(entityName)) {
						  continue;
					  }
					  if(entityName.equals(SynchronisePersistant.class.getSimpleName())
							  || entityName.equals(VillePersistant.class.getSimpleName())
							  || entityName.equals(RegionPersistant.class.getSimpleName())){
						  continue;
					  }
					  
					  	int idx = 0;
				        List<BasePersistant> listData = null;
				        
				        try{
							listData = em.createQuery("from "+entityName+" order by id asc")
				        					.getResultList();
				        } catch(Exception e){
				        	System.out.println("Erreur Persistant =>"+entityName+"==>"+e.getMessage());
				        }
				        
				        if(listData == null || listData.size() == 0){
				        	continue;
				        }
				        trx.begin();	
				        for (Object object : listData) {
				        	if(idx != 0 && (idx%100)==0){
				        		trx.commit();
				        		trx.begin();
				        	}
				        	
					        Object codeFunc = ReflectUtil.getObjectPropertieValue(object, "code_func");
					        if(StringUtil.isEmpty(codeFunc)){
					        	codeFunc = ReplicationGenerationEventListener.getCodeFunc(object);
					        	ReflectUtil.setProperty(object, "code_func", codeFunc);
					        	em.merge(object);
					        	em.flush();
					        }
							idx++;
				        }
				        trx.commit();
				    }
				  
				  // Purge de la table de la synchro
				  if(entitie == null) {
					  trx.begin();
					  em.createQuery("delete from SynchronisePersistant")
					  			.executeUpdate();
					  trx.commit();
				  }
				  
				  // Ajout de la synchronisation
				  for(String entityName : entiesTableFinal){
					  if(entityName.equals(SynchronisePersistant.class.getSimpleName())
							  || entityName.equals(VillePersistant.class.getSimpleName())
							  || entityName.equals(RegionPersistant.class.getSimpleName())
							  ){
						  continue;
					  }
					  
					  	int idx = 0;
				        List<BasePersistant> listData = null;
				        try{
							listData = em.createQuery("from "+entityName+" order by id")
				        					.getResultList();
				        } catch(Exception e){
				        	System.out.println("Persistant =>"+entityName+"==>"+e.getMessage());
				        }
				        
				        if(listData == null || listData.size() == 0){
				        	continue;
				        }
				        
				        trx.begin();	
				        for (Object object : listData) {
				        	if(idx != 0 && (idx%100)==0){
				        		trx.commit();
				        		trx.begin();
				        	}
				        	
				        	boolean isToUpdate = false;
				        	Long id = (Long)ReflectUtil.getObjectPropertieValue(object, "id");
				        	// Code func
				        	String codeFunc = (String)ReflectUtil.getObjectPropertieValue(object, "code_func");
				        	if(StringUtil.isEmpty(codeFunc)){
								codeFunc = ReplicationGenerationEventListener.getCodeFunc(object);
								ReflectUtil.setProperty(object, "code_func", codeFunc);
								isToUpdate = true;
							}
				        	// Abonnée
				        	Object abonne = ReflectUtil.getObjectPropertieValue(object, "opc_abonne");
				        	if(abonne == null){
				        		ReflectUtil.setProperty(object, "opc_abonne", abonneP);
				        		isToUpdate = true;
				        	}
				        	// Société
				        	Object societe = ReflectUtil.getObjectPropertieValue(object, "opc_societe");
				        	if(societe == null){
				        		ReflectUtil.setProperty(object, "opc_societe", societeP);
				        		isToUpdate = true;
				        	}
				        	// Etablissement
				        	Object etablissemnt = ReflectUtil.getObjectPropertieValue(object, "opc_etablissement");
				        	if(etablissemnt == null){
				        		ReflectUtil.setProperty(object, "opc_etablissement", etablissementP);
				        		isToUpdate = true;
				        	}
				        	
				        	if(isToUpdate){
				        		em.merge(object);
				        		em.flush();
				        	}
				        	
			        		List<SynchronisePersistant> listSynchro = em.createQuery("from SynchronisePersistant "
			        				+ "where code=:code ")
			        			.setParameter("code", codeFunc)
	        					.getResultList();
			        	
			        		SynchronisePersistant synchroP = null;
			        		if(listSynchro != null && listSynchro.size() > 0) {
				        		synchroP = listSynchro.get(0);
				        		synchroP.setAction("U");
				        	} else {
				        		synchroP = new SynchronisePersistant();
				        		synchroP.setAction("C");
				        	}
				        	
				        	synchroP.setCode(codeFunc);
				        	synchroP.setDate_creation(date_creation);
				        	synchroP.setElement_id(id);
				        	synchroP.setClassType(entityName);
				        	synchroP.setEts_code(etablissementP.getCode_authentification().trim());
				        	
			        		em.merge(synchroP);
			        		em.flush();
							idx++;
				        }
				        trx.commit();
				    }
		    	 } catch(Exception e){
		    		 if(trx.isActive()){
		    			 trx.rollback();
		    		 }
		    		 e.printStackTrace();
		    	 } finally{
		    		 em.close();
		    	 }
		    	 
		    	 MessageService.getGlobalMap().remove("IS_SYNCHRO");
		    	 
				  return true;
		     }});
	  }
		
	
	
	
	public static final String[] searchPackages = {
		    "appli.model.domaine.administration.persistant",
		    "appli.model.domaine.auto.persistant",
		    "appli.model.domaine.caisse.persistant",
		    "appli.model.domaine.compta.persistant",
		    "appli.model.domaine.personnel.persistant.paie",
		    "appli.model.domaine.fidelite.persistant",
		    "appli.model.domaine.habilitation.persistant",
		    "appli.model.domaine.personnel.persistant",
		    "appli.model.domaine.stock.persistant",
		    "appli.model.domaine.vente.persistant",
		    "framework.model.beanContext",
		    "appli.model.domaine.caisse_restau.persistant",
		    "appli.model.domaine.stock.persistant.centrale"
		};

	  public Class<?> findClassByName(String name) {
	    for(int i=0; i<searchPackages.length; i++){
	      try{
	    	  return Class.forName(searchPackages[i] + "." + name);
	      } catch (Exception e){
	      }
	    }
	    
	    System.out.println("PACKAGE NOT FOUND *******======>"+name);
	    return null;
	  }
	  
	  /**
	   * Poster les données en attente dans la table data_synchronise
	   * @param entitie
	   */
/*	  @Override
	  public void postAllDataWaiting(){
		  ReplicationGenerationEventListener._IS_POST_INPROGRESS = true;
		  
/*		  if(ContextAppli.IS_CLOUD_MASTER()) {
			  List<EtablissementPersistant> listEtsP = getQuery("from EtablissementPersistant "
						+ "where url_distant != null "
						+ "and is_synchro_cloud=1")
						.getResultList();
	
				for (EtablissementPersistant etsP : listEtsP) {
					if(StringUtil.isEmpty(etsP.getUrl_distant()) || !BooleanUtil.isTrue(etsP.getIs_synchro_cloud())) {
						continue;
					}
					postAllToRemote(etsP, etsP.getUrl_distant());
				}
		  } else {*/
	/*		  String backOfficeUrl = StrimUtil.getGlobalConfigPropertieIgnoreErreur("url.cloud");
			  if(StringUtil.isNotEmpty(backOfficeUrl)){
				  EtablissementPersistant etsP = ((EtablissementPersistant)getQuery("from EtablissementPersistant")
						.getResultList().get(0));
				  if(etsP != null && BooleanUtil.isTrue(etsP.getIs_synchro_cloud())) {
					  	 postDataToCloud(etsP, backOfficeUrl);
					}
			  }					
			 
		  //}
		  
		  ReplicationGenerationEventListener._IS_POST_INPROGRESS = false;
	  }*/
	  
//	  public void createAllTriggers(Connection connection){
//		  	String dataBaseName = connection.getCatalog();
//			String req = "SELECT table_name FROM information_schema.COLUMNS where table_schema='"+dataBaseName+"' and COLUMN_NAME='etablissement_id'";
//			Statement statement = connection.createStatement();
//			ResultSet rs = statement.executeQuery(req);
//			ContextAppli.getEtablissementBean().getCode_authentification();
//			ContextAppli.getSocieteBean().getCode_func();
//			
//	        while (rs.next()) {
//	            String table_name = rs.getString("table_name");
//	            
//	            String reqTrigger = 
//	            "CREATE TRIGGER "+table_name+"_trigger "
//	            + " AFTER INSERT "
//	            + " ON "+table_name
//	            + " FOR EACH ROW "
//	            + " Insert into data_synchronise(code, action, tableName, element_id, ets_code, societe_code, date_creation) "
//	            + " VALUES(concat('"+table_name.substring(0, 6)+"'), 'I', '"+table_name+"', NEW.id, NEW.abonne_id, NEW.etablissement_id, NEW.societe_id) ";
//	            
//				PreparedStatement statementDel = connection.prepareStatement(reqTrigger);
//				int nbrDelete = statementDel.executeUpdate();
//				statementDel.close();
//	        }
//			statement.close();
//	  }
//	  
//
//		/**
//		 * @param entityManager
//		 * @return
//		 */
//		private Connection getConnection(EntityManager entityManager){
//			SessionImpl sessionImpl = (SessionImpl)entityManager.getDelegate();
//			Connection connection = null;
//			try {
//				connection = sessionImpl.connection();
//				//connection.setAutoCommit(false);
//			} catch (Exception e1) {
//				throw new RuntimeException(e1);
//			};
//			
//			return connection;
//		}
	  
	  private List<String> getEntitiesOrder(List<String> listDbClass){
		  
		  //::!!!!!!!! Atention entity en String car non incluse dans admi  et certains projets donc problème compilation
		  List<String> listClass = new ArrayList<>();
		  listClass.add(AbonnePersistant.class.getSimpleName());
		  
		  listClass.add(SocietePersistant.class.getSimpleName());
		  listClass.add(EtablissementPersistant.class.getSimpleName());
		  
		  listClass.add(RegionPersistant.class.getSimpleName());
		  listClass.add(VillePersistant.class.getSimpleName());
		  listClass.add(ComptePersistant.class.getSimpleName());
		  
		  listClass.add(TypeEnumPersistant.class.getSimpleName());
		  listClass.add(ValTypeEnumPersistant.class.getSimpleName());
		  
		  listClass.add(ExercicePersistant.class.getSimpleName());
		  
		  listClass.add("FamillePersistant");
		  listClass.add("FamilleStockPersistant");
		  listClass.add("FamilleFournisseurPersistant");
		  listClass.add("FamilleCuisinePersistant");
		  listClass.add("FamilleConsommationPersistant");
		  listClass.add("FamilleMenuPersistant");
		  
		  listClass.add("FournisseurPersistant");
		  
		  listClass.add("EmplacementPersistant");
		  
		  listClass.add("PostePersistant");
		  listClass.add("ClientPersistant");
		  
		  listClass.add("EmployePersistant");
		  
		  listClass.add(ProfilePersistant.class.getSimpleName());
		  listClass.add(UserPersistant.class.getSimpleName());
		  
		  listClass.add("TravauxChantierPersistant");
		  listClass.add("TravauxPersistant");
		  
		  listClass.add("ArticlePersistant");
		  listClass.add("ArticleDetailPersistant");
		  listClass.add("ArticleClientPrixPersistant");
		  
		  listClass.add(CompteBancairePersistant.class.getSimpleName());
		  
		  listClass.add("CaissePersistant");
		  listClass.add("JourneePersistant");
		  listClass.add("CaisseJourneePersistant");
		  
		  listClass.add("VehiculePersistant");
		  
		  listClass.add("CompteBancaireFondsPersistant");
		  
		  listClass.add("ListChoixPersistant");
		  listClass.add("ListChoixDetailPersistant");
		  listClass.add("MenuCompositionPersistant");
		  listClass.add("MenuCompositionDetailPersistant");
		  
		  listClass.add("AgencementPersistant");
		  
		  listClass.add("PlanningPersistant");
		  listClass.add("AssurancePersistant");
		  listClass.add("DemandeTransfertPersistant");
		  
		  listClass.add("VignettePersistant");
		  
		  listClass.add("InventairePersistant");
		  listClass.add("InventaireDetailPersistant");

		  listClass.add("MouvementPersistant");
		  
		  listClass.add(MailQueuePersistant.class.getSimpleName());
		  listClass.add("PreparationArticlePersistant");
		  
		  listClass.add("PreparationTransfoArticlePersistant");
		  listClass.add("PointageEventPersistant");
		  
		  listClass.add("VisitePersistant");
		  listClass.add("CarteFidelitePersistant");
		  listClass.add("FournisseurChequePersistant");
		  
		  listClass.add("TicketCaisseConfDetailPersistant");
		  listClass.add("ChargeDiversPersistant");
		  listClass.add("TokenPersistant");
		  
		  listClass.add("GedPersistant");
		  listClass.add("GedFichierPersistant");
		  
		  listClass.add("ClientContactPersistant");
		  
		  listClass.add("FournisseurContactPersistant");
		  listClass.add("PaiementPersistant");
		 
		  listClass.add("CarburantPersistant");
		  listClass.add(EtablissementOuverturePersistant.class.getSimpleName());
		  listClass.add("IncidentPersistant");
		  
		  listClass.add("CuisineJourneePersistant");
		  
		  listClass.add("PreparationPersistant");
		  listClass.add("TypeFraisPersistant");
		  listClass.add("TypePlanningPersistant");
		  listClass.add("EmplacementSeuilPersistant");
		  
		  listClass.add("MouvementArticlePersistant");
		  listClass.add("ParametragePersistant");
		  listClass.add(MessagePersistant.class.getSimpleName());
		  
		  listClass.add("TicketCaisseConfPersistant");
		  
		  listClass.add("FraisDetailPersistant");
		  listClass.add("DemandeTransfertArticlePersistant");
		  listClass.add("VidangeDetailPersistant");
		  listClass.add("OffrePersistant");
		  listClass.add("MarquePersistant");
		  listClass.add(JobPersistant.class.getSimpleName());
		 
		  listClass.add("CarteFideliteConsoPersistant");
		  listClass.add("TicketCaissePersistant");
		  listClass.add(ProfileMenuPersistant.class.getSimpleName());
		  
		  listClass.add("VidangePersistant");
		  listClass.add(MessageDetailPersistant.class.getSimpleName());
		  
		  listClass.add("CaisseMouvementPersistant");
		  listClass.add("CaisseMouvementStatutPersistant");
		  listClass.add("CaisseMouvementArticlePersistant");
		  listClass.add("CaisseMouvementOffrePersistant");
		  listClass.add("ClientPortefeuilleMvmPersistant");		  
		  listClass.add("SalairePersistant");
		  
		  listClass.add("SocieteLivrContactPersistant");
		  listClass.add("PreparationTransfoPersistant");
		  
		  listClass.add(DataFormPersistant.class.getSimpleName());
		  listClass.add(DataValuesPersistant.class.getSimpleName());
		  
		  listClass.add("CompteInfosPersistant");
		  listClass.add("SocieteLivrPersistant");
		  
		  listClass.add("CompteBancaireBordereauPersistant");
		  listClass.add("EcriturePersistant");
		  listClass.add("LotArticlePersistant");
		  
		  listClass.add("CaisseMouvementTracePersistant");
		  
		  listClass.add("EtatFinancePersistant");
		  listClass.add("EtatFinanceDetailPersistant");
		  listClass.add("EtatFinancePaiementPersistant");
		  
		  listClass.add("FraisPersistant");
		  listClass.add("PreparationTransfoArticlePersistant");
		  listClass.add("ArticleBalancePersistant");
		  listClass.add("ArticleStockInfoPersistant");
		  listClass.add("CarteFideliteClientPersistant");
		  
		  listClass.add("PointagePersistant");
		  listClass.add("CarteFidelitePointsPersistant");
		  
		  for(String cl : listDbClass){
			  if(!listClass.contains(cl)){
				  listClass.add(cl);
			  }
		  }
		  
		  return listClass;
	  }
	
	/**
	 * @param em
	 * @param field
	 * @param dataRemote
	 */
	private void setOpcByCodeFunc(EntityManager em, Field field, 
			String etsCode,
			Object dataRemote){
		
		Object remoteEntity = null;
		try {
			field.setAccessible(true);
			Object opcEntity = field.get(dataRemote);
			if(opcEntity == null){
				return;
			}
			
			String currCode = ReflectUtil.getStringPropertieValue(opcEntity, "code_func");
			String className = opcEntity.getClass().getSimpleName();
			Object opc_persistant = super.getEntityByCodeFunc(em, className, currCode);

			// Si l'opc n'est pas trouvé alors on va la demander à l'instance et la créer pour éviter l'erreur de sauvegarde
			if(opc_persistant == null) {
				EtablissementPersistant etsP = getEtablissement(etsCode);
				String remoteUrl = null;
				if(ReplicationGenerationEventListener._IS_CLOUD_SYNCHRO_INSTANCE){
					remoteUrl = etsP.getUrl_distant();
				} else {
					remoteUrl = StrimUtil.getGlobalConfigPropertieIgnoreErreur("url.cloud");
				}
				if(StringUtil.isNotEmpty(remoteUrl)) {
					String json = FileUtilController.callURL(remoteUrl+"/synchroCtrl?mt=load_opc&cfunc="+currCode+"&cls="+className+"&ets="+etsCode);
					remoteEntity = ControllerUtil.getSingleObjectFromJson(json, opcEntity.getClass());
					// Merger en local
					if(remoteEntity != null) {
						MessageService.getGlobalMap().put("IS_SYNCHRO", true);
						
						ReflectUtil.setProperty(remoteEntity, "id", null);
			        	ReflectUtil.setProperty(remoteEntity, "opc_abonne", etsP.getOpc_abonne());
			        	ReflectUtil.setProperty(remoteEntity, "abonne_id", etsP.getOpc_abonne().getId());
			        	ReflectUtil.setProperty(remoteEntity, "opc_societe", etsP.getOpc_societe());
			        	ReflectUtil.setProperty(remoteEntity, "societe_id", etsP.getOpc_societe().getId());
			        	ReflectUtil.setProperty(remoteEntity, "opc_etablissement", etsP);
			        	ReflectUtil.setProperty(remoteEntity, "etablissement_id", etsP.getId());
			        	
			        	// Effacrer les listes
			        	Field[] fields = remoteEntity.getClass().getDeclaredFields();
			    		for (Field fieldLst : fields) {
			    			if(fieldLst.getName().startsWith("list")){// Traiter le cas des listes avec cascade
			    				fieldLst.setAccessible(true);
			    				ReflectUtil.setProperty(dataRemote, fieldLst.getName(), null); 
			    				fieldLst.setAccessible(false); 
			    			}
			    		}
			    		
			    		for (Field fieldSub : remoteEntity.getClass().getDeclaredFields()) {
				    		if(fieldSub.getName().startsWith("opc_")){
				    			setOpcByCodeFunc(em, fieldSub, etsCode, remoteEntity);
				    		}
			    		}
			    		
			        	//
						opc_persistant = em.merge(remoteEntity);
						em.flush();
						MessageService.getGlobalMap().remove("IS_SYNCHRO", true);
					}
				}
			}
			if(opc_persistant != null) {
				ReflectUtil.setProperty(dataRemote, field.getName(), opc_persistant);
			}
			field.setAccessible(false);
		} catch (Exception e) {
			if(remoteEntity != null) {
				System.out.println("ENTITE SYNC ERR ===>"+remoteEntity.getClass().getSimpleName());
			}
			e.printStackTrace();
		}
	} 
 
	/**
	 * @param codeAuth
	 * @return
	 */
	private EtablissementPersistant getEtablissement(String codeAuth){
		return (EtablissementPersistant) getSingleResult(getEntityManager().createQuery("from EtablissementPersistant ds "
				+ "where code_authentification=:codeAuth")
				.setParameter("codeAuth", codeAuth));
	}

	@Override
	public Object getOpcByCodeFunc(String entityName, String codeFunc) {
		if(entityName.indexOf("$") != -1) {
			entityName = entityName.substring(0, entityName.indexOf("$"));
		}
		return getSingleResult(getEntityManager().createQuery("from "+entityName+" ds "
				+ "where code_func=:codeFunc")
				.setParameter("codeFunc", codeFunc));
	}
}
