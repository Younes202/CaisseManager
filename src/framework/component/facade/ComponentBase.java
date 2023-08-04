package framework.component.facade;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import framework.controller.ControllerUtil;
import framework.model.common.constante.ProjectConstante;

public class ComponentBase implements UI_IComponent {
	
	private String name;
	private HttpServletRequest requestGui;
	
	/**
	 * @param requestGui
	 */
	public void setRequestGui(HttpServletRequest requestGui) {
		this.requestGui = requestGui;
	}
	
	public HttpServletRequest getRequestGui() {
		return this.requestGui;
	}
	
	public void setUIName(String name){
		this.name = name;
	}

	/**
	 * @param methodeName
	 * @param value
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void manageGuiComponent(String methodeName, Object value){
			Map<String, Map<String, Object>> componentMap = (Map<String, Map<String, Object>>)ControllerUtil.getMenuAttribute(ProjectConstante.MAP_COMPONENT, requestGui);
			if(componentMap == null){
				componentMap = new LinkedHashMap<String, Map<String,Object>>();
				ControllerUtil.setMenuAttribute(ProjectConstante.MAP_COMPONENT, componentMap, requestGui);
			}
			//
			Map<String, Object> methodesMap = componentMap.get(this.name);
			List listMethodeValues = new ArrayList();
			//
			if(methodesMap == null){
				methodesMap = new LinkedHashMap<String, Object>();
				componentMap.put(this.name, methodesMap);
			}
			//
			if(methodesMap.get(methodeName) != null){
				((List)methodesMap.get(methodeName)).add(value);
			} else{
				listMethodeValues.add(value);
				methodesMap.put(methodeName, listMethodeValues);
			}
		}
}
