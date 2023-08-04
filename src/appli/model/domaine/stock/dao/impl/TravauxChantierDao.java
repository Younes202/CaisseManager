package appli.model.domaine.stock.dao.impl;

import javax.inject.Named;

import appli.model.domaine.stock.dao.ITravauxChantierDao;
import appli.model.domaine.stock.persistant.TravauxChantierPersistant;
import framework.model.util.GenericJpaDao;

@Named
public class TravauxChantierDao extends GenericJpaDao<TravauxChantierPersistant, Long> implements ITravauxChantierDao{

}
