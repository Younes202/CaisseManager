package appli.model.domaine.stock.service;

import appli.controller.domaine.stock.bean.MarqueBean;
import framework.model.service.IGenericJpaService;

public interface IMarqueService extends IGenericJpaService<MarqueBean, Long> {

	String genererCode();

	void activerDesactiverElement(Long marqueId);

}
