package appli.model.domaine.stock.dao.impl;

import javax.inject.Named;

import appli.model.domaine.stock.dao.ITransformationDao;
import appli.model.domaine.stock.persistant.MouvementPersistant;
import framework.model.util.GenericJpaDao;

@Named
public class TransformationDao extends GenericJpaDao<MouvementPersistant, Long> implements ITransformationDao{

}
