package framework.model.common.util;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * @author Enzo
 *
 */
public class DateUtil {


	public enum TIME_ENUM {
		MINUTE, HOUR, WEEK, DAY, MONTH, YEAR, SECOND
	}

	/**
	 * Mettre heure, minute, seconde à 0. Util pour les requetes
	 * @param date
	 * @return
	 */
	public static long[] getPeriodeInNorme(long s) {
		long annee   = s / 60 / 60 / 24 / 365;
		long jour    = s / 60 / 60 / 24 % 365;
		long heure   = s / 60 / 60 % 24;
		long minute  = s / 60 % 60;
		long seconde = s % 60;
		
		return new long[] {annee, jour, heure, minute, seconde};
	}
	
	public static String getPeriodeInNormeStr(long s) {
		long[] p = getPeriodeInNorme(s);
		String val = "";
		
		if(p[0] != 0) {
			val += p[0]+"an, ";
		}
		if(p[1] != 0) {
			val += p[1]+"j, ";
		}
		if(p[2] != 0) {
			val += p[2]+"h, ";
		}
		if(p[3] != 0) {
			val += p[3]+"min, ";
		}
		if(p[4] != 0) {
			val += p[4]+"sec";
		}
		return val;
	}
	
	public static Date getEndOfDay(Date date) {
		if(date == null) {
			return null;
		}
	  LocalDateTime localDateTime = dateToLocalDateTime(date);
	  LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
	  return localDateTimeToDate(endOfDay);
	}

	public static Date getStartOfDay(Date date) {
		if(date == null) {
			return null;
		}
	  LocalDateTime localDateTime = dateToLocalDateTime(date);
	   LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
	  return localDateTimeToDate(startOfDay);
	}
	private static Date localDateTimeToDate(LocalDateTime startOfDay) {
		if(startOfDay == null) {
			return null;
		}
	  return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
	}
	private static LocalDateTime dateToLocalDateTime(Date date) {
		if(date == null) {
			return null;
		}
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
	}
		
	
	public static String[][] getTempArray(){
		String[][] tempArray = new String[96][2];
		String temp = null;
		int min = 0;
		int idx = 0;
		for(int i=0; i<24; i++){
			min = 0;
			while (min != 60){
				if(min == 60){
					min = 0;
				}

				String minSt = (""+min).length()==1 ? "0"+min : ""+min;

				if((""+i).length() == 1){
					temp = ("0"+i) + ":" + minSt;
				} else{
					temp = i + ":" + minSt;
				}

				min = min + 15;
				// Add
				tempArray[idx][0] = temp;
				tempArray[idx][1] = temp;
				idx++;
			}
		}

		return tempArray;
	}

    /**
     * @return
     */
    public static Date getCurrentDate() {
        return new Date(GregorianCalendar.getInstance(Locale.FRANCE).getTimeInMillis());
    }
    
    /**
     * @return
     */
    public static String getFullCurrentDate() {
		String pattern = StrimUtil.getGlobalConfigPropertie("ddMMyyyy.full.format");
		/* Formatting Date -> String */
		return (new SimpleDateFormat(pattern)).format(new Date());

    }

	/**
	 * @param dateString
	 * @return
	 */
	public static boolean isDate(String dateString) {
		String pattern = StrimUtil.getGlobalConfigPropertie("ddMMyyyy.format");

		return isDate(dateString, pattern);
	}

	/**
	 * @param dateString
	 * @param pattern
	 * @return
	 */
	public static boolean isDate(String dateString, String pattern) {
		Boolean iValid = true;
		//
		try {
			Date myDate = stringToDate(dateString, pattern);
			Calendar cl = Calendar.getInstance();
			cl.setTime(myDate);

			int year = cl.get(Calendar.YEAR);
			int month = cl.get(Calendar.MONTH);
			int day = cl.get(Calendar.DAY_OF_MONTH);

			Calendar c = Calendar.getInstance();
			c.setLenient(false);
			c.set(year, month, day);

			c.getTime();
		} catch (Exception e) {
			iValid = false;
		}

		return iValid;
	}

	/**
	 * @param date :
	 *            date to convert
	 * @param pattern :
	 *            pattern or format of date
	 * @return date string
	 */
	public static String dateToString(Date date) {
		if(date != null){
			String pattern = StrimUtil.getGlobalConfigPropertie("ddMMyyyy.format");
			/* Formatting Date -> String */
			return (new SimpleDateFormat(pattern)).format(date);
		}

		return null;
	}

	/**
	 * @param date
	 * @return
	 */
	public static String dateTimeToString(Date date) {
		if(date != null){
			String pattern = StrimUtil.getGlobalConfigPropertie("ddMMyyyy.full.format");
			return (new SimpleDateFormat(pattern)).format(date);
		}
		return null;
	}

	/**
	 * @param date :
	 *            date to convert
	 * @param pattern :
	 *            pattern or format of date
	 * @return date string
	 */
	public static String dateToString(Date date, String pattern) {
		if(date != null){
			/* Formatting Date -> String */
			return (new SimpleDateFormat(pattern)).format(date);
		}

		return null;
	}

	/**
	 * Convert string to date
	 *
	 * @param dateString :
	 *            date string to convert
	 * @param pattern :
	 *            pattern or format of date
	 * @return date
	 */
	public static Date stringToDate(String dateString, String pattern) throws RuntimeException {
		if(StringUtil.isEmpty(dateString) || (dateString.indexOf("_") != -1)){
			return null;
		}
		/* Parsing String -> Date */
		Date date = null;
		SimpleDateFormat fd = new SimpleDateFormat(pattern);
		fd.setLenient(false);
		try{
			date = fd.parse(dateString);
		} catch (Exception e) {
			fd.setLenient(true);
			try{	
				date = fd.parse(dateString);
			} catch (Exception e2) {	
				throw new RuntimeException(e2);
			}
		}

		return date;
	}

	/**
	 * Convert string to date
	 *
	 * @param dateString :
	 *            date string to convert
	 * @param pattern :
	 *            pattern or format of date
	 * @return date
	 * @throws Exception
	 */
/*	public static Date stringToDate(String dateString, String pattern) throws Exception {
		if(StringUtil.isEmpty(dateString)){
			return null;
		}
		/* Parsing String -> Date */
	/*	SimpleDateFormat fd = new SimpleDateFormat(pattern);
		fd.setLenient(false);

		return fd.parse(dateString);
	}*/

	/**
	 * @param dateString
	 * @return
	 * @throws Exception
	 */
	public static Date stringToDate(String dateString) throws RuntimeException {
		String pattern = StrimUtil.getGlobalConfigPropertie("ddMMyyyy.format");
		return stringToDate(dateString, pattern);
	}

	/**
	 * @param date
	 * @return
	 */
	public static String getDefaultFormattedDate(Date date){
		if(date != null){
			String format = StrimUtil.getGlobalConfigPropertie("ddMMyyyy.format");
			return DateUtil.dateToString(date, format);
		}

		return null;
	}

	public static Calendar getCalendar(Date date){
		if(date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	/**
	 * @param dayCount
	 * @return
	 */
	public static Date addSubstractDate(Date date, TIME_ENUM time, int value){
		Calendar cal = getCalendar(date);
		switch(time){
			case SECOND : cal.add(Calendar.SECOND, value); break;
			case MINUTE : cal.add(Calendar.MINUTE, value); break;
			case HOUR 	: cal.add(Calendar.HOUR, value); break;
			case DAY 	: cal.add(Calendar.DATE, value); break;
			case WEEK 	: cal.add(Calendar.WEEK_OF_MONTH, value); break;
			case MONTH 	: cal.add(Calendar.MONTH, value); break;
			case YEAR 	: cal.add(Calendar.YEAR, value); break;
		}
		return cal.getTime();
	}

	/**
	 * @param dayCount
	 * @return
	 */
	public static Date setDetailDate(Date date, TIME_ENUM time, int value){
		Calendar cal = getCalendar(date);
		switch(time){
			case SECOND : cal.set(Calendar.SECOND, value); break;
			case MINUTE : cal.set(Calendar.MINUTE, value); break;
			case HOUR 	: cal.set(Calendar.HOUR_OF_DAY, value); break;
			case DAY 	: cal.set(Calendar.DATE, value); break;
			case WEEK 	: cal.set(Calendar.WEEK_OF_MONTH, value); break;
			case MONTH 	: cal.set(Calendar.MONTH, value); break;
			case YEAR 	: cal.set(Calendar.YEAR, value); break;
		}
		return cal.getTime();
	}

	/**
	 * @param date
	 * @return
	 */
	public static int getMinWeekDate(Date date){
		return getCalendar(date).getActualMinimum(Calendar.DAY_OF_WEEK);
	}
	public static int getMaxWeekDate(Date date){
		return getCalendar(date).getActualMaximum(Calendar.DAY_OF_WEEK);
	}
	public static int getMaxMonthDate(Date date){
		return getCalendar(date).getActualMaximum(Calendar.DAY_OF_MONTH);
	}

    /**
     * @param date
     * @return
     */
    public static Date getPreviousDayDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        return new Date(cal.getTimeInMillis());
    }

    /**
     * Get next day date
     * @param UI_Date date
     * @return Date
     */
    public static Date getNextDayDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);
        return new Date(cal.getTimeInMillis());
    }

    public static int getFirstDayOfWeek(Date date){
    	return getFirstDayOfWeek(getCalendar(date));
    }

    public static int getFirstDayOfWeek(Calendar calendar){
    	Calendar cal = (Calendar)calendar.clone();

	    while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
	    	cal.add(Calendar.DATE, -1);
	    }

	    return cal.get(Calendar.DATE);
    }
    

	/**
	 *   d - day of month (no leading zero)
		dd - day of month (two digit)
	 	 o - day of the year (no leading zeros)
		oo - day of the year (three digit)
		 D - day name short
		DD - day name long
		 m - month of year (no leading zero)
		mm - month of year (two digit)
		 M - month name short
		MM - month name long
		 y - year (two digit)
		yy - year (four digit)
	 * @param javaPattern
	 * @return
	 */
	public static String javaPatternToPckerPattern(String javaPattern){
		//
		if(javaPattern.equals(StrimUtil.getGlobalConfigPropertie("ddMMyyyy.format"))){
			return "dd/mm/yy";
		} else if(javaPattern.equals(StrimUtil.getGlobalConfigPropertie("yyyymmdd.format"))){
			return "yy/mm/dd";
		} else if(javaPattern.equals(StrimUtil.getGlobalConfigPropertie("ddmmyy.format"))){
			return "dd/mm/yy";
		}

		return "";
	}
	
	/**
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getDiffDays(Date startDate, Date endDate){ 
//		long startTime = startDate.getTime();
//		long endTime = endDate.getTime();
//		long diffTime = endTime - startTime;
//		long diffDays = diffTime / (1000 * 60 * 60 * 24);
		
		long diff = endDate.getTime() - startDate.getTime();
		Long diffDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
		
//		return Integer.valueOf(Long.toString(diffDays));
		return diffDays.intValue();
	}
	
	/**
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getDiffMonth(Date startDate, Date endDate){
		Calendar startCalendar = new GregorianCalendar();
		startCalendar.setTime(startDate);
		Calendar endCalendar = new GregorianCalendar();
		endCalendar.setTime(endDate);
		int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
		
		int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
		
		return diffMonth;
	}
	
	/**
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getDiffYear(Date startDate, Date endDate){
		Calendar startCalendar = new GregorianCalendar();
		startCalendar.setTime(startDate);
		Calendar endCalendar = new GregorianCalendar();
		endCalendar.setTime(endDate);

		int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
		
		return diffYear; 
	}
	
	/**
     * 
     * @param startDate
     * @param endDate
     * @return 
     */
    public static int getDiffSeconds(Date startDate, Date endDate){
        long diff = endDate.getTime() - startDate.getTime();
	Long diffDays = TimeUnit.SECONDS.convert(diff, TimeUnit.MILLISECONDS);
	return diffDays.intValue();
    }
    
    public static int getDiffHours(Date startDate, Date endDate){
        long diff = endDate.getTime() - startDate.getTime();
	Long diffDays = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS);
	return diffDays.intValue();
    }
    
    /**
     * 
     * @param startDate
     * @param endDate
     * @return 
     */
    public static int getDiffMinuts(Date startDate, Date endDate){
        long diff = endDate.getTime() - startDate.getTime();
        Long diffDays = TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS);
        return diffDays.intValue();
    }
    
	public static String getShortDay(Date date){ 
		String[] joursCourts = new String[] {"","Di","Lu","Ma",	"Me","Je","Ve",	"Sa" };
		DateFormatSymbols monDFS = new DateFormatSymbols();
		monDFS.setShortWeekdays(joursCourts);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE", monDFS);
		
		if(date != null){
			return dateFormat.format(date);
		}
		
		return null;
	}
	
	/**
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isInDates(Date dateReference1, Date dateDebut2, Date dateReference2, Date dateFin2, boolean equals){
		if(equals){
			return (dateReference1.after(dateDebut2) || dateReference1.equals(dateDebut2)) && (dateReference2.before(dateFin2) || dateReference2.equals(dateFin2));
		} else{
			return (dateReference1.after(dateDebut2) && dateReference2.before(dateFin2));
		}
	}
}
