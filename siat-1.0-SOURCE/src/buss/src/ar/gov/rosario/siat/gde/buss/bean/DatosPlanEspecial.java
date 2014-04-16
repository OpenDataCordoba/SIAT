//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.Date;

public class DatosPlanEspecial {
	private Date fechaFormalizacion;
	private Integer cantMaxCuo;
	private Double impMinCuo;
	private Double descCapital;
	private Double descActualizacion;
	private Double interes;
	private Date venPrimeraCuota;
	private Double importeAnticipo;
	
//getters & setters
	public Date getFechaFormalizacion() {
		return fechaFormalizacion;
	}
	public void setFechaFormalizacion(Date fechaFormalizacion) {
		this.fechaFormalizacion = fechaFormalizacion;
	}
	public Integer getCantMaxCuo() {
		return cantMaxCuo;
	}
	public void setCantMaxCuo(Integer cantMaxCuo) {
		this.cantMaxCuo = cantMaxCuo;
	}
	public Double getImpMinCuo() {
		return impMinCuo;
	}
	public void setImpMinCuo(Double impMinCuo) {
		this.impMinCuo = impMinCuo;
	}
	public Double getDescCapital() {
		return descCapital;
	}
	public void setDescCapital(Double descCapital) {
		this.descCapital = descCapital;
	}
	public Double getDescActualizacion() {
		return descActualizacion;
	}
	public void setDescActualizacion(Double descActualizacion) {
		this.descActualizacion = descActualizacion;
	}
	public Double getInteres() {
		return interes;
	}
	public void setInteres(Double interes) {
		this.interes = interes;
	}
	public Date getVenPrimeraCuota() {
		return venPrimeraCuota;
	}
	public void setVenPrimeraCuota(Date venPrimeraCuota) {
		this.venPrimeraCuota = venPrimeraCuota;
	}
	public Double getImporteAnticipo() {
		return importeAnticipo;
	}
	public void setImporteAnticipo(Double importeAnticipo) {
		this.importeAnticipo = importeAnticipo;
	}
	
	
}
