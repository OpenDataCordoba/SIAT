//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.buss.bean;

import java.util.Date;

/**
 * Bean correspondiente a Cuota, este no esta mapeado a la bd
 * 
 * 
 * @author tecso
 */
public class CdMCuota {
	
	private Double monto;
	private Double capital;
	private Double interes;
	private Date fechaVto;
	private Date fechaGracia;

	public Double getMonto() {
		if (monto == null) {
			return interes + capital;
		} else {
			return monto;
		}
		
	}
	public void setMonto(Double monto) {
		this.monto = monto;
	}
	public Double getCapital() {
		return capital;
	}
	public void setCapital(Double capital) {
		this.capital = capital;
	}
	public Double getInteres() {
		return interes;
	}
	public void setInteres(Double interes) {
		this.interes = interes;
	}
	public Date getFechaVto() {
		return fechaVto;
	}
	public void setFechaVto(Date fechaVto) {
		this.fechaVto = fechaVto;
	}
	public Date getFechaGracia() {
		return fechaGracia;
	}
	public void setFechaGracia(Date fechaGracia) {
		this.fechaGracia = fechaGracia;
	}

}
