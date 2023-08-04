package appli.controller.domaine.compta.action;

import java.util.List;

import javax.inject.Inject;

import appli.controller.domaine.compta.bean.CompteBean;
import appli.model.domaine.compta.service.ICompteService;
import appli.model.domaine.compta.service.IExerciceService;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.ContextGloabalAppli;
import framework.controller.annotation.WorkController;
import framework.model.beanContext.ComptePersistant;
import framework.model.beanContext.ExercicePersistant;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.service.MessageService;
import framework.model.common.util.StringUtil;

/**
  *
 */
@WorkController(nameSpace="compta", bean=CompteBean.class, jspRootPath="/domaine/compta/")
public class CompteAction extends ActionBase {
	@Inject
	private ICompteService compteService;
	@Inject
	private IExerciceService exerciceService;
	
	/**
	 * @param httpUtil
	 */
	public void work_init(ActionUtil httpUtil) {
		boolean isEditionPage = httpUtil.isEditionPage();
		List<ExercicePersistant> listExercice = exerciceService.findAll(ExercicePersistant.class);
		List<ComptePersistant> listCompte = compteService.getPlanComptable(true);
		httpUtil.setRequestAttribute("listCompte", listCompte);
		
		if(listExercice.size() == 0){
			MessageService.addGrowlMessage(MSG_TYPE.WARNING, "Initialisation des comptes", "Vous devez d'abord créer un exercice avant d'initialiser les comptes.");
		}
		// Options d'initialisation
		String[][] listChoix = {{"NO", "Non"}, {"C", "Uniquement si créditeur"}, {"D", "Uniquement si débiteur"}, {"ALL", "Dans tous les cas"}}; 
		httpUtil.setRequestAttribute("list_choix_init", listChoix);
		
		// Parent ID
		if(httpUtil.getWorkId() != null) {
			ComptePersistant parentCompte = compteService.getCompteParent(httpUtil.getWorkIdLong());
			httpUtil.setRequestAttribute("parent_compte", parentCompte.getId());
		}
		
		//String parentId = httpUtil.getParameter("fam");
		/*String currParentId = httpUtil.getParameter("compte.parent_id");
		if(currParentId == null && parentId != null){
			ComptePersistant parentCompte = compteService.getCompteParent(Long.valueOf(EncryptionUtil.decrypt(parentId)));
			if(parentCompte != null){
				currParentId = ""+parentCompte.getId();
			}
		}
		httpUtil.setRequestAttribute("parent_compte", currParentId);*/
		
		//
	//	if(StringUtil.isNotEmpty(parentId)){
	//		httpUtil.setRequestAttribute("fam", parentId);
	//	} else{
			//httpUtil.setRequestAttribute("fam", EncryptionUtil.encrypt(compteService.getCompteRoot().getId().toString()));// Parent racine
	//	}
		
		// Le paramètre exercice
		if(!isEditionPage){
			httpUtil.setRequestAttribute("list_exercice", exerciceService.findAll(ExercicePersistant.class));
		}
	}
	
	/**
	 * @param httpUtil
	 */
	public void work_find(ActionUtil httpUtil) {
		ExercicePersistant currentExercice = ContextGloabalAppli.getExerciceBean();
		if(currentExercice == null) {
			MessageService.addBannerMessage(MSG_TYPE.WARNING, "Aucun exercice actif n'a été trouvé ! Veuillez créer un nouvel exercice !");
			httpUtil.setDynamicUrl("/domaine/compta/compte_list.jsp");
			return;
		} 
		Long exerciceId = currentExercice.getId();
		List<CompteBean> listCompte;
		
		if(StringUtil.isTrue(""+httpUtil.getMenuAttribute("isBl"))){
			listCompte = compteService.getListCompteByCode("1%");
			listCompte.addAll(compteService.getListCompteByCode("4%"));
			listCompte.addAll(compteService.getListCompteByCode("5%"));
		} else{
			String compte = httpUtil.getParameter("compte");

			if("CP".equals(compte)){
				listCompte = compteService.getListCompteByCode("6%");
				listCompte.addAll(compteService.getListCompteByCode("7%"));
			} else if("CR".equals(compte)){
				listCompte = compteService.getListCompteChargeRecuperable(true);
			} else if("CNR".equals(compte)){
				listCompte = compteService.getListCompteChargeRecuperable(false);
			} else if("A".equals(compte)){
				listCompte = compteService.getListCompteByCode("47%");
			} else if("C".equals(compte)){
				listCompte = compteService.getListCompteByCode("450%");
			} else if("F".equals(compte)){
				listCompte = compteService.getListCompteByCode("40%");
			} else{
				listCompte = compteService.getListCompteByCode(null);
			}
		}
		
		// Compte list
	    httpUtil.setRequestAttribute("listCompte", listCompte);

	    // Liste des clés
		httpUtil.setRequestAttribute("list_type_compte", new String[][]{
				{"CP", "Charges et produits"},
				{"CR","Charges récupérables"},
				{"CNR","Charges non récupérables"},
				{"A","Comptes d'attente"},
				{"C","Comptes copropriétaires"},
				{"F","Comptes fournisseurs"}
			});
		
		httpUtil.setDynamicUrl("/domaine/compta/compte_list.jsp");
	}
	
	/**
	 * 
	 */
	public void work_delete(ActionUtil httpUtil) {
		Long compteId = httpUtil.getWorkIdLong();
		compteService.delete(compteId);
		
		work_find(httpUtil);
	}
	
	/**
	 * @param httpUtil
	 */
	public void work_post(ActionUtil httpUtil) {
	
	}
}
