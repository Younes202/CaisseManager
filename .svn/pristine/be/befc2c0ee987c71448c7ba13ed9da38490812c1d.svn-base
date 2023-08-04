package appli.model.domaine.util_srv.job;

import java.util.ArrayList;
import java.util.List;

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

import appli.model.domaine.stock.service.IFamilleService;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StringUtil;

public class JobSaveLocalDbCron {
	private final static Logger LOGGER = Logger.getLogger(JobSaveLocalDbCron.class);
	
	private Scheduler scheduler;
	private List<JobKey> lisJobKey = new ArrayList<>();
	
	/** Instance unique non préinitialisée */
	private static JobSaveLocalDbCron INSTANCE = null;
 
	/** Point d'accès pour l'instance unique du singleton */
	public static JobSaveLocalDbCron getInstance()
	{			
		if (INSTANCE == null)
		{ 	INSTANCE = new JobSaveLocalDbCron();	
		}
		return INSTANCE;
	}
	
	public void init_job_save_db() {
		IFamilleService famSrv = ServiceUtil.getBusinessBean(IFamilleService.class);
		
		try {
			// Effacer l'ancienne config
			this.reset_jobs();
						
			List<EtablissementPersistant> listEts = famSrv.findAll(EtablissementPersistant.class);
			
			if(this.scheduler == null) {
				this.scheduler = new StdSchedulerFactory().getScheduler();
			}
			
			for(EtablissementPersistant etsP : listEts) {
				if(StringUtil.isEmpty(etsP.getHeure_save_db1())) { 
					continue;
				}
				//
				String[] dtArray = StringUtil.getArrayFromStringDelim(etsP.getHeure_save_db1(), ":");
				String heureDebut1 = dtArray[0]; 
				String minuteDebut1 = dtArray[1];
				
				// Heure 1
				JobKey jobKeyDebut = new JobKey("save_db_job_debut1_"+etsP.getId(), "group_1");
				JobDetail jobSave1 = JobBuilder.newJob(JobSaveLocalDb.class).withIdentity(jobKeyDebut).build();
				lisJobKey.add(jobKeyDebut);
				
				String expressionDebut = "0 "+minuteDebut1+" "+heureDebut1+" ? * *";//Sec Min Hour ? * Day
				
				Trigger triggerSave1 = TriggerBuilder
						.newTrigger()
						.withIdentity("triggerSaveDb1_"+etsP.getId(), "group_1")
						.withSchedule(CronScheduleBuilder.cronSchedule(expressionDebut))
						.build();
				this.scheduler.scheduleJob(jobSave1, triggerSave1);
				
				// Heure 2
				if(StringUtil.isNotEmpty(etsP.getHeure_save_db2())) {
					dtArray = StringUtil.getArrayFromStringDelim(etsP.getHeure_save_db2(), ":");
					String heureDebut2 = dtArray[0]; 
					String minuteDebut2 = dtArray[1];
					
					JobKey jobKeyFin = new JobKey("save_db_job_debut2_"+etsP.getId(), "group_1");
					JobDetail jobSave2 = JobBuilder.newJob(JobFermetureJournee.class).withIdentity(jobKeyFin).build();
					lisJobKey.add(jobKeyFin);
					
					String expressionFin = "0 "+minuteDebut2+" "+heureDebut2+" ? * *";//Sec Mmin Hour ? * Day
					
					Trigger triggerSave2 = TriggerBuilder
							.newTrigger()
							.withIdentity("triggerSaveDb2_"+etsP.getId(), "group_1")
							.withSchedule(CronScheduleBuilder.cronSchedule(expressionFin))
							.build();
					
					this.scheduler.scheduleJob(jobSave2, triggerSave2);
				}
			}
			//
			if(!this.scheduler.isStarted()) {
				this.scheduler.start();
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
			LOGGER.error("Erreur : ", e);
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
}
