//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del EstadoObra
 * @author tecso
 *
 */
public class EstadoObraVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "estadoObraVO";
	
	private String desEstadoObra;
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public EstadoObraVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public EstadoObraVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesEstadoObra(desc);
	}
	
	public String getDesEstadoObra() {
		return desEstadoObra;
	}

	public void setDesEstadoObra(String desEstadoObra) {
		this.desEstadoObra = desEstadoObra;
	}
	
	// Getters y Setters
	
	
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
