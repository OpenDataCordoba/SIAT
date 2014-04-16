//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.emision;

import java.util.HashMap;

import ar.gov.rosario.siat.emi.buss.bean.ResLiqDeu;
import coop.tecso.adpcore.AdpProcessorContext;

/**
 * Contexto de ejecucion para los threads
 * procesadores del Resumen de Liquidacion
 * de Deuda
 * 
 * @author Tecso Coop. Lmtda.
 */
public class ResLiqDeuProContext extends AdpProcessorContext {

	private HashMap<Long,String> cacheProcuradores;

	private HashMap<Long,String> cacheConvenios;
	
	private ResLiqDeu resLiqDeu;
	
	// Constructor
	public ResLiqDeuProContext() {
	}

	public HashMap<Long, String> getCacheProcuradores() {
		return cacheProcuradores;
	}

	public void setCacheProcuradores(HashMap<Long, String> cacheProcuradores) {
		this.cacheProcuradores = cacheProcuradores;
	}

	public HashMap<Long, String> getCacheConvenios() {
		return cacheConvenios;
	}

	public void setCacheConvenios(HashMap<Long, String> cacheConvenios) {
		this.cacheConvenios = cacheConvenios;
	}

	public ResLiqDeu getResLiqDeu() {
		return resLiqDeu;
	}

	public void setResLiqDeu(ResLiqDeu resLiqDeu) {
		this.resLiqDeu = resLiqDeu;
	}
}
