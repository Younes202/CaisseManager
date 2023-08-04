package appli.model.domaine.administration.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.administration.bean.EtablissementBean;
import appli.model.domaine.administration.persistant.UserPersistant;
import appli.model.domaine.administration.service.IEtablissementService;
import appli.model.domaine.administration.service.IUserService;
import appli.model.domaine.administration.validator.EtablissementValidator;
import appli.model.domaine.caisse.persistant.LivreurPositionPersistant;
import appli.model.domaine.personnel.persistant.EmployePersistant;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.StringUtil;
import framework.model.service.GenericJpaService;


@WorkModelClassValidator(validator=EtablissementValidator.class)
@Named
public class EtablissementService extends GenericJpaService<EtablissementBean, Long> implements IEtablissementService{
	
	@Inject
	IUserService userService;
	@Override
	@Transactional
	public void majPositionLivreur(EmployePersistant livreur, String str_position) {
		// ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor)ServiceUtil.getBusinessBean("taskExecutor");
		final String str_positionF = (str_position == null ? "" : str_position.replaceAll("\\|,", "|"));
		EntityManager em = getEntityManager();
		//
		if(StringUtil.isNotEmpty(str_position)) {
//	 	 	taskExecutor.submit(new Callable<Object>() {
	      //      public Object call() throws Exception {
					List<LivreurPositionPersistant> listCoords = new ArrayList<>();
					String[] positionsArray = StringUtil.getArrayFromStringDelim(str_positionF, "|");
					UserPersistant liv = (UserPersistant) userService.findById(UserPersistant.class, livreur.getId()); 

					if(positionsArray != null && positionsArray.length > 0) {
						
						BigDecimal posLat = null, posLong = null;
						
						for (String vals :  positionsArray) {
							LivreurPositionPersistant livreurP = new LivreurPositionPersistant();
							String[]coordsArray = StringUtil.getArrayFromStringDelim(vals, ",");
							
							Date date_position = null;
							String dateString = coordsArray[2];
							
							if(DateUtil.isDate(dateString, "dd/MM/yyyy HH:mm:ss a")) {
								 date_position = DateUtil.stringToDate(dateString, "dd/MM/yyyy HH:mm:ss a");
							} else if(DateUtil.isDate(dateString, "dd/MM/yyyy HH:mm:ss aaa")) {
								 date_position = DateUtil.stringToDate(dateString, "dd/MM/yyyy HH:mm:ss aaa");
							} else {
								 date_position = DateUtil.stringToDate(dateString, "dd/MM/yyyy HH:mm:ss");
							}
							
							posLat = BigDecimalUtil.get(coordsArray[0]); 
							posLong = BigDecimalUtil.get(coordsArray[1]);
							
							listCoords.add(new LivreurPositionPersistant(date_position, posLat, posLong,liv));
						}
						
						for(LivreurPositionPersistant livreurP : listCoords) {
							livreurP.setPosition_lng(posLong);
							livreurP.setPosition_lat(posLat);
							
							em.merge(livreurP);
						}
						
						// Maj position livreur
						liv.setPosition_lng(posLong);
						liv.setPosition_lat(posLat);
						userService.mergeEntity(liv);
						
						
					}
		 		 //	return null;
	             }
	           //  });
			//}
	}
	
	@Override
	@Transactional
	public void delete(Long id) {
		try{
			super.delete(id);
		} catch(Exception e){
			MessageService.addBannerMessage("Cet établissement est lié à des enregistrements");
		}
	}

	@Override
	public void activerDesactiverElement(Long workIdLong) {
		EtablissementPersistant etsP = findById(EtablissementPersistant.class, workIdLong);
		etsP.setIs_disable(BooleanUtil.isTrue(etsP.getIs_disable()) ? false : true);
		//
		getEntityManager().merge(etsP);
	}

	@Override
	public Map<String, List<EtablissementPersistant>> getListEtablissement() {
		List<EtablissementPersistant> listEts = getEntityManager().createQuery("from EtablissementPersistant "
				+ "where (is_disable is null or is_disable=0) "
//				+ "and is_commande_line=1 "
				+ "order by type_appli, raison_sociale")
				.getResultList();
		
		Map<String, List<EtablissementPersistant>> mapEts = new HashMap<>();
		for (EtablissementPersistant etablissementP : listEts) {
			List<EtablissementPersistant> listSubEts = mapEts.get(etablissementP.getDomaine_activite());
			if(listSubEts == null) {
				listSubEts = new ArrayList<>();
				mapEts.put(etablissementP.getDomaine_activite(), listSubEts);
			}
			listSubEts.add(etablissementP);
		}
		
		return mapEts;
	}
}
