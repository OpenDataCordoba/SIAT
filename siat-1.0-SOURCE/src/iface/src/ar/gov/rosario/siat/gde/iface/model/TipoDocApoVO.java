//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del TipoDocApo
 * @author tecso
 *
 */
public class TipoDocApoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoDocApoVO";
	
	private String desTipoDocApo = "";
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public TipoDocApoVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipoDocApoVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesTipoDocApo(desc);
	}

	// Getters y Setters
	public String getDesTipoDocApo() {
		return desTipoDocApo;
	}

	public void setDesTipoDocApo(String desTipoDocApo) {
		this.desTipoDocApo = desTipoDocApo;
	}
	
	

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
