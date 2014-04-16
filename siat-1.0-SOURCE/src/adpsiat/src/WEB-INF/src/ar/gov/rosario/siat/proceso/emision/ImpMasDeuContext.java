//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.emision;

import java.util.Map;

import ar.gov.rosario.siat.emi.buss.bean.ImpMasDeuTotReport;
import coop.tecso.adpcore.AdpProcessorContext;
import coop.tecso.demoda.iface.helper.SpooledWriter;

/**
 * Contexto de ejecucion para los threads
 * procesadores de la Impresion Masiva de
 * Deuda
 * 
 * @author Tecso Coop. Lmtda.
 */
public class ImpMasDeuContext extends AdpProcessorContext {

	private SpooledWriter spoolRecibos;
	
	private Map<String, String> mapLocalidades;

	private ImpMasDeuTotReport reporte;
	
	// Constructor
	public ImpMasDeuContext() {
	}

	public SpooledWriter getSpoolRecibos() {
		return spoolRecibos;
	}

	public void setSpoolRecibos(SpooledWriter spoolRecibos) {
		this.spoolRecibos = spoolRecibos;
	}

	public Map<String, String> getMapLocalidades() {
		return mapLocalidades;
	}

	public void setMapLocalidades(Map<String, String> mapLocalidades) {
		this.mapLocalidades = mapLocalidades;
	}

	public ImpMasDeuTotReport getReporte() {
		return reporte;
	}

	public void setReporte(ImpMasDeuTotReport reporte) {
		this.reporte = reporte;
	}
}
