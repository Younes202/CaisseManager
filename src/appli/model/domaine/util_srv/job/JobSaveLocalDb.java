package appli.model.domaine.util_srv.job;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import appli.model.domaine.administration.service.IParametrageService;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StringUtil;

public class JobSaveLocalDb implements Job {
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		IParametrageService paramService = ServiceUtil.getBusinessBean(IParametrageService.class);
		List<EtablissementPersistant> listEts = paramService.findAll(EtablissementPersistant.class);
		//
		for (EtablissementPersistant etsP : listEts) {
			if(StringUtil.isNotEmpty(etsP.getSave_db_path())){
				paramService.dumpBase(etsP.getSave_db_path());	
			}
		}
					
	}
}
