package appli.controller.domaine.compta.bean;

import java.util.Map;

import framework.controller.bean.action.IViewBean;
import framework.model.beanContext.ComptePersistant;

public class CompteBean extends ComptePersistant implements IViewBean {
	private Long element_id;
	private Long parent_id;
	private Map<String, Object> mapCompteCredit;
	private Map<String, Object> mapCompteDebit;
	
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
	
	public void setMapCompteDebit(Map<String, Object> mapCompteDebit) {
		this.mapCompteDebit = mapCompteDebit;
	}
	public void setMapCompteCredit(Map<String, Object> mapCompteCredit) {
		this.mapCompteCredit = mapCompteCredit;
	}
	public Map<String, Object> getMapCompteCredit() {
		return mapCompteCredit;
	}
	public Map<String, Object> getMapCompteDebit() {
		return mapCompteDebit;
	}
}
