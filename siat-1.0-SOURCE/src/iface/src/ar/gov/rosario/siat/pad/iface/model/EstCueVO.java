//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;


/**
 * Value Object del EstadoCuenta
 * @author tecso
 *
 */
public class EstCueVO extends SiatBussImageModel {

	private static final long serialVersionUID = 0;
	
	public static final Long ID_ACTIVO = 1L; 
	public static final Long ID_CANCELADO = 8L;

	public static final String NAME = "estCueVO";
	
	private String descripcion = "";
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public EstCueVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public EstCueVO(int id, String desc) {
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
	public boolean getEsActivo(){
		return ID_ACTIVO.equals(this.getId());
	}

	// View flags getters
	
	
	
	// View getters
	
}
