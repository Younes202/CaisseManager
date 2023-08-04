package framework.model.common.validator;

import java.util.Map;

import framework.component.ComponentUtil;
import framework.controller.annotation.WorkValidator;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.service.MessageService;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;

@WorkValidator(alias="alpha")
public class AlphaValidator implements IValidator {

	/*
	 * (non-Javadoc)
	 *
	 * @see model.service.commun.validator.Validator#validate(java.lang.String, java.lang.String)
	 */
	public String validate(String fieldName, Object fieldValue, Map<String, String> params) throws Exception {
		if (StringUtil.isEmpty(fieldValue)){
			return null;
		}
		//
		String fieldValueSt = (String)fieldValue;
		String excludCarac = null, regexExcludCarac = null;
		if(params != null){
			String pattern = params.get(ProjectConstante.VAL_FORMAT);
			if(pattern != null){
				fieldValueSt = ComponentUtil.getFormattedString(fieldValueSt, pattern);
			}
			//
			String excludeCracs = params.get(ProjectConstante.VAL_EXCLUDE);
			// Get hexadecimal caracters
			if(excludeCracs != null){
				excludCarac = StrimUtil.getGlobalConfigPropertie(excludeCracs);
				regexExcludCarac = StringUtil.getRegexException(excludCarac);
			}
		}

		//
		if (!StringUtil.isAlpha(fieldValueSt, regexExcludCarac)) {
			 MessageService.addFieldMessage(fieldName, StrimUtil.label("work.alpha.error"));
		}

		return fieldValueSt;
	}
}
