//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.iface.model;

import coop.tecso.demoda.iface.model.BussImageModel;


/**
 * Bean correspondiente a EstSolicitudVO
 * 
 * @author tecso
 */
public class EstSolicitudVO extends BussImageModel {

	private static final long serialVersionUID = 1L;

	private String descripcion;

	// Constructores
	public EstSolicitudVO(){
		super();
	}

	public EstSolicitudVO(long id, String descripcion){
		super();
		setId(id);
		setDescripcion(descripcion);
	}

	// Getters y setters
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
