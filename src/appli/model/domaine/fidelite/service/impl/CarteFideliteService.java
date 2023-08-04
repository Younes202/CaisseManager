package appli.model.domaine.fidelite.service.impl;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.fidelite.bean.CarteFideliteBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.fidelite.dao.ICarteFideliteDao;
import appli.model.domaine.fidelite.persistant.CarteFidelitePersistant;
import appli.model.domaine.fidelite.service.ICarteFideliteService;
import appli.model.domaine.fidelite.validator.CarteFideliteValidator;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.common.util.BooleanUtil;
import framework.model.service.GenericJpaService;

@WorkModelClassValidator(validator=CarteFideliteValidator.class) 
@Named
public class CarteFideliteService extends GenericJpaService<CarteFideliteBean, Long> implements ICarteFideliteService{
	@Inject
	ICarteFideliteDao carteFideliteDao;
	
	
	@Override
	@Transactional
	public void activerDesactiverElement(Long carteId){
		CarteFidelitePersistant carteFidelitePersistant = carteFideliteDao.findById(carteId);
		carteFidelitePersistant.setIs_active(BooleanUtil.isTrue(carteFidelitePersistant.getIs_active()) ? false : true);
		
		//
		getEntityManager().merge(carteFidelitePersistant);
	}
	
	@Override
	@Transactional
	public CarteFidelitePersistant getCarteOrCarteParDefaut(Long carteId){
		if(carteId != null){
			CarteFidelitePersistant carteFidelitePersistant = carteFideliteDao.findById(carteId);
			if(carteFidelitePersistant != null){
				return carteFidelitePersistant;
			}
		}
		//
		CarteFidelitePersistant carteFideliteP = (CarteFidelitePersistant) getSingleResult(getQuery("from CarteFidelitePersistant where is_default is not null"));
		if(carteFideliteP == null){
			carteFideliteP = new CarteFidelitePersistant();
			carteFideliteP.setIs_active(true);
			carteFideliteP.setIs_default(true);
			carteFideliteP.setLibelle("CARTE PAR DEFAUT");
			carteFideliteP.setDate_creation(new Date());
			carteFideliteP.setSignature(ContextAppli.getUserBean().getLogin());
			carteFideliteP = getEntityManager().merge(carteFideliteP);
		}
		
		return carteFideliteP;
	}
	
	@Override
	@Transactional
	public void autoPurgeElement(Long carteId){
		
	}
}
