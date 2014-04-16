//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

public class AuxLiqComProDeuVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	
	private String strPeriodoDeuda ="";
	
	private String fechaVtoDeuda ="";
	
	private String fechaPago ="";
	
	private String fechaFor = "";
	
	private String porcentajeComision ="";
	
	private String importeComision ="";
	
	private String importeAplicado ="";

	public String getStrPeriodoDeuda() {
		return strPeriodoDeuda;
	}

	public void setStrPeriodoDeuda(String strPeriodoDeuda) {
		this.strPeriodoDeuda = strPeriodoDeuda;
	}

	public String getFechaVtoDeuda() {
		return fechaVtoDeuda;
	}

	public void setFechaVtoDeuda(String fechaVtoDeuda) {
		this.fechaVtoDeuda = fechaVtoDeuda;
	}

	public String getPorcentajeComision() {
		return porcentajeComision;
	}

	public void setPorcentajeComision(String porcentajeComision) {
		this.porcentajeComision = porcentajeComision;
	}

	public String getImporteComision() {
		return importeComision;
	}

	public void setImporteComision(String importeComision) {
		this.importeComision = importeComision;
	}

	public String getImporteAplicado() {
		return importeAplicado;
	}

	public void setImporteAplicado(String importeAplicado) {
		this.importeAplicado = importeAplicado;
	}

	public String getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(String fechaPago) {
		this.fechaPago = fechaPago;
	}

	public String getFechaFor() {
		return fechaFor;
	}

	public void setFechaFor(String fechaFor) {
		this.fechaFor = fechaFor;
	}
	

	
	
	
}
