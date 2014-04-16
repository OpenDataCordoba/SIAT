//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pro.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Representa los tipos de ejecucion. 
 * @author tecso
 *
 */
public class TipoEjecucionVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoEjecucionVO";

	private String desTipoEjecucion;
	
	// Constructores
	public TipoEjecucionVO(){
		super();
	}
	public TipoEjecucionVO(int id, String desTipoEjecucion) {
		super(id);
		setDesTipoEjecucion(desTipoEjecucion);
	}
	// Getters y Setters
	public String getDesTipoEjecucion(){
		return desTipoEjecucion;
	}
	public void setDesTipoEjecucion(String desTipoEjecucion){
		this.desTipoEjecucion = desTipoEjecucion;
	}
}
