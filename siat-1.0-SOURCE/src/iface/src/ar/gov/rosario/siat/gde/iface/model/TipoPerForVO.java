//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del TipoPerFor
 * @author tecso
 *
 */
public class TipoPerForVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoPerForVO";
	
	private String 	desTipoPerFor;
	private Integer esTitular;
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public TipoPerForVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipoPerForVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesTipoPerFor(desc);
	}

	
	public String getDesTipoPerFor() {
		return desTipoPerFor;
	}

	// Getters y Setters
	public void setDesTipoPerFor(String desTipoPerFor) {
		this.desTipoPerFor = desTipoPerFor;
	}

	public Integer getEsTitular() {
		return esTitular;
	}

	public void setEsTitular(Integer esTitular) {
		this.esTitular = esTitular;
	}
	
	

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
