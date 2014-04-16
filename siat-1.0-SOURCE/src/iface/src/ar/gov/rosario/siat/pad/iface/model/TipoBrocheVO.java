//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

public class TipoBrocheVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	private String desTipoBroche;
	
	// Constructores
	public TipoBrocheVO(){
		super();
	}
	
	public TipoBrocheVO(int id, String desTipoBroche) {
		super(id);
		setDesTipoBroche(desTipoBroche);
	}
	
	// Getters y Setters
	public String getDesTipoBroche() {
		return desTipoBroche;
	}
	public void setDesTipoBroche(String desTipoBroche) {
		this.desTipoBroche = desTipoBroche;
	}

}
