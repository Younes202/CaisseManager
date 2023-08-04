package appli.model.domaine.fidelite.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.fidelite.bean.CarteFideliteClientBean;
import appli.model.domaine.fidelite.dao.ICarteFideliteClientDao;
import appli.model.domaine.fidelite.persistant.CarteFideliteClientPersistant;
import appli.model.domaine.fidelite.persistant.CarteFideliteConsoPersistant;
import appli.model.domaine.fidelite.persistant.CarteFidelitePersistant;
import appli.model.domaine.fidelite.persistant.CarteFidelitePointsPersistant;
import appli.model.domaine.fidelite.service.ICarteFideliteClientService;
import appli.model.domaine.fidelite.validator.CarteFideliteClientValidator;
import appli.model.domaine.personnel.persistant.ClientPersistant;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.common.annotation.validator.WorkModelMethodValidator;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.DateUtil;
import framework.model.service.GenericJpaService;
@WorkModelClassValidator(validator=CarteFideliteClientValidator.class)
@Named
public class CarteFideliteClientService extends GenericJpaService<CarteFideliteClientBean, Long> implements ICarteFideliteClientService{

	@Inject
	private ICarteFideliteClientDao carteFideliteClientDao;
	
	@Override
	@Transactional
	public void activerDesactiverElement(Long carteclientId){
		CarteFideliteClientPersistant carteFideliteClientPersistant = carteFideliteClientDao.findById(carteclientId);
		carteFideliteClientPersistant.setIs_active(BooleanUtil.isTrue(carteFideliteClientPersistant.getIs_active()) ? false : true);
		//
		getEntityManager().merge(carteFideliteClientPersistant);
	}

	@Override
	public CarteFideliteClientPersistant getClientCarteByCodeBarre(String codeBarre) {
		List<CarteFideliteClientPersistant> listCarteActive = getQuery("from CarteFideliteClientPersistant WHERE code_barre=:codeBarre")
				.setParameter("codeBarre", codeBarre).getResultList();
		
		 return (listCarteActive.size() > 0 ? listCarteActive.get(0) : null); 
	}

	@Override
	public CarteFideliteClientPersistant getCarteClientActive(Long clientId){
		List<CarteFideliteClientPersistant> listCarteActive = getQuery("from CarteFideliteClientPersistant "
    	  		+ "WHERE opc_client.id =:clientId "
    	  		+ "AND (opc_carte_fidelite.is_active is null or opc_carte_fidelite.is_active=1) "
    	   		+ "and (date_debut is null or date_debut <=:dateJour) "
    	   		+ "AND (date_fin is null or date_fin >:dateJour) order by id desc")
    	   .setParameter("clientId", clientId)
    	   .setParameter("dateJour", DateUtil.getStartOfDay(new Date()))
    	   .getResultList();
		
    	   return (listCarteActive.size() > 0 ? listCarteActive.get(0) : null); 
	}
	
	@Override
	public List<ClientPersistant> getClientWithCarteActive(){
		List<CarteFideliteClientPersistant> listCarteActive = (List<CarteFideliteClientPersistant>) getQuery("from CarteFideliteClientPersistant "
    	  		+ "WHERE (opc_carte_fidelite.is_active is null or opc_carte_fidelite.is_active=true) "
    	   		+ "and (date_debut is null or date_debut <=:dateJour) "
    	   		+ "AND (date_fin is null or date_fin >:dateJour) order by id desc")
    	   .setParameter("dateJour", DateUtil.getStartOfDay(new Date()))
    	   .getResultList();
		
		List<ClientPersistant> listClient = new ArrayList<>();
		for (CarteFideliteClientPersistant carteFideliteCltP : listCarteActive) {
			listClient.add(carteFideliteCltP.getOpc_client());
		}
		
		return listClient;
	}

	@Override
	@Transactional
	public void offrirPoints(Long carteClientId, Integer nbrPoint) {
		 CarteFideliteClientPersistant carteFideliteCliP = carteFideliteClientDao.findById(carteClientId);
		 CarteFidelitePersistant opc_carte_fidelite = carteFideliteCliP.getOpc_carte_fidelite();
		 BigDecimal mttPointGagne = BigDecimalUtil.multiply(BigDecimalUtil.divide(opc_carte_fidelite.getMtt_pf_palier(), opc_carte_fidelite.getMtt_palier()),BigDecimalUtil.get(nbrPoint));
		 
		 // Ajout d'une trace
		 CarteFidelitePointsPersistant carteFidelitePointsPersistant = new CarteFidelitePointsPersistant();
		 carteFidelitePointsPersistant.setDate_gain(new Date());
		 carteFidelitePointsPersistant.setMtt_gain(mttPointGagne);
//		 carteFidelitePointsPersistant.setNbr_point_gain(nbrPoint);
		 carteFidelitePointsPersistant.setOpc_carte_client(carteFideliteCliP);
		 carteFidelitePointsPersistant.setSource("CDE");
		 // Sauvegarde
		 EntityManager entityManager = getEntityManager();
		 entityManager.merge(carteFidelitePointsPersistant);
		  
		 // Maj points et montants client carte
		 carteFideliteCliP.setMtt_total(BigDecimalUtil.add(carteFideliteCliP.getMtt_total(), mttPointGagne));
//		 carteFideliteCliP.setTotal_point(carteFideliteCliP.getTotal_point() + nbrPoint);
		 //
		 entityManager.merge(carteFideliteCliP);
	}
	
	@Override
	@Transactional
	@WorkModelMethodValidator
	public void deletePointsGagnes(Long carteClientPointId) {
		CarteFidelitePointsPersistant carteClientPointP = (CarteFidelitePointsPersistant)carteFideliteClientDao.findById(CarteFidelitePointsPersistant.class, carteClientPointId);
		CarteFideliteClientPersistant carteClient = carteClientPointP.getOpc_carte_client();
		//
		getQuery("delete from CarteFidelitePointsPersistant where id=:carteClientPointId")
			.setParameter("carteClientPointId", carteClientPointId)
			.executeUpdate();
		//
		carteClient.setMtt_total(BigDecimalUtil.substract(carteClient.getMtt_total(), carteClientPointP.getMtt_gain()));
//		carteClient.setTotal_point(carteClient.getTotal_point() - carteClientPointP.getNbr_point_gain());
		 //
		getEntityManager().merge(carteClient);
	}
	@Override
	@Transactional
	@WorkModelMethodValidator
	public void deletePointsConso(Long carteClientConsoId) {
		CarteFideliteConsoPersistant carteClientConsoP = (CarteFideliteConsoPersistant)carteFideliteClientDao.findById(CarteFideliteConsoPersistant.class, carteClientConsoId);
		CarteFideliteClientPersistant carteClient = carteClientConsoP.getOpc_carte_client();
		//
		getQuery("delete from CarteFideliteConsoPersistant where id=:carteClientConsoId")
		.setParameter("carteClientConsoId", carteClientConsoId)
		.executeUpdate();
		 // Maj points et montants client carte
		carteClient.setMtt_total(BigDecimalUtil.substract(carteClient.getMtt_total(), carteClientConsoP.getMtt_conso()));
//		carteClient.setTotal_point(carteClient.getTotal_point() - carteClientConsoP.getNbr_point_conso());
		 //
		 getEntityManager().merge(carteClient);
	}
}
