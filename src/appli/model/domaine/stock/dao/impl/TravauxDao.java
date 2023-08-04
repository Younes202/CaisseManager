package appli.model.domaine.stock.dao.impl;

import javax.inject.Named;

import appli.model.domaine.stock.dao.ITravauxDao;
import appli.model.domaine.stock.persistant.TravauxPersistant;
import framework.model.util.GenericJpaDao;

@Named
public class TravauxDao extends GenericJpaDao<TravauxPersistant, Long> implements ITravauxDao{

}
