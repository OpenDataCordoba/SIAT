//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 	Contiene Datos usados en el Balance.
 * 
 * @author Tecso
 *
 */
public class BalanceSession {

	//private static Logger log = Logger.getLogger(BalanceSession.class);
		
	private List<String> listAdvertencias = new ArrayList<String>();

	// Mapa para Procesos de Asentamientos y Delegadores 
	private Map<String, Long> mapProcesos = new HashMap<String, Long>();
	
	// Mapa para almacenar CodRefPag de Servicio Banco 85 de transacciones a corregir 
	// (TODO eliminar este mapa luego de que se hayan procesado en balance los dias de banco anteriores al 01/10/2009)
	private Map<String, Boolean> mapCodRefPagGRE = new HashMap<String, Boolean>();
	
	private Long warnings = 0L;
	
	private boolean logDetalladoEnabled = false;
	
	// Para medir tiempos y promedios:
	private boolean logStatsEnabled = false;
	private Map<String, double[]> mapStats = new HashMap<String, double[]>();
	
	private boolean forzado = false;
	
	// Getters y Setters
	public Map<String, double[]> getMapStats() {
		return mapStats;
	}
	public void setMapStats(Map<String, double[]> mapStats) {
		this.mapStats = mapStats;
	}
	public boolean isLogDetalladoEnabled() {
		return logDetalladoEnabled;
	}
	public void setLogDetalladoEnabled(boolean logDetalladoEnabled) {
		this.logDetalladoEnabled = logDetalladoEnabled;
	}
	
	public Long getWarnings() {
		return warnings;
	}
	
	public void setWarnings(Long warnings) {
		this.warnings = warnings;
	}	
	
	/**
	 * Incrementa el contador de Advertencias.
	 */
	public void incWarnings() {
		this.warnings = warnings + 1;
	}
	
	/**
	 * Agrega una Advertencia a la lista e incrementa el contador.
	 */
	public void addWarning(String wrn) {
		this.listAdvertencias.add(wrn);
		this.incWarnings();
	}
	
	/**
	 *  Limpia la lista de Advertencias y lleva el contador a cero. 
	 */
	public void clearWarnings(){
		this.warnings = 0L;
		this.listAdvertencias.clear();
	}
	
	public boolean isLogStatsEnabled() {
		return logStatsEnabled;
	}
	public void setLogStatsEnabled(boolean logStatsEnabled) {
		this.logStatsEnabled = logStatsEnabled;
	}
	
	/**
	 *  Acumula el tiempo y aumenta el contador para el objecto de clave pasada en el mapa de estadisticas.
	 * @param key
	 * @param tiempo
	 */
	public void addStats(String key, Long tiempo){
		double[] value = null;
		value = mapStats.get(key);
		if(value == null){
			value = new double[2];
			value[0] = 1D;
			value[1] = tiempo;
			mapStats.put(key, value);
		}else{
			value[0]++;
			value[1] += tiempo;
			mapStats.put(key, value);			
		}
	}
	
	/**
	 *  Devuelve el Valor Promedio del Tiempo para la Clave pasada.
	 *  
	 *  @param key
	 * 
	 * @return
	 */
	public Double getStats(String key){
		Double valorPromedio = null;
		double[] value = null;
		value = mapStats.get(key);
		if(value != null){
			valorPromedio = value[1]/value[0];
		}
		return valorPromedio;
	}
	
	/**
	 * Devuelve una lista de Strings con los valores promedios de tiempos para cada clave del mapa.
	 * <p>Cada elemento de la lista corresponde a:
	 * <i>   [key]: [valorPromedio] ms </i></p>
	 * <p>Ejemplo:
	 * <i>   Transaccion: 50 ms  </i></p>
	 * @return listString
	 * 
	 */
	public List<String> getStats(){
		List<String> stats = new ArrayList<String>();
		for(String key: mapStats.keySet()){
			Double valorPromedio = null;
			double[] value = null;
			value = mapStats.get(key);
			if(value != null){
				valorPromedio = value[1]/value[0];
				stats.add(key+": "+valorPromedio.toString()+" ms, sobre "+value[0]+" mediciones");
			}	
		}
		return stats;
	}
	public boolean isForzado() {
		return forzado;
	}
	public void setForzado(boolean forzado) {
		this.forzado = forzado;
	}
	public List<String> getListAdvertencias() {
		return listAdvertencias;
	}
	public void setListAdvertencias(List<String> listAdvertencias) {
		this.listAdvertencias = listAdvertencias;
	}
	public Map<String, Long> getMapProcesos() {
		return mapProcesos;
	}
	public void setMapProcesos(Map<String, Long> mapProcesos) {
		this.mapProcesos = mapProcesos;
	}
	
	
	
	/**
	 *  Busca el codRefPag pasado en el Mapa de codigos a corregir para transacciones de gravamenes. 
	 *  Si el mapa esta vacio, primero lo inicializa.
	 * 
	 * (TODO eliminar este metodo luego de que se hayan procesado en balance los dias de banco anteriores al 01/10/2009)
	 * @param keyToValidate
	 * @return
	 */
	public Boolean isCodRefPagInFixMap(String keyToValidate){
		if(this.mapCodRefPagGRE.isEmpty()){
			BalanceHelper balanceHelper = new BalanceHelper();
			String listCodRefPag = balanceHelper.listCodRefPag;
			for(String codRefPag: listCodRefPag.split(",")){
				this.mapCodRefPagGRE.put(codRefPag, true);
			}
		}
		if(this.mapCodRefPagGRE.get(keyToValidate) != null)
			return true;
		else 
			return false;
	}
	
	
}
