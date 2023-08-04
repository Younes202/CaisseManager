package framework.model.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import framework.model.common.constante.ProjectConstante;

public class StringUtil {

	/**
	 * @param key
	 * @param prefix
	 * @return 
	 */
	public static String getStringSubstring(String key, String prefix) {
		int idxscope = key.indexOf(prefix);
		if ((key != null) && (idxscope != -1)) {
			key = key.substring(prefix.length(), key.length());
		}

		return key;
	}

	/**
	 * @param valeur
	 * @return
	 */
	public static boolean isEmpty(Object valeur) {
		if ((valeur != null) && !"".equals(("" + valeur).trim()) && !"undefined".equals(("" + valeur).trim()) && !"null".equals(("" + valeur).trim())) {
			return false;
		}

		return true;
	}
	
	/**
	 * @param valeur
	 * @return
	 */
	public static boolean isEmptyList(Collection<?> list) {
		if (list != null && list.size() > 0) {
			return false;
		}

		return true;
	}
	
	public static boolean isTrue(Integer value) {
		return isTrue(""+value);
	}
	
	/**
	 * @param value
	 * @return
	 */
	public static boolean isTrue(String value) {
		return (isNotEmpty(value) && ("true".equalsIgnoreCase(value) || "1".equals(value)));
	}
	public static boolean isTrueOrNull(String value) {
		return (isEmpty(value) || isTrue(value));
	}
	/**
	 * @param value
	 * @return
	 */
	public static boolean isFalse(String value) {
		return (isNotEmpty(value) && ("false".equalsIgnoreCase(value) || "0".equals(value)));
	}
	public static boolean isFalse(Integer value) {
		return isFalse(""+value);
	}
	public static boolean isFalseOrNull(Integer value) {
		return isFalseOrNull(""+value);
	}
	public static boolean isFalseOrNull(String value) {
		return (isEmpty(value) || (isFalse(value)));
	}
	
	/**
	 * @param valeur
	 * @return
	 */
	public static boolean isNotEmpty(Object valeur) {
		return !isEmpty(valeur);
	}
	
	/**
	 * @param value
	 * @return
	 */
	public static String getValueOrEmpty(Object value) {
		if ((value == null) || "null".equals((""+value).trim())) {
			return "";
		} else {
			return ""+value;
		}
	}
	
	public static String getValueOrNull(Object value) {
		if ((value == null) || "null".equals((""+value).trim())) {
			return null;
		} else {
			return ""+value; 
		}
	}

	
	public static String getHtmlEmpty(Object value) {
		if (isEmpty(value)) {
			return "&nbsp;";
		} else {
			return ""+value;
		}
	}

	/**
	 * @param val
	 * @return
	 */
	public static boolean isMail(String val) {
		boolean match = val.matches("^\\w[\\w+\\.\\-]*@[\\w\\-]+\\.\\w[\\w+\\.\\-]*\\w$");

		return match;
	}

	/**
	 * @param val
	 * @param except
	 * @return
	 */
	public static boolean isAlphaNum(String val, String except) {
		if (StringUtil.isNotEmpty(except)){
			return val.matches("[^" + except + "]{0,}$");
		}
		
		return true;
	}

	/**
	 * @param val
	 * @param except
	 * @return
	 */
	public static boolean isStrictAlpha(String val) {
		boolean match = val.matches("^[a-zA-Z]+$");
		return match;
	}
	
	/**
	 * Strictly alpha without space ... 
	 * @param val
	 * @return
	 */
	public static boolean isAlpha(String val, String except) {
		if (except == null){
			except = "";
		}
		boolean match = val.matches("[^" + except + "0-9]{0,}$");

		return match;
	}

	/**
	 * Password matching expression. Password validator Requires 6-20 characters including at least 1 upper or lower alpha, and 1
	 * digit. It should disallow just about everything else, inluding extended characters.
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isPassword(String val) {
		boolean match = val.matches("^(?=.*\\d)(?=.*[a-zA-Z])(?!.*[\\W_\\x7B-\\xFF]).{6,15}$");

		return match;
	}

	/**
	 * @param val
	 * @return
	 */
	public static boolean isPhone(String val) {
		return false;
	}

	/**
	 * @param val
	 * @return
	 */
	public static boolean isCpostal(String val) {
		return false;
	}

	/**
	 * @param fullNames
	 * @param fullValues
	 * @param delim1
	 * @param delim2
	 * @return
	 */
	public static Map<String, String> getMapFromStringDelim(String fullNames, String fullValues, String delim1, String delim2) {
		Map<String, String> analyseMap = null;
		StringTokenizer stName = new StringTokenizer(fullNames, delim1);
		StringTokenizer stValue = new StringTokenizer(fullValues, delim2);
		//
		if (stName.countTokens() > 0) {
			analyseMap = new LinkedHashMap<String, String>(stName.countTokens());
			stName.nextToken();
			while (stName.hasMoreTokens()) {
				String name = stName.nextToken();
				String value = stValue.nextToken();
				analyseMap.put(name, value);
			}
		}
		return analyseMap;
	}
	
	/**
	 * @param fullNames :
	 *            String with | separator
	 * @param fullValues :
	 *            String with | separator
	 * @return map with key value = name and value = value
	 */
	public static Map<String, String> getMapFromStringDelim(String fullNames, String fullValues) {
		return getMapFromStringDelim(fullNames, fullValues, "|", "|");
	}

	/**
	 * @param value
	 * @param separator
	 * @return String aray
	 */
	public static String[] getArrayFromStringDelim(String value, String separator) {
		if (StringUtil.isEmpty(value)) {
			return null;
		}

		String[] values;
		StringTokenizer stName = new StringTokenizer(value, separator);

		if (stName.countTokens() > 0) {
			values = new String[stName.countTokens()];
			int idx = 0;
			while (stName.hasMoreTokens()) {
				String val =  stName.nextToken();
				if(isNotEmpty(val)){
					values[idx] = val;
					idx++;
				}
			}
			return values;
		}

		values = new String[2];
		values[0] = "";
		values[1] = "";

		return values;
	}

	/**
	 * @param value
	 * @return
	 */
	public static String[] getArrayFromStringDelim(String value) {
		return getArrayFromStringDelim(value, ProjectConstante.SEPARATOR);
	}
	
	/**
	 * @param array
	 * @return
	 */
	public static String getStringDelimFromStringArray(Object[] array) {
		return getStringDelimFromStringArray(array, ProjectConstante.SEPARATOR);
	}
	
	public static String getStringDelimFromStringArray(Object[] array, String separator, boolean addApp) {
		return getStringDelimFromStringArray(array, separator, "", true);
	}
	
	/**
	 * @param array
	 * @return
	 */
	public static String getStringDelimFromStringArray(Object[] array, String separator) {
		return getStringDelimFromStringArray(array, separator, "", false);
	}
	
	/**
	 * @param result
	 * @return
	 */
	public static String[][] stringDelimToArray(String result){
		return stringDelimToArray(result, "|", ";");
	}
	
	/**
	 * @param result
	 * @param delim1
	 * @param delim2
	 * @return
	 */
	public static String[][] stringDelimToArray(String result, String delim1, String delim2){
		if(result != null){
			String[] data = StringUtil.getArrayFromStringDelim(result, delim1);
			if(data != null && data.length > 0){
				String[][] groupValues = new String[data.length][2];
				
				for(int i=0; i<data.length; i++){
					String[] data2 = StringUtil.getArrayFromStringDelim(data[i], delim2);
					groupValues[i] = data2;
				}
				
				return groupValues;
			}
		}
		
		return null;
	}
	
	/**
	 * @param array
	 * @return
	 */
	public static String getStringFromStringArray(String[][] array) {
		return getStringFromStringArray(array, ";", "|");
	}
	
	/**
	 * @param array
	 * @param separator1
	 * @param separator2
	 * @return
	 */
	public static String getStringFromStringArray(String[][] array, String separator1, String separator2) {
		String result = "";
		
		for(String[] data : array){
			result += StringUtil.getStringDelimFromStringArray(data, separator1) + separator2;
		}
		
		return result;
	}
	
	/**
	 * @param array
	 * @param separator
	 * @param delimCarac
	 * @return
	 */
	public static String getStringDelimFromStringArray(Object[] array, String separator, String delimCarac, boolean addApost) {
		StringBuilder result = new StringBuilder("");
		if ((array != null) && (array.length > 0)) {
			for(int i = 0; i < array.length; i++){
				result.append(delimCarac + (addApost ? "'"+array[i]+"'":array[i]) + delimCarac);
				if(i < (array.length-1)){
					result.append(separator);
				}
			}
		}

		return result.toString();		
	}

	/**
	 * @param validator
	 * @return
	 */
	public static Map<String, String> getValidatorFragments(String validator) {
		if (StringUtil.isEmpty(validator))
			return null;

		/*List<String> params = new ArrayList<String>();

		int idx = validator.indexOf("{");
		if (idx != -1) {
			params.add(validator.substring(0, idx).trim());
			params.add(validator.substring((idx + 1), (validator.length() - 1)).trim());
		} else {
			params.add(validator.trim());
		}

		return params;*/
		Map<String, String> validatorMap = new HashMap<String, String>();
		String[] validatorArray = StringUtil.getArrayFromStringDelim(validator, ";");
		for(String param : validatorArray){
			String[] paramArray = StringUtil.getArrayFromStringDelim(param, "=");
			if(paramArray.length > 1){
				validatorMap.put(paramArray[0].trim(), paramArray[1].trim());
			} else{
				validatorMap.put("val", paramArray[0].trim());
			}
		}
		
		return validatorMap;
	}

	/**
	 * @param data
	 * @return
	 */
	public static String[] listToStringArray(List<String> data) {
		if (data != null) {
			return data.toArray(new String[data.size()]);
		} else {
			return null;
		}
	}
	
	/**
	 * Test if array of string contains string passed in parameter
	 * @param st
	 * @param tab
	 * @return
	 */
	public static boolean contains(String st, String[] tab){
		if((tab != null) && (st != null)){
			for(String val : tab){
				if(val != null && val.trim().equals(st.trim())){
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * @param str
	 * @param origine
	 * @param target
	 * @return
	 */
	public static String replaceAll(String str, char origine, String target) {
		if(str != null){
			char[] charArray = str.toCharArray();
			StringBuilder sb = new StringBuilder();
			for(char c : charArray){
				if(c == origine){
					sb.append(target);
				} else{
					sb.append(c);
				}
			}
			return sb.toString();
		}
		return str;
	}
	
	/**
	 * @param value
	 * @param delim
	 * @return
	 */
	public static List<String> getElementsList(String value, String delim, boolean isBracket) {
		String[] elements = getElementsArray(value, delim, isBracket);
		List<String> listElements = null;
		if((elements != null) && (elements.length > 0)){
			listElements = new ArrayList<String>(elements.length);
			for(String st : elements){
				listElements.add(st);
			}
		}
		
		return listElements;
	}
	
	/**
	 * String[][]
	 * @param data
	 * @param field1
	 * @param field2
	 * @return
	 */
	public static String[][] listToStringArray(List data, String field1, String field2){
		if((data != null) && (data.size() > 0)){
			String[][] stringArray = new String[data.size()][2];
			//
			for(int i=0; i<data.size(); i++){
				Object obj = data.get(i);
				String value1 = ReflectUtil.getStringPropertieValue(obj, field1);
				String value2 = ReflectUtil.getStringPropertieValue(obj, field2);
				//
				stringArray[i][0] = value1;
				stringArray[i][1] = value2;
			}
			//
			return stringArray;
		}
		
		return null;
	}
	
	/**
	 * @param value
	 * @param delim
	 * @return
	 */
	public static String[] getElementsArray(String value, String delim, boolean isBracket) {
		String[] elements = null;
		if(StringUtil.isNotEmpty(value)){
			if (isBracket == true) {
				value = value.substring(1, value.length() - 1);
			}
			int idx = 0;
			StringTokenizer st = new StringTokenizer(value, delim);
			if (st.countTokens() > 0) {
				elements = new String[st.countTokens()];
				while (st.hasMoreTokens()) {
					elements[idx] = st.nextToken().trim();
					idx++;
				}
			}
		}
		return elements;
	}
	
	/**
	 * @param str
	 * @return
	 */
	public static String firstCharToLowerCase(String str){
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}
	
	/**
	 * @param str
	 * @return
	 */
	public static String firstCharToUpperCase(String str){
		if(isNotEmpty(str)){
			return str.substring(0, 1).toUpperCase() + str.substring(1);
		}
		return null;
	}
	
	/**
	 * Delete '\t', '\n', '\s'
	 * @param text
	 * @return
	 */
	public static String cleanText(String text){
		if(StringUtil.isNotEmpty(text)){
			// Tabulation '\t'
			// Saut de ligne '\n'
			// Espace par '\s'
			//
			text = text.replaceAll("\\t", " ").replaceAll("\\n", " ");//.replaceAll("\\s", " ");
			//
			while(text.indexOf("  ") != -1){
				text = text.trim().replaceAll("  ", " ");
			}
		}
		
		return text;
	}
	
	/**
	 * @param value
	 * @param fullLength : Length to attempt
	 * @param completeChar : Char to append
	 * @param way : Add to left or to right
	 * @return
	 */
	public static String completeStringLength(String value, int fullLength, char completeChar, char way){
		if(isNotEmpty(value) && value.length() < fullLength){
			String completeSt = "";
			while(completeSt.length() < (fullLength-value.length())){
				completeSt += completeChar;
			}
			if(way == 'L'){
				value = completeSt + value;
			} else if(way == 'R'){
				value = value + completeSt;
			}
		}
		
		return value;
	}
	
	/**
	 * @param except
	 * @return
	 */
	public static String getRegexException(String pattern) {
		if (StringUtil.isEmpty(pattern))
			return null;
		// Get caracs
		StringTokenizer st = new StringTokenizer(pattern, " ");
		StringBuilder sb = new StringBuilder();
		while (st.hasMoreTokens()) {
			char c = st.nextToken().charAt(0);
			String hexacode = toHexString(c);
			sb.append("\\x" + hexacode);
		}

		return sb.toString();
	}

	// Fast convert a byte array to a hex string
	// with possible leading zero.
	private static String toHexString(char c) {
		// look up high nibble char
		StringBuilder sb = new StringBuilder();
		sb.append(hexChar[(c & 0xf0) >>> 4]);
		// look up low nibble char
		sb.append(hexChar[c & 0x0f]);

		return sb.toString();
	}

	// table to convert a nibble to a hex char.
	private static final char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
}
