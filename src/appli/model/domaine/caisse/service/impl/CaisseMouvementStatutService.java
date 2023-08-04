/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appli.model.domaine.caisse.service.impl;

import java.util.List;

import javax.inject.Named;

import appli.controller.domaine.caisse.bean.CaisseMouvementBean;
import appli.model.domaine.caisse.service.ICaisseMouvementStatutService;
import appli.model.domaine.personnel.persistant.EmployePersistant;
import framework.model.service.GenericJpaService;

@Named
public class CaisseMouvementStatutService extends GenericJpaService<CaisseMouvementBean, Long> implements ICaisseMouvementStatutService{
	
//	@Override
//	public CaisseMouvementStatutPersistant getCommandeStatutByCmdAndStatut(Long commande_id, String statut) {
//		List<CaisseMouvementStatutPersistant> listCmd = getQuery("from CaisseMouvementStatutPersistant "
//				+ "where opc_caisse_mouvement.id=:cmd_id and statut_cmd=:statut "
//				+ "order by date_statut asc")
//				.setParameter("cmd_id", commande_id)
//				.setParameter("statut", statut)
//				.getResultList();
//		return (listCmd.size()>0 ? listCmd.get(0) : null); 
//	}
	
	@Override
	public List<EmployePersistant> getCuisiniers() {
		return getQuery("select distinct(opc_employe) from CaisseMouvementStatutPersistant cs "
				+ "order by cs.opc_employe.numero")
				.getResultList(); 
	}
}

