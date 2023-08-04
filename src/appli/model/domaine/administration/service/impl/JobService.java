package appli.model.domaine.administration.service.impl;

import javax.inject.Named;

import appli.controller.domaine.administration.bean.JobBean;
import appli.model.domaine.administration.service.IJobService;
import framework.model.service.GenericJpaService;

@Named
public class JobService extends GenericJpaService<JobBean, Long> implements IJobService{
	
}
