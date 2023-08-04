package framework.model.util.audit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.event.spi.EventSource;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.hibernate.event.spi.PreUpdateEvent;
import org.hibernate.event.spi.PreUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import appli.controller.domaine.util_erp.ContextAppli;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.common.service.MessageService;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.synchro.ISynchroniseService;

@Component
public class ReplicationGenerationEventListener implements 
    PostDeleteEventListener, PostInsertEventListener, PostUpdateEventListener,
    PreInsertEventListener, PreUpdateEventListener {

	public static boolean _IS_CLOUD_SYNCHRO_INSTANCE = false; //Cas master cloud donc si c'est un cloud
	public static boolean _IS_LOCAL_SYNCHRO_INSTANCE = false; //Si instance non cloud mais avec synchro activée
	public static boolean _IS_POST_INPROGRESS = false;
	public static List<String> _LIST_SYNCHRO_PREVENT = new ArrayList<>();
	
    @Override
    public void onPostDelete(PostDeleteEvent event) {
        Class<?> entityClass = event.getEntity().getClass();
        if(SynchronisePersistant.class.equals(entityClass)){
			return;
		}
        if(MessageService.getGlobalMap().get("IS_SYNCHRO") != null){
			return;
		}
       System.out.println("-------DELETE--------->"+entityClass.getSimpleName());
       addSynchroniseDataAsync(event.getSession(), event.getEntity(), "D");
    }

    @Override
    public void onPostInsert(PostInsertEvent event) {
    	Class<?> entityClass = event.getEntity().getClass();
    	if(SynchronisePersistant.class.equals(entityClass)){
			return;
		}
    	if(MessageService.getGlobalMap().get("IS_SYNCHRO") != null){
			return;
		}
    	System.out.println("--------INSERT-------->"+entityClass.getSimpleName());
        
    	addSynchroniseDataAsync(event.getSession(), event.getEntity(), "C");
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        Class<?> entityClass = event.getEntity().getClass();
        if(SynchronisePersistant.class.equals(entityClass)){
			return;
		}
        if(MessageService.getGlobalMap().get("IS_SYNCHRO") != null){
			return;
		}
        System.out.println("-------UPDATE--------->"+entityClass.getSimpleName());
        
        addSynchroniseDataAsync(event.getSession(), event.getEntity(), "U");	
    }

	@Override
	public boolean requiresPostCommitHanding(EntityPersister arg0) {
		return true;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addSynchroniseDataAsync(EventSource session, Object entity, String action){
		if(!ReplicationGenerationEventListener._IS_LOCAL_SYNCHRO_INSTANCE && !ReplicationGenerationEventListener._IS_CLOUD_SYNCHRO_INSTANCE) {
			return;
		}
		ISynchroniseService synchroService = ServiceUtil.getBusinessBean(ISynchroniseService.class);
		EtablissementPersistant etsP = (EtablissementPersistant) ReflectUtil.getObjectPropertieValue(entity, "opc_etablissement");
		if(etsP != null) {
			etsP = synchroService.findById(EtablissementPersistant.class, etsP.getId());
		} else if(ReplicationGenerationEventListener._IS_LOCAL_SYNCHRO_INSTANCE) {
			etsP = synchroService.findAll(EtablissementPersistant.class).get(0);
		}
		if(etsP == null) {
			etsP = ContextAppli.getEtablissementBean();
		}
		
		if(etsP == null || (ReplicationGenerationEventListener._IS_CLOUD_SYNCHRO_INSTANCE && !BooleanUtil.isTrue(etsP.getIs_synchro_cloud()))) {
			return;
		}
		final EtablissementPersistant etsPF = etsP;
		
		ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor)ServiceUtil.getBusinessBean("taskExecutor");
		taskExecutor.submit(new Callable() {
			public Object call() throws Exception {
				try{
					MessageService.getGlobalMap().put("GLOBAL_ETABLISSEMENT", etsPF);
					addSynchroniseData(session, entity, action);
				} catch(Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		  }
		);
		
	}

	/**
	 * @param session
	 * @param entity
	 * @param action
	 */
	private void addSynchroniseData(EventSource session, Object entity, String action){
		ISynchroniseService synchroService = ServiceUtil.getBusinessBean(ISynchroniseService.class);
		String classTypeStr = entity.getClass().getSimpleName();
		// Ne pas tracer la synchro elle même		
		if(classTypeStr.equals(SynchronisePersistant.class.getSimpleName())){
			return;
		}
		// Ce token est généré depuis la synchronisation
        String operationId = ReflectUtil.getStringPropertieValue(entity, "sync_opr_id");
        
        if(StringUtil.isNotEmpty(operationId)){
        	return;
        }

        boolean isCreation = action.equals("C");
		Long elementId = (Long) ReflectUtil.getObjectPropertieValue(entity, "id");
		
		 if(elementId == null) {
			 return;
		 }
		
        String key = elementId+"_"+classTypeStr+"_"+action;			        
        if(isCreation && _LIST_SYNCHRO_PREVENT.contains(key)){
        	return;
        }
		
		if(isCreation){ 
			_LIST_SYNCHRO_PREVENT.add(key);
        }
		Session sessionH = session.getSessionFactory().openSession();
		Transaction transaction = sessionH.getTransaction();
		try{
			String codeFunc = ReflectUtil.getStringPropertieValue(entity, "code_func");
			if(StringUtil.isEmpty(codeFunc)){
				// Gestion des cascades
				String currIdStr = ReflectUtil.getStringPropertieValue(entity, "id");
				if(StringUtil.isNotEmpty(currIdStr)) {
					entity = synchroService.findById(entity.getClass(), Long.valueOf(currIdStr));
					codeFunc = ReflectUtil.getStringPropertieValue(entity, "code_func");
				}
				if(StringUtil.isEmpty(codeFunc)){
					codeFunc = getCodeFunc(entity);
					ReflectUtil.setProperty(entity, "code_func", codeFunc);
				}
				transaction.begin();				
				entity = sessionH.merge(entity);
				transaction.commit();
			}
			
			/**
			 * Dans le cas du master on poste à tous les établissement car au momement de la synchronisation 
			 * chaque établissement supprime la data synchronisées
			 */
			EtablissementPersistant etsP = ContextAppli.getEtablissementBean();
			Date date_creation = new Date();
			List<SynchronisePersistant> listSynchP =
					/*sessionH.createQuery("from SynchronisePersistant "
							+ "where classType=:classType "
							+ "and action=:action "
							+ "and element_id=:elementId "
							+ "and ets_code=:etsCode")
						.setParameter("action", action)
						.setParameter("classType", classTypeStr)
						.setParameter("elementId", elementId)
						.setParameter("etsCode", etsP.getCode_authentification())
						.setMaxResults(20)
						.list();*/
			sessionH.createQuery("from SynchronisePersistant "
					+ "where code=:codeFunc")
				.setParameter("codeFunc", codeFunc)
				.list();
			SynchronisePersistant syncData = listSynchP.size() > 0 ? listSynchP.get(0) : null;
			
			if(syncData == null){
				syncData = new SynchronisePersistant();
				syncData.setEts_code(etsP.getCode_authentification().trim());
				syncData.setClassType(classTypeStr);
				syncData.setCode(codeFunc);
				syncData.setElement_id(elementId);
				
				syncData.setAction(action);
				syncData.setDate_creation(date_creation);
				syncData.setEts_code(etsP.getCode_authentification());

				transaction.begin();
				sessionH.merge(syncData);
				transaction.commit();
			}
			
		} catch(Exception e){
			e.printStackTrace();
			transaction.rollback();	
		} finally{
			sessionH.close();	
		}
		
		// Synchronisation immédiate
//		if(ReplicationGenerationEventListener._IS_LOCAL_SYNCHRO_INSTANCE) {
//			synchroService.postDataToCloud();
//		}
	}
	
	public static String getCodeFunc(Object object) {
		 EtablissementPersistant etablissementP = ContextAppli.getEtablissementBean();
		 if(etablissementP == null) {
			 etablissementP = (EtablissementPersistant) ReflectUtil.getObjectPropertieValue(object, "opc_etablissement");
		 }
		 
		 if(etablissementP == null && ReplicationGenerationEventListener._IS_LOCAL_SYNCHRO_INSTANCE) {
			 ISynchroniseService synchroService = ServiceUtil.getBusinessBean(ISynchroniseService.class);
			 etablissementP = synchroService.findAll(EtablissementPersistant.class).get(0);
		 }
		 
		 Object id = ReflectUtil.getObjectPropertieValue(object, "id");
		 String etsCodeAuth = (etablissementP == null ? "---------" : etablissementP.getCode_authentification());
		  
		 String codeFunc = id.toString()
				+ object.getClass().getSimpleName().substring(0, 2)
				+ etsCodeAuth.substring(0, 4);
				//+(""+System.currentTimeMillis()).substring(6);
		  
		 return codeFunc; 
	}

	@Override
	public boolean onPreUpdate(PreUpdateEvent event) {
		if(SynchronisePersistant.class.equals(event.getEntity().getClass())){
			return false; 
		}
		return false;
	}

	@Override
	public boolean onPreInsert(PreInsertEvent event) {
		if(SynchronisePersistant.class.equals(event.getEntity().getClass())){
			return false;
		}
		
		ServiceUtil.setGenericFields(event.getEntity());
		return false;
	}
}