package appli.model.domaine.stock.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.stock.bean.EmplacementBean;
import appli.model.domaine.stock.dao.IEmplacementDao;
import appli.model.domaine.stock.persistant.EmplacementPersistant;
import appli.model.domaine.stock.service.IEmplacementService;
import appli.model.domaine.stock.validator.EmplacementValidator;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.common.util.BooleanUtil;
import framework.model.service.GenericJpaService;

@WorkModelClassValidator(validator=EmplacementValidator.class)
@Named
public class EmplacementService extends GenericJpaService<EmplacementBean, Long> implements IEmplacementService{
	@Inject
	private IEmplacementDao emplacementDao;
	
	@Override
	@Transactional
	public void activerDesactiverElement(Long emplacementId) {
		EmplacementPersistant emplacementPersistant = emplacementDao.findById(emplacementId);
		emplacementPersistant.setIs_desactive(BooleanUtil.isTrue(emplacementPersistant.getIs_desactive()) ? false : true);
		//
		getEntityManager().merge(emplacementPersistant);
	}
	
	@Override
	public List<EmplacementPersistant> getEmplacementsInternes() {
		 return getQuery("from EmplacementPersistant "
		 		+ "where origine_auth is null "
		 		+ "order by titre")
				 .getResultList();
	}
	
	@Override
	public List<EmplacementPersistant> getEmplacementsExternes() {
		 return getQuery("from EmplacementPersistant "
		 		+ "where origine_auth is not null "
		 		+ "order by titre")
				 .getResultList();
	}
	
	@Override
	public List<EmplacementPersistant> getEmplacementsExternes(String auth) {
		 return getQuery("from EmplacementPersistant "
		 		+ "where origine_auth is not null "
		 		+ "and origine_auth=:oriAuth "
		 		+ "order by titre")
				 .setParameter("oriAuth", auth)
				 .getResultList();
	}
	
	@Override
	public List<EmplacementPersistant> getListEmplacementActifs() {
		return getQuery("from EmplacementPersistant e "
				+ "where (e.is_desactive is null or e.is_desactive is 0) "
				+ "and origine_auth is null")
				.getResultList();
	}
	
}
