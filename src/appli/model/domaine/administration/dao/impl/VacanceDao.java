package appli.model.domaine.administration.dao.impl;

import javax.inject.Named;

import appli.model.domaine.administration.dao.IVacanceDao;
import appli.model.domaine.administration.persistant.VacancePersistant;
import framework.model.util.GenericJpaDao;

@Named
public class VacanceDao extends GenericJpaDao<VacancePersistant, Long> implements IVacanceDao{

}
