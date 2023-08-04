package framework.model.common.validator;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import framework.controller.annotation.WorkValidator;
import framework.model.common.service.MessageService;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;

@WorkValidator(alias="email")
public class MailValidator implements IValidator {
	  private static final String EMAIL_PATTERN = 
          "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	/*
	 * (non-Javadoc)
	 * 
	 * @see model.service.commun.validator.Validator#validate(java.lang.String, java.lang.String)
	 */
	public String validate(String fieldName, Object fieldValue, Map<String, String> params) throws Exception {
		if (StringUtil.isEmpty(fieldValue)){
			return null;
		}
		
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher((String)fieldValue);
		
		if(!matcher.matches()){
			MessageService.addFieldMessage(fieldName, StrimUtil.label("work.email.error"));
		}
		
		return (String)fieldValue;
	}
}
