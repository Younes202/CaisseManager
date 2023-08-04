/**
 *
 */
package framework.model.common.validator;

import java.util.Map;

import framework.controller.annotation.WorkValidator;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.service.MessageService;
import framework.model.common.service.TypeValidatorService;
import framework.model.common.util.NumericUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;

/**
 * @author 
 *
 */
@WorkValidator(alias="max")
public class MaximumValidator implements IValidator {

	/*
	 * (non-Javadoc)
	 *
	 * @see model.service.commun.validator.Validator#validate(java.lang.String, java.lang.String)
	 */
	public Object validate(String fieldName, Object fieldValue, Map<String, String> params) throws Exception {
		if (StringUtil.isEmpty(fieldValue))
			return null;

		int max = NumericUtil.getIntOrDefault(params.get(ProjectConstante.VAL_MAX));
		Object returnObject = TypeValidatorService.validateType(ProjectConstante.TYPE_DATA_ENUM.DECIMAL, fieldName, ""+fieldValue);
		//
		if (NumericUtil.isNum(returnObject) || NumericUtil.isDecimal(returnObject)) {
			if (((Double)returnObject).doubleValue() > max) {
				MessageService.addFieldMessage(fieldName, StrimUtil.label("work.inferieur.error", ""+max));
			}
		}

		return fieldValue;
	}

}
