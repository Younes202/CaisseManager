package framework.model.util.audit;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.stereotype.Component;

@Component
public class HibernateEventWiring {

	@PersistenceUnit
    private EntityManagerFactory emf;
	 @Inject
    private ReplicationGenerationEventListener listener;

    @PostConstruct
    public void registerListeners() {
        if(!ReplicationGenerationEventListener._IS_CLOUD_SYNCHRO_INSTANCE && 
        		!ReplicationGenerationEventListener._IS_LOCAL_SYNCHRO_INSTANCE){
        	return;
        }
    	SessionFactoryImpl sessionFactory = emf.unwrap(SessionFactoryImpl.class);
    	EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
    	
        registry.getEventListenerGroup(EventType.POST_COMMIT_INSERT).appendListener(listener);
        registry.getEventListenerGroup(EventType.POST_COMMIT_UPDATE).appendListener(listener);
        registry.getEventListenerGroup(EventType.POST_COMMIT_DELETE).appendListener(listener);
        registry.getEventListenerGroup(EventType.PRE_INSERT).appendListener(listener);
        registry.getEventListenerGroup(EventType.PRE_UPDATE).appendListener(listener);
    }
}