package appli.model.domaine.caisse_restau.service.impl;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.caisse.bean.MenuCompositionBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.caisse.service.IMenuCompositionService;
import appli.model.domaine.caisse_restau.dao.IMenuCompositionDao;
import appli.model.domaine.caisse_restau.validator.MenuCompositionValidator;
import appli.model.domaine.vente.persistant.CaissePersistant;
import appli.model.domaine.vente.persistant.MenuCompositionDetailPersistant;
import appli.model.domaine.vente.persistant.MenuCompositionPersistant;
import framework.controller.bean.PagerBean;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.common.annotation.validator.WorkModelMethodValidator;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.service.MessageService;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StringUtil;
import framework.model.service.GenericJpaService;


@WorkModelClassValidator(validator=MenuCompositionValidator.class)
@Named
public class MenuCompositionService extends GenericJpaService<MenuCompositionBean, Long> implements IMenuCompositionService{
	@Inject
	private IMenuCompositionDao menuDao;
	
	 @Override
	    public List<MenuCompositionPersistant> getListeMenuEnfants(Long parentId, Long caisseId, boolean excludeDisabled) {
	       MenuCompositionPersistant parentFamille = findById(parentId);
	        List<MenuCompositionPersistant> listMenu = null;
	        
	        boolean isCaisseSpecifique = false;
	        if(caisseId != null) {
	        	CaissePersistant currCaisse = (CaissePersistant)findById(CaissePersistant.class, caisseId);
	        	isCaisseSpecifique = BooleanUtil.isTrue(currCaisse.getIs_specifique());
	        }
	        
	        String req = "from MenuCompositionPersistant "
	                + "where b_left>:bLeft and b_right<:bRight "
	                + "and level=:level";
	        //
	        if(excludeDisabled) {
	        	req = req + " and (is_desactive is null or is_desactive=0) ";
	        }
	        if(caisseId != null) {
	        	if(isCaisseSpecifique){
		        	req = req + " and caisse_target is not null and caisse_target like :currCaisse ";
		        } else{
		        	req = req + " and (caisse_target is null or caisse_target like :currCaisse) ";
		        }
	        }
	        req += getReqCompl();
	        
	        req = req + " order by b_left";
	        
	        // Familles filles
	        if(parentFamille.getB_right()-parentFamille.getB_left() > 1){
	            Query query = getQuery(req)
	                .setParameter("bLeft", parentFamille.getB_left())
	                .setParameter("bRight", parentFamille.getB_right())
	                .setParameter("level", parentFamille.getLevel()+1);
	            //
	            if(caisseId != null) {
	            	query.setParameter("currCaisse", "%|"+caisseId+"|%");
	            }
	            listMenu = query.getResultList();
	        }
	        
	        return listMenu;
	    }
	 
	 	@SuppressWarnings("unchecked")
		@Override
		public MenuCompositionPersistant getMenuParent(MenuCompositionPersistant menu) {
			// On cherche le parent dans l'ancien exercice
			List<MenuCompositionPersistant> listMenus = getQuery(
					"from MenuCompositionPersistant cpt where "
							+ "cpt.b_left<:left and cpt.b_right>:right "
							+ getReqCompl()
							+ "order by cpt.b_left desc ")
					.setParameter("left", menu.getB_left())
					.setParameter("right", menu.getB_right())
					.getResultList();

			for (MenuCompositionPersistant menuPersistant : listMenus) {
				return menuPersistant;
			}
			return null;
		}
	 	
	    @Override
	    public MenuCompositionPersistant getMenuParent(Long menuCompId) {
	        return getMenuParent(findById(menuCompId));
	    }
	
	
	@Override
	@Transactional
	@WorkModelMethodValidator
	public void createMenu(MenuCompositionBean compositionBean){
		menuDao.createMenu(compositionBean);
	}
	
	@Override
	@Transactional
	@WorkModelMethodValidator
	public void deleteMenu(Long id){
		MenuCompositionPersistant menuPersistant = findById(id);
		int b_right = menuPersistant.getB_right();
		int b_left = menuPersistant.getB_left();
		
		List<MenuCompositionPersistant> listMenu = (List<MenuCompositionPersistant>) getQuery(
				"from MenuCompositionPersistant compte "
						+ "where compte.b_left>=:left and compte.b_right<=:right"
						+ getReqCompl()
					)
				.setParameter("left", b_left).setParameter("right", b_right)
				.getResultList();

		for (MenuCompositionPersistant menuCompDetP : listMenu) {
			// Supprimer le détail
			getQuery("delete from MenuCompositionDetailPersistant "
					+ "where opc_menu.id=:menuId"
					+ getReqCompl())
				.setParameter("menuId", menuCompDetP.getId())
				.executeUpdate();
		}
		
		getEntityManager().flush();		
		//
		menuDao.deleteMenu(id);
	}

	@Override
	@WorkModelMethodValidator
	public void updateMenu(MenuCompositionBean menuBean) {
		if (menuBean.getId() != null) {
			MenuCompositionBean artBean = findById(menuBean.getId());
			artBean.getList_composition().clear();
			artBean.getList_composition().addAll(menuBean.getList_composition());
		}
		
		menuDao.updateMenu(menuBean);
	}
	
	@Override
	@Transactional
	public MenuCompositionPersistant getMenuRoot() {
		MenuCompositionPersistant mnuP = (MenuCompositionPersistant) getSingleResult(getQuery("from MenuCompositionPersistant where code=:code"
				+ getReqCompl())
			.setParameter("code", "ROOT"));
			
		if(mnuP == null) {
			String leftMnu = (String) MessageService.getGlobalMap().get(ProjectConstante.LEFT_MENU_ID);
			mnuP = new MenuCompositionPersistant();
			mnuP.setB_left(0);
			mnuP.setB_right(1);
			mnuP.setCode("ROOT");
			mnuP.setDate_creation(new Date());
			mnuP.setDate_maj(new Date());
			mnuP.setLibelle("Menu client");
			mnuP.setLevel(0);
			mnuP.setSignature("ADMIN");
			mnuP.setMnu_source(leftMnu);
			//
			mnuP = getEntityManager().merge(mnuP);
		}
		return mnuP;
	}

	private String getReqCompl() {
		String leftMnu = (String) MessageService.getGlobalMap().get(ProjectConstante.LEFT_MENU_ID);
		String reqCompl = "";
		if("cai-menu".equals(leftMnu) || StringUtil.isEmpty(leftMnu)) {
			reqCompl += " and (mnu_source is null or mnu_source='"+leftMnu+"') ";
		} else {
			reqCompl += " and mnu_source='"+leftMnu+"' ";
		}
		return reqCompl;
	}
	
	@Override
	public List<MenuCompositionPersistant> getListeMenu(boolean ignoreRoot, boolean isActifOnly) {
		String req = "from MenuCompositionPersistant where 1=1 ";
		if(ignoreRoot) {
			req = req + "and code!=:code ";
		}
		if(isActifOnly){
			req = req + " and (is_desactive is null or is_desactive=0) ";
		}
		
		req = req + getReqCompl();
		
		req = req +  "order by b_left";
		
		Query query = getQuery(req);
		
		if(ignoreRoot) {
			query.setParameter("code", "ROOT");
		}
		return query.getResultList();
	}
	
	@Override
	@Transactional
	public void activerDesactiverElement(Long menuId) {
		if(menuId == null) {
			return;
		}
		
		MenuCompositionPersistant menuPersistant = menuDao.findById(menuId);
		menuPersistant.setIs_desactive(BooleanUtil.isTrue(menuPersistant.getIs_desactive()) ? false : true);
		
		List<MenuCompositionPersistant> listEnfant = getListeMenuEnfants(menuId, null, false);
		if(listEnfant != null) {
			for (MenuCompositionPersistant menuCompositionP : listEnfant) {
				menuCompositionP.setIs_desactive(BooleanUtil.isTrue(menuPersistant.getIs_desactive()) ? true : false);
			}
		}
		
		//
		getEntityManager().merge(menuPersistant);
	}
	
	/**
	 * 
	 */
	@Override
	public String generateCode(Long elementId) {
		MenuCompositionPersistant parentMnu = menuDao.findById(elementId);
		boolean isPrentRoot = parentMnu.getCode().equals("ROOT");
		int parentLength = (isPrentRoot ? 0 : parentMnu.getCode().length());
		
		Query query = getNativeQuery("select "
				+ "max("
				+ "	case when LENGTH(code) > "+parentLength+" then CAST(SUBSTR(code, "+(parentLength+1)+") AS UNSIGNED) "
				+ " else 0 end "
				+ ") from menu_composition "
				+ "where b_left>:left and b_right<:right and level=:level"
				+ getReqCompl()) 
			.setParameter("left", parentMnu.getB_left())
			.setParameter("right", parentMnu.getB_right())
			.setParameter("level", parentMnu.getLevel()+1);
				
		BigDecimal max_num = (BigDecimal)query.getSingleResult();
		
		String nextCode = "1";
		if(max_num != null){
			nextCode = ""+(max_num.intValue() + 1);
		}
		
		return (isPrentRoot ? ""+nextCode : parentMnu.getCode()+nextCode);
	}

	@Override
	public void changerOrdre(Map<String, Object> mapOrder) {
		menuDao.changerOrdre(mapOrder);
	}

	@Override
	@Transactional
	public void saveConf(String menuArtStr, String familleStr) {
		EtablissementPersistant restauP = (EtablissementPersistant) findById(EtablissementPersistant.class, ContextAppli.getEtablissementBean().getId());
		restauP.setVente_familles(familleStr);
		restauP.setVente_menus_art(menuArtStr);
		//
		getEntityManager().merge(restauP);
	}

	@Override
	@Transactional
	public void dupliquerMenu(MenuCompositionBean menuBean, Long workIdLong) {
		if(workIdLong == null){
			return;
		}
		String leftMnu = (String) MessageService.getGlobalMap().get(ProjectConstante.LEFT_MENU_ID);
		MenuCompositionPersistant menuParent = getMenuParent(workIdLong);
		menuParent.getIs_menu();
		
		Long parentsId = menuParent.getId();
		
		// Dupplic élément
		menuBean.setId(null);
		menuBean.setElement_id(parentsId);
		menuBean.setCode(menuBean.getCode()+"_C");
		menuBean.setMnu_source(leftMnu);
		//
		createMenu(menuBean);
		
		parentsId = menuBean.getId();
		List<MenuCompositionPersistant> listEnfants = getListeMenuEnfants(workIdLong, null, true);
		if(listEnfants != null){
			for (MenuCompositionPersistant menuCompositionP : listEnfants) {
				MenuCompositionBean mnuB = (MenuCompositionBean) ServiceUtil.persistantToBean(MenuCompositionBean.class, menuCompositionP);
				mnuB.setElement_id(parentsId);
				mnuB.setId(null);
				mnuB.setCode(mnuB.getCode()+"_C");
				mnuB.setMnu_source(leftMnu);
				
				List<MenuCompositionDetailPersistant> listDet = new ArrayList<>();
				for(MenuCompositionDetailPersistant data : mnuB.getList_composition()){
					data = (MenuCompositionDetailPersistant) ReflectUtil.cloneBean(data);
					data.setOpc_menu(null);
					data.setId(null);
					listDet.add(data);
				}
				mnuB.setList_composition(listDet);
				
				createMenu(mnuB);
			}
		}
	}
	
	@Override
	public List<MenuCompositionPersistant> getListeMenuCaissePagination(Long caisseId, PagerBean pagerBean) {
		CaissePersistant currCaisse = null;
		if(caisseId != null && caisseId > 0){
			currCaisse = (CaissePersistant)findById(CaissePersistant.class, caisseId);
		}
		boolean isCaisseSpecifique = (currCaisse != null && BooleanUtil.isTrue(currCaisse.getIs_specifique()));
		
		String req = "from MenuCompositionPersistant "
				+ "where level=1 and (is_desactive is null or is_desactive=false) "
				+ getReqCompl();
		//
		if(isCaisseSpecifique){
	        req = req + " and caisse_target is not null and caisse_target like :currCaisse ";
	    } else{
	       	req = req + " and (caisse_target is null or caisse_target like :currCaisse) ";
	    }

		if(pagerBean != null){
			Long count = (Long) getQuery("select count(0) "+req)
					.setParameter("currCaisse", "%|"+caisseId+"|%")
					.getSingleResult();
			pagerBean.setNbrLigne(count.intValue());
		}
		
		Query query = getQuery(req + " order by b_left")
				.setParameter("currCaisse", "%|"+caisseId+"|%");
		
		if(pagerBean != null){
			query.setMaxResults(pagerBean.getElementParPage())
				.setFirstResult(pagerBean.getStartIdx());
		}
		return query.getResultList();
	}
}
