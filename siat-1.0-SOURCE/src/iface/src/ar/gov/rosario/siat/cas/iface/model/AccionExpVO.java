//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del AccionExp
 * @author tecso
 *
 */
public class AccionExpVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "accionExpVO";
	
	private String desAccionExp;
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public AccionExpVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public AccionExpVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesAccionExp(desc);
	}

	// Getters y Setters
	public String getDesAccionExp() {
		return desAccionExp;
	}
	public void setDesAccionExp(String desAccionExp) {
		this.desAccionExp = desAccionExp;
	}
	


	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
