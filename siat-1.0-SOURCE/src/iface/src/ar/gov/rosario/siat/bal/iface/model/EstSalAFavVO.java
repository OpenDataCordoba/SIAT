//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del EstSalAFav
 * @author tecso
 *
 */
public class EstSalAFavVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "estSalAFavVO";
	
	private String desEstSalAFav;
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public EstSalAFavVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public EstSalAFavVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesEstSalAFav(desc);
	}
	
	// Getters y Setters
	
	public String getDesEstSalAFav() {
		return desEstSalAFav;
	}

	public void setDesEstSalAFav(String desEstSalAFav) {
		this.desEstSalAFav = desEstSalAFav;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
