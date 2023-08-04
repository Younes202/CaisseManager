/**
 *
 */
package framework.component.facade.text;


/**
 * @author 
 *
 */
public class UI_Date extends TextBase {

	/**
	 * @param picto
	 *            the picto to set
	 */
	public void setPicto(String picto) {
		manageGuiComponent("setPicto", picto);
	}

	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(String pattern) {
		manageGuiComponent("setPattern", pattern);
	}

	/**
	 * 	Restrict the range of selectable dates
	 * with the minDate and maxDate options. Set the beginning and end dates as actual dates
	 * (new Date(2009, 1 - 1, 26)), as a numeric offset from today (-20), or as a string of periods
	 * and units ('+1M +10D'). For the last, use 'D' for days, 'W' for weeks, 'M' for months, or 'Y' for years.
	 */
	public void setDateMin(String dateMin) {
		manageGuiComponent("setDateMin", dateMin);
	}

	/**
	 * 	Restrict the range of selectable dates
	 * with the minDate and maxDate options. Set the beginning and end dates as actual dates
	 * (new Date(2009, 1 - 1, 26)), as a numeric offset from today (-20), or as a string of periods
	 * and units ('+1M +10D'). For the last, use 'D' for days, 'W' for weeks, 'M' for months, or 'Y' for years.
	 */
	public void setDateMax(String dateMax) {
		manageGuiComponent("setDateMax", dateMax);
	}
}
