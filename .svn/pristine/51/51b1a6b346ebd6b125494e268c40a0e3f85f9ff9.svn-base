package appli.model.domaine.administration.service.impl;

import java.util.List;

import javax.inject.Named;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.administration.bean.AgencementBean;
import appli.model.domaine.administration.persistant.AgencementPersistant;
import appli.model.domaine.administration.service.IAgencementService;
import framework.model.common.util.BooleanUtil;
import framework.model.service.GenericJpaService;

@Named
public class AgencementService extends GenericJpaService<AgencementBean, Long> implements IAgencementService{

	@Override
	@Transactional
	public void manageCalendrier(Long agencementId) {
		AgencementPersistant agencementB = findById(AgencementPersistant.class, agencementId);
		if(BooleanUtil.isTrue(agencementB.getIs_calendrier())) {
			agencementB.setIs_calendrier(false);
		} else {
			agencementB.setIs_calendrier(true);
		}
		getEntityManager().merge(agencementB);
	}

	@Override
	public List<AgencementPersistant> findAgencementByCalndrier() {
		return getQuery("from AgencementPersistant "
				+ "where is_calendrier is not null and is_calendrier=1 "
				+ "order by emplacement")
			.getResultList();
	}

}
