//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del DesImp
 * @author tecso
 *
 */
public class DesImpVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "desImpVO";
	
	private String desDesImp;
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public DesImpVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public DesImpVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesDesImp(desc);
	}
	
	// Getters y Setters

	public String getDesDesImp() {
		return desDesImp;
	}

	public void setDesDesImp(String desDesImp) {
		this.desDesImp = desDesImp;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
