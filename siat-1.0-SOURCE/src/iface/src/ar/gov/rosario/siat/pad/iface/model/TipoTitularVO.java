//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

public class TipoTitularVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	
	private String desTipoTitular = "";

	// View Constants

	// Constructores
    public TipoTitularVO(){}

    public TipoTitularVO(Long id, String desTipoTitular){
    	super(id);
    	this.desTipoTitular = desTipoTitular;
    }

	// Getters y Setters    
	public String getDesTipoTitular() {
		return desTipoTitular;
	}
	public void setDesTipoTitular(String desTipoTitular) {
		this.desTipoTitular = desTipoTitular;
	}

	// View getters	
}
