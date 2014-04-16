//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del TipoDeudaPlan
 * @author tecso
 *
 */
public class TipoDeudaPlanVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoDeudaPlanVO";
	
	private String desTipoDeudaPlan;
	
	// Constructores
	public TipoDeudaPlanVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipoDeudaPlanVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesTipoDeudaPlan(desc);
	}

	
	// Getters y Setters
	public String getDesTipoDeudaPlan() {
		return desTipoDeudaPlan;
	}

	public void setDesTipoDeudaPlan(String desTipoDeudaPlan) {
		this.desTipoDeudaPlan = desTipoDeudaPlan;
	}
	
	
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
