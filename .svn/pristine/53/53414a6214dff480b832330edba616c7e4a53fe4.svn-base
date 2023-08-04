package appli.model.domaine.stock.validator;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

import appli.controller.domaine.stock.bean.FamilleBean;
import appli.model.domaine.stock.dao.IEmplacementDao;
import appli.model.domaine.stock.dao.IFamilleDao;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.persistant.EmplacementPersistant;
import appli.model.domaine.stock.persistant.FamilleConsommationPersistant;
import appli.model.domaine.stock.persistant.FamilleCuisinePersistant;
import appli.model.domaine.stock.persistant.FamilleFournisseurPersistant;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.stock.persistant.FamilleStockPersistant;
import appli.model.domaine.stock.persistant.FournisseurPersistant;
import appli.model.domaine.stock.persistant.MouvementPersistant;
import appli.model.domaine.stock.service.impl.FamilleService;
import framework.model.common.service.MessageService;
import framework.model.common.util.DateUtil;

@Named
public class FamilleValidator {
	@Inject
	private IFamilleDao familleDao;
	@Inject
	private IEmplacementDao emplacementDao;
	
	/*
	 * 
	 */
	public void updateCreateValidator(FamilleBean familleBean) {
		// Libellé
		String req = "from FamillePersistant where libelle=:libelle and type=:type";
		if(familleBean.getId() != null) {
			req = req + " and id!=:currId";
		}
		Query query = familleDao.getQuery(req).setParameter("libelle", familleBean.getLibelle())
					.setParameter("type", familleBean.getType());
		if(familleBean.getId() != null) {
			query.setParameter("currId", familleBean.getId());
		}
		List list = query.getResultList();
		
		if(list.size() > 0){
			MessageService.addFieldMessageKey("famille.libelle", "msg.valeur.exist");
		}
		
		// Code
		req = "from FamillePersistant where code=:code and type=:type";
		if(familleBean.getId() != null) {
			req = req + " and id!=:currId";
		}
		query = familleDao.getQuery(req)
					.setParameter("code", familleBean.getCode())
					.setParameter("type", familleBean.getType());
		if(familleBean.getId() != null) {
			query.setParameter("currId", familleBean.getId());
		}
		list = query.getResultList();
		if(list.size() > 0){
			MessageService.addFieldMessageKey("famille.code", "msg.valeur.exist");
		}
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void updateFamille(FamilleBean familleBean) {
		updateCreateValidator(familleBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void createFamille(FamilleBean familleBean){
		updateCreateValidator(familleBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void deleteFamille(Long id){
		FamillePersistant familleP = familleDao.findById(id);
		List<FamillePersistant> lisFamille = getListChilds(familleP);
		
		for (FamillePersistant famillePersistant : lisFamille) {
			if(famillePersistant.getType().equals(FamilleService.FAMILLE_TYPE_ENUM.CO.toString())){
				List<MouvementPersistant> list_mvm_consommation = ((FamilleConsommationPersistant)famillePersistant).getList_mvm_consommation();
				if(list_mvm_consommation.size() > 0){
					MessageService.addBannerMessage("Cette famille est utilisée par une consommation du ("+DateUtil.dateToString(list_mvm_consommation.get(0).getDate_mouvement()));
					return;
				}			
			} else if(famillePersistant.getType().equals(FamilleService.FAMILLE_TYPE_ENUM.FO.toString())){
				List<FournisseurPersistant> list_fournisseur = ((FamilleFournisseurPersistant)famillePersistant).getList_fournisseur();
				if(list_fournisseur.size() > 0){
					MessageService.addBannerMessage("Cette famille est utilisée pour un fournisseur ("+list_fournisseur.get(0).getLibelle());
					return;
				}
			} else if(famillePersistant.getType().equals(FamilleService.FAMILLE_TYPE_ENUM.ST.toString())){
				List<ArticlePersistant> list_article = ((FamilleStockPersistant)famillePersistant).getList_article();
				if(list_article.size() > 0){
					MessageService.addBannerMessage("Cette famille est utilisée pour un article ("+list_article.get(0).getCode()+"-"+list_article.get(0).getLibelle()+")");
					return;
				}

				String req = "from EmplacementPersistant where familles_cmd like :famId or familles_ex_cmd like :famId ";
				Query query = emplacementDao.getQuery(req).setParameter("famId", "%;"+id+";%");
				List<EmplacementPersistant> listEmp = (List<EmplacementPersistant>)query.getResultList();
				String empTitres = "";
				for(EmplacementPersistant emp : listEmp) {
					empTitres += emp.getTitre()+", ";
				}
				if(listEmp.size() > 0){
					MessageService.addBannerMessage("Cette famille est utilisée dans ces emplacement: "+empTitres+"<br>-Veuillez modifier la fiche d'emplacement et réessayer à nouveau");
					return;
				}
			} else if(famillePersistant.getType().equals(FamilleService.FAMILLE_TYPE_ENUM.CU.toString())){
				// A TRAITER REFACTO
				List<ArticlePersistant> list_article = ((FamilleCuisinePersistant)famillePersistant).getList_article();
				if(list_article.size() > 0){
					MessageService.addBannerMessage("Cette famille est utilisée pour un article ("+list_article.get(0).getCode()+"-"+list_article.get(0).getLibelle()+")");
					return;
				}
				// Liste de choix
//				List<ListChoixDetailPersistant> list_choix = ((FamilleCuisinePersistant)famillePersistant).getList_choix();
//				if(list_choix.size() > 0){
//					ListChoixPersistant opc_list_choix = list_choix.get(0).getOpc_choix_parent();
//					MessageService.addBannerMessage("Cette famille est utilisée dans une liste de choix ("+opc_list_choix.getCode()+"-"+opc_list_choix.getLibelle()+")");
//					return;
//				}
//				// Menu composition
//				List<MenuCompositionPersistant> list_menu = ((FamilleCuisinePersistant)famillePersistant).getList_menu();
//				if(list_menu.size() > 0){
//					MessageService.addBannerMessage("Cette famille est utilisée dans un menu ("+list_menu.get(0).getCode()+" "+list_menu.get(0).getLibelle()+")");
//					return;
//				}
			}
		}
	}
	
	/**
	 * @param fam
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<FamillePersistant> getListChilds(FamillePersistant fam){
        return familleDao.getQuery("from "+fam.getClass().getSimpleName()
                + " where b_left>=:bLeft and b_right<=:bRight "
                + "order by b_left desc")
                .setParameter("bLeft", fam.getB_left())
                .setParameter("bRight", fam.getB_right())
                .getResultList();
	}
}
