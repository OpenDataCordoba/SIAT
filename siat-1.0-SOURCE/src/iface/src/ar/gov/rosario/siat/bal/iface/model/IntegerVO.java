//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import coop.tecso.demoda.iface.helper.StringUtil;

public class IntegerVO {

	private Integer value;
	private String  valueView = "";
	private String  descripcion = "";
	
	// Constructor
	public IntegerVO(){
	}
	
	public IntegerVO(Integer value){
		this.value = value;
		this.valueView = StringUtil.formatInteger(value);
	}

	public IntegerVO(Integer value, String descripcion){
		this.value = value;
		this.valueView = StringUtil.formatInteger(value);
		this.descripcion = descripcion;
	}
	
	// Getters y Setters
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
		this.valueView = StringUtil.formatInteger(value);
	}
	public String getValueView() {
		if(value == -1)
			return descripcion;
		return valueView;
	}
	public void setValueView(String valueView) {
		this.valueView = valueView;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
}
