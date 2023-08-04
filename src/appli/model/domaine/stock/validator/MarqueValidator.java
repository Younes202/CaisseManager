package appli.model.domaine.stock.validator;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.stock.bean.MarqueBean;
import appli.model.domaine.stock.dao.IMarqueDao;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import framework.model.common.service.MessageService;

@Named
public class MarqueValidator {
	@Inject
	private IMarqueDao marqueDao;
	
	/*
	 * 
	 */
	public void updateCreateValidator(MarqueBean marqueBean) {
		if(marqueDao.isNotUnique(marqueBean, "code")){
			MessageService.addFieldMessage("marque.titre", "Cette valeur existe déjà");
		}
		if(marqueDao.isNotUnique(marqueBean, "libelle")){
			MessageService.addFieldMessage("marque.libelle", "Cette valeur existe déjà");
		}
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void update(MarqueBean marqueBean) {
		updateCreateValidator(marqueBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void create(MarqueBean marqueBean){
		updateCreateValidator(marqueBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void delete(Long id){
		List<ArticlePersistant> listMvm = marqueDao.getQuery("from ArticlePersistant where opc_marque.id=:marqueId")
											.setParameter("marqueId", id).getResultList();
		if(listMvm.size()>0){
			MessageService.addBannerMessage("Cet marque est utilisé dans des articles ("+listMvm.get(0).getCode()+").");
		}
	}
	
}
