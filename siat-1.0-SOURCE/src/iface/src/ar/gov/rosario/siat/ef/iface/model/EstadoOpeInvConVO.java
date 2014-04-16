//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del EstadoOpeInvCon
 * @author tecso
 *
 */
public class EstadoOpeInvConVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "estadoOpeInvConVO";
	
	private String desEstadoOpeInvCon="";
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public EstadoOpeInvConVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public EstadoOpeInvConVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesEstadoOpeInvCon(desc);
	}
	
	// Getters y Setters
	public String getDesEstadoOpeInvCon() {
		return desEstadoOpeInvCon;
	}

	public void setDesEstadoOpeInvCon(String desEstadoOpeInvCon) {
		this.desEstadoOpeInvCon = desEstadoOpeInvCon;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
