package appli.model.domaine.caisse.dao.impl;

import javax.inject.Named;

import appli.model.domaine.caisse.dao.ICaisseJourneeDao;
import appli.model.domaine.vente.persistant.CaisseJourneePersistant;
import framework.model.util.GenericJpaDao;

@Named
public class CaisseJourneeDao extends GenericJpaDao<CaisseJourneePersistant, Long> implements ICaisseJourneeDao{

}
