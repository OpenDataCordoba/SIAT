//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

public class EstadoEnvioVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	
	private String desEstado="";
	

	// Constructores 
	public EstadoEnvioVO(){
		super();
	}
	
	public EstadoEnvioVO(int id, String descripcion){
		super();
		this.setId(new Long(id));
		this.setDesEstado(descripcion);
		
	}
	
	// Getters y Setters
	

	public String getDesEstado() {
		return desEstado;
	}

	public void setDesEstado(String desEstado) {
		this.desEstado = desEstado;
	}

	

	//	 Buss flags getters y setters
	
	
}
