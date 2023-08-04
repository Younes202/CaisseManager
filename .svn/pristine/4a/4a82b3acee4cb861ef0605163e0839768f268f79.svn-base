package appli.model.domaine.stock.service.impl;

import java.util.List;

import javax.inject.Named;

import appli.controller.domaine.stock.bean.TravauxBean;
import appli.model.domaine.stock.persistant.TravauxPersistant;
import appli.model.domaine.stock.service.ITravauxService;
import appli.model.domaine.stock.validator.TravauxValidator;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.service.GenericJpaService;

@WorkModelClassValidator(validator=TravauxValidator.class)
@Named
public class TravauxService extends GenericJpaService<TravauxBean, Long> implements ITravauxService{

	@Override
	public List<TravauxPersistant> getTravauxActifs() {
		return getQuery("from TravauxPersistant trv where opc_chantier.date_fin is not null "
				+ "order by opc_chantier.libelle, trv.libelle")
				.getResultList();
	}

}
