/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appli.model.domaine.caisse.service;

import java.util.Date;
import java.util.List;

import appli.controller.domaine.administration.bean.EtatFinanceBean;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import framework.model.service.IGenericJpaService;

/**
 *
 * @author hp
 */
public interface IEtatFinancierCaisseService extends IGenericJpaService<EtatFinanceBean, Long> {

	List<CaisseMouvementPersistant> getListMouvementMoisCaissePointe(Date dateDebut, Date dateFin);

	List<CaisseMouvementPersistant> getListMouvementJourneeCaissePointe(Long joureeId);
}
