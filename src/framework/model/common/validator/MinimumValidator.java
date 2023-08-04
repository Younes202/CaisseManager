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

/**
 * @author 
 * 
 */
@WorkValidator(alias="min")
public class MinimumValidator implements IValidator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.service.commun.validator.Validator#validate(java.lang.String, java.lang.String)
	 */
	public Object validate(String fieldName, Object fieldValue, Map<String, String> params) throws Exception {
		int min = NumericUtil.getIntOrDefault(params.get(ProjectConstante.VAL_MIN));
		Object returnObject = TypeValidatorService.validateType(ProjectConstante.TYPE_DATA_ENUM.DECIMAL, fieldName, ""+fieldValue);
		//
		if (NumericUtil.isNum(returnObject) || NumericUtil.isDecimal(returnObject)) {
			if (((Double)returnObject).doubleValue() < min) {
				MessageService.addFieldMessage(fieldName, StrimUtil.label("work.superieur.error", ""+min));
			}
		}
		
		return min;
	}

}
