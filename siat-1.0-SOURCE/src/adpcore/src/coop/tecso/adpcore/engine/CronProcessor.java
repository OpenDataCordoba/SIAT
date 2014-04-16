//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.adpcore.engine;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import coop.tecso.adpcore.AdpEngine;
import coop.tecso.adpcore.AdpProcess;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpUtil;

public class CronProcessor {

	static private Logger log = Logger.getLogger(CronProcessor.class);	
	
	private Map<Long, String> processLastExe = new HashMap<Long, String>(); // Almacena los valores en milisegundos de los momentos de ultima ejecucion para los procesos

	private boolean started = false;
	private int processorFrecuency = -1; //cada cuanto se ejecuta el processor
	private int count = 0; //frecuency contador
	
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
	
	/**
	 * Comienza el monitor de procesos de ejecucion periodica.
	 * @throws Exception
	 */
	public void start() throws Exception {
		if (started) {
			log.info("CronProcessor ya esta iniciado.");
			return;
		}
		started = true;
		log.info("CronProcessor iniciando...");

		processorFrecuency = Integer.parseInt(AdpUtil.getConfig("cronProcessor.frecuency", "30"));
		log.info("CronProcessor iniciado.");
	}

	/**
	 * Detiene el monitor.
	 * @throws Exception
	 */
	public void stop() throws Exception {
		if (!started) {
			log.info("CronProcessor ya esta detenido.");
			return;
		}
		processLastExe.clear(); 
	}
  
	/**
	 * Ejecutado periodicamente teniendo en cuenta el 
	 * processoFrecuncy: 
	 * 1 ejecutar siempre, 
	 * n ejecutar cada n*delay_de_engine
	 */
	public void background() throws Exception {
		if (started) {
			count = (count + 1) % processorFrecuency;
			if (count==0) {
				// Verificamos para cada proceso si corresponde su ejecucion
				Date currentTime =  new Date();
				for (AdpProcess process: AdpDao.getPeriodicProcess()) {
					if(watch(process, currentTime)) runProcess(process);
				}
			}
		}
	}
	
	
	/**
	 * Check whether to run a race for the process passed as parameter.
	 * Verifies the time parameter for the cron expression.
	 * @param process
	 * @param checkDate
	 * @return
	 */
	private boolean watch(AdpProcess process, Date checkDate){
		String checkExecution = isCurrentTime(process.getCronExpression().trim(),checkDate);
		if(null != checkExecution){
			String lastExecution = processLastExe.get(process.getId());
			if(null == lastExecution || lastExecution != checkExecution){
				processLastExe.put(process.getId(), checkExecution);
				log.info("########################## checkDate-cronExpression : "+checkDate+"-"+process.getCronExpression());
				return true;
			}
		}
		return false;
	}
	
	public void runProcess(AdpProcess process) {
			try {
				//verficamos que el proceso no este lokeado.
				// refresca los datos
				AdpDao.loadProcess(process.getId(), process);

				if (process.isLocked()) {
					log.info("Ejecucion periodica de proceso: Ignorando ejecucion por proceso con marca de lock. Proceso: '" + process.getCodProceso());
					return;
				}
				
				//chequeamos si este nodo puede ejecutar el proceso
				if (!AdpEngine.canExecProcess(process)) {
					log.info("Ejecucion periodica de proceso: Ignorando ejecucion, por no ser nodo valido. Este nodo: '" + AdpEngine.nodeName() + "' " + " nodos habilitados: " + process.getEjecNodo());
					return;
				}
				
				// validamos si no existe una corrida en ejecucion o por comenzar para el proceso
				if (AdpDao.getRunningRunIdByCodProcess(process.getCodProceso()) != 0) {
					log.info("Ejecucion periodica de proceso: Ignorando, el proceso ya se encuentra actualmente en ejecucion. Proceso: '" + process.getCodProceso());
					return;
				}					
				
				//	 encontramos el proceso del archivo que cambio. Scheduleamos la corrida ya!
				log.info("Ejecucion periodica de proceso: Proceso: " + process.getCodProceso() + ". Preparando ejecucion.");
				AdpRun run = AdpRun.newRun(process.getId(), "Iniciado por ejecucion periodica.");
			
				// llamar a validate
				if(!AdpEngine.validateProcess(run)){
					log.debug("CronProcessor: runProcess(): No se paso la validacion para la ejecucion del proceso '" + process.getCodProceso() + "'.");
					return;
				}
				
				run.create();
				try {
					run.execute(null);
				} catch (Exception e) {
					log.error("CronProcessor: runProcess(): Error durante la ejecucion del proceso '" + process.getCodProceso() + "'.", e);				
				}					
			

			} catch (Exception e) {
				log.error("CronProcessor: runProcess(): No se pudo comenzar corrida de proceso '" + process.getCodProceso() + "'.", e);
			}		
	}

	/**
	 * @return the processorFrecuency
	 */
	public int getProcessorFrecuency() {
		return processorFrecuency;
	}

	/**
	 * @param processorFrecuency the processorFrecuency to set
	 */
	public void setProcessorFrecuency(int processorFrecuency) {
		this.processorFrecuency = processorFrecuency;
	}
	
	/**
	 * Verifies if a valid date based on the cron expression and the date to check.
	 * @param cronExpression
	 * @param checkDate
	 * @return
	 */
	public String isCurrentTime(String cronExpression, Date checkDate){
		
		String[] fields = cronExpression.split(" ");
		String minute = fields[0].replace("*/", "0-59/");
		String hour = fields[1].replace("*/", "0-23/");
		String dayOfMonth = fields[2].replace("*/", "1-31/");
		String month = getMonth(fields[3].replace("*/", "1-12/"));
		String dayOfWeek = getDayOfWeek(fields[4].replace("*/", "0-6/"));
		
		Calendar c = GregorianCalendar.getInstance(); 
		c.setTime(checkDate);
		
		if(isCurrentValue(minute,c.get(Calendar.MINUTE)) 
			&& isCurrentValue(hour,c.get(Calendar.HOUR_OF_DAY))
			&& isCurrentValue(dayOfMonth,c.get(Calendar.DAY_OF_MONTH))
			&& isCurrentValue(month,c.get(Calendar.MONTH))
			&& isCurrentValue(dayOfWeek,c.get(Calendar.DAY_OF_WEEK))){
			
			String strDate= c.get(Calendar.MINUTE)+"|";
				   strDate+=c.get(Calendar.HOUR_OF_DAY)+"|";
				   strDate+=c.get(Calendar.DAY_OF_MONTH)+"|";
				   strDate+=c.get(Calendar.MONTH)+"|";
				   strDate+=c.get(Calendar.DAY_OF_WEEK);
			return strDate;
		}else{
			return null;
		}
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
				formatedStr += (strRet!=null?strRet:jumpExp[0])+"/";
				formatedStr += jumpExp[1];
			}else{
				strRet = dayOfWeekMap.get(rangeExp[1].toUpperCase());
				formatedStr += strRet!=null?strRet:rangeExp[1];
			}
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
	
	/**
	 * @return
	 */
	public static String validateCronExpression(String cronExpression){
		String strError = "";
		
		String[] fields = cronExpression.split(" ");
		if(fields.length != 5){
			return "Expresión Cron mal formada";
		}
		
		for (int i = 0; i < fields.length; i++) {
			if(fields[i].contains(",") && (fields[i].contains("-") || fields[i].contains("/"))){
				return "Expresión Cron no válida en columna "+ i+1;
			}
		}
		
		// Validate Minutes
		if(!fields[0].equals("*")){
			List<String> listMininute = getValues(fields[0]);
			for (String minute : listMininute) {
				try{
					int min = Integer.valueOf(minute);
					if(min<0 || min>59){
						strError = "El valor ingresado en minutos supera el rango permitido";
					}
				} catch (Exception e) {
					strError = "El valor ingresado en minutos no es válido";
				}
				if(!strError.equals("")) return strError;
			}
		}
		// Validate Hours
		if(!fields[1].equals("*")){
			List<String> listHour = getValues(fields[1]);
			for (String strHour : listHour) {
				try{
					int hour = Integer.valueOf(strHour);
					if(hour<0 || hour>23){
						strError = "El valor ingresado en horas supera el rango permitido";
					}
				} catch (Exception e) {
					strError = "El valor ingresado en horas no es válido";
				}
				if(!strError.equals("")) return strError;
			}
		}
		// Validate Day Of Month
		if(!fields[2].equals("*")){
			List<String> listDOM = getValues(fields[2]);
			for (String strDOM : listDOM) {
				try{
					int dom = Integer.valueOf(strDOM);
					if(dom<0 || dom>31){
						strError = "El valor ingresado en día del mes supera el rango permitido";
					}
				} catch (Exception e) {
					strError = "El valor ingresado en día del mes no es válido";
				}
				if(!strError.equals("")) return strError;
			}
		}
		// Validate Month
		if(!fields[3].equals("*")){
			List<String> listMonth = getValues(fields[3]);
			for (String strMonth : listMonth) {
				if(strMonth.length() == 1) strMonth = "0"+strMonth;
				if(null == monthMap.get(strMonth.toUpperCase())){
					return "El valor ingresado en mes no es válido";
				}
			}
		}
		// Validate Day Of Week
		if(!fields[4].equals("*")){
			List<String> listDOW = getValues(fields[4]);
			for (String strDOW : listDOW) {
				if(null == dayOfWeekMap.get(strDOW.toUpperCase())){
					return "El valor ingresado en día de la semana no es válido";
				}
			}
		}

		return strError;
	}
	
	/**
	 * @return
	 */
	private static List<String> getValues(String field){
		List<String> listValue = new ArrayList<String>();
		if(field.contains("*/")){
			listValue.add(field.replace("*/", ""));
		}else if(field.contains("-")){
			String[] rgMinute = field.split("-");
			if(rgMinute[1].contains("/")){
				String[] brMinute = rgMinute[1].split("/");
				listValue.add(brMinute[0]);
				listValue.add(brMinute[1]);
			}else{
				listValue.add(rgMinute[1]);
			}
			listValue.add(rgMinute[0]);
		}else if(field.contains(",")){
			String[] mtMinute = field.split(",");
			for (String minute : mtMinute) {
				listValue.add(minute);
			}
		}else{
			listValue.add(field);
		}
		return listValue;
	}
}
