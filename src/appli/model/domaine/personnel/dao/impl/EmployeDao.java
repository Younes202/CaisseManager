package appli.model.domaine.personnel.dao.impl;

import javax.inject.Named;

import appli.model.domaine.personnel.dao.IEmployeDao;
import appli.model.domaine.personnel.persistant.EmployePersistant;
import framework.model.util.GenericJpaDao;


@Named
public class EmployeDao extends GenericJpaDao<EmployePersistant, Long> implements IEmployeDao{

}
