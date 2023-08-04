package framework.model.common.util;

import java.math.BigDecimal;

public class NumericUtil {

	private static final int DEFAULT_NUM_RETURN = 0;
	
	/**
	 * @param value
	 * @return
	 */
	public static int getIntOrDefault(Object value) {
		if(StringUtil.isNotEmpty(value)){
			try {
				return Integer.parseInt(("" + value).trim());
			} catch (Exception e) {
				return DEFAULT_NUM_RETURN;
			}
		} else{
			return DEFAULT_NUM_RETURN;
		}
	}

	/**
	 * @param value
	 * @return
	 */
	public static Integer getIntegerOrDefault(Object value) {
		if(StringUtil.isNotEmpty(value)){
			try {
				return Integer.parseInt(("" + value).trim());
			} catch (Exception e) {
				return new Integer(DEFAULT_NUM_RETURN);
			}
		} else{
			return new Integer(DEFAULT_NUM_RETURN);
		}
	}
	
	/**
	 * @param value
	 * @return
	 */
	public static Float getFloatOrDefault(Object value) {
		if(StringUtil.isNotEmpty(value)){
			value = replaceBlank(""+value);
			try {
				return Float.parseFloat(("" + value).trim());
			} catch (Exception e) {
				return new Float(DEFAULT_NUM_RETURN);
			}
		} else{
			return new Float(DEFAULT_NUM_RETURN);
		}
	}

	/**
	 * @param value
	 * @return
	 */
	public static Integer getIntegerOrDefault(Object value, int defaultValue) {
		if(StringUtil.isNotEmpty(value)){
			try {
				return Integer.parseInt(("" + value).trim());
			} catch (Exception e) {
				return defaultValue;
			}
		} else{
			return defaultValue;
		}
	}

	/**
	 * @param value
	 * @return
	 */
	public static Long getLongOrDefault(Object value) {
		if(StringUtil.isNotEmpty(value)){
			value = replaceBlank(""+value);
			try {
				return Long.parseLong(("" + value).trim());
			} catch (Exception e) {
				return new Long(DEFAULT_NUM_RETURN);
			}
		} else{
			return new Long(DEFAULT_NUM_RETURN);
		}
	}

	/**
	 * @param value
	 * @return
	 */
	public static Long getLong(Object value) {
		if(StringUtil.isNotEmpty(value)){
			value = replaceBlank(""+value);
			try {
				return Long.parseLong(("" + value).trim());
			} catch (Exception e) {
				return null;
			}
		} else{
			return null;
		}
	}

	/**
	 * @param value
	 * @return
	 */
	public static Double getDoubleOrDefault(Object value) {
		if(StringUtil.isNotEmpty(value)){
			value = replaceBlank(""+value);
			try {
				return Double.parseDouble(("" + value).trim());
			} catch (Exception e) {
				return new Double(DEFAULT_NUM_RETURN);
			}
		} else{
			return new Double(DEFAULT_NUM_RETURN);
		}
	}

	/**
	 * @param value
	 * @return
	 */
	public static Long toLong(Object value) {
		if(StringUtil.isNotEmpty(value)){
			value = replaceBlank(""+value);
			return Long.parseLong(("" + value).trim());
		}

		return null;
	}

	/**
	 * @param value
	 * @return
	 */
	public static Float toFloat(Object value) {
		if(StringUtil.isNotEmpty(value)){
			value = replaceBlank(""+value);
			return Float.parseFloat(""+value);
		}

		return null;
	}

	/**
	 * @param value
	 * @return
	 */
	public static Double toDouble(Object value) {
		if(StringUtil.isNotEmpty(value)){
			value = replaceBlank(""+value);
			return Double.parseDouble(("" + value).trim());
		}

		return null;
	}

	/**
	 * @param value
	 * @return
	 */
	public static Integer toInteger(Object value) {
		if(StringUtil.isNotEmpty(value)){
			return Integer.parseInt(("" + value).trim());
		}

		return null;
	}

	/**
	 * @param valeur
	 * @return
	 */
	public static boolean isInt(final Object valeur) {
		boolean retour = false;
		try {
			Integer.parseInt("" + valeur);
			retour = true;
		} catch (Exception pe) {
			retour = false;
		}

		return retour;
	}

	/**
	 * @param valeur
	 * @return
	 */
	public static boolean isLong(Object valeur) {
		boolean retour = false;
		try {
			valeur = replaceBlank(""+valeur);
			Long.parseLong("" + valeur);
			retour = true;
		} catch (Exception pe) {
			retour = false;
		}

		return retour;
	}

	/**
	 * @param val
	 * @return
	 */
	public static boolean isNum(Object value) {
		String val = ("" + value).trim();
		value = replaceBlank(""+value);
		boolean match = val.matches("^[0-9]{0,}$");

		return match;
	}
	
	public static String replaceBlank(String val){
		if(StringUtil.isEmpty(val) || val.matches(".*[a-zA-Z]+.*")){
			return val;
		}
		val = (""+val).replace(",", ".");
		String finalVal = "";
		for(char c : val.toCharArray()){
			boolean match = (c+"").matches("^[0-9]{0,}$");
			if(match || c == '.' || c==',' || c=='-'|| c=='+'){
				finalVal = finalVal + c;
			}
		}
		return finalVal;
	}

	/**
	 * @param val
	 * @return
	 */
	public static boolean isDecimal(Object value) {
		String val = ("" + value).trim();
		val = replaceBlank(val);
		//
		//boolean match = val.matches("^[0-9]+(\\.[0-9]+|\\,[0-9]+)?$");
		boolean match = val.matches("^-?[0-9]\\d*(\\.\\d+)?$");//val.matches("^-?[0-9]{1,12}(?:\\.[0-9]{1,4})?$");
		return match;
	}

	/**
	 * This convert if value off cell is not null
	 * @param array
	 * @return
	 */
	public static Long[] stringArrayToLongArray(String[] array){
		Long[] ids = null;
		if(array != null){
			int length = array.length;
			if((array != null) && (length > 0)){
				ids = new Long[length];

				int cpt = 0;
				for(int i=0; i<length; i++){
					String st = array[i];
					if(StringUtil.isNotEmpty(st)){
						Long value = NumericUtil.getLong(st);
						if(value != null){
							ids[i] = value;
						} else{
							cpt++;
						}
					} else{
						cpt++;
					}
				}
				// Create new Array
				Long[] idsNotNullCells = new Long[length-cpt];
				cpt = 0;
				// Delete null cells
				for(Long id : ids){
					if(id != null){
						idsNotNullCells[cpt] = id;
						cpt++;
					}
				}
				//
				ids = idsNotNullCells;
			}
		}

		return ids;
	}

    /**
     * @param min
     * @param max
     * @return
     */
    public static int getRandomNumber(int min, int max) {
        return (int) (Math.random() * (max - min + 1) ) + min;
   }
	
	/**
	 * @param val
	 * @return empty if val e=is null or equals to zero
	 */
	public static String getMtt(BigDecimal val){
		if(val == null || val.compareTo(BigDecimalUtil.ZERO) == 0){
			return "";
		}
		return ""+val;
	}
}
