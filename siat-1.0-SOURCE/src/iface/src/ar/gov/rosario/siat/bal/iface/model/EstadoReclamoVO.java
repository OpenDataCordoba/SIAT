//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del EstadoReclamo
 * @author tecso
 *
 */
public class EstadoReclamoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "estadoReclamoVO";
	
	private String desEstadoReclamo;
	private String respuesta;
	
	// Buss Flags
	
	
	// View Constants
	
	
	public String getDesEstadoReclamo() {
		return desEstadoReclamo;
	}

	public void setDesEstadoReclamo(String desEstadoReclamo) {
		this.desEstadoReclamo = desEstadoReclamo;
	}

	// Constructores
	public EstadoReclamoVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public EstadoReclamoVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesEstadoReclamo(desc);
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public String getRespuesta() {
		return respuesta;
	}

	
	
	// Getters y Setters

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
