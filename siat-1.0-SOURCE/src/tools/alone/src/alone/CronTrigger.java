//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package alone;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author leonardo
 *
 */
public class CronTrigger {
	
	private static final Map<String,String> dayOfWeekMap;
	static {
		Map<String,String> map = new HashMap<String, String>();
		map.put("SUN", "1");
		map.put("MON", "2");
		map.put("TUE", "3");
		map.put("WED", "4");
		map.put("THU", "5");
		map.put("FRI", "6");
		map.put("SAT", "7");
		map.put("0", "1");
		map.put("1", "2");
		map.put("2", "3");
		map.put("3", "4");
		map.put("4", "5");
		map.put("5", "6");
		map.put("6", "7");	
		map.put("7", "1");
		dayOfWeekMap = Collections.unmodifiableMap(map);
	}
	
	private static final Map<String,String> monthMap;
	static {
		Map<String,String> map = new HashMap<String, String>();
		map.put("JAN", "0");
		map.put("FEB", "1");
		map.put("MAR", "2");
		map.put("APR", "3");
		map.put("MAY", "4");
		map.put("JUN", "5");
		map.put("JUL", "6");
		map.put("AUG", "7");
		map.put("SEP", "8");
		map.put("OCT", "9");
		map.put("NOV", "10");
		map.put("DEC", "11");
		map.put("01", "0");
		map.put("02", "1");
		map.put("03", "2");
		map.put("04", "3");
		map.put("05", "4");
		map.put("06", "5");	
		map.put("07", "6");
		map.put("08", "7");
		map.put("09", "8");
		map.put("10", "9");
		map.put("11", "10");
		map.put("12", "11");
		monthMap = Collections.unmodifiableMap(map);
	}
	
	public static void main(String[] args) throws Exception {
		
		CronTrigger ct = new CronTrigger();
		
//		Alone.init(false, false);

		List<String> listExpression = new ArrayList<String>();
		
		listExpression.add("0 23 * 1,2,3 sun");
		listExpression.add("0 23 * 1-10 *");
		listExpression.add("0 23 * 2 0");
		listExpression.add("0 23 * 2 7");
		listExpression.add("0 23 3 2 *");
		listExpression.add("0 1-23 * * 5");
		listExpression.add("* 1-22/1 * * *");
		listExpression.add("*/1 */1 */1 feb,sep,dec sun/1");
		listExpression.add("* * * * *");
//		listExpression.add("");
//		listExpression.add("");
		
	
		/*
		 * 	year the year minus 1900.
	month the month between 0-11.
	date the day of the month between 1-31.
	hrs the hours between 0-23.
	min the minutes between 0-59.
		 * */
		int i = 0;
		Date test = new Date(111, 1, 6, 20, 0);
		System.out.println("\nINI TEST: "+test);
		for (String exp : listExpression) {
			System.out.println("EXP-"+ ++i+"-> '"+exp+"' :\t"+ct.parseCronExp(exp,test));
		}
		
		
	}
	
	
	/**
	 * 	field         allowed values
     *  -----         --------------
     *  minute        0-59
     *  hour          0-23
     *  day of month  1-31
     *  month         1-12 (or names, see below)
     *  day of week   0-7 (0 or 7 is Sun, or use names)
     *  
	 * @param cronExpression
	 * @param date
	 * @return
	 */
//	public Date parseCronExp(String cronExpression, Date date){
//		
//		String[] fields = cronExpression.split(" ");
//		
//		if(fields.length < 5){
//			//TODO
//		}
//		
////		Date date = new Date();   // given date
//		Calendar c = GregorianCalendar.getInstance(); // creates a new calendar instance
//		
//		String minute = fields[0];
//		String hour = fields[1];
//		String dayOfMonth = fields[2];
//		String month = fields[3];
//		String dayOfWeek = fields[4];
//		
//		if(isCurrentValue(minute,c.get(Calendar.MINUTE)) 
//				&& isCurrentValue(hour,c.get(Calendar.HOUR_OF_DAY))
//				&& isCurrentValue(dayOfMonth,c.get(Calendar.DAY_OF_MONTH))
//				&& isCurrentValue(month,c.get(Calendar.MONTH))
//				&& isCurrentValue(dayOfWeek,c.get(Calendar.DAY_OF_WEEK))){
//
//			return date;
//		}
//		
//		
//		return null;
//	}
	
	
	
	public boolean parseCronExp(String cronExpression, Date date){
		
		String[] fields = cronExpression.split(" ");
		
		Calendar c = GregorianCalendar.getInstance(); // creates a new calendar instance
		c.setTime(date);
				
		String minute = fields[0].replace("*/", "0-59/");
		String hour = fields[1].replace("*/", "0-23/");
		String dayOfMonth = fields[2].replace("*/", "1-31/");
		String month = getMonth(fields[3].replace("*/", "01-12/"));
		String dayOfWeek = getDayOfWeek(fields[4].replace("*/", "0-6/"));
		
		return (isCurrentValue(minute,c.get(Calendar.MINUTE)) 
				&& isCurrentValue(hour,c.get(Calendar.HOUR_OF_DAY))
				&& isCurrentValue(dayOfMonth,c.get(Calendar.DAY_OF_MONTH))
				&& isCurrentValue(month,c.get(Calendar.MONTH))
				&& isCurrentValue(dayOfWeek,c.get(Calendar.DAY_OF_WEEK)));
	}
	
	

	/**
	 * Compares the value of the given calendar field with a cron expression field.
	 * @param field
	 * @param time
	 * @return
	 */
	private boolean isCurrentValue(String field, Integer time){
		if(field.equals("*")) return true;

		//Range expression
		if(field.contains("-")){
			String[] rangeExp = field.split("-");
			Integer valueFrom = Integer.valueOf(rangeExp[0]);
			if(time < valueFrom) return false;
			if(rangeExp[1].contains("/")){
				String[] jumpExp = rangeExp[1].split("/");
				Integer valueTo = Integer.valueOf(jumpExp[0]);
				Integer jump = Integer.valueOf(jumpExp[1]);
				if(time > valueTo - jump) return false;
				Integer total = valueFrom + jump + time;
				if(total%jump == 0)
					return true;
				else
					return false;
			}
			Integer valueTo = Integer.valueOf(rangeExp[1]);
			if(valueTo >= time) 
				return true;
			else
				return false;
		}
		// Multivalue expression
		if(field.contains(",")){
			String[] stepExp = field.split(",");
			for (String step : stepExp) {
				Integer value = Integer.valueOf(step);
				if(value.equals(time))
					return true;
				if(value>time)
					break;
			}
			return false;
		}
		return Integer.valueOf(field).equals(time)?true:false;
	}

	/**
	 * @return the number of month formatted
	 */
	private String getMonth(String monthField){
		if(monthField.equals("*")) return monthField;
		
		String formatedStr = "";
		// Range expression
		if(monthField.contains("-")){
			String[] rangeExp = monthField.split("-");
			if(rangeExp[0].length() == 1)
				rangeExp[0] = "0"+rangeExp[0];
			formatedStr += monthMap.get(rangeExp[0].toUpperCase())+"-";
			
			if(rangeExp[1].contains("/")){
				String[] jumpExp = rangeExp[1].split("/");
				if(jumpExp[0].length() == 1)
					jumpExp[0] = "0"+jumpExp[0];
				formatedStr += monthMap.get(jumpExp[0].toUpperCase())+"/";
				formatedStr += jumpExp[1];
			}else{
				if(rangeExp[1].length() == 1)
					rangeExp[1] = "0"+rangeExp[1];
				formatedStr += monthMap.get(rangeExp[1].toUpperCase());
			}
			return formatedStr;
		}
		// Multivalue expression
		if(monthField.contains(",")){
			String[] monthValues = monthField.split(",");
			for (int i = 0; i < monthValues.length; i++) {
				if(monthValues[i].length() == 1)
					monthValues[i] = "0"+monthValues[i];
				formatedStr += monthMap.get(monthValues[i].toUpperCase());
				if(i<monthValues.length - 1) formatedStr+=",";
			}
			return formatedStr;
		}
		if(monthField.length() == 1) monthField = "0"+monthField;
		formatedStr += monthMap.get(monthField.toUpperCase());
		
		return formatedStr;
	}

	/**
	 * @return the day of week formatted
	 */
	private String getDayOfWeek(String dayField){
		if(dayField.equals("*")) return dayField;

		String formatedStr = "";
		// Range expression
		if(dayField.contains("-")){
			String[] rangeExp = dayField.split("-");
			String strRet = dayOfWeekMap.get(rangeExp[0].toUpperCase());
			formatedStr += (strRet!=null?strRet:rangeExp[0])+"-";
			if(rangeExp[1].contains("/")){
				String[] jumpExp = rangeExp[1].split("/");
				strRet = dayOfWeekMap.get(jumpExp[0].toUpperCase());
				formatedStr += strRet!=null?strRet:jumpExp[0];
				formatedStr +="/"+jumpExp[1];
			}else{
				strRet = dayOfWeekMap.get(rangeExp[1].toUpperCase());
				formatedStr += strRet!=null?strRet:rangeExp[1];
			}
			System.out.println("RET: "+formatedStr);
			return formatedStr;
		}
		// Multivalue expression
		if(dayField.contains(",")){
			String[] dayValues = dayField.split(",");
			for (int i = 0; i < dayValues.length; i++) {
				String strRet = dayOfWeekMap.get(dayValues[i].toUpperCase());
				formatedStr+= strRet!=null?strRet:dayValues[i];
				if(i<dayValues.length - 1) formatedStr+=",";
			}
			return formatedStr;
		}
		formatedStr = dayOfWeekMap.get(dayField.toUpperCase());
		return formatedStr!=null?formatedStr:dayField;
	}
	
	
}
