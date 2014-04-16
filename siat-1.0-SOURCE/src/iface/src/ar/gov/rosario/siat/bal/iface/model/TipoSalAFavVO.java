//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del TipoSalAFav
 * @author tecso
 *
 */
public class TipoSalAFavVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoSalAFavVO";
	
	private String desTipoSalAFav;
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public TipoSalAFavVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipoSalAFavVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesTipoSalAFav(desc);
	}
	
	// Getters y Setters
	
	public String getDesTipoSalAFav() {
		return desTipoSalAFav;
	}

	public void setDesTipoSalAFav(String desTipoSalAFav) {
		this.desTipoSalAFav = desTipoSalAFav;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
