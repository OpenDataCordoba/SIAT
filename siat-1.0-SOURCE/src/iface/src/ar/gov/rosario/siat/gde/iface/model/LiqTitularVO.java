//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import coop.tecso.demoda.iface.helper.StringUtil;

public class LiqTitularVO {

	private Long   idTitular;
	private String desTitular="";
	
	private String desTitularContr="";

	private boolean existePersona = true;
	
	//  Getters y Setters
	public String getDesTitular() {
		return desTitular;
	}
	public void setDesTitular(String desTitular) {
		this.desTitular = desTitular;
	}
	public Long getIdTitular() {
		return idTitular;
	}
	public void setIdTitular(Long idTitular) {
		this.idTitular = idTitular;
	}
	public String getDesTitularContr() {
		if(!StringUtil.isNullOrEmpty(desTitularContr))
			return desTitularContr;
		else
			return desTitular;
	}
	public void setDesTitularContr(String desTitularContr) {
		this.desTitularContr = desTitularContr;
	}
	
	
	public boolean getExistePersona() {
		return existePersona;
	}
	public void setExistePersona(boolean existePersona) {
		this.existePersona = existePersona;
	}
	
}
