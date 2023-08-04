package appli.model.domaine.administration.service.impl;

import javax.inject.Named;

import appli.controller.domaine.administration.bean.LieuxBean;
import appli.model.domaine.administration.service.ILieuxService;
import appli.model.domaine.administration.validator.LieuxValidator;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.service.GenericJpaService;

@WorkModelClassValidator(validator=LieuxValidator.class)
@Named
public class LieuxService extends GenericJpaService<LieuxBean, Long> implements ILieuxService{

}
