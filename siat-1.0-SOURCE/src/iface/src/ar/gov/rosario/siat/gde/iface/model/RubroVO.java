//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del Rubro
 * @author tecso
 *
 */
public class RubroVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "rubroVO";
	
	private String rubro="";
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public RubroVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public RubroVO(int id, String desc) {
		super();
		setId(new Long(id));
		
	}

	
	// Getters y Setters
	public String getRubro() {
		return rubro;
	}

	public void setRubro(String rubro) {
		this.rubro = rubro;
	}
	
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
