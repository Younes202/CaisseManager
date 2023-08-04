package appli.model.domaine.personnel.dao.impl;

import javax.inject.Named;

import appli.model.domaine.personnel.dao.ITypeFraisDao;
import appli.model.domaine.personnel.persistant.TypeFraisPersistant;
import framework.model.util.GenericJpaDao;


@Named
public class TypeFraisDao extends GenericJpaDao<TypeFraisPersistant, Long> implements ITypeFraisDao{

}
