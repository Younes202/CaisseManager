package appli.model.domaine.util_srv.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import appli.model.domaine.administration.persistant.ParametragePersistant;
import appli.model.domaine.stock.service.IFamilleService;
import framework.model.beanContext.EtablissementOuverturePersistant;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.audit.ReplicationGenerationEventListener;
import framework.model.util.synchro.ISynchroniseService;

//@Named
public class JobJourneeCron {
	private final static Logger LOGGER = Logger.getLogger(JobJourneeCron.class);
	
	private Scheduler scheduler;
	private List<JobKey> lisJobKey = new ArrayList<>();
	
	/** Instance unique non préinitialisée */
	private static JobJourneeCron INSTANCE = null;
 
	/** Point d'accès pour l'instance unique du singleton */
	public static JobJourneeCron getInstance()
	{			
		if (INSTANCE == null)
		{ 	INSTANCE = new JobJourneeCron();	
		}
		return INSTANCE;
	}
	
	/**
	 * Poster vers les établissement ayant accés distant périodiquement
	 */
	public void init_job_postSyncToRemote(){
		ISynchroniseService synchroService = ServiceUtil.getBusinessBean(ISynchroniseService.class);
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
		    @Override
		    public void run() {
				try {
					if(!ReplicationGenerationEventListener._IS_POST_INPROGRESS) {
						
						ReplicationGenerationEventListener._IS_POST_INPROGRESS = true;
						synchroService.postDataToCloud();
						synchroService.getDataFromCloud();
						ReplicationGenerationEventListener._IS_POST_INPROGRESS = false;
					}
				} catch(Exception e) {
					System.out.println(e.getMessage());
				}
		    }
		}, 0, 1000*30);// 30secondes
			
	}

	public void init_job_journee() {
		IFamilleService famSrv = ServiceUtil.getBusinessBean(IFamilleService.class);
		
		EntityManager em = null;
		try {
			em = famSrv.getEntityManager().getEntityManagerFactory().createEntityManager();
			
			// Effacer l'ancienne config
			this.reset_jobs();
						
			List<ParametragePersistant> listParam = em.createQuery("from ParametragePersistant where groupe=:group and code=:code")
					.setParameter("group", "BACK_OFF")
					.setParameter("code", "JOURNEE_AUTO")
					.getResultList();
			
			List<String> days = new ArrayList<>();
			days.add("MON");
			days.add("TUE");
			days.add("WED");
			days.add("THU");
			days.add("FRI");
			days.add("SAT");
			days.add("SUN");
			
			if(this.scheduler == null) {
				this.scheduler = new StdSchedulerFactory().getScheduler();
			}
			
			for(ParametragePersistant paramP : listParam) {
				boolean isAuto = StringUtil.isTrue(paramP.getValeur());
				
				if(!isAuto) { 
					continue;
				}
				//
				EtablissementPersistant etsP = em.find(EtablissementPersistant.class, paramP.getOpc_etablissement().getId());
				List<EtablissementOuverturePersistant> listOuverture = etsP.getList_ouverture();
				
				int idx = 1;
				for (EtablissementOuverturePersistant jourOuvertureP : listOuverture) {
					String jour = days.get(Integer.valueOf(jourOuvertureP.getJour_ouverture())-1);
					String heureDebut = jourOuvertureP.getHeure_debut_matin();
					String heureFin = jourOuvertureP.getHeure_fin_midi();
	
					// Heure début
					if(heureDebut != null) {
						JobKey jobKeyDebut = new JobKey("journee_job_debut_"+idx+"_"+etsP.getId(), "group_1");
						JobDetail jobDebut = JobBuilder.newJob(JobOuvertureJournee.class).withIdentity(jobKeyDebut).build();
						lisJobKey.add(jobKeyDebut);
						
						String[] heureDebutArray = StringUtil.getArrayFromStringDelim(heureDebut, ":");
						String expressionDebut = "0 "+heureDebutArray[1]+" "+heureDebutArray[0]+" ? * "+jour;//Sec Mmin Hour ? * Day
						
						Trigger triggerDebut = TriggerBuilder
								.newTrigger()
								.withIdentity("triggerJourneeDebut_"+idx+"_"+etsP.getId(), "group_1")
								.withSchedule(CronScheduleBuilder.cronSchedule(expressionDebut))
								.build();
						this.scheduler.scheduleJob(jobDebut, triggerDebut);
					}
					
					// Heure fin
					if(heureFin != null) {
						JobKey jobKeyFin = new JobKey("journee_job_fin_"+idx+"_"+etsP.getId(), "group_1");
						JobDetail jobFin = JobBuilder.newJob(JobFermetureJournee.class).withIdentity(jobKeyFin).build();
						lisJobKey.add(jobKeyFin);
						
						String[] heureFinArray = StringUtil.getArrayFromStringDelim(heureFin, ":");
						String expressionFin = "0 "+heureFinArray[1]+" "+heureFinArray[0]+" ? * "+jour;//Sec Mmin Hour ? * Day
						
						Trigger triggerFin = TriggerBuilder
								.newTrigger()
								.withIdentity("triggerJourneeFin_"+idx+"_"+etsP.getId(), "group_1")
								.withSchedule(CronScheduleBuilder.cronSchedule(expressionFin))
								.build();
						
						this.scheduler.scheduleJob(jobFin, triggerFin);
					}
					//
					idx++;
				}
			}
			if(em != null && em.isOpen()) {
				em.close();
			}
			//
			if(!this.scheduler.isStarted()) {
				this.scheduler.start();
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
			LOGGER.error("Erreur : ", e);
		} finally {
			if(em != null && em.isOpen()) {
				em.close();
			}
		}
	}
	
	/**
	 * 
	 */
	private void reset_jobs() {
		if(this.scheduler == null) {
			return;
		}
		try {
			this.scheduler.deleteJobs(this.lisJobKey);
		} catch (SchedulerException e) {
			LOGGER.error("Erreur : ", e);
		}
	}
	
	public Scheduler getScheduler() {
		return this.scheduler;
	}
	
	public List<JobKey> getLisJobKey(){
		return this.lisJobKey;
	}
	
//	public static void main(String[] args) throws SchedulerException {
//		JobManualTask jb = JobManualTask.getInstance();
//		jb.init_jobs();
//		try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		
//		jb.getScheduler().deleteJobs(jb.getLisJobKey());
////		jb.getScheduler().shutdown();
//		
//		jb.init_jobs();
//	}

}