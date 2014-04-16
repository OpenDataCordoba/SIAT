//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

public class TotalesConvenioReport {

	private String desFormaPago = ""; //(Contado - Financiado)
	private Integer cantidad = 0;
	private Double anticipo = 0D;
	private Double resto = 0D;
	
	// Constructor
	public TotalesConvenioReport(){
		
	}

	// Getters y Setters
	public String getDesFormaPago() {
		return desFormaPago;
	}
	public void setDesFormaPago(String desFormaPago) {
		this.desFormaPago = desFormaPago;
	}

	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Double getAnticipo() {
		return anticipo;
	}
	public void setAnticipo(Double anticipo) {
		this.anticipo = anticipo;
	}

	public Double getResto() {
		return resto;
	}
	public void setResto(Double resto) {
		this.resto = resto;
	}
	
	
}
