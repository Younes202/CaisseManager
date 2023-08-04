package appli.model.domaine.caisse.dao.impl;

import javax.inject.Named;

import appli.model.domaine.caisse.dao.ITicketCaisseDao;
import appli.model.domaine.caisse.persistant.TicketCaissePersistant;
import framework.model.util.GenericJpaDao;

@Named
public class TicketCaisseDao extends GenericJpaDao<TicketCaissePersistant, Long> implements ITicketCaisseDao{

}
