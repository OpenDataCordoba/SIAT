//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del TipoPrivilegio
 * @author tecso
 *
 */
public class TipoPrivilegioVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoPrivilegioVO";
	
	private String descripcion;
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public TipoPrivilegioVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipoPrivilegioVO(int id, String desc) {
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


	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
	
	
	
}
