package appli.model.domaine.caisse.dao.impl;

import javax.inject.Named;

import appli.model.domaine.caisse.dao.ICaisseMouvementDao;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import framework.model.util.GenericJpaDao;

@Named
public class CaisseMouvementDao extends GenericJpaDao<CaisseMouvementPersistant, Long> implements ICaisseMouvementDao{


	
}
