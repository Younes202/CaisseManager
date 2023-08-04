/**
 * 
 */
package framework.model.common.validator;

import java.util.Map;

import framework.component.ComponentUtil;
import framework.controller.annotation.WorkValidator;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.service.MessageService;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;

/**
 * @author 
 * 
 */
@WorkValidator(alias="alphanum")
public class AlphaNumericValidator implements IValidator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.service.commun.validator.Validator#validate(java.lang.String, java.lang.String)
	 */
	public String validate(String fieldName, Object fieldValue, Map<String, String> params) throws Exception{
		if (StringUtil.isEmpty(fieldValue)){
			return null;
		}
		//
		String fieldValueSt = (String)fieldValue;
		String excludCarac = null, regexExcludCarac = null;
		if(params != null){
			String pattern = params.get(ProjectConstante.VAL_FORMAT);
			fieldValueSt = ComponentUtil.getFormattedString(fieldValueSt, pattern);
			//
			String excludeGroup = params.get(ProjectConstante.VAL_EXCLUDE);
			if(excludeGroup != null){
				// Get hexadecimal caracters
				excludCarac = StrimUtil.getGlobalConfigPropertie(excludeGroup);
				regexExcludCarac = StringUtil.getRegexException(excludCarac);
			}
		}
		// 
		if (!StringUtil.isAlphaNum(fieldValueSt, regexExcludCarac)) {
			String message = (excludCarac != null) ? StrimUtil.label("work.alphanum.except.error", excludCarac) 
												   : StrimUtil.label("work.alphanum.error");
			MessageService.addFieldMessage(fieldName, message);
		}
		
		return fieldValueSt;
	}
}
