package appli.model.domaine.stock.validator;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.stock.bean.InventaireBean;
import appli.model.domaine.stock.dao.IInventaireDao;
import appli.model.domaine.stock.dao.IMouvementDao;
import appli.model.domaine.stock.persistant.InventairePersistant;
import appli.model.domaine.stock.persistant.MouvementPersistant;
import appli.model.domaine.stock.service.IInventaireService;
import framework.model.common.service.MessageService;
import framework.model.common.util.DateUtil;

@Named
public class InventaireValidator {
	
	@Inject
	IInventaireDao inventaireDao;
	@Inject
	IMouvementDao mouvementDao;
	@Inject
	IInventaireService inventaireService;
	
	/*
	 * 
	 */
	public void updateCreateValidator(InventaireBean inventaireBean) {
		Date maxDate = inventaireService.getMaxDateInventaireValide(inventaireBean.getOpc_emplacement().getId());
		
		if(maxDate != null && inventaireBean.getDate_realisation().before(maxDate)){
			MessageService.addBannerMessage("La date de réalisation doit être postérieure ou égale à la date du dernier inventaire validé ("+DateUtil.dateToString(maxDate)+").");
		}
		
		// Controler inventaire non validé
		if(inventaireService.getInventaireNonValide(inventaireBean.getOpc_emplacement().getId()).size()>0){
			MessageService.addBannerMessage("Un inventaire non validé est lié à cet emplacement. Veuillez le valider avant de continuer.");
			return;
		}
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void update(InventaireBean inventaireBean) {
		if(inventaireBean.getIs_valid() != null && inventaireBean.getIs_valid()){
			MessageService.addBannerMessage("On ne peut pas modifier un inventaire validé.");
		}
		updateCreateValidator(inventaireBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void create(InventaireBean inventaireBean){
		updateCreateValidator(inventaireBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void annulerValidation(Long inventaireId){
		InventairePersistant inventairePersistant = inventaireDao.findById(inventaireId);
		Date maxDate = inventaireService.getMaxDateInventaireValide(inventairePersistant.getOpc_emplacement().getId());
		//
		if(maxDate != null && inventairePersistant.getDate_realisation().compareTo(maxDate) != 0){
			MessageService.addBannerMessage("Un autre inventaire validé existe déjà.");
		} else if(inventairePersistant.getIs_valid() == null || !inventairePersistant.getIs_valid()){
			MessageService.addBannerMessage("Cet inventaire n'a pas été validé");
		}
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void validerInventaire(Long inventaireId){
		InventairePersistant inventairePersistant = inventaireDao.findById(inventaireId);
		// Vérifier si c'est le dernier inventaire
		Date maxDateValide = inventaireService.getMaxDateInventaireValide(inventairePersistant.getOpc_emplacement().getId());
		Date minDateNonValide = inventaireService.getMinDateInventaireNonValide(inventairePersistant.getOpc_emplacement().getId());
		
		if(maxDateValide != null && inventairePersistant.getDate_realisation().before(maxDateValide)){
			MessageService.addBannerMessage("La date de réalisation doit être postérieure à la date du dernier inventaire validé ("+DateUtil.dateToString(maxDateValide)+").");
		} else if(minDateNonValide != null && minDateNonValide.before(inventairePersistant.getDate_realisation())){ 
			MessageService.addBannerMessage("Un inventaire antérieur et non validé a été trouvé en date du "+DateUtil.dateToString(minDateNonValide)+"). Vous devez le valider en premier.");
		} else if(inventairePersistant.getIs_valid() != null && inventairePersistant.getIs_valid()){
			MessageService.addBannerMessage("Cet inventaire est déjà validé.");
		}
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void delete(Long id){
		InventairePersistant inventairePersistant = inventaireDao.findById(id);
		if(inventairePersistant.getIs_valid() != null && inventairePersistant.getIs_valid()){
			MouvementPersistant mouvementPersistant = (MouvementPersistant) mouvementDao.getQuery("from MouvementPersistant mvm where mvm.opc_inventaire.id = :inventaireId")
					.setParameter("inventaireId", id).getSingleResult();
			if(mouvementPersistant!=null){
				MessageService.addBannerMessage("cet inventaire est lié à des mouvements de stock");
			}
		}
	}
	
}
