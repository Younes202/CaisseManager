package appli.model.domaine.fidelite.dao.impl;

import javax.inject.Named;

import appli.model.domaine.fidelite.dao.ICarteFideliteDao;
import appli.model.domaine.fidelite.persistant.CarteFidelitePersistant;
import framework.model.util.GenericJpaDao;

@Named
public class CarteFideliteDao extends GenericJpaDao<CarteFidelitePersistant, Long> implements ICarteFideliteDao{

}
