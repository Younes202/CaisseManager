package appli.controller.domaine.fidelite.action;

import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.inject.Inject;

import org.hibernate.criterion.Order;

import appli.controller.domaine.fidelite.bean.CarteFideliteClientBean;
import appli.model.domaine.administration.service.IClientService;
import appli.model.domaine.fidelite.persistant.CarteFideliteClientPersistant;
import appli.model.domaine.fidelite.persistant.CarteFideliteConsoPersistant;
import appli.model.domaine.fidelite.persistant.CarteFidelitePointsPersistant;
import appli.model.domaine.fidelite.service.ICarteFideliteClientService;
import appli.model.domaine.fidelite.service.ICarteFideliteService;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.StringUtil;

@WorkController(nameSpace = "fidelite", bean=CarteFideliteClientBean.class, jspRootPath = "/domaine/fidelite/")
public class CarteFideliteClientAction extends ActionBase {

	@Inject
	private ICarteFideliteClientService carteFideliteClientService;
	@Inject
	private IClientService clientService;
	@Inject
	private ICarteFideliteService carteFideliteService;

	public void work_init(ActionUtil httpUtil) {
		httpUtil.setRequestAttribute("liste_carte", carteFideliteService.findAll(Order.asc("libelle")));
		httpUtil.setRequestAttribute("liste_client", clientService.findAll(Order.asc("nom"), Order.asc("prenom")));
		httpUtil.setUserAttribute("MNU_FIDELITE", "POINT");
	}
	
	@Override
	public void work_edit(ActionUtil httpUtil) {
		if(httpUtil.getWorkIdLong() != null){
			httpUtil.setMenuAttribute("carteClientId", httpUtil.getWorkIdLong());
		}
		Long carteClientId = (Long) httpUtil.getMenuAttribute("carteClientId");
		
		if(carteClientId != null){
			httpUtil.setViewBean(carteFideliteClientService.findById(carteClientId));
		}
		//
		httpUtil.setDynamicUrl("/domaine/fidelite/carteFideliteClient_edit.jsp");
	}
	
	@Override
	public void work_merge(ActionUtil httpUtil) {
		CarteFideliteClientBean cfB = (CarteFideliteClientBean) httpUtil.getViewBean();
		
		if(StringUtil.isEmpty(cfB.getCode_barre())) {
			String tm = (""+System.currentTimeMillis());
			String codeBarre = tm.substring(tm.length()-4)+"_"+new Random(1000).nextInt();
			cfB.setCode_barre(codeBarre);
		}
		
		super.work_merge(httpUtil);
	}
	
	@Override
	public void work_find(ActionUtil httpUtil) {
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_carteFideliteClient");
		Long carteId = (Long) httpUtil.getMenuAttribute("carteId");
		cplxTable.getFormBean().getFormCriterion().put("carteId", carteId);
		
		List<CarteFideliteClientPersistant> listData = (List<CarteFideliteClientPersistant>) carteFideliteClientService.findByCriteriaByQueryId(cplxTable, "carteFideliteClient_find");
		httpUtil.setRequestAttribute("list_carteFideliteClient", listData);
		
		httpUtil.setDynamicUrl("/domaine/fidelite/carteFideliteClient_list.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void findPointsGain(ActionUtil httpUtil){
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_points_carte");
		Map<String, Object> formCriterion = cplxTable.getFormBean().getFormCriterion();
		formCriterion.put("carteClientId", httpUtil.getMenuAttribute("carteClientId"));

		List<CarteFidelitePointsPersistant> listData = (List<CarteFidelitePointsPersistant>) carteFideliteClientService.findByCriteriaByQueryId(cplxTable, "clientCartePoint_find");
		httpUtil.setRequestAttribute("list_client_carte", listData);
		
		httpUtil.setDynamicUrl("/domaine/fidelite/clientCarteFidelite_point_list.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void findPointsConso(ActionUtil httpUtil){
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_points_carte_conso");
		Map<String, Object> formCriterion = cplxTable.getFormBean().getFormCriterion();
		formCriterion.put("carteClientId", httpUtil.getMenuAttribute("carteClientId"));
		
		List<CarteFideliteConsoPersistant> listData = (List<CarteFideliteConsoPersistant>) carteFideliteClientService.findByCriteriaByQueryId(cplxTable, "clientCarteConso_find");
		httpUtil.setRequestAttribute("list_points_carte", listData);
		
		httpUtil.setDynamicUrl("/domaine/fidelite/clientCarteFidelite_conso_list.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void initOffre(ActionUtil httpUtil){
		httpUtil.setDynamicUrl("/domaine/fidelite/clientCarteFidelite_offre_edit.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void offrirPoints(ActionUtil httpUtil){
		Long carteClientId = (Long)httpUtil.getMenuAttribute("carteClientId");
		
		if(BigDecimalUtil.isZero(BigDecimalUtil.get(httpUtil.getParameter("carteFidelite.points")))){
			MessageService.addFieldMessage("carteFidelite.points", "Champs obligatoire");
			return;
		}
		
		Integer nbrPoint = Integer.valueOf(httpUtil.getParameter("carteFidelite.points"));
		//
		carteFideliteClientService.offrirPoints(carteClientId, nbrPoint);
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "", "Les points sont ajoutés.");
		//
		findPointsGain(httpUtil);
	}
	
	/**
	 * @param httpUtil
	 */
	public void deletePoints(ActionUtil httpUtil){
		Long elementId = httpUtil.getWorkIdLong();
		String tp = httpUtil.getParameter("tp");
		//
		carteFideliteClientService.deletePointsGagnes(elementId);
		//
		findPointsGain(httpUtil);
	}
	
	/**
	 * @param httpUtil
	 */
	public void desactiver(ActionUtil httpUtil) {
		carteFideliteClientService.activerDesactiverElement(httpUtil.getWorkIdLong());
		work_find(httpUtil);
	}
}
