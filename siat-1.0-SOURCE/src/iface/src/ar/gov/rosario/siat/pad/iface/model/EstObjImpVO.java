//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del EstObjImp
 * @author tecso
 *
 */
public class EstObjImpVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "estObjImpVO";
	
	private String desEstObjImp="";
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public EstObjImpVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public EstObjImpVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesEstObjImp(desc);
	}
	
	// Getters y Setters

	public String getDesEstObjImp() {
		return desEstObjImp;
	}

	public void setDesEstObjImp(String desEstObjImp) {
		this.desEstObjImp = desEstObjImp;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
