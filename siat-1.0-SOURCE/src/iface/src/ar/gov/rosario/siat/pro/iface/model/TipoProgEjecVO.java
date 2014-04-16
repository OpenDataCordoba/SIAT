//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pro.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Representa los tipos de programas que se pueden utilizar para delegar la ejecucion de un proceso.
 * @author tecso
 *
 */
public class TipoProgEjecVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoProEjecVO";

	private String desTipoProgEjec;
	
	// Constructores
	
	public TipoProgEjecVO(){
		super();
	}
	public TipoProgEjecVO(int id, String desTipoProgEjec){
		super(id);
		setDesTipoProgEjec(desTipoProgEjec);
	}
	// Getters y Setters
	public String getDesTipoProgEjec(){
		return desTipoProgEjec;
	}
	public void setDesTipoProgEjec(String desTipoProgEjec){
		this.desTipoProgEjec = desTipoProgEjec;
	}

}
