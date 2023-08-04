/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appli.model.domaine.fidelite.dao;

import java.math.BigDecimal;
import java.util.List;

import appli.controller.domaine.stock.bean.ArticleBean;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import framework.model.service.IGenericJpaService;
import framework.model.util.printGen.PrintPosBean;

/**
 *
 * @author hp
 */
public interface IPortefeuille2Service extends IGenericJpaService<ArticleBean, Long> {

	PrintPosBean ajouterRecharge(Long elementId, BigDecimal montant, String modePaiement, boolean isFromCaisse, String tp);//CLI, SOC

//	void majSoldePortefeuilleClient(CaisseMouvementPersistant currentCommande);

	BigDecimal getSoldePortefeuilleMvm(Long elementId, String tp);// CLI, SOC

	void deleteRecharge(Long porteFeuilleMvmId);

	List<CaisseMouvementPersistant> getListMouvementRechargePortefeuille(Long elementId, String tp);

	List<CaisseMouvementPersistant> getListMouvementPortefeuille(Long elementId, String tp);

	void majSoldePortefeuilleMvm(Long elementId, String tp);

	BigDecimal[] getSoldePortefeuilleDetail(Long elementId, String tp);
}
