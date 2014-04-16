//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del Mandatario
 * @author tecso
 *
 */
public class MandatarioVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "mandatarioVO";
	
	private String descripcion;
	private String domicilio;
	private String telefono;
	private String horarioAtencion;
	private String observaciones;
	
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public MandatarioVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public MandatarioVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDescripcion(desc);
	}
	
	// Getters y Setters

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getHorarioAtencion() {
		return horarioAtencion;
	}

	public void setHorarioAtencion(String horarioAtencion) {
		this.horarioAtencion = horarioAtencion;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}


	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
