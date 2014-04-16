//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

public class LugarEventoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	private String descripcion = "";
	private String domicilio = "";
	private Long factorOcupacional;
	
	private String factorOcupacionalView = "";
	
	//Constructores 
	public LugarEventoVO(){
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public LugarEventoVO(int id, String descripcion) {
		super();
		setId(new Long(id));
		setDescripcion(descripcion);
	}

	// Getters & Setters
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getDomicilio() {
		return domicilio;
	}
	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}
	public Long getFactorOcupacional() {
		return factorOcupacional;
	}
	public void setFactorOcupacional(Long factorOcupacional) {
		this.factorOcupacional = factorOcupacional;
		this.factorOcupacionalView = StringUtil.formatLong(factorOcupacional);
	}
	public String getFactorOcupacionalView() {
		return factorOcupacionalView;
	}
	public void setFactorOcupacionalView(String factorOcupacionalView) {
		this.factorOcupacionalView = factorOcupacionalView;
	}


	
}
