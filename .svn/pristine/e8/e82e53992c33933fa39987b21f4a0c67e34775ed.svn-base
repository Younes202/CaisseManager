package appli.controller.domaine.caisse.action;

import java.math.BigDecimal;

import javax.inject.Inject;

import appli.controller.domaine.stock.bean.ArticleBean;
import appli.model.domaine.fidelite.dao.IPortefeuille2Service;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.printGen.PrintPosBean;

@WorkController(nameSpace = "fidelite", bean = ArticleBean.class, jspRootPath = "/domaine/fidelite/")
public class Portefeuille2Action extends ActionBase {
	@Inject
	private IPortefeuille2Service portefeuilleService2;
	
	public void save_recharge(ActionUtil httpUtil) {
		String mode_paie = httpUtil.getParameter("mode_paie");
		Long clientId = (Long)httpUtil.getMenuAttribute("clientId");
		BigDecimal montant = BigDecimalUtil.get(httpUtil.getParameter("mtt_recharge"));
		
		if(StringUtil.isEmpty(mode_paie)){
			MessageService.addFieldMessage("mode_paie", "Champs obligatoire");
			return;
		}
		if(BigDecimalUtil.isZero(montant)){
			MessageService.addFieldMessage("mtt_recharge", "Champs obligatoire");
			return;
		}
		
		//
		PrintPosBean pu = portefeuilleService2.ajouterRecharge(clientId, montant, mode_paie, false, "CLI");
		
		if(pu != null){
			printData(httpUtil, pu, true);
		}
		//
		httpUtil.setDynamicUrl("fidelite.portefeuille.find_recharge");
	}
}
