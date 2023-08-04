package framework.controller.bean;

public class MenuBean {
	private String id;
	private String compositId;
	private String linkText;
	private String pageTitle;
	private String linkTitle;
	private String url;
	private String icon;
	private String linkHelp;
	private String params;
	private boolean isVisible;
	private boolean isSheet;
	
	private int level;
	private String[] additionalRrights;
	private String[] excludedRrights;

	public MenuBean(String id, String linkText, String icon, int level){
		this.id = id;
		this.icon = icon;
		this.linkText = linkText;
		this.level = level;
		this.isVisible = true;
		this.isSheet = false;
	}
	public MenuBean(String id, String url, boolean isVisible, int level){
		this.id = id;
		this.url = url;
		this.isVisible = isVisible;
		this.level = level;
		this.isSheet = false;
	}
	public MenuBean(String id, String linkText, String linkTitle, String url, String icon, String params, int level){
		this.id = id;
		this.linkText = linkText;
		this.linkTitle = linkTitle;
		this.url = url;
		this.icon = icon;
		this.params = params;
		this.level = level;
		this.isSheet = true;
		this.isVisible = true;
	}
	public MenuBean(String id, String linkText, String linkTitle, String url, String icon, String params, 
			String[] additionalRrights, 
			String[] excludedRrights, int level){
		this.id = id;
		this.linkText = linkText;
		this.linkTitle = linkTitle;
		this.url = url;
		this.icon = icon;
		this.params = params;
		this.additionalRrights = additionalRrights;
		this.excludedRrights = excludedRrights;
		this.level = level;
		this.isSheet = true;
		this.isVisible = true;
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the linkText
	 */
	public String getLinkText() {
		return linkText;
	}

	/**
	 * @param linkText
	 *            the linkText to set
	 */
	public void setLinkText(String linkText) {
		this.linkText = linkText;
	}

	/**
	 * @return the pageTitle
	 */
	public String getPageTitle() {
		return pageTitle;
	}

	/**
	 * @param pageTitle
	 *            the pageTitle to set
	 */
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the params
	 */
	public String getParams() {
		return params;
	}

	/**
	 * @param params
	 *            the params to set
	 */
	public void setParams(String params) {
		this.params = params;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level
	 *            the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * @return the linkTitle
	 */
	public String getLinkTitle() {
		return linkTitle;
	}

	/**
	 * @param linkTitle
	 *            the linkTitle to set
	 */
	public void setLinkTitle(String linkTitle) {
		this.linkTitle = linkTitle;
	}

	public String getLinkHelp() {
		return linkHelp;
	}

	public void setLinkHelp(String linkHelp) {
		this.linkHelp = linkHelp;
	}

	/**
	 * @return the compositId
	 */
	public String getCompositId() {
		return compositId;
	}

	/**
	 * @param compositId
	 *            the compositId to set
	 */
	public void setCompositId(String compositId) {
		this.compositId = compositId;
	}

	public String[] getAdditionalRrights() {
		return additionalRrights;
	}

	public void setAdditionalRrights(String[] additionalRrights) {
		this.additionalRrights = additionalRrights;
	}

	public String[] getExcludedRrights() {
		return excludedRrights;
	}

	public void setExcludedRrights(String[] excludedRrights) {
		this.excludedRrights = excludedRrights;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public boolean getIsVisible() {
		return isVisible;
	}

	public void setIsVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	public boolean isSheet() {
		return isSheet;
	}
	public void setSheet(boolean isSheet) {
		this.isSheet = isSheet;
	}
}
