//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

public class TipoEntradaVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	private String codigo = "";
	private String descripcion = "";
	
	//Constructores 
	public TipoEntradaVO(){
		super();
	}

	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipoEntradaVO(int id, String descripcion) {
		super();
		setId(new Long(id));
		setDescripcion(descripcion);
	}
	
	// Getters & Setters
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
