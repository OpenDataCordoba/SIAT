//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del EstTranAfip
 * @author tecso
 *
 */
public class EstTranAfipVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "estTranAfipVO";
	
	private String descripcion;
		
	// Constructores
	public EstTranAfipVO() {
		super();
	}

	// Getters y Setters
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}
	
}
