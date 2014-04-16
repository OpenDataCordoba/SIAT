//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

public class ConDeuDetPrint extends SiatBussImageModel {
	private static final long serialVersionUID = 1L;

	private Long   anio;
	private Long   periodo;
	private Double saldo;
	private Date   fechaVencimiento;

	public ConDeuDetPrint(){
	}
	
	/**
	 * Constructor
	 *
	 * @param sweActionName
	 * @param anio
	 * @param periodo
	 * @param importe
	 * @param fechaVencimiento
	 */
	public ConDeuDetPrint(Long anio, Long periodo, Double saldo, Date fechaVencimiento) {
		this.anio = anio;
		this.periodo = periodo;
		this.saldo = saldo;
		this.fechaVencimiento = fechaVencimiento;
	}

	public Long getAnio() {
		return anio;
	}
	public void setAnio(Long anio) {
		this.anio = anio;
	}
	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}
	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
	public Double getSaldo() {
		return saldo;
	}
	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}
	public Long getPeriodo() {
		return periodo;
	}
	public void setPeriodo(Long periodo) {
		this.periodo = periodo;
	}
	

}

