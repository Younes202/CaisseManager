package framework.model.common.validator;

import java.util.Map;

import framework.controller.annotation.WorkValidator;

@WorkValidator(alias="postal")
public class PostalValidator implements IValidator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.service.commun.validator.Validator#validate(java.lang.String, java.lang.String)
	 */
	public Object validate(String fieldName, Object fieldValue, Map<String, String> params) throws Exception {
		return fieldValue;
	}

}
