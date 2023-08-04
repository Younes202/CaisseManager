package appli.model.domaine.caisse.service;

import java.util.List;

import appli.controller.domaine.stock.bean.FamilleBean;
import appli.model.domaine.stock.persistant.FamillePersistant;
import framework.controller.bean.PagerBean;
import framework.model.service.IGenericJpaService;

public interface IFamille2Service extends IGenericJpaService<FamilleBean, Long> {

	List<FamillePersistant> getFamilleEnfants(Long famId, Long caisseId);
	List<FamillePersistant> getFamilleEnfants(Long famId, Long caisseId, PagerBean pagerBean);
}
