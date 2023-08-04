package appli.model.domaine.stock.validator;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.stock.bean.TravauxBean;
import appli.model.domaine.stock.dao.ITravauxDao;
import appli.model.domaine.stock.persistant.MouvementPersistant;
import appli.model.domaine.stock.persistant.TravauxPersistant;
import framework.model.common.service.MessageService;

@Named
public class TravauxValidator {
	@Inject
	private ITravauxDao travauxDao;
	
	/*
	 * 
	 */
	public void updateCreateValidator(TravauxBean travauxBean) {
	
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void update(TravauxBean travauxBean) {
		updateCreateValidator(travauxBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void create(TravauxBean travauxBean){
		updateCreateValidator(travauxBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void delete(Long id){
		TravauxPersistant travauxPersistant = travauxDao.findById(id);
		List<MouvementPersistant> listMvm = travauxPersistant.getList_achat();
		if(listMvm.size()>0){
			MessageService.addDialogMessage("Ce travaux est utilisé par le mouvement d'achat");
		}
	}
	
	/**
	 * @param ids
	 */
	public void delete_group(Long[] ids){
		for (Long artId : ids) {
			delete(artId);
		}
	}
}
