package framework.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.commons.beanutils.PropertyUtils;

import framework.component.action.Button;
import framework.component.box.RadioButton;
import framework.controller.ControllerUtil;
import framework.controller.bean.action.IViewBean;
import framework.model.common.constante.ActionConstante;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.service.MessageService;
import framework.model.common.util.ControllerBeanUtil;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.StringUtil;

@SuppressWarnings("serial")
public abstract class ComponentBase extends Component {
	// Abstract methods
	public abstract void doBeforStartComponent() throws JspException; 
	public abstract void writeStartComponent() throws JspException;
	public abstract void writeEndComponent() throws JspException;
	public abstract void doAfterEndComponent() throws JspException;
	public abstract void releaseAll();

	private boolean isEditableField;
	private boolean isBoxField;

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest request = getGuiOrContextHttpRequest();
		isEditableField = ComponentUtil.isEditableField(this);
		isBoxField = ComponentUtil.isBoxField(this);

		// Befor start write
		doBeforStartComponent();

		// Valuate from bean or map
		valuateFromBeanOrParamsMap(request);

		// Valuate field from gui actions
		valuateFromGui(request);
		//
		if(StringUtil.isTrueOrNull(getVisible())){
			addRequiredImg(request);
			
			// Valuate field from map and gui action
			prepareAndWriteComponent(request);
			//
			return EVAL_BODY_INCLUDE;
		} else{
			return SKIP_BODY;
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		HttpServletRequest request = getGuiOrContextHttpRequest();
		//
		if(StringUtil.isTrueOrNull(getVisible())){
			// Valuate field from gui actions
			valuateFromGui(request);
			// Write end component
			writeEndComponent();
			// After End
			doAfterEndComponent();
		}
		//
		super.release();
		releaseAll();
		//
		isEditableField = false;
		isBoxField = false;

		return EVAL_PAGE;
	}

	/**
	 * Get value from request
	 *
	 * @param key
	 * @return
	 */
	protected Object getAttribute(String key) {
		return getGuiOrContextHttpRequest().getAttribute(key);
	}

	/**
	 * @throws JspException
	 *
	 */
	protected void prepareAndWriteComponent(HttpServletRequest request) throws JspException{
		// Disable or enable fields
		disableComponentIfReadOnly(request);
		// add elements to map validators
		buildMapValidators(request);
		//
		writeStartComponent();
	}

	/**
	 * @param request
	 */
	private void disableComponentIfReadOnly(HttpServletRequest request){
		if(StringUtil.isTrue(super.getReadOnly()) || StringUtil.isTrue(super.getDisable())){
			disableComponent(true);
		}
	}

	/**
	 *
	 */
	private void disableComponent(boolean state) {
		boolean isReadStyle = false;
		//
		if(state){
			if(isBoxField){
				this.setDisable(""+state);
				isReadStyle = true;
			} else if (isEditableField){
				this.setReadOnly(""+state);
				isReadStyle = true;
			} else if(this instanceof Button){
				this.setVisible(""+(!state));
				isReadStyle = true;
			}
			// Change style
			if(isReadStyle){
				appendStyle("background-color:#eeeeee;");
			}
		}
	}

	/**
	 * Get value from component stocked in session map
	 * @throws JspException
	 */
	@SuppressWarnings("unchecked")
	private void valuateFromGui(HttpServletRequest request) throws JspException {
		if(getName() != null){
			Map<String, Map<String, Object>> componentMap = (Map<String, Map<String, Object>>)ControllerUtil.getMenuAttribute(ProjectConstante.MAP_COMPONENT, request);
			if(componentMap != null){
				Map<String, Object> methodesMap = componentMap.get(getName());
				if(methodesMap != null){
					List<String> listToRemove = new ArrayList<String>();
					for(String key : methodesMap.keySet()){
						List listValues = (List)methodesMap.get(key);
						//
						try {
							for(Object passedValue : listValues){
								ReflectUtil.callComponentFieldOrMethode(this, key, passedValue);
								listToRemove.add(key);
							}
						} catch (Exception e) {
							throw new JspException(e);
						}
					}
					// Remove after call					
					for(String key : listToRemove){
						methodesMap.remove(key);
					}
				}
			}
		}
	}

	/**
	 * Search the value from beanView at first, if it does n't exist
	 * then serch from params map
	 */
	public void valuateFromBeanOrParamsMap(HttpServletRequest request){
		if(StringUtil.isTrueOrNull(getAutoValue())){
			if((getValue() == null) && (getName() != null) && (isEditableField || isBoxField)){
				// If refresh action or close popup, valuate all data from params
				String forceFromMap = (String)request.getAttribute("IS_FROM_MAP");

				boolean isForceFromMap = "OK".equals(forceFromMap);
				boolean isForceFromBean = "KO".equals(forceFromMap);
				IViewBean viewBean = null;
				
				// First valuate from bean view
				if(!isForceFromMap){
					String beanName = (String)request.getAttribute("work_bean_name");
					if(beanName != null){
						viewBean = (IViewBean)request.getAttribute(beanName);
						if(viewBean != null){
							try {
								String aliasBean = ControllerBeanUtil.getAliasBeanByObject(viewBean) + ".";
								int idxAlias = getName().indexOf(aliasBean);
								String shortName = getName().substring(idxAlias+aliasBean.length());
								setValue(PropertyUtils.getProperty(viewBean, shortName));
							} catch (Exception e) {
								isForceFromMap = true;
							}
						}
					} else{
						isForceFromMap = true;
					}
				} 
				// Load value from map
//				else if(!isForceFromBean){
				if((!isForceFromBean || isForceFromMap) && (viewBean == null)){
					Map params = (Map)request.getAttribute(ProjectConstante.WORK_PARAMS);
					if(params != null){
						setValue(params.get(getName()));
					}
				}
			}
		}
	}

	/**
	 * @param component
	 * @return
	 */
	private String addRequiredImg(HttpServletRequest request){
		if(!ComponentUtil.isReadOnlyAttributeForm(request)){
			if(StringUtil.isTrue(this.getRequired())){
				this.appendStyle("border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;");
			}
		}

		return "";
	}

	/**
	 * @return
	 */
	protected boolean isKeyLabel(){
		HttpServletRequest request = getGuiOrContextHttpRequest();
		String action = ControllerUtil.getAction(request);
		// Add field name if is key
		if (StringUtil.isTrue(this.getIskey()) && !(ActionConstante.CREATE.equals(action) && MessageService.isError())
			&& !ActionConstante.INIT_CREATE.equals(action)
			&& !ActionConstante.INIT_DUPLIC.equals(action)) {
			return true;
		}

		return false;
	}

	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected void buildMapValidators(HttpServletRequest request){
		Map<String, Map<String, String>> mapValidators = ControllerUtil.getMapValidator(request);
		// if map is not null and form is declared
		if(mapValidators != null){
			if((this.getName() != null) && (isEditableField || isBoxField) && !(this instanceof RadioButton)){
				Map<String, String> mapAnalyser = new HashMap<String, String>();
				//
				if(getRequired() != null){
					mapAnalyser.put(ProjectConstante.REQUIR, getRequired());
				}
				if(getMin() != null){
					mapAnalyser.put(ProjectConstante.MIN, getMin());
				}
				if(getMax() != null){
					mapAnalyser.put(ProjectConstante.MAX, getMax());
				}
				if(getMinlength() != null){
					mapAnalyser.put(ProjectConstante.MINLENGTH, getMinlength());
				}
				if(getMaxlength() != null){
					mapAnalyser.put(ProjectConstante.MAXLENGTH, getMaxlength());
				}
				if(getValidator() != null){
					mapAnalyser.put(ProjectConstante.VALID, getValidator());
				}
				if(getType() != null){
					mapAnalyser.put(ProjectConstante.TYPE, this.getType());
				}

				// Add to map validators
				if(mapAnalyser.size() > 0){
					mapValidators.put(getName(), mapAnalyser);
				} else{
					mapAnalyser = null;
				}
			}
		}
	}
}
