//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

public class TipOriMovVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	
	private String descripcion = "";
	
	// Constructores 
	public TipOriMovVO(){
		super();
	}
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipOriMovVO(int id, String descripcion) {
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
