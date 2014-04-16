//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del TipoSellado
 * @author tecso
 *
 */
public class TipoSelladoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoSelladoVO";
	
	private String desTipoSellado;
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public TipoSelladoVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipoSelladoVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesTipoSellado(desc);
	}

	// Getters y Setters
	
	public String getDesTipoSellado() {
		return desTipoSellado;
	}

	public void setDesTipoSellado(String desTipoSellado) {
		this.desTipoSellado = desTipoSellado;
	}
	

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
