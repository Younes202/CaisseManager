
package framework.component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

import framework.component.action.Link;
import framework.component.action.LinkPopup;
import framework.component.box.CheckBox;
import framework.component.box.RadioButton;
import framework.component.box.RadioGroup;
import framework.component.box.Select;
import framework.component.text.Date;
import framework.component.text.Hidden;
import framework.component.text.Text;
import framework.component.text.TextArea;
import framework.component.text.TextAreaRich;
import framework.component.work.LinkBase;
import framework.controller.ControllerUtil;
import framework.controller.bean.KeyLabelBean;
import framework.controller.bean.action.IViewBean;
import framework.model.common.constante.ActionConstante;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.constante.ProjectConstante.QUERY_CONDITIONS;
import framework.model.common.constante.ProjectConstante.TYPE_DATA_ENUM;
import framework.model.common.util.ControllerBeanUtil;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.StringUtil;

public class ComponentUtil {
	private final static Logger LOGGER = Logger.getLogger(ComponentUtil.class);
	/**
	 * @param script
	 * @return
	 */
	public static String getJavascriptOnReadyBloc(String script){
		return getJavascriptBloc(getOnReadyScript(script), null);
	}
	public static String getJavascriptOnReadyBloc(String script, String id){
		return getJavascriptBloc(getOnReadyScript(script), id);
	}

	/**
	 * @param script
	 * @return
	 */
	public static String getOnReadyScript(String script){
		return "\n$(document).ready(function(){\n"+script+"\n});";
	}

	/**
	 * @param script
	 * @return
	 */
	public static String getJavascriptBloc(StringBuilder script) {
		return "\n<script type='text/javascript'>\n" + script.toString() + "\n</script>\n";
	}

	public static String getJavascriptBloc(String script) {
		return getJavascriptBloc(script, null);
	}
	/**
	 * @param script
	 * @return
	 */
	public static String getJavascriptBloc(String script, String id) {
		id = (id != null ? " id=\""+id+"\"" : "");
		return "\n<script type='text/javascript' "+id+">\n" + script + "\n</script>\n";
	}

	/**
	 * @param component
	 * @return
	 */
	public static boolean isEditableField(IComponent component){
		return (component instanceof Text || component instanceof TextArea || component instanceof TextAreaRich
				||component instanceof Date || component instanceof Hidden);
	} 

	/**
	 * @param component
	 * @return
	 */
	public static boolean isBoxField(IComponent component){
		return (component instanceof CheckBox || component instanceof Select
				|| component instanceof RadioGroup || component instanceof RadioButton
				|| component instanceof Upload);
	}

	/**
	 * @param component
	 * @return
	 */
	public static boolean isLinkField(IComponent component){
		return (component instanceof Link || component instanceof LinkPopup || component instanceof LinkBase);
	}

	/**
	 * @param path
	 */
	public static void insertFragment(PageContext pageContext, String path) throws JspException{
		try {
			pageContext.include(path);
		} catch (ServletException e) {
			throw new JspException(e);
		} catch (IOException e) {
			throw new JspException(e);
		}
	}

	public static void writeComponent(PageContext pageContext, String sb) {
		try{
			pageContext.getOut().print(sb);
		} catch (IOException e) {
			LOGGER.error("Erreur : ", e);
		}
	}

	public static void writeComponent(PageContext pageContext, StringBuilder sb) {
		try{
			pageContext.getOut().print(sb.toString());
		} catch (IOException e) {
			LOGGER.error("Erreur : ", e);
		}
	}

	/**
	 * @param data
	 * @param key
	 * @param label
	 * @return
	 * @throws JspException
	 */
	public static List<KeyLabelBean> getArrayData(Object data, String key, String labels) throws JspException{
		List<KeyLabelBean> dataReturn = null;
		if(data != null){
			String[] labelsArray = StringUtil.getArrayFromStringDelim(labels, ";");
			dataReturn = new ArrayList<KeyLabelBean>();
			try{
				if(data instanceof String[][]){
					String[][] dataArray = (String[][])data;
					for(String[] keyLabel : dataArray){
						if(keyLabel.length > 1){
							dataReturn.add(new KeyLabelBean(keyLabel[0], keyLabel[1]));
						}
					}
				} else if(data instanceof List){
					List<?> dataArray = (List<?>) data;
					//
					for(Object bean : dataArray){
						String itemKey = ""+ReflectUtil.getStringPropertieValue(bean, key);
						String itemValue = "";
						if(labels != null){
							for (String label : labelsArray) {
								String attributeValue = "";
								if(label.startsWith("'")){
									attributeValue = label.substring(1, (label.length()-1));
								} else{
									attributeValue = ReflectUtil.getStringPropertieValue(bean, label);
								}
								itemValue = itemValue + StringUtil.getValueOrEmpty(attributeValue);
							}
						}
						//
						dataReturn.add(new KeyLabelBean(itemKey, itemValue));
					}
				} else if(data instanceof Set){
					Set<?> dataArray = (Set<?>) data;
					for(Object bean : dataArray){
						String itemKey = ""+ReflectUtil.getStringPropertieValue(bean, key);
						String itemValue = "";
						if(labels != null){
							for (String label : labelsArray) {
								String attributeValue = "";
								if(label.startsWith("'")){
									attributeValue = label.substring(1, (label.length()-1));
								} else{
									attributeValue = ReflectUtil.getStringPropertieValue(bean, label);
								}
								itemValue = itemValue + StringUtil.getValueOrEmpty(attributeValue);
							}
						}
						//
						dataReturn.add(new KeyLabelBean(itemKey, itemValue));
					}
				} else{
					throw new JspException("The select component accept List or Set or String[][] for items argument !");
				}
			} catch (Exception e) {
				throw new JspException(e);
			}
		}

		return dataReturn;
	}

	/**
	 * @param pattern
	 * @param fieldValue
	 * @return
	 */
	public static String getFormattedString(String fieldValue, String pattern) {
		if ("upper".equals(pattern)) {
			return fieldValue.toUpperCase();
		} else if ("lower".equals(pattern)) {
			return fieldValue.toLowerCase();
		} else if ("fupper".equals(pattern)) {
			return StringUtil.firstCharToUpperCase(fieldValue);
		} else {
			return fieldValue;
		}
	}

	/**
	 * @param id
	 * @param current
	 * @return
	 */
	public static final String getConditionsSelect (String id, Object current, String type){
		TYPE_DATA_ENUM typeData = ProjectConstante.TYPE_DATA_ENUM.getTypeData(type);
		String condition = null;
		// 
		switch(typeData){
		  case DATE : {
			  condition = "NUMERIC";
		  }; break;
		  case DATE_TIME : {
			  condition = "NUMERIC";
		  }; break;
		  case BOOLEAN : {
			  condition = "BOOLEAN"; 
		  }; break;
		  case DECIMAL : {
			  condition = "NUMERIC";
		  }; break;
		  case LONG : {
			  condition = "NUMERIC";
		  }; break;
		  case INTEGER : {
			  condition = "NUMERIC";
		  }; break;
		  case STRING : {
			  condition = "STRING";
		  }; break;
		  default: {
			  condition = "STRING";
		  }; break;
		}
		//
		StringBuilder sb = new StringBuilder();
		sb.append("<select class=\"select\" id=\""+id+"\" name=\""+id+"\" style=\"width:100%;padding-left:0px;padding-right:0px;\">");
		for(QUERY_CONDITIONS cd : QUERY_CONDITIONS.values()){
			if(cd.toString().startsWith(condition)){
				String selected = cd.getCode().equals(current) ? " selected=selected" : "";
				sb.append("<option value=\""+cd.getCode()+"\" "+selected + ">"+cd.getLibelle()+"</option>");
			}
		}
		sb.append("</select>");

		return sb.toString();
	}

	/**
	 * @param pageContext
	 * @return
	 */
	public static boolean isFormUseValidator(PageContext pageContext){
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		Map<String, Map<String, String>> mapValidators = ControllerUtil.getMapValidator(request);

		return ((mapValidators != null) && (mapValidators.size() > 0));
	}

	/**
	 * @param array
	 * @return
	 */
	public static String getJavaScriptArrayFromJavaArray(List<String> array){
		String stringArray = "";
		for(int i=0; i<array.size(); i++){
			stringArray = stringArray + "'" + array.get(i) + "'" + ((i < (array.size()-1)) ? "," : "");
		}

		return "[" + stringArray + "]";
	}

	/**
	 * Read only setted manually in action
	 * @param request
	 * @return
	 */
	public static boolean isReadOnlyFormSetted(HttpServletRequest request){
		return StringUtil.isTrue(""+request.getAttribute(ProjectConstante.IS_SET_READ_ONLY_FORM));
	}

	/**
	 * @param action
	 * @return
	 */
	public static boolean isReadOnlyAction(HttpServletRequest request){
		String action = ControllerUtil.getAction(request);
		//
		if(StringUtil.isEmpty(action)){
			action = ControllerUtil.getAction(request);
		}
		//
		if(StringUtil.isNotEmpty(action)){ 
			if (ActionConstante.EDIT.equals(action)
					|| ActionConstante.INIT.equals(action)
					){
				return true;
			}
		}

		return false;
	}

	/**
	 * @param request
	 * @return
	 */
	public static boolean isReadOnlyAttributeForm(HttpServletRequest request){
		return (request.getAttribute(ProjectConstante.IS_READ_ONLY_FORM) != null) || isReadOnlyFormSetted(request);
	}

	/**
	 * @param component
	 * @return
	 */
	public static String getLabelForKeyField(ComponentBase component){
		String defaultClass = "label";
		String valueSt = StringUtil.getValueOrEmpty(component.getValue());
		String label = "<label " + component.getFullClassStyle(defaultClass) + ">" + valueSt + "</label>\n" +
				"<input type='hidden' " + component.getFullName() + " value='" + valueSt + "'/>";

		return label;
	}

	/**
	 * @param value
	 * @return
	 */
	public static String getJQueryName(String value){
		return ComponentUtil.replaceAll(value, '.', "\\\\.");
	}

	/**
	 * @param component
	 * @param request
	 * @return
	 * @throws JspTagException
	 */
	public static String getFormAction(HttpServletRequest request) {
		String formAction = (String)request.getAttribute(ProjectConstante.WORK_FORM_ACTION);

		return formAction;
	}

	/**
	 * @param request
	 * @return
	 */
	public static boolean isBodyTableAction(PageContext pageContext){
		HttpServletRequest request = ((HttpServletRequest) pageContext.getRequest());
		return ControllerUtil.isBodyTableAction(request);
	}

	/**
	 * @param request
	 * @return
	 */
	public static String getCurrentAjaxTable(PageContext pageContext){
		HttpServletRequest request = ((HttpServletRequest) pageContext.getRequest());
		return ControllerUtil.getParam(request, ActionConstante.BODY_TABLE);
	}

	/**
	 * @param str
	 * @param origine
	 * @param target
	 * @return
	 */
	public static String replaceAll(String str, char origine, String target) {
		if(str != null){
			char[] charArray = str.toCharArray();
			if(str.indexOf(".") != -1){
				StringBuilder sb = new StringBuilder();
				for(char c : charArray){
					if(c == origine){
						sb.append(target);
					} else{
						sb.append(c);
					}
				}
				return sb.toString();
			}
		}
		return str;
	}

	/**
	 * Search the value from beanView at first, if it does n't exist
	 * then serch from params map
	 */
	public static String getValueFromBeanOrParamsMap(HttpServletRequest request, String name){
		boolean fromMap = false;
		Object value = null;
		// First valuate from bean view
		String beanName = (String)request.getAttribute("work_bean_name");
		if(beanName != null){
			IViewBean viewBean = (IViewBean)request.getAttribute(beanName);
			if(viewBean != null){
				try {
					String aliasBean = ControllerBeanUtil.getAliasBeanByObject(viewBean) + ".";
					int idxAlias = name.indexOf(aliasBean);
					String shortName = name.substring(idxAlias+aliasBean.length());
					value = PropertyUtils.getProperty(viewBean, shortName);
				} catch (Exception e) {
					fromMap = true;
				}
			}
		} else{
			fromMap = true;
		}
		// Load value from map
		if(fromMap){
			Map params = (Map)request.getAttribute(ProjectConstante.WORK_PARAMS);
			if(params != null){
				value = params.get(name);
			}
		}

		return StringUtil.getValueOrEmpty(value);
	}

	/**
	 * @param component
	 * @return
	 */
	public static String getImageAlign(Component component){
		String align = "";
		boolean isReadOnly = ComponentUtil.isReadOnlyAttributeForm(component.getGuiOrContextHttpRequest());
		if((component instanceof Select) && !isReadOnly){
			align = "vertical-align:super;";
		} else{
			align = "vertical-align:middle;";
		}

		return align;
	}

}
