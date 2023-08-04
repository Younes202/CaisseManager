package framework.model.common.service;

import framework.model.common.constante.ProjectConstante;
import framework.model.common.constante.ProjectConstante.TYPE_DATA_ENUM;
import framework.model.common.util.DateUtil;
import framework.model.common.util.NumericUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;

public class TypeValidatorService {
	
	/**
	 * @param type
	 * @param fieldName
	 * @param fieldValue
	 * @return
	 * @throws Exception
	 */
	public static Object validateType(ProjectConstante.TYPE_DATA_ENUM type, String fieldName, Object fieldValue) throws RuntimeException{
		try{
			if(fieldValue instanceof String[]){
				return validateArrayType(type, fieldName, (String[])fieldValue);
			} else{
				return validateStringType(type, fieldName, (String)fieldValue);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param type
	 * @param fieldName
	 * @param fieldValues
	 * @return
	 * @throws Exception
	 */
	private static Object[] validateArrayType(ProjectConstante.TYPE_DATA_ENUM type, String fieldName, String[] fieldValues) throws Exception{
		if ((fieldValues == null) || (fieldValues.length == 0)){
			return null;
		}
		//
		int length = fieldValues.length;
		Object[] data = null;
		String messageKey = null;
		switch(type){
			case STRING_ARRAY : {
				return fieldValues;
			}
			case LONG_ARRAY : {
				Long[] array = new Long[length];
				for(int i=0; i<length; i++){
					String value = fieldValues[i];
					if(NumericUtil.isInt(value)){
						array[i] = NumericUtil.toLong(value);	
					} else{
						messageKey = "work.numeric.error";
						break;
					}
				}
				data = array;
			}; break;
			case INTEGER_ARRAY : {
				Integer[] array = new Integer[length];
				for(int i=0; i<length; i++){
					String value = fieldValues[i];
					if(NumericUtil.isInt(value)){
						array[i] = NumericUtil.toInteger(value);	
					} else{
						messageKey = "work.numeric.error";
						break;
					}
				}
				data = array;
			}; break;
			case DECIMAL_ARRAY : {
				Double [] array = new Double[length];
				for(int i=0; i<length; i++){
					String value = fieldValues[i];
					if(NumericUtil.isDecimal(value)){
						array[i] = NumericUtil.toDouble(value);	
					} else{
						messageKey = "work.decimal.error";
						break;
					}
				}
				data = array;
			}; break;
			/*case LONG_ARRAY : {
				Long [] array = new Long[length];
				for(int i=0; i<length; i++){
					String value = fieldValues[i];
					if(NumericUtil.isLong(value)){
						array[i] = NumericUtil.toLong(value);	
					} else{
						messageKey = "work.numeric.error";
						break;
					}
				}
				data = array;
			}; break;*/
		}
		
		//
		if (messageKey != null) {
			MessageService.addFieldMessage(fieldName, StrimUtil.label(messageKey));
		}
		
		return data;
	}
	
	/**
	 * @param type
	 * @param fieldName
	 * @param fieldValue
	 * @return
	 * @throws Exception
	 */
	private static Object validateStringType(ProjectConstante.TYPE_DATA_ENUM type, String fieldName, String fieldValue) throws Exception{
		if (StringUtil.isEmpty(fieldValue) || type == null){
			return null;
		}
		//
		String messageKey = null;
		//
		switch(type){
			case STRING : {
				return fieldValue;
			}
			case LONG : {
				if(NumericUtil.isLong(fieldValue)){
					return NumericUtil.toLong(fieldValue);
				} else{
					messageKey = "work.field.numeric.error";
				}
			}; break;
			case INTEGER : {
				if(NumericUtil.isInt(fieldValue)){
					return NumericUtil.toInteger(fieldValue);
				} else{
					messageKey = "work.field.numeric.error";
				}
			}; break;
			case DECIMAL : {
				if(NumericUtil.isDecimal(fieldValue.replaceAll(" ", ""))){
					return NumericUtil.toDouble(fieldValue.replaceAll(" ", ""));
				} else{
					messageKey = "work.field.decimal.error";
				}
			}; break;
			case DATE : {
				if(DateUtil.isDate(fieldValue)){
					return DateUtil.stringToDate(fieldValue);
				} else{
					messageKey = "work.field.date.error";
				}
			}; break;
			case BOOLEAN : {
				return (StringUtil.isTrue(fieldValue) ? 1 : 0);
			}
		}
		
		//
		if (messageKey != null) {
			MessageService.addFieldMessage(fieldName, StrimUtil.label(messageKey));
		}
		
		return fieldValue;
	}
	
	/**
	 * @param stType
	 * @param fieldValue
	 * @return
	 * @throws Exception
	 */
	public static Object convertToType(String stType, String fieldValue) {
		if (StringUtil.isEmpty(fieldValue)){
			return null;
		}
		//
		try {
			TYPE_DATA_ENUM type = TYPE_DATA_ENUM.getTypeData(stType);
			//
			switch(type){
				case STRING : {
					return fieldValue;
				}
				case LONG : {
					if(NumericUtil.isInt(fieldValue)){
						return NumericUtil.toInteger(fieldValue);
					}
				}; break;
				case INTEGER : {
					if(NumericUtil.isInt(fieldValue)){
						return NumericUtil.toInteger(fieldValue);
					}
				}; break;
				case DECIMAL : {
					if(NumericUtil.isDecimal(fieldValue)){
						return NumericUtil.toDouble(fieldValue);
					}
				}; break;
				case DATE : {
					if(DateUtil.isDate(fieldValue)){
						return DateUtil.stringToDate(fieldValue);
					}
				}; break;
				case BOOLEAN : {
					return (StringUtil.isTrue(fieldValue) ? 1 : 0);
				}
			}
		} catch (Exception e) {
			return fieldValue;		
		}
				
		return fieldValue;
	}

}
