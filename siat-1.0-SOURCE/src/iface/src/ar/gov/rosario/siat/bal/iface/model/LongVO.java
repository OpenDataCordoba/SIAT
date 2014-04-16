//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import coop.tecso.demoda.iface.helper.StringUtil;

public class LongVO {

	private Long value;
	private String  valueView = "";
	private String  descripcion = "";
	
	// Constructor
	public LongVO(){
	}
	
	public LongVO(Long value){
		this.value = value;
		this.valueView = StringUtil.formatLong(value);
	}

	public LongVO(Long value, String descripcion){
		this.value = value;
		this.valueView = StringUtil.formatLong(value);
		this.descripcion = descripcion;
	}
	
	// Getters y Setters
	public Long getValue() {
		return value;
	}
	public void setValue(Long value) {
		this.value = value;
		this.valueView = StringUtil.formatLong(value);
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
