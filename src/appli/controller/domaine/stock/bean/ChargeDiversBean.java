package appli.controller.domaine.stock.bean;

import appli.model.domaine.stock.persistant.ChargeDiversPersistant;
import framework.controller.bean.action.IViewBean;

public class ChargeDiversBean extends ChargeDiversPersistant implements IViewBean {
	private Long[] mouvementIds;
	private Long[] avoirIds;
	private String num_cheque_f;
	
	public String getNum_cheque_f() {
		return num_cheque_f;
	}

	public void setNum_cheque_f(String num_cheque_f) {
		this.num_cheque_f = num_cheque_f;
	}

	public Long[] getMouvementIds() {
		return mouvementIds;
	}

	public void setMouvementIds(Long[] mouvementIds) {
		this.mouvementIds = mouvementIds;
	}

	public Long[] getAvoirIds() {
		return avoirIds;
	}

	public void setAvoirIds(Long[] avoirIds) {
		this.avoirIds = avoirIds;
	}
}
