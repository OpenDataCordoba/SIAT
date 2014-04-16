//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del TipoDistrib
 * @author tecso
 *
 */
public class TipoDistribVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoDistribVO";
	
	private String desTipoDistrib;
	
	// Buss Flags
	
	
	// View Constants
	
	// Constructores
	public TipoDistribVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipoDistribVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesTipoDistrib(desc);
	}

	//	 Getters y Setters
	public String getDesTipoDistrib() {
		return desTipoDistrib;
	}

	public void setDesTipoDistrib(String desTipoDistrib) {
		this.desTipoDistrib = desTipoDistrib;
	}
	
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
