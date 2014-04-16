//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;


public class DateUtil {
    
    static Logger log = Logger.getLogger(DateUtil.class);
    																		// Correspondencia con la annotaciones								
    public static String ddSMMSYYYY_MASK = "dd/MM/yyyy";  					//DDsMMsYYYY_MASK.java
    public static String dd_MM_YYYY_MASK = "dd-MM-yyyy"; 					//DD_MM_YYYY_MASK.java
    public static String ddMMYYYY_MASK   = "ddMMyyyy";  					//DDMMYYYY_MASK.java
    public static String YYYYMM_MASK   	 = "yyyyMM"; 						//YYYYMM_MASK.java
    public static String YYYYMMDD_MASK   	 = "yyyyMMdd"; 					//YYYYMMDD_MASK.java
    
    public static String ddSMMSYY_MASK   = "dd/MM/yy";  					//DDsMMsYY_MASK.java
    public static String dd_MM_YY_MASK   = "dd-MM-yy";  					//DD_MM_YY_MASK.java
    public static String ddMMYY_MASK     = "ddMMyy"; 						//DDMMYY_MASK.java
    public static String yyyy_MM_dd_MASK = "yyyy-MM-dd"; 					//YYYY_MM_DD_MASK.java
    public static String yyyy_MM_dd_HH_MM_SS_MASK = "yyyy-MM-dd HH:mm:ss";	//YYYY_MM_DD_HH_MM_SS_MASK.java
    public static String ddSMMSYYYY_HH_MM_MASK = "dd/MM/yyyy HH:mm"; 		//DDsMMsYYYY_HH_MM_MASK.java
    
    public static String HOUR_MINUTE_MASK = "HH:mm";  						//HOUR_MINUTE_MASK.java
    public static String MINUTE_MASK = "mm"; 								//MINUTE_MASK.java
	public static String yyMMdd_MASK = "yyMMdd";
	public static String MMSddSyy= "MM/dd/yy";
	
    public static boolean isValidDate(String dateToValidate){
    	
    	return (getDate(dateToValidate) != null);
    }

    public static Date getDate(String dateToValidate){
    	Date result = null;
    	
    	String funcName = "getDate";
    	
    	// dd/MM/yyyy mask    	
		try {
			if (dateToValidate.length()==10) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				sdf.setLenient( false );
				result = sdf.parse(dateToValidate);
				if (log.isDebugEnabled()) log.debug(funcName + " - mask dd/MM/yyyy - sdf.parse: " +  result + " locale:" + Locale.getDefault());
				if (result !=null) {
					return result;
				}
			}
		} catch (Exception e) {
			result = null;
		}
		
    	// dd/MM/yy mask
		try {
			if (dateToValidate.length()==8) {
				String mesDiaStr = dateToValidate.substring(0, 6);
				String aniosStr  = dateToValidate.substring(6);
				String preAnioStr = "19";
				
				Integer anios = Integer.parseInt(aniosStr);
				
				Calendar fecActual = new GregorianCalendar();
				int anioActual = fecActual.get(Calendar.YEAR);
				
				if (anios.intValue() <= anioActual - 2000){
					preAnioStr = "20";
				}
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				sdf.setLenient( false );
				result = sdf.parse(mesDiaStr.concat(preAnioStr).concat(aniosStr));
				if (log.isDebugEnabled()) log.debug(funcName + " - mask dd/MM/yyyy - sdf.parse: " +  result + " locale:" + Locale.getDefault());
				if (result !=null) {
					return result;
				}
			}
		} catch (Exception e) {
			result = null;
		}
		
		// MM/dd/yy mask
		try {
			if (dateToValidate.length()==8) {
				String mesDiaStr = dateToValidate.substring(0, 6);
				String aniosStr  = dateToValidate.substring(6);
				String preAnioStr = "19";
				
				Integer anios = Integer.parseInt(aniosStr);
				
				Calendar fecActual = new GregorianCalendar();
				int anioActual = fecActual.get(Calendar.YEAR);
				
				if (anios.intValue() <= anioActual - 2000){
					preAnioStr = "20";
				}
				
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
				sdf.setLenient( false );
				result = sdf.parse(mesDiaStr.concat(preAnioStr).concat(aniosStr));
				if (log.isDebugEnabled()) log.debug(funcName + " - mask dd/MM/yyyy - sdf.parse: " +  result + " locale:" + Locale.getDefault());
				if (result !=null) {
					return result;
				}
			}
		} catch (Exception e) {
			result = null;
		}

    	// dd-MM-yyyy mask
		try {
			if (dateToValidate.length()==10) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				sdf.setLenient( false );
				result = sdf.parse(dateToValidate);
				if (log.isDebugEnabled()) log.debug(funcName + " - mask dd-MM-yyyy - sdf.parse: " +  result + " locale:" + Locale.getDefault());
				if (result !=null) {
					return result;
				}
			}
		} catch (Exception e) {
			result = null;
		}
		
    	// dd-MM-yy mask
		try {
			if (dateToValidate.length()==8) {
				
				String mesDiaStr = dateToValidate.substring(0, 6);
				String aniosStr  = dateToValidate.substring(6);
				String preAnioStr = "19";
				
				Integer anios = Integer.parseInt(aniosStr);
				
				Calendar fecActual = new GregorianCalendar();
				int anioActual = fecActual.get(Calendar.YEAR);
				
				if (anios.intValue() <= anioActual - 2000){
					preAnioStr = "20";
				}
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				sdf.setLenient( false );
				result = sdf.parse(mesDiaStr.concat(preAnioStr).concat(aniosStr));
				if (log.isDebugEnabled()) log.debug(funcName + " - mask dd-MM-yyyy - sdf.parse: " +  result + " locale:" + Locale.getDefault());
				if (result !=null) {
					return result;
				}
			}
		} catch (Exception e) {
			result = null;
		}

    	// ddMMyyyy mask
		try {
			if (dateToValidate.length()==8) {
				SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
				sdf.setLenient( false );
				result = sdf.parse(dateToValidate);
				if (log.isDebugEnabled()) log.debug(funcName + " - mask ddMMyyyy - sdf.parse: " +  result + " locale:" + Locale.getDefault());
				if (result !=null) {
					return result;
				}
			}
		} catch (Exception e) {
			result = null;
		}
		
		// ddMMyy mask 
		try {
			if (dateToValidate.length()==6) {
				// 301105
				String mesDiaStr = dateToValidate.substring(0, 4);
				String aniosStr  = dateToValidate.substring(4);
				String preAnioStr = "19";
				
				Integer anios = Integer.parseInt(aniosStr);
				
				Calendar fecActual = new GregorianCalendar();
				int anioActual = fecActual.get(Calendar.YEAR);
				
				if (anios.intValue() <= anioActual - 2000){
					preAnioStr = "20";
				}
				
				SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
				
				sdf.setLenient( false );
				result = sdf.parse(mesDiaStr.concat(preAnioStr).concat(aniosStr));
				if (log.isDebugEnabled()) log.debug(funcName + " - mask ddMMyy - sdf.parse: " +  result + " locale:" + Locale.getDefault());
				if (result !=null) {
				return result;
				}
			}
		} catch (Exception e) {
			result = null;
		}
    	
    	return result;
    }

    public static String formatDate(Date dateToFormat, String dateMask){
    	String result;
    	
    	String funcName = "formatDate";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(dateMask);
			sdf.setLenient( false );
			result = sdf.format(dateToFormat);
			if (log.isDebugEnabled()) log.debug(funcName + " - mask " + dateMask + " - sdf.format: " +  result + " locale:" + Locale.getDefault());
			if (result !=null) {
				return result.toString();
			}
		} catch (Exception e) {
			result = "";
		}
		
		return result;
    }
    
    /** Valida si dateToValidate este en el intervalo entre dateFrom y dateTo. 
	 *  <p><b>Importante:</b>
	 *  <p>Si dateTo es null, se valida que dateToValidate sea mayor que dateFrom.
	 *  @param Date dateToValidate, Date dateFrom, Date dateTo
	 *  @return boolean
	 */
    public static boolean isDateInRange(Date dateToValidate, Date dateFrom, Date dateTo){
    	boolean result = false;
    	
    	//Para el caso que dateTo es null si dateToValidate es mayor a dateFrom se da por validada.
    	if (dateTo == null) {
    		if ( isDateBeforeOrEqual(dateFrom, dateToValidate) )  {
    			result = true;
    		}
    	}
    	if (dateTo != null && !isDateEqual(dateFrom, dateTo) ) {
    		if ( isDateBeforeOrEqual(dateFrom, dateToValidate)
    			&& isDateAfterOrEqual(dateTo, dateToValidate) )  {
    			result = true;
    		}
    		
    	}
    	
    	return result;
    }

    public static boolean isDateAfter(Date dateToValidate, Date dateToCompare){
    	boolean result = false;
    	
    	int dateCompareResult = dateCompare(dateToValidate, dateToCompare);
    	if ( dateCompareResult > 0 ) {
    		result = true;
    	}
    	
    	return result;
    }

    public static boolean isDateAfterOrEqual(Date dateToValidate, Date dateToCompare){
    	boolean result = false;
    	
    	int dateCompareResult = dateCompare(dateToValidate, dateToCompare);
    	if ( dateCompareResult >= 0 ) {
    		result = true;
    	}
    	
    	return result;
    }

    public static boolean isDateEqual(Date dateToValidate, Date dateToCompare){
    	boolean result = false;
    	
    	int dateCompareResult = dateCompare(dateToValidate, dateToCompare);
    	if ( dateCompareResult == 0 ) {
    		result = true;
    	}
    	
    	return result;
    }

    public static boolean isDateBefore(Date dateToValidate, Date dateToCompare){
    	boolean result = false;
    	
    	int dateCompareResult = dateCompare(dateToValidate, dateToCompare);
    	if ( dateCompareResult < 0 ) {
    		result = true;
    	}
    	
    	return result;
    }

    public static boolean isDateBeforeOrEqual(Date dateToValidate, Date dateToCompare){
    	boolean result = false;
    	
    	int dateCompareResult = dateCompare(dateToValidate, dateToCompare);
    	if ( dateCompareResult <= 0 ) {
    		result = true;
    	}
    	
    	return result;
    }
    
	/**
	 * Compara las dos fechas sin tener encuenta la hora.
	 * Retorna -1 si dateToValidate es Menor que dateToCompara
	 * Retorna  0 si dateToValidate es Igual que dateToCompara
	 * Retorna  1 si dateToValidate es Mayor que dateToCompara
	 */
    public static int dateCompare(Date dateToValidate, Date dateToCompare){
    	int FECHA_ERROR = -2;
    	int FECHA_MENOR = -1;
    	int FECHA_IGUAL = 0;
    	int FECHA_MAYOR = 1;

    	int result = FECHA_ERROR;
    	
    	try {
    		Calendar calendarToValidate = Calendar.getInstance();
    		calendarToValidate.setTime(dateToValidate);
    		calendarToValidate.set(Calendar.AM_PM,0);
    		calendarToValidate.set(Calendar.HOUR, 0);
    		calendarToValidate.set(Calendar.MINUTE, 0);
    		calendarToValidate.set(Calendar.SECOND, 0);
    		calendarToValidate.set(Calendar.MILLISECOND, 0);
    	
    		Calendar calendarToCompare = Calendar.getInstance();
    		calendarToCompare.setTime(dateToCompare);
    		calendarToCompare.set(Calendar.AM_PM,0);
    		calendarToCompare.set(Calendar.HOUR, 0);
    		calendarToCompare.set(Calendar.MINUTE, 0);
    		calendarToCompare.set(Calendar.SECOND, 0);
    		calendarToCompare.set(Calendar.MILLISECOND, 0);

    		switch (calendarToValidate.compareTo(calendarToCompare)) {
    			case -1:
    				result = FECHA_MENOR;
    				break;
    			case 0:
    				result = FECHA_IGUAL;
    				break;
    			case 1:
    				result = FECHA_MAYOR;
    				break;
    		}
     	} 
    	catch (Exception exception) {
    		exception.printStackTrace();
    	}
    	
    	return result;
    }

    /**
     * Obtiene un Array de Integer de 3 elementos: anios, meses y dias de la edad sobre la Fecha de Nacimiento
     * @author tomado del Groso Marcos Lewis
     * @param fechaNacimiento
     * @return Integer[] anios, meses, d'ias
     */
    public static Integer[] getEdad(Date fechaNacimiento) {

    	Integer[] edad = new Integer[3]; // anios, meses, d'ias

    	if (fechaNacimiento != null) {
    		Calendar cNac = new GregorianCalendar();
    		cNac.setTime(fechaNacimiento);
    		cNac.set(Calendar.HOUR, 0);
    		cNac.set(Calendar.MINUTE, 0);
    		cNac.set(Calendar.SECOND, 0);
    		cNac.set(Calendar.MILLISECOND, 0);

    		Calendar cActual = new GregorianCalendar();
    		cActual.set(Calendar.HOUR, 0);
    		cActual.set(Calendar.MINUTE, 0);
    		cActual.set(Calendar.SECOND, 0);
    		cActual.set(Calendar.MILLISECOND, 0);

    		int anioActual = cActual.get(Calendar.YEAR);
    		int anioNac = cNac.get(Calendar.YEAR);
    		int mesActual = cActual.get(Calendar.MONTH);
    		int mesNac = cNac.get(Calendar.MONTH);
    		int diaActual = cActual.get(Calendar.DAY_OF_MONTH);
    		int diaNac = cNac.get(Calendar.DAY_OF_MONTH);

    		int anios = anioActual - anioNac;
    		int meses = mesActual - mesNac;
    		int dias = diaActual - diaNac;

    		if (dias < 0) {

    			// Para el anio bisiesto le ponemos 29 dias a febrero
    			int diasdelmes[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    			// Las condiciones de un anio bisiesto:
    			// Un anio es bisiesto si es divisible por 4, excepto aquellos divisibles por 100 pero no por 400.
    			if ((anioNac % 4 == 0) && ((anioNac % 100 != 0) || (anioNac % 400 == 0))) {
    				diasdelmes[1] = 29;
    			}

    			meses -= 1;
    			dias += diasdelmes[mesNac];
    		}
    		if (meses < 0) {
    			meses += 12;
    			anios -= 1;
    		}
    		if (anios < 0) {
    			log.error("anios menor a cero");
    			return null;
    		}
    		edad[0] = anios;
    		edad[1] = meses;
    		edad[2] = dias;

    		return edad;

    	}else{
    		log.error("fecha de nacimiento nula");
    		return null;
    	}

    }
    
    /**
     * Obtiene la Edad a partir de una cantidad de meses.
     * @author David
     * @param  meses
     * @return Integer[]
     */
    public static Integer[] getEdad(Integer meses) {

    	Integer[] edad = new Integer[3]; // anios, meses, d'ias

    	if(meses == null){
    		return edad;
    	}
    	
    	int anios = meses.intValue() / 12;
    	int m    = meses.intValue() - anios *  12;
    	
    	edad[0] = anios;
   		edad[1] = m;
   		edad[2] = 0;

   		return edad;
    }

    
    public static boolean isValidTime(String timeToValidate, String timeMask){
    	boolean result = false;
    	
    	String funcName = "isValidTime";
    	
    	try {
			if (timeToValidate.length()==timeMask.length()) {
				SimpleDateFormat sdf = new SimpleDateFormat(timeMask);
				sdf.setLenient( false );
				Date date = sdf.parse(timeToValidate);
				if (log.isDebugEnabled()) log.debug(funcName + " - mask " + timeMask + " - sdf.parse: " +  date + " locale:" + Locale.getDefault());
				if (date !=null) {
					return true;
				}
			}
			
		} catch (Exception e) {
			
		}
		
		return result;
    }

    public static Date getTime(String timeToValidate, String timeMask){
    	Date result = null;
    	
    	String funcName = "getTime";
    	
		try {
			if (timeToValidate.length()==timeMask.length()) {
				SimpleDateFormat sdf = new SimpleDateFormat(timeMask);
				sdf.setLenient( false );
				result = sdf.parse(timeToValidate);
				if (log.isDebugEnabled()) log.debug(funcName + " - mask " + timeMask + " - sdf.parse: " +  result + " locale:" + Locale.getDefault());
				if (result !=null) {
					return result;
				}
			}
		} catch (Exception e) {
			result = null;
		}
		
    	return result;
    }

    public static boolean isValidDate(String dateToValidate, String dateMask){
    	boolean result = false;
    	
    	String funcName = "isValidDate";
    	
		try {
			if (dateToValidate.length()==dateMask.length()) {
				SimpleDateFormat sdf = new SimpleDateFormat(dateMask);
				sdf.setLenient( false );
				Date date = sdf.parse(dateToValidate);
				if (log.isDebugEnabled()) log.debug(funcName + " - mask " + dateMask + " - sdf.parse: " +  date + " locale:" + Locale.getDefault());
				if (date !=null) {
					return true;
				}
			}
		} catch (Exception e) {
			
		}
		
    	return result;
    }

    public static Date getDate(String dateToValidate, String dateMask){
    	Date result = null;
    	
    	String funcName = "getDate";
    	
		try {
			if (dateToValidate.length()==dateMask.length()) {
				SimpleDateFormat sdf = new SimpleDateFormat(dateMask);
				sdf.setLenient( false );
				result = sdf.parse(dateToValidate);
				if (log.isDebugEnabled()) log.debug(funcName + " - mask " + dateMask + " - sdf.parse: " +  result + " locale:" + Locale.getDefault());
				if (result !=null) {
					return result;
				}
			}
		} catch (Exception e) {
			result = null;
		}
		
    	return result;
    }

    private static int timeCompare(Date dateToValidate, Date dateToCompare){
    	int FECHA_ERROR = -2;
    	int FECHA_MENOR = -1;
    	int FECHA_IGUAL = 0;
    	int FECHA_MAYOR = 1;

    	int result = FECHA_ERROR;
    	
    	try {
    		Calendar calendarToValidate = Calendar.getInstance();
    		calendarToValidate.setTime(dateToValidate);
    		calendarToValidate.set(Calendar.DAY_OF_MONTH, 0);
    		calendarToValidate.set(Calendar.MONTH, 0);
    		calendarToValidate.set(Calendar.YEAR, 0);
    		calendarToValidate.set(Calendar.SECOND, 0);
    		calendarToValidate.set(Calendar.MILLISECOND, 0);
    	
    		Calendar calendarToCompare = Calendar.getInstance();
    		calendarToCompare.setTime(dateToCompare);
    		calendarToCompare.set(Calendar.DAY_OF_MONTH, 0);
    		calendarToCompare.set(Calendar.MONTH, 0);
    		calendarToCompare.set(Calendar.YEAR, 0);    		
    		calendarToCompare.set(Calendar.SECOND, 0);
    		calendarToCompare.set(Calendar.MILLISECOND, 0);

    		switch (calendarToValidate.compareTo(calendarToCompare)) {
    			case -1:
    				result = FECHA_MENOR;
    				break;
    			case 0:
    				result = FECHA_IGUAL;
    				break;
    			case 1:
    				result = FECHA_MAYOR;
    				break;
    		}
     	} 
    	catch (Exception exception) {
    		exception.printStackTrace();
    	}
    	
    	return result;
    }

    public static boolean isTimeAfter(Date dateToValidate, Date dateToCompare){
    	boolean result = false;
    	
    	int dateCompareResult = timeCompare(dateToValidate, dateToCompare);
    	if ( dateCompareResult > 0 ) {
    		result = true;
    	}
    	
    	return result;
    }

    public static boolean isTimeAfterOrEqual(Date dateToValidate, Date dateToCompare){
    	boolean result = false;
    	
    	int dateCompareResult = timeCompare(dateToValidate, dateToCompare);
    	if ( dateCompareResult >= 0 ) {
    		result = true;
    	}
    	
    	return result;
    }

    public static boolean isTimeEqual(Date dateToValidate, Date dateToCompare){
    	boolean result = false;
    	
    	int dateCompareResult = timeCompare(dateToValidate, dateToCompare);
    	if ( dateCompareResult == 0 ) {
    		result = true;
    	}
    	
    	return result;
    }

    public static boolean isTimeBefore(Date dateToValidate, Date dateToCompare){
    	boolean result = false;
    	
    	int dateCompareResult = timeCompare(dateToValidate, dateToCompare);
    	if ( dateCompareResult < 0 ) {
    		result = true;
    	}
    	
    	return result;
    }

    public static boolean isTimeBeforeOrEqual(Date dateToValidate, Date dateToCompare){
    	boolean result = false;
    	
    	int dateCompareResult = timeCompare(dateToValidate, dateToCompare);
    	if ( dateCompareResult <= 0 ) {
    		result = true;
    	}
    	
    	return result;
    }
    
    public static Date addDaysToDate (Date fecha, Integer days) {
    	Calendar fechaC = new GregorianCalendar();
    	fechaC.setTime(fecha);
    	fechaC.add(Calendar.DAY_OF_MONTH, days.intValue());
    	return fechaC.getTime();
    }

    public static Date addWeeksToDate (Date fecha, Integer weeks) {
    	Calendar fechaC = new GregorianCalendar();
    	fechaC.setTime(fecha);    	
    	fechaC.add(Calendar.WEEK_OF_YEAR, weeks);
    	return fechaC.getTime();
    }
    
    public static Date addMinutesToDate (Date fecha, Integer minutes) {
    	Calendar fechaC = new GregorianCalendar();
    	fechaC.setTime(fecha);
    	fechaC.add(Calendar.MINUTE, minutes.intValue());
    	return fechaC.getTime();
    }
    
    public static Date addMonthsToDate (Date fecha, Integer months) {
    	Calendar fechaC = new GregorianCalendar();
    	fechaC.setTime(fecha);
    	fechaC.add(Calendar.MONTH, months.intValue());
    	return fechaC.getTime();
    }
    
    public static Date addYearsToDate (Date fecha, Integer years) {
    	Calendar fechaC = new GregorianCalendar();
    	fechaC.setTime(fecha);
    	fechaC.add(Calendar.YEAR, years.intValue());
    	return fechaC.getTime();
    }
    
    // Devuelve la misma fecha, pero con HH:mm:ss = 00:00:00
    public static Date getDateFromDate (Date fecha) {
    	return getDate(formatDate(fecha, "dd/MM/yyyy"));
    }

    // Devuelve la hora de una fecha
    public static Date getTimeFromDate (Date fecha) {
    	String dateStr = DateUtil.formatDate(fecha, "HH:mm");
    	return getTime(dateStr, DateUtil.HOUR_MINUTE_MASK);
    }
    
    
    // Devuelve la fecha correspondiente al Domingo de la semana de "fecha"
    public static Date getFirstDayOfWeek(Date fecha) {
    	Calendar fechaC = new GregorianCalendar();
    	fechaC.setTime(fecha);
    	
    	while (fechaC.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
    		fechaC.add(Calendar.DAY_OF_MONTH, -1 );	
		}
    	return getDateFromDate (fechaC.getTime());
    }

    public static Date getLastDayOfWeek(Date fecha) {
    	Calendar fechaC = new GregorianCalendar();
    	fechaC.setTime(fecha);
    	
    	while (fechaC.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
    		fechaC.add(Calendar.DAY_OF_MONTH, 1 );	
		}
    	return getDateFromDate (fechaC.getTime());
    }
    
    // Devuelve la fecha correspondiente al primer dia del mes de la fecha
    public static Date getFirstDayOfMonth(Date fecha) {
    	Calendar fechaC = new GregorianCalendar();
    	fechaC.setTime(fecha);
    	fechaC.set(Calendar.DAY_OF_MONTH, 1);
    	return getDateFromDate (fechaC.getTime());
    }
    
	public static Date getFirstDatOfMonth(Integer month, Integer year) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.YEAR, year);
		return  calendar.getTime();
	}

    // Devuelve la fecha correspondiente al primer dia del mes de la fecha ACTUAL
    public static Date getFirstDayOfMonth() {
    	return DateUtil.getFirstDayOfMonth(new Date());
    }

    
    public static Date getLastDayOfMonth() {
    	Calendar fechaC = new GregorianCalendar();
    	
    	fechaC.setTime(getFirstDayOfMonth());
    	
    	fechaC.add(Calendar.MONTH, 1);
    	    	
    	fechaC.add(Calendar.DAY_OF_MONTH, -1);
    	    	
    	return getDateFromDate (fechaC.getTime());
    }
    
    public static Date getLastDayOfMonth(Date fecha) {
    	Calendar fechaC = new GregorianCalendar();
    	
    	fechaC.setTime(getFirstDayOfMonth(fecha));
    	
    	fechaC.add(Calendar.MONTH, 1);
    	    	
    	fechaC.add(Calendar.DAY_OF_MONTH, -1);
    	    	
    	return getDateFromDate (fechaC.getTime());
    }
    
    
    public static long getMinutes(Date fechaDesde, Date fechaHasta){
    	
    	Calendar fechaDesdeCal = Calendar.getInstance();
    	fechaDesdeCal.setTime(fechaDesde);
    	Calendar fechaHastaCal = Calendar.getInstance();
    	fechaHastaCal.setTime(fechaHasta);
    	
    	long horaHasta = fechaHastaCal.getTimeInMillis() - fechaDesdeCal.getTimeInMillis();
    	
    	return (horaHasta / (1000 * 60));
    }
    

    /**
     * Devuelve la fecha correspondiente al anio, mes y dia
     * En caso de error devuelve null 
     * @param anio
     * @param mes
     * @param dia
     * @return Date
     */
    public static Date getDate(int anio, int mes, int dia) {
    	Date result = null;
    	try {
    		Calendar fechaC = new GregorianCalendar();
        	fechaC.set(anio,mes,dia);
        	result =  getDateFromDate (fechaC.getTime());	
		} catch (Exception e) {
			result = null;
		}
    	return result;
    }
    
    /**
     * Obtiene el anio actual
     * @return int
     */
    public static int getAnioActual(){
    	Calendar fechaActual = new GregorianCalendar();
    	return fechaActual.get(Calendar.YEAR);
    }
    
    /**
     * Obtiene el anio del parametro fecha
     * @param fecha
     * @return int
     */
    public static int getAnio(Date fecha){
    	Calendar fechaC = new GregorianCalendar();
    	fechaC.setTime(fecha);
    	return fechaC.get(Calendar.YEAR);
    }
    
    /**
     * Obtiene el mes del parametro fecha
     * @param fecha
     * @return int
     */
    public static int getMes(Date fecha){
    	Calendar fechaC = new GregorianCalendar();
    	fechaC.setTime(fecha);
    	return fechaC.get(Calendar.MONTH)+1;
    }
    
    /**
     * Obtiene la lista de Fechas incluyendo los extremos del rango de fechas
     * 
     * @param fechaDesde
     * @param fechaHasta
     * @return List<Date>
     */
    public static List<Date> getListDate(Date fechaDesde, Date fechaHasta){
    	
    	List<Date> listDate = new ArrayList<Date>();

    	if(fechaDesde == null){
    		log.error("fechaDesde nula");
    		return null;
    	}
    	if(fechaHasta == null){
    		log.error("fechaHasta nula");
    		return null;
    	}
    	
    	if(DateUtil.isDateAfter(fechaDesde, fechaHasta)){
    		log.error("fechaDesde mayor a la fecha hasta");
    		return null;
    	}
    	
    	fechaDesde = DateUtil.getDateFromDate(fechaDesde);
    	fechaHasta = DateUtil.getDateFromDate(fechaHasta);
    	
    	while (DateUtil.isDateBeforeOrEqual(fechaDesde, fechaHasta)) {
    		listDate.add(fechaDesde);
    		fechaDesde = DateUtil.addDaysToDate(fechaDesde, new Integer(1));
		}
    	
    	return listDate;
    }
    
    /**
     * Obtiene la lista de Fechas del primer dia de cada mes del rango de fechas
     * 
     * @param fechaDesde
     * @param fechaHasta
     * @return List<Date>
     */
    public static List<Date> getListFirstDayEachMonth(Date fechaDesde, Date fechaHasta){
    	
    	List<Date> listDate = new ArrayList<Date>();

    	if(fechaDesde == null){
    		log.error("fechaDesde nula");
    		return null;
    	}
    	if(fechaHasta == null){
    		log.error("fechaHasta nula");
    		return null;
    	}
    	
    	if(DateUtil.isDateAfter(fechaDesde, fechaHasta)){
    		log.error("fechaDesde mayor a la fecha hasta");
    		return null;
    	}
    	
    	fechaDesde = DateUtil.getDateFromDate(fechaDesde);
    	fechaHasta = DateUtil.getDateFromDate(fechaHasta);
    	
    	fechaDesde=DateUtil.getFirstDayOfMonth(fechaDesde);
    	
    	
    	while (DateUtil.isDateBeforeOrEqual(fechaDesde, fechaHasta)) {
    		listDate.add(fechaDesde);
    		fechaDesde = DateUtil.addMonthsToDate(fechaDesde, new Integer(1));
		}
    	
    	return listDate;
    }
    
    
    /**
     *  Cuenta la cantidad de dias entre dos fechas.
     * 
     * @param fechaDesde
     * @param fechaHasta
     * @return cantDias
     */
    public static int getCantDias(Date fechaDesde, Date fechaHasta){
	  	List<Date> listDate = DateUtil.getListDate(fechaDesde, fechaHasta); 
	  	int cantDias = 0;
	  	for(Date date: listDate)
	  		cantDias++;
    	return (cantDias-1);
    }
  
    /**
     * Cuenta la cantidad de meses entre dos fechas.
     */
    public static double getCantMeses(Date fecha1, Date fecha2) {
    	double elapsed = -1; // Por defecto estaba en 0 y siempre asi no haya pasado un mes contaba 1)
    	
    	Calendar g1 = Calendar.getInstance();
    	g1.setTime(fecha1);
    	
    	Calendar g2 = Calendar.getInstance();
    	g2.setTime(fecha2);
    	
    	GregorianCalendar gc1, gc2;
    	
    	
    	if (g2.after(g1)) {
	    	gc2 = (GregorianCalendar) g2.clone();
	    	gc1 = (GregorianCalendar) g1.clone();
    	}
    	else {
	    	gc2 = (GregorianCalendar) g1.clone();
	    	gc1 = (GregorianCalendar) g2.clone();
    	}

    	boolean salir = false;
    	while(!salir){
    		if(gc1.before(gc2)){
    			gc1.add(Calendar.MONTH, 1);
    	    	elapsed++;
    		}else{
    			gc1.add(Calendar.MONTH, -1);//vuelve a la fecha anterior
    			Double cantDias = new Double(getCantDias(gc1.getTime(), gc2.getTime()));	    			
    			elapsed +=cantDias/30.44D; // segun google: 1 mes = 30,4368499 d�as
    			salir=true;
    		}
    	}

    	//if (gc1.get(Calendar.DATE)==(gc2.get(Calendar.DATE))) 
    //		elapsed++; // si es el mismo dia cuenta para la suma de meses
    	
    	return elapsed;
    }
    
    /**
     * Cuenta la cantidad de anios entre dos fechas.
     */
    public static double getCantAnios(Date fecha1, Date fecha2){
    	double elapsed = -1D; // Por defecto estaba en 0 y siempre asi no haya pasado un mes contaba 1)
    	
       	Calendar g1 = Calendar.getInstance();
    	g1.setTime(fecha1);
    	
    	Calendar g2 = Calendar.getInstance();
    	g2.setTime(fecha2);
    	
    	GregorianCalendar gc1, gc2;
    	
    	if (g2.after(g1)) {
	    	gc2 = (GregorianCalendar) g2.clone();
	    	gc1 = (GregorianCalendar) g1.clone();
    	}
    	else {
	    	gc2 = (GregorianCalendar) g1.clone();
	    	gc1 = (GregorianCalendar) g2.clone();
    	}

    	while ( gc1.before(gc2) ) {
    	gc1.add(Calendar.YEAR, 1);
    	elapsed++;
    	}

    	boolean salir = false;
    	
    	while(!salir){
    		if(gc1.before(gc2)){
    			gc1.add(Calendar.YEAR, 1);
    	    	elapsed++;
    		}else{
    			gc1.add(Calendar.YEAR, -1);//vuelve a la fecha anterior
    			Double cantDias = new Double(getCantDias(gc1.getTime(), gc2.getTime()));
    			elapsed +=cantDias/365D;
    			salir=true;
    		}
    	}
    		    	
    	return elapsed;
    }
    
    /**
     * Devuelve la diferencia entre las 2 fecha pasadas como parametro en el formato (dias, meses, anios) pasado como parametro
     * @param fecha1
     * @param fecha2
     * @param formato --> Acepta las constantes de la clase Calendar (DAY_OF_YEAR, MONTH, YEAR)
     * @return 0 si el formato no es alguno de estas 3 constantes
     */
    public static double getDifEntreFechas(Date fecha1, Date fecha2, int formato){
    	if(formato==Calendar.DAY_OF_YEAR){
    		if(isDateAfter(fecha1, fecha2))
    			return getCantDias(fecha2, fecha1);
    		return getCantDias(fecha1, fecha2);
    	}
    	if(formato==Calendar.MONTH)
    		return getCantMeses(fecha1, fecha2);
    	if(formato==Calendar.YEAR)
    		return getCantAnios(fecha1, fecha2);
    	return 0;
    }
    
    /**
     * Dada una fecha devuelve la misma en letra
     * Ej: 19 de marzo de 2008
     * 
     * @author Cristian
     * @param fecha
     * @return
     */
    public static String getDateEnLetras(Date fecha){
    	
    	String fechaStr = formatDate(fecha, ddSMMSYYYY_MASK);
    	
    	try {
		    String dia = fechaStr.substring(0,2); 
		    String mes = getMesEnLetra(fecha);	    
		    String anio = fechaStr.substring(6,10);
		    
	    	return  dia + " de " + mes + " de " + anio;
	    	
    	} catch (Exception e){
    		return "";
    	}
    }
    
    /**
     * Devuelve el nombre del Mes de la fecha pasada como parametro
     * @author Alejandro
     * @param fecha
     * @return
     */
    public static String getMesEnLetra(Date fecha){
    	String fechaStr = formatDate(fecha, ddSMMSYYYY_MASK);
    	
    	try {
		    String mes = fechaStr.substring(3,5);	    
	    	
		    if (mes.equals("01")){
		    	return "enero";
		    } else if (mes.equals("02")){
		    	return "febrero";
		    } else if (mes.equals("03")){
		    	return "marzo";
		    }else if (mes.equals("04")){
		    	return "abril";
		    }else if (mes.equals("05")){
		    	return "mayo";
		    }else if (mes.equals("06")){
		    	return "junio";
		    }else if (mes.equals("07")){
		    	return "julio";
		    }else if (mes.equals("08")){
		    	return "agosto";
		    }else if (mes.equals("09")){
		    	return "septiembre";
		    }else if (mes.equals("10")){
		    	return "octubre";
		    }else if (mes.equals("11")){
		    	return "noviembre";
		    }else if (mes.equals("12")){
		    	return "diciembre";
		    }
		    
    	} catch (Exception e){    		
    	}
    	return "";
    }
    
    /**
     * Formatea el resultado adecuado para los reportes
     * @param  fecha
     * @return String
     */
    public static String formatDateForReport(Date fecha){
    	String fechaFormateada = DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK);
    	
    	String dateHHmmss = DateUtil.formatDate(fecha, "HH:mm:ss");
    	
    	if("00:00:00".equals(dateHHmmss)){
    		return fechaFormateada;
    	}
    	
    	return fechaFormateada + " " + dateHHmmss;
    } 

	/**
	 * Devuelve true si y solo si el entero pasado como parametro corresponde a un mes valido
	 **/
	public static boolean isValidMes(Integer mes ) {
		if (mes <0 || mes>12)
			return false;
		
		return true;
	}

	/**
	 * Devuelve true si y solo si el entero pasado como parametro corresponde a un anio valido
	 **/
	public static boolean isValidAnio(Integer anio ) {
		return true;
	}
	
	 /**
     * Obtiene una lista de pares de Fechas que marcan el principio y fin de los rangos de meses por a�o calculados entre las fecha desde y hasta indicadas.
     * 
     * @param fechaDesde
     * @param fechaHasta
     * @return List<Date[]>
     */
    public static List<Date[]> getListDateRangeForYears(Date fechaDesde, Date fechaHasta){
    	
    	List<Date[]> listDateRange = new ArrayList<Date[]>();

    	if(fechaDesde == null){
    		log.error("fechaDesde nula");
    		return null;
    	}
    	if(fechaHasta == null){
    		log.error("fechaHasta nula");
    		return null;
    	}
    	
    	if(DateUtil.isDateAfter(fechaDesde, fechaHasta)){
    		log.error("fechaDesde mayor a la fecha hasta");
    		return null;
    	}
    	
    	fechaDesde = DateUtil.getDateFromDate(fechaDesde);
    	fechaHasta = DateUtil.getDateFromDate(fechaHasta);
    	
    	fechaDesde=DateUtil.getFirstDayOfMonth(fechaDesde);
    	
    	while (DateUtil.isDateBeforeOrEqual(fechaDesde, fechaHasta)) {
    		
    		Date fechaHastaIntervalo = DateUtil.getDate(DateUtil.getAnio(fechaDesde), 11, 1);
    		// Si la fecha hasta del intervalo calculada es mayor a la fecha hasta la reemplazamos por el 1er dia del mes de la fecha hasta.
    		if(DateUtil.isDateAfter(fechaHastaIntervalo, fechaHasta)){
    			fechaHastaIntervalo = DateUtil.getFirstDayOfMonth(fechaHasta);
    		}
    		// Si la fecha desde es menor a la fecha hasta del intervalo en analisis agregamos el rango a la lista
    		if(DateUtil.isDateBeforeOrEqual(fechaDesde, fechaHastaIntervalo)){
    			Date[] rango = new Date[2];
    			rango[0] = fechaDesde;
    			rango[1] = fechaHastaIntervalo;
    			
    			listDateRange.add(rango);
    			fechaDesde = DateUtil.getDate(DateUtil.getAnio(fechaDesde)+1, 0, 1);
    		}
		}
    	
    	return listDateRange;
    }
    
    
}
