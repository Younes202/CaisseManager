package appli.model.domaine.caisse.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.fidelite.bean.CarteFideliteBean;
import appli.model.domaine.caisse.service.ICarteFidelite2Service;
import appli.model.domaine.fidelite.persistant.CarteFideliteClientPersistant;
import appli.model.domaine.fidelite.persistant.CarteFideliteConsoPersistant;
import appli.model.domaine.fidelite.persistant.CarteFidelitePersistant;
import appli.model.domaine.fidelite.persistant.CarteFidelitePointsPersistant;
import appli.model.domaine.fidelite.service.ICarteFideliteClientService;
import appli.model.domaine.stock.validator.ArticleValidator;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.common.util.BigDecimalUtil;
import framework.model.service.GenericJpaService;

@WorkModelClassValidator(validator=ArticleValidator.class)
@Named
public class CarteFidelite2Service extends GenericJpaService<CarteFideliteBean, Long> implements ICarteFidelite2Service{
	@Inject
	private ICarteFideliteClientService carteFideliteClientService;
	
	@Override
	@Transactional
	public void majPointsCarteFideliteClient(CaisseMouvementPersistant currentCommande) {
		if(currentCommande.getOpc_client() == null){
			return;
		}
		CarteFideliteClientPersistant carteFideliteCliP = carteFideliteClientService.getCarteClientActive(currentCommande.getOpc_client().getId());
		if(carteFideliteCliP == null){
			return;
		}
		
		CarteFidelitePersistant opc_carte_fidelite = carteFideliteCliP.getOpc_carte_fidelite();
	
	 	// Consommation des points ---------Donc cette commande ne donne pas de points-------------------------- 
		if(!BigDecimalUtil.isZero(currentCommande.getMtt_donne_point())){
			 // Ajout d'une trace
	   		 CarteFideliteConsoPersistant carteFideliteConsoPersistant = new CarteFideliteConsoPersistant();
	   		 carteFideliteConsoPersistant.setDate_conso(new Date());
	   		 carteFideliteConsoPersistant.setMtt_conso(currentCommande.getMtt_donne_point());
	   		 carteFideliteConsoPersistant.setOpc_carte_client(carteFideliteCliP);
	   		 carteFideliteConsoPersistant.setOpc_mvn_caisse(currentCommande);
	   		 // Sauvegarde
	   		 EntityManager entityManager = carteFideliteClientService.getEntityManager();
	   		 entityManager.merge(carteFideliteConsoPersistant);
	   		  
	   		 // Maj points et montants client carte
	   		 carteFideliteCliP.setMtt_total(BigDecimalUtil.substract(carteFideliteCliP.getMtt_total(), currentCommande.getMtt_donne_point()));
	   		 //
	   		 entityManager.merge(carteFideliteCliP);
		}
   		 
		// Si utilisation de points alors on retire le montant des points du montant global ------------------------
		BigDecimal mttCommande = currentCommande.getMtt_commande_net();
		BigDecimal mttBaseCalculPoint = mttCommande;
		//
		if(!BigDecimalUtil.isZero(currentCommande.getMtt_donne_point())){
			mttBaseCalculPoint = BigDecimalUtil.substract(mttCommande, currentCommande.getMtt_donne_point());
		}
    		 
		 BigDecimal mttSeuil = opc_carte_fidelite.getMtt_seuil();
		 //
		 if(mttSeuil == null || mttBaseCalculPoint.compareTo(mttSeuil) > 0){
			 BigDecimal mttPointGagne = BigDecimalUtil.ZERO;
			 if(!BigDecimalUtil.isZero(opc_carte_fidelite.getMtt_palier())){
				 mttPointGagne = BigDecimalUtil.divide(BigDecimalUtil.multiply(mttBaseCalculPoint, opc_carte_fidelite.getMtt_pf_palier()), opc_carte_fidelite.getMtt_palier());
			 }
			 // Ajout d'une trace
    		 CarteFidelitePointsPersistant carteFidelitePointsPersistant = new CarteFidelitePointsPersistant();
    		 carteFidelitePointsPersistant.setDate_gain(new Date());
    		 carteFidelitePointsPersistant.setMtt_gain(mttPointGagne);
    		 carteFidelitePointsPersistant.setOpc_carte_client(carteFideliteCliP);
    		 carteFidelitePointsPersistant.setSource("CMD");
    		 carteFidelitePointsPersistant.setOpc_mvn_caisse(currentCommande);
    		 // Sauvegarde
    		 EntityManager entityManager = carteFideliteClientService.getEntityManager();
    		 entityManager.merge(carteFidelitePointsPersistant);
    		  
    		 // Maj points et montants client carte
    		 carteFideliteCliP.setMtt_total(BigDecimalUtil.add(carteFideliteCliP.getMtt_total(), mttPointGagne));
    		 //
    		 entityManager.merge(carteFideliteCliP);
		 }  
	}

}
