package appli.model.domaine.administration.dao.impl;

import javax.inject.Named;

import appli.model.domaine.administration.dao.ISocieteDao;
import appli.model.domaine.administration.validator.SocieteValidator;
import framework.model.beanContext.SocietePersistant;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.util.GenericJpaDao;

@Named
@WorkModelClassValidator(validator= SocieteValidator.class)
public class SocieteDao  extends GenericJpaDao<SocietePersistant, Long> implements ISocieteDao{

}
