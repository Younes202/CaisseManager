package appli.model.domaine.caisse_restau.validator;


import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.caisse_restau.bean.ListChoixBean;
import appli.model.domaine.caisse_restau.dao.IListChoixDao;
import appli.model.domaine.vente.persistant.ListChoixDetailPersistant;
import appli.model.domaine.vente.persistant.MenuCompositionDetailPersistant;
import framework.model.common.service.MessageService;

@Named
public class ListChoixValidator {
	@Inject
	private IListChoixDao listChoixDao;
	
	public void updateCreateValidator(ListChoixBean listChoixBean) {
		if(listChoixDao.isNotUnique(listChoixBean, "libelle")){
			MessageService.addFieldMessageKey("listChoix.libelle", "msg.valeur.exist");
		} else if(listChoixDao.isNotUnique(listChoixBean, "code")){
			MessageService.addFieldMessageKey("listChoix.code", "msg.valeur.exist");
		} else if(listChoixBean.getList_choix_detail() == null || listChoixBean.getList_choix_detail().size() == 0) {
			MessageService.addBannerMessage("Vous devez ajouter au moins une composante à cette liste.");			
		}
	}
	
	public void update(ListChoixBean listChoixBean) {
		updateCreateValidator(listChoixBean);
	}

	public void create(ListChoixBean listChoixBean) {
		updateCreateValidator(listChoixBean);
	}

	public void delete(Long id){
		List<ListChoixDetailPersistant> listElement = listChoixDao.getQuery("from ListChoixDetailPersistant where opc_list_choix.id=:choixId")
				.setParameter("choixId", id)
				.getResultList();
		if(listElement.size() > 0){
			MessageService.addBannerMessage("Cette liste de choix est utilisée dans une autre liste de choix ("+listElement.get(0).getOpc_choix_parent().getLibelle()+")");
		} else{
			listElement = listChoixDao.getQuery("from ListChoixDetailPersistant where opc_choix_parent.id=:choixId")
				.setParameter("choixId", id)
				.getResultList();
			if(listElement.size() > 0){
				MessageService.addBannerMessage("Cette liste de choix est utilisée dans une autre liste de choix ("+listElement.get(0).getOpc_choix_parent().getLibelle()+")");
			} else{
				List<MenuCompositionDetailPersistant> listMenu = listChoixDao.getQuery("from MenuCompositionDetailPersistant where opc_list_choix.id=:choixId")
				.setParameter("choixId", id)
				.getResultList();
				if(listMenu.size() > 0){
					MessageService.addBannerMessage("Cette liste de choix est utilisée dans un menu ("+listMenu.get(0).getOpc_menu().getLibelle()+")");
				}
			}
		}
	}
}
