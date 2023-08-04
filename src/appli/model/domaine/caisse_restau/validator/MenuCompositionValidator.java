package appli.model.domaine.caisse_restau.validator;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.caisse.bean.MenuCompositionBean;
import appli.model.domaine.caisse_restau.dao.IMenuCompositionDao;
import appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant;
import appli.model.domaine.vente.persistant.MenuCompositionDetailPersistant;
import appli.model.domaine.vente.persistant.MenuCompositionPersistant;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.ServiceUtil;

@Named
public class MenuCompositionValidator {
	@Inject
	private IMenuCompositionDao menuDao;
	
	/*
	 * 
	 */
	public void updateCreateValidator(MenuCompositionBean menuBean) {
		if(BooleanUtil.isTrue(menuBean.getIs_menu()) && menuBean.getMtt_prix() == null) {
			MessageService.addBannerMessage("Le prix est obligatoire pour les menus.");
		}
		
		if(ServiceUtil.isNotEmpty(menuBean.getList_composition())){
			for(MenuCompositionDetailPersistant comp : menuBean.getList_composition()){
				if(comp.getOpc_article() == null 
						&& comp.getOpc_list_choix() == null 
						&& comp.getOpc_famille() == null 
						&& comp.getOpc_article_destock() == null
						&& comp.getOpc_article_inc() == null){
					MessageService.addFieldMessage("opc_list_choix.id_"+comp.getIdxIhm(), "Champs obligtoire");
					MessageService.addFieldMessage("opc_article.id_"+comp.getIdxIhm(), "Champs obligtoire");
					MessageService.addFieldMessage("opc_article_destock.id_"+comp.getIdxIhm(), "Champs obligtoire");
					MessageService.addFieldMessage("opc_famille.id_"+comp.getIdxIhm(), "Champs obligtoire");
				}
				
				if(comp.getOpc_article_destock() != null){
					comp.setPrix(BigDecimalUtil.ZERO);
					comp.setNombre(0);
				} else if(comp.getOpc_article_inc() != null){
					comp.setPrix(BigDecimalUtil.ZERO);
					comp.setNombre(0);
				}
				
				if(comp.getQuantite() == null){
					MessageService.addFieldMessage("quantite_"+comp.getIdxIhm(),  "Champs obligtoire");
				}
				if(comp.getPrix() == null && BigDecimalUtil.isZero(menuBean.getMtt_prix())){
					MessageService.addFieldMessage("prix_"+comp.getIdxIhm(), "Champs obligtoire");
				}
				if(comp.getNombre() == null){
					MessageService.addFieldMessage("nombre_"+comp.getIdxIhm(), "Champs obligtoire");
				}
			}
			
			if(!MessageService.isError()) {
				List<MenuCompositionPersistant> listMnu = menuDao.getQuery("from MenuCompositionPersistant where code=:code "
						+ "and mnu_source!='"+menuBean.getMnu_source()+"' "+(menuBean.getId()==null?"":"and id != "+menuBean.getId()))
						.setParameter("code", menuBean.getCode())
						.getResultList();
				
				if(listMnu.size() > 0){
					MessageService.addFieldMessageKey("menuComposition.code", "msg.valeur.exist");
				}
			}
		}
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void updateMenu(MenuCompositionBean menuBean) {
		updateCreateValidator(menuBean);
		
		// 
/*		Long elementId = menuBean.getElement_id();
		MenuCompositionPersistant menuPrentDB = menuDao.getMenuParent(menuBean);
		// Vérifier si le parent à changé
		if(!menuBean.getId().equals(menuPrentDB.getId()) && menuBean.getB_right()-menuBean.getB_right() != 1){
			MessageService.addBannerMessage("Il n'est pas possible de déplacer une menu");
		}*/
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void createMenu(MenuCompositionBean menuBean){
		updateCreateValidator(menuBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void deleteMenu(Long id){
		MenuCompositionPersistant menuPersistant = menuDao.findById(id);
		List<MenuCompositionPersistant> lisMenu = getListChilds(menuPersistant);
		
		for (MenuCompositionPersistant menuP : lisMenu) {
			List<CaisseMouvementArticlePersistant> listMvm = menuDao.getQuery("from CaisseMouvementArticlePersistant where opc_menu.id=:menuId")
				.setParameter("menuId", menuP.getId())
				.getResultList();
			
			if(listMvm.size() > 0){
				MessageService.addBannerMessage("Ce menu est lié à des commandes");
				return;
			}
		}
	}
	
	/**
	 * @param fam
	 * @return
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	private List<MenuCompositionPersistant> getListChilds(MenuCompositionPersistant fam){
        return menuDao.getQuery("from "+fam.getClass().getSimpleName()
                + " where b_left>=:bLeft and b_right<=:bRight "
                + "order by b_left desc")
                .setParameter("bLeft", fam.getB_left())
                .setParameter("bRight", fam.getB_right())
                .getResultList();
	}
	
}
