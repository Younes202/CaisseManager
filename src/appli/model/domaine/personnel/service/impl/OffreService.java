package appli.model.domaine.personnel.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.personnel.bean.OffreBean;
import appli.model.domaine.personnel.dao.IOffreDao;
import appli.model.domaine.personnel.persistant.OffrePersistant;
import appli.model.domaine.personnel.service.IOffreService;
import appli.model.domaine.personnel.validator.OffreValidator;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.common.annotation.validator.WorkModelMethodValidator;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.DateUtil.TIME_ENUM;
import framework.model.service.GenericJpaService;

@WorkModelClassValidator(validator=OffreValidator.class)
@Named
public class OffreService extends GenericJpaService<OffreBean, Long> implements IOffreService{
	@Inject
	IOffreDao offreDao;
	
	@Override
	@Transactional
	@WorkModelMethodValidator
	public void create(OffreBean offreBean) {
		if(!offreBean.getDestination().equals("A")){
			OffrePersistant offreP = (OffrePersistant) offreDao.getSingleResult(offreDao.getQuery("from OffrePersistant where date_fin is null and destination=:dest")
					.setParameter("dest", offreBean.getDestination()));
			// L'offre sans date de fin prend la date de début -1 de la nouvelle offre
			if(offreP != null){
				offreP.setDate_fin(DateUtil.addSubstractDate(offreBean.getDate_debut(), TIME_ENUM.DAY, -1));
				offreDao.update(offreP);
			}
		} else{
			if(offreBean.getType_offre().equals("P")){// Prix d'achat
				offreBean.setTaux_reduction(null);
				offreBean.setMtt_seuil(null);
				offreBean.setMtt_plafond(null);
			}
		}
		super.create(offreBean);
	}
	
	@Override
	@Transactional
	public void activerDesactiverElement(Long offreId) {
		OffrePersistant offrePersistant = findById(OffrePersistant.class, offreId);
		offrePersistant.setIs_disable(BooleanUtil.isTrue(offrePersistant.getIs_disable()) ? false : true);
		//
		getEntityManager().merge(offrePersistant); 
	}
}
