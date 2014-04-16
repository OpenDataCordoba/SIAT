//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

public class EstadoGesJudVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	
	public static String VIGENTE = "Vigente";
	public static String CADUCO = "Caduco";
	
	private Integer idEstadoGesJud;
	private String desEstadoGesJud = "";

	
	// constructores
	public EstadoGesJudVO(){
		
	}
	
	public EstadoGesJudVO(Integer idEstadoGesJud, String desEstadoGesJud) {
		super();
		this.idEstadoGesJud = idEstadoGesJud;
		this.desEstadoGesJud = desEstadoGesJud;
	}


	// Getters y Setters
	public Integer getIdEstadoGesJud() {
		return idEstadoGesJud;
	}

	public void setIdEstadoGesJud(Integer id) {
		this.idEstadoGesJud = id;
	}

	public String getDesEstadoGesJud() {
		return desEstadoGesJud;
	}

	public void setDesEstadoGesJud(String desEstadoGesJud) {
		this.desEstadoGesJud = desEstadoGesJud;
	}

}
