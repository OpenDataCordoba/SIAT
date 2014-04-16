//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object del EstadoConvenio
 * @author tecso
 *
 */
public class ConEstConVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "conEstConVO";
	
	private String desEstado;
	
	private Date fechaConEstCon ;
	
	private CasoVO caso = new CasoVO();
	
	private String observacion;
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public ConEstConVO() {
		super();
	}
	

	// Getters y Setters
	public Date getFechaConEstCon() {
		return fechaConEstCon;
	}

	public void setFechaConEstCon(Date fechaConEstCon) {
		this.fechaConEstCon = fechaConEstCon;
	}

	public CasoVO getCaso() {
		return caso;
	}
	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	

	// Buss flags getters y setters
	
	
	// View flags getters
	public String getFechaConEstConView(){
		return (fechaConEstCon!=null?DateUtil.formatDate(fechaConEstCon, DateUtil.ddSMMSYYYY_MASK):"");
	}
	
	public String getIdCasoView(){
		return  getCaso().getCasoView();
	}

	public String getDesEstado() {
		return desEstado;
	}
	public void setDesEstado(String desEstado) {
		this.desEstado = desEstado;
	}
	
	// View getters
}
