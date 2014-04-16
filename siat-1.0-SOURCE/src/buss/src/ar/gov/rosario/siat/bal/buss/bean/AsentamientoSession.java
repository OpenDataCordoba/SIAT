//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.exe.buss.bean.CueExe;
import ar.gov.rosario.siat.exe.buss.bean.CueExeCache;
import ar.gov.rosario.siat.gde.buss.bean.ConstanciaDeu;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.ConvenioDeuda;
import ar.gov.rosario.siat.gde.buss.bean.Recibo;
import ar.gov.rosario.siat.gde.buss.bean.ReciboConvenio;

/**
 * 	Contiene Mapas usados en el Asentamiento.
 * 
 * @author Tecso
 *
 */
public class AsentamientoSession {

	private static Logger log = Logger.getLogger(AsentamientoSession.class);
	
	// Mapa para almacenar los AuxRecaudado
	private Map<String, AuxRecaudado> mapAuxRecaudado = new HashMap<String, AuxRecaudado>();
	// Mapa para almacenar los AuxSellado
	private Map<String, AuxSellado> mapAuxSellado = new HashMap<String, AuxSellado>();
	// Mapa para almacenar los AuxConvenio
	private Map<String, AuxConvenio> mapAuxConvenio = new HashMap<String, AuxConvenio>();
	// Mapa para almacenar los AuxConDeu
	private Map<String, AuxConDeu> mapAuxConDeu = new HashMap<String, AuxConDeu>();
	// Mapa de indice para AuxPagDeu
	private Map<String, Boolean> mapAuxPagDeuIndex = new HashMap<String, Boolean>();
	// Mapa de indice para AuxPagCuo
	private Map<String, Boolean> mapAuxPagCuoIndex = new HashMap<String, Boolean>();
	// Mapa de indice para AuxPagRec
	private Map<String, Boolean> mapAuxPagRecIndex = new HashMap<String, Boolean>();
	// Mapa de indice para AuxPagRecCon
	private Map<String, Boolean> mapAuxPagRecConIndex = new HashMap<String, Boolean>();

	// Mapa para almacenar las listas de ConvenioDeuda por Convenio manejadas como 'pilas' LIFO
	private Map<String, List<ConvenioDeuda>> mapStackConDeu = new HashMap<String, List<ConvenioDeuda>>();

	// Mapa para almacenar contadores de cuotas asentadas como pagos buenos
	private Map<String, Integer> mapCuotaCounter = new HashMap<String, Integer>();

	// Mapa para almacenar las listas de CueExe que no Actualizan Deuda por Cuenta
	private Map<String, List<CueExe>> mapCueExeNoAct = new HashMap<String, List<CueExe>>();
	private CueExeCache cueExeCache = new CueExeCache();
	
	// Lista de Constancias de Deuda a Actualizar en el 3er Paso
	private List<ConstanciaDeu> listConstanciaDeu = new ArrayList<ConstanciaDeu>();
	
	// Listas para Registrar Recibos Cancelados
	private List<Recibo> listRecibo = new ArrayList<Recibo>();
	private List<Transaccion> listTransaccionDeRecibo = new ArrayList<Transaccion>();

	// Lista de Convenios a Cancelar
	private List<Convenio> listConvenioACancelar = new ArrayList<Convenio>();

	// Listas para Registrar Recibos de Convenio Cancelados
	private List<ReciboConvenio> listReciboConvenio = new ArrayList<ReciboConvenio>();
	private List<Transaccion> listTransaccionDeReciboConvenio = new ArrayList<Transaccion>();

	private Map<String, Date> mapRecFecUltPag = new HashMap<String, Date>();
	
	private Long warnings = 0L;
	
	private boolean logDetalladoEnabled = false;
	
	// Para medir tiempos y promedios:
	private boolean logStatsEnabled = false;
	private Map<String, double[]> mapStats = new HashMap<String, double[]>();
	
	private boolean forzado = false;
	
	// Getters y Setters
	
	
	public Map<String, AuxConDeu> getMapAuxConDeu() {
		return mapAuxConDeu;
	}
	public void setMapAuxConDeu(Map<String, AuxConDeu> mapAuxConDeu) {
		this.mapAuxConDeu = mapAuxConDeu;
	}
	public Map<String, AuxConvenio> getMapAuxConvenio() {
		return mapAuxConvenio;
	}
	public void setMapAuxConvenio(Map<String, AuxConvenio> mapAuxConvenio) {
		this.mapAuxConvenio = mapAuxConvenio;
	}
	public Map<String, Boolean> getMapAuxPagCuoIndex() {
		return mapAuxPagCuoIndex;
	}
	public void setMapAuxPagCuoIndex(Map<String, Boolean> mapAuxPagCuoIndex) {
		this.mapAuxPagCuoIndex = mapAuxPagCuoIndex;
	}
	public Map<String, Boolean> getMapAuxPagDeuIndex() {
		return mapAuxPagDeuIndex;
	}
	public void setMapAuxPagDeuIndex(Map<String, Boolean> mapAuxPagDeuIndex) {
		this.mapAuxPagDeuIndex = mapAuxPagDeuIndex;
	}
	public Map<String, Boolean> getMapAuxPagRecConIndex() {
		return mapAuxPagRecConIndex;
	}
	public void setMapAuxPagRecConIndex(Map<String, Boolean> mapAuxPagRecConIndex) {
		this.mapAuxPagRecConIndex = mapAuxPagRecConIndex;
	}
	public Map<String, Boolean> getMapAuxPagRecIndex() {
		return mapAuxPagRecIndex;
	}
	public void setMapAuxPagRecIndex(Map<String, Boolean> mapAuxPagRecIndex) {
		this.mapAuxPagRecIndex = mapAuxPagRecIndex;
	}
	public Map<String, AuxRecaudado> getMapAuxRecaudado() {
		return mapAuxRecaudado;
	}
	public void setMapAuxRecaudado(Map<String, AuxRecaudado> mapAuxRecaudado) {
		this.mapAuxRecaudado = mapAuxRecaudado;
	}
	public Map<String, AuxSellado> getMapAuxSellado() {
		return mapAuxSellado;
	}
	public void setMapAuxSellado(Map<String, AuxSellado> mapAuxSellado) {
		this.mapAuxSellado = mapAuxSellado;
	}
	public List<ConstanciaDeu> getListConstanciaDeu() {
		return listConstanciaDeu;
	}
	public void setListConstanciaDeu(List<ConstanciaDeu> listConstanciaDeu) {
		this.listConstanciaDeu = listConstanciaDeu;
	}
	public List<Recibo> getListRecibo() {
		return listRecibo;
	}
	public void setListRecibo(List<Recibo> listRecibo) {
		this.listRecibo = listRecibo;
	}
	public List<Transaccion> getListTransaccionDeRecibo() {
		return listTransaccionDeRecibo;
	}
	public void setListTransaccionDeRecibo(List<Transaccion> listTransaccionDeRecibo) {
		this.listTransaccionDeRecibo = listTransaccionDeRecibo;
	}
	public List<Convenio> getListConvenioACancelar() {
		return listConvenioACancelar;
	}
	public void setListConvenioACancelar(List<Convenio> listConvenioACancelar) {
		this.listConvenioACancelar = listConvenioACancelar;
	}
	public List<ReciboConvenio> getListReciboConvenio() {
		return listReciboConvenio;
	}
	public void setListReciboConvenio(List<ReciboConvenio> listReciboConvenio) {
		this.listReciboConvenio = listReciboConvenio;
	}
	public List<Transaccion> getListTransaccionDeReciboConvenio() {
		return listTransaccionDeReciboConvenio;
	}
	public void setListTransaccionDeReciboConvenio(
			List<Transaccion> listTransaccionDeReciboConvenio) {
		this.listTransaccionDeReciboConvenio = listTransaccionDeReciboConvenio;
	}
	public Map<String, Date> getMapRecFecUltPag() {
		return mapRecFecUltPag;
	}
	public void setMapRecFecUltPag(Map<String, Date> mapRecFecUltPag) {
		this.mapRecFecUltPag = mapRecFecUltPag;
	}
	public Map<String, List<ConvenioDeuda>> getMapStackConDeu() {
		return mapStackConDeu;
	}
	public void setMapStackConDeu(Map<String, List<ConvenioDeuda>> mapStackConDeu) {
		this.mapStackConDeu = mapStackConDeu;
	}
	
	/**
	 *  Insterta el ConvenioDeuda al final de la lista de ConveniosDeuda guardadas en el mapStackConDeu
	 * 	(Si no existe la lista en el mapa, la crea)
	 * 
	 * @param idConvenio
	 * @param convenioDeuda
	 */
	public ConvenioDeuda pushIntoStackConDeu(Long idConvenio, ConvenioDeuda convenioDeuda){
		List<ConvenioDeuda> listConvenioDeuda = this.mapStackConDeu.get(idConvenio.toString());
		if(listConvenioDeuda == null){
			listConvenioDeuda = new ArrayList<ConvenioDeuda>();
			this.mapStackConDeu.put(idConvenio.toString(), listConvenioDeuda);
		}
		listConvenioDeuda.add(convenioDeuda);
		return convenioDeuda;
	}
	/**
	 *  Obtiene y remueve el ConvenioDeuda del final de la lista de ConveniosDeuda guardadas en el mapStackConDeu
	 * 	(Si no existe la lista en el mapa o esta vacia retorna null);
	 * 
	 * @param idConvenio
	 */
	public ConvenioDeuda popFromStackConDeu(Long idConvenio){
		List<ConvenioDeuda> listConvenioDeuda = this.mapStackConDeu.get(idConvenio.toString());
		if(listConvenioDeuda == null || listConvenioDeuda.isEmpty()){
			return null;
		}
		
		return listConvenioDeuda.remove(listConvenioDeuda.size( )-1);
	}	
	
	public Map<String, List<CueExe>> getMapCueExeNoAct() {
		return mapCueExeNoAct;
	}
	public void setMapCueExeNoAct(Map<String, List<CueExe>> mapCueExeNoAct) {
		this.mapCueExeNoAct = mapCueExeNoAct;
	}
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
	public Map<String, Integer> getMapCuotaCounter() {
		return mapCuotaCounter;
	}
	public void setMapCuotaCounter(Map<String, Integer> mapCuotaCounter) {
		this.mapCuotaCounter = mapCuotaCounter;
	}

	
	/**
	 * Incrementa el contador de Advertencias.
	 */
	public void incWarnings() {
		this.warnings = warnings + 1;
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
	public CueExeCache getCueExeCache() {
		return cueExeCache;
	}
	public void setCueExeCache(CueExeCache cueExeCache) {
		this.cueExeCache = cueExeCache;
	}
	public boolean isForzado() {
		return forzado;
	}
	public void setForzado(boolean forzado) {
		this.forzado = forzado;
	}
	
}