/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appli.model.domaine.caisse.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import appli.controller.domaine.administration.bean.UserBean;
import appli.controller.domaine.caisse.bean.CaisseMouvementBean;
import appli.controller.domaine.stock.bean.MouvementBean;
import appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.service.IGenericJpaService;

/**
 *
 * @author 
 */
public interface ICaisseMouvementService extends IGenericJpaService<CaisseMouvementBean, Long> {

	String generateCodeBarre();

	void deleteImageCodeBarre(CaisseMouvementPersistant mouvement);

	CaisseMouvementPersistant getCommandeByCodeBarre(String codeBarre);

	CaisseMouvementPersistant getCommandeByReference(String ref_commande);

	List<CaisseMouvementPersistant> getListMouvementCaisse(Date dateDebut, Date dateFin);

	void mergeRetourCaisse(MouvementBean mouvementBean, List<CaisseMouvementArticlePersistant> listCaisseMvmArt);
	
	void caisseMvmTraceur(CaisseMouvementPersistant caisseMvmPOrigine, EtablissementPersistant etsB, UserBean userBean);

	Map<String, Object> getDataSynthese(Long livreurId, Date dateDebut, Date dateFin);

	List<CaisseMouvementArticlePersistant> getMvmMargeEmploye(Long journeeId, Date dateDebut, Date dateFin,
			long userId);

	List<CaisseMouvementPersistant> getListCommandes(Long userId, String type, Integer page);
}
