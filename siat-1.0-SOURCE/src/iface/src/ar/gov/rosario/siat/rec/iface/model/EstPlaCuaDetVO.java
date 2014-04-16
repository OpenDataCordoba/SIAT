//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del EstPlaCuaDet
 * @author tecso
 *
 */
public class EstPlaCuaDetVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "estPlaCuaDetVO";
	
	private String desEstPlaCuaDet;
	
	// Buss Flags
	
	// View Constants
	
	// Constructores
	public EstPlaCuaDetVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public EstPlaCuaDetVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesEstPlaCuaDet(desc);
	}

	public String getDesEstPlaCuaDet() {
		return desEstPlaCuaDet;
	}

	public void setDesEstPlaCuaDet(String desEstPlaCuaDet) {
		this.desEstPlaCuaDet = desEstPlaCuaDet;
	}
	
	// Getters y Setters

	
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
