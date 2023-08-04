package appli.model.domaine.util_srv.job;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.administration.dao.IParametrageDao;
import appli.model.domaine.vente.persistant.JourneePersistant;
import framework.model.common.util.DateUtil;
import framework.model.common.util.DateUtil.TIME_ENUM;
import framework.model.common.util.ServiceUtil;

public class JobOuvertureJournee implements Job {
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		EntityManager em = null;
		try {
			IParametrageDao parametrageDao = (IParametrageDao)ServiceUtil.getBusinessBean(IParametrageDao.class);
			em = parametrageDao.getEntityManager().getEntityManagerFactory().createEntityManager();
			
			//
			List<JourneePersistant> listJourneeOuverte = parametrageDao.getQuery(em, "from JourneePersistant where statut_journee=:statut")
					.setParameter("statut", ContextAppli.STATUT_JOURNEE.OUVERTE.getStatut())
					.getResultList();
			// une seule journée ouverte
			if(listJourneeOuverte != null && listJourneeOuverte.size() > 0){
				return;
			}
			List<JourneePersistant> listJournee = parametrageDao.getQuery(em, "from JourneePersistant journee "
					+ "where order by date_journee desc")
					.getResultList();
			
			Date newDate = (listJournee.size()>0) ? DateUtil.addSubstractDate(listJournee.get(0).getDate_journee(), TIME_ENUM.DAY, 1) : new Date();
			
			em.getTransaction().begin();
			JourneePersistant e = new JourneePersistant();
			newDate = DateUtil.setDetailDate(newDate, TIME_ENUM.HOUR, 13);
			e.setDate_journee(newDate);
			e.setOpc_user(ContextAppli.getUserBean());
			e.setStatut_journee(ContextAppli.STATUT_JOURNEE.OUVERTE.getStatut());
			
			em.merge(e);
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
