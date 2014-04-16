//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Seccion
 * @author tecso
 *
 */
public class SeccionVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "seccionVO";
	
	private String descripcion;

	private ZonaVO zona;

	//Constructores

	public SeccionVO(){
		super();
	}
	
	public SeccionVO(int id, String descripcion) {
		super(id);
		setDescripcion(descripcion);
	}
	
	//Getters Y Setters
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public ZonaVO getZonaVO() {
		return zona;
	}
	
	public void setZonaVO(ZonaVO zona) {
		this.zona = zona;
	}
}
