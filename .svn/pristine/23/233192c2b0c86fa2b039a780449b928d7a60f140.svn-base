package appli.controller.domaine.administration.action;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.criterion.Order;

import appli.controller.domaine.administration.bean.CompteBancaireBean;
import appli.model.domaine.administration.service.ITypeEnumService;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.beanContext.DataFormPersistant;
import framework.model.common.service.MessageService;

@WorkController(nameSpace = "admin", bean = CompteBancaireBean.class, jspRootPath = "/domaine/administration/")
public class DataFormAction extends ActionBase {
	@Inject
	private ITypeEnumService typeEnumService;
	
	/**
	 * @param httpUtil
	 */
	public void work_init(ActionUtil httpUtil){
		httpUtil.setRequestAttribute("listEnum", typeEnumService.findAll(Order.asc("libelle")));
		httpUtil.setRequestAttribute("typeArray", new String[][] {
			{"STRING", "Caractères"}, 
			{"LONG", "Numérique entier"}, 
			{"DECIMAL", "Numérique décimal"}, 
			{"DATE", "Date"}, 
			{"ENUM", "Enumération"}, 
			{"BOOLEAN", "Case à cocher"},
			{"TEXT", "Text"},
			{"TITRE", "Titre bloc"},
		});
		httpUtil.setRequestAttribute("blocArray", new String[][] {
			{"12", "1 Colonne"}, 
			{"6", "2 Colonnes"},
			{"4", "3 Colonnes"}
		});
		
		if(httpUtil.getParameter("tp") != null){
			httpUtil.setMenuAttribute("tp", httpUtil.getParameter("tp"));
		}
		httpUtil.setRequestAttribute("dataGroup", httpUtil.getMenuAttribute("tp"));
	}
	
	@Override
	public void work_find(ActionUtil httpUtil) {
		String groupe = (String) httpUtil.getMenuAttribute("tp");
		List<DataFormPersistant> listDataForm = typeEnumService.findAllForm(groupe);
		
		httpUtil.setRequestAttribute("list_data", listDataForm);
		
		httpUtil.setDynamicUrl("/domaine/administration/dataForm_list.jsp");
	}
	
	@Override
	public void work_merge(ActionUtil httpUtil) {
		List<DataFormPersistant> listDataForm = (List<DataFormPersistant>) httpUtil.buildListBeanFromMap("data_label", DataFormPersistant.class, 
				"data_label", "eaiid", "data_enum", "data_code", "data_type", "max_length", "data_style", "is_required", "data_group");
		
		if(listDataForm.size() == 0){
			MessageService.addBannerMessage("Veuillez saisir au moins une valeur.");
			return;
		}
		
		typeEnumService.mergeDataForm(listDataForm);
		
		httpUtil.writeResponse("MSG_CUSTOM:Le formulaire est mise à jour.");
	}
}
