package appli.model.domaine.stock.service;

import java.util.List;

import appli.controller.domaine.stock.bean.TravauxBean;
import appli.model.domaine.stock.persistant.TravauxPersistant;
import framework.model.service.IGenericJpaService;

public interface ITravauxService extends IGenericJpaService<TravauxBean, Long> {

	List<TravauxPersistant> getTravauxActifs();

}
