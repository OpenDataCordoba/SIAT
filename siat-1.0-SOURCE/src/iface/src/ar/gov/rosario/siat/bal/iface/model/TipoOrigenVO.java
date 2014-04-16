//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del TipoOrigen
 * @author tecso
 *
 */
public class TipoOrigenVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoOrigenVO";

	public static final long ID_TIPO_AREA = 1L;
	public static final long ID_TIPO_ASENTAMIENTO = 2L;

	private String desTipoOrigen;
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public TipoOrigenVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipoOrigenVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesTipoOrigen(desc);
	}

	public String getDesTipoOrigen() {
		return desTipoOrigen;
	}

	public void setDesTipoOrigen(String desTipoOrigen) {
		this.desTipoOrigen = desTipoOrigen;
	}
	
	// Getters y Setters
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
