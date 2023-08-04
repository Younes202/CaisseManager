package appli.model.domaine.personnel.validator;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

import appli.controller.domaine.personnel.bean.OffreBean;
import appli.model.domaine.personnel.dao.IOffreDao;
import appli.model.domaine.personnel.persistant.OffrePersistant;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;

@Named
public class OffreValidator {
	@Inject
	private IOffreDao offreDao;
	
	/* (non-Javadoc)
	 * @see model.service.commun.validator.ActionValidator#validate(java.lang.Object, java.lang.String)
	 */
	public void updateCreateValidator(OffreBean offreBean) {
		if(offreBean.getType_offre().equals("R") && (offreBean.getTaux_reduction() == null || BigDecimalUtil.isZero(offreBean.getTaux_reduction()))){
			MessageService.addFieldMessage("offre.taux_reduction", "Le taux est obligatoire");
		}
		
		
		// Vérifier la cohérence dates
		if(offreBean.getDate_fin() != null && offreBean.getDate_debut().after(offreBean.getDate_fin())){
			MessageService.addBannerMessage("La date de début doit être postérieure à la date fin");
		}

		if(!offreBean.getDestination().equals("A")){
			String request = "from OffrePersistant where (date_debut>=:dtDebut or date_fin >=:dtDebut) and destination=:dest ";
			
			if(offreBean.getId() != null){
				request = request + "and id!=:currId";
			}
			
			Query query = offreDao.getQuery(request) 
				.setParameter("dest", offreBean.getDestination())
				.setParameter("dtDebut", offreBean.getDate_debut());
			
			if(offreBean.getId() != null){
				query.setParameter("currId", offreBean.getId());
			}
			
			List<OffrePersistant> listOffres = query.getResultList();
			if(listOffres.size() > 0){
				MessageService.addBannerMessage("Des offres ont été trouvées dans cet interval de dates. Veuillez leur attribuer une date de fin ou changer de dates.");
			}
		}
	}
	
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void update(OffreBean offreBean) {
		updateCreateValidator(offreBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void create(OffreBean offreBean){
		updateCreateValidator(offreBean);
	}
	
	public void delete(Long offreId){
		if(offreDao.getQuery("from CaisseMouvementOffrePersistant where opc_offre.id=:offreId")
			.setParameter("offreId", offreId)
			.getResultList().size() > 0){
			MessageService.addBannerMessage("Cette offre est liée à des ventes");
		}
	}
}
