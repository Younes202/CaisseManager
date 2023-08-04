package appli.model.domaine.dashboard.dao.impl;

import javax.inject.Named;


import appli.model.domaine.dashboard.dao.IDashBoardDao;
import appli.model.domaine.habilitation.persistant.ProfilePersistant;
import framework.model.util.GenericJpaDao;

@Named
public class DashBoardDao extends GenericJpaDao<ProfilePersistant, Long> implements IDashBoardDao{

	
}
