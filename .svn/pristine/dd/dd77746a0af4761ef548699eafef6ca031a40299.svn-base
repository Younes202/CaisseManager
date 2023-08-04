/**
 * 
 */
package framework.model.common.validator;

import java.util.Map;

import framework.controller.annotation.WorkValidator;
import framework.model.common.service.MessageService;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;

/**
 * @author 
 * 
 */
@WorkValidator(alias="required")
public class RequiredValidator implements IValidator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.service.commun.validator.Validator#validate(java.lang.String, java.lang.String)
	 */
	public Object validate(String fieldName, Object fieldValue, Map<String, String> params) throws Exception {
		boolean isError = false;
		//
		if (StringUtil.isEmpty(fieldValue)){
			isError = true;
		} else if(fieldValue instanceof String[]){
			if ((fieldValue != null) && (((String[])fieldValue).length == 0)){
				isError = true;
			}
		}

		//
		if(isError){
			MessageService.addFieldMessage(fieldName, StrimUtil.label("work.required.error"));
		}
		
		return fieldValue;
	}

}
