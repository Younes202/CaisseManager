package appli.model.domaine.caisse.dao.impl;

import javax.inject.Named;

import appli.model.domaine.caisse.dao.ICaisseDao;
import appli.model.domaine.vente.persistant.CaissePersistant;
import framework.model.util.GenericJpaDao;

@Named
public class CaisseDao extends GenericJpaDao<CaissePersistant, Long> implements ICaisseDao{

}
