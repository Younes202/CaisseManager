package appli.model.domaine.stock.validator;

import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.stock.bean.FournisseurChequeBean;
import appli.model.domaine.stock.dao.IFournisseurChequeDao;
import appli.model.domaine.stock.persistant.FournisseurChequePersistant;
import framework.model.common.service.MessageService;

@Named
public class FournisseurChequeValidator {
	@Inject
	private IFournisseurChequeDao fournisseurChequeDao;
	
	/*
	 * 
	 */
	public void updateCreateValidator(FournisseurChequeBean fournisseurChequeBean) {
		if(fournisseurChequeDao.isNotUnique(fournisseurChequeBean, "num_cheque")){
			MessageService.addFieldMessageKey("fournisseurCheque.num_cheque", "msg.valeur.exist");
		}
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void update(FournisseurChequeBean fournisseurChequeBean) {
		updateCreateValidator(fournisseurChequeBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void create(FournisseurChequeBean fournisseurChequeBean){
		updateCreateValidator(fournisseurChequeBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void delete(Long id){
		FournisseurChequePersistant fcPersistant = fournisseurChequeDao.findById(id);
		if(fcPersistant.getElementId() != null){
			MessageService.addDialogMessage("Ce chèque est utilisé dans un paiement.");
		}
	}
	
}
