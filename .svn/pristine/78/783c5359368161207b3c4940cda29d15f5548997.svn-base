package appli.model.domaine.personnel.validator;

import java.math.BigDecimal;

import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.personnel.bean.FraisBean;
import appli.model.domaine.personnel.dao.IFraisDao;
import appli.model.domaine.personnel.persistant.FraisDetailPersistant;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;

@Named
public class FraisValidator {
	@Inject
	IFraisDao fraisDao;
	
	/* (non-Javadoc)
	 * @see model.service.commun.validator.ActionValidator#validate(java.lang.Object, java.lang.String)
	 */
	public void updateCreateValidator(FraisBean fraisBean) {
		if(fraisBean.getList_detail() == null || fraisBean.getList_detail().size() == 0){
 		    MessageService.addBannerMessage("Veuillez saisir au moins une ligne de détail.");
 		} else{
 		    BigDecimal mttTotalDetail = null;
 		    for(FraisDetailPersistant detail : fraisBean.getList_detail()){
 		    	mttTotalDetail = BigDecimalUtil.add(mttTotalDetail, detail.getMontant());
 		    }
 		    if(mttTotalDetail.compareTo(fraisBean.getMtt_total()) != 0){
 		    	MessageService.addBannerMessage("Le total des détails ne correspond pas au montant total ("+BigDecimalUtil.formatNumber(mttTotalDetail)+"/"+BigDecimalUtil.formatNumber(fraisBean.getMtt_total())+").");
 		    }
 		}
	}
	
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void update(FraisBean fraisBean) {
		updateCreateValidator(fraisBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void create(FraisBean fraisBean){
		updateCreateValidator(fraisBean);
	}
}
