package appli.model.domaine.stock.validator;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.stock.bean.EmplacementBean;
import appli.model.domaine.stock.dao.IEmplacementDao;
import appli.model.domaine.stock.dao.IMouvementDao;
import appli.model.domaine.stock.persistant.EmplacementPersistant;
import appli.model.domaine.stock.persistant.MouvementPersistant;
import framework.model.common.service.MessageService;

@Named
public class EmplacementValidator {
	@Inject
	private IEmplacementDao emplacementDao;
	@Inject
	private IMouvementDao mouvementDao;
	
	/*
	 * 
	 */
	public void updateCreateValidator(EmplacementBean emplacementBean) {
		if(emplacementDao.isNotUnique(emplacementBean, "titre")){
			MessageService.addFieldMessage("emplacement.titre", "Cette valeur existe déjà");
		}
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void update(EmplacementBean emplacementBean) {
		updateCreateValidator(emplacementBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void create(EmplacementBean emplacementBean){
		updateCreateValidator(emplacementBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void delete(Long id){
		EmplacementPersistant emplacementPersistant = emplacementDao.findById(id);
		//A TRAITER REFACTO
//		List<CaissePersistant> listCaisses = caisseDao.getQuery("from CaissePersistant caisse where caisse.opc_stock_cible.id=:emplacementId")
//											.setParameter("emplacementId", id).getResultList();
//		if(listCaisses.size()>0){
//			MessageService.addBannerMessage("Cet emplacement est utilisé par la caisse");
//		}
		List<MouvementPersistant> listMvm = mouvementDao.getQuery("from MouvementPersistant mvm where mvm.opc_emplacement.id=:emplacementId or mvm.opc_destination.id=:emplacementId")
											.setParameter("emplacementId", id).getResultList();
		if(listMvm.size()>0){
			MessageService.addBannerMessage("Cet emplacement est utilisé par des mouvements de stock");
		}
	}
	
}
