//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

public class CriRepVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	private String codCriRep;

	private String desCriRep;
	
	// Constructores
	
	public CriRepVO(){
		super();
	}

	// Getters y Setters
	
	public String getCodCriRep() {
		return codCriRep;
	}
	public void setCodCriRep(String codCriRep) {
		this.codCriRep = codCriRep;
	}
	public String getDesCriRep() {
		return desCriRep;
	}
	public void setDesCriRep(String desCriRep) {
		this.desCriRep = desCriRep;
	}

}
