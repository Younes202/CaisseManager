package appli.model.domaine.stock.validator;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.stock.bean.FournisseurBean;
import appli.model.domaine.stock.dao.IFournisseurDao;
import appli.model.domaine.stock.persistant.FournisseurPersistant;
import appli.model.domaine.stock.persistant.MouvementPersistant;
import framework.model.common.service.MessageService;

@Named
public class FournisseurValidator {
	@Inject
	private IFournisseurDao fournisseurDao;
	
	/*
	 * 
	 */
	public void updateCreateValidator(FournisseurBean fournisseurBean) {
		if(fournisseurDao.isNotUnique(fournisseurBean, "code")){
			MessageService.addFieldMessageKey("fournisseur.code", "msg.valeur.exist");
		}
		if(fournisseurDao.isNotUnique(fournisseurBean, "marque")){
			MessageService.addFieldMessageKey("fournisseur.marque", "msg.valeur.exist");
		}
		if(fournisseurDao.isNotUnique(fournisseurBean, "libelle")){
			MessageService.addFieldMessageKey("fournisseur.libelle", "msg.valeur.exist");
		}
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void update(FournisseurBean fournisseurBean) {
		updateCreateValidator(fournisseurBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void create(FournisseurBean fournisseurBean){
		updateCreateValidator(fournisseurBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void delete(Long id){
		FournisseurPersistant fournisseurPersistant = fournisseurDao.findById(id);
		List<MouvementPersistant> listMvm = fournisseurPersistant.getList_mouvement();
		if(listMvm.size()>0){
			MessageService.addDialogMessage("Ce fournisseur est utilisé par le mouvement de stock");
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
