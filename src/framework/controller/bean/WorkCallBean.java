package framework.controller.bean;

import framework.controller.bean.action.IViewBean;


public class WorkCallBean {
	private boolean isCalled;
	private IViewBean viewBean;
	
	/**
	 * @return the isCalled
	 */
	public boolean isCalled() {
		return isCalled;
	}
	/**
	 * @param isCalled the isCalled to set
	 */
	public void setCalled(boolean isCalled) {
		this.isCalled = isCalled;
	}
	/**
	 * @return the viewBean
	 */
	public IViewBean getViewBean() {
		return viewBean;
	}
	/**
	 * @param viewBean the viewBean to set
	 */
	public void setViewBean(IViewBean viewBean) {
		this.viewBean = viewBean;
	}
}
