package framework.model.common.util.converter;

import java.util.Date;

import org.apache.commons.beanutils.Converter;

import framework.model.common.util.DateUtil;
import framework.model.common.util.StringUtil;

public class DateConverter implements Converter {
	@SuppressWarnings("unchecked")
	public Date convert(Class type, Object value) {
		if(value instanceof String && (""+value).indexOf("/") != -1){
			value = DateUtil.stringToDate(""+value);
		}
		
		return (Date)(StringUtil.isEmpty(value) ? null : value);
	}
}
