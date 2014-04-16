//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del EstForDecJur
 * @author tecso
 *
 */
public class EstForDecJurVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "estForDecJurVO";
	
	private String descripcion;
	
	// Constructores
	public EstForDecJurVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public EstForDecJurVO(int id, String descripcion) {
		super();
		setId(new Long(id));
		setDescripcion(descripcion);
	}
	
	// Getters y Setters
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
