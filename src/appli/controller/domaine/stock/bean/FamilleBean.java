package appli.controller.domaine.stock.bean;

import java.math.BigDecimal;

import appli.model.domaine.stock.persistant.FamillePersistant;
import framework.controller.bean.action.IViewBean;

public class FamilleBean extends FamillePersistant implements IViewBean {
	private Long element_id;
	private Long parent_id;
	// CU -----
	public String[] caisses_target;
	// END CU ----
	
	// SP -----
	private Integer durree;// En minute
	private String consigne_avant;
	private String consigne_apres;
	private BigDecimal mtt_tarif;
	// END SP ------
	
	public Long getElement_id() {
		return element_id;
	}

	public void setElement_id(Long element_id) {
		this.element_id = element_id;
	}

	public Long getParent_id() {
		return parent_id;
	}

	public void setParent_id(Long parent_id) {
		this.parent_id = parent_id;
	}

	public String[] getCaisses_target() {
		return caisses_target;
	}

	public void setCaisses_target(String[] caisses_target) {
		this.caisses_target = caisses_target;
	}

	public Integer getDurree() {
		return durree;
	}

	public void setDurree(Integer durree) {
		this.durree = durree;
	}

	public String getConsigne_avant() {
		return consigne_avant;
	}

	public void setConsigne_avant(String consigne_avant) {
		this.consigne_avant = consigne_avant;
	}

	public String getConsigne_apres() {
		return consigne_apres;
	}

	public void setConsigne_apres(String consigne_apres) {
		this.consigne_apres = consigne_apres;
	}

	public BigDecimal getMtt_tarif() {
		return mtt_tarif;
	}

	public void setMtt_tarif(BigDecimal mtt_tarif) {
		this.mtt_tarif = mtt_tarif;
	}
}
