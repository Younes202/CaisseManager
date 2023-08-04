package appli.model.domaine.stock.service;

import appli.controller.domaine.stock.bean.PreparationTransfoBean;
import framework.model.service.IGenericJpaService;

public interface IPreparationTransfoService extends IGenericJpaService<PreparationTransfoBean, Long> {
	
	String genererCode();

	void merge(PreparationTransfoBean prepTrB, boolean isNewComposant);
}
