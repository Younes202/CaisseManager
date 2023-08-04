package appli.model.domaine.personnel.dao.impl;

import javax.inject.Named;

import appli.model.domaine.personnel.dao.IFraisDao;
import appli.model.domaine.personnel.persistant.FraisPersistant;
import framework.model.util.GenericJpaDao;


@Named
public class FraisDao extends GenericJpaDao<FraisPersistant, Long> implements IFraisDao{

}
