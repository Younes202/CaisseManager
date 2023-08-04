package appli.model.domaine.personnel.dao.impl;

import javax.inject.Named;

import appli.model.domaine.personnel.dao.IOffreDao;
import appli.model.domaine.personnel.persistant.OffrePersistant;
import framework.model.util.GenericJpaDao;


@Named
public class OffreDao extends GenericJpaDao<OffrePersistant, Long> implements IOffreDao{

}
