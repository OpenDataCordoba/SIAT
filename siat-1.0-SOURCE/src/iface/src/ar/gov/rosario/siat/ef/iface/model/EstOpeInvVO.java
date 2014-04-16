//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del EstOpeInv
 * @author tecso
 *
 */
public class EstOpeInvVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "estOpeInvVO";
	
	private String desEstOpeInv;
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public EstOpeInvVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public EstOpeInvVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesEstOpeInv(desc);
	}
	
	// Getters y Setters

	public String getDesEstOpeInv() {
		return desEstOpeInv;
	}

	public void setDesEstOpeInv(String desEstOpeInv) {
		this.desEstOpeInv = desEstOpeInv;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
