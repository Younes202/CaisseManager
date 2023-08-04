package appli.model.domaine.stock.service.impl;

import java.util.List;

import javax.inject.Named;

import appli.controller.domaine.stock.bean.DemandeTransfertBean;
import appli.model.domaine.stock.persistant.DemandeTransfertPersistant;
import appli.model.domaine.stock.service.IDemandeTransfertService;
import framework.model.service.GenericJpaService;

@Named
public class DemandeTransfertService extends GenericJpaService<DemandeTransfertBean, Long> implements IDemandeTransfertService{
	@Override
	public List<DemandeTransfertPersistant> getListDemandeNonTransfere(){
		return getQuery("from DemandeTransfertPersistant demandeTransfert "
				+ "where demandeTransfert.statut = 'ENREGISTREE' "
				+ "order by demandeTransfert.date_souhaitee desc")
				.getResultList();
	}
}