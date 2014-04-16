//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import coop.tecso.demoda.iface.helper.NumberUtil;

/**
 * Value Object del CompFuenteResMensual
 * se forma dinamicamente, no existe el BO
 * @author tecso
 *
 */
public class CompFuenteResMensualVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "compFuenteResMensualVO";
	
	private Integer periodo;
	
	private Integer anio;
	
	private Double baseFuenteMin=0D;

	private Double baseFuenteSus=0D;

	
	
	// Buss Flags
	
	
	// View Constants
	



	// Constructores
	public CompFuenteResMensualVO() {
		super();
	}
	
	
	// Getters y Setters
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	


	

	public Integer getPeriodo() {
		return periodo;
	}


	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}


	public Integer getAnio() {
		return anio;
	}


	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	
	public Double getBaseFuenteMin() {
		return baseFuenteMin;
	}


	public void setBaseFuenteMin(Double baseFuenteMin) {
		this.baseFuenteMin = baseFuenteMin;
	}


	public Double getBaseFuenteSus() {
		return baseFuenteSus;
	}


	public void setBaseFuenteSus(Double baseFuenteSus) {
		this.baseFuenteSus = baseFuenteSus;
	}


	

	// View getters
	
	public String getBaseFuenteMinView() {
		return (baseFuenteMin!=null)?NumberUtil.round(baseFuenteMin, SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}
	
	public String getBaseFuenteSusView() {
		return (baseFuenteSus!=null)?NumberUtil.round(baseFuenteSus, SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}
	
	public String getDiferenciaView() {
		return NumberUtil.round(baseFuenteMin - baseFuenteSus, SiatParam.DEC_IMPORTE_VIEW).toString();
	}
	
	public String getPeriodoView(){
		return this.periodo+"/"+this.anio;
	}

}
