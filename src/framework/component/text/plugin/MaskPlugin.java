package framework.component.text.plugin;


public class MaskPlugin {
	
	/**
	 * a - Represents an alpha character (A-Z,a-z)
     * 9 - Represents a numeric character (0-9)
     * * - Represents an alphanumeric character (A-Z,a-z,0-9)
     * Example of pattern :
     * 99/99/9999
     * (999) 999-9999
     * (999) 999-9999? x99999
     * 99-9999999
     * 999-99-9999
     * a*-999-a999
     * ~9.99 ~9.99 999
     * See documentation at http://digitalbush.com/projects/masked-input-plugin/ 
	 * @param sb
	 * @param componentId
	 * @param pattern
	 */
	public static String getMaskScript(String formattedId, String pattern){
		// Mask script
		return  "jQuery(function($) {\n" +
				"	$(\"#" + formattedId + "\").mask('"+pattern+"');\n" + 
				"});\n";
	}
}
