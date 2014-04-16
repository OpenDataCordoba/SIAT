//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del EstadoPlanFis
 * @author tecso
 *
 */
public class EstadoPlanFisVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "estadoPlanFisVO";
	
	private String desEstadoPlanFis = "";
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public EstadoPlanFisVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public EstadoPlanFisVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesEstadoPlanFis(desc);
	}
	
	// Getters y Setters
	public String getDesEstadoPlanFis() {
		return desEstadoPlanFis;
	}
	public void setDesEstadoPlanFis(String desEstadoPlanFis) {
		this.desEstadoPlanFis = desEstadoPlanFis;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
