package appli.model.domaine.personnel.service.impl;

import javax.inject.Named;

import appli.controller.domaine.personnel.bean.TypeFraisBean;
import appli.model.domaine.personnel.service.ITypeFraisService;
import framework.model.service.GenericJpaService;

@Named
public class TypeFraisService extends GenericJpaService<TypeFraisBean, Long> implements ITypeFraisService{	
	
}
