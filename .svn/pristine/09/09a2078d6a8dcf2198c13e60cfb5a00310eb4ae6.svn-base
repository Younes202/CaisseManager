package framework.controller.bean.action;


public class ActionBean {
	private String action;
	private String jsp_path;
	private Boolean isUseBean;
	private Boolean useModelValidator;
	private Boolean useFormValidator = true;
	private Class bean;

	/**
	 *
	 */
	public ActionBean(){

	}

	/**
	 * @param actionBean
	 */
	public ActionBean(ActionBean actionBean){
		this.action = actionBean.getAction();
		this.isUseBean = actionBean.isUseBean();
		this.useModelValidator = actionBean.isUseModelValidator();
		this.useFormValidator = actionBean.isUseFormValidator();
		this.bean = actionBean.getBean();
	}

	/**
	 * @return the useModelValidator
	 */
	public Boolean isUseModelValidator() {
		return useModelValidator;
	}

	/**
	 * @param useModelValidator the useModelValidator to set
	 */
	public void setUseModelValidator(Boolean useModelValidator) {
		this.useModelValidator = useModelValidator;
	}

	/**
	 * @return the useFormValidator
	 */
	public Boolean isUseFormValidator() {
		return useFormValidator;
	}

	/**
	 * @param useFormValidator the useFormValidator to set
	 */
	public void setUseFormValidator(Boolean useFormValidator) {
		this.useFormValidator = useFormValidator;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the jsp_path
	 */
	public String getJsp_path() {
		return jsp_path;
	}

	/**
	 * @param jsp_path
	 *            the jsp_path to set
	 */
	public void setJsp_path(String jsp_path) {
		this.jsp_path = jsp_path;
	}

	/**
	 * @return the isUseBean
	 */
	public Boolean isUseBean() {
		return isUseBean;
	}

	/**
	 * @param isUseBean
	 *            the isUseBean to set
	 */
	public void setUseBean(Boolean isUseBean) {
		this.isUseBean = isUseBean;
	}

	public Class getBean() {
		return bean;
	}

	public void setBean(Class bean) {
		this.bean = bean;
	}
}
