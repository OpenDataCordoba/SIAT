//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

public class TipoDeudaVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "tipoDeudaVO";
	
	private String desTipoDeuda;
	
	// Constructores
	public TipoDeudaVO(){
		super();
	}

	//Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipoDeudaVO(int id, String desc) {
		setId(new Long(id));
		setDesTipoDeuda(desc);
	}

	// Getters y Setters
	public String getDesTipoDeuda() {
		return desTipoDeuda;
	}

	public void setDesTipoDeuda(String desTipoDeuda) {
		this.desTipoDeuda = desTipoDeuda;
	}
	

	
}
