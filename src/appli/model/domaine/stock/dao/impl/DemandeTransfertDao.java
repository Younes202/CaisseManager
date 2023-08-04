package appli.model.domaine.stock.dao.impl;

import javax.inject.Named;

import appli.model.domaine.stock.dao.IDemandeTransfertDao;
import appli.model.domaine.stock.persistant.DemandeTransfertPersistant;
import framework.model.util.GenericJpaDao;

@Named
public class DemandeTransfertDao extends GenericJpaDao<DemandeTransfertPersistant, Long> implements IDemandeTransfertDao{

}
