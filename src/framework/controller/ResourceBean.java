package framework.controller;

import framework.controller.bean.action.IViewBean;
import framework.model.service.IGenericJpaService;

public class ResourceBean {
	
	private Class<? extends IViewBean> viewBean;
	private IGenericJpaService service;

	/**
	 * @param service
	 * @param viewBean
	 * @param tableId
	 */
	public ResourceBean(IGenericJpaService service, Class<? extends IViewBean> viewBean){
		this.viewBean = viewBean;
		this.service = service;
	}

	/**
	 * @return the viewBean
	 */
	public Class<? extends IViewBean> getViewBean() {
		return viewBean;
	}

	/**
	 * @param viewBean the viewBean to set
	 */
	public void setViewBean(Class<IViewBean> viewBean) {
		this.viewBean = viewBean;
	}

	/**
	 * @return the service
	 */
	public IGenericJpaService getService() {
		return service;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(IGenericJpaService service) {
		this.service = service;
	}
}
