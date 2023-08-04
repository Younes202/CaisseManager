/**
 *
 */
package framework.model.common.validator;

import java.util.Map;

import framework.controller.annotation.WorkValidator;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.service.FrameworkMessageService;
import framework.model.common.service.MessageService;
import framework.model.common.util.NumericUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;

/**
 * @author 
 *
 */
@WorkValidator(alias="maxlength")
public class MaxLengthValidator implements IValidator {

	/*
	 * (non-Javadoc)
	 *
	 * @see model.service.commun.validator.Validator#validate(java.lang.String, java.lang.String)
	 */
	public Object validate(String fieldName, Object fieldValue, Map<String, String> params) throws Exception {
		Integer maxlength = NumericUtil.toInteger(params.get(ProjectConstante.VAL_MAXLENGTH));
		if((maxlength == null) || (maxlength == 0)){
			String message = StrimUtil.label("work.length.val.error", fieldName, StringUtil.getValueOrEmpty(maxlength));
			FrameworkMessageService.addDevelopperMessage(message);
		} else{
			if (StringUtil.getValueOrEmpty(fieldValue).trim().length() > maxlength) {
				MessageService.addFieldMessage(fieldName, StrimUtil.label("work.maxlength.error", ""+maxlength));
			}
		}

		return fieldValue;
	}

}
