//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del EstadoActa
 * @author tecso
 *
 */
public class EstadoActaVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "estadoActaVO";
	
	private String desEstadoActa;
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public EstadoActaVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public EstadoActaVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesEstadoActa(desc);
	}

	// Getters y Setters
	public String getDesEstadoActa() {
		return desEstadoActa;
	}

	public void setDesEstadoActa(String desEstadoActa) {
		this.desEstadoActa = desEstadoActa;
	}
	

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
