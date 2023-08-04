package appli.model.domaine.util_srv.job;

import java.util.List;

import javax.persistence.EntityManager;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.administration.dao.IParametrageDao;
import appli.model.domaine.vente.persistant.CaisseJourneePersistant;
import appli.model.domaine.vente.persistant.JourneePersistant;
import framework.model.common.util.ServiceUtil;

public class JobFermetureJournee implements Job {
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		EntityManager em = null;
		try {
			IParametrageDao parametrageDao = (IParametrageDao)ServiceUtil.getBusinessBean(IParametrageDao.class);
			em = parametrageDao.getEntityManager().getEntityManagerFactory().createEntityManager();
			
			List<CaisseJourneePersistant> listCaisseJournee = parametrageDao.getQuery(em, "from CaisseJourneePersistant").getResultList();
			// Si journee exist
			if(listCaisseJournee == null || listCaisseJournee.size() == 0){
				return;
			}
			// Si toutes les caisses fermées
			for (CaisseJourneePersistant caisseJourneePersistant : listCaisseJournee) {
				if(!caisseJourneePersistant.getStatut_caisse().equals(ContextAppli.STATUT_JOURNEE.CLOTURE.getStatut())){
					return;
				}
			}
			
			List<JourneePersistant> listJournee = parametrageDao.getQuery(em, "from JourneePersistant where and statut_journee=:statut")
					.setParameter("statut", ContextAppli.STATUT_JOURNEE.OUVERTE.getStatut())
					.getResultList();
			
			// Si journee not exist
			if(listJournee == null || listJournee.size() == 0){
				return;
			}
			
			em.getTransaction().begin();
			JourneePersistant journeePersistant = listJournee.get(0);
			journeePersistant.setStatut_journee(ContextAppli.STATUT_JOURNEE.CLOTURE.getStatut());
			em.merge(journeePersistant);
			em.getTransaction().commit();
		} catch(Exception e) {
			em.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			if(em != null) {
				em.close();
			}
		}
	}
}

