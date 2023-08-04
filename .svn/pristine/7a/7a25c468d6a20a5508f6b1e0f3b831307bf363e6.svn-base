package appli.model.domaine.stock.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.stock.bean.MouvementBean;
import appli.model.domaine.stock.service.IMouvementService;
import appli.model.domaine.stock.service.ITransformationService;
import framework.model.service.GenericJpaService;

@Named
public class TransformationService extends GenericJpaService<MouvementBean, Long> implements ITransformationService{

	@Inject
	private IMouvementService mouvementService;
	
	@Override
	public void delete(Long id) {
		mouvementService.delete(id);
	}
}