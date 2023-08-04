package appli.model.domaine.dashboard.service;

import java.util.List;

import appli.controller.domaine.habilitation.bean.ProfileBean;
import framework.model.service.IGenericJpaService;

public interface IDashBoardAutoService extends IGenericJpaService<ProfileBean, Long> {
	List<Object[]> getListIncidents();
	List<Object[]> getConsommation();
	List<Object[]> getVisiteTechniqueProche();
	List<Object[]> getVignetteProche();
	List<Object[]> getAssuranceProche();
	List<Object[]> getVidangeProche();
 }
