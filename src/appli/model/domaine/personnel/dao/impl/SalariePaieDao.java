package appli.model.domaine.personnel.dao.impl;

import javax.inject.Named;

import appli.model.domaine.personnel.dao.ISalariePaieDao;
import appli.model.domaine.personnel.persistant.paie.SalairePersistant;
import framework.model.util.GenericJpaDao;


@Named
public class SalariePaieDao extends GenericJpaDao<SalairePersistant, Long> implements ISalariePaieDao{

}
