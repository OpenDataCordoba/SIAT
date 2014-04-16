//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del MotivoCierre
 * @author tecso
 *
 */
public class MotivoCierreVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "motivoCierreVO";
	
	private String desMotivo="";
	
	// Buss Flags
	
	
	// View Constants
	

	// Constructores
	public MotivoCierreVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public MotivoCierreVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesMotivo(desc);
	}
	
	// Getters y Setters

	public String getDesMotivo() {
		return desMotivo;
	}

	public void setDesMotivo(String desMotivo) {
		this.desMotivo = desMotivo;
	}


	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
