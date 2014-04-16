//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del TipCodEmi
 * @author tecso
 *
 */
public class TipCodEmiVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipCodEmiVO";
	
	private String desTipCodEmi;
	
	// Buss Flags
	
	// View Constants
	
	// Constructores
	public TipCodEmiVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipCodEmiVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesTipCodEmi(desc);
	}

	// Getters y Setters
	public String getDesTipCodEmi() {
		return desTipCodEmi;
	}

	public void setDesTipCodEmi(String desTipCodEmi) {
		this.desTipCodEmi = desTipCodEmi;
	}
	
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
