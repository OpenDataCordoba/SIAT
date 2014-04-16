//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

public class ClasificadorVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "clasificadorVO";
	
	private String  descripcion = "";
	private Integer cantNivel;
	
	private String cantNivelView = "";

	//Constructores 
	public ClasificadorVO(){
		super();
	}

	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public ClasificadorVO(int id, String descripcion) {
		super();
		setId(new Long(id));
		setDescripcion(descripcion);
	}

	// Getters y Setters
	
	public Integer getCantNivel() {
		return cantNivel;
	}
	public void setCantNivel(Integer cantNivel) {
		this.cantNivel = cantNivel;
		this.cantNivelView = StringUtil.formatInteger(cantNivel);
	}
	public String getCantNivelView() {
		return cantNivelView;
	}
	public void setCantNivelView(String cantNivelView) {
		this.cantNivelView = cantNivelView;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
	
}
