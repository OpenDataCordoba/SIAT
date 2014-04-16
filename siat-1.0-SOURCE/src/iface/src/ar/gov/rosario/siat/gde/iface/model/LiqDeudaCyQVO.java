//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

/**
 * Clase utilizada para representar cada bloque de Procediciemton de Deuda en Concurso y Quiera
 * 
 * @author Tecso
 *
 */
public class LiqDeudaCyQVO extends LiqBlockDeuda {
				 
	private Long idProcedimiento;
	private String nroProcedimiento="";
	private String fechaActualizacion="";
	
	private boolean mostrarChkAllCyQ = false;
	
	// Getters y Setters
	public String getFechaActualizacion() {
		return fechaActualizacion;
	}
	public void setFechaActualizacion(String fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}
	
	public String getNroProcedimiento() {
		return nroProcedimiento;
	}
	public void setNroProcedimiento(String nroProcedimiento) {
		this.nroProcedimiento = nroProcedimiento;
	}
	
	public Long getIdProcedimiento() {
		return idProcedimiento;
	}
	public void setIdProcedimiento(Long idProcedimiento) {
		this.idProcedimiento = idProcedimiento;
	}
	
	public boolean getMostrarChkAllCyQ() {
		return mostrarChkAllCyQ;
	}
	public void setMostrarChkAllCyQ(boolean mostrarChkAllCyQ) {
		this.mostrarChkAllCyQ = mostrarChkAllCyQ;
	}
	
}
