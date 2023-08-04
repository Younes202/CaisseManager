package appli.model.domaine.personnel.service.impl;

import javax.inject.Named;

import appli.controller.domaine.personnel.bean.ComposantClientPrixBean;
import appli.model.domaine.personnel.service.IComposantClientPrixService;
import appli.model.domaine.personnel.validator.ComposantClientPrixValidator;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.service.GenericJpaService;

@WorkModelClassValidator(validator=ComposantClientPrixValidator.class)
@Named
public class ComposantClientPrixService extends GenericJpaService<ComposantClientPrixBean, Long> implements IComposantClientPrixService{

}
