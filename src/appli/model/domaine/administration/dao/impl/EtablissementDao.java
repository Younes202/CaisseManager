package appli.model.domaine.administration.dao.impl;

import javax.inject.Named;

import appli.model.domaine.administration.dao.IEtablissementDao;
import appli.model.domaine.administration.validator.EtablissementValidator;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.util.GenericJpaDao;


@Named
@WorkModelClassValidator(validator= EtablissementValidator.class)
public class EtablissementDao extends GenericJpaDao<EtablissementPersistant, Long> implements IEtablissementDao{

}
