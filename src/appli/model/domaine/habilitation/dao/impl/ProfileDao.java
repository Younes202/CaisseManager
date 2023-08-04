package appli.model.domaine.habilitation.dao.impl;

import javax.inject.Named;

import appli.model.domaine.habilitation.dao.IProfileDao;
import appli.model.domaine.habilitation.persistant.ProfilePersistant;
import framework.model.util.GenericJpaDao;

@Named
public class ProfileDao extends GenericJpaDao<ProfilePersistant, Long> implements IProfileDao{

}
