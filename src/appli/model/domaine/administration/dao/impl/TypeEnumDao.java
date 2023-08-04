package appli.model.domaine.administration.dao.impl;

import javax.inject.Named;

import appli.model.domaine.administration.dao.ITypeEnumDao;
import appli.model.domaine.administration.persistant.TypeEnumPersistant;
import framework.model.util.GenericJpaDao;


@Named
public class TypeEnumDao extends GenericJpaDao<TypeEnumPersistant, Long> implements ITypeEnumDao{

}
