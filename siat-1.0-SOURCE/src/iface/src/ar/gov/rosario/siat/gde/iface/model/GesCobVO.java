//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object de Gestion Cobranza
 * @author tecso
 *
 */
public class GesCobVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "gesCobVO";
	
	private CobranzaVO cobranza;
	
	private Date fecha;
	
	private String observacion;
	
	private EstadoCobranzaVO estadoCobranza = new EstadoCobranzaVO();
	
	private Date fechaControl;
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public GesCobVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.


	// Getters y Setters


	public CobranzaVO getCobranza() {
		return cobranza;
	}

	public void setCobranza(CobranzaVO cobranza) {
		this.cobranza = cobranza;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public EstadoCobranzaVO getEstadoCobranza() {
		return estadoCobranza;
	}

	public void setEstadoCobranza(EstadoCobranzaVO estadoCobranza) {
		this.estadoCobranza = estadoCobranza;
	}

	public Date getFechaControl() {
		return fechaControl;
	}

	public void setFechaControl(Date fechaControl) {
		this.fechaControl = fechaControl;
	}
	
	

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
	
	public String getFechaView(){
		return (this.fecha!=null)?DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK):"";
	}
	
	public String getFechaControlView(){
		return (this.fechaControl!=null)?DateUtil.formatDate(fechaControl, DateUtil.ddSMMSYYYY_MASK):"";
	}
}
