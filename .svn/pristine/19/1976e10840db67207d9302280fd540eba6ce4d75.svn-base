package framework.controller;

import framework.component.facade.UI_IComponent;
import framework.component.facade.UI_Tooltip;
import framework.component.facade.UI_Upload;
import framework.component.facade.action.UI_Button;
import framework.component.facade.action.UI_Link;
import framework.component.facade.action.UI_LinkImage;
import framework.component.facade.box.UI_CheckBox;
import framework.component.facade.box.UI_RadioButton;
import framework.component.facade.box.UI_RadioGroup;
import framework.component.facade.box.UI_Select;
import framework.component.facade.complex.table.UI_BannerTableAction;
import framework.component.facade.complex.table.UI_Table;
import framework.component.facade.text.UI_Date;
import framework.component.facade.text.UI_Hidden;
import framework.component.facade.text.UI_Label;
import framework.component.facade.text.UI_Text;
import framework.component.facade.text.UI_TextArea;
import framework.component.facade.text.UI_TextAreaRich;
import framework.model.common.constante.ActionConstante;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.util.StringUtil;

public class ActionUtil extends HttpUtil {

	public boolean isEditionPage(){
		return isEditionPage(new String[]{});
	}
	
	/**
	 * @return
	 */
	public boolean isEditionPage(String ... actionToInclude){
		return ControllerUtil.isEditionPage(this.getRequest(), actionToInclude);
	}

	/**
	 * @return
	 */
	public boolean isResultPage(){
		return ControllerUtil.isResultPage(this.getRequest());
	}

	/**
	 * @return
	 */
	public boolean isCreateAction(){
		String action = ControllerUtil.getAction(getRequest());
		
		return ((ActionConstante.CREATE.equals(action) && isError())
		|| ActionConstante.INIT_CREATE.equals(action)
		|| ActionConstante.INIT_DUPLIC.equals(action));
	}

	/**
	 * Set read only form
	 */
	public void setFormReadOnly(){
		this.getRequest().setAttribute(ProjectConstante.IS_SET_READ_ONLY_FORM, "true");
	}
	public void setFormReadOnly(boolean readOnly){
		this.getRequest().setAttribute(ProjectConstante.IS_SET_READ_ONLY_FORM, ""+readOnly);
	}

	/**
	 * @param tableName
	 * @return
	 */
	public String[] getRowsOrder(String tableName){
		String hiddenOrderId = tableName+"_work_order";
		String hiddenOrderValue = ControllerUtil.getParam(getRequest(), hiddenOrderId);

		return ((hiddenOrderValue != null) ? StringUtil.getArrayFromStringDelim(hiddenOrderValue) : null);
	}

	/**
	 * @param script
	 */
	public void addJavaScript(String script){
		// Concat if exist
		String jsAddes = (String)getRequestAttribute("PAGE_JS");
		if(jsAddes != null){
			setRequestAttribute("PAGE_JS", jsAddes + "\n" + script);
		} else{
			setRequestAttribute("PAGE_JS", script);
		}
	}
	
//	/**
//	 * @param script
//	 */
//	public void writeResponse(String value){
//		setDynamicUrl("MSG_RES");
//		try {
//			getResponse().getWriter().write(value);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	//----------------Manage Component---------------------
	private UI_IComponent getUI_Component(UI_IComponent component, String name){
		component.setUIName(name);
		component.setRequestGui(getRequest());

		return component;
	}
	/*-------------- Standard components ----------------------*/
	public UI_Text getText(String name) {
		return (UI_Text)getUI_Component(new UI_Text(), name);
	}
	public UI_RadioButton getRadio(String name) {
		return (UI_RadioButton)getUI_Component(new UI_RadioButton(), name);
	}
	public UI_RadioGroup getRadioGroup(String name) {
		return (UI_RadioGroup)getUI_Component(new UI_RadioGroup(), name);
	}

	public UI_Hidden getHidden(String name) {
		return (UI_Hidden)getUI_Component(new UI_Hidden(), name);
	}
	public UI_Select getSelect(String name) {
		return (UI_Select)getUI_Component(new UI_Select(), name);
	}
	public UI_CheckBox getCheckBox(String name) {
		return (UI_CheckBox)getUI_Component(new UI_CheckBox(), name);
	}
	public UI_Link getLink(String name) {
		return (UI_Link)getUI_Component(new UI_Link(), name);
	}
	public UI_Button getButton(String name) {
		return (UI_Button)getUI_Component(new UI_Button(), name);
	}
	public UI_LinkImage getLinkImage(String name) {
		return (UI_LinkImage)getUI_Component(new UI_LinkImage(), name);
	}
	public UI_Date getDate(String name) {
		return (UI_Date)getUI_Component(new UI_Date(), name);
	}
	public UI_Label getLabel(String name) {
		return (UI_Label)getUI_Component(new UI_Label(), name);
	}
	public UI_Upload getUpload(String name) {
		return (UI_Upload)getUI_Component(new UI_Upload(), name);
	}
	public UI_TextArea getTextArea(String name) {
		return (UI_TextArea)getUI_Component(new UI_TextArea(), name);
	}
	public UI_TextAreaRich getTextAreaRich(String name) {
		return (UI_TextAreaRich)getUI_Component(new UI_TextAreaRich(), name);
	}
	public UI_Tooltip getTooltip(String name) {
		return (UI_Tooltip)getUI_Component(new UI_Tooltip(), name);
	}
	public UI_Table getTable(String name) {
		return (UI_Table)getUI_Component(new UI_Table(), name);
	}
	public UI_BannerTableAction getBannerTableAction() {
		return (UI_BannerTableAction)getUI_Component(new UI_BannerTableAction(), "banner_action");
	}
	public String getAction() {
		return ControllerUtil.getAction(getRequest());
	}
}
