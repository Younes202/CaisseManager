package framework.model.common.service;

import java.util.HashMap;
import java.util.Map;

import framework.model.common.constante.ProjectConstante;
import framework.model.common.util.NumericUtil;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;
import framework.model.common.validator.MaxLengthValidator;
import framework.model.common.validator.MaximumValidator;
import framework.model.common.validator.MinLengthValidator;
import framework.model.common.validator.MinimumValidator;

public abstract class CommunValidatorService {

	/**
	 * @param validatorType
	 * @param fieldName
	 * @param fieldValue
	 * @return
	 */
	public static Object validateField(String validator, String fieldName, Object fieldValue) throws Exception{
		// Type validator
		Map<String, String> fragments = StringUtil.getValidatorFragments(validator);
		// Validator path
		Class<?> validatorClass = IhmMappingService.VALIDATOR_MAP.get(fragments.get(ProjectConstante.VAL_VALIDATOR));
		if(validatorClass == null){
			String message = StrimUtil.label("work.validator.error", validator, fieldName);
			FrameworkMessageService.addDevelopperMessage(message);
			throw new Exception(message);
		}
		// Delete validator alias from list
		fragments.remove(0);
		// String array
		//String validatorArgs = (fragments != null) ? fragments.get(0) : null;
		// Invoke validate methode
		Object[] args = new Object[] { fieldName, fieldValue, fragments};
		Class<?>[] types = new Class[] { String.class, Object.class, Map.class };
		
		Object validatorInstance = ServiceUtil.getBusinessBean(validatorClass);
		// For Spring
		//Object returnObject = ReflectUtil.invokeMethode(validatorClass.newInstance(), "validate", args, types);
		// For Guice
		Object returnObject = ReflectUtil.invokeMethode(validatorInstance, "validate", args, types);

		return returnObject;
	}

	/**
	 * @param max
	 * @param min
	 * @param fieldName
	 * @param fieldValue
	 * @return
	 * @throws Exception
	 */
	public static Object validateMinMax(String max, String min, String fieldName, Object fieldValue) throws Exception {
		// Developper errors
		String message = null;
		Object returnObject = fieldValue;
		//
		if ((max != null) && (min != null)) {
			message = validateCoherence(max, min, fieldName);
			if (message != null) {
				FrameworkMessageService.addDevelopperMessage(message);
			}
		}
		// Form errors
		if (message == null) {
			Map<String, String> paramsMap = new HashMap<String, String>();
			if ((min != null)) {
				paramsMap.put(ProjectConstante.VAL_MIN, min);
				//
				returnObject = new MinimumValidator().validate(fieldName, fieldValue, paramsMap);
			}
			if ((max != null)) {
				paramsMap.put(ProjectConstante.VAL_MAX, max);
				//
				returnObject = new MaximumValidator().validate(fieldName, fieldValue, paramsMap);
			}
		}

		return returnObject;
	}

	/**
	 * @param max
	 * @param min
	 * @param fieldName
	 * @param fieldValue
	 * @return
	 * @throws Exception
	 */
	public static Object validateMinMaxLength(String max, String min, String fieldName, Object fieldValue) throws Exception {
		// Developper errors
		String message = null;
		Object returnObject = fieldValue;
		//
		if ((max != null) && (min != null)) {
			message = validateCoherence(max, min, fieldName);
			if (message != null) {
				FrameworkMessageService.addDevelopperMessage(message);
			}
		}
		// Form errors
		if (message == null) {
			Map<String, String> paramsMap = new HashMap<String, String>();
			if ((min != null)) {
				paramsMap.put(ProjectConstante.VAL_MINLENGTH, min);
				//
				returnObject = new MinLengthValidator().validate(fieldName, fieldValue, paramsMap);
			}
			if ((max != null)) {
				paramsMap.put(ProjectConstante.VAL_MAXLENGTH, max);
				//
				returnObject = new MaxLengthValidator().validate(fieldName, fieldValue, paramsMap);
			}
		}

		return returnObject;
	}

	/**
	 * @param maxValue
	 * @param minValue
	 * @return
	 */
	private static String validateCoherence(String maxValue, String minValue, String fieldName) {
		if (!NumericUtil.isNum(maxValue)) {
			return StrimUtil.label("work.min.numeric.error", fieldName);
		} else if (!NumericUtil.isNum(minValue)) {
			return StrimUtil.label("work.max.numeric.error", fieldName);
		} else if (NumericUtil.getIntOrDefault(minValue) > NumericUtil.getIntOrDefault(maxValue)) {
			return StrimUtil.label("work.min.max.error", fieldName);
		}

		return null;
	}
}
