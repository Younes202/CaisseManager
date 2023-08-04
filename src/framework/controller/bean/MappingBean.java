package framework.controller.bean;

import java.util.List;

import framework.controller.bean.action.ActionBean;
import framework.controller.bean.action.IViewBean;

public class MappingBean {
	private Class<?> controller;
	private String nameSpace;
	private String alias;
	private String jspRootPath;
	private Class<? extends IViewBean> bean;
	private List<ActionBean> listAction;

	
	public String getJspRootPath() {
		return jspRootPath;
	}

	public void setJspRootPath(String jspRootPath) {
		this.jspRootPath = jspRootPath;
	}

	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias
	 *            the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * @return the listCaseAction
	 */
	public List<ActionBean> getListAction() {
		return listAction;
	}

	/**
	 * @param listCaseAction
	 *            the listCaseAction to set
	 */
	public void setListAction(List<ActionBean> listAction) {
		this.listAction = listAction;
	}

	public Class<? extends IViewBean> getBean() {
		return bean;
	}

	public void setBean(Class<? extends IViewBean> bean) {
		this.bean = bean;
	}

	public Class<?> getController() {
		return controller;
	}

	public void setController(Class<?> controller) {
		this.controller = controller;
	}

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

}
