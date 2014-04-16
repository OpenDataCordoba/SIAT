//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del EstadoConvenio
 * @author tecso
 *
 */
public class EstadoConvenioVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "estadoConvenioVO";
	
	private String desEstadoConvenio;
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public EstadoConvenioVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public EstadoConvenioVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesEstadoConvenio(desc);
	}

	// Getters y Setters
	public String getDesEstadoConvenio() {
		return desEstadoConvenio;
	}

	public void setDesEstadoConvenio(String desEstadoConvenio) {
		this.desEstadoConvenio = desEstadoConvenio;
	}
	
	

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
