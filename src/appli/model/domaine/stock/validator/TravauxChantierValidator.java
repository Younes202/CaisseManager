package appli.model.domaine.stock.validator;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.stock.bean.TravauxChantierBean;
import appli.model.domaine.stock.dao.ITravauxChantierDao;
import appli.model.domaine.stock.persistant.TravauxChantierPersistant;
import appli.model.domaine.stock.persistant.TravauxPersistant;
import framework.model.common.service.MessageService;

@Named
public class TravauxChantierValidator {
	@Inject
	private ITravauxChantierDao travauxChantierDao;
	
	/*
	 * 
	 */
	public void updateCreateValidator(TravauxChantierBean travauxChantierBean) {
	
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void update(TravauxChantierBean travauxChantierBean) {
		updateCreateValidator(travauxChantierBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void create(TravauxChantierBean travauxChantierBean){
		updateCreateValidator(travauxChantierBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void delete(Long id){
		TravauxChantierPersistant travauxChantierPersistant = travauxChantierDao.findById(id);
		List<TravauxPersistant> listMvm = travauxChantierPersistant.getList_travaux();
		if(listMvm.size()>0){
			MessageService.addDialogMessage("Ce chantier est associé à des des travaux");
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
