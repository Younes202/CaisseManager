/**
 * 
 */
package framework.model.common.validator;

import java.util.Map;


/**
 * @author 
 * 
 */
public interface IValidator {
	
	/**
	 * In order field name, field value, others
	 * 
	 * @param values
	 * @return
	 */
	public Object validate(String fieldName, Object fieldValue, Map<String, String> params) throws Exception;
}
