package appli.model.domaine.personnel.service.impl;

import javax.inject.Named;

import appli.controller.domaine.personnel.bean.PosteBean;
import appli.model.domaine.personnel.service.IPosteService;
import appli.model.domaine.personnel.validator.PosteValidator;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.service.GenericJpaService;

@WorkModelClassValidator(validator=PosteValidator.class)
@Named
public class PosteService extends GenericJpaService<PosteBean, Long> implements IPosteService{

}
