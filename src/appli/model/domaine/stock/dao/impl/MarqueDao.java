package appli.model.domaine.stock.dao.impl;

import javax.inject.Named;

import appli.model.domaine.stock.dao.IMarqueDao;
import appli.model.domaine.stock.persistant.MarquePersistant;
import framework.model.util.GenericJpaDao;

@Named
public class MarqueDao extends GenericJpaDao<MarquePersistant, Long> implements IMarqueDao{

}
